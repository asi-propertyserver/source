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
package at.freebim.db.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import at.freebim.db.domain.ParameterSet;
import at.freebim.db.domain.base.rel.RelationType;

/**
 * The repository for the node/class {@link ParameterSet}. it extends
 * {@link Neo4jRepository}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.ParameterSet
 * @see org.springframework.data.neo4j.repository.Neo4jRepository
 */
public interface ParameterSetRepository extends Neo4jRepository<ParameterSet, Long> {

	/**
	 * Get a list of {@link ParameterSet}s that are connected to a node that has the
	 * provided id.
	 *
	 * @param nodeId the id of the node
	 * @return the {@link Iterable} of {@link ParameterSet}s
	 */
	@Query("START n=node({0}) MATCH (n)-[:" + RelationType.HAS_PARAMETER_SET + "]->(ps) RETURN ps")
	Iterable<ParameterSet> getFor(Long nodeId);

	/**
	 * Find the list of {@link ParameterSet}s that have the provided bsdd-guid.
	 *
	 * @param bsddGuid the bsdd-guid
	 * @return the {@link List} of {@link ParameterSet}s
	 */
	@Query("MATCH (b:ParameterSet) WHERE b.bsddGuid={0} RETURN b")
	List<ParameterSet> findByBsddGuid(String bsddGuid);

}
