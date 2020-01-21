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
import java.util.Arrays;
import java.util.Collection;

import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import at.freebim.db.domain.base.Coded;
import at.freebim.db.domain.base.LifetimeBaseNode;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.RoleContributor;
import at.freebim.db.domain.base.Timestampable;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.rel.Responsible;
import at.freebim.db.domain.rel.WorksFor;
import at.freebim.db.json.ContributorDeserializer;
import at.freebim.db.json.ContributorSerializer;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * This node represents a contributor. It extends {@link LifetimeBaseNode} and
 * implements {@link Coded}, {@link Named} and {@link Timestampable}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.NodeEntity
 * @see at.freebim.db.domain.base.LifetimeBaseNode
 * @see at.freebim.db.domain.base.Coded
 * @see at.freebim.db.domain.base.Named
 * @see at.freebim.db.domain.base.Timestampable
 */
@NodeBackup
@NodeEntity
@JsonSerialize(using = ContributorSerializer.class)
@JsonDeserialize(using = ContributorDeserializer.class)
public class Contributor extends LifetimeBaseNode implements Coded, Named, Timestampable {

	private static final long serialVersionUID = 8956085760004460624L;

	/**
	 * The code.
	 *
	 * @see at.freebim.db.domain.base.Coded
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index
	private String code;

	/**
	 * The first name of the {@link Contributor}.
	 */
	private String firstName;

	/**
	 * The last name of the {@link Contributor}.
	 *
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index
	private String lastName;

	/**
	 * The title of the {@link Contributor}.
	 */
	private String title;

	/**
	 * The email of the {@link Contributor}.
	 *
	 * @see org.neo4j.ogm.annotation.Index
	 */
	@Index
	private String email;

	/**
	 * The name of the company the {@link Contributor} is part of.
	 */
	private String company;

	/**
	 * The relation to which {@link Company} or companies the {@link Contributor}
	 * works for.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.WORKS_FOR, direction = Relationship.OUTGOING)
	private Iterable<WorksFor> companies;

	/**
	 * The roles.
	 */
	private RoleContributor[] roles;

	/**
	 * The relation for which {@link Library} the {@link Contributor} is
	 * responsible.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.RESPONSIBLE, direction = Relationship.OUTGOING)
	private Iterable<Responsible> responsible;

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
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName the first name to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName the last name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the email address.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email address.
	 *
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + Arrays.hashCode(roles);
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
		Contributor other = (Contributor) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (!Arrays.equals(roles, other.roles))
			return false;
		return true;
	}

	/**
	 * Get the name.
	 *
	 * @return the name
	 */
	public String getName() {
		StringBuilder b = new StringBuilder();
		if (this.title != null) {
			b.append(this.title);
			b.append(" ");
		}
		if (this.firstName != null) {
			b.append(this.firstName);
			b.append(" ");
		}
		if (this.lastName != null) {
			b.append(this.lastName);
		}
		return b.toString();
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
		Contributor other = (Contributor) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	/**
	 * Gets the roles ({@link RoleContributor}) of the {@link Contributor}.
	 *
	 * @return the roles
	 */
	public Collection<RoleContributor> getRoles() {
		if (this.roles == null)
			return new ArrayList<RoleContributor>();
		return Arrays.asList(this.roles);
	}

	/**
	 * Sets the roles of the {@link Contributor}.
	 *
	 * @param roles the roles to set
	 */
	public void setRoles(RoleContributor[] roles) {
		this.roles = roles;
	}

	/**
	 * Gets the relations to the libraries ({@link Library}) the {@link Contributor}
	 * is responsible for.
	 *
	 * @return the relation to libraries the {@link Contributor} is responsible for.
	 */
	public Iterable<Responsible> getResponsible() {
		return responsible;
	}

	/**
	 * Gets the relations to the companies ({@link Company}) the {@link Contributor}
	 * is working for
	 *
	 * @return the relation to companies the {@link Contributor} is working for.
	 */
	public Iterable<WorksFor> getCompanies() {
		return companies;
	}

	/**
	 * Get the name of the company.
	 *
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

}
