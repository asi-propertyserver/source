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

import at.freebim.db.domain.base.HierarchicalBaseNode;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.Orderable;
import at.freebim.db.domain.base.Timestampable;
import at.freebim.db.domain.base.UUidIdentifyableVisitor;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.rel.Responsible;
import at.freebim.db.json.LibraryDeserializer;
import at.freebim.db.json.LibrarySerializer;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * A library is the root of a single hierarchy and is always a child of the
 * BigBangNode. It may be created automatically on startup (like 'Ifc4'
 * library), during automatic import (like 'ON 6240 2' and 'freeClass') or via
 * the web application user interface. This node extends
 * {@link HierarchicalBaseNode} and implements {@link Named}, {@link Orderable}
 * and {@link Timestampable}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.NodeEntity
 * @see at.freebim.db.domain.base.HierarchicalBaseNode
 * @see at.freebim.db.domain.base.Named
 * @see at.freebim.db.domain.base.Orderable
 * @see at.freebim.db.domain.base.Timestampable
 */
@NodeBackup
@NodeEntity
@JsonSerialize(using = LibrarySerializer.class)
@JsonDeserialize(using = LibraryDeserializer.class)
public class Library extends HierarchicalBaseNode implements Named, Orderable, Timestampable {

	private static final long serialVersionUID = -1023807494205664124L;

	/**
	 * The name.
	 *
	 * @see at.freebim.db.domain.base.Named
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index
	private String name;

	/**
	 * The url.
	 */
	private String url;

	/**
	 * The description.
	 */
	private String desc;

	/**
	 * The time stamp.
	 */
	private Long ts;

	/**
	 * The language code.
	 */
	private String languageCode;
	/**
	 * The relation to the {@link Contributor}s that are responsible for this
	 * {@link Library}.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.RESPONSIBLE, direction = Relationship.INCOMING)
	private ArrayList<Responsible> responsible;

	/**
	 * Create new instance.
	 */
	public Library() {
		super();
		this.languageCode = "de-AT";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.Named#getName()
	 */
	public String getName() {
		return name;
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
	 * Get the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Get the description.
	 *
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Set the description.
	 *
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see at.freebim.db.domain.base.BaseNode#equalsData(java.lang.Object)
	 */
	@Override
	public boolean equalsData(Object obj) {
		if (this == obj)
			return true;
		if (!super.equalsData(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Library other = (Library) obj;
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
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (languageCode == null) {
			if (other.languageCode != null)
				return false;
		} else if (!languageCode.equals(other.languageCode))
			return false;
		return true;
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
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((ts == null) ? 0 : ts.hashCode());
		result = prime * result + ((languageCode == null) ? 0 : languageCode.hashCode());
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
		if (obj == null)
			return false;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Library other = (Library) obj;
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
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (ts == null) {
			if (other.ts != null)
				return false;
		} else if (!ts.equals(other.ts))
			return false;
		if (languageCode == null) {
			if (other.languageCode != null)
				return false;
		} else if (!languageCode.equals(other.languageCode))
			return false;
		return true;
	}

	/**
	 * Get the relation to the {@link Contributor}s that are responsible for the
	 * {@link Library}.
	 *
	 * @return the relations to the responsible {@link Contributor}s
	 */
	public Iterable<Responsible> getResponsible() {
		return this.responsible;
	}

	/**
	 * Get the time stamp.
	 *
	 * @return the time stamp
	 */
	public Long getTs() {
		return ts;
	}

	/**
	 * Set a time stamp.
	 *
	 * @param ts the time stamp to set
	 */
	public void setTs(Long ts) {
		this.ts = ts;
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

	/**
	 * Get the language code.
	 *
	 * @return the languageCode
	 */
	public String getLanguageCode() {
		return languageCode;
	}

	/**
	 * Set the language code.
	 *
	 * @param languageCode the languageCode to set
	 */
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
}
