/**
 * 
 */
var ifd = ifd || {};

/**
 *  The list of available concept types in bsDD. Any concept added to bsDD should fit in one and only one of types.
 */
ifd.IfdConceptTypeEnum = {
	/**
	 * Actors are persons, professions, organizations. Actors are a roles
	 * external to the system, and can be humans, groups of humans, machines, or
	 * devices. As bsDD only speaks about types of things "Picasso" is not an
	 * actor concept in bsDD while "painter" is. <br>
	 * <br>
	 */
	ACTOR : "ACTOR",

	/**
	 * Examples are any specific behavior, actions, organic human or machine
	 * processes, chemical reactions and the action of natural forces <br>
	 * <br>
	 */
	ACTIVITY : "ACTIVITY",

	/**
	 * Anything serving as a representation of a person's thinking by means of
	 * symbolic marks. A document is a writing that contains information. Any
	 * item (not necessarily on paper) that can be indexed or cataloged.
	 * Examples of documents are standards, books, etc. Please note that
	 * classifications have it's own type in bsDD <br>
	 * <br>
	 */
	DOCUMENT : "DOCUMENT",

	/**
	 * Properties or a property refers to the intrinsic or extrinsic qualities
	 * of objects. It can be any characteristic or attribute of an object or
	 * substance. A property will always have a measure in which the quantity
	 * can be expressed. A property is used to describe the quantity of another
	 * concept. <br>
	 * <br>
	 */
	PROPERTY : "PROPERTY",

	/**
	 * A subject in bsDD is any physical or logical thing. Examples of subjects
	 * are doors, windows roads, airports, software, control systems and lobby.
	 * A subject can be composed of other subjects and will typically be
	 * described by its properties. <br>
	 * <br>
	 */
	SUBJECT : "SUBJECT",

	/**
	 * A unit of measurement: any division of quantity accepted as a standard of
	 * measurement or exchange. Units can be monetary units, SI units, derived
	 * units, and concersion based units. Class is also a unit. A unit is a
	 * physical quantity, with a value of one, which is used as a standard in
	 * terms of which other quantities are expressed. <br>
	 * <br>
	 */
	UNIT : "UNIT",

	/**
	 * A measure is used to determine the extent or quantity of a concept. It
	 * can be a specific extent or quantity of a substance, or a graduated scale
	 * by which the dimensions or mass of an object or substance may be
	 * determined. A measure in bsDD can have multiple values but only one unit.
	 * Measures are named by including the unit in their name. Eg. positive
	 * length measure in mm <br>
	 * <br>
	 */
	MEASURE : "MEASURE",

	/**
	 * Values in bsDD are normally given where there exist typical values for a
	 * given property. Examples are fire resistance classes, wood qualities,
	 * standard widths and heights for certain concepts. Values are language
	 * dependent and could be strings as well as numbers. <br>
	 * <br>
	 */
	VALUE : "VALUE",

	/**
	 * A nest is a collection that only allows things of the same type to be
	 * members of the collection. A nest can be collection of all other types of
	 * concepts but each nest can only contain concepts of one particular type.
	 * A nest can also be a collection of other nests or bags. <br>
	 * <br>
	 */
	NEST : "NEST",

	/**
	 * A bag is a collection that allows things of different types to be members
	 * of the collection. A bag can be mixed collection of all other types of
	 * concepts also other nests and bags. <br>
	 * <br>
	 */
	BAG : "BAG",

	/**
	 * Classifications are particular documents containing classification codes
	 * and structures. A classification in bsDD is used to classify concepts of
	 * any of the other concept types. The classification code is stored in the
	 * short-name of the classification concept, while the actual name of the
	 * classification is stored in full-name. Classifications are modeled as
	 * structures by means of composition. <br>
	 * <br>
	 */
	CLASSIFICATION : "CLASSIFICATION",

	/**
	 * This should never be used. Its provided for debug purposes.
	 */
	UNDEFINED : "UNDEFINED"

};

/**
 *  The Enum IfdDescriptionTypeEnum.
 */
ifd.IfdDescriptionTypeEnum = {

	/**
	 * A description of type definition. Represents a definition of a concept.
	 */
	DEFINITION : "DEFINITION",

	/**
	 * A description of type comment. Represents a comment on a concept.
	 */
	COMMENT : "COMMENT",

	/**
	 * The UNDEFINED.
	 */
	UNDEFINED : "UNDEFINED"
};

/**
 *  The Enum IfdNameTypeEnum.
 */
ifd.IfdNameTypeEnum = {

	/**
	 * A full name of a concept. The name without abbreviations.
	 */
	FULLNAME : "FULLNAME",

	/**
	 * A short, or abbreviated, version of a name of a concept. E.g "mm" is a
	 * short name for "millimeter".
	 */
	SHORTNAME : "SHORTNAME",

	/**
	 * The possible lexeme of a concept. This represents the stem of a word.
	 */
	LEXEME : "LEXEME",

	/**
	 * The UNDEFINED.
	 */
	UNDEFINED : "UNDEFINED"
};

/**
 *  The Enum IfdRelationshipTypeEnum.
 */
