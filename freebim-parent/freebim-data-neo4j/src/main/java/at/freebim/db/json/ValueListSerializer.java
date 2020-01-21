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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

import at.freebim.db.domain.Measure;
import at.freebim.db.domain.ValueList;
import at.freebim.db.domain.ValueListEntry;
import at.freebim.db.domain.rel.HasEntry;
import at.freebim.db.domain.rel.HasValue;

/**
 * This class represents a json-serializer for the class/node {@link ValueList}.
 * It extends {@link StatedBaseNodeSerializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.ValueList
 * @see StatedBaseNodeSerializer
 */
public class ValueListSerializer extends StatedBaseNodeSerializer<ValueList> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8623857595979581182L;

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ValueListSerializer.class);

	/**
	 * The json-serializer for the relations from {@link ValueList} to
	 * {@link ValueListEntry}.
	 */
	private IterableSerializer<HasEntry> entriesRelationSerializer;

	/**
	 * The json-serializer for the relations from {@link Measure} to
	 * {@link ValueList}.
	 */
	private IterableSerializer<HasValue> measureRelationSerializer;

	/**
	 * Creates a new instance.
	 */
	public ValueListSerializer() {
		this.entriesRelationSerializer = new IterableSerializer<HasEntry>(NodeFields.HAS_ENTRY.getSerial());
		this.measureRelationSerializer = new IterableSerializer<HasValue>(NodeFields.MEASURES.getSerial());
	}

	@Override
	public void serialize(final ValueList v, final JsonGenerator g, final SerializerProvider p)
			throws IOException, JsonProcessingException {

		logger.debug("serialize {}", v.getName());

		g.writeStartObject();

		super.serialize(v, g, p);

		writeStringField(g, NodeFields.NAME.getSerial(), v.getName());

		writeStringField(g, NodeFields.NAME_EN.getSerial(), v.getNameEn());

		writeStringField(g, NodeFields.BSDD_GUID.getSerial(), v.getBsddGuid());

		this.entriesRelationSerializer.serialize(v.getEntries(), g, p);

		this.measureRelationSerializer.serialize(v.getMeasures(), g, p);

		g.writeEndObject();
	}

}
