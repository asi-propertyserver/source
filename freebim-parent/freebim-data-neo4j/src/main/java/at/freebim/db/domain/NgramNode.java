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
package at.freebim.db.domain;

import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

import at.freebim.db.domain.base.BaseNode;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * The node for n-grams. It extends {@link BaseNode}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.NodeEntity
 * @see at.freebim.db.domain.base.BaseNode
 */
@NodeBackup
@NodeEntity
public class NgramNode extends BaseNode {

	private static final long serialVersionUID = 8034580075674875431L;

	/**
	 * The n-gram. The n-gram is unique in the neo4j-database.
	 *
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index(unique = true)
	private String ng;

	/**
	 * Get the n-gram.
	 *
	 * @return the n-gram
	 */
	public String getNg() {
		return ng;
	}

	/**
	 * Set the n-gram.
	 *
	 * @param ng the n-gram to set
	 */
	public void setNg(String ng) {
		this.ng = ng;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ng == null) ? 0 : ng.hashCode());
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
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NgramNode other = (NgramNode) obj;
		if (ng == null) {
			if (other.ng != null)
				return false;
		} else if (!ng.equals(other.ng))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.BaseNode#equalsData(java.lang.Object)
	 */
	@Override
	public boolean equalsData(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!super.equalsData(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NgramNode other = (NgramNode) obj;
		if (ng == null) {
			if (other.ng != null)
				return false;
		} else if (!ng.equals(other.ng))
			return false;
		return true;
	}

}
