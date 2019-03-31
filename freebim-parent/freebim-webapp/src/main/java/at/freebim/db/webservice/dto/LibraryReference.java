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
 * DTO of a {@link Library} reference.<br>
 * This is a back link to an object in an external source (typically an external
 * database, but might be an Excel table also), referenced by a table name (
 * <code>refIdName</code>) and an ID (<code>refId</code>).
 * The class extends {@link Base}.
 * 
 * @see at.freebim.db.webservice.dto.Library
 * @see at.freebim.db.webservice.dto.Base
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class LibraryReference extends Base<UuidIdentifyable> {

	/**
	 * This represents a node and and an id of the reference to a library.
	 * 
	 * @see at.freebim.db.domain.Library
	 * @see at.freebim.db.domain.rel.References
	 */
	private final at.freebim.db.service.LibraryService.LibraryReference ref;

	/**
	 * Creates a new instance.
	 * @param ref The original node.
	 * @param dtoHelper The helper.
	 */
	public LibraryReference(at.freebim.db.service.LibraryService.LibraryReference ref, DtoHelper dtoHelper) {
		super(ref.node, dtoHelper);
		this.ref = ref;
	}

	/**
	 * Get the refId.
	 * @return The refId.
	 */
	public String getRefId() {
		return this.dtoHelper.getString(ref.refId);
	}

	/**
	 * Set the refId.
	 * @param refId The refId to set.
	 */
	public void setRefId(String refId) {
		ref.refId = refId;
	}

}
