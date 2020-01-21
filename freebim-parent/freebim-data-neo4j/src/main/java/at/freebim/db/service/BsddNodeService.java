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

import at.freebim.db.domain.BsddNode;

/**
 * The service of the node/class {@link BsddNode}. It extends
 * {@link BaseNodeService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.BsddNode
 * @see at.freebim.db.service.BaseNodeService
 */
public interface BsddNodeService extends BaseNodeService<BsddNode> {

	/**
	 * Get the {@link BsddNode} with the corresponding guid.
	 *
	 * @param guid the guid
	 * @return the {@link BsddNode} with the corresponding guid
	 */
	BsddNode getByGuid(String guid);

	/**
	 * Create BSDD relations for nodes that have an EQUAL relation to other nodes
	 * (all at once).
	 *
	 * @return all node-ID's of targets of newly created relations.
	 */
	ArrayList<Long> spreadToEqualNodes();

	/**
	 * Set bsdd field to all equal nodes
	 *
	 * @return Number of affected nodes.
	 */
	Long setBsddFieldToEqualNodes();
}
