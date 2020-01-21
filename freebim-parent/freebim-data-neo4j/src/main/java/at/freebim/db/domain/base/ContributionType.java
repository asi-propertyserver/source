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
 * This enum sets the type of contribution you made. By type is meant if you
 * created, modified or deleted a contribution.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.rel.ContributedBy
 */
public enum ContributionType {

	UNDEFINED(1), CREATE(2), MODIFY(3), DELETE(4);

	/**
	 * The code.
	 */
	private final int code;

	/**
	 * Set the type.
	 *
	 * @param code the type to set
	 */
	ContributionType(int code) {
		this.code = code;
	}

	/**
	 * Takes an integer and returns an enum {@link ContributionType}.
	 *
	 * @param code an integer between 1 and 4 representing an enum value
	 */
	public static ContributionType fromCode(int code) {
		switch (code) {
		default:
		case 1:
			return UNDEFINED;
		case 2:
			return CREATE;
		case 3:
			return MODIFY;
		case 4:
			return DELETE;
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
