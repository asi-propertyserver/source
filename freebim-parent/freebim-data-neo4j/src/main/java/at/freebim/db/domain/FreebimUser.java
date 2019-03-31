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

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import at.freebim.db.domain.base.LifetimeBaseNode;
import at.freebim.db.domain.base.Role;
import at.freebim.db.domain.base.Timestampable;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.json.FreebimUserDeserializer;
import at.freebim.db.domain.json.FreebimUserSerializer;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * The node of the freebim user (The user of the system).
 * It extends {@link LifetimeBaseNode} and implements {@link Timestampable}.
 * 
 * @see at.freebim.db.domain.base.LifetimeBaseNode
 * @see at.freebim.db.domain.base.Timestampable
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@NodeBackup
@NodeEntity
@JsonSerialize(using = FreebimUserSerializer.class)
@JsonDeserialize(using = FreebimUserDeserializer.class)
public class FreebimUser extends LifetimeBaseNode implements Timestampable {

	private static final long serialVersionUID = 5765769154877302645L;

    /**
     * The user name.
     */
    private String username;
	
	/**
	 * The password.
	 */
	private String password;
	
	/**
	 * The roles.
	 */
	private Role[] roles;
	
	/**
	 * The assigned {@link Contributor}.
	 */
	private Contributor contributor;
	
	/**
	 * The id of the {@link Contributor}.
	 */
	private Long contributorId; // transient

	/**
	 * Gets the user name.
	 * 
	 * @return the user name
	 */
	@Indexed(unique=true)
	@NotNull
	@Size(min=2, max=64, message="2 ... 64")
	@Pattern(regexp="[a-zA-Z0-9\\.\\-@_]+")
	public String getUsername() {
		return username;
	}
	/**
	 * Sets the user name.
	 * 
	 * @param login the user name to set
	 */
	public void setUsername(String login) {
		this.username = login;
	}
	
	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	@NotNull
	@Size(min=7, max=32, message="3 ... 32")
	public String getPassword() {
		return password;
	}
	/**
	 * Sets the password.
	 * 
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * Get the {@link Role}s.
	 * 
	 * @return the {@link Role}s
	 */
	public Collection<Role> getRoles() {
		if (this.roles == null)
			return new ArrayList<Role>();
		return Arrays.asList(this.roles);
	}
	/**
	 * Set the {@link Role}s.
	 * 
	 * @param roles the {@link Role}s to set.
	 */
	public void setRoles(Role[] roles) {
		this.roles = roles;
	}
	
	/**
	 * Gets the assigned {@link Contributor}.
	 * 
	 * @return the {@link Contributor}
	 */
	@RelatedTo(type = RelationType.BELONGS_TO)
	public Contributor getContributor() {
		return contributor;
	}
	/**
	 * Sets the {@link Contributor}.
	 * 
	 * @param contributor the {@link Contributor} to set
	 */
	public void setContributor(Contributor contributor) {
		this.contributor = contributor;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((contributor == null) ? 0 : contributor.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + Arrays.hashCode(roles);
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		FreebimUser other = (FreebimUser) obj;
		if (contributor == null) {
			if (other.contributor != null)
				return false;
		} else if (!contributor.equals(other.contributor))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (!Arrays.equals(roles, other.roles))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	@Override
	public String getName() {
		return "";
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
		FreebimUser other = (FreebimUser) obj;
		if (contributor == null) {
			if (other.contributor != null)
				return false;
		} else if (!contributor.equals(other.contributor))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (!Arrays.equals(roles, other.roles))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	/**
	 * Gets the id of the {@link Contributor}
	 * 
	 * @return the contributorId
	 */
	@Transient
	public Long getContributorId() {
		return contributorId;
	}
	/**
	 * Sets the id of the {@link Contributor}.
	 * 
	 * @param contributorId the contributorId to set
	 */
	public void setContributorId(Long contributorId) {
		this.contributorId = contributorId;
	}

}
