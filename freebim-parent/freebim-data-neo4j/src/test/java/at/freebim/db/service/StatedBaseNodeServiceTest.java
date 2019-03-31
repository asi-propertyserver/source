/******************************************************************************
 * Copyright (C) 2009-2019  ASI-Propertyserver
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see {@literal<http://www.gnu.org/licenses/>}.
 *****************************************************************************/
package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.SimpleNamedNode;
import at.freebim.db.domain.base.RoleContributor;
import at.freebim.db.domain.base.State;
import at.freebim.db.domain.base.StatedBaseNode;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/test-config.xml"})
@Transactional
public class StatedBaseNodeServiceTest {

	@Autowired
	private SimpleNamedNodeService simpleNamedNodeService;
	
	@Autowired
	private ContributorService contributorService;
	
	@Autowired
	private FreebimUserService freebimUserService;
	
	@Test
	public void testWithoutContributor() {
		
		// if SimpleNamedNode is not a StatedBaseNode this test wouldn't make sense 
		assertTrue(StatedBaseNode.class.isAssignableFrom(SimpleNamedNode.class));
		
		String name = "TestNode";
		SimpleNamedNode node = new SimpleNamedNode();
		node.setName(name);
		node = this.simpleNamedNodeService.save(node);
		assertNotNull(node);
		assertEquals(node.getName(), name);
		
		try {
			node.setState(State.CHECKED);
			node = this.simpleNamedNodeService.save(node);
			fail("No Contributor, not allowed to set state");
		} catch (BadCredentialsException e) {
			; // required to come here
		}
	}
	
	private void login(RoleContributor role) {
		Contributor c = new Contributor();
		c.setCode("TEST");
		c.setRoles(new RoleContributor[]{role});
		c = this.contributorService.save(c);
		assertNotNull(c);
		assertEquals(c.getCode(), "TEST");
		
		FreebimUser tester = new FreebimUser();
		tester.setUsername("tester");
		tester.setPassword("xyz");
		tester.setContributor(c);
		tester = this.freebimUserService.save(tester);
		assertNotNull(tester);
		assertEquals(tester.getUsername(), "tester");
		
		Authentication auth = new UsernamePasswordAuthenticationToken (tester.getUsername (), tester.getPassword (), tester.getRoles());
		SecurityContextHolder.getContext().setAuthentication(auth);

	}
	
	@Test
	public void testWithContributorNoSetStatus() {
		
		String name = "TestNode";
		SimpleNamedNode node = new SimpleNamedNode();
		node.setName(name);
		node = this.simpleNamedNodeService.save(node);
		
		this.login(RoleContributor.ROLE_DELETE);
		
		try {
			node.setState(State.CHECKED);
			node = this.simpleNamedNodeService.save(node);
			assertNotNull(node);
			assertEquals(node.getName(), name);
			fail("Contributor is not allowed to set state");
		} catch (BadCredentialsException e) {
			// required to come here
		}
		
		assertNotNull(node);
		assertEquals(node.getName(), name);
	}

	
	@Test
	public void testWithContributorSetStatus() {
		
		String name = "TestNode";
		SimpleNamedNode node = new SimpleNamedNode();
		node.setName(name);
		node = this.simpleNamedNodeService.save(node);
		
		this.login(RoleContributor.ROLE_SET_STATUS);
		
		try {
			node.setState(State.CHECKED);
			node = this.simpleNamedNodeService.save(node);
			assertNotNull(node);
			assertEquals(node.getName(), name);
			// required to come here
		} catch (BadCredentialsException e) {
			fail("Contributor is allowed to set state");
		}
		
		assertNotNull(node);
		assertEquals(node.getName(), name);
	}

	@Test
	public void testWithContributorRelease() {
		
		String name = "TestNode";
		SimpleNamedNode node = new SimpleNamedNode();
		node.setName(name);
		node = this.simpleNamedNodeService.save(node);
		
		this.login(RoleContributor.ROLE_SET_RELEASE_STATUS);
		
		try {
			node.setState(State.CHECKED);
			node = this.simpleNamedNodeService.save(node);
			assertNotNull(node);
			assertEquals(node.getName(), name);
			// required to come here
		} catch (BadCredentialsException e) {
			fail("Contributor is allowed to set state");
		}
		
		assertNotNull(node);
		assertEquals(node.getName(), name);
	}

	@Test
	public void testWithContributorRelease2() {
		
		String name = "TestNode";
		SimpleNamedNode node = new SimpleNamedNode();
		node.setName(name);
		node = this.simpleNamedNodeService.save(node);
		
		this.login(RoleContributor.ROLE_SET_RELEASE_STATUS);
		
		try {
			node.setState(State.RELEASED);
			node = this.simpleNamedNodeService.save(node);
			assertNotNull(node);
			assertEquals(node.getName(), name);
			// required to come here
		} catch (BadCredentialsException e) {
			fail("Contributor is allowed to set state");
		}
		
		assertNotNull(node);
		assertEquals(node.getName(), name);
	}

	@Test
	public void testWithContributorNotRelease() {
		
		String name = "TestNode";
		SimpleNamedNode node = new SimpleNamedNode();
		node.setName(name);
		node = this.simpleNamedNodeService.save(node);
		
		this.login(RoleContributor.ROLE_SET_STATUS);
		
		try {
			node.setState(State.RELEASED);
			node = this.simpleNamedNodeService.save(node);
			assertNotNull(node);
			assertEquals(node.getName(), name);
			fail("Contributor is allowed to set state");
		} catch (BadCredentialsException e) {
			// required to come here
		}
		
		assertNotNull(node);
		assertEquals(node.getName(), name);
	}

}