ifd.IfdRelationshipTypeEnum = {

	/**
	 * A relationship to collect any concepts type into two types of
	 * collections; NEST or BAG. When used in addChildren the concept must be of
	 * type NEST or BAG while the children can be any of the available concept
	 * types. When used in addParents the concept can be any type while the
	 * parents must be of type NEST or BAG.
	 */
	COLLECTS : "COLLECTS",

	/**
	 * A relationship to assign a collection of type NEST or BAG to any other
	 * concept. When used in addChildren the concept can be any type while the
	 * parents must be of type NEST or BAG. When used in addParents the concept
	 * must be of type NEST or BAG while the children can be any of the
	 * available concept types.
	 */
	ASSIGNS_COLLECTIONS : "ASSIGNS_COLLECTIONS",

	/**
	 * The ASSOCIATES.
	 */
	ASSOCIATES : "ASSOCIATES",

	/**
	 * Composes tells that something is a part of something else. Normally the
	 * parts (children) are of the same type as the whole (parent) but this is
	 * not enforced by the API.
	 */
	COMPOSES : "COMPOSES",

	/**
	 * The GROUPS.
	 */
	GROUPS : "GROUPS",

	/**
	 * Specializes tells that something is a type of something else.
	 * Specialization does not enforce inheritance of properties as often the
	 * case. Each contributor is free to add properties on subtypes as she like.
	 * On concept can only be a specialization of another concept of the same
	 * type. A concept can however be a specialization of many concepts.
	 */
	SPECIALIZES : "SPECIALIZES",

	/**
	 * The ACT s_ upon.
	 */
	ACTS_UPON : "ACTS_UPON",

	/**
	 * The SEQUENCES.
	 */
	SEQUENCES : "SEQUENCES",

	/**
	 * The DOCUMENTS.
	 */
	DOCUMENTS : "DOCUMENTS",

	/**
	 * The CLASSIFIES.
	 */
	CLASSIFIES : "CLASSIFIES",

	/**
	 * The ASSIGN s_ measures.
	 */
	ASSIGNS_MEASURES : "ASSIGNS_MEASURES",

	/**
	 * The ASSIGN s_ properties.
	 */
	ASSIGNS_PROPERTIES : "ASSIGNS_PROPERTIES",

	/**
	 * The ASSIGN s_ units.
	 */
	ASSIGNS_UNITS : "ASSIGNS_UNITS",

	/**
	 * The ASSIGN s_ values.
	 */
	ASSIGNS_VALUES : "ASSIGNS_VALUES",

	/**
	 * The ASSIGN s_ propert y_ wit h_ values.
	 */
	ASSIGNS_PROPERTY_WITH_VALUES : "ASSIGNS_PROPERTY_WITH_VALUES",

	/**
	 * The UNDEFINED.
	 */
	UNDEFINED : "UNDEFINED"
};

/**
 *  The list of statuses in bsDD.
 */
ifd.IfdStatusEnum = {

  /**
   *  For newly added concepts that are not checked by the responsible organisation. <br>
   *  <br>
   */
  DRAFT : "DRAFT",

  /**
   *  Checked by the responsible organisation <br>
   *  <br>
   */
  CHECKED : "CHECKED",

  /**
   *  The concept is approved by the responsible organisation <br>
   *  <br>
   */
  APPROVED : "APPROVED",

  /**
   *  The concept is regarded as no longer a part of the library. The API will return the object but with the status invalid. <br>
   *  <br>
   */
  INVALID : "INVALID",

  /**
   *  The concept is merged with another concept and the guid will no longer be returned by the API. Instead the guid of the concept
   *  replacing this concept is returned. The replacement guid will be stored in the VersionId of the transferred concept. <br>
   *  <br>
   */
  TRANSFERRED : "TRANSFERRED"
};


/**
 *  The Enum IfdUserRoleTypeEnum.
 */
ifd.IfdUserRoleTypeEnum = {

	/**
	 * An administrator has full rights in the system.
	 */
	IFD_ADMINISTRATOR : "IFD_ADMINISTRATOR",

	/**
	 * An editor can typically edit all the content in the system, but not
	 * content affecting contexts the user do not have edit access to.
	 */
	IFD_EDITOR : "IFD_EDITOR",

	/**
	 * A user with read and comment rights, can read all content (except
	 * contexts he has no access to) and add comments to concepts.
	 */
	IFD_READ_AND_COMMENT : "IFD_READ_AND_COMMENT",

	/**
	 * A user with read only access can read all content (except contexts he has
	 * no access to).
	 */
	IFD_READ_ONLY : "IFD_READ_ONLY",

	/**
	 * The unknown.
	 */
	PUBLIC : "PUBLIC",

	/**
	 * An inactive user has no access at all.
	 */
	INACTIVE : "INACTIVE"
};

/**
 *  The Enum IfdValueRoleEnum.
 */
ifd.IfdValueRoleEnum = {

	/**
	 * The enumeration.
	 */
	ENUMERATION : "ENUMERATION",

	/**
	 * The nominal.
	 */
	NOMINAL : "NOMINAL",

	/**
	 * The minimum.
	 */
	MINIMUM : "MINIMUM",

	/**
	 * The maximum.
	 */
	MAXIMUM : "MAXIMUM",

	/**
	 * The lowertolerance.
	 */
	LOWERTOLERANCE : "LOWERTOLERANCE",

	/**
	 * The uppertolerance.
	 */
	UPPERTOLERANCE : "UPPERTOLERANCE",

	/**
	 * The defining.
	 */
	DEFINING : "DEFINING",

	/**
	 * The defined.
	 */
	DEFINED : "DEFINED"
};

