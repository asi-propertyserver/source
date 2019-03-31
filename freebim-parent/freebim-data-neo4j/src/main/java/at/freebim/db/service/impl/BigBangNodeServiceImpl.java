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

import java.util.Iterator;
import java.util.Map;

import org.neo4j.graphdb.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.service.BigBangNodeService;
import at.freebim.db.service.RelationService;

/**
 * This is the service implementation for the {@link BigBangNode}.
 * This service extends {@link HierarchicalBaseNodeServiceImpl} and {@link BigBangNodeService}.
 * 
 * @see at.freebim.db.domain.BigBangNode
 * @see at.freebim.db.service.impl.HierarchicalBaseNodeServiceImpl
 * @see at.freebim.db.service.BigBangNodeService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Service
public class BigBangNodeServiceImpl extends HierarchicalBaseNodeServiceImpl<BigBangNode> implements BigBangNodeService {

	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(BigBangNodeServiceImpl.class);

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#setRepository(org.springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(GraphRepository<BigBangNode> r) {
		this.repository = r;
	}
	
	/**
	 * The service that handles relations.
	 */
	@Autowired
	private RelationService relationService;
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.BigBangNodeService#getBigBangNode()
	 */
	@Override
	@Transactional(readOnly=true)
	public BigBangNode getBigBangNode() {
		String query = "MATCH (n:BigBangNode) return n";
		BigBangNode tn = null;
		Result<Map<String, Object>> result = this.relationService.getTemplate().query(query, null);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			Node n = (Node) map.get("n");
	
			BaseNode bn = this.relationService.createTreeNode(n);
			if (bn != null) {
				tn = (BigBangNode) bn;
				logger.debug("Big-Bang-Node found, nodeId={}", bn.getNodeId());
			}
		}
		
		if (tn == null) {
			createInitialStructure();
			return this.getBigBangNode();
		}

		return tn;
	}

	/**
	 * Create the {@link BigBangNode}.
	 */
	@Transactional
	private void createInitialStructure() {
		logger.info("going to create initial structure ...");
		BigBangNode bigBangNode = new BigBangNode();
		bigBangNode = super.save(bigBangNode);
		logger.info("Big-Bang-Node created, nodeId={}", bigBangNode.getNodeId());
		
		
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.BaseNodeServiceImpl#getRelevantQuery(java.lang.StringBuilder, java.lang.String)
	 */
	@Override
	protected void getRelevantQuery(StringBuilder b, String returnStatement) {
		b.append("MATCH (y:BigBangNode) ");
		b.append(returnStatement);
	}


}
