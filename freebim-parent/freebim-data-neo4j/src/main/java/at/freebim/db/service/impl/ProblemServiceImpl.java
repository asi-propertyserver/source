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

import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.Phase;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.service.DateService;
import at.freebim.db.service.ProblemService;

/**
 * The service that handles problems with nodes ({@link Measure},
 * {@link Parameter}, {@link Phase}). This service implements
 * {@link ProblemService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Measure
 * @see at.freebim.db.domain.Parameter
 * @see at.freebim.db.domain.Phase
 * @see at.freebim.db.service.ProblemService
 */
@Service
public class ProblemServiceImpl implements ProblemService {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ProblemServiceImpl.class);

	/**
	 * A {@link Session} serves as the main point of integration for the Neo4j OGM.
	 * All the publicly-available capabilities of the framework are defined by this
	 * interface
	 */
	@Autowired
	private Session template;

	/**
	 * The service that handles dates.
	 */
	@Autowired
	private DateService dateService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.ProblemService#getMissingMeasure()
	 */
	@Override
	@Transactional
	public ArrayList<Long> getMissingMeasure() {

		final String query = "MATCH (n:Parameter) WHERE NOT (n)-[:HAS_MEASURE]->(:Measure) RETURN ID(n) AS id";
		final Iterable<Map<String, Object>> result = this.template.query(query, new HashMap<>(), true);
		final Iterator<Map<String, Object>> iter = result.iterator();
		ArrayList<Long> res = new ArrayList<>();
		while (iter.hasNext()) {
			final Map<String, Object> map = iter.next();
			Long id = (Long) map.get("id");
			res.add(id);
		}

		logger.info("getMissingMeasure count = [{}]", res.size());
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.ProblemService#getEmptyMeasure()
	 */
	@Override
	@Transactional
	public ArrayList<Long> getEmptyMeasure() {

		final String query = "MATCH (m:Measure) WHERE NOT (m)-[:HAS_VALUE]->(:ValueList) AND NOT (m)-[:OF_DATATYPE]->(:DataType) RETURN ID(m) AS id";
		final Iterable<Map<String, Object>> result = this.template.query(query, new HashMap<>(), true);
		final Iterator<Map<String, Object>> iter = result.iterator();
		ArrayList<Long> res = new ArrayList<>();
		while (iter.hasNext()) {
			final Map<String, Object> map = iter.next();
			Long id = (Long) map.get("id");
			res.add(id);
		}

		logger.info("getEmptyMeasure count = [{}]", res.size());
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.ProblemService#getComponentWithoutParameters()
	 */
	@Override
	@Transactional
	public ArrayList<Long> getComponentWithoutParameters() {

		final StringBuilder b = new StringBuilder();
		b.append("MATCH path = shortestPath ((bbn:BigBangNode)-[:");
		b.append(RelationTypeEnum.PARENT_OF);
		b.append("*]->(c:Component) ) WHERE all(y in nodes(path) WHERE NOT (y)-[:");
		b.append(RelationTypeEnum.HAS_PARAMETER);
		b.append("]->(:Parameter)) AND NOT (c)-[:");
		b.append(RelationTypeEnum.PARENT_OF);
		b.append("]->() RETURN ID(c) AS id");

		final Iterable<Map<String, Object>> result = this.template.query(b.toString(), new HashMap<>(), true);
		final Iterator<Map<String, Object>> iter = result.iterator();
		ArrayList<Long> res = new ArrayList<>();
		while (iter.hasNext()) {
			final Map<String, Object> map = iter.next();
			Long id = (Long) map.get("id");
			res.add(id);
		}

		logger.info("getComponentWithoutParameters count = [{}]", res.size());
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.ProblemService#deletedPhase()
	 */
	@Override
	@Transactional
	public ArrayList<Long> deletedPhase() {

		final StringBuilder b = new StringBuilder();
		final Long now = this.dateService.getMillis();
		Map<String, Object> params = new HashMap<>();
		params.put("now", now);

		b.append("MATCH (ph:Phase) ");
		b.append(" WHERE ph.validFrom > {now} OR (ph.validTo IS NOT NULL AND ph.validTo < {now})");
		b.append(" WITH ph AS ph MATCH (:Component)-[rel:HAS_PARAMETER]->(p:Parameter) WHERE ph.uuid = rel.phaseUuid");
		b.append(" RETURN distinct ID(p) AS id");

		final String query = b.toString();
		final Iterable<Map<String, Object>> result = this.template.query(query, params, true);
		final Iterator<Map<String, Object>> iter = result.iterator();
		ArrayList<Long> res = new ArrayList<>();
		while (iter.hasNext()) {
			final Map<String, Object> map = iter.next();
			Long id = (Long) map.get("id");
			res.add(id);
		}

		logger.info("deletedPhase count = [{}]", res.size());
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.ProblemService#specializableParameters(java.lang.Long)
	 */
	@Override
	@Transactional
	public ArrayList<IdPair> specializableParameters(Long libid) {

		logger.info("specializableParameters libid=[{}] ...", libid);

		ArrayList<IdPair> res = new ArrayList<>();
		final Long now = this.dateService.getMillis();
		Map<String, Object> params = new HashMap<>();

		StringBuilder b = new StringBuilder();

		b.append("START lib=node({libid}) MATCH (comp:Component)-[:REFERENCES]->(lib)");

		b.append("WITH comp AS a MATCH (a)-[:PARENT_OF]->(b)-[:HAS_PARAMETER]->(p)");
		b.append(" WHERE a.validFrom < {now} AND (a.validTo IS NULL OR a.validTo > {now})");
		b.append(" AND b.validFrom < {now} AND (b.validTo IS NULL OR b.validTo > {now})");
		b.append(" AND p.validFrom < {now} AND (p.validTo IS NULL OR p.validTo > {now})");

		b.append(" WITH a AS a, p AS p, count(*) as cnt");
		b.append(" MATCH (a)-[:PARENT_OF]->(b)-[:HAS_PARAMETER]->(p)");
		b.append(" WHERE cnt > 1 ");
		b.append(" AND b.validFrom < {now} AND (b.validTo IS NULL OR b.validTo > {now})");
		b.append(" OPTIONAL MATCH (a)-[:PARENT_OF]->(d)");
		b.append(" WHERE cnt > 1 ");
		b.append(" AND d.validFrom < {now} AND (d.validTo IS NULL OR d.validTo > {now})");
		b.append(" AND NOT (d)-[:HAS_PARAMETER]->(p)");

		b.append(" WITH a AS a, b AS b, p AS p, d AS d, cnt AS cnt");
		b.append(" WHERE d IS NULL OR NOT (a)-[:PARENT_OF]->(d)");
		b.append(" RETURN DISTINCT ID(b) AS id1, b.name AS n1, ID(p) AS id2, p.name AS n2");

		params.put("now", now);
		params.put("libid", libid);

		try {
			Iterable<Map<String, Object>> r = this.template.query(b.toString(), params, true);
			Iterator<Map<String, Object>> i = r.iterator();
			while (i.hasNext()) {
				final Map<String, Object> map = i.next();
				Long id1 = (Long) map.get("id1");
				String n1 = (String) map.get("n1");
				Long id2 = (Long) map.get("id2");
				String n2 = (String) map.get("n2");
				IdPair pair = new IdPair();
				pair.a = id1;
				pair.an = n1;
				pair.b = id2;
				pair.bn = n2;
				res.add(pair);
			}
			i = null;
			r = null;
		} catch (Error e) {
			logger.error("", e);
		}
		logger.info("specializableParameters libid=[{}] count = [{}]", libid, res.size());
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.ProblemService#multipleParameterAssignment()
	 */
	@Override
	@Transactional
	public ArrayList<IdTriple> multipleParameterAssignment() {

		logger.info("multipleParameterAssignment  ...");

		ArrayList<IdTriple> res = new ArrayList<>();
		final Long now = this.dateService.getMillis();
		Map<String, Object> params = new HashMap<>();
		params.put("now", now);

		StringBuilder b = new StringBuilder();

		b.append("MATCH (a)-[:PARENT_OF*]->(b),(a)-[:HAS_PARAMETER]->(p),(b)-[:HAS_PARAMETER]->(q)");
		b.append(" WHERE p=q");
		b.append(" AND a.validFrom < {now} AND (a.validTo IS NULL OR a.validTo > {now})");
		b.append(" AND b.validFrom < {now} AND (b.validTo IS NULL OR b.validTo > {now})");
		b.append(" AND p.validFrom < {now} AND (p.validTo IS NULL OR p.validTo > {now})");
		b.append(" RETURN DISTINCT");

		b.append("  ID(a) AS ida, a.name AS aname");
		b.append(", ID(b) AS idb, b.name AS bname");
		b.append(", ID(p) AS idp, p.name AS pname");

		try {
			Iterable<Map<String, Object>> r = this.template.query(b.toString(), params, true);
			Iterator<Map<String, Object>> i = r.iterator();
			while (i.hasNext()) {
				final Map<String, Object> map = i.next();
				Long ida = (Long) map.get("ida");
				String aname = (String) map.get("aname");
				Long idb = (Long) map.get("idb");
				String bname = (String) map.get("bname");
				Long idp = (Long) map.get("idp");
				String pname = (String) map.get("pname");
				IdTriple triple = new IdTriple();
				triple.a = ida;
				triple.an = aname;
				triple.b = idb;
				triple.bn = bname;
				triple.c = idp;
				triple.cn = pname;
				res.add(triple);
			}
			i = null;
			r = null;
		} catch (Error e) {
			logger.error("", e);
		}
		logger.info("multipleParameterAssignment count = [{}]", res.size());
		return res;
	}

}
