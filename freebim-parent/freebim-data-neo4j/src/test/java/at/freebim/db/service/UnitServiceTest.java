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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/test-config.xml"})
@Transactional
public class UnitServiceTest {

	@Autowired
    protected UnitService unitService;
	
	
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
