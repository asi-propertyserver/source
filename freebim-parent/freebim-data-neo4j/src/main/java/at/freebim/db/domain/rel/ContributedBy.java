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

import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.base.ContributedBaseNode;
import at.freebim.db.domain.base.ContributionType;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.base.rel.TimestampedBaseRel;
import at.freebim.db.domain.json.rel.ContributedByDeserializer;
import at.freebim.db.domain.json.rel.ContributedBySerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between the nodes or classes {@link ContributedBaseNode} and {@link Contributor}.
 * It denotes that a node {@link ContributedBaseNode} has been contributed by the node {@link Contributor}.
 * This relation extends {@link TimestampedBaseRel}.
 * 
 * @see at.freebim.db.domain.base.ContributedBaseNode
 * @see at.freebim.db.domain.Contributor
 * @see at.freebim.db.domain.base.rel.TimestampedBaseRel
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RelationshipBackup
@RelationshipEntity (type=RelationType.CONTRIBUTED_BY)
@JsonSerialize(using = ContributedBySerializer.class)
@JsonDeserialize(using = ContributedByDeserializer.class)
public class ContributedBy extends TimestampedBaseRel<ContributedBaseNode, Contributor> {

	private static final long serialVersionUID = -5724667008711661547L;
	
	/**
	 * Creates a new instance of the relation with the type of the relation 
	 * and the type of the contribution.
	 */
	public ContributedBy() {
		super(RelationType.CONTRIBUTED_BY);
		this.contributionType = ContributionType.MODIFY;
	}
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.CONTRIBUTED_BY.name();
	}
	
	/**
	 * The typ of the contribution.
	 */
	private ContributionType contributionType;

	/**
	 * @return the contributionType
	 */
	public ContributionType getContributionType() {
		return contributionType;
	}
	/**
	 * @param contributionType the contributionType to set
	 */
	public void setContributionType(ContributionType contributionType) {
		this.contributionType = contributionType;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((contributionType == null) ? 0 : contributionType.hashCode());
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
		ContributedBy other = (ContributedBy) obj;
		if (contributionType != other.contributionType)
			return false;
		return true;
	}
}
