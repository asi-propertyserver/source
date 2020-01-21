/**
 * 
 */

var jsonpCallback = function (x) {
	alert("jsonpCallback: " + x);
};


at.freebim.db.bsdd = {
		
//	api : "api/4.0/",
	api : "ifd/",
		
//	server : "test.bsdd.buildingsmart.org",
//	server : "test.bsdd.catenda.com",
//	server : "bsdd.catenda.com",
	testserver : "db.freebim.at", // "test.bsdd.catenda.com"
	server : "db.freebim.at",
	
	/**
	 * some cookie name definitions:
	 */
	cookie : "bsdd-login",
	lastSearchType : "bsdd.lastSearchType",
	lastUsername : "bsdd.lastUsername",
	userName : "bsdd.userName",
	userGuid : "bsdd.userGuid",
	
	/**
	 * results
	 */
	ownedContext : [],
	editableContext : [],
	languages : {}, // key is languageCode, i.e. 'de-DE', 'de-AT', value is IfdLanguage
	
	currentFreebimNode : undefined,
	currentIfdConcept : undefined,
	currentIfdConcepts : {},
	
	getURL : function (url) {
		if (at.freebim.db.bsdd.debugVersion) {
			// not a release version
//			at.freebim.db.bsdd.server = jq("input[type='radio'][name='bsdd-server-switch']:checked").val() || at.freebim.db.bsdd.testserver;
			return "http://" + at.freebim.db.bsdd.server + "/" + at.freebim.db.bsdd.api + url;
		} else {
			// release version
			return "/ifd/" + url;
		}
	},
	
	doAjax : function (type, url, data, success, msg, fail) {
		at.freebim.db.request.doAjax(type, url, data, msg).then((result, textStatus, jqXHR) => {
			success(result, textStatus, jqXHR);
		}).catch((error) => {
			if(fail) {
				fail(error);
			}
		});
		/*
		var h =  { "Accept" : "application/json" }, key = "_" + Math.random();
		key = key.replace(".", "");
		jq(document).trigger("show_progress", [ { key : key, msg : msg } ]);
		jq.ajax({
            headers: h,
            xhrFields: { withCredentials: true },
            url: at.freebim.db.bsdd.getURL(url),
            type: type,
            dataType: 'json',
            contentType : 'application/json',
            data: data,
            crossDomain: true,
            success: function (response, textStatus, jqXHR) {
            	jq(document).trigger("hide_progress", [ { key : key } ]);
//            	var nextPage = jqXHR.getResponseHeader("Next-Page");
            	if (response.result) {
            		success(response.result, textStatus, jqXHR);
            	}
            },
            error: function (response, textStatus) {
				jq(document).trigger("hide_progress", [ { key : key } ]);
            	jq(document).trigger("alert", [{
					title: "DLG_TITLE_FREEBIM_ERROR", // "freeBIM - ERROR", 
					content: textStatus + ": " + response.responseText
				}]);
            	if (fail) {
            		fail(response);
            	}
            }
        });*/
	},

	init : function () {
		
		var t = net.spectroom.js.cookie.get(at.freebim.db.bsdd.lastSearchType);
		if (!t) {
			net.spectroom.js.cookie.set(at.freebim.db.bsdd.lastSearchType, "SUBJECT", 1);
		}
		
		jq(document).delegate(".ifdconcept-btn-parents", "click", function (event, data) {
			var x = jq(this), guid = x.closest(".bsdd-IfdConcept").attr("bsdd-guid");
			jq(document).trigger("IfdConcept.parents", [{
				guid: guid 
			}]);
		});
		
		jq(document).delegate(".ifdconcept-btn-children", "click", function (event, data) {
			var x = jq(this), guid = x.closest(".bsdd-IfdConcept").attr("bsdd-guid");
			jq(document).trigger("IfdConcept.children", [{
				guid: guid 
			}]);
		});
		
		jq(document).delegate("p.GUID input[type='text']", "input", function (event, data) {
			var x = jq(this);
			if (at.freebim.db.bsdd.isValidGuid(x.val())) {
				x.removeClass("invalid");
			} else {
				x.addClass("invalid");
			}
		});
		
		jq(document).on("bsdd-login", function (event, data) {
			var bsdd = data.parentId, i18n = net.spectroom.js.i18n;
			bsdd = jq(bsdd);
			if (bsdd) {
				var loginForm = "", 
					btnText = i18n.g("DLG_LOGIN"),
					i18n_btn = "DLG_LOGIN",
					btn, btnId = null;
				
				bsdd.empty();
//				if (at.freebim.db.bsdd.debugVersion) {
//					// not a release version
//					loginForm += "<p>" + i18n.get("DLG_TITLE_BSDD_SERVER") + ":</p>";
//					loginForm += "<input type='radio' name='bsdd-server-switch' value='bsdd.catenda.com'>bsdd.catenda.com ";
//					loginForm += "<input type='radio' name='bsdd-server-switch' value='test.bsdd.catenda.com' checked>test.bsdd.catenda.com ";
//				} else {
					// release version
					loginForm += "<p>" + at.freebim.db.bsdd.server + "</p>";
//				}
				loginForm += "<table>";
				loginForm += "<thead><tr></tr></thead>";
				loginForm += "<tbody>";
				if (!net.spectroom.js.cookie.get(at.freebim.db.bsdd.cookie)) {
					btnId = "bsdd-loginbtn";
					loginForm += "<tr><td><label for='bsdd-username'>" + i18n.get("DLG_USERNAME") + ":</label></td><td><input type='text' name='bsdd-username' class='bsdd-username' /></td></tr>";
					loginForm += "<tr><td><label for='bsdd-password'>" + i18n.get("DLG_PASSWORD") + ":</label></td><td><input type='password' name='bsdd-password' class='bsdd-password' /></td></tr>";
					btnText = i18n.g("DLG_LOGIN");
				} else {
					btnId = "bsdd-logoutbtn";
					var un = net.spectroom.js.cookie.get(at.freebim.db.bsdd.userName);
					btnText = i18n.g("DLG_LOGOUT");
					i18n_btn = "DLG_LOGOUT";
				}
				loginForm += "<tr><td colspan='2'><input type='button' name='" + btnId + "' class='" + btnId + "' i18n='" + i18n_btn + "' value='" + btnText + "' /></td></tr>";
				loginForm += "</tbody";
				loginForm += "</table>";
				bsdd.html(loginForm);
				
				btn = jq(data.parentId + " ." + btnId + "");
				btn.val(btnText);
				
				if (!net.spectroom.js.cookie.get(at.freebim.db.bsdd.cookie)) {
					var un = net.spectroom.js.cookie.get(at.freebim.db.bsdd.lastUsername);
					if (un) {
						jq(data.parentId + " .bsdd-username").val(un);
					}
					jq(data.parentId + " ." + btnId + "").click(function () {
						jq(document).trigger("do-bsdd-login", [{ email: jq(data.parentId + " .bsdd-username").val(), pwd: jq(data.parentId + " .bsdd-password").val(), parentId: data.parentId }]);
					});
					jq(data.parentId + " .bsdd-username, " + data.parentId + " .bsdd-password").keyup(function (event) {
						if (event.keyCode == 13) {
							// login when user hits ENTER
							jq(document).trigger("do-bsdd-login", [{ email: jq(data.parentId + " .bsdd-username").val(), pwd: jq(data.parentId + " .bsdd-password").val(), parentId: data.parentId }]);
						}
					});
				} else {
					jq(data.parentId + " ." + btnId + "").click(function () {
						jq(document).trigger("do-bsdd-logout", [{parentId: data.parentId}]);
					});
				}

			}
			
		});
		
		jq(document).on("IfdContext/currentUserCanEdit", function (event, data) {
			var i18n = net.spectroom.js.i18n;
			at.freebim.db.bsdd.doAjax("GET", "IfdContext/currentUserCanEdit", {},
	            function (response, textStatus) {
					if (response && response.IfdContext) {
						var res = "";
						net.spectroom.js.forAll(response.IfdContext, function (c, i) {
							if (i>0) {
								res += ", ";
							}
							res += c.guid;
						});
						jq(document).trigger("alert", [{
							title: "DLG_TITLE_FREEBIM_INFO", // "freeBIM - Information", 
							content: i18n.g("BSDD_EDITABLE_CONTEXT") + ": " + res, // "editierbare bsDD-Context Objekte: " + res,
							autoClose : 5000 
						}]);
					} else {
						jq(document).trigger("alert", [{
							title: "DLG_TITLE_FREEBIM_INFO", 
							content: i18n.g("BSDD_NO_EDITABLE_CONTEXT"), // "kein editierbarer bsDD-Context gefunden.",
							autoClose : 5000 
						}]);
					}
				},
				i18n.g("BSDD_LOAD_EDITABLE_CONTEXT") // "lade editierbare bsDD-Context Objekte ..."
			);
		});

		jq(document).on("IfdContext/currentUserIsOwner", function (event, data) {
			var i18n = net.spectroom.js.i18n;
			at.freebim.db.bsdd.doAjax("GET", "IfdContext/currentUserIsOwner", {},
	            function (response, textStatus) {
					if (response) {
						var res = "";
						if (response.IfdContext) {
							net.spectroom.js.forAll(response.IfdContext, function (c, i) {
								if (i>0) {
									res += ", ";
								}
								res += c.guid;
							});
						} 
						jq(document).trigger("alert", [{
							title: "DLG_TITLE_FREEBIM_INFO", 
							content: i18n.g("BSDD_ASSIGNED_CONTEXT") + ": " + res, // "zugeordnete bsDD-Context Objekte: " + res,
							autoClose : 5000 
						}]);
					} else {
						jq(document).trigger("alert", [{
							title: "DLG_TITLE_FREEBIM_INFO", 
							content: i18n.g("BSDD_NO_ASSIGNED_CONTEXT"),
							autoClose : 5000 
						}]);
					}
				},
				i18n.g("BSDD_LOAD_ASSIGNED_CONTEXT")
			);
		});

		jq(document).on("IfdLanguage", function (event, data) {
			var i18n = net.spectroom.js.i18n;
			at.freebim.db.bsdd.doAjax("GET", "IfdLanguage", {},
	            function (response, textStatus) {
					if (response) {
						var res = "";
						if (response.IfdLanguage) {
							net.spectroom.js.forAll(response.IfdLanguage, function (l, i) {
								if (i>0) {
									res += ", ";
								}
								res += l.guid;
								at.freebim.db.bsdd.languages["" + l.languageCode] = l;
							});
						} 
//						jq(document).trigger("alert", [{
//							title: "DLG_TITLE_FREEBIM_INFO", 
//							content: "bsDD Sprach Objekte: " + res,
//							autoClose : 5000 
//						}]);
					} else {
						jq(document).trigger("alert", [{
							title: "DLG_TITLE_FREEBIM_INFO", 
							content: i18n.g("BSDD_NO_LANGUAGES"), // "keine bsDD-Sprache gefunden.",
							autoClose : 5000 
						}]);
					}
				},
				i18n.g("BSDD_LOAD_LANGUAGES") // "lade bsDD-Sprachen ..."
			);
		});

		jq(document).on("do-bsdd-login", function (event, data) {
			var i18n = net.spectroom.js.i18n,
				d = { email : data.email, password : data.pwd };
			at.freebim.db.bsdd.doAjax("POST", "session/login", d,
	            function (response, textStatus) {
					var msg = "'" + response.user.name + ", " + response.user.preferredOrganization + "' " + i18n.g("BSDD_LOGIN_SUCCESS");
					net.spectroom.js.cookie.set(at.freebim.db.bsdd.cookie, "1", 1);
					net.spectroom.js.cookie.set(at.freebim.db.bsdd.lastUsername, data.email, 14);
					net.spectroom.js.cookie.set(at.freebim.db.bsdd.userName, response.user.name, 1);
					net.spectroom.js.cookie.set(at.freebim.db.bsdd.userGuid, response.user.guid, 1);
					
//					jq(document).trigger("IfdContext/currentUserCanEdit", [{}]);
//					jq(document).trigger("IfdContext/currentUserIsOwner", [{}]);
//					jq(document).trigger("IfdContext/currentUserIsOwner", [{}]);
					jq(document).trigger("bsdd-login", [{parentId: data.parentId}]);
					jq(document).trigger("bsdd-is-logged-in", [{parentId: data.parentId}]);
					
//		            	alert(textStatus + ": " + msg);
	            	jq(document).trigger("alert", [{
						title: "DLG_TITLE_FREEBIM_INFO", 
						content: msg 
					}]);
	            },
	            i18n.g("BSDD_LOGIN")
	        );
		});
		
		jq(document).on("do-bsdd-logout", function (event, data) {
			var i18n = net.spectroom.js.i18n;
			if (net.spectroom.js.cookie.get(at.freebim.db.bsdd.cookie)) {
				at.freebim.db.bsdd.doAjax("POST", "session/logout", {},
						function (response, textStatus) {
					net.spectroom.js.cookie.clear(at.freebim.db.bsdd.cookie);
					net.spectroom.js.cookie.clear(at.freebim.db.bsdd.userName);
					jq(document).trigger("alert", [{
						title: "DLG_TITLE_FREEBIM_INFO", 
						content: i18n.g("BSDD_LOGOUT_SUCCESS"), // "erfolgreich bei bsDD abgemeldet.",
						autoClose : 5000 
					}]);
					if (data.parentId) {
						jq(document).trigger("bsdd-login", [{ parentId: data.parentId }]);
						jq(document).trigger("bsdd-is-logged-out", [{parentId: data.parentId}]);
					}
				},
				i18n.g("BSDD_LOGOUT"), // "bsDD logout ..."
				function () {
					net.spectroom.js.cookie.clear(at.freebim.db.bsdd.cookie);
					net.spectroom.js.cookie.clear(at.freebim.db.bsdd.userName);
					if (data.parentId) {
						jq(document).trigger("bsdd-login", [{ parentId: data.parentId }]);
						jq(document).trigger("bsdd-is-logged-out", [{parentId: data.parentId}]);
					}
				}
				);			
			}
		});
		
		/**
		 * @param data {
		 *  lang 		language to search name for, i.e. 'de-DE'
		 *  type		nameType, i.e. 'FULLNAME'
		 *  search		the name to search for, 
		 *  callback    the callback function (IfdName[], lang, type)
		 *  }
		 */
		jq(document).on("IfdName/search", function (event, data) {
			var i18n = net.spectroom.js.i18n, 
				languageGuid = at.freebim.db.bsdd.languages[data.lang].guid,
				type = ((data.type) ? data.type : "FULLNAME");
			if (languageGuid && data.callback) {
				at.freebim.db.bsdd.doAjax("GET", "IfdName/search/filter/language/" + languageGuid + "/nametype/" + type + "/" + data.search, {page_size: 200, page:0},
		            function (response, textStatus) {
						if (response && response.IfdName) {
							if (jq.isArray(response.IfdName)) {
								data.callback(response.IfdName, data.lang, type);
							} else {
								data.callback([response.IfdName], data.lang, type);
							}
						}
		            },
		            i18n.g("BSDD_FIND_NAME") // "suche bsDD Name ..."
		        );	
			}
		});
		
		/**
		 * data: {IfdName, callback}
		 */
		jq(document).on("IfdName-ADD", function (event, data) {
			var i18n = net.spectroom.js.i18n;
			at.freebim.db.bsdd.doAjax("POST", "IfdName", 
				{
					languageGuid: data.IfdName.language.guid,
					name: data.IfdName.name,
					nameType: data.IfdName.nameType
				},
	            function (response, textStatus) {
					data.callback(response.guid);
	            },
	            i18n.g1("BSDD_SAVE_NAME", data.IfdName.name) //"speichere '" + data.IfdName.name + "' ins bsDD ..."
	        );	
		});
		
		/**
		 * data: {IfdDescription, callback}
		 */
		jq(document).on("IfdDescription-ADD", function (event, data) {
			var i18n = net.spectroom.js.i18n;
			at.freebim.db.bsdd.doAjax("POST", "IfdDescription", 
				{
					languageGuid: data.IfdDescription.language.guid,
					description: data.IfdDescription.description,
					descriptionType: data.IfdDescription.descriptionType
				},
	            function (response, textStatus) {
					data.callback(response.guid);
	            },
	            i18n.g1("BSDD_SAVE_NAME", data.IfdDescription.description) // "speichere '" + data.IfdDescription.description + "' ins bsDD ..."
	        );	
		});


		/**
		 * data: {guid, languageGuid, name, nameType, callback}
		 */
		jq(document).on("IfdConcept/guid/name", function (event, data) {
			var i18n = net.spectroom.js.i18n;
			at.freebim.db.bsdd.doAjax("POST", "IfdConcept/" + data.guid + "/name", 
				{
					languageGuid: data.languageGuid,
					name: data.name,
					nameType: data.nameType
				},
	            function (response, textStatus) {
					data.callback(response);
	            },
	            i18n.g1("BSDD_SAVE_NAME", data.name) // "speichere '" + data.name + "' ins bsDD ..."
	        );	
		});

		/**
		 * data: {guid, languageGuid, definition, callback}
		 */
		jq(document).on("IfdConcept/guid/definition", function (event, data) {
			var i18n = net.spectroom.js.i18n;
			at.freebim.db.bsdd.doAjax("POST", "IfdConcept/" + data.guid + "/definition", 
				{
					languageGuid: data.languageGuid,
					definition: data.definition
				},
	            function (response, textStatus) {
					data.callback(response);
	            },
	            i18n.g1("BSDD_SAVE_NAME", data.definition) // "speichere '" + data.definition + "' ins bsDD ..."
	        );	
		});
		
		/**
		 * data: {guid, languageGuid, definition, callback}
		 */
		jq(document).on("IfdConcept/add", function (event, data) {
			var i18n = net.spectroom.js.i18n;
			at.freebim.db.bsdd.doAjax("POST", "IfdConcept", 
				{
				fullNameGuids: data.fullNameGuids, 	// a comma separated list of GUIDs for existing fullnames. 
													// Must contain minimum two GUIDs. 
													// One for a name in international English and one for a name in another language.	form	
				definitionGuids: data.definitionGuids, // a comma separated list of GUIDs for existing defintions. Can be empty.	form	
				commentGuids: data.commentGuids, // a comma separated list of GUIDs for existing comments. Can be empty.	form	
				conceptType: data.conceptType, // the concept type	form	
				shortNameGuids: data.shortNameGuids, // a comma separated list of the short name GUIDs	form	
				lexemeGuids: data.lexemeGuids, // a comma separated list of the lexeme GUIDs	form	
				illustrationGuids: data.illustrationGuids, // a comma separated list of the illustration GUIDs	form	
				ownerGuid: data.ownerGuid // the GUID of the owner	form	
				},
	            function (response, textStatus) {
					data.callback(response.guid);
	            },
	            i18n.g1("BSDD_SAVE_NAME", "IfdConcept") // "speichere ins bsDD ..."
	        );	
		});



		jq(document).on("IfdConcept.parents", function (event, data) {
			var i18n = net.spectroom.js.i18n;
			at.freebim.db.bsdd.doAjax("GET", "IfdConcept/" + data.guid + "/parents", {
						page_size: 200, 
						page: 0,
						cache : false
					},
		            function (response, textStatus) {
		            	if (response) {
		            		if (data.callback) {
		            			data.callback(response);
		            		}
		            		if (!data.silent) {
			            		var dlg = net.spectroom.js.newDiv();
								jq(dlg).dialog({
									title : "IfdConceptInRelationship",
									width: 800,
									height: 600,
									modal : true,
									open : function () {
										net.spectroom.js.forAll(response.IfdConceptInRelationship, function (r, i) {
											var x = document.createElement("div");
											jq(dlg).append(x);
				            				ifd.IfdConceptInRelationship.prototype.toInfoBox.call(this, x, r);
										});
									},
									close : function () {
										jq(dlg).remove();
									}
								});
							}
		            	} else {
							jq(document).trigger("alert", [{
								title: "DLG_TITLE_FREEBIM_INFO", 
								content: i18n.g("BSDD_NOTHING_FOUND"), // "keine Einträge gefunden.",
								autoClose : 2000 
							}]);
						}
		            },
		            i18n.g("BSDD_LOAD_PARENTS") // "lade Parent bsDD-Objekte ..."
		        );
		});
		
		jq(document).on("IfdConcept.children", function (event, data) {
			var i18n = net.spectroom.js.i18n;
			at.freebim.db.bsdd.doAjax("GET", "IfdConcept/" + data.guid + "/children", {
						page_size: 300, 
						page: undefined,
						cache : false
					},
		            function (response, textStatus) {
		            	if (response) {
		            		if (data.callback) {
		            			data.callback(response);
		            		}
		            		if (!data.silent) {
			            		var dlg = net.spectroom.js.newDiv();
								jq(dlg).dialog({
									title : "IfdConceptInRelationship",
									width: 800,
									height: 600,
									modal : true,
									open : function () {
										net.spectroom.js.forAll(response.IfdConceptInRelationship, function (r, i) {
											var x = document.createElement("div");
											jq(dlg).append(x);
				            				ifd.IfdConceptInRelationship.prototype.toInfoBox.call(this, x, r);
										});
									},
									close : function () {
										jq(dlg).remove();
									}
								});
		            		}
		            	} else {
							jq(document).trigger("alert", [{
								title: "DLG_TITLE_FREEBIM_INFO", 
								content: i18n.g("BSDD_NOTHING_FOUND"),
								autoClose : 2000 
							}]);
						}
		            },
		            i18n.g("BSDD_LOAD_ASSIGNED_OBJECTS") // "lade zugeordnete bsDD-Objekte ..."
		        );
		});

		
		jq(document).on("IfdConcept/GET", function (event, data) {
			var i18n = net.spectroom.js.i18n;
        	at.freebim.db.bsdd.doAjax("GET", "IfdConcept/" + data.guid, {},
	        	function (response, textStatus, jqXHR) {
					if (data.callback) {
						data.callback(response);
					}
        			if (data.div) {
						ifd.IfdConcept.prototype.toInfoBox.call(this, data.div, response);
        			} else {
        				if (!data.silent) {
        	        		var dlg = net.spectroom.js.newDiv();
        					jq(dlg).dialog({
        						title : "buildingSMART Data Dictionary",
        						width: 800,
        						height: 600,
        						modal : true,
        						open : function () {
        							ifd.IfdConcept.prototype.toInfoBox.call(this, dlg, response);
        							if (data.callback) {
        								data.callback(response);
        							}
        						},
        						close : function () {
        							jq(dlg).remove();
        						}
        					});	
        				}
        			}
	        	}, 
	        	i18n.g("BSDD_LOAD_OBJECT") // "lade bsDD-Objekt ..."
	        ); 
		});
		
		jq(document).on("IfdConcept.fetch", function (event, data) {
			var i18n = net.spectroom.js.i18n,
				t = data.type,
				s = data.search,
				l = data.lang, i, ss = "", ls = "";
			
			if (l.length > 1) {
				// english is present!
				// search in Pset_s 's too
				var db = at.freebim.db; d = db.domain; ps = d.ParameterSet, nf = d.NodeFields;
				ps.init();
				if (!ps.arr) {
					// load all ParameterSet objects
					jq(document).trigger("_load", [{cn: ps.className, callback: function () {
						jq(document).trigger("IfdConcept.fetch", data);
					}}]);
					return;
				}
				net.spectroom.js.forAll(ps.arr, function (id, i) {
					var p = d.get(id);
					if (db.time.validNode(p)) {
						s.push(p[nf.NAME_EN] + " . " + s[1]);
						l.push("ENGLISH");
					}
				});
			}
			// search in IFC language too
			if (!data.strict) {
				s.push(s[0]);
				l.push("IFC");
			}
			
			for (i=0; i<s.length; i++) {
				ss += ((i > 0) ? "|" : "");
				ss += at.freebim.db.bsdd.simplifyText(s[i]);
			}
			for (i=0; i<l.length; i++) {
				ls += ((i > 0) ? "|" : "");
				ls += l[i];
			}
			at.freebim.db.bsdd.doAjax ("POST", "IfdConcept/searchForDuplicates/filter/type/" + t,
				{ 	
					nameList : ss, 
					languageFamilyList : ls, 
					cache : "false"
				}, 
				function (response, textStatus, jqXHR) {
					var res = [];
					if (response) {
						if (response.IfdConcept) {
							net.spectroom.js.forAll(response.IfdConcept, function(value, index) {
								var g = value.guid;
								if (res.indexOf(g) <= 0) {
									res.push(g);
								}
							});
						}
					}
					data.callback(res);
				},
				i18n.g1("BSDD_FIND", s) // "suche in bsDD: '" + s + "' ..."
			);
		
		});
		
		jq(document).on("IfdConcept.searchForDuplicates", function (event, data) {
			var i18n = net.spectroom.js.i18n,
				t = data.type,
				s = data.search,
				l = data.lang, i, ss = "", ls = "";
			
			if (!data.strict) {
				s.push(s[0]);
				l.push("IFC");
			}

			for (i=0; i<s.length; i++) {
				ss += ((i > 0) ? "|" : "");
				ss += at.freebim.db.bsdd.simplifyText(s[i]);
			}
			for (i=0; i<l.length; i++) {
				ls += ((i > 0) ? "|" : "");
				ls += l[i];
			}
			at.freebim.db.bsdd.doAjax ("POST", "IfdConcept/searchForDuplicates/filter/type/" + t,
				{ 	
					nameList : ss, 
					languageFamilyList : ls, 
					cache : "false"
				}, 
				function (response, textStatus, jqXHR) {
					if (response) {
						var dlg = net.spectroom.js.newDiv(), x = jq(dlg);
						x.dialog({
							title : "buildingSMART Data Dictionary",
							width: 800,
							height: 600,
							modal : false,
							open : function () {
								if (response.IfdConcept) {
									if (jq.isArray(response.IfdConcept)) {
										var i, hl = document.createElement("div"), n = response.IfdConcept.length, 
											shortList = ((n > 1) ? document.createElement("select") : null);
										jq(hl).html(i18n.g2("BSDD_FIND_RESULT", s, n)) // "bsDD-Suche nach '" + s + "' liefert " + n + " Ergebnisse: ");
										.attr("i18n_P1", s).attr("i18n_P2", n);
										x.append(hl);
										if (shortList) {
											jq(hl).append(shortList);
											var li = document.createElement("option");
											jq(li).html("... alle anzeigen").val("0");
											jq(shortList).append(li).change(function() {
												if (jq(shortList).val() == "0") {
													jq(".bsdd-IfdConcept").show();
													x.dialog( "option", "buttons", [{text: i18n.getButton("DLG_BTN_CLOSE"), click: function () {jq(this).dialog("close");} }]);
												} else {
													var guid = jq(shortList).val();
													jq(".bsdd-IfdConcept").hide();
													jq(".bsdd-IfdConcept[bsdd-guid='" + guid + "']").show();
													if (data.callback) {
														var btns = [{text: i18n.getButton("DLG_BTN_CLOSE"), click: function () {jq(this).dialog("close");} }];
														if (at.freebim.db.contributor) {
															btns.push({
														      text: i18n.getButton("DLG_BTN_USE_BSDD_GUID"),
														      click: function() {
													    		  data.callback(guid);
														    	  jq(this).dialog( "close" );
														      }
														});
														}
														x.dialog( "option", "buttons", btns);
													}
												}
											});
										}
										for (i=0; i<n; i++) {
											var c = response.IfdConcept[i];
											var div = document.createElement("div");
											x.append(div);
											ifd.IfdConcept.prototype.toInfoBox.call(this, div, c);
										}
										if (shortList) {
											var addToShortlist = function (lang) {
												var sel = jq(".bsdd-fullNames tr." + lang + " td[title='FULLNAME']"),
													gr = null;
												if (sel) {
													sel.each(function() {
														if (!gr) {
															gr = document.createElement("optgroup");
															jq(gr).attr("label", lang);
															jq(shortList).append(gr);
														}
														var y = jq(this), val = y.html(), li = document.createElement("option");
														jq(li).html(val).attr("value", y.parentsUntil(".bsdd-IfdConcept").parent().attr("bsdd-guid"));
														jq(gr).append(li);
													});
													var my_options = jq(gr).children();
													my_options.sort(function(a,b) {
														var at = a.text.toLowerCase(),
															bt = b.text.toLowerCase();
													    if (at > bt) return 1;
													    else if (at < bt) return -1;
													    else return 0;
													});
													jq(gr).empty().append( my_options );
												}
											};
											addToShortlist("ifc-2X2");
											addToShortlist("ifc-2X4");
											addToShortlist("de-DE");
											addToShortlist("de-AT");
											addToShortlist("de-CH");
											if (l.indexOf("ENGLISH") >= 0) {
												addToShortlist("en");
												addToShortlist("en-US");
												addToShortlist("en-GB");
												addToShortlist("en-CA");
												addToShortlist("en-AU");
												addToShortlist("en-NZ");
											}
										}
										x.dialog( "option", "buttons", [{text: i18n.getButton("DLG_BTN_CLOSE"), click: function () {jq(this).dialog("close");} }]);
									} else {
										var c = response.IfdConcept;
										var div = document.createElement("div");
										x.append(div);
										ifd.IfdConcept.prototype.toInfoBox.call(this, div, c);
									}
								} else {
									jq(document).trigger("alert", [{
										title: "DLG_TITLE_FREEBIM_INFO", 
										content: i18n.g("BSDD_NOTHING_FOUND"), // "keine Einträge für " + t + " '" + s + "' gefunden.",
										autoClose : 2000 
									}]);
								}
							},
							close : function () {
								x.remove();
							}
						});
						if (data.callback && (!jq.isArray(response.IfdConcept) || response.IfdConcept.length == 1)) {
							var guid = response.IfdConcept.guid, btns = [{text: i18n.getButton("DLG_BTN_CLOSE"), click: function () {jq(this).dialog("close");} }];
							if (at.freebim.db.contributor) {
								btns.push({
								      text: i18n.getButton("DLG_BTN_USE_BSDD_GUID"),
								      click: function() {
							    		  data.callback(guid);
								    	  jq(this).dialog( "close" );
								      }
								});
							}
							x.dialog( "option", "buttons", btns);
						}
					} else {
						jq(document).trigger("alert", [{
							title: "DLG_TITLE_FREEBIM_INFO", 
							content: i18n.g("BSDD_NOTHING_FOUND"), // "keine Einträge für " + t + " '" + s + "' gefunden.",
							autoClose : 2000 
						}]);
					}
				},
				i18n.g1("BSDD_FIND", s) // "suche in bsDD: '" + s + "' ..."
			);
		
		});
	},
	
	bsddDlg : null,
	
	getConceptTypeFromClassName : function (cn) {
		switch (cn) {
		case "Component" : 		return "SUBJECT";
		case "Parameter" : 		return "PROPERTY";
		case "Unit" : 			return "UNIT";
		case "Measure" : 		return "MEASURE";
		case "ValueListEntry" : return "VALUE";
		case "Document" : 		return "DOCUMENT";
		}
	},
	
	createIfdConceptForNode : function (node) {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, i18n = net.spectroom.js.i18n, 
			ins = db.bsdd.instantiate, lib, lang = "de-AT", 
			c = Object.create(ifd.IfdConcept), v;
			
		c.conceptType = db.bsdd.getConceptTypeFromClassName(node[nf.CLASS_NAME]);
		
		// get language from referenced Library
		if (node[nf.REFERENCES] && node[nf.REFERENCES].length > 0) {
			lib = d.get(node[nf.REFERENCES][0][rf.TO_NODE]);
			lang = lib[nf.LANGUAGE];
		}
		
		c.guid = i18n.g("ND");
		c.versionId = i18n.g("ND"); // "1"; // String
		c.versionDate = i18n.g("ND"); // new Date().toISOString(); // String
		c.status = i18n.g("ND"); // ifd.IfdStatusEnum.APPROVED;
		
		v = node[nf.CODE];
		if (v != undefined && v.length > 0) {
			c.shortNames = [];
			c.shortNames = [ins.IfdName(v, ifd.IfdNameTypeEnum.SHORTNAME, lang)];
		}
		
		v = node[nf.NAME];
		if (v != undefined && v.length > 0) {
			c.fullNames = [];
			c.fullNames.push(ins.IfdName(v, ifd.IfdNameTypeEnum.FULLNAME, lang));
		}
		
		v = node[nf.NAME_EN];
		if (v != undefined && v.length > 0) {
			c.fullNames = c.fullNames || [];
			c.fullNames.push(ins.IfdName(v, ifd.IfdNameTypeEnum.FULLNAME, "en"));
		}
		
		v = node[nf.DESC];
		if (v != undefined && v.length > 0) {
			c.definitions = [];
			c.definitions.push(ins.IfdDescription(v, ifd.IfdDescriptionTypeEnum.DEFINITION , lang));
		}
		
		v = node[nf.DESC_EN];
		if (v != undefined && v.length > 0) {
			c.definitions = c.definitions || [];
			c.definitions.push(ins.IfdDescription(v, ifd.IfdDescriptionTypeEnum.DEFINITION, "en"));
		}
		
		v = node[nf.COMMENT];
		if (v != undefined && v.length > 0) {
			c.comments =[]; // IfdDescription[]
			c.definitions.push(ins.IfdDescription(v, ifd.IfdDescriptionTypeEnum.COMMENT, lang));
		}
		return c;
	},
	
	loadByGuid : function (guid, callback) {
		var b = at.freebim.db.bsdd; 
		if (b.bsddDlg) {
			b.bsddDlg.remove();
		}
		b.bsddDlg = net.spectroom.js.newDiv();
		jq(b.bsddDlg).dialog({
			title : "buildingSMART Data Dictionary",
			width: 600,
			height: 400,
			modal : false,
			open : function () {
				jq(document).trigger("IfdConcept/GET", [{guid: guid, div: b.bsddDlg, callback: callback}]);
			},
			close : function () {
				jq(b.bsddDlg).remove();
				b.bsddDlg = null;
			}
		});
	},
	
	simplifyText : function (txt) {
		if (txt) {
	    	txt = txt.replace(/⁰/g, "0");
	    	txt = txt.replace(/¹/g, "1");
	    	txt = txt.replace(/²/g, "2");
	    	txt = txt.replace(/³/g, "3");
	    	txt = txt.replace(/⁴/g, "4");
	    	txt = txt.replace(/⁵/g, "5");
	    	txt = txt.replace(/⁶/g, "6");
	    	txt = txt.replace(/⁻/g, "-");
	    	txt = txt.replace(/⁺/g, "+");
    	}
		return txt;
	},
	
	upload : function (ifdConcept, callback, depth) {
		var db = at.freebim.db, b = db.bsdd, js = net.spectroom.js, fa = js.forAll, complete, i18n = net.spectroom.js.i18n;
		
		if (!db.user || !db.user.isEdit) {
			jq(document).trigger("alert", [{
				title: "DLG_TITLE_FREEBIM_INFO", 
				content: i18n.g("LOGIN_NO_AUTH"),
				autoClose : null
			}]);
			return;
		}
		
		if (!depth) {
			depth = 1;
		}
		if (depth > 100) {
			// theoretically, if isValidGuid() fails ...
			jq(document).trigger("alert", [{
				title: "DLG_TITLE_FREEBIM_ERROR", 
				content: i18n.get("BSDD_CANT_CREATE")
			}]);
			callback(null);
			return;
		}
		if (b.isValidGuid(ifdConcept.guid)) {
			
			// update an existing IfdConcept ...
			
			fa(ifdConcept.fullNames, function (ifdName, i) {
				if (!b.isValidGuid(ifdName.guid)) {
					at.freebim.db.bsdd.doAjax("POST", "IfdConcept/" + ifdConcept.guid + "/name", 
						{
							languageGuid: ifdName.language.guid,
							name: ifdName.name,
							nameType: ifdName.nameType
						},
			            function (response, textStatus) {
			            },
			            i18n.get("BSDD_SAVE_NAME", ifdName.name) // "speichere '" + ifdName.name + "' ins bsDD ..."
			        );
				}	
			});
			fa(ifdConcept.shortNames, function (ifdName, i) {
				if (!b.isValidGuid(ifdName.guid)) {
					at.freebim.db.bsdd.doAjax("POST", "IfdConcept/" + ifdConcept.guid + "/name", 
						{
							languageGuid: ifdName.language.guid,
							name: ifdName.name,
							nameType: ifdName.nameType
						},
			            function (response, textStatus) {
			            },
			            i18n.get("BSDD_SAVE_NAME", ifdName.name) // "speichere '" + ifdName.name + "' ins bsDD ..."
			        );
				}	
			});
			fa(ifdConcept.lexemes, function (ifdName, i) {
				if (!b.isValidGuid(ifdName.guid)) {
					at.freebim.db.bsdd.doAjax("POST", "IfdConcept/" + ifdConcept.guid + "/name", 
						{
							languageGuid: ifdName.language.guid,
							name: ifdName.name,
							nameType: ifdName.nameType
						},
			            function (response, textStatus) {
			            },
			            i18n.get("BSDD_SAVE_NAME", ifdName.name) // "speichere '" + ifdName.name + "' ins bsDD ..."
			        );
				}	
			});
			fa(ifdConcept.definitions, function (ifdDesc, i) {
				if (!b.isValidGuid(ifdDesc.guid)) {
					at.freebim.db.bsdd.doAjax("POST", "IfdConcept/" + ifdConcept.guid + "/definition", 
						{
							languageGuid: ifdDesc.language.guid,
							definition: ifdDesc.description
						},
			            function (response, textStatus) {
			            },
			            i18n.get("BSDD_SAVE_NAME", ifdDesc.description) // "speichere '" + ifdDesc.description + "' ins bsDD ..."
			        );
				}	
			});
			fa(ifdConcept.comments, function (ifdDesc, i) {
				if (!b.isValidGuid(ifdDesc.guid)) {
					at.freebim.db.bsdd.doAjax("POST", "IfdConcept/" + ifdConcept.guid + "/comment", 
						{
							languageGuid: ifdDesc.language.guid,
							comment: ifdDesc.description
						},
			            function (response, textStatus) {
			            },
			            i18n.get("BSDD_SAVE_NAME", ifdDesc.description) // "speichere '" + ifdDesc.description + "' ins bsDD ..."
			        );
				}	
			});
			
			callback(ifdConcept.guid);

			
		} else {
			
			// create a new IfdConcept ...
			
			var data = {
				fullNameGuids: "", 	// a comma separated list of GUIDs for existing fullnames. 
									// Must contain minimum two GUIDs. 
									// One for a name in international English and one for a name in another language.	form	
				definitionGuids: "", // a comma separated list of GUIDs for existing defintions. Can be empty.	form	
				commentGuids: "", // a comma separated list of GUIDs for existing comments. Can be empty.	form	
				conceptType: ifdConcept.conceptType, // the concept type	form	
				shortNameGuids: "", // a comma separated list of the short name GUIDs	form	
				lexemeGuids: "", // a comma separated list of the lexeme GUIDs	form	
				illustrationGuids: "", // a comma separated list of the illustration GUIDs	form	
				ownerGuid: js.cookie.get(b.userGuid), // the GUID of the owner	form
				callback: callback 	// this callback funtion is called after successfully saving the 
									// IfdConcept with it's newly created guid as parameter.
			};
			
			if (ifdConcept.fullNames && ifdConcept.fullNames.length > 1) {
				complete = fa(ifdConcept.fullNames, function (ifdName, i) {
					if (b.isValidGuid(ifdName.guid)) {
						if (i > 0) {
							data.fullNameGuids += ",";
						}
						data.fullNameGuids += ifdName.guid;
					} else {
						jq(document).trigger("IfdName-ADD", [{ IfdName: ifdName, callback: 
							function (guid) {
								ifdName.guid = guid;
								b.upload(ifdConcept, callback, depth + 1);
								return;
							}
						}]);
						return false;
					}	
				});
				if (!complete) {
					return;
				}
			} else {
				jq(document).trigger("alert", [{
					title: "DLG_TITLE_FREEBIM_ERROR", 
					content: i18n.get("BSDD_CANT_CREATE") + ". " + i18n.get("BSDD_MISSING_NAME")// "bsDD-Objekt kann nicht erzeugt werden, es müssen mindestens zwei Bezeichnungen vorhanden sein."
				}]);
				return;
			}
			
			complete = fa(ifdConcept.shortNames, function (ifdName, i) {
				if (b.isValidGuid(ifdName.guid)) {
					if (i > 0) {
						data.shortNameGuids += ",";
					}
					data.shortNameGuids += ifdName.guid;
				} else {
					jq(document).trigger("IfdName-ADD", [{ IfdName: ifdName, callback: 
						function (guid) {
							ifdName.guid = guid;
							b.upload(ifdConcept, callback, depth + 1);
							return;
						}
					}]);
					return false;
				}
			});
			if (!complete) {
				return;
			}

			complete = fa(ifdConcept.definitions, function (ifdDesc, i) {
				if (b.isValidGuid(ifdDesc.guid)) {
					if (i > 0) {
						data.definitionGuids += ",";
					}
					data.definitionGuids += ifdDesc.guid;
				} else {
					jq(document).trigger("IfdDescription-ADD", [{ IfdDescription: ifdDesc, callback: 
						function (guid) {
							ifdDesc.guid = guid;
							b.upload(ifdConcept, callback, depth + 1);
							return;
						}
					}]);
					return false;
				}
			});
			if (!complete) {
				return;
			}

			jq(document).trigger("IfdConcept/add", data);
		}
	},
	
	langFamilyFromCode: function (lang) {
		switch (lang) {
		default:
		case "de-AT":
		case "de-DE":
		case "de-CH":
			return "GERMAN";
		case "en":
			return "ENGLISH";
		case "IFC2X4":
			return "IFC";
		}
	},

	instantiate : {
		IfdName : function (name, type, lang) {
			var b = at.freebim.db.bsdd, x = new ifd.IfdName();
			x.name = name;
			x.nameType = type;
			x.language = b.languages[lang];
			x.languageFamily = b.langFamilyFromCode(lang);
			return x;
		},
		IfdDescription : function (description, type, lang) {
			var b = at.freebim.db.bsdd, x = new ifd.IfdDescription();
			x.description = description;
			x.descriptionType = type;
			x.language = b.languages[lang];
			x.languageFamily = b.langFamilyFromCode(lang);
			return x;
		}
	},
	
	divOfIfdConcept : function () {
		var c = at.freebim.db.bsdd.currentIfdConcept;
		if (c) {
			return jq(".bsdd-IfdConcept[bsdd-guid='" + c.guid + "']");
		}
	},
	
	makeBsddDroppable : function (activate) {
		var db = at.freebim.db, div = db.bsdd.divOfIfdConcept();
		// some stuff is draggable from from freeBIM to bsDD
		if (div) {
			if (activate) {
				if (!jq(div).hasClass("ui-droppable")) {
					jq(div).droppable({
						accept: "p",
						hoverClass: "drop-hover",
						tolerance: "pointer",
						drop: function(event, ui) {
							db.bsdd.fromFreebimToBsdd(jq(ui.draggable));
						}
					});
				}
			} else {
				if (jq(div).hasClass("ui-droppable")) {
					jq(div).droppable("destroy");
				}
			}
		}	
	},
	
	isValidGuid : function (g) {
		if (g) {
			var m = g.match("^[0-9a-zA-Z_$]{22}$");
			if (m === null) {
				m = g.match("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
			}
			return (m != null);
		}
		return false;
	},
	
	makeFreebimDroppable : function (activate) {
		var db = at.freebim.db, nf = db.domain.NodeFields, b = db.bsdd,
			div = jq(".ui-dialog-content[freebimid='" + b.currentFreebimNode[nf.FREEBIM_ID] + "']").find(".content");
		if (activate) {
			if (!div.hasClass("ui-droppable")) {
				// some stuff is draggable from from bsDD to freeBIM
				div.droppable({
					accept: "td, span.GUID",
					hoverClass: "drop-hover",
					tolerance: "pointer",
					drop: function(event, ui) {
						b.fromBsddToFreebim(jq(ui.draggable));
					}
				});
			}
		} else {
			if (div.hasClass("ui-droppable")) {
				div.droppable("destroy");
			}
		}

	},
	
	makeFreebimDraggable : function (activate) {
		var b = at.freebim.db.bsdd, nf = at.freebim.db.domain.NodeFields, i18n = net.spectroom.js.i18n;
		if (activate) {
			// some stuff is draggable from freeBIM to bsDD
			jq(".ui-dialog-content").find(".content").find("#field_" + nf.NAME + ", #field_" + nf.NAME_EN + ", #field_" + nf.DESC + ", #field_" + nf.DESC_EN + ", #field_" + nf.CODE).parent()
				.draggable({ helper: "clone" })
				.addClass("fromFreebimToBsdd")
				.off("dblclick")
				.dblclick(function () { 
					b.fromFreebimToBsdd(jq(this));
				});
			
			jq(".fromFreebimToBsdd").each(function () {
				var x = jq(this);
				x.attr("orig-title", x.attr("title")).attr("title", i18n.g("BSDD_DRAGNDROP_TO_BSDD")).attr("i18n_title", "BSDD_DRAGNDROP_TO_BSDD");
			});

		} else {
			var f = jq(".fromFreebimToBsdd");
			f.each(function () {
				var x = jq(this);
				x.attr("title", x.attr("orig-title"));
			});
			if (f.hasClass("ui-draggable")) {
				f.draggable("destroy");
			}
			f.off("dblclick").removeClass("fromFreebimToBsdd");
		}
	},
	
	makeBsddDraggable : function (activate) {
		var i18n = net.spectroom.js.i18n;
		if (activate) {
			// some stuff is draggable from bsDD to freeBIM
			var b = at.freebim.db.bsdd, div = b.divOfIfdConcept(),
				l = ".bsdd-fullNames ", c = ".bsdd-shortNames ", d = ".bsdd-definitions ", 
				e = "tr.de-DE td[title='", f = "tr.en td[title='", g = "tr.en-GB td[title='", k = "tr.en-US td[title='", m = "tr.de-AT td[title='",
				h = "FULLNAME'], ", i = "SHORTNAME'], ", j = "DESCRIPTION'], ", 
				x = jq(div).find(
					l + e + h +
					l + m + h +
					l + f + h +
					l + g + h +
					l + k + h +
					c + e + i +
					c + m + i +
					c + f + i +
					c + g + i +
					c + k + i +
					d + e + j +
					d + m + j +
					d + f + j +
					d + g + j +
					d + k + j +
					"div.bsdd-IfdBase span.GUID");
			x.addClass("fromBsddToFreebim")
			.draggable({ helper: "clone" })
			.off("dblclick")
			.dblclick(function () {
				b.fromBsddToFreebim(jq(this));
			});
			jq(".fromBsddToFreebim").each(function () {
				var x = jq(this);
				x.attr("orig-title", x.attr("title")).attr("title", i18n.g("BSDD_DRAGNDROP_TO_FREEBIM")).attr("i18n_title", "BSDD_DRAGNDROP_TO_FREEBIM");
			});
		} else {
			var f = jq(".fromBsddToFreebim");
			f.each(function () {
				var x = jq(this);
				x.attr("title", x.attr("orig-title"));
			});
			if (f.hasClass("ui-draggable")) {
				f.draggable("destroy");
			}
			f.off("dblclick").removeClass("fromBsddToFreebim");
		}
	},
	
	setFromFreebimToBsdd : function (activate) {
		var b = at.freebim.db.bsdd;
		if (activate && !net.spectroom.js.cookie.get(b.cookie)) {
			return;
		}
		b.makeFreebimDraggable(activate);
		b.makeBsddDroppable(activate);		
	},
	
	setFromBsddToFreebim : function (activate) {
		var b = at.freebim.db.bsdd;
		b.makeBsddDraggable(activate);
		b.makeFreebimDroppable(activate);
	},
	
	fromFreebimToBsdd : function (e) {
		// create IfdName objects corresponding to the dragged element
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, 
			lib, lang = "de-AT", x = e.find(".jsForm-field"), id = x.attr("id"), 
			ifdElem, ins = db.bsdd.instantiate,
			val = x.val(), c = db.bsdd.currentIfdConcept,
			node = db.bsdd.currentFreebimNode;
		if (node != undefined && c != undefined && val != undefined && val.length > 0) {
			
			// get language from referenced Library
			if (node[nf.REFERENCES] && node[nf.REFERENCES].length > 0) {
				lib = d.get(node[nf.REFERENCES][0][rf.TO_NODE]);
				lang = lib[nf.LANGUAGE];
			}
			id = id.replace("field_", "");
			switch (id) {
			case nf.CODE:
				ifdElem = ins.IfdName(val, ifd.IfdNameTypeEnum.SHORTNAME, lang);
				c.shortNames = ((c.shortNames === undefined) ? [] : ((jq.isArray(c.shortNames)) ? c.shortNames : [c.shortNames]) );
				c.shortNames.push(ifdElem);
				break;
			case nf.NAME:
				ifdElem = ins.IfdName(val, ifd.IfdNameTypeEnum.FULLNAME, lang);
				c.fullNames = ((c.fullNames === undefined) ? [] : ((jq.isArray(c.fullNames)) ? c.fullNames : [c.fullNames]) );
				c.fullNames.push(ifdElem);
				break;
			case nf.NAME_EN:
				ifdElem = ins.IfdName(val, ifd.IfdNameTypeEnum.FULLNAME, "en");
				c.fullNames = ((c.fullNames === undefined) ? [] : ((jq.isArray(c.fullNames)) ? c.fullNames : [c.fullNames]) );
				c.fullNames.push(ifdElem);
				break;
			case nf.DESC:
				ifdElem = ins.IfdDescription(val, ifd.IfdDescriptionTypeEnum.DEFINITION, lang);
				c.definitions = ((c.definitions === undefined) ? [] : ((jq.isArray(c.definitions)) ? c.definitions : [c.definitions]) );
				c.definitions.push(ifdElem);
				break;
			case nf.DESC_EN:
				ifdElem = ins.IfdDescription(val, ifd.IfdDescriptionTypeEnum.DEFINITION, "en");
				c.definitions = ((c.definitions === undefined) ? [] : ((jq.isArray(c.definitions)) ? c.definitions : [c.definitions]) );
				c.definitions.push(ifdElem);
				break;
			case nf.COMMENT:
				ifdElem = ins.IfdDescription(val, ifd.IfdDescriptionTypeEnum.COMMENT, lang);
				c.comments = ((c.comments === undefined) ? [] : ((jq.isArray(c.comments)) ? c.comments : [c.comments]) );
				c.comments.push();
				break;
			}
			db.bsdd.renderIfdConcept();
			db.bsdd.makeBsddDroppable(true);
		}
	},
	
	fromBsddToFreebim : function (e) {
		// some defined fields could be set with values of the bsdd object
		var db = at.freebim.db, nf = db.domain.NodeFields, x = undefined, 
			node = db.bsdd.currentFreebimNode,
			contentDiv = jq(".ui-dialog-content").find(".content");
		
		if (e.closest("div").hasClass("bsdd-IfdBase")) {
			node[nf.BSDD_GUID] = e.find("input").val();
			x = jq(contentDiv).find("#field_" + nf.BSDD_GUID);
			if (at.freebim.db.bsdd.isValidGuid(node[nf.BSDD_GUID])) {
				x.removeClass("invalid");
			} else {
				x.addClass("invalid");
			}
			x.val(node[nf.BSDD_GUID]);
		} else if (e.closest("div").hasClass("bsdd-fullNames")) {
			// it's a full name
			if (e.closest("tr").hasClass("de-DE") || e.closest("tr").hasClass("de-AT") || e.closest("tr").hasClass("de-CH")) {
				node[nf.NAME] = e.html();
				x = jq(contentDiv).find("#field_" + nf.NAME);
				x.val(node[nf.NAME]);
			} else if (e.closest("tr").hasClass("en") || e.closest("tr").hasClass("en-GB") || e.closest("tr").hasClass("en-US")) {
				node[nf.NAME_EN] = e.html();
				x = jq(contentDiv).find("#field_" + nf.NAME_EN);
				x.val(node[nf.NAME_EN]);
			}
		} else if (e.closest("div").hasClass("bsdd-shortNames")) {
			// it's a short name
			node[nf.CODE] = e.html();
			x = jq(contentDiv).find("#field_" + nf.CODE);
			x.val(node[nf.CODE]);
		} else if (e.closest("div").hasClass("bsdd-definitions")) {
			// it's a definition
			if (e.closest("tr").hasClass("de-DE") || e.closest("tr").hasClass("de-AT") || e.closest("tr").hasClass("de-CH")) {
				node[nf.DESC] = e.html();
				x = jq(contentDiv).find("#field_" + nf.DESC); 
				x.val(node[nf.DESC]);
			} else if (e.closest("tr").hasClass("en") || e.closest("tr").hasClass("en-GB") || e.closest("tr").hasClass("en-US")) {
				node[nf.DESC_EN] = e.html();
				x = jq(contentDiv).find("#field_" + nf.DESC_EN);
				x.val(node[nf.DESC_EN]);
			}
		}
		if (x) {
			x.animate({
				borderWidth: "2px",
				borderColor: "black",
				backgroundColor: "yellow"
			}, 400, "swing", 
				function () { 
					x.animate({ 
						borderWidth: "1px",
						backgroundColor: "white",
						borderColor: "rgb(211, 211, 211)"
					}); 
				}
			);
		}
	},
	
	renderIfdConcept : function (div) {
		var b = at.freebim.db.bsdd, c = b.currentIfdConcept, i18n = net.spectroom.js.i18n;
		div = div || b.divOfIfdConcept();
		if (c && div) {
			ifd.IfdConcept.prototype.toInfoBox.call(this, div, c);
			jq(div).find("tr[guid='-']").append("<td class='delete-ifd' i18n_title='BSDD_REMOVE' title='" + i18n.g("BSDD_REMOVE") + "'>✗</td>");
			b.makeBsddDraggable(true);
		}
	},
	
	createHeadline : function (item, ifdConcept) {
		// set the accordion header
		jq(item.h).addClass("header").attr("guid", ifdConcept.guid);
		item.txt = [];
		
		// (german first, then english, then ifc)
		var lang = document.createElement("div"),
		addFullname = function (v) {
			var s = 0;
			switch (v.languageFamily) {
			case "ENGLISH":
				s = 1;
				break;
			case "GERMAN":
				break;
			case "IFC":
				s = 2;
				break;
			default:
				return;
			}
			item.txt.push({ s: s, lc: v.language.languageCode, n: v.name});
		};
		jq(lang).addClass("de");
		jq(item.h).append(lang);
		if (ifdConcept.fullNames) {
			net.spectroom.js.forAll(ifdConcept.fullNames, function (v) {
				addFullname(v);
			});
		}
		
		item.txt.sort(function (t1, t2) {
			if (t1.s < t2.s) { return -1; }
			else if (t1.s > t2.s) { return 1; }
			else {
				if (t1.lc < t2.lc) { return -1; }
				else if (t1.lc > t2.lc) { return 1; }
				else {
					var n1 = t1.n.toLowerCase(), n2 = t2.n.toLowerCase();
					if (n1 < n2) { return -1; }
					else if (n1 > n2) { return 1; }
					else { return 0; }
				}
			}
		});
		item.s = "";
		jq.each(item.txt, function (i, txt) {
			var lc = txt.lc.substr(txt.lc.length - 2, 2).toUpperCase() + ".png",
				name = "<img src='/resources/flags/64/" + lc + "' alt='" + txt.lc + "' class='ifd-flag'>" + txt.n + " ";
			jq(lang).append(name);
			item.s += txt.s + "-" + txt.n;
		});
	}
};

jq(document).ready(function () {
	
	var db = at.freebim.db, d = db.domain, nf = d.NodeFields, rf = d.RelationFields, i18n = net.spectroom.js.i18n;
	
	db.bsdd.init();

	if (db.contributor) {
		jq(document).trigger("IfdLanguage", [{}]);
		jq(document).trigger("bsdd-login", [{ parentId: "#bsdd" }]);
		
		jq(document).delegate("input[type='button'].delete-bsdd-guids", "click", function (event, data) {
			if (db.bsdd.currentFreebimNode) {
				var bsddData = jq(this).closest(".bsdd-content").find(".bsdd-data");
				bsddData.empty();
				
				db.bsdd.currentFreebimNode[nf.LOADED_BSDD] = [];
				jq(this).closest(".ui-dialog-content").find(".bsdd-button").prev().val("");
				
				if (net.spectroom.js.cookie.get(at.freebim.db.bsdd.cookie)) {
					// we are logged in, so we can
					// create a new IfdConcept
					var c = db.bsdd.createIfdConceptForNode(db.bsdd.currentFreebimNode);
					db.bsdd.currentIfdConcept = c;
					
					// render the content of that IfdConcept 
					db.bsdd.renderIfdConcept(bsddData);
					
					db.bsdd.setFromFreebimToBsdd(true);
					db.bsdd.setFromBsddToFreebim(true);
					
				} else {
					jq(document).trigger("alert", [{
						title: "DLG_TITLE_FREEBIM_INFO", 
						content: i18n.get(""), // "Nicht bei bsDD-Server angemeldet.",
						autoClose : 1500
					}]);
				}
				jq(this).hide();

			}
		});
		
		/**
		 * @param data.parentId ID of login container
		 */
		jq(document).on("bsdd-is-logged-in", function (event, data) {
			if (db.bsdd.currentFreebimNode && db.bsdd.currentIfdConcept) {
				db.bsdd.setFromFreebimToBsdd(true);
			}
		});

		/**
		 * @param data.parentId ID of login container
		 */
		jq(document).on("bsdd-is-logged-out", function (event, data) {
			if (db.bsdd.currentFreebimNode && db.bsdd.currentIfdConcept) {
				db.bsdd.setFromFreebimToBsdd(false);
			}
		});
		
		jq(document).delegate("td.delete-ifd", "click", function (event, data) {
			var c = db.bsdd.currentIfdConcept;
			if (db.bsdd.currentIfdConcept) {
				var btn = jq(this), tr = btn.parent(), rowIndex = tr.index(), type = tr.closest("div").attr("class");
				switch (type) {
				case "bsdd-shortNames":
					c.shortNames.splice(rowIndex, 1);
					if (c.shortNames.length == 0) {
						c.shortNames = null;
					}
					break;
				case "bsdd-fullNames":
					c.fullNames.splice(rowIndex, 1);
					if (c.fullNames.length == 0) {
						c.fullNames = null;
					}
					break;
				case "bsdd-definitions":
					c.definitions.splice(rowIndex, 1);
					if (c.definitions.length == 0) {
						c.definitions = null;
					}
					break;
				case "bsdd-comments":
					c.comments.splice(rowIndex, 1);
					if (c.comments.length == 0) {
						c.comments = null;
					}
					break;
				}
				db.bsdd.renderIfdConcept();
			}
			
		});

	}
	
	
	jq(document).delegate(".bsdd-button", "click", function (event, data) {
		var th = jq(this),
			nodeId = th.attr("nodeid"), cn = th.attr("cn"),
			guid = th.prev().val();
		
		db.bsdd.currentIfdConcept = undefined;
		db.bsdd.currentIfdConcepts = {};
		
		// get the node, i.e. a Component
		d.getOrLoad(nodeId, cn, function (node) {
			db.bsdd.currentFreebimNode = node;

			var contentDiv = th.closest(".ui-dialog-content"),
			dlg = jq(d[cn].form.parent),
			bsddContent = document.createElement("div"),
			
			cleanup = function () {
				dlg
				.dialog( "option", "width", net.spectroom.js.form.w )
				.dialog( "option", "height", net.spectroom.js.form.h )
				.off( "dialogbeforeclose" );
				// cleanup
				jq(contentDiv).find(".bsdd-content").remove();
				jq(contentDiv).children().css("width", "");
				jq(contentDiv).attr("bsddContent", "");
				db.bsdd.setFromFreebimToBsdd(false);
			},
			head = document.createElement("div"),
			bsddData = document.createElement("div");
			
			jq(contentDiv).attr("freebimid", node[nf.FREEBIM_ID]);
			
			// clicked button will toggle display of bsdd content
			if (jq(contentDiv).attr("bsddContent") === "1") {
				th.attr("title", i18n.g("BSDD_SHOW_GUIDS")).attr("i18n_title", "BSDD_SHOW_GUIDS"); // "bsDD-Guids anzeigen");
				cleanup();
				return;
			}
			jq(contentDiv).attr("bsddContent", "1").children().css("width", "48%");
			th.attr("title", i18n.g("BSDD_HIDE_GUIDS")).attr("i18n_title", "BSDD_HIDE_GUIDS");
			
			// adjust current dialog width to screen width (almost)
			dlg.dialog( "option", "width", jq(window).width() - 40 )
				.dialog( "option", "height", jq(window).height() - 40 )
				.on( "dialogbeforeclose", function( event, ui ) {
					cleanup();
				});
	
			jq(contentDiv).append(bsddContent);
			jq(bsddContent).append(head).append(bsddData);
			jq(bsddData).addClass("bsdd-data");
			
			jq(bsddContent).addClass("bsdd-content");
			jq(head).addClass("bsdd-head").append("<img alt='bsDD' src='/resources/ifc-logo.png'>");
			
			// show bsdd-login 
			jq(head).append("<div id='bsdd-login2'></div>");
			jq(document).trigger("bsdd-login", [{ parentId: "#bsdd-login2" }]);
			
			
			if (db.bsdd.isValidGuid(guid)) {
				
				node[nf.BSDD_GUID] = guid;

				// it' s an already assigned bsdd-Guid, show content from buildingsmart.org 
				
				at.freebim.db.bsdd.doAjax("GET", "IfdConcept/" + node[nf.BSDD_GUID], {},
			        	function (response, textStatus, jqXHR) {
					
							// render the content of that IfdConcept 
//							ifd.IfdConcept.prototype.toInfoBox.call(this, bsddData, response);
							db.bsdd.currentIfdConcepts[response.guid] = response;
							db.bsdd.currentIfdConcept = response;
							
							db.bsdd.renderIfdConcept(bsddData);
							
							db.bsdd.setFromFreebimToBsdd(true);
							db.bsdd.setFromBsddToFreebim(true);
				}, 
				i18n.g("BSDD_LOAD_OBJECT"),
				function (error) {
				}); // doAjax
	        	return;
			}


			
			var bsdd = node[nf.LOADED_BSDD], bsddCount;
			if (bsdd && bsdd.length > 0) {
				bsddCount = bsdd.length;
				
				// show fetched bsdd-guids
				
				jq(head).append("<div class='bsdd-acc-buttons'><input type='button' class='std-button delete-bsdd-guids' value='" + i18n.getButton("BSDD_IGNORE") + "' i18n_title='BSDD_IGNORE_TITLE' title='" + i18n.g("BSDD_IGNORE_TITLE") + "'></div>");
				
				// create the accordion
				var items = [], acc = document.createElement("div"), 
				createAcc = function () {
					// sort them and create accordion
					items.sort(function (i1, i2) {
						var a = i1.s,
							b = i2.s;
						if (a < b) { return -1; }
						else if (a > b) { return 1; }
						else return 0;
					});
					jq.each(items, function (idx, it) {
						jq(acc).append(it.h);
						jq(acc).append(it.d);
					});
					
					
					jq(acc).accordion({
						header: "h3",
						active: false,
						collapsible: true,
						heightStyle: "content",
						icons: { "header": "ui-icon-plus", "activeHeader": "ui-icon-minus" },
						activate: function ( event, ui ) {
							
							db.bsdd.currentIfdConcept = db.bsdd.currentIfdConcepts[jq(ui.newHeader).attr("guid")];
							db.bsdd.setFromFreebimToBsdd((db.bsdd.currentIfdConcept != undefined));
							db.bsdd.setFromBsddToFreebim((db.bsdd.currentIfdConcept != undefined));
							
						}
					}); // accordion
				};
				jq(bsddData).append(acc);
				
				// for aech Bsdd relation
				jq.each(bsdd, function (i, r) {
					
					// get the BsddNode
					d.getOrLoad(r[rf.FROM_NODE], "BsddNode", function (bsddNode) {
						
						if (db.bsdd.isValidGuid(bsddNode[nf.BSDD_GUID])) {
							
							var item = { h: document.createElement("h3"), d: document.createElement("div")};
							items.push(item);
							
							// get the content from buildingsmart.org
							at.freebim.db.bsdd.doAjax("GET", "IfdConcept/" + bsddNode[nf.BSDD_GUID], {},
						        	function (response, textStatus, jqXHR) {
								
										bsddCount--;
										
										db.bsdd.createHeadline(item, response);
										
										// render the content of that IfdConcept 
										ifd.IfdConcept.prototype.toInfoBox.call(this, item.d, response);
										db.bsdd.currentIfdConcepts[response.guid] = response;
										
										if (bsddCount === 0) {
											// got last bsdd relation,
											createAcc();
	
										} // // got last bsdd relation
	
							}, 
							i18n.g("BSDD_LOAD_OBJECT"),
							function (error) {
								bsddCount--;
								// error getting last bsdd relation
								if (bsddCount === 0) {
									createAcc();
								}
							}); // doAjax
							
						} else {
							bsddCount--;
						}
						
					}); // // get the BsddNode
				}); // // for aech Bsdd relation

			} else {
				
				// create a new IfdConcept
				
				var c = db.bsdd.createIfdConceptForNode(node);
				db.bsdd.currentIfdConcept = c;
				
				// render the content of that IfdConcept 
				db.bsdd.renderIfdConcept(bsddData);
				
				db.bsdd.setFromFreebimToBsdd(true);
				db.bsdd.setFromBsddToFreebim(true);
			}
			
			

		});	// get the node, i.e. a Component
	}); // jq(document).delegate(".bsdd-button", "click", ...)
	
	
});

