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

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;

import at.freebim.db.domain.base.BsddObject;
import at.freebim.db.domain.base.ContributedBaseNode;
import at.freebim.db.domain.base.Described;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.UUidIdentifyableVisitor;
import at.freebim.db.domain.json.UnitDeserializer;
import at.freebim.db.domain.json.UnitSerializer;
import at.freebim.db.domain.rel.OfUnit;
import at.freebim.db.domain.rel.UnitConversion;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * The node for a unit.
 * It extends {@link ContributedBaseNode} and 
 * implements {@link Described} {@link Named} and {@link BsddObject}.
 * 
 * @see at.freebim.db.domain.base.ContributedBaseNode
 * @see at.freebim.db.domain.base.Described
 * @see at.freebim.db.domain.base.Named
 * @see at.freebim.db.domain.base.BsddObject
 * 
 * @author rainer.breuss@uibk.ac.at
 * */
@NodeBackup
@NodeEntity
@JsonSerialize(using = UnitSerializer.class)
@JsonDeserialize(using = UnitDeserializer.class)
public class Unit extends ContributedBaseNode implements Described, Named, BsddObject {

	private static final long serialVersionUID = 5728609484139126008L;

	/**
	 * The code of the unit.
	 */
	private String unitCode;
	
	/**
	 * The name.
	 * 
	 * @see at.freebim.db.domain.base.Named
	 */
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
	 * The bsdd-guid.
	 * 
	 * @see at.freebim.db.domain.base.BsddObject
	 */
	private String bsddGuid;
	
	/**
	 * The relations to other {@link Unit}s that represent the conversion.
	 */
	private Iterable<UnitConversion> conversions;
	
	
	/**
	 * The incoming relations from the node {@link Measure}.
	 */
	private Iterable<OfUnit> measures;


	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.Named#getName()
	 */
	@Indexed
	public String getName() {
		return name;
	}

	/**
	 * Get the unit code.
	 * 
	 * @return the unit code
	 */
	public String getUnitCode() {
		return unitCode;
	}

	/**
	 * Set the name.
	 * 
	 * @param unit the name to set
	 */
	public void setName(String unit) {
		this.name = unit;
	}

	/**
	 * Set the unit code.
	 * 
	 * @param unitCode the unit code to set
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	/**
	 * Set the description.
	 * 
	 * @param comment the description to set
	 */
	public void setDesc(String comment) {
		this.desc = comment;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.Described#getDesc()
	 */
	@Override
	public String getDesc() {
		return this.desc;
	}

	/**
	 * Get the english name.
	 * 
	 * @return the english name
	 */
	public String getDescEn() {
		return this.descEn;
	}

	/**
	 * Set the english description.
	 * 
	 * @param descEn the english description
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.BsddObject#getBsddGuid()
	 */
	@Indexed
	public String getBsddGuid() {
		return this.bsddGuid;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.BsddObject#setBsddGuid(java.lang.String)
	 */
	public void setBsddGuid(String guid) {
		this.bsddGuid = guid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((bsddGuid == null) ? 0 : bsddGuid.hashCode());
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((descEn == null) ? 0 : descEn.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nameEn == null) ? 0 : nameEn.hashCode());
		result = prime * result
				+ ((unitCode == null) ? 0 : unitCode.hashCode());
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
		Unit other = (Unit) obj;
		if (bsddGuid == null) {
			if (other.bsddGuid != null)
				return false;
		} else if (!bsddGuid.equals(other.bsddGuid))
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
		if (unitCode == null) {
			if (other.unitCode != null)
				return false;
		} else if (!unitCode.equals(other.unitCode))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.BaseNode#equalsData(java.lang.Object)
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
		Unit other = (Unit) obj;
		if (bsddGuid == null) {
			if (other.bsddGuid != null)
				return false;
		} else if (!bsddGuid.equals(other.bsddGuid))
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
		if (unitCode == null) {
			if (other.unitCode != null)
				return false;
		} else if (!unitCode.equals(other.unitCode))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.UUidIdentifyableVistitable#accept(at.freebim.db.domain.base.UUidIdentifyableVisitor)
	 */
	@Override
	public void accept(UUidIdentifyableVisitor visitor) {
		if (visitor != null)
			visitor.visit(this);
	}

	/**
	 * The relations to nodes of the same type that represent the conversion. 
	 * 
	 * @return the converted {@link Unit}s
	 */
	@RelatedToVia(type="UNIT_CONVERSION", direction=Direction.BOTH)
	@Fetch
	public Iterable<UnitConversion> getConversions() {
		return conversions;
	}

	/**
	 * The incoming relation from a node {@link Measure}.
	 * 
	 * @return the measures
	 */
	@RelatedToVia(type="OF_UNIT", direction=Direction.INCOMING)
	@Fetch
	public Iterable<OfUnit> getMeasures() {
		return measures;
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
}
