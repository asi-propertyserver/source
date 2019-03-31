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

import at.freebim.db.domain.base.LifetimeBaseNode;
import at.freebim.db.service.DateService;

/**
 * This abstract class represents a json-serializer for a generic class/node <b>T</b>
 * that extends {@link LifetimeBaseNode}.
 * The class itself extends {@link BaseNodeSerializer}.
 * 
 * @see at.freebim.db.domain.base.LifetimeBaseNode
 * @see at.freebim.db.domain.json.BaseNodeDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 */
public abstract class LifetimeBaseNodeSerializer<T extends LifetimeBaseNode> extends BaseNodeSerializer<T> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(LifetimeBaseNodeSerializer.class);
	
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.BaseNodeSerializer#serialize(at.freebim.db.domain.base.BaseNode, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final T v, final JsonGenerator g,
			final SerializerProvider p) throws IOException,
			JsonProcessingException {
		
		logger.debug("serializing '{}' ...", ((v == null) ? "null" : v.getName()));
		
		try {
			super.serialize(v, g, p);
		} catch (Exception e) {
			logger.error("Error serializing super class: ", e);
		}
		
		if (v == null)
			return;
		
		if (v.getValidFrom() != null)
			g.writeStringField(NodeFields.VALID_FROM.getSerial(), String.valueOf(v.getValidFrom() - DateService.FREEBIM_DELTA));
		if (v.getValidTo() != null)
			g.writeStringField(NodeFields.VALID_TO.getSerial(), String.valueOf(v.getValidTo() - DateService.FREEBIM_DELTA));

	}
}
