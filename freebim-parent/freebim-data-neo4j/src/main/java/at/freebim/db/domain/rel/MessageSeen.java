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

import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.MessageNode;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.base.rel.TimestampedBaseRel;
import at.freebim.db.domain.json.rel.MessageSeenDeserializer;
import at.freebim.db.domain.json.rel.MessageSeenSerializer;

/**
 * The node to node relation between the nodes or classes {@link MessageNode} and {@link FreebimUser}.
 * It denotes that a message ({@link MessageNode}) has been seen by a user ({@link FreebimUser}).
 * This relation extends {@link TimestampedBaseRel}.
 * 
 * @see at.freebim.db.domain.MessageNode
 * @see at.freebim.db.domain.FreebimUser
 * @see at.freebim.db.domain.base.rel.TimestampedBaseRel
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RelationshipEntity (type=RelationType.MESSAGE_SEEN)
@JsonSerialize(using = MessageSeenSerializer.class)
@JsonDeserialize(using = MessageSeenDeserializer.class)
public class MessageSeen extends TimestampedBaseRel<MessageNode, FreebimUser> {

	private static final long serialVersionUID = 8233615436789129496L;

	/**
	 * Creates a new instance of the relation with the type of the relation. 
	 */
	public MessageSeen() {
		super(RelationType.MESSAGE_SEEN);
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.MESSAGE_SEEN.name();
	}

}
