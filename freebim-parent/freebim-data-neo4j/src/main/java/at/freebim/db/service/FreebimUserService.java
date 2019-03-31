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

import org.springframework.security.access.annotation.Secured;

import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.base.Role;
import at.freebim.db.repository.UsernameExistsException;


/**
 * The service for the node/class {@link FreebimUser}.
 * This service extends {@link LifetimeBaseNodeService}.
 * 
 * @see at.freebim.db.domain.FreebimUser
 * @see at.freebim.db.service.LifetimeBaseNodeService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface FreebimUserService extends LifetimeBaseNodeService<FreebimUser> {

	/**
	 * Get the {@link FreebimUser} that has the provided user name.
	 * 
	 * @param login get user for a user name.
	 * @return the found user.
	 */
	public FreebimUser get(String login);
	
	/**
	 * Registers a new {@link FreebimUser}. Throws an {@link UsernameExistsException} when the 
	 * username already exists in the database. 
	 * 
	 * @param login the username
	 * @param password the password. Can also be already hashed
	 * @param roles the roles the user should have
	 * @param contributor the {@link Contributor} the {@link FreebimUser} should have
	 * @return the persisted and saved {@link FreebimUser}.
	 * @throws UsernameExistsException will be thrown when the username already exists in the database
	 */
	@Secured({"ROLE_USERMANAGER"})
	public FreebimUser register(String login, String password, Role[] roles, Contributor contributor) throws UsernameExistsException;
		
	/**
	 * Test if user may login and has all required permission.
	 * 
	 * @param login User name.
	 * @param password Plain password.
	 * @param roles All passed roles must exist for the specified user.
	 * @return <code>true</code> If, and only if all criteria are fulfilled.
	 */
	public boolean test(String login, String password, Role [] roles);
}