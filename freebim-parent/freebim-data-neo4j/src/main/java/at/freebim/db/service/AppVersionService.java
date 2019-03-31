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

import at.freebim.db.domain.Component;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.rel.Equals;
import at.freebim.db.domain.rel.References;

/**
 * The interface for the app-version service.
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface AppVersionService {

	/**
	 * Creates the parent of the hierarchy relation.
	 * 
	 * 
	 * @return the number of created relations
	 * */
	public Long createParentOfRelations();
	
	/**
	 * Create the ValueList of the {@link Component} relation an delete unused nodes or duplicated.
	 * 
	 * @return number of nodes queried
	 * */
	public Long createValueListOfComponentRelations();

	/**
	 * Delete equal relations ({@link Equals}) that are duplicated.
	 * This cypher query denotes a duplicate (a)-[r1:EQUALS]->(b)-[r2:EQUALS]->(a) .
	 * 
	 * @return the number of deleted relations
	 */
	public Long dropDuplicateEqualRelations();

	/**
	 * Drop equal relations that are defined multiple times.
	 * 
	 * @return the number of deleted relations
	 */
	public Long dropMultipleEqualRelations();

	/**
	 * Drop equal relation from a node to itself.
	 * 
	 * @return the number of deleted relations
	 */
	public Long dropEqualSelfRelations();

	/**
	 * Perform a cleanup.
	 * Simply delete all nodes that do not have the type Parameter 
	 * but are in the following relation: (co:Component)-[r:HAS_PARAMETER]->(x)
	 */
	public void performDbCleanup();
	
	/**
	 * Correct the {@link References} from {@link Component}s to {@link Library}s.
	 */
	public void correctComponentLibraryReferences();
}
