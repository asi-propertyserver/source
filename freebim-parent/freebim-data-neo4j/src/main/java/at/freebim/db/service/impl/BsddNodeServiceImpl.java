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

import at.freebim.db.domain.BsddNode;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.rel.Bsdd;
import at.freebim.db.repository.BsddNodeRepository;
import at.freebim.db.service.BsddNodeService;
import at.freebim.db.service.RelationService;

/**
 * The service for the node/class {@link BsddNode}.
 * This service extends {@link BaseNodeServiceImpl} and {@link BsddNodeService}.
 * 
 * @see at.freebim.db.domain.BsddNode
 * @see at.freebim.db.service.impl.BaseNodeServiceImpl
 * @see at.freebim.db.service.BsddNodeService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Service
public class BsddNodeServiceImpl extends BaseNodeServiceImpl<BsddNode> implements BsddNodeService {
	
	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(BsddNodeServiceImpl.class);
	
	/**
	 * The service that handles relations.
	 */
	@Autowired
	private RelationService relationService;

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.AbstractService#setRepository(org.springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(GraphRepository<BsddNode> r) {
		this.repository = r;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BsddNodeService#getByGuid(java.lang.String)
	 */
	@Override
	@Transactional
	public BsddNode getByGuid(String guid) {
		return ((BsddNodeRepository) this.repository).getByGuid(guid);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.BaseNodeServiceImpl#getRelevantQuery(java.lang.StringBuilder, java.lang.String)
	 */
	protected void getRelevantQuery(StringBuilder b, String returnStatement) {

	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.BaseNodeServiceImpl#save(at.freebim.db.domain.base.BaseNode)
	 */
	@Override
	@Transactional(readOnly=true)
	public BsddNode save(BsddNode node) {
		if (node != null) {
			BsddNode saved = this.getByGuid(node.getGuid());
			if (saved != null) {
				return saved;
			}
		}
		return super.save(node);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BsddNodeService#spreadToEqualNodes()
	 */
	@Override
	@Transactional
	public ArrayList<Long> spreadToEqualNodes() {

		logger.info("spreadToEqualNodes ...");
		
		ArrayList<Long> res = new ArrayList<Long>();
		
		String query = "MATCH (bsdd:BsddNode)-[:BSDD]->(n)-[eq:EQUALS*]-(e) WHERE all(r in eq WHERE r.q >= 1) AND NOT (bsdd)-[:BSDD]->(e) RETURN DISTINCT bsdd, e";
		
		Result<Map<String, Object>> result = this.relationService.getTemplate().query(query, null);
		Iterator<Map<String, Object>> iter = result.iterator();
		
		while (iter.hasNext()) {

			Map<String, Object> map = iter.next();
			Node bsdd = (Node) map.get("bsdd");
			Node e = (Node) map.get("e");
			BsddNode bsddNode = (BsddNode) this.relationService.createTreeNode((Node) bsdd);
			UuidIdentifyable eNode = (UuidIdentifyable) this.relationService.createTreeNode((Node) e);
			if (bsddNode != null && eNode != null) {
				Bsdd rel = new Bsdd();
				rel.setN1(bsddNode);
				rel.setN2(eNode);
				this.relationService.save(rel);
				res.add(eNode.getNodeId());
			}
		}
		
		logger.info("spreadToEqualNodes n=[{}].", res.size());
		
		return res;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BsddNodeService#setBsddFieldToEqualNodes()
	 */
	@Override
	@Transactional
	public Long setBsddFieldToEqualNodes() {
		
		logger.info("setBsddFieldToEqualNodes ...");
		
		StringBuilder b = new StringBuilder();
		Long count = 0L;

		b.append("MATCH (n)-[eq:EQUALS*]->(e)");
		b.append(" WHERE all(r IN eq WHERE r.q >= 1) AND HAS(n.bsddGuid) AND n.bsddGuid =~ \".{22,36}\"");
		b.append(" AND (n.bsddGuid <> e.bsddGuid OR e.bsddGuid IS NULL) SET e.bsddGuid = n.bsddGuid");
		b.append(" RETURN count(DISTINCT ID(e)) AS count");
		
		String query = b.toString();
		
		Result<Map<String, Object>> result = this.relationService.getTemplate().query(query, null);
		Iterator<Map<String, Object>> iter = result.iterator();
		
		while (iter.hasNext()) {

			Map<String, Object> map = iter.next();
			Long c = (Long) map.get("count");
			count += c;
		}
		
		logger.info("setBsddFieldToEqualNodes n=[{}].", count);

		b.setLength(0);
		
		b.append("MATCH (n)<-[eq:EQUALS*]-(e)");
		b.append(" WHERE all(r IN eq WHERE r.q >= 1) AND HAS(n.bsddGuid) AND n.bsddGuid =~ \".{22,36}\"");
		b.append(" AND (n.bsddGuid <> e.bsddGuid OR e.bsddGuid IS NULL) SET e.bsddGuid = n.bsddGuid");
		b.append(" RETURN count(DISTINCT ID(e)) AS count");
		
		query = b.toString();
		
		result = this.relationService.getTemplate().query(query, null);
		iter = result.iterator();
		
		while (iter.hasNext()) {

			Map<String, Object> map = iter.next();
			Long c = (Long) map.get("count");
			count += c;
		}
		
		logger.info("setBsddFieldToEqualNodes n=[{}].", count);

		return count;
	}

}
