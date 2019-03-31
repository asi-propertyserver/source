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

import at.freebim.db.domain.SimpleNamedNode;

/**
 * The service for the node/class {@link SimpleNamedNode}.
 * This service extends {@link HierarchicalBaseNodeService}.
 * 
 * @see at.freebim.db.domain.SimpleNamedNode
 * @see at.freebim.db.service.HierarchicalBaseNodeService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface SimpleNamedNodeService extends HierarchicalBaseNodeService<SimpleNamedNode> {

	/**
	 * Find the {@link SimpleNamedNode} that has the provided name and type.
	 * 
	 * @param name the name of {@link SimpleNamedNode}
	 * @param type the type of {@link SimpleNamedNode}. Can be <code>null</code>.
	 * 
	 * @return the {@link SimpleNamedNode} that has the provided name and type.
	 */
	public SimpleNamedNode find(String name, String type);

	/**
	 * Find the {@link SimpleNamedNode} that has the provided name and type.
	 * If no such node is found, create a new one with the provided name and type.
	 * 
	 * @param name the name of {@link SimpleNamedNode}
	 * @param type the type of {@link SimpleNamedNode}. Can be <code>null</code>.
	 * 
	 * @return the {@link SimpleNamedNode} that has the provided name and type.
	 */
	public SimpleNamedNode get(String name, String type);
}
