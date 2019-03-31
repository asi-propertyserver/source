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
package at.freebim.db.domain.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;

import at.freebim.db.domain.Company;


/**
 * 
 * This class represents a json-deserializer for the class/node {@link Company}.
 * It extends {@link LifetimeBaseNodeDeserializer}.
 * 
 * @see at.freebim.db.domain.Company
 * @see at.freebim.db.domain.json.LifetimeBaseNodeDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 * */
public class CompanyDeserializer extends LifetimeBaseNodeDeserializer<Company> {

	/**
	 * Creates a new instance.
	 */
	public CompanyDeserializer() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext)
	 */
	@Override
	public Company deserialize(JsonParser p, DeserializationContext c)
			throws IOException, JsonProcessingException {
		
		Company v = new Company();
		v = super.deserialize(p, c, v);
		
		v.setCode(this.getTextForField(NodeFields.CODE.getSerial()));
		v.setName(this.getTextForField(NodeFields.NAME.getSerial()));
		v.setUrl(this.getTextForField(NodeFields.URL.getSerial()));
		v.setLogo(this.getTextForField(NodeFields.LOGO.getSerial()));
		
		return v;
	}

}
