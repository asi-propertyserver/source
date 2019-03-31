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
package at.freebim.db.webservice.dto;

import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.webservice.DtoHelper;

/**
 * Base class representing a single DTO of a node.
 * The class extends {@link UuidIdentifyable}.
 * 
 * @see at.freebim.db.domain.base.UuidIdentifyable
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 * @param <T>
 */
public class Base<T extends UuidIdentifyable> {

	protected T node;
	protected DtoHelper dtoHelper;

	/**
	 * Constructs a new instance.
	 * @param node The original node.
	 * @param dtoHelper The helper.
	 */
	public Base(T node, DtoHelper dtoHelper) {
		this.node = node;
		this.dtoHelper = dtoHelper;
	}
	
	/**
	 * Get the freeBIM-ID of the original node.
	 * @return The freeBIM-ID.
	 */
	public String getFreebimId() {
		return this.dtoHelper.getString(this.node.getUuid());
	}

	/**
	 * Set the freeBIM-ID of the original node.
	 * @param uuid The freeBIM-ID to set.
	 */
	public void setFreebimId(String uuid) {
		this.node.setUuid(uuid);
	}
}
