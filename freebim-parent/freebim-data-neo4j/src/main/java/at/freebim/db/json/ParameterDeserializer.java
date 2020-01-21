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

import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.base.ParameterType;

/**
 * This class represents a json-deserializer for the class/node
 * {@link Parameter}. It extends {@link StatedBaseNodeDeserializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Parameter
 * @see StatedBaseNodeDeserializer
 */
public class ParameterDeserializer extends StatedBaseNodeDeserializer<Parameter> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5534930835499263619L;

	/**
	 * Creates a new instance.
	 */
	public ParameterDeserializer() {
		super();
	}

	@Override
	public Parameter deserialize(JsonParser p, DeserializationContext c) throws IOException, JsonProcessingException {

		Parameter v = new Parameter();
		v = super.deserialize(p, c, v);

		v.setCode(this.getTextForField(NodeFields.CODE.getSerial()));
		v.setName(this.getTextForField(NodeFields.NAME.getSerial()));
		v.setNameEn(this.getTextForField(NodeFields.NAME_EN.getSerial()));
		v.setDesc(this.getTextForField(NodeFields.DESC.getSerial()));
		v.setDescEn(this.getTextForField(NodeFields.DESC_EN.getSerial()));
		v.setDefaultString(this.getTextForField(NodeFields.DEFAULT.getSerial()));

		ParameterType ptype = ParameterType.UNDEFINED;
		Integer code = this.getIntegerForField(NodeFields.PARAM_TYPE.getSerial());
		if (code != null) {
			ptype = ParameterType.fromCode(code.intValue());
		}
		v.setPtype(ptype);

		v.setBsddGuid(this.getTextForField(NodeFields.BSDD_GUID.getSerial()));

		return v;
	}

}
