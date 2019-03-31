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

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * This abstract class/node is the base for all nodes that have a lifetime.
 * It defines a start and an end-point in which this node is valid.
 * Additionally id extends {@link BaseNode} and implements {@link Named}.
 * 
 * @see at.freebim.db.domain.base.BaseNode
 * @see at.freebim.db.domain.base.Named
 * 
 * @author rainer.breuss@uibk.ac.at
 */
@NodeEntity
@SuppressWarnings("serial")
public abstract class LifetimeBaseNode extends BaseNode implements Named {

	/**
	 * The starting time stamp.
	 */
	protected Long validFrom;
	
	/**
	 * The end time stamp. 
	 */
	protected Long validTo;

	/**
	 * Creates a new instance of the node/class with the default values.
	 */
	public LifetimeBaseNode() {
		super();
		this.validFrom = 0L;
		this.validTo = null;
	}

	/**
	 * Gets the point at which this node/class is valid.
	 * 
	 * @return valid from
	 */
	@Fetch
	public Long getValidFrom() {
		return validFrom;
	}

	/**
	 * Sets the point at which this node/class is valid.
	 * 
	 * @param	validFrom	The validFrom point
	 */
	public void setValidFrom(Long validFrom) {
		this.validFrom = validFrom;
	}

	/**
	 * Gets the point to which this node/class is valid.
	 * 
	 * @return valid to
	 */
	@Fetch
	public Long getValidTo() {
		return validTo;
	}

	/**
	 * Sets the point to which this node/class is valid.
	 * 
	 * @param	validTo	The validTo point
	 */
	public void setValidTo(Long validTo) {
		this.validTo = validTo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((validFrom == null) ? 0 : validFrom.hashCode());
		result = prime * result + ((validTo == null) ? 0 : validTo.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		LifetimeBaseNode other = (LifetimeBaseNode) obj;
		if (validFrom == null) {
			if (other.validFrom != null)
				return false;
		} else if (!validFrom.equals(other.validFrom))
			return false;
		if (validTo == null) {
			if (other.validTo != null)
				return false;
		} else if (!validTo.equals(other.validTo))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.BaseNode#equalsData(java.lang.Object)
	 */
	@Override
	public boolean equalsData(Object obj) {
		if (this == obj)
			return true;
		if (!super.equalsData(obj) || getClass() != obj.getClass())
			return false;

		
		LifetimeBaseNode other = (LifetimeBaseNode) obj;

		if (validTo == null && other.validTo != null) {
			return false;
		}
		if (other.validTo == null && validTo != null) {
			return false;
		}

		return true;

	}

}
