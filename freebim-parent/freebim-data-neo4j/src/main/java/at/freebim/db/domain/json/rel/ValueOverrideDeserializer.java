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

import at.freebim.db.domain.rel.ValueOverride;

/**
 * This class represents a json-deserializer for the class/relation {@link ValueOverride}.
 * It extends {@link BaseRelDeserializer}.
 * 
 * @see at.freebim.db.domain.rel.ValueOverride
 * @see at.freebim.db.domain.json.rel.BaseRelDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class ValueOverrideDeserializer extends BaseRelDeserializer<ValueOverride> {

	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext)
	 */
	@Override
	public ValueOverride deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		ObjectCodec oc = p.getCodec();
        this.jn = oc.readTree(p);

        ValueOverride v = new ValueOverride();
        
        v.setValue(this.getTextForField(RelationFields.VALUE.getSerial()));
        v.setMeasure(this.getTextForField(RelationFields.MEASURE.getSerial()));

		return super.deserialize(p, ctxt, v);
	}


}
