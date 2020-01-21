package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.MessageNode;
import at.freebim.db.domain.base.Role;
import at.freebim.db.domain.rel.MessageClosed;
import at.freebim.db.domain.rel.MessageSeen;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class MessageNodeServiceTest extends AbstractBaseTest {

	@Autowired
	private MessageNodeService messageNodeService;

	@Autowired
	private DateService dateService;

	@Autowired
	private FreebimUserService freebimUserService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getCurrentMessagesTest() {
		MessageNode messageNode = new MessageNode();
		messageNode.setMessage("TEST MESSAGE");
		messageNode.setRoles(new Role[] { Role.ROLE_ADMIN, Role.ROLE_EDIT });
		messageNode.setValidFrom(dateService.getMillis() - 100);
		messageNode.setShowFrom(dateService.getMillis() - 100);
		messageNode.setShowUntil(dateService.getMillis() + 10000);

		messageNode = messageNodeService.save(messageNode);
		assertNotNull(messageNode.getNodeId());

		List<MessageNode> mTemp = messageNodeService.getCurrentMessages();

		assertNotNull(mTemp);
		assertEquals(1, mTemp.size());
		assertEquals(messageNode.getNodeId(), mTemp.get(0).getNodeId());

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void setSeenTest() {
		MessageNode messageNode = new MessageNode();
		messageNode.setMessage("TEST MESSAGE");
		messageNode.setRoles(new Role[] { Role.ROLE_ADMIN });
		messageNode.setValidFrom(dateService.getMillis() - 100);

		messageNode = messageNodeService.save(messageNode);
		assertNotNull(messageNode.getNodeId());

		messageNodeService.setSeen(messageNode.getNodeId());

		MessageNode temp = messageNodeService.getByNodeId(messageNode.getNodeId());
		Iterable<MessageSeen> sTemp = temp.getMessageSeen();
		Iterator<MessageSeen> it = sTemp.iterator();

		FreebimUser freebimUser = freebimUserService.get("admin2");

		while (it.hasNext()) {
			assertEquals(freebimUser.getNodeId(), it.next().getN2().getNodeId());
		}

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void setClosedTest() {
		MessageNode messageNode = new MessageNode();
		messageNode.setMessage("TEST MESSAGE");
		messageNode.setRoles(new Role[] { Role.ROLE_ADMIN });
		messageNode.setValidFrom(dateService.getMillis() - 100);

		messageNode = messageNodeService.save(messageNode);
		assertNotNull(messageNode.getNodeId());

		messageNodeService.setClosed(messageNode.getNodeId());

		MessageNode temp = messageNodeService.getByNodeId(messageNode.getNodeId());
		Iterable<MessageClosed> sTemp = temp.getMessageClosed();
		Iterator<MessageClosed> it = sTemp.iterator();

		FreebimUser freebimUser = freebimUserService.get("admin2");

		while (it.hasNext()) {
			assertEquals(freebimUser.getNodeId(), it.next().getN2().getNodeId());
		}
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllRelevantTest() {
		MessageNode messageNode = new MessageNode();
		messageNode.setMessage("TEST MESSAGE");
		messageNode.setRoles(new Role[] { Role.ROLE_ADMIN });
		messageNode.setValidFrom(dateService.getMillis() - 100);

		messageNode = messageNodeService.save(messageNode);
		assertNotNull(messageNode.getNodeId());

		ArrayList<MessageNode> messageNodes = messageNodeService.getAllRelevant();

		assertNotNull(messageNodes);
		assertEquals(1, messageNodes.size());
		assertEquals(messageNode.getNodeId(), messageNodes.get(0).getNodeId());
	}
}
