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
package at.freebim.db.domain.base;

import org.springframework.security.core.GrantedAuthority;

/**
 * Represents a Role. This clsss implements {@link GrantedAuthority}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.springframework.security.core.GrantedAuthority
 */
public enum Role implements GrantedAuthority {

	/*
	 * Role names have to start with 'ROLE_'. It won't work otherwise due to some
	 * undocumented spring circumstances ...
	 */
	ROLE_ANONYMOUS, ROLE_GUEST, ROLE_ADMIN, ROLE_CONTRIBUTOR, ROLE_USERMANAGER, ROLE_WEBSERVICE_READ,
	ROLE_WEBSERVICE_WRITE, ROLE_EDIT;

	/**
	 * Returns the name of the authority.
	 *
	 * @return returns the name
	 */
	@Override
	public String getAuthority() {
		return name();
	}
}
