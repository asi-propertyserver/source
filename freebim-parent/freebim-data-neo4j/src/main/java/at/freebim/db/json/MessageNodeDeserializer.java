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
package at.freebim.db.json;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import at.freebim.db.domain.MessageNode;
import at.freebim.db.domain.MessageType;
import at.freebim.db.domain.base.Role;
import at.freebim.db.service.DateService;

/**
 * This class represents a json-deserializer for the class/node
 * {@link MessageNode}. It extends {@link LifetimeBaseNodeDeserializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.MessageNode
 * @see LifetimeBaseNodeDeserializer
 */
public class MessageNodeDeserializer extends LifetimeBaseNodeDeserializer<MessageNode> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5118109612253800820L;

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(MessageNodeDeserializer.class);

	/**
	 * The json-deserializer for the list of {@link Role}s.
	 */
	private final IterableDeserializer<Role> roleDeserializer;

	/**
	 * Creates a new instance.
	 */
	public MessageNodeDeserializer() {
		this.roleDeserializer = new IterableDeserializer<Role>(Role.class);
	}

	@Override
	public MessageNode deserialize(JsonParser p, DeserializationContext c) throws IOException, JsonProcessingException {

		MessageNode v = new MessageNode();
		v = super.deserialize(p, c, v);

		v.setTitle(this.getTextForField(NodeFields.TITLE.getSerial()));
		v.setMessage(this.getTextForField(NodeFields.NAME.getSerial()));
		v.setMessage_en(this.getTextForField(NodeFields.NAME_EN.getSerial()));

		String s = this.getTextForField(NodeFields.TYPE.getSerial());
		MessageType type = ((s == null) ? MessageType.INFO : MessageType.valueOf(s));
		type = ((type == null) ? MessageType.INFO : type);
		v.setType(type);

		Long l = this.getLongForField(NodeFields.SHOW_FROM.getSerial());
		if (l != null) {
			v.setShowFrom(l + DateService.FREEBIM_DELTA);
		}
		l = this.getLongForField(NodeFields.SHOW_UNTIL.getSerial());
		if (l != null) {
			v.setShowUntil(l + DateService.FREEBIM_DELTA);
		}

		ArrayList<Role> roleList = this.roleDeserializer.deserialize(this.jn.get(NodeFields.ROLES.getSerial()), c);
		Role[] roles = new Role[roleList.size()];
		roleList.toArray(roles);
		v.setRoles(roles);

		if (logger.isDebugEnabled())
			logger.debug("MessageNode deserialized: [{}]", v);

		return v;
	}
}
