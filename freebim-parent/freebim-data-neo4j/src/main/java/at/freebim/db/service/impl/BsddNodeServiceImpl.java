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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.BsddNode;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.rel.Bsdd;
import at.freebim.db.repository.BsddNodeRepository;
import at.freebim.db.service.BsddNodeService;
import at.freebim.db.service.RelationService;

/**
 * The service for the node/class {@link BsddNode}. This service extends
 * {@link BaseNodeServiceImpl} and {@link BsddNodeService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.BsddNode
 * @see at.freebim.db.service.impl.BaseNodeServiceImpl
 * @see at.freebim.db.service.BsddNodeService
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.AbstractService#setRepository(org.springframework.
	 * data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(Neo4jRepository<BsddNode, Long> r) {
		this.repository = r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.BsddNodeService#getByGuid(java.lang.String)
	 */
	@Override
	@Transactional
	public BsddNode getByGuid(String guid) {
		return ((BsddNodeRepository) this.repository).getByGuid(guid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.BaseNodeServiceImpl#getRelevantQuery(java.lang.
	 * StringBuilder, java.lang.String)
	 */
	protected void getRelevantQuery(StringBuilder b, String returnStatement) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.BaseNodeServiceImpl#save(at.freebim.db.domain.base
	 * .BaseNode)
	 */
	@Override
	@Transactional(readOnly = true)
	public BsddNode save(BsddNode node) {
		if (node != null) {
			BsddNode saved = this.getByGuid(node.getGuid());
			if (saved != null) {
				return saved;
			}
		}
		return super.save(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.BsddNodeService#spreadToEqualNodes()
	 */
	@Override
	@Transactional
	public ArrayList<Long> spreadToEqualNodes() {

		logger.info("spreadToEqualNodes ...");

		ArrayList<Long> res = new ArrayList<>();

		String query = "MATCH (bsdd:BsddNode)-[:BSDD]->(n)-[eq:EQUALS*]-(e:UuidIdentifyable) WHERE all(r in eq WHERE r.q >= 1) AND NOT (bsdd)-[:BSDD]->(e) RETURN DISTINCT bsdd, e";

		Iterable<Map<String, Object>> result = this.relationService.getTemplate().query(query, new HashMap<>(), true);
		Iterator<Map<String, Object>> iter = result.iterator();

		while (iter.hasNext()) {

			Map<String, Object> map = iter.next();
			BsddNode bsdd = (BsddNode) map.get("bsdd");
			UuidIdentifyable e = (UuidIdentifyable) map.get("e");
			if (bsdd != null && e != null) {
				Bsdd rel = new Bsdd();
				rel.setN1(bsdd);
				rel.setN2(e);
				this.relationService.save(rel);
				res.add(e.getNodeId());
			}
		}

		logger.info("spreadToEqualNodes n=[{}].", res.size());

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.BsddNodeService#setBsddFieldToEqualNodes()
	 */
	@Override
	@Transactional
	public Long setBsddFieldToEqualNodes() {

		logger.info("setBsddFieldToEqualNodes ...");

		StringBuilder b = new StringBuilder();
		Long count = 0L;

		b.append("MATCH (n)-[eq:EQUALS*]->(e)");
		b.append(" WHERE all(r IN eq WHERE r.q >= 1) AND n.bsddGuid IS NOT NULL AND n.bsddGuid =~ \".{22,36}\"");
		b.append(" AND (n.bsddGuid <> e.bsddGuid OR e.bsddGuid IS NULL) SET e.bsddGuid = n.bsddGuid");
		b.append(" RETURN count(DISTINCT ID(e)) AS count");

		String query = b.toString();

		Iterable<Map<String, Object>> result = this.relationService.getTemplate().query(query, new HashMap<>());
		Iterator<Map<String, Object>> iter = result.iterator();

		while (iter.hasNext()) {

			Map<String, Object> map = iter.next();
			Long c = (Long) map.get("count");
			count += c;
		}

		logger.info("setBsddFieldToEqualNodes n=[{}].", count);

		b.setLength(0);

		b.append("MATCH (n)<-[eq:EQUALS*]-(e)");
		b.append(" WHERE all(r IN eq WHERE r.q >= 1) AND n.bsddGuid IS NOT NULL AND n.bsddGuid =~ \".{22,36}\"");
		b.append(" AND (n.bsddGuid <> e.bsddGuid OR e.bsddGuid IS NULL) SET e.bsddGuid = n.bsddGuid");
		b.append(" RETURN count(DISTINCT ID(e)) AS count");

		query = b.toString();

		result = this.relationService.getTemplate().query(query, new HashMap<>());
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
