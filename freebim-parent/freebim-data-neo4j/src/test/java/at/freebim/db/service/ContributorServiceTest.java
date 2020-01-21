package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.base.RoleContributor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class ContributorServiceTest extends AbstractBaseTest {

	@Autowired
	private ContributorService contributorService;

	@Autowired
	private DateService dateService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getByCodeTest() {
		Contributor c = new Contributor();
		c.setCode("ABC");

		c = contributorService.save(c);

		assertNotNull(c.getNodeId());

		Contributor cTemp = contributorService.getByCode("ABC");

		assertNotNull(cTemp);
		assertEquals(c.getNodeId(), cTemp.getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void testTest() {
		RoleContributor[] roleContributor = new RoleContributor[2];
		roleContributor[0] = RoleContributor.ROLE_DELETE;
		roleContributor[1] = RoleContributor.ROLE_LIBRARY_REFERENCES;

		Contributor c = new Contributor();
		c.setCode("ABC");
		c.setRoles(roleContributor);

		c = contributorService.save(c);

		assertTrue(contributorService.test(c, roleContributor));
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllRelevantTest() {
		Contributor contributor = new Contributor();
		contributor.setLastName("TESTNAME");
		contributor.setValidFrom(dateService.getMillis() - 100);

		contributor = contributorService.save(contributor);
		assertNotNull(contributor.getNodeId());

		List<Contributor> cTemp = contributorService.getAllRelevant();

		assertNotNull(cTemp);
		assertEquals(1, cTemp.size());
		assertEquals(contributor.getNodeId(), cTemp.get(0).getNodeId());
	}
}
