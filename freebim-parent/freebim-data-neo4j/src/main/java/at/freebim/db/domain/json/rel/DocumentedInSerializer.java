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
package at.freebim.db.domain.json.rel;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;

import at.freebim.db.domain.rel.DocumentedIn;

/**
 * This class represents a json-serializer for the class/relation {@link DocumentedIn}.
 * It extends {@link TimestampedBaseRelSerializer}.
 * 
 * @see at.freebim.db.domain.rel.DocumentedIn
 * @see at.freebim.db.domain.json.rel.TimestampedBaseRelSerializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class DocumentedInSerializer extends TimestampedBaseRelSerializer<DocumentedIn> {

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.rel.BaseRelSerializer#serialize(at.freebim.db.domain.base.rel.BaseRel, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final DocumentedIn v, final JsonGenerator g, final SerializerProvider p)
			throws IOException, JsonProcessingException {
		
		g.writeStartObject();
		
		super.serialize(v, g, p);
		
		g.writeEndObject();
	}

}
