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

import at.freebim.db.domain.rel.OfDiscipline;

/**
 * This class represents a json-serializer for the class/relation
 * {@link OfDiscipline}. It extends {@link BaseRelSerializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.rel.OfDiscipline
 * @see BaseRelSerializer
 */
public class OfDisciplineSerializer extends BaseRelSerializer<OfDiscipline> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8875005729187093286L;

	@Override
	public void serialize(final OfDiscipline v, final JsonGenerator g, final SerializerProvider p)
			throws IOException, JsonProcessingException {

		g.writeStartObject();
		super.serialize(v, g, p);
		g.writeEndObject();
	}

}