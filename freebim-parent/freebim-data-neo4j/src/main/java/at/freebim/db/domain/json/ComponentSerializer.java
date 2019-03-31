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

import at.freebim.db.domain.Component;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.base.ComponentType;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.domain.rel.ComponentComponent;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.OfMaterial;
import at.freebim.db.domain.rel.ValueOverride;


/**
 * This class represents a json-serializer for the class/node {@link Component}.
 * It extends {@link HierarchicalBaseNodeSerializer}.
 * 
 * @see at.freebim.db.domain.Component
 * @see at.freebim.db.domain.json.HierarchicalBaseNodeSerializer
 * 
 * @author rainer.breuss@uibk.ac.at
 * */
public class ComponentSerializer extends HierarchicalBaseNodeSerializer<Component> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ComponentSerializer.class);
	
	/**
	 * The json-serializer for the relations from a {@link StatedBaseNode} to a {@link Parameter}.
	 */
	private IterableSerializer<HasParameter> parameterRelationSerializer;
	
	/**
	 * The json-serializer for the relations between {@link Component}s.
	 */
	private IterableSerializer<ComponentComponent> partsRelationSerializer;
	
	/**
	 * The json-serializer for the relations from a {@link Component} 
	 * to a {@link Component} that is a material.
	 */
	private IterableSerializer<OfMaterial> materialRelationSerializer;
	
	/**
	 * The json-serializer for the relations
	 */
	private IterableSerializer<ValueOverride> valueOverrideRelationSerializer;

	/**
	 * Creates a new instance.
	 */
	public ComponentSerializer() {
		super();
		this.parameterRelationSerializer = new IterableSerializer<HasParameter>(NodeFields.HAS_PARAMETER.getSerial());
		this.partsRelationSerializer = new IterableSerializer<ComponentComponent>(NodeFields.PARTS.getSerial());
		this.materialRelationSerializer = new IterableSerializer<OfMaterial>(NodeFields.MATERIAL.getSerial());
		this.valueOverrideRelationSerializer = new IterableSerializer<ValueOverride>(NodeFields.OVERRIDE.getSerial());
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.HierarchicalBaseNodeSerializer#serialize(at.freebim.db.domain.base.HierarchicalBaseNode, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final Component v, final JsonGenerator g, final SerializerProvider p)
			throws IOException, JsonProcessingException {
		
		logger.debug("serializing '{}' ...", ((v == null) ? "null" : v.getName()));

		if (v == null)
			return;
				
		g.writeStartObject();
		
		try {
			super.serialize(v, g, p);
		} catch (Exception e) {
			logger.error("Error serializing super class: ", e);
		}
		
		writeStringField(g, NodeFields.CODE.getSerial(), v.getCode());

		writeStringField(g, NodeFields.NAME.getSerial(), v.getName());

		writeStringField(g, NodeFields.NAME_EN.getSerial(), v.getNameEn());

		writeStringField(g, NodeFields.DESC.getSerial(), v.getDesc());

		writeStringField(g, NodeFields.DESC_EN.getSerial(), v.getDescEn());

		writeStringField(g, NodeFields.BSDD_GUID.getSerial(), v.getBsddGuid());

		if (v.getType() != null) {
			g.writeNumberField(NodeFields.TYPE.getSerial(), ((v.getType() == null) ? ComponentType.UNDEFINED.getCode() : v.getType().getCode()) );
		}

		this.parameterRelationSerializer.serialize(v.getParameter(), g, p);

		this.partsRelationSerializer.serialize(v.getParts(), g, p);
		
		this.materialRelationSerializer.serialize(v.getMaterial(), g, p);
		
		this.valueOverrideRelationSerializer.serialize(v.getValueOverride(), g, p);
		
		g.writeBooleanField(NodeFields.IS_MATERIAL.getSerial(), v.isM());

		g.writeEndObject();
		
		logger.debug("finished.");

	}

}
