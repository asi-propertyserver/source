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

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import at.freebim.db.domain.Company;

/**
 * Repository for the node/class {@link Company}. It extends
 * {@link Neo4jRepository}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Company
 * @see org.springframework.data.neo4j.repository.Neo4jRepository
 */
public interface CompanyRepository extends Neo4jRepository<Company, Long> {

	/**
	 * Gets the Company that has the provided name.
	 *
	 * @param name the name
	 */
	@Query("MATCH (n:Company) where n.name={0} RETURN n")
	Company get(String name);
}
