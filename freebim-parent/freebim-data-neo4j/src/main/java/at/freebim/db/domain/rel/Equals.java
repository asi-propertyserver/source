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

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.data.neo4j.annotation.RelationshipEntity;

import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.rel.QualifiedBaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.json.rel.EqualsDeserializer;
import at.freebim.db.domain.json.rel.EqualsSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between two nodes or classes {@link BaseNode}.
 * It denotes if two nodes {@link BaseNode} are equals.
 * This relation extends {@link QualifiedBaseRel}.
 * 
 * @see at.freebim.db.domain.base.BaseNode
 * @see at.freebim.db.domain.base.rel.QualifiedBaseRel
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RelationshipBackup
@RelationshipEntity (type=RelationType.EQUALS)
@JsonSerialize(using = EqualsSerializer.class)
@JsonDeserialize(using = EqualsDeserializer.class)
public class Equals extends QualifiedBaseRel<BaseNode, BaseNode> {

	private static final long serialVersionUID = -7773746983619468400L;
	
	/**
	 * Is this relation valid.
	 */
	private boolean valid;
	
	/**
	 * The name of the starting class of the relation.
	 */
	private String fromClass;
	
	
	/**
	 * The name of the end class of the relation.
	 */
	private String toClass;
	
	/**
	 * Creates a new instance of the relation with the type of the relation. 
	 */
	public Equals() {
		super(RelationType.EQUALS);
		this.valid = true;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.EQUALS.name();
	}

	/**
	 * Checks if the relation is valid.
	 * 
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Set if the relation is valid.
	 * 
	 * @param valid the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * Get the name of the starting class of the relation.
	 * 
	 * @return the fromClass
	 */
	public String getFromClass() {
		return fromClass;
	}

	/**
	 * Get the name of the end class of the relation.
	 * 
	 * @return the toClass
	 */
	public String getToClass() {
		return toClass;
	}

	/**
	 * Set the name of the starting class of the relation.
	 * 
	 * @param fromClass the fromClass to set
	 */
	public void setFromClass(String fromClass) {
		this.fromClass = fromClass;
	}

	/**
	 * Set the name of the end class of the relation.
	 * 
	 * @param toClass the toClass to set
	 */
	public void setToClass(String toClass) {
		this.toClass = toClass;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((fromClass == null) ? 0 : fromClass.hashCode());
		result = prime * result + ((toClass == null) ? 0 : toClass.hashCode());
		result = prime * result + (valid ? 1231 : 1237);
		return result;
	}

	/* (non-Javadoc)
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
		Equals other = (Equals) obj;
		if (fromClass == null) {
			if (other.fromClass != null)
				return false;
		} else if (!fromClass.equals(other.fromClass))
			return false;
		if (toClass == null) {
			if (other.toClass != null)
				return false;
		} else if (!toClass.equals(other.toClass))
			return false;
		if (valid != other.valid)
			return false;
		return true;
	}
	
	
}
