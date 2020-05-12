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
package at.freebim.db.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.base.Role;
import at.freebim.db.domain.base.RoleContributor;
import at.freebim.db.domain.base.State;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.service.ContributorService;
import at.freebim.db.service.FreebimUserService;
import at.freebim.db.service.StatedBaseNodeService;

/**
 * This service defines the basics for all services that define functionality
 * for a class <b>T</b> that extends {@link StatedBaseNode}. This service
 * extends {@link ContributedBaseNodeServiceImpl} and implements
 * {@link StatedBaseNodeService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.StatedBaseNode
 * @see at.freebim.db.service.impl.ContributedBaseNodeServiceImpl
 * @see at.freebim.db.service.StatedBaseNodeService
 */
public abstract class StatedBaseNodeServiceImpl<T extends StatedBaseNode> extends ContributedBaseNodeServiceImpl<T>
		implements StatedBaseNodeService<T> {

	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(StatedBaseNodeServiceImpl.class);

	/**
	 * The service that handles the {@link FreebimUser}.
	 */
	@Autowired
	private FreebimUserService freebimUserService;

	/**
	 * The service that handles {@link Contributor}s.
	 */
	@Autowired
	private ContributorService contributorService;

	@Autowired
	private Session session;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.BaseNodeServiceImpl#filterBeforeSave(at.freebim.db
	 * .domain.base.BaseNode)
	 */
	@Override
	public T filterBeforeSave(T node) {
		if (node != null) {
			T origNode = null;
			if (node.getNodeId() != null) {
				// get the unmodified node
				origNode = this.getByNodeId(node.getNodeId());
			}
			boolean needsCheck, ok = false;
			if (origNode == null) {
				// node hasn't been saved yet,
				// permission required if new state should be something other than UNDEFINED
				needsCheck = (!State.UNDEFINED.equals(node.getState()));
			} else {
				// permission required if new state differs from old state
				needsCheck = (!origNode.getState().equals(node.getState()));
			}

			if (needsCheck) {
				// permission required
				FreebimUser user = null;
				Contributor c = null;
				// test whether current contributor is allowed to set State of node
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if (auth != null) {
					logger.debug("auth.name=[{}]", auth.getName());

					user = this.freebimUserService.get(auth.getName());
					if (user != null && user.getContributor() != null) {
						c = this.contributorService.getByNodeId(user.getContributor().getNodeId());
						logger.debug("node [{}] linked to contributor [{}].", node.getNodeId(),
								user.getContributor().getNodeId());

						if (node.getState().equals(State.RELEASED)
								|| (origNode != null && origNode.getState().equals(State.RELEASED))) {
							// new state differs from old state and
							// RELEASE state should be changed
							if (this.contributorService.test(c,
									new RoleContributor[] { RoleContributor.ROLE_SET_RELEASE_STATUS })) {
								ok = true;
							}
						} else {
							// new state differs from old state but it's not a RELEASE state
							if (this.contributorService.test(c,
									new RoleContributor[] { RoleContributor.ROLE_SET_STATUS })
									|| this.contributorService.test(c,
											new RoleContributor[] { RoleContributor.ROLE_SET_RELEASE_STATUS })) {
								ok = true;
							}
						}

					}
				}
				if (!ok) {
					logger.warn("BadCredentials user=[{}], contributor=[{}].",
							((user == null) ? "null" : user.getUsername()), ((c == null) ? "null" : c.getCode()));
					throw new BadCredentialsException("Fehlende Berechtigung.");
				}
			}
		}
		return super.filterBeforeSave(node);
	}

	@Override
	public T filterResponse(T node, Long now) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		// permission required
		FreebimUser user = null;
		if (auth != null) {
			logger.debug("auth.name=[{}]", auth.getName());
			user = this.freebimUserService.get(auth.getName());

			if (user != null) {
				if (user.getRoles().contains(Role.ROLE_GUEST) 
						&& node instanceof StatedBaseNode
						&& !(node instanceof BigBangNode)
						&& !(node instanceof Library)) {
					switch (node.getState()) {
					case CHECKED:
					case IMPORTED:
					case RELEASED:
					case UNDEFINED:
						break;
					case REJECTED:
					case TODELETE:
					default:
						return super.filterResponse(null, now);
					}
				}
			}

		}

		return super.filterResponse(node, now);
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<Long> getAllRelevantNodeIds() {
		ArrayList<Long> res = new ArrayList<>();
		StringBuilder b = new StringBuilder();

		final Long now = this.dateService.getMillis();
		Map<String, Object> params = new HashMap<>();
		params.put("now", now);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		boolean permissionRequired = false;
		// permission required
		FreebimUser user = null;
		if (auth != null) {
			logger.debug("auth.name=[{}]", auth.getName());
			user = this.freebimUserService.get(auth.getName());
			if (user != null) {
				if (user.getRoles().contains(Role.ROLE_GUEST)) {
					permissionRequired = true;
				}
			}
		}
		if (permissionRequired) {
			getRelevantQuery(b, "AND (y.state IN ['CHECKED', 'IMPORTED', 'RELEASED', 'UNDEFINED'] or ('BigBangNode' in labels(y) or 'Library' in labels(y))) RETURN DISTINCT ID(y) AS id");
		} else {
			getRelevantQuery(b, " RETURN DISTINCT ID(y) AS id");
		}

		String query = b.toString();

		Iterable<Map<String, Object>> result = this.session.query(query, params, true);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			Long nodeId = (Long) map.get("id");
			res.add(nodeId);
			logger.debug("    found node {} ", nodeId);
		}
		if (logger.isInfoEnabled()) {
			logger.info("\tfound {} relevant node id's for class " + this.getClass().getSimpleName() + " in {} ms.",
					res.size(), this.dateService.getMillis() - now);
		}
		return res;
	}

}
