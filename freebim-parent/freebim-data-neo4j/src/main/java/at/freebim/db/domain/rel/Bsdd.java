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

import at.freebim.db.domain.BsddNode;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.json.rel.BsddDeserializer;
import at.freebim.db.domain.json.rel.BsddSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relations between the classes or nodes {@link BsddNode} and {@link UuidIdentifyable}.
 * It extends {@link BaseRel}.
 * 
 * @see at.freebim.db.domain.BsddNode
 * @see at.freebim.db.domain.base.UuidIdentifyable
 * @see at.freebim.db.domain.base.rel.BaseRel
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RelationshipBackup
@RelationshipEntity(type=RelationType.BSDD)
@JsonSerialize(using = BsddSerializer.class)
@JsonDeserialize(using = BsddDeserializer.class)
public class Bsdd extends BaseRel<BsddNode, UuidIdentifyable> {

	private static final long serialVersionUID = -3865793334835198307L;

	/**
	 * Creates a new instance of the relation with the type of the relation. 
	 */
	public Bsdd() {
		super(RelationType.BSDD);
	}


	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.BSDD.name();
	}
}
