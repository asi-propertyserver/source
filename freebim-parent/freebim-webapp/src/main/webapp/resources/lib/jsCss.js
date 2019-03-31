/**
 * 
 */

net.spectroom.js.css = {
		
	init : function (filename) {
		
		var self = Object.create(net.spectroom.js.css);
		self.getFile(self, filename);
		
		self.getRule = function (rule) {
			var r, i, n;
			if (self.css.cssRules) {
				r = self.css.cssRules;
			} else {
				// IE
				r = self.css.rules;
			}	
			n = r.length;
			for (i=0; i<n; i++) {
				if (rule == r[i].selectorText) {
					return r[i];
				}
			}
		};
		
		self.addRule = function (rule, value) {
			if (self.css.cssRules) {
				self.css.insertRule(rule + value, 0);
			} else {
				// IE
				self.css.addRule(rule, value, 0);
			}	
			return self.getRule(rule);
		};
		
		return self;
	}
		
	, getFile : function (self, filename) {
		var sts = document.styleSheets, i, n = sts.length;
		for (i=0; i<n; i++) {
			if (sts.item(i).href.indexOf(filename) >= 0) {
				self.css = sts.item(i);
				return;
			}						
		}					
	}

	
};





