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

import at.freebim.db.domain.base.BaseNode;

/**
 * The service for a class <b>T</b> that extends {@link BaseNode}. The service
 * extends {@link BaseService}.
 *
 * @author rainer.breuss@uibk.ac.at
 */
public interface BaseNodeService<T extends BaseNode> extends BaseService<T> {

	/**
	 * Apply filter before the class/node is being inserted.
	 *
	 * @param node a class that extends {@link BaseNode}.
	 * @return the filtered class/node
	 */
	T filterBeforeInsert(T node);

	/**
	 * Apply filter after the class/node has been inserted.
	 *
	 * @param node a class that extends {@link BaseNode}.
	 * @return the filtered class/node
	 */
	T filterAfterInsert(T node);

	/**
	 * Apply filter before the class/node is being saved.
	 *
	 * @param node a class that extends {@link BaseNode}.
	 * @return the filtered class/node
	 */
	T filterBeforeSave(T node);

	/**
	 * Apply filter after the class/node has been saved.
	 *
	 * @param node a class that extends {@link BaseNode}.
	 * @return the filtered class/node
	 */
	T filterAfterSave(T node);

	/**
	 * Apply a filter to the result of an action.
	 *
	 * @param node a class that extends {@link BaseNode}.
	 * @param now  when should the filter be applied
	 * @return the filtered class/node
	 */
	T filterResponse(T node, Long now);

	/**
	 * Apply a filter before the class/node is being deleted.
	 *
	 * @param node a class that extends {@link BaseNode}.
	 * @return the filtered class/node
	 */
	T filterBeforeDelete(T node);

	/**
	 * Apply a filter after the class/node has been deleted.
	 *
	 * @param node a class that extends {@link BaseNode}.
	 * @return the filtered class/node
	 */
	T filterAfterDelete(T node);

	/**
	 * Load all relevant node/class objects that extend {@link BaseNode}.
	 *
	 * @return {@link ArrayList} of relevant class/node objects that extend
	 *         {@link BaseNode}.
	 */
	ArrayList<T> getAllRelevant();

	/**
	 * Load all relevant node/class ids.
	 *
	 * @return {@link ArrayList} of relevant ids of class/node objects that extend
	 *         {@link BaseNode}.
	 */
	ArrayList<Long> getAllRelevantNodeIds();
}
