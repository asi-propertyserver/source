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
package at.freebim.db.webservice.dto.rel;

import at.freebim.db.webservice.DtoHelper;
import at.freebim.db.webservice.dto.Component;
import at.freebim.db.webservice.dto.Measure;
import at.freebim.db.webservice.dto.ValueList;

/**
 * DTO for a {@link Measure} to {@link ValueList} relation.
 * These relations might carry a {@link Component} information.
 * The class extends {@link Rel}.
 * 
 * @see at.freebim.db.webservice.dto.Measure
 * @see at.freebim.db.webservice.dto.ValueList
 * @see at.freebim.db.webservice.dto.rel.Rel
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class ValueListRel extends Rel {

	/**
	 * The freeBIM-ID of a {@link Component}.
	 */
	private String component;

	/**
	 * Constructs a new instance.
	 * @param uuid The freeBIM-ID of the related object.
	 * @param componentUuid The optional freeBIM-ID of the {@link Component}.
	 * @param info The optional <code>info</code> field of the original relation object.
	 * @param dtoHelper The helper.
	 */
	public ValueListRel(String uuid, String componentUuid, String info, DtoHelper dtoHelper) {
		super(uuid, info, dtoHelper);
		this.component = componentUuid;
	}

	/**
	 * Get the freeBIM-ID of the {@link Component}.
	 * 
	 * @return The freeBIM-ID of the {@link Component}.
	 */
	public String getComponent() {
		return component;
	}

	/**
	 * Set the freeBIM-ID of the {@link Component}.
	 * 
	 * @param uuid The freeBIM-ID of the {@link Component} to set.
	 */
	public void setComponent(String uuid) {
		this.component = uuid;
	}

}