/**
 *  The Class IfdBase is the basic for all objects, and keeps the GUID.
 */
ifd.IfdBase = function () {
	this._n = "IfdBase";
	Object.getPrototypeOf(ifd.IfdBase.prototype).constructor.call(this);
	this.guid = ""; // String
};
ifd.IfdBase.prototype.constructor = ifd.IfdBase;
ifd.IfdBase.prototype.init = function (v) {
	this.guid = v.guid;
};
ifd.IfdBase.prototype.toInfoBox = function (box, v) {
	if (v) {
		var div = document.createElement("div");
		jq(div).addClass("bsdd-IfdBase");
		jq(box).append(div);
		jq(div).html("<span class=\"GUID\">GUID: <input style=\"width:250px;\" type=\"text\" readonly=\"true\" value=\"" + v.guid + "\"></span>");
	}
};

/**
 * (no documentation provided)
 */
ifd.IfdCursor = function () {
	this._n = "IfdCursor";
	Object.getPrototypeOf(ifd.IfdCursor.prototype).constructor.call(this);
	this.page = null; // String
	this.previousPage = null; // String
	this.nextPage = null; // String
	this.pageSize = null; // int
};
ifd.IfdCursor.prototype.constructor = ifd.IfdCursor;

/**
 *  The Class IfdError.
 */
ifd.IfdError = function () {
	this._n = "IfdError";
	Object.getPrototypeOf(ifd.IfdError.prototype).constructor.call(this);
	this.code = null; // int
	this.message = null; // String
};
ifd.IfdError.prototype.constructor = ifd.IfdError;
ifd.IfdError.prototype.toInfoBox = function (box, v) {
	if (v) {
		var div = document.createElement("div");
		jq(div).addClass("bsdd-IfdError");
		jq(box).append(div);
		jq(div).html("ERROR " + v.code + ": '" + v.message + "'");
	}
};

/**
 * (no documentation provided)
 */
ifd.IfdPageList = function () {
	this._n = "IfdPageList";
	Object.getPrototypeOf(ifd.IfdPageList.prototype).constructor.call(this);
	this.list = null; // Object[]
	this.cursor = null; // IfdCursor
};
ifd.IfdPageList.prototype.constructor = ifd.IfdPageList;

/**
 *  The Class IfdSimpleName.
 */
ifd.IfdSimpleName = function () {
	this._n = "IfdSimpleName";
	Object.getPrototypeOf(ifd.IfdSimpleName.prototype).constructor.call(this);
	this.name = null; // String
	this.nameType = null; // IfdNameTypeEnum
};
ifd.IfdSimpleName.prototype.constructor = ifd.IfdSimpleName;
ifd.IfdSimpleName.prototype.toInfoBox = function (box, v) {
	if (v) {
		var div = document.createElement("div");
		jq(div).addClass("bsdd-IfdSimpleName");
		jq(box).append(div);
		jq(div).html(v.name).attr("title", v.nameType);
	}
};

/**
 * (no documentation provided)
 */
ifd.IfdStatistics = function () {
	this._n = "IfdStatistics";
	Object.getPrototypeOf(ifd.IfdStatistics.prototype).constructor.call(this);
	this.count = null; // long
	this.objectName = null; // String
};
ifd.IfdStatistics.prototype.constructor = ifd.IfdStatistics;  
ifd.IfdStatistics.prototype.toInfoBox = function (box, v) {
	if (v) {
		var div = document.createElement("div");
		jq(div).addClass("bsdd-IfdStatistics");
		jq(box).append(div);
		jq(div).html(v.objectName + ": " + v.count);
	}
};

/**
 * The Class IfdAPISession maintains state about each session.
 * @augments IfdBase
 */
ifd.IfdAPISession = function () {
	this._n = "IfdAPISession";
	Object.getPrototypeOf(ifd.IfdAPISession.prototype).constructor.call(this);
	this.user = null; // IfdUser
	this.timestamp = null; // Date
	this.invalid = null; // boolean
};
ifd.IfdAPISession.prototype = Object.create(ifd.IfdBase.prototype);
ifd.IfdAPISession.prototype.constructor = ifd.IfdAPISession;
ifd.IfdAPISession.prototype.toInfoBox = function (box, v) {
	if (v) {
		var div = document.createElement("div"), i18n = net.spectroom.js.i18n;
		jq(div).addClass("bsdd-IfdAPISession");
		jq(box).append(div);
		jq(div).html("<h5>Session</h5>" + i18n.g("IFD_VALID") + ": " + ((v.invalid) ? i18n.g("IFD_VALID_NO") : i18n.g("IFD_VALID_YES")) + ", " + i18n.g("IFD_TIMESTAMP") + ": " + ((v.timestamp) ? v.timestamp : ""));
		ifd.IfdUser.prototype.toInfoBox.call(this, div, v.user);
	}
};

/**
 * The Class IfdConBase.
 * @augments IfdBase
 */
