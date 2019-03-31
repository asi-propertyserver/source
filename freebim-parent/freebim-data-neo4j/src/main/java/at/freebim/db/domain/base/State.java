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
package at.freebim.db.domain.base;

/**
 * This enum represents a state in which a contribution can be in.
 * 
 * @see at.freebim.db.domain.base.StatedBaseNode
 * @see at.freebim.db.domain.base.ContributedBaseNode
 * 
 * @author rainer.breuss@uibk.ac.at
 */
public enum State {

	UNDEFINED (1, "STATE_UNDEFINED", "state-undefined"),
	REJECTED (2, "STATE_REJECTED", "state-rejected"),
	TODELETE (5, "STATE_TODELETE", "state-rejected"),
	CHECKED (3, "STATE_CHECKED", "state-checked"),
	RELEASED (4, "STATE_RELEASED", "state-released"),
	IMPORTED (6, "STATE_IMPORTED", "state-imported");
	
	/**
	 * The code.
	 */
	private final int code;
	
	
	/**
	 * The name in the user interface.
	 */
	private final String uiName;
	
	
	/**
	 * The name of the css class.
	 */
	private final String cssClass;
	
	/**
	 * @param	code		the code representing the enum
	 * @param	uiName		the name of the enum in the ui
	 * @param 	cssClass	the css class representing the state
	 * */
	private State(int code, String uiName, String cssClass) {
		this.code = code;
		this.uiName = uiName;
		this.cssClass = cssClass;
	}
	
	/**
	 * Get the user interface name.
	 * 
	 * @return the uiName
	 */
	public String getUiName() {
		return uiName;
	}

	/**
	 * Get the name of the css class.
	 * 
	 * @return the cssClass
	 */
	public String getCssClass() {
		return cssClass;
	}

	/**
	 * Get the code.
	 * 
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * Turns an integer in the enum {@link State}.
	 * 
	 * @param code	the enum code
	 * @return the enum
	 * */
	public static State fromCode(int code) {
		switch (code) {
			default:
			case 1 : return UNDEFINED;
			case 2 : return REJECTED;
			case 3 : return CHECKED;
			case 4 : return RELEASED;
			case 5 : return TODELETE;
		}
	}
}
