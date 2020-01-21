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
package at.freebim.db.json;

/**
 * This enum list the name of the fields. These names are used when serializing
 * to json.
 *
 * @author rainer.breuss@uibk.ac.at
 */
public enum NodeFields {

	APP_VERSION("appVersion"), // appVersion field
	BSDD_GUID("b"), // bsddGuid field
	LOADED_BSDD("bsdd"), // Bsdd relations
	CLASS_NAME("c"), // class name
	CONTRIBUTED_BY("cb"), // ContributedBy relations
	COMPANY_COMPANY("cc"), // CompanyCompany relations
	CODE("cd"), // code field
	CHILDS("ch"), // ChildOf relations
	CONTRIBUTOR_ID("ci"), // contributorId field
	COMMENT("co"), // comment field
	PARENTS("cp"), // ChildOf parents relations
	UNIT_CONVERSION("cv"), // UnitConversion relations
	COMPANY("cy"), // company field
	DESC("d"), // desc field
	DESC_EN("de"), // descEn field
	DISCIPLINE("di"), // OfDiscipline relations
	DOCUMENTED("do"), // DocumentedIn relations
	DEFAULT("ds"), // defaultString field
	DATATYPE("dt"), // OfDataType relations
	HAS_ENTRY("e"), // HasEntry relations
	EMAIL("em"), // email field
	EQUALS("eq"), // Equals relations
	FIRSTNAME("fn"), // firstName field
	HEX_COLOR("hc"), // hexColor field
	HP("hp"), // incoming HasParameter relations of Parameter
	HAS_PARAMETER("hp"), // outgoing HasParameter relations of Component
	NODEID("i"), // nodeId
	LANGUAGE("lg"), // lang field of Library
	LISTS("li"), // HasEntry relations
	LASTNAME("ln"), // lastName field
	LOGO("logo"), // logo field of Company
	LEVEL("lv"), // level field
	IS_MATERIAL("m"), // material, or not material
	MATERIAL("ma"), // OfMaterial relations
	MEASURES("me"), // HasMeasure relations; HasValue relations; Measure in OfDataType relations;
					// Measure in OfUnit relations
	NAME("n"), // name field
	NAME_EN("ne"), // nameEn field
	NODES("no"), // nodes of Bsdd relations; nodes in DocumentdIn relations
	OVERRIDE("o"), // ValueOverride relations
	OWNERS("ow"), // HasParameterSet relations
	PARAMETER("p"), // parameters in OfDiscipline relations
	PARTS("parts"), // ComponentComponent relations
	PASSWORD("pwd"), // password field
	PREFIX("pr"), // prefix field
	PARAMS("ps"), // HasMeasure relations; ContainsParameter relations
	PSETS("psets"), // HasParameterSet relations
	PARAM_TYPE("pt"), // ParameterType field
	REFERENCES("r"), // References relations
	ROLES("ro"), // Role and RoleContributor array
	RESPONSIBLE("rs"), // Responsible relations
	REGEXPR("rx"), // regExp field
	STATE("s"), // state field
	SHOW_FROM("sF"), // MessageNode
	STATUS_COMMENT("sc"), // status comment field
	SHOW_UNTIL("sU"), // MessageNode
	TITLE("title"), // title field of Contributor
	TIMESTAMP("ts"), // time stamp field
	TYPE("ty"), // type field for ComponentType, ParameterSetType
	FREEBIM_ID("u"), // uuid field
	USERNAME("un"), // username field
	UNIT("unit"), // OfUnit relations
	URL("url"), // url field
	HAS_VALUE("v"), // HasValue relations
	VALID_FROM("vF"), // valid from field
	VALID_TO("vT"), // valid to field
	WORKS_FOR("wf"), // WorksFor relations of Company
	;

	private final String serial;

	private NodeFields(String ser) {
		this.serial = ser;
	}

	public String getSerial() {
		return this.serial;
	}

}
