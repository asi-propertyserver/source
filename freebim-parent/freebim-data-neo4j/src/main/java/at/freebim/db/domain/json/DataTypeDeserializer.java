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

import at.freebim.db.domain.DataType;

/**
 * This class represents a json-deserializer for the class/node {@link DataType}.
 * It extends {@link ContributedBaseNodeDeserializer}.
 * 
 * @see at.freebim.db.domain.DataType
 * @see at.freebim.db.domain.json.ContributedBaseNodeDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 */
public class DataTypeDeserializer extends ContributedBaseNodeDeserializer<DataType> {

	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext)
	 */
	@Override
	public DataType deserialize(JsonParser p, DeserializationContext c)
			throws IOException, JsonProcessingException {
		
		DataType v = new DataType();
		v = super.deserialize(p, c, v);
		
		v.setName(this.getTextForField(NodeFields.NAME.getSerial()));
		v.setNameEn(this.getTextForField(NodeFields.NAME_EN.getSerial()));
		v.setDesc(this.getTextForField(NodeFields.DESC.getSerial()));
		v.setDescEn(this.getTextForField(NodeFields.DESC_EN.getSerial()));
		v.setBsddGuid(this.getTextForField(NodeFields.BSDD_GUID.getSerial()));
		v.setRegExp(this.getTextForField(NodeFields.REGEXPR.getSerial()));
		
		return v;
	}
}
