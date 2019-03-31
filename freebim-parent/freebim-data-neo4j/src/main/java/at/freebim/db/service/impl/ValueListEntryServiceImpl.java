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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Service;

import at.freebim.db.domain.ValueListEntry;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.repository.ValueListEntryRepository;
import at.freebim.db.service.ValueListEntryService;

/**
 * The service for the node/class {@link ValueListEntry}.
 * This service extends {@link StatedBaseNodeServiceImpl} and 
 * implements {@link ValueListEntryService}.
 * 
 * @see at.freebim.db.domain.ValueListEntry
 * @see at.freebim.db.service.impl.StatedBaseNodeServiceImpl
 * @see at.freebim.db.service.ValueListEntryService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Service
public class ValueListEntryServiceImpl extends StatedBaseNodeServiceImpl<ValueListEntry> implements ValueListEntryService {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ValueListEntryServiceImpl.class);
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#setRepository(org.springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(GraphRepository<ValueListEntry> r) {
		this.repository = r;
	}
	


	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.ContributedBaseNodeServiceImpl#filterRequest(at.freebim.db.domain.base.ContributedBaseNode)
	 */
	@Override
	public ValueListEntry filterBeforeSave(ValueListEntry node) {
		
		this.correctNumberFormat(node);
		
		return super.filterBeforeSave(node);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.ValueListEntryService#correctNumberFormat(at.freebim.db.domain.ValueListEntry)
	 */
	@Override
	public void correctNumberFormat(ValueListEntry node) {
		if (node != null) {
			// convert decimal numbers with comma to international format with dot
			// 47,11 --> 47.11
			String name = node.getName();
			if (name != null) {
				name = name.replaceAll("([0-9]+)[,]([0-9]+)", "$1.$2");
				node.setName(name);
			}
		}
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.UuidIdentifyableServiceImpl#filterBeforeInsert(at.freebim.db.domain.base.UuidIdentifyable)
	 */
	@Override
	public ValueListEntry filterBeforeInsert(ValueListEntry node) {
		try {
			this.correctNumberFormat(node);
		} catch (Exception e) {
			logger.error("Error in correctNumberFormat:", e);
		}
		return super.filterBeforeInsert(node);
	}

	
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#getRelevantQuery(java.lang.StringBuilder, java.lang.String)
	 */
	@Override
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

		b.append(with);
		b.append(" (x)-[:");
		b.append(RelationTypeEnum.HAS_ENTRY);
		b.append("]->(y)");
		b.append(where);

		b.append(returnStatement);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BsddObjectService#findByBsddGuid(java.lang.String)
	 */
	@Override
	public List<ValueListEntry> findByBsddGuid(String bsddGuid) {
		return ((ValueListEntryRepository) this.repository).findByBsddGuid(bsddGuid);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.ValueListEntryService#getByName(java.lang.String)
	 */
	@Override
	public List<ValueListEntry> getByName(String name) {
		return ((ValueListEntryRepository) this.repository).getByName(name);
	}
	

}
