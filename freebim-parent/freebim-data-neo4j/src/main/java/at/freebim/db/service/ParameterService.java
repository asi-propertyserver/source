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

import at.freebim.db.domain.Library;
import at.freebim.db.domain.Parameter;

/**
 * The service for the node/class {@link Parameter}.
 * This service extends {@link StatedBaseNodeService} and {@link BsddObjectService}.
 * 
 * @see at.freebim.db.domain.Parameter
 * @see at.freebim.db.service.StatedBaseNodeService
 * @see at.freebim.db.service.BsddObjectService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface ParameterService extends StatedBaseNodeService<Parameter>, BsddObjectService<Parameter> {
	
	/**
	 * Get a {@link List} of {@link Parameter} from the {@link Library} that
	 * has the provided id.
	 * 
	 * @param name the name of the {@link Parameter}
	 * @param libraryId the id of the {@link Library}
	 * @return the {@link List} of {@link Parameter}s that have the provided name
	 */
	public List<Parameter> getByNameFromLibrary(String name, Long libraryId);
}
