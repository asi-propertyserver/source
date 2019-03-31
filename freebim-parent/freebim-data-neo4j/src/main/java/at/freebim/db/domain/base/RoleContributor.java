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
 * Represents the role of a contributer.
 * This class implements {@link GrantedAuthority}.
 * 
 * @see org.springframework.security.core.GrantedAuthority
 * 
 * @author rainer.breuss@uibk.ac.at
 */
public enum RoleContributor implements GrantedAuthority {

	/*
	 * Role names have to start with 'ROLE_'. 
	 * It won't work otherwise due to some undocumented spring circumstances ...
	 */
	ROLE_DELETE, 
	ROLE_VIEW_EXTENSIONS, 
	ROLE_SET_STATUS, 
	ROLE_SET_RELEASE_STATUS, 
	ROLE_LIBRARY_REFERENCES;
	
	/**
	 * Returns the name of the authority.
	 * 
	 * @return returns the name
	 * */
	@Override
    public String getAuthority() {
        return name();
    }
}
