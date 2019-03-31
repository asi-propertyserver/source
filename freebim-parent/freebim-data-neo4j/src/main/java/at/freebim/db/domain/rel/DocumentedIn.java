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

import at.freebim.db.domain.Document;
import at.freebim.db.domain.base.ContributedBaseNode;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.base.rel.TimestampedBaseRel;
import at.freebim.db.domain.json.rel.DocumentedInDeserializer;
import at.freebim.db.domain.json.rel.DocumentedInSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between the nodes or classes {@link ContributedBaseNode} and {@link Document}.
 * It denotes that a node {@link ContributedBaseNode} has been documented in the node {@link Document}.
 * This relation extends {@link TimestampedBaseRel}.
 * 
 * @see at.freebim.db.domain.base.ContributedBaseNode
 * @see at.freebim.db.domain.Document
 * @see at.freebim.db.domain.base.rel.TimestampedBaseRel
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RelationshipBackup
@RelationshipEntity (type=RelationType.DOCUMENTED_IN)
@JsonSerialize(using = DocumentedInSerializer.class)
@JsonDeserialize(using = DocumentedInDeserializer.class)
public class DocumentedIn extends TimestampedBaseRel<ContributedBaseNode, Document> {

	private static final long serialVersionUID = -8383972250953827991L;

	/**
	 * Creates a new instance of the relation with the type of the relation. 
	 */
	public DocumentedIn() {
		super(RelationType.DOCUMENTED_IN);
	}
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.DOCUMENTED_IN.name();
	}

}
