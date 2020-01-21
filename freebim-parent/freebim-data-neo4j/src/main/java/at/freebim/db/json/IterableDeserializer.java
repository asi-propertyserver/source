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
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class represents a json-deserializer for a class <b>T</b> . This class
 * can deserialize a list of elements of the type <b>T</b>.
 *
 * @author rainer.breuss@uibk.ac.at
 */
public class IterableDeserializer<T> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(IterableDeserializer.class);

	/**
	 * The representation of the class to be deserialized.
	 */
	private final Class<?> clazz;

	/**
	 * Creates a new instance with the type of the class to deserialize.
	 *
	 * @param clazz the class to deserialize
	 */
	public IterableDeserializer(Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * This method takes a json-object and returns a list of class-objects of the
	 * type @T that have been deserialized.
	 *
	 * @param node the json-object
	 * @param c    the context
	 * @see com.fasterxml.jackson.databind.JsonNode
	 * @see java.util.ArrayList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<T> deserialize(JsonNode node, DeserializationContext c)
			throws IOException, JsonProcessingException {

		logger.debug("deserialize ...");

		ArrayList<T> v = new ArrayList<T>();

		if (node != null && node.isArray()) {
			Iterator<JsonNode> elems = node.elements();
			ObjectMapper mapper = new ObjectMapper();
			while (elems.hasNext()) {

				logger.debug("deserialize ..");

				JsonNode next = elems.next();

				T entry = null;
				try {
					entry = (T) mapper.treeToValue(next, this.clazz);
				} catch (JsonParseException e) {
					logger.error("", e);
				} catch (JsonMappingException e) {
					logger.error("", e);
				} catch (IOException e) {
					logger.error("", e);
				} catch (Exception e) {
					logger.error("", e);
				}
				v.add(entry);
			}
		}

		logger.debug("deserialize.");

		return v;
	}

}
