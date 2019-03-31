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

import at.freebim.db.domain.base.HierarchicalBaseNode;


/**
 * 
 * This abstract class represents a json-deserializer for a generic class/node <b>T</b>
 * that extends {@link HierarchicalBaseNode}.
 * The abstract class itself extends {@link ParameterizedDeserializer}.
 * 
 * @see at.freebim.db.domain.base.HierarchicalBaseNode
 * @see at.freebim.db.domain.json.ParameterizedDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 * */
public abstract class HierarchicalBaseNodeDeserializer<T extends HierarchicalBaseNode> extends ParameterizedDeserializer<T> {

	/**
	 * Creates a new instance.
	 */
	public HierarchicalBaseNodeDeserializer() {
		super();
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.ParameterizedDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext, at.freebim.db.domain.base.Parameterized)
	 */
	@Override
	public T deserialize(JsonParser p, DeserializationContext c, T v)
			throws IOException, JsonProcessingException {
		
		v = super.deserialize(p, c, v);
		
		v.setLevel(this.getTextForField(NodeFields.LEVEL.getSerial()));

		return v;
	}

}
