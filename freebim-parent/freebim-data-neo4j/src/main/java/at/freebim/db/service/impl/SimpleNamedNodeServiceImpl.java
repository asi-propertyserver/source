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
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.SimpleNamedNode;
import at.freebim.db.repository.SimpleNamedNodeRepository;
import at.freebim.db.service.SimpleNamedNodeService;

/**
 * This is the service for the node/class {@link SimpleNamedNode}.
 * This service extends {@link HierarchicalBaseNodeServiceImpl} and
 * implements {@link SimpleNamedNodeService}.
 * 
 * @see at.freebim.db.domain.SimpleNamedNode
 * @see at.freebim.db.service.impl.HierarchicalBaseNodeServiceImpl
 * @see at.freebim.db.service.SimpleNamedNodeService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Service
public class SimpleNamedNodeServiceImpl extends HierarchicalBaseNodeServiceImpl<SimpleNamedNode> implements SimpleNamedNodeService {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SimpleNamedNodeServiceImpl.class);
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#setRepository(org.springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(GraphRepository<SimpleNamedNode> r) {
		this.repository = r;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.SimpleNamedNodeService#find(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public SimpleNamedNode find(String name, String type) {
		SimpleNamedNode node = null;
		if (type == null)
			node = ((SimpleNamedNodeRepository) this.repository).get(name);
		else 
			node = ((SimpleNamedNodeRepository) this.repository).get(name, type);
		node = this.filterResponse(node, null);
		return node;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.SimpleNamedNodeService#get(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public SimpleNamedNode get(String name, String type) {
		SimpleNamedNode node = null;
		if (type == null)
			node = ((SimpleNamedNodeRepository) this.repository).get(name);
		else 
			node = ((SimpleNamedNodeRepository) this.repository).get(name, type);
		
		if (node == null) {
			node = new SimpleNamedNode();
			node.setName(name);
			node.setType(type);
			node = this.repository.save(node);
			logger.info("Root node '{}' saved, nodeId={}", name, node.getNodeId());
		}
		
		logger.debug("got Root node '{}', nodeId={}", name, node.getNodeId());
		node = this.filterResponse(node, null);
		return node;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#getRelevantQuery(java.lang.StringBuilder, java.lang.String)
	 */
	protected void getRelevantQuery(StringBuilder b, String returnStatement) {
		throw new RuntimeException("getRelevantQuery not implemented for " + this.getClass().getName());
	}

}
