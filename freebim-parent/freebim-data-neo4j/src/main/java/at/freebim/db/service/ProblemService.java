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

import at.freebim.db.domain.Library;

/**
 * The interface for the problem service.
 *
 * @author rainer.breuss@uibk.ac.at
 */
public interface ProblemService {

	/**
	 * Get all nodeId's of Parameters without a Measure.
	 *
	 * @return the {@link ArrayList} of nodeId's
	 */
	ArrayList<Long> getMissingMeasure();

	/**
	 * Get all nodeId's of Measure without ValueList and without DataType.
	 *
	 * @return the {@link ArrayList} of nodeId's
	 */
	ArrayList<Long> getEmptyMeasure();

	/**
	 * Get all nodeId's of Components without any Parameters on their path to
	 * BigBangNode.
	 *
	 * @return the {@link ArrayList} of nodeId's
	 */
	ArrayList<Long> getComponentWithoutParameters();

	/**
	 * Get all nodeId's of Parameters referenced by a HasParameter relations which
	 * is referencing a deleted phase.
	 *
	 * @return the {@link ArrayList} of nodeID's
	 */
	ArrayList<Long> deletedPhase();

	/**
	 * Get all parameter-ID's of parameters that could be assigned to parent node.
	 *
	 * @param libid the if of the {@link Library}
	 * @return the {@link ArrayList} of nodeID's
	 */
	ArrayList<IdPair> specializableParameters(Long libid);

	/**
	 * Get all ID's of parameters that are assigned multiple times along a single
	 * path.
	 *
	 * @return An <code>IdTriple</code> that contains the first Component in a, the
	 *         2nd Component in b and the Parameter in c.
	 */
	ArrayList<IdTriple> multipleParameterAssignment();

	/**
	 * Represents a pair of nodes with id and name.
	 *
	 * @author rainer.breuss@uibk.ac.at
	 */
	class IdPair {
		public Long a; // id of a node
		public String an; // name of a node
		public Long b; // id of a node
		public String bn; // the name of a node
	}

	/**
	 * Represents a triple of node with id and name. This class extends
	 * {@link IdPair}.
	 *
	 * @author rainer.breuss@uibk.ac.at
	 * @see at.freebim.db.service.ProblemService.IdTriple
	 */
	class IdTriple extends IdPair {
		public Long c; // id of a node
		public String cn; // name of a node
	}
}
