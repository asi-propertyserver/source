/**
 * 
 */
var net = net || {};
net.spectroom = net.spectroom || {};
net.spectroom.js = net.spectroom.js || {};

net.spectroom.js.download = function (content, fileName) {
	var BOM = "\uFEFF", a = document.createElement('a'),
	  	mimeType = mimeType || 'application/octet-stream';
	content = BOM + content;
	if (navigator.msSaveBlob) { // IE10
	    navigator.msSaveBlob(new Blob([content], { type: mimeType }), fileName);
	} else if ('download' in a) { //html5 A[download]
	    a.href = 'data:' + mimeType + ',' + encodeURIComponent(content);
	    a.setAttribute('download', fileName);
	    document.body.appendChild(a);
	    setTimeout(function() {
	        a.click();
	        document.body.removeChild(a);
	    }, 66);
	} else { //do iframe dataURL download (old ch+FF):
	    var f = document.createElement('iframe');
	    document.body.appendChild(f);
	    f.src = 'data:' + mimeType + ',' + encodeURIComponent(content);
	    setTimeout(function() {
	        document.body.removeChild(f);
	    }, 333);
	}
};