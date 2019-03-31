/**
 * 
 */
var net = net || {};
net.spectroom = net.spectroom || {};
net.spectroom.js = net.spectroom.js || {};

net.spectroom.js.cookie = net.spectroom.js.cookie || {
	
	set : function (cname, cvalue, exdays, domain, path) {
	    var d = new Date(), expires;
	    d.setTime(d.getTime() + (exdays*24*60*60*1000));
	    expires = "expires="+d.toGMTString();
	    document.cookie = cname + "=" + cvalue + "; " + expires + ((domain) ? "; Domain=" + domain : "") + ((path) ? "; Path=" + path : "");
	},

	get : function (cname) {
	    var i, c, name = cname + "=", ca = document.cookie.split(';');
	    for(i=0; i<ca.length; i++) {
	        c = ca[i].trim();
	        if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
	    }
	    return "";
	},
	
	clear : function (cname) {
		net.spectroom.js.cookie.set(cname, "", -1);
	}
	
	
};

