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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.rel.Equals;

/**
 * This abstract class represents a json-serializer for the class/node
 * {@link BaseNode} It extends {@link JsonSerializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.BaseNode
 * @see com.fasterxml.jackson.databind.JsonSerializer
 */
public abstract class BaseNodeSerializer<T extends BaseNode> extends StdSerializer<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(BaseNodeSerializer.class);

	/**
	 * The json-serializer for the {@link Equals} relations of a {@link BaseNode}.
	 */
	private IterableSerializer<Equals> equalsRelationSerializer;

	/**
	 * Creates a new instance.
	 */
	public BaseNodeSerializer() {
		this(null);
		this.equalsRelationSerializer = new IterableSerializer<>(NodeFields.EQUALS.getSerial());
	}

	/**
	 * Creates a new instance.
	 */
	public BaseNodeSerializer(Class<T> t) {
		super(t);
		this.equalsRelationSerializer = new IterableSerializer<>(NodeFields.EQUALS.getSerial());
	}

	@Override
	public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		logger.debug("serializing '{}' ...", ((value == null) ? "null" : value.getClass().getSimpleName()));

		if (value == null)
			return;

		gen.writeNumberField(NodeFields.NODEID.getSerial(), value.getNodeId());
		gen.writeStringField(NodeFields.CLASS_NAME.getSerial(), value.getClass().getSimpleName());

		this.equalsRelationSerializer.serialize(value.getEq(), gen, provider);

	}

	/**
	 * Creates a field with a value in an json-object.
	 *
	 * @param g         the JsonGenerator
	 * @param fieldName the name of the field in the json-object
	 * @param value     the value of the field in the json-object
	 */
	protected void writeStringField(final JsonGenerator g, final String fieldName, final String value)
			throws JsonGenerationException, IOException {
		if (value != null) {
			g.writeStringField(fieldName, value);
		}
	}

}
