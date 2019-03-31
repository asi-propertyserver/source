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
import at.freebim.db.domain.base.Timestampable;
import at.freebim.db.domain.base.UUidIdentifyableVisitor;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.json.DocumentDeserializer;
import at.freebim.db.domain.json.DocumentSerializer;
import at.freebim.db.domain.rel.DocumentedIn;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * This node represents a document.
 * It extends {@link UuidIdentifyable} and
 * implements {@link Named}, {@link Described}, {@link Timestampable} and {@link BsddObject}.
 * 
 * @see at.freebim.db.domain.base.UuidIdentifyable
 * @see at.freebim.db.domain.base.Named
 * @see at.freebim.db.domain.base.Described
 * @see at.freebim.db.domain.base.Timestampable
 * @see at.freebim.db.domain.base.BsddObject
 * 
 * @author rainer.breuss@uibk.ac.at
 * */
@NodeBackup
@NodeEntity
@JsonSerialize(using = DocumentSerializer.class)
@JsonDeserialize(using = DocumentDeserializer.class)
public class Document extends UuidIdentifyable implements Named, Described, Timestampable, BsddObject {

	private static final long serialVersionUID = -7486439704418091110L;

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
	 * The description in english.
	 */
	private String descEn;
	
	/**
	 * The bsdd guid.
	 * 
	 * @see at.freebim.db.domain.base.BsddObject
	 */
	private String bsddGuid;
	
	
	/**
	 * The relation to the {@link ContributedBaseNode} in which it is documented in.
	 */
	private Iterable<DocumentedIn> documentedNodes;

	
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.Named#getName()
	 */
	@Indexed
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.Described#getDesc()
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Get the english description.
	 * 
	 * @return the descEn
	 */
	public String getDescEn() {
		return descEn;
	}
	
	/**
	 * The bsdd-guid.
	 * 
	 * @return the bsddGuid
	 */
	@Indexed
	public String getBsddGuid() {
		return bsddGuid;
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
	 * Set the english description.
	 * 
	 * @param descEn the descEn to set
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.BsddObject#setBsddGuid(java.lang.String)
	 */
	public void setBsddGuid(String bsddGuid) {
		this.bsddGuid = bsddGuid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((descEn == null) ? 0 : descEn.hashCode());
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
		Document other = (Document) obj;
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
		} else if (!name.equalsIgnoreCase(other.name))
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
		Document other = (Document) obj;
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
		return true;
	}

	/**
	 * Gets the relations to the {@link ContributedBaseNode}s in which it is documented.
	 * 
	 * @return the relation to the {@link ContributedBaseNode}s
	 */
	@RelatedToVia(type = RelationType.DOCUMENTED_IN, direction=Direction.INCOMING)
	@Fetch
	public Iterable<DocumentedIn> getDocumentedNodes() {
		return documentedNodes;
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
