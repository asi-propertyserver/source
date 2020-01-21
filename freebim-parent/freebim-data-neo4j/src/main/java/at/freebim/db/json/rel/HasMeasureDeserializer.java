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
package at.freebim.db.json.rel;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;

import at.freebim.db.domain.rel.HasMeasure;

/**
 * This class represents a json-deserializer for the class/relation
 * {@link HasMeasure}. It extends {@link OrderedBaseRelDeserializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.rel.HasMeasure
 * @see OrderedBaseRelDeserializer
 */
public class HasMeasureDeserializer extends OrderedBaseRelDeserializer<HasMeasure> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6707821831360557644L;

	@Override
	public HasMeasure deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {

		ObjectCodec oc = p.getCodec();
		this.jn = oc.readTree(p);

		HasMeasure v = new HasMeasure();

		v.setBsddGuid(this.getTextForField(RelationFields.BSDD_GUID.getSerial()));

		return super.deserialize(p, ctxt, v);
	}

}
