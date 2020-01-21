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
import java.util.Set;

import at.freebim.db.domain.NgramNode;
import at.freebim.db.domain.base.Coded;
import at.freebim.db.domain.base.Described;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.Ngramed;

/**
 * The service for the n-gram.
 *
 * @author rainer.breuss@uibk.ac.at
 */
public interface NgramService {

	/**
	 * Check if the service is active.
	 *
	 * @return returns if the service is active
	 */
	boolean isActive();

	/**
	 * Sets if the service is active or not.
	 *
	 * @param active pass <code>true</code> if the service should be active
	 *               <code>false</code> otherwise
	 */
	void setActive(boolean active);

	/**
	 * Find the matching nodes for a query {@link String}.
	 *
	 * @param s       the query {@link String}
	 * @param clazz   the class of n-gram relations ({@link Coded}, {@link Named},
	 *                {@link Described}
	 * @param toClass the class of destination nodes
	 * @return the {@link List} of matching nodes with there quality
	 *         ({@link MatchResult})
	 */
	List<MatchResult> find(String s, Class<? extends Ngramed> clazz, Class<?> toClass);

	/**
	 * Create the n-grams.
	 *
	 * @param s the string from which n-grams should be created
	 * @return the {@link List} of n-grams
	 */
	List<String> create(String s);

	/**
	 * Creates n-grams for the node that has the provided id.
	 *
	 * @param nodeId the id of the node
	 */
	void createForNode(Long nodeId);

	/**
	 * Creates the {@link NgramNode}s for the provided {@link String}.
	 *
	 * @param s the {@link String}
	 * @return a {@link Set} of {@link NgramNode}s that have been created from the
	 *         provided {@link String}
	 */
	Set<NgramNode> forString(String s);

	/**
	 * Delete the relationship to a provided node.
	 *
	 * @param node  the {@link Ngramed}
	 * @param clazz the relation to {@link Ngramed}
	 * @return the number of relations deleted
	 */
	int deleteFor(Ngramed node, Class<? extends Ngramed> clazz);

	/**
	 * Get matching node c. (fromNode)<-[fromNgramed]-()-[toNgramed]->(c). The
	 * quality of the relations should be greater than the provided value.
	 *
	 * @param fromNodeId  the starting node id
	 * @param fromNgramed the starting relation
	 * @param toNgramed   the ending relation
	 * @param toClass     the type of the found class
	 * @param minQuality  the minimal quality
	 * @return the {@link List} of matching nodes
	 */
	List<MatchResult> getMatch(Long fromNodeId, Class<? extends Ngramed> fromNgramed,
			Class<? extends Ngramed> toNgramed, Class<?> toClass, double minQuality);

	/**
	 * The result of n-gram operations.
	 *
	 * @author rainer.breuss@uibk.ac.at
	 */
	class MatchResult {

		/**
		 * The node {@link Ngramed}
		 */
		public Ngramed node;

		/**
		 * The quality of the n-gram.
		 */
		public double q;
	}

}
