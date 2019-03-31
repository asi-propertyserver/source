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
import at.freebim.db.domain.base.Described;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.Orderable;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.domain.base.UUidIdentifyableVisitor;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.json.ValueListEntryDeserializer;
import at.freebim.db.domain.json.ValueListEntrySerializer;
import at.freebim.db.domain.rel.HasEntry;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * The node for an entry of the {@link ValueList}.
 * It extends {@link StatedBaseNode} and 
 * implements {@link Orderable}, {@link Named}, {@link Described}, {@link Coded} and {@link BsddObject}.
 * 
 * @see at.freebim.db.domain.base.StatedBaseNode
 * @see at.freebim.db.domain.base.Orderable
 * @see at.freebim.db.domain.base.Named
 * @see at.freebim.db.domain.base.Described
 * @see at.freebim.db.domain.base.Coded
 * @see at.freebim.db.domain.base.BsddObject
 * 
 * @author rainer.breuss@uibk.ac.at
 * */
@NodeBackup
@NodeEntity
@JsonSerialize(using = ValueListEntrySerializer.class)
@JsonDeserialize(using = ValueListEntryDeserializer.class)
public class ValueListEntry extends StatedBaseNode implements Orderable, Named, Described, Coded, BsddObject {

	private static final long serialVersionUID = -8108032396592965477L;

	/**
	 * The name.
	 * 
	 * @see at.freebim.db.domain.base.Named
	 */
	private String name;
	
	/**
	 * The description.
	 * 
	 * @see at.freebim.db.domain.base.Described
	 */
	private String desc;
	
	/**
	 * The comment.
	 * 
	 * @see at.freebim.db.domain.base.Coded
	 */
	private String comment;
	
	
	/**
	 * The english name.
	 */
	private String nameEn;

	/**
	 * The description in english.
	 */
	private String descEn;
	
	/**
	 * The bsdd-guid.
	 * 
	 * @see at.freebim.db.domain.base.BsddObject
	 */
	protected String bsddGuid;

	
	/**
	 * The relation from a {@link ValueList} to a {@link ValueListEntry}.
	 */
	private Iterable<HasEntry> lists;
	

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.Described#getDesc()
	 */
	@Indexed
	public String getDesc() {
		return desc;
	}

	/**
	 * Get the comment.
	 * 
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.Named#getName()
	 */
	@Override
	@Indexed
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.Coded#getCode()
	 */
	@Override
	public String getCode() {
		return this.comment;
	}
	
	/**
	 * @return the nameEn
	 */
	public String getNameEn() {
		return nameEn;
	}

	public String getDescEn() {
		return this.descEn;
	}

	/**
	 * @return the lists
	 */
	@RelatedToVia(type=RelationType.HAS_ENTRY, direction=Direction.INCOMING)
	@Fetch
	public Iterable<HasEntry> getLists() {
		return lists;
	}

	/**
	 * Set the name.
	 * 
	 * @param value the name to set
	 */
	public void setName(String value) {
		this.name = value;
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
	 * Set the comment.
	 * 
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @param en_name the nameEn to set
	 */
	public void setNameEn(String en_name) {
		this.nameEn = en_name;
	}

	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((descEn == null) ? 0 : descEn.hashCode());
		result = prime * result + ((nameEn == null) ? 0 : nameEn.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ValueListEntry other = (ValueListEntry) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (bsddGuid == null) {
			if (other.bsddGuid != null)
				return false;
		} else if (!bsddGuid.equals(other.bsddGuid))
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
		ValueListEntry other = (ValueListEntry) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.BsddObject#getBsddGuid()
	 */
	@Indexed
	public String getBsddGuid() {
		return bsddGuid;
	}


	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.BsddObject#setBsddGuid(java.lang.String)
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
}
