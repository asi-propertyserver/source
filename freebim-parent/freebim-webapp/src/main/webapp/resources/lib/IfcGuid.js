/*
 * com.muigg.ifc JavaScript Library v0.0.1
 * http://www.muigg.com/ifc
 *
 * Copyright (c) 2014 Rainer Breuss
 * Licensed under the GPL v3 license.
 * http://www.muigg.com/ifc/license
 *
 * Date: 2014-07-05T08:14:12+02:00
 * Revision: 1
 */
var com = com || {};
com.muigg = com.muigg || {};
com.muigg.ifc = com.muigg.ifc || {};
com.muigg.ifc.IfcGuid = {

	GUID : {
		init : function() {
			this.Data1 = 0;
			this.Data2 = 0;
			this.Data3 = 0;
			this.Data4 = new Array(8);
		}
	}
/*
	, cConversionTable85 : 
        //          1         2         3         4         5         6         7         8
        //0123456789012345678901234567890123456789012345678901234567890123456789012345678901234
         "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#$%&^|*+,-./:;<=>?~`@_"
*/
	, cConversionTable :
        //          1         2         3         4         5         6   
        //0123456789012345678901234567890123456789012345678901234567890123
         "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_$"
	
	, getString16FromGuid : function (guid) {
		var str = 
			this.formatHex(guid.Data1, 8) + "-" +
			this.formatHex(guid.Data2, 4) + "-" + 
			this.formatHex(guid.Data3, 4) + "-" +
			this.formatHex(guid.Data4[0], 2) + 
			this.formatHex(guid.Data4[1], 2) + "-" + 
			this.formatHex(guid.Data4[2], 2) + 
			this.formatHex(guid.Data4[3], 2) + 
			this.formatHex(guid.Data4[4], 2) + 
			this.formatHex(guid.Data4[5], 2) + 
			this.formatHex(guid.Data4[6], 2) + 
			this.formatHex(guid.Data4[7], 2);
		return str;
	}
	
	, formatHex : function (val, len) {
		val = val.toString(16);
		while (val.length < len) {
			val = "0" + val;
		} 
		return val;
	}
	
	/**
	 * @param {String} str A GUID in string format:
	 *    0         1         2         3    
	 *   'cd707c50-55e9-46f5-8219-ac863540e947'
	 */
	, getGuidFromString16 : function (str) {
		var guid = Object.create(com.muigg.ifc.IfcGuid.GUID);
		guid.init();
		guid.Data1 = parseInt(str.substr(0,8), 16);
		guid.Data2 = parseInt(str.substr(9,4), 16);
		guid.Data3 = parseInt(str.substr(14,4), 16);
		guid.Data4[0] = parseInt(str.substr(19,2), 16);
		guid.Data4[1] = parseInt(str.substr(21,2), 16);
		guid.Data4[2] = parseInt(str.substr(24,2), 16);
		guid.Data4[3] = parseInt(str.substr(26,2), 16);
		guid.Data4[4] = parseInt(str.substr(28,2), 16);
		guid.Data4[5] = parseInt(str.substr(30,2), 16);
		guid.Data4[6] = parseInt(str.substr(32,2), 16);
		guid.Data4[7] = parseInt(str.substr(34,2), 16);
		return guid;
	}
	
	, compress : function (uuid) {
		var guid = this.getGuidFromString16(uuid);
		return this.getString64FromGuid(guid);
	}
	
	, CreateCompressedGuidString : function () {
		var str = uuid.v4();
		console.log(str);
		var guid = this.getGuidFromString16(str);
		return this.getString64FromGuid(guid);
	}
	
	, getString64FromGuid : function (guid) {
		var num = Array(6);
	    num[0] = Math.floor(guid.Data1 / 16777216);                                             //    16. byte  (guid.Data1 / 16777216) is the same as (guid.Data1 >> 24)
	    num[1] = Math.floor(guid.Data1 % 16777216);                                             // 15-13. bytes (guid.Data1 % 16777216) is the same as (guid.Data1 & 0xFFFFFF)
	    num[2] = Math.floor(guid.Data2 * 256 + guid.Data3 / 256);                               // 12-10. bytes
	    num[3] = Math.floor((guid.Data3 % 256) * 65536 + guid.Data4[0] * 256 + guid.Data4[1]);  // 09-07. bytes
	    num[4] = Math.floor(guid.Data4[2] * 65536 + guid.Data4[3] * 256 + guid.Data4[4]);       // 06-04. bytes
	    num[5] = Math.floor(guid.Data4[5] * 65536 + guid.Data4[6] * 256 + guid.Data4[7]);       // 03-01. bytes
	    //
	    // Conversion of the numbers into a system using a base of 64
	    //
	    var buf = "", i;
	    n = 3;
	    for (i = 0; i < 6; i++) {
	    	try {
	    		buf += this.cv_to_64 (num[i], n);
	    	}
	        catch (ex) {
	        	if (console && console.err) {
	        		console.err("Error in com.muigg.ifc.IfcGuid.getString64FromGuid: " + ex.message);
	        	}
	            return null;
	        }
	        n = 5;
	    }
	    return buf;
	}
	
	//
	// Conversion of an integer into a number with base 64
	// using the coside table cConversionTable
	//
	, cv_to_64 : function (num, len) {
	    var act, iDigit, nDigits, result = "";

	    act = num;
	    nDigits = len - 1;

	    for (iDigit = 0; iDigit < nDigits; iDigit++) {
	        result = this.cConversionTable[(act % 64)] + result;
	        act = Math.floor(act / 64);
	    }
	    if (act != 0)
	        throw "ERROR in com.muigg.ifc.IfcGuid.cv_to_64";

	    return result;
	}
	
	/**
	 * Reconstructs a Guid-object form an compressed String representation of a GUID
	 * 
	 * @param string compressed String representation of a GUID, 22 character long
	 * @param guid contains the reconstructed Guid as a result of this method 
	 * @return true if no error occurred
	 */
	, getGuidFromCompressedString : function (string, guid) {
	    var str = new Array(6),
	    	len, i, j, k, m,
	    	num = new Array(6);
	    
	    str[0] = new Array(5);
	    str[1] = new Array(5);
	    str[2] = new Array(5);
	    str[3] = new Array(5);
	    str[4] = new Array(5);
	    str[5] = new Array(5);
	    num[0] = new Array(1);
	    num[1] = new Array(1);
	    num[2] = new Array(1);
	    num[3] = new Array(1);
	    num[4] = new Array(1);
	    num[5] = new Array(1);
	    
	    len = string.length;
	    if (len != 22)
	        return false;

	    j = 0;
	    m = 2;

	    for (i = 0; i < 6; i++) {
	    	for(k = 0; k<m; k++){
	    		str[i][k] = string.charAt(j+k);
	    	}
	    	str[i][m]= '\0';
	    	j = j + m;
	    	m = 4;
	    }
	    for (i = 0; i < 6; i++) {
	        if (!this.cv_from_64 (num[i], str[i])) {
	            return false;
	        }
	    }

	    guid.Data1= Math.floor((num[0][0] * 16777216 + num[1][0]));              // 16-13. bytes
	    guid.Data2= Math.floor((num[2][0] / 256));                                // 12-11. bytes
	    guid.Data3= Math.floor(((num[2][0] % 256) * 256 + num[3][0] / 65536));    // 10-09. bytes

	    guid.Data4[0] = Math.floor(((num[3][0] / 256) % 256));                   //    08. byte
	    guid.Data4[1] = Math.floor((num[3][0] % 256));                           //    07. byte
	    guid.Data4[2] = Math.floor((num[4][0] / 65536));                         //    06. byte
	    guid.Data4[3] = Math.floor(((num[4][0] / 256) % 256));                   //    05. byte
	    guid.Data4[4] = Math.floor((num[4][0] % 256));                           //    04. byte
	    guid.Data4[5] = Math.floor((num[5][0] / 65536));                         //    03. byte
	    guid.Data4[6] = Math.floor(((num[5][0] / 256) % 256));                   //    02. byte
	    guid.Data4[7] = Math.floor((num[5][0] % 256));                           //    01. byte

	    return true;
	}

	/**
	 * Calculate a numberer from the code
	 * @param res Array of long
	 * @param str Array of char
	 * @return true if no error occurred
	 */
	, cv_from_64 : function (res, str ) {
	    var len, i, j, index;

	    for(len = 1; len<5; len++)
	    	if(str[len] == '\0') 
	    		break;

	    res[0]=0;

	    for (i = 0; i < len; i++) {
	        index = -1;
	        for (j = 0; j < 64; j++) {
	            if (this.cConversionTable[j] == str[i]) {
	               index = j;
	               break;
	            }
	        }
	        if (index == -1)
	            return false;
	        res[0] = res[0] * 64 + index;
	    }
	    return true;
	},
	
	convert : function (val) {
		if (val) {
			if (val.length == 22) {
				// is short version, uncompress
				var guid = Object.create(com.muigg.ifc.IfcGuid.GUID);
				guid.init();
				com.muigg.ifc.IfcGuid.getGuidFromCompressedString(val, guid);
				val = com.muigg.ifc.IfcGuid.getString16FromGuid(guid);
			} else if (val.length == 36) {
				// is long version, compress
				val = com.muigg.ifc.IfcGuid.compress(val);
			}
		}
		return val;
	}

};