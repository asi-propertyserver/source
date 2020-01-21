/**
 * 
 */
jq(document).on("_regexprCreated", function (event, data) {
	if (at.freebim.db.domain.parameterlist.validateTimer) {
		clearTimeout(at.freebim.db.domain.parameterlist.validateTimer);
	}
	at.freebim.db.domain.parameterlist.validateTimer = setTimeout(function () {
		jq("input[type='text'][regexpr]").each(function () {
			at.freebim.db.domain.parameterlist.validate(jq(this));
		});
	}, 500);
});
jq(document).delegate("input[type='text'][regexpr]", "input", function (event, data) {
	at.freebim.db.domain.parameterlist.validate(jq(this));
});

at.freebim.db.domain.parameterlist = {

		rels : {},
		
		tables : [],
		
		validateTimer : null,
		
		validate : function (x) {
			var r = x.attr("regexpr"), v;
			x.removeClass("invalid");
			if (r) {
				v = x.val();
				if (v != undefined) {
					if (v == "ND") {
						return;
					}
					if (!v.match(r)) {
						x.addClass("invalid");
					}
				}
			}
		},
		
		sort : function (tbody, col, th) {
			var rows = jq(tbody).children(), dir = th.data("sort-dir");
		jq(tbody).empty();
		
		rows.sort(function (tr1, tr2) {
			var a = jq(jq(tr1).children()[col]).html(),
				b = jq(jq(tr2).children()[col]).html(), res;
			if (a && a.toLowerCase() && b && b.toLowerCase()) {
				a = ((col == 0) ? a * 1 : a.toLowerCase());
				b = ((col == 0) ? b * 1 : b.toLowerCase());
				if (a > b) {
					res = 1;
				} else if (a < b) {
					res = -1;
				} else {
					res = 0;
				}
				return dir * res;
			}
			return 0;
		});
		jq(tbody).append(rows);
		jq(tbody).parent().find(".sort-asc").removeClass("sort-asc");
		jq(tbody).parent().find(".sort-desc").removeClass("sort-desc");
		th.data("sort-dir", -dir).addClass(((dir == 1) ? "sort-asc" : "sort-desc"));
	},
	addTable : function (div, entity) {
		var d = at.freebim.db.domain, pl = d.parameterlist, i18n = net.spectroom.js.i18n, nf = d.NodeFields,
		cte = d.ComponentTypeEnum;
		table = document.createElement("table"), 
		thead = document.createElement("thead"),
		tbody = document.createElement("tbody"),
		tr = document.createElement("tr"),
		th = document.createElement("th"),
		matDiv = document.createElement("div"),
		partDiv = document.createElement("div");
		
		jq(div).addClass("pl-div")
			.append(table);

		jq(matDiv).addClass("material").appendTo(div);
		jq(partDiv).addClass("parts").appendTo(div);
		
		if (entity[nf.TYPE] != undefined) {
			switch (entity[nf.TYPE]) {
			default:
			case cte.ABSTRACT : 
				break;
			case cte.UNDEFINED : 
			case cte.CONCRETE : 
			case cte.COMPOUND : 
			case cte.VARIABLE : 
				if (!entity[nf.MATERIAL] || entity[nf.MATERIAL].length < 1) {
					jq(matDiv).append("<input type='button' class='std-button select-material-btn' value=\"" + 
							i18n.gA1("SELECT_MATERIAL", d.Component.i18n, d.Component.getNameOf(entity)) + "\">");
				}
				break;
			}
			switch (entity[nf.TYPE]) {
			default:
			case cte.ABSTRACT : 
			case cte.CONCRETE : 
			case cte.COMPOUND : 
				break;
			case cte.UNDEFINED : 
			case cte.VARIABLE : 
				jq(partDiv).append("<input type='button' class='std-button select-parts-btn' value=\"" + 
					i18n.gA1("SELECT_PARTS", d.Component.i18n, d.Component.getNameOf(entity)) + "\">");
				break;
			}
		}
			
		jq(table).append(thead).css("width", "100%").addClass("jsTable");
		jq(table).append(tbody);
		jq(thead).append(tr);
		
		jq(th).html(i18n.get("PLIST_NR")).click(function () {
			// sort by number
			pl.sort(tbody, 0, jq(this));
		}).data("sort-dir", -1).addClass("jsTable-sortable sort-asc");
		jq(tr).append(th);

		th = document.createElement("th");
		jq(th).html(i18n.get("CLAZZ_PHASE")).click(function () {
			// sort by Phase
			pl.sort(tbody, 1, jq(this));
		}).data("sort-dir", -1).addClass("jsTable-sortable");
		jq(tr).append(th);
		
		th = document.createElement("th");
		jq(th).html(i18n.get("CLAZZ_PARAMETER")).click(function () {
			// sort by Parameter name
			pl.sort(tbody, 2, jq(this));
		}).data("sort-dir", -1).addClass("jsTable-sortable");
		jq(tr).append(th);
		
		th = document.createElement("th");
		jq(th).html(i18n.get("CLAZZ_VALUELISTENTRY"));
		jq(tr).append(th);
		
		pl.tables.push({ comps : [] });
		return tbody;
	},
	pimpOrdering : function (o) {
		o = ((o) ? o : "999");
		if (o.length < 2) {
			o = "0" + o;
		}
		if (o.length < 3) {
			o = "0" + o;
		}
		return o;
	},
	addAllValueList : function (dir, entity, p, m, pDiv, type, rels, cn, f, level) {
		if (rels) {
			var d = at.freebim.db.domain, pl = d.parameterlist, rf = d.RelationFields, nf = d.NodeFields, 
				i, j, m = rels.length, n = pl.tables[pl.tables.length - 1].comps.length;
			for (i=0; i<n; i++) {
				for (j=0; j<m; j++) {
					if (rels[j][rf.COMPONENT] === undefined || pl.tables[pl.tables.length - 1].comps[i] === rels[j][rf.COMPONENT]) {
						var div = ((type) ? document.createElement(type) : null),
						rel = rels[j], id;
						id = rel[rf.TO_NODE];
						if (div) {
							jq(pDiv).append(div);
							jq(div).attr("nodeid", id);
						}
						d.parameterlist.addSingle(entity, p, m, ((div) ? div : pDiv), id, cn, f, rel, level);
						return;
					}
				}
			}
		}
	},
	addAll : function (dir, entity, p, m, pDiv, type, rels, cn, f, level) {
		if (rels) {
			var d = at.freebim.db.domain, i, n = rels.length, rf = d.RelationFields, nf = d.NodeFields;
			if (n > 0 && rels[0][rf.ORDERING] != undefined) {
				rels.sort(function (r1, r2) {
					return (r1[rf.ORDERING] - r2[rf.ORDERING]);
				});
			}
			for (i=0; i<n; i++) {
				var div = ((type) ? document.createElement(type) : null),
					rel = rels[i], id;
				switch (dir) {
				case "IN": 
					id = rel[rf.FROM_NODE];
					break;
				case "OUT": 
					id = rel[rf.TO_NODE];
					break;
				case "BOTH": 
					// p contains entity that is currently processed if we are 
					// processing EQUALS relations.
					// p is null otherwise.
					id = ((p[nf.NODEID] == rel[rf.TO_NODE]) ? rel[rf.FROM_NODE] : rel[rf.TO_NODE]);
					break;
				}
					
				if (div) {
					jq(pDiv).append(div);
					jq(div).attr("nodeid", id);
				}
				d.parameterlist.addSingle(entity, p, m, ((div) ? div : pDiv), id, cn, f, rel, level);
			}
		}
	},
	addSingle : function (entity, p, m, div, id, cn, f, r, level) {
		at.freebim.db.domain.getOrLoad(id, cn, function (e) {
			f(entity, p, m, div, e, r, level);
		});
	},
	addPart : function (entity, p, m, tbody, part, r, level) {
		var nf = at.freebim.db.domain.NodeFields, i18n = net.spectroom.js.i18n;
		if (part && at.freebim.db.time.validNode(part)) {
			var d = at.freebim.db.domain, pl = d.parameterlist, next, 
				div = null; // document.createElement("div");
			div = jq(tbody).closest("table").siblings(".parts").last();
			jq(div).children("input").val(i18n.g("PART") + ": " + part[nf.NAME]);
			jq(div).append("<div class='pl-part-toggle'>-</div>"); // click handled in file: delegate.js
			next = pl.addTable(div, part);
			jq(div).attr("nodeid", part[nf.NODEID]);
			pl.addParams(entity, p, m, next, part, r, level);
			jq(tbody).parent().parent().append("<div class='parts'><input type='button' class='std-button select-parts-btn' value='" + 
					i18n.gA1("SELECT_PARTS", d.Component.i18n, d.Component.getNameOf(part)) + "'></div>");
		}
	},
	addMaterial : function (entity, p, m, tbody, mat, r, level) {
		var nf = at.freebim.db.domain.NodeFields, i18n = net.spectroom.js.i18n;
		if (mat && at.freebim.db.time.validNode(mat)) {
			var d = at.freebim.db.domain, pl = d.parameterlist, next, 
				div = null; //document.createElement("div");
			div = jq(tbody).closest("table").siblings(".material");
			jq(div).children("input").val(i18n.g("MATERIAL") + ": " + mat[nf.NAME]);
			jq(div).append("<div class='pl-material-toggle'>-</div>"); // click handled in file: delegate.js
			next = pl.addTable(div, mat);
			jq(div).attr("nodeid", mat[nf.NODEID]);
			pl.addParams(entity, p, m, next, mat, r, level);
		}
	},
	addMeasure : function (entity, p, m, div, e, r, level) {
		var nf = at.freebim.db.domain.NodeFields;
		if (e && at.freebim.db.time.validNode(e)) {
			var d = at.freebim.db.domain, pl = d.parameterlist;
			jq(div).attr("measure", e[nf.NODEID]).addClass("pl-measure");
			pl.addAll("OUT", entity, p, e, div, "div", e[nf.DATATYPE], d.DataType.className, pl.addDataType, level);
			pl.addAllValueList("OUT", entity, p, e, div, "div", e[nf.HAS_VALUE], d.ValueList.className, pl.addValueList, level);
			pl.addAll("OUT", entity, p, e, div, "div", e[nf.UNIT], d.Unit.className, pl.addUnit, level);
		}
	},
	getValueOverride : function (entity, p, m) {
		var nf = at.freebim.db.domain.NodeFields;
		if (entity[nf.OVERRIDE]) {
			var d = at.freebim.db.domain, i, n = entity[nf.OVERRIDE].length, o, rf = d.RelationFields, nf = d.NodeFields;
			for (i=0; i<n; i++) {
				o = entity[nf.OVERRIDE][i];
				if (o[rf.TO_NODE] == p[nf.NODEID] && (!o[rf.MEASURE] || o[rf.MEASURE] == m[nf.FREEBIM_ID])) {
					return o[rf.VALUE];
				}
			}
		}
		return null;
	},
	addDataType : function (entity, p, m, div, e, r, level) {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, i18n = net.spectroom.js.i18n;
		if (e && db.time.validNode(e)) {
			var override = d.parameterlist.getValueOverride(entity, p, m),
//				relId = jq(div).parentsUntil("tr").parent().attr("relid"),
//				rel = at.freebim.db.domain.parameterlist.rels[relId * 1],
//				defValue = ((override) ? override : ((rel && rel.d) ? rel[rf.DEFAULT_VALUE] : p[nf.DEFAULT]));
			defValue = ((override) ? override : p[nf.DEFAULT]);
			switch (e[nf.NAME]) {
			case "Ja / Nein Wert":
				var html = "<input type='radio' value='ND' class='freebim-value' name='" + p[nf.NODEID] + "' title='nicht definiert' " + ((!defValue || defValue == "ND") ? "checked" : "") + " >ND ";
				html += "<input type='radio' value='true' class='freebim-value' name='" + p[nf.NODEID] + "' " + ((defValue == "true") ? "checked" : "") + " >" + i18n.get("YES");
				html += "<input type='radio' value='false' class='freebim-value' name='" + p[nf.NODEID] + "' " + ((defValue == "false") ? "checked" : "") + " >" + i18n.get("NO");
				jq(div).html(html);
				break;
			case "Liste":
				break;
			case "Guid":
				jq(div).html("<input type='text' value='" + ((entity[nf.BSDD_GUID]) ? entity[nf.BSDD_GUID] : "ND") + "' class='freebim-value' readonly >");
				break;
			default:
				if (db.contributor) {
					jq(div).append("<sub class='" + d.DataType.className + "' >" + e[nf.NAME] + "</sub> ");
				}
				jq(div).append("<input type='text' value='" + ((defValue) ? defValue : "ND") + "' title='" + e[nf.DESC] + "' class='freebim-value' " + ((e[nf.REGEXPR]) ? "regexpr='" + e[nf.REGEXPR] + "'" : "") + " >");
				if (e[nf.REGEXPR]) {
					jq(document).trigger("_regexprCreated", [{}]);
				}
				break;
			}
			jq(div).css("float", "left");
		}
	},
	addUnit : function (entity, p, m, div, e, r, level) {
		var nf = at.freebim.db.domain.NodeFields;
		if (e && at.freebim.db.time.validNode(e)) {
			var d = at.freebim.db.domain;
			jq(div).html(((e[nf.CODE]) ? e[nf.CODE] : e[nf.NAME])).addClass("paramlist unit").attr("nodeid", e[nf.NODEID]);
			d.showId(div);
			d.showFreebimId(div);
			d.showBsdd(div);
		}
	},
	addValueList : function (entity, p, m, div, e, r, level) {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, rf = d.RelationFields, i18n = net.spectroom.js.i18n;
		if (e && db.time.validNode(e)) {
			jq(div).attr("title", i18n.g1("PLIST_LIST", e[nf.NAME]));
			if (e[nf.HAS_ENTRY]) {
				var i, n = e[nf.HAS_ENTRY].length, sel = document.createElement("select");
				jq(div).append(sel).css("float", "left");
				jq(sel).addClass("freebim-value select");
				for (i=0; i<n; i++) {
					var r = e[nf.HAS_ENTRY][i];
					d.parameterlist.addSingle(entity, p, m, sel, r[rf.TO_NODE], d.ValueListEntry.className, d.parameterlist.addValueListEntry, r, level);
				}
			}
		}
	},
	notDefinedOption : null,
	addValueListEntry : function (entity, p, m, sel, e, r, level) {
		var nf = at.freebim.db.domain.NodeFields;
		if (e && at.freebim.db.time.validNode(e)) {
			var d = at.freebim.db.domain, rf = d.RelationFields, pl = d.parameterlist, opt = document.createElement("option"), o = pl.pimpOrdering("" + r[rf.ORDERING]);
			if(!pl.notDefinedOption && e[nf.NAME] == "ND") {
				pl.notDefinedOption = e;
			}
			jq(opt).attr("value", e[nf.NODEID]).attr("nodeid", e[nf.NODEID]).attr("freebimid", e[nf.FREEBIM_ID]).attr("ordering", o).addClass("paramlist").html(e[nf.NAME] + ((e[nf.DESC]) ? " (" + e[nf.DESC] + ")" : ""));
			jq(sel).append(opt);
			d.showId(opt);
			d.showFreebimId(opt);
			d.showBsdd(opt);
			if (sel.optionTimer) {
				clearTimeout(sel.optionTimer);
			}
			sel.optionTimer = setTimeout(function() {
				// sort all options by ordering
				sel.optionTimer = null;
				var opts = jq(sel).children();
				jq(sel).empty();
				opts.sort(function (opt1, opt2) {
					var a = jq(opt1).attr("ordering"),
						b = jq(opt2).attr("ordering");
					if (a > b) {
						return 1;
					} else if (a < b) {
						return -1;
					} else {
						return 0;
					}
				});
				jq(sel).append(opts);
				if (pl.notDefinedOption) {
					jq(sel).val(pl.notDefinedOption[nf.FREEBIM_ID]);
				}
				var override = pl.getValueOverride(entity, p, m),
//					relId = jq(sel).parentsUntil("tr").parent().attr("relid"),
//					rel = pl.rels[relId * 1],
//					defValue = ((override) ? override : ((rel && rel.d) ? rel[rf.DEFAULT_VALUE] : p[nf.DEFAULT]));
				defValue = ((override) ? override : p[nf.DEFAULT]);
				if (defValue) {
					if (defValue == "ND" && pl.notDefinedOption) {
						defValue = pl.notDefinedOption[nf.FREEBIM_ID];
					}
					jq(opts).each(function () {
						if (jq(this).attr("freebimid") == defValue) {
							jq(this)[0].selected = true;
						} else {
							jq(this)[0].selected = false;
						}
					});
				}
			}, 500);
		}
	},
	phaseTd : function (tr, td, p, r) {
		var d = at.freebim.db.domain, nf = d.NodeFields, rf = d.RelationFields,
		getPhase = function (ph) {
			jq(td).css("background-color", "");
			jq(td).attr("phase", ph[nf.NODEID]).attr("rel", r[rf.ID]).html(ph[nf.CODE]).attr("title", ph[nf.NAME]).css("background-color", "#" + ph[nf.HEX_COLOR]);
			jq(tr).attr("phase", ph[nf.CODE]);
		};
		jq(at.freebim.db.domain.rel.HasParameter).trigger("phaseFromRel", [{rel: r, okFn: getPhase}]);
	},
	addParam : function (entity, p, m, tr, e, r, level) {
		var db = at.freebim.db, d = at.freebim.db.domain, nf = d.NodeFields, rf = d.RelationFields, pl = d.parameterlist;
		if (e && db.time.validNode(e)) {
			
			var tbody = jq(tr).parent(), td = document.createElement("td"), o = pl.pimpOrdering("" + r[rf.ORDERING]);
			
			if (jq(tbody).find("tr[level][nodeid='" + e[nf.NODEID] + "']").length > 0) {
				// this parameter is in table already
				return;
			}
			
			var eqRels = e[nf.EQUALS];
			if (eqRels && eqRels.length > 0) {
				for (var i=0; i<eqRels.length; i++) {
					if (e[nf.NODEID] == eqRels[i][rf.FROM_NODE] && jq(tbody).find("tr[level][nodeid='" + eqRels[i][rf.TO_NODE] + "']").length > 0) {
						return;
					}
					if (e[nf.NODEID] == eqRels[i][rf.TO_NODE] && jq(tbody).find("tr[level][nodeid='" + eqRels[i][rf.FROM_NODE] + "']").length > 0) {
						return;
					}
				}
			}
			
			p = e;
			jq(tr).attr("level", level).attr("e", r[rf.FROM_NODE]).attr("ordering", o).attr("relid", r[rf.ID]).attr("nodeid", p[nf.NODEID]);
			
			pl.rels[r[rf.ID] * 1] = r;

			jq(tr).append(td);

			td = document.createElement("td");
			jq(tr).append(td);
			pl.phaseTd(tr, td, p, r);
			
			td = document.createElement("td");
			jq(tr).append(td);
			jq(td).addClass("paramlist");
			if (at.freebim.db.contributor) {
				d.renderNode(p, td);
			} else {
				jq(td).html(p[nf.NAME]).attr("nodeid", e[nf.NODEID]).attr("title", e[nf.DESC]);
			}
			
			td = document.createElement("td");
			jq(tr).append(td);
			pl.addAll("OUT", entity, p, m, td, "div", p[nf.MEASURES], d.Measure.className, pl.addMeasure, level);
			if (tbody.paramlistTimer) {
				clearTimeout(tbody.paramlistTimer);
			}
			tbody.paramlistTimer = setTimeout(function() {
				// sort all table rows by level and ordering
				tbody.paramlistTimer = null;
				var rows = tbody.children("tr[level]");
				tbody.empty();
				rows.sort(function (tr1, tr2) {
					var a = jq(tr1).attr("level") + jq(tr1).attr("ordering"),
						b = jq(tr2).attr("level") + jq(tr2).attr("ordering");
					if (a > b) {
						return 1;
					} else if (a < b) {
						return -1;
					} else {
						return 0;
					}
				});
				tbody.append(rows);
				var nr = 1;
				jq(rows).each(function () {
					jq(jq(this).children()[0]).html(nr++);
				});
			}, 500);
		} else {
			jq(tr).remove();
		}
	},
	addParams : function (entity, p, m, tbody, e, r, level) {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, pl = d.parameterlist, c = d.Component.className;
		if (e && db.time.validNode(e)) {
			if (e[nf.CLASS_NAME] != c) {
				// this is the case if we process an EQUALS relation to a Parameter ...
				return;
			}
			
			// store this Component in the list of all components of this table
			var t = pl.tables[pl.tables.length - 1];
			t.comps.push(e[nf.FREEBIM_ID]);
			
			// hide 'Add Material'-Button if e is a material
			if (e[nf.NAME] == "IfcMaterial") {
				t.m = true;
				jq(tbody).closest("table").next(".material").hide();
			}
			
			// if we did add this entity (due to multiple parents ...),
			// don't add it again.
			if (jq(tbody).find("tr[e='" + e[nf.NODEID] + "']").length > 0) {
				return;
			}
			pl.addAll("IN", entity, null, null, tbody, null, e[nf.PARENTS], c, pl.addParams, level - 1);
			pl.addAll("OUT", entity, null, null, tbody, "tr", e[nf.HAS_PARAMETER], d.Parameter.className, pl.addParam, level);

			// add a marker <tr> to prevent cyclic EQUALS references
			jq(tbody).append("<tr e='" + e[nf.NODEID] + "' style='display:none;'></tr>");
			pl.addAll("BOTH", entity, e, null, tbody, null, e[nf.EQUALS], c, pl.addParams, level - 1);
			
			pl.addAll("OUT", entity, null, null, tbody, null, e[nf.MATERIAL], c, pl.addMaterial, level);
			pl.addAll("OUT", entity, null, null, tbody, null, e[nf.PARTS], c, pl.addPart, level);
		}
	},
	saveAsDefault : function (tbody, entity) {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, rows = jq(tbody).children(),
			override = [];
		jq(rows).each(function () {
			var row = jq(this), 
				cols = row.children(),
				paramId = row.attr("nodeid"),
				vh = jq(cols[3]).find("input[type='text'], input[type='radio']:checked, select");
			jq(vh).each(function () {
				var v = jq(this), mDiv = v.closest("div[measure]"), value = v.val();
				d.getOrLoad(mDiv.attr("measure"), d.Measure.className, function (measure) {
					if (!value) {
						return;
					}
					if (v.hasClass("select")) {
						var e = at.freebim.db.domain.get(value);
						value = e[nf.FREEBIM_ID];
					} else if (v.attr("type") == "radio") {
						value = jq("input[type='radio'][name='" + v.attr("name") + "']:checked").val();
					}
					var rel = {};
					rel[rf.TYPE] = d.RelationTypeEnum.VALUE_OVERRIDE;
					rel[rf.ID] = null;
					rel[rf.FROM_NODE] = entity[nf.NODEID]; // id of the Component ...
					rel[rf.TO_NODE] = paramId; // id of the Parameter
					rel[rf.VALUE] = value;
					rel[rf.MEASURE] = measure[nf.FREEBIM_ID];
					override.push(rel);
				});
					
			});
		});
		entity[nf.OVERRIDE] = override;
		jq(document).trigger("_save", [{entity: entity}]);
	},
	getJson : function (entity, tbody) {
		var nf = at.freebim.db.domain.NodeFields, 
			json = {
				name: entity[nf.NAME],
				freebimId: entity[nf.FREEBIM_ID],
				parameter: []
			}, rows = jq(tbody).children();
		if (entity[nf.BSDD_GUID]) {
			json.bsddGuid = entity[nf.BSDD_GUID];
		}
		jq(rows).each(function() {
			var row = jq(this), 
				cols = row.children(), 
				nodeId = jq(cols[2]).attr("nodeid"), 
				node = at.freebim.db.domain.get(nodeId), 
				vh = jq(cols[3]).find("input[type='text'], input[type='radio']:checked, select"),
				p = {},
				ph = row.attr("phase");
			// only export selected parameters 
			if (at.freebim.db.domain.rel.HasParameter.selectedPhase 
					&& at.freebim.db.domain.rel.HasParameter.selectedPhase[nf.CODE] != "ND" 
					&& ph > at.freebim.db.domain.rel.HasParameter.selectedPhase[nf.CODE]) {
				return;
			}
			p.name = jq(cols[2]).html();
			p.freebimId = node[nf.FREEBIM_ID];
			if (node[nf.BSDD_GUID]) {
				p.bsddGuid = node[nf.BSDD_GUID];
			}
			jq(vh).each(function () {
				var v = jq(this), mDiv = v.closest("div[measure]"), measure = mDiv.attr("measure"), m = {};
				measure = at.freebim.db.domain.get(measure);
				m.name = measure[nf.NAME];
				m.freebimId = measure[nf.FREEBIM_ID];
				if (measure[nf.BSDD_GUID]) {
					m.bsddGuid = measure[nf.BSDD_GUID];
				}
				m.value = v.val();
				if (!m.value) {
					return;
				}
				if (v.hasClass("select")) {
					var e = at.freebim.db.domain.get(m.value);
					if (at.freebim.db.domain.parameterlist.notDefinedOption && e[nf.FREEBIM_ID] == at.freebim.db.domain.parameterlist.notDefinedOption[nf.FREEBIM_ID]) {
						return;
					}
					m.value = {freebimId: e[nf.FREEBIM_ID], value: e[nf.NAME]};
					if (e[nf.BSDD_GUID]) {
						m.value.bsddGuid = e[nf.BSDD_GUID];
					}

				} else if (v.attr("type") == "radio") {
					m.value = v.val();
					if (m.value == "ND") {
						return;
					}
				} else if (m.value == "ND") {
					return;
				}
				m.unit = mDiv.find(".unit").attr("nodeid");
				if (m.unit) {
					var e = at.freebim.db.domain.get(m.unit);
					m.unit = {freebimId: e[nf.FREEBIM_ID], name: e[nf.NAME], code: e[nf.CODE]};
					if (e[nf.BSDD_GUID]) {
						m.unit.bsddGuid = e[nf.BSDD_GUID];
					}
				}
				if (!p.measure) {
					p.measure = [];
				}
				p.measure.push(m);
			});
			if (p.measure) {
				json.parameter.push(p);
			}
		});
		jq(jq(tbody).parent().siblings(".parts")).each(function () {
			var tbody = jq(this).children("table").children("tbody")[0], part = jq(this).attr("nodeid");
			if (part != undefined) {
				part = at.freebim.db.domain.get(part);
				if (!json.parts) {
					json.parts = [];
				}
				json.parts.push(at.freebim.db.domain.parameterlist.getJson(part, tbody));
			}
		});
		jq(jq(tbody).parent().siblings(".material")).each(function () {
			var tbody = jq(this).children("table").children("tbody")[0], mat = jq(this).attr("nodeid");
			if (mat != undefined) {
				mat = at.freebim.db.domain.get(mat);
				if (!json.material) {
					json.material = [];
				}
				json.material.push(at.freebim.db.domain.parameterlist.getJson(mat, tbody));
			}
		});
		return json;
	},
	exportAsCsv : function (tbody, entity) {
		var nf = at.freebim.db.domain.NodeFields, csv = at.freebim.db.domain.parameterlist.getCsv(entity, tbody);
		new net.spectroom.js.download(csv, entity[nf.NAME] + ".csv");
	},
	getCsv : function (entity, tbody) {
		var nf = at.freebim.db.domain.NodeFields, 
			csv = "", rows = jq(tbody).children(),
			delim = "\t", nl = "\n";
		// entity header:
		csv += "name";
		csv += delim;
		csv += "freebimID";
		csv += delim;
		csv += "bsDD-GUID";
		csv += nl;
		csv += entity[nf.NAME];
		csv += delim;
		csv += entity[nf.FREEBIM_ID];
		csv += delim;
		if (entity[nf.BSDD_GUID]) {
			csv += entity[nf.BSDD_GUID];
		}
		// data header line:
		csv += nl;
		csv += nl;
		csv += "Phase-Code";
		csv += delim;
		csv += "Phase-Name";
		csv += delim;
		csv += "Phase-freeBIM-ID";
		csv += delim;
		csv += "Parameter-Name";
		csv += delim;
		csv += "Parameter-freeBIM-ID";
		csv += delim;
		csv += "Parameter-bsDD-GUID";
		csv += delim;
		csv += "Measure-Name";
		csv += delim;
		csv += "Measure-freeBIM-ID";
		csv += delim;
		csv += "Measure-bsDD-GUID";
		csv += delim;
		csv += "Unit-Name";
		csv += delim;
		csv += "Unit-freeBIM-ID";
		csv += delim;
		csv += "Unit-bsDD-GUID";
		// data lines:
		if (entity[nf.BSDD_GUID]) {
			csv.bsddGuid = entity[nf.BSDD_GUID];
		}
		jq(rows).each(function() {
			var row = jq(this), 
				cols = row.children(), 
				nodeId = jq(cols[2]).attr("nodeid"), 
				node = at.freebim.db.domain.get(nodeId), 
				vh = jq(cols[3]).find("input[type='text'], input[type='radio']:checked, select"),
				p,
				ph;
			
			// only export selected parameters 
			if (at.freebim.db.domain.rel.HasParameter.selectedPhase 
					&& at.freebim.db.domain.rel.HasParameter.selectedPhase[nf.CODE] != "ND" 
					&& ph > at.freebim.db.domain.rel.HasParameter.selectedPhase[nf.CODE]) {
				return;
			}
			
			csv += nl;
			// Phase
			ph = at.freebim.db.domain.get(jq(cols[1]).attr("phase"));
			csv += ph[nf.CODE]; // phase code
			csv += delim;
			csv += ph[nf.NAME]; // phase name
			csv += delim;
			csv += ph[nf.FREEBIM_ID]; // phase freeBIM-ID
			csv += delim;
			
			// Parameter
			p = at.freebim.db.domain.get(jq(cols[2]).attr("nodeid"));
			csv += p[nf.NAME]; // Parameter name
			csv += delim;
			csv += p[nf.FREEBIM_ID];
			csv += delim;
			if (p[nf.BSDD_GUID]) {
				csv += p[nf.BSDD_GUID];
			}
			
			// Measure
			var measureCount = 0;
			jq(vh).each(function () {
				if (measureCount > 0) {
					csv += nl;
					csv += delim;
					csv += delim;
					csv += delim;
					csv += delim;
					csv += delim;
					csv += delim;
				}
				var v = jq(this), mDiv = v.closest("div[measure]"), measure = mDiv.attr("measure"), m = {};
				measure = at.freebim.db.domain.get(measure);
				csv += delim;
				csv += measure[nf.NAME];
				csv += delim;
				csv += measure[nf.FREEBIM_ID];
				csv += delim;
				if (measure[nf.BSDD_GUID]) {
					csv += measure[nf.BSDD_GUID];
				}
				
				m.unit = mDiv.find(".unit").attr("nodeid");
				if (m.unit) {
					var e = at.freebim.db.domain.get(m.unit);
					csv += delim;
					csv += e[nf.CODE];
					csv += delim;
					csv += e[nf.FREEBIM_ID];
					csv += delim;
					if (e[nf.BSDD_GUID]) {
						csv += e[nf.BSDD_GUID];
					}
				}
				measureCount++;
			});
		});
		jq(jq(tbody).parent().siblings(".parts")).each(function () {
			var tbody = jq(this).children("table").children("tbody")[0], part = jq(this).attr("nodeid");
			if (part != undefined) {
				part = at.freebim.db.domain.get(part);
				csv += nl;
				csv += nl;
				csv += at.freebim.db.domain.parameterlist.getCsv(part, tbody);
			}
		});
		jq(jq(tbody).parent().siblings(".material")).each(function () {
			var tbody = jq(this).children("table").children("tbody")[0], mat = jq(this).attr("nodeid");
			if (mat != undefined) {
				mat = at.freebim.db.domain.get(mat);
				csv += nl;
				csv += nl;
				csv += at.freebim.db.domain.parameterlist.getCsv(mat, tbody);
			}
		});
		return csv;
	},
	exportAsJson : function (tbody, entity) {
		var nf = at.freebim.db.domain.NodeFields, res = "", jsonDlg = net.spectroom.js.newDiv(), i18n = net.spectroom.js.i18n;
		jq(jsonDlg).dialog({
			title : i18n.g("DLG_TITLE_JSON_EXPORT"),
			modal : true,
			autoOpen : true,
			width : 600,
			height : 450,
			open : function () {
				if (tbody) {
					var json = at.freebim.db.domain.parameterlist.getJson(entity, tbody);
					res = JSON.stringify(json, null, "    ");
					jq(jsonDlg).html("<pre>" + res + "</pre>");
				}
			},
			buttons: [
				{
					text : i18n.getButton("DLG_BTN_CLOSE"),
					click : function() {
						jq(jsonDlg).dialog("close");
					}
				}
			],
			close : function(event, ui) {
				jq(jsonDlg).remove();
			}
		}).prev().attr("i18n_dlg", "DLG_TITLE_JSON_EXPORT");			
	},
	addPhaseFilter : function (dlg) {
		var d = at.freebim.db.domain, hp = d.rel.HasParameter, nf = d.NodeFields, div = document.createElement("div"),
			phaseBtn = document.createElement("button"), i18n = net.spectroom.js.i18n;
		hp.init();
		jq(dlg).append(div);
		jq(div).append(phaseBtn);
		jq(div).append("<div class='selected-phase info-only' abbr='1'></div>");
		jq(phaseBtn).html(i18n.get("DLG_TITLE_SPECIFY_PHASE")).addClass("select-phase").click(function () {
			var rel = {p:((hp.selectedPhase) ? hp.selectedPhase[nf.FREEBIM_ID] : null)}, okFn = function (ph) { 
				hp.selectedPhase = ph;
				var x = jq(".selected-phase");
				if (ph) {
					if (ph[nf.CODE] == "ND") {
						jq("tr[level]").show();
					} else {
						jq("tr[level]").hide().each(function () {
							var tr = jq(this), trph = tr.attr("phase");
							if (!trph || trph == "ND" || trph <= ph[nf.CODE]) {
								tr.show();
							}
						});
					}
					d.renderNode(ph, x.get(0));
					x.removeClass("freebim-contextmenu hide-unused");
				}
			};
			jq(hp).trigger("editPhase", [{rel: rel, okFn: okFn}]);
		});
	}
};

jq(document).on("show-parameterlist", function (event, data) {
	var nf = at.freebim.db.domain.NodeFields, i18n = net.spectroom.js.i18n;
	at.freebim.db.domain.rel.HasParameter.init();
	at.freebim.db.domain.getOrLoad(data.nodeId, at.freebim.db.domain.Component.className, function (entity) {
		at.freebim.db.domain.parameterlist.tables = [];
		var dlg, buttons = [], div = document.createElement("div"), tbody = at.freebim.db.domain.parameterlist.addTable(div, entity);
		at.freebim.db.domain.parameterlist.rels = {};
		if (entity) {
			jq(div).attr("nodeid", entity[nf.NODEID]);
			dlg = net.spectroom.js.newDiv();
			buttons.push({
				text : i18n.getButton("DLG_BTN_CLOSE"),
				click : function() {
					jq(dlg).dialog("close");
				}
			});
			if (at.freebim.db.contributor) {
				buttons.push({
					text : i18n.getButton("DLG_BTN_SAVE_AS_DEFAULT"),
					click : function() {
						at.freebim.db.domain.parameterlist.saveAsDefault(tbody, entity);
					}
				});
			}
			buttons.push({
				text : i18n.getButton("DLG_BTN_EXPORT_AS_CSV"),
				click : function() {
					at.freebim.db.domain.parameterlist.exportAsCsv(tbody, entity);
				}
			});
			buttons.push({
				text : i18n.getButton("DLG_BTN_EXPORT_AS_JSON"),
				click : function() {
					at.freebim.db.domain.parameterlist.exportAsJson(tbody, entity);
				}
			});
			buttons.push({
				text : i18n.getButton("DLG_BTN_EXPORT_AS_IFC"),
				click : function() {
					jq(document).trigger("alert", [{
						title: "DLG_TITLE_FREEBIM_INFO", 
						content: i18n.get("EXPORT_AS_IFC_NIY"),
						autoClose : 1500 
					}]);
				}
			});
			buttons.push();
			jq(dlg).dialog({
				title : i18n.g1("DLG_TITLE_PARAMETERLIST", entity[nf.NAME]),
				modal : true,
				autoOpen : true,
				width : 800,
				height : 600,
				open : function () {
					
					at.freebim.db.domain.parameterlist.addPhaseFilter(dlg);
					jq(dlg).append(div);
					jq(div).prepend("<div class='pl-component-toggle'>-</div> '" + entity[nf.NAME] + "':"); // click handled in file: delegate.js
					at.freebim.db.domain.parameterlist.addParams(entity, null, null, tbody, entity, null, 999);
				},
				buttons: buttons,
				close : function(event, ui) {
					jq(dlg).remove();
				}
			}).prev().attr("i18n_dlg1", "DLG_TITLE_PARAMETERLIST").attr("i18n_P1", entity[nf.NAME]);
		}
	});
});

