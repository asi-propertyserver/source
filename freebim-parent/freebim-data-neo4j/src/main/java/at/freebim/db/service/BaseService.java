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
package at.freebim.db.service;

import java.util.ArrayList;

import org.springframework.security.access.annotation.Secured;
/**
 * The base implementation of a service for a class <b>T</b>.
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface BaseService<T> {
	
	/**
	 * Get all objects of the class <b>T</b>.
	 * 
	 * @param onlyRelevant determines if you only get relevant objects
	 * 
	 * */
	public ArrayList<T> getAll(boolean onlyRelevant);
	
	/**
	 * Save a object of the class <b>T</b>.
	 * 
	 * @param node the object that should be saved
	 * 
	 * @return returns the saved object
	 * 
	 * */
	@Secured({"ROLE_EDIT", "ROLE_ADMIN"})
	public T save(T node);
	
	/**
	 * Get a object of the class <b>T</b> by its id.
	 * 
	 * @param nodeId the id of the object.
	 * 
	 * @return returns the found object
	 * 
	 * */
	public T getByNodeId(Long nodeId);
	
	/**
	 * Deletes a object.
	 * 
	 * @param node the object that should be deleted
	 * 
	 * @return returns the deleted object
	 * 
	 * */
	@Secured({"ROLE_EDIT", "ROLE_ADMIN"})
	public T delete(T node);
	
	/**
	 * Deletes the object that has the provided node id.
	 * 
	 * @param nodeId the id of the object
	 * 
	 * @return returns the deleted object
	 * */
	@Secured({"ROLE_EDIT", "ROLE_ADMIN"})
	public T deleteByNodeId(Long nodeId);

}
