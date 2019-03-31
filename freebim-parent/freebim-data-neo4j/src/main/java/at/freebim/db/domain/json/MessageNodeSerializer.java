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
package at.freebim.db.domain.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.MessageNode;
import at.freebim.db.domain.MessageType;
import at.freebim.db.domain.base.Role;
import at.freebim.db.domain.rel.MessageSeen;
import at.freebim.db.service.DateService;

/**
 * 
 * This class represents a json-serializer for the class/node {@link MessageNode}.
 * It extends {@link LifetimeBaseNodeSerializer}.
 * 
 * @see at.freebim.db.domain.MessageNode
 * @see at.freebim.db.domain.json.LifetimeBaseNodeSerializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class MessageNodeSerializer extends LifetimeBaseNodeSerializer<MessageNode> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(MessageNodeSerializer.class);

	/**
	 * The json-serializer for the list of {@link Role}s.
	 */
	private final IterableSerializer<Role> roleSerializer;
	
	
	/**
	 * The json-serializer for the relations from {@link MessageNode} to {@link FreebimUser}.
	 * It denotes if a message has been seen by the user.
	 */
	private IterableSerializer<MessageSeen> seenRelationSerializer;

	/**
	 * Creates a new instance.
	 */
	public MessageNodeSerializer() {
		super();
		this.roleSerializer = new IterableSerializer<>(NodeFields.ROLES.getSerial());
		this.seenRelationSerializer = new IterableSerializer<MessageSeen>(NodeFields.USERNAME.getSerial());
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.LifetimeBaseNodeSerializer#serialize(at.freebim.db.domain.base.LifetimeBaseNode, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final MessageNode v, final JsonGenerator g,
			final SerializerProvider p) throws IOException,
			JsonProcessingException {
		
		logger.debug("serializing '{}' ...", v);
		
		if (v == null)
			return;

		g.writeStartObject();
		
		try {
			super.serialize(v, g, p);
		} catch (Exception e) {
			logger.error("Error serializing super class: ", e);
		}
		
		this.writeStringField(g, NodeFields.TITLE.getSerial(), v.getTitle());
		this.writeStringField(g, NodeFields.NAME.getSerial(), v.getMessage());
		this.writeStringField(g, NodeFields.NAME_EN.getSerial(), v.getMessage_en());
		
		MessageType type = v.getType();
		type = ((type == null) ? MessageType.INFO : type);
		this.writeStringField(g, NodeFields.TYPE.getSerial(), type.name());
		
		if (v.getShowFrom() != null)
			g.writeStringField(NodeFields.SHOW_FROM.getSerial(), String.valueOf(v.getShowFrom() - DateService.FREEBIM_DELTA));
		if (v.getShowUntil() != null)
			g.writeStringField(NodeFields.SHOW_UNTIL.getSerial(), String.valueOf(v.getShowUntil() - DateService.FREEBIM_DELTA));
		
		this.roleSerializer.serialize(v.getRoles(), g, p);
		this.seenRelationSerializer.serialize(v.getMessageSeen(), g, p);
		
		g.writeEndObject();

	}
}
