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
package at.freebim.db.webapp.session;

import java.util.List;

/**
 * This interface is used to track all sessions.
 * 
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface SessionTracker {

	public static final String SESSION_INFO = "SESSION_INFO";

	public enum SessionAction {
		RELATION_MODIFIED, SAVED, INSERTED, DELETED
	};

	/**
	 * Add info to a {@link SessionObject}. That is part of the specified session.
	 * 
	 * @param info               the info that should be added to the
	 *                           {@link SessionObject}
	 * @param sessionIdInitiator the id of the session
	 */
	public void addInfo(NodeInfo info, String sessionIdInitiator);

	/**
	 * Get the session object that has the specified id.
	 * 
	 * @param sessionId the id of the session
	 * @return the {@link SessionObject} that has the specified session id
	 */
	public SessionObject getSessionObject(String sessionId);

	/**
	 * Get the username of all logged in users.
	 * 
	 * @return the {@link List} of usernames
	 */
	public List<String> getLoggedInUsernames();

	/**
	 * Get all the session id of the session of a user that has the specified user
	 * name.
	 * 
	 * @param username the user name
	 * @return the {@link List} of session id's
	 */
	public List<String> getSessionsForUsername(String username);

	/**
	 * Get the time stamp of the last request from the session that has the provided
	 * session id.
	 * 
	 * @param sessionId the id of the session
	 * @return the time stamp of the last request
	 */
	public Long getLastRequest(String sessionId);

}