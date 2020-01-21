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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.freebim.db.domain.MessageNode;
import at.freebim.db.service.BaseNodeService;
import at.freebim.db.service.MessageNodeService;

/**
 * The controller for the node/entity {@link MessageNode}. It extends
 * {@link BaseController}.
 * 
 * @see at.freebim.db.domain.MessageNode
 * @see at.freebim.db.webapp.controller.BaseController
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RestController
@RequestMapping("/messages")
public class MessageNodeController extends BaseController<MessageNode> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(MessageNodeController.class);

	/**
	 * The service that handles the node/entity {@link MessageNode}.
	 */
	@Autowired
	private MessageNodeService messageNodeService;

	/**
	 * Creates a new instance.
	 */
	public MessageNodeController() {
		super(MessageNode.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.webapp.controller.BaseController#getService()
	 */
	@Override
	protected BaseNodeService<MessageNode> getService() {
		return this.messageNodeService;
	}

	/**
	 * Get all current messages.
	 * 
	 * @return the {@link AjaxResponse} that includes all messages of the type
	 *         {@link MessageNode} or the error message
	 */
	@GetMapping(value = "/getCurrent")
	public @ResponseBody AjaxResponse getCurrentMessages() {
		logger.debug("getCurrentMessages ...");

		AjaxResponse response = null;
		try {
			ArrayList<MessageNode> res = (ArrayList<MessageNode>) ((MessageNodeService) this.getService())
					.getCurrentMessages();
			response = new AjaxResponse(res);
			logger.debug("getCurrentMessages returned [{}] messages.", ((res == null) ? "null" : res.size()));

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
	 * Set a message as seen for the current logged in user.
	 * 
	 * @param nodeId the id of the {@link MessageNode} that will be set to seen
	 * @return the {@link AjaxResponse} that includes if the message has been set
	 *         successfully to seen or the error message
	 */
	@PostMapping(value = "/setSeen")
	public @ResponseBody AjaxResponse setSeen(@RequestParam(value = "nodeId", required = true) Long nodeId) {
		logger.debug("setSeen ...");
		AjaxResponse response = null;
		try {
			((MessageNodeService) this.getService()).setSeen(nodeId);
			response = new AjaxResponse(true);
			logger.debug("setSeen finished.");

		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			logger.error(e.toString(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
	}

	/**
	 * Set a message as closed for the current logged in user.
	 * 
	 * @param nodeId the id of the {@link MessageNode} that will be set to closed
	 * @return the {@link AjaxResponse} that includes if the message has been set
	 *         successfully to closed or the error message
	 */
	@PostMapping(value = "/setClosed")
	public @ResponseBody AjaxResponse setClosed(@RequestParam(value = "nodeId", required = true) Long nodeId) {
		logger.debug("setClosed ...");
		AjaxResponse response = null;
		try {
			((MessageNodeService) this.getService()).setClosed(nodeId);
			response = new AjaxResponse(true);
			logger.debug("setClosed finished.");

		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			logger.error(e.toString(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
	}
}