ifd.IfdConBase = function () {
	this._n = "IfdConBase";
	Object.getPrototypeOf(ifd.IfdConBase.prototype).constructor.call(this);
	this.versionId = null; // String
	this.versionDate = null; // String
	this.status = null; // IfdStatusEnum
	this.fullNames = null; // IfdName[]
	this.definitions = null; // IfdDescription[]
	this.comments = null; // IfdDescription[]
};
ifd.IfdConBase.prototype = Object.create(ifd.IfdBase.prototype);
ifd.IfdConBase.prototype.constructor = ifd.IfdConBase;
ifd.IfdConBase.prototype.init = function (v) {
	ifd.IfdBase.prototype.init.call(this, v);
	this.versionId = v.versionId; // String
	this.versionDate = v.versionDate; // String
	this.status = v.status; // IfdStatusEnum
	this.fullNames = v.fullNames; // IfdName[]
	this.definitions = v.definitions; // IfdDescription[]
	this.comments = v.comments; // IfdDescription[]
};
ifd.IfdConBase.prototype.toInfoBox = function (box, v) {
	var div, id, table, tbody, i18n = net.spectroom.js.i18n;
	ifd.IfdBase.prototype.toInfoBox.call(this, box, v);
	if (v.versionId != undefined || v.versionDate) {
		div = document.createElement("div");
		jq(div).addClass("bsdd-version");
		jq(box).append(div);
		jq(div).html(i18n.g("IFD_VERSION") + ": " + ((v.versionId != undefined) ? v.versionId + " " : "") + ((v.versionDate) ? i18n.g("IFD_DATE") + ": " + v.versionDate : ""));
	}
	if (v.status != undefined) {
		div = document.createElement("div");
		jq(div).addClass("bsdd-status");
		jq(box).append(div);
		jq(div).html("Status: " + v.status);
	}
	if (v.fullNames) {
		id = "_" + Math.random();
		id = id.replace(".", "");
		div = document.createElement("div");
		jq(div).addClass("bsdd-fullNames");
		jq(box).append(div);
		table = document.createElement("table");
		jq(table).addClass("bsdd").append("<thead><tr><th class='ifd-lang'>" + i18n.g("IFD_LANGUAGE") + "</th><th>" + i18n.g("IFD_DESCRIPTION") + "</th></tr></thead>").appendTo(div);
		tbody = document.createElement("tbody");
		jq(tbody).appendTo(table);
		net.spectroom.js.forAll(v.fullNames, function (fn) {
			ifd.IfdName.prototype.asTableRow.call(this, fn, jq(tbody));
		});
	}
	if (v.definitions) {
		id = "_" + Math.random();
		id = id.replace(".", "");
		div = document.createElement("div");
		jq(div).addClass("bsdd-definitions");
		jq(box).append(div);
		table = document.createElement("table");
		jq(table).addClass("bsdd").append("<thead><tr><th class='ifd-lang'>" + i18n.g("IFD_LANGUAGE") + "</th><th>" + i18n.g("IFD_DEFINITION") + "</th></tr></thead>").appendTo(div);
		tbody = document.createElement("tbody");
		jq(tbody).appendTo(table);
		net.spectroom.js.forAll(v.definitions, function (def) {
			ifd.IfdDescription.prototype.asTableRow.call(this, def, jq(tbody));
		});
	}
	if (v.comments) {
		id = "_" + Math.random();
		id = id.replace(".", "");
		div = document.createElement("div");
		jq(div).addClass("bsdd-comments");
		jq(box).append(div);
		table = document.createElement("table");
		jq(table).addClass("bsdd").append("<thead><tr><th class='ifd-lang'>" + i18n.g("IFD_LANGUAGE") + "</th><th>" + i18n.g("IFD_COMMENT") + "</th></tr></thead>").appendTo(div);
		tbody = document.createElement("tbody");
		jq(tbody).appendTo(table);
		net.spectroom.js.forAll(v.comments, function (c) {
			ifd.IfdDescription.prototype.asTableRow.call(this, c, jq(tbody));
		});
	}
};

/**
 * The Class IfdConcept.
 * @augments IfdConBase
 */	
