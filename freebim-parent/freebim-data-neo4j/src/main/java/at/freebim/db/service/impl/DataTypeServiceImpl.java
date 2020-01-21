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
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Service;

import at.freebim.db.domain.DataType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.repository.DataTypeRepository;
import at.freebim.db.service.DataTypeService;

/**
 * The service for the node/class {@link DataType}. This service extends
 * {@link ContributedBaseNodeServiceImpl} and implements
 * {@link DataTypeService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.DataType
 * @see at.freebim.db.service.DataTypeService
 * @see at.freebim.db.service.impl.ContributedBaseNodeServiceImpl
 */
@Service
public class DataTypeServiceImpl extends ContributedBaseNodeServiceImpl<DataType> implements DataTypeService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#setRepository(org.
	 * springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(Neo4jRepository<DataType, Long> r) {
		this.repository = r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.DataTypeService#getByName(java.lang.String)
	 */
	@Override
	public DataType getByName(String name) {
		return ((DataTypeRepository) this.repository).getByName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.BaseNodeServiceImpl#getRelevantQuery(java.lang.
	 * StringBuilder, java.lang.String)
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
		b.append(
				" WHERE ALL(y IN nodes(path) WHERE y.validFrom < {now} AND (y.validTo IS NULL OR y.validTo > {now}) )");

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
		b.append(RelationTypeEnum.OF_DATATYPE);
		b.append("]->(y)");
		b.append(where);

		b.append(returnStatement);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.BsddObjectService#findByBsddGuid(java.lang.String)
	 */
	@Override
	public List<DataType> findByBsddGuid(String bsddGuid) {
		return ((DataTypeRepository) this.repository).findByBsddGuid(bsddGuid);
	}

}
