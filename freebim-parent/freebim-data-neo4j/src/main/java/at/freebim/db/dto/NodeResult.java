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
package at.freebim.db.dto;

import java.io.Serializable;

/**
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class NodeResult implements Serializable {

	private static final long serialVersionUID = -519301255643832152L;
	
	/**
	 * The id of the node.
	 */
	public Long nodeId;
	
	/**
	 * The name of the node.
	 */
	public String name;

	/**
	 * Create a new instance.
	 */
	public NodeResult() {
		super();
	}

	
	/**
	 * Set the id of the node and the name
	 * 
	 * @param nodeId the id of the node
	 * @param name the name of the node
	 */
	public NodeResult(Long nodeId, String name) {
		this.nodeId = nodeId;
		this.name = name;
	}

	/**
	 * Get the id of the node.
	 * 
	 * @return the nodeId
	 */
	public Long getNodeId() {
		return nodeId;
	}

	/**
	 * Get the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