ifd.IfdConcept = function () {
	this._n = "IfdConcept";
	Object.getPrototypeOf(ifd.IfdConcept.prototype).constructor.call(this);
	this.conceptType = null; // IfdConceptTypeEnum
	this.shortNames = null; // IfdName[]
	this.lexemes = null; // IfdName[]
	this.illustrations = null; // IfdIllustration[]
	this.owner = null; // IfdOrganization
};
ifd.IfdConcept.prototype = Object.create(ifd.IfdConBase.prototype);
ifd.IfdConcept.prototype.constructor = ifd.IfdConcept;
ifd.IfdConcept.prototype.init = function (v) {
	ifd.IfdConBase.prototype.init.call(this, v);
	this.conceptType = v.conceptType; // IfdConceptTypeEnum
	this.shortNames = v.shortNames; // IfdName[]
	this.lexemes = v.lexemes; // IfdName[]
	this.illustrations = v.illustrations; // IfdIllustration[]
	this.owner = v.owner; // IfdOrganization
};
ifd.IfdConcept.prototype.toInfoBox = function (box, v) {
	jq(box).empty().html("<img src=\"resources/ifc-logo.png\" alt=\"ifd\" class='ifd-logo'> <h4>" + v.conceptType + "</h4>");
	ifd.IfdConBase.prototype.toInfoBox.call(this, box, v);
	jq(box).addClass("bsdd-IfdConcept").attr("bsdd-guid", v.guid);
	var i18n = net.spectroom.js.i18n, div, id, 
		childrenBtn = document.createElement("button"), 
		parentBtn = document.createElement("button");
	jq(parentBtn).addClass("ifdconcept-btn-parents").append(i18n.g("IFD_ASSIGNED_TO")).appendTo(box);
	jq(childrenBtn).addClass("ifdconcept-btn-children").append(i18n.g("IFD_ASSIGNED_ELEMENTS")).appendTo(box);
	if (v.shortNames) {
		id = "_" + Math.random();
		id = id.replace(".", "");
		div = document.createElement("div");
		jq(div).addClass("bsdd-shortNames");
		jq(box).append(div);
		table = document.createElement("table");
		jq(table).addClass("bsdd").append("<thead><tr><th class='ifd-lang'>" + i18n.g("IFD_LANGUAGE") + "</th><th>" + i18n.g("IFD_SHORTNAME") + "</th></tr></thead>").appendTo(div);
		tbody = document.createElement("tbody");
		jq(tbody).appendTo(table);
		net.spectroom.js.forAll(v.shortNames, function (sn) {
			ifd.IfdName.prototype.asTableRow.call(this, sn, jq(tbody));
		});
	}
	if (v.lexemes) {
		id = "_" + Math.random();
		id = id.replace(".", "");
		div = document.createElement("div");
		jq(div).addClass("bsdd-lexemes");
		jq(box).append(div);
		table = document.createElement("table");
		jq(table).addClass("bsdd").append("<thead><tr><th class='ifd-lang'>" + i18n.g("IFD_LANGUAGE") + "</th><th>" + i18n.g("IFD_LEXEME") + "</th></tr></thead>").appendTo(div);
		tbody = document.createElement("tbody");
		jq(tbody).appendTo(table);
		net.spectroom.js.forAll(v.lexemes, function (lex) {
			ifd.IfdName.prototype.asTableRow.call(this, lex, jq(tbody));
		}); 
	}
	if (v.illustrations) {
		div = document.createElement("div");
		jq(div).addClass("bsdd-illustrations");
		jq(box).append(div);
		net.spectroom.js.forAll(v.illustrations, function (il) {
			ifd.IfdIllustration.prototype.toInfoBox.call(this, div, il);
		}); 
	}
	if (v.owner) {
		ifd.IfdOrganization.prototype.toInfoBox.call(this, box, v);
	}
};
	
/**
 *  The Class IfdConceptInRelationship.
 *  @augments IfdConcept
 */
ifd.IfdConceptInRelationship = function () {
	this._n = "IfdConceptInRelationship";
	Object.getPrototypeOf(ifd.IfdConceptInRelationship.prototype).constructor.call(this);
	this.contexts = null; // IfdContext[]
	this.relationshipType = null; // IfdRelationshipTypeEnum
};
ifd.IfdConceptInRelationship.prototype = Object.create(ifd.IfdConcept.prototype);
ifd.IfdConceptInRelationship.prototype.constructor = ifd.IfdConceptInRelationship;	
ifd.IfdConceptInRelationship.prototype.toInfoBox = function (box, v) {
	var i18n = net.spectroom.js.i18n;
	jq(box).empty().html("<img src=\"resources/ifc-logo.png\" alt=\"ifd\" class='ifd-logo'> <h4>" + i18n.g("IFD_RELATION") + ": " + v.relationshipType + "</h4>");
	ifd.IfdConcept.prototype.toInfoBox.call(this, box, v);
	jq(box).addClass("bsdd-IfdConceptInRelationship");
	if (v.contexts) {
		net.spectroom.js.forAll(v.contexts, function (c) {
			var div = document.createElement("div");
			jq(box).append(div);
			ifd.IfdContext.prototype.toInfoBox.call(this, div, c);
		});
	}
};

	
/**
 *  The Class IfdContext.
 *  @augments IfdConBase
 */
ifd.IfdContext = function () {
	this._n = "IfdContext";
	Object.getPrototypeOf(ifd.IfdContext.prototype).constructor.call(this);
	this.restricted = null; // boolean
	this.readOnly = null; // boolean
};
ifd.IfdContext.prototype = Object.create(ifd.IfdConBase.prototype);
ifd.IfdContext.prototype.constructor = ifd.IfdContext;
ifd.IfdContext.prototype.toInfoBox = function (box, v) {
	var i18n = net.spectroom.js.i18n;
	jq(box).empty().html("<img src=\"resources/ifc-logo.png\" alt=\"ifd\" class='ifd-logo'> <h4>IfdContext: " + i18n.g("IFD_RESTRICTED") + "=" + v.restricted + ", " + i18n.g("IFD_READONLY") + "=" + v.readOnly + "</h4>");
	ifd.IfdConBase.prototype.toInfoBox.call(this, box, v);
	jq(box).addClass("bsdd-IfdContext");
};

/**
 *  The Class IfdLanguageRepresentationBase.
 *  @augments IfdBase
 */
ifd.IfdLanguageRepresentationBase = function () {
	this._n = "IfdLanguageRepresentationBase";
	Object.getPrototypeOf(ifd.IfdLanguageRepresentationBase.prototype).constructor.call(this);
	this.language = null; // IfdLanguage
	this.languageFamily = ""; // String
};
ifd.IfdLanguageRepresentationBase.prototype = Object.create(ifd.IfdBase.prototype);
ifd.IfdLanguageRepresentationBase.prototype.constructor = ifd.IfdLanguageRepresentationBase;
ifd.IfdLanguageRepresentationBase.prototype.toInfoBox = function (box, v) {
	if (v) {
		var div = document.createElement("div");
		jq(div).addClass("bsdd-IfdLanguageRepresentationBase");
		jq(box).append(div);
		if (v.language) {
			ifd.IfdLanguage.prototype.toInfoBox.call(this, div, v.language);
		}
		if (v.languageFamily) {
			jq(div).attr("languageFamily", v.languageFamily);
		}
	}
};

/**
 *  The Class IfdDescription.
 *  @augments IfdLanguageRepresentationBase
 */
ifd.IfdDescription = function () {
	this._n = "IfdDescription";
	Object.getPrototypeOf(ifd.IfdDescription.prototype).constructor.call(this);
	this.description = null; // String
	this.descriptionType = null; // IfdDescriptionTypeEnum
};
ifd.IfdDescription.prototype = Object.create(ifd.IfdLanguageRepresentationBase.prototype);
ifd.IfdDescription.prototype.constructor = ifd.IfdDescription;
ifd.IfdDescription.prototype.asTableRow = function (v, tbody) {
	var tr = document.createElement("tr"), td = document.createElement("td");
	ifd.IfdLanguageRepresentationBase.prototype.toInfoBox.call(this, td, v);
	jq(tr).append(td).addClass(v.language.languageCode).attr("guid", ((v.guid) ? v.guid : "-"));
	td = document.createElement("td");
	jq(td).html(v.description).attr("title", "DESCRIPTION");
	jq(tr).append(td);
	jq(tbody).append(tr);
};


/**
 *  The Class IfdIllustration.
 *  @augments IfdLanguageRepresentationBase
 */
ifd.IfdIllustration = function () {
	this._n = "IfdIllustration";
	Object.getPrototypeOf(ifd.IfdIllustration.prototype).constructor.call(this);
	this.blobstoreKey = null; // String
	this.illustrationUrl = null; // String
};
ifd.IfdIllustration.prototype = Object.create(ifd.IfdLanguageRepresentationBase.prototype);
ifd.IfdIllustration.prototype.constructor = ifd.IfdIllustration;
ifd.IfdIllustration.prototype.toInfoBox = function (box, v) {
	if (v) {
		var t = "", div = document.createElement("div");
		jq(div).addClass("bsdd-IfdIllustration");
		jq(box).append(div);
		if (v.illustrationUrl) {
			t += "<a href=\"" + v.illustrationUrl + "\" target=\"_blank\">";
			t += "<img src=\"" + v.illustrationUrl + "\" width=\"50px\" alt=\"IfdIllustration\" />";
			t += "</a>";
		}
		jq(div).html(t);
	}
};


/**
 *  The Class IfdLanguage.
 *  @augments IfdBase
 */
ifd.IfdLanguage = function () {
	this._n = "IfdLanguage";
	Object.getPrototypeOf(ifd.IfdLanguage.prototype).constructor.call(this);
	this.nameInEnglish = null; // String
	this.nameInSelf = null; // String
	this.languageCode = null; // String
};
ifd.IfdLanguage.prototype = Object.create(ifd.IfdBase.prototype);
ifd.IfdLanguage.prototype.constructor = ifd.IfdLanguage;
ifd.IfdLanguage.prototype.toInfoBox = function (box, v) {
	if (v) {
		var t = "", title = "", div = document.createElement("div");
		jq(div).addClass("bsdd-IfdLanguage");
		jq(box).append(div);
		if (v.languageCode && v.languageCode.length >= 2) {
			var lc = v.languageCode.substr(v.languageCode.length - 2, 2).toUpperCase() + ".png";
			t += "<img class='ifd-flag' src='resources/flags/64/" + lc + "' alt='" + v.languageCode + "'>";
		}
		title += ((v.languageCode) ? v.languageCode + " " : "");
		title += ((v.nameInEnglish) ? v.nameInEnglish + " " : "");
		title += ((v.nameInSelf) ? v.nameInSelf : "");
		jq(div).html(t);
		if (title) {
			jq(div).attr("title", title);
		}
	}
};

/**
 *  The Class IfdLanguageRepresentationPreference.
 *  @augments IfdBase
 */
ifd.IfdLanguageRepresentationPreference = function () {
	this._n = "IfdLanguageRepresentationPreference";
	Object.getPrototypeOf(ifd.IfdLanguageRepresentationPreference.prototype).constructor.call(this);
	this.languageRepresentationGuid = null; // String
	this.conceptGuid = null; // String
	this.organizationGuid = null; // String
};
ifd.IfdLanguageRepresentationPreference.prototype = Object.create(ifd.IfdBase.prototype);
ifd.IfdLanguageRepresentationPreference.prototype.constructor = ifd.IfdLanguageRepresentationPreference;

/**
 *  The Class IfdName.
 *  @augments IfdLanguageRepresentationBase
 */
ifd.IfdName = function () {
	this._n = "IfdName";
	Object.getPrototypeOf(ifd.IfdName.prototype).constructor.call(this);
	this.name = null; // String
	this.nameType = null; // IfdNameTypeEnum
};
ifd.IfdName.prototype = Object.create(ifd.IfdLanguageRepresentationBase.prototype);
ifd.IfdName.prototype.constructor = ifd.IfdName;
ifd.IfdName.prototype.asTableRow = function (v, tbody) {
	var tr = document.createElement("tr"), td = document.createElement("td");
	ifd.IfdLanguageRepresentationBase.prototype.toInfoBox.call(this, td, v);
	jq(tr).append(td).addClass(v.language.languageCode).attr("guid", ((v.guid) ? v.guid : "-"));
	td = document.createElement("td");
	jq(td).html(v.name).attr("title", v.nameType);
	jq(tr).append(td);
	jq(tbody).append(tr);
};

/**
 *  The Class IfdOrganization.
 *  @augments IfdBase
 */
ifd.IfdOrganization = function () {
	this._n = "IfdOrganization";
	Object.getPrototypeOf(ifd.IfdOrganization.prototype).constructor.call(this);
	this.name = null; // String
	this.URL = null; // String
};
ifd.IfdOrganization.prototype = Object.create(ifd.IfdBase.prototype);
ifd.IfdOrganization.prototype.constructor = ifd.IfdOrganization;
ifd.IfdOrganization.prototype.toInfoBox = function (box, v) {
	var t = "", div = document.createElement("div"), i18n = net.spectroom.js.i18n;
	jq(div).addClass("bsdd-IfdOrganization");
	jq(box).append(div);
	if (v.URL) {
		t += "<a href=\"" + v.URL + "\" target=\"_blank\">";
	}
	t += ((v.name) ? v.name : "...");
	if (v.URL) {
		t += "</a>";
	}
	jq(div).html(i18n.g("IFD_ORGANIZATION") + ": " + t);
};

/**
 *  The Class IfdProperty.
 *  @augments IfdConceptInRelationship
 */
ifd.IfdProperty = function () {
	this._n = "IfdProperty";
	Object.getPrototypeOf(ifd.IfdProperty.prototype).constructor.call(this);
	this.measure = null; // IfdConcept
	this.unit = null; // IfdConcept
};
ifd.IfdProperty.prototype = Object.create(ifd.IfdConceptInRelationship.prototype);
ifd.IfdProperty.prototype.constructor = ifd.IfdProperty;

/**
 *  The Class IfdPropertyWithValues.
 *  @augments IfdProperty
 */
ifd.IfdPropertyWithValues = function () {
	this._n = "IfdPropertyWithValues";
	Object.getPrototypeOf(ifd.IfdPropertyWithValues.prototype).constructor.call(this);
	this.valueRolePairs = null; // IfdValueRolePair[]
};
ifd.IfdPropertyWithValues.prototype = Object.create(ifd.IfdProperty.prototype);
ifd.IfdPropertyWithValues.prototype.constructor = ifd.IfdPropertyWithValues;

/**
 *  The Class IfdRelationship.
 *  @augments IfdBase
 */
ifd.IfdRelationship = function () {
	this._n = "IfdRelationship";
	Object.getPrototypeOf(ifd.IfdRelationship.prototype).constructor.call(this);
	this.contexts = null; // IfdContext[]
	this.relationshipType = null; // IfdRelationshipTypeEnum
	this.parent = null; // IfdConcept
	this.child = null; // IfdConcept
}; 
ifd.IfdRelationship.prototype = Object.create(ifd.IfdBase.prototype);
ifd.IfdRelationship.prototype.constructor = ifd.IfdRelationship;

/**
 *  The Class IfdRelationshipWithValues.
 *  @augments IfdRelationship
 */
ifd.IfdRelationshipWithValues = function () {
	this._n = "IfdRelationshipWithValues";
	Object.getPrototypeOf(ifd.IfdRelationshipWithValues.prototype).constructor.call(this);
	this.valueRoles = null; // IfdValueRolePair[]
	this.measure = null; // IfdConcept
};
ifd.IfdRelationshipWithValues.prototype = Object.create(ifd.IfdRelationship.prototype);
ifd.IfdRelationshipWithValues.prototype.constructor = ifd.IfdRelationshipWithValues;

/**
 *  The Class IfdReport.
 *  @augments IfdBase
 */
ifd.IfdReport = function () {
	this._n = "IfdReport";
	Object.getPrototypeOf(ifd.IfdReport.prototype).constructor.call(this);
	this.id = null; // String
	this.NamedNodeMap = null; // String
	this.description = null; // String
};
ifd.IfdReport.prototype = Object.create(ifd.IfdBase.prototype);
ifd.IfdReport.prototype.constructor = ifd.IfdReport;

/**
 *  The Class IfdReportItem.
 *  @augments IfdBase
 */
ifd.IfdReportItem = function () {
	this._n = "IfdReportItem";
	Object.getPrototypeOf(ifd.IfdReportItem.prototype).constructor.call(this);
	this.guid = null; // String
};
ifd.IfdReportItem.prototype = Object.create(ifd.IfdBase.prototype);
ifd.IfdReportItem.prototype.constructor = ifd.IfdReportItem;

/**
 *  The Class IfdSandbox.
 *  @augments IfdBase
 */
ifd.IfdSandbox = function () {
	this._n = "IfdSandbox";
	Object.getPrototypeOf(ifd.IfdSandbox.prototype).constructor.call(this);
	this.name = null; // String
	this.public = null; // boolean
	this.owner = null; // IfdOrganization
};
ifd.IfdSandbox.prototype = Object.create(ifd.IfdBase.prototype);
ifd.IfdSandbox.prototype.constructor = ifd.IfdSandbox;

/**
 *  The Class IfdSandboxConcept.
 *  @augments IfdConcept
 */
ifd.IfdSandboxConcept = function () {
	this._n = "IfdSandboxConcept";
	Object.getPrototypeOf(ifd.IfdSandboxConcept.prototype).constructor.call(this);
	this.sandbox = null; // IfdSandbox
};
ifd.IfdSandboxConcept.prototype = Object.create(ifd.IfdConcept.prototype);
ifd.IfdSandboxConcept.prototype.constructor = ifd.IfdSandboxConcept;

/**
 *  The Class IfdSandboxConceptInRelationship.
 *  @augments IfdSandboxConcept
 */
ifd.IfdSandboxConceptInRelationship = function () {
	this._n = "IfdSandboxConceptInRelationship";
	Object.getPrototypeOf(ifd.IfdSandboxConceptInRelationship.prototype).constructor.call(this);
	this.relationshipType = null; // IfdRelationshipTypeEnum
};
ifd.IfdSandboxConceptInRelationship.prototype = Object.create(ifd.IfdSandboxConcept.prototype);
ifd.IfdSandboxConceptInRelationship.prototype.constructor = ifd.IfdSandboxConceptInRelationship;

/**
 *  The Class IfdSimpleConcept.
 *  @augments IfdBase
 */
ifd.IfdSimpleConcept = function () {
	this._n = "IfdSimpleConcept";
	Object.getPrototypeOf(ifd.IfdSimpleConcept.prototype).constructor.call(this);
	this.names = null; // IfdSimpleName[]
};
ifd.IfdSimpleConcept.prototype = Object.create(ifd.IfdBase.prototype);
ifd.IfdSimpleConcept.prototype.constructor = ifd.IfdSimpleConcept;

/**
 *  The Class IfdSimpleProperty.
 *  @augments IfdSimpleConcept
 */
ifd.IfdSimpleProperty = function () {
	this._n = "IfdSimpleProperty";
	Object.getPrototypeOf(ifd.IfdSimpleProperty.prototype).constructor.call(this);
	this.values = null; // IfdSimpleValue[]
	this.unit = null; // IfdSimpleConcept
};
ifd.IfdSimpleProperty.prototype = Object.create(ifd.IfdSimpleConcept.prototype);
ifd.IfdSimpleProperty.prototype.constructor = ifd.IfdSimpleProperty;

/**
 *  The Class IfdSimpleValue.
 *  @augments IfdSimpleConcept
 */
ifd.IfdSimpleValue = function () {
	this._n = "IfdSimpleValue";
	Object.getPrototypeOf(ifd.IfdSimpleValue.prototype).constructor.call(this);
	this.values = null; // IfdSimpleName[]
	this.valueRole = null; // IfdValueRoleEnum
};
ifd.IfdSimpleValue.prototype = Object.create(ifd.IfdSimpleConcept.prototype);
ifd.IfdSimpleValue.prototype.constructor = ifd.IfdSimpleValue;

/**
 *  The Class IfdTag.
 *  @augments IfdBase
 */
ifd.IfdTag = function () {
	this._n = "IfdTag";
	Object.getPrototypeOf(ifd.IfdTag.prototype).constructor.call(this);
	this.name = null; // IfdSimpleName
	this.synonyms = null; // IfdSimpleName[]
	this.definition = null; // String
};
ifd.IfdTag.prototype = Object.create(ifd.IfdBase.prototype);
ifd.IfdTag.prototype.constructor = ifd.IfdTag;

/**
 *  The Class IfdUser.
 *  @augments IfdBase
 */
ifd.IfdUser = function () {
	this._n = "IfdUser";
	Object.getPrototypeOf(ifd.IfdUser.prototype).constructor.call(this);
	this.name = null; // String
	this.email = null; // String
	this.createdDate = null; // String
	this.role = null; // IfdUserRoleTypeEnum
	this.memberOf = null; // IfdOrganization
	this.preferredOrganization = null; // String
};
ifd.IfdUser.prototype = Object.create(ifd.IfdBase.prototype);
ifd.IfdUser.prototype.constructor = ifd.IfdUser;
ifd.IfdUser.prototype.toInfoBox = function (box, v) {
	if (v) {
		var t = "", div = document.createElement("div"), i18n = net.spectroom.js.i18n;
		jq(div).addClass("bsdd-IfdUser");
		jq(box).append(div);
		t += "<h5>" + i18n.g("IFD_USER") + "</h5>";
		ifd.IfdBase.prototype.toInfoBox.call(this, div, v);
		t += i18n.g("IFD_USER_NAME") + ": " + v.name + "<br/>";
		t += i18n.g("IFD_USER_EMAIL") + ": <a href=\"mailto:" + v.email + "\">" + v.email + "</a><br/>";
		t += i18n.g("IFD_USER_ROLE") + ": " + v.role + "<br/>";
		t += i18n.g("IFD_ORGANIZATION") + ": " + v.preferredOrganization + "<br/>";
		jq(div).html(t);
	}
};

/**
 *  The Class IfdValueRolePair.
 *  @augments IfdBase
 */
ifd.IfdValueRolePair = function () {
	this._n = "IfdValueRolePair";
	Object.getPrototypeOf(ifd.IfdValueRolePair.prototype).constructor.call(this);
	this.value = null; // IfdConcept
	this.valueRole = null; // IfdValueRoleEnum
};
ifd.IfdValueRolePair.prototype = Object.create(ifd.IfdBase.prototype);
ifd.IfdValueRolePair.prototype.constructor = ifd.IfdValueRolePair;





























































	
	
	
	
	
	
	
	