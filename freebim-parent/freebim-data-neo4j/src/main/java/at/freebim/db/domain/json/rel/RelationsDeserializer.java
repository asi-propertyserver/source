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
import java.util.ArrayList;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.json.AbstractDeserializer;
import at.freebim.db.dto.Relations;

/**
 * This class represents a json-deserializer for the class {@link Relations}.
 * It extends {@link AbstractDeserializer}.
 * 
 * @see at.freebim.db.dto.Relations
 * @see at.freebim.db.domain.json.AbstractDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class RelationsDeserializer extends AbstractDeserializer<Relations>{

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(RelationsDeserializer.class);
	
	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext)
	 */
	@Override
	public Relations deserialize(JsonParser p, DeserializationContext c)
			throws IOException, JsonProcessingException {
		
		ObjectCodec oc = p.getCodec();
        this.jn = oc.readTree(p);

        Relations relations = new Relations();
        ArrayList<BaseRel<?, ?>> entryList = new ArrayList<BaseRel<?, ?>>();

        try {
			
			relations.c = this.getTextForField(RelationFields.CLASS_NAME.getSerial());
			relations.t = this.jn.get(RelationFields.TYPE.getSerial()).getIntValue();
			relations.dir = this.getTextForField(RelationFields.DIRECTION.getSerial());
			
			JsonNode rels = this.jn.get(RelationFields.RELATIONS.getSerial());
		    if (rels != null && rels.isArray()) {
		    	Iterator<JsonNode> elems = rels.getElements();
		    	Class<?> clazz;
				try {
					clazz = Class.forName("at.freebim.db.domain.rel." + relations.c);
		    		ObjectMapper mapper = new ObjectMapper();
			    	while (elems.hasNext()) {
						
			    		logger.debug("deserialize ..");

						JsonNode next = elems.next();
			    		
			    		BaseRel<?,?> entry = null;
			    		try {
			    			entry = (BaseRel<?,?>) mapper.treeToValue(next, clazz);
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
	    relations.relations = new BaseRel<?,?>[n];
	    for (int i=0; i<n; i++) {
	    	relations.relations[i] = entryList.get(i);
	    }
		return relations;

	}

	
}
