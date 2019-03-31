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

/**
 * Abstract base class for relations that could b
 * e qualified by a floating point value in the range 
 * from <code>0.</code> (bad) to <code>1.</code> (good).
 * It extends {@link BaseRel}.
 * 
 * @see at.freebim.db.domain.base.rel.BaseRel
 * @see at.freebim.db.domain.base.NodeIdentifyable
 * 
 *
 * @param <FROM> Type that represents the start node of the relation.
 * @param <TO> Type that represents the end node of the relation.
 * 
 * @author rainer.breuss@uibk.ac.at
 */
@RelationshipEntity
public abstract class QualifiedBaseRel<FROM extends NodeIdentifyable, TO extends NodeIdentifyable> extends BaseRel<FROM, TO> {

	private static final long serialVersionUID = 7721954778926106394L;

	/**
	 * The quality.
	 */
	private double q;
	
	/**
	 * Initialize the relation with a type.
	 * 
	 * @param type The type of the relation.
	 */
	public QualifiedBaseRel(String type) {
		super(type);
	}

	/**
	 * Get the quality.
	 * 
	 * @return the q
	 */
	public double getQ() {
		return q;
	}

	/**
	 * Set the quality.
	 * 
	 * @param q the q to set
	 */
	public void setQ(double q) {
		this.q = q;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(q);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		QualifiedBaseRel<FROM, TO> other = (QualifiedBaseRel<FROM, TO>) obj;
		if (Double.doubleToLongBits(q) != Double.doubleToLongBits(other.q))
			return false;
		return true;
	}



}
