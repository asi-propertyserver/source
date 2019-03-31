var net = net || {};
net.spectroom = net.spectroom || {};
net.spectroom.js = net.spectroom.js || {};

net.spectroom.js.shrinkText = function (txt, max, elem) {
	if (txt && txt.length > max) {
		if (elem) {
			elem.title = txt;
			jq(elem).tooltip({
				open: function(event, ui) {
					// remove all other tooltips!
				    jq(ui.tooltip).siblings(".ui-tooltip").remove();
				},
				content : function (callback) {
					callback(elem.title.replace(/\n/g, "<br/>"));
				},
				show: { delay: 1500 }
			});
		}
		var x = Math.floor((max - 5) / 2);
		txt = txt.substring(0,x) + " ... " + txt.substring(txt.length-x, txt.length) + " ♦︎";
		txt.replace("\n", " ");
	}
	return txt;
};

net.spectroom.js.forAll = function (x, f) {
	if (x) {
		if (jq.isArray(x)) {
			var i, n = x.length;
			for (i=0; i<n; i++) {
				if (f(x[i], i) === false) {
					return false;
				};
			}
			return true;
		} else {
			return f(x, 0);
		}
	}
	return true;
};

net.spectroom.js.table = {

	edit : false, 
	add : false, 
	dataCallback : null,
	rowCount : 0,
	idCol : null,
	csvDelim : "%%%TAB%%%",
	csvNewline : "%%%NEWLINE%%%",

	def : { // a column definition
		label : "", // label
		field : "", // value
		type : "text", // type: text, bool, callback, select
		callback : null, // callback,
		// function name is in fiels 'data',
		// call: fn(cellvalue, td) if field is specified, or
		// fn(rowvalue, td) if field is not specified
		data : null, // default values,
		// if type:"bool" data:{T:"Yes",F:"No"}
		// if type:"callback" data:functionName
		sort : true, // sortable
		href : null
	// href, callback function to be called on cell click
	},

	cols : [], // array of def

	table : null,
	thead : null,
	fthead : null, // fixed table header
	tbody : null,

	idCol : null,

	owner : null,

	create : function(self, el, owner, clazz) {
		// el is the <div class="content">
		var i18n = net.spectroom.js.i18n;
		if (!el.id) {
			el.id = net.spectroom.js.rndId();
		}
		self.id = el.id; 
		self.clazz = clazz;
		jq(self).on("cellChanged", function(event, data) {
			self.updateTableHeaderWidth();
		});
		self.updateTableHeaderWidth = function() {
			if (self.headerTimout) {
				clearTimeout(self.headerTimout);
			}
			self.headerTimout = setTimeout(function() {
				var tr = self.thead.childNodes[0];
				if (tr) {
					var i, colWidths = [], n = tr.childNodes.length;
					for (i = 0; i < n; i++) {
						colWidths.push(jq(tr.childNodes[i]).width());
					}
					tr = self.fthead.childNodes[0];
					for (i = 0; i < n; i++) {
						var th = tr.childNodes[i];
						jq(th).width(colWidths[i]);
					}
//					jq(self.fthead).css("visibility", "visible");

				}
				if (self.initialSort) {
					self.sort(self.initialSort);
				}
			}, 200);

		};
		self.createHeader = function(thead, doSort) {
			var tr = document.createElement("tr"), col, n = self.cols.length;
			thead.appendChild(tr);
			if (self.add || self.edit) {
				var th = document.createElement("th");
				if (self.add) {
					th.appendChild(document.createTextNode("✚︎"));
					th.title = i18n.gA("TITLE_CREATE_NEW", self.clazz.i18n);
					jq(th).off("click")
					.click(function() {
						jq(document).trigger("_addEvent", [{cn: clazz.className}]);
					})
					.hover(function() {
						jq(this).css('cursor', 'pointer');
					}, function() {
						jq(this).css('cursor', 'auto');
					})
					.addClass("jsTable-editcolumn")
					.attr("i18n_titleA", "TITLE_CREATE_NEW")
					.attr("i18n_titlePA", self.clazz.i18n);
				}
				tr.appendChild(th);
			}
			for (col = 0; col < n; col++) {
				var th = document.createElement("th"), def = self.cols[col];
				jq(th).append(i18n.get(def.label));
				if (doSort && def.sort) {
					jq(th).addClass("jsTable-sortable");
					th.jsTableSort = (col + 1);
					jq(th).hover(function() {
						jq(this).css('cursor', 'pointer');
					}, function() {
						jq(this).css('cursor', 'auto');
					});
				}
				tr.appendChild(th);
			}
			if (doSort) {
				jq("#" + self.id + " .jsTable-sortable").off("click").click(function($) {
					this.jsTableSort *= -1;
					self.sort(this.jsTableSort);
				});
			}
		};
		self.showSortColumn = function() {
			jq(self.fthead.childNodes[0]).children().removeClass("sort-asc").removeClass("sort-desc");
			var sd = ((self.currentSort > 0) ? "asc" : "desc"), col = Math.abs(self.currentSort);
			jq(self.fthead.childNodes[0].childNodes[self.edit ? col : col - 1]).addClass("sort-" + sd);
		};
		self.sort = function(col) {
			jq(self).trigger("_sortBegin", [{cn: self.clazz.className}]);
			self.initialSort = null;
			self.currentSort = col;
			var i, n, arr = [], dir = col, cmp = function (a, b) {
				if (a == null || a == undefined) {
					if (b == null || b == undefined) 
						return 0;
					else
						return dir * -1;
				}
				if (b == null || b == undefined)
					return dir * 1;
				a = ((a != null && a.toLowerCase) ? a.toLowerCase() : a);
				b = ((b != null && b.toLowerCase) ? b.toLowerCase() : b);
				if (a < b)
					return dir * -1;
				else if (a > b)
					return dir * 1;
				else
					return 0;
			};
			col = Math.abs(col) - 1;

			// shuffle all table rows into an array
			// and remove all table rows from table body
			while (self.tbody.childNodes.length > 0) {
				arr.push(self.tbody.removeChild(self.tbody.firstChild));
			}
			
			// sort the array of table rows by column values
			if (self.cols[col].sortValue) {
				var sfn = self.cols[col].sortValue;
				arr.sort(function(tr1, tr2) {
					var a = sfn(tr1.jsTableRow), 
						b = sfn(tr2.jsTableRow);
					if (a != undefined && b != undefined) {
						return cmp(a, b);
					} else {
						return 0;
					}
				});
			} else {
				arr.sort(function(tr1, tr2) {
					var f = function (y) {
						var res = 0,
							a = self.getSortValue(tr1, tr1.childNodes[y+1], tr1.jsTableRow, y),
							b = self.getSortValue(tr2, tr2.childNodes[y+1], tr2.jsTableRow, y);
						res = cmp(a, b);
						if (res == 0) {
							y++;
							if (y == self.cols.length) {
								y = 0;
							}
							if (y == col) {
								return 0;
							}
							return f(y);
						} else {
							return res;
						}
					};
					return f(col);
				});
			}
			// add sorted table rows to table body again
			n = arr.length;
			for (i = 0; i < n; i++) {
				self.tbody.appendChild(arr[i]);
			}
			self.showSortColumn();
			jq(self).trigger("_sortEnd", [{cn: self.clazz.className}]);
		};
		self.getSortValue = function(tr, td, row, col) {
			var def = self.cols[col];
			if (def.sort) {
				if (def.sortValue) {
					return def.sortValue(tr.jsTableRow);
				} else {
					return self.getValue(tr, td, row, col);
				}
			} else {
				return 0;
			}
		};
		self.getValue = function(tr, td, row, col, dontShrink) {
			var def = self.cols[col],
				v = self.dataCallback.call(self.owner, row),
				v = ((v != null && def.field != null) ? v[def.field] : v);
			if (v == null) { 
				return null;
			}
			switch (def.type) {
			default:
			case "text": // text
				if (v == null) {
					v = ((def.data) ? def.data : ""); // use default, if any
				}
				break;
			case "bool": // boolean
				if (!def.data || !def.data.T || !def.data.F) {
					def.data.T = i18n.get("YES");
					def.data.F = i18n.get("NO");
				}
				v = ((v && (v == true || v == 1)) ? def.data.T : def.data.F);
				break;
			case "list": // list of values
			case "callback": // callback
				v = def.data(v, tr, td, row, col);
				break;
			case "custom":
				v = ((v) ? v : jq(td).html());
				break;
			}
			if (!dontShrink && def.maxlen && def.maxlen > 0) {
				v = net.spectroom.js.shrinkText(v, def.maxlen, td);
			}
			return v;
		};
		self.createBody = function() {
			self.tbody = document.createElement("tbody");
			self.table.appendChild(self.tbody);
			var n = self.rowCount, d = 100, fi = function (i) {
				setTimeout(function () {
					var row;
					for (row = i; row < i+d && row < n; row++) {
						self.insertRow(row);
					}
					if (row ==  n) {
						self.updateTableHeaderWidth();
						jq("#" + self.id).attr("tableCreated", "1");
						self.reSort();
						jq(self).trigger(self.clazz.className + "_tableCreated", [{table: self}]);
					}
				}, 1);
			};
			
			//if no element is provided just create the table
			if(n == 0) {
				fi(0);
			} else {
				for (var i=0; i<n; i+=d) {
					fi(i);
				}
			}
		};
		self.insertRow = function(row) {
			var tr = document.createElement("tr");
			self.tbody.appendChild(tr);
			jq(tr).attr("row", row).addClass(self.clazz.className);
			tr.jsTableRow = row;
			self.createRow(row, tr);
		};
		self.createRow = function(row, tr) {
			var col, ncols = self.cols.length, v;
			jq(tr).empty();
			jq(tr).addClass("jsTableId");
			v = self.dataCallback.call(self.owner, row, tr);
			if (v == null || v == undefined) {
				jq(tr).addClass("jsTable-emptyRow");
				return;
			} else {
				if (jq(tr).hasClass("jsTable-emptyRow")) {
					jq(tr).removeClass("jsTable-emptyRow");
					jq(net.spectroom.js.table).trigger("_tableCount", [{}]);
				}
			}
			if (v == "delayed") {
				return;
			}
			jq(tr).attr("value", v[self.idCol]);
			if (self.add || self.edit) {
				var td = document.createElement("td");
				if (self.edit) {
					td.appendChild(document.createTextNode("✎"));
					td.title = i18n.gA("TITLE_EDIT_OBJECT", self.clazz.i18n);
					jq(td).attr("i18n_titleA", "TITLE_EDIT_OBJECT")
						.attr("i18n_titlePA", self.clazz.i18n)
						.off("click")
						.click(function() {
//						jq("#" + self.id + " .jsTableRow-selected").removeClass("jsTableRow-selected");
//						jq(tr).addClass("jsTableRow-selected");
						jq(document).trigger("_editEvent", [{entity : self.dataCallback.call(self.owner, row) }]);
//						jq(document).on(clazz.className + "_saved", function (event, data) {
//							self.createRow(row, tr);
//							jq(document).off(clazz.className + "_saved");
//						});
//						jq(document).on(clazz.className + "_cancel", function (event, data) {
//							jq(document).off(clazz.className + "_saved");
//							jq(document).off(clazz.className + "_cancel");
//						});
					})
					.hover(function() {
						jq(this).css('cursor', 'pointer');
					}, function() {
						jq(this).css('cursor', 'auto');
					})
					.addClass("jsTable-editcolumn");
				}
				tr.appendChild(td);
			}
			if (v) {
				for (col = 0; col < ncols; col++) {
					var td = document.createElement("td");
					tr.appendChild(td);
					jq(td).addClass("col_" + col)/*.attr("value", v[self.idCol])*/;
					self.createCell(tr, td, row, col);
				}
			}
			if (self.rowCreated) {
				self.rowCreated({table:self, row:row, tr:tr, value:v});
			}
			
			self.reSort();
		};
		self.createCell = function(tr, td, row, col) {
			var v, def = self.cols[col];
			jq(td).empty();
			v = self.getValue(tr, td, row, col);
			if (def.type == "custom") {
				def.createCell(tr, td, row, col);
			} else {
				if (v == null) {
					return;
				}
				switch (def.type) {
				default:
				case "button":
					var b = document.createElement("button");
					td.appendChild(b);
					jq(b).append(def.value(v, b));
					if (def.classes) {
						var i, n = def.classes.length;
						for (i=0; i<n; i++) {
							jq(b).addClass(def.classes[i]);
						}
					}
					jq(b).off("click").click(function(event) {
						def.action(event, v);
					});
					break;
				case "text": // text
				case "bool": // boolean
				case "callback": // callback
					jq(td).append(v);
					break;
				case "list": // list of values
					if (v && v.length > 0) {
						var sel = document.createElement("select"),
							i, n = v.length;
						jq(sel).css("maxWidth", "200px").css("width", "200px");
						td.appendChild(sel);
						for (i=0; i<n; i++) {
							var o = document.createElement("option");
							jq(o).append(v[i]);
							sel.appendChild(o);
						}
						jq(td).append(" (" + v.length + ")");
					}
					break;
				}
			}
			if (def.href) {
				jq(td).off("click").click(function() {
					def.href(row, col);
				}).addClass("jsTableCell-clickable");
			}
			if (v && def.cl) {
				jq(td).addClass(def.cl);
			}
			if (self.cellCreated) {
				self.cellCreated({table: self, row: row, col:col, tr:tr, td:td});
			}
			self.updateTableHeaderWidth();
		};
				

		self.owner = owner;
		
		jq(el).empty();
		
		if (self.filters) {
			self.filterTimeout = null;
			var i, n = self.filters.length, 
				filterdiv = document.createElement("div"),
				div = document.createElement("div"),
				cbinv = document.createElement("input"),
				a = document.createElement("div"),
			filterFn = function () {
				jq(a).show();
				if (self.filterTimeout) {
					clearTimeout(self.filterTimeout);
				}
				self.filterTimeout = setTimeout(function () {
					var j, m = self.tbody.childNodes.length, inverse = jq(cbinv).is(':checked');
					setTimeout(function () {
						// for each table row
						for (j=0; j<m; j++) {
							var filtered = false, tr = self.tbody.childNodes[j], row = null;
							if (tr) {
								row = jq(tr).attr("row");
								if (row != undefined) {
									// for each defined filter
									for (i=0; i<n; i++) {
										try {
											filtered = self.filters[i].perform(row);
										} catch (ex) {
											at.freebim.db.logger.error("Error in performFilter: " + ex.message);
											filtered = undefined;
										}
										if (filtered == undefined) {
											continue;
										}
										if (inverse) {
											filtered = !filtered;
										}
										if (filtered) {
											break;
										}
									}
									if (filtered == true) {
										jq(tr).addClass("jsTable-filtered");
									} else {
										jq(tr).removeClass("jsTable-filtered");
									}
								}
							}
							
						}
						jq(a).hide();
						jq(document).trigger("_filterEnd", [{ cn: self.clazz.className }]);
					}, 10);
				}, 250);
			};
			el.appendChild(filterdiv);
			
			jq(div).append(i18n.get("TABLE_FILTER") + ": ").addClass("filter-label");
			jq(filterdiv).addClass("filter").append(div);
			
			for (i=0; i<n; i++) {
				self.filters[i].perform = self.filters[i].add(self.clazz, filterdiv, filterFn, self.filters[i].f);
			}
			
			div = document.createElement("div");
			jq(div).append(cbinv).append(i18n.get("TABLE_FILTER_INVERT")).addClass("filter-inverse")
			.attr("title", i18n.g("TABLE_FILTER_INVERT_TITLE")) // "Filterergebnis invertieren"
			.attr("i18n_title", "TABLE_FILTER_INVERT_TITLE");
			
			jq(cbinv).attr("type", "checkbox").change(function () {
				filterFn();
			});
			filterdiv.appendChild(div);

			jq(a).addClass("filter-activity").html(" " + i18n.get("TABLE_FILTER_IN_PROGRESS")).hide();
			jq(filterdiv).append(a);
			jq(document).on("filter", function (event, data) {
				filterFn();
			});
		}
		
		if (self.counter) {
			var counterDiv = document.createElement("div");
			jq(counterDiv).addClass("counter").attr("i18n_title", "TABLE_ROWCOUNT");
			counterDiv.title = i18n.g("TABLE_ROWCOUNT");
			el.appendChild(counterDiv);
		}
		if (self.csv !== undefined && self.csv !== false && (typeof self.csv === "string")) {
			var csvButton = document.createElement("input");
			jq(csvButton).addClass("csv_btn")
				.addClass("std-button")
				.attr("i18n_title", "TABLE_CSV")
				.attr("type", "button")
				.attr("value", "⬇︎")
				.click(function () {
					jq(self).trigger("_csvBegin", [{cn: self.clazz.className}]);
					setTimeout(function () {
						var row, n = self.rowCount, data = self.csv;
						for (row=0; row<n; row++) {
							var tr = jq(self.table).find("tr[row='" + row + "']"); 
							if (tr) {
								if (jq(tr).hasClass("hide-deleted")
										|| jq(tr).hasClass("hide-unused")
										|| jq(tr).hasClass("jsTable-filtered")) {
									continue;
								}
								data += self.csvNewline;
								data += self.csvAddRow(tr, row);
							}
						}
//						data = data.replace(/\t/g, " __TAB__ ");
//						data = data.replace(/\n/g, " __NEWLINE__ ");
						data = data.replace(/%%%TAB%%%/g, ";");
						data = data.replace(/%%%NEWLINE%%%/g, "\n");
						jq(self).trigger("_csvEnd", [{cn: self.clazz.className}]);
						new net.spectroom.js.download(data, self.clazz.title + ".csv");
					}, 40);
				});
			csvButton.title = i18n.g("TABLE_CSV");
			el.appendChild(csvButton);
			self.csvAddRow = function (tr, row) {
				var data = "", col, ncols = self.cols.length, td;
				for (col=0; col<ncols; col++) {
					var def = self.cols[col];
					if (def.csv === false) {
						continue;
					}
					td = jq(tr).find(".col_" + col);
					data += self.csvAddCell(tr, td, row, col);
				}
				return data;
			};
			self.csvAddCell = function (tr, td, row, col) {
				var def = self.cols[col];
				if (def.csv instanceof Function) {
					return def.csv(row, col);
				} else {
					try {
						var s = self.getValue(tr, td, row, col, true);
						if (s === null || s === undefined) {
							return net.spectroom.js.table.csvDelim;
						}
						if (!(typeof s === "string") || s.indexOf("<span") >= 0) {
							s = jq(s).text();
						}
						s = s.replace(/"/g, "");
						s = s.replace(/'/g, "");
						return "\"" + s + "\"" + net.spectroom.js.table.csvDelim;
					} catch (err) {
						return net.spectroom.js.table.csvDelim;
					}
				}
			};
		}
		self.resortTimeout = null;
		self.reSort = function () {
			if (self.currentSort) {
				if (self.resortTimeout) {
					clearTimeout(self.resortTimeout);
				}
				self.resortTimeout = setTimeout(function() { 
					self.sort(self.currentSort);
				}, 200);
			}
		};
		
		
		var div = document.createElement("div");
		jq(div).addClass("jsTable-fthead");
		el.appendChild(div);
		var ft = document.createElement("table");
		jq(ft).addClass("jsTable");
		jq(ft).addClass("jsTable-fthead");
		div.appendChild(ft);
		self.fthead = document.createElement("thead");
		ft.appendChild(self.fthead);
		self.createHeader(self.fthead, true);
		self.table = document.createElement("table");
		jq(self.table).addClass("jsTable jsTable-data clazz_" + self.clazz.className).data("table", self).attr("clazz", self.clazz.className);
		div = document.createElement("div");
		jq(div).addClass("jsTable-data");
		el.appendChild(div);
		div.appendChild(self.table);
		self.thead = document.createElement("thead");
		self.table.appendChild(self.thead);

		self.createHeader(self.thead, false);
		self.createBody();
//		jq(self.thead).css("visibility", "hidden");
		self.resizeTimer = null;
		jq(window).resize(function() {
			self.updateTableHeaderWidth();
		});
	}
};