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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

import at.freebim.db.domain.rel.Equals;

/**
 * This class represents a json-serializer for the class/relation
 * {@link Equals}. It extends {@link QualifiedBaseRelSerializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.rel.Equals
 * @see QualifiedBaseRelSerializer
 */
public class EqualsSerializer extends QualifiedBaseRelSerializer<Equals> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4882411984133134222L;

	@Override
	public void serialize(final Equals v, final JsonGenerator g, final SerializerProvider p)
			throws IOException, JsonProcessingException {

		g.writeStartObject();

		if (v.getFromClass() != null && v.getFromClass().length() > 0)
			g.writeStringField(RelationFields.FROM_CLASS.getSerial(), v.getFromClass());

		if (v.getToClass() != null && v.getToClass().length() > 0)
			g.writeStringField(RelationFields.TO_CLASS.getSerial(), v.getToClass());

		if (!v.isValid()) {
			g.writeNumberField(RelationFields.INVALID.getSerial(), 1);
		}
		super.serialize(v, g, p);
		g.writeEndObject();
	}

}
