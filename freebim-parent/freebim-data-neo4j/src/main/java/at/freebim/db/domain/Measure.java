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
import at.freebim.db.domain.base.ContributedBaseNode;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.Orderable;
import at.freebim.db.domain.base.UUidIdentifyableVisitor;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.rel.HasMeasure;
import at.freebim.db.domain.rel.HasValue;
import at.freebim.db.domain.rel.OfDataType;
import at.freebim.db.domain.rel.OfUnit;
import at.freebim.db.json.MeasureDeserializer;
import at.freebim.db.json.MeasureSerializer;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * This node represents a measurement. It extends {@link ContributedBaseNode}
 * and implements {@link Named}, {@link Orderable} and {@link BsddObject}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.ContributedBaseNode
 * @see at.freebim.db.domain.base.Named
 * @see at.freebim.db.domain.base.Orderable
 * @see at.freebim.db.domain.base.BsddObject
 */
@NodeBackup
@NodeEntity
@JsonSerialize(using = MeasureSerializer.class)
@JsonDeserialize(using = MeasureDeserializer.class)
public class Measure extends ContributedBaseNode implements Named, Orderable, BsddObject {

	private static final long serialVersionUID = -1330064613916093507L;

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
	 * The description.
	 */
	private String desc;

	/**
	 * The english description.
	 */
	private String descEn;

	/**
	 * The prefix.
	 */
	private String prefix;

	/**
	 * The bsdd-guid.
	 *
	 * @see at.freebim.db.domain.base.BsddObject
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index
	private String bsddGuid;

	/**
	 * The relations to the {@link DataType}.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.OF_DATATYPE, direction = Relationship.OUTGOING)
	private ArrayList<OfDataType> dataType;

	/**
	 * The relations to the {@link Unit}.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.OF_UNIT, direction = Relationship.OUTGOING)
	private ArrayList<OfUnit> unit;

	/**
	 * The relations to the {@link ValueList}.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.HAS_VALUE, direction = Relationship.OUTGOING)
	private ArrayList<HasValue> value;

	/**
	 * The relations to the {@link Parameter}s.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.HAS_MEASURE, direction = Relationship.INCOMING)
	private ArrayList<HasMeasure> params;

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.Named#getName()
	 */
	@Override
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
	 * @see at.freebim.db.domain.base.BsddObject#getBsddGuid()
	 */
	public String getBsddGuid() {
		return bsddGuid;
	}

	/**
	 * Set the bsdd guid.
	 *
	 * @param bsddGuid the bsddGuid to set
	 */
	public void setBsddGuid(String bsddGuid) {
		this.bsddGuid = bsddGuid;
	}

	/**
	 * Get the relations to the {@link DataType}s.
	 *
	 * @return the relations to the {@link DataType}s
	 */
	public Iterable<OfDataType> getDataType() {
		return this.dataType;
	}

	/**
	 * Get the relations to the {@link Unit}s
	 *
	 * @return the relations to the {@link Unit}s
	 */
	public Iterable<OfUnit> getUnit() {
		return this.unit;
	}

	/**
	 * Get the relations to the {@link ValueList}s
	 *
	 * @return the relations to the the {@link ValueList}s
	 */
	public Iterable<HasValue> getValue() {
		return this.value;
	}

	/**
	 * Get the incoming relation from the {@link Parameter}.
	 *
	 * @return the incoming relation from the {@link Parameter}.
	 */
	public Iterable<HasMeasure> getParams() {
		return this.params;
	}

	/**
	 * Get the english name.
	 *
	 * @return the english name
	 */
	public String getNameEn() {
		return this.nameEn;
	}

	/**
	 * Set the english name
	 *
	 * @param nameEn the english name to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	/**
	 * Get the description.
	 *
	 * @return the desc
	 */
	public String getDesc() {
		return this.desc;
	}

	/**
	 * Set the description.
	 *
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
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
		result = prime * result + ((bsddGuid == null) ? 0 : bsddGuid.hashCode());
		result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((descEn == null) ? 0 : descEn.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nameEn == null) ? 0 : nameEn.hashCode());
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Measure other = (Measure) obj;
		if (bsddGuid == null) {
			if (other.bsddGuid != null)
				return false;
		} else if (!bsddGuid.equals(other.bsddGuid))
			return false;
		if (dataType == null) {
			if (other.dataType != null)
				return false;
		} else if (!dataType.equals(other.dataType))
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
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		if (params == null) {
			if (other.params != null)
				return false;
		} else if (!params.equals(other.params))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.domain.base.ContributedBaseNode#equalsData(java.lang.Object)
	 */
	@Override
	public boolean equalsData(Object obj) {
		if (this == obj)
			return true;
		if (!super.equalsData(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Measure other = (Measure) obj;
		if (!equalsRelation(dataType, other.dataType))
			return false;
		if (!equalsRelation(unit, other.unit))
			return false;
		if (!equalsRelation(value, other.value))
			return false;
		return true;
	}

	/**
	 * Set the prefix.
	 *
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Get the prefix.
	 *
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
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
