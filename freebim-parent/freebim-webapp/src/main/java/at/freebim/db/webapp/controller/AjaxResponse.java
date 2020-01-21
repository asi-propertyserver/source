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

import java.io.Serializable;
import java.util.ArrayList;

import at.freebim.db.webapp.session.NodeInfo;

/**
 * This class is used as general response to an ajax-request.
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class AjaxResponse {

	/**
	 * The error.
	 */
	private String error;

	/**
	 * Is set to true when you need to log in to be able to call the method.
	 */
	private boolean needsLogin;

	/**
	 * Is set to true when the access is denied.
	 */
	private boolean accessDenied;

	/**
	 * The serialized result. For example when you request a list of nodes the
	 * result will be saved into this variable.
	 */
	private Serializable result;

	/**
	 * A list of nodes that have been modified in some way.
	 */
	private ArrayList<NodeInfo> savedNodes;

	/**
	 * Is set to <code>true</code> if a bsdd-guid changed.
	 */
	private boolean bsddGuidChanged;

	/**
	 * Create a new instance with the result.
	 * 
	 * @param result the serializable result
	 */
	public AjaxResponse(Serializable result) {
		this.result = result;
		bsddGuidChanged = false;
	}

	/**
	 * Get the error.
	 * 
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * Set an error.
	 * 
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Checks if you need to login.
	 * 
	 * @return returns <code>true</code> if you need to log in <code>false</code>
	 *         otherwise.
	 */
	public boolean isNeedsLogin() {
		return needsLogin;
	}

	/**
	 * Sets if you need to log in.
	 * 
	 * @param needsLogin
	 */
	public void setNeedsLogin(boolean needsLogin) {
		this.needsLogin = needsLogin;
	}

	/**
	 * Checks if the access is denied.
	 * 
	 * @return returns true if the access was denied false otherwise
	 */
	public boolean isAccessDenied() {
		return accessDenied;
	}

	/**
	 * Sets if the access was denied.
	 * 
	 * @param accessDenied
	 */
	public void setAccessDenied(boolean accessDenied) {
		this.accessDenied = accessDenied;
	}

	/**
	 * Get the serialized result.
	 * 
	 * @return the result
	 */
	public Serializable getResult() {
		return result;
	}

	/**
	 * Set the serialized result.
	 * 
	 * @param result the result to set
	 */
	public void setResult(Serializable result) {
		this.result = result;
	}

	/**
	 * Get a {@link ArrayList} of modified nodes.
	 * 
	 * @return the {@link ArrayList} of modified nodes
	 */
	public ArrayList<NodeInfo> getSavedNodes() {
		return savedNodes;
	}

	/**
	 * Set the {@link ArrayList} of modified nodes.
	 * 
	 * @param savedNodes the {@link ArrayList} of {@link NodeInfo} to set
	 */
	public void setSavedNodes(ArrayList<NodeInfo> savedNodes) {
		this.savedNodes = savedNodes;
	}

	/**
	 * Checks if the bsdd-guid did change.
	 * 
	 * @return <code>true</code> if it did change <code>false</code> otherwise
	 */
	public boolean isBsddGuidChanged() {
		return bsddGuidChanged;
	}

	/**
	 * Set if the bsdd-guid did change.
	 * 
	 * @param bsddGuidChanged the bsddGuidChanged to set
	 */
	public void setBsddGuidChanged(boolean bsddGuidChanged) {
		this.bsddGuidChanged = bsddGuidChanged;
	}
}
