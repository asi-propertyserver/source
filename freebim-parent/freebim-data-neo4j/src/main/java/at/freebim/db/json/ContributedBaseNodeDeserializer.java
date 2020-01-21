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

import at.freebim.db.domain.base.ContributedBaseNode;

/**
 * This abstract class represents a json-deserializer for a class/node <b>T</b>.
 * <b>T</b> should extend {@link ContributedBaseNode}. This abstract class
 * extends {@link UuidentifyableDeserializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.ContributedBaseNode
 * @see UuidentifyableDeserializer
 */
public abstract class ContributedBaseNodeDeserializer<T extends ContributedBaseNode>
		extends UuidentifyableDeserializer<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8108319080054831959L;

	/**
	 * Creates a new instance.
	 */
	public ContributedBaseNodeDeserializer() {
		super();
	}

	@Override
	public T deserialize(JsonParser p, DeserializationContext c, T v) throws IOException, JsonProcessingException {

		v = super.deserialize(p, c, v);

		return v;
	}

}
