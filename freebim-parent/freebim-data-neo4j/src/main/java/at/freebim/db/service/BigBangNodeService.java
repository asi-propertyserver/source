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

import at.freebim.db.domain.BigBangNode;

/**
 * The service for the class/node {@link BigBangNode}.
 * It extends {@link HierarchicalBaseNodeService}.
 * 
 * @see at.freebim.db.domain.BigBangNode
 * @see at.freebim.db.service.HierarchicalBaseNodeService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface BigBangNodeService extends HierarchicalBaseNodeService<BigBangNode> {

	/**
	 * Get the root node/class ({@link BigBangNode}.
	 * If there exists no {@link BigBangNode} one is created.
	 * 
	 * @return returns the root node/class {@link BigBangNode}.
	 * */
	public BigBangNode getBigBangNode();
}
