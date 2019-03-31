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

import java.util.ArrayList;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import at.freebim.db.domain.Library;
import at.freebim.db.domain.base.rel.RelationType;

/**
 * The repository for the node/class {@link Library}.
 * It extends {@link GraphRepository}.
 * 
 * @see at.freebim.db.domain.Library
 * @see org.springframework.data.neo4j.repository.GraphRepository
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface LibraryRepository extends GraphRepository<Library> {

	/**
	 * Gets the {@link Library} node that has the provided name.
	 * 
	 * @param name the name
	 * @return {@link Library}
	 * */
	@Query("MATCH (n:Library) where n.name={0} RETURN n")
	public Library get(String name);

	/**
	 * Get all libraries a specified contributor is responsible for.
	 * 
	 * @param nodeId ID of contributor
	 * @return returns a {@link ArrayList} of {@link Library}
	 */
	@Query("START c=node({0}) MATCH (c)-[:" + RelationType.RESPONSIBLE + "]->(lib:Library) RETURN lib")
	public ArrayList<Library> getForContributorId(Long nodeId);
	
	/**
	 * Get all refIdName's for a specified library.
	 * 
	 * @param nodeId ID of {@link Library}
	 * @return returns a {@link ArrayList} of {@link String}
	 */
	@Query("START l=node({0}) MATCH (n)-[ref:" + RelationType.REFERENCES + "]->(l) RETURN DISTINCT ref.refIdName")
	public ArrayList<String> getRefIdNames(Long nodeId);

}
