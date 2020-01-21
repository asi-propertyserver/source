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
 * The typ of the component.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Component
 */
public enum ComponentType {

	UNDEFINED(1), ABSTRACT(2), CONCRETE(3), COMPOUND(4), VARIABLE(5);

	/**
	 * The code
	 */
	private final int code;

	/**
	 * Set the type of the component.
	 *
	 * @param code the type to set
	 */
	ComponentType(int code) {
		this.code = code;
	}

	/**
	 * Get the enum corresponding to the integer
	 *
	 * @param code the code
	 */
	public static ComponentType fromCode(int code) {
		switch (code) {
		default:
		case 1:
			return UNDEFINED;
		case 2:
			return ABSTRACT;
		case 3:
			return CONCRETE;
		case 4:
			return COMPOUND;
		case 5:
			return VARIABLE;
		}
	}

	/**
	 * Get the code.
	 *
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
}
