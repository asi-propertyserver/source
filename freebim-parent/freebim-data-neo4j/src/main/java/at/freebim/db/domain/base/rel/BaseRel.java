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
package at.freebim.db.domain.base.rel;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.RelationshipType;
import org.springframework.data.neo4j.annotation.StartNode;

import at.freebim.db.domain.base.NodeIdentifyable;

/**
 * Abstract base class for all classes that represent relations in the neo4j database.
 * It implements {@link Serializable}.
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 * @param <FROM> Type that represents the start node of the relation.
 * @param <TO> Type that represents the end node of the relation.
 */
@RelationshipEntity
public abstract class BaseRel<FROM extends NodeIdentifyable, TO extends NodeIdentifyable> implements Serializable {

	private static final long serialVersionUID = -8079350053343203291L;

	/**
	 * The id in the database. Is unique but not consistent. 
	 * For example when nodes get deleted the id can later be reused from a new created node.
	 */
	@GraphId
	@Id
	private Long id;
	
	/**
	 * The node from which the relation starts.
	 */
	@StartNode 
	private FROM n1;
	
	/**
	 * The id of the node from which the relation starts.
	 */
	@Transient
	private Long n1Id;

	/**
	 * The node to which the relation goes.
	 */
	@EndNode
	private TO n2;
	
	
	/**
	 * The id of the node to which the relation goes.
	 */
	@Transient
	private Long n2Id;
	
	
	/**
	 * The type of the relationship.
	 */
	@RelationshipType 
	private String type;
	
	/**
	 * The time stamp of the starting point at which this relation is valid.
	 */
	private Long validFrom;
	
	/**
	 * The time stamp of the ending point to which this relation is valid.
	 */
	private Long validTo;
	
	
	/**
	 * The info to the relation.
	 */
	private String info;

	/**
	 * Set the type of the relation.
	 * 
	 * @param type the type to set
	 */
	public BaseRel(String type) {
		this.type = type;
	}
	
	/**
	 * Get the starting node of the relation.
	 * 
	 * @return the starting node
	 * */
	public FROM getN1() {
		return n1;
	}
	
	/**
	 * Sets the starting node.
	 * 
	 * @param n1 the starting node
	 * */
	public void setN1(FROM n1) {
		this.n1 = n1;
	}
	
	/**
	 * Get the ending point of the relation.
	 * 
	 * @return the end node
	 * */
	public TO getN2() {
		return n2;
	}
	
	
	/**
	 * Sets the end node.
	 * 
	 * @param n2 the end node
	 * */
	public void setN2(TO n2) {
		this.n2 = n2;
	}
	
	/**
	 * Get the id.
	 * 
	 * @return the id
	 * */
	public Long getId() {
		return id;
	}
	
	/**
	 * Set the id.
	 * 
	 * @param id the id
	 * */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get the type of the relation.
	 *  
	 * @return the type
	 */
	public abstract String getType();

	/**
	 * Set the type.
	 * 
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((n1 == null) ? 0 : n1.getNodeId().hashCode());
		result = prime * result + ((n2 == null) ? 0 : n2.getNodeId().hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((validFrom == null) ? 0 : validFrom.hashCode());
		result = prime * result + ((validTo == null) ? 0 : validTo.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		@SuppressWarnings("unchecked")
		BaseRel<FROM, TO> other = (BaseRel<FROM, TO>) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type != other.type)
			return false;
		if (n1 == null) {
			if (other.n1 != null)
				return false;
		} else {
			if (n1.getNodeId() == null) {
				if (other.n1.getNodeId() != null)
					return false;
			} else if (!n1.getNodeId().equals(other.n1.getNodeId()))
				return false;
		}
		if (n2 == null) {
			if (other.n2 != null)
				return false;
		} else {
			if (n2.getNodeId() == null) {
				if (other.n2.getNodeId() != null)
					return false;
			} else if (!n2.getNodeId().equals(other.n2.getNodeId()))
				return false;
		}
		if (validFrom == null) {
			if (other.validFrom != null)
				return false;
		} else {
			if (!validFrom.equals(other.validFrom))
				return false;
		}
		if (validTo == null) {
			if (other.validTo != null)
				return false;
		} else {
			if (!validTo.equals(other.validTo))
				return false;
		}
		return true;
	}

	/**
	 * Get the id of the node from which the relation starts.
	 * 
	 * @return the n1Id
	 */
	public Long getN1Id() {
		return n1Id;
	}

	/**
	 * Get the id of the node to which the relation goes.
	 * 
	 * @return the n2Id
	 */
	public Long getN2Id() {
		return n2Id;
	}

	/**
	 * Set the id of the node from which the relation starts.
	 * 
	 * @param n1Id the id to set
	 */
	public void setN1Id(Long n1Id) {
		this.n1Id = n1Id;
	}

	/**
	 * Set the id of the node to which the relation goes.
	 * 
	 * @param n2Id the Id to set
	 */
	public void setN2Id(Long n2Id) {
		this.n2Id = n2Id;
	}

	/**
	 * Get the time stamp at which the relation is valid.
	 * 
	 * @return the validFrom
	 */
	public Long getValidFrom() {
		return validFrom;
	}

	/**
	 * Get the time stamp to which the relation is valid.
	 * 
	 * @return the validTo
	 */
	public Long getValidTo() {
		return validTo;
	}

	/**
	 * Set the time stamp from which the relation is valid.
	 * 
	 * @param validFrom the validFrom to set
	 */
	public void setValidFrom(Long validFrom) {
		this.validFrom = validFrom;
	}

	/**
	 * Set the time stamp to which the relation is valid.
	 * 
	 * @param validTo the validTo to set
	 */
	public void setValidTo(Long validTo) {
		this.validTo = validTo;
	}

	/**
	 * Get the info.
	 * 
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * Set the info.
	 * 
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	
}
