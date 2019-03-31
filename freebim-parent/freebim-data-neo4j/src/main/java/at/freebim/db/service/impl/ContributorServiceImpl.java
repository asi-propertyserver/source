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

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.base.RoleContributor;
import at.freebim.db.repository.ContributorRepository;
import at.freebim.db.service.ContributorService;
import at.freebim.db.service.DateService;

/**
 * The service for the node/class {@link Contributor}.
 * This service extends {@link LifetimeBaseNodeServiceImpl} and implements
 * {@link ContributorService}.
 * 
 * @see at.freebim.db.domain.Contributor
 * @see at.freebim.db.service.ContributorService
 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Service
public class ContributorServiceImpl extends LifetimeBaseNodeServiceImpl<Contributor> implements ContributorService {

	/**
	 * The logger.
	 */
	protected static Logger logger = LoggerFactory.getLogger(ContributorServiceImpl.class);

	/**
	 * The service for dates.
	 */
	@Autowired
	private DateService dateService;
	
	/**
	 * The user name of the admin user.
	 */
	@Value("${admin.username}")
	private String adminUsername;

	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#setRepository(org.springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(GraphRepository<Contributor> r) {
		this.repository = r;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.ContributorService#getByCode(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public Contributor getByCode(String code) {
		logger.debug("getByCode: {}, {}", code, this.repository);
		Contributor res = ((ContributorRepository) this.repository).getByCode(code);
		res = this.filterResponse(res, null);
		return res;				
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#getRelevantQuery(java.lang.StringBuilder, java.lang.String)
	 */
	@Override
	protected void getRelevantQuery(StringBuilder b, String returnStatement) {
		b.append("MATCH (y:Contributor)");
		b.append(" WHERE y.validFrom < {now} AND (y.validTo IS NULL OR y.validTo > {now})");

		b.append(returnStatement);
	}


	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@PostConstruct
	@Transactional
	public void init() {
		logger.debug("init ...");
		Contributor freeBimContributor = ((ContributorRepository) this.repository).getByCode(FREEBIM_CONTRIBUTOR_CODE);
		if (freeBimContributor == null) {
			logger.info("[{}] not found, create it ...", FREEBIM_CONTRIBUTOR_CODE);
			freeBimContributor = new at.freebim.db.domain.Contributor();
			freeBimContributor.setCode(FREEBIM_CONTRIBUTOR_CODE);
			freeBimContributor.setEmail("webmaster@freebim.at");
			freeBimContributor.setFirstName("freeBIM");
			freeBimContributor.setLastName("Tirol");
			freeBimContributor.setTitle("");
			freeBimContributor.setValidFrom(dateService.getMillis());
			freeBimContributor.setValidTo(Long.MAX_VALUE);
			RoleContributor[] roles = new RoleContributor[] {
					RoleContributor.ROLE_DELETE, 
					RoleContributor.ROLE_LIBRARY_REFERENCES, 
					RoleContributor.ROLE_SET_STATUS};
			freeBimContributor.setRoles(roles);
			freeBimContributor = super.save(freeBimContributor);
			
			logger.info("freeBimContributor saved. nodeId=[{}]", freeBimContributor.getNodeId());
		}
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.ContributorService#test(at.freebim.db.domain.Contributor, at.freebim.db.domain.base.RoleContributor[])
	 */
	@Override
	public boolean test(Contributor contributor, RoleContributor[] roleContributors) {
		if (contributor != null) {
			Collection<RoleContributor> roles = contributor.getRoles();
			if (roles != null) {
				for (RoleContributor role : roleContributors) {
					if (!roles.contains(role)) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	
}
