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

import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.domain.base.rel.OrderedBaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.json.rel.ContainsParameterDeserializer;
import at.freebim.db.domain.json.rel.ContainsParameterSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between the nodes or classes {@link StatedBaseNode} and {@link Parameter}.
 * This relation extends {@link OrderedBaseRel}.
 * 
 * 
 * @see at.freebim.db.domain.base.StatedBaseNode
 * @see at.freebim.db.domain.Parameter
 * @see at.freebim.db.domain.base.rel.OrderedBaseRel
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RelationshipBackup
@RelationshipEntity (type=RelationType.CONTAINS_PARAMETER)
@JsonSerialize(using = ContainsParameterSerializer.class)
@JsonDeserialize(using = ContainsParameterDeserializer.class)
public class ContainsParameter extends OrderedBaseRel<StatedBaseNode, Parameter> {

	private static final long serialVersionUID = -6053155575923276428L;
	
	/**
	 * Creates a new instance of the relation with the type of the relation. 
	 */
	public ContainsParameter() {
		super(RelationType.CONTAINS_PARAMETER);
	}
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.CONTAINS_PARAMETER.name();
	}	
	
}
