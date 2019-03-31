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
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * The main controller.
 * It extends {@link BaseAuthController}.
 * 
 * @see at.freebim.db.webapp.controller.BaseAuthController
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Controller
@RequestMapping("/")
public class MainController extends BaseAuthController {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	/**
     * Show the main page.
     * 
     * @param model the model
     * 
     * @return the name of the JSP-page
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String add(Model model) {
		logger.debug("Show main page");
		super.setUserInfo(model);
		return "main-page";
	}
    
    /**
     * Show the main page with an login error.
     * 
     * @param model the model
     * 
     * @return the name of the JSP-page
     */
    @RequestMapping(value = "login-error", method = RequestMethod.GET)
    public String loginError(Model model) {
		logger.debug("Show main page with login error");
		super.setUserInfo(model);
		model.addAttribute("loginError", true);
		return "main-page";
	}
    
    /**
     * Show the main page with an login error.
     * 
     * @param model the model
     * 
     * @return the name of the JSP-page
     */
    @RequestMapping(value = "auth-error", method = RequestMethod.GET)
    public String authError(Model model) {
		logger.debug("Show main page with login error");
		super.setUserInfo(model);
		model.addAttribute("authError", true);
		return "main-page";
	}

    /**
     * Get a ping (ajax request).
     * 
     * @param model the model
     * 
     * @return an empty {@link AjaxResponse}
     */
    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse get(Model model) {
		logger.debug("ping received ...");

		AjaxResponse response = null;
		try {
			response = new AjaxResponse(null);
			
		} catch (AccessDeniedException e) {
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}

		savedNodesNotifications(response);
		
		return response;
	}

    /**
     * Get a list of all current logged in users.
     * 
     * @param model the model
     * 
     * @return the {@link AjaxResponse} that includes all usernames or the error message
     */
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse getUsers(Model model) {
		logger.debug("get logged in users ...");

		AjaxResponse response = null;
		try {
			ArrayList<String> users = (ArrayList<String>) this.sessionTracker.getLoggedInUsernames();
			Collections.sort(users);
			response = new AjaxResponse(users);
			
		} catch (AccessDeniedException e) {
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}

		return response;
	}

    /**
     * Get the sessions for a user with the provided username.
     * 
     * @param username the username
     * @param model the model
     * @return the {@link AjaxResponse} that contains the session of the user or the error message
     */
    @RequestMapping(value = "/sessions", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse getSessions(String username, Model model) {
		logger.debug("get sessions for username '{}' ...", username);

		AjaxResponse response = null;
		try {
			ArrayList<String> sessions = (ArrayList<String>) this.sessionTracker.getSessionsForUsername(username);
			response = new AjaxResponse(sessions);
			
		} catch (AccessDeniedException e) {
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}

		return response;
	}

    /**
     * Get last request time stamp for a specified session (ajax request).
     * 
     * @param sessionId the id of the session for which the last request will be returned
     * @param model the model
     * @return the {@link AjaxResponse} containing the time stamp of the last request or the error message
     */
    @RequestMapping(value = "/lastRequest", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse getLastRequest(String sessionId, Model model) {
		logger.debug("get last request time stamp for sessionId= '{}' ...", sessionId);

		AjaxResponse response = null;
		try {
			Long ts = this.sessionTracker.getLastRequest(sessionId);
			response = new AjaxResponse(ts);
			
		} catch (AccessDeniedException e) {
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}

		return response;
	}
}
