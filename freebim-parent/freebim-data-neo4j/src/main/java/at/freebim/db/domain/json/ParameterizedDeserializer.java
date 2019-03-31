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

import at.freebim.db.domain.base.Parameterized;

/**
 * This abstract class represents a json-deserializer for a class/node <b>T</b> 
 * that extends {@link Parameterized}.
 * The class itself extends {@link StatedBaseNodeDeserializer}. 
 * 
 * @see at.freebim.db.domain.base.Parameterized
 * @see at.freebim.db.domain.json.StatedBaseNodeDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public abstract class ParameterizedDeserializer<T extends Parameterized> extends StatedBaseNodeDeserializer<T> {

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.StatedBaseNodeDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext, at.freebim.db.domain.base.StatedBaseNode)
	 */
	@Override
	public T deserialize(JsonParser p, DeserializationContext c, T v)
			throws IOException, JsonProcessingException {

		return super.deserialize(p, c, v);
	}


}
