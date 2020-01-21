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

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.Unit;
import at.freebim.db.domain.rel.HasMeasure;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.OfUnit;
import at.freebim.db.domain.rel.ParentOf;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class UnitServiceTest extends AbstractBaseTest {

	@Autowired
	private UnitService unitService;

	@Autowired
	private BigBangNodeService bigBangNodeService;

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private ComponentService componentService;

	@Autowired
	private MeasureService measureService;

	@Autowired
	private ParameterService parameterService;

	@Autowired
	private DateService dateService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void findByBsddGuidTest() {
		Unit unit = new Unit();
		unit.setBsddGuid("TESTBSDD");

		unit = unitService.save(unit);
		assertNotNull(unit.getNodeId());

		List<Unit> uTemp = unitService.findByBsddGuid("TESTBSDD");
		assertNotNull(uTemp);
		assertEquals(1, uTemp.size());
		assertEquals(unit.getNodeId(), uTemp.get(0).getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllRelevantTest() {
		BigBangNode bigBangNode = bigBangNodeService.getBigBangNode();

		Library library = new Library();
		library.setName("TEST");
		library.setValidFrom(dateService.getMillis() - 100);

		library = libraryService.save(library);
		assertNotNull(library.getNodeId());

		Component component = new Component();
		component.setName("TESTC");
		component.setValidFrom(dateService.getMillis() - 100);

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		Parameter parameter = new Parameter();
		parameter.setName("TESTP");
		parameter.setValidFrom(dateService.getMillis() - 100);

		parameter = parameterService.save(parameter);
		assertNotNull(parameter.getNodeId());

		Measure measure = new Measure();
		measure.setName("TESTM");
		measure.setValidFrom(dateService.getMillis() - 100);

		measure = measureService.save(measure);
		assertNotNull(measure.getNodeId());

		Unit unit = new Unit();
		unit.setBsddGuid("TESTBSDD");

		unit = unitService.save(unit);
		assertNotNull(unit.getNodeId());

		ParentOf parentOf = new ParentOf();
		parentOf.setN1(bigBangNode);
		parentOf.setN2(library);

		session.save(parentOf);

		ParentOf parentOf1 = new ParentOf();
		parentOf1.setN1(library);
		parentOf1.setN2(component);

		session.save(parentOf1);

		HasParameter hasParameter = new HasParameter();
		hasParameter.setN1(component);
		hasParameter.setN2(parameter);
		hasParameter.setPhaseUuid("TESTUUID");

		session.save(hasParameter);

		HasMeasure hasMeasure = new HasMeasure();
		hasMeasure.setN1(parameter);
		hasMeasure.setN2(measure);

		session.save(hasMeasure);

		OfUnit ofUnit = new OfUnit();
		ofUnit.setN1(measure);
		ofUnit.setN2(unit);

		session.save(ofUnit);

		List<Unit> uTemp = unitService.getAllRelevant();
		assertNotNull(uTemp);
		assertEquals(1, uTemp.size());
		assertEquals(unit.getNodeId(), uTemp.get(0).getNodeId());
	}

	@Test
	public void testStandardization() {
		String r, s;

		s = "CM";
		r = this.unitService.standardize(s);
		assertEquals("cm", r);

		s = "KG/M3";
		r = this.unitService.standardize(s);
		assertEquals("kg/m³", r);

		s = "W/(M2K)";
		r = this.unitService.standardize(s);
		assertEquals("W/m²K", r);

		s = "KN/MM2";
		r = this.unitService.standardize(s);
		assertEquals("kN/mm²", r);

		s = "^-1";
		r = this.unitService.standardize(s);
		assertEquals("⁻¹", r);

		s = "m^3";
		r = this.unitService.standardize(s);
		assertEquals("m³", r);

		s = "cm^6";
		r = this.unitService.standardize(s);
		assertEquals("cm⁶", r);
	}

}
