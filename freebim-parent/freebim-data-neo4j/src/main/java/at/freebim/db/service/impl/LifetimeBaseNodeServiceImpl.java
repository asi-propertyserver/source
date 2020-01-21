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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.base.LifetimeBaseNode;
import at.freebim.db.service.DateService;
import at.freebim.db.service.LifetimeBaseNodeService;

/**
 * This service defines the basics for all services that define functionality
 * for a class <b>T</b> that extends {@link LifetimeBaseNode}. This service
 * extends {@link BaseNodeServiceImpl} and implements
 * {@link LifetimeBaseNodeService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.LifetimeBaseNode
 * @see at.freebim.db.service.LifetimeBaseNodeService
 * @see at.freebim.db.service.impl.BaseNodeServiceImpl
 */
@Service
public class LifetimeBaseNodeServiceImpl<T extends LifetimeBaseNode> extends BaseNodeServiceImpl<T>
		implements LifetimeBaseNodeService<T> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(LifetimeBaseNodeServiceImpl.class);

	/**
	 * The service that handles dates.
	 */
	@Autowired
	private DateService dateService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.AbstractService#setRepository(org.springframework.
	 * data.neo4j.repository.GraphRepository)
	 */
	@Override
	public void setRepository(Neo4jRepository<T, Long> r) {
		this.repository = r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.AbstractService#delete(at.freebim.db.domain.base.
	 * BaseNode)
	 */
	@Override
	@Transactional
	public T delete(T node) {
		super.deleteNgrams(node);
		node.setValidTo(this.dateService.getMillis());
		node = this.repository.save(node);

		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.BaseNodeServiceImpl#filterBeforeInsert(at.freebim.
	 * db.domain.base.BaseNode)
	 */
	@Override
	public T filterBeforeInsert(T node) {

		logger.debug("filterBeforeInsert");
		try {
			if (node != null) {
				if (node.getValidFrom() == null || node.getValidFrom() == 0L) {
					node.setValidFrom(this.dateService.getMillis());
				}
			}
		} catch (Exception e) {
			logger.error("Error setting validFrom:", e);
		}
		return super.filterBeforeInsert(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.BaseNodeServiceImpl#getRelevantQuery(java.lang.
	 * StringBuilder, java.lang.String)
	 */
	protected void getRelevantQuery(StringBuilder b, String returnStatement) {
		throw new RuntimeException("getRelevantQuery not implemented for " + this.getClass().getName());
	}

}
