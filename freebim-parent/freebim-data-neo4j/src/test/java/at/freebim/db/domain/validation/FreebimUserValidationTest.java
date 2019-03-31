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
package at.freebim.db.domain.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.FreebimUser;
import at.freebim.db.service.FreebimUserService;

/**
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/test-config.xml"})
@Transactional
public class FreebimUserValidationTest {

	@Autowired
    protected FreebimUserService freebimUserService;
	
	@Test
	public void testFreebimUser() {
		FreebimUser u = new FreebimUser();
		u.setUsername("");
		u.setPassword("");
		try {
			this.freebimUserService.save(u);
			assertNotNull(null);
		} catch (Exception e) {
			; // MUST come here
		}
				
		u = new FreebimUser();
		u.setUsername("A");
		u.setPassword("");
		try {
			this.freebimUserService.save(u);
			assertNotNull(null);
		} catch (Exception e) {
			; // MUST come here
		}
		
		u = new FreebimUser();
		u.setUsername("<test>name");
		u.setPassword("");
		try {
			this.freebimUserService.save(u);
			assertNotNull(null);
		} catch (Exception e) {
			; // MUST come here
		}
		
		u = new FreebimUser();
		u.setUsername("test.name");
		u.setPassword("");
		try {
			u = this.freebimUserService.save(u);
			// MUST come here
			assertEquals(u.getUsername(), "test.name");
		} catch (Exception e) {
			assertNotNull(null);
		}
	}
	
}
