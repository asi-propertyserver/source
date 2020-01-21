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
import org.neo4j.ogm.annotation.Transient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import at.freebim.db.domain.base.BsddObject;
import at.freebim.db.domain.base.Coded;
import at.freebim.db.domain.base.ComponentType;
import at.freebim.db.domain.base.Described;
import at.freebim.db.domain.base.HierarchicalBaseNode;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.Orderable;
import at.freebim.db.domain.base.UUidIdentifyableVisitor;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.rel.ComponentComponent;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.OfMaterial;
import at.freebim.db.domain.rel.ValueOverride;
import at.freebim.db.json.ComponentDeserializer;
import at.freebim.db.json.ComponentSerializer;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * This node represents a component. It extends {@link HierarchicalBaseNode} and
 * implements {@link Orderable}, {@link Named}, {@link Coded}, {@link Described}
 * and {@link BsddObject}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.NodeEntity
 * @see at.freebim.db.domain.base.HierarchicalBaseNode
 * @see at.freebim.db.domain.base.Orderable
 * @see at.freebim.db.domain.base.Named
 * @see at.freebim.db.domain.base.Coded
 * @see at.freebim.db.domain.base.Described
 * @see at.freebim.db.domain.base.BsddObject
 */
@NodeBackup
@NodeEntity
@JsonSerialize(using = ComponentSerializer.class)
@JsonDeserialize(using = ComponentDeserializer.class)
public class Component extends HierarchicalBaseNode implements Orderable, Named, Coded, Described, BsddObject {

	private static final long serialVersionUID = 8072531491485797633L;
	/**
	 * The code.
	 *
	 * @see at.freebim.db.domain.base.Coded
	 */
	protected String code;
	/**
	 * The name.
	 *
	 * @see at.freebim.db.domain.base.Named
	 */
	protected String name;
	/**
	 * The english name.
	 */
	protected String nameEn;
	/**
	 * The description.
	 *
	 * @see at.freebim.db.domain.base.Described
	 */
	protected String desc;
	/**
	 * The english description.
	 */
	protected String descEn;
	/**
	 * The guid of the bsdd object.
	 *
	 * @see at.freebim.db.domain.base.BsddObject
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index
	protected String bsddGuid;
	/**
	 * The type of the component.
	 */
	protected ComponentType type;
	/**
	 * The relation to the {@link Parameter}s.
	 *
	 * @see at.freebim.db.domain.Parameter
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.HAS_PARAMETER, direction = Relationship.OUTGOING)
	private Iterable<HasParameter> parameter;
	/**
	 * The relation to other {@link Component}s.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.COMP_COMP, direction = Relationship.OUTGOING)
	private Iterable<ComponentComponent> parts;
	/**
	 * The relation to the materials ({@link Component}s) the {@link Component} is
	 * build of.
	 *
	 * @see at.freebim.db.domain.Component
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.OF_MATERIAL, direction = Relationship.OUTGOING)
	private Iterable<OfMaterial> material;
	/**
	 * The relation to the {@link Parameter}s that will be overridden.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.VALUE_OVERRIDE, direction = Relationship.OUTGOING)
	private Iterable<ValueOverride> valueOverride;
	/**
	 * Determines if this {@link Component} is a material.
	 *
	 * @see org.neo4j.ogm.annotation.Transient
	 */
	@Transient
	private boolean m;

	/**
	 * Create new instance.
	 */
	public Component() {
		super();
		this.type = ComponentType.CONCRETE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.Coded#getCode()
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Set the code.
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
		return name;
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
		return desc;
	}

	/**
	 * Sets the description.
	 *
	 * @param desc the description to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * Gets the type of the component.
	 *
	 * @return the type of the component
	 */
	public ComponentType getType() {
		return type;
	}

	/**
	 * Sets the type of the component.
	 *
	 * @param type the type of the component to set
	 */
	public void setType(ComponentType type) {
		this.type = type;
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
	 * Sets the english name
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
		return descEn;
	}

	/**
	 * Sets the english description.
	 *
	 * @param descEn the english description to set
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
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
	 * Get the relation to {@link Parameter}s that the {@link Component} has
	 *
	 * @return the relation to {@link Parameter}s that the {@link Component} has
	 */
	public Iterable<HasParameter> getParameter() {
		return this.parameter;
	}

	/**
	 * Get the material out of which the component is constructed.
	 *
	 * @return the relation to materials ({@link Component}) that the
	 *         {@link Component} is made of
	 */
	public Iterable<OfMaterial> getMaterial() {
		return this.material;
	}

	/**
	 * Get the relations to the different parts of which a {@link Component} is
	 * composed.
	 *
	 * @return the parts
	 */
	public Iterable<ComponentComponent> getParts() {
		return parts;
	}

	/**
	 * Get the relation to the values that have been overriden.
	 *
	 * @return the valueOverride
	 */
	public Iterable<ValueOverride> getValueOverride() {
		return valueOverride;
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
		if (!super.equalsData(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Component other = (Component) obj;
		if (!equalsRelation(parameter, other.parameter))
			return false;
		if (!equalsRelation(material, other.material))
			return false;
		if (!equalsRelation(parts, other.parts))
			return false;
		return true;
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
		result = prime * result + ((bsddGuid == null) ? 0 : bsddGuid.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((descEn == null) ? 0 : descEn.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nameEn == null) ? 0 : nameEn.hashCode());
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
		result = prime * result + ((parts == null) ? 0 : parts.hashCode());
		result = prime * result + ((material == null) ? 0 : material.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Component other = (Component) obj;
		if (bsddGuid == null) {
			if (other.bsddGuid != null)
				return false;
		} else if (!bsddGuid.equals(other.bsddGuid))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
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
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		if (parts == null) {
			if (other.parts != null)
				return false;
		} else if (!parts.equals(other.parts))
			return false;
		if (material == null) {
			if (other.material != null)
				return false;
		} else if (!material.equals(other.material))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	/**
	 * Check if this {@link Component} is a material.
	 *
	 * @return <code>true</code> if path to this Component leads to
	 *         <code>IfcMaterialDefinition</code>
	 */
	public boolean isM() {
		return m;
	}

	/**
	 * Set if this is a material
	 *
	 * @param isMaterial set if it is a material
	 */
	public void setM(boolean isMaterial) {
		this.m = isMaterial;
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
