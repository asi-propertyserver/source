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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.neo4j.kernel.DeadlockDetectedException;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.BsddNode;
import at.freebim.db.domain.Company;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.DataType;
import at.freebim.db.domain.Discipline;
import at.freebim.db.domain.Document;
import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Measure;
import at.freebim.db.domain.MessageNode;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.ParameterSet;
import at.freebim.db.domain.Phase;
import at.freebim.db.domain.SimpleNamedNode;
import at.freebim.db.domain.Unit;
import at.freebim.db.domain.ValueList;
import at.freebim.db.domain.ValueListEntry;
import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.ContributedBaseNode;
import at.freebim.db.domain.base.HierarchicalBaseNode;
import at.freebim.db.domain.base.LifetimeBaseNode;
import at.freebim.db.domain.base.NodeIdentifyable;
import at.freebim.db.domain.base.Role;
import at.freebim.db.domain.base.State;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.rel.BelongsTo;
import at.freebim.db.domain.rel.Bsdd;
import at.freebim.db.domain.rel.ChildOf;
import at.freebim.db.domain.rel.CompanyCompany;
import at.freebim.db.domain.rel.ComponentComponent;
import at.freebim.db.domain.rel.ContainsParameter;
import at.freebim.db.domain.rel.ContributedBy;
import at.freebim.db.domain.rel.DocumentedIn;
import at.freebim.db.domain.rel.Equals;
import at.freebim.db.domain.rel.HasEntry;
import at.freebim.db.domain.rel.HasMeasure;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.HasParameterSet;
import at.freebim.db.domain.rel.HasValue;
import at.freebim.db.domain.rel.MessageClosed;
import at.freebim.db.domain.rel.MessageSeen;
import at.freebim.db.domain.rel.NgramCodeOf;
import at.freebim.db.domain.rel.NgramDescOf;
import at.freebim.db.domain.rel.NgramNameOf;
import at.freebim.db.domain.rel.OfDataType;
import at.freebim.db.domain.rel.OfDiscipline;
import at.freebim.db.domain.rel.OfMaterial;
import at.freebim.db.domain.rel.OfUnit;
import at.freebim.db.domain.rel.ParentOf;
import at.freebim.db.domain.rel.References;
import at.freebim.db.domain.rel.Responsible;
import at.freebim.db.domain.rel.UnitConversion;
import at.freebim.db.domain.rel.ValueOverride;
import at.freebim.db.domain.rel.WorksFor;
import at.freebim.db.dto.Relations;
import at.freebim.db.repository.BaseNodeRepository;
import at.freebim.db.repository.BigBangNodeRepository;
import at.freebim.db.repository.BsddNodeRepository;
import at.freebim.db.repository.CompanyRepository;
import at.freebim.db.repository.ComponentRepository;
import at.freebim.db.repository.ContributorRepository;
import at.freebim.db.repository.DataTypeRepository;
import at.freebim.db.repository.DisciplineRepository;
import at.freebim.db.repository.DocumentRepository;
import at.freebim.db.repository.FreebimUserRepository;
import at.freebim.db.repository.LibraryRepository;
import at.freebim.db.repository.LifetimeBaseNodeRepository;
import at.freebim.db.repository.MeasureRepository;
import at.freebim.db.repository.MessageNodeRepository;
import at.freebim.db.repository.ParameterRepository;
import at.freebim.db.repository.ParameterSetRepository;
import at.freebim.db.repository.PhaseRepository;
import at.freebim.db.repository.SimpleNamedNodeRepository;
import at.freebim.db.repository.UnitRepository;
import at.freebim.db.repository.ValueListEntryRepository;
import at.freebim.db.repository.ValueListRepository;
import at.freebim.db.service.BigBangNodeService;
import at.freebim.db.service.BsddNodeService;
import at.freebim.db.service.DateService;
import at.freebim.db.service.FreebimUserService;
import at.freebim.db.service.LifetimeBaseNodeService;
import at.freebim.db.service.RelationService;

/**
 * The service that handles relations in the neo4j graph. This service
 * implements {@link RelationService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.service.RelationService
 */
@Service
public class RelationServiceImpl implements RelationService {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(RelationServiceImpl.class);

	/**
	 * The service that handles nodes that extends {@link LifetimeBaseNode}.
	 */
	@Autowired
	private LifetimeBaseNodeService<LifetimeBaseNode> lifetimeBaseNodeService;

	/**
	 * The service that handles the {@link BigBangNode}.
	 */
	@Autowired
	private BigBangNodeService bigBangNodeService;

	/**
	 * The service that handles dates.
	 */
	@Autowired
	private DateService dateService;

	/**
	 * A {@link Session} serves as the main point of integration for the Neo4j OGM.
	 * All the publicly-available capabilities of the framework are defined by this
	 * interface
	 */
	@Autowired
	private Session template;

	/**
	 * The service that handles the {@link FreebimUser}.
	 */
	@Autowired
	private FreebimUserService freebimUserService;
	
	
	@Autowired
	private BaseNodeRepository baseNodeRepository;
	@Autowired 
	private BigBangNodeRepository bigBangNodeRepository;
	@Autowired
	private BsddNodeRepository bsddNodeRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private ComponentRepository componentRepository;
	@Autowired
	private ContributorRepository contributorRepository;
	@Autowired
	private DataTypeRepository dataTypeRepository;
	@Autowired
	private DisciplineRepository disciplineRepository;
	@Autowired
	private DocumentRepository documentRepository;
	@Autowired
	private FreebimUserRepository freebimUserRepository;
	@Autowired
	private LibraryRepository libraryRepository;
	@Autowired 
	private MeasureRepository measureRepository;
	@Autowired
	private MessageNodeRepository messageNodeRepository;
	@Autowired
	private ParameterRepository parameterRepository;
	@Autowired
	private ParameterSetRepository parameterSetRepository;
	@Autowired
	private PhaseRepository phaseRepository;
	@Autowired
	private SimpleNamedNodeRepository simpleNamedNodeRepository;
	@Autowired
	private UnitRepository unitRepository;
	@Autowired
	private ValueListEntryRepository valueListEntryRepository;
	@Autowired
	private ValueListRepository valueListRepository;
	
	private BaseNode loadNode(Long nodeId, Class<? extends BaseNode> clazz) {
		switch (clazz.getSimpleName()) {
			case  "BaseNode":
				return baseNodeRepository.findById(nodeId).get();
			case "BigBangNode":
				return bigBangNodeRepository.findById(nodeId).get();
			case "BsddNode":
				return bsddNodeRepository.findById(nodeId).get();
			case "Company":
				return companyRepository.findById(nodeId).get();
			case "Component":
				return componentRepository.findById(nodeId).get();
			case "Contributor":
				return contributorRepository.findById(nodeId).get();
			case "DataType":
				return dataTypeRepository.findById(nodeId).get();
			case "Discipline":
				return disciplineRepository.findById(nodeId).get();
			case "Document":
				return documentRepository.findById(nodeId).get();
			case "FreebimUser":
				return freebimUserRepository.findById(nodeId).get();
			case "Library":
				return libraryRepository.findById(nodeId).get();
			case "Measure":
				return measureRepository.findById(nodeId).get();
			case "MessageNode":
				return messageNodeRepository.findById(nodeId).get();
			case "Parameter":
				return parameterRepository.findById(nodeId).get();
			case "ParameterSet":
				return parameterSetRepository.findById(nodeId).get();
			case "Phase":
				return phaseRepository.findById(nodeId).get();
			case "SimpleNamedNode":
				return simpleNamedNodeRepository.findById(nodeId).get();
			case "Unit":
				return unitRepository.findById(nodeId).get();
			case "ValueList":
				return valueListRepository.findById(nodeId).get();
			case "ValueListEntry":
				return valueListEntryRepository.findById(nodeId).get();
			default:
				{
					BaseNode temp = getTemplate().load(BaseNode.class, nodeId);
					return getTemplate().load(temp.getClass(), nodeId, 1);
				}
		}
	}
	
