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
import at.freebim.db.webservice.dto.Component;
import at.freebim.db.webservice.dto.Parameter;
import at.freebim.db.webservice.dto.Phase;

/**
 * DTO for a {@link Component} to {@link Parameter} relation. These relations
 * carries the {@link Phase} information. The class extends {@link OrderedRel}.
 * 
 * @see at.freebim.db.webservice.dto.Component
 * @see at.freebim.db.webservice.dto.Parameter
 * @see at.freebim.db.webservice.dto.Phase
 * @see at.freebim.db.webservice.dto.rel.OrderedRel
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class ParameterRel extends OrderedRel {

	/**
	 * The freeBIM-ID of the {@link Phase}.
	 */
	private String phase;

	/**
	 * Constructs a new instance.
	 * 
	 * @param uuid      The freeBIM-ID of the related object.
	 * @param order     The order field used to sort.
	 * @param phaseUuid The freeBIM-ID of the {@link Phase}.
	 * @param info      The optional <code>info</code> field of the original
	 *                  relation object.
	 * @param dtoHelper The helper.
	 */
	public ParameterRel(String uuid, int order, String phaseUuid, String info, DtoHelper dtoHelper) {
		super(uuid, order, info, dtoHelper);
		this.phase = phaseUuid;
	}

	/**
	 * Get the freeBIM-ID of the {@link Phase}.
	 * 
	 * @return the phase
	 */
	@XmlElement
	public String getPhase() {
		return phase;
	}

	/**
	 * Set the freeBIM-ID of the {@link Phase}.
	 * 
	 * @param phase the phase to set
	 */
	public void setPhase(String phase) {
		this.phase = phase;
	}

}
