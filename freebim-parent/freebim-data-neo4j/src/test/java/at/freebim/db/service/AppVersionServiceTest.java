package at.freebim.db.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.rel.ChildOf;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class AppVersionServiceTest extends AbstractBaseTest {

	@Autowired
	private AppVersionService appVersionService;

	@Autowired
	private ComponentService componentService;

	@Autowired
	private DateService dateService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void createParentOfRelationsTest() {
		// create child of relation
		Component c1 = new Component();
		c1.setCode("ABC");
		c1.setName("TEST");
		c1.setDesc("TEST2");
		c1.setM(false);
		c1.setValidFrom(dateService.getMillis());
		c1 = componentService.save(c1);

		Component c2 = new Component();
		c2.setCode("ABC");
		c2.setName("TEST");
		c2.setDesc("TEST2");
		c2.setM(false);
		c2.setValidFrom(dateService.getMillis());
		c2 = componentService.save(c2);

		ChildOf childOf = new ChildOf();
		childOf.setN1(c1);
		childOf.setN2(c2);
		session.save(childOf);

		Long created = appVersionService.createParentOfRelations();

		assertEquals((Long) 1L, created);
	}
}
