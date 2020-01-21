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

import at.freebim.db.domain.BsddNode;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Parameter;

/**
 * The repository for the node/class {@link Parameter}. It extends
 * {@link Neo4jRepository}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Parameter
 * @see org.springframework.data.neo4j.repository.Neo4jRepository
 */
public interface ParameterRepository extends Neo4jRepository<Parameter, Long> {

	/**
	 * Gets a {@link List} of {@link Parameter} from a library that have the
	 * provided libraryId and the corresponding name.
	 *
	 * @param name      the name of the field from {@link Parameter}
	 * @param libraryId the id of the {@link Library}
	 * @return {@link List} of {@link Parameter}
	 */
	@Query("START lib=node({1}) MATCH (p:Parameter)-[:REFERENCES]->(lib) WHERE p.name={0} OR p.nameEn = {0} RETURN p")
	List<Parameter> getByNameFromLibrary(String name, Long libraryId);

	/**
	 * Gets a {@link List} of {@link Parameter} that have the provided bsdd-guid.
	 *
	 * @param bsddGuid the bsdd-guid {@link BsddNode}.
	 * @return {@link List} of {@link Parameter}
	 */
	@Query("MATCH (b:Parameter) WHERE b.bsddGuid={0} RETURN b")
	List<Parameter> findByBsddGuid(String bsddGuid);
}
