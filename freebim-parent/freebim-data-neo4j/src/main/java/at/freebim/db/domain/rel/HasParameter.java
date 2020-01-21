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

import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.RelationshipEntity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.domain.base.rel.BsddRelation;
import at.freebim.db.domain.base.rel.OrderedBaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.json.rel.HasParameterDeserializer;
import at.freebim.db.json.rel.HasParameterSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between the nodes or classes {@link StatedBaseNode}
 * and {@link Parameter}. It denotes if a {@link StatedBaseNode} has a
 * {@link Parameter}. This relation extends {@link OrderedBaseRel} and
 * implements {@link BsddRelation}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.RelationshipEntity
 * @see at.freebim.db.domain.base.StatedBaseNode
 * @see at.freebim.db.domain.Parameter
 * @see at.freebim.db.domain.base.rel.OrderedBaseRel
 * @see at.freebim.db.domain.base.rel.BsddRelation
 */
@RelationshipBackup
@RelationshipEntity(type = RelationType.HAS_PARAMETER)
@JsonSerialize(using = HasParameterSerializer.class)
@JsonDeserialize(using = HasParameterDeserializer.class)
public class HasParameter extends OrderedBaseRel<StatedBaseNode, Parameter> implements BsddRelation {

	private static final long serialVersionUID = -6053155575923276428L;

	/**
	 * The uuid of the phase.
	 */
	private String phaseUuid;

	/**
	 * The default value.
	 */
	private String defaultValue;

	/**
	 * The bsdd-guid.
	 *
	 * @see at.freebim.db.domain.base.rel.BsddRelation
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index
	private String bsddGuid;

	/**
	 * Creates a new instance of the relation with the type of the relation.
	 */
	public HasParameter() {
		super(RelationType.HAS_PARAMETER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.HAS_PARAMETER.name();
	}

	/**
	 * Get the uuid of the phase.
	 *
	 * @return the phaseUuid
	 */
	public String getPhaseUuid() {
		return phaseUuid;
	}

	/**
	 * Set the uuid of the phase.
	 *
	 * @param phase the phaseUuid to set
	 */
	public void setPhaseUuid(String phase) {
		this.phaseUuid = phase;
	}

	/**
	 * Get the default value.
	 *
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Set the default value.
	 *
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
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
		result = prime * result + ((defaultValue == null) ? 0 : defaultValue.hashCode());
		result = prime * result + ((phaseUuid == null) ? 0 : phaseUuid.hashCode());
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
		HasParameter other = (HasParameter) obj;
		if (defaultValue == null) {
			if (other.defaultValue != null)
				return false;
		} else if (!defaultValue.equals(other.defaultValue))
			return false;
		if (phaseUuid == null) {
			if (other.phaseUuid != null)
				return false;
		} else if (!phaseUuid.equals(other.phaseUuid))
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

}
