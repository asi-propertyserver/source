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
package at.freebim.db.domain.rel;

import org.neo4j.ogm.annotation.RelationshipEntity;

import at.freebim.db.domain.base.HierarchicalBaseNode;
import at.freebim.db.domain.base.rel.BsddRelation;
import at.freebim.db.domain.base.rel.OrderedBaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between two nodes or classes of the type
 * {@link HierarchicalBaseNode}. It denotes that one node is child of another
 * node. This creates a hierarchy. This relation extends {@link OrderedBaseRel}.
 * and implements {@link BsddRelation}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.RelationshipEntity
 * @see at.freebim.db.domain.base.HierarchicalBaseNode
 * @see at.freebim.db.domain.base.rel.OrderedBaseRel
 * @see at.freebim.db.domain.base.rel.BsddRelation
 * @deprecated Parent-Child-Relations are established via {@link ParentOf}
 *             relations now.
 */
@RelationshipBackup
@RelationshipEntity(type = RelationType.CHILD_OF)
public class ChildOf extends OrderedBaseRel<HierarchicalBaseNode, HierarchicalBaseNode> implements BsddRelation {

	private static final long serialVersionUID = 4682531219800977662L;

	/**
	 * The bsdd-guid.
	 *
	 * @see at.freebim.db.domain.base.rel.BsddRelation
	 */
	private String bsddGuid;

	/**
	 * Creates a new instance of the relation with the type of the relation.
	 */
	public ChildOf() {
		super(RelationType.CHILD_OF);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.CHILD_OF.name();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.rel.BsddRelation#getBsddGuid()
	 */
	@Override
	public String getBsddGuid() {
		return this.bsddGuid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.rel.BsddRelation#setBsddGuid(java.lang.String)
	 */
	@Override
	public void setBsddGuid(String guid) {
		this.bsddGuid = guid;
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
		result = prime * result + ((bsddGuid == null) ? 0 : bsddGuid.hashCode());
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
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChildOf other = (ChildOf) obj;
		if (bsddGuid == null) {
			if (other.bsddGuid != null)
				return false;
		} else if (!bsddGuid.equals(other.bsddGuid) && !(other.bsddGuid == null && bsddGuid.equals("")))
			return false;
		return true;
	}

}
