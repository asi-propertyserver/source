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
package at.freebim.db.webapp.session;

import at.freebim.db.webapp.session.SessionTracker.SessionAction;

/**
 * This node is used to report modifications ({@link SessionAction}) of nodes.
 * 
 * @see SessionAction
 * 
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class NodeInfo {
	
	
	/**
	 * The class name.
	 */
	private String c;
	
	/**
	 * The node id.
	 */
	private Long nodeId;
	
	/**
	 * The time stamp.
	 */
	private Long ts;
	
	
	/**
	 * The action that has been performed in this session.
	 */
	private final SessionAction action;
	
	/**
	 * Creates a new instance of the class.
	 * 
	 * @param action the {@link SessionAction}
	 */
	public NodeInfo(SessionAction action) {
		super();
		this.action = action;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NodeInfo [action=" + action + ", clName=" + c + ", nodeId=" + nodeId + ", ts=" + ts + "]";
	}

	/**
	 * Get the class name.
	 * 
	 * @return the class name
	 */
	public String getC() {
		return c;
	}

	/**
	 * Get the id of the node
	 * 
	 * @return the id of the node
	 */
	public Long getNodeId() {
		return nodeId;
	}

	/**
	 * Get the time stamp.
	 * 
	 * @return the time stamp
	 */
	public Long getTs() {
		return ts;
	}

	/**
	 * Get the session action.
	 * 
	 * @return the action
	 */
	public SessionAction getAction() {
		return action;
	}

	/**
	 * Set the class name.
	 * 
	 * @param c the class name to set
	 */
	public void setC(String c) {
		this.c = c;
	}

	/**
	 * Set the id of the node
	 * 
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * Set the time stamp.
	 * 
	 * @param ts the time stamp to set
	 */
	public void setTs(Long ts) {
		this.ts = ts;
	}


}
