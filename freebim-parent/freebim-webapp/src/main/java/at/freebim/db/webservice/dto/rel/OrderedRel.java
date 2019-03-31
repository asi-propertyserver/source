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
 * A DTO for a relation that has an integer field <code>order</code> which can be used to
 * sort a list of related nodes.
 * The class extends {@link Rel}.
 * 
 * @see at.freebim.db.webservice.dto.rel.Rel
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class OrderedRel extends Rel {

	/**
	 * The order.
	 */
	private int order;

	/**
	 * Constructs a new instance.
	 * 
	 * @param uuid The freeBIM-ID of the related object.
	 * @param order The order field used to sort.
	 * @param info The optional <code>info</code> field of the original relation object.
	 * @param dtoHelper The helper.
	 */
	public OrderedRel(String uuid, int order, String info, DtoHelper dtoHelper) {
		super(uuid, info, dtoHelper);
		this.order = order;
	}

	/**
	 * Get the order.
	 * 
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Set the order.
	 * 
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}

}
