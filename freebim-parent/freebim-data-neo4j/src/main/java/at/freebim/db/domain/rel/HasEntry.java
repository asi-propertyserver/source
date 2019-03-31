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

import at.freebim.db.domain.ValueList;
import at.freebim.db.domain.ValueListEntry;
import at.freebim.db.domain.base.rel.BsddRelation;
import at.freebim.db.domain.base.rel.OrderedBaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.json.rel.HasEntryDeserializer;
import at.freebim.db.domain.json.rel.HasEntrySerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between the nodes or classes {@link ValueList} and {@link ValueListEntry}.
 * It denotes if a entry in a list ({@link ValueList}) has an entry ({@link ValueListEntry}).
 * This relation extends {@link OrderedBaseRel} and implements {@link BsddRelation}.
 * 
 * @see at.freebim.db.domain.ValueList
 * @see at.freebim.db.domain.ValueListEntry
 * @see at.freebim.db.domain.base.rel.OrderedBaseRel
 * @see at.freebim.db.domain.base.rel.BsddRelation
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RelationshipBackup
@RelationshipEntity (type=RelationType.HAS_ENTRY)
@JsonSerialize(using = HasEntrySerializer.class)
@JsonDeserialize(using = HasEntryDeserializer.class)
public class HasEntry extends OrderedBaseRel<ValueList, ValueListEntry> implements BsddRelation {

	private static final long serialVersionUID = 6549262644908555091L;
	
	/**
	 * The bsdd-guid.
	 * 
	 * @see at.freebim.db.domain.base.rel.BsddRelation
	 */
	private String bsddGuid;

	/**
	 * Creates a new instance of the relation with the type of the relation. 
	 */
	public HasEntry() {
		super(RelationType.HAS_ENTRY);
	}
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.HAS_ENTRY.name();
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BsddRelation#setBsddGuid(java.lang.String)
	 */
	@Override
	public void setBsddGuid(String guid) {
		this.bsddGuid = guid;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BsddRelation#getBsddGuid()
	 */
	@Override
	public String getBsddGuid() {
		return this.bsddGuid;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((bsddGuid == null) ? 0 : bsddGuid.hashCode());
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
		HasEntry other = (HasEntry) obj;
		if (bsddGuid == null) {
			if (other.bsddGuid != null)
				return false;
		} else if (!bsddGuid.equals(other.bsddGuid))
			return false;
		return true;
	}

}
