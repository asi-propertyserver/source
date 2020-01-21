package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
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
import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.base.Role;
import at.freebim.db.domain.base.RoleContributor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class FreebimUserServiceTest extends AbstractBaseTest {

	@Autowired
	private FreebimUserService freebimUserService;

	@Autowired
	private ContributorService contributorService;

	@Autowired
	private DateService dateService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN", "USERMANAGER" })
	public void getAndRegisterTest() {

		Contributor contributor = new Contributor();
		contributor.setFirstName("testtest");
		contributor.setLastName("testtest");
		contributor.setCode("testtest");
		contributor.setEmail("test@test.test");

		RoleContributor[] roleContributors = new RoleContributor[2];
		roleContributors[0] = RoleContributor.ROLE_DELETE;
		roleContributors[1] = RoleContributor.ROLE_LIBRARY_REFERENCES;
		contributor.setRoles(roleContributors);
		contributor = contributorService.save(contributor);

		Role[] roles = new Role[2];
		roles[0] = Role.ROLE_WEBSERVICE_READ;
		roles[1] = Role.ROLE_CONTRIBUTOR;
		FreebimUser user = freebimUserService.register("testuser", "testpassword", roles, contributor);

		assertEquals(user.getNodeId(), freebimUserService.get("testuser").getNodeId());

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN", "USERMANAGER" })
	public void testTest() {
		Contributor contributor = new Contributor();
		contributor.setFirstName("testtest");
		contributor.setLastName("testtest");
		contributor.setCode("testtest");
		contributor.setEmail("test@test.test");

		RoleContributor[] roleContributors = new RoleContributor[2];
		roleContributors[0] = RoleContributor.ROLE_DELETE;
		roleContributors[1] = RoleContributor.ROLE_LIBRARY_REFERENCES;
		contributor.setRoles(roleContributors);
		contributor = contributorService.save(contributor);

		Role[] roles = new Role[2];
		roles[0] = Role.ROLE_WEBSERVICE_READ;
		roles[1] = Role.ROLE_CONTRIBUTOR;
		FreebimUser user = freebimUserService.register("testuser", "testpassword", roles, contributor);

		assertTrue(freebimUserService.test("testuser", "testpassword", roles));
		assertEquals("testuser", user.getUsername());

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllRelevantTest() {
		// in the setup there will be created a user
		// we will delete it beforehand
		session.query("Match (n:FreebimUser) Detach Delete n;", new HashMap<>());

		Contributor contributor = new Contributor();
		contributor.setLastName("TEST");

		contributor = contributorService.save(contributor);
		assertNotNull(contributor.getNodeId());

		FreebimUser f2 = new FreebimUser();
		f2.setUsername("TEST");
		f2.setPassword("TEST");
		f2.setContributor(contributor);
		f2.setValidFrom(dateService.getMillis() - 100);
		f2 = freebimUserService.save(f2);
		assertNotNull(f2.getNodeId());

		List<FreebimUser> fTemp = freebimUserService.getAllRelevant();

		assertEquals(1, fTemp.size());
		assertEquals(f2.getNodeId(), fTemp.get(0).getNodeId());
	}
}
