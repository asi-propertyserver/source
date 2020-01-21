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

import org.neo4j.ogm.annotation.Relationship;

import at.freebim.db.domain.ParameterSet;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.rel.HasParameterSet;
import at.freebim.db.domain.rel.ParentOf;

/**
 * This abstract class is the base for all nodes that represent a hierarchy. It
 * extends {@link Parameterized} and implements {@link Orderable}. To achieve to
 * hierarchy the node references parents and childs.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.Parameterized
 * @see at.freebim.db.domain.base.Orderable
 * @see at.freebim.db.domain.rel.ParentOf
 */
@SuppressWarnings("serial")
public abstract class HierarchicalBaseNode extends Parameterized implements Orderable {

	/**
	 * The level.
	 */
	private String level;

	/**
	 * The relations to other nodes of the same type that are considered parents in
	 * the hierarchy.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.PARENT_OF, direction = Relationship.INCOMING)
	private Iterable<ParentOf> parents;

	/**
	 * The relations to other nodes of the same type that are considered childs in
	 * the hierarchy.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.PARENT_OF, direction = Relationship.OUTGOING)
	private Iterable<ParentOf> childs;

	/**
	 * The relations to {@link ParameterSet}s.
	 *
	 * @see org.neo4j.ogm.annotation.Relationship
	 */
	@Relationship(type = RelationType.HAS_PARAMETER_SET, direction = Relationship.OUTGOING)
	private Iterable<HasParameterSet> parameterSets;

	/**
	 * Creates a new instance.
	 */
	protected HierarchicalBaseNode() {
		super();
	}

	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * Sets the level.
	 *
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj) || getClass() != obj.getClass())
			return false;

		HierarchicalBaseNode other = (HierarchicalBaseNode) obj;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (parents == null) {
			if (other.parents != null)
				return false;
		} else if (!parents.equals(other.parents))
			return false;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.StatedBaseNode#equalsData(java.lang.Object)
	 */
	@Override
	public boolean equalsData(Object obj) {
		if (this == obj)
			return true;
		if (!super.equalsData(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;

		return true;
	}

	/**
	 * Gets the relations to other nodes of the same type that are considered
	 * parents in the hierarchy.
	 *
	 * @return the relations to the parents
	 */
	public Iterable<ParentOf> getParents() {
		return this.parents;
	}

	/**
	 * Gets the relations to other nodes of the same type that are considered childs
	 * in the hierarchy.
	 *
	 * @return the relations to the child's
	 */
	public Iterable<ParentOf> getChilds() {
		return this.childs;
	}

	/**
	 * Get the relations to {@link ParameterSet}s.
	 *
	 * @return the relation to the {@link ParameterSet}s
	 */
	public Iterable<HasParameterSet> getParameterSets() {
		return this.parameterSets;
	}

}
