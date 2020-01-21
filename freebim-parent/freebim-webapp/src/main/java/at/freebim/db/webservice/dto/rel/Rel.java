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
package at.freebim.db.webservice.dto.rel;

import javax.xml.bind.annotation.XmlElement;

import at.freebim.db.webservice.DtoHelper;

/**
 * DTO for a simple relation.
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class Rel {

	/**
	 * The freeBIM-ID
	 */
	private String freebimId;

	/**
	 * The information.
	 */
	private String info;

	/**
	 * The helper class.
	 */
	private DtoHelper dtoHelper;

	/**
	 * Constructs a new relation instance.
	 * 
	 * @param uuid      The freeBIM-ID of the related object.
	 * @param info      The optional <code>info</code> field of the original
	 *                  relation object.
	 * @param dtoHelper The helper.
	 */
	public Rel(String uuid, String info, DtoHelper dtoHelper) {
		this.freebimId = uuid;
		this.info = info;
		this.dtoHelper = dtoHelper;
	}

	/**
	 * Get the freeBIM-ID of the related object.
	 * 
	 * @return the freebimId
	 */
	@XmlElement
	public String getFreebimId() {
		return this.dtoHelper.getString(freebimId);
	}

	/**
	 * Set the freeBIM-ID of the related object.
	 * 
	 * @param freebimId the freebimId to set
	 */
	public void setFreebimId(String freebimId) {
		this.freebimId = freebimId;
	}

	/**
	 * Get the info field.
	 * 
	 * @return the info
	 */
	@XmlElement
	public String getInfo() {
		return this.dtoHelper.getString(info);
	}

	/**
	 * Set the info field.
	 * 
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

}
