/**
 * Copyright (C) 2009-2019  ASI-Propertyserver
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see {@literal<http://www.gnu.org/licenses/>}.
 */
/**
 *
 */
package at.freebim.db.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.base.Role;
import at.freebim.db.service.AbstractBaseTest;
import at.freebim.db.service.ContributorService;
import at.freebim.db.service.FreebimUserService;

/**
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Neo4jTestConfiguration.class })
@Transactional
public class DomainTest extends AbstractBaseTest {

	@Autowired
	private FreebimUserService freebimUserService;
	@Autowired
	private ContributorService contributorService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void testFreebimUser() {
		FreebimUser rb = new FreebimUser();
		rb.setUsername("rbreuss");
		rb.setPassword("1234");
		rb.setRoles(new Role[] { Role.ROLE_USERMANAGER, Role.ROLE_CONTRIBUTOR });
		FreebimUser user = freebimUserService.save(rb);
		assertEquals("user username", "rbreuss", user.getUsername());
		assertTrue("user password", this.passwordEncoder.matches("1234", user.getPassword()));

		FreebimUser hans = this.freebimUserService.get("hans");
		assertNull(hans);
		FreebimUser rbFound = this.freebimUserService.get("rbreuss");
		assertNotNull(rbFound);
		assertEquals("user username", "rbreuss", rbFound.getUsername());
		assertTrue("user password", this.passwordEncoder.matches("1234", rbFound.getPassword()));

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void testGetContributor() {
		Contributor c = new Contributor();
		c.setCode("RB");
		c = this.contributorService.save(c);
		assertNotNull(c);
		assertEquals("code", "RB", c.getCode());
		Contributor res = this.contributorService.getByCode("RB");
		assertEquals("code", "RB", res.getCode());

	}
}
