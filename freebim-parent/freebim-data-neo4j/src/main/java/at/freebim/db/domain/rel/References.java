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
package at.freebim.db.domain.rel;

import org.neo4j.ogm.annotation.RelationshipEntity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import at.freebim.db.domain.Library;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.base.rel.TimestampedBaseRel;
import at.freebim.db.json.rel.ReferencesDeserializer;
import at.freebim.db.json.rel.ReferencesSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation of nodes or classes {@link UuidIdentifyable} and
 * {@link Library}. It denotes that a node ({@link UuidIdentifyable}) is
 * referenced by a {@link Library}. This relation extends
 * {@link TimestampedBaseRel}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.RelationshipEntity
 * @see at.freebim.db.domain.base.UuidIdentifyable
 * @see at.freebim.db.domain.Library
 * @see at.freebim.db.domain.base.rel.TimestampedBaseRel
 */
@RelationshipBackup
@RelationshipEntity(type = RelationType.REFERENCES)
@JsonSerialize(using = ReferencesSerializer.class)
@JsonDeserialize(using = ReferencesDeserializer.class)
public class References extends TimestampedBaseRel<UuidIdentifyable, Library> {

	private static final long serialVersionUID = -6722779089353616868L;

	/**
	 * The name of the reference.
	 */
	private String refIdName;

	/**
	 * The id of the reference.
	 */
	private String refId;

	/**
	 * Creates a new instance of the relation with the type of the relation.
	 */
	public References() {
		super(RelationType.REFERENCES);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.REFERENCES.name();
	}

	/**
	 * Get the name of the reference.
	 *
	 * @return the refIdName
	 */
	public String getRefIdName() {
		return refIdName;
	}

	/**
	 * Set the name of the reference.
	 *
	 * @param refIdName the refIdName to set
	 */
	public void setRefIdName(String refIdName) {
		this.refIdName = refIdName;
	}

	/**
	 * Get the id of the reference.
	 *
	 * @return the refId
	 */
	public String getRefId() {
		return refId;
	}

	/**
	 * Set the id of the reference.
	 *
	 * @param refId the refId to set
	 */
	public void setRefId(String refId) {
		this.refId = refId;
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
		result = prime * result + ((refId == null) ? 0 : refId.hashCode());
		result = prime * result + ((refIdName == null) ? 0 : refIdName.hashCode());
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
		References other = (References) obj;
		if (refId == null) {
			if (other.refId != null)
				return false;
		} else if (!refId.equals(other.refId))
			return false;
		if (refIdName == null) {
			if (other.refIdName != null)
				return false;
		} else if (!refIdName.equals(other.refIdName))
			return false;
		return true;
	}
}
