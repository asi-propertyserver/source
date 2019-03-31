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

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.rel.Equals;

/**
 * This abstract class represents a json-serializer for the class/node {@link BaseNode}
 * It extends {@link JsonSerializer}.
 * 
 * @see at.freebim.db.domain.base.BaseNode
 * @see org.codehaus.jackson.map.JsonSerializer
 * 
 * @author rainer.breuss@uibk.ac.at
 */
public abstract class BaseNodeSerializer<T extends BaseNode> extends JsonSerializer<T> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(BaseNodeSerializer.class);
	
	/**
	 * The json-serializer for the {@link Equals} relations of a {@link BaseNode}.
	 */
	private final IterableSerializer<Equals> equalsRelationSerializer;

	/**
	 * Creates a new instance.
	 */
	public BaseNodeSerializer() {
		super();
		this.equalsRelationSerializer = new IterableSerializer<>(NodeFields.EQUALS.getSerial());
	}
 
	
	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final T v, final JsonGenerator g,
			final SerializerProvider p) throws IOException,
			JsonProcessingException {
		
		logger.debug("serializing '{}' ...", ((v == null) ? "null" : v.getClass().getSimpleName()));
		
		if (v == null)
			return;
		
		g.writeNumberField(NodeFields.NODEID.getSerial(), v.getNodeId());
		g.writeStringField(NodeFields.CLASS_NAME.getSerial(), v.getClass().getSimpleName());
		
		this.equalsRelationSerializer.serialize(v.getEq(), g, p);
	}
	
	/**
	 * 
	 * Creates a field with a value in an json-object.
	 * 
	 * @param g the JsonGenerator
	 * @param fieldName the name of the field in the json-object
	 * @param value the value of the field in the json-object
	 * */
	protected void writeStringField(final JsonGenerator g, final String fieldName, final String value) throws JsonGenerationException, IOException {
		if (value != null) {
			g.writeStringField(fieldName, value);
		}
	}

}
