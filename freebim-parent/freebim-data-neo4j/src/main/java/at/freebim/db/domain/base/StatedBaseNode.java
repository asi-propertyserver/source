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
package at.freebim.db.domain.base;

import org.neo4j.ogm.annotation.NodeEntity;

/**
 * This abstract class/node is the base for all nodes that need to define a
 * state. It extends {@link ContributedBaseNode}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.ContributedBaseNode
 * @see at.freebim.db.domain.base.State
 */
@SuppressWarnings("serial")
@NodeEntity
public abstract class StatedBaseNode extends ContributedBaseNode {

	/**
	 * The current state.
	 */
	private State state;

	/**
	 * The comment to the status.
	 */
	private String statusComment;

	/**
	 * Creates a new instance with the default values.
	 */
	protected StatedBaseNode() {
		super();
		this.state = State.UNDEFINED;
	}

	/**
	 * Gets the current state.
	 *
	 * @return the current state .
	 */
	public State getState() {
		return ((state == null) ? State.UNDEFINED : state);
	}

	/**
	 * Sets the current state.
	 *
	 * @param state the current state
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * Gets the status comment.
	 *
	 * @return the status comment
	 */
	public String getStatusComment() {
		return statusComment;
	}

	/**
	 * Sets the status comment.
	 *
	 * @param statusComment the comment
	 */
	public void setStatusComment(String statusComment) {
		this.statusComment = statusComment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.domain.base.ContributedBaseNode#equalsData(java.lang.Object)
	 */
	@Override
	public boolean equalsData(Object obj) {
		if (this == obj)
			return true;
		if (!super.equalsData(obj) || getClass() != obj.getClass())
			return false;

		StatedBaseNode other = (StatedBaseNode) obj;
		if (state != State.UNDEFINED && state != other.state)
			return false;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.ContributedBaseNode#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((statusComment == null) ? 0 : statusComment.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj) || getClass() != obj.getClass())
			return false;

		StatedBaseNode other = (StatedBaseNode) obj;
		if (state != other.state)
			return false;
		if (statusComment == null) {
			if (other.statusComment != null)
				return false;
		} else if (!statusComment.equals(other.statusComment))
			return false;
		return true;
	}
}
