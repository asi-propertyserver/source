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

import java.util.List;

import org.springframework.security.access.annotation.Secured;

import at.freebim.db.domain.ValueList;

/**
 * The service for the node/class {@link ValueList}. This service extends
 * {@link StatedBaseNodeService} and {@link BsddObjectService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.ValueList
 * @see at.freebim.db.service.StatedBaseNodeService
 * @see at.freebim.db.service.BsddObjectService
 */
public interface ValueListService extends StatedBaseNodeService<ValueList>, BsddObjectService<ValueList> {

	/**
	 * Gets a {@link List} of {@link ValueList} that have the provided name.
	 *
	 * @param name the name of the {@link ValueList}
	 * @return the {@link List} of {@link ValueList} that have the provided name
	 */
	@Secured({ "ROLE_EDIT", "ROLE_ADMIN" })
	List<ValueList> getByName(String name);

}
