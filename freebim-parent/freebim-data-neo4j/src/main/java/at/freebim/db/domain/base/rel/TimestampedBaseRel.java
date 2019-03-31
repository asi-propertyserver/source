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
import at.freebim.db.domain.base.Timestampable;

/**
 * Abstract base class for relations that carry a time stamp.
 * It extends {@link BaseRel}.
 * 
 * @see at.freebim.db.domain.base.rel.BaseRel
 * @see at.freebim.db.domain.base.NodeIdentifyable
 * @see at.freebim.db.domain.base.Timestampable
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 * @param <FROM> Type that represents the start node of the relation.
 * @param <TO> Type that represents the end node of the relation.
 */
@RelationshipEntity
public abstract class TimestampedBaseRel<FROM extends NodeIdentifyable, TO extends Timestampable> extends BaseRel<FROM, TO> {

	private static final long serialVersionUID = 7721954778926106394L;

	/**
	 * The time stamp.
	 */
	private long ts;
	
	/**
	 * Initialize the relation with a type.
	 * 
	 * @param type The type of the relation.
	 */
	public TimestampedBaseRel(String type) {
		super(type);
	}

	/**
	 * Get the time stamp.
	 * 
	 * @return the time stamp
	 */
	public long getTs() {
		return ts;
	}

	/**
	 * Set the time stamp.
	 * 
	 * @param ts the time stamp to set
	 */
	public void setTs(long ts) {
		this.ts = ts;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (ts ^ (ts >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj) || getClass() != obj.getClass())
			return false;

		@SuppressWarnings("unchecked")
		TimestampedBaseRel<FROM, TO> other = (TimestampedBaseRel<FROM, TO>) obj;
		if (ts != other.ts)
			return false;
		return true;
	}





}
