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
/**
 * 
 */
package at.freebim.db.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.service.NgramService;
import at.freebim.db.service.UnitService;

/**
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/test-config.xml"})
@Transactional
public class NgramTest {

	@Autowired
    protected NgramService ngramService;
	@Autowired
    protected UnitService unitService;
	
	@Autowired
    protected Neo4jOperations template;
	
	@Test
	public void testRegex() {
		this.ngramService.setActive(true);
		int i;
		String s1 = "123,456";
		List<String> strings = this.ngramService.create(s1);
		assertNotNull(strings);
		
		assertEquals(9, strings.size());
		i = 0;
		assertEquals("  1", strings.get(i++));
		assertEquals(" 12", strings.get(i++));
		assertEquals("123", strings.get(i++));
		assertEquals("23.", strings.get(i++));
		assertEquals("3.4", strings.get(i++));
		assertEquals(".45", strings.get(i++));
		assertEquals("456", strings.get(i++));
		assertEquals("56 ", strings.get(i++));
		assertEquals("6  ", strings.get(i++));
		
		s1 = "123.456";
		strings = this.ngramService.create(s1);
		assertNotNull(strings);
		assertEquals(9, strings.size());
		i = 0;
		assertEquals("  1", strings.get(i++));
		assertEquals(" 12", strings.get(i++));
		assertEquals("123", strings.get(i++));
		assertEquals("23.", strings.get(i++));
		assertEquals("3.4", strings.get(i++));
		assertEquals(".45", strings.get(i++));
		assertEquals("456", strings.get(i++));
		assertEquals("56 ", strings.get(i++));
		assertEquals("6  ", strings.get(i++));
		
		s1 = "Test.";
		strings = this.ngramService.create(s1);
		assertNotNull(strings);
		assertEquals(6, strings.size());
		i = 0;
		assertEquals("  t", strings.get(i++));
		assertEquals(" te", strings.get(i++));
		assertEquals("tes", strings.get(i++));
		assertEquals("est", strings.get(i++));
		assertEquals("st ", strings.get(i++));
		assertEquals("t  ", strings.get(i++));
		
		s1 = "Te\tst\nnewline";
		strings = this.ngramService.create(s1);
		assertNotNull(strings);
		assertEquals(13, strings.size());
		i = 0;
		assertEquals("  t", strings.get(i++));
		assertEquals(" te", strings.get(i++));
		assertEquals("tes", strings.get(i++));
		assertEquals("est", strings.get(i++));
		assertEquals("stn", strings.get(i++));
		assertEquals("tne", strings.get(i++));
		assertEquals("new", strings.get(i++));
		assertEquals("ewl", strings.get(i++));
		assertEquals("wli", strings.get(i++));
		assertEquals("lin", strings.get(i++));
		assertEquals("ine", strings.get(i++));
		assertEquals("ne ", strings.get(i++));
		assertEquals("e  ", strings.get(i++));

	}
}
