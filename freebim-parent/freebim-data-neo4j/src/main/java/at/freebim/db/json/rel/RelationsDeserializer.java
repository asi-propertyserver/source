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
import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.dto.Relations;
import at.freebim.db.json.AbstractDeserializer;

/**
 * This class represents a json-deserializer for the class {@link Relations}. It
 * extends {@link AbstractDeserializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.dto.Relations
 * @see AbstractDeserializer
 */
public class RelationsDeserializer extends AbstractDeserializer<Relations> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6000680711504831383L;
	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(RelationsDeserializer.class);

	@Override
	public Relations deserialize(JsonParser p, DeserializationContext c) throws IOException, JsonProcessingException {

		ObjectCodec oc = p.getCodec();
		this.jn = oc.readTree(p);

		Relations relations = new Relations();
		ArrayList<BaseRel<?, ?>> entryList = new ArrayList<BaseRel<?, ?>>();

		try {

			relations.c = this.getTextForField(RelationFields.CLASS_NAME.getSerial());
			relations.t = this.jn.get(RelationFields.TYPE.getSerial()).intValue();
			relations.dir = this.getTextForField(RelationFields.DIRECTION.getSerial());

			JsonNode rels = this.jn.get(RelationFields.RELATIONS.getSerial());
			if (rels != null && rels.isArray()) {
				Iterator<JsonNode> elems = rels.elements();
				Class<?> clazz;
				try {
					clazz = Class.forName("at.freebim.db.domain.rel." + relations.c);
					ObjectMapper mapper = new ObjectMapper();
					while (elems.hasNext()) {

						logger.debug("deserialize ..");

						JsonNode next = elems.next();

						BaseRel<?, ?> entry = null;
						try {
							entry = (BaseRel<?, ?>) mapper.treeToValue(next, clazz);
							logger.debug("entry is a {}", entry.getClass().getSimpleName());
							entryList.add(entry);
						} catch (JsonParseException e) {
							logger.error("", e);
						} catch (JsonMappingException e) {
							logger.error("", e);
						} catch (IOException e) {
							logger.error("", e);
						} catch (Exception e) {
							logger.error("", e);
						}
					}
				} catch (ClassNotFoundException e) {
					logger.error("unknown class: '" + relations.c + "'", e);
				}
			}
		} catch (Exception e) {
			logger.error("Error deserializing Relations", e);
		}

		int n = entryList.size();
		relations.relations = new BaseRel<?, ?>[n];
		for (int i = 0; i < n; i++) {
			relations.relations[i] = entryList.get(i);
		}
		return relations;

	}

}
