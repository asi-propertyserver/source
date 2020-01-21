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
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import at.freebim.db.domain.base.BsddObject;
import at.freebim.db.domain.base.Coded;
import at.freebim.db.domain.base.Described;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.Orderable;
import at.freebim.db.domain.base.ParameterType;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.domain.base.UUidIdentifyableVisitor;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.rel.ContainsParameter;
import at.freebim.db.domain.rel.HasMeasure;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.OfDiscipline;
import at.freebim.db.json.ParameterDeserializer;
import at.freebim.db.json.ParameterSerializer;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * The node for the parameter. It extends {@link StatedBaseNode} and implements
 * {@link Orderable}, {@link Coded}, {@link Named}, {@link Described} and
 * {@link BsddObject}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.NodeEntity
 * @see at.freebim.db.domain.base.StatedBaseNode
 * @see at.freebim.db.domain.base.Orderable
 * @see at.freebim.db.domain.base.Coded
 * @see at.freebim.db.domain.base.Named
 * @see at.freebim.db.domain.base.Described
 * @see at.freebim.db.domain.base.BsddObject
 */
@NodeBackup
@NodeEntity
@JsonSerialize(using = ParameterSerializer.class)
@JsonDeserialize(using = ParameterDeserializer.class)
public class Parameter extends StatedBaseNode implements Orderable, Coded, Named, Described, BsddObject {

	private static final long serialVersionUID = 7604731865056125520L;
	/**
	 * The code.
	 *
	 * @see at.freebim.db.domain.base.Coded
	 */
	private String code;
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
	 * The description.
	 *
	 * @see at.freebim.db.domain.base.Described
	 */
	private String desc;
	/**
	 * The english description.
	 */
	private String descEn;
	/**
	 * The default string.
	 */
	private String defaultString;
	/**
	 * The type of parameter.
	 */
	private ParameterType ptype;
	/**
	 * The bsdd-guid.
	 *
	 * @see at.freebim.db.domain.base.BsddObject
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index
	private String bsddGuid;
	/**
	 * The relation to the {@link Discipline}s.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.OF_DISCIPLINE, direction = Relationship.OUTGOING)
	private Iterable<OfDiscipline> discipline;
	/**
	 * The relation to the node {@link Measure}.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.HAS_MEASURE)
	private Iterable<HasMeasure> measures;
	/**
	 * The incoming relation from nodes of the type {@link StatedBaseNode}.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.HAS_PARAMETER, direction = Relationship.INCOMING)
	private Iterable<HasParameter> components;
	/**
	 * The incoming relation from the nodes of the type {@link StatedBaseNode}.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.CONTAINS_PARAMETER, direction = Relationship.INCOMING)
	private Iterable<ContainsParameter> psets;

	/**
	 * Create new instance.
	 */
	public Parameter() {
		super();
		this.ptype = ParameterType.UNDEFINED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.Coded#getCode()
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * Set the code
	 *
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.Described#getDesc()
	 */
	public String getDesc() {
		return this.desc;
	}

	/**
	 * Set the description.
	 *
	 * @param desc the description to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * Get the default string.
	 *
	 * @return the default string
	 */
	public String getDefaultString() {
		return this.defaultString;
	}

	/**
	 * Set the default string.
	 *
	 * @param defaultString the default string to set
	 */
	public void setDefaultString(String defaultString) {
		this.defaultString = defaultString;
	}

	/**
	 * Get the relations to the {@link Discipline}s.
	 *
	 * @return the relations to the {@link Discipline}s
	 */
	public Iterable<OfDiscipline> getDiscipline() {
		return this.discipline;
	}

	/**
	 * Get the type of the parameter.
	 *
	 * @return the parameter type
	 */
	public ParameterType getPtype() {
		return ptype;
	}

	/**
	 * Set the type of parameter.
	 *
	 * @param ptype the parameter type to set
	 */
	public void setPtype(ParameterType ptype) {
		this.ptype = ptype;
	}

	/**
	 * Get the english name.
	 *
	 * @return the english name.
	 */
	public String getNameEn() {
		return nameEn;
	}

	/**
	 * Set the english name.
	 *
	 * @param en_name the english name to set
	 */
	public void setNameEn(String en_name) {
		this.nameEn = en_name;
	}

	/**
	 * Get the english description.
	 *
	 * @return the english description
	 */
	public String getDescEn() {
		return this.descEn;
	}

	/**
	 * Set the english description.
	 *
	 * @param descEn the english description to set
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
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
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((defaultString == null) ? 0 : defaultString.hashCode());
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((descEn == null) ? 0 : descEn.hashCode());
		result = prime * result + ((nameEn == null) ? 0 : nameEn.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((ptype == null) ? 0 : ptype.hashCode());
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
		Parameter other = (Parameter) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (defaultString == null) {
			if (other.defaultString != null)
				return false;
		} else if (!defaultString.equals(other.defaultString))
			return false;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (descEn == null) {
			if (other.descEn != null)
				return false;
		} else if (!descEn.equals(other.descEn))
			return false;
		if (bsddGuid == null) {
			if (other.bsddGuid != null)
				return false;
		} else if (!bsddGuid.equals(other.bsddGuid))
			return false;
		if (nameEn == null) {
			if (other.nameEn != null)
				return false;
		} else if (!nameEn.equals(other.nameEn))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (measures == null) {
			if (other.measures != null)
				return false;
		} else if (!measures.equals(other.measures))
			return false;
		if (discipline == null) {
			if (other.discipline != null)
				return false;
		} else if (!discipline.equals(other.discipline))
			return false;
		if (ptype != other.ptype)
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
		Parameter other = (Parameter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (defaultString == null) {
			if (other.defaultString != null)
				return false;
		} else if (!defaultString.equals(other.defaultString))
			return false;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (descEn == null) {
			if (other.descEn != null)
				return false;
		} else if (!descEn.equals(other.descEn))
			return false;
		if (nameEn == null) {
			if (other.nameEn != null)
				return false;
		} else if (!nameEn.equals(other.nameEn))
			return false;
		if (ptype != other.ptype)
			return false;
		return true;
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
	 * Get the a relation that determines if this {@link Parameter} has
	 * {@link Measure}s.
	 *
	 * @return the relation to the {@link Measure}s
	 */
	public Iterable<HasMeasure> getMeasures() {
		return this.measures;
	}

	/**
	 * Get the relations that determine if this {@link Parameter} is connected to a
	 * {@link StatedBaseNode}s. You could also say has this {@link Parameter} an
	 * incoming link to a {@link StatedBaseNode}.
	 *
	 * @return the relation to the {@link StatedBaseNode}s or components
	 */
	public Iterable<HasParameter> getComponents() {
		return this.components;
	}

	/**
	 * Get the relations that determine if a list of {@link StatedBaseNode}s
	 * contains this {@link Parameter}.
	 *
	 * @return the relation to the {@link StatedBaseNode}s
	 */
	public Iterable<ContainsParameter> getPsets() {
		return this.psets;
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
