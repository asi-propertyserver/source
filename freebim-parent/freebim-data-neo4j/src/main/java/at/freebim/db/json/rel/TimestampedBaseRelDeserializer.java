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
package at.freebim.db.json.rel;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import at.freebim.db.domain.base.rel.TimestampedBaseRel;
import at.freebim.db.service.DateService;

/**
 * This class represents a json-deserializer for a class/relation <b>T</b> that
 * extends {@link TimestampedBaseRel}. The class itself extends
 * {@link BaseRelDeserializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.rel.TimestampedBaseRel
 * @see BaseRelDeserializer
 */
public abstract class TimestampedBaseRelDeserializer<T extends TimestampedBaseRel<?, ?>>
		extends BaseRelDeserializer<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3056144583069263032L;

	@Override
	public T deserialize(JsonParser jp, DeserializationContext ctxt, T v) throws IOException, JsonProcessingException {

		if (v != null) {
			Long ts = this.getLongForField(RelationFields.TIMESTAMP.getSerial());
			if (ts != null) {
				v.setTs(ts + DateService.FREEBIM_DELTA);
			}
		}
		return super.deserialize(jp, ctxt, v);
	}
}
