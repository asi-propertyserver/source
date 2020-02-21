function setCookieMill(cname, cvalue, mill, domain, path) {
    var d = new Date(), expires;
    d.setTime(d.getTime() + mill);
    expires = "expires=" + d.toGMTString();
    document.cookie = cname + "=" + cvalue + "; " + expires + ((domain) ? "; Domain=" + domain : "") + ((path) ? "; Path=" + path : "");
}

at.freebim.db.request = {
    get: function (url, data) {
        return new Promise((resolve, reject) => {
            jq.get(url, data, function (response) {
                resolve(response);
            }).fail(function (error) {
                if (error.status == 401 || JSON.parse(error.responseText).message == "Expired or invalid JWT token") {
                    let re = resolve;
                    let rej = reject;
                    at.freebim.db.request.refreshToken().then((resolve) => {
                        if (resolve) {
                            at.freebim.db.request.get(url, data).then((r) => {
                                re(r);
                            }).catch((e) => {
                                rej(e);
                            });
                        }
                    }).catch((e) => {
                        reject(error);
                    });
                } else {
                    reject(error);
                }
            });
        });
    },
    post: function (url, data) {
        return new Promise((resolve, reject) => {
            jq.post(url, data, function (response) {
                resolve(response);
            }).fail(function (error) {
                if (error.status == 401 || JSON.parse(error.responseText).message == "Expired or invalid JWT token") {
                    let re = resolve;
                    let rej = reject;
                    at.freebim.db.request.refreshToken().then((resolve) => {
                        if (resolve) {
                            at.freebim.db.request.post(url, data).then((r) => {
                                re(r);
                            }).catch((e) => {
                                rej(e);
                            });
                        }
                    }).catch((e) => {
                        reject(error);
                    });
                } else {
                    reject(error);
                }
            });
        });
    },
    doAjax: function (type, url, data, msg) {
        var h = { "Accept": "application/json", "Bearer": localStorage.getItem("token") }, key = "_" + Math.random();
        key = key.replace(".", "");
        jq(document).trigger("show_progress", [{ key: key, msg: msg }]);

        return new Promise((resolve, reject) => {
            jq.ajax({
                headers: h,
                xhrFields: { withCredentials: true },
                url: at.freebim.db.bsdd.getURL(url),
                type: type,
                dataType: 'json',
                contentType: 'application/json',
                data: data,
                crossDomain: true,
                success: function (response, textStatus, jqXHR) {
                    jq(document).trigger("hide_progress", [{ key: key }]);
                    // var nextPage = jqXHR.getResponseHeader("Next-Page");
                    if (response.result) {
                        resolve(response.result, textStatus, jqXHR);
                    }
                },
                error: function (response, textStatus) {
                    if (response.status == 401 || JSON.parse(response.responseText).message == "Expired or invalid JWT token") {
                        let re = resolve;
                        let rej = reject;
                        jq(document).trigger("hide_progress", [{ key: key }]);
                        at.freebim.db.request.refreshToken().then((resolve) => {
                            if (resolve) {
                                at.freebim.db.request.doAjax(type, url, data, msg).then((r) => {
                                    re(r);
                                }).catch((e) => {
                                    rej(e);
                                });
                            }
                        }).catch((e) => {
                            jq(document).trigger("hide_progress", [{ key: key }]);
                            jq(document).trigger("alert", [{
                                title: "DLG_TITLE_FREEBIM_ERROR", // "freeBIM
                                // - ERROR",
                                content: textStatus + ": " + response.responseText
                            }]);
                            reject(response);
                        });
                    } else {
                        reject(response);
                    }
                }
            });
        });
    },
    login: function () {
        let username = jq("#j_username").val();
        let password = jq("#j_password").val();

        let data = {
            username: username,
            password: password
        };


        jq.ajax({
            url: "/login/rest",
            contentType: "application/json; charset=utf-8",
            headers: { "Accept": "application/json" },
            data: JSON.stringify(data),
            type: "POST",
            success: function (result) {
                if (result) {
                    setCookieMill("token", result.token, window.validity, "", "/");
                    localStorage.setItem("refresh-token", result.refreshToken);

                    document.location.href = "/"
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                jq("#login_error_box").show();
            }
        });

        return false;
    },
    request: function (url, payload, msg, timeout, method) {
        return new Promise((resolve, reject) => {
            var db = at.freebim.db, d = db.domain, i18n = net.spectroom.js.i18n, key = "_" + Math.random();
            key = key.replace(".", "");
            jq(document).trigger("show_progress", [{ key: key, msg: msg }]);

            let met = method == null ? "POST" : method;
            jq.ajax({
                url: url,
                method: met,
                data: met !== "GET" ? JSON.stringify(payload) : payload,
                contentType: "application/json; charset=utf-8",
                success: function (response) {
                    jq(document).trigger("hide_progress", [{ key: key }]);
                    if (response.error != undefined) {
                        alert(response.error);
                        return;
                    } else if (response.accessDenied) {
                        jq(document).trigger("alert", [{
                            title: "DLG_TITLE_FREEBIM_INFO",
                            content: i18n.g("LOGIN_NO_AUTH"),
                            autoClose: null
                        }]);
                        return;
                    }
                    resolve(response);
                    if (response.savedNodes) {
                        try {
                            d.handleSavedNodes(response.savedNodes);
                        } catch (e) {
                            db.logger.error("Error in handleSavedNodes: " + e.message);
                        }
                    }
                },
                error: function (error) {
                    if (error.status == 401 || JSON.parse(error.responseText).message == "Expired or invalid JWT token") {
                        let re = resolve;
                        let rej = reject;
                        jq(document).trigger("hide_progress", [{ key: key }]);
                        at.freebim.db.request.refreshToken().then((resolve) => {
                            if (resolve) {
                                at.freebim.db.request.request(url, payload, msg, timeout, method).then((r) => {
                                    re(r);
                                }).catch((e) => {
                                    rej(e);
                                });
                            }
                        }).catch((e) => {
                            jq(document).trigger("hide_progress", [{ key: key }]);
                            reject(error);

                        });
                    } else {
                        reject(error);
                    }
                },
                timeout: ((timeout) ? timeout : 0)
            });
        });
    },

    refreshToken: function () {
        let refreshToken = localStorage.getItem("refresh-token");

        return new Promise((resolve, reject) => {
            if (refreshToken != null) {
                net.spectroom.js.cookie.set("token", "", 0, "", "/");
                jq.ajax({
                    url: "/login/refresh/token",
                    method: "POST",
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify({ refreshToken: refreshToken }),
                    success: function (response) {
                        if (response.token) {
                            net.spectroom.js.cookie.set("token", response.token, 0.01041666667, "", "/");
                            resolve(true);
                        } else {
                            net.spectroom.js.cookie.set("token", "", 0, "", "/");
                            localStorage.removeItem("refresh-token");
                            document.location.href = "/";
                            resolve(false);
                        }
                    },
                    error: function (error) {
                        if (error.status == 401) {
                            net.spectroom.js.cookie.set("token", "", 0, "", "/");
                            localStorage.removeItem("refresh-token");
                            document.location.href = "/";
                        }
                        reject(false);
                    }
                });
            } else {
                reject(false);
            }
        });
    },

    logout: function () {
        net.spectroom.js.cookie.set("token", "", 0, "", "/");
        localStorage.removeItem("refresh-token");

        document.location.href = "/";
        return false;
    }
};

if (localStorage.getItem("refresh-token") != null && localStorage.getItem("refresh-token") != '') {

    // create tracker for ajax requests
    XMLHttpRequest.prototype._originalOpen = XMLHttpRequest.prototype.open;

    XMLHttpRequest.prototype.open = function (method, url, async, user, password) {
        if (url !== "/login/refresh/token") {
            let old_timeout = localStorage.getItem('last-request-timer');
            if (old_timeout) {
                clearTimeout(old_timeout);
            }

            let logout_timeout = localStorage.getItem('logout-timer');
            if (logout_timeout) {
                clearTimeout(logout_timeout);
            }



            if (localStorage.getItem("refresh-token")) {
                let timeout = setTimeout(function () {

                    alert('Inactivity! Logout in 10min!');
                    let logout = setTimeout(function () {
                        net.spectroom.js.cookie.set("token", "", 0, "", "/");
                        localStorage.removeItem("refresh-token");
                        document.location.href = "/";
                    }, 600000);

                    localStorage.setItem('logout-timer', logout);
                }, window.refresh_token_validity > 600000 ? Number(window.refresh_token_validity) - 600000 : Number(window.refresh_token_validity));

                localStorage.setItem('last-request-timer', timeout);
            }
        }

        this._originalOpen(method, url, async, user, password);
    }

    setInterval(function () {
        at.freebim.db.request.refreshToken().then(() => {
            at.freebim.db.logger.debug("Got new jwt token");
        }).catch(() => {
            at.freebim.db.logger.debug("Could not retrive new jwt token");
        })
    }, 60000);
}