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
package at.freebim.db.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.base.LifetimeBaseNode;
import at.freebim.db.service.DataTypeService;
import at.freebim.db.service.DateService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/test-config.xml"})
@Transactional
public class LifetimeBaseNodeTest {

	@Autowired
	private DataTypeService dataTypeService;
	
	@Autowired
	private DateService dateService;
	
	@Test
	public void test() {
		assertTrue(LifetimeBaseNode.class.isAssignableFrom(DataType.class));
		
		// create a new DataType
		DataType dt = new DataType();
		dt.setName("TestName");
		
		// save it
		dt = this.dataTypeService.save(dt);
		
		// is it saved?
		assertNotNull(dt);
		Long nodeId = dt.getNodeId();
		assertNotNull(nodeId);
		assertEquals("TestName", dt.getName());
		
		// did LifetimeBaseNodeServiceImpl work?
		Long validFrom = dt.getValidFrom();
		Long validTo = dt.getValidTo();
		assertNotNull(validFrom);
		assertNull(validTo);
		
		// is relevant information correct?
		Long now = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
		assertTrue(validFrom <= now);
		
		// modify the node
		dt.setDesc("TestDescription");
		dt = this.dataTypeService.save(dt);
		
		// still ok?
		assertNotNull(dt);
		assertEquals("TestName", dt.getName());
		assertEquals("TestDescription", dt.getDesc());
		
		// has relevant information changed?
		assertEquals(nodeId, dt.getNodeId());
		assertEquals(validFrom, dt.getValidFrom());
		assertEquals(validTo, dt.getValidTo());

		// retrieve the previously saved node
		dt = this.dataTypeService.getByNodeId(nodeId);
		
		// still ok?
		assertNotNull(dt);
		assertEquals(nodeId, dt.getNodeId());
		assertEquals(validFrom, dt.getValidFrom());
		assertEquals(validTo, dt.getValidTo());

		// delete the previously saved node
		this.dataTypeService.delete(dt);
		
		// retrieve the deleted node again
		dt = this.dataTypeService.getByNodeId(nodeId);
		
		// really deleted?
		assertTrue(dt.getValidTo() != null);
		assertTrue(dt.getValidTo() <= this.dateService.getMillis());
	}

}
