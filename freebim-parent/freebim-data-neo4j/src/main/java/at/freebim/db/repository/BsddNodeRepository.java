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
import org.springframework.data.neo4j.repository.GraphRepository;

import at.freebim.db.domain.BsddNode;

/**
 * Repository for the node/class {@link BsddNode}.
 * It extends {@link GraphRepository}.
 * 
 * 
 * @see at.freebim.db.domain.BsddNode
 * @see org.springframework.data.neo4j.repository.GraphRepository
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface BsddNodeRepository extends GraphRepository<BsddNode> {

	/**
	 * Gets the BsddNode that has the provided guid. 
	 * 
	 * 
	 * @param guid the guid from the BsddNode
	 * */
	@Query("MATCH (n:BsddNode) WHERE n.guid={0} RETURN n")
	public BsddNode getByGuid(String guid);
	
}
