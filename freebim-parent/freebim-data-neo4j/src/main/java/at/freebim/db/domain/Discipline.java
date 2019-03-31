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
import at.freebim.db.domain.base.Coded;
import at.freebim.db.domain.base.ContributedBaseNode;
import at.freebim.db.domain.base.Described;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.UUidIdentifyableVisitor;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.json.DisciplineDeserializer;
import at.freebim.db.domain.json.DisciplineSerializer;
import at.freebim.db.domain.rel.OfDiscipline;
import net.spectroom.neo4j.backup.annotation.NodeBackup;


/**
 * The node for discipline.
 * It extends {@link ContributedBaseNode} and 
 * implements {@link Coded}, {@link Named}, {@link BsddObject} and {@link Described}
 * 
 * @see at.freebim.db.domain.base.ContributedBaseNode
 * @see at.freebim.db.domain.base.Coded
 * @see at.freebim.db.domain.base.Named
 * @see at.freebim.db.domain.base.BsddObject
 * @see at.freebim.db.domain.base.Described
 * 
 * 
 * @author rainer.breuss@uibk.ac.at
 * */
@NodeBackup
@NodeEntity
@JsonSerialize(using = DisciplineSerializer.class)
@JsonDeserialize(using = DisciplineDeserializer.class)
public class Discipline extends ContributedBaseNode implements Coded, Named, Described, BsddObject {

	private static final long serialVersionUID = 2586536401560515720L;

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
	 * The bsdd guid.
	 * 
	 * @see at.freebim.db.domain.base.BsddObject
	 */
	private String bsddGuid;
	
	/**
	 * The relations to the {@link Parameter}s.
	 */
	private Iterable<OfDiscipline> parameters;

	
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.Coded#getCode()
	 */
	@Indexed
	public String getCode() {
		return code;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.Described#getDesc()
	 */
	public String getDesc() {
		return desc;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.Named#getName()
	 */
	@Indexed
	public String getName() {
		return name;
	}

	
	/**
	 * Get the name in english.
	 * 
	 * @return the name in english
	 */
	public String getNameEn() {
		return nameEn;
	}


	/**
	 * Set the code.
	 * 
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * Set the name.
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((descEn == null) ? 0 : descEn.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nameEn == null) ? 0 : nameEn.hashCode());
		result = prime * result + ((bsddGuid == null) ? 0 : bsddGuid.hashCode());
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
		Discipline other = (Discipline) obj;
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
		if (bsddGuid == null) {
			if (other.bsddGuid != null)
				return false;
		} else if (!bsddGuid.equals(other.bsddGuid))
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
		Discipline other = (Discipline) obj;
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


	/**
	 * Gets the description in english.
	 * 
	 * @return the descEn
	 */
	public String getDescEn() {
		return descEn;
	}

	/**
	 * Sets the english name,
	 * 
	 * @param nameEn the english name to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	/**
	 * Set the english description.
	 * 
	 * @param descEn the descEn to set
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}

	/**
	 * Gets the bsdd guid.
	 * 
	 * @return the bsddGuid
	 */
	@Indexed
	public String getBsddGuid() {
		return bsddGuid;
	}

	/**
	 * Sets the bsdd guid.
	 * 
	 * @param bsddGuid the bsddGuid to set
	 */
	public void setBsddGuid(String bsddGuid) {
		this.bsddGuid = bsddGuid;
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
	 * Gets the relations to the {@link Parameter}s.
	 * 
	 * @return the relations to the {@link Parameter}s
	 */
	@RelatedToVia(type = RelationType.OF_DISCIPLINE, direction=Direction.INCOMING)
	@Fetch
	public Iterable<OfDiscipline> getParameters() {
		return parameters;
	}

}
