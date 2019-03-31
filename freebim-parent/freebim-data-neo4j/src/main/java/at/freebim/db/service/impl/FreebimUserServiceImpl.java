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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.neo4j.graphdb.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.Role;
import at.freebim.db.domain.rel.BelongsTo;
import at.freebim.db.repository.FreebimUserRepository;
import at.freebim.db.repository.UsernameExistsException;
import at.freebim.db.service.ContributorService;
import at.freebim.db.service.DateService;
import at.freebim.db.service.FreebimUserService;
import at.freebim.db.service.RelationService;

/**
 * This service handles a user of the system. A user is represented by the node/class {@link FreebimUser}.
 * This service extends {@link LifetimeBaseNodeServiceImpl} and implements {@link FreebimUserService}.
 * 
 * @see at.freebim.db.domain.FreebimUser
 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl
 * @see at.freebim.db.service.FreebimUserService
 * 
 * 
 * @author rainer.breuss@uibk.ac.at
 * 
 */
@Service("FreebimUserService")
public class FreebimUserServiceImpl extends LifetimeBaseNodeServiceImpl<FreebimUser> implements FreebimUserService {
	
	/**
	 * The logger.
	 */
	protected static Logger logger = LoggerFactory.getLogger(FreebimUserServiceImpl.class);
	
	/**
	 * The user name of the admin user.
	 */
	@Value("${admin.username}")
	private String username;
	
	
	/**
	 * The password of the admin user.
	 */
	@Value("${admin.password}")
	private String password;
	
	/**
	 * The name of the guest.
	 */
	@Value("${guest.username}")
	private String guestName;
	
