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

import at.freebim.db.domain.Component;
import at.freebim.db.domain.Library;

/**
 * The service for {@link Component}. This service extends
 * {@link HierarchicalBaseNodeService} and {@link BsddObjectService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Component
 * @see at.freebim.db.service.HierarchicalBaseNodeService
 * @see at.freebim.db.service.BsddObjectService
 */
public interface ComponentService extends HierarchicalBaseNodeService<Component>, BsddObjectService<Component> {

	/**
	 * Gets a {@link List} of {@link Component}s that are in the library with the
	 * provided id and have the provided name.
	 *
	 * @param name      the name of the {@link Component}
	 * @param libraryId the id of the {@link Library}
	 * @return returns a {@link List} of {@link Component}s
	 */
	List<Component> getByNameFromLibrary(String name, Long libraryId);

	/**
	 * Get node that has the provided node id in the library with the provided id.
	 *
	 * @param srcNodeId     the node id
	 * @param libraryNodeId the library id
	 * @return returns a {@link Component}
	 */
	Component getEqualFromLibrary(Long srcNodeId, Long libraryNodeId);

	/**
	 * Get nodes that are in the hierarchy of the provided root node.
	 *
	 * @param rootName the name of the root node
	 * @return returns a {@link List} of {@link Component}s
	 */
	List<Component> getNodesFromHierarchy(String rootName);

}
