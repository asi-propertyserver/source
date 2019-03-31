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

import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.service.BigBangNodeService;
import at.freebim.db.service.RelationService;
import at.freebim.db.service.RelationService.RelationResult;
import at.freebim.db.service.RelationService.SimplePathResult;

/**
 * This controller can handles relations between nodes.
 * It extends {@link BaseAuthController}.
 * 
 * @see at.freebim.db.webapp.controller.BaseAuthController
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Controller
@RequestMapping("/relations")
public class RelationsController extends BaseAuthController {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(RelationsController.class);

	/**
	 * The service that handles the root node {@link BigBangNode}.
	 */
	@Autowired
	private BigBangNodeService bigBangNodeService;
	
	
	/**
	 * The service that handles relations.
	 */
	@Autowired
	private RelationService relationService;
	
    /**
     * Get the {@link BigBangNode}.
     * 
     * @param model the model
     * @return the {@link AjaxResponse} that includes the {@link BigBangNode} or the error message
     */
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse init(Model model) {
		logger.debug("Get the BigBangNode");
		
		AjaxResponse response = null;
		try {
			// Delegate to service 
			BigBangNode bbn = this.bigBangNodeService.getBigBangNode();
			response = new AjaxResponse(bbn);
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
     * Get all ingoing or outgoing relations of a node.
     * 
     * @param nodeId the id of the node
     * @param model the model 
     * @return the {@link AjaxResponse} that includes the relations or the error message
     */
    @RequestMapping(value = "/load", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse load(@RequestParam(value="nodeId", required=true) Long nodeId,
    		Model model) {
		logger.debug("Load relations for node[{}]", nodeId);

		AjaxResponse response = null;
		try {
			// Delegate to service 
			ArrayList<RelationResult> res = this.relationService.getAllRelatedInOut(nodeId);
			response = new AjaxResponse(res);
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
     * Get a node by its id.
     * 
     * @param nodeId the id of the node
     * @param model the model
     * @return the {@link AjaxResponse} that includes the node or the error message
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse get(@RequestParam(value="nodeId", required=true) Long nodeId, 
    		Model model) {
		logger.debug("Get a single entity, nodeId={}", nodeId);

		AjaxResponse response = null;
		try {
			// Delegate to service 
			BaseNode entity = this.relationService.getNodeById(nodeId);
			
			response = new AjaxResponse(entity);
			
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
     * Get all paths ({@link SimplePathResult}) to a node with the provided id.
     * 
     * @param nodeId the id of the node
     * @param max the maximum number of paths
     * @param onlyValid if <code>true</code> all paths have to be valid
     * @param model the model
     * @return the {@link AjaxResponse} that includes the paths or the erro message
     */
    @RequestMapping(value = "/getAllPaths", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse get(@RequestParam(value="nodeId", required=true) Long nodeId,
    		@RequestParam(value="max", required=true) Long max,
    		@RequestParam(value="onlyValid", required=true) boolean onlyValid, 
    		Model model) {
		logger.debug("Get all paths, nodeId={}", nodeId);

		AjaxResponse response = null;
		try {
			// Delegate to service 
			response = new AjaxResponse((ArrayList<SimplePathResult>) this.relationService.getAllPaths(nodeId, onlyValid, max));
			
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
}