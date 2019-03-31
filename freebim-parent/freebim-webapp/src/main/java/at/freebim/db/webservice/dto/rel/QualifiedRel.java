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

import at.freebim.db.webservice.DtoHelper;

/**
 * DTO for a relation with a floating point 'quality' field.
 * The class extends {@link Rel}.
 * 
 * @see at.freebim.db.webservice.dto.rel.Rel
 *
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class QualifiedRel extends Rel {

	/**
	 * The quality.
	 */
	private double q;

	/**
	 * Constructs a new instance.
	 * 
	 * @param uuid The freeBIM-ID of the related object.
	 * @param q The 'quality' field.
	 * @param info The optional <code>info</code> field of the original relation object.
	 * @param dtoHelper The helper.
	 */
	public QualifiedRel(String uuid, double q, String info, DtoHelper dtoHelper) {
		super(uuid, info, dtoHelper);
		this.q = q;
	}

	/**
	 * Get the 'quality' field.
	 * 
	 * @return the q
	 */
	public double getQ() {
		return q;
	}

	/**
	 * Set the 'quality' field.
	 * 
	 * @param q the q to set
	 */
	public void setQ(double q) {
		this.q = q;
	}

}
