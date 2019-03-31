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
package at.freebim.db.domain.base.rel;

import java.io.Serializable;

/**
 * The interface that every bsdd related relation should implement.
 * It extends {@link Serializable}.
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface BsddRelation extends Serializable {
	
	/**
	 * Set the bsdd-guid (global unique identifier).
	 * 
	 * @param guid the guid to set
	 */
	public void setBsddGuid(String guid);
	
	/**
	 * Get the bsdd-guid (global unique identifier).
	 * 
	 * @return the bsdd-guid
	 */
	public String getBsddGuid();
	
	/**
	 * Get the id.
	 * 
	 * @return the id
	 */
	public Long getId();
}
