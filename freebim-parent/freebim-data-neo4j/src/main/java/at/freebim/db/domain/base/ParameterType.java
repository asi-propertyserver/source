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
 * The type of a parameter.
 *
 * @author rainer.breuss@uibk.ac.at
 */
public enum ParameterType {

	UNDEFINED(9), INSTANCE(2), TYPE(1), DERIVED(3), IFC_ATTRIBUTE(4), IFC_OPTIONAL_ATTRIBUTE(5);

	/**
	 * The code.
	 */
	private final int code;

	/**
	 * Set the type of the parameter.
	 *
	 * @param code the code of the parameter to set
	 */
	ParameterType(int code) {
		this.code = code;
	}

	/**
	 * Takes an integer and returns the corresponding enum.
	 *
	 * @param code the code
	 * @return the corresponding enum
	 */
	public static final ParameterType fromCode(int code) {
		switch (code) {
		case 1:
			return ParameterType.TYPE;
		case 2:
			return ParameterType.INSTANCE;
		case 3:
			return ParameterType.DERIVED;
		case 4:
			return ParameterType.IFC_ATTRIBUTE;
		case 5:
			return ParameterType.IFC_OPTIONAL_ATTRIBUTE;
		default:
			return ParameterType.UNDEFINED;
		}
	}

	/**
	 * Get the code of the parameter type.
	 *
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
}
