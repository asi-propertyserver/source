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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.MessageNode;
import at.freebim.db.domain.base.Role;
import at.freebim.db.domain.rel.MessageClosed;
import at.freebim.db.domain.rel.MessageSeen;
import at.freebim.db.repository.MessageNodeRepository;
import at.freebim.db.service.DateService;
import at.freebim.db.service.FreebimUserService;
import at.freebim.db.service.MessageNodeService;
import at.freebim.db.service.RelationService;

/**
 * The service for the node/class {@link MessageNode}. This service extends
 * {@link LifetimeBaseNodeServiceImpl} and implements
 * {@link MessageNodeService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.MessageNode
 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl
 * @see at.freebim.db.service.MessageNodeService
 */
@Service
public class MessageNodeServiceImpl extends LifetimeBaseNodeServiceImpl<MessageNode> implements MessageNodeService {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(MessageNodeServiceImpl.class);

	/**
	 * The service that handles dates.
	 */
	@Autowired
	private DateService dateService;

	/**
	 * The service that handles relations.
	 */
	@Autowired
	private RelationService relationService;

	/**
	 * The service that handles the {@link FreebimUser}.
	 */
	@Autowired
	private FreebimUserService freebimUserService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#setRepository(org.
	 * springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(Neo4jRepository<MessageNode, Long> r) {
		this.repository = r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#getRelevantQuery(java.
	 * lang.StringBuilder, java.lang.String)
	 */
	@Override
	protected void getRelevantQuery(StringBuilder b, String returnStatement) {
		b.append("MATCH (y:MessageNode)");
		b.append(" WHERE y.validFrom < {now} AND (y.validTo IS NULL OR y.validTo > {now})");
		b.append(returnStatement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.MessageNodeService#getCurrentMessages()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<MessageNode> getCurrentMessages() {
		List<MessageNode> msgs = new ArrayList<>();
		FreebimUser user = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null) {
			logger.debug("getCurrentMessages for ROLE_ANONYMOUS");
			msgs = ((MessageNodeRepository) this.repository).getMessagesForAnonymous(this.dateService.getMillis());
		} else {
			logger.debug("getCurrentMessages for user=[{}]", auth.getName());

			user = this.freebimUserService.get(auth.getName());
			if (user != null) {
				msgs = ((MessageNodeRepository) this.repository).getMessagesFor(user.getNodeId(),
						this.dateService.getMillis());
			}
		}
		logger.debug("found [{}] messages for user [{}]", msgs.size(), ((user == null) ? "null" : user.getUsername()));
		return msgs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.MessageNodeService#setSeen(java.lang.Long)
	 */
	@Override
	@Transactional
	public void setSeen(Long messageId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			FreebimUser user = this.freebimUserService.get(auth.getName());
			if (user != null) {
				logger.debug("auth.name=[{}]", auth.getName());
				MessageNode msg = this.repository.findById(messageId).orElse(null);
				if (msg != null) {
					MessageSeen r = new MessageSeen();
					r.setN1(msg);
					r.setN2(user);
					r.setTs(this.dateService.getMillis());
					r = (MessageSeen) this.relationService.save(r);
					logger.info("Message id=[{}] marked as seen for user [{}].", msg.getNodeId(), user.getUsername());
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.MessageNodeService#setClosed(java.lang.Long)
	 */
	@Override
	@Transactional
	public void setClosed(Long messageId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			FreebimUser user = this.freebimUserService.get(auth.getName());
			if (user != null) {
				if (user.getRoles().contains(Role.ROLE_GUEST))
					return;
				logger.debug("auth.name=[{}]", auth.getName());
				MessageNode msg = this.repository.findById(messageId).orElse(null);
				if (msg != null) {
					MessageClosed r = new MessageClosed();
					r.setN1(msg);
					r.setN2(user);
					r.setTs(this.dateService.getMillis());
					r = (MessageClosed) this.relationService.save(r);
					logger.info("Message id=[{}] marked as closed for user [{}].", msg.getNodeId(), user.getUsername());
				}
			}
		}
	}

}
