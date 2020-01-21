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

import at.freebim.db.domain.DataType;
import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.Unit;
import at.freebim.db.domain.ValueList;
import at.freebim.db.domain.rel.HasMeasure;
import at.freebim.db.domain.rel.HasValue;
import at.freebim.db.domain.rel.OfDataType;
import at.freebim.db.domain.rel.OfUnit;

/**
 * This class represents a json-serializer for the class/node {@link Measure}.
 * It extends {@link ContributedBaseNodeSerializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Measure
 * @see ContributedBaseNodeSerializer
 */
public class MeasureSerializer extends ContributedBaseNodeSerializer<Measure> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8348982409257699438L;

	/**
	 * The json-serializer for the relations from {@link Measure} to
	 * {@link DataType}.
	 */
	private IterableSerializer<OfDataType> dataTypeRelationSerializer;

	/**
	 * The json-serializer for the relations from {@link Measure} to {@link Unit}.
	 */
	private IterableSerializer<OfUnit> unitRelationSerializer;

	/**
	 * The json-serializer for the relations from {@link Measure} to
	 * {@link ValueList}.
	 */
	private IterableSerializer<HasValue> valueRelationSerializer;

	/**
	 * The json-serializer for the relations from {@link Parameter} to
	 * {@link Measure}.
	 */
	private IterableSerializer<HasMeasure> paramRelationSerializer;

	/**
	 * Creates a new instance.
	 */
	public MeasureSerializer() {
		this.paramRelationSerializer = new IterableSerializer<HasMeasure>(NodeFields.PARAMS.getSerial());
		this.dataTypeRelationSerializer = new IterableSerializer<OfDataType>(NodeFields.DATATYPE.getSerial());
		this.unitRelationSerializer = new IterableSerializer<OfUnit>(NodeFields.UNIT.getSerial());
		this.valueRelationSerializer = new IterableSerializer<HasValue>(NodeFields.HAS_VALUE.getSerial());
	}

	@Override
	public void serialize(final Measure v, final JsonGenerator g, final SerializerProvider p)
			throws IOException, JsonProcessingException {

		g.writeStartObject();

		super.serialize(v, g, p);

		writeStringField(g, NodeFields.NAME.getSerial(), v.getName());

		writeStringField(g, NodeFields.NAME_EN.getSerial(), v.getNameEn());

		writeStringField(g, NodeFields.DESC.getSerial(), v.getDesc());

		writeStringField(g, NodeFields.DESC_EN.getSerial(), v.getDescEn());

		writeStringField(g, NodeFields.PREFIX.getSerial(), v.getPrefix());

		this.dataTypeRelationSerializer.serialize(v.getDataType(), g, p);

		this.unitRelationSerializer.serialize(v.getUnit(), g, p);

		this.valueRelationSerializer.serialize(v.getValue(), g, p);

		writeStringField(g, NodeFields.BSDD_GUID.getSerial(), v.getBsddGuid());

		this.paramRelationSerializer.serialize(v.getParams(), g, p);

		g.writeEndObject();
	}

}
