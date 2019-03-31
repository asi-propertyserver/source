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

import org.springframework.data.neo4j.annotation.RelationshipEntity;

import at.freebim.db.domain.NgramNode;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.rel.NgramRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * 
 *  The node to node relation between nodes or classes {@link Named}.
 *  It denotes that a {@link NgramNode} is connected to a node that extends {@link Named}.
 *  This relation extends {@link NgramRel}.
 *  
 *  @see at.freebim.db.domain.base.Named
 *  @see at.freebim.db.domain.base.rel.NgramRel
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RelationshipBackup
@RelationshipEntity (type=RelationType.NGRAM_NAME_OF)
public class NgramNameOf extends NgramRel<Named> {

	private static final long serialVersionUID = -1760962231404416057L;

	/**
	 * Creates a new instance of the relation with the type of the relation. 
	 */
	public NgramNameOf() {
		super(RelationType.NGRAM_NAME_OF);
	}
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.NGRAM_NAME_OF.name();
	}

}
