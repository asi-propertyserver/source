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

import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.ParameterSet;
import at.freebim.db.domain.base.HierarchicalBaseNode;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.domain.rel.ContainsParameter;
import at.freebim.db.domain.rel.HasParameterSet;

/**
 * This class represents a json-serializer for the class/node {@link ParameterSet}.
 * It extends {@link ParameterizedSerializer}.
 * 
 * @see at.freebim.db.domain.ParameterSet
 * @see at.freebim.db.domain.json.ParameterizedSerializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class ParameterSetSerializer extends ParameterizedSerializer<ParameterSet> {

	/**
	 * The json-serializer for the relations from a {@link StatedBaseNode} to the node {@link Parameter}.
	 */
	private final IterableSerializer<ContainsParameter> parameterRelationSerializer;
	
	/**
	 * The json-serializer for the relations from a {@link HierarchicalBaseNode} to a {@link ParameterSet}.
	 */
	private final IterableSerializer<HasParameterSet> ownerRelationSerializer;
	
	/**
	 * Creates a new instance.
	 */
	public ParameterSetSerializer() {
		super();
		this.parameterRelationSerializer = new IterableSerializer<ContainsParameter>(NodeFields.PARAMS.getSerial());
		this.ownerRelationSerializer = new IterableSerializer<HasParameterSet>(NodeFields.OWNERS.getSerial());
	}
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.StatedBaseNodeSerializer#serialize(at.freebim.db.domain.base.StatedBaseNode, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final ParameterSet v, final JsonGenerator g, final SerializerProvider p)
			throws IOException, JsonProcessingException {

		g.writeStartObject();
		
		super.serialize(v, g, p);
		
		writeStringField(g, NodeFields.NAME.getSerial(), v.getName());
		
		writeStringField(g, NodeFields.NAME_EN.getSerial(), v.getNameEn());
		
		writeStringField(g, NodeFields.BSDD_GUID.getSerial(), v.getBsddGuid());
		
		g.writeNumberField(NodeFields.TYPE.getSerial(), v.getType().getCode());
		
		this.parameterRelationSerializer.serialize(v.getParameters(), g, p);

		this.ownerRelationSerializer.serialize(v.getOwners(), g, p);
		
		g.writeEndObject();
	}

}
