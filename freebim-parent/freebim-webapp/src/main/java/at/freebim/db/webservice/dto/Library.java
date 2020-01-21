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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.freebim.db.webservice.DtoHelper;

/**
 * DTO of a {@link at.freebim.db.domain.Library}. The class extends
 * {@link StatusBase}.
 * 
 * @see at.freebim.db.domain.Library
 * @see at.freebim.db.webservice.dto.StatusBase
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class Library extends StatusBase<at.freebim.db.domain.Library> {

	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(Library.class);

	private List<LibraryReference> entries;
	private String refIdName;

	/**
	 * Creates a new instance.
	 * 
	 * @param library   The original node.
	 * @param dtoHelper The helper.
	 */
	public Library(at.freebim.db.domain.Library library, DtoHelper dtoHelper) {
		super(library, dtoHelper);
		this.refIdName = null;
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param library   The original node.
	 * @param refIdName Column name in the original referenced database.
	 * @param dtoHelper The helper.
	 */
	public Library(at.freebim.db.domain.Library library, String refIdName, DtoHelper dtoHelper) {
		this(library, dtoHelper);
		this.refIdName = refIdName;
	}

	/**
	 * Get all objects having a reference to the original database determined by
	 * <code>refIdName</code> column name.
	 * 
	 * @return A list of all references.
	 */
	@XmlElement
	public List<LibraryReference> getReferences() {
		return entries;
	}

	/**
	 * Set referencing objects.
	 * 
	 * @param entries the entries to set
	 */
	public void setReferences(List<LibraryReference> entries) {
		this.entries = entries;
	}

	/**
	 * Get the column name of the original database.
	 * 
	 * @return the refIdName
	 */
	@XmlElement
	public String getRefIdName() {
		return this.dtoHelper.getString(refIdName);
	}

	/**
	 * Set the column name of the original database.
	 * 
	 * @param refIdName the refIdName to set
	 */
	public void setRefIdName(String refIdName) {
		this.refIdName = refIdName;
	}

	/**
	 * Get the name of the Library.
	 * 
	 * @return The Library name.
	 */
	@XmlElement
	public String getName() {
		return this.dtoHelper.getString(this.node.getName());
	}

	/**
	 * Set the name of the Library.
	 * 
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.node.setName(name);
	}

	/**
	 * Get the description.
	 * 
	 * @return The description.
	 */
	@XmlElement
	public String getDesc() {
		return this.dtoHelper.getString(this.node.getDesc());
	}

	/**
	 * Set the description.
	 * 
	 * @param desc The description to set.
	 */
	public void setDesc(String desc) {
		this.node.setDesc(desc);
	}

	/**
	 * Get the last update time stamp.
	 * 
	 * @return The time stamp in ISO-8601 format.
	 */
	@XmlElement
	public String getLastUpdate() {
		Long ts = this.node.getTs();
		if (ts != null) {
			try {
				TimeZone tz = TimeZone.getTimeZone("UTC");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				df.setTimeZone(tz);
				String asISO = df.format(ts);
				return asISO;
			} catch (Exception e) {
				logger.error("Error formatting last modified timestamp:", e);
			}
		}
		return null;
	}

	/**
	 * Get the URL of the Library.
	 * 
	 * @return The URL.
	 */
	@XmlElement
	public String getURL() {
		return this.dtoHelper.getString(this.node.getUrl());
	}

	/**
	 * Set the URL of the Library.
	 * 
	 * @param url The URL to set.
	 */
	public void setURL(String url) {
		this.node.setUrl(url);
	}

	/**
	 * Get the language code.
	 * 
	 * @return The language code, i.e. 'de-AT'.
	 */
	@XmlElement
	public String getLanguageCode() {
		return this.dtoHelper.getString(this.node.getLanguageCode());
	}

	/**
	 * Set the language code.
	 * 
	 * @param lc The language code to set, i.e. 'de-AT'.
	 */
	public void setLanguageCode(String lc) {
		this.node.setLanguageCode(lc);
	}

}
