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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.rel.HasEntry;
import at.freebim.db.service.RelationService;
import at.freebim.db.service.ValueListEntryService;
import at.freebim.db.service.ValueListService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/test-config.xml"})
@Transactional
public class FetchRelatedTest {
	
	private static final Logger logger = LoggerFactory.getLogger(FetchRelatedTest.class);
	
	
	@Autowired
	private ValueListEntryService valueListEntryService;
	@Autowired
	private ValueListService valueListService;
	@Autowired
	private RelationService relationService;
	
	@Test
	public void test() {

		ValueListEntry e = new ValueListEntry();
		e.setName("test");
		e = this.valueListEntryService.save(e);
		assertNotNull(e);
		
		ValueList vl = new ValueList();
		vl.setName("test-list");
		vl = this.valueListService.save(vl);
		assertNotNull(vl);
		
		HasEntry rel = new HasEntry();
		rel.setN1(vl);
		rel.setN2(e);
		rel.setOrdering(17);
		rel = (HasEntry) this.relationService.save(rel);
		assertNotNull(rel);
		
		ArrayList<ValueListEntry> entries = this.valueListEntryService.getAll(false);
		assertNotNull(entries);
		
		assertTrue(entries.size() > 0);
		
		for (ValueListEntry entry : entries) {
			assertNotNull(entry);
			
			logger.info("Entry: {}", entry.getName());
			
			Iterable<HasEntry> iterable = entry.getLists();
			if (iterable != null) {
				Iterator<HasEntry> iter = iterable.iterator();
				if (iter != null) {
					if (iter.hasNext()) {
						HasEntry he = iter.next();
						if (he != null) {
							he.getId();
						}
					}
				}
			}
		}
	}

}