jq(document).delegate(".select-material-btn", "click", function (event, data) {

	var db = at.freebim.db, d = db.domain, nf = d.NodeFields, i18n = net.spectroom.js.i18n,
		x = jq(this), 
		compId = x.parent().parent().attr("nodeid"),
		tbody = x.parent().siblings("table").children("tbody");
	
	d.getOrLoad(compId, d.Component.className, function (entity) {
		
		var c = d.get(x.parent().attr("nodeid"));
		// no material Component assigned.
		d.selectComponent(c, function (comp) {
			x.siblings().remove();
			if (comp) {
				// comp is a Component
				d.parameterlist.addMaterial (entity, null, null, tbody, comp, null, 999);
			} else {
				x.parent().attr("nodeid", null);
				x.val(i18n.gA1("SELECT_MATERIAL", d.Component.i18n, d.Component.getNameOf(entity)));
			}
		});
	});
});

jq(document).delegate(".select-parts-btn", "click", function (event, data) {

	var db = at.freebim.db, d = db.domain, nf = d.NodeFields, i18n = net.spectroom.js.i18n,
		x = jq(this), 
		compId = x.parent().parent().attr("nodeid"),
		tbody = x.parent().siblings("table").children("tbody");
	
	d.getOrLoad(compId, d.Component.className, function (entity) {
		
		var c = d.get(x.parent().attr("nodeid"));
		// no part Component assigned.
		d.selectComponent(c, function (comp) {
			x.siblings().remove();
			if (comp) {
				// comp is a Component
				d.parameterlist.addPart (entity, null, null, tbody, comp, null, 999);
			} else {
				x.parent().attr("nodeid", null);
				x.val(i18n.gA1("SELECT_PARTS", d.Component.i18n, d.Component.getNameOf(entity)));
			}
		});
	});
});