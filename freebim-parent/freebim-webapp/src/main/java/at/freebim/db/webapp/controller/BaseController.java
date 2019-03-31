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

import java.io.IOException;
import java.util.ArrayList;

import javax.validation.ValidationException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.kernel.DeadlockDetectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.BsddObject;
import at.freebim.db.domain.base.NodeIdentifyable;
import at.freebim.db.dto.Relations;
import at.freebim.db.service.BaseNodeService;
import at.freebim.db.service.BsddService;
import at.freebim.db.service.RelationService;
import at.freebim.db.service.RelationService.UpdateRelationsResult;
import at.freebim.db.webapp.session.SessionTracker.SessionAction;

/**
 * This is the basic class for all controllers, 
 * it extends {@link BaseAuthController}.
 * 
 * @see at.freebim.db.webapp.controller.BaseAuthController
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public abstract class BaseController<T extends BaseNode> extends BaseAuthController {

	/**
	 * The logger.
	 */
	static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	/**
	 * The representation of the generic class <b>T</b>.
	 */
	private final Class<T> clazz;
	
	
	/**
	 * Helper for converting between java objects.
	 */
	private ObjectMapper mapper;
	
	/**
	 * The service that handles relations.
	 */
	@Autowired
	protected RelationService relationService;
	
	/**
	 * With this service you can check if the bsdd-guid did change.
	 */
	@Autowired
	private BsddService bsddService;
	
	
	/**
	 * Creates a new instance of the controller.
	 * 
	 * @param clazz
	 */
	protected BaseController(Class<T> clazz) {
		super();
		this.clazz = clazz;
		this.mapper = new ObjectMapper();
	}

	/**
	 * This function should return the service for the generic-class <b>T</b>.
	 * To enable the class to do basic operations on the database.
	 * 
	 * @return the service
	 */
	protected abstract BaseNodeService<T> getService();
	

    /**
     * Get a list of all components from the class <b>T</b> that the service can find.
     * For this the function <code>this.getService().getAll(clean);</code> is used.
     * 
     * @param clean this is a required request parameter it determines if only relevant data is being loaded
     * @param model the model
     * @return the {@link AjaxResponse} to the request
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse list(@RequestParam(value="clean", required=true) boolean clean, Model model) {
		logger.debug("get all entities");
		
		AjaxResponse response = null;
		try {
			// call Service 
			ArrayList<T> entries = this.getService().getAll(clean);
			logger.debug("got {} entries.", ((entries == null) ? "null" : entries.size()));
			response = new AjaxResponse(entries);
			
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
     * Get a single entity of the class <b>T</b>. To get this entity the function
     * <code>this.getService().getByNodeId(nodeId);</code> is used.
     * 
     * @param nodeId the id of the entity to get. This parameter is required
     * @param model the model
     * @return the {@link AjaxResponse} to the request
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse get(@RequestParam(value="nodeId", required=true) Long nodeId, 
    		Model model) {
		logger.debug("Get a single entity for {}, nodeId={}", this.getClass().getSimpleName(), nodeId);

		AjaxResponse response = null;
		try {
			// Delegate to service 
			T entity = this.getService().getByNodeId(nodeId);
			
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
     * Delete a single entity of the class <b>T</b>. To delete this entity the function
     * <code>this.getService().deleteByNodeId(nodeId);</code> is used.
     * 
     * @param nodeId the id of the entity that will be deleted
     * @param model the model
     * @return the {@link AjaxResponse} to the request
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse delete(@RequestParam(value="nodeId", required=true) Long nodeId, 
    		Model model) {
		logger.debug("Delete a single entity, nodeId={}", nodeId);

		AjaxResponse response = null;
		// Delegate to service 
		T entity;
		int counter = 100;
		while (counter-- > 0) {
			try {
				entity = this.getService().deleteByNodeId(nodeId);
				counter = 0;

				this.notifySessionSaved(entity.getNodeId(), SessionAction.DELETED);

				response = new AjaxResponse(entity);
				break;

			} catch (ConcurrencyFailureException e) {
				logger.info(e.getMessage());
				try {
					Thread.sleep(500L);
				} catch (InterruptedException e1) {
					;
				}
			} catch (DeadlockDetectedException e) {
				logger.info(e.getMessage());
				try {
					Thread.sleep(500L);
				} catch (InterruptedException e1) {
					;
				}
			} catch (AccessDeniedException e) {
				response = new AjaxResponse(null);
				response.setAccessDenied(true);
				break;
			} catch (Exception e) {
				logger.info(e.toString());
				response = new AjaxResponse(null);
				response.setError(e.toString());
				break;
			}
		}
		
		savedNodesNotifications(response);
		
		return response;
	}

    /**
     * Extracts the entity <b>T</b> from a json-object.
     * 
     * @param json the json-object from which the class will be extracted
     * @return the extracted entity
     */
    private T getEntityFromJson(String json) {
		T entity = null;
		try {
			entity = mapper.readValue(json, this.clazz);
		} catch (JsonParseException e) {
			logger.error("", e);
		} catch (JsonMappingException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} catch (Exception e) {
			logger.error("", e);
		}
		return entity;
    }


    /**
     * Save the entity <b>T</b> that is encoded within the json-object.
     * The function <code>this.getService().save(entity);</code> is used.
     * 
     * @param json the json-object
     * @param model the model
     * @return the {@link AjaxResponse} to the request
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse save(String json, Model model) {
		logger.debug("Save entity: {}", json);
		
		T entity = this.getEntityFromJson(json);
		SessionAction action = ((entity.getNodeId() == null) ? SessionAction.INSERTED : SessionAction.SAVED);
		
		boolean modifiedBsddGuid = false;
		if (action == SessionAction.SAVED && BsddObject.class.isAssignableFrom(entity.getClass())) {
			modifiedBsddGuid = this.bsddService.didBsddGuidChange((BsddObject) entity);
		}
		
		
		AjaxResponse response = null;
		
		int counter = 100;
		while (counter-- > 0) {
			try {
				// Delegate to service 
				entity = this.getService().save(entity);
				counter = 0;

				if (action == SessionAction.INSERTED) {
					this.notifySessionInserted(entity, action);
				} else {
					this.notifySessionSaved(entity.getNodeId(), action);
				}
				
				response = new AjaxResponse(entity);
				response.setBsddGuidChanged(modifiedBsddGuid);
				break;

			} catch (ConcurrencyFailureException e) {
				logger.info(e.getMessage());
				try {
					Thread.sleep(500L);
				} catch (InterruptedException e1) {
					;
				}
			} catch (DeadlockDetectedException e) {
				logger.info(e.getMessage());
				try {
					Thread.sleep(500L);
				} catch (InterruptedException e1) {
					;
				}
			} catch (ValidationException e) {
				response = new AjaxResponse(null);
				response.setError(e.getLocalizedMessage());
				break;
			} catch (AccessDeniedException e) {
				logger.error("AccessDenied: ", e);
				response = new AjaxResponse(null);
				response.setAccessDenied(true);
				break;
			} catch (Exception e) {
				logger.error("ERROR: ", e);
				response = new AjaxResponse(null);
				response.setError(e.toString());
				break;
			}
		}
		
		savedNodesNotifications(response);

		return response;
	}
    
    /**
     * Modify the relation from a entity with the specified node-id.
     * 
     * @param nodeId the id of the node for which the relation will be modified
     * @param relArray the relation as json-object
     * @param model the model
     * @return the {@link AjaxResponse} to the request
     */
    @RequestMapping(value = "/saveRelations", method = RequestMethod.POST)
    public @ResponseBody <FROM extends NodeIdentifyable, TO extends NodeIdentifyable>
    	AjaxResponse saveRelations(Long nodeId, String relArray, Model model) {
		logger.debug("Save relations to nodeId {} ...", nodeId);
		
		AjaxResponse response = null;

		try {
			Relations[] arr = mapper.readValue(relArray, Relations[].class);

			int counter = 100;
			while (counter-- > 0) {
				try {
					// Delegate to service 
					UpdateRelationsResult<?, ?> res = this.relationService.updateRelations(nodeId, arr); 
					counter = 0;
					for (Long id : res.affectedNodes) {
						this.notifySessionSaved(id, SessionAction.RELATION_MODIFIED);
					}
					response = new AjaxResponse(res.baseNode);
					break;
					
				} catch (ConcurrencyFailureException e) {
					logger.info(e.getMessage());
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e1) {
						;
					}
				} catch (DeadlockDetectedException e) {
					logger.info(e.getMessage());
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e1) {
						;
					}
				} catch (AccessDeniedException e) {
					logger.error("AccessDenied: ", e);
					response = new AjaxResponse(null);
					response.setAccessDenied(true);
					break;
				} catch (Exception e) {
					logger.error("ERROR: ", e);
					response = new AjaxResponse(null);
					response.setError(e.toString());
					break;
				}
			}

		} catch (JsonParseException e) {
			logger.error("", e);
		} catch (JsonMappingException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} catch (Exception e) {
			logger.error("", e);
		}
		
		savedNodesNotifications(response);
		
		return response;
    }

    /**
     * Get all the ids of the entities/nodes that are relevant.
     * The function <code>this.getService().getRelevantNodeIds();</code> is used.
     * 
     * @param model the model
     * @return the {@link AjaxResponse} to the request, containing a list of all relevant node ids
     */
    @RequestMapping(value = "/ids", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse getIds(Model model) {
		logger.debug("get all component ID's");
		
		AjaxResponse response = null;
		try {
			// Delegate to componentService 
			ArrayList<Long> entries = (ArrayList<Long>) this.getService().getAllRelevantNodeIds();
			logger.debug("got {} entries.", ((entries == null) ? "null" : entries.size()));
			response = new AjaxResponse(entries);
			
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
