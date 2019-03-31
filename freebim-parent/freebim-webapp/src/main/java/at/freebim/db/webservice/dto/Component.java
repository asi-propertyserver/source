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
 * DTO of a {@link at.freebim.db.domain.Component}.
 * The class extends {@link StatusBase}.
 * 
 * @see at.freebim.db.domain.Component
 * @see at.freebim.db.webservice.dto.StatusBase
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class Component extends StatusBase<at.freebim.db.domain.Component> {
	
	private String libraryId;
	
	/**
	 * Creates a new instance.
	 * @param node The original node.
	 * @param libraryId The freeBIM-ID of the referenced {@link Library}.
	 * @param dtoHelper The helper.
	 */
	public Component(at.freebim.db.domain.Component node, String libraryId, DtoHelper dtoHelper) {
		super(node, dtoHelper);
		this.libraryId = libraryId;
	}

	/**
	 * Get the code.
	 * @return The code.
	 */
	public String getCode() {
		return this.dtoHelper.getString(this.node.getCode());
	}

	/**
	 * Set the code.
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.node.setCode(code);
	}

	/**
	 * Get the local name.
	 * @return The name.
	 */
	public String getName() {
		return this.dtoHelper.getString(this.node.getName());
	}

	/** 
	 * Set the local name.
	 * @param name The local name to set.
	 */
	public void setName(String name) {
		this.node.setName(name);
	}

	/**
	 * Get the name in international English.
	 * @return the name in international English.
	 */
	public String getNameEn() {
		return this.dtoHelper.getString(this.node.getNameEn());
	}
	
	/**
	 * Set the name in international English.
	 * @param nameEn The name to set (in international English).
	 */
	public void setNameEn(String nameEn) {
		this.node.setNameEn(nameEn);
	}

	/**
	 * Get the local description.
	 * @return The local description.
	 */
	public String getDesc() {
		return this.dtoHelper.getString(this.node.getDesc());
	}

	/**
	 * Set the local description.
	 * @param desc The local description to set.
	 */
	public void setDesc(String desc) {
		this.node.setDesc(desc);
	}

	/**
	 * Get the description in international English.
	 * @return The description in international English.
	 */
	public String getDescEn() {
		return this.dtoHelper.getString(this.node.getDescEn());
	}
	
	/**
	 * Set the description in international English.
	 * @param descEn The description to set (in international English).
	 */
	public void setDescEn(String descEn) {
		this.node.setDescEn(descEn);
	}

	/**
	 * Get the bsDD-Guid.
	 * @return The bsDD-Guid.
	 */
	public String getBsddGuid() {
		return this.dtoHelper.getString(this.node.getBsddGuid());
	}

	/**
	 * Set the bsDD-Guid.
	 * @param guid The bsDD-Guid to set.
	 */
	public void setBsddGuid(String guid) {
		this.node.setBsddGuid(guid);
	}

	/**
	 * Get the freeBIM-ID of the referenced {@link Library}.
	 * @return the libraryId
	 */
	public String getLibraryId() {
		return libraryId;
	}

	/**
	 * Set the freeBIM-ID of the referenced {@link Library}.
	 * @param libraryId the libraryId to set
	 */
	public void setLibraryId(String libraryId) {
		this.libraryId = libraryId;
	}

}
