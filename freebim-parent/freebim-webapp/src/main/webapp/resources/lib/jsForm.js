var net = net || {};
net.spectroom = net.spectroom || {};
net.spectroom.js = net.spectroom.js || {};

net.spectroom.js.rndId = function () {
	var id = "" + Math.random();
	id = id.replace(".", "");
	return id;
};

net.spectroom.js.newDiv = function () {
	var d = document.createElement("div"), id = net.spectroom.js.rndId();
	jq("body").append(d);
	jq(d).attr("id", id);
	return d;
};

net.spectroom.js.htmlEscape = function (str) {
	if (str) {
		if (str.toLowerCase) {
		    return String(str)
	        .replace(/&/g, '&amp;')
	        .replace(/"/g, '&quot;')
	        .replace(/'/g, '&#39;')
	        .replace(/</g, '&lt;')
	        .replace(/>/g, '&gt;');
		}
	}
};

net.spectroom.js.form = {

	method : "POST",
	
	w: 600,
	h: 400,

	action : "",

	def : {
		label : "", // label
		type : "text", // input type="text"
		field : null
	// name of data field
	},

	fields : [],

	form : null,

	parent : null,

	create : function(parent, owner, clazz) {
		var self = this, i18n = net.spectroom.js.i18n, div, i, n = self.fields.length, 
			content = document.createElement("div");
		if (parent == null) {
			parent = net.spectroom.js.newDiv();
			jq(parent).addClass("jsForm-dlg");
		}
		jq(content).addClass("content");
		jq(parent).append(content);
		self.parent = parent;
		self.owner = owner;
		self.clazz = clazz;
		jq(parent).dialog({
			modal : true,
			autoOpen : false,
			width : net.spectroom.js.form.w,
			height : net.spectroom.js.form.h,
			close : function(event, ui) {
				
			},
			open : function () {
			}
		});
		jq(document).off(clazz.className + "_showInfo");
		jq(document).on(clazz.className + "_showInfo", function(e, data) {
			self.isInfo = true;
			self.createForm(content);
			self.setEntity(data.entity);
		});

		self.createForm = function(par) {
			jq(par).empty();
			self.form = document.createElement("form");
			self.form.setAttribute("method", self.method);
			self.form.setAttribute("action", self.action);
			jq(self.form).addClass("jsForm");
			par.appendChild(self.form);
			self.createFields();
		};
		self.show = function(title) {
			jq(".ui-tooltip").remove();
			self.isInfo = false;
			self.createForm(content);

			jq(self.parent).dialog("open");
			jq(self.parent).dialog("option", "title", title);
		};
		self.hide = function() {
			jq(self.parent).dialog("close");
		};
		self.createFields = function() {
			var i, n = self.fields.length;
			self.entity = null;
			for (i = 0; i < n; i++) {
				self.createField(i);
			}
		};
		self.createField = function(i) {
			var dlgId = jq(self.parent).attr("id"), j, n, ip, p, def = self.fields[i], className = "jsForm-field";
			switch (def.type) {
			case "checkbox":
				className = "jsForm-checkbox"; // no break
			case "password": // no break
			case "text":
				p = document.createElement("p");
				self.createLabel(i, p);
				ip = document.createElement("input");
				ip.type = def.type;
				ip.name = "field_" + def.field;
				ip.id = "field_" + def.field;
				if (self.isInfo) {
					ip.disabled = true;
				}
				if (def.readOnly) {
					ip.readOnly = def.readOnly;
				}
				jq(ip).addClass(className + ((def.readOnly) ? " readOnly" : ""));
				p.appendChild(ip);
				self.form.appendChild(p);
				if (def.onChange) {
					jq(ip).change(def.onChange);
				}
				break;
			case "markdown":
				p = document.createElement("p");
				self.createLabel(i, p);
				ip = document.createElement("div");
				md = document.createElement("div");
//				ip.type = def.type;
				ip.name = "field_" + def.field;
				ip.id = "field_" + def.field;
				if (self.isInfo) {
					ip.disabled = true;
				}
				if (def.readOnly) {
					ip.readOnly = true;
				}
				jq(ip).addClass(className + ((def.readOnly) ? " readOnly" : ""));
				p.appendChild(ip);
				self.form.appendChild(p);
				if (def.onChange) {
					jq(ip).change(def.onChange);
				}
				jq(ip).append(md);
				jq(md).trumbowyg({
				    btns: [
				        ['viewHTML'],
				        ['formatting'],
				        'btnGrp-semantic',
				        ['superscript', 'subscript'],
				        ['link'],
				        ['insertImage'],
				        'btnGrp-justify',
				        'btnGrp-lists',
				        ['horizontalRule'],
				        ['removeformat'],
				        ['fullscreen']
				    ],
				    resetCss: true,
				    autogrow: true
				});
				def.md = md;
				break;
			case "textarea":
				p = document.createElement("p");
				self.createLabel(i, p);
				ip = document.createElement("textarea");
//				ip.type = def.type;
				ip.name = "field_" + def.field;
				ip.id = "field_" + def.field;
				if (self.isInfo) {
					ip.disabled = true;
				}
				if (def.readOnly) {
					ip.readOnly = true;
				}
				jq(ip).addClass(className + ((def.readOnly) ? " readOnly" : ""));
				p.appendChild(ip);
				self.form.appendChild(p);
				if (def.onChange) {
					jq(ip).change(def.onChange);
				}
				break;
			case "hidden":
				ip = document.createElement("input");
				ip.type = def.type;
				ip.name = "field_" + def.field;
				ip.id = "field_" + def.field;
				self.form.appendChild(ip);
				break;
			case "datetime":
				p = document.createElement("p");
				self.createLabel(i, p);
				ip = document.createElement("input");
				ip.type = def.type;
				ip.name = "field_" + def.field;
				ip.id = "field_" + def.field;
				if (self.isInfo) {
					ip.disabled = true;
				}
				if (def.readOnly) {
					ip.readOnly = def.readOnly;
				}
				jq(ip).addClass(className + ((def.readOnly) ? " readOnly" : ""));
				p.appendChild(ip);
				self.form.appendChild(p);
				jq(ip).datepicker({
					dateFormat: 'yy-mm-dd',
					dayNames : net.spectroom.js.i18n.g("days"),
					dayNamesMin : net.spectroom.js.i18n.g("shortDays"),
					dayNamesShort : net.spectroom.js.i18n.g("shortDays"),
					monthNames : net.spectroom.js.i18n.g("months"),
					monthNames : net.spectroom.js.i18n.g("months"),
					monthNamesShort : net.spectroom.js.i18n.g("shortMonths"),
					onClose : function (dateText, inst) {
						var date = jq(ip).datepicker("getDate"),
						ts = Date.UTC(date.getUTCFullYear(), date.getUTCMonth(), date.getUTCDate(), date.getUTCHours(), date.getUTCMinutes(), date.getUTCSeconds(), date.getUTCMilliseconds());
						jq(ip).attr("value", ts);
					}
				});
				break;
			case "checkboxgroup":
				p = document.createElement("p");
				self.createLabel(i, p);
				// p.appendChild(document.createElement("br"));
				n = def.values.length;
				for (j = 0; j < n; j++) {
					if (j > 0) {
						var l = document.createElement("label");
						l.appendChild(document.createTextNode(""));
						l.setAttribute("for", "field_" + def.field);
						jq(l).addClass("jsForm-label");
						p.appendChild(l);
					}
					ip = document.createElement("input");
					ip.type = "checkbox";
					ip.name = "field_" + def.field + "[]";
					ip.setAttribute("value", def.values[j].value);
					ip.id = "field_" + def.field + "_" + j;
					if (self.isInfo) {
						ip.disabled = true;
					}
					jq(p).append(ip).append(i18n.get(def.values[j].label)).append(document.createElement("br"));
					if (def.onChange) {
						jq(ip).change(def.onChange);
					}
				}
				self.form.appendChild(p);
				break;
			case "list": 
				var b, sel = jq("#" + dlgId + " #" + "field_" + def.field)[0];
				if (!sel) {
					p = document.createElement("p");
					self.form.appendChild(p);
					self.createLabel(i, p);
					if (!self.isInfo && def.buttons && def.buttons.indexOf("add") >= 0) {
						// create an 'add' button
						b = document.createElement("button");
						b.appendChild(document.createTextNode("✚"));
						b.id = "field_" + def.field + "_addBtn";
						p.appendChild(b);
						jq("#" + dlgId + " #" + "field_" + def.field + "_addBtn").off("click").click(function(event) {
							// add an element to the list
						}).addClass("jsForm-button").addClass("jsForm-addButton");
					}
					
					if (!self.isInfo && def.buttons && def.buttons.indexOf("remove") >= 0) {
						// create a 'remove' button
						b = document.createElement("button");
						b.id = "field_" + def.field + "_removeBtn";
						b.appendChild(document.createTextNode("x"));
						p.appendChild(b);
						jq("#" + dlgId + " #" + "field_" + def.field + "_removeBtn").off("click").click(function(event) {
							// remove selected elements from the list
							event.preventDefault();
							jq(document).trigger(self.clazz.className + "_" + "field_" + def.field + "_remove", [{func: self.setEntityValue, index:i, entity:self.entity, keys:jq("#" + dlgId + " #" + "field_" + def.field).val() }]);
							return;
						}).addClass("jsForm-button").addClass("jsForm-removeButton").attr("disabled", true);
					}
					
					if (!self.isInfo && def.buttons && def.buttons.indexOf("select") >= 0) {
						// create a select with an 'add' button
						var addSel = document.createElement("SELECT");
						addSel.id = "field_" + def.field + "_addSelect";
						p.appendChild(addSel);
						def.selectValues(addSel);
						b = document.createElement("button");
						b.id = "field_" + def.field + "_addSelectedBtn";
						b.appendChild(document.createTextNode("✚"));
						p.appendChild(b);
						jq("#" + dlgId + " #" + "field_" + def.field + "_addSelectedBtn").off("click").click(function(event) {
							// add the selected elements to the list
							event.preventDefault();
							jq(document).trigger(self.clazz.className + "_" + "field_" + def.field + "_addSelected", [{func: self.setEntityValue, index:i, entity:self.entity, keys:jq("#" + dlgId + " #" + "field_" + def.field + "_addSelect").val() }]);
						}).addClass("jsForm-button").addClass("jsForm-addButton");
					}
					
					p = document.createElement("p");
					self.form.appendChild(p);
					var l = document.createElement("label");
					l.appendChild(document.createTextNode(" "));
					l.setAttribute("for", "field_" + def.field);
					jq(l).addClass("jsForm-label");
					p.appendChild(l);
					
					sel = document.createElement("select");
					sel.id = "field_" + def.field;
					sel.multiple = true;
					if (self.isInfo) {
						sel.disabled = true;
					}
					p.appendChild(sel);

					if (def.buttons && def.buttons.indexOf("remove") >= 0) {
						jq("#" + dlgId + " #" + sel.id).change(function(event) {
							var value = jq(this).val();
							if (value) { jq("#" + dlgId + " #" + "field_" + def.field + "_removeBtn").removeAttr("disabled");} else { jq("#" + dlgId + " #" + "field_" + def.field + "_removeBtn").attr("disabled", true); }
						});
					}
					jq(sel).addClass("jsForm-field");
				} else {
					jq(sel).empty();
				}
				break;
			case "select":
				var sel = jq("#" + dlgId + " #" + "field_" + def.field)[0];
				if (!sel) {
					p = document.createElement("p");
					self.form.appendChild(p);
					self.createLabel(i, p);
					sel = document.createElement("select");
					sel.id = "field_" + def.field;
					p.appendChild(sel);
					jq(sel).addClass("jsForm-field" + ((def.readOnly) ? " readOnly" : ""));
					if (def.onChange) {
						jq(sel).change(def.onChange);
					}
				} else {
					jq(sel).empty();
				}
				if (self.isInfo) {
					sel.disabled = true;
				}
				if (def.readOnly) {
					sel.readOnly = true;
					sel.disabled = true;
				}
				var values = null;
				if (jq.isArray(def.values)) {
					// values supplied as array during initialization
					values = def.values;
				} else {
					// either an array of values is returned, or the ID
					// of the event that is triggered when values are available
					// (paramter 'data.arr' passed with the event contains the
					// values).
					values = def.values(self.createField, i, self.fields);
					if (!jq.isArray(values)) {
						jq(document).one(values, function(e, data) {
							self.createField(i);
							self.setEntityValue(i, self.entity);
						});
						return;
					}
				}
				n = values.length;
				// the very first option
				ip = document.createElement("option");
				ip.name = "field_" + def.field + "[]";
				ip.setAttribute("value", "null");
				ip.id = "field_" + def.field + "_null";
				ip.setAttribute("selected", "true");
				sel.appendChild(ip);
				sel.selectedIndex = 0;
				jq(ip).append(i18n.g("OPTION_PLEASE_SELECT")).attr("i18n", "OPTION_PLEASE_SELECT");
				for (j = 0; j < n; j++) {
					ip = document.createElement("option");
					ip.name = "field_" + def.field + "[]";
					var e = def.data.call(self.owner, values[j]), f = def.dataValue
							.call(self.owner, values[j]);
					ip.setAttribute("value", f);
					ip.id = "field_" + def.field + "_" + j;
					sel.appendChild(ip);
					jq(ip).append(e);
				}

				break;
			case "custom":
				if (def.createField) {
					p = document.createElement("p");
					self.createLabel(i, p);
					ip = document.createElement("div");
					ip.name = "field_" + def.field;
					ip.id = "field_" + def.field;
					if (self.isInfo) {
						ip.disabled = true;
					}
					if (def.readOnly) {
						ip.readOnly = def.readOnly;
					}
					jq(ip).addClass(className + ((def.readOnly) ? " readOnly" : ""));
					p.appendChild(ip);
					self.form.appendChild(p);
					def.createField(self, i, ip);
				} else {
					if (console && console.error) {
						console.error("function 'createField' not defined for field " + i);
					}
				}
				break;
			}
		};
		self.createLabel = function(i, p) {
			var l, def = self.fields[i];
			l = document.createElement("label");
			jq(l).addClass("jsForm-label" + ((def.readOnly) ? " readOnly" : ""))
				.attr("for", "field_" + def.field)
				.append(i18n.get(def.label))
				.appendTo(p);
			if (def.className) {
				jq(p).addClass(def.className);
			}
		};
		self.createButtons = function() {
			var btns = [], v = self.entity;
			
			btns.push({
				text : i18n.getButton("DLG_BTN_CANCEL"), // "Abbrechen",
				click : function() {
					var entity = self.getEntity();
					jq(document).trigger("_cancel", [{entity : entity}]);
					self.hide();
				}
			});
			
			if (self.clazz.canDelete(v)) {
				btns.push({
				text : i18n.getButton("DLG_BTN_DELETE"), // "Löschen",
					click : function() {
						var entity = self.getEntity();
						jq(document).trigger("_delete", [{entity : entity}]);
						self.hide();
					}
				});
			}
			if (self.clazz.canSave(v)) {
				btns.push({
					text : i18n.getButton("DLG_BTN_SAVE"), // "Speichern",
					click : function() {
						var entity = self.getEntity();
						if (self.beforeSave) {
							self.beforeSave(entity, function (res) {
								if (res) {
									jq(document).trigger("_save", [{entity : entity}]);
									self.hide();
								}
							});
							return;
						}
						jq(document).trigger("_save", [{entity : entity}]);
						self.hide();
					}
				});
			}
			jq(self.parent).dialog("option", "buttons", btns);
		};
		self.getEntity = function() {
			var i, n = self.fields.length;
			for (i = 0; i < n; i++) {
				self.getEntityValue(i, self.entity);
			}
			return self.entity;
		};
		self.getEntityValue = function(i, res) {
			var dlgId = jq(self.parent).attr("id"), v, k, n, def = self.fields[i], 
			key = "" + def.field, id = "#" + dlgId + " #field_" + key;
			switch (def.type) {
			case "password":
			case "text":
			case "textarea":
			case "hidden":
				res[key] = jq(id).val();
				if (def.value) {
					res[key] = def.value(res[key]);
				}
				break;
			case "markdown":
				res[key] = jq(def.md).trumbowyg("html");
				if (def.value) {
					res[key] = def.value(res[key]);
				}
				break;
			case "datetime":
				res[key] = jq(id).datepicker("getDate").getTime();
				if (def.value) {
					res[key] = def.value(res[key]);
				}
				break;
			case "checkbox":
				res[key] = jq(id)[0].checked;
				break;
			case "checkboxgroup":
				n = def.values.length;
				res[key] = [];
				for (k = 0; k < n; k++) {
					var cb = jq(id + "_" + k)[0];
					if (cb.checked == true) {
						res[key].push(cb.value);
					}
				}
				break;
			case "select":
				v = jq(id).val();
				v = ((v == "null") ? null : v);
				if (v && def.value) {
					v = def.value(v, res[key]);
				}
				res[key] = v;
				break;
			case "list":
				res[key] = [];
				var sel = jq(id)[0], i, n = sel.childNodes.length;
				for (i=0; i<n; i++) {
					var o = sel.childNodes[i];
					res[key].push(o.value);
				}
				break;
			case "custom":
				if (def.getValue) {
					res[key] = def.getValue(jq(id));
				}
				break;
			}
		};
		self.setEntity = function(entity) {
			var i, n = self.fields.length;
			self.entity = entity;
			for (i = 0; i < n; i++) {
				self.setEntityValue(i, self.entity);
			}
			self.createButtons();
			if (self.afterEntitySet) {
				self.afterEntitySet(self.entity);
			}
		};
		self.newEntity = function() {
			var i, n = self.fields.length;
			self.entity = { c: self.clazz.className };
			for (i = 0; i < n; i++) {
				var def = self.fields[i], key = "" + self.fields[i].field;
				if (def.fixed != undefined) {
					self.entity[key] = def.fixed;
					continue;
				}
				switch (def.type) {
				case "markdown":
				case "password":
				case "text":
				case "textarea":
				case "datetime":
				case "hidden":
					self.entity[key] = "";
					break;
				case "checkbox":
					self.entity[key] = false;
					break;
				case "checkboxgroup":
					self.entity[key] = [];
					break;
				case "select":
					self.entity[key] = -1;
					break;
				case "list":
					self.entity[key] = [];
					break;
				}
			}
			return self.entity;
		};
		self.setEntityValue = function(i, entity) {
			if (!entity) {
				return;
			}
			var k, j, n, dlgId = jq(self.parent).attr("id"), def = self.fields[i], key = "" + self.fields[i].field;
			if (def.fixed != undefined) {
				jq("#" + dlgId + " #" + "field_" + def.field).val(def.fixed);
				return;
			}
			switch (def.type) {
			case "password":
			case "text":
			case "textarea":
			case "hidden":
				var val = entity[key];
				if (def.callBack)
					val = def.callBack(val);
				val = ((val) ? ("" + val).trim() : "");
				jq("#" + dlgId + " #" + "field_" + def.field).val(val);
				break;
			case "markdown":
				var val = entity[key];
				if (def.callBack)
					val = def.callBack(val);
				val = ((val) ? ("" + val).trim() : "");
				jq(def.md).trumbowyg("html", "" + val);
				var y = jq(def.md).trumbowyg("html");
				break;
			case "checkbox":
				jq("#" + dlgId + " #" + self.fields[i].field)[0].checked = (entity[key] == true);
				break;
			case "checkboxgroup":
				n = def.values.length;
				for (k = 0; k < n; k++) {
					var cb = jq("#" + dlgId + " #" + "field_" + def.field + "_" + k)[0], found = false;
					if (entity[key] && entity[key].length) {
						for (j = 0; j < entity[key].length; j++) {
							if (entity[key][j] == cb.value) {
								found = true;
								break;
							}
						}
					}
					cb.checked = found;
				}
				break;
			case "datetime":
				var val = entity[key];
				if (def.callBack)
					val = def.callBack(val);
				val = ((val) ? new Date(val * 1) : new Date());
				jq("#" + dlgId + " #" + "field_" + def.field).datepicker("setDate", val);
				break;
			case "select":
				var val = entity[key];
				if (def.callBack)
					val = def.callBack(val);
				jq("#" + dlgId + " #" + "field_" + def.field).val(val);
				break;
			case "list":
				var j, sel = jq("#" + dlgId + " #" + "field_" + def.field)[0];
				jq("#" + dlgId + " #" + "field_" + def.field).empty();
				var values = entity[key];
				if (def.callBack)
					values = def.callBack(values);
				n = values.length;
				if (!def.size) {
					sel.size = n;
				}
				for (j=0; j<n; j++) {
					var o = document.createElement("option");
					sel.appendChild(o);
					o.value = def.dataValue(values[j]);
					o.appendChild(document.createTextNode(def.data(values[j])));
				}
				break;
			case "custom":
				if (def.setEntityValue) {
					var ip = jq("#" + dlgId + " #" + "field_" + def.field)[0];
					def.setEntityValue(self, i, entity, ip);
				} else {
					if (console && console.error) {
						console.error("no function 'setEntityValue' found for field: " + i);
					}
				}
				break;
			}
			if (def.validate) {
				def.validate(entity, jq("#" + dlgId + " #" + "field_" + def.field));
			}
		};
	}

};