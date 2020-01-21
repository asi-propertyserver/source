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

import at.freebim.db.domain.base.BaseNode;

/**
 * This abstract class represents a json-deserializer for the class/node
 * {@link BaseNode}. It extends {@link AbstractDeserializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see AbstractDeserializer
 * @see at.freebim.db.domain.base.BaseNode
 */
public abstract class BaseNodeDeserializer<T extends BaseNode> extends AbstractDeserializer<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2257697216309955676L;

	/**
	 * Creates a new instance.
	 */
	public BaseNodeDeserializer() {
		super();
	}

	@Override
	public T deserialize(JsonParser p, DeserializationContext c, T v) throws IOException, JsonProcessingException {

		super.deserialize(p, c, v);

		v.setNodeId(this.getLongForField(NodeFields.NODEID.getSerial()));

		return v;
	}

}
