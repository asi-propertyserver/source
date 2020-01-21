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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.json.AbstractDeserializer;

/**
 * This abstract class represents the base-deserializer for relations. It can
 * deserialize a class/relation <b>T</b> that extends {@link BaseRel}. The class
 * itself extends {@link AbstractDeserializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.rel.BaseRel
 * @see AbstractDeserializer
 */
public abstract class BaseRelDeserializer<T extends BaseRel<?, ?>> extends AbstractDeserializer<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8249046458838511035L;

	@Override
	public T deserialize(JsonParser jp, DeserializationContext ctxt, T v) throws IOException, JsonProcessingException {

		if (v != null) {
			RelationTypeEnum rt = RelationTypeEnum.fromCode(jn.get(RelationFields.TYPE.getSerial()).intValue());

			v.setType(rt.name());
			v.setId(this.getLongForField(RelationFields.ID.getSerial()));
			v.setN1Id(this.getLongForField(RelationFields.FROM_NODE.getSerial()));
			v.setN2Id(this.getLongForField(RelationFields.TO_NODE.getSerial()));
			v.setInfo(this.getTextForField(RelationFields.INFO.getSerial()));
		}

		return v;
	}

}
