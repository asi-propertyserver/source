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
import at.freebim.db.domain.BsddNode;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.rel.Bsdd;
import at.freebim.db.domain.rel.Equals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class BsddNodeServiceTest extends AbstractBaseTest {

	@Autowired
	private BsddNodeService bsddNodeService;

	@Autowired
	private ComponentService componentService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getByBsddGuidTest() {
		String bsdd = java.util.UUID.randomUUID().toString();

		BsddNode component = new BsddNode();
		component.setGuid(bsdd);

		component = bsddNodeService.save(component);

		BsddNode cTemp = bsddNodeService.getByGuid(bsdd);

		assertNotNull(cTemp);
		assertEquals(bsdd, cTemp.getGuid());

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void spreadToEqualNodesTest() {
		String bsdd1 = java.util.UUID.randomUUID().toString();
		String bsdd2 = java.util.UUID.randomUUID().toString();
		String bsdd3 = java.util.UUID.randomUUID().toString();

		BsddNode component = new BsddNode();
		component.setGuid(bsdd1);

		Component component1 = new Component();
		component1.setBsddGuid(bsdd2);

		Component comonent2 = new Component();
		comonent2.setBsddGuid(bsdd3);

		component = bsddNodeService.save(component);
		component1 = componentService.save(component1);
		comonent2 = componentService.save(comonent2);

		Bsdd bsdd = new Bsdd();
		bsdd.setN1(component);
		bsdd.setN2(component1);
		session.save(bsdd);

		Equals equals = new Equals();
		equals.setQ(2.0);
		equals.setN1(component1);
		equals.setN2(comonent2);
		session.save(equals);

		assertEquals(1, bsddNodeService.spreadToEqualNodes().size());

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void setBsddFieldToEqualNodes() {
		String bsdd2 = java.util.UUID.randomUUID().toString();
		String bsdd3 = java.util.UUID.randomUUID().toString();

		Component component1 = new Component();
		component1.setBsddGuid(bsdd2);

		Component comonent2 = new Component();
		comonent2.setBsddGuid(bsdd3);

		component1 = componentService.save(component1);
		comonent2 = componentService.save(comonent2);

		Equals equals = new Equals();
		equals.setQ(2.0);
		equals.setN1(component1);
		equals.setN2(comonent2);
		session.save(equals);

		assertEquals((Long) 1L, bsddNodeService.setBsddFieldToEqualNodes());
	}
}
