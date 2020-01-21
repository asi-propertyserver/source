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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import at.freebim.db.domain.Company;

/**
 * This class represents a json-deserializer for the class/node {@link Company}.
 * It extends {@link LifetimeBaseNodeDeserializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Company
 * @see LifetimeBaseNodeDeserializer
 */
public class CompanyDeserializer extends LifetimeBaseNodeDeserializer<Company> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4515942670310832548L;

	/**
	 * Creates a new instance.
	 */
	public CompanyDeserializer() {
		super();
	}

	@Override
	public Company deserialize(JsonParser p, DeserializationContext c) throws IOException, JsonProcessingException {

		Company v = new Company();
		v = super.deserialize(p, c, v);

		v.setCode(this.getTextForField(NodeFields.CODE.getSerial()));
		v.setName(this.getTextForField(NodeFields.NAME.getSerial()));
		v.setUrl(this.getTextForField(NodeFields.URL.getSerial()));
		v.setLogo(this.getTextForField(NodeFields.LOGO.getSerial()));

		return v;
	}

}
