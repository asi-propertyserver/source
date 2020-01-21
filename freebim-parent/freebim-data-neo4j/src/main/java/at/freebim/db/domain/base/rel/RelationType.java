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
 */
public interface RelationType {

	String BELONGS_TO = "BELONGS_TO";
	String BSDD = "BSDD";
	String CHILD_OF = "CHILD_OF";
	String COMP_COMP = "COMP_COMP";
	String COMPANY_COMPANY = "COMPANY_COMPANY";
	String CONTAINS_PARAMETER = "CONTAINS_PARAMETER";
	String CONTRIBUTED_BY = "CONTRIBUTED_BY";
	String DOCUMENTED_IN = "DOCUMENTED_IN";
	String EQUALS = "EQUALS";
	String HAS_ENTRY = "HAS_ENTRY";
	String HAS_MEASURE = "HAS_MEASURE";
	String HAS_PARAMETER = "HAS_PARAMETER";
	String HAS_PARAMETER_SET = "HAS_PARAMETER_SET";
	String HAS_VALUE = "HAS_VALUE";
	String NGRAM_CODE_OF = "NGRAM_CODE_OF";
	String NGRAM_DESC_OF = "NGRAM_DESC_OF";
	String NGRAM_NAME_OF = "NGRAM_NAME_OF";
	String OF_DATATYPE = "OF_DATATYPE";
	String OF_DISCIPLINE = "OF_DISCIPLINE";
	String OF_MATERIAL = "OF_MATERIAL";
	String OF_PARAMETER = "OF_PARAMETER";
	String OF_PHASE = "OF_PHASE";
	String OF_UNIT = "OF_UNIT";
	String PARENT_OF = "PARENT_OF";
	String REFERENCES = "REFERENCES";
	String RESPONSIBLE = "RESPONSIBLE";
	String UNIT_CONVERSION = "UNIT_CONVERSION";
	String VALUE_OVERRIDE = "VALUE_OVERRIDE";
	String WORKS_FOR = "WORKS_FOR";
	String MESSAGE_SEEN = "MESSAGE_SEEN";
	String MESSAGE_CLOSED = "MESSAGE_CLOSED";
}
