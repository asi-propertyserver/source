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

import at.freebim.db.domain.base.State;
import at.freebim.db.domain.base.StatedBaseNode;

/**
 * This class represents a json-deserializer for a class/node <b>T</b> that extends {@link StatedBaseNode}.
 * The class itself extends {@link ContributedBaseNodeDeserializer}.
 * 
 * @see at.freebim.db.domain.base.StatedBaseNode
 * @see at.freebim.db.domain.json.ContributedBaseNodeDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public abstract class StatedBaseNodeDeserializer<T extends StatedBaseNode> extends ContributedBaseNodeDeserializer<T> {

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.ContributedBaseNodeDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext, at.freebim.db.domain.base.ContributedBaseNode)
	 */
	@Override
	public T deserialize(final JsonParser p, final DeserializationContext c, T v)
			throws IOException, JsonProcessingException {

		v = super.deserialize(p, c, v);
		
		State state = State.UNDEFINED;
		Integer code = this.getIntegerForField(NodeFields.STATE.getSerial());
		if (code != null) {
			state = State.fromCode(code.intValue());
		}
		v.setState(state);
		
		v.setStatusComment(this.getTextForField(NodeFields.STATUS_COMMENT.getSerial()));
		
		return v;
	}
}
