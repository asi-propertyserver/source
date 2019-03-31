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

import at.freebim.db.domain.Component;
import at.freebim.db.domain.base.ComponentType;


/**
 * 
 * This class represents a json-deserializer for the class/node {@link Component}.
 * It extends {@link HierarchicalBaseNodeDeserializer}.
 * 
 * @see at.freebim.db.domain.Component
 * @see at.freebim.db.domain.json.HierarchicalBaseNodeDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 * */
public class ComponentDeserializer extends HierarchicalBaseNodeDeserializer<Component> {

	/**
	 * Creates a new instance.
	 */
	public ComponentDeserializer() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext)
	 */
	@Override
	public Component deserialize(JsonParser p, DeserializationContext c)
			throws IOException, JsonProcessingException {
		
        Component v = new Component();
		v = super.deserialize(p, c, v);
		
		v.setCode(this.getTextForField(NodeFields.CODE.getSerial()));
		v.setName(this.getTextForField(NodeFields.NAME.getSerial()));
		v.setNameEn(this.getTextForField(NodeFields.NAME_EN.getSerial()));
		v.setDesc(this.getTextForField(NodeFields.DESC.getSerial()));
		v.setDescEn(this.getTextForField(NodeFields.DESC_EN.getSerial()));
		v.setBsddGuid(this.getTextForField(NodeFields.BSDD_GUID.getSerial()));
		
		ComponentType ct = ComponentType.UNDEFINED;
		Integer code = this.getIntegerForField(NodeFields.TYPE.getSerial());
		if (code != null) {
			ct = ComponentType.fromCode(code.intValue());
		}
		v.setType(ct);

		return v;
	}

}
