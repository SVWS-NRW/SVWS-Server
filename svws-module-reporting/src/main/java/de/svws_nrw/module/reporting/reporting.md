# Reporting - Klassen, Struktur und Generierung
Das Reporting erzeugt für die über die API gestellten Anfragen eine Rückgabe in Form von HTML- oder PDF-Dateien. Die PDF-Dateien werden dabei ebenfalls aus HTML-Dateien erzeugt, wofür OpenHtmlToPDF verwendet wird.
Daher sind [HTML-Templates](#HTML-Templates) die Basis der Reporterstellung, welche durch die Template-Engine Thymeleaf mit Daten gefüllt werden.

Die folgende Dokumentation soll die einzelnen Klassen und deren Zusammenspiel bei der Erzeugung der Druckausgabe darstellen. Folgende Punkte werden dabei behandelt.

<!-- TOC -->
* [Reporting - Klassen, Struktur und Generierung](#reporting---klassen-struktur-und-generierung)
  * [Datenklassen für das Reporting](#datenklassen-für-das-reporting)
    * [Reporting-Types](#reporting-types)
    * [Reporting-Proxy-Types](#reporting-proxy-types)
  * [Ablauf der Reporterstellung](#ablauf-der-reporterstellung)
  * [Informationen zu den einzelnen Klassen](#informationen-zu-den-einzelnen-klassen)
    * [Reporting-Reportvorlage](#reporting-reportvorlage)
    * [Reporting-Parameter](#reporting-parameter)
    * [Reporting-Factory](#reporting-factory)
    * [HTML-Factory](#html-factory)
    * [HTML-Contexts](#html-contexts)
    * [HTML-Builder](#html-builder)
  * [HTML-Templates und Reporting-Reportvorlage](#html-templates-und-reporting-reportvorlage)
<!-- TOC -->

## Datenklassen für das Reporting

### Reporting-Types
Die Reporting-Types sind als reine Datentypen-Klassen konzipiert, d. h. sie haben keine Anbindung an die Datenbank.

Auch wenn sie den CoreTypes häufig ähneln, so werden hier unter Umständen nicht alle Felder übernommen oder es werden zusätzliche statische oder berechnete Felder ergänzt. So bleibt das Reporting von Änderungen und die CoreTypes unabhängig voneinander (und damit auch einmal erstellte Templates) und die Reporting-Types können untereinander verwendet werden.

Sie sind alle abgeleitet von der abstrakten Klasse ReportingBase Type und dienen ihrerseits als Super-Klassen der [Proxy-Klassen](#Reporting-Proxy-Types).

### Reporting-Proxy-Types
Die Reporting-Proxy-Types erweitern die Reporting-Types im Rahmen einer Vererbung. Während die Reporting-Types als strukturdefinierende Datenklassen konzipiert sind, die keine Abhängigkeit zur Datenbank haben, können Proxy-Types auf die Datenbank zugreifen oder über andere Core- und Data-Klassen gefüllt werden.

Des Weiteren gilt:

* Die Proxy-Types stellen in der Regel einen oder mehrere zusätzliche Constructors zur Verfügung, um Reporting-Objekte
  aus Stammdatenobjekten erstellen zu können. Darin werden Felder, die Reporting-Objekte zurückgegeben und nicht im
  Stammdatenobjekt enthalten sind, mit null initialisiert.
* Die Proxy-Types überschreiben einzelne Getter der Super-Klasse (beispielsweise bei Feldern, die mit null initialisiert wurden)
  und laden dort dann aus der Datenbank die Daten bei Bedarf nach (lazy-loading), um den Umfang der Datenstrukturen gering zu
  halten.
* Die Proxy-Types können zudem auf den Reporting-Context zugreifen. Über dessen domänenspezifische Repositories werden neben den
  Stammdaten der Schule auch Maps bereitgestellt, in denen zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte wie
  Kataloge, Lehrer, Schüler usw. gespeichert sind. So sollen Datenbankzugriffe minimiert werden. Werden in der Proxy-Klasse Daten
  nachgeladen, so werden sie dabei auch in der Regel in der entsprechenden Map des zuständigen Repositorys ergänzt.


## Ablauf der Reporterstellung
Der Ablauf der Reporterstellung ist dabei der folgende:

* API-Aufruf erfolgt unter der Server-API *APIReporting*. Bei diesem API-Aufruf werden [ReportingParameter](#Reporting-Parameter) übergeben.
* Der API-Aufruf erfolgt an der [ReportingFactory](#Reporting-Factory).
* Die [HTML-Factory](#HTML-Factory) wird aufgerufen und diese erzeugt die [HTML-builder](#HTML-Builder).
* [HTML-builder](#HTML-Builder) erzeugen dann entweder direkt HTML-Code für die Ausgabe oder werden an weitere Factories wie die PDF-Factory weitergegeben.
* Die letzte Factory in der Kette erzeugt mit ihrem Builder eine passende API-Response, die auf die ursprüngliche Anfrage zurückgegeben wird.

## Informationen zu den einzelnen Klassen
Im Folgenden werden einzelne ergänzende Informationen zu den Klassen gegeben, die an der Report-Erzeugung beteiligt sind.

### Reporting-Reportvorlage
Eine Reportvorlage ist ein CoreType, welche die notwendigen Informationen für die Report-Erstellung enthält. Dazu zählen zum Beispiel die Bezeichnung der Vorlage, die Angaben rund um die Template-Datei und insbesondere die ReportingParameter.

### Reporting-Parameter
Die ReportingParameter sind ein CoreType, welche die notwendigen Informationen für die Report-Erstellung enthalten. Dazu zählen zum Beispiel das Ausgabeformat, IDs der auszugebenden Daten oder weitere, den Druck steuerende Parameter und vorlagespezifische Parameter und Optionen.

### Reporting-Factory
Die Reporting-Factory wertet die [ReportingParameter](#Reporting-Parameter) aus und initialisiert zentrale Objekte der Reporting-Klassen. Gemäß dem gewünschten Ausgabeformat werden dann eine oder mehrere spezialisierte Factories aufgerufen, in der Regel erfolgt dabei zuerst der Aufruf der [HTML-Factory](#HTML-Factory).

### HTML-Factory
Diese erstellt dann die notwendigen [HTML-Contexts](#HTML-Contexts). Contexts sind eine Sammlung von Objekten, welche die Daten für das Füllen durch die Thymeleaf-HTML-Template-Engine bereitstellen. Diese Contexts werden danach in den [HTML-builder](HTML-builder) verwendet.

### HTML-Contexts
Die Erzeugung der HTML-Dateien erfolgt durch die Verwendung von Thymeleaf und [HTML-Templates](#HTML-Templates), die mit Daten gefüllt werden. Diese Daten stammen aus dem sogenannten HTML-Context.

Ein Context stellt ein Objekt dar, welches mehrere Objekte in einer Liste verwalten kann. Die Variablen stehen dann in den [HTML-Templates](#HTML-Templates) zur Verfügung, um sie mit Daten zu füllen.

Es ist jedoch nach Möglichkeit darauf zu achten, dass ein Context so erstellt wird, dass darin nur eine Variable enthalten ist, unter der sich der "Datenbaum" für dieses Template ergibt. Darin sind dann alle Objekte und ihre Abhängigkeiten abgebildet.

Alle Contexts erben hier von *HtmlContext.java*. Diese Klasse kapselt nur die ThymeLeaf-Context-Class.

### HTML-Builder
Die Aufgabe der HTML-Builder besteht darin, aus dem gewählten [HTML-Template](#HTML-Templates) und den erstellten Contexts den HTML-Code der finalen HTML-Datei zu bauen. Dieser wird entweder direkt zur Ausgabe genutzt oder die HTML-Builder werden an weitere Factories übergeben, um dort mithilfe anderer Builder aus dem HTML ein anderes Ausgabeformat zu erzeugen.


## HTML-Templates und Reporting-Reportvorlage
Die HTML-Templates werden durch [Thymeleaf](https://www.thymeleaf.org) verarbeitet. Daher ist dessen Syntax für die Templates zu verwenden. Gleichzeitig ist zu bedenken, dass der generierte HTML-Code in der Regel für die Druckausgabe gedacht ist und dafür durch OpenHtmlToPDF in eine PDF-Datei umgewandelt wird. Daher ist hier insbesondere beim CSS auf die Verwendung von PrintCSS zu achten.

Ein Template wird mit seiner HTML-Datei sowie weiteren Eigenschaften in der Enum *ReportingReportvorlage* unter core\types definiert und kann dort auch angepasst werden. Auf das template wird stets über die *ReportingReportvorlage* zugegriffen.

In der *ReportingReportvorlage* wird ein Root-Pfad angegeben. Unter dem müssen alle Ressourcen (HTML, CSS, fonts) gefunden werden können. Alle weiteren Pfadangaben werden relativ zu diesem Root-Pfad angegeben. Bei der Verlinkung von CSS-Dateien bedeutet dies beispielsweise, dass bei einer CSS-Datei, die neben dem HTML-Template im gleichen Verzeichnis liegt, zunächst relativ zum Root-Verzeichnis hochgegangen wird (mit "../") und dann wieder durch Angabe des Pfads zum HTML-Verzeichnis zurück. Beispiel: "../../listen/schueler/kakao-listen.css".

Zudem müssen die CSS-Dateien mittels Thymeleaf th:href eingebunden werden, damit die Pfade im realen Betrieb auch korrekt gesetzt werden. Ein solcher Aufruf wäre: *\<link rel="stylesheet" th:href="@{css/svws-reporting.css}" href="../../../css/svws-reporting.css" />*

Alle weiteren Informationen rund um die Erstellung der Templates sind in einer weiteren [Dokumentation](reporting-template-erstellung.md) untergebracht.
