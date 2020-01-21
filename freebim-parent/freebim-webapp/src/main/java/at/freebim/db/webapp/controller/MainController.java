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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * The main controller. It extends {@link BaseAuthController}.
 * 
 * @see at.freebim.db.webapp.controller.BaseAuthController
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RestController
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
	@RequestMapping(value = "")
	public ModelAndView add(Model model, @RequestParam(required = false) Boolean loginError) {
		logger.debug("Show main page");
		model.addAttribute("loginError", loginError);
		super.setUserInfo(model);
		return new ModelAndView("main-page");
	}

	/**
	 * Show the main page with an login error.
	 * 
	 * @param model the model
	 * 
	 * @return the name of the JSP-page
	 */
	@RequestMapping(value = "login-error", method = RequestMethod.GET)
	public ModelAndView loginError(Model model) {
		logger.debug("Show main page with login error");
		super.setUserInfo(model);
		model.addAttribute("loginError", true);
		return new ModelAndView("main-page");
	}

	/**
	 * Show the main page with an login error.
	 * 
	 * @param model the model
	 * 
	 * @return the name of the JSP-page
	 */
	@RequestMapping(value = "auth-error", method = RequestMethod.GET)
	public ModelAndView authError(Model model) {
		logger.debug("Show main page with login error");
		super.setUserInfo(model);
		model.addAttribute("authError", true);
		return new ModelAndView("main-page");
	}

	//TODO: comes with next release
	/*
	@RequestMapping(value = "api-docs", method = RequestMethod.GET)
	public ModelAndView showApiDocs(Model model) {
		logger.debug("Show api docs");
		super.setUserInfo(model);
		return new ModelAndView("api-doc");
	}*/

}
