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

import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import at.freebim.db.domain.BsddNode;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.rel.Bsdd;
import at.freebim.db.domain.rel.References;

/**
 * This abstract class/node is the base for all nodes that need a universal
 * unique id. Since the normal graph id can be reused when nodes are deleted,
 * this uuid can be used to reference nodes from external applications. It
 * extends {@link LifetimeBaseNode} and implements
 * {@link UUidIdentifyableVistitable}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.NodeEntity
 * @see at.freebim.db.domain.base.LifetimeBaseNode
 * @see at.freebim.db.domain.base.UUidIdentifyableVistitable
 */
@NodeEntity
public abstract class UuidIdentifyable extends LifetimeBaseNode implements UUidIdentifyableVistitable {

	private static final long serialVersionUID = -4153983138487987940L;

	/**
	 * The universal unique identifier.
	 *
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index(unique = true)
	private String uuid;

	/**
	 * The relations to the libraries ({@link Library}) that are references.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.REFERENCES, direction = Relationship.OUTGOING)
	private Iterable<References> ref;

	/**
	 * possible bsDD-Guids (candidates)
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.BSDD, direction = Relationship.INCOMING)
	private Iterable<Bsdd> bsdd;

	/**
	 * Creates a new instance.
	 */
	public UuidIdentifyable() {
		super();
	}

	/**
	 * Unique ID (freeBIM-ID) used to reference this node from external applications
	 * or as a string parameter inside of relations (i.e. HasParameter relation uses
	 * this to reference a Phase).<br>
	 * (nodeId is not guaranteed to be persistent!).
	 *
	 * @return the uuid
	 */
	public String getUuid() {
		return this.uuid;
	}

	/**
	 * Set the universal unique identifier.
	 *
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		UuidIdentifyable other = (UuidIdentifyable) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	/**
	 * Get the relations to the libraries ({@link Library}) that are references.
	 *
	 * @return the references
	 */
	public Iterable<References> getRef() {
		return this.ref;
	}

	/**
	 * Get the incoming relations to the {@link BsddNode}s that are bsdd-guid
	 * candidates.
	 *
	 * @return the bsdd
	 */
	public Iterable<Bsdd> getBsdd() {
		return this.bsdd;
	}

}
