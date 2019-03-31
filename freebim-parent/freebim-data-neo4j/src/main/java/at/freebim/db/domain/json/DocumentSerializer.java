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

import at.freebim.db.domain.Document;
import at.freebim.db.domain.base.ContributedBaseNode;
import at.freebim.db.domain.rel.DocumentedIn;

/**
 * This class represents a json-serializer for the class/node {@link Document}.
 * It extends {@link UuidentifyableSerializer}.
 * 
 * @see at.freebim.db.domain.Document
 * @see at.freebim.db.domain.json.UuidentifyableDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 */
public class DocumentSerializer extends UuidentifyableSerializer<Document> {

	/**
	 * The json-serializer for the relations from a {@link ContributedBaseNode} to a {@link Document}.
	 */
	private IterableSerializer<DocumentedIn> documentedRelationSerializer;

	/**
	 * Creates a new instance.
	 */
	public DocumentSerializer() {
		super();
		this.documentedRelationSerializer = new IterableSerializer<DocumentedIn>(NodeFields.NODES.getSerial());
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.ContributedBaseNodeSerializer#serialize(at.freebim.db.domain.base.ContributedBaseNode, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final Document v, final JsonGenerator g, final SerializerProvider p)
			throws IOException, JsonProcessingException {
		
		g.writeStartObject();
		
		super.serialize(v, g, p);

		writeStringField(g, NodeFields.NAME.getSerial(), v.getName());
		
		writeStringField(g, NodeFields.DESC.getSerial(), v.getDesc());

		writeStringField(g, NodeFields.DESC_EN.getSerial(), v.getDescEn());

		writeStringField(g, NodeFields.BSDD_GUID.getSerial(), v.getBsddGuid());
		
		this.documentedRelationSerializer.serialize(v.getDocumentedNodes(), g, p);
		
		g.writeEndObject();
	}

}
