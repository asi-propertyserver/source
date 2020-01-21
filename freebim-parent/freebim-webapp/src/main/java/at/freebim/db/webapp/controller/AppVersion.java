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

/**
 * Get information relevant to the version of the app.
 * 
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface AppVersion {

	/**
	 * Get the version of the app.
	 * 
	 * @return the version of the app
	 */
	public String getAppVersion();

	/**
	 * Get the time at which the app was build.
	 * 
	 * @return the time at which the app was build
	 */
	public String getBuildTime();

	/**
	 * Checks if this is a release version.
	 * 
	 * @return returns <code>true</code> if the app is a release version and
	 *         <code>false</code> otherwise.
	 */
	public boolean isRelease();

}