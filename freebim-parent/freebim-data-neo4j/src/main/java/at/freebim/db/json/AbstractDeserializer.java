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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * This represents the abstract class for deserialization of json-objects. It
 * extends {@link JsonDeserializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see com.fasterxml.jackson.databind.deser.std.StdDeserializer
 */
public abstract class AbstractDeserializer<T> extends StdDeserializer<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7876700172256108555L;

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AbstractDeserializer.class);

	/**
	 * Base class for all json-nodes. This is the basic of the json-tree.
	 */
	protected JsonNode jn;

	/**
	 * Creates a new instance.
	 */
	public AbstractDeserializer() {
		this(null);
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param vc the type of the class for which this instance will be created for.
	 */
	public AbstractDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public T deserialize(JsonParser p, DeserializationContext c, T v) throws IOException, JsonProcessingException {

		ObjectCodec oc = p.getCodec();
		this.jn = oc.readTree(p);
		return v;
	}

	/**
	 * Takes the name of the field and returns the value of the field as
	 * {@link String}.
	 *
	 * @param fieldName the field you are searching in the json-object
	 * @returns returns the value of the field
	 */
	protected String getTextForField(final String fieldName) {
		final JsonNode node = this.jn.get(fieldName);
		if (node != null) {
			return node.textValue();
		}
		return "";
	}

	/**
	 * Takes the name of the field and returns the value of the field as
	 * {@link Long}.
	 *
	 * @param fieldName the field you are searching in the json-object
	 * @returns returns the value of the field
	 */
	protected Long getLongForField(final String fieldName) {
		if (this.jn != null && this.jn.has(fieldName)) {
			final JsonNode node = this.jn.get(fieldName);
			if (node.isLong()) {
				return Long.valueOf(node.longValue());
			} else if (node.isTextual()) {
				final String s = node.textValue();
				if (s != null && s.length() > 0) {
					try {
						final Long l = Long.parseLong(s);
						return l;
					} catch (NumberFormatException e) {
						logger.error("Can't parse Long value for field [" + fieldName + "].", e);
					}
				}
			} else if (node.isInt()) {
				return Long.valueOf(node.intValue());
			}

		}
		return null;
	}

	/**
	 * Takes the name of the field and returns the value of the field as
	 * {@link Integer}.
	 *
	 * @param fieldName the field you are searching in the json-object
	 * @returns returns the value of the field
	 */
	protected Integer getIntegerForField(final String fieldName) {
		if (this.jn != null && this.jn.has(fieldName)) {
			final JsonNode node = this.jn.get(fieldName);
			if (node.isInt()) {
				return Integer.valueOf(node.intValue());
			} else if (node.isTextual()) {
				final String s = node.textValue();
				if (s != null && s.length() > 0) {
					try {
						final Integer l = Integer.parseInt(s);
						return l;
					} catch (NumberFormatException e) {
						logger.error("Can't parse Integer value for field [" + fieldName + "].", e);
					}
				}
			} else if (node.isInt()) {
				return Integer.valueOf(node.intValue());
			}

		}
		return null;
	}

}
