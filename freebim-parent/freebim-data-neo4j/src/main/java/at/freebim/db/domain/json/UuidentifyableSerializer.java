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

import at.freebim.db.domain.BsddNode;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.rel.Bsdd;
import at.freebim.db.domain.rel.References;

/**
 * This class represents a json-serializer for a class/node <b>T</b> that extends {@link UuidIdentifyable}.
 * The class itself extends {@link LifetimeBaseNodeSerializer}.
 * 
 * @see at.freebim.db.domain.base.UuidIdentifyable
 * @see at.freebim.db.domain.json.LifetimeBaseNodeSerializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class UuidentifyableSerializer<T extends UuidIdentifyable> extends LifetimeBaseNodeSerializer<T> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(UuidentifyableSerializer.class);
	
	/**
	 * The json-serializer for the relation from a {@link UuidIdentifyable} to a {@link Library}.
	 */
	private final IterableSerializer<References> refRelationSerializer;
	
	/**
	 * The json-serializer for the relation from a {@link BsddNode} to a {@link UuidIdentifyable}.
	 */
	private IterableSerializer<Bsdd> bsddRelationSerializer;

	/**
	 * Creates a new instance.
	 */
	public UuidentifyableSerializer() {
		super();
		this.refRelationSerializer = new IterableSerializer<>(NodeFields.REFERENCES.getSerial());
		this.bsddRelationSerializer = new IterableSerializer<Bsdd>(NodeFields.LOADED_BSDD.getSerial());
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.LifetimeBaseNodeSerializer#serialize(at.freebim.db.domain.base.LifetimeBaseNode, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
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
		
		writeStringField(g, NodeFields.FREEBIM_ID.getSerial(), v.getUuid());
		
		this.refRelationSerializer.serialize(v.getRef(), g, p);
		this.bsddRelationSerializer.serialize(v.getBsdd(), g, p);
		
	}
}
