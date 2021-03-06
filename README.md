# ASI - Propertyserver

This repository contains the source code of the BIM property server of Austria.

The ASI - Propertyserver is used by [Austrian Standards International](https://www.austrian-standards.at/en/home/) and is part of the [ÖNORM A 6241-2](https://www.austrian-standards.at/infopedia-themencenter/infopedia-artikel/building-information-modeling-bim/#c7261).

It has been (and will be further) developed by [DBIS - Databases and Information Systems](https://dbis-informatik.uibk.ac.at/) of the [University of Innsbruck](https://www.uibk.ac.at/), [Department of Computer Science](https://www.uibk.ac.at/informatik/).
It has been (and is currently) funded by the [Tyrolean Regional Government](https://www.tirol.gv.at/) via three consequent projects:

* freeBIM
* freeBIM 2
* freeBIM - connect

The ASI - Propertyserver is therefore also known as the 'freeBIM-Propertyserver'.

You will find more information about the project and all project partners at: [https://www.freebim.at](https://www.freebim.at/) (in german)

You'll find a running instance of this application at: [http://db.freebim.at](http://db.freebim.at)

## Table Of Contents
[Requirements](#requirements)  
[Setting up the project](#setting-up-the-project)  
[Setting up the environment](#setting-up-the-environment)  
[Start the application](#start-the-application)  
[Content](#content)  
[Contributors](#contributors)

## Requirements

```
java 8
maven
tomcat
apache
internet connection (for build process)
```

## Setting up the project

Create a directory for the project, we'll use `<BASEDIR>` for this directory in this document.

```
$ mkdir <BASEDIR>
$ cd <BASEDIR>
```

GIT-Clone the project:

```
$ git clone https://github.com/asi-propertyserver/source.git
```

### Create properties folder

```
cd /etc

# creating directory
sudo mkdir -p asi-propertyserver/production
sudo mkdir -p asi-propertyserver/backup
cd asi-propertyserver

# change group and rights for group
sudo chown root:<your_group> production/
sudo chown root:<your_group> backup/
sudo chmod g+w production
sudo chmod g+w backup

```


### Adjust the properties

```
$ cd source/freebim-parent/freebim-webapp/src/main/resources
$ ls
application.properties  banner.txt              log4j2-spring.xml       version.properties

$ cd /etc/asi-propertyserver/production/
$ ls
app.properties
```

This is a template of the `app.properties` -file.

```
server.port = 8090
props.file_upload_dir = /etc/asi-propertyserver/upload/
backup.scheduler.minute=45
backup.scheduler.hour=3
backup.scheduler.dayOfWeek=SUNDAY
backup.scheduler.dayOfMonth=1
backup.scheduler.directory=/etc/asi-propertyserver/backup
backup.scheduler.package=at.freebim.db.domain
backup.scheduler.monthly=false
backup.scheduler.weekly=false
backup.scheduler.daily=false
backup.scheduler.hourly=false
freebim.backup.dir=/etc/asi-propertyserver/backup
db.location=/etc/asi-propertyserver/database
admin.username=admin
admin.password=password
guest.username=ON_GUEST
guest.password=bs_DACH
bsdd.url = http://bsdd.buildingsmart.org/api/4.0/
db.uri=bolt://localhost:7687
db.username=neo4j
db.password=password
security.secret=testtoken
security.refresh.secret=testrefresh
security.validity=900000
security.refresh.validity=604800000
```

In the `app.properties` -file you will find the **admin**, **neo4j** and **backup** configuration.  
You should **set a new password for the `admin` user** at least and change the secrets for the jwt-token. 

### Javascript minifier

A C-program is used to minify the javascript files. Since it was compiled under Mac OS it may be possible that it does not work
on your machine. Just check the output when building. It says something like, there is a format error in the **jsmin.exe**-file.
When you encounter this problem just use **gcc** to compile 
**source/freebim-parent/freebim-webapp/jsmin.c** and replace the **jsmin.exe** file. To make sure everything works on your first run, you can do this also beforehand. 

### Logging

Create a folder `asi-propertyserver` in the `/var/log/` directory or adjust the logging configuration in the `log4j.properties` - file. 
Make sure the program has access to that folder.

```
sudo mkdir -p /var/log/asi-propertyserver
sudo chown root:<your_group> /var/log/asi-propertyserver/
sudo chmod g+w /var/log/asi-propertyserver/
```

### Build the application

`cd` into the `freebim-parent` project to build the application.

```
$ cd <BASEDIR>/source/freebim-parent
$ mvn clean -Prelease javadoc:javadoc install
```

Maven will automatically run all the Unit-Tests of the project. Execution of Unit-Tests could be disabled by passing an additional argument to Maven:

```
$ mvn clean -Prelease javadoc:javadoc install -Dmaven.test.skip=true
```

Maven will build the sub-projects and create the final `ROOT.war` file in the `target` directory of the `freebim-webapp` project.
This file can now be deployed to an existing tomcat.

```
$ ls freebim-webapp/target/
ROOT			antrun			generated-sources	javadoc-bundle-options	maven-status		surefire		test-classes
ROOT.war		classes			generated-test-sources	maven-archiver		site			surefire-reports
```

## Setting up the environment 

### Configure Apache

First you have to make sure the following things are not commented out (activated) in the **httpd.conf** file from the apache webserver:

```
LoadModule proxy_module libexec/apache2/mod_proxy.so
LoadModule proxy_ajp_module libexec/apache2/mod_proxy_ajp.so
LoadModule vhost_alias_module libexec/apache2/mod_vhost_alias.so
LoadModule rewrite_module libexec/apache2/mod_rewrite.so
```

If you have a newer version of the web-server (apache), the **httpd.conf** might not exist. On his place there is 
a **apache2.conf**. Just make sure that the modules are present in the **mods-enabled/** folder or enable
them with **a2enmod**.

```
sudo a2enmod proxy
sudo a2enmod proxy_ajp
sudo a2enmod vhost_alias
sudo atenmode rewrite
```

Still in the same file we have to include the virtual hosts file:

```
Include /private/etc/apache2/extra/httpd-vhosts.conf
```

If this file does not exists you can create an empty one on your own.

The next step is to create a directory for the war- and the apache configuration-file. 
This directory can be anywhere. Also the name of the directory **db.freebim.at** is not fixed, just make sure to adjust the name in the configuration-files. Additionally you have to make sure that the apache user can read the directory and the files in it.

```
$ cd <directory>
$ mkdir db.freebim.at
$ cd db.freebim.at
$ mkdir conf
$ touch conf/httpd.conf
```

Now that we have created the **httpd.conf** file we can add our apache web-service configuration.

```
# Web-Application:
<VirtualHost *:80>
   DocumentRoot "<directory>/db.freebim.at"
   ServerName <server-name>
# Proxy specific settings
   ProxyRequests Off
   ProxyPreserveHost On
   ProxyPass / ajp://localhost:8009/
   ProxyPassReverse / ajp://localhost:8009/
    LogLevel debug
   <Directory <directory>/db.freebim.at>
       Options Indexes FollowSymLinks MultiViews
       AllowOverride None
       Order allow,deny
       allow from all
   </Directory>
</VirtualHost>
# Web-Service:
<VirtualHost *:80>
   ServerAdmin <email-address>
   DocumentRoot "<directory>/db.freebim.at"
   ServerName <ws-server-name>
   ProxyRequests Off
   ProxyPreserveHost On
   ProxyPass / http://localhost:8081/
   ProxyPassReverse / http://localhost:8081/
</VirtualHost>
```

Include the configuration-file it in the **httpd-vhosts.conf**-file.

```
Include <directory>/db.freebim.at/conf/httpd.conf

```

### Configure Tomcat

Edit the **server.xml**-file which is located in the tomcat `conf` - directory.
Enable AJP:

```
<!-- Define an AJP 1.3 Connector on port 8009 -->
<Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />
```

Add a new host:

```
<Engine ...>
	...
	<Host ...>
	</Host>
	<Host appBase="<directory>/db.freebim.at" autoDeploy="true" name="<server-name>" unpackWARs="true">
        <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs" pattern="%h %l %u %t &quot;%r&quot; %s %b" prefix="localhost_access_log" suffix=".txt"/>
	</Host>
</Engine>
```

Now that everything is configured you can simply copy the **ROOT.war**-file in the `appBase` directory and fire everything up.

```
$ cp <>/freebim-webapp/target/ROOT.war <directory>/db.freebim.at/
```


## Start the application

Restart Apache-Webserver after you configured it.

```
$ sudo apachectl restart
```

Start tomcat. It may be necessary to start tomcat as **root** when he has not permissions to read or write in the directory that we created before (**<directory>/db.freebim.at/).

```
$ sudo catalina run
```

## Content

A newly created instance of this application will contain 

* Two users: `admin` and `guest` (as you named them in the `freebim.properties` - file).
* One Contributor (which is referenced internally)
* One Library `Ifc4` with the basic IFC data structure.

For the impatient we provide an **anonymized backup** of all data stored in our running application at [http://db.freebim.at](http://db.freebim.at)

1. download the backup data from [https://github.com/asi-propertyserver/content](https://github.com/asi-propertyserver/content)
2. put the backup into your configured `freebim.backup.dir` - directory.

```
/etc/asi-propertyserver/backup
└── 20190330_085328
    ├── nodes
    └── relationships
```
3. log in as `admin` 
4. press the 'restore backup' button
5. wait and watch the server logs. After `restoreBackup` has finished you have to reload the app in the browser.

```
...
2019-03-31 09:12:01,844  INFO SpectroomBackupServiceImpl:241 - restore relationships finished.
2019-03-31 09:12:01,844  INFO SpectroomBackupServiceImpl:243 - restoreBackup finished.
...
```

Since this is a full backup restore these steps will also **override** your configured `admin` and `guest` user settings.  
The login-data used in the backup are:

| Username    | Password       |
|:------------|:---------------|
| `admin`     | `password`     |

**Don't forget to change the password** of `admin` afterwards.

## Contributors

Rainer Breuss <rainer.breuss@uibk.ac.at>

Patrick Lanzinger <patrick.lanzinger@student.uibk.ac.at>







