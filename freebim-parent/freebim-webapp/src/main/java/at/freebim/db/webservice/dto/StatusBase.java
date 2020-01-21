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
package at.freebim.db.webservice.dto;

import javax.xml.bind.annotation.XmlElement;

import at.freebim.db.domain.base.State;
import at.freebim.db.domain.base.StatedBaseNode;
import at.freebim.db.webservice.DtoHelper;

/**
 * Base class representing a single DTO node with a state field. The class
 * extends {@link Base}.
 * 
 * @see at.freebim.db.domain.base.StatedBaseNode
 * @see at.freebim.db.webservice.dto.Base
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 * @param <T> the node with a state field
 */
public class StatusBase<T extends StatedBaseNode> extends Base<T> {

	/**
	 * Creates a new instance.
	 * 
	 * @param node      the node
	 * @param dtoHelper the helper class
	 */
	protected StatusBase(T node, DtoHelper dtoHelper) {
		super(node, dtoHelper);
	}

	/**
	 * Get the state.
	 * 
	 * @return the state
	 */
	@XmlElement
	public State getState() {
		return this.node.getState();
	}

	/**
	 * Set the state.
	 * 
	 * @param state the state
	 */
	public void setState(State state) {
		this.node.setState(state);
	}
}
