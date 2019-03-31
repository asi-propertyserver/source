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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import at.freebim.db.domain.BigBangNode;
import at.freebim.db.service.BaseNodeService;
import at.freebim.db.service.BigBangNodeService;

/**
 * @author rainer.breuss@uibk.ac.at
 */
@Controller
@RequestMapping("/bbn")
public class BigBangNodeController extends BaseUuidController<BigBangNode> {

	private static final Logger logger = LoggerFactory.getLogger(BigBangNodeController.class);

	@Autowired
	private BigBangNodeService bigBangNodeService;
	
	public BigBangNodeController() {
		super(BigBangNode.class);
	}

	@Override
	protected BaseNodeService<BigBangNode> getService() {
		return this.bigBangNodeService;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody AjaxResponse get(Model model) {
		logger.debug("get ...");
		
		AjaxResponse response = null;
		try {
			// Delegate to service 
			BigBangNode bbn = this.bigBangNodeService.getBigBangNode();
			response = new AjaxResponse(bbn);
			
		} catch (AuthenticationCredentialsNotFoundException e) {
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (AccessDeniedException e) {
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		
		return response;
	}
}
