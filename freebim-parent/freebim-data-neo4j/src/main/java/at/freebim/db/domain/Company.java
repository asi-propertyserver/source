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

import at.freebim.db.domain.base.Coded;
import at.freebim.db.domain.base.LifetimeBaseNode;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.Orderable;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.rel.CompanyCompany;
import at.freebim.db.domain.rel.WorksFor;
import at.freebim.db.json.CompanyDeserializer;
import at.freebim.db.json.CompanySerializer;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * This node represents a company. It extends {@link LifetimeBaseNode} and
 * implements {@link Coded}, {@link Named} and {@link Orderable}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.NodeEntity
 * @see at.freebim.db.domain.base.LifetimeBaseNode
 * @see at.freebim.db.domain.base.Coded
 * @see at.freebim.db.domain.base.Named
 * @see at.freebim.db.domain.base.Orderable
 */
@NodeBackup
@NodeEntity
@JsonSerialize(using = CompanySerializer.class)
@JsonDeserialize(using = CompanyDeserializer.class)
public class Company extends LifetimeBaseNode implements Coded, Named, Orderable {

	private static final long serialVersionUID = -7291119789263108625L;

	/**
	 * The code.
	 *
	 * @see at.freebim.db.domain.base.Coded
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index
	private String code;

	/**
	 * The name.
	 *
	 * @see at.freebim.db.domain.base.Named
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index
	private String name;

	/**
	 * The URL.
	 *
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index
	private String url;

	/**
	 * The logo.
	 */
	private String logo;

	/**
	 * The relation to the contributors.
	 *
	 * @see at.freebim.db.domain.Contributor
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.WORKS_FOR, direction = Relationship.INCOMING)
	private ArrayList<WorksFor> contributor;

	/**
	 * The relation between companies.
	 *
	 * @see at.freebim.db.domain.Company
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.COMPANY_COMPANY, direction = Relationship.UNDIRECTED)
	private ArrayList<CompanyCompany> company;

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.Coded#getCode()
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code the code
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * Sets the first name.
	 *
	 * @param firstName the first name
	 */
	public void setName(String firstName) {
		this.name = firstName;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param lastName the url
	 */
	public void setUrl(String lastName) {
		this.url = lastName;
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
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((logo == null) ? 0 : logo.hashCode());
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
		if (!super.equals(obj) || getClass() != obj.getClass())
			return false;

		Company other = (Company) obj;
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
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (logo == null) {
			if (other.logo != null)
				return false;
		} else if (!logo.equals(other.logo))
			return false;
		return true;
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
		if (obj == null)
			return false;
		if (!super.equalsData(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
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
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (logo == null) {
			if (other.logo != null)
				return false;
		} else if (!logo.equals(other.logo))
			return false;
		return true;
	}

	/**
	 * Get all {@link Contributor}s that are connected to that {@link Company}.
	 *
	 * @return the contributors
	 */
	public Iterable<WorksFor> getContributor() {
		return contributor;
	}

	/**
	 * Get all companies that are connected to this {@link Company}.
	 *
	 * @return the company relations
	 */
	public Iterable<CompanyCompany> getCompany() {
		return company;
	}

	/**
	 * Get the logo.
	 *
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * Set the logo.
	 *
	 * @param logo the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

}
