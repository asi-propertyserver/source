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
package at.freebim.db.domain.base;

/**
 * The basic interface that implement all nodes, that are bsdd-objects. This
 * interface extends {@link NodeIdentifyable}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.NodeIdentifyable
 */
public interface BsddObject extends NodeIdentifyable {

	/**
	 * Get the bsdd guid.
	 *
	 * @return the bsdd guid
	 */
	String getBsddGuid();

	/**
	 * Set the bsdd guid.
	 *
	 * @param guid the bsdd guid to set
	 */
	void setBsddGuid(String guid);
}
