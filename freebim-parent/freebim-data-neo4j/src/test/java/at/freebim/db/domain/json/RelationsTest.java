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
package at.freebim.db.domain.json;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.rel.HasEntry;
import at.freebim.db.dto.Relations;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/test-config.xml"})
@Transactional
public class RelationsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(RelationsTest.class);
	
	@Test
	public void test() {

		HasEntry rel = new HasEntry();
		Relations rels = new Relations();
		rels.c = "HasEntry";
		rels.relations = new BaseRel<?,?>[3];
		rels.relations[0] = rel;
		rels.relations[1] = rel;
		rels.relations[2] = rel;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(out, rels);
			
			String json = out.toString();
			logger.info(json);
			
			Relations relations = mapper.readValue(json, Relations.class);
			logger.info("count = {}", relations.relations.length);
			
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
			fail("JsonGenerationException");
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
			fail("JsonMappingException");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			fail("IOException");
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				fail("IOException");
			}
		}
	}

}
