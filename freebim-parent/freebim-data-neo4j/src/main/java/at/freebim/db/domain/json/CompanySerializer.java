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

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;

import at.freebim.db.domain.Company;
import at.freebim.db.domain.rel.CompanyCompany;
import at.freebim.db.domain.rel.WorksFor;

/**
 * This class represents a json-serializer for the class/node {@link Company}.
 * It extends {@link LifetimeBaseNodeSerializer}.
 * 
 * @see at.freebim.db.domain.Company
 * @see at.freebim.db.domain.json.LifetimeBaseNodeSerializer
 * 
 * @author rainer.breuss@uibk.ac.at
 */
public class CompanySerializer extends LifetimeBaseNodeSerializer<Company> {

	/**
	 * The serializer for the relation: what users are working for the company.
	 */
	private final IterableSerializer<WorksFor> contributorSerializer;
	
	
	/**
	 * The serializer for the relation between companies ({@link Company}).
	 */
	private final IterableSerializer<CompanyCompany> compSerializer;
	
	/**
	 * Creates a new instance.
	 */
	public CompanySerializer() {
		super();
		this.contributorSerializer = new IterableSerializer<WorksFor>(NodeFields.WORKS_FOR.getSerial());
		this.compSerializer = new IterableSerializer<CompanyCompany>(NodeFields.COMPANY_COMPANY.getSerial());
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.LifetimeBaseNodeSerializer#serialize(at.freebim.db.domain.base.LifetimeBaseNode, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final Company v, final JsonGenerator g,
			final SerializerProvider p) throws IOException,
			JsonProcessingException {
		
		g.writeStartObject();
		
		super.serialize(v, g, p);
		
		if (v == null)
			return;

		writeStringField(g, NodeFields.CODE.getSerial(), v.getCode());
		writeStringField(g, NodeFields.NAME.getSerial(), v.getName());
		writeStringField(g, NodeFields.URL.getSerial(), v.getUrl());
		writeStringField(g, NodeFields.LOGO.getSerial(), v.getLogo());
		
		this.contributorSerializer.serialize(v.getContributor(), g, p);
		this.compSerializer.serialize(v.getCompany(), g, p);

		g.writeEndObject();
	}
}
