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
package at.freebim.db.domain.base.rel;

import org.neo4j.graphdb.RelationshipType;

/**
 * Enumeration of all used relations.<br>
 * This enumeration MUST correspond to {@link RelationType}
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public enum RelationTypeEnum implements RelationshipType  {

	BELONGS_TO 			(16, 	"RT_BELONGS_TO"),
	BSDD 				(50, 	"RT_BSDD"),
	CHILD_OF 			(4, 	"RT_CHILD_OF"),
	COMP_COMP 			(17, 	"RT_COMP_COMP"),
	COMPANY_COMPANY		(25,	"RT_COMPANY_COMPANY"),
	CONTAINS_PARAMETER	(18, 	"RT_CONTAINS_PARAMETER"),
	CONTRIBUTED_BY 		(1, 	"RT_CONTRIBUTED_BY"),
	DOCUMENTED_IN 		(6, 	"RT_DOCUMENTED_IN"), 
	EQUALS 				(9, 	"RT_EQUALS"),
	HAS_ENTRY 			(7, 	"RT_HAS_ENTRY"),
	HAS_MEASURE 		(5, 	"RT_HAS_MEASURE"),
	HAS_PARAMETER 		(2, 	"RT_HAS_PARAMETER"),
	HAS_PARAMETER_SET 	(19, 	"RT_HAS_PARAMETER_SET"),
	HAS_VALUE 			(8, 	"RT_HAS_VALUE"),
	NGRAM_CODE_OF 		(97, 	"RT_NGRAM_CODE_OF"),
	NGRAM_DESC_OF 		(98, 	"RT_NGRAM_DESC_OF"),
	NGRAM_NAME_OF 		(99, 	"RT_NGRAM_NAME_OF"),
	OF_DATATYPE 		(11, 	"RT_OF_DATATYPE"),
	OF_DISCIPLINE 		(12, 	"RT_OF_DISCIPLINE"),
	OF_MATERIAL 		(13, 	"RT_OF_MATERIAL"),
	OF_UNIT 			(10, 	"RT_OF_UNIT"),
	PARENT_OF 			(23, 	"RT_PARENT_OF"),
	REFERENCES 			(3, 	"RT_REFERENCES"), 
	RESPONSIBLE  		(20, 	"RT_RESPONSIBLE"), 
	UNIT_CONVERSION		(22, 	"RT_UNIT_CONVERSION"),
	VALUE_OVERRIDE		(21, 	"RT_VALUE_OVERRIDE"),
	WORKS_FOR			(24,	"RT_WORKS_FOR"),
	MESSAGE_SEEN		(51,	"RT_MESSAGE_SEEN"), 
	MESSAGE_CLOSED		(52,	"RT_MESSAGE_CLOSED")
	;

	/**
	 * The code.
	 */
	private final int code;
	
	/**
	 * The user interface name.
	 */
	private final String uiName;
	
	/**
	 * Set the code and user interface name.
	 * 
	 * @param code the code to set
	 * @param uiName the user interface name to set
	 */
	private RelationTypeEnum(int code, String uiName) {
		this.code = code;
		this.uiName = uiName;
	}

	/**
	 * Get the user interface name.
	 * 
	 * @return the uiName
	 */
	public String getUiName() {
		return uiName;
	}

	/**
	 * Get the code.
	 * 
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * Turns an integer in an enum.
	 * 
	 * @param code the enum code
	 * @return the enum
	 * */
	public static RelationTypeEnum fromCode(int code) {
		switch (code) {
		default:
		case 1 : return CONTRIBUTED_BY;
		case 2 : return HAS_PARAMETER;
		case 3 : return REFERENCES; 
		case 5 : return HAS_MEASURE;
		case 6 : return DOCUMENTED_IN; 
		case 7 : return HAS_ENTRY;
		case 8 : return HAS_VALUE;
		case 9 : return EQUALS;
		case 10 : return OF_UNIT;
		case 11 : return OF_DATATYPE;
		case 12 : return OF_DISCIPLINE;
		case 13 : return OF_MATERIAL;
		case 16 : return BELONGS_TO;
		case 17 : return COMP_COMP;
		case 18 : return CONTAINS_PARAMETER;
		case 19 : return HAS_PARAMETER_SET;
		case 20 : return RESPONSIBLE;
		case 21 : return VALUE_OVERRIDE;
		case 22 : return UNIT_CONVERSION;
		case 23 : return PARENT_OF;
		case 24 : return WORKS_FOR;
		case 25 : return COMPANY_COMPANY;
		
		case 50 : return BSDD;
		case 51 : return MESSAGE_SEEN;
		case 52 : return MESSAGE_CLOSED;
		
		case 97 : return NGRAM_CODE_OF;
		case 98 : return NGRAM_DESC_OF;
		case 99 : return NGRAM_NAME_OF;
		}
	}
}
