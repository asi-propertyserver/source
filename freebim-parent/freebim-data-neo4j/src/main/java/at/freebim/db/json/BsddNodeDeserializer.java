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

import at.freebim.db.domain.BsddNode;

/**
 * This class represents a json-deserializer for the class/node
 * {@link BsddNode}. It extends {@link BaseNodeDeserializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.BsddNode
 * @see BaseNodeDeserializer
 */
public class BsddNodeDeserializer extends BaseNodeDeserializer<BsddNode> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7694101778884839703L;

	@Override
	public BsddNode deserialize(JsonParser p, DeserializationContext c) throws IOException, JsonProcessingException {

		BsddNode v = new BsddNode();
		v = super.deserialize(p, c, v);

		v.setGuid(this.getTextForField(NodeFields.BSDD_GUID.getSerial()));

		return v;
	}
}
