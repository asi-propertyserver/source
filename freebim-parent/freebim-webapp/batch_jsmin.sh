#!/bin/bash

# ./delete_min_files.sh

createMinified () {
	p=$1
	s=$2
	arr=$3
	ext=$4
	for file in "${arr[@]}" ; do
		f=$p$s$file$ext
		echo "creating minified version of file:" $f
		./jsmin.exe < $f > $p"min/"$s$file$ext $5
	done
	
}

do_js () {
	local arr=( 
		"admin"
		"bsdd"
		"ctxmenu" 
		"delegate" 
		"domain" 
		"graph" 
		"imprint" 
		"main" 
		"merge" 
		"nodeFields"
		"onLayout" 
		"paramlist" 
		"problems" 
		"pset" 
		"rel" 
		"reltype"
		"search" 
		"state" 
		"statistic" 
		"tabs" 
		"tree"
	)
	
	createMinified "src/main/webapp/resources/"  "js/" $arr ".js" "© 2015 db.freebim.at"
}

do_lib1 () {
	local arr=( 
		"IfcGuid"
		"ifd"
	)
	
	createMinified "src/main/webapp/resources/"  "lib/" $arr ".js" "© 2015 www.muigg.com"
}
do_lib2 () {
	local arr=( 
		"i18n"
		"jsCookie"
		"jsDownload"
		"jsCss"
		"jsForm" 
		"jsTable" 
		"json2" 
		"objectKeys"
	)
	
	createMinified "src/main/webapp/resources/"  "lib/" $arr ".js" "© 2015 www.spectroom.net"
}

do_lang () {
	local arr=( 
		"de-DE"
		"en-US"
		"es-ES"
		"fr-FR"
	)
	
	createMinified "src/main/webapp/resources/"  "lang/" $arr ".json"
}

echo "create directories"
mkdir -p src/main/webapp/resources/min/
mkdir -p src/main/webapp/resources/min/js/
mkdir -p src/main/webapp/resources/min/lib/
mkdir -p src/main/webapp/resources/min/lang/

do_js
do_lib1
do_lib2
do_lang

# echo "create minified JavaScript files done."

