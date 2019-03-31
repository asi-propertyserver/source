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

import org.springframework.data.neo4j.annotation.NodeEntity;

import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.Coded;
import at.freebim.db.domain.base.Described;
import at.freebim.db.domain.base.Named;

/**
 * The node for searching string.
 * It extends {@link BaseNode} and 
 * implements {@link Named}, {@link Described} and {@link Coded}.
 * 
 * @see at.freebim.db.domain.base.BaseNode
 * @see at.freebim.db.domain.base.Named
 * @see at.freebim.db.domain.base.Described
 * @see at.freebim.db.domain.base.Coded
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@NodeEntity
public class SearchStringNode extends BaseNode implements Named, Described, Coded {

	private static final long serialVersionUID = -7244725116364027119L;

	/**
	 * The string for searching.
	 */
	private String searchString;

	/**
	 * Get the string to search.
	 * 
	 * @return the search string
	 */
	public String getSearchString() {
		return searchString;
	}

	/**
	 * Set the search string.
	 * 
	 * @param searchString the search string to set
	 */
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.Coded#getCode()
	 */
	@Override
	public String getCode() {
		return this.searchString;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.Described#getDesc()
	 */
	@Override
	public String getDesc() {
		return this.searchString;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.Named#getName()
	 */
	@Override
	public String getName() {
		return this.searchString;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((searchString == null) ? 0 : searchString.hashCode());
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
		SearchStringNode other = (SearchStringNode) obj;
		if (searchString == null) {
			if (other.searchString != null)
				return false;
		} else if (!searchString.equals(other.searchString))
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
		if (obj == null)
			return false;
		if (!super.equalsData(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchStringNode other = (SearchStringNode) obj;
		if (searchString == null) {
			if (other.searchString != null)
				return false;
		} else if (!searchString.equals(other.searchString))
			return false;
		return true;
	}

}
