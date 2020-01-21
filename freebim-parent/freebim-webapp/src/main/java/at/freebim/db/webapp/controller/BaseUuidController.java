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

import java.io.Serializable;

import javax.validation.Valid;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.service.UuidIdentifyableService;
import at.freebim.db.webapp.models.request.BaseUuidGetModel;
import io.swagger.annotations.ApiOperation;

/**
 * The abstract base class for all controllers that handle a node/entity that
 * extends {@link UuidIdentifyable}. The class itself extends
 * {@link BaseController}.
 * 
 * @see at.freebim.db.webapp.controller.BaseController
 * @see at.freebim.db.domain.base.UuidIdentifyable
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public abstract class BaseUuidController<T extends UuidIdentifyable> extends BaseController<T> {

	/**
	 * Creates a new instance.
	 * 
	 * @param clazz the representation of a class
	 */
	protected BaseUuidController(Class<T> clazz) {
		super(clazz);
	}

	/**
	 * Load a node/entity of the type <b>T</b> that has the provided uuid. If an
	 * error occurs the error field of the response will be set. The same is true
	 * when the access was denied.
	 * 
	 * @param u     the uuid of the node/entity
	 * @param model the model
	 * @return the {@link AjaxResponse} that includes the found node/entity
	 */
	@ApiOperation(value = "Get the node that has the provided UUID", notes = "Load the node from the databse that has the provided UUID")
	@GetMapping(value = "/getbyuuid")
	public @ResponseBody AjaxResponse get(@Valid @RequestBody(required = true) BaseUuidGetModel json) {
		logger.debug("Get a single entity, uuid={}", json.getU());

		AjaxResponse response = null;
		try {
			// Delegate to service
			UuidIdentifyableService<T> service = (UuidIdentifyableService<T>) this.getService();
			UuidIdentifyable entity = service.getByUuid(json.getU());

			response = new AjaxResponse((Serializable) entity);
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
