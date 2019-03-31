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

import at.freebim.db.webservice.DtoHelper;

/**
 * DTO of a {@link at.freebim.db.domain.ValueListEntry}.
 * The class extends {@link StatusBase}.
 * 
 * @see at.freebim.db.domain.ValueListEntry
 * @see at.freebim.db.webservice.dto.StatusBase
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class ValueListEntry extends StatusBase<at.freebim.db.domain.ValueListEntry> {

	/**
	 * Creates a new instance.
	 * 
	 * @param node The original node.
	 * @param dtoHelper The helper.
	 */
	public ValueListEntry(at.freebim.db.domain.ValueListEntry node, DtoHelper dtoHelper) {
		super(node, dtoHelper);
	}

	/**
	 * Get the local description.
	 * 
	 * @return The local description.
	 */
	public String getDesc() {
		return this.dtoHelper.getString(node.getDesc());
	}

	/**
	 * Set the local description.
	 * 
	 * @param desc The local description to set.
	 */
	public void setDesc(String desc) {
		node.setDesc(desc);
	}

	/**
	 * Get the description in international English.
	 * 
	 * @return The description in international English.
	 */
	public String getDescEn() {
		return this.dtoHelper.getString(node.getDescEn());
	}

	/**
	 * Set the description in international English.
	 * 
	 * @param descEn The description to set (in international English).
	 */
	public void setDescEn(String descEn) {
		node.setDescEn(descEn);
	}
	
	/**
	 * Get the local name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return this.dtoHelper.getString(node.getName());
	}

	/** 
	 * Set the local name.
	 * 
	 * @param name The local name to set.
	 */
	public void setName(String name) {
		node.setName(name);
	}

	/** 
	 * Get the comment.
	 * 
	 * @return The comment.
	 */
	public String getComment() {
		return this.dtoHelper.getString(node.getComment());
	}

	/** 
	 * Set the comment.
	 * 
	 * @param c The comment to set.
	 */
	public void setComment(String c) {
		node.setComment(c);
	}

	/**
	 * Get the name in international English.
	 * 
	 * @return the name in international English.
	 */
	public String getNameEn() {
		return this.dtoHelper.getString(node.getNameEn());
	}

	/**
	 * Set the name in international English.
	 * 
	 * @param nameEn The name to set (in international English).
	 */
	public void setNameEn(String nameEn) {
		node.setNameEn(nameEn);
	}

}
