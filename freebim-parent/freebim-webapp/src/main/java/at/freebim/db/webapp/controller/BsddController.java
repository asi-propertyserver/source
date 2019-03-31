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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import at.freebim.db.domain.BsddNode;
import at.freebim.db.service.BaseNodeService;
import at.freebim.db.service.BsddNodeService;
import at.freebim.db.webapp.session.SessionTracker.SessionAction;

/**
 * The controller that handles bsdd node/entity ({@link BsddNode}).
 * It extends {@link BaseController}.
 * 
 * @see at.freebim.db.domain.BsddNode
 * @see at.freebim.db.webapp.controller.BaseController
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Controller
@RequestMapping("/bsdd")
public class BsddController extends BaseController<BsddNode> {

	/**
	 * The service that handles the {@link BsddNode}.
	 */
	@Autowired
	private BsddNodeService bsddNodeService;
	
    /**
     * Creates a new instance.
     */
    public BsddController() {
		super(BsddNode.class);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.webapp.controller.BaseController#getService()
	 */
	@Override
	protected BaseNodeService<BsddNode> getService() {
		return this.bsddNodeService;
	}


    /**
     * Get a node/entity of the type {@link BsddNode} that has the provided guid.
     * When an error occurs the error field in the response is set.
     * The same is true when the access was denied.
     * 
     * @param guid the guid of the {@link BsddNode}
     * @param model the model
     * @return the {@link AjaxResponse} that includes the found {@link BsddNode} or error messages
     */
    @RequestMapping(value = "/getByGuid", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse getByGuid(@RequestParam(value="guid", required=true) String guid, 
    		Model model) {
		logger.info("Get a single entity for {}, guid={}", this.getClass().getSimpleName(), guid);

		AjaxResponse response = null;
		try {
			// Delegate to bsddNodeService 
			BsddNode entity = this.bsddNodeService.getByGuid(guid);
			
			logger.info("\tfound nodeId=[{}] for guid={}", ((entity == null) ? "null" : entity.getNodeId()), guid);
			
			response = new AjaxResponse(entity);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
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
     * Creates a BSDD relation to other nodes that are already marked as equal.
     * When an error occurs the error field in the response is set.
     * The same is true when the access was denied.
     * 
     * @param model the model
     * @return the {@link AjaxResponse} that includes the ids of 
     * the nodes to which the bsdd relation was spread or error messages
     */
    @RequestMapping(value = "/spreadToEqualNodes", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse spreadToEqualNodes(Model model) {
		logger.debug("spreadToEqualNodes ");

		AjaxResponse response = null;
		try {
			// Delegate to bsddNodeService 
			ArrayList<Long> nodeIds = this.bsddNodeService.spreadToEqualNodes();
			for (Long nodeId : nodeIds) {
				this.notifySessionSaved(nodeId, SessionAction.RELATION_MODIFIED);
			}
			
			response = new AjaxResponse(nodeIds);
			
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
