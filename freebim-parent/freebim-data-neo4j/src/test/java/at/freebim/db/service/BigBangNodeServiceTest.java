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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class BigBangNodeServiceTest {

	@Autowired
	private BigBangNodeService bigBangNodeService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getBigBangNodeTest() {
		BigBangNode bigBangNode = this.bigBangNodeService.getBigBangNode();

		assertNotNull(bigBangNode);
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllRelevantTest() {
		BigBangNode bigBangNode = this.bigBangNodeService.getBigBangNode();

		List<BigBangNode> bTemp = this.bigBangNodeService.getAllRelevant();

		assertNotNull(bTemp);
		assertEquals(1, bTemp.size());
		assertEquals(bigBangNode.getNodeId(), bTemp.get(0).getNodeId());
	}
}
