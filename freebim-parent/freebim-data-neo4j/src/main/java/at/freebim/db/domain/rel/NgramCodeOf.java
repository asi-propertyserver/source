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

import at.freebim.db.domain.NgramNode;
import at.freebim.db.domain.base.Coded;
import at.freebim.db.domain.base.rel.NgramRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between the nodes or classes {@link Coded}. It
 * denotes that a {@link NgramNode} is connected to a node that extends
 * {@link Coded}. This relation extends {@link NgramRel}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.RelationshipEntity
 * @see at.freebim.db.domain.base.Coded
 * @see at.freebim.db.domain.base.rel.NgramRel
 */
@RelationshipBackup
@RelationshipEntity(type = RelationType.NGRAM_CODE_OF)
public class NgramCodeOf extends NgramRel<Coded> {

	private static final long serialVersionUID = -4483856612223968146L;

	/**
	 * Creates a new instance of the relation with the type of the relation.
	 */
	public NgramCodeOf() {
		super(RelationType.NGRAM_CODE_OF);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.NGRAM_CODE_OF.name();
	}

}
