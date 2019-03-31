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

import at.freebim.db.domain.Measure;

/**
 * The repository for the node/class {@link Measure}.
 * It extends {@link GraphRepository}.
 * 
 * @see at.freebim.db.domain.Measure
 * @see org.springframework.data.neo4j.repository.GraphRepository
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface MeasureRepository extends GraphRepository<Measure> {

	/**
	 * Gets all {@link Measure} nodes that have the provided bsdd-guid.
	 * 
	 * @param bsddGuid the bsdd guid
	 * @return {@link List} of {@link Measure} nodes
	 * */
	@Query("MATCH (m:Measure) WHERE m.bsddGuid={0} RETURN m")
	public List<Measure> findByBsddGuid(String bsddGuid);

	/**
	 * Gets all {@link Measure} nodes that have the provided name.
	 * 
	 * @param name the name
	 * @return {@link List} of {@link Measure} objects
	 * */
	@Query("MATCH (m:Measure) WHERE m.name={0} OR m.nameEn={0} RETURN m")
	public List<Measure> findByName(String name);

}
