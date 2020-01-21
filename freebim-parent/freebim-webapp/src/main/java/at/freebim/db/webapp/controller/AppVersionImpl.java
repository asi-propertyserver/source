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
package at.freebim.db.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.Company;
import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.base.Role;
import at.freebim.db.domain.rel.References;
import at.freebim.db.domain.rel.WorksFor;
import at.freebim.db.service.AppVersionService;
import at.freebim.db.service.BigBangNodeService;
import at.freebim.db.service.BsddNodeService;
import at.freebim.db.service.CompanyService;
import at.freebim.db.service.ContributorService;
import at.freebim.db.service.DateService;
import at.freebim.db.service.FreebimUserService;
import at.freebim.db.service.RelationService;

/**
 * This service is used to update the structure in the database to a specified
 * version. It implements {@link AppVersion}.
 * 
 * @see at.freebim.db.webapp.controller.AppVersion
 * 
 * @author rainer.breuss@uibk.ac.at
 * 
 */
@Service
public class AppVersionImpl implements AppVersion {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AppVersionImpl.class);

	/**
	 * The application version.
	 */
	@Value("${app.version}")
	protected String appVersion;

	/**
	 * The time at which this version was build.
	 */
	@Value("${app.buildtime}")
	protected String buildTime;

	/**
	 * Determines if this build is a release build.
	 */
	@Value("${app.release}")
	protected boolean release;

	/**
	 * The user name of the admin.
	 */
	@Value("${admin.username}")
	private String username;

	/**
	 * The password of the admin.
	 */
	@Value("${admin.password}")
	private String password;

	/**
	 * The service that handles the root node {@link BigBangNode}.
	 */
	@Autowired
	private BigBangNodeService bigBangNodeService;

	/**
	 * The service that handles the relations.
	 */
	@Autowired
	private RelationService relationService;

	/**
	 * The service that handles bsdd-nodes.
	 */
	@Autowired
	private BsddNodeService bsddNodeService;

	/**
	 * The service that handles the version of the app.
	 */
	@Autowired
	private AppVersionService appVersionService;

	/**
	 * The service that handles the node {@link Contributor}.
	 */
	@Autowired
	private ContributorService contributorService;

	/**
	 * The service that handles the node {@link Company}.
	 */
	@Autowired
	private CompanyService companyService;

	/**
	 * The service that handles the date.
	 */
	@Autowired
	private DateService dateService;

	/**
	 * The service that handles the {@link FreebimUser}.
	 */
	@Autowired
	private FreebimUserService freebimUserService;

	/**
	 * Find the {@link BigBangNode} and make an update or correct {@link References}
	 * if necessary.
	 */
	@PostConstruct
	public void init() {
		logger.info("Application version = [{}], build time: {}.", this.appVersion, this.buildTime);
		BigBangNode bbn = this.bigBangNodeService.getBigBangNode();
		if (bbn != null) {

			FreebimUser admin = this.freebimUserService.get(this.username);

			Authentication auth = new UsernamePasswordAuthenticationToken(admin.getUsername(), this.password,
					admin.getRoles());
			SecurityContextHolder.getContext().setAuthentication(auth);

			try {
				this.appVersionService.correctComponentLibraryReferences();
			} catch (Exception e) {
				logger.error("Error in correctComponentLibraryReferences: ", e);
			}

			try {
				this.performUpdate(bbn);
			} catch (Exception e) {
				logger.error("Error in performUpdate: ", e);
			}

			SecurityContextHolder.getContext().setAuthentication(null);
		}
	}

	/**
	 * Update the structure in the database.
	 * 
	 * @param bbn the root node of the structure
	 */
	private void performUpdate(BigBangNode bbn) {

		if (this.appVersion.equals(bbn.getAppVersion())) {
			bbn = this.bigBangNodeService.save(bbn);
			logger.info("Application version = [{}] stored to BigBangNode.", bbn.getAppVersion());
			return;
		}

		if (bbn.getAppVersion() != null) {

			switch (bbn.getAppVersion()) {

			case "1.7.2":
			case "1.7.2-SNAPSHOT":
				logger.info("perform update from [{}] to [{}] ...", bbn.getAppVersion(), this.appVersion);

				// set class names to nodes of Equal relations
				try {
					this.setClassnamesToNodesOfEqualRelations();
				} catch (Exception e) {
					logger.error("Can't setClassnamesToNodesOfEqualRelations", e);
				}

				// apply spread Bsdd relations to Equal related nodes
				try {
					this.bsddNodeService.spreadToEqualNodes();
				} catch (Exception e) {
					logger.error("Can't spreadToEqualNodes", e);
				}

				// Equal related nodes should have the same bsdd-Guid
				try {
					this.bsddNodeService.setBsddFieldToEqualNodes();
				} catch (Exception e) {
					logger.error("Can't setBsddFieldToEqualNodes", e);
				}

				logger.info("perform update from [{}] done.", bbn.getAppVersion());

				bbn.setAppVersion("1.7.3"); // next Version
				return;

			case "1.7.3":
			case "1.7.3-SNAPSHOT":
				logger.info("perform update from [{}] to [{}] ...", bbn.getAppVersion(), this.appVersion);
				Long count = this.appVersionService.createParentOfRelations();
				logger.info("\t[{}] ParentOf relations created.", count);
				logger.info("perform update from [{}] done.", bbn.getAppVersion());
				bbn.setAppVersion("1.8"); // next Version
				break;

			case "1.8":
				// create Company nodes for Contributors
				logger.info("create Company nodes for Contributors ...");
				HashMap<String, Company> companies = new HashMap<String, Company>();
				ArrayList<Company> comps = this.companyService.getAll(false);
				Long now = this.dateService.getMillis();
				for (Company c : comps) {
					if (c.getValidFrom() < now && (c.getValidTo() == null || c.getValidTo() > now)) {
						String cn = c.getName().replaceAll(" ", "").trim().toLowerCase();
						companies.put(cn, c);
					}
				}
				ArrayList<Contributor> contributors = this.contributorService.getAll(false);
				for (Contributor contributor : contributors) {

					String companyName = contributor.getCompany().trim();
					String cn = companyName.replaceAll(" ", "").trim().toLowerCase();
					Company c = companies.get(cn);
					if (c == null) {
						c = new Company();
						c.setName(companyName);
						c = this.companyService.save(c);
						logger.info("\tCompany [{}] saved, nodeId=[{}].", companyName, c.getNodeId());
						companies.put(cn, c);
					}
					WorksFor wf = new WorksFor();
					wf.setN1(contributor);
					wf.setN2(c);
					this.relationService.save(wf);
				}
				logger.info("create Company nodes for Contributors finished.");

				// add the new role 'ROLE_EDIT' to all FreebimUser instances
				// that have an assigned Contributor
				logger.info("update users ...");
				ArrayList<FreebimUser> users = this.freebimUserService.getAll(false);
				for (FreebimUser user : users) {
					ArrayList<Role> roles = new ArrayList<Role>();
					for (Role r : user.getRoles()) {
						roles.add(r);
					}
					Contributor c = user.getContributor();
					if (c != null || roles.contains(Role.ROLE_USERMANAGER) || roles.contains(Role.ROLE_ADMIN)) {
						if (!roles.contains(Role.ROLE_EDIT)) {
							roles.add(Role.ROLE_EDIT);
							user.setRoles(roles.toArray(new Role[] {}));
							user = this.freebimUserService.save(user);
							logger.info("\trole '{}' added to user [{}].", Role.ROLE_EDIT, user.getUsername());
						}
					}
				}
				logger.info("update users finished.");

				logger.info("perform update from [{}] done.", bbn.getAppVersion());
				bbn.setAppVersion("1.8.1"); // next Version
				break;

			case "1.8.1":
			case "1.8.1-SNAPSHOT":
				bbn.setAppVersion("1.9"); // next Version
				break;

			case "1.9":
				logger.info("perform update from [{}] to [{}] ...", bbn.getAppVersion(), this.appVersion);
				count = this.appVersionService.createParentOfRelations();
				logger.info("\t[{}] ParentOf relations created.", count);
				logger.info("perform update from [{}] done.", bbn.getAppVersion());
				bbn.setAppVersion("1.10"); // next Version
				break;

			case "1.10":
			case "1.10-SNAPSHOT":
				logger.info("perform update from [{}] to [{}] ...", bbn.getAppVersion(), this.appVersion);
				count = this.appVersionService.createValueListOfComponentRelations();
				logger.info("\t[{}] ValueListOfComponent relations created.", count);
				logger.info("perform update from [{}] done.", bbn.getAppVersion());
				bbn.setAppVersion("1.11"); // next Version
				break;

			case "1.11":
			case "1.11.1-SNAPSHOT":
				count = this.appVersionService.dropEqualSelfRelations();
				logger.info("\t[{}] Equal self Relations removed.", count);
				logger.info("perform update from [{}] to [{}] ...", bbn.getAppVersion(), this.appVersion);
				count = this.appVersionService.dropDuplicateEqualRelations();
				logger.info("\t[{}] duplicate Equal Relations removed.", count);
				count = this.appVersionService.dropMultipleEqualRelations();
				logger.info("\t[{}] Multiple Equal Relations removed.", count);
				bbn.setAppVersion("1.12.6"); // next Version
				break;

			case "1.12.6":
				logger.info("perform DB-cleanup ...");
				this.appVersionService.performDbCleanup();
				bbn.setAppVersion("1.12.7"); // next Version
				break;

			case "1.12.7":
				logger.info("perform DB-cleanup ...");
				this.appVersionService.performDbCleanup();
				bbn.setAppVersion(this.appVersion); // next Version
				break;

			default:
				logger.info("No specific update required for [{}].", bbn.getAppVersion());
				bbn.setAppVersion(this.appVersion); // next Version
				break;

			} // switch (bbn.getAppVersion())

			performUpdate(bbn);

		} else {

			// no appVersion in BigBangNode before 1.7.3
			logger.info("no Application version in BigBangNode.");
			bbn.setAppVersion("1.7.2");
			this.performUpdate(bbn);
			return;

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.webapp.controller.AppVersion#getAppVersion()
	 */
	@Override
	public String getAppVersion() {
		return appVersion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.webapp.controller.AppVersion#getBuildTime()
	 */
	@Override
	public String getBuildTime() {
		return buildTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.webapp.controller.AppVersion#isRelease()
	 */
	@Override
	public boolean isRelease() {
		return this.release;
	}

	/**
	 * Cross-Class-Equality, that is an Equal relation like
	 * 
	 * (a:ValueListEntry)-[:EQUALS]-(b:Component)
	 * 
	 * need to know the class names of the related nodes.
	 * 
	 * This information wasn't stored prior to version 1.7.3, so we have to fetch it
	 * once now.
	 */
	private void setClassnamesToNodesOfEqualRelations() {

		logger.info("setClassnamesToNodesOfEqualRelations ...");

		StringBuilder b = new StringBuilder();

		b.append("MATCH (n)-[r:EQUALS]->(e)");
		b.append(" WITH r AS r,");
		b.append(" [x IN labels(n) WHERE x =~ \"_.+\" | replace(x, \"_\", \"\")] AS fc,");
		b.append(" [y IN labels(e) WHERE y =~ \"_.+\" | replace(y, \"_\", \"\")] AS tc");
		b.append(" WHERE fc <> tc SET r.fromClass=fc, r.toClass=tc");
		b.append(" RETURN count(r) AS c");

		String query = b.toString();

		Iterable<Map<String, Object>> result = this.relationService.getTemplate().query(query,
				new HashMap<String, Object>());
		Iterator<Map<String, Object>> iter = result.iterator();

		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			Long count = (Long) map.get("c");
			logger.info("setClassnamesToNodesOfEqualRelations n=[{}].", count);
		}
	}

}
