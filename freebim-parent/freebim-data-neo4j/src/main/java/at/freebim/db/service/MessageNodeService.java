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
package at.freebim.db.service;

import java.util.List;

import at.freebim.db.domain.MessageNode;

/**
 * The service for the node/class {@link MessageNode}. This service extends
 * {@link LifetimeBaseNodeService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.MessageNode
 * @see at.freebim.db.service.LifetimeBaseNodeService
 */
public interface MessageNodeService extends LifetimeBaseNodeService<MessageNode> {

	/**
	 * Get all current messages.
	 *
	 * @return a {@link List} of the current {@link MessageNode}s
	 */
	List<MessageNode> getCurrentMessages();

	/**
	 * Set a {@link MessageNode} to seen for the current logged in user.
	 *
	 * @param messageId the id of the {@link MessageNode}
	 */
	void setSeen(Long messageId);

	/**
	 * Set a {@link MessageNode} to closed for the current logged in user.
	 *
	 * @param messageId the id of the {@link MessageNode}
	 */
	void setClosed(Long messageId);

}
