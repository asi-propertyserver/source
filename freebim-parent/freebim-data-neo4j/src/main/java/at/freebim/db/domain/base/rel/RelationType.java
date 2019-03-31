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


/**
 * Definition of all used relations.<br>
 * This interface MUST correspond to {@link RelationTypeEnum}
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface RelationType {

	public static final String BELONGS_TO = "BELONGS_TO";
	public static final String BSDD = "BSDD";
	public static final String CHILD_OF = "CHILD_OF";
	public static final String COMP_COMP = "COMP_COMP";
	public static final String COMPANY_COMPANY = "COMPANY_COMPANY";
	public static final String CONTAINS_PARAMETER = "CONTAINS_PARAMETER";
	public static final String CONTRIBUTED_BY = "CONTRIBUTED_BY";
	public static final String DOCUMENTED_IN = "DOCUMENTED_IN";
	public static final String EQUALS = "EQUALS";
	public static final String HAS_ENTRY = "HAS_ENTRY";
	public static final String HAS_MEASURE = "HAS_MEASURE";
	public static final String HAS_PARAMETER = "HAS_PARAMETER";
	public static final String HAS_PARAMETER_SET = "HAS_PARAMETER_SET";
	public static final String HAS_VALUE = "HAS_VALUE";
	public static final String NGRAM_CODE_OF = "NGRAM_CODE_OF";
	public static final String NGRAM_DESC_OF = "NGRAM_DESC_OF";
	public static final String NGRAM_NAME_OF = "NGRAM_NAME_OF";
	public static final String OF_DATATYPE = "OF_DATATYPE";
	public static final String OF_DISCIPLINE = "OF_DISCIPLINE";
	public static final String OF_MATERIAL = "OF_MATERIAL";
	public static final String OF_PARAMETER = "OF_PARAMETER";
	public static final String OF_PHASE = "OF_PHASE";
	public static final String OF_UNIT = "OF_UNIT";
	public static final String PARENT_OF = "PARENT_OF";
	public static final String REFERENCES = "REFERENCES";
	public static final String RESPONSIBLE = "RESPONSIBLE";
	public static final String UNIT_CONVERSION = "UNIT_CONVERSION";
	public static final String VALUE_OVERRIDE = "VALUE_OVERRIDE";
	public static final String WORKS_FOR = "WORKS_FOR";
	public static final String MESSAGE_SEEN = "MESSAGE_SEEN";
	public static final String MESSAGE_CLOSED = "MESSAGE_CLOSED";
}
