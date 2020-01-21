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
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.base.Role;

/**
 * This class represents a json-deserializer for the class/node
 * {@link FreebimUser}. It extends {@link LifetimeBaseNodeDeserializer}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.FreebimUser
 * @see LifetimeBaseNodeDeserializer
 */
public class FreebimUserDeserializer extends LifetimeBaseNodeDeserializer<FreebimUser> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6112043683766192582L;
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

	@Override
	public FreebimUser deserialize(JsonParser p, DeserializationContext c) throws IOException, JsonProcessingException {

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
