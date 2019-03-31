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


import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles and retrieves the login or denied page depending on the URI template.
 * It extends {@link BaseAuthController}.
 * 
 * @see at.freebim.db.webapp.controller.BaseAuthController
 * 
 * @author rainer.breuss@uibk.ac.at
 */
@Controller
@RequestMapping("/")
public class LoginLogoutController extends BaseAuthController {
        
	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(LoginLogoutController.class);

	/**
	 * Handles and retrieves the login JSP page.
	 * 
	 * @param error determines of the page should be displayed with an error message
	 * @param model the model
	 * @return the name of the JSP page
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLoginPage(@RequestParam(value="error", required=false) boolean error, 
			Model model) {
		logger.debug("Received request to show login page ...");

		super.setUserInfo(model);
		// Add an error message to the model if login is unsuccessful
		// The 'error' parameter is set to true based on the when the authentication has failed. 
		// We declared this under the defaultFailureUrl attribute inside the spring-security.xml

		if (error == true) {
			logger.debug("with error");
			// Assign an error message
			model.addAttribute("error", "You have entered an invalid username or password!");
		} else {
			model.addAttribute("error", "");
		}
		
		// This will resolve to /WEB-INF/jsp/loginpage.jsp
		return "loginpage";
	}
	
	/**
	 * Handles and retrieves the login JSP page.
	 * 
	 * @param force determines if it was a forced logout
	 * @param model the model
	 * @return the name of the JSP page
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String getLogoutPage(@RequestParam(value="force", required=false) boolean force, 
			Model model) {
		logger.debug("Received request to show logout page, force=" + force);

		super.setUserInfo(model);
				// Add an error message to the model if login is unsuccessful
		// The 'error' parameter is set to true based on the when the authentication has failed. 
		// We declared this under the defaultFailureUrl attribute inside the spring-security.xml

		
		return "main-page";
	}
	
	/**
	 * Handles and retrieves the login JSP page
	 * j_spring_security_logout
	 * 
	 * @param model the model
	 * 
	 * @return the name of the JSP page
	 */
	@RequestMapping(value = "/dologout", method = RequestMethod.POST)
	public String getLoggedOutPage(Model model) {
		logger.debug("do log out");
		super.setUserInfo(model);
		Map<String, Object> map = model.asMap();
		Set<String> keys = map.keySet();
		for (String key : keys) {
			logger.debug("Model: {} \t-> {}", key, map.get(key));
		}
		return "tree-page";
	}
	
	/**
	 * Handles and retrieves the denied JSP page. This is shown whenever a regular user
	 * tries to access an admin only page.
	 * 
	 * @return the name of the JSP page
	 */
	@RequestMapping(value = "/denied", method = { RequestMethod.GET, RequestMethod.POST })
 	public String getDeniedPage() {
		logger.debug("Received request to show denied page");
		
		// This will resolve to /WEB-INF/jsp/deniedpage.jsp
		return "deniedpage";
	}
}