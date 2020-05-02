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

import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.base.rel.BsddRelation;
import at.freebim.db.domain.base.rel.OrderedBaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.json.rel.HasMeasureDeserializer;
import at.freebim.db.json.rel.HasMeasureSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between to nodes or classes {@link Parameter} and
 * {@link Measure}. It denotes if a {@link Parameter} has a measurement
 * ({@link Measure}). This relation extends {@link OrderedBaseRel} and
 * implements {@link BsddRelation}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.RelationshipEntity
 * @see at.freebim.db.domain.Parameter
 * @see at.freebim.db.domain.Measure
 * @see at.freebim.db.domain.base.rel.OrderedBaseRel
 * @see at.freebim.db.domain.base.rel.BsddRelation
 */
@RelationshipBackup
@RelationshipEntity(type = RelationType.HAS_MEASURE)
@JsonSerialize(using = HasMeasureSerializer.class)
@JsonDeserialize(using = HasMeasureDeserializer.class)
public class HasMeasure extends OrderedBaseRel<Parameter, Measure> implements BsddRelation {

	private static final long serialVersionUID = 4682531219800977662L;

	/**
	 * The bsdd-guid.
	 *
	 * @see at.freebim.db.domain.base.rel.BsddRelation
	 */
	private String bsddGuid;

	/**
	 * Creates a new instance of the relation with the type of the relation.
	 */
	public HasMeasure() {
		super(RelationType.HAS_MEASURE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.HAS_MEASURE.name();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.rel.BsddRelation#getBsddGuid()
	 */
	@Override
	public String getBsddGuid() {
		return this.bsddGuid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.rel.BsddRelation#setBsddGuid(java.lang.String)
	 */
	@Override
	public void setBsddGuid(String guid) {
		this.bsddGuid = guid;
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
		HasMeasure other = (HasMeasure) obj;
		if (bsddGuid == null) {
			if (other.bsddGuid != null)
				return false;
		} else if (!bsddGuid.equals(other.bsddGuid) && !(other.bsddGuid == null && bsddGuid.equals("")))
			return false;
		return true;
	}

}
