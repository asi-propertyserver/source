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

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.annotation.Secured;

import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.rel.References;

/**
 * The service for the node/class {@link Library}.
 * This service extends {@link HierarchicalBaseNodeService}.
 * 
 * @see at.freebim.db.domain.Library
 * @see at.freebim.db.service.HierarchicalBaseNodeService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface LibraryService extends HierarchicalBaseNodeService<Library> {

	public static final String LIBRARY_NAME_FREEBIM = "ON6241-2-freeBIM"; // "freeBIM";
	public static final String LIBRARY_NAME_IFC4 = "Ifc4";
	public static final String LIBRARY_NAME_IFC2x3 = "Ifc2x3";
	public static final String LIBRARY_NAME_FREECLASS = "freeClass";

	/**
	 * Gets the ifc library id.
	 * 
	 * @return the ifc library id as {@link Long}
	 */
	public Long getIfcLibraryId();
	
	/**
	 * Get the {@link Library} with the provided name.
	 * 
	 * @param name the name of the {@link Library}
	 * @return returns the {@link Library} with the provided name.
	 */
	public Library get(String name);

	/**
	 * Get all libraries a specified contributor is responsible for.
	 * @param contributor The contributor.
	 * @return all Libraries for the specified contributor.
	 */	
	public ArrayList<Library> getForContributor(Contributor contributor);
	
	
	/**
	 * Represents a reference from a node to a {@link Library}.
	 * 
	 * @see at.freebim.db.domain.Library
	 * @see at.freebim.db.domain.rel.References
	 * 
	 * @author rainer.breuss@uibk.ac.at
	 *
	 */
	public class LibraryReference {
		/**
		 * The node that references the library.
		 */
		public UuidIdentifyable node;
		
		
		/**
		 * The id of the reference.
		 * 
		 * @see at.freebim.db.domain.rel.References
		 */
		public String refId;
	}
	
	/**
	 * Get a {@link List} of {@link References} to a {@link Library}.
	 * The id of the library and the name of the reference should be provided.
	 * 
	 * @param libraryNodeId the id of the library
	 * @param refIdName the name of the reference
	 * @return the referenced nodes.
	 */
	public List<LibraryReference> getReferencedNodes(Long libraryNodeId, String refIdName);

	/**
	 * Get all refIdName's for a specified library.
	 * 
	 * @param libraryNodeId the id of the {@link Library}
	 * @return the names used in the referenced library
	 */
	public List<String> getRefIdNames(Long libraryNodeId);

	/**
	 * Get all nodes referencing a specified Library with a specified <code>refIdName</code> and a specified <code>refId</code>.
	 * 
	 * @param libraryId <code>nodeId</code> of Library.
	 * @param refIdName the name of the {@link References}
	 * @param refId the id/code of the {@link References}
	 * @return the referenced nodes.
	 */
	public List<UuidIdentifyable> getReferencedNodesForRefId(Long libraryId, String refIdName, String refId);
	
	/**
	 * Delete all connections to a {@link Library}.
	 * 
	 * @param libraryNodeId the id of the {@link Library}
	 * @return the {@link Library} that has been cleared with an updated time stamp
	 */
	@Secured({"ROLE_EDIT", "ROLE_ADMIN"})
	public Library clear(Long libraryNodeId);
	
	/**
	 * Send an update event. That event will than create the ifc-structure.
	 * 
	 * @param libraryNodeId the id of the {@link Library}
	 * @return the {@link Library} to which the event was set with an updated time stamp
	 */
	@Secured({"ROLE_EDIT", "ROLE_ADMIN"})
	public Library update(Long libraryNodeId);
}
