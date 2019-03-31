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

import at.freebim.db.domain.Company;

/**
 * The service for {@link Company}. 
 * This service extends {@link LifetimeBaseNodeService}.
 * 
 * @see at.freebim.db.domain.Company
 * @see at.freebim.db.service.LifetimeBaseNodeService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface CompanyService extends LifetimeBaseNodeService<Company> {

	/**
	 * The name of the company.
	 */
	public String FREEBIM_COMPANY_NAME = "freeBIM";

	/**
	 * Gets the {@link Company} with the provided name.
	 * 
	 * @param name the name of the {@link Company}.
	 * 
	 * @return returns the {@link Company}.
	 */
	public Company get(String name);
}
