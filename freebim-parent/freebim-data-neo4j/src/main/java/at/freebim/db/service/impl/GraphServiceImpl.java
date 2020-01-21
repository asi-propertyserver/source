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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.service.DateService;
import at.freebim.db.service.GraphService;
import at.freebim.db.service.RelationService;

/**
 * This service is used to create a graph from a node. It implements
 * {@link GraphService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.service.GraphService
 */
@Service
public class GraphServiceImpl implements GraphService {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(GraphServiceImpl.class);

	/**
	 * The service that handles dates.
	 */
	@Autowired
	private DateService dateService;

	/**
	 * The service that handles relations.
	 */
	@Autowired
	private RelationService relationService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.GraphService#getGraphFor(java.lang.Long)
	 */
	@Override
	public Graph getGraphFor(Long nodeId, boolean recursive, boolean withParams, boolean withEquals) {

		logger.debug("getGraphFor [{}] ...", nodeId);

		Graph graph = new Graph();

		Map<String, Object> params = new HashMap<>();
		params.put("id", nodeId);
		params.put("now", this.dateService.getMillis());

		StringBuilder b = new StringBuilder();

		b.append("MATCH (a) WHERE ID(a)={id}");
		b.append(" MATCH path = (a)-[r:PARENT_OF");
		if (recursive) {
			b.append("*");
		}
		b.append("]->(b)");
		b.append(", (b)-[:REFERENCES]->(lib)");
		b.append(
				" WHERE ALL(y IN nodes(path) WHERE y.validFrom < {now} AND (y.validTo IS NULL OR y.validTo > {now}) )");
		if (withParams) {
			b.append(" OPTIONAL MATCH (b)-[prel:HAS_PARAMETER]->(p)");
			b.append(", (p)-[:REFERENCES]->(plib)");
			b.append(" WHERE p.validFrom < {now} AND (p.validTo IS NULL OR p.validTo > {now})");
		}
		if (withEquals) {
			b.append(" OPTIONAL MATCH (b)-[eqrel:EQUALS]-(eq)");
			b.append(", (eq)-[:REFERENCES]->(eqlib)");
			b.append(" WHERE eq.validFrom < {now} AND (eq.validTo IS NULL OR eq.validTo > {now})");
		}
		b.append(" RETURN distinct");
		b.append(" ID(b) AS id");
		b.append(", b.name AS name");
		b.append(", ID(lib) AS libId");
		b.append(", last(relationShips(path)) AS rel");

		// added only for relationship mapping
		b.append(", a");
		b.append(", b");
		b.append(", lib");

		if (withParams) {
			b.append(", ID(p) AS pid");
			b.append(", p.name AS pname");
			b.append(", ID(plib) AS plibId");
			b.append(", prel AS prel");

			// added only for relationship mapping
			b.append(", p");
			b.append(", plib");
		}
		if (withEquals) {
			b.append(", ID(eq) AS eqid");
			b.append(", eq.name AS eqname");
			b.append(", ID(eqlib) AS eqlibId");
			b.append(", eqrel AS eqrel");
			b.append(", SUBSTRING(head(filter (cn in labels(eq) where cn =~ '_.*')), 1) AS eqcn");

			// added only for relationship mapping
			b.append(", eq");
			b.append(", eqlib");
		}

		b.append(" UNION ALL");
		b.append(" MATCH (a) WHERE ID(a)={id} MATCH (a)-[:REFERENCES]->(lib)");
		if (withParams) {
			b.append(" OPTIONAL MATCH (a)-[prel:HAS_PARAMETER]->(p)");
			b.append(", (p)-[:REFERENCES]->(plib)");
			b.append(" WHERE p.validFrom < {now} AND (p.validTo IS NULL OR p.validTo > {now})");
		}
		if (withEquals) {
			b.append(" OPTIONAL MATCH (a)-[eqrel:EQUALS]-(eq)");
			b.append(", (eq)-[:REFERENCES]->(eqlib)");
			b.append(" WHERE eq.validFrom < {now} AND (eq.validTo IS NULL OR eq.validTo > {now})");
		}
		b.append(" RETURN distinct");
		b.append(" ID(a) AS id");
		b.append(", a.name AS name");
		b.append(", ID(lib) AS libId");
		b.append(", null AS rel");

		// added only for relationship mapping
		b.append(", a");
		b.append(", a as b");
		b.append(", lib");

		if (withParams) {
			b.append(", ID(p) AS pid");
			b.append(", p.name AS pname");
			b.append(", ID(plib) AS plibId");
			b.append(", prel AS prel");

			// added only for relationship mapping
			b.append(", p");
			b.append(", plib");
		}
		if (withEquals) {
			b.append(", ID(eq) AS eqid");
			b.append(", eq.name AS eqname");
			b.append(", ID(eqlib) AS eqlibId");
			b.append(", eqrel AS eqrel");
			b.append(", SUBSTRING(head(filter (cn in labels(eq) where cn =~ '_.*')), 1) AS eqcn");

			// added only for relationship mapping
			b.append(", eq");
			b.append(", eqlib");
		}

		Iterable<Map<String, Object>> result = this.relationService.getTemplate().query(b.toString(), params, true);
		Iterator<Map<String, Object>> iter = result.iterator();

		while (iter.hasNext()) {

			Map<String, Object> map = iter.next();

			Long id = (Long) map.get("id");
			if (id != null) {
				Node node = graph.nodes.get(id);
				if (node == null) {
					String name = (String) map.get("name");
					node = new Node(id, name, "Component");
					graph.nodes.put(id, node);
				}
				Long libId = (Long) map.get("libId");
				if (!node.libs.contains(libId)) {
					node.libs.add(libId);
				}
			}
			BaseRel<?, ?> r = (BaseRel<?, ?>) map.get("rel");
			if (r != null) {
				graph.links.add(r);
			}

			if (withParams) {
				Long pid = (Long) map.get("pid");
				if (pid != null) {
					Node pnode = graph.nodes.get(pid);
					if (pnode == null) {
						String pname = (String) map.get("pname");
						pnode = new Node(pid, pname, "Parameter");
						graph.nodes.put(pid, pnode);
					}
					Long plibId = (Long) map.get("plibId");
					if (!pnode.libs.contains(plibId)) {
						pnode.libs.add(plibId);
					}
				}
				BaseRel<?, ?> pr = (BaseRel<?, ?>) map.get("prel");
				BaseRel<?, ?> prel = pr;
				graph.links.add(prel);
			}

			if (withEquals) {
				Long eqid = (Long) map.get("eqid");
				if (eqid != null) {
					Node eqnode = graph.nodes.get(eqid);
					if (eqnode == null) {
						String eqname = (String) map.get("eqname");
						String eqcn = (String) map.get("eqcn");
						eqnode = new Node(eqid, eqname, eqcn);
						graph.nodes.put(eqid, eqnode);
					}
					Long eqlibId = (Long) map.get("eqlibId");
					if (!eqnode.libs.contains(eqlibId)) {
						eqnode.libs.add(eqlibId);
					}
				}
				BaseRel<?, ?> eqr = (BaseRel<?, ?>) map.get("eqrel");
				BaseRel<?, ?> eqrel = eqr;
				graph.links.add(eqrel);
			}

		}
		logger.debug("    found [{}] nodes, [{}] links.", graph.nodes.size(), graph.links.size());

		return graph;
	}

}
