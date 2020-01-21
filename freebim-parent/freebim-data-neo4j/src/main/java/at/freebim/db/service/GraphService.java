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
package at.freebim.db.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.base.rel.BaseRel;

/**
 * The service for the graph.
 *
 * @author rainer.breuss@uibk.ac.at
 */
public interface GraphService {

	/**
	 * Get the {@link Graph} for the node with the provided node id.
	 *
	 * @param nodeId     the id of the node
	 * @param recursive  should the {@link Graph} be created recursively
	 * @param withParams should {@link Parameter} be included
	 * @param withEquals should equals be included
	 * @return the created {@link Graph}
	 */
	Graph getGraphFor(Long nodeId, boolean recursive, boolean withParams, boolean withEquals);

	/**
	 * The graph.
	 *
	 * @author rainer.breuss@uibk.ac.at
	 * @see java.io.Serializable
	 */
	class Graph implements Serializable {
		private static final long serialVersionUID = -2150449743117268199L;

		/**
		 * The relation/links/edges of the graph.
		 */
		public final ArrayList<BaseRel<?, ?>> links;

		/**
		 * The nodes of the graph.
		 */
		public final Map<Long, Node> nodes;

		public Graph() {
			this.links = new ArrayList<BaseRel<?, ?>>();
			this.nodes = new HashMap<Long, Node>();
		}
	}

	/**
	 * A node of the {@link Graph}.
	 *
	 * @author rainer.breuss@uibk.ac.at
	 * @see at.freebim.db.service.GraphService.Graph
	 * @see java.io.Serializable
	 */
	class Node implements Serializable {
		private static final long serialVersionUID = -321505272565973072L;

		/**
		 * The id of the {@link Node}.
		 */
		public final Long id;

		/**
		 * The name of the {@link Node}.
		 */
		public final String n;

		/**
		 * The type of the {@link Node}.
		 */
		public final String t;

		/**
		 * The libraries of the {@link Node}.
		 */
		public final ArrayList<Long> libs;

		public Node(Long id, String name, String type) {
			super();
			this.id = id;
			this.n = name;
			this.t = type;
			this.libs = new ArrayList<Long>();
		}
	}
}
