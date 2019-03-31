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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.view.RedirectView;

import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.Role;
import at.freebim.db.domain.base.RoleContributor;
import at.freebim.db.service.ContributorService;
import at.freebim.db.service.DateService;
import at.freebim.db.service.FreebimUserService;
import at.freebim.db.webapp.session.NodeInfo;
import at.freebim.db.webapp.session.SessionObject;
import at.freebim.db.webapp.session.SessionTracker;
import at.freebim.db.webapp.session.SessionTracker.SessionAction;

/**
 * The basic component of a controller adds the exception handling, 
 * the method to create a model, from the current authenticated user, and some
 * basic methods to track the modification of nodes. Additionally it already integrates 
 * some basic services that might be needed from controllers that extend this class.
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@ControllerAdvice
public class BaseAuthController {
	
	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(BaseAuthController.class);

	/**
	 * This service is used to update the app or get information about the current version.
	 */
	@Autowired
	protected AppVersion appVersion;
	
	/**
	 * This service handles the {@link FreebimUser}.
	 */
	@Autowired
	protected FreebimUserService freebimUserService;
	
	/**
	 * This service handles the {@link Contributor}.
	 */
	@Autowired
	protected ContributorService contributorService;
	
	/**
	 * This service is used to track changes in the current session.
	 */
	@Autowired 
	protected SessionTracker sessionTracker;
	
	/**
	 * This service handles the date.
	 */
	@Autowired
	private DateService dateService;
	
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
	
	/**
	 * Creates a new instance.
	 */
	public BaseAuthController() {
		super();
	}
	
	/**
	 * This method handles exceptions of the type {@link HttpRequestMethodNotSupportedException}.
	 * When this method is called you are being redirected to /.
	 * 
	 * @param ex the exception
	 * @return the view to which you will be redirected
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public RedirectView handleException1(HttpRequestMethodNotSupportedException ex) {
		logger.info("handling HttpRequestMethodNotSupportedException, will return 'main-page' ...");
		RedirectView redirectView = new RedirectView("/");
	    return redirectView;
	}
	
	/**
	 * This method handles exceptions of the type {@link NoSuchRequestHandlingMethodException}.
	 * When this method is called you are being redirected to main-page.
	 * 
	 * @param ex the exception
	 * @return the view to which you will be redirected
	 */
	@ExceptionHandler(NoSuchRequestHandlingMethodException.class)
	public String handleException2(NoSuchRequestHandlingMethodException ex) {
		logger.error("handling Exception: ", ex);
		logger.info("handling NoSuchRequestHandlingMethodException, will return 'main-page' ...");
	    return "main-page";
	}
	
	/**
	 * This method handles exceptions of the type {@link MissingServletRequestParameterException}.
	 * When this method is called you are being redirected to /.
	 * 
	 * @param ex the exception
	 * @return the view to which you will be redirected
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public RedirectView handleException3(Exception ex) {
		logger.error("handling Exception: ", ex);
		RedirectView redirectView = new RedirectView("/");
	    return redirectView;
	}
	
	/**
	 * This method handles exceptions of the type {@link Exception}.
	 * It is only called when there is no other suitable method that can handle the exception.
	 * When this method is called you are being redirected to main-page.
	 * 
	 * @param ex the exception
	 * @return the view to which you will be redirected
	 */
	@ExceptionHandler(Exception.class)
	public String handleException4(Exception ex) {
		logger.error("handling Exception: ", ex);
		logger.info("handling Exception, will return 'main-page' ...");
	    return "main-page";
	}

	/**
	 * Set the information about the user in the provided {@link Model}.
	 * 
	 * @param model in this object the informations are set
	 */
	protected void setUserInfo(Model model) {

		model.addAttribute("appVersion", appVersion.getAppVersion());
		model.addAttribute("minified", ((appVersion.isRelease()) ? "/min" : "") );
		model.addAttribute("release", appVersion.isRelease());
		model.addAttribute("appBuildTimestamp", appVersion.getBuildTime());
		
		model.addAttribute("guest_name", this.guestName);
		model.addAttribute("guest_password", this.guestPassword);

		SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.GERMANY);
		SimpleDateFormat DATEFORMAT = new SimpleDateFormat("d. M. yyyy", Locale.GERMANY);
		SimpleDateFormat TIMEFORMAT = new SimpleDateFormat("HH:mm:ss z", Locale.GERMANY);
		try {
			Date date = ISO8601DATEFORMAT.parse(appVersion.getBuildTime());
			model.addAttribute("appBuildDate", DATEFORMAT.format(date));
			model.addAttribute("appBuildTime", TIMEFORMAT.format(date));
		} catch (ParseException e) {
			model.addAttribute("appBuildDate", "?");
			model.addAttribute("appBuildTime", "?");
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			logger.debug("auth.name=[{}]", auth.getName());
			model.addAttribute("username", auth.getName());
	
			FreebimUser user = this.freebimUserService.get(auth.getName());
			if (user != null) {
				Collection<Role> roles = user.getRoles();
				model.addAttribute("userGuest", roles.contains(Role.ROLE_GUEST));
				boolean isAdmin = roles.contains(Role.ROLE_ADMIN);
				model.addAttribute("userAdmin", isAdmin);
				model.addAttribute("userContributor", roles.contains(Role.ROLE_CONTRIBUTOR));
				model.addAttribute("userUsermanager", roles.contains(Role.ROLE_USERMANAGER));
				model.addAttribute("userEdit", roles.contains(Role.ROLE_EDIT));
				if (user.getContributor() != null) {
					Contributor c = this.contributorService.getByNodeId(user.getContributor().getNodeId());
					logger.debug("contributor=[{}]", c.getCode());
					model.addAttribute("contributorId", c.getNodeId());
					model.addAttribute("contributorCode", c.getCode());
					model.addAttribute("contributorName", ((c.getTitle() != null) ? c.getTitle() + " " : "") + ((c.getFirstName() != null) ? c.getFirstName() + " " : "") + ((c.getLastName() != null) ? c.getLastName() : ""));
					model.addAttribute("contributorId", c.getNodeId());
					model.addAttribute("contributorMayDelete", this.contributorService.test(c, new RoleContributor[]{RoleContributor.ROLE_DELETE}));
					model.addAttribute("contributorMayViewExtensions", this.contributorService.test(c, new RoleContributor[]{RoleContributor.ROLE_VIEW_EXTENSIONS}));
					model.addAttribute("contributorMaySetStatus", this.contributorService.test(c, new RoleContributor[]{RoleContributor.ROLE_SET_STATUS}));
					model.addAttribute("contributorMaySetReleaseStatus", this.contributorService.test(c, new RoleContributor[]{RoleContributor.ROLE_SET_RELEASE_STATUS}));
					model.addAttribute("contributorMayManageLibraries", this.contributorService.test(c, new RoleContributor[]{RoleContributor.ROLE_LIBRARY_REFERENCES}));
					
				}
			}
		}
	}
	
	/**
	 * Add the modified and saved nodes to the ajax response.
	 * 
	 * @param response the response to whiche the modiefied nodes will be added
	 */
	protected void savedNodesNotifications(AjaxResponse response) {
		if (response != null) {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			if (attr != null) {
				HttpServletRequest rq = attr.getRequest();
				if (rq != null) {
					HttpSession session = rq.getSession();
					if (session != null) {
						String sessionId = session.getId();
						SessionObject so = this.sessionTracker.getSessionObject(sessionId);
						if (so != null) {
							ArrayList<NodeInfo> transmit = so.getInfos();
							if (transmit != null && transmit.size() > 0) {
								response.setSavedNodes(transmit);
								logger.debug("transmitting {} NodeInfo for sessionId= {} .", transmit.size(), sessionId);
							} else {
								logger.debug("No NodeInfo for sessionId= {} .", sessionId);
							}
						} else {
							logger.debug("No SessionObject for sessionId= {} .", sessionId);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Add to the {@link SessionTracker} the id of the node and 
	 * the action that has been performed ({@link SessionAction}).
	 * 
	 * @param nodeId the id of the node that has in any way been modified
	 * @param action the action that has been performed on the node
	 */
	protected void notifySessionSaved(Long nodeId, SessionAction action) {
		if (nodeId == null)
			return;

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession();
		String sessionId = session.getId();
		
		NodeInfo info = new NodeInfo(action);
		info.setNodeId(nodeId);
		info.setTs(this.dateService.getMillis());
		
		this.sessionTracker.addInfo(info, sessionId);
	}

	/**
	 * Add to the {@link SessionTracker} the id of the node and 
	 * the action that has been performed ({@link SessionAction}).
	 * 
	 * @param node from this node the id will be taken and added to the {@link SessionTracker}.
	 * @param action
	 */
	protected void notifySessionInserted(BaseNode node, SessionAction action) {
		if (node == null)
			return;

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession();
		String sessionId = session.getId();
		
		NodeInfo info = new NodeInfo(action);
		info.setNodeId(node.getNodeId());
		info.setC(node.getClass().getSimpleName());
		info.setTs(this.dateService.getMillis());
		
		this.sessionTracker.addInfo(info, sessionId);
	}

}