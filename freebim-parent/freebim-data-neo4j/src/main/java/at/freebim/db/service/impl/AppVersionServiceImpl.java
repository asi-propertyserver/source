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

import org.neo4j.kernel.DeadlockDetectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.Component;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.ParameterSet;
import at.freebim.db.domain.ValueList;
import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.HierarchicalBaseNode;
import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.rel.ChildOf;
import at.freebim.db.domain.rel.Equals;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.HasValue;
import at.freebim.db.domain.rel.ParentOf;
import at.freebim.db.domain.rel.References;
import at.freebim.db.service.AppVersionService;
import at.freebim.db.service.ComponentService;
import at.freebim.db.service.LibraryService;
import at.freebim.db.service.MeasureService;
import at.freebim.db.service.ParameterService;
import at.freebim.db.service.RelationService;
import at.freebim.db.service.ValueListService;

/**
 * The app-version service. This class implements {@link AppVersionService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.service.AppVersionService
 */
@Service
public class AppVersionServiceImpl implements AppVersionService {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AppVersionServiceImpl.class);

	/**
	 * The service that handles relations.
	 */
	@Autowired
	private RelationService relationService;

	/**
	 * The service that handles the {@link ValueList}.
	 */
	@Autowired
	private ValueListService valueListService;

	/**
	 * The service that handles {@link Component}s.
	 */
	@Autowired
	private ComponentService componentService;

	/**
	 * The service that handles {@link Parameter}s.
	 */
	@Autowired
	private ParameterService parameterService;

	/**
	 * The service that handles {@link Measure}s.
	 */
	@Autowired
	private MeasureService measureService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.AppVersionService#createParentOfRelations()
	 */
	@Override
	@Transactional
	public Long createParentOfRelations() {

		Long res = 0L;
		String q = "MATCH (a)-[co:CHILD_OF]->(b) RETURN a, co, b";

		Iterable<Map<String, Object>> result = this.relationService.getTemplate().query(q, new HashMap<>(), true);
		Iterator<Map<String, Object>> iter = result.iterator();

		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			try {
				HierarchicalBaseNode a = (HierarchicalBaseNode) map.get("a");
				ChildOf r = (ChildOf) map.get("co");
				HierarchicalBaseNode b = (HierarchicalBaseNode) map.get("b");

				ParentOf po = new ParentOf();
				po.setInfo(r.getInfo());
				po.setOrdering(r.getOrdering());
				po.setN1(b);
				po.setN2(a);

				this.relationService.save(po);
				this.relationService.delete(r);

				res++;

			} catch (Exception e) {
				logger.error("Error in 'IN': ", e);
			}
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.AppVersionService#createValueListOfComponentRelations()
	 */
	public Long createValueListOfComponentRelations() {

		logger.info("createValueListOfComponentRelations ...");

		Long res = 0L;

		HashMap<String, Long> parameterIdMap = new HashMap<>();
		HashMap<Long, Long> parameterMeasureMap = new HashMap<>();
		HashMap<Long, ValueList> valueListMap = new HashMap<>();
		HashMap<Long, Component> componentMap = new HashMap<>();
		HashMap<Long, Parameter> parameterMap = new HashMap<>();
		HashMap<Long, Measure> measureMap = new HashMap<>();
		List<Long> paramsToKeep = new ArrayList<>();
		List<Long> paramsToDelete = new ArrayList<>();
		List<Long> measuresToKeep = new ArrayList<>();
		List<Long> measuresToDelete = new ArrayList<>();
		List<Long> relationsHandled = new ArrayList<>();
		List<Long> relationsToDelete = new ArrayList<>();

		StringBuilder b = new StringBuilder();

		b.append("MATCH path = (bbn:BigBangNode)-[:PARENT_OF*]->(c)");
		b.append(
				" WHERE ALL (y in nodes(path) WHERE y.validFrom<timestamp() AND (y.validTo IS NULL OR y.validTo > timestamp()))");
		b.append(" WITH c AS c");
		b.append(" MATCH path = (c)-[:HAS_PARAMETER]->(p)");
		b.append(
				" WHERE ALL (y in nodes(path) WHERE y.validFrom<timestamp() AND (y.validTo IS NULL OR y.validTo > timestamp()))");
		b.append(" with p.name AS pname, count(*) AS cnt");
		b.append(" MATCH (y:Parameter)");
		b.append(" WHERE cnt > 1");
		b.append(" AND y.name=pname");
		b.append(" AND y.validFrom<timestamp() AND (y.validTo IS NULL OR y.validTo > timestamp())");
		b.append(" WITH cnt AS cnt, y AS p");
		b.append(" MATCH path = (c)-[hp:HAS_PARAMETER]->(p)-[:HAS_MEASURE]->(m)-[hv:HAS_VALUE]->(vl)");
		b.append(
				" where ALL (y in nodes(path) WHERE y.validFrom<timestamp() AND (y.validTo IS NULL OR y.validTo > timestamp()))");
		b.append(" OPTIONAL MATCH (p)-[eq:EQUALS]-(x)");
		b.append(" return ID(c) AS cid");
		b.append(" , ID(p) AS pid");
		b.append(" , p.name AS pname");
		b.append(" , ID(m) AS mid");
		b.append(" , ID(vl) AS vlid");
		b.append(" , hp.phaseUuid AS phase");
		b.append(" , hp.ordering AS ordering");
		b.append(" , hv AS hv");
		b.append(" , eq AS eq");

		String query = b.toString();

		Iterable<Map<String, Object>> result = this.relationService.getTemplate().query(query, new HashMap<>(), true);
		Iterator<Map<String, Object>> iter = result.iterator();

		while (iter.hasNext()) {

			Map<String, Object> map = iter.next();
			try {
				String pname = (String) map.get("pname");
				pname = pname.trim();
				Long pid = parameterIdMap.get(pname);
				Parameter p;

				Long vlid = (Long) map.get("vlid");
				ValueList vl = valueListMap.get(vlid);
				if (vl == null) {
					vl = this.valueListService.getByNodeId(vlid);
					valueListMap.put(vlid, vl);
				}
				Long cid = (Long) map.get("cid");
				Component c = componentMap.get(cid);
				if (c == null) {
					c = this.componentService.getByNodeId(cid);
					componentMap.put(cid, c);
				}

				if (pid == null) {
					// never seen this parameter before
					pid = (Long) map.get("pid");
					parameterIdMap.put(pname, pid);
					parameterMeasureMap.put(pid, (Long) map.get("mid"));
					p = this.parameterService.getByNodeId(pid);
					parameterMap.put(pid, p);
					paramsToKeep.add(pid);

					Long mid = (Long) map.get("mid");
					Measure m = this.measureService.getByNodeId(mid);
					// rename that Measure, it was name of ValueList
					// an becomes name of Parameter now.
					m.setName(p.getName());
					m = this.measureService.save(m);
					logger.info("\tMeasure [{}] saved.", m.getName());
					measureMap.put(pid, m);
					measuresToKeep.add(mid);

					HasValue r = (HasValue) map.get("hv");
					if (r != null) {
						// HasValue hv = (HasValue) this.relationService.createTreeRel(r);
						this.relationService.delete(r);
					}
					HasValue hv = new HasValue();
					hv.setN1(m);
					hv.setN2(vl);
					hv.setComponentUuid(c.getUuid());
					hv = (HasValue) this.relationService.save(hv);
					logger.info("\tHasValue relation id=[{}] saved.", hv.getId());

				} else {
					// we have a parameter with the same name already
					// so we could delete this one and use the one we got first
					p = parameterMap.get(pid);

					pid = (Long) map.get("pid");
					if (!paramsToKeep.contains(pid) && !paramsToDelete.contains(pid)) {
						paramsToDelete.add(pid);
					}

					// create new (:Component)-[:HAS_PARAMETER]->(:Parameter) relation
					// but check for existing first ...
					HasParameter hp = null;
					Iterable<HasParameter> ci = c.getParameter();
					Iterator<HasParameter> citer = ci.iterator();
					while (citer.hasNext()) {
						HasParameter existingHp = citer.next();
						if (p.getNodeId().equals(existingHp.getN2().getNodeId())) {
							hp = existingHp;
							break;
						}
					}
					if (hp == null) {
						// no existing found, create new ...
						hp = new HasParameter();
						hp.setN1(c);
						hp.setN2(p);
						// get the order
						Integer ordering = (Integer) map.get("ordering");
						hp.setOrdering((ordering == null) ? 0 : ordering);
						// get the Phase
						String phase = (String) map.get("phase");
						hp.setPhaseUuid(phase);
						hp = (HasParameter) this.relationService.save(hp);
						logger.info("\tHasParameter relation id=[{}] saved.", hp.getId());

						c = this.componentService.getByNodeId(c.getNodeId());
						componentMap.put(cid, c);
					}

					// there might be EQUALS relations from deleted Parameter to other nodes
					Equals eq = (Equals) map.get("eq");
					if (eq != null && !relationsHandled.contains(eq.getId())) {
						Equals rel = eq;

						if (rel != null) {
							relationsHandled.add(rel.getId());
							BaseNode other = ((rel.getN1().getNodeId().equals(pid)) ? rel.getN2() : rel.getN1());

							if (other != null) {
								Equals e = new Equals();
								if (rel.getN1().getNodeId().equals(pid)) {
									e.setN1(p);
									e.setN2(other);
								} else {
									e.setN1(other);
									e.setN2(p);
								}
								e.setQ(rel.getQ());
								e = (Equals) this.relationService.save(e);
								logger.info("\tEquals relation id=[{}] saved.", e.getId());

								// delete the old EQUALS relation:
								if (!relationsToDelete.contains(rel.getId())) {
									relationsToDelete.add(rel.getId());
								}
							}

						}
					}

					// we could delete the Measure too ...
					Long mid = (Long) map.get("mid");
					if (!measuresToKeep.contains(mid) && !measuresToDelete.contains(mid)) {
						measuresToDelete.add(mid);
					}

					// create new (:Measure)-[:HAS_VALUE]->(:ValueList) relation
					Measure m = measureMap.get(p.getNodeId());

					Iterable<HasValue> iterable = m.getValue();
					Iterator<HasValue> hviter = iterable.iterator();
					boolean hasHv = false;
					while (hviter.hasNext()) {
						HasValue hv = hviter.next();
						if (hv.getN1().getNodeId().equals(m.getNodeId())
								&& hv.getN2().getNodeId().equals(vl.getNodeId())
								&& c.getUuid().equals(hv.getComponentUuid())) {
							hasHv = true;
							break;
						}
					}
					if (!hasHv) {
						HasValue hv = new HasValue();
						hv.setN1(m);
						hv.setN2(vl);
						hv.setComponentUuid(c.getUuid());
						hv = (HasValue) this.relationService.save(hv);
						logger.info("\tHasValue relation id=[{}] saved.", hv.getId());

						m = this.measureService.getByNodeId(m.getNodeId());
						measureMap.put(p.getNodeId(), m);
					}

					res++;
				}

			} catch (Exception e) {
				logger.error("Error in createValueListOfComponentRelations: ", e);
			}
		}

		// actually delete unused Parameters and Measures:
		for (Long pid : paramsToDelete) {
			int counter = 100;
			while (counter-- > 0) {
				try {
					this.parameterService.deleteByNodeId(pid);
					counter = 0;
					break;
				} catch (DeadlockDetectedException e) {
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e1) {
						counter = 0;
					}
				} catch (Exception ex) {
					logger.error("Error when trying to delete node=[{}]", pid);
				}
			}
			logger.info("\tParameter nodeId=[{}] deleted.", pid);
		}
		for (Long pid : measuresToDelete) {
			int counter = 100;
			while (counter-- > 0) {
				try {
					this.measureService.deleteByNodeId(pid);
					counter = 0;
					break;
				} catch (DeadlockDetectedException e) {
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e1) {
						counter = 0;
					}
				} catch (Exception ex) {
					logger.error("Error when trying to delete node=[{}]", pid);
				}
			}
			logger.info("\tMeasure nodeId=[{}] deleted.", pid);
		}
		for (Long pid : relationsToDelete) {
			int counter = 100;
			while (counter-- > 0) {
				try {
					this.relationService.deleteByNodeId(pid);
					counter = 0;
					break;
				} catch (DeadlockDetectedException e) {
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e1) {
						counter = 0;
					}
				} catch (Exception ex) {
					logger.error("Error when trying to delete node=[{}]", pid);
				}
			}
			logger.info("\tEquals relation Id=[{}] deleted.", pid);
		}

		logger.info("\t[{}] Parameters deleted.", paramsToDelete.size());
		logger.info("\t[{}] Measures deleted.", measuresToDelete.size());
		logger.info("\t[{}] Equals relations deleted.", relationsToDelete.size());

		logger.info("createValueListOfComponentRelations finished.");

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.AppVersionService#dropDuplicateEqualRelations()
	 */
	@Override
	@Transactional
	public Long dropDuplicateEqualRelations() {

		logger.info("dropDuplicateEqualRelations ...");

		List<Long> toDelete = new ArrayList<>();
		StringBuilder b = new StringBuilder();

		b.append("MATCH (a)-[r1:EQUALS]->(b)-[r2:EQUALS]->(a)");
		b.append(" WHERE r1.q = r2.q");
		b.append(" return ID(r2) AS id");

		Iterable<Map<String, Object>> result = this.relationService.getTemplate().query(b.toString(), new HashMap<>(), true);
		Iterator<Map<String, Object>> iter = result.iterator();

		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();

			try {

				Long id = (Long) map.get("id");
				if (!toDelete.contains(id)) {
					toDelete.add(id);
				}

			} catch (Exception e) {
				logger.error("Error in dropDuplicateEqualRelations: ", e);
			}
		}

		for (Long id : toDelete) {
			int counter = 100;
			while (counter-- > 0) {
				try {
					BaseRel<?, ?> rel = this.relationService.getByNodeId(id);
					this.relationService.delete(rel);
					logger.info("duplicate Equal Relation id=[{}] deleted.", id);
					counter = 0;
					break;
				} catch (DeadlockDetectedException e) {
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e1) {
						counter = 0;
					}
				} catch (Exception ex) {
					logger.error("Error when trying to delete node=[{}]", id);
				}
			}
		}

		logger.info("dropDuplicateEqualRelations finished, count=[{}].", toDelete.size());
		return (long) toDelete.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.AppVersionService#dropMultipleEqualRelations()
	 */
	@Override
	@Transactional
	public Long dropMultipleEqualRelations() {

		logger.info("dropMultipleEqualRelations ...");

		List<String> existing = new ArrayList<>();
		List<Long> toDelete = new ArrayList<>();
		StringBuilder b = new StringBuilder();

		b.append("MATCH (a)-[r:EQUALS]->(b)");
		b.append(" WITH a AS a, count(*) AS cnt");
		b.append(" WHERE cnt > 1");
		b.append(" MATCH (a)-[r:EQUALS]->(b)");
		b.append(" WITH a AS a, b AS b, count(*) AS cnt");
		b.append(" WHERE cnt > 1");
		b.append(" MATCH (a)-[r:EQUALS]->(b)");
		b.append(" RETURN ID(a) AS ida, ID(b) AS idb, ID(r) AS idr");

		Iterable<Map<String, Object>> result = this.relationService.getTemplate().query(b.toString(), new HashMap<>(), true);
		Iterator<Map<String, Object>> iter = result.iterator();

		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();

			try {

				Long ida = (Long) map.get("ida");
				Long idb = (Long) map.get("idb");
				String key = ida + "-->" + idb;
				if (existing.contains(key)) {
					Long idr = (Long) map.get("idr");
					if (!toDelete.contains(idr)) {
						toDelete.add(idr);
					}
				} else {
					existing.add(key);
				}

			} catch (Exception e) {
				logger.error("Error in dropMultipleEqualRelations: ", e);
			}
		}

		for (Long id : toDelete) {
			int counter = 100;
			while (counter-- > 0) {
				try {
					
					BaseRel<?, ?> rel = this.relationService.getByNodeId(id);
					this.relationService.delete(rel);
					logger.info("multiple Equal Relation id=[{}] deleted.", id);
					counter = 0;
					break;
				} catch (DeadlockDetectedException e) {
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e1) {
						counter = 0;
					}
				} catch (Exception ex) {
					logger.error("Error when trying to delete node=[{}]", id);
				}
			}
		}

		logger.info("dropMultipleEqualRelations finished, count=[{}]", toDelete.size());
		return (long) toDelete.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.AppVersionService#dropEqualSelfRelations()
	 */
	@Override
	@Transactional
	public Long dropEqualSelfRelations() {

		logger.info("dropEqualSelfRelations ...");

		List<Long> toDelete = new ArrayList<>();
		StringBuilder b = new StringBuilder();

		b.append("MATCH (a)-[r:EQUALS]->(b)");
		b.append(" WHERE a=b");
		b.append(" RETURN ID(r) AS idr");

		Iterable<Map<String, Object>> result = this.relationService.getTemplate().query(b.toString(), new HashMap<>(), true);
		Iterator<Map<String, Object>> iter = result.iterator();

		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();

			try {

				Long idr = (Long) map.get("idr");
				if (!toDelete.contains(idr)) {
					toDelete.add(idr);
				}

			} catch (Exception e) {
				logger.error("Error in dropEqualSelfRelations: ", e);
			}
		}

		for (Long id : toDelete) {
			int counter = 100;
			while (counter-- > 0) {
				try {
					BaseRel<?, ?> rel = this.relationService.getByNodeId(id);
					this.relationService.delete(rel);
					logger.info("Equal self Relation id=[{}] deleted.", id);
					counter = 0;
					break;
				} catch (DeadlockDetectedException e) {
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e1) {
						counter = 0;
					}
				} catch (Exception ex) {
					logger.error("Error when trying to delete node=[{}]", id);
				}
			}
		}

		logger.info("dropEqualSelfRelations finished, count=[{}]", toDelete.size());
		return (long) toDelete.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.AppVersionService#performDbCleanup()
	 */
	@Override
	@Transactional
	public void performDbCleanup() {
		logger.info("performDbCleanup ...");

		StringBuilder b = new StringBuilder();

		b.append("MATCH (co:Component)-[r:HAS_PARAMETER]->(x)");
		b.append(" WHERE NOT (x:Parameter)");
		b.append(" DELETE r");

		this.relationService.getTemplate().query(b.toString(), new HashMap<>());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.AppVersionService#correctComponentLibraryReferences()
	 */
	@Override
	public void correctComponentLibraryReferences() {
		/*correctLibraryReferences(LibraryService.LIBRARY_NAME_FREEBIM);
		correctLibraryReferences(LibraryService.LIBRARY_NAME_IFC2x3);
		correctLibraryReferences(LibraryService.LIBRARY_NAME_IFC4);
		correctLibraryReferences(LibraryService.LIBRARY_NAME_FREECLASS);
		correctPsetReferences();*/
	}

	/**
	 * Correct the {@link ParameterSet} {@link References}. This is done be deleting
	 * the old one and creating a new one.
	 */
	private void correctPsetReferences() {
		logger.info("correctPsetReferences ...");

		Map<String, Object> params = new HashMap<>();
		params.put("libName", LibraryService.LIBRARY_NAME_IFC4);
		StringBuilder b = new StringBuilder();

		// correct reference relations for ParameterSet instances
		// they all should reference IFC-Library
		b.append("MATCH (lib:Library)");
		b.append(" WHERE lib.name={libName}");
		b.append(" WITH DISTINCT lib");
		b.append(" MATCH (pset:ParameterSet)");
		b.append(" WHERE NOT (pset)-[:REFERENCES]->(lib)");
		b.append(" WITH DISTINCT lib, pset");
		b.append(" MATCH (pset)-[oldRef:REFERENCES]->()");
		b.append(" CREATE (pset)-[newRef:REFERENCES { ");
		b.append(" __type__:'References'");
		b.append(", refIdName:oldRef.refIdName");
		b.append(", refId: oldRef.refId");
		b.append(", ts: oldRef.ts");
		b.append(", validFrom:oldRef.validFrom");
		b.append(", validTo: oldRef.validTo");
		b.append(", info: oldRef.info");
		b.append(" } ]->(lib)");
		b.append(" WITH oldRef, newRef");
		b.append(" DELETE oldRef");
		b.append(" RETURN count(newRef) AS cnt");

		Iterable<Map<String, Object>> result = this.relationService.getTemplate().query(b.toString(), params);
		Iterator<Map<String, Object>> iter = result.iterator();

		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			Number cnt = (Number) map.get("cnt");
			logger.info("corrected [{}] reference relations for ParameterSets.", cnt.longValue());
		}
	}

	/**
	 * Correct the {@link References} of {@link Component}s to {@link Library}s.
	 * This is done be either creating one if there is none at all or by correcting
	 * it if they point to the wrong {@link Library}.
	 *
	 * @param libName the id of the {@link Library} that will be corrected
	 */
	private void correctLibraryReferences(String libName) {
		logger.info("correctLibraryReferences [{}] ...", libName);

		Map<String, Object> params = new HashMap<>();
		params.put("libName", libName);
		StringBuilder b = new StringBuilder();

		// create reference relations if there is non at all
		b.append("MATCH (lib:Library)");
		b.append(" WHERE lib.name={libName}");
		b.append(" WITH DISTINCT lib");
		b.append(" MATCH (lib)-[:PARENT_OF*0..]->(co:Component)");
		b.append(" WHERE NOT (co)-[:REFERENCES]->()");
		b.append(" WITH DISTINCT lib, co");
		b.append(" CREATE (co)-[newRef:REFERENCES { ");
		b.append(" __type__:'References'");
		b.append(" } ]->(lib)");
		b.append(" RETURN count(newRef) AS cnt");

		Iterable<Map<String, Object>> result = this.relationService.getTemplate().query(b.toString(), params);
		Iterator<Map<String, Object>> iter = result.iterator();

		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			Number cnt = (Number) map.get("cnt");
			logger.info("created [{}] reference relations for Library [{}].", cnt.longValue(), libName);
		}

		b.setLength(0);

		// correct reference relations if they point to the wrong Library
		b.append("MATCH (lib:Library)");
		b.append(" WHERE lib.name={libName}");
		b.append(" WITH DISTINCT lib");
		b.append(" MATCH (lib)-[:PARENT_OF*0..]->(co:Component)");
		b.append(" WHERE NOT (co)-[:REFERENCES]->(lib)");
		b.append(" WITH DISTINCT lib, co");
		b.append(" MATCH (co)-[oldRef:REFERENCES]->()");
		b.append(" CREATE (co)-[newRef:REFERENCES { ");
		b.append(" __type__:'References'");
		b.append(", refIdName:oldRef.refIdName");
		b.append(", refId: oldRef.refId");
		b.append(", ts: oldRef.ts");
		b.append(", validFrom:oldRef.validFrom");
		b.append(", validTo: oldRef.validTo");
		b.append(", info: oldRef.info");
		b.append(" } ]->(lib)");
		b.append(" WITH oldRef, newRef");
		b.append(" DELETE oldRef");
		b.append(" RETURN count(newRef) AS cnt");

		result = this.relationService.getTemplate().query(b.toString(), params);
		iter = result.iterator();

		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			Number cnt = (Number) map.get("cnt");
			logger.info("corrected [{}] reference relations for Library [{}].", cnt.longValue(), libName);
		}
	}

}
