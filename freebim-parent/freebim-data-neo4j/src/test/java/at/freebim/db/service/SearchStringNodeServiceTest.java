package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.SearchStringNode;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class SearchStringNodeServiceTest extends AbstractBaseTest {

	@Autowired
	private SearchStringNodeService searchStringNodeService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void saveTest() {
		SearchStringNode searchStringNode = new SearchStringNode();
		searchStringNode.setSearchString("TESTTEST");

		assertNull(searchStringNode.getNodeId());
		searchStringNode = searchStringNodeService.save(searchStringNode);

		assertNotNull(searchStringNode.getNodeId());
		assertEquals("TESTTEST", searchStringNode.getName());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void deleteTest() {
		SearchStringNode searchStringNode = new SearchStringNode();
		searchStringNode.setSearchString("TESTTEST");

		assertNull(searchStringNode.getNodeId());
		searchStringNode = searchStringNodeService.save(searchStringNode);

		assertNotNull(searchStringNode.getNodeId());
		assertEquals("TESTTEST", searchStringNode.getName());

		searchStringNodeService.delete(searchStringNode);

		searchStringNode = session.load(SearchStringNode.class, searchStringNode.getNodeId());
		assertNull(searchStringNode);

	}
}
