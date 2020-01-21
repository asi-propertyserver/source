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

import at.freebim.db.domain.Discipline;

/**
 * Repository for the node/class {@link Discipline}. It extends
 * {@link Neo4jRepository}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Discipline
 * @see org.springframework.data.neo4j.repository.Neo4jRepository
 */
public interface DisciplineRepository extends Neo4jRepository<Discipline, Long> {

	/**
	 * Gets a list of {@link Discipline} that have the provided bsdd-guid.
	 *
	 * @param bsddGuid the bsdd-guid
	 */
	@Query("MATCH (b:Discipline) WHERE b.bsddGuid={0} RETURN b")
	List<Discipline> findByBsddGuid(String bsddGuid);

}
