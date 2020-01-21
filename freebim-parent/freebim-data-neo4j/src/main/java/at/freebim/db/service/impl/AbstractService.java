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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.Coded;
import at.freebim.db.domain.base.Described;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.Ngramed;
import at.freebim.db.service.NgramService;

/**
 * This is the basis service for a class <b>T</b> that extends {@link BaseNode}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.BaseNode
 */
public abstract class AbstractService<T extends BaseNode> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AbstractService.class);

	/**
	 * The service that handles n-grams.
	 */
	@Autowired
	protected NgramService ngramService;

	/**
	 * This is the basic repository to communicate with the neo4j database. The
	 * class <b>T</b> is the node on which you do operations.
	 */
	protected Neo4jRepository<T, Long> repository;

	/**
	 * Set the repository.
	 *
	 * @param r the repository
	 */
	public abstract void setRepository(Neo4jRepository<T, Long> r);

	/**
	 * Get all nodes of the type <b>T</b> that are in the database.
	 *
	 * @return all nodes of the type <b>T</b>
	 */
	@Transactional(readOnly = true)
	public ArrayList<T> getAll() {
		final Iterable<T> users = this.repository.findAll();
		final Iterator<T> iter = users.iterator();
		final ArrayList<T> list = new ArrayList<T>();

		iter.forEachRemaining(list::add);

		return list;
	}

	/**
	 * Get the node of the type <b>T</b> that has the provided id.
	 *
	 * @param nodeId the id of the node
	 * @return the node of the type <b>T</b>
	 */
	@Transactional(readOnly = true)
	public T getByNodeId(Long nodeId) {
		logger.debug("getByNodeId: {}", nodeId);
		return this.repository.findById(nodeId).orElse(null);
	}

	/**
	 * Save a node of the type <b>T</b>.
	 *
	 * @param node the node to save
	 * @return the saved node
	 */
	@Transactional
	public T save(T node) {
		node = this.repository.save(node);
		logger.debug("{} saved, nodeId={}.", node.getClass().getSimpleName(), node.getNodeId());
		return node;
	}

	/**
	 * Delete a node.
	 *
	 * @param node the node that will be deleted
	 * @return the deleted node
	 */
	@Transactional
	public T delete(T node) {
		deleteNgrams(node);
		this.repository.delete(node);
		return node;
	}

	/**
	 * Delete the created n-gram nodes for the provided node.
	 *
	 * @param node the node from which the n-grams will be deleted
	 */
	@Transactional
	protected void deleteNgrams(T node) {
		if (Named.class.isAssignableFrom(node.getClass())) {
			this.ngramService.deleteFor((Ngramed) node, Named.class);
		}
		if (Described.class.isAssignableFrom(node.getClass())) {
			this.ngramService.deleteFor((Ngramed) node, Described.class);
		}
		if (Coded.class.isAssignableFrom(node.getClass())) {
			this.ngramService.deleteFor((Ngramed) node, Coded.class);
		}
	}
}
