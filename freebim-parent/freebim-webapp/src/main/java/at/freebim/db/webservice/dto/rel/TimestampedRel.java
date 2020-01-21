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
 * DTO for a relation with a time stamp. The class extends {@link Rel}.
 * 
 * @see at.freebim.db.webservice.dto.rel.Rel
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class TimestampedRel extends Rel {

	/**
	 * The time stamp.
	 */
	private String ts;

	/**
	 * Constructs a new instance.
	 * 
	 * @param uuid      The freeBIM-ID of the related object.
	 * @param isoTs     The time stamp in ISO-8601 format.
	 * @param info      The optional <code>info</code> field of the original
	 *                  relation object.
	 * @param dtoHelper The helper.
	 */
	public TimestampedRel(String uuid, String isoTs, String info, DtoHelper dtoHelper) {
		super(uuid, info, dtoHelper);
		this.ts = isoTs;
	}

	/**
	 * Get the time stamp.
	 * 
	 * @return The time stamp in ISO-8601 format.
	 */
	@XmlElement
	public String getTs() {
		return ts;
	}

	/**
	 * Set the time stamp in ISO-8601 format.
	 * 
	 * @param ts The time stamp to set.
	 */
	public void setTs(String ts) {
		this.ts = ts;
	}

}
