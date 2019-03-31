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
package at.freebim.db.service;

import java.util.List;

import org.springframework.security.access.annotation.Secured;

/**
 * The interface for the admin service.
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface AdminService {

	/**
	 * Creates a backup. If the directory, in which the backup will be saved, does not exist 
	 * then it will be created.
	 * 
	 * @return returns <code>true</code> if the backup succeeds and <code>false</code> otherwise
	 * 
	 * */
	@Secured({"ROLE_ADMIN"})
	public boolean createBackup();
	
	/**
	 * Get a list of all available backups, previously created via {@link #createBackup()}.
	 * @return The list of available backups.
	 */
	@Secured({"ROLE_ADMIN"})
	public List<String> getAvailableBackups();

	/**
	 * Restore a backup.<br>
	 * All existing data will be deleted.
	 * 
	 * @param backup Name (directory) of backup to restore.<br>
	 * 			Possible backup names could be retrieved via {@link #getAvailableBackups()}.
	 * 
	 * @return <code>true</code> on success, <code>false</code> on error.
	 */
	@Secured({"ROLE_ADMIN"})
	public boolean restoreBackup(String backup);
	
}
