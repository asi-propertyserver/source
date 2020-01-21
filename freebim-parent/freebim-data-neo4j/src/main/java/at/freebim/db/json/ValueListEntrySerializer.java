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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

import at.freebim.db.domain.ValueList;
import at.freebim.db.domain.ValueListEntry;
import at.freebim.db.domain.rel.HasEntry;

/**
 * This class represents a json-serializer for the class/node
 * {@link ValueListEntry}. It extends {@link StatedBaseNodeSerializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.ValueListEntry
 * @see StatedBaseNodeSerializer
 */
public class ValueListEntrySerializer extends StatedBaseNodeSerializer<ValueListEntry> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1594239714917568227L;
	/**
	 * The json-serializer for the relation from {@link ValueList} to
	 * {@link ValueListEntry}.
	 */
	private IterableSerializer<HasEntry> listRelationSerializer;

	/**
	 * Creates a new instance.
	 */
	public ValueListEntrySerializer() {
		this.listRelationSerializer = new IterableSerializer<HasEntry>(NodeFields.LISTS.getSerial());
	}

	@Override
	public void serialize(final ValueListEntry v, final JsonGenerator g, final SerializerProvider p)
			throws IOException, JsonProcessingException {

		g.writeStartObject();

		super.serialize(v, g, p);

		writeStringField(g, NodeFields.NAME.getSerial(), v.getName());

		writeStringField(g, NodeFields.NAME_EN.getSerial(), v.getNameEn());

		writeStringField(g, NodeFields.DESC.getSerial(), v.getDesc());

		writeStringField(g, NodeFields.DESC_EN.getSerial(), v.getDescEn());

		writeStringField(g, NodeFields.COMMENT.getSerial(), v.getComment());

		writeStringField(g, NodeFields.BSDD_GUID.getSerial(), v.getBsddGuid());

		this.listRelationSerializer.serialize(v.getLists(), g, p);

		g.writeEndObject();
	}

}
