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

import at.freebim.db.domain.Component;
import at.freebim.db.domain.Library;

/**
 * Repository for the node/class {@link Component}. It extends
 * {@link Neo4jRepository}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Component
 * @see org.springframework.data.neo4j.repository.Neo4jRepository
 */
public interface ComponentRepository extends Neo4jRepository<Component, Long> {

	/**
	 * Get the list of {@link Component}s that have the provided name and are in the
	 * provided library.
	 *
	 * @param name      the name of the {@link Component}s
	 * @param libraryId the id of the {@link Library}
	 */
	@Query("START lib=node({1}) MATCH (c:Component)-[:REFERENCES]->(lib) WHERE c.name={0} OR c.nameEn = {0} RETURN c")
	List<Component> getByNameFromLibrary(String name, Long libraryId);

	/**
	 * Gets the components that have the provided bsdd-guid.
	 *
	 * @param bsddGuid the bsdd guid
	 */
	@Query("MATCH (b:Component) WHERE b.bsddGuid={0} RETURN b")
	List<Component> findByBsddGuid(String bsddGuid);
}
