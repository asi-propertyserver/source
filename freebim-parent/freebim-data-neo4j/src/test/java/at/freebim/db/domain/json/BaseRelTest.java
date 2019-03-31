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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.Component;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.rel.ComponentComponent;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.OfMaterial;
import at.freebim.db.service.ComponentService;
import at.freebim.db.service.ParameterService;
import at.freebim.db.service.RelationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/test-config.xml"})
@Transactional
public class BaseRelTest {
	
	private static final Logger logger = LoggerFactory.getLogger(BaseRelTest.class);

	@Autowired
	private ComponentService componentService;
	
	@Autowired
	private ParameterService parameterService;
	
	@Autowired
	private RelationService relationService;
	
	@Test
	public void test() {

		Component comp = new Component();
		comp.setName("TestComponent");
		comp = this.componentService.save(comp);
		Long nodeId = comp.getNodeId();
		
		Parameter p = new Parameter();
		p.setName("TestParameter");
		p = this.parameterService.save(p);
		
		HasParameter rel = new HasParameter();
		rel.setN1(comp);
		rel.setN2(p);
		rel.setOrdering(1);
		rel = (HasParameter) this.relationService.save(rel);

		Component mat = new Component();
		mat.setName("TestMaterial");
		mat = this.componentService.save(mat);
		OfMaterial matRel = new OfMaterial();
		matRel.setN1(comp);
		matRel.setN2(mat);
		matRel = (OfMaterial) this.relationService.save(matRel);
		
		Component part = new Component();
		part.setName("TestPart");
		part = this.componentService.save(part);
		ComponentComponent partsRel = new ComponentComponent();
		partsRel.setN1(comp);
		partsRel.setN2(part);
		partsRel = (ComponentComponent) this.relationService.save(partsRel);
		

		comp = this.componentService.getByNodeId(nodeId);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(out, comp);
			logger.info(out.toString());
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
