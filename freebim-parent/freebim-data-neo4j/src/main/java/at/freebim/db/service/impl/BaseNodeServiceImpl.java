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

import org.neo4j.graphdb.Node;
import org.neo4j.kernel.DeadlockDetectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.Coded;
import at.freebim.db.domain.base.Described;
import at.freebim.db.domain.base.Named;
import at.freebim.db.service.AsyncNgramCreator;
import at.freebim.db.service.BaseNodeService;
import at.freebim.db.service.DateService;
import at.freebim.db.service.RelationService;

/**
 * This service defines the basics for all services that 
 * define functionality for a class <b>T</b> that extends {@link BaseNode}.
 * This service extends {@link AbstractService} and implements {@link BaseNodeService}.
 * 
 * @see at.freebim.db.domain.base.BaseNode
 * @see at.freebim.db.service.impl.AbstractService
 * @see at.freebim.db.service.BaseNodeService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public abstract class BaseNodeServiceImpl<T extends BaseNode> extends AbstractService<T> implements BaseNodeService<T> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(BaseNodeServiceImpl.class);
	
	/**
	 * The creator for n-grams.
	 */
	@Autowired
	private AsyncNgramCreator asyncNgramCreator;
	
	/**
	 * The services that handles relations.
	 */
	@Autowired
	private RelationService relationService;
	
	/**
	 * The service that handles dates.
	 */
	@Autowired
	private DateService dateService;
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.AbstractService#getAll()
	 */
	@Override
	@Transactional(readOnly=true)
	public ArrayList<T> getAll(boolean onlyRelevant) {
		if (!onlyRelevant) {
			final ArrayList<T> all = super.getAll();
			logger.debug("getAll: got {} objects for service {}", ((all == null) ? "null" : all.size()), this.getClass());
			final ArrayList<T> res = new ArrayList<T>();
			final Long now = this.dateService.getMillis();
			for (T node : all) {
				node = this.filterResponse(node, now);
				if (node != null)
					res.add(node);
			}
			logger.debug("getAll: has {} objects after filtering.",res.size());
			return res;
		} else {
			return getAllRelevant();
		}
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.AbstractService#getByNodeId(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public T getByNodeId(Long nodeId) {
		T node = super.getByNodeId(nodeId);
		node = this.filterResponse(node, null);
		return node;
	}
	
	
	

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.AbstractService#save(at.freebim.db.domain.base.BaseNode)
	 */
	@Override
	@Transactional
	public T save(T node) {
		logger.debug("save: {} nodeId={} ...", node == null ? "?" : node.getClass().getSimpleName(), node == null ? "null" : node.getNodeId());

		boolean isInsert = (node.getNodeId() == null);
		if (isInsert)
			node = this.filterBeforeInsert(node);
		else {
			node = this.filterBeforeSave(node);
		}
		T saved = null;

		if (node.getNodeId() != null) {
			
			saved = this.repository.findOne(node.getNodeId());

			if (node.equals(saved)) {
				logger.debug("{} not modified.", node.getClass().getSimpleName());
				return node;
			}
		}
		
		try {
			
			node = super.save(node);
			
		} catch (ConcurrencyFailureException e) {
			logger.info(e.getMessage());
			// caught by calling code, hopefully ...
			throw e;
		} catch (DeadlockDetectedException e) {
			logger.info(e.getMessage());
			// caught by calling code, hopefully ...
			throw e;
		} catch (Exception e) {
			logger.error("Can't save node!", e);
			throw e;
		}

		if (isInsert)
			node = this.filterAfterInsert(node);
		else
			node = this.filterAfterSave(node);
		
		node = this.filterResponse(node, null);
		logger.debug("saved: nodeId={}", node == null ? "null" : node.getNodeId());

		if (node != null) {
			boolean needsNgramUpdate = (saved == null);
			if (!needsNgramUpdate && Named.class.isAssignableFrom(node.getClass())) {
				Named n = (Named) node;
				final String name = n.getName();
				String oldName = ((Named) saved).getName();
				if (oldName != null) {
					needsNgramUpdate = (!oldName.equals(name));
				} else {
					needsNgramUpdate = (name != null);
				}
			}
			if (!needsNgramUpdate && Described.class.isAssignableFrom(node.getClass())) {
				Described n = (Described) node;
				final String desc = n.getDesc();
				String oldDesc = ((Described) saved).getDesc();
				if (oldDesc != null) {
					needsNgramUpdate = (!oldDesc.equals(desc));
				} else {
					needsNgramUpdate = (desc != null);
				}
			}
			if (!needsNgramUpdate && Coded.class.isAssignableFrom(node.getClass())) {
				Coded n = (Coded) node;
				final String code = n.getCode();
				String oldcode = ((Coded) saved).getCode();
				if (oldcode != null) {
					needsNgramUpdate = (!oldcode.equals(code));
				} else {
					needsNgramUpdate = (code != null);
				}
			}
			if (needsNgramUpdate) {
				this.asyncNgramCreator.add(node.getNodeId());
			}
		}
		return node;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BaseNodeService#filterResponse(at.freebim.db.domain.base.BaseNode)
	 */
	@Override
	public T filterResponse(T node, Long now) {
		return node;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BaseNodeService#filterBeforeSave(at.freebim.db.domain.base.BaseNode)
	 */
	@Override
	public T filterBeforeSave(T node) {
		return node;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.BaseNodeService#filterAfterSave(at.freebim.db.domain.base.BaseNode)
	 */
	@Override
	public T filterAfterSave(T node) {
		return node;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BaseNodeService#filterBeforeInsert(at.freebim.db.domain.base.BaseNode)
	 */
	@Override
	public T filterBeforeInsert(T node) {
		return node;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BaseNodeService#filterAfterInsert(at.freebim.db.domain.base.BaseNode)
	 */
	@Override
	public T filterAfterInsert(T node) {
		return node;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BaseNodeService#filterBeforeDelete(at.freebim.db.domain.base.BaseNode)
	 */
	@Override
	public T filterBeforeDelete(T node) {
		return node;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BaseNodeService#filterAfterDelete(at.freebim.db.domain.base.BaseNode)
	 */
	@Override
	public T filterAfterDelete(T node) {
		return node;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BaseService#deleteByNodeId(java.lang.Long)
	 */
	@Override
	@Transactional
	public T deleteByNodeId(Long nodeId) {
		if (nodeId != null) {
			T node = this.repository.findOne(nodeId);
			if (node != null) {
				try {
					node = filterBeforeDelete(node);

					node = delete(node);
					
					node = filterAfterDelete(node);
					
				} catch (AccessDeniedException e) {
					logger.error("Can't delete node, nodeId=[" + nodeId + "]", e);
					throw e;
				} 
				node = filterResponse(node, null);
				return node;
			}
		}
		return null;
	}
	
	/**
	 * @param b
	 * @param returnStatement
	 */
	protected abstract void getRelevantQuery(StringBuilder b, String returnStatement);
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.BaseNodeService#getAllRelevant()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public ArrayList<T> getAllRelevant() {

		ArrayList<T> res = new ArrayList<T>();
		StringBuilder b = new StringBuilder();

		final Long now = this.dateService.getMillis();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "now", now );

		getRelevantQuery(b, " RETURN DISTINCT y AS n");
		
		String query = b.toString();

		Result<Map<String, Object>> result = this.relationService.getTemplate().query(query, params);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			Node n = (Node) map.get("n");
			BaseNode pathNode = this.relationService.createTreeNode(n);
			
			res.add((T) pathNode);
			
			
			logger.debug("    found node {} ", pathNode.getNodeId());
		}
		logger.debug("getAllRelevant returns [{}] nodes.", res.size());
		return res;
	}
	

	/* (non-Javadoc)
	 * @see at.freebim.db.service.BaseNodeService#getAllRelevantNodeIds()
	 */
	@Override
	@Transactional(readOnly=true)
	public ArrayList<Long> getAllRelevantNodeIds() {
		ArrayList<Long> res = new ArrayList<Long>();
		StringBuilder b = new StringBuilder();

		final Long now = this.dateService.getMillis();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "now", now );

		getRelevantQuery(b, " RETURN DISTINCT ID(y) AS id");

		String query = b.toString();

		Result<Map<String, Object>> result = this.relationService.getTemplate().query(query, params);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			Long nodeId = (Long) map.get("id");
			res.add(nodeId);
			logger.debug("    found node {} ", nodeId);
		}
		if (logger.isInfoEnabled()) {
			logger.info("\tfound {} relevant node id's for class " + this.getClass().getSimpleName() + " in {} ms.", res.size(), this.dateService.getMillis() - now);
		}
		return res;
	}
}
