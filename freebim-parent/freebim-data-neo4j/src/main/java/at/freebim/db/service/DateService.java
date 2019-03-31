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

/**
 * The service for date.
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface DateService {

	/**
	 * Get the date from now as {@link String}.
	 * 
	 * @return returns a date as {@link String}
	 */
	public String getNow();
	
	/**
	 * Get the milliseconds from a date.
	 * 
	 * @return returns a date as {@link Long} in milliseconds
	 */
	public Long getMillis();
	
	/**
	 * Cast the provided date-time-{@link String} to milliseconds.
	 * 
	 * @param dateTime the date-time as {@link String}
	 * @return returns a date-time as {@link Long} in milliseconds
	 */
	public Long getMillisFrom(String dateTime);


	public Long FREEBIM_DELTA = 1262304000000L;
}
