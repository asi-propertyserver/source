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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.Coded;
import at.freebim.db.domain.base.Described;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.Ngramed;
import at.freebim.db.domain.base.State;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.service.NgramService;
import at.freebim.db.service.NgramService.MatchResult;
import at.freebim.db.service.RelationService;

/**
 * This controller can be used to search for things in the database.
 * It extends {@link BaseAuthController}.
 * 
 * @see at.freebim.db.webapp.controller.BaseAuthController
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Controller
@RequestMapping("/search")
public class SearchController extends BaseAuthController {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

	/**
	 * The service that handles the n-grams.
	 */
	@Autowired
	private NgramService service;
	
	/**
	 * The service that handles the relations.
	 */
	@Autowired
	private RelationService relationService;
	    
    /**
     * Make a fulltext search. You have to provide a string to search after and then 
     * a type after what you search. A type is for example, the type 
     * ({@link Named}, {@link Coded}, {@link Described}, ...) of nodes, you 
     * have to query over to match the search string against.
     * 
     * @param searchstring the string after that you want to search. This parameter is required
     * @param searchtype the type after what you want to search. This parameter is required
     * @param model the model
     * @return the {@link AjaxResponse} that includes the node that has been found or the error message
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse search(
    		@RequestParam(value="searchstring", required=true) String searchstring, 
    		@RequestParam(value="searchtype", required=true) String searchtype,
    		Model model) {
    	
		logger.debug("search r '{}' search for [{}] ...", searchtype, searchstring);
		
		AjaxResponse response = null;
		try {
			Class<? extends Ngramed> clazz = null;
			switch (searchtype) {
			case "Named" : clazz = Named.class; break;
			case "Coded" : clazz = Coded.class; break;
			case "Described" : clazz = Described.class; break;
			case "freebim-id" : 
				UuidIdentifyable res = this.relationService.findByFreebimId(searchstring);
				logger.debug("found [{}].", ((res == null) ? "null" : res.getNodeId()));
				response = new AjaxResponse(res);
				return response;
			case "bsdd-guid" :
				ArrayList<BaseNode> bn = this.relationService.findByBsddGuid(searchstring);
				logger.debug("found [{}].", ((bn == null) ? "null" : bn.size()));
				response = new AjaxResponse(bn);
				return response;
			case "state" :
				State s = State.fromCode(Integer.valueOf(searchstring));
				ArrayList<StatedBaseNode> stateRes = this.relationService.findByState(s.name());
				logger.debug("found [{}] entries.", ((stateRes == null) ? "null" : stateRes.size()));
				response = new AjaxResponse(stateRes);
				return response;
			default: 
				logger.error("searchtype [" + searchtype + "] not supported.");
				return new AjaxResponse(null);
			}
			// Delegate to service 
			ArrayList<MatchResult> res = (ArrayList<MatchResult>) this.service.find(searchstring, clazz, null);
			response = new AjaxResponse(res);

		} catch (AccessDeniedException e) {
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error("Exception caught: ", e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		
		return response;
	}

    /**
     * Make a search using n-grams. You have to specify a string after what you want to search 
     * and a class name (class that extends {@link Ngramed}).
     * 
     * @param searchstring the string after what you search. 
     * This parameter is required
     * @param clazzName the name of the class. Must extend {@link Ngramed}. 
     * This parameter is required
     * @param model the model
     * @return the {@link AjaxResponse} that includes the found list a ngram nodes or the erro message
     */
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse find(
    		@RequestParam(value="searchstring", required=true) String searchstring, 
    		@RequestParam(value="clazz", required=true) String clazzName,
    		Model model) {
    	
		logger.debug("find r '{}' search for class [{}] ...", searchstring, clazzName);
		
		AjaxResponse response = null;
		try {
			Class<? extends Ngramed> clazz = Named.class;
			Class<?> toClazz = Class.forName(clazzName);

			// Delegate to service 
			ArrayList<MatchResult> res = (ArrayList<MatchResult>) this.service.find(searchstring, clazz, toClazz);
			response = new AjaxResponse(res);

		} catch (AccessDeniedException e) {
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error("Exception caught: ", e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		
		return response;
	}



    
}
