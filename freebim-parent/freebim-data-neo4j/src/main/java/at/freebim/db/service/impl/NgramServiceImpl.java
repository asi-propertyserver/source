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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neo4j.kernel.DeadlockDetectedException;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.NgramNode;
import at.freebim.db.domain.SearchStringNode;
import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.Coded;
import at.freebim.db.domain.base.Described;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.Ngramed;
import at.freebim.db.domain.base.rel.NgramRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.rel.NgramCodeOf;
import at.freebim.db.domain.rel.NgramDescOf;
import at.freebim.db.domain.rel.NgramNameOf;
import at.freebim.db.repository.NgramRepository;
import at.freebim.db.service.NgramService;
import at.freebim.db.service.SearchStringNodeService;

/**
 * The service for things regarding n-grams. This service implements
 * {@link NgramService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.service.NgramService
 */
@Service
public class NgramServiceImpl implements NgramService {

	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(NgramServiceImpl.class);

	/**
	 * Determines if the service is active or not.
	 */
	private boolean active = true;

	/**
	 * A {@link HashMap} of {@link NgramNode}s where the n-gram is the key.
	 */
	private HashMap<String, NgramNode> map;

	/**
	 * A {@link Session} serves as the main point of integration for the Neo4j OGM.
	 * All the publicly-available capabilities of the framework are defined by this
	 * interface
	 */
	@Autowired
	private Session template;

	/**
	 * Determines if a whitespace should be added at the beginning.
	 */
	private boolean doStart = true;

	/**
	 * Determines if a whitespace should be added at the end.
	 */
	private boolean doEnd = true;

	/**
	 * Determines if the whitespaces should be removed.
	 */
	private boolean doRemoveWhitespace = true;

	/**
	 * Determines if the umlaut should be removed.
	 */
	private boolean doRemoveUml = true;

	/**
	 * The repository for n-grams.
	 */
	@Autowired
	private NgramRepository repository;

	/**
	 * The service for the node/class {@link SearchStringNode}.
	 */
	@Autowired
	private SearchStringNodeService searchStringNodeService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.NgramService#deleteFor(at.freebim.db.domain.base.
	 * Ngramed, java.lang.Class)
	 */
	@Override
	@Transactional
	public int deleteFor(Ngramed node, Class<? extends Ngramed> clazz) {

		String rel = "";
		switch (clazz.getSimpleName()) {
		case "Named":
			rel = RelationType.NGRAM_NAME_OF;
			break;
		case "Described":
			rel = RelationType.NGRAM_DESC_OF;
			break;
		case "Coded":
			rel = RelationType.NGRAM_CODE_OF;
			break;
		default:
			logger.error("Class {} not supported.", clazz.getSimpleName());
			return 0;
		}

		String query = "MATCH (a) WHERE ID(a)=" + node.getNodeId() + "";
		query += " MATCH (x)-[:`" + rel + "`]->(a)";
		query += " RETURN x";

		Iterable<Map<String, Object>> res = this.template.query(query, new HashMap<>(), true);
		Iterator<Map<String, Object>> iter = res.iterator();
		Map<String, Object> map;
		int n = 0;
		while (iter.hasNext()) {
			map = iter.next();
			Set<String> keys = map.keySet();
			for (String key : keys) {
				BaseNode o = (BaseNode) map.get(key);
				String deleteQuery = "MATCH (a) WHERE ID(a)=" + node.getNodeId() + "";
				deleteQuery += " MATCH (x)-[r:`" + rel + "`]->(a)";
				deleteQuery += "WHERE x.id=" + o.getNodeId();
				deleteQuery += " DETACH DELETE r";

				this.template.query(deleteQuery, new HashMap<>());
				n++;
			}
		}
		logger.debug("{} Ngram's deleted.", n);

		return n;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.NgramService#createForNode(at.freebim.db.domain.base.
	 * BaseNode)
	 */
	@Transactional
	@Override
	public void createForNode(Long nodeId) {
		if (nodeId == null)
			return;
		BaseNode node = this.template.load(BaseNode.class, nodeId);
		if (node == null)
			return;

		if (Named.class.isAssignableFrom(node.getClass())) {
			Named n = (Named) node;
			final String name = n.getName();
			if (name == null || name.length() == 0)
				this.deleteFor(n, Named.class);
			else
				this.createFor(name, n, Named.class);
		}
		if (Described.class.isAssignableFrom(node.getClass())) {
			Described n = (Described) node;
			final String desc = n.getDesc();
			if (desc == null || desc.length() == 0)
				this.deleteFor(n, Described.class);
			else
				this.createFor(desc, n, Described.class);
		}
		if (Coded.class.isAssignableFrom(node.getClass())) {
			Coded n = (Coded) node;
			final String code = n.getCode();
			if (code == null || code.length() == 0)
				this.deleteFor(n, Coded.class);
			else
				this.createFor(code, n, Coded.class);
		}

	}

	/**
	 * Creates the n-grams for a node.
	 *
	 * @param s     the string from which the n-grams will be created
	 * @param node  the node from which the n-gram relation starts to the created
	 *              nodes
	 * @param clazz the type of the class for which n-grams will be created
	 */
	@Transactional
	private void createFor(String s, Ngramed node, Class<? extends Ngramed> clazz) {
		if (!this.active || node == null || s == null || s.length() == 0)
			return;

		try {
			int counter = 100;
			while (counter-- > 0) {
				try {
					this.deleteFor(node, clazz);
					counter = 0;
					break;
				} catch (DeadlockDetectedException e) {
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e1) {
						counter = 0;
					}
				} catch (Exception ex) {
					logger.error("Error deleting node=[{}]", node.getNodeId());
				}
			}
		} catch (Exception e) {
			logger.error("Can't delete Ngrams's for node, nodeId = " + node.getNodeId(), e);
		}

		Set<NgramNode> ngs = this.forString(s);
		int n = 0;
		double q = 1. / (s.length() + 2);
		for (NgramNode ng : ngs) {
			if (ng == null)
				continue;

			NgramRel<? extends Ngramed> rel;

			try {
				switch (clazz.getSimpleName()) {
				case "Named":
					NgramNameOf nrel = new NgramNameOf();
					nrel.setN2((Named) node);
					rel = nrel;
					break;
				case "Described":
					NgramDescOf drel = new NgramDescOf();
					drel.setN2((Described) node);
					rel = drel;
					break;
				case "Coded":
					NgramCodeOf crel = new NgramCodeOf();
					crel.setN2((Coded) node);
					rel = crel;
					break;
				default:
					logger.error("Ngram for class {} not supported.", clazz.getSimpleName());
					continue;
				}

				rel.setN1(ng);
				rel.setQ(q);
				this.template.save(rel);
				n++;
			} catch (DeadlockDetectedException e) {
				logger.info(e.getMessage());
			} catch (Exception e) {
				logger.error("Error creating Ngram's for node, nodeId = " + node.getNodeId(), e);
			}
		}
		logger.debug("{} Ngram's created for '{}'.", n, s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.NgramService#forString(java.lang.String)
	 */
	@Override
	@Transactional
	public Set<NgramNode> forString(String string) {
		HashSet<NgramNode> res = new HashSet<>();

		if (!this.active || string == null || string.length() == 0)
			return res;

		if (this.map == null) {
			this.map = new HashMap<>();
			// load all existing entries into our hash map
			Iterable<NgramNode> ngs = this.repository.findAll();
			Iterator<NgramNode> iter = ngs.iterator();
			while (iter.hasNext()) {
				NgramNode ng = iter.next();
				this.map.put(ng.getNg(), ng);
			}
		}
		List<String> strings = this.create(string);
		for (String s : strings) {

			try {
				NgramNode ng = this.map.get(s);
				if (ng == null) {
					ng = new NgramNode();
					ng.setNg(s);
					ng = this.repository.save(ng);

					this.map.put(s, ng);
				}
				res.add(ng);
			} catch (Exception e) {
				logger.error("Error saving Ngram node for string [{}]", s);
			}
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.NgramService#create(java.lang.String)
	 */
	public List<String> create(String s) {
		List<String> res = new ArrayList<>();
		if (!this.active || s == null || s.length() == 0) {
			return res;
		}

		s = s.trim().toLowerCase();

		// convert decimal numbers with comma to international format
		s = s.replaceAll("([0-9])[,]([0-9])", "$1.$2");

		// remove period in text
		s = s.replaceAll("(\\D)[\\.]", "$1");

		if (this.doRemoveWhitespace) {
			s = s.replaceAll("\\s", ""); // any whitespace
			s = s.replace(" ", "");
			s = s.replace(";", "");
			s = s.replace("'", "");
			s = s.replace("\"", "");
			s = s.replace("(", "");
			s = s.replace(")", "");
			s = s.replace("[", "");
			s = s.replace("]", "");
			s = s.replace("{", "");
			s = s.replace("}", "");
		}

		if (this.doRemoveUml) {
			s = s.replace("ä", "a");
			s = s.replace("ö", "o");
			s = s.replace("ü", "u");
			s = s.replace("ß", "ss");
		}

		if (this.doStart)
			s = "  " + s;
		if (this.doEnd)
			s = s + "  ";

		int a, b, n = s.length() - 2;
		for (a = 0; a < n; a++) {
			b = a + 3;
			res.add(s.substring(a, b));
		}

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.NgramService#find(java.lang.String,
	 * java.lang.Class, java.lang.Class)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<MatchResult> find(String s, Class<? extends Ngramed> toNgramed, Class<?> toClass) {

		logger.info("find [{}]... ", s);

		SearchStringNode node = new SearchStringNode();
		node.setSearchString(s);
		node = this.searchStringNodeService.save(node);

		this.createFor(s, node, toNgramed);

		List<MatchResult> res = this.getMatch(node.getNodeId(), toNgramed, toNgramed, toClass, 0.2);

		this.searchStringNodeService.delete(node);

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.NgramService#getMatch(java.lang.Long,
	 * java.lang.Class, java.lang.Class, java.lang.Class, double)
	 */
	@Override
	@Transactional
	public List<MatchResult> getMatch(Long fromNodeId, Class<? extends Ngramed> fromNgramed,
			Class<? extends Ngramed> toNgramed, Class<?> toClass, double minQuality) {

		List<MatchResult> result = new ArrayList<>();
		String relFrom = "";
		switch (fromNgramed.getSimpleName()) {
		case "Named":
			relFrom = RelationType.NGRAM_NAME_OF;
			break;
		case "Described":
			relFrom = RelationType.NGRAM_DESC_OF;
			break;
		case "Coded":
			relFrom = RelationType.NGRAM_CODE_OF;
			break;
		default:
			logger.error("Class {} not supported.", fromNgramed.getSimpleName());
			return result;
		}

		String relTo = "";
		switch (toNgramed.getSimpleName()) {
		case "Named":
			relTo = RelationType.NGRAM_NAME_OF;
			break;
		case "Described":
			relTo = RelationType.NGRAM_DESC_OF;
			break;
		case "Coded":
			relTo = RelationType.NGRAM_CODE_OF;
			break;
		default:
			logger.error("Class {} not supported.", toNgramed.getSimpleName());
			return result;
		}

		/*
		 * START a=node(1499) MATCH
		 * (x)-[:`NGRAM_NAME_OF`]->(a)<-[:`NGRAM_NAME_OF`]-(ng)-[:`NGRAM_NAME_OF`]->(b)<
		 * -[:`NGRAM_NAME_OF`]-(y) RETURN ID(a),a.Name,ID(b),b.Name,count(distinct
		 * x),count(distinct y),count(distinct ng),((count(distinct
		 * ng)*1.0/count(distinct x))+(count(distinct ng)*1.0/count(distinct y)))/2.0 AS
		 * q order by q desc LIMIT 250
		 *
		 * START a=node(1499) MATCH
		 * (x)-[:`NGRAM_NAME_OF`]->(a)<-[:`NGRAM_NAME_OF`]-(ng)-[:`NGRAM_DESC_OF`]->(b)<
		 * -[:`NGRAM_DESC_OF`]-(y) where a<>b RETURN
		 * ID(a),a.Name,ID(b),b.desc,count(distinct x),count(distinct y),count(distinct
		 * ng),((count(distinct ng)*1.0/count(distinct x))+(count(distinct
		 * ng)*1.0/count(distinct y)))/2.0 AS q order by q desc LIMIT 250
		 *
		 * START a=node(1499) MATCH
		 * (x)-[:`NGRAM_NAME_OF`]->(a)<-[:`NGRAM_NAME_OF`]-(ng)-[:`NGRAM_DESC_OF`]->(b:`
		 * ValueListEntry`)<-[:`NGRAM_DESC_OF`]-(y) WHERE a<>b RETURN ((count(distinct
		 * ng)*1.0/count(distinct x))+(count(distinct ng)*1.0/count(distinct y)))/2.0 AS
		 * q ORDER BY q DESC LIMIT 25
		 */

		StringBuilder b = new StringBuilder();
		b.append("Match (a) WHERE ID(a)={a_id} WITH a");
		b.append(" MATCH (a)<-[l:`");
		b.append(relFrom);
		b.append("`]-(ng)-[r:`");
		b.append(relTo);
		b.append("`]->(b");
		if (toClass != null) {
			b.append(":`");
			b.append(toClass.getSimpleName());
			b.append("`");
		}
		b.append(")");
		b.append(" WHERE a<>b");
		b.append(" RETURN b, ((sum(l.q) + sum(r.q)) / 2.0) AS q");
		b.append(" ");
		b.append(" ORDER BY q DESC LIMIT 100");

		HashMap<String, Object> params = new HashMap<>();
		params.put("a_id", fromNodeId);

		String query = b.toString();
		Iterable<Map<String, Object>> res = this.template.query(query, params, true);
		Iterator<Map<String, Object>> iter = res.iterator();

		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			double q = ((Double) map.get("q")).doubleValue();
			if (q > minQuality) {
				MatchResult r = new MatchResult();
				r.q = q;
				r.node = (Ngramed) map.get("b");

				result.add(r);
			}
		}
		return result;
	}

	/**
	 * Check if it is active.
	 *
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Set if it is active.
	 *
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

}
