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
package at.freebim.db.repository;

/**
 * The {@link Exception} that is thrown when the user name already exists.
 * 
 * @see java.lang.IllegalArgumentException
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class UsernameExistsException extends IllegalArgumentException {

	private static final long serialVersionUID = -3414761844872705150L;

	/**
	 * Set the error message.
	 * 
	 * @param login the message
	 */
	public UsernameExistsException(String login) {
		super(login);
	}

}
