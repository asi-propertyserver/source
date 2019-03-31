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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.security.access.annotation.Secured;

import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.NodeIdentifyable;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.dto.Relations;

/**
 * The service for the relation/class {@link BaseRel}.
 * This service extends {@link BaseService}.
 * 
 * @see at.freebim.db.domain.base.rel.BaseRel
 * @see at.freebim.db.service.BaseService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface RelationService extends BaseService<BaseRel<?,?>> {

	/**
	 * A path of nodes.
	 * 
	 * @author rainer.breuss@uibk.ac.at
	 *
	 */
	public class PathResult {
		/**
		 * The path of nodes.
		 */
		public List<BaseNode> path;
		
		/**
		 * Create new instance.
		 */
		public PathResult() {
			this.path = new ArrayList<BaseNode>();
		}
	}
	/**
	 * A simple path of nodes.
	 * 
	 * @author rainer.breuss@uibk.ac.at
	 *
	 */
	public class SimplePathResult {
		/**
		 * The ids of the nodes from the path.
		 */
		public List<Long> ids;
		
		/**
		 * The names of the nodes from the path. 
		 */
		public List<String> names;
		
		/**
		 * The class names of the nodes from the path.
		 */
		public List<String> clNames;
		
		/**
		 * Create new instance.
		 */
		public SimplePathResult() {
			this.ids = new ArrayList<Long>();
			this.names = new ArrayList<String>();
			this.clNames = new ArrayList<String>();
		}
	}
	
	/**
	 * A simple relation.
	 * 
	 * @author rainer.breuss@uibk.ac.at
	 *
	 */
	public class RelationResult {
		/**
		 * The direction of the relation.
		 */
		public String dir;
		
		/**
		 * A node of the relation.
		 */
		public BaseNode node;
		
		/**
		 * The class name of the node.
		 */
		public String className;
		
		/**
		 * The relation.
		 */
		public BaseRel<?,?> relation;
		
		/**
		 * The time stamp.
		 */
		public Long ts;
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((className == null) ? 0 : className.hashCode());
			result = prime * result + ((dir == null) ? 0 : dir.hashCode());
			result = prime * result + ((node == null) ? 0 : node.hashCode());
			result = prime * result + ((ts == null) ? 0 : ts.hashCode());
			result = prime * result
					+ ((relation == null) ? 0 : relation.hashCode());
			return result;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			RelationResult other = (RelationResult) obj;
			if (className == null) {
				if (other.className != null)
					return false;
			} else if (!className.equals(other.className))
				return false;
			if (dir == null) {
				if (other.dir != null)
					return false;
			} else if (!dir.equals(other.dir))
				return false;
			if (node == null) {
				if (other.node != null)
					return false;
			} else if (!node.equals(other.node))
				return false;
			if (ts == null) {
				if (other.ts != null)
					return false;
			} else if (!ts.equals(other.ts))
				return false;
			if (relation == null) {
				if (other.relation != null)
					return false;
			} else if (!relation.equals(other.relation))
				return false;
			return true;
		}
	}

	/**
	 * Represents a result of updates to nodes that have been made.
	 * 
	 * @author rainer.breuss@uibk.ac.at
	 */
	public class UpdateRelationsResult<FROM extends NodeIdentifyable, TO extends NodeIdentifyable> {
		/**
		 * The base node.
		 */
		public BaseNode baseNode;
		/**
		 * All id of all affected nodes.
		 */
		public final ArrayList<Long> affectedNodes;
		
		/**
		 * Create new instance.
		 */
		public UpdateRelationsResult() {
			super();
			this.affectedNodes = new ArrayList<Long>();
		}
		/**
		 * Add the id of an affected node.
		 * 
		 * @param id the id of an affected node
		 */
		public void addAffectedNode(Long id) {
			if (!this.affectedNodes.contains(id)) {
				this.affectedNodes.add(id);
			}
		}
	}


	/**
	 * Delete all relations of a specific type from the provided node.
	 * 
	 * @param node the node from which the relations will be deleted
	 * @param reltype the type of the relations that should be deleted
	 */
	@Secured({"ROLE_EDIT", "ROLE_ADMIN"})
	public void deleteAllRelationsFor(BaseNode node, String reltype);
	
	/**
	 * Get the mediator class for the graph related services like the {@link org.neo4j.graphdb.GraphDatabaseService}, 
	 * the used {@link org.springframework.data.neo4j.core.TypeRepresentationStrategy}, 
	 * entity instantiators for nodes and relationships as well as a spring conversion service.
	 * @return the {@link Neo4jTemplate}
	 */
	public Neo4jTemplate getTemplate();
	
	/**
	 * Create a node for the tree or {@link BaseNode} out of the
	 * class {@link Node}.
	 * 
	 * @param n the node
	 * @return the tree node or {@link BaseNode}
	 */
	public BaseNode createTreeNode(Node n);

	/**
	 * Create a node for the tree or {@link BaseNode} out of the
	 * class {@link Node}.
	 * 
	 * @param n the node
	 * @param clazz the type of the node to which it should be casted
	 * @return the tree node or {@link BaseNode}
	 */
	public BaseNode createTreeNode(Node n, Class<? extends BaseNode> clazz);


	/**
	 * Get all paths from BigBangNode to a specified node.
	 * 
	 * @param nodeId ID of target node. 
	 * @param onlyValid If set to <code>true</code> all nodes on path have to be valid.
	 * @param max Maximum number of returned paths.
	 * @return {@link List} of all found shortest paths.
	 */
	public List<SimplePathResult> getAllPaths(Long nodeId, boolean onlyValid, Long max);
	
	/**
	 * Get all ingoing or outgoing relations and nodes that are related
	 * to the node that has the provided id.
	 * 
	 * @param nodeId the id of the node
	 * @return a {@link ArrayList} of related relations and node
	 */
	public ArrayList<RelationResult> getAllRelatedInOut(Long nodeId);

	/**
	 * Create from a {@link Relationship} an object of the type {@link BaseRel}.
	 * 
	 * @param n the {@link Relationship}
	 * @return the casted class {@link BaseRel}
	 */
	public BaseRel<?,?> createTreeRel(Relationship n);
	
	/**
	 * Checks if the provided node is a material.
	 * 
	 * @param nodeId the id of the node
	 * @return <code>true</code> if material <code>false</code> otherwise
	 */
	public boolean isMaterial(Long nodeId);

	/**
	 * Update the relations from a node that has the provided id.
	 * 
	 * @param nodeId the id of the node from which the relations should be updated
	 * @param relationsArray the array of all the relations the node should have
	 * @return the result of all the updated relations from the node
	 */
	@Secured({"ROLE_EDIT", "ROLE_ADMIN"})
	public <FROM extends NodeIdentifyable, TO extends NodeIdentifyable> 
		UpdateRelationsResult<FROM, TO> updateRelations(Long nodeId, Relations [] relationsArray);

	/**
	 * Find a node by its unique freebim-Id.
	 * 
	 * @param freebimId the unique freebim-ID
	 * @return the found node
	 */
	public UuidIdentifyable findByFreebimId(String freebimId);
	
	
	/**
	 * Find a node by its bsdd-guid.
	 * 
	 * @param bsddGuid the bsdd-guid
	 * @return a {@link ArrayList} of nodes that have the provided bss-guid
	 */
	public ArrayList<BaseNode> findByBsddGuid(String bsddGuid);
	
	
	/**
	 * Find a node by its state.
	 * 
	 * @param searchstring the state.
	 * @return a {@link ArrayList} of nodes that have the provided state
	 */
	public ArrayList<StatedBaseNode> findByState(String searchstring);

	/**
	 * Fully loades the single entity or collection 
	 * thereof which has been loaded lazily (i.e. just with the id but w/o values).
	 * 
	 * @param child the node that will be fully loaded
	 * @return the fully loaded {@link BaseNode}
	 */
	public BaseNode fetch(BaseNode child);

	/**
	 * Get a node by its uuid.
	 * 
	 * @param uuid the uuid
	 * @return the {@link UuidIdentifyable} that has the provided uuid
	 */
	public UuidIdentifyable getByUuid(String uuid);

	/**
	 * Get a node by its id.
	 * 
	 * @param nodeId the id of the node
	 * @return the {@link BaseNode} that has the provided id
	 */
	public BaseNode getNodeById(Long nodeId);
	
	/**
	 * Fully loades a related node. Which one of the relation is loaded is
	 * determined by the direction.
	 * 
	 * @param src the source node
	 * @param rel the relation 
	 * @param dir the direction of the relation
	 * @return the fully loaded {@link BaseNode}
	 */
	public <R extends BaseRel<? extends NodeIdentifyable, ? extends NodeIdentifyable>> 
		BaseNode getRelatedNode(BaseNode src, R rel, Direction dir);
	
	/**
	 * Save the provided {@link HasParameter}.
	 * 
	 * @param rel this relation will be saved
	 * @return the saved object {@link HasParameter}
	 */
	@Secured({"ROLE_EDIT", "ROLE_ADMIN"})
	public HasParameter saveDefaultForParameter(HasParameter rel);
	
	/**
	 * Create or update a {@link BaseRel} with the provided properties.
	 * 
	 * @param rel the relation
	 * @param relProperties the properties of the relation
	 */
	@Secured({"ROLE_EDIT", "ROLE_ADMIN"})
	public <FROM extends NodeIdentifyable, TO extends NodeIdentifyable> 
		void createIfNotExists(BaseRel<FROM, TO> rel, Map<String, Object> relProperties);

}
