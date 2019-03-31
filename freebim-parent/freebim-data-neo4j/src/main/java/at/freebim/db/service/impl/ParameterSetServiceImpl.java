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
package at.freebim.db.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Service;

import at.freebim.db.domain.ParameterSet;
import at.freebim.db.repository.ParameterSetRepository;
import at.freebim.db.service.ParameterSetService;

/**
 * The service for the node/class {@link ParameterSet}.
 * This service extends {@link ParameterizedServiceImpl} and 
 * implements {@link ParameterSetService}.
 * 
 * @see at.freebim.db.domain.ParameterSet
 * @see at.freebim.db.service.impl.ParameterizedServiceImpl
 * @see at.freebim.db.service.ParameterSetService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Service
public class ParameterSetServiceImpl extends ParameterizedServiceImpl<ParameterSet> implements ParameterSetService {

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#setRepository(org.springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(GraphRepository<ParameterSet> r) {
		this.repository = r;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#getRelevantQuery(java.lang.StringBuilder, java.lang.String)
	 */
	@Override
	protected void getRelevantQuery(StringBuilder b, String returnStatement) {
		b.append("MATCH (y:ParameterSet)");
		b.append(" WHERE y.validFrom < {now} AND (y.validTo IS NULL OR y.validTo > {now})");

		b.append(returnStatement);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BsddObjectService#findByBsddGuid(java.lang.String)
	 */
	@Override
	public List<ParameterSet> findByBsddGuid(String bsddGuid) {
		return ((ParameterSetRepository) this.repository).findByBsddGuid(bsddGuid);
	}
	
}
