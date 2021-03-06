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
package at.freebim.db.service;

import org.springframework.security.access.annotation.Secured;

import at.freebim.db.domain.SearchStringNode;

/**
 * The service for the node/class {@link SearchStringNode}. This service extends
 * {@link BaseNodeService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.SearchStringNode
 * @see at.freebim.db.service.BaseNodeService
 */
public interface SearchStringNodeService extends BaseNodeService<SearchStringNode> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.BaseService#save(java.lang.Object)
	 */
	@Secured({ "ROLE_EDIT", "ROLE_ADMIN", "ROLE_GUEST" })
	SearchStringNode save(SearchStringNode node);

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.BaseService#delete(java.lang.Object)
	 */
	@Secured({ "ROLE_EDIT", "ROLE_ADMIN", "ROLE_GUEST" })
	SearchStringNode delete(SearchStringNode node);

}
