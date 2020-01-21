package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.rel.References;
import at.freebim.db.domain.rel.Responsible;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class LibraryServiceTest extends AbstractBaseTest {

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private ContributorService contributorService;

	@Autowired
	private ComponentService componentService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getIfcLibraryIdTest() {
		Long id = libraryService.getIfcLibraryId();

		assertNotNull(id);
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getTest() {
		Library library = new Library();
		library.setName("TESTLIBTEST");

		library = libraryService.save(library);

		Library lTemp = libraryService.get("TESTLIBTEST");

		assertEquals(library.getNodeId(), lTemp.getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getForContributorTest() {
		Contributor contributor = new Contributor();
		contributor.setFirstName("TESTFIRSTNAME");
		contributor.setLastName("TESTLASTNAME");
		contributor.setEmail("TEST@TEST.TEST");
		contributor.setCode("TEST");
		contributor = contributorService.save(contributor);

		Library library = new Library();
		library.setName("TESTLIBTEST");

		library = libraryService.save(library);

		Responsible responsible = new Responsible();
		responsible.setN1(contributor);
		responsible.setN2(library);
		session.save(responsible);

		ArrayList<Library> list = libraryService.getForContributor(contributor);

		assertEquals(1, list.size());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getReferencedNodesTest() {
		Library library = new Library();
		library.setName("TESTLIBTEST");

		library = libraryService.save(library);

		Component component = new Component();
		component.setName("TESTCOMPONENT");
		component.setDesc("TETS");
		component.setBsddGuid(java.util.UUID.randomUUID().toString());
		component = componentService.save(component);

		References references = new References();
		references.setRefIdName("TEST");
		references.setN2(library);
		references.setN1(component);
		session.save(references);

		assertEquals(1, libraryService.getReferencedNodes(library.getNodeId(), "TEST").size());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getRefIdNamesTest() {
		Library library = new Library();
		library.setName("TESTLIBTEST");

		library = libraryService.save(library);

		Component component = new Component();
		component.setName("TESTCOMPONENT");
		component.setDesc("TETS");
		component.setBsddGuid(java.util.UUID.randomUUID().toString());
		component = componentService.save(component);

		References references = new References();
		references.setRefIdName("TEST");
		references.setN2(library);
		references.setN1(component);
		session.save(references);

		assertEquals(1, libraryService.getRefIdNames(library.getNodeId()).size());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getReferencedNodesForRefIdTest() {
		Library library = new Library();
		library.setName("TESTLIBTEST");

		library = libraryService.save(library);

		Component component = new Component();
		component.setName("TESTCOMPONENT");
		component.setDesc("TETS");
		component.setBsddGuid(java.util.UUID.randomUUID().toString());
		component = componentService.save(component);

		References references = new References();
		references.setRefIdName("TEST");
		references.setRefId("TESTID");
		references.setN2(library);
		references.setN1(component);
		session.save(references);

		assertEquals(1, libraryService
				.getReferencedNodesForRefId(library.getNodeId(), references.getRefIdName(), references.getRefId())
				.size());
	}
}
