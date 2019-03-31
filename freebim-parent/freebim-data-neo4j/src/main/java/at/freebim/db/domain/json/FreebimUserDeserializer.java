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

import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.base.Role;

/**
 * This class represents a json-deserializer for the class/node {@link FreebimUser}.
 * It extends {@link LifetimeBaseNodeDeserializer}.
 * 
 * @see at.freebim.db.domain.FreebimUser
 * @see at.freebim.db.domain.json.LifetimeBaseNodeDeserializer
 * 
 * @author rainer.breuss@uibk.ac.at
 */
public class FreebimUserDeserializer extends LifetimeBaseNodeDeserializer<FreebimUser> {

	/**
	 * The json-deserializer for the {@link Role}s from a {@link FreebimUser}.
	 */
	private final IterableDeserializer<Role> roleDeserializer;
	
	/**
	 * Creates a new instance.
	 */
	public FreebimUserDeserializer() {
		this.roleDeserializer = new IterableDeserializer<Role>(Role.class);
	}
	
	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext)
	 */
	@Override
	public FreebimUser deserialize(JsonParser p, DeserializationContext c)
			throws IOException, JsonProcessingException {
		
		FreebimUser v = new FreebimUser();
		v = super.deserialize(p, c, v);
		
		v.setUsername(this.getTextForField(NodeFields.USERNAME.getSerial()));
		v.setPassword(this.getTextForField(NodeFields.PASSWORD.getSerial()));
		
		ArrayList<Role> roleList = this.roleDeserializer.deserialize(this.jn.get(NodeFields.ROLES.getSerial()), c);
		Role[] roles = new Role[roleList.size()];
		roleList.toArray(roles);
		v.setRoles(roles);
		
		v.setContributorId(this.getLongForField(NodeFields.CONTRIBUTOR_ID.getSerial()));

		return v;
	}
}