	/**
	 * The password of the guest.
	 */
	@Value("${guest.password}")
	private String guestPassword;
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#setRepository(org.springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(GraphRepository<FreebimUser> r) {
		this.repository = r;
	}

	/**
	 * The service that handles {@link Contributor}.
	 */
	@Autowired
	private ContributorService contributorService;
	
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
	 * Injected by spring:
	 * 
	 * 
	 */
	//TODO: deprecated: should be updated
	@Autowired 
	private Md5PasswordEncoder passwordEncoder;

	/**
	 * The salt for the password hashing.
	 */
	@Value("${props.salt}")
	private final String salt = "";



	/* (non-Javadoc)
	 * @see at.freebim.db.service.FreebimUserService#get(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public FreebimUser get(String login) {
		logger.debug("get [{}]", login);
		FreebimUser node = ((FreebimUserRepository) this.repository).findByUsername(login);
		return this.filterResponse(node, null);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.FreebimUserService#register(java.lang.String, java.lang.String, at.freebim.db.domain.base.Role[], at.freebim.db.domain.Contributor)
	 */
	@Override
	@Transactional
	public FreebimUser register(String login, String password, Role[] roles, Contributor contributor) throws UsernameExistsException {
		logger.debug("registering user: {} ...", login);
		FreebimUser user = ((FreebimUserRepository) this.repository).findByUsername(login);
		if (user != null)
			throw new UsernameExistsException(login);
		user = new FreebimUser();
		user.setUsername(login);
		// if password is hashed already, don't hash it again
		if (password.length() == 32) {
			user.setPassword(password);
		} else {
			user.setPassword(passwordEncoder.encodePassword(password, this.salt));
		}
		user.setRoles(roles);
		user.setContributor(contributor);
		user.setValidFrom(this.dateService.getMillis());
		user = super.save(user);
		logger.info("user: {} registered, nodeId={}.", user.getUsername(), user.getNodeId());
		return user;
	}

	@Transactional
	@PostConstruct
	public void init()  {
		logger.debug("init ...");
		
		// create admin user if not exists
		FreebimUser user = ((FreebimUserRepository) this.repository).findByUsername(this.username);
		if (user == null) {
			user = new FreebimUser();
			user.setUsername(this.username);
			user.setPassword(this.passwordEncoder.encodePassword(this.password, this.salt));
			user.setValidFrom(this.dateService.getMillis());
			user.setRoles(new Role[]{Role.ROLE_USERMANAGER, Role.ROLE_EDIT, Role.ROLE_ADMIN});
			user = super.save(user);
			logger.info("User 'admin' saved. nodeId=[{}].", user.getNodeId());
			
			Authentication auth = new UsernamePasswordAuthenticationToken (user.getUsername(), this.password, user.getRoles());
			SecurityContextHolder.getContext().setAuthentication(auth);

			try {
				Contributor freeBimContributor = this.contributorService.getByCode(ContributorService.FREEBIM_CONTRIBUTOR_CODE);
				BelongsTo rel = new BelongsTo();
				rel.setN1(user);
				rel.setN2(freeBimContributor);
				this.relationService.save(rel);

			} catch (Exception e) {
				logger.error("Error in createIfcStructure: ", e);
			}
			
			SecurityContextHolder.getContext().setAuthentication(null);

			
		}
		
		//create guest user if no one exists
		FreebimUser guest = ((FreebimUserRepository) this.repository).findByUsername(this.guestName);
		if (guest == null) {
			guest = new FreebimUser();
			guest.setUsername(this.guestName);
			guest.setPassword(this.passwordEncoder.encodePassword(this.guestPassword, this.salt));
			guest.setValidFrom(this.dateService.getMillis());
			guest.setRoles(new Role[] { Role.ROLE_GUEST });
			guest = super.save(guest);
			logger.info("User 'guest' saved. nodeId=[{}].", guest.getNodeId());
		}
		
		logger.debug("init done.");
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.FreebimUserService#test(java.lang.String, java.lang.String, at.freebim.db.domain.base.Role[])
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean test(String login, String password, Role [] roles) {
		logger.debug("test login=[{}] roles=[{}] ...", login, roles);
		boolean res = false;
		FreebimUser user = ((FreebimUserRepository) this.repository).findByUsername(login);
		if (user != null) {
			String pwd = this.passwordEncoder.encodePassword(password, this.salt);
			if (user.getPassword().equals(pwd)) {
				res = true;
				Collection<Role> userRoles = user.getRoles();
				for (Role role : roles) {
					if (!userRoles.contains(role)) {
						res = false;
						break;
					}
				}
			}
			user = null;
		}
		logger.debug("test login=[{}] = [{}].", login, res);
		return res;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#filterRequest(at.freebim.db.domain.base.LifetimeBaseNode)
	 */
	@Override
	public FreebimUser filterBeforeSave(FreebimUser node) {
		
		this.manageBeforeSave(node);

		return super.filterBeforeSave(node);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.BaseNodeServiceImpl#filterBeforeInsert(at.freebim.db.domain.base.BaseNode)
	 */
	@Override
	public FreebimUser filterBeforeInsert(FreebimUser node) {
		try {
			this.manageBeforeSave(node);
		} catch (Exception e) {
			logger.error("Error in manageBeforeSave:", e);
		}
		return super.filterBeforeInsert(node);
	}

	/**
	 * Check the password and hash it if necessary.
	 * Additionally connect the {@link Contributor}. 
	 * 
	 * @param node the {@link FreebimUser}
	 */
	private void manageBeforeSave(FreebimUser node) {
		if (node != null) {
			if (node.getPassword().length() != 32) {
				node.setPassword(this.passwordEncoder.encodePassword(node.getPassword(), this.salt));
			}	
			
			if (node.getContributorId() != null) {
				Contributor c = this.contributorService.getByNodeId(node.getContributorId());
				node.setContributor(c);
			}
			
			manageRoleContributor(node);
		}
	}
	
	private void manageRoleContributor(FreebimUser node) {
		// ROLE_CONTRIBUTOR will never be set by user interface,
		// it's determined by value of contributor field,
		// and will be lost when contributor becomes invalid:
		if (node != null) {
			Long now = this.dateService.getMillis();
			if (node.getContributor() != null && (node.getContributor().getValidFrom() != null && node.getContributor().getValidFrom() <= now) && (node.getContributor().getValidTo() == null || node.getContributor().getValidTo() > now)) {
				if (node.getRoles() == null) {
					ArrayList<Role> roles = new ArrayList<Role>();
					node.setRoles(roles.toArray(new Role[]{}));
				}
				Collection<Role> roles = node.getRoles();
				if (!node.getRoles().contains(Role.ROLE_CONTRIBUTOR)) {
					ArrayList<Role> newRoles = new ArrayList<Role>();
					for (Role r : roles) {
						newRoles.add(r);
					}
					newRoles.add(Role.ROLE_CONTRIBUTOR);
					node.setRoles(newRoles.toArray(new Role[]{}));
				}
			} else {
				Collection<Role> roles = node.getRoles();
				if (roles.contains(Role.ROLE_CONTRIBUTOR)) {
					ArrayList<Role> newRoles = new ArrayList<Role>();
					for (Role r : roles) {
						if (Role.ROLE_CONTRIBUTOR.equals(r))
							continue;
						newRoles.add(r);
					}
					node.setRoles(newRoles.toArray(new Role[]{}));
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#filterResponse(at.freebim.db.domain.base.LifetimeBaseNode)
	 */
	@Override
	public FreebimUser filterResponse(FreebimUser node, Long now) {
		manageRoleContributor(node);
		return super.filterResponse(node, now);
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.BaseNodeServiceImpl#getAllRelevant()
	 */
	@Override
	@Transactional(readOnly=true)
	public ArrayList<FreebimUser> getAllRelevant() {

		final Long now = this.dateService.getMillis();
		ArrayList<FreebimUser> res = new ArrayList<FreebimUser>();

		StringBuilder b = new StringBuilder();

		b.append("MATCH (n:FreebimUser)"); 
		b.append(" WHERE n.validFrom <= {now} AND (NOT HAS(n.validTo) OR n.validTo > {now})");
		b.append(" RETURN distinct n");

		
		String query = b.toString();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "now", now );
		
		Result<Map<String, Object>> result = this.relationService.getTemplate().query(query, params);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			Node n = (Node) map.get("n");
			BaseNode pathNode = this.relationService.createTreeNode(n);
			if (pathNode != null && FreebimUser.class.isAssignableFrom(pathNode.getClass())) {
				res.add((FreebimUser) pathNode);
				logger.debug("    found node {} ", pathNode.getNodeId());
			}
		}
		return res;
	}

}
