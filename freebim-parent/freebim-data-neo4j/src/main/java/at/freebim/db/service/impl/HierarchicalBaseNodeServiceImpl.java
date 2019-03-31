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
package at.freebim.db.service.impl;


import at.freebim.db.domain.base.HierarchicalBaseNode;
import at.freebim.db.service.HierarchicalBaseNodeService;


/**
 * This service defines the basics for all services that 
 * define functionality for a class <b>T</b> that extends {@link HierarchicalBaseNode}.
 * This service extends {@link ParameterizedServiceImpl} and implements 
 * {@link HierarchicalBaseNodeService}.
 * 
 * @see at.freebim.db.domain.base.HierarchicalBaseNode
 * @see at.freebim.db.service.impl.ParameterizedServiceImpl
 * @see at.freebim.db.service.HierarchicalBaseNodeService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public abstract class HierarchicalBaseNodeServiceImpl<T extends HierarchicalBaseNode>
		extends ParameterizedServiceImpl<T> implements
		HierarchicalBaseNodeService<T> {
	
}
