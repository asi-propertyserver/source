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
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

import at.freebim.db.domain.base.BsddObject;
import at.freebim.db.domain.base.Coded;
import at.freebim.db.domain.base.ContributedBaseNode;
import at.freebim.db.domain.base.Described;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.UUidIdentifyableVisitor;
import at.freebim.db.domain.json.PhaseDeserializer;
import at.freebim.db.domain.json.PhaseSerializer;
import net.spectroom.neo4j.backup.annotation.NodeBackup;


/**
 * The node for the phase.
 * It extends {@link ContributedBaseNode} and 
 * implements {@link Coded}, {@link Named}, {@link Described} and {@link BsddObject}.
 * 
 * @see at.freebim.db.domain.base.ContributedBaseNode
 * @see at.freebim.db.domain.base.Coded
 * @see at.freebim.db.domain.base.Named
 * @see at.freebim.db.domain.base.Described
 * @see at.freebim.db.domain.base.BsddObject
 * 
 * 
 * @author rainer.breuss@uibk.ac.at
 * */
@NodeBackup
@NodeEntity
@JsonSerialize(using = PhaseSerializer.class)
@JsonDeserialize(using = PhaseDeserializer.class)
public class Phase extends ContributedBaseNode implements Coded, Named, Described, BsddObject {

	private static final long serialVersionUID = 5728609484139126008L;
	
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
	 * The color in the hex-format.
	 */
	private String hexColor;

	/**
	 * The bsdd guid.
	 * 
	 * @see at.freebim.db.domain.base.BsddObject
	 */
	private String bsddGuid;

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

	/**
	 * Get the color in the hex-format.
	 * 
	 * @return the color
	 */
	public String getHexColor() {
		return hexColor;
	}

	/**
	 * Set the color in the hex-format.
	 * 
	 * @param code the color to set
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * Set the description.
	 * 
	 * @param desc the description to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * Set the color in the hex-format.
	 * 
	 * @param hexColor the color to set
	 */
	public void setHexColor(String hexColor) {
		this.hexColor = hexColor;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.Named#getName()
	 */
	@Override
	@Indexed
	public String getName() {
		return this.name;
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
	 * Set the english name.
	 * 
	 * @param descEn the english name to set
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
		Phase other = (Phase) obj;
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
		if (descEn == null) {
			if (other.descEn != null)
				return false;
		} else if (!descEn.equals(other.descEn))
			return false;
		if (hexColor == null) {
			if (other.hexColor != null)
				return false;
		} else if (!hexColor.equals(other.hexColor))
			return false;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		return true;
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
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((descEn == null) ? 0 : descEn.hashCode());
		result = prime * result
				+ ((hexColor == null) ? 0 : hexColor.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nameEn == null) ? 0 : nameEn.hashCode());
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
		Phase other = (Phase) obj;
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
		if (hexColor == null) {
			if (other.hexColor != null)
				return false;
		} else if (!hexColor.equals(other.hexColor))
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
	 * @see at.freebim.db.domain.base.UUidIdentifyableVistitable#accept(at.freebim.db.domain.base.UUidIdentifyableVisitor)
	 */
	@Override
	public void accept(UUidIdentifyableVisitor visitor) {
		if (visitor != null)
			visitor.visit(this);
	}

	/**
	 * Get the english name
	 * 
	 * @return the english name
	 */
	public String getNameEn() {
		return nameEn;
	}

	/**
	 * Set the english name
	 * 
	 * @param nameEn the english name to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	
}
