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

import at.freebim.db.domain.ValueListEntry;

/**
 * The service for the node/class {@link ValueListEntry}.
 * This service extends {@link StatedBaseNodeService} and {@link BsddObjectService}.
 * 
 * @see at.freebim.db.domain.ValueListEntry
 * @see at.freebim.db.service.StatedBaseNodeService
 * @see at.freebim.db.service.BsddObjectService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface ValueListEntryService extends StatedBaseNodeService<ValueListEntry>, BsddObjectService<ValueListEntry> {
	
	/**
	 * Convert decimal numbers with comma to international format with dot.<br>
	 * i.e. <code>47,11</code> becomes <code>47.11</code>
	 * @param node
	 */
	public void correctNumberFormat(ValueListEntry node);

	/**
	 * Get the {@link ValueListEntry} that has the provided name.
	 * 
	 * @param name the name of {@link ValueListEntry}
	 * @return the {@link ValueListEntry} that has the provided name
	 */
	@Secured({"ROLE_EDIT", "ROLE_ADMIN"})
	public List<ValueListEntry> getByName(String name);
}
