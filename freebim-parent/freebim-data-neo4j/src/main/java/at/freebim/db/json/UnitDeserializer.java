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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import at.freebim.db.domain.Unit;

/**
 * This class represents a json-deserializer for the class/node {@link Unit}. It
 * extends {@link ContributedBaseNodeDeserializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Unit
 * @see ContributedBaseNodeDeserializer
 */
public class UnitDeserializer extends ContributedBaseNodeDeserializer<Unit> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2484382130166316882L;

	@Override
	public Unit deserialize(JsonParser p, DeserializationContext c) throws IOException, JsonProcessingException {

		Unit v = new Unit();
		v = super.deserialize(p, c, v);

		v.setName(this.getTextForField(NodeFields.NAME.getSerial()));
		v.setNameEn(this.getTextForField(NodeFields.NAME_EN.getSerial()));
		v.setDesc(this.getTextForField(NodeFields.DESC.getSerial()));
		v.setDescEn(this.getTextForField(NodeFields.DESC_EN.getSerial()));
		v.setUnitCode(this.getTextForField(NodeFields.CODE.getSerial()));
		v.setBsddGuid(this.getTextForField(NodeFields.BSDD_GUID.getSerial()));

		return v;
	}
}
