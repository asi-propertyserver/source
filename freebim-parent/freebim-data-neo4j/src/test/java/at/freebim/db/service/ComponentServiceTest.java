package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.rel.Equals;
import at.freebim.db.domain.rel.ParentOf;
import at.freebim.db.domain.rel.References;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class ComponentServiceTest extends AbstractBaseTest {

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private ComponentService componentService;

	@Autowired
	private DateService dateService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void findByBsddGuidTest() {
		Component component = new Component();
		component.setBsddGuid("TESTTEST");

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		List<Component> cTemp = componentService.findByBsddGuid("TESTTEST");
		assertEquals(1, cTemp.size());
		assertEquals(component.getNodeId(), cTemp.get(0).getNodeId());

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getEqualFromLibraryTest() {
		// Create library
		Library library = new Library();
		library.setName("TestComponentLibrary");
		library.setDesc("TESTTESTTEST");
		library.setLanguageCode("AT");
		library.setTs(dateService.getMillis());
		library.setValidFrom(dateService.getMillis());
		library = libraryService.save(library);

		assertNotNull(library.getNodeId());

		// Create references component
		Component component1 = new Component();
		component1.setName("TestComponentComponentTest");
		component1.setValidFrom(dateService.getMillis());
		component1 = componentService.save(component1);

		Component component2 = new Component();
		component2.setName("TestComponentComponentTest");
		component2.setValidFrom(dateService.getMillis());
		component2 = componentService.save(component2);

		// references
		References ref = new References();
		ref.setValidFrom(dateService.getMillis());
		ref.setN1(component1);
		ref.setN2(library);
		ref.setRefIdName("TestReference");
		session.save(ref);

		// equals
		Equals equals = new Equals();
		equals.setValid(true);
		equals.setValidFrom(dateService.getMillis());
		equals.setN1(component1);
		equals.setN2(component2);
		session.save(equals);

		Assert.assertNotNull(componentService.getEqualFromLibrary(component2.getNodeId(), library.getNodeId()));

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getNodesFromHierarchyTest() {
		Component component = new Component();
		component.setName("TEST1");

		Component component1 = new Component();
		component1.setName("TEST2");

		component = componentService.save(component);
		component1 = componentService.save(component1);

		assertNotNull(component.getNodeId());
		assertNotNull(component1.getNodeId());

		ParentOf parentOf = new ParentOf();
		parentOf.setN1(component);
		parentOf.setN2(component1);

		session.save(parentOf);

		List<Component> cTemp = componentService.getNodesFromHierarchy("TEST1");

		assertEquals(2, cTemp.size());
		assertEquals(component.getNodeId(), cTemp.get(0).getNodeId());
		assertEquals(component1.getNodeId(), cTemp.get(1).getNodeId());
		// assertEquals();
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getByNameFromLibraryTest() {
		Library library = new Library();
		library.setName("TESTLIB");

		library = libraryService.save(library);

		Component component = new Component();
		component.setName("TESTCOMP");

		component = componentService.save(component);

		assertNotNull(library.getNodeId());
		assertNotNull(component.getNodeId());

		References references = new References();
		references.setN2(library);
		references.setN1(component);

		session.save(references);

		List<Component> cTemp = componentService.getByNameFromLibrary("TESTCOMP", library.getNodeId());

		assertNotNull(cTemp);
		assertEquals(1, cTemp.size());
		assertEquals(component.getNodeId(), cTemp.get(0).getNodeId());
	}
}
