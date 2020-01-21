package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.NgramNode;
import at.freebim.db.domain.base.Coded;
import at.freebim.db.domain.base.Named;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class NgramServiceTest extends AbstractBaseTest {

	@Autowired
	private NgramService ngramService;

	@Autowired
	private ComponentService componentService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void isActiveTest() {
		ngramService.setActive(true);
		assertTrue(ngramService.isActive());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void setActiveTest() {
		ngramService.setActive(true);
		assertTrue(ngramService.isActive());

		ngramService.setActive(false);
		assertFalse(ngramService.isActive());

		ngramService.setActive(true);
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void forStringTest() {
		String ngram = "TEST";

		Set<NgramNode> nodes = ngramService.forString(ngram);

		Iterator<NgramNode> iteratorNodes = nodes.iterator();

		assertEquals(6, nodes.size());
		assertEquals(3, iteratorNodes.next().getNg().length());
		assertEquals(3, iteratorNodes.next().getNg().length());
		assertEquals(3, iteratorNodes.next().getNg().length());
		assertEquals(3, iteratorNodes.next().getNg().length());
		assertEquals(3, iteratorNodes.next().getNg().length());
		assertEquals(3, iteratorNodes.next().getNg().length());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void createForNodeTest() {
		Component component = new Component();
		component.setName("TESTCOMPONENT");
		component.setDesc("DESCRIPTION");
		component.setCode("CODE");

		component = componentService.save(component);

		ngramService.createForNode(component.getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void deleteForTest() {
		Component component = new Component();
		component.setName("TEST");
		component.setDesc("TESTDESCRIPTION");
		component.setCode("ABCDED");

		component = componentService.save(component);

		assertNotNull(component.getNodeId());
		ngramService.createForNode(component.getNodeId());

		assertTrue(ngramService.deleteFor(component, Coded.class) > 0);

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void findTest() {
		Component component = new Component();
		component.setName("TEST");
		component.setDesc("TESTDESCRIPTION");
		component.setCode("ABCDED");

		component = componentService.save(component);

		assertNotNull(component.getNodeId());
		ngramService.createForNode(component.getNodeId());

		List<NgramService.MatchResult> result = ngramService.find("TEST", Named.class, Component.class);

		assertEquals(1, result.size());
	}
}
