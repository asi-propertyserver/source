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
import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.base.RoleContributor;
import at.freebim.db.domain.rel.Responsible;
import at.freebim.db.domain.rel.WorksFor;

/**
 * This class represents a json-serializer for the class/node {@link Contributor}.
 * It extends {@link LifetimeBaseNodeSerializer}.
 * 
 * @see at.freebim.db.domain.Contributor
 * @see at.freebim.db.domain.json.LifetimeBaseNodeSerializer
 * 
 * @author rainer.breuss@uibk.ac.at
 */
public class ContributorSerializer extends LifetimeBaseNodeSerializer<Contributor> {

	/**
	 * The json-serializer for the  list of roles for the contributor.
	 */
	private final IterableSerializer<RoleContributor> roleSerializer;
	
	/**
	 * The json-serializer for the relations to the list of companies ({@link Company}) 
	 * that the {@link Contributor} is working for.
	 */
	private final IterableSerializer<WorksFor> companySerializer;
	
	/**
	 * The json-serializer for the relations to the list of libraries ({@link Library})
	 * the the {@link Contributor} is responsible for.
	 */
	private final IterableSerializer<Responsible> resposibleRelationSerializer;
	
	/**
	 * Creates a new instance.
	 */
	public ContributorSerializer() {
		super();
		this.roleSerializer = new IterableSerializer<>(NodeFields.ROLES.getSerial());
		this.companySerializer = new IterableSerializer<>(NodeFields.WORKS_FOR.getSerial());
		this.resposibleRelationSerializer = new IterableSerializer<Responsible>(NodeFields.RESPONSIBLE.getSerial());
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.LifetimeBaseNodeSerializer#serialize(at.freebim.db.domain.base.LifetimeBaseNode, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final Contributor v, final JsonGenerator g,
			final SerializerProvider p) throws IOException,
			JsonProcessingException {
		
		g.writeStartObject();
		
		super.serialize(v, g, p);
		
		if (v == null)
			return;

		writeStringField(g, NodeFields.CODE.getSerial(), v.getCode());
		writeStringField(g, NodeFields.FIRSTNAME.getSerial(), v.getFirstName());
		writeStringField(g, NodeFields.LASTNAME.getSerial(), v.getLastName());
		writeStringField(g, NodeFields.TITLE.getSerial(), v.getTitle());
		writeStringField(g, NodeFields.EMAIL.getSerial(), v.getEmail());
		
		this.roleSerializer.serialize(v.getRoles(), g, p);
		this.companySerializer.serialize(v.getCompanies(), g, p);
		this.resposibleRelationSerializer.serialize(v.getResponsible(), g, p);

		g.writeEndObject();
	}
}
