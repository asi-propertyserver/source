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
import org.springframework.data.neo4j.repository.GraphRepository;

import at.freebim.db.domain.Phase;

/**
 * The repository for the node/class {@link Phase}.
 * It extends {@link GraphRepository}.
 * 
 * @see at.freebim.db.domain.Phase
 * @see org.springframework.data.neo4j.repository.GraphRepository
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface PhaseRepository extends GraphRepository<Phase> {

	/**
	 * Gets a node/class {@link Phase} that has the provided code.
	 * 
	 * @param c the code
	 * 
	 * @return {@link Phase}
	 * 
	 * */
	@Query("MATCH (n:Phase) WHERE n.code={0} RETURN n")
	public Phase getByCode(String c);
	
	/**
	 * Gets the node/class {@link Phase} that has the provided uuid.
	 * 
	 * @param uuid the uuid
	 * 
	 * @return {@link Phase} 
	 * 
	 * */
	@Query("MATCH (n:Phase) WHERE n.uuid={0} RETURN n")
	public Phase getByUuid(String uuid);

	
	/**
	 * Gets a {@link List} of {@link Phase} that have the corresponding bsdd guid.
	 * 
	 * 
	 * @param bsddGuid the bsdd guid
	 * 
	 * @return {@link List} of {@link Phase}
	 * */
	@Query("MATCH (b:Phase) WHERE b.bsddGuid={0} RETURN b")
	public List<Phase> findByBsddGuid(String bsddGuid);

}
