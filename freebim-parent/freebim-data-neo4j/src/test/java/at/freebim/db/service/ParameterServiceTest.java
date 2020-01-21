package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.ParentOf;
import at.freebim.db.domain.rel.References;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class ParameterServiceTest extends AbstractBaseTest {

	@Autowired
	private ParameterService parameterService;

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private DateService dateService;

	@Autowired
	private ComponentService componentService;

	@Autowired
	private BigBangNodeService bigBangNodeService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getByNameFromLibraryTest() {
		Parameter parameter = new Parameter();
		parameter.setName("TESTTEST");
		parameter.setCode("TESTTEST");
		parameter.setDesc("TESTTEST");

		parameter = parameterService.save(parameter);

		Library library = new Library();
		library.setName("TESTLIBRARY");
		library.setDesc("TESTTEST");

		library = libraryService.save(library);

		References references = new References();
		references.setRefId("IDID");
		references.setRefIdName("TESTIDIDI");
		references.setN1(parameter);
		references.setN2(library);
		session.save(references);

		List<Parameter> result = parameterService.getByNameFromLibrary("TESTTEST", library.getNodeId());

		assertEquals(1, result.size());
		assertEquals("TESTTEST", result.get(0).getName());

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void findByBsddGuidTest() {

		Parameter parameter = new Parameter();
		parameter.setName("TESTTEST");
		parameter.setCode("TESTTEST");
		parameter.setDesc("TESTTEST");
		parameter.setBsddGuid("TESTBSDD");

		parameter = parameterService.save(parameter);
		assertNotNull(parameter.getNodeId());

		List<Parameter> pTemp = parameterService.findByBsddGuid("TESTBSDD");

		assertNotNull(pTemp);
		assertEquals(1, pTemp.size());
		assertEquals(parameter.getNodeId(), pTemp.get(0).getNodeId());

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllRelevant() {
		BigBangNode bigBangNode = bigBangNodeService.getBigBangNode();
		assertNotNull(bigBangNode.getNodeId());

		Component component = new Component();
		component.setName("TEST1");
		component.setValidFrom(dateService.getMillis() - 100);

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		Component component1 = new Component();
		component1.setName("TEST1");
		component1.setValidFrom(dateService.getMillis() - 100);

		component1 = componentService.save(component1);
		assertNotNull(component1.getNodeId());

		ParentOf parentOf = new ParentOf();
		parentOf.setN1(bigBangNode);
		parentOf.setN2(component);

		session.save(parentOf);

		ParentOf parentOf1 = new ParentOf();
		parentOf1.setN1(component);
		parentOf1.setN2(component1);

		session.save(parentOf1);

		Parameter parameter = new Parameter();
		parameter.setName("TESTTEST");
		parameter.setCode("TESTTEST");
		parameter.setDesc("TESTTEST");
		parameter.setBsddGuid("TESTBSDD");
		parameter.setValidFrom(dateService.getMillis() - 100);

		parameter = parameterService.save(parameter);
		assertNotNull(parameter.getNodeId());

		HasParameter hasParameter = new HasParameter();
		hasParameter.setN1(component1);
		hasParameter.setN2(parameter);

		session.save(hasParameter);

		List<Parameter> pTemp = parameterService.getAllRelevant();
		assertNotNull(pTemp);
		assertEquals(1, pTemp.size());
		assertEquals(parameter.getNodeId(), pTemp.get(0).getNodeId());
	}
}
