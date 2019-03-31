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
import java.util.ArrayList;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;

import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.base.RoleContributor;

/**
 * This class represents a json-deserializer for the class/node {@link Contributor}.
 * It extends {@link LifetimeBaseNodeDeserializer}.
 * 
 * @see at.freebim.db.domain.Contributor
 * @see at.freebim.db.domain.json.LifetimeBaseNodeDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 */
public class ContributorDeserializer extends LifetimeBaseNodeDeserializer<Contributor> {

	/**
	 * The json-deserializer for the list of roles of the {@link Contributor}.
	 */
	private final IterableDeserializer<RoleContributor> roleDeserializer;
	
	/**
	 * Creates a new instance.
	 */
	public ContributorDeserializer() {
		super();
		this.roleDeserializer = new IterableDeserializer<RoleContributor>(RoleContributor.class);
	}

	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext)
	 */
	@Override
	public Contributor deserialize(JsonParser p, DeserializationContext c)
			throws IOException, JsonProcessingException {
		
		Contributor v = new Contributor();
		v = super.deserialize(p, c, v);
		
		v.setCode(this.getTextForField(NodeFields.CODE.getSerial()));
		v.setFirstName(this.getTextForField(NodeFields.FIRSTNAME.getSerial()));
		v.setLastName(this.getTextForField(NodeFields.LASTNAME.getSerial()));
		v.setTitle(this.getTextForField(NodeFields.TITLE.getSerial()));
		v.setEmail(this.getTextForField(NodeFields.EMAIL.getSerial()));
		
		ArrayList<RoleContributor> roleList = this.roleDeserializer.deserialize(this.jn.get(NodeFields.ROLES.getSerial()), c);
		
		RoleContributor[] roles = new RoleContributor[roleList.size()];
		roleList.toArray(roles);
		v.setRoles(roles);

		return v;
	}
	
	
}
