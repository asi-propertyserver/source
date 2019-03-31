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
import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.base.ParameterType;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.domain.rel.ContainsParameter;
import at.freebim.db.domain.rel.HasMeasure;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.OfDiscipline;

/**
 * This class represents a json-serializer for the class/node {@link Parameter}.
 * It extends {@link StatedBaseNodeSerializer}.
 * 
 * @see at.freebim.db.domain.Parameter
 * @see at.freebim.db.domain.json.StatedBaseNodeSerializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class ParameterSerializer extends StatedBaseNodeSerializer<Parameter> {

	/**
	 * The json-serializer for the relations from {@link Parameter} to {@link Discipline}.
	 */
	private IterableSerializer<OfDiscipline> disciplineRelationSerializer;
	
	/**
	 * The json-serializer for the relations from {@link Parameter} to {@link Measure}.
	 */
	private IterableSerializer<HasMeasure> measureRelationSerializer;
	
	/**
	 * The json-serializer for the relations from {@link StatedBaseNode} to {@link Parameter}.
	 */
	private IterableSerializer<HasParameter> componentRelationSerializer;
	
	/**
	 * The json-serializer for the relations from {@link StatedBaseNode} to {@link Parameter}.
	 */
	private IterableSerializer<ContainsParameter> psetRelationSerializer;

	/**
	 * Creates a new instance.
	 */
	public ParameterSerializer() {
		super();
		this.disciplineRelationSerializer = new IterableSerializer<OfDiscipline>(NodeFields.DISCIPLINE.getSerial());
		this.measureRelationSerializer = new IterableSerializer<HasMeasure>(NodeFields.MEASURES.getSerial());
		this.componentRelationSerializer = new IterableSerializer<HasParameter>(NodeFields.HP.getSerial());
		this.psetRelationSerializer = new IterableSerializer<ContainsParameter>(NodeFields.PSETS.getSerial());
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.StatedBaseNodeSerializer#serialize(at.freebim.db.domain.base.StatedBaseNode, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final Parameter v, final JsonGenerator g, final SerializerProvider p)
			throws IOException, JsonProcessingException {

		g.writeStartObject();
		
		super.serialize(v, g, p);
		
		writeStringField(g, NodeFields.CODE.getSerial(), v.getCode());
		
		writeStringField(g, NodeFields.NAME.getSerial(), v.getName());
		
		writeStringField(g, NodeFields.NAME_EN.getSerial(), v.getNameEn());
		
		writeStringField(g, NodeFields.DESC.getSerial(), v.getDesc());

		writeStringField(g, NodeFields.DESC_EN.getSerial(), v.getDescEn());

		writeStringField(g, NodeFields.DEFAULT.getSerial(), v.getDefaultString());
		
		g.writeNumberField(NodeFields.PARAM_TYPE.getSerial(), ((v.getPtype() == null) ? ParameterType.UNDEFINED.getCode() : v.getPtype().getCode()) );
				
		writeStringField(g, NodeFields.BSDD_GUID.getSerial(), v.getBsddGuid());
		
		this.disciplineRelationSerializer.serialize(v.getDiscipline(), g, p);

		this.measureRelationSerializer.serialize(v.getMeasures(), g, p);
		
		this.componentRelationSerializer.serialize(v.getComponents(), g, p);
		
		this.psetRelationSerializer.serialize(v.getPsets(), g, p);

		g.writeEndObject();
	}

}
