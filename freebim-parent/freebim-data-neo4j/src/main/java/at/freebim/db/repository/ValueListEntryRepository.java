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

import at.freebim.db.domain.ValueListEntry;

/**
 * The repository for the node/class {@link ValueListEntry}. It extends
 * {@link Neo4jRepository}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.ValueListEntry
 * @see org.springframework.data.neo4j.repository.Neo4jRepository
 */
public interface ValueListEntryRepository extends Neo4jRepository<ValueListEntry, Long> {

	/**
	 * Gets a {@link List} of {@link ValueListEntry} that have the provided
	 * bsdd-guid.
	 *
	 * @param bsddGuid the bsdd-guid
	 * @return {@link List} of {@link ValueListEntry}
	 */
	@Query("MATCH (vle:ValueListEntry) WHERE vle.bsddGuid={0} RETURN vle")
	List<ValueListEntry> findByBsddGuid(String bsddGuid);

	/**
	 * Gets a {@link List} of {@link ValueListEntry} that have the provided name.
	 *
	 * @param name the field name or nameEn of {@link ValueListEntry}
	 * @return {@link List} of {@link ValueListEntry}
	 */
	@Query("MATCH (vle:ValueListEntry) WHERE vle.name={0} OR vle.nameEn={0} RETURN vle")
	List<ValueListEntry> getByName(String name);

}