	private BaseNode saveNode(BaseNode node) {
		switch (node.getClass().getSimpleName()) {
			case  "BaseNode":
				return baseNodeRepository.save(node);
			case "BigBangNode":
				return bigBangNodeRepository.save((BigBangNode)node);
			case "BsddNode":
				return bsddNodeRepository.save((BsddNode)node);
			case "Company":
				return companyRepository.save((Company)node);
			case "Component":
				return componentRepository.save((Component)node);
			case "Contributor":
				return contributorRepository.save((Contributor)node);
			case "DataType":
				return dataTypeRepository.save((DataType)node);
			case "Discipline":
				return disciplineRepository.save((Discipline)node);
			case "Document":
				return documentRepository.save((Document)node);
			case "FreebimUser":
				return freebimUserRepository.save((FreebimUser)node);
			case "Library":
				return libraryRepository.save((Library)node);
			case "Measure":
				return measureRepository.save((Measure)node);
			case "MessageNode":
				return messageNodeRepository.save((MessageNode)node);
			case "Parameter":
				return parameterRepository.save((Parameter)node);
			case "ParameterSet":
				return parameterSetRepository.save((ParameterSet)node);
			case "Phase":
				return phaseRepository.save((Phase)node);
			case "SimpleNamedNode":
				return simpleNamedNodeRepository.save((SimpleNamedNode)node);
			case "Unit":
				return unitRepository.save((Unit)node);
			case "ValueList":
				return valueListRepository.save((ValueList)node);
			case "ValueListEntry":
				return valueListEntryRepository.save((ValueListEntry)node);
			default:
				{
					getTemplate().save(node);
					return node;
				}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.RelationService#getTemplate()
	 */
	@Override
	public Session getTemplate() {
		return this.template;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.BaseService#save(java.lang.Object)
	 */
	@Override
	@Transactional
	public BaseRel<?, ?> save(BaseRel<?, ?> rel) {		
		this.template.save(rel);
		return rel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.BaseService#getByNodeId(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public BaseRel<?, ?> getByNodeId(Long nodeId) {
		BaseRel<?, ?> temp = this.template.load(BaseRel.class, nodeId);
		return this.template.load(temp.getClass(), nodeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.BaseService#delete(java.lang.Object)
	 */
	@Override
	@Transactional
	public BaseRel<?, ?> delete(BaseRel<?, ?> rel) {
		this.template.delete(rel);
		return rel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.RelationService#deleteAllRelationsFor(at.freebim.db.
	 * domain.base.BaseNode, java.lang.String)
	 */
	@Override
	@Transactional
	public void deleteAllRelationsFor(BaseNode node, String reltype) {
		StringBuilder b = new StringBuilder();
		b.append("MATCH (n)-[r]-(x) WHERE ID(n)={id} AND r.type={reltype} DELETE r");
		String query = b.toString();

		logger.debug("deleteAllRelationsFor: query=[{}]", query);

		HashMap<String, Object> params = new HashMap<>();
		params.put("id", node.getNodeId());
		params.put("reltype", reltype);

		this.template.query(query, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.RelationService#getAllPaths(java.lang.Long,
	 * boolean, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<SimplePathResult> getAllPaths(Long nodeId, boolean onlyValid, Long max) {
		logger.debug("getAllPaths node {}", nodeId);

		BigBangNode bigBangNode = bigBangNodeService.getBigBangNode();

		// when start node equals end not the function allShortestPaths does not work.
		if (bigBangNode.getNodeId() == nodeId) {
			logger.debug("getAllPaths does not work on the BigBangNode {}", nodeId);
			return null;
		}

		List<SimplePathResult> res = new ArrayList<>();
		HashMap<String, Object> params = new HashMap<>();
		params.put("id", nodeId);
		if (max != null) {
			params.put("max", max);
		}

		StringBuilder b = new StringBuilder();
		b.append("Match (n) where Id(n)={id} with n as n MATCH (bbn:BigBangNode)");

		b.append(" WITH n AS n, bbn AS bbn MATCH");
		b.append(
				" path = allShortestPaths( (bbn)-[:PARENT_OF|HAS_PARAMETER|HAS_MEASURE|HAS_VALUE|HAS_ENTRY|DOCUMENTED_IN*]->(n) )");
		if (onlyValid) {
			b.append(
					" WHERE ALL (y IN nodes(path) WHERE y.validFrom < timestamp() AND (y.validTo IS NULL OR y.validTo > timestamp()) )");
		}
		b.append(" RETURN distinct [ids IN nodes(path) | ID(ids)] AS ids");
		b.append(", [names IN nodes(path) where names.name IS NOT NULL | names.name] AS names");
		b.append(", [clazz IN [n IN nodes(path) | [cn in labels(n) where cn =~ '_.*']] | clazz] AS cn");
		if (max != null) {
			b.append(" LIMIT {max}");
		}
		String query = b.toString();

		logger.debug("query= [{}]", query);

		Iterable<Map<String, Object>> result = this.template.query(query, params, true);
		Iterator<Map<String, Object>> iter = result.iterator();

		while (iter.hasNext()) {
			SimplePathResult path = new SimplePathResult();
			res.add(path);
			Map<String, Object> map = iter.next();
			Long[] w = (Long[]) map.get("ids");

			if (w != null) {
				for (Long o : w) {
					path.ids.add(o);
				}
			}

			String[] names = (String[]) map.get("names");

			if (names != null) {
				for (String o : names) {
					path.names.add(o);
				}
			}

			Object[] cn = (Object[]) map.get("cn");

			if (cn != null) {
				for (Object o : cn) {
					if (o instanceof List) {
						List<String> temp = (List<String>) o;
						if (temp.size() > 0) {
							String cnResult = temp.get(0);
							cnResult = cnResult.substring(1);
							path.clNames.add(cnResult);
						}
					}
				}
			}
			logger.debug("    ids [{}] on path to node {} ", path.ids, nodeId);
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.RelationService#getAllRelatedInOut(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public ArrayList<RelationResult> getAllRelatedInOut(Long nodeId) {
		logger.debug("getAllRelatedInOut for node {}", nodeId);

		ArrayList<RelationResult> res = new ArrayList<>();

		Map<String, Object> params = new HashMap<>();
		params.put("id", nodeId);

		StringBuilder b = new StringBuilder();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// permission required
		FreebimUser user = null;
		if (auth != null) {
			logger.debug("auth.name=[{}]", auth.getName());
			user = this.freebimUserService.get(auth.getName());

			if (user != null) {

				if (user.getRoles().contains(Role.ROLE_GUEST)) {
					b.append("MATCH (n) WHERE ID(n)={id}");
					b.append(" AND (\"StatedBaseNode\" in labels(n) and (n.status = \"CHECKED\" or ");
					b.append("(\"BigBangNode\" in labels(n) or \"Library\" in labels(n))))");

					b.append(" OPTIONAL MATCH (n)<-[relIn]-(a) WHERE");
					b.append(" type(relIn) <> \"");
					b.append(RelationType.REFERENCES);
					b.append("\"");
					b.append(" AND");
					b.append(" type(relIn) <> \"");
					b.append(RelationType.NGRAM_NAME_OF);
					b.append("\"");
					b.append(" AND");
					b.append(" type(relIn) <> \"");
					b.append(RelationType.NGRAM_CODE_OF);
					b.append("\"");
					b.append(" AND");
					b.append(" type(relIn) <> \"");
					b.append(RelationType.NGRAM_DESC_OF);
					b.append("\"");
					b.append(" AND");
					b.append(" type(relIn) <> \"");
					b.append(RelationType.BELONGS_TO);
					b.append("\"");

					b.append(" AND (\"StatedBaseNode\" in labels(a) and (a.status = \"CHECKED\" or ");
					b.append("(\"BigBangNode\" in labels(a) or \"Library\" in labels(a))))");

					b.append(" OPTIONAL MATCH (n)-[relOut]->(b) WHERE");
					b.append(" type(relOut) <> \"");
					b.append(RelationType.NGRAM_NAME_OF);
					b.append("\"");
					b.append(" AND");
					b.append(" type(relOut) <> \"");
					b.append(RelationType.NGRAM_CODE_OF);
					b.append("\"");
					b.append(" AND");
					b.append(" type(relOut) <> \"");
					b.append(RelationType.NGRAM_DESC_OF);
					b.append("\"");
					b.append(" AND");
					b.append(" type(relOut) <> \"");
					b.append(RelationType.BELONGS_TO);
					b.append("\"");
					b.append(" AND (\"StatedBaseNode\" in labels(b) and (b.status = \"CHECKED\" or ");
					b.append("(\"BigBangNode\" in labels(b) or \"Library\" in labels(b))))");
					b.append(" RETURN distinct n, a, b, relIn, relOut, relIn.ts AS t1, relOut.ts AS t2");
				} else {
					b.append("MATCH (n) WHERE ID(n)={id}");
					b.append(" OPTIONAL MATCH (n)<-[relIn]-(a) WHERE");
					b.append(" type(relIn) <> \"");
					b.append(RelationType.REFERENCES);
					b.append("\"");
					b.append(" AND");
					b.append(" type(relIn) <> \"");
					b.append(RelationType.NGRAM_NAME_OF);
					b.append("\"");
					b.append(" AND");
					b.append(" type(relIn) <> \"");
					b.append(RelationType.NGRAM_CODE_OF);
					b.append("\"");
					b.append(" AND");
					b.append(" type(relIn) <> \"");
					b.append(RelationType.NGRAM_DESC_OF);
					b.append("\"");
					b.append(" AND");
					b.append(" type(relIn) <> \"");
					b.append(RelationType.BELONGS_TO);
					b.append("\"");

					b.append(" OPTIONAL MATCH (n)-[relOut]->(b) WHERE");
					b.append(" type(relOut) <> \"");
					b.append(RelationType.NGRAM_NAME_OF);
					b.append("\"");
					b.append(" AND");
					b.append(" type(relOut) <> \"");
					b.append(RelationType.NGRAM_CODE_OF);
					b.append("\"");
					b.append(" AND");
					b.append(" type(relOut) <> \"");
					b.append(RelationType.NGRAM_DESC_OF);
					b.append("\"");
					b.append(" AND");
					b.append(" type(relOut) <> \"");
					b.append(RelationType.BELONGS_TO);
					b.append("\"");
					b.append(" RETURN distinct n, a, b, relIn, relOut, relIn.ts AS t1, relOut.ts AS t2");

				}
			}
		}

		String query = b.toString();
		logger.debug("query= [{}]", query);

		Iterable<Map<String, Object>> result = this.template.query(query, params, true);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			try {
				BaseRel<?, ?> relTypeIn = (BaseRel<?, ?>) map.get("relIn");
				Long ts = (Long) map.get("t1");
				if (relTypeIn != null) {
					BaseNode r = (BaseNode) map.get("a");
					if (r != null && !this.isInResult(res, r)) {
						RelationResult rr = new RelationResult();
						rr.className = r.getClass().getSimpleName();
						rr.node = loadNode(r.getNodeId(), r.getClass());
						rr.relation = relTypeIn;
						rr.dir = "IN";
						rr.ts = ts;
						if (!res.contains(rr))
							res.add(rr);
					}
				}
			} catch (Exception e) {
				logger.error("Error in 'IN': ", e);
			}
			try {
				BaseRel<?, ?> relTypeOut = (BaseRel<?, ?>) map.get("relOut");
				Long ts = (Long) map.get("t2");
				if (relTypeOut != null) {
					BaseNode r = (BaseNode) map.get("b");
					if (r != null && !this.isInResult(res, r)) {
						RelationResult rr = new RelationResult();
						rr.className = r.getClass().getSimpleName();
						rr.node = loadNode(r.getNodeId(), r.getClass());
						rr.relation = relTypeOut;
						rr.dir = "OUT";
						rr.ts = ts;
						if (!res.contains(rr))
							res.add(rr);
					}
				}
			} catch (Exception e) {
				logger.error("Error in 'OUT': ", e);
			}
		}

		Long now = this.dateService.getMillis();
		for (RelationResult rr : res) {
			BaseNode bn = rr.node;
			if (Component.class.isAssignableFrom(bn.getClass())) {
				((Component) bn).setM(this.isMaterial(bn.getNodeId()));
			}
			if (LifetimeBaseNode.class.isAssignableFrom(bn.getClass())) {
				bn = this.lifetimeBaseNodeService.filterResponse((LifetimeBaseNode) bn, now);
			}

		}
		return res;

	}

	private boolean isInResult(List<RelationResult> res, BaseNode n) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.ComponentService#isMaterial(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isMaterial(final Long nodeId) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.BaseService#deleteByNodeId(java.lang.Long)
	 */
	@Override
	public BaseRel<?, ?> deleteByNodeId(Long nodeId) {
		return null;
	}
	
	//https://stackoverflow.com/questions/22031207/find-all-classes-and-interfaces-a-class-extends-or-implements-recursively
	private Set<Class<?>> getAllExtendedOrImplementedTypesRecursively(Class<?> clazz) {
	    List<Class<?>> res = new ArrayList<>();

	    do {
	        res.add(clazz);

	        // Add the super class
	        Class<?> superClass = clazz.getSuperclass();

	        // Interfaces does not have java,lang.Object as superclass, they have null, so break the cycle and return
	        if (superClass == null) {
	            break;
	        }

	        // Now inspect the superclass 
	        clazz = superClass;
	    } while (!"java.lang.Object".equals(clazz.getCanonicalName()));

	    return new HashSet<Class<?>>(res);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.RelationService#updateRelations(java.lang.Long,
	 * at.freebim.db.dto.Relations[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public <FROM extends NodeIdentifyable, TO extends NodeIdentifyable> UpdateRelationsResult<FROM, TO> updateRelations(
			Long nodeId, Relations[] relationsArray, Class<? extends BaseNode> clazz) {

		logger.debug("update relations of nodeId={}", nodeId);
		UpdateRelationsResult<FROM, TO> updateRelationsResult = new UpdateRelationsResult<FROM, TO>();

		BaseNode node = loadNode(nodeId, clazz);

		if (node == null) {
			logger.error("No Node for nodeId=[{}].", nodeId);
			return updateRelationsResult;
		}
		
		//get all fields
		Set<Class<?>> classes = getAllExtendedOrImplementedTypesRecursively(clazz);
		List<Field> nodeFields = new ArrayList<Field>();
		for(Class<?> c:classes) {
			for(Field f:c.getDeclaredFields()) {
				nodeFields.add(f);
			}
		}
		
		//update all relationship fields
		for(Field f: nodeFields) {
			f.setAccessible(true);
			Relationship t = f.getAnnotation(Relationship.class);
			if (t != null) {
				String type = t.type();
				String direction = t.direction();
				if (direction.equals("OUTGOING")) { //TODO: switch
					direction = "OUT";
				}
				if (direction.equals("INCOMING")) {
					direction = "IN";
				}
				if (direction.equals("UNDIRECTED")) {
					direction = "BOTH";
				}
				
				for (Relations r: relationsArray) {
					if (RelationTypeEnum.fromCode(r.t).name().equals(type) && r.dir.equals(direction)) {			
						try {
							Object tempRel = f.get(node);
							if (tempRel == null) {
								tempRel = f.getType().newInstance();//new ArrayList<BaseRel<FROM, TO>>();
							}
							
							
							if (tempRel instanceof ArrayList) {
								HashMap<Long, BaseRel<FROM, TO>> existingRelations = new HashMap<>();
								for (BaseRel<FROM, TO> nr: (ArrayList<BaseRel<FROM, TO>>)tempRel) {
									existingRelations.put(nr.getId(), nr);
								}
								f.set(node, f.getType().newInstance());
								tempRel = f.getType().newInstance();
								
								//check if relationship already exists
								for (BaseRel<FROM, TO> rel:(BaseRel<FROM, TO>[])r.relations) {
									Long relatedNodeId = null;
									boolean in = false;
									switch (r.dir) {
									case "IN":
										relatedNodeId = rel.getN1Id();
										in = true;
										break;
									case "BOTH":
										if (nodeId.equals(rel.getN1Id())) {
											relatedNodeId = rel.getN2Id();
										} else {
											relatedNodeId = rel.getN1Id();
											in = true;
										}
										break;
									case "OUT":
										relatedNodeId = rel.getN2Id();
										break;
									}
									
									if (relatedNodeId != null) {
										BaseNode rn = this.template.load(BaseNode.class, relatedNodeId);
										rn = loadNode(relatedNodeId, rn.getClass());
										
										if (rn != null) {
											try {
												if (in) {
													FROM relatedNode = (FROM) rn;
													rel.setN1(relatedNode);
													rel.setN2((TO) node);
												} else {
													TO relatedNode = (TO) rn;// this.createTreeNode(rn);
													rel.setN1((FROM) node);
													rel.setN2(relatedNode);
												}
											} catch (ConcurrencyFailureException e) {
												throw e;
											} catch (DeadlockDetectedException e) {
												throw e;
											} catch (Exception e) {
												logger.error("Can't create [" + rel.getType() + "] relation for node [" + node.getNodeId()
														+ "]: ", e);
												continue;
											}
											
											BaseRel<?, ?> existingRel = existingRelations.get(rel.getId());
											if (rel.equals(existingRel)) {
												existingRelations.remove(rel.getId());
												logger.debug("old {} relation unchanged for nodeId={}", type, nodeId);
											} else {
												if (rel.getId() != null) {
													BaseRel<FROM, TO> toRemove = null;
													for(BaseRel<FROM, TO> updateRel: (ArrayList<BaseRel<FROM, TO>>)tempRel) {
														if (updateRel.getId().equals(rel.getId())) {
															toRemove = updateRel;
														}
													}
													
													if (toRemove != null) {
														((ArrayList<BaseRel<FROM, TO>>)tempRel).remove(toRemove);
													}
												}
												
											}
											((ArrayList<BaseRel<FROM, TO>>)tempRel).add(rel);
										}
									}

								}
								f.set(node, tempRel);
							}
						} catch (IllegalArgumentException | IllegalAccessException | SecurityException | InstantiationException e) {
							logger.debug("old {} Exception when updating relations for nodeId={}", type, nodeId);
							e.printStackTrace();
						}
					}
				}
			}
		}
		node = saveNode(node);
		
		updateRelationsResult.baseNode = node;

		return updateRelationsResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.RelationService#findByState(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public ArrayList<StatedBaseNode> findByState(String state) {

		String query = "MATCH (n:StatedBaseNode) WHERE n.state={state} RETURN n";
		Map<String, Object> params = new HashMap<>();
		params.put("state", state);

		logger.debug("query= [{}]", query);

		ArrayList<StatedBaseNode> res = new ArrayList<>();
		Iterable<Map<String, Object>> result = this.template.query(query, params, true);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			try {
				StatedBaseNode n = (StatedBaseNode) map.get("n");
				if (n != null) {
					res.add(n);
				}
			} catch (Exception e) {
				logger.error("Error in 'IN': ", e);
			}
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.RelationService#findByFreebimId(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public UuidIdentifyable findByFreebimId(String freebimId) {

		String query = "MATCH (n:UuidIdentifyable) WHERE n.uuid={uuid} RETURN n";
		Map<String, Object> params = new HashMap<>();
		params.put("uuid", freebimId);

		logger.debug("query= [{}]", query);

		UuidIdentifyable res = null;
		Iterable<Map<String, Object>> result = this.template.query(query, params, true);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			try {
				res = (UuidIdentifyable) map.get("n");
			} catch (Exception e) {
				logger.error("Error in 'IN': ", e);
			}
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.RelationService#findByBsddGuid(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public ArrayList<BaseNode> findByBsddGuid(String bsddGuid) {
		logger.debug("findByBsddGuid [{}]", bsddGuid);
		String query = "MATCH (n:BaseNode) WHERE n.bsddGuid IS NOT NULL AND n.bsddGuid={bsddGuid} RETURN n";
		Map<String, Object> params = new HashMap<>();
		params.put("bsddGuid", bsddGuid);

		logger.debug("query= [{}]", query);

		ArrayList<BaseNode> res = new ArrayList<>();
		Iterable<Map<String, Object>> result = this.template.query(query, params, true);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			try {
				BaseNode n = (BaseNode) map.get("n");
				if (n != null) {
					res.add(n);
				}
			} catch (Exception e) {
				logger.error("Error in 'IN': ", e);
			}
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.RelationService#fetch(at.freebim.db.domain.base.
	 * BaseNode)
	 */
	@Override
	@Transactional(readOnly = true)
	public BaseNode fetch(BaseNode node, Class<? extends BaseNode> clazz) {
		if (node != null) {
			try {
				return loadNode(node.getNodeId(), clazz);
			} catch (Exception e) {
				logger.error("Error fetching node with nodeId=" + node.getNodeId() + ".", e);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.RelationService#getByUuid(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public UuidIdentifyable getByUuid(String uuid) {

		String query = "MATCH (n:UuidIdentifyable) WHERE n.uuid={uuid} RETURN n";
		Map<String, Object> params = new HashMap<>();
		params.put("uuid", uuid);

		logger.debug("query= [{}]", query);

		UuidIdentifyable res = null;
		Iterable<Map<String, Object>> result = this.template.query(query, params, true);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			try {
				res = (UuidIdentifyable) map.get("n");
				res = (UuidIdentifyable)loadNode(res.getNodeId(), res.getClass());
			} catch (Exception e) {
				logger.error("Error in 'IN': ", e);
			}
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.RelationService#getNodeById(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public BaseNode getNodeById(Long nodeId, Class<? extends BaseNode> clazz) {
		if (nodeId != null) {
			try {
				BaseNode node = loadNode(nodeId.longValue(), clazz);

				
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();

				// permission required
				FreebimUser user = null;
				if (auth != null) {
					logger.debug("auth.name=[{}]", auth.getName());
					user = this.freebimUserService.get(auth.getName());

					if (user != null) {
						if (user.getRoles().contains(Role.ROLE_GUEST)) {
							if (node instanceof StatedBaseNode) {
								if (!(node instanceof BigBangNode) && !(node instanceof Component)
										&& !(node instanceof Library)) {
									StatedBaseNode temp = (StatedBaseNode) node;

									if (temp.getState() != State.CHECKED) {
										return null;
									}
								}
							}
						}
					}
				}

				return loadNode(nodeId.longValue(), clazz);
			} catch (Exception e) {
				logger.error("Error fetching node with nodeId=" + nodeId + ".", e);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.RelationService#getRelatedNode(at.freebim.db.domain.
	 * base.BaseNode, at.freebim.db.domain.base.rel.BaseRel,
	 * org.neo4j.graphdb.Direction)
	 */
	@Transactional(readOnly = true)
	@Override
	public <R extends BaseRel<? extends NodeIdentifyable, ? extends NodeIdentifyable>> BaseNode getRelatedNode(
			BaseNode src, R rel, String dir) {
		if (src != null && rel != null) {
			BaseNode child = null;
			switch (dir) {
			case Relationship.INCOMING:
				child = (BaseNode) rel.getN1();
				break;
			case Relationship.OUTGOING:
				child = (BaseNode) rel.getN2();
				break;
			case Relationship.UNDIRECTED:
				if (src.getNodeId().equals(rel.getN1().getNodeId())) {
					child = (BaseNode) rel.getN2();
				} else if (src.getNodeId().equals(rel.getN2().getNodeId())) {
					child = (BaseNode) rel.getN1();
				}
				break;
			}
			return this.fetch(child, child.getClass());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.RelationService#saveDefaultForParameter(at.freebim.db.
	 * domain.rel.HasParameter)
	 */
	@Override
	@Transactional
	public HasParameter saveDefaultForParameter(HasParameter rel) {
		if (rel != null) {
			this.template.save(rel);
		}
		return rel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.BaseService#getAll(boolean)
	 */
	@Override
	public ArrayList<BaseRel<?, ?>> getAll(boolean onlyRelevant) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.RelationService#createIfNotExists(at.freebim.db.domain.
	 * base.rel.BaseRel, java.util.Map)
	 */
	@Override
	public <FROM extends NodeIdentifyable, TO extends NodeIdentifyable> void createIfNotExists(BaseRel<FROM, TO> rel,
			Map<String, Object> relProperties) {
		if (rel != null && rel.getN1() != null && rel.getN2() != null && rel.getN1().getNodeId() != null
				&& rel.getN2().getNodeId() != null) {
			StringBuilder b = new StringBuilder();
			Map<String, Object> params = new HashMap<>();
			params.put("ida", rel.getN1().getNodeId());
			params.put("idb", rel.getN2().getNodeId());
			params.put("__reltype__", rel.getClass().getSimpleName());

			b.append("MATCH (a), (b) WHERE ID(a)={ida} AND ID(b)={idb}");
			b.append(" WITH a, b");
			b.append(" MERGE (a)-[:");
			b.append(rel.getType());
			b.append("{ __type__ : {__reltype__}");
			if (relProperties != null && relProperties.size() > 0) {
				for (String k : relProperties.keySet()) {
					b.append(", ");
					b.append(k);
					b.append(" : {");
					b.append(k);
					b.append("}");
					params.put(k, relProperties.get(k));
				}
			}
			b.append(" }");
			b.append("]->(b)");
			b.append(" RETURN 1");

			this.template.query(b.toString(), params);
		}
	}
}
