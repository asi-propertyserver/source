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

import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.Document;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.rel.ContributedBy;
import at.freebim.db.domain.rel.DocumentedIn;

/**
 * This abstract class represents the base node for things regarding
 * contributions. It extends {@link UuidIdentifyable}. This node has the
 * capability to connect {@link Contributor}s and {@link Document}s.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.NodeEntity
 * @see at.freebim.db.domain.base.UuidIdentifyable
 * @see at.freebim.db.domain.rel.ContributedBy
 * @see at.freebim.db.domain.rel.DocumentedIn
 * @see at.freebim.db.domain.base.BaseNode
 */
@NodeEntity
@SuppressWarnings("serial")
public abstract class ContributedBaseNode extends UuidIdentifyable {

	/**
	 * The relations to the {@link Contributor}s.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.CONTRIBUTED_BY, direction = Relationship.OUTGOING)
	private Iterable<ContributedBy> contributor;

	/**
	 * The relations to the {@link Document}s.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.DOCUMENTED_IN, direction = Relationship.OUTGOING)
	private Iterable<DocumentedIn> docs;

	/**
	 * Create new instance.
	 */
	protected ContributedBaseNode() {
		super();
	}

	/**
	 * Gets the relations to the {@link Contributor}s.
	 *
	 * @return the relations to the {@link Contributor}s
	 */
	public Iterable<ContributedBy> getContributor() {
		return this.contributor;
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
		result = prime * result + ((contributor == null) ? 0 : contributor.hashCode());
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
		ContributedBaseNode other = (ContributedBaseNode) obj;
		if (contributor == null) {
			if (other.contributor != null)
				return false;
		} else if (!contributor.equals(other.contributor))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.LifetimeBaseNode#equalsData(java.lang.Object)
	 */
	@Override
	public boolean equalsData(Object obj) {
		if (this == obj)
			return true;
		if (!super.equalsData(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	/**
	 * Gets the relations to the {@link Document}s.
	 *
	 * @return the relations to the {@link Document}s
	 */
	public Iterable<DocumentedIn> getDocs() {
		return this.docs;
	}

}
