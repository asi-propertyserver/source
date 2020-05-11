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

import at.freebim.db.domain.Component;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.json.rel.ValueOverrideDeserializer;
import at.freebim.db.json.rel.ValueOverrideSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between nodes or classes {@link Component} and
 * {@link Parameter}. This relation extends {@link BaseRel}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.RelationshipEntity
 * @see at.freebim.db.domain.Component
 * @see at.freebim.db.domain.Parameter
 * @see at.freebim.db.domain.base.rel.BaseRel
 */
@RelationshipBackup
@RelationshipEntity(type = RelationType.VALUE_OVERRIDE)
@JsonSerialize(using = ValueOverrideSerializer.class)
@JsonDeserialize(using = ValueOverrideDeserializer.class)
public class ValueOverride extends BaseRel<Component, Parameter> {

	private static final long serialVersionUID = -3664766423091303340L;

	/**
	 * The value.
	 */
	private String value;

	/**
	 * The measure.
	 */
	private String measure;

	/**
	 * Creates a new instance of the relation with the type of the relation.
	 */
	public ValueOverride() {
		super(RelationType.VALUE_OVERRIDE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.VALUE_OVERRIDE.name();
	}

	/**
	 * Get the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the value.
	 *
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
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
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((measure == null) ? 0 : measure.hashCode());
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
		ValueOverride other = (ValueOverride) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value) && !(other.value == null && value.equals("")))
			return false;
		if (measure == null) {
			if (other.measure != null)
				return false;
		} else if (!measure.equals(other.measure) && !(other.measure == null && measure.equals("")))
			return false;
		return true;
	}

	/**
	 * Get the measure.
	 *
	 * @return the measure
	 */
	public String getMeasure() {
		return measure;
	}

	/**
	 * Set the measure.
	 *
	 * @param measure the measure to set
	 */
	public void setMeasure(String measure) {
		this.measure = measure;
	}

}
