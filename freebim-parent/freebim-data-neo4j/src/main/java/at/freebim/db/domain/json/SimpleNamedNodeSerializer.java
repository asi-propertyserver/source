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

import at.freebim.db.domain.SimpleNamedNode;

/**
 * This class represents a json-serializer for the class/node {@link SimpleNamedNode}.
 * It extends {@link HierarchicalBaseNodeSerializer}. 
 * 
 * @see at.freebim.db.domain.SimpleNamedNode
 * @see at.freebim.db.domain.json.HierarchicalBaseNodeSerializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class SimpleNamedNodeSerializer extends HierarchicalBaseNodeSerializer<SimpleNamedNode> {

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.HierarchicalBaseNodeSerializer#serialize(at.freebim.db.domain.base.HierarchicalBaseNode, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final SimpleNamedNode v, final JsonGenerator g,
			final SerializerProvider p) throws IOException, JsonProcessingException {

		g.writeStartObject();
		
		super.serialize(v, g, p);
		
		writeStringField(g, NodeFields.NAME.getSerial(), v.getName());
		
		writeStringField(g, NodeFields.TYPE.getSerial(), v.getType());
		
		g.writeEndObject();
	}

}
