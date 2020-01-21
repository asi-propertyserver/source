package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.NodeIdentifyable;
import at.freebim.db.domain.base.State;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.ParentOf;
import at.freebim.db.domain.rel.Responsible;
import at.freebim.db.dto.Relations;
import at.freebim.db.service.RelationService.UpdateRelationsResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class RelationServiceTest extends AbstractBaseTest {

	@Autowired
	private RelationService relationService;

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private ContributorService contributorService;

	@Autowired
	private ComponentService componentService;

	@Autowired
	private ParameterService parameterService;

	@Autowired
	private DateService dateService;

	@Autowired
	private BigBangNodeService bigBangNodeService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllPathsTest() {
		Library library = new Library();
		library.setName("TEST");
		library.setValidFrom(dateService.getMillis() - 100);

		library = libraryService.save(library);
		assertNotNull(library.getNodeId());

		Component component = new Component();
		component.setName("TESTTEST");
		component.setDesc("TESTDESCTEST");
		component.setValidFrom(dateService.getMillis() - 100);

		component = componentService.save(component);

		BigBangNode bigBangNode = bigBangNodeService.getBigBangNode();

		ParentOf parentOf = new ParentOf();
		parentOf.setN1(library);
		parentOf.setN2(component);
		parentOf.setValidFrom(dateService.getMillis() - 100);

		session.save(parentOf);

		ParentOf parentOf1 = new ParentOf();
		parentOf1.setN1(bigBangNode);
		parentOf1.setN2(library);
		parentOf.setValidFrom(dateService.getMillis() - 100);

		session.save(parentOf1);

		List<RelationService.SimplePathResult> result = relationService.getAllPaths(component.getNodeId(), true,
				(Long) 10L);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertTrue(result.get(0).ids.contains(library.getNodeId()));
		assertTrue(result.get(0).ids.contains(component.getNodeId()));
		assertTrue(result.get(0).ids.contains(bigBangNode.getNodeId()));

		assertTrue(result.get(0).names.contains("TEST"));
		assertTrue(result.get(0).names.contains("TESTTEST"));
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllRelatedInOutTest() {

		Library library = new Library();
		library.setName("TEST");

		library = libraryService.save(library);
		assertNotNull(library.getNodeId());

		Component component = new Component();
		component.setName("TEST");

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		Contributor contributor = new Contributor();
		contributor.setLastName("TESTLAST");

		contributor = contributorService.save(contributor);
		assertNotNull(contributor.getNodeId());

		ParentOf parentOf1 = new ParentOf();
		parentOf1.setN1(library);
		parentOf1.setN2(component);

		session.save(parentOf1);

		Responsible responsible = new Responsible();
		responsible.setN2(library);
		responsible.setN1(contributor);

		session.save(responsible);

		ArrayList<RelationService.RelationResult> rTemp = relationService.getAllRelatedInOut(library.getNodeId());

		assertEquals(3, rTemp.size());

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void updateRelationsTest() {
		Library library = new Library();
		library.setName("TEST");

		library = libraryService.save(library);
		assertNotNull(library.getNodeId());

		Component component = new Component();
		component.setName("TESTCOMPONENT");

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		Component component1 = new Component();
		component1.setName("TEST2");

		component1 = componentService.save(component1);
		assertNotNull(component1.getNodeId());

		ParentOf parentOf = new ParentOf();
		parentOf.setN1(library);
		parentOf.setN2(component);
		parentOf.setN1Id(library.getNodeId());
		parentOf.setN2Id(component.getNodeId());

		session.save(parentOf);

		ParentOf parentOf1 = new ParentOf();
		parentOf1.setN1(library);
		parentOf1.setN2(component1);
		parentOf1.setN1Id(library.getNodeId());
		parentOf1.setN2Id(component1.getNodeId());

		Relations relations = new Relations();
		relations.dir = "OUT";
		relations.t = RelationTypeEnum.PARENT_OF.getCode();
		relations.relations = new BaseRel[] { parentOf1 };

		UpdateRelationsResult<NodeIdentifyable, NodeIdentifyable> updates = relationService
				.updateRelations(library.getNodeId(), new Relations[] { relations }, Library.class);

		assertEquals(3, updates.affectedNodes.size());
		assertEquals(library.getNodeId(), updates.baseNode.getNodeId());

		Library ltemp = libraryService.getByNodeId(library.getNodeId());

		assertEquals(component1.getNodeId(), ltemp.getChilds().iterator().next().getN2().getNodeId());

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void findByStateTest() {
		Component component = new Component();
		component.setState(State.IMPORTED);

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		List<StatedBaseNode> states = relationService.findByState(State.IMPORTED.toString());

		assertNotNull(states);
		assertEquals(1, states.size());
		assertEquals(component.getNodeId(), states.get(0).getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void findByFreebimIdTest() {
		Component component = new Component();
		component.setUuid("TESTUUID");

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		UuidIdentifyable result = relationService.findByFreebimId("TESTUUID");

		assertNotNull(result);
		assertNotNull(result.getNodeId());
		assertEquals(component.getNodeId(), result.getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void findByBsddGuidTest() {
		Component component = new Component();
		component.setBsddGuid("BSDDGUID");

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		List<BaseNode> result = relationService.findByBsddGuid("BSDDGUID");

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(component.getNodeId(), result.get(0).getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void fetchTest() {
		Component component = new Component();
		component.setBsddGuid("BSDDGUID");

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		BaseNode result = relationService.fetch(component, component.getClass());

		assertNotNull(result);
		assertEquals(component.getNodeId(), result.getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getByUuidTest() {
		Component component = new Component();
		component.setUuid("TESTUUID");

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		UuidIdentifyable result = componentService.getByUuid("TESTUUID");

		assertNotNull(result);
		assertEquals(component.getNodeId(), result.getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getNodeByIdTest() {
		Component component = new Component();
		component.setUuid("TESTUUID");

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		BaseNode result = relationService.getNodeById(component.getNodeId(), Component.class);

		assertNotNull(result);
		assertEquals(component.getNodeId(), result.getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getRelatedNodeTest() {
		Library library = new Library();
		library.setName("TEST");

		library = libraryService.save(library);
		assertNotNull(library.getNodeId());

		Component component = new Component();
		component.setName("TESTCOMPONENT");

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		ParentOf parentOf = new ParentOf();
		parentOf.setN1(library);
		parentOf.setN2(component);
		parentOf.setN1Id(library.getNodeId());
		parentOf.setN2Id(component.getNodeId());

		session.save(parentOf);

		Component result = (Component) relationService.getRelatedNode(library, parentOf, Relationship.OUTGOING);

		assertNotNull(result);
		assertEquals(component.getNodeId(), result.getNodeId());

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void saveDefaultForParameterTest() {
		Component component = new Component();
		component.setName("TEST");

		component = componentService.save(component);
		assertNotNull(component);

		Parameter parameter = new Parameter();
		parameter.setName("TESTP");

		parameter = parameterService.save(parameter);
		assertNotNull(parameter.getNodeId());

		HasParameter hasParameter = new HasParameter();
		hasParameter.setN1(component);
		hasParameter.setN2(parameter);

		assertNull(hasParameter.getId());

		hasParameter = relationService.saveDefaultForParameter(hasParameter);
		assertNotNull(hasParameter.getId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void createIfNotExistsTest() {
		Library library = new Library();
		library.setName("TEST");

		library = libraryService.save(library);
		assertNotNull(library.getNodeId());

		Component component = new Component();
		component.setName("TESTCOMPONENT");

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		ParentOf parentOf = new ParentOf();
		parentOf.setN1(library);
		parentOf.setN2(component);
		parentOf.setN1Id(library.getNodeId());
		parentOf.setN2Id(component.getNodeId());

		relationService.createIfNotExists(parentOf, null);

		Library lTemp = session.load(Library.class, library.getNodeId());
		assertNotNull(lTemp.getChilds());
		assertEquals(component.getNodeId(), lTemp.getChilds().iterator().next().getN2().getNodeId());
	}

}
