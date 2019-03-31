/**
 * 
 */
Object.keys = Object.keys || (function () {
		    var hasOwnProperty = Object.prototype.hasOwnProperty,
		        hasDontEnumBug = !{toString:null}.propertyIsEnumerable("toString"),
		        DontEnums = [
		            'toString',
		            'toLocaleString',
		            'valueOf',
		            'hasOwnProperty',
		            'isPrototypeOf',
		            'propertyIsEnumerable',
		            'constructor'
		        ],
		        DontEnumsLength = DontEnums.length;
		  
		    return function (o) {
		        if (typeof o != "object" && typeof o != "function" || o === null)
		            throw new TypeError("Object.keys called on a non-object");
		     
		        var result = [];
		        for (var name in o) {
		            if (hasOwnProperty.call(o, name))
		                result.push(name);
		        }
		     
		        if (hasDontEnumBug) {
		            for (var i = 0; i < DontEnumsLength; i++) {
		                if (hasOwnProperty.call(o, DontEnums[i]))
		                    result.push(DontEnums[i]);
		            }   
		        }
		     
		        return result;
		    };
		})();

if (!Array.prototype.indexOf) {
    Array.prototype.indexOf = function(val) {
        return jQuery.inArray(val, this);
    };
}
if (!Array.prototype.isArray) {
    Array.prototype.isArray = function() {
        return jQuery.isArray(this);
    };
}

if (!Object.create) {
    Object.create = (function(){
        function F(){}

        return function(o) {
            if (arguments.length != 1) {
                throw new Error('Object.create implementation only accepts one parameter.');
            }
            F.prototype = o;
            return new F();
        };
    })();
}

Date.toISOString = Date.toISOString || function() {
	var y = this.getFullYear() + "",
		m = (this.getMonth() + 1) + "",
		d = this.getDate() + "",
		h = this.getHours() + "",
		mm = this.getMinutes() + "",
		s = this.getSeconds() + "";
	y = ((y.length < 1) ? "1" : y);
	y = ((y.length < 2) ? "0" + y : y);
	y = ((y.length < 3) ? "0" + y : y);
	y = ((y.length < 4) ? "0" + y : y);
	m = ((m.length < 2) ? "0" + m : m);
	d = ((d.length < 2) ? "0" + d : d);
	h = ((m.length < 2) ? "0" + h : h);
	mm = ((mm.length < 2) ? "0" + mm : mm);
	s = ((s.length < 2) ? "0" + s : s);
	return y + "-" + m + "-" + d + "T" + h + ":" + mm + ":" + s;
};