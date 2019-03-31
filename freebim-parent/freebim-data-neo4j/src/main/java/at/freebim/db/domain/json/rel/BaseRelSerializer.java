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

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.freebim.db.domain.base.NodeIdentifyable;
import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.service.DateService;

/**
 * This class represents a json-serializer for a class/relation <b>T</b> that extends {@link BaseRel}.
 * The class itself extends {@link JsonSerializer}.
 * 
 * @see at.freebim.db.domain.base.rel.BaseRel
 * @see org.codehaus.jackson.map.JsonSerializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class BaseRelSerializer<T extends BaseRel<?,?>> extends JsonSerializer<T> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(BaseRelSerializer.class);
	
	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final T v, final JsonGenerator g,
			final SerializerProvider p) throws IOException,
			JsonProcessingException {
		
		logger.debug ("serializing {} ...", ((v == null) ? "null" : v.getType()));
		
		if (v == null)
			return;

		RelationTypeEnum rt = RelationTypeEnum.valueOf(v.getType());
		g.writeNumberField(RelationFields.TYPE.getSerial(), rt.getCode());
		if (v.getId() != null)
			g.writeNumberField(RelationFields.ID.getSerial(), v.getId());
		
		if (v.getValidFrom() != null) {
			g.writeStringField(RelationFields.VALID_FROM.getSerial(), String.valueOf(v.getValidFrom().longValue() - DateService.FREEBIM_DELTA));
		}

		if (v.getValidTo() != null) {
			g.writeStringField(RelationFields.VALID_TO.getSerial(), String.valueOf(v.getValidTo().longValue() - DateService.FREEBIM_DELTA));
		}

		writeNodeInfo(v.getN1(), RelationFields.FROM_NODE.getSerial(), g);
		
		writeNodeInfo(v.getN2(), RelationFields.TO_NODE.getSerial(), g);
		
		if (v.getInfo() != null && v.getInfo().length() > 0) {
			g.writeStringField(RelationFields.INFO.getSerial(), v.getInfo());
		}
		
		logger.debug("finished.");
	}
	
	/**
	 * Write a number in a json field.
	 * 
	 * @param node the node from which the id will be written in the field
	 * @param idx the name of the field
	 * @param g
	 * @throws JsonGenerationException
	 * @throws IOException
	 */
	private void writeNodeInfo(final NodeIdentifyable node, final String idx, final JsonGenerator g) throws JsonGenerationException, IOException {
		
		if (node != null && node.getNodeId() != null) {
			
			g.writeNumberField(idx, node.getNodeId().longValue());
			
		}

	}

}