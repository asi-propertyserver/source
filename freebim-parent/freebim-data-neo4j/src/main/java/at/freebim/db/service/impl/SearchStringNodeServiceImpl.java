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

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.SearchStringNode;
import at.freebim.db.service.SearchStringNodeService;

/**
 * The service for the node/class {@link SearchStringNode}. This class extends
 * {@link BaseNodeServiceImpl} and implements {@link SearchStringNodeService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.SearchStringNode
 * @see at.freebim.db.service.impl.BaseNodeServiceImpl
 * @see at.freebim.db.service.SearchStringNodeService
 */
@Service
public class SearchStringNodeServiceImpl extends BaseNodeServiceImpl<SearchStringNode>
		implements SearchStringNodeService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.AbstractService#setRepository(org.springframework.
	 * data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(Neo4jRepository<SearchStringNode, Long> r) {
		this.repository = r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.BaseNodeServiceImpl#save(at.freebim.db.domain.base
	 * .BaseNode)
	 */
	@Override
	public SearchStringNode save(SearchStringNode node) {
		node = this.repository.save(node);
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.impl.BaseNodeServiceImpl#getAllRelevant()
	 */
	@Override
	@Transactional(readOnly = true)
	public ArrayList<SearchStringNode> getAllRelevant() {
		return null;
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
