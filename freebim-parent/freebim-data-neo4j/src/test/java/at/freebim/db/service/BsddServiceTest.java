package at.freebim.db.service;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.Component;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class BsddServiceTest extends AbstractBaseTest {

	@Autowired
	private ComponentService componentService;

	@Autowired
	private BsddService bsddService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void didBsddChangeTest() {
		Component obj1 = new Component();
		obj1.setCode("DidBsddChangeTest");
		obj1.setDesc("Test description");
		obj1.setName("DidBsddChangeTest");

		componentService.save(obj1);

		assertFalse(bsddService.didBsddGuidChange(obj1));

	}
}
