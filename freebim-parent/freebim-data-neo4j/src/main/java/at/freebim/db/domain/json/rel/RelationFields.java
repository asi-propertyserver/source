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
package at.freebim.db.domain.json.rel;

/**
 * Enumeration of field names used to serialize our relations.
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public enum RelationFields {

	FROM_NODE 			("a"),		// ID of FROM node
	TO_NODE				("b"),		// ID of TO node
	CLASS_NAME			("c"),		// class name
	CONTRIBUTION_TYPE	("ct"), 	// contribution type
	COMPONENT			("cu"),		// componentUuid
	DEFAULT_VALUE		("d"),		// default value
	DATA				("data"),	// client side only
	DIRECTION			("dir"),	// direction
	FROM_CLASS			("fc"),		// from class name
	FIELD_NAME			("field"),	// client side only
	BSDD_GUID			("g"),		// bsddGuid
	INFO				("i"),		// info
	ID					("id"),		// ID of relation
	INVALID				("iv"),		// invalid
	MEASURE				("m"),		// freebim-ID of measure in value override relations
	ORDERING			("o"),		// ordering
	PHASE				("p"), 		// phaseUuid
	REF_ID				("ri"),		// refId
	REF_ID_NAME			("rn"), 	// refIdName
	RELATIONS			("relations"), // client side only
	QUALITY				("q"),		// quality 
	TYPE				("t"),		// type of relation
	TO_CLASS			("tc"),		// to class name
	TIMESTAMP			("ts"),		// time stamp
	VALUE				("v"),		// value of overrides relation
	VALID_FROM			("vF"),		// valid from
	VALID_TO			("vT")		// valid to
	;
	
	private final String serial;
	
	/**
	 * Set the serial.
	 * 
	 * @param ser the serial 
	 * */
	private RelationFields(String ser) {
		this.serial = ser;
	}
	
	/**
	 * Get the serial.
	 * 
	 * @return the serial
	 * */
	public String getSerial() {
		return this.serial;
	}
	
	
}
