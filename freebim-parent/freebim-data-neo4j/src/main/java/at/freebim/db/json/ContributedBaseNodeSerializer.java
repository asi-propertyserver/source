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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.Document;
import at.freebim.db.domain.base.ContributedBaseNode;
import at.freebim.db.domain.rel.ContributedBy;
import at.freebim.db.domain.rel.DocumentedIn;

/**
 * This abstract class represents a json-serializer for a class/node <b>T</b> .
 * <b>T</b> should extend {@link ContributedBaseNode}. This abstract class
 * extends {@link UuidentifyableSerializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.ContributedBaseNode
 * @see UuidentifyableSerializer
 */
public class ContributedBaseNodeSerializer<T extends ContributedBaseNode> extends UuidentifyableSerializer<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5894762930851191005L;

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ContributedBaseNodeSerializer.class);

	/**
	 * The json-serializer for the relations to the list of users
	 * ({@link FreebimUser}) that have contributed.
	 */
	private IterableSerializer<ContributedBy> contributedRelationSerializer;

	/**
	 * The json-serializer for the relations to the list of {@link Document}s.
	 */
	private IterableSerializer<DocumentedIn> documentedRelationSerializer;

	/**
	 * Create new instance.
	 */
	public ContributedBaseNodeSerializer() {
		super();
		this.contributedRelationSerializer = new IterableSerializer<ContributedBy>(
				NodeFields.CONTRIBUTED_BY.getSerial());
		this.documentedRelationSerializer = new IterableSerializer<DocumentedIn>(NodeFields.DOCUMENTED.getSerial());
	}

	@Override
	public void serialize(final T v, final JsonGenerator g, final SerializerProvider p)
			throws IOException, JsonProcessingException {

		logger.debug("serializing '{}' ...", ((v == null) ? "null" : v.getName()));

		try {
			super.serialize(v, g, p);
		} catch (Exception e) {
			logger.error("Error serializing super class: ", e);
		}

		if (v == null)
			return;

		try {
			this.contributedRelationSerializer.serialize(v.getContributor(), g, p);
		} catch (Exception e) {
			logger.error("Error serializing contributedRelation");
		}

		this.documentedRelationSerializer.serialize(v.getDocs(), g, p);
	}

}
