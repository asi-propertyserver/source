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

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.freebim.db.domain.base.rel.QualifiedBaseRel;

/**
 * This class represents a json-deserializer for a class/relation <b>T</b> that extends {@link QualifiedBaseRel}.
 * The class itself extends {@link BaseRelDeserializer}.
 * 
 * @see at.freebim.db.domain.base.rel.QualifiedBaseRel
 * @see at.freebim.db.domain.json.rel.BaseRelDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public abstract class QualifiedBaseRelDeserializer<T extends QualifiedBaseRel<?,?>> extends BaseRelDeserializer<T> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(QualifiedBaseRelDeserializer.class);

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.json.rel.BaseRelDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext, at.freebim.db.domain.base.rel.BaseRel)
	 */
	@Override
	public T deserialize(JsonParser jp, DeserializationContext ctxt, T v)
			throws IOException, JsonProcessingException {
		
		v.setQ(this.jn.get(RelationFields.QUALITY.getSerial()).getDoubleValue());
		
		logger.debug("q=[{}]", v.getQ());

		return super.deserialize(jp, ctxt, v);
	}
}
