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
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.freebim.db.domain.rel.UnitConversion;

/**
 * This class represents a json-deserializer for the class/relation {@link UnitConversion}.
 * It extends {@link QualifiedBaseRelDeserializer}.
 * 
 * @see at.freebim.db.domain.rel.UnitConversion
 * @see at.freebim.db.domain.json.rel.QualifiedBaseRelDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class UnitConversionDeserializer extends QualifiedBaseRelDeserializer<UnitConversion> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(UnitConversionDeserializer.class);

	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext)
	 */
	@Override
	public UnitConversion deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		ObjectCodec oc = p.getCodec();
        this.jn = oc.readTree(p);

        UnitConversion v = new UnitConversion();
        
		v = super.deserialize(p, ctxt, v);
		logger.debug("q=[{}] ", v.getQ());
		return v;
	}

}
