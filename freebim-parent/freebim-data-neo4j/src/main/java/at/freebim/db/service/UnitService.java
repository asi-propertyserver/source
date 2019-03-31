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

import at.freebim.db.domain.Unit;

/**
 * The service for the node/class {@link Unit}.
 * This service extends {@link BsddObjectService} and {@link ContributedBaseNodeService}.
 * 
 * @see at.freebim.db.domain.Unit
 * @see at.freebim.db.service.ContributedBaseNodeService
 * @see at.freebim.db.service.BsddObjectService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface UnitService extends ContributedBaseNodeService<Unit>, BsddObjectService<Unit> {

	/**
	 * Convert the passed unit name to a standardized format:
	 * <ul>
	 * <li>MM -> mm</li>
	 * <li>CM -> cm</li>
	 * <li>DM -> dm</li>
	 * <li>M -> m</li>
	 * <li>KM -> km</li>
	 * <li>mm2 -> mm²</li>
	 * <li>cm2 -> cm²</li>
	 * <li>dm2 -> dm²</li>
	 * <li>m2 -> m²</li>
	 * <li>km2 -> km²</li>
	 * <li>mm3 -> mm³</li>
	 * <li>cm3 -> cm³</li>
	 * <li>dm3 -> dm³</li>
	 * <li>m3 -> m³</li>
	 * <li>km3 -> km³</li>
	 * <li>S -> s</li>
	 * <li>KN -> kN</li>
	 * <li>KG -> kg</li>
	 * <li>KJ -> kJ</li>
	 * <li>MK -> mK</li>
	 * Name will be trimmed, inner blanks and brackets <code>(,[,{,},],)</code> will be removed.
	 * </ul>
	 * @param name The unit name to standardize.
	 * @return The standardized unit name.
	 */
	public String standardize(String name);
}
