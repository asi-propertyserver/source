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
import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.rel.ParentOf;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class BaseNodeServiceTest extends AbstractBaseTest {

	@Autowired
	private ComponentService service;

	@Autowired
	private DateService dateService;

	@Autowired
	private BigBangNodeService bigBangNodeService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllTest() {
		Component c1 = new Component();
		c1.setName("TEST");
		c1.setDesc("TESTDESC");
		c1.setCode("ABC");

		Component c2 = new Component();
		c2.setName("TEST");
		c2.setDesc("TESTDESC");
		c2.setCode("ABC");

		c1 = service.save(c1);
		c2 = service.save(c2);

		assertEquals(2, service.getAll(false).size());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getByNodeIdTest() {
		Component c1 = new Component();
		c1.setName("TEST");
		c1.setDesc("TESTDESC");
		c1.setCode("ABC");

		c1 = service.save(c1);

		Component cTemp = service.getByNodeId(c1.getNodeId());

		assertNotNull(cTemp);
		assertEquals(cTemp.getNodeId(), c1.getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void saveTest() {
		Component c1 = new Component();
		c1.setName("TEST");
		c1.setDesc("TESTDESC");
		c1.setCode("ABC");

		c1 = service.save(c1);

		assertNotNull(c1.getNodeId());

		Long tempId = c1.getNodeId();

		c1.setName("trcelasjfö");
		c1 = service.save(c1);

		assertEquals(tempId, c1.getNodeId());
		assertEquals("trcelasjfö", c1.getName());

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllRelevantTest() {
		BigBangNode bigBangNode = bigBangNodeService.getBigBangNode();

		Component c1 = new Component();
		c1.setName("TEST");
		c1.setDesc("TESTDESC");
		c1.setCode("ABC");
		c1.setValidFrom(dateService.getMillis() - 10);

		Component c2 = new Component();
		c2.setName("TEST");
		c2.setDesc("TESTDESC");
		c2.setCode("ABC");
		c2.setValidFrom(dateService.getMillis() - 10);

		c1 = service.save(c1);
		c2 = service.save(c2);

		ParentOf p1 = new ParentOf();
		p1.setN1(bigBangNode);
		p1.setN2(c1);
		session.save(p1);

		ParentOf p2 = new ParentOf();
		p2.setN1(c1);
		p2.setN2(c2);
		session.save(p2);

		assertEquals(1, service.getAllRelevant().size());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllRelevantNodeIds() {
		BigBangNode bigBangNode = bigBangNodeService.getBigBangNode();

		Component c1 = new Component();
		c1.setName("TEST");
		c1.setDesc("TESTDESC");
		c1.setCode("ABC");
		c1.setValidFrom(dateService.getMillis() - 10);

		Component c2 = new Component();
		c2.setName("TEST");
		c2.setDesc("TESTDESC");
		c2.setCode("ABC");

		c1 = service.save(c1);
		c2 = service.save(c2);

		ParentOf p1 = new ParentOf();
		p1.setN1(bigBangNode);
		p1.setN2(c1);
		session.save(p1);

		ParentOf p2 = new ParentOf();
		p2.setN1(c1);
		p2.setN2(c2);
		session.save(p2);

		assertEquals(1, service.getAllRelevantNodeIds().size());
	}
}
