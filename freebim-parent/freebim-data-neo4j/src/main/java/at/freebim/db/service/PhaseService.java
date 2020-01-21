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

import at.freebim.db.domain.Phase;

/**
 * The service for the node/class {@link Phase}. This service extends
 * {@link ContributedBaseNodeService} and {@link BsddObjectService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Phase
 * @see at.freebim.db.service.ContributedBaseNodeService
 * @see at.freebim.db.service.BsddObjectService
 */
public interface PhaseService extends ContributedBaseNodeService<Phase>, BsddObjectService<Phase> {

	String CODE_UNDEFINED = "ND";

	/**
	 * Get the {@link Phase} that has the provided code.
	 *
	 * @param code the code of the {@link Phase}
	 * @return the {@link Phase} that has the provided code
	 */
	Phase getByCode(String code);

}
