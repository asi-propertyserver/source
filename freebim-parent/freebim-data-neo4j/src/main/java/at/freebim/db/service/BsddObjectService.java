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

import at.freebim.db.domain.base.BsddObject;

/**
 * The service for the node/class <b>T</b> that extent {@link BsddObject}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.BsddObject
 */
public interface BsddObjectService<T extends BsddObject> {

	/**
	 * Find all nodes <b>T</b> that extend {@link BsddObject} that have the provided
	 * bsdd-guid.
	 *
	 * @param bsddGuid the bsdd-guid
	 * @return returns a {@link List} of nodes <b>T</b> that have the corresponding
	 *         bsdd-guid
	 */
	List<T> findByBsddGuid(String bsddGuid);
}
