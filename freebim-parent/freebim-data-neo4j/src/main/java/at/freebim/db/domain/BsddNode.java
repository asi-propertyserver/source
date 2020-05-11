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

import java.util.ArrayList;

import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.rel.Bsdd;
import at.freebim.db.json.BsddNodeDeserializer;
import at.freebim.db.json.BsddNodeSerializer;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * This is the Bsdd-node (Building smart data dictionary node). This node
 * extends {@link BaseNode}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.NodeEntity
 * @see at.freebim.db.domain.base.BaseNode
 */
@NodeBackup
@NodeEntity
@JsonSerialize(using = BsddNodeSerializer.class)
@JsonDeserialize(using = BsddNodeDeserializer.class)
public class BsddNode extends BaseNode {

	private static final long serialVersionUID = 4458338660403305612L;

	/**
	 * The guid.
	 *
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index(unique = true)
	private String guid;

	/**
	 * The bsdd relations.
	 *
	 * @see at.freebim.db.domain.rel.Bsdd
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.BSDD, direction = Relationship.OUTGOING)
	private ArrayList<Bsdd> nodes;

	/**
	 * Create a new instance.
	 */
	public BsddNode() {
		super();
	}

	/**
	 * Returns the guid.
	 *
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * Sets the guid.
	 *
	 * @param name the guid to set
	 */
	public void setGuid(String name) {
		this.guid = name;
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
		result = prime * result + ((guid == null) ? 0 : guid.hashCode());
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
		BsddNode other = (BsddNode) obj;
		if (guid == null) {
			if (other.guid != null)
				return false;
		} else if (!guid.equals(other.guid))
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
		BsddNode other = (BsddNode) obj;
		if (guid == null) {
			if (other.guid != null)
				return false;
		} else if (!guid.equals(other.guid))
			return false;
		return true;
	}

	/**
	 * Get the relations from this {@link BsddNode} to a {@link UuidIdentifyable}
	 * node.
	 *
	 * @return the nodes
	 */
	public Iterable<Bsdd> getNodes() {
		return nodes;
	}

}
