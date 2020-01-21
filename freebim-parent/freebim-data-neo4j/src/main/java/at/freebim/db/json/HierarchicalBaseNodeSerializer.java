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

import at.freebim.db.domain.ParameterSet;
import at.freebim.db.domain.base.HierarchicalBaseNode;
import at.freebim.db.domain.rel.HasParameterSet;
import at.freebim.db.domain.rel.ParentOf;

/**
 * This abstract class represents a json-serializer for a generic class/node
 * <b>T</b> that extends {@link HierarchicalBaseNode}. The class itself extends
 * {@link ParameterizedSerializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.HierarchicalBaseNode
 * @see ParameterizedSerializer
 */
public class HierarchicalBaseNodeSerializer<T extends HierarchicalBaseNode> extends ParameterizedSerializer<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3193393887809485825L;

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(HierarchicalBaseNodeSerializer.class);

	/**
	 * The json-serializer for the relation to the parents of a
	 * {@link HierarchicalBaseNode}.
	 */
	private final IterableSerializer<ParentOf> parentRelationSerializer;

	/**
	 * The json-serializer for the relation to the child's of a
	 * {@link HierarchicalBaseNode}.
	 */
	private final IterableSerializer<ParentOf> childsRelationSerializer;

	/**
	 * The json-serializer for the relation from a {@link HierarchicalBaseNode} to a
	 * {@link ParameterSet}.
	 */
	private final IterableSerializer<HasParameterSet> parameterSetRelationSerializer;

	/**
	 * Creates a new instance.
	 */
	public HierarchicalBaseNodeSerializer() {
		super();
		this.parentRelationSerializer = new IterableSerializer<ParentOf>(NodeFields.PARENTS.getSerial());
		this.childsRelationSerializer = new IterableSerializer<ParentOf>(NodeFields.CHILDS.getSerial());
		this.parameterSetRelationSerializer = new IterableSerializer<HasParameterSet>(NodeFields.PSETS.getSerial());
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

		this.parentRelationSerializer.serialize(v.getParents(), g, p);
		this.childsRelationSerializer.serialize(v.getChilds(), g, p);
		this.parameterSetRelationSerializer.serialize(v.getParameterSets(), g, p);

		writeStringField(g, NodeFields.LEVEL.getSerial(), v.getLevel());
	}

}
