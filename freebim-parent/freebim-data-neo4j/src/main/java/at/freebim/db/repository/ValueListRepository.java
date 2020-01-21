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

import at.freebim.db.domain.ValueList;

/**
 * The repository of the node/class {@link ValueList}. It extends
 * {@link Neo4jRepository}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.ValueList
 * @see org.springframework.data.neo4j.repository.Neo4jRepository
 */
public interface ValueListRepository extends Neo4jRepository<ValueList, Long> {

	/**
	 * Gets a {@link List} of {@link ValueList} that have the provided bsdd-guid.
	 *
	 * @param bsddGuid the bsdd-guid of the node/class {@link ValueList}
	 * @return {@link List} of {@link ValueList}
	 */
	@Query("MATCH (vl:ValueList) WHERE vl.bsddGuid={0} RETURN vl")
	List<ValueList> findByBsddGuid(String bsddGuid);

	/**
	 * Gets a {@link List} of {@link ValueList} that have the provided name.
	 *
	 * @param name the field name or nameEn of {@link ValueList}
	 * @return {@link List} of {@link ValueList}
	 */
	@Query("MATCH (vl:ValueList) WHERE vl.name={0} OR vl.nameEn={0} RETURN vl")
	List<ValueList> findByName(String name);

}
