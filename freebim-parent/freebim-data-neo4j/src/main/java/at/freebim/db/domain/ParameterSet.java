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
import at.freebim.db.domain.base.ParameterSetType;
import at.freebim.db.domain.base.Parameterized;
import at.freebim.db.domain.base.UUidIdentifyableVisitor;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.rel.ContainsParameter;
import at.freebim.db.domain.rel.HasParameterSet;
import at.freebim.db.json.ParameterSetDeserializer;
import at.freebim.db.json.ParameterSetSerializer;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * The node for the set of parameters. It extends {@link Parameterized} and
 * implements {@link Named} and {@link BsddObject}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.NodeEntity
 * @see at.freebim.db.domain.base.Parameterized
 * @see at.freebim.db.domain.base.Named
 * @see at.freebim.db.domain.base.BsddObject
 */
@NodeBackup
@NodeEntity
@JsonSerialize(using = ParameterSetSerializer.class)
@JsonDeserialize(using = ParameterSetDeserializer.class)
public class ParameterSet extends Parameterized implements Named, BsddObject {

	private static final long serialVersionUID = -4244177266667693558L;

	/**
	 * The name.
	 *
	 * @see at.freebim.db.domain.base.Named
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index
	private String name;

	/**
	 * The name in english.
	 */
	private String nameEn;

	/**
	 * The relation to the {@link Parameter}s that are part of this set.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.CONTAINS_PARAMETER, direction = Relationship.OUTGOING)
	private ArrayList<ContainsParameter> parameters;

	/**
	 * The relation to the hierarchical owner of the set.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.HAS_PARAMETER_SET, direction = Relationship.INCOMING)
	private ArrayList<HasParameterSet> owners;

	/**
	 * The bsdd-guid.
	 *
	 * @see at.freebim.db.domain.base.BsddObject
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index
	private String bsddGuid;

	/**
	 * The type of the parameter set.
	 */
	private ParameterSetType type;

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
	 * @param en_name the nameEn to set
	 */
	public void setNameEn(String en_name) {
		this.nameEn = en_name;
	}

	/**
	 * The relations to the {@link Parameter}s that are part of this set.
	 *
	 * @return the relation to the {@link Parameter}s
	 */
	public Iterable<ContainsParameter> getParameters() {
		return this.parameters;
	}

	/**
	 * The relations to the hierarchical owners of this set.
	 *
	 * @return the relations to the hierarchical owner
	 */
	public Iterable<HasParameterSet> getOwners() {
		return this.owners;
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
		ParameterSet other = (ParameterSet) obj;
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
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParameterSet other = (ParameterSet) obj;
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
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.BsddObject#getBsddGuid()
	 */
	public String getBsddGuid() {
		return bsddGuid;
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
	 * Get the type of the parameter set.
	 *
	 * @return the type
	 */
	public ParameterSetType getType() {
		return type;
	}

	/**
	 * Set the type of the parameter set.
	 *
	 * @param type the type to set
	 */
	public void setType(ParameterSetType type) {
		this.type = type;
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
