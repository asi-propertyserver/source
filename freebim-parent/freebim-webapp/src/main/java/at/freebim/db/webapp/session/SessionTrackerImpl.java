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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import at.freebim.db.service.DateService;

/**
 * This class tracks all sessions.
 * It implements {@link SessionTracker}.
 * 
 * @see at.freebim.db.webapp.session.SessionTracker
 * 
 * @author rainer.breuss@uibk.ac.at
 * 
 */
@Service
@Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SessionTrackerImpl implements SessionTracker {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SessionTrackerImpl.class);

	/**
	 * Maintains a registry of {@link SessionInformation} instances.
	 */
	@Autowired
	private SessionRegistry sessionRegistry;

	/**
	 * A hashmap of {@link SessionObject}.
	 */
	private HashMap<String, SessionObject> sessions;

	/**
	 * Initialize.
	 */
	@PostConstruct
	public void init() {
		this.sessions = new HashMap<String, SessionObject>();
		logger.info("constructed.");
	}
	
	/**
	 * Make cleanup.
	 */
	@PreDestroy
	public void cleanup() {
		synchronized (this.sessions) {
			this.sessions.clear();
		}
		logger.info("cleared.");
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.webapp.session.SessionTracker#getLoggedInUsernames()
	 */
	public List<String> getLoggedInUsernames() {
		List<String> res = new ArrayList<String>();
		if (this.sessionRegistry != null) {
			List<Object> principals = this.sessionRegistry.getAllPrincipals();
			for (Object p : principals) {
				String username = (String) p;
				if (!res.contains(username))
					res.add(username);
				logger.debug("\t currently logged in: {}", username);
			}
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.webapp.session.SessionTracker#getLastRequest(java.lang.String)
	 */
	public Long getLastRequest(String sessionId) {
		SessionInformation si = this.sessionRegistry.getSessionInformation(sessionId);
		if (si != null) {
			return si.getLastRequest().getTime() - DateService.FREEBIM_DELTA;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.webapp.session.SessionTracker#getSessionsForUsername(java.lang.String)
	 */
	public List<String> getSessionsForUsername(String username) {
		List<String> res = new ArrayList<String>();
		if (username != null) {
			List<SessionInformation> sessionInfoList = this.sessionRegistry.getAllSessions(username, false);
			if (sessionInfoList != null) {
				for (SessionInformation si : sessionInfoList) {
					if (!si.isExpired() && !res.contains(si.getSessionId()))
						res.add(si.getSessionId());
					logger.debug("\t {}: sessionId: {}", username, si.getSessionId());
				}
			}
		}
		return res;
	}

	/**
	 * Get a {@link List} of all the current session ids for all users.
	 * 
	 * @return teh {@link List} of session ids
	 */
	private List<String> getAllSessionIds() {
		List<String> res = new ArrayList<String>();
		if (this.sessionRegistry != null) {
			List<Object> principals = this.sessionRegistry.getAllPrincipals();
			for (Object p : principals) {
				List<SessionInformation> sessionInfoList = this.sessionRegistry.getAllSessions(p, false);
				if (sessionInfoList != null) {
					for (SessionInformation si : sessionInfoList) {
						if (!si.isExpired() && !res.contains(si.getSessionId()))
							res.add(si.getSessionId());
						logger.debug("\t {}: sessionId: {}", p, si.getSessionId());
					}
				}
			}
		}
		List<String> keysToRemove = new ArrayList<String>();
		Set<String> keys = null;
		synchronized (this.sessions) {
			keys = this.sessions.keySet();
		}
		for (String sessionId : keys) {
			if (!res.contains(sessionId)) {
				keysToRemove.add(sessionId);
			}
		}
		for (String sessionId : keysToRemove) {
			synchronized (this.sessions) {
				this.sessions.remove(sessionId);
			}
			logger.debug("Session {} removed.", sessionId);
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.webapp.SessionListener#addInfo(at.freebim.db.webapp.SessionTrackerImpl.SessionInfo, java.lang.String)
	 */
	@Override
	public void addInfo(NodeInfo info, String sessionIdInitiator) {
		
		final List<String> allSessionIds = getAllSessionIds();
		logger.info(sessionIdInitiator + " sent a message: {}, total sessions: {}", info, allSessionIds.size());
		for (String sessionId : allSessionIds) {

			if (sessionId.equals(sessionIdInitiator))
				continue;
			
			SessionObject so = this.getSessionObject(sessionId);
			
			if (so == null) {
				so = new SessionObject();
				synchronized (this.sessions) {
					this.sessions.put(sessionId, so);
				}
			}
			so.addInfo(info);
			logger.info("    set to session: {}", sessionId);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.webapp.session.SessionTracker#getSessionObject(java.lang.String)
	 */
	@Override
	public SessionObject getSessionObject(String sessionId) {
		SessionObject so = null;
		synchronized (this.sessions) {
			so = this.sessions.get(sessionId);
		}
		return so;
	}
}
