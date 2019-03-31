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

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.base.ContributedBaseNode;
import at.freebim.db.domain.base.ContributionType;
import at.freebim.db.domain.base.Role;
import at.freebim.db.domain.base.RoleContributor;
import at.freebim.db.domain.rel.ContributedBy;
import at.freebim.db.service.ContributedBaseNodeService;
import at.freebim.db.service.ContributorService;
import at.freebim.db.service.DateService;
import at.freebim.db.service.FreebimUserService;
import at.freebim.db.service.RelationService;

/**
 * This service defines the basics for all services that 
 * define functionality for a class <b>T</b> that extends {@link ContributedBaseNode}.
 * This service extends {@link UuidIdentifyableServiceImpl} and implements {@link ContributedBaseNodeService}.
 * 
 * @see at.freebim.db.domain.base.ContributedBaseNode
 * @see at.freebim.db.service.ContributedBaseNodeService
 * @see at.freebim.db.service.impl.UuidIdentifyableServiceImpl
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public abstract class ContributedBaseNodeServiceImpl<T extends ContributedBaseNode> extends
		UuidIdentifyableServiceImpl<T> implements ContributedBaseNodeService<T> {

	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(ContributedBaseNodeServiceImpl.class);
	
	/**
	 * The service that handles the node/class {@link FreebimUser}.
	 */
	@Autowired
	private FreebimUserService freebimUserService;
	
	/**
	 * The service that handles the node/class {@link Contributor}.
	 */
	@Autowired
	private ContributorService contributorService;
	
	/**
	 * The service that handles relations.
	 */
	@Autowired
	private RelationService relationService;
	
	/**
	 * The service that handles dates.
	 */
	@Autowired
	protected DateService dateService;
	
	/**
	 * A contributor
	 */
	private Contributor freebimContributor; 


	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.BaseNodeServiceImpl#filterAfterSave(at.freebim.db.domain.base.BaseNode)
	 */
	@Override
	@Transactional
	public T filterAfterSave(T node) {
		this.addContributedByRelation(node, ContributionType.MODIFY);
		return super.filterAfterSave(node);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.BaseNodeServiceImpl#filterAfterDelete(at.freebim.db.domain.base.BaseNode)
	 */
	@Override
	@Transactional
	public T filterAfterDelete(T node) {
		this.addContributedByRelation(node, ContributionType.DELETE);
		return super.filterAfterDelete(node);
	}
	
	/**
	 * Gets the current {@link Contributor}. This means that the {@link Contributor} for the 
	 * currently logged in user will be loaded. Returns <code>null</code> if no one is logged in.
	 * 
	 * @return the currently logged in user/{@link Contributor} or <code>null</code> when no one is logged in
	 */
	@Transactional(readOnly=true)
	private Contributor getCurrentContributor() {
		Contributor c = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			logger.debug("auth.name=[{}]", auth.getName());
			
			FreebimUser user = this.freebimUserService.get(auth.getName());
			if (user != null && user.getContributor() != null) {
				c = this.contributorService.getByNodeId(user.getContributor().getNodeId());
				if (c != null) {
					logger.debug("current contributor: [{}], nodeId=[{}]", c.getName(), c.getNodeId());
				}
				return c;
			}
		}
		return null;
	}

	/**
	 * Adds the relation contributed by to a node <code>T</code> and the current {@link Contributor}.
	 * The type of relation is given by the second parameter.
	 * 
	 * @param node the node to which the contributed by relation will be added
	 * @param ct the type of contribution
	 */
	@Transactional
	private void addContributedByRelation(T node, ContributionType ct) {
		if (node != null) {
			// set currently logged in contributor
			Contributor c = getCurrentContributor();
			if (c == null) {
				// no Contributor!
				// probably an ADMIN task ...
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if (auth != null) {
					logger.debug("auth.name=[{}]", auth.getName());
					if (auth.getAuthorities().contains(Role.ROLE_ADMIN)) {
						if (this.freebimContributor == null) {
							this.freebimContributor = this.contributorService.getByCode(ContributorService.FREEBIM_CONTRIBUTOR_CODE);
						}
						c = this.freebimContributor;
					}
				}
			}
			if (c != null) {
				ContributedBy rel = new ContributedBy();
				rel.setN1(node);
				rel.setN2(c);
				rel.setContributionType(ct);
				rel.setContributionType(node.getValidTo() != null ? ContributionType.DELETE : ct);
				rel.setTs(this.dateService.getMillis());
				rel = (ContributedBy) this.relationService.save(rel);
				logger.debug("CONTRIBUTED_BY relation saved, nodeId={}", rel.getId());
			} else {
				logger.warn("Can't link node nodeId=[{}] to contributor!", node.getNodeId());
			}
		}
	}


	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.BaseNodeServiceImpl#filterAfterInsert(at.freebim.db.domain.base.BaseNode)
	 */
	@Override
	public T filterAfterInsert(T node) {
		this.addContributedByRelation(node, ContributionType.CREATE);
		return super.filterAfterInsert(node);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.BaseNodeServiceImpl#filterBeforeDelete(at.freebim.db.domain.base.BaseNode)
	 */
	@Override
	@Transactional(readOnly=true)
	public T filterBeforeDelete(T node) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			logger.debug("auth.name=[{}]", auth.getName());
			if (auth.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return super.filterBeforeDelete(node);
			}
		}
		Contributor c = getCurrentContributor();
		if (c != null) {
			boolean mayDelete = this.contributorService.test(c, new RoleContributor[] {RoleContributor.ROLE_DELETE});
			if (mayDelete) {
				return super.filterBeforeDelete(node);
			}
			Iterable<ContributedBy> iterable = node.getContributor();
			Iterator<ContributedBy> iter = iterable.iterator();
			while (iter.hasNext()) {
				ContributedBy cb = iter.next();
				if (cb.getContributionType() == ContributionType.CREATE) {
					if (c.getNodeId().equals(cb.getN2().getNodeId())) {
						return super.filterBeforeDelete(node);
					}
				}
			}
		}
		throw new AccessDeniedException("Deletion not allowed.");
	}
}
