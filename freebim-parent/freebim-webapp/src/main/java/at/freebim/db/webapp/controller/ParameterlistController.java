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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.freebim.db.domain.Component;
import at.freebim.db.service.BaseNodeService;
import at.freebim.db.service.ComponentService;

/**
 * A controller for the node/entity {@link Component}. It extends
 * {@link BaseUuidController}.
 * 
 * @see at.freebim.db.domain.Component
 * @see at.freebim.db.webapp.controller.BaseUuidController
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RestController
@RequestMapping("/parameterlist")
public class ParameterlistController extends BaseUuidController<Component> {

	/**
	 * The service that handles the node/entity {@link Component}.
	 */
	@Autowired
	private ComponentService componentService;

	/**
	 * Creates a new instance.
	 */
	public ParameterlistController() {
		super(Component.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.webapp.controller.BaseController#getService()
	 */
	@Override
	protected BaseNodeService<Component> getService() {
		return this.componentService;
	}

	/**
	 * Show component as parameterlist-page.
	 * 
	 * @param freebimId
	 * @param model     the model
	 * 
	 * @return the name of the JSP page
	 * 
	 */
	@GetMapping(value = "/{freebimId}")
	public String parameterlist(@PathVariable String freebimId, Model model) {
		logger.debug("Show parameterlist-page");
		super.setUserInfo(model);

		model.addAttribute("freebimId", freebimId);

		return "parameterlist-page";
	}

}
