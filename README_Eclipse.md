***Installation unter Windows 10 64bit***
====================


# Systemvoraussetzungen und Installationshinweise
Die gesammte Entwicklungsumgebung belegt in etwa 3 GB und sollte auf einem lokalem Laufwerk liegen.

# Maria db installieren

+ download : Maria db 10.6 -> https://mariadb.org/download/?t=mariadb&p=mariadb&r=10.6.5&os=windows&cpu=x86_64&pkg=msi&m=netcologne
+ root user einrichten

# JDK 17 installieren

+ Download des jdk-17 -> https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.zip
+ Entpacken in z.B. C:\jdk-17\
+ Path setzen: 
    + Über das Windowssymbol den Editor für die Umgebungsvariablen öffnen 
    + die Variable Path bearbeiten und einen weiteren Eintrag zum Java Verzeichnis einfügen

Variable Path
C.\jdk-17\bin


# Eclipse installieren und konfigurieren

+ Installieren eclipse-inst-win64.zip (aktuelle Version) (Eclipse IDE for Java Developers)-> https://www.eclipse.org/downloads/
+ Einmaliger Start Eclipse und festlegen des Workspace: `C:\eclipse\`
+ Bei Bedarf den Speicher hochsetzen: per texteditor `C:\eclipse\eclipse.ini` entsprechend z.B. aus der 512 eine 2048 machen

```
-Xms1024m
-Xmx8192m
```


+ Eclipse > Help > Marcet Place -> JSON editor suchen und "JSON Editor Plugin 1.1.2" installieren

+ Eclipse > Window > Preferences > General > Editors > Text Editors > Spelling > UTF-8

+ Eclipse > Window > Preferences > General > Workspace > Text file encodig > Other UTF-8



## Git Repositories in Eclipse einrichten 

+ Eclipse > Windows > Perspektive > Open Perspective > Other  -> Git

### Quellen aus Github eintragen:

https://github.com/SVWS-NRW/SVWS-Server

+ Repositories in Eclipse clonen: rechte Maustaste Git > Clone a Git Repository
+ URL und Passwort eingeben

Anschließend git clone ausführen.

## Gradle Projekte importieren

In der Java-Perspektive nun auf Import > Import existing gradle-Project.
Dort den SVWS-Server-Ordner auswählen. 

Momentan wird im ./gradle-Verzeichnis noch eine Datei gradle.properties erwartet.
Datei erzeugen und hier die beiden Einträge mit den eigenen GitHub-Credentials eintragen.

github_actor=githubusername
github_token=ghp_43b4b5b3nm4b5nm43b5m32m6v2

#### SVWS Einstellungen

Die Beispiel-Config ins Zielverzeichnis kopieren und umbenennen.

```
 git/SVWS-Swerver/svws-server-app/src/main/resources/svwsconfig.json.example #Zielverzeichnis#/svwsconfig.json
```
		
Beispiel einer svwsconfig.json, bitte die Userdaten und Passwörter entspechend anpassen:
		
```json
{
"EnableClientProtection" : null,
"DisableDBRootAccess": false,
"DisableAutoUpdates" : false,
"UseHTTPDefaultv11": false,
"PortHTTPS": 443,
"UseCORSHeader": true,
"ClientPath": ".../git/SVWS-Server/svws-webclient/build/output",
"LoggingEnabled": true,
"LoggingPath": "logs",
"TempPath": "/home/svwsdeveloper/temp",
"TLSKeystorePath": ".",
"TLSKeystorePassword": "svwskeystore",
"DBKonfiguration": {
	"dbms": "MARIA_DB",
	"location": "localhost",
	"SchemaKonfiguration": [
		{
		"name": "schildtest",
		"svwslogin": false,
		"username": "svwsadmin",
		"password": "svwsadmin"
		}
		]
	}
}
```

# Optionale Software 


## DBeaver
+ download: https://dbeaver.io/download/

## VSCodeUserSetup
+ Install VSCodeUserSetup-x64-latest.exe (optional)


# Installation in einer Proxy-Umgebung

```
Eclipse > Windows > Preferences > General > Network Connection
````

den Http und den Https-Proxy eintragen. (Sollte er automatisch finden, wenn konfiguriert.)


