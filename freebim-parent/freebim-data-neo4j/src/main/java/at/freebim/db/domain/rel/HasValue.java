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

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.data.neo4j.annotation.RelationshipEntity;

import at.freebim.db.domain.Measure;
import at.freebim.db.domain.ValueList;
import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.base.rel.BsddRelation;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.json.rel.HasValueDeserializer;
import at.freebim.db.domain.json.rel.HasValueSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between the nodes or classes {@link Measure} and {@link ValueList}.
 * It denotes that a measurement ({@link Measure}) has a list of values ({@link ValueList}).
 * This relation extends {@link BaseRel} and 
 * implements {@link BsddRelation}.
 * 
 * @see at.freebim.db.domain.Measure
 * @see at.freebim.db.domain.ValueList
 * @see at.freebim.db.domain.base.rel.BaseRel
 * @see at.freebim.db.domain.base.rel.BsddRelation
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RelationshipBackup
@RelationshipEntity (type=RelationType.HAS_VALUE)
@JsonSerialize(using = HasValueSerializer.class)
@JsonDeserialize(using = HasValueDeserializer.class)
public class HasValue extends BaseRel<Measure, ValueList> implements BsddRelation {

	private static final long serialVersionUID = 5183301403871922206L;
	
	/**
	 * The bsdd-guid.
	 * 
	 * @see at.freebim.db.domain.base.rel.BsddRelation
	 */
	private String bsddGuid;
	
	/**
	 * The uuid of the component.
	 */
	private String componentUuid;

	/**
	 * Creates a new instance of the relation with the type of the relation. 
	 */
	public HasValue() {
		super(RelationType.HAS_VALUE);
	}
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.HAS_VALUE.name();
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BsddRelation#setBsddGuid(java.lang.String)
	 */
	@Override
	public void setBsddGuid(String guid) {
		this.bsddGuid = guid;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BsddRelation#getBsddGuid()
	 */
	@Override
	public String getBsddGuid() {
		return this.bsddGuid;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bsddGuid == null) ? 0 : bsddGuid.hashCode());
		result = prime * result + ((componentUuid == null) ? 0 : componentUuid.hashCode());
		return result;
	}
	/* (non-Javadoc)
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
		HasValue other = (HasValue) obj;
		if (bsddGuid == null) {
			if (other.bsddGuid != null)
				return false;
		} else if (!bsddGuid.equals(other.bsddGuid))
			return false;
		if (componentUuid == null) {
			if (other.componentUuid != null)
				return false;
		} else if (!componentUuid.equals(other.componentUuid))
			return false;
		return true;
	}
	/**
	 * Get the uuid of the component.
	 * 
	 * @return the componentUuid
	 */
	public String getComponentUuid() {
		return componentUuid;
	}
	/**
	 * Set the uuid of the component.
	 * 
	 * @param componentUuid the componentUuid to set
	 */
	public void setComponentUuid(String componentUuid) {
		this.componentUuid = componentUuid;
	}

	
}
