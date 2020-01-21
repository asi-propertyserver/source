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

import at.freebim.db.domain.Library;
import at.freebim.db.service.DateService;

/**
 * This class represents a json-deserializer for the class/node {@link Library}.
 * It extends {@link HierarchicalBaseNodeDeserializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Library
 * @see HierarchicalBaseNodeDeserializer
 */
public class LibraryDeserializer extends HierarchicalBaseNodeDeserializer<Library> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4500401304202313228L;

	/**
	 * Creates a new instance.
	 */
	public LibraryDeserializer() {
		super();
	}

	@Override
	public Library deserialize(JsonParser p, DeserializationContext c) throws IOException, JsonProcessingException {

		Library v = new Library();
		v = super.deserialize(p, c, v);

		v.setName(this.getTextForField(NodeFields.NAME.getSerial()));
		v.setUrl(this.getTextForField(NodeFields.URL.getSerial()));
		v.setDesc(this.getTextForField(NodeFields.DESC.getSerial()));
		v.setLanguageCode(this.getTextForField(NodeFields.LANGUAGE.getSerial()));

		Long l = this.getLongForField(NodeFields.TIMESTAMP.getSerial());
		if (l != null) {
			v.setTs(l + DateService.FREEBIM_DELTA);
		}

		return v;
	}

}
