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
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.Component;
import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.repository.ComponentRepository;
import at.freebim.db.service.ComponentService;
import at.freebim.db.service.RelationService;

/**
 * The service for the node/class {@link Component}. This service extends
 * {@link HierarchicalBaseNodeServiceImpl} and implements
 * {@link ComponentService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Component
 * @see at.freebim.db.service.ComponentService
 * @see at.freebim.db.service.impl.HierarchicalBaseNodeServiceImpl
 */
@Service
public class ComponentServiceImpl extends HierarchicalBaseNodeServiceImpl<Component> implements ComponentService {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ComponentServiceImpl.class);
	/**
	 * The service that handles relations.
	 */
	@Autowired
	private RelationService relationService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#setRepository(org.
	 * springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(Neo4jRepository<Component, Long> r) {
		this.repository = r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.ComponentService#getByNameFromLibrary(java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Component> getByNameFromLibrary(String name, Long libraryId) {
		return ((ComponentRepository) this.repository).getByNameFromLibrary(name, libraryId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#filterResponse(at.
	 * freebim.db.domain.base.LifetimeBaseNode)
	 */
	@Override
	public Component filterResponse(final Component node, final Long now) {
		if (node != null) {
			node.setM(this.relationService.isMaterial(node.getNodeId()));
		}
		return super.filterResponse(node, now);
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

		b.append(returnStatement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.BsddObjectService#findByBsddGuid(java.lang.String)
	 */
	@Override
	public List<Component> findByBsddGuid(String bsddGuid) {
		return ((ComponentRepository) this.repository).findByBsddGuid(bsddGuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.ComponentService#getEqualFromLibrary(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public Component getEqualFromLibrary(Long srcNodeId, Long libraryNodeId) {
		logger.info("getEqualFromLibrary ...");
		Map<String, Object> params = new HashMap<>();
		params.put("srcId", srcNodeId);
		params.put("libId", libraryNodeId);
		StringBuilder b = new StringBuilder();
		b.append("MATCH (lib)<-[:REFERENCES]-(n:Component)-[:EQUALS]-(a)");
		b.append(" WHERE ID(lib)={libId} AND ID(a)={srcId}");
		b.append(" RETURN n");

		Component co = null;
		Iterable<Map<String, Object>> result = this.relationService.getTemplate().query(b.toString(), params, true);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();

			co = (Component) map.get("n");
			if (co != null) {
				logger.debug("    found node {} ", co.getNodeId());
			}
		}
		return co;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.ComponentService#getNodesFromHierarchy(java.lang.
	 * String)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Component> getNodesFromHierarchy(String rootName) {
		logger.info("getNodesFromHierarchy ...");
		List<Component> res = new ArrayList<Component>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rootName", rootName);
		params.put("now", this.dateService.getMillis());
		StringBuilder b = new StringBuilder();

		b.append("MATCH path=(n:Component)-[:PARENT_OF*0..]->(co)");
		b.append(" WHERE n.name={rootName} ");
		b.append(
				" AND ALL (y in nodes(path) WHERE y.validFrom <= {now} AND (y.validTo IS NULL OR y.validTo >= {now}))");
		b.append(" RETURN co");

		Component co = null;
		Iterable<Map<String, Object>> result = this.relationService.getTemplate().query(b.toString(), params, true);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			BaseNode n = (BaseNode) map.get("co");
			if (n != null && n instanceof Component) {
				co = (Component) n;
				res.add(co);
				logger.debug("    found node {} ", n.getNodeId());
			}
		}
		logger.info("getNodesFromHierarchy found [{}] components.", res.size());
		return res;
	}

}
