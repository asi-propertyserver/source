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
 * The type of the parameter set.
 *
 * @author rainer.breuss@uibk.ac.at
 */
public enum ParameterSetType {

	PROPERTYSET(1), QUANTITYSET(2);

	/**
	 * The code.
	 */
	private final int code;

	/**
	 * Set the type of the parameter set.
	 *
	 * @param code the code of the parameter set type to set
	 */
	ParameterSetType(int code) {
		this.code = code;
	}

	/**
	 * Takes an integer and returns the corresponding enum
	 *
	 * @param code the code
	 * @return the corresponding enum
	 */
	public static ParameterSetType fromCode(int code) {
		switch (code) {
		default:
		case 1:
			return PROPERTYSET;
		case 2:
			return QUANTITYSET;
		}
	}

	/**
	 * Returns the code of the type of the parameter set.
	 *
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
}
