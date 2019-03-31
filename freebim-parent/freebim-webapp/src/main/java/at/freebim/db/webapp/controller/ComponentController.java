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

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import at.freebim.db.domain.Component;
import at.freebim.db.domain.Library;
import at.freebim.db.service.BaseNodeService;
import at.freebim.db.service.ComponentService;

/**
 * The controller that handles the node/entity {@link Component}.
 * It extends {@link BaseUuidController}.
 * 
 * @see at.freebim.db.domain.Component
 * @see at.freebim.db.webapp.controller.BaseUuidController
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Controller
@RequestMapping("/component")
public class ComponentController extends BaseUuidController<Component> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ComponentController.class);

	/**
	 * The service that handles the interaction with the node {@link Component}.
	 */
	@Autowired
	private ComponentService componentService;
	
	
	/**
	 * Creates a new instance.
	 */
	public ComponentController() {
		super(Component.class);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.webapp.controller.BaseController#getService()
	 */
	@Override
	protected BaseNodeService<Component> getService() {
		return this.componentService;
	}
	
	/**
	 * Get the list of {@link Component}s that have the provided name
	 * and are part of the library that has the provided id.
	 * 
	 * @param name the name of the {@link Component}
	 * @param libId the id of the {@link Library}
	 * @param model the model
	 * @return the {@link AjaxResponse} that includes the list of {@link Component}s
	 */
	@RequestMapping(value = "/getByNameFromLibrary", method = RequestMethod.POST)
	public @ResponseBody  AjaxResponse getByNameFromLibrary(String name, Long libId, Model model) {
		logger.debug("getByNameFromLibrary name=[{}], libId: [{}]", name, libId);

		super.setUserInfo(model);
		
		AjaxResponse response = null;
		ArrayList<Component> res = (ArrayList<Component>) this.componentService.getByNameFromLibrary(name, libId);

		response = new AjaxResponse(res);
		return response;
	}


	

}
