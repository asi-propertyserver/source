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

import at.freebim.db.domain.base.BsddObject;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.domain.base.UUidIdentifyableVisitor;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.rel.HasEntry;
import at.freebim.db.domain.rel.HasValue;
import at.freebim.db.json.ValueListDeserializer;
import at.freebim.db.json.ValueListSerializer;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * The node for a list of value. It extends {@link StatedBaseNode} and
 * implements {@link Named} and {@link BsddObject}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.NodeEntity
 * @see at.freebim.db.domain.base.StatedBaseNode
 * @see at.freebim.db.domain.base.Named
 * @see at.freebim.db.domain.base.BsddObject
 */
@NodeBackup
@NodeEntity
@JsonSerialize(using = ValueListSerializer.class)
@JsonDeserialize(using = ValueListDeserializer.class)
public class ValueList extends StatedBaseNode implements Named, BsddObject {

	private static final long serialVersionUID = 5728609484139126008L;

	/**
	 * The name.
	 *
	 * @see at.freebim.db.domain.base.Named
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index
	private String name;

	/**
	 * The english name.
	 */
	private String nameEn;

	/**
	 * The bsdd-guid.
	 *
	 * @see at.freebim.db.domain.base.BsddObject
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index
	private String bsddGuid;

	/**
	 * The relations to the entries in this list.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = "HAS_ENTRY", direction = Relationship.OUTGOING)
	private ArrayList<HasEntry> entries;

	/**
	 * The relations to the nodes of the type {@link Measure} that owns this list.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.HAS_VALUE, direction = Relationship.INCOMING)
	private ArrayList<HasValue> measures;

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.Named#getName()
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the name.
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the english name.
	 *
	 * @return the english name
	 */
	public String getNameEn() {
		return nameEn;
	}

	/**
	 * Set the english name.
	 *
	 * @param nameEn the english name to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.BsddObject#getBsddGuid()
	 */
	public String getBsddGuid() {
		return this.bsddGuid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.BsddObject#setBsddGuid(java.lang.String)
	 */
	public void setBsddGuid(String bsddGuid) {
		this.bsddGuid = bsddGuid;
	}

	/**
	 * Get the relations to the nodes of the type {@link ValueListEntry} that are
	 * the elements of this list.
	 *
	 * @return the entries ({@link ValueListEntry}) of the list
	 */
	public Iterable<HasEntry> getEntries() {
		return this.entries;
	}

	/**
	 * The relations to the nodes of the type {@link Measure} that own this list.
	 *
	 * @return the relations to the nodes of the type {@link Measure}
	 */
	public Iterable<HasValue> getMeasures() {
		return this.measures;
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
		result = prime * result + ((entries == null) ? 0 : entries.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nameEn == null) ? 0 : nameEn.hashCode());
		result = prime * result + ((bsddGuid == null) ? 0 : bsddGuid.hashCode());
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
		ValueList other = (ValueList) obj;
		if (entries == null) {
			if (other.entries != null)
				return false;
		} else if (!entries.equals(other.entries))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nameEn == null) {
			if (other.nameEn != null)
				return false;
		} else if (!nameEn.equals(other.nameEn))
			return false;
		if (bsddGuid == null) {
			if (other.bsddGuid != null)
				return false;
		} else if (!bsddGuid.equals(other.bsddGuid))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.StatedBaseNode#equalsData(java.lang.Object)
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
		ValueList other = (ValueList) obj;
		if (!equalsRelation(entries, other.entries))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.domain.base.UUidIdentifyableVistitable#accept(at.freebim.db.
	 * domain.base.UUidIdentifyableVisitor)
	 */
	@Override
	public void accept(UUidIdentifyableVisitor visitor) {
		if (visitor != null)
			visitor.visit(this);
	}
}
