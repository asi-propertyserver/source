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

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;

import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.base.ParameterType;

/**
 * This class represents a json-deserializer for the class/node {@link Parameter}.
 * It extends {@link StatedBaseNodeDeserializer}.
 * 
 * @see at.freebim.db.domain.Parameter
 * @see at.freebim.db.domain.json.StatedBaseNodeDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class ParameterDeserializer extends StatedBaseNodeDeserializer<Parameter> {

	/**
	 * Creates a new instance.
	 */
	public ParameterDeserializer() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext)
	 */
	@Override
	public Parameter deserialize(JsonParser p, DeserializationContext c)
			throws IOException, JsonProcessingException {
		
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
