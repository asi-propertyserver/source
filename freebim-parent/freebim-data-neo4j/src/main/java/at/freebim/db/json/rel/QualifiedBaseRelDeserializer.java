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
package at.freebim.db.json.rel;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import at.freebim.db.domain.base.rel.QualifiedBaseRel;

/**
 * This class represents a json-deserializer for a class/relation <b>T</b> that
 * extends {@link QualifiedBaseRel}. The class itself extends
 * {@link BaseRelDeserializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.rel.QualifiedBaseRel
 * @see BaseRelDeserializer
 */
public abstract class QualifiedBaseRelDeserializer<T extends QualifiedBaseRel<?, ?>> extends BaseRelDeserializer<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -75355277114635396L;
	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(QualifiedBaseRelDeserializer.class);

	@Override
	public T deserialize(JsonParser jp, DeserializationContext ctxt, T v) throws IOException, JsonProcessingException {

		v.setQ(this.jn.get(RelationFields.QUALITY.getSerial()).doubleValue());

		logger.debug("q=[{}]", v.getQ());

		return super.deserialize(jp, ctxt, v);
	}
}
