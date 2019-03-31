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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.freebim.db.domain.rel.CompanyCompany;

/**
 * This class represents a json-serializer for the class/relation {@link CompanyCompany}.
 * It extends {@link OrderedBaseRelSerializer}.
 * 
 * @see at.freebim.db.domain.rel.CompanyCompany
 * @see at.freebim.db.domain.json.rel.OrderedBaseRelSerializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class CompanyCompanySerializer extends OrderedBaseRelSerializer<CompanyCompany> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(CompanyCompanySerializer.class);

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.rel.BaseRelSerializer#serialize(at.freebim.db.domain.base.rel.BaseRel, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final CompanyCompany v, final JsonGenerator g, final SerializerProvider p)
			throws IOException, JsonProcessingException {
		
		logger.debug ("serializing {} ...", ((v == null) ? "null" : v.getType()));

		if (v == null)
			return;

		g.writeStartObject();
	
		super.serialize(v, g, p);
		
		g.writeEndObject();

		logger.debug ("finished.");
	}

}
