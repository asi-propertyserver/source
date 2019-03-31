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

import at.freebim.db.domain.ParameterSet;
import at.freebim.db.domain.base.HierarchicalBaseNode;
import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.json.rel.HasParameterSetDeserializer;
import at.freebim.db.domain.json.rel.HasParameterSetSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between the nodes or classes {@link HierarchicalBaseNode} and {@link ParameterSet}.
 * It denotes the owner ({@link HierarchicalBaseNode}) of a {@link ParameterSet}.
 * This relation extends {@link BaseRel}.
 * 
 * @see at.freebim.db.domain.base.HierarchicalBaseNode
 * @see at.freebim.db.domain.ParameterSet
 * @see at.freebim.db.domain.base.rel.BaseRel
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RelationshipBackup
@RelationshipEntity (type=RelationType.HAS_PARAMETER_SET)
@JsonSerialize(using = HasParameterSetSerializer.class)
@JsonDeserialize(using = HasParameterSetDeserializer.class)
public class HasParameterSet extends BaseRel<HierarchicalBaseNode, ParameterSet> {

	private static final long serialVersionUID = -1363333941128941159L;

	/**
	 * Creates a new instance of the relation with the type of the relation. 
	 */
	public HasParameterSet() {
		super(RelationType.HAS_PARAMETER_SET);
	}
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.HAS_PARAMETER_SET.name();
	}

}
