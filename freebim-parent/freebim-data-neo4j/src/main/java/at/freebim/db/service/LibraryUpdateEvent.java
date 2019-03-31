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

import org.springframework.context.ApplicationEvent;

import at.freebim.db.domain.Library;

/**
 * The {@link ApplicationEvent} for the {@link Library} updates.
 * 
 * @see at.freebim.db.domain.Library
 * @see org.springframework.context.ApplicationEvent
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class LibraryUpdateEvent extends ApplicationEvent {

	private static final long serialVersionUID = 750701160930411965L;

	/**
	 * Create new instance.
	 * 
	 * @param source the component that published the event (never <code>null</code>)
	 */
	public LibraryUpdateEvent(Object source) {
		super(source);
	}

}
