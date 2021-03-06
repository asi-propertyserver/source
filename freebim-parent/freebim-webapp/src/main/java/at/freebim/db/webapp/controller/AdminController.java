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
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.freebim.db.service.AdminService;
import at.freebim.db.webapp.models.request.RestoreBackupModel;
import io.swagger.annotations.ApiOperation;

/**
 * The methods in this controller should only be called from the admin. This
 * class extends {@link BaseAuthController}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.webapp.controller.BaseAuthController
 */
@RestController
@RequestMapping("/admin")
public class AdminController extends BaseAuthController {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	/**
	 * This service just gives us a method to create a backup.
	 */
	@Autowired
	private AdminService adminService;

	/**
	 * Create a backup of the system. Returns a {@link AjaxResponse} that includes a
	 * boolean or an error message.
	 *
	 * @param model the model
	 * @return <code>true</code> if the backup was successful and <code>false</code>
	 *         otherwise
	 */
	@ApiOperation(value = "Create a backup", notes = "By calling this endpoint a backup will be created on the system")
	@PostMapping(value = "/createBackup")
	public @ResponseBody AjaxResponse createBackup() {

		logger.info("createBackup ...");

		AjaxResponse response = null;
		try {

			// Delegate to service
			boolean res = this.adminService.createBackup();
			response = new AjaxResponse(res);

		} catch (AccessDeniedException e) {
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error("Exception caught: ", e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}

		return response;
	}

	/**
	 * List all available backups
	 *
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "Get a list of all backups", notes = "Get a list of all backups that have been created on the system.")
	@GetMapping(value = "/listBackups")
	public @ResponseBody AjaxResponse listBackups() {
		logger.info("listBackups ...");

		AjaxResponse response = null;
		try {

			// Delegate to service
			List<String> res = this.adminService.getAvailableBackups();
			ArrayList<String> list = new ArrayList<String>(res);
			response = new AjaxResponse(list);

		} catch (AccessDeniedException e) {
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error("Exception caught: ", e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}

		return response;
	}

	@ApiOperation(value = "Restore backup", notes = "Restore the system. To restore it use the backup that has the provided name")
	@PostMapping(value = "/restoreBackup")
	public @ResponseBody AjaxResponse restoreBackup(@Valid @RequestBody(required = true) RestoreBackupModel json) {

		logger.info("restoreBackup [{}] ...", json.getBackup());

		AjaxResponse response = null;
		try {

			// Delegate to service
			boolean res = this.adminService.restoreBackup(json.getBackup());
			response = new AjaxResponse(res);

		} catch (AccessDeniedException e) {
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error("Exception caught in restoreBackup: ", e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}

		return response;
	}

}
