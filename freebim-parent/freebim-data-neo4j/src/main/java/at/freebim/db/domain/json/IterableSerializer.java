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
import java.util.Iterator;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * This class represents a json-serializer for a class <b>T</b> .
 * This class can serialize a list of elements of the type <b>T</b>. 
 * 
 * @author rainer.breuss@uibk.ac.at
 */
public class IterableSerializer<T> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(IterableSerializer.class);

	/**
	 * The name of the json-field for the serialized array/iterable.
	 */
	private final String fieldName;
	
	/**
	 * Creates a new instance with the name of the json-field in which the {@link Iterable} will be serialized.
	 * 
	 * @param fieldName the name of the json-field
	 */
	public IterableSerializer(final String fieldName) {
		super();
		this.fieldName = fieldName;
	}

	/**
	 * This method takes a @Iterable of classes of the type @T and creates an array of json-objects
	 * 
	 * @param v the iterable
	 * @param g the json generator
	 * @param p the serializer provider
	 * 
	 * */
	public void serialize(final Iterable<T> v, final JsonGenerator g,
			final SerializerProvider p) throws IOException,
			JsonProcessingException {
		
		logger.debug("serializing field '{}' of type '{}' ...", this.fieldName, ((v == null) ? "null" : v.getClass().getSimpleName()));
		
		if (v == null)
			return;
		
		final Iterator<T> iter = v.iterator();
		if (iter != null && iter.hasNext()) {
			
			g.writeFieldName(this.fieldName);
			g.writeStartArray();

			while (iter.hasNext()) {
				final T rel = iter.next();
				if (rel != null) {
					try {
						g.writeObject(rel);
					} catch (Exception e) {
						logger.error("Error serializing relation: fieldname=" + this.fieldName, e);
					}
				}
			}

			g.writeEndArray();

		}
	}

}
