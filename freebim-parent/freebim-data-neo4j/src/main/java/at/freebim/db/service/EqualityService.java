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

import java.util.List;

import at.freebim.db.domain.base.NodeIdentifyable;
import at.freebim.db.domain.base.rel.BaseRel;

/**
 * The interface for the equality service.
 *
 * @author rainer.breuss@uibk.ac.at
 */
public interface EqualityService {

	/**
	 * Test whether the passed object equals the object that is referenced by the
	 * passed relationship.
	 *
	 * @param <T>      Type of entity
	 * @param <R>      Type of relationship
	 * @param iterable The relationship.
	 * @param obj      The object.
	 * @return <code>true</code> whenever both objects are equal (have the same node
	 *         ID).
	 */
	<T extends NodeIdentifyable, R extends BaseRel<?, T>> boolean relatedEquals(final Iterable<R> iterable,
			final T obj);

	/**
	 * Test whether the passed object list equals the objects that are referenced by
	 * the passed relationship.
	 *
	 * @param <T>      Type of entity
	 * @param <R>      Type of relationship
	 * @param iterable The relationship.
	 * @param list     The objects.
	 * @return true if equal.
	 */
	<T extends NodeIdentifyable, R extends BaseRel<?, T>> boolean relatedEquals(final Iterable<R> iterable,
			final List<T> list);

}
