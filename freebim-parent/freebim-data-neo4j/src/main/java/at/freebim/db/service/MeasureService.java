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

import at.freebim.db.domain.DataType;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Unit;
import at.freebim.db.domain.ValueList;

/**
 * The service for the node/class {@link Measure}. This service extends
 * {@link ContributedBaseNodeService} and {@link BsddObjectService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Measure
 * @see at.freebim.db.service.ContributedBaseNodeService
 * @see at.freebim.db.service.BsddObjectService
 */
public interface MeasureService extends ContributedBaseNodeService<Measure>, BsddObjectService<Measure> {

	/**
	 * Get existing (or create a new) Measure that suits all passed parameters.
	 *
	 * @param dataType
	 * @param unit
	 * @param valueList
	 * @param prefix
	 * @param existingMeasures List of Measure objects to find potential existing
	 *                         measures.
	 * @param library          Library an eventually new created Measure should
	 *                         reference.
	 * @return the created {@link Measure} object/node
	 */
	@Secured({ "ROLE_EDIT", "ROLE_ADMIN" })
	Measure getMeasureFor(final DataType dataType, final Unit unit, final ValueList valueList, String prefix,
			final List<Measure> existingMeasures, final Library library);

	/**
	 * Get all {@link Measure}s that have the provided name.
	 *
	 * @param name the name of the {@link Measure}
	 * @return a {@link List} of all the {@link Measure}s that have the provided
	 *         name
	 */
	@Secured({ "ROLE_EDIT", "ROLE_ADMIN" })
	List<Measure> getByName(String name);
}
