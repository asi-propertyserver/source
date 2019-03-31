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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import at.freebim.db.domain.Library;
import at.freebim.db.domain.rel.References;
import at.freebim.db.service.BaseNodeService;
import at.freebim.db.service.LibraryService;

/**
 * The controller for the node/entity {@link Library}.
 * It extends {@link BaseController}.
 * 
 * @see at.freebim.db.domain.Library
 * @see at.freebim.db.webapp.controller.BaseController
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Controller
@RequestMapping("/libraries")
public class LibraryController extends BaseController<Library> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);

	/**
	 * The service that handles the node/entity {@link Library}.
	 */
	@Autowired
	private LibraryService libraryService;
	
	/**
	 * Creates a new instance.
	 */
	public LibraryController() {
		super(Library.class);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.webapp.controller.BaseController#getService()
	 */
	@Override
	protected BaseNodeService<Library> getService() {
		return this.libraryService;
	}
	
    /**
     * Delete every element from a {@link Library} and every relation to it. The {@link Library} 
     * is specified by the provided id.
     * 
     * @param libId the id of the library. This parameter is required
     * @param model the model
     * @return the {@link AjaxResponse} that includes the library that has been cleared or a error message
     */
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse clearLibrary(@RequestParam(value="libId", required=true) Long libId, 
    		Model model) {
		logger.debug("clearLibrary nodeId={}", libId);
		
		AjaxResponse response = null;
		try {
			// Delegate to service 
			Library lib = this.libraryService.clear(libId);
			response = new AjaxResponse(lib);
			
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
		
		savedNodesNotifications(response);

		return response;
	}

    
    /**
     * Update the {@link Library} that has the provided id.
     * When an error occurs the message will be added to the response.
     * 
     * @param libId the id of the {@link Library} that will be updated
     * @param model the model
     * @return the {@link AjaxResponse} that includes the updated {@link Library} or the error messages
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse updateLibrary(@RequestParam(value="libId", required=true) Long libId, 
    		Model model) {
		logger.debug("updateLibrary nodeId={}", libId);
		
		AjaxResponse response = null;
		try {
			// Delegate to service 
			Library lib = this.libraryService.update(libId);
			response = new AjaxResponse(lib);
			
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
		
		savedNodesNotifications(response);

		return response;
	}


    
    /**
     * Get the a list of all names of the {@link References} 
     * that are connected to a {@link Library}
     * with the provided id. When an error occurs 
     * the message will be added to the response.
     * 
     * @param libId the id of the {@link Library}
     * @param model the model
     * @return the {@link AjaxResponse} that includes the list of 
     * {@link References} names or the error messages
     */
    @RequestMapping(value = "/refIdNames", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse refIdNames(@RequestParam(value="libId", required=true) Long libId, 
    		Model model) {
		logger.debug("get all refIdNames for library nodeId={}", libId);
		
		AjaxResponse response = null;
		try {
			// Delegate to service 
			ArrayList<String> refIdNames = (ArrayList<String>) this.libraryService.getRefIdNames(libId);
			logger.debug("got {} refIdNames.", ((refIdNames == null) ? "null" : refIdNames.size()));
			response = new AjaxResponse(refIdNames);
			
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
		
		savedNodesNotifications(response);

		return response;
	}
}
