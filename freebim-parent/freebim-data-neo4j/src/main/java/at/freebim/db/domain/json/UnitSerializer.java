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

import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Unit;
import at.freebim.db.domain.rel.OfUnit;
import at.freebim.db.domain.rel.UnitConversion;

/**
 * This class represents a json-serializer for the class/node {@link Unit}.
 * It extends {@link ContributedBaseNodeSerializer}.
 * 
 * @see at.freebim.db.domain.Unit
 * @see at.freebim.db.domain.json.ContributedBaseNodeSerializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class UnitSerializer extends ContributedBaseNodeSerializer<Unit> {

	/**
	 * The json-serializer for the relations from a {@link Unit} to a {@link Unit}.
	 * This relation denotes a conversion.
	 */
	private final IterableSerializer<UnitConversion> conversionsSerializer;
	
	/**
	 * The json-serializer for the relations from {@link Measure} to {@link Unit}.
	 */
	private final IterableSerializer<OfUnit> measuresSerializer;
	
	

	/**
	 * Creates a new instance.
	 */
	public UnitSerializer() {
		super();
		this.conversionsSerializer = new IterableSerializer<UnitConversion>(NodeFields.UNIT_CONVERSION.getSerial());
		this.measuresSerializer = new IterableSerializer<OfUnit>(NodeFields.MEASURES.getSerial());
	}



	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.ContributedBaseNodeSerializer#serialize(at.freebim.db.domain.base.ContributedBaseNode, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final Unit v, final JsonGenerator g, final SerializerProvider p)
			throws IOException, JsonProcessingException {

		g.writeStartObject();
		
		super.serialize(v, g, p);
		
		writeStringField(g, NodeFields.NAME.getSerial(), v.getName());
		
		writeStringField(g, NodeFields.NAME_EN.getSerial(), v.getNameEn());
		
		writeStringField(g, NodeFields.DESC.getSerial(), v.getDesc());

		writeStringField(g, NodeFields.DESC_EN.getSerial(), v.getDescEn());

		writeStringField(g, NodeFields.CODE.getSerial(), v.getUnitCode());

		writeStringField(g, NodeFields.BSDD_GUID.getSerial(), v.getBsddGuid());
		
		this.conversionsSerializer.serialize(v.getConversions(), g, p);
		
		this.measuresSerializer.serialize(v.getMeasures(), g, p);
		
		g.writeEndObject();
	}

	
}
