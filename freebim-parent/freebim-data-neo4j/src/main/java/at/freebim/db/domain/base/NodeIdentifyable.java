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

import java.io.Serializable;

/**
 * Interface that forces you to create a getter method for an id.
 *
 * @author rainer.breuss@uibk.ac.at
 */
public interface NodeIdentifyable extends Serializable {

	/**
	 * Gets the unique id of this node. Unique does not mean consistent. When nodes
	 * get deleted the id can be reused in new nodes.
	 *
	 * @return the node id
	 */
	Long getNodeId();
}
