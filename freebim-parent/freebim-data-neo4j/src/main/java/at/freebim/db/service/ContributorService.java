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

import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.base.RoleContributor;

/**
 * The service for the class {@link Contributor}.
 * The service extends {@link LifetimeBaseNodeService}.
 * 
 * @see at.freebim.db.domain.Contributor
 * @see at.freebim.db.service.LifetimeBaseNodeService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface ContributorService extends LifetimeBaseNodeService<Contributor> {

	public static final String FREEBIM_CONTRIBUTOR_CODE = "freeBIM-App";
	
	/**
	 * Get the {@link Contributor} that has the provided code.
	 * 
	 * @param code the code from the {@link Contributor}
	 * @return returns a {@link Contributor}
	 */
	public Contributor getByCode(String code);

	/**
	 * Checks if the {@link Contributor} has all the roles that are provided in the second parameter
	 * <b>roleContributors</b>.
	 * 
	 * @param contributor the {@link Contributor}
	 * @param roleContributors an array of roles
	 * @return <code>true</code> if the {@link Contributor} has every role, <code>false</code> otherwise
	 */
	public boolean test(Contributor contributor, RoleContributor[] roleContributors);
}
