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
package at.freebim.db.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.freebim.db.domain.Contributor;
import at.freebim.db.service.ContributorService;

/**
 * The controller that handles the node/entity {@link Contributor}. It extends
 * {@link BaseController}.
 * 
 * @see at.freebim.db.domain.Contributor
 * @see at.freebim.db.webapp.controller.BaseController
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RestController
@RequestMapping("/contributor")
public class ContributorController extends BaseController<Contributor> {

	/**
	 * The service that handles the node/entity {@link Contributor}.
	 */
	@Autowired
	private ContributorService service;

	/**
	 * Creates a new instance.
	 */
	public ContributorController() {
		super(Contributor.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.webapp.controller.BaseController#getService()
	 */
	@Override
	protected ContributorService getService() {
		return this.service;
	}

}
