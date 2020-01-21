package at.freebim.db.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class GraphServiceTest extends AbstractBaseTest {

	@Autowired
	private GraphService graphService;

	@Autowired
	private BigBangNodeService bigBangNodeService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getGraphForTest() {
		Long id = bigBangNodeService.getBigBangNode().getNodeId();

		GraphService.Graph graph = graphService.getGraphFor(id, true, true, true);

		assertNotNull(graph);
	}
}
