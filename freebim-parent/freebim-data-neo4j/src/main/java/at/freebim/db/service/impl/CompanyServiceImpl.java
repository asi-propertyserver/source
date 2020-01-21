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

import at.freebim.db.domain.Company;
import at.freebim.db.repository.CompanyRepository;
import at.freebim.db.service.CompanyService;

/**
 * The service for the node/class {@link Company}. It extends
 * {@link LifetimeBaseNodeServiceImpl} and implements {@link CompanyService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Company
 * @see at.freebim.db.service.CompanyService
 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl
 */
@Service
public class CompanyServiceImpl extends LifetimeBaseNodeServiceImpl<Company> implements CompanyService {

	/**
	 * The logger.
	 */
	protected static Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#setRepository(org.
	 * springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(Neo4jRepository<Company, Long> r) {
		this.repository = r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.CompanyService#get(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public Company get(String name) {
		logger.debug("get [{}]", name);
		Company node = ((CompanyRepository) this.repository).get(name);
		node = this.filterResponse(node, null);
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#getRelevantQuery(java.
	 * lang.StringBuilder, java.lang.String)
	 */
	@Override
	protected void getRelevantQuery(StringBuilder b, String returnStatement) {
		b.append("MATCH (y:Company)");
		b.append(" WHERE y.validFrom < {now} AND (y.validTo IS NULL OR y.validTo > {now})");

		b.append(returnStatement);
	}

}
