package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.SimpleNamedNode;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class SimpleNamedNodeServiceTest extends AbstractBaseTest {

	@Autowired
	private SimpleNamedNodeService simpleNamedNodeService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void findTest() {
		SimpleNamedNode simpleNamedNode = new SimpleNamedNode();
		simpleNamedNode.setName("TESTNAMETEST");
		simpleNamedNode.setType("TEST1");
		simpleNamedNode = simpleNamedNodeService.save(simpleNamedNode);
		assertNotNull(simpleNamedNode.getNodeId());

		SimpleNamedNode sTemp = simpleNamedNodeService.find("TESTNAMETEST", simpleNamedNode.getType());
		SimpleNamedNode s1Temp = simpleNamedNodeService.find("TESTNAMETEST", null);

		assertNotNull(sTemp);
		assertNotNull(s1Temp);

		assertEquals(simpleNamedNode.getNodeId(), sTemp.getNodeId());
		assertEquals(simpleNamedNode.getNodeId(), s1Temp.getNodeId());

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getTest() {
		SimpleNamedNode simpleNamedNode = new SimpleNamedNode();
		simpleNamedNode.setName("TESTNAMETEST");
		simpleNamedNode.setType("TEST1");
		simpleNamedNode = simpleNamedNodeService.save(simpleNamedNode);
		assertNotNull(simpleNamedNode.getNodeId());

		SimpleNamedNode sTemp = simpleNamedNodeService.get("TESTNAMETEST", simpleNamedNode.getType());
		SimpleNamedNode s1Temp = simpleNamedNodeService.get("TESTNAMETEST", null);

		assertNotNull(sTemp);
		assertNotNull(s1Temp);

		assertEquals(simpleNamedNode.getNodeId(), sTemp.getNodeId());
		assertEquals(simpleNamedNode.getNodeId(), s1Temp.getNodeId());

		SimpleNamedNode simpleNamedNode1 = simpleNamedNodeService.get("ABCDEFG", "TEST123");

		assertNotNull(simpleNamedNode1);
		assertNotNull(simpleNamedNode1.getNodeId());
		assertEquals("ABCDEFG", simpleNamedNode1.getName());
		assertEquals("TEST123", simpleNamedNode1.getType());

	}
}
