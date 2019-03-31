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
package at.freebim.db.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import at.freebim.db.domain.FreebimUser;



/**
 * Repository for the node/class {@link FreebimUser}.
 * It extends {@link GraphRepository}.
 * 
 * @see at.freebim.db.domain.FreebimUser
 * @see org.springframework.data.neo4j.repository.GraphRepository
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface FreebimUserRepository extends GraphRepository<FreebimUser> {

	/**
	 * Gets the {@link FreebimUser} that has the provided user name.
	 * 
	 * @param login the user name
	 * @return returns the {@link FreebimUser}
	 * */
	public FreebimUser findByUsername(String login);
	
}
