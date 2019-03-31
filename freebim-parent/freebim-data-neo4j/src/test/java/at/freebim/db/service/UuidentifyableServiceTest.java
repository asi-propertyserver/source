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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.SimpleNamedNode;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.domain.base.UuidIdentifyable;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/test-config.xml"})
@Transactional
public class UuidentifyableServiceTest {

	@Autowired
	private SimpleNamedNodeService simpleNamedNodeService;

	@Test
	public void test1() {
		
		// if SimpleNamedNode is not a UuidIdentifyable this test wouldn't make sense 
		assertTrue(UuidIdentifyable.class.isAssignableFrom(StatedBaseNode.class));
		
		SimpleNamedNode a = new SimpleNamedNode();
		a.setName("NodeA");
		a = this.simpleNamedNodeService.save(a);
		assertNotNull(a);
		assertNotNull(a.getUuid());
		assertEquals(a.getName(), "NodeA");
		
		SimpleNamedNode b = new SimpleNamedNode();
		b.setName("NodeB");
		// set UUID of 2nd node to UUID of 1st node
		b.setUuid(a.getUuid());
		b = this.simpleNamedNodeService.save(b);
		assertNotNull(b);
		assertEquals(b.getName(), "NodeB");
		
		// same node, since UUID is indexed with 'unique' attribute
		assertTrue(a.getNodeId().equals(b.getNodeId()));
		assertTrue(a.getUuid().equals(b.getUuid()));

	}
	
	@Test
	public void test2() {
		
		// if SimpleNamedNode is not a UuidIdentifyable this test wouldn't make sense 
		assertTrue(UuidIdentifyable.class.isAssignableFrom(StatedBaseNode.class));
		
		SimpleNamedNode a = new SimpleNamedNode();
		a.setName("NodeA");
		a = this.simpleNamedNodeService.save(a);
		assertNotNull(a);
		assertNotNull(a.getUuid());
		assertEquals(a.getName(), "NodeA");
		
		SimpleNamedNode b = new SimpleNamedNode();
		b.setName("NodeB");
		b = this.simpleNamedNodeService.save(b);
		assertNotNull(b);
		assertNotNull(b.getUuid());
		assertEquals(b.getName(), "NodeB");
		
		// 2 different nodes
		assertTrue(!a.getNodeId().equals(b.getNodeId()));
		assertTrue(!a.getUuid().equals(b.getUuid()));

		b.setUuid(a.getUuid());

		try {
			b = this.simpleNamedNodeService.save(b);
			fail("not allowed to have same UUID");
		} catch (DataIntegrityViolationException e) {
			; // required to come here
		}
	}


}
