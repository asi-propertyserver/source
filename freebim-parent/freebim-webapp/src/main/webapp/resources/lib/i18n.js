/**
 * 
 */
// create non conflicting jQuery instance
var jq = jQuery.noConflict();

var net = net || {};
net.spectroom = net.spectroom || {};
net.spectroom.js = net.spectroom.js || {};

net.spectroom.js.i18n = net.spectroom.js.i18n || {
	
	rids : {},
	
	strings : [],
	
	/**
	 * Get a SPAN tag containing translated text for a simple resource-ID.
	 * The returned SPAN will have an attribute 'i18n' containing the passed resource-ID.
	 * <pre>"RES_ID": "translated text"</pre>
	 * @param rid Resource-ID.
	 * @returns {String}
	 */
	get : function (rid) {
		return "<span i18n='" + rid + "'>" + net.spectroom.js.i18n.g(rid) + "</span>";
	},
	
	/**
	 * Get a SPAN tag containing translated text for a resource-ID with value.
	 * Occurrances of '%1' in the resource will be replaced with the passed value. 
	 * The returned SPAN will have an attribute 'i18n1' containing the passed resource-ID,
	 * and an attribute 'i18n_P1' containing the passed value.
	 * <pre>"RES_ID": "new '%1' created"</pre>
	 * @param rid Resource-ID.
	 * @param v Value.
	 * @returns {String}
	 */
	get1 : function (rid, v1) {
		var v = net.spectroom.js.i18n.g1(rid, v1);
		return "<span i18n_P1='" + v + "' i18n1='" + rid + "'>" + v + "</span>";
	},

	/**
	 * Get a SPAN tag containing translated text for a resource-ID with two values.
	 * Occurrances of '%1' in the resource will be replaced with the first passed value. 
	 * Occurrances of '%2' in the resource will be replaced with the second passed value. 
	 * The returned SPAN will have an attribute 'i18n2' containing the passed resource-ID,
	 * an attribute 'i18n_P1' containing the first passed value.
	 * and an attribute 'i18n_P2' containing the second passed value.
	 * <pre>"RES_ID": "value of '%1' changed to: %2"</pre>
	 * @param rid Resource-ID.
	 * @param v1 First value.
	 * @param v2 Second value.
	 * @returns {String}
	 */
	get2 : function (rid, v1, v2) {
		return "<span i18n_P1='" + v1 + "' i18n_P2='" + v2 + "' i18n2='" + rid + "'>" + net.spectroom.js.i18n.g2(rid, v1, v2) + "</span>";
	},
	
	/**
	 * Get a SPAN tag containing translated text for a resource-ID, where the resource is an array.
	 * The resource at index 0 will be used to create the SPAN tag.
	 * The returned SPAN will have an attribute 'i18n_0' containing the passed resource-ID.
	 * <pre>"RES_ID": ["red", "green", "blue"]</pre>
	 * @param rid Resource-ID.
	 * @returns {String}
	 */
	get_0 : function (rid) {
		return "<span i18n_0='" + rid + "'>" + net.spectroom.js.i18n.g_0(rid) + "</span>";
	},

	/**
	 * Get a SPAN tag containing translated text for a resource-ID, where the resource is an array.
	 * The resource at index 1 will be used to create the SPAN tag.
	 * The returned SPAN will have an attribute 'i18n_1' containing the passed resource-ID.
	 * <pre>"RES_ID": ["red", "green", "blue"]</pre>
	 * @param rid Resource-ID.
	 * @returns {String}
	 */
	get_1 : function (rid) {
		return "<span i18n_1='" + rid + "'>" + net.spectroom.js.i18n.g_1(rid) + "</span>";
	},

	/**
	 * Get a SPAN tag containing translated text for a resource-ID, where the resource is an array.
	 * The resource at index 2 will be used to create the SPAN tag.
	 * The returned SPAN will have an attribute 'i18n_2' containing the passed resource-ID.
	 * <pre>"RES_ID": ["red", "green", "blue"]</pre>
	 * @param rid Resource-ID.
	 * @returns {String}
	 */
	get_2 : function (rid) {
		return "<span i18n_2='" + rid + "'>" + net.spectroom.js.i18n.g_2(rid) + "</span>";
	},
	
	/**
	 * Get a SPAN tag containing translated text for a resource-ID depending on the passed value, where the resource is an array.
	 * The index is calculated:
	 * <pre>index = ((n > 1) ? 2 : n)</pre>
	 * The returned SPAN will have an attribute 'i18n_n' containing the passed resource-ID
	 * and an attribute 'i18n_nn' containing the passed n.
	 * <pre>"RES_ID": ["nothing found", "element found", "elements found"]</pre>
	 * @param rid Resource-ID.
	 * @param n Count.
	 * @returns {String}
	 */
	get_n : function (rid, n) {
		return "<span i18n_n='" + rid + "' i18n_nn='" + n + "'>" + net.spectroom.js.i18n.g_n(rid, n) + "</span>";
	},
	
	
	/**
	 * Get translated text for a resource-ID, where the resource is an array.
	 * The resource at index 0 will be returned.
	 * <pre>"RES_ID": ["red", "green", "blue"]</pre>
	 * @param rid Resource-ID.
	 * @returns {String}
	 */
	g_0 : function (rid) {
		try {
			return net.spectroom.js.i18n.strings[rid][0];
		} catch (e) {
			log4javascript.getDefaultLogger().error("net.spectroom.js.i18n.g_0", e);
			return "";
		}
	},

	/**
	 * Get translated text for a resource-ID, where the resource is an array.
	 * The resource at index 1 will be returned.
	 * <pre>"RES_ID": ["red", "green", "blue"]</pre>
	 * @param rid Resource-ID.
	 * @returns {String}
	 */
	g_1 : function (rid) {
		try {
			return net.spectroom.js.i18n.strings[rid][1];			
		} catch (e) {
			log4javascript.getDefaultLogger().error("net.spectroom.js.i18n.g_1", e);
			return "";
		}
	},
	
	/**
	 * Get translated text for a resource-ID, where the resource is an array.
	 * The resource at index 2 will be returned.
	 * <pre>"RES_ID": ["red", "green", "blue"]</pre>
	 * @param rid Resource-ID.
	 * @returns {String}
	 */
	g_2 : function (rid) {
		try {
			return net.spectroom.js.i18n.strings[rid][2];			
		} catch (e) {
			log4javascript.getDefaultLogger().error("net.spectroom.js.i18n.g_2", e);
			return "";
		}
	},
	
	/**
	 * Get translated text for a resource-ID depending on the passed value, where the resource is an array.
	 * The index is calculated:
	 * <pre>index = ((n > 1) ? 2 : n)</pre>
	 * <pre>"RES_ID": ["nothing found", "element found", "elements found"]</pre>
	 * @param rid
	 * @param n
	 * @returns
	 */
	g_n : function (rid, n) {
		try {
			return net.spectroom.js.i18n.strings[rid][((n > 1) ? 2 : n)];			
		} catch (e) {
			log4javascript.getDefaultLogger().error("net.spectroom.js.i18n.g_n", e);
			return "";
		}
	},
	
	/**
	 * Get translated text for a simple resource-ID.
	 * <pre>"RES_ID": "translated text"</pre>
	 * @param rid Resource-ID.
	 * @returns {String}
	 */
	g : function (rid) {
		try {
			return net.spectroom.js.i18n.strings[rid];
		} catch (e) {
			log4javascript.getDefaultLogger().error("net.spectroom.js.i18n.g", e);
			return "";
		}
	},
	
	/**
	 * Get translated text for a resource-ID with value.
	 * Occurrances of '%1' in the resource will be replaced with the passed value. 
	 * <pre>"RES_ID": "new '%1' created"</pre>
	 * @param rid Resource-ID.
	 * @param v Value.
	 * @returns {String}
	 */
	g1 : function (rid, v) {
		try {
			return net.spectroom.js.i18n.strings[rid].replace("%1", v);	
		} catch (e) {
			log4javascript.getDefaultLogger().error("net.spectroom.js.i18n.g1", e);
			return "";
		}
	},
	
	/**
	 * Get translated text for a resource-ID with two values.
	 * Occurrances of '%1' in the resource will be replaced with the first passed value. 
	 * Occurrances of '%2' in the resource will be replaced with the second passed value. 
	 * <pre>"RES_ID": "value of '%1' changed to: %2"</pre>
	 * @param rid Resource-ID.
	 * @param v1 First value.
	 * @param v2 Second value.
	 * @returns {String}
	 */
	g2 : function (rid, v1, v2) {
		try {
			return net.spectroom.js.i18n.strings[rid].replace("%1", v1).replace("%2", v2);		
		} catch (e) {
			log4javascript.getDefaultLogger().error("net.spectroom.js.i18n.g2", e);
			return "";
		}
	},
	
	/**
	 * Get combined translated text for two resource-ID's.
	 * <pre>"RES_ID": "created instance of type '%A'"</pre>
	 * @param rid First resource-ID.
	 * @param A Second resource-ID.
	 * @returns {String}
	 */
	gA : function (rid, A) {
		try {
			var i = net.spectroom.js.i18n, s = i.g(rid), sA = i.g(A);
			return ((s) ? s.replace("%A", sA) : "");
		} catch (e) {
			log4javascript.getDefaultLogger().error("net.spectroom.js.i18n.gA", e);
			return "";
		}
	},
	
	/**
	 * Get combined translated text for two resource-ID's and a value.
	 * <pre>"RES_ID": "value of object '%A' is %1"</pre>
	 * @param rid First resource-ID.
	 * @param A Second resource-ID.
	 * @param v Value.
	 * @returns {String}
	 */
	gA1 : function (rid, A, v) {
		try {
			var i = net.spectroom.js.i18n, s = i.g(rid), sA = i.g(A);
			return s.replace("%A", sA).replace("%1", v);
		} catch (e) {
			log4javascript.getDefaultLogger().error("net.spectroom.js.i18n.gA1", e);
			return "";
		}
	},
	
	titleAttributes : {},
	
	/**
	 * Get translated TITLE attribute.
	 * Use this special method to allow autom. translation of TITLE attributes
	 * where you don't have control over creation of these attributes 
	 * (i.e. when using jQuery plugins).
	 * @param rid Resource-ID.
	 * @returns {String}
	 */
	getTitle : function (rid) {
		try {
			var i = net.spectroom.js.i18n,
				s = i.g(rid);
			i.titleAttributes[s] = rid;
			return s;	
		} catch (e) {
			log4javascript.getDefaultLogger().error("net.spectroom.js.i18n.getTitle", e);
			return "";
		}
	},
	
	/**
	 * @private
	 */
	translateTitleAttributes : function () {
		var i = net.spectroom.js.i18n,
			ts = Object.keys(i.titleAttributes);
		jq.each(ts, function (j, s) {
			jq("*[title='" + s + "']").attr("title", i.getTitle(i.titleAttributes[s]));
		});
		
		jq("*[i18n_title]").each(function () {
			var x = jq(this), rid = x.attr("i18n_title"), s = i.g(rid);
			x.attr("title", s);
		});
		jq("*[i18n_title1]").each(function () {
			var x = jq(this), rid = x.attr("i18n_title1"), v = x.attr("i18n_titleP1"), s = i.g(rid);
			s = s.replace("%1", v);
			x.attr("title", s);
		});
		jq("*[i18n_title2]").each(function () {
			var x = jq(this), rid = x.attr("i18n_title2"), v1 = x.attr("i18n_titleP1"), v2 = x.attr("i18n_titleP2"), s = i.g(rid);
			s = s.replace("%1", v1);
			s = s.replace("%2", v2);
			x.attr("title", s);
		});
		jq("*[i18n_titleA]").each(function () {
			var x = jq(this), rid = x.attr("i18n_titleA"), ridA = x.attr("i18n_titlePA"), s = i.g(rid), sA = i.strings[ridA];
			s = s.replace("%A", sA);
			x.attr("title", s);
		});
		if (i.g("DLG_BTN_CLOSE")) {
			jq(".ui-dialog-titlebar-close").attr("title", i.g("DLG_BTN_CLOSE"));
			jq(".ui-dialog-titlebar-close").find(".ui-button-text").html(i.g("DLG_BTN_CLOSE"));
		}
	},
	
	/**
	 * @private
	 */
	translateDlgTitles : function () {
		var i = net.spectroom.js.i18n;
		jq("*[i18n_dlg]").each(function () {
			var x = jq(this), t = x.find(".ui-dialog-title");
			var rid = x.attr("i18n_dlg");
			t.html(i.g(rid));
		});
		jq("*[i18n_dlg1]").each(function () {
			var x = jq(this), t = x.find(".ui-dialog-title");
			var rid = x.attr("i18n_dlg1");
			t.html(i.g1(rid, x.attr("i18n_P1")));
		});
	},
	
	dlgButtons : {},
	
	/**
	 * Get translated jQuery-ui-dialog-button text.
	 * @param rid Resource-ID.
	 * @returns {String}
	 */
	getButton : function (rid) {
		try {
			var i = net.spectroom.js.i18n,
				s = i.g(rid);
			s = ((s) ? s : rid);
			i.dlgButtons[s] = rid;
			return s;			
		} catch (e) {
			log4javascript.getDefaultLogger().error("net.spectroom.js.i18n.getButton", e);
			return "";
		}
	},
	
	/**
	 * @private
	 */
	translateDlgButtons : function () {
		var i = net.spectroom.js.i18n;
		jq(".ui-button-text").each(function () {
			var x = jq(this), s = x.html();
			if (i.dlgButtons[s]) {
				var rid = i.dlgButtons[s];
				x.html(i.g(rid));
			}
		});
	},
	
	
	/**
	 * @private
	 */
	translateSpans : function () {
		var i = net.spectroom.js.i18n;
		jq("*[i18n]").each(function () {
			var x = jq(this), rid = x.attr("i18n");
			x.html(i.g(rid));
		});
		jq("text[i18n]").each(function () {
			var x = jq(this), rid = x.attr("i18n");
			x.html(i.g(rid));
		});
		jq("*[i18n_0]").each(function () {
			var x = jq(this), rid = x.attr("i18n_0");
			x.html(i.g_0(rid));
		});
		jq("*[i18n_1]").each(function () {
			var x = jq(this), rid = x.attr("i18n_1");
			x.html(i.g_1(rid));
		});
		jq("*[i18n_n]").each(function () {
			var x = jq(this), rid = x.attr("i18n_n"), n = x.attr("i18n_nn") * 1;
			x.html(i.g_n(rid, n));
		});
		jq("*[i18n1]").each(function () {
			var x = jq(this), rid = x.attr("i18n1"), v = x.attr("i18n_P1"), s = i.g1(rid, v);
			x.html(s);
		});
		jq("*[i18n2]").each(function () {
			var x = jq(this), rid = x.attr("i18n2"), v1 = x.attr("i18n_P1"), v2 = x.attr("i18n_P2"), 
			s = i.g2(rid, v1, v2);
			x.html(s);
		});
	},
	
	/**
	 * @private
	 */
	translateButtons : function () {
		var i = net.spectroom.js.i18n;
		jq("input[type='submit'][i18n]").each(function () {
			var x = jq(this), rid = x.attr("i18n");
			x.attr("value", i.g(rid));
		});
		jq("input[type='button'][i18n]").each(function () {
			var x = jq(this), rid = x.attr("i18n");
			x.attr("value", i.g(rid));
		});
	},
	
	/**
	 * @private
	 */
	translateLinks : function () {
		var i = net.spectroom.js.i18n;
		jq("a[i18n_href]").each(function () {
			var x = jq(this), rid = x.attr("i18n_href");
			x.attr("href", i.g(rid));
		});
	},
	
	/**
	 * @private
	 */
	translate : function () {
		var i = net.spectroom.js.i18n;
		log4javascript.getDefaultLogger().info("net.spectroom.js.i18n.translate '" + i.lang + "'");
		try {
			i.translateSpans();
			i.translateButtons();
			i.translateDlgButtons();
			i.translateTitleAttributes();
			i.translateDlgTitles();
			i.translateLinks();
		} catch (e) {
			log4javascript.getDefaultLogger().error("Error in net.spectroom.js.i18n.translate: ", e.message);
		}
		jq(document).trigger("i18n_translate", [{ lang: i.lang }]);
	},
		
	/**
	 * @private
	 */
	loadStrings : function () {
		var i = net.spectroom.js.i18n, 
			url = "/resources/" + ((freebim_debug === true) ? "" : "min/") + "lang/" + i.lang + ".json?ver=" + freebim_version;
		log4javascript.getDefaultLogger().info("net.spectroom.js.i18n.loadStrings '" + i.lang + "'");
		setTimeout(function () {
			jq.ajax({
				url: url,
				data: {},
				success: function (response) {
					
					log4javascript.getDefaultLogger().info("net.spectroom.js.i18n.loadStrings success");
					i.strings = response;
					i.translate();
				},
				//async: false,
				dataType: "json"
			}).fail(function (e) {
				log4javascript.getDefaultLogger().error("net.spectroom.js.i18n.loadStrings", e);
				alert("Can't load resource for '" + url + "'.");
			});
		}, 1);
	},

	/**
	 * Create a simple language selector at a specified DOM element.
	 * @param parentSelect jQuery selector of parent DOM element, i.e.: '#langSelDiv'
	 */
	createLangSelect : function (parentSelect) {
		var i = net.spectroom.js.i18n,
			p = jq(parentSelect), 
			sel = document.createElement("select"), 
			o, j, l,
			langs = [
			         { lang: "de-DE", ui: "deutsch"},
			         { lang: "en-US", ui: "english"},
			         { lang: "fr-FR", ui: "français"},
			         { lang: "es-ES", ui: "español"}
			         ];
		n = langs.length;
		for (j=0; j<n; j++) {
			l = langs[j];
			o = document.createElement("option");
			jq(o).appendTo(sel).attr("value", l.lang).html(l.ui);
			if (l.lang == i.lang) {
				o.selected = true;
			}
		}
		jq(sel).appendTo(p).change(function () {
			i.lang = jq(this).val();
			log4javascript.getDefaultLogger().info("net.spectroom.js.i18n lang changed to '" + i.lang + "'");
			net.spectroom.js.cookie.set("net.spectroom.js.i18n.lang", i.lang, 365);
			i.loadStrings();
		});
	}

};

net.spectroom.js.i18n.lang = net.spectroom.js.cookie.get("net.spectroom.js.i18n.lang");
if (!net.spectroom.js.i18n.lang) {
	net.spectroom.js.i18n.lang = "de-DE";
	net.spectroom.js.cookie.set("net.spectroom.js.i18n.lang", net.spectroom.js.i18n.lang, 365);
}

net.spectroom.js.i18n.loadStrings();

jq(document).ready(function () {
	
	var js = net.spectroom.js, i = js.i18n, db = at.freebim.db;

	i.createLangSelect("#lang_select");
	
//	i.translate();
});