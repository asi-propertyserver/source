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
package at.freebim.db.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.MessageNode;

/**
 * The repository for the node/class {@link MessageNode}.
 * It extends {@link GraphRepository}.
 * 
 * @see at.freebim.db.domain.MessageNode
 * @see org.springframework.data.neo4j.repository.GraphRepository
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface MessageNodeRepository extends GraphRepository<MessageNode> {

	/**
	 * Gets a {@link List} of {@link MessageNode} from a {@link FreebimUser} that has the provided id and are 
	 * greater than the time stamp.
	 * 
	 * @param userId the id of the user
	 * @param timestamp the time stamp
	 * 
	 * @return {@link List} of {@link MessageNode}
	 * */
	@Query("MATCH (msg:MessageNode), (u:FreebimUser)"
			+ " WHERE ID(u)={0}"
			+ " AND ANY (r IN msg.roles WHERE r IN u.roles)"
			+ " AND NOT (msg)-[:MESSAGE_CLOSED]->(u)"
			+ " AND msg.showFrom <= {1} AND msg.showUntil >= {1}"
			+ " AND (msg.validFrom IS NULL OR msg.validFrom <= {1}) AND (msg.validTo IS NULL OR msg.validTo >= {1})"
			+ " RETURN msg")
	public List<MessageNode> getMessagesFor(Long userId, Long timestamp);
	
	
	/**
	 * Gets a {@link List} of {@link MessageNode} from the anonymous user that are 
	 * greater that the provided time stamp.
	 * 
	 * @param timestamp the time stamp
	 * 
	 * @return {@link List} of {@link MessageNode}
	 * 
	 * @see at.freebim.db.domain.base.Role
	 * */
	@Query("MATCH (msg:MessageNode)"
			+ " WHERE \"ROLE_ANONYMOUS\" IN msg.roles"
			+ " AND msg.showFrom <= {0} AND msg.showUntil >= {0}"
			+ " AND (msg.validFrom IS NULL OR msg.validFrom <= {0}) AND (msg.validTo IS NULL OR msg.validTo >= {0})"
			+ " RETURN msg")
	public List<MessageNode> getMessagesForAnonymous(Long timestamp);
}
