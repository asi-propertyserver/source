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

import at.freebim.db.domain.Discipline;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.rel.OfDiscipline;

/**
 * This class represents a json-serializer for the class/node {@link Discipline}.
 * It extends {@link ContributedBaseNodeSerializer}.
 * 
 * @see at.freebim.db.domain.Discipline
 * @see at.freebim.db.domain.json.ContributedBaseNodeDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 */
public class DisciplineSerializer extends ContributedBaseNodeSerializer<Discipline> {

	/**
	 * The json-serializer for the relation from a {@link Parameter} to a {@link Discipline}.
	 */
	private IterableSerializer<OfDiscipline> parameterRelationSerializer;

	/**
	 * Creates a new instance.
	 */
	public DisciplineSerializer() {
		super();
		this.parameterRelationSerializer = new IterableSerializer<OfDiscipline>(NodeFields.PARAMETER.getSerial());
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.ContributedBaseNodeSerializer#serialize(at.freebim.db.domain.base.ContributedBaseNode, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final Discipline v, final JsonGenerator g, final SerializerProvider p)
			throws IOException, JsonProcessingException {
		
		g.writeStartObject();
		
		super.serialize(v, g, p);
		
		writeStringField(g, NodeFields.CODE.getSerial(), v.getCode());

		writeStringField(g, NodeFields.NAME.getSerial(), v.getName());
		
		writeStringField(g, NodeFields.DESC.getSerial(), v.getDesc());

		writeStringField(g, NodeFields.DESC_EN.getSerial(), v.getDescEn());

		writeStringField(g, NodeFields.BSDD_GUID.getSerial(), v.getBsddGuid());
		
		this.parameterRelationSerializer.serialize(v.getParameters(), g, p);
		
		g.writeEndObject();
	}

}
