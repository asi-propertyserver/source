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

import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.base.Role;

/**
 * This class represents a json-serializer for the class/node {@link FreebimUser}.
 * It extends {@link LifetimeBaseNodeSerializer}.
 * 
 * @see at.freebim.db.domain.FreebimUser
 * @see at.freebim.db.domain.json.LifetimeBaseNodeDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 */
public class FreebimUserSerializer extends LifetimeBaseNodeSerializer<FreebimUser> {

	/**
	 * The json-serializer for the {@link Role}s of a {@link FreebimUser}.
	 */
	private final IterableSerializer<Role> roleSerializer;

	/**
	 * Creates a new instance.
	 */
	public FreebimUserSerializer() {
		super();
		this.roleSerializer = new IterableSerializer<>(NodeFields.ROLES.getSerial());
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.LifetimeBaseNodeSerializer#serialize(at.freebim.db.domain.base.LifetimeBaseNode, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final FreebimUser v, final JsonGenerator g,
			final SerializerProvider p) throws IOException,
			JsonProcessingException {
		
		g.writeStartObject();

		super.serialize(v, g, p);
		
		if (v == null)
			return;

	    writeStringField(g, NodeFields.USERNAME.getSerial(), v.getUsername());
		
	    writeStringField(g, NodeFields.PASSWORD.getSerial(), v.getPassword());
		
	    this.roleSerializer.serialize(v.getRoles(), g, p);
		
		if (v.getContributor() != null) {
			g.writeNumberField(NodeFields.CONTRIBUTOR_ID.getSerial(), v.getContributor().getNodeId());
		}
		
		g.writeEndObject();
	}
}
