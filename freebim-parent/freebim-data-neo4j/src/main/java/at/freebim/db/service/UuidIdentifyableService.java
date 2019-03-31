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

import at.freebim.db.domain.base.UuidIdentifyable;

/**
 * The service for a node/class <b>T</b> that extends {@link UuidIdentifyable}.
 * This service extends {@link LifetimeBaseNodeService}.
 * 
 * @see at.freebim.db.domain.base.UuidIdentifyable
 * @see at.freebim.db.service.LifetimeBaseNodeService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface UuidIdentifyableService<T extends UuidIdentifyable> extends LifetimeBaseNodeService<T> {

	/**
	 * Get a {@link UuidIdentifyable} that has the provided uuid.
	 * 
	 * @param uuid the uuid of {@link UuidIdentifyable}
	 * @return the corresponding {@link UuidIdentifyable}
	 */
	public UuidIdentifyable getByUuid(String uuid);
}
