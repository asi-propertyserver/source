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

import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.service.UuidIdentifyableService;

/**
 * The abstract base class for all controllers that handle a 
 * node/entity that extends {@link UuidIdentifyable}. 
 * The class itself extends {@link BaseController}.
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
     * Load a node/entity of the type <b>T</b> that has the provided uuid.
     * If an error occurs the error field of the response will be set.
     * The same is true when the access was denied.
     * 
     * @param u the uuid of the node/entity
     * @param model the model
     * @return the {@link AjaxResponse} that includes the found node/entity
     */
    @RequestMapping(value = "/getbyuuid", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse get(@RequestParam(value="u", required=true) String u, 
    		Model model) {
		logger.debug("Get a single entity, uuid={}", u);

		AjaxResponse response = null;
		try {
			// Delegate to service 
			UuidIdentifyableService<T> service = (UuidIdentifyableService<T>) this.getService();
			UuidIdentifyable entity = service.getByUuid(u);
			
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
