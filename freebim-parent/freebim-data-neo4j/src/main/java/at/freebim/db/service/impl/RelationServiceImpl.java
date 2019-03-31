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

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.kernel.DeadlockDetectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.Component;
import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.LifetimeBaseNode;
import at.freebim.db.domain.base.NodeIdentifyable;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.dto.Relations;
import at.freebim.db.service.DateService;
import at.freebim.db.service.LifetimeBaseNodeService;
import at.freebim.db.service.RelationService;
import scala.collection.convert.Wrappers.SeqWrapper;

/**
 * The service that handles relations in the neo4j graph.
 * This service implements {@link RelationService}.
 * 
 * @see at.freebim.db.service.RelationService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Service
public class RelationServiceImpl implements RelationService {
	
	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(RelationServiceImpl.class);

	/**
	 * Mediator class for the graph related services of neo4j.
	 */
	@Autowired
	private Neo4jTemplate template;

	/**
	 * The service that handles nodes that extends {@link LifetimeBaseNode}.
	 */
	@Autowired
	private LifetimeBaseNodeService<LifetimeBaseNode> lifetimeBaseNodeService;
	
	/**
	 * The service that handles dates.
	 */
	@Autowired
	private DateService dateService;

	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#getTemplate()
	 */
	@Override
	public Neo4jTemplate getTemplate() {
		return this.template;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.BaseService#save(java.lang.Object)
	 */
	@Override
	@Transactional
	public BaseRel<?,?> save(BaseRel<?,?> rel) {
		return this.template.save(rel);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BaseService#getByNodeId(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public BaseRel<?,?> getByNodeId(Long nodeId) {
		Relationship rel = this.template.getRelationship(nodeId);
		return this.createTreeRel(rel);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BaseService#delete(java.lang.Object)
	 */
	@Override
	@Transactional
	public BaseRel<?,?> delete(BaseRel<?,?> rel) {
		this.template.delete(rel);
		return rel;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#deleteAllRelationsFor(at.freebim.db.domain.base.BaseNode, java.lang.String)
	 */
	@Override
	@Transactional
	public void deleteAllRelationsFor(BaseNode node, String reltype) {
		StringBuilder b = new StringBuilder();
		b.append("START n=node(");
		b.append(node.getNodeId());
		b.append(") MATCH (n)-[r:");
		b.append(reltype);
		b.append("]-(x) DELETE r");
		String query = b.toString();
		logger.debug("deleteAllRelationsFor: query=[{}]", query);
		this.template.query(query, null);		
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#createTreeNode(org.neo4j.graphdb.Node, java.lang.Class)
	 */
	@Override
	@Transactional
	public BaseNode createTreeNode(Node n, Class<? extends BaseNode> clazz) {
		if (n != null) {
			try {
				BaseNode bn = (BaseNode) this.template.projectTo(n, clazz);
				return bn;
			} catch (Exception ex) {
				logger.error("Can't project node " + n.getId() + " to class " + clazz.getName() + ".", ex);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#createTreeRel(org.neo4j.graphdb.Relationship)
	 */
	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public BaseRel<?,?> createTreeRel(Relationship n) {
		if (n != null) {
			Class<? extends BaseRel<?, ?>> clazz = null;
			try {
				
				clazz = this.template.getStoredJavaType(n);
			} catch (Exception e) {
				logger.error("No stored Java Type for relationship id={}", n.getId());
			}
			if (clazz == null) {
				logger.error("No class found for relationship id={}", n.getId());
				return null;
			}

			if (clazz != null) {
				try {
					BaseRel<?,?> bn = this.template.convert(n, clazz);
					logger.debug(" Relationship is a {} now.", bn.getClass().getName());
					return bn;
				} catch (Exception ex) {
					logger.error("Can't project relation " + n.getId() + " to class " + clazz.getName() + ".", ex);
				}
			}
		}
		return null;
	}

	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#createTreeNode(org.neo4j.graphdb.Node)
	 */
	@Override
	@Transactional
	public BaseNode createTreeNode(Node n) {
		if (n != null) {
			Class<?> clazz = this.template.getStoredJavaType(n);
			if (clazz == null) {
				logger.error("No class found for node. nodeId={}", n.getId());
				return null;
			}
			
			try {
				BaseNode bn = (BaseNode) this.template.projectTo(n, clazz);
				logger.debug("node {} of class {} found.", n.getId(), clazz.getName());
				return bn;
			} catch (Exception ex) {
				logger.error("Can't project node " + n.getId() + " to class " + clazz.getName() + ".", ex);
				return null;
			}
		}
		return null;
	}
	



	


	

	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#getAllPaths(java.lang.Long, boolean, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<SimplePathResult> getAllPaths(Long nodeId, boolean onlyValid, Long max) {
		logger.debug("getAllPaths node {}", nodeId);
		
		List<SimplePathResult> res = new ArrayList<SimplePathResult>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "id", nodeId );
		if (max != null) {
			params.put( "max", max );
		}
		
		StringBuilder b = new StringBuilder();
		b.append("START n=node({id}) MATCH (bbn:BigBangNode)");

		b.append(" WITH n AS n, bbn AS bbn MATCH"); 
		b.append(" path = allShortestPaths( (bbn)-[:PARENT_OF|HAS_PARAMETER|HAS_MEASURE|HAS_VALUE|HAS_ENTRY|DOCUMENTED_IN*]->(n) )");
		if (onlyValid) {
			b.append(" WHERE ALL (y IN nodes(path) WHERE y.validFrom < timestamp() AND (y.validTo IS NULL OR y.validTo > timestamp()) )");
		}
		b.append(" RETURN distinct extract(ids IN nodes(path) | ID(ids)) AS ids");
		b.append(", extract(names IN nodes(path) | names.name) AS names");
		b.append(", extract(clazz IN extract(n IN nodes(path) | filter (cn in labels(n) where cn =~ '_.*')) | clazz) AS cn");
		if (max != null) {
			b.append(" LIMIT {max}");
		}
		String query = b.toString();

		logger.debug("query= [{}]", query);

		Result<Map<String, Object>> result = this.template.query(query, params);
		Iterator<Map<String, Object>> iter = result.iterator();
		
		while (iter.hasNext()) {
			SimplePathResult path = new SimplePathResult();
			res.add(path);
			Map<String, Object> map = iter.next();
			SeqWrapper<?> w = (SeqWrapper<?>) map.get("ids");
			for (Object o : w) {
				path.ids.add((Long) o);
			}
			w = (SeqWrapper<?>) map.get("names");
			for (Object o : w) {
				path.names.add((String) o);
			}
			w = (SeqWrapper<?>) map.get("cn");
			for (Object o : w) {
				scala.collection.convert.Wrappers.SeqWrapper<?> ow = (scala.collection.convert.Wrappers.SeqWrapper<?>) o;
				String cn = ow.get(0).toString();
				cn = cn.substring(1);
				path.clNames.add(cn);
			}
			logger.debug("    ids [{}] on path to node {} ", path.ids, nodeId);
		}
		return res;
	}

	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#getAllRelatedInOut(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public ArrayList<RelationResult> getAllRelatedInOut(Long nodeId) {
		logger.debug("getAllRelatedInOut for node {}", nodeId);
		
		ArrayList<RelationResult> res = new ArrayList<RelationResult>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "id", nodeId );
		
		StringBuilder b = new StringBuilder();
		b.append("START n=node({id}) OPTIONAL MATCH (n)<-[relIn]-(a) WHERE");
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
		b.append(" RETURN distinct a, b, relIn, relOut, relIn.ts AS t1, relOut.ts AS t2");

		String query = b.toString();
		logger.debug("query= [{}]", query);
		
		Result<Map<String, Object>> result = this.template.query(query, params);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			try {
				Relationship relTypeIn = (Relationship) map.get("relIn");
				Long ts = (Long) map.get("t1");
				if (relTypeIn != null) {
					Node r = (Node) map.get("a");
					if (r != null && !this.isInResult(res,  r)) {
						BaseNode bn = this.createTreeNode(r);
						if (bn != null) {
							if (bn != null) {
								RelationResult rr = new RelationResult();
								rr.className = bn.getClass().getSimpleName();
								rr.node = bn;
								BaseRel<?,?> rel = this.createTreeRel(relTypeIn);
								rr.relation = rel;
								rr.dir = "IN";
								rr.ts = ts;
								if (!res.contains(rr))
									res.add(rr);
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error("Error in 'IN': ", e);
			}
			try {
				Relationship relTypeOut = (Relationship) map.get("relOut");
				Long ts = (Long) map.get("t2");
				if (relTypeOut != null) {
					Node r = (Node) map.get("b");
					if (r != null && !this.isInResult(res,  r)) {
						BaseNode bn = this.createTreeNode(r);
						if (bn != null) {
							if (bn != null) {
								RelationResult rr = new RelationResult();
								rr.className = bn.getClass().getSimpleName();
								rr.node = bn;
								BaseRel<?,?> rel = this.createTreeRel(relTypeOut);
								rr.relation = rel;
								rr.dir = "OUT";
								rr.ts = ts;
								if (!res.contains(rr))
									res.add(rr);
							}
						}
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
	
	private boolean isInResult(List<RelationResult> res, Node n) {
		return false;
	}
	


	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.ComponentService#isMaterial(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean isMaterial(final Long nodeId) {
		return false;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BaseService#deleteByNodeId(java.lang.Long)
	 */
	@Override
	public BaseRel<?, ?> deleteByNodeId(Long nodeId) {	
		return null;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#updateRelations(java.lang.Long, at.freebim.db.dto.Relations[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public <FROM extends NodeIdentifyable, TO extends NodeIdentifyable> 
		UpdateRelationsResult<FROM, TO> updateRelations(Long nodeId, Relations [] relationsArray) {

		logger.debug("update relations of nodeId={}", nodeId);
		UpdateRelationsResult<FROM, TO> updateRelationsResult = new UpdateRelationsResult<FROM, TO>();

		Node n = this.template.getNode(nodeId);
		BaseNode node = this.createTreeNode(n);
		if (node == null) {
			logger.error("No Node for nodeId=[{}].", nodeId);
			return updateRelationsResult;
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "nodeId", nodeId );
		
		for (Relations relations : relationsArray) {
		
			StringBuilder b = new StringBuilder();
			String type = RelationTypeEnum.fromCode(relations.t).name();
			b.append("START n=node({nodeId}) MATCH (n)");
			switch (relations.dir) {
			case "IN":
				b.append("<-[rel:");
				b.append(type);
				b.append("]-");
				break;
			case "BOTH": 
				b.append("-[rel:");
				b.append(type);
				b.append("]-");
				break;
			case "OUT": 
				b.append("-[rel:");
				b.append(type);
				b.append("]->");
				break;
			default:
				logger.error("unknown relation direction {} for nodeId={}", relations.dir, nodeId);
				return updateRelationsResult;
			}
			b.append("(x) RETURN rel");
			
			
			String query = b.toString();
			logger.debug("query: {}", query);
			
			HashMap<Long, BaseRel<FROM, TO>> existingRelations = new HashMap<Long, BaseRel<FROM, TO>>();
			Result<Map<String, Object>> result = this.template.query(query, params);
			Iterator<Map<String, Object>> iter = result.iterator();
			while (iter.hasNext()) {
				try {
					Map<String, Object> map = iter.next();
					Relationship relTypeIn = (Relationship) map.get("rel");
					if (relTypeIn != null) {
						BaseRel<FROM, TO> rel = (BaseRel<FROM, TO>) this.createTreeRel(relTypeIn);
						if (rel != null) {
							logger.debug("    got id={} existing {} relations.", rel.getId(), type);
							existingRelations.put(rel.getId(), rel);
						}
					}
				} catch (Exception e) {
					logger.error("Can't get relation of type [" + type + "]: [" + e.getMessage() + "] caught.");
					continue;
				}
			}
			logger.debug("got {} existing {} relations.", existingRelations.keySet().size(), type);
			
			BaseRel<FROM,TO>[] rels = (BaseRel<FROM, TO>[]) relations.relations;
			for (BaseRel<FROM,TO> rel : rels) {
				Long relatedNodeId = null;
				boolean in = false;
				switch (relations.dir) {
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
					Node rn = this.template.getNode(relatedNodeId);
					if (rn != null) {
												
						try {
							if (in) {
								FROM relatedNode = (FROM) this.createTreeNode(rn);
								if (relatedNode == null) {
									logger.error("related node nodeId=[{}] is null.", relatedNodeId);
									continue;
								}
								rel.setN1(relatedNode);
								rel.setN2((TO) node);
							} else {
								TO relatedNode = (TO) this.createTreeNode(rn);
								if (relatedNode == null) {
									logger.error("related node nodeId=[{}] is null.", relatedNodeId);
									continue;
								}
								rel.setN1((FROM) node);
								rel.setN2(relatedNode);
							}
						} catch (ConcurrencyFailureException e) {
							throw e;
						} catch (DeadlockDetectedException e) {
							throw e;
						} catch (Exception e) {
							logger.error("Can't create [" + rel.getType() + "] relation for node [" + node.getNodeId() + "]: ", e);
							continue;
						}
						if (rel.getId() != null) {
							BaseRel<?, ?> existingRel = existingRelations.get(rel.getId());
							if (rel.equals(existingRel)) {
								existingRelations.remove(rel.getId());
								logger.debug("old {} relation unchanged for nodeId={}", type, nodeId);
								continue;
							}
						}
						rel.setId(null); // force creation of a new relationship (seems to be a bug in SDN, when updating existing relations pointing to another node)
						
						updateRelationsResult.addAffectedNode(rel.getN1().getNodeId());
						updateRelationsResult.addAffectedNode(rel.getN2().getNodeId());
						
						try {
							rel = this.template.save(rel);
						} catch (Exception e) {
							logger.error("Can't save [" + rel.getType() + "] relation for node [" + node.getNodeId() + "]: [" + e.getMessage() + "] caught.");
							continue;
						}
						logger.debug("    new {} relation saved for nodeId={}", type, nodeId);
					}
				}
			}
			logger.debug("deleting {} existing {} relations.", existingRelations.keySet().size(), type);
			for (BaseRel<?,?> rel : existingRelations.values()) {
				
				try {
					updateRelationsResult.addAffectedNode(rel.getN1().getNodeId());
					updateRelationsResult.addAffectedNode(rel.getN2().getNodeId());
					
					this.template.delete(rel);
				} catch (Exception e) {
					logger.error("Can't delete old relation id=[" + ((rel == null) ? null : rel.getId()) + "] for nodeId=[" + nodeId + "]: [" + e.getMessage() + "] caught.");
					continue;
				}
				
				logger.debug("    old relation id={} deleted for nodeId={}", rel.getId(), nodeId);
			}
		} // end: for (Relations relations : relationsArray)
		
		n = this.template.getNode(nodeId);
		node = this.createTreeNode(n);
		
		updateRelationsResult.baseNode = node;
		
		return updateRelationsResult;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#findByState(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public ArrayList<StatedBaseNode> findByState(String state) {

		String query = "MATCH (n:StatedBaseNode) WHERE n.state={state} RETURN n";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "state", state );
		
		logger.debug("query= [{}]", query);
		
		ArrayList<StatedBaseNode> res = new ArrayList<StatedBaseNode>();
		Result<Map<String, Object>> result = this.template.query(query, params);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			try {
				Node n = (Node) map.get("n");
				if (n != null) {
					BaseNode bn = this.createTreeNode(n);
					if (bn != null) {
						if (StatedBaseNode.class.isAssignableFrom(bn.getClass())) {
							res.add((StatedBaseNode) bn);
						}
					}
				}
			} catch (Exception e) {
				logger.error("Error in 'IN': ", e);
			}
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#findByFreebimId(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public UuidIdentifyable findByFreebimId(String freebimId) {
		
		String query = "MATCH (n:UuidIdentifyable) WHERE n.uuid={uuid} RETURN n";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "uuid", freebimId );
		
		logger.debug("query= [{}]", query);
		
		UuidIdentifyable res = null;
		Result<Map<String, Object>> result = this.template.query(query, params);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			try {
				Node n = (Node) map.get("n");
				if (n != null) {
					BaseNode bn = this.createTreeNode(n);
					if (bn != null) {
						if (UuidIdentifyable.class.isAssignableFrom(bn.getClass())) {
							if (res != null) {
								logger.error("Multiple nodes found for UUID {}.", freebimId);
							}
							res = (UuidIdentifyable) bn;
						}
					}
				}
			} catch (Exception e) {
				logger.error("Error in 'IN': ", e);
			}
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#findByBsddGuid(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public ArrayList<BaseNode> findByBsddGuid(String bsddGuid) {
		logger.debug("findByBsddGuid [{}]", bsddGuid);
		String query = "MATCH (n:BaseNode) WHERE has(n.bsddGuid) AND n.bsddGuid={bsddGuid} RETURN n";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "bsddGuid", bsddGuid );
		
		logger.debug("query= [{}]", query);
		
		ArrayList<BaseNode> res = new ArrayList<BaseNode>();
		Result<Map<String, Object>> result = this.template.query(query, params);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			try {
				Node n = (Node) map.get("n");
				if (n != null) {
					BaseNode bn = this.createTreeNode(n);
					if (bn != null) {
						res.add(bn);
					}
				}
			} catch (Exception e) {
				logger.error("Error in 'IN': ", e);
			}
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#fetch(at.freebim.db.domain.base.BaseNode)
	 */
	@Override
	@Transactional(readOnly=true)
	public BaseNode fetch(BaseNode node) {
		if (node != null) {
			try {
				return this.template.fetch(node);
			} catch (Exception e) {
				logger.error("Error fetching node with nodeId=" + node.getNodeId() + ".", e);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#getByUuid(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public UuidIdentifyable getByUuid(String uuid) {
		
		String query = "MATCH (n:UuidIdentifyable) WHERE n.uuid={uuid} RETURN n";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "uuid", uuid );
		
		logger.debug("query= [{}]", query);
		
		UuidIdentifyable res = null;
		Result<Map<String, Object>> result = this.template.query(query, params);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			try {
				Node n = (Node) map.get("n");
				if (n != null) {
					BaseNode bn = this.createTreeNode(n);
					if (bn != null) {
						if (UuidIdentifyable.class.isAssignableFrom(bn.getClass())) {
							if (res != null) {
								logger.error("Multiple nodes found for UUID {}.", uuid);
							}
							try {
								res = (UuidIdentifyable) bn;
							} catch (Exception e) {
								return null;
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error("Error in 'IN': ", e);
			}
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#getNodeById(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public BaseNode getNodeById(Long nodeId) {
		if (nodeId != null) {
			Node node;
			try {
				node = this.template.getNode(nodeId.longValue());
				BaseNode bn = this.createTreeNode(node);
				return bn;
			} catch (Exception e) {
				logger.error("Error fetching node with nodeId=" + nodeId + ".", e);
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#getRelatedNode(at.freebim.db.domain.base.BaseNode, at.freebim.db.domain.base.rel.BaseRel, org.neo4j.graphdb.Direction)
	 */
	@Transactional(readOnly=true)
	@Override
	public <R extends BaseRel<? extends NodeIdentifyable, ? extends NodeIdentifyable>> BaseNode getRelatedNode(BaseNode src, R rel, Direction dir) {
		if (src != null && rel != null) {
			BaseNode child = null;
			if (rel != null) {
				switch (dir) {
				case INCOMING : 
					child = (BaseNode) rel.getN1();
					break;
				case OUTGOING : 
					child = (BaseNode) rel.getN2();
					break;
				case BOTH :
					if (src.getNodeId().equals(rel.getN1().getNodeId())) {
						child = (BaseNode) rel.getN2();
					} else if (src.getNodeId().equals(rel.getN2().getNodeId())) {
						child = (BaseNode) rel.getN1();
					}
					break;
				}
				return this.fetch(child);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#saveDefaultForParameter(at.freebim.db.domain.rel.HasParameter)
	 */
	@Override
	@Transactional
	public HasParameter saveDefaultForParameter(HasParameter rel) {
		if (rel != null) {
			rel = this.template.save(rel);
		}
		return rel;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BaseService#getAll(boolean)
	 */
	@Override
	public ArrayList<BaseRel<?, ?>> getAll(boolean onlyRelevant) {
		return null;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.RelationService#createIfNotExists(at.freebim.db.domain.base.rel.BaseRel, java.util.Map)
	 */
	@Override
	public <FROM extends NodeIdentifyable, TO extends NodeIdentifyable> void createIfNotExists(BaseRel<FROM, TO> rel, Map<String, Object> relProperties) {
		if (rel != null && rel.getN1() != null && rel.getN2() != null && rel.getN1().getNodeId() != null && rel.getN2().getNodeId() != null) {
			StringBuilder b = new StringBuilder();
			Map<String, Object> params = new HashMap<String, Object>();
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
