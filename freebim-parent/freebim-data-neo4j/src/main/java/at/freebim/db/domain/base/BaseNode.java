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
package at.freebim.db.domain.base;

import java.util.ArrayList;
import java.util.Iterator;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;

import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.rel.Equals;


/** 
 * This abstract class is the base class for nodes.
 * It implements {@link NodeIdentifyable}.
 * 
 * @see at.freebim.db.domain.base.NodeIdentifyable
 * 
 * @author rainer.breuss@uibk.ac.at
 */
@NodeEntity
public abstract class BaseNode implements NodeIdentifyable {

	private static final long serialVersionUID = -5200529970695975807L;

	/**
	 * The id.
	 * 
	 * @see at.freebim.db.domain.base.NodeIdentifyable
	 */
	protected Long nodeId;
	
	/**
	 * Holds the relations to other nodes of the same type that are considered equal.
	 */
	private Iterable<Equals> eq;

	/**
	 * Create new instance.
	 */
	public BaseNode() {
		super();
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.NodeIdentifyable#getNodeId()
	 */
	@GraphId 
	public Long getNodeId() {
		return this.nodeId;
	}

	/**
	 * Sets the unique id of this node. But is not guaranteed to be persistent.
	 * 
	 * @param nodeId the id to set
	 * */
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
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
		BaseNode other = (BaseNode) obj;
		if (nodeId == null) {
			if (other.nodeId != null)
				return false;
		} else if (!nodeId.equals(other.nodeId))
			return false;
		return true;
	}

	/**
	 * Checks if this object is equal to the provided object.
	 * 
	 * @param obj the object to check
	 * @return the result if the objects are equal
	 */
	public boolean equalsData(Object obj) {
		return true; 
	}

	/**
	 * Gets the relations to other nodes of the same type that are considered equal.
	 * 
	 * @return the relation to the equal nodes
	 */
	@RelatedToVia(type=RelationType.EQUALS, direction=Direction.BOTH)
	@Fetch
	public Iterable<Equals> getEq() {
		return eq;
	}
	
	/**
	 * Checks if two {@link Iterable} {@link BaseRel} are the same
	 * Returns true if this is the case.
	 * 
	 * @return the result if the relations are equal
	 */
	protected <T extends NodeIdentifyable, R extends BaseRel<?,T>>
		boolean equalsRelation(final Iterable<R> self, final Iterable<R> other) {
		
		if (self == null || other == null) {
			return false;
		} else {
			final ArrayList<Long> rels = new ArrayList<Long>();
			final Iterator<R> iter = self.iterator();
			while (iter.hasNext()) {
				final R rel = iter.next();
				final Long relatedNodeId = rel.getN2().getNodeId();
				rels.add(relatedNodeId);
			}
			final Iterator<R> otherIter = other.iterator();
			while (otherIter.hasNext()) {
				final R rel = iter.next();
				final Long relatedNodeId = rel.getN2().getNodeId();
				if (!rels.contains(relatedNodeId)) {
					return false;
				}
				rels.remove(relatedNodeId);
			}
			if (rels.size() > 0)
				return false;
		}
		
		return true;
	}
}