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

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import at.freebim.db.domain.base.LifetimeBaseNode;
import at.freebim.db.domain.base.Role;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.rel.MessageClosed;
import at.freebim.db.domain.rel.MessageSeen;
import at.freebim.db.json.MessageNodeDeserializer;
import at.freebim.db.json.MessageNodeSerializer;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * The node for a message. It extends {@link LifetimeBaseNode}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.NodeEntity
 * @see at.freebim.db.domain.base.LifetimeBaseNode
 */
@NodeBackup
@NodeEntity
@JsonSerialize(using = MessageNodeSerializer.class)
@JsonDeserialize(using = MessageNodeDeserializer.class)
public class MessageNode extends LifetimeBaseNode {

	private static final long serialVersionUID = 1482282605927864446L;

	/**
	 * The title.
	 */
	private String title;
	/**
	 * The message.
	 */
	private String message;
	/**
	 * The message in english.
	 */
	private String message_en;
	/**
	 * The type of the message.
	 */
	private MessageType type;
	/**
	 * Show the message starting this time stamp.
	 */
	private Long showFrom;
	/**
	 * Show the message until this time stamp.
	 */
	private Long showUntil;
	/**
	 * The roles.
	 */
	private Role[] roles;
	/**
	 * The relation to the {@link FreebimUser}s that have seen the message.
	 */
	@Relationship(type = RelationType.MESSAGE_SEEN, direction = Relationship.OUTGOING)
	private Iterable<MessageSeen> messageSeen;
	/**
	 * The relation to the {@link FreebimUser}s that have closed the message.
	 */
	@Relationship(type = RelationType.MESSAGE_CLOSED, direction = Relationship.OUTGOING)
	private Iterable<MessageClosed> messageClosed;

	public MessageNode() {
		super();
	}

	/**
	 * Get the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the title.
	 *
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set the message.
	 *
	 * @param name the message
	 */
	public void setMessage(String name) {
		this.message = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.Named#getName()
	 */
	public String getName() {
		return message;
	}

	/**
	 * Gets the message in english.
	 *
	 * @return the message_en
	 */
	public String getMessage_en() {
		return message_en;
	}

	/**
	 * Sets the english message.
	 *
	 * @param message_en the message_en to set
	 */
	public void setMessage_en(String message_en) {
		this.message_en = message_en;
	}

	/**
	 * Get the type of the message.
	 *
	 * @return the type of the message
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * Set the type of the message.
	 *
	 * @param type the type of the message to set
	 */
	public void setType(MessageType type) {
		this.type = type;
	}

	/**
	 * Gets the time stamp from which the message will be shown.
	 *
	 * @return the time stamp
	 */
	public Long getShowFrom() {
		return showFrom;
	}

	/**
	 * Set the time stamp from which the message will be shown.
	 *
	 * @param ts the time stamp to set
	 */
	public void setShowFrom(Long ts) {
		this.showFrom = ts;
	}

	/**
	 * Gets the time stamp to which the message will be shown.
	 *
	 * @return the time stamp
	 */
	public Long getShowUntil() {
		return showUntil;
	}

	/**
	 * Set the time stamp to which the message will be shown
	 *
	 * @param ts the time stamp
	 */
	public void setShowUntil(Long ts) {
		this.showUntil = ts;
	}

	/**
	 * Get the roles.
	 *
	 * @return the roles
	 */
	public Collection<Role> getRoles() {
		if (this.roles == null)
			return new ArrayList<Role>();
		return Arrays.asList(this.roles);
	}

	/**
	 * Set the roles.
	 *
	 * @param roles the roles to set
	 */
	public void setRoles(Role[] roles) {
		this.roles = roles;
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
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((message_en == null) ? 0 : message_en.hashCode());
		result = prime * result + ((showFrom == null) ? 0 : showFrom.hashCode());
		result = prime * result + ((showUntil == null) ? 0 : showUntil.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		MessageNode other = (MessageNode) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (message_en == null) {
			if (other.message_en != null)
				return false;
		} else if (!message_en.equals(other.message_en))
			return false;
		if (showFrom == null) {
			if (other.showFrom != null)
				return false;
		} else if (!showFrom.equals(other.showFrom))
			return false;
		if (showUntil == null) {
			if (other.showUntil != null)
				return false;
		} else if (!showUntil.equals(other.showUntil))
			return false;
		if (type != other.type)
			return false;
		if (!Arrays.equals(roles, other.roles))
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
		MessageNode other = (MessageNode) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (message_en == null) {
			if (other.message_en != null)
				return false;
		} else if (!message_en.equals(other.message_en))
			return false;
		if (showFrom == null) {
			if (other.showFrom != null)
				return false;
		} else if (!showFrom.equals(other.showFrom))
			return false;
		if (showUntil == null) {
			if (other.showUntil != null)
				return false;
		} else if (!showUntil.equals(other.showUntil))
			return false;
		if (type != other.type)
			return false;
		if (!Arrays.equals(roles, other.roles))
			return false;
		return true;
	}

	/**
	 * Get the relation to the {@link FreebimUser}s that have seen the message.
	 *
	 * @return the relation to the {@link FreebimUser}s
	 */
	public Iterable<MessageSeen> getMessageSeen() {
		return this.messageSeen;
	}

	/**
	 * Get the relation to the {@link FreebimUser}s that have closed the message.
	 *
	 * @return the relation to the {@link FreebimUser}s
	 */
	public Iterable<MessageClosed> getMessageClosed() {
		return this.messageClosed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MessageNode [");
		if (nodeId != null)
			builder.append("nodeId=").append(nodeId).append(", ");
		if (title != null)
			builder.append("title=").append(title).append(", ");
		if (message != null)
			builder.append("message=").append(message).append(", ");
		if (type != null)
			builder.append("type=").append(type).append(", ");
		if (showFrom != null)
			builder.append("showFrom=").append(showFrom).append(", ");
		if (showUntil != null)
			builder.append("showUntil=").append(showUntil).append(", ");
		if (validFrom != null)
			builder.append("validFrom=").append(validFrom).append(", ");
		if (validTo != null)
			builder.append("validTo=").append(validTo);
		builder.append("]");
		return builder.toString();
	}

}
