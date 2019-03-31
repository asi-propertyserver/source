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

import at.freebim.db.domain.ValueList;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.repository.ValueListRepository;
import at.freebim.db.service.ValueListService;

/**
 * The service for the node/class {@link ValueList}.
 * This service extends {@link StatedBaseNodeServiceImpl}
 * and implements {@link ValueListService}.
 * 
 * @see at.freebim.db.domain.ValueList
 * @see at.freebim.db.service.impl.StatedBaseNodeServiceImpl
 * @see at.freebim.db.service.ValueListService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Service
public class ValueListServiceImpl extends StatedBaseNodeServiceImpl<ValueList> implements ValueListService {

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#setRepository(org.springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(GraphRepository<ValueList> r) {
		this.repository = r;		
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#getRelevantQuery(java.lang.StringBuilder, java.lang.String)
	 */
	protected void getRelevantQuery(StringBuilder b, String returnStatement) {

		String with = " WITH y AS x, count(*) AS cnt MATCH";
		String where = " WHERE y.validFrom < {now} AND (y.validTo IS NULL OR y.validTo > {now})";

		b.append("MATCH (y:BigBangNode)");
		
		b.append(with); 
		b.append(" (x)-[:");
		b.append(RelationTypeEnum.PARENT_OF);
		b.append("]->(y)");
		b.append(where);

		b.append(with);
		b.append(" path=");
		b.append(" (x)-[:");
		b.append(RelationTypeEnum.PARENT_OF);
		b.append("*]->(y)");
		b.append(" WHERE ALL(y IN nodes(path) WHERE y.validFrom < {now} AND (y.validTo IS NULL OR y.validTo > {now}) )");

		b.append(with);
		b.append(" (x)-[:");
		b.append(RelationTypeEnum.HAS_PARAMETER);
		b.append("]->(y)");
		b.append(where);

		b.append(with);
		b.append(" (x)-[:");
		b.append(RelationTypeEnum.HAS_MEASURE);
		b.append("]->(y)");
		b.append(where);

		b.append(with);
		b.append(" (x)-[:");
		b.append(RelationTypeEnum.HAS_VALUE);
		b.append("]->(y)");
		b.append(where);
		
		b.append(returnStatement);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BsddObjectService#findByBsddGuid(java.lang.String)
	 */
	@Override
	public List<ValueList> findByBsddGuid(String bsddGuid) {
		return ((ValueListRepository) this.repository).findByBsddGuid(bsddGuid);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.ValueListService#getByName(java.lang.String)
	 */
	@Override
	public List<ValueList> getByName(String name) {
		return ((ValueListRepository) this.repository).findByName(name);
	}
	
}
