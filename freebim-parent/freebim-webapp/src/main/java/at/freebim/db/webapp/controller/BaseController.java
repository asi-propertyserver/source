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
 * along with this program.  If not, see
 *{@literal<http://www.gnu.org/licenses/>}.
 *****************************************************************************/
package at.freebim.db.webapp.controller;

import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.BsddObject;
import at.freebim.db.service.BaseNodeService;
import at.freebim.db.service.BsddService;
import at.freebim.db.service.RelationService;
import at.freebim.db.service.RelationService.UpdateRelationsResult;
import at.freebim.db.webapp.models.request.DeleteModel;
import at.freebim.db.webapp.models.request.SaveRelationsModel;
import at.freebim.db.webapp.session.SessionTracker.SessionAction;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import javax.validation.Valid;
import javax.validation.ValidationException;
import org.neo4j.kernel.DeadlockDetectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is the basic class for all controllers, it extends
 * {@link BaseAuthController}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.webapp.controller.BaseAuthController
 */
@RestController
public abstract class BaseController<T extends BaseNode> extends BaseAuthController {

	/**
	 * The logger.
	 */
	static final Logger logger = LoggerFactory.getLogger(BaseController.class);

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

	private Class<T> clazz;

	/**
	 * Creates a new instance of the controller.
	 *
	 * @param clazz
	 */
	protected BaseController(Class<T> type) {
		super();
		this.clazz = type;
	}

	/**
	 * This function should return the service for the generic-class <b>T</b>. To
	 * enable the class to do basic operations on the database.
	 *
	 * @return the service
	 */
	protected abstract BaseNodeService<T> getService();

	/**
	 * Get a list of all components from the class <b>T</b> that the service can
	 * find. For this the function <code>this.getService().getAll(clean);</code> is
	 * used.
	 *
	 * @param clean this is a required request parameter it determines if only
	 *              relevant data is being loaded
	 * @param model the model
	 * @return the {@link AjaxResponse} to the request
	 */
	@ApiOperation(value = "Get a list of all nodes", notes = "Get a list of all nodes")
	@GetMapping(value = "/list")
	public @ResponseBody AjaxResponse list(@RequestParam(value = "clean", required = true) boolean clean) {
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
	 * @param model  the model
	 * @return the {@link AjaxResponse} to the request
	 */
	@ApiOperation(value = "Get the node that has the specified id", notes = "Get the node from the database that has the id that was provided as parameter")
	@GetMapping(value = "/get")
	public @ResponseBody AjaxResponse get(@RequestParam(value = "nodeId", required = true) Long nodeId) {
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
	 * Delete a single entity of the class <b>T</b>. To delete this entity the
	 * function <code>this.getService().deleteByNodeId(nodeId);</code> is used.
	 *
	 * @param nodeId the id of the entity that will be deleted
	 * @param model  the model
	 * @return the {@link AjaxResponse} to the request
	 */
	@ApiOperation(value = "Delete the node with the specified id", notes = "Delete the node from the database that has the id that was provided as parameter")
	@DeleteMapping(value = "/delete")
	public @ResponseBody AjaxResponse delete(@RequestBody(required = true) DeleteModel json) {
		logger.debug("Delete a single entity, nodeId={}", json.getNodeId());

		AjaxResponse response = null;
		// Delegate to service
		T entity;
		int counter = 100;
		while (counter-- > 0) {
			try {
				entity = this.getService().deleteByNodeId(json.getNodeId());
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
	 * Save the entity <b>T</b> that is encoded within the json-object. The function
	 * <code>this.getService().save(entity);</code> is used.
	 *
	 * @param json  the json-object
	 * @param model the model
	 * @return the {@link AjaxResponse} to the request
	 */
	@ApiOperation(value = "Save the node that was passed as parameter", notes = "Save the node in the database that was provided as parameter")
	@PostMapping(value = "/save")
	public @ResponseBody AjaxResponse save(@Valid @RequestBody(required = true) T entity) {
		logger.debug("Save entity: {}", entity.getNodeId());

		SessionAction action = ((entity.getNodeId() == null) ? SessionAction.INSERTED : SessionAction.SAVED);

		boolean modifiedBsddGuid = false;
		if (action == SessionAction.SAVED && BsddObject.class.isAssignableFrom(entity.getClass())) {
			modifiedBsddGuid = this.bsddService.didBsddGuidChange((BsddObject) entity);
		}

		AjaxResponse response = null;
		//compare objects----------------------------------------------------------
		T temp = null;
		if (entity.getNodeId() != null) {
			temp = this.getService().getByNodeId(entity.getNodeId());
			for(Field f: entity.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				for (Field y: temp.getClass().getDeclaredFields()) {
					y.setAccessible(true);
					if (!Modifier.isFinal(f.getModifiers()) && y.getName().equals(f.getName())) {
						try {
							if (!Modifier.isFinal(f.getModifiers()) && f.get(entity) != null) {
								y.set(temp, f.get(entity));
							}
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		entity = temp;
		//-------------------------------------------------------------------------

		if (entity instanceof FreebimUser) {
			FreebimUser user = (FreebimUser) entity;
			FreebimUser savedUser = ((user.getNodeId() == null) ? null
					: this.freebimUserService.getByNodeId(user.getNodeId()));
			if ((savedUser == null || !user.getPassword().equals(savedUser.getPassword()))
					&& (user.getPassword().length() > 31 || user.getPassword().length() < 5)) {
				response = new AjaxResponse(null);
				response.setError("Speichern nicht erfolgreich! Das Password sollte zwischen 5 und 31 Zeichen haben!");

				return response;
			}
		}

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
	 * @param nodeId   the id of the node for which the relation will be modified
	 * @param relArray the relation as json-object
	 * @param model    the model
	 * @return the {@link AjaxResponse} to the request
	 */
	@ApiOperation(value = "Save the relations of a node", notes = "Save the relations of a node. The node with the provided id will be loaded and and all relations that are not in the array will be deleted or those that are not already present will be added")
	@PutMapping(value = "/saveRelations")
	public @ResponseBody AjaxResponse saveRelations(@RequestBody(required = true) SaveRelationsModel json) {
		logger.debug("Save relations to nodeId {} ...", json.getNodeId());

		AjaxResponse response = null;
		int counter = 100;

		while (counter-- > 0) {
			try {
				// Delegate to service
				UpdateRelationsResult<?, ?> res = this.relationService.updateRelations(json.getNodeId(),
						json.getRelArray(), this.clazz);
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

		savedNodesNotifications(response);

		return response;
	}

	/**
	 * Get all the ids of the entities/nodes that are relevant. The function
	 * <code>this.getService().getRelevantNodeIds();</code> is used.
	 *
	 * @param model the model
	 * @return the {@link AjaxResponse} to the request, containing a list of all
	 *         relevant node ids
	 */
	@ApiOperation(value = "Get the ids of all relevant nodes.", notes = "Load the ids of the all the relevant nodes.")
	@GetMapping(value = "/ids")
	public @ResponseBody AjaxResponse getIds() {
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
