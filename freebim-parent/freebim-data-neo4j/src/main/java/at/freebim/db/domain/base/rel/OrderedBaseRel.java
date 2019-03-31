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
package at.freebim.db.domain.base.rel;

import org.springframework.data.neo4j.annotation.RelationshipEntity;

import at.freebim.db.domain.base.NodeIdentifyable;
import at.freebim.db.domain.base.Orderable;

/**
 * Abstract base of relations where end nodes could be represented as an ordered
 * list of nodes. It extends {@link BaseRel}.
 * 
 * @see at.freebim.db.domain.base.rel.BaseRel
 * @see at.freebim.db.domain.base.NodeIdentifyable
 * @see at.freebim.db.domain.base.Orderable
 * 
 * @author rainer.breuss@uibk.ac.at
 * 
 * @param <FROM>
 *            Type that represents the start node of the relation.
 * @param <TO>
 *            Type that represents the end node of the relation. This is the
 *            type that could be ordered.
 */
@RelationshipEntity
public abstract class OrderedBaseRel<FROM extends NodeIdentifyable, TO extends Orderable> extends BaseRel<FROM, TO> {

	private static final long serialVersionUID = 439664392924563708L;

	/**
	 * The ordering.
	 */
	private int ordering;
	
	/**
	 * Initializes the relation with a type.
	 * 
	 * @param type The type of the relation.
	 */
	public OrderedBaseRel(String type) {
		super(type);
	}

	/**
	 * Get the ordering.
	 * 
	 * @return the ordering
	 */
	public int getOrdering() {
		return ordering;
	}

	/**
	 * Set the ordering.
	 * 
	 * @param ordering the ordering to set
	 */
	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ordering;
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
		@SuppressWarnings("unchecked")
		OrderedBaseRel<FROM, TO> other = (OrderedBaseRel<FROM, TO>) obj;
		if (ordering != other.ordering)
			return false;
		return true;
	}


}
