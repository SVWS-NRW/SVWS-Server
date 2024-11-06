# Reporting - Klassen, Struktur und Generierung
Das Reporting erzeugt für die über die API gestellten Anfragen eine Rückgabe in Form von html- oder PDF-Dateien. Die PDF-Dateien werden dabei ebenfalls aus html-Dateien erzeugt, wofür OpenHtmlToPDF verwendet wird.
Daher sind [html-Templates](#html-Templates) die Basis der Reporterstellung, welche durch die Template-Engine Thymeleaf mit Daten gefüllt werden.

Die folgende Dokumentation soll die einzelnen Klassen und deren Zusammenspiel bei der Erzeugung der Druckausgabe darstellen. Folgende Punkte werden dabei behandelt.

<!-- TOC -->
* [Reporting - Klassen, Struktur und Generierung](#reporting---klassen-struktur-und-generierung)
  * [Datenklassen für das Reporting](#datenklassen-für-das-reporting)
    * [Reporting-Types](#reporting-types)
    * [Reporting-Proxy-Types](#reporting-proxy-types)
  * [Ablauf der Reporterstellung](#ablauf-der-reporterstellung)
  * [Informationen zu den einzelnen Klassen](#informationen-zu-den-einzelnen-klassen)
    * [Reporting-Parameter](#reporting-parameter)
    * [Reporting-Factory](#reporting-factory)
    * [html-Factory](#html-factory)
    * [html-Contexts](#html-contexts)
    * [html-Builder](#html-builder)
  * [html-Templates](#html-templates)
<!-- TOC -->

## Datenklassen für das Reporting

### Reporting-Types
Die Reporting-Types sind als reine Datentypen-Klassen konzipiert, d. h. sie haben keine Anbindung an die Datenbank.

Auch wenn sie den CoreTypes häufig ähneln, so werden hier unter Umständen nicht alle Felder übernommen oder es werden zusätzliche statische oder berechnete Felder ergänzt. So bleibt das Reporting von Änderungen und die CoreTypes unabhängig voneinander (und damit auch einmal erstellte Templates) und die Reporting-Types können untereinander verwendet werden.

Sie dienen aber als Super-Klasse einer [Proxy-Klasse](#Reporting-Proxy-Types).

### Reporting-Proxy-Types
Die Reporting-Proxy-Types erweitern die Reporting-Types im Rahmen einer Vererbung. Während die Reporting-Types als strukturdefinierende Datenklassen konzipiert sind, die keine Abhängigkeit zur Datenbank haben, können Proxy-Types auf die Datenbank zugreifen oder über andere Core- und Data-Klassen gefüllt werden.

Des Weiteren gilt:

* Die Proxy-Types stellen in der Regel einen oder mehrere zusätzlichen Constructors zur Verfügung, um Reporting-Objekte
  aus Stammdatenobjekten erstellen zu können. Darin werden Felder, die Reporting-Objekte zurückgegeben und nicht im
  Stammdatenobjekt enthalten sind, mit null initialisiert.
* Die Proxy-Types überschreiben einzelne Getter der Super-Klasse (beispielsweise bei Feldern, die mit null initialisiert wurden)
  und laden dort dann aus der Datenbank die Daten bei Bedarf nach (lazy-loading), um den Umfang der Datenstrukturen gering zu
  halten.
* Die Proxy-Types können zudem auf das Reporting-Repository zugreifen. Dieses enthält neben den Stammdaten der Schule einige Maps,
  in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte wie Kataloge, Lehrer, Schüler usw. gespeichert werden.
  So sollen Datenbankzugriffe minimiert werden. Werden in der Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der
  Regel in der entsprechenden Map des Repository ergänzt.


## Ablauf der Reporterstellung
Der Ablauf der Reporterstellung ist dabei der folgende:

* API-Aufruf erfolgt unter der Server-API *APIReporting*. Bei diesem API-Aufruf werden [ReportingParameter](#Reporting-Parameter) übergeben.
* Der API-Aufruf erfolgt an der [ReportingFactory](#Reporting-Factory).
* Die [html-Factory](#html-Factory) wird aufgerufen und diese erzeugt die [html-builder](#html-Builder).
* [html-builder](#html-Builder) erzeugen dann entweder direkt html-Code für die Ausgabe oder werden an weitere Factories wie die PDF-Factory weitergegeben.
* Die letzte Factory in der Kette erzeugt mit ihrem Builder eine passende API-Response, die auf die ursprüngliche Anfrage zurückgegeben wird.

## Informationen zu den einzelnen Klassen
Im Folgenden werden einzelne ergänzende Informationen zu den Klassen gegeben, die an der Report-Erzeugung beteiligt sind.

### Reporting-Parameter
Die ReportingParameter sind ein CoreType, welche die notwendigen Informationen für die Report-Erstellung enthalten. Dazu zählen zum Beispiel das Ausgabeformat, das Template, IDs der auszugebenden Daten oder weitere, den Druck steuerende Parameter.

### Reporting-Factory
Die Reporting-Factory wertet die [ReportingParameter](#Reporting-Parameter) aus und initialisiert zentrale Objekte der Reporting-Klassen. Gemäß dem gewünschten Ausgabeformat werden dann eine oder mehrere spezialisierte Factories aufgerufen, in der Regel erfolgt dabei zuerst der Aufruf der [html-Factory](#html-Factory).

### html-Factory
Diese erstellt dann die notwendigen [html-Contexts](#html-Contexts). Contexts sind eine Sammlung von Objekten, welche die Daten für das Füllen durch die Thymeleaf-html-Template-Engine bereitstellen. Diese Contexts werden danach in den [html-builder](html-builder) verwendet.

### html-Contexts
Die Erzeugung der html-Dateien erfolgt durch die Verwendung von Thymeleaf und [html-Templates](#html-Templates), die mit Daten gefüllt werden. Diese Daten stammen aus dem sogenannten html-Context.

Ein Context stellt ein Objekt dar, welches mehrere Objekte in einer Liste verwalten kann. Die Variablen stehen dann in den [html-Templates](#html-Templates) zur Verfügung, um sie mit Daten zu füllen.

Es ist jedoch nach Möglichkeit darauf zu achten, dass ein Context so erstellt wird, dass darin nur eine Variable enthalten ist, unter der sich der "Datenbaum" für dieses Template ergibt. Darin sind dann alle Objekte und ihre Abhängigkeiten abgebildet.

Alle Contexts erben hier von *HtmlContext.java*. Diese Klasse kapselt nur die ThymeLeaf-Context-Class.

### html-Builder
Die Aufgabe der html-Builder besteht darin, aus dem gewählten [html-Template](#html-Templates) und den erstellten Contexts den html-Code der finalen html-Datei zu bauen. Dieser wird entweder direkt zur Ausgabe genutzt oder die html-Builder werden an weitere Factories übergeben, um dort mit Hilfe anderer Builder aus dem html ein anderes Ausgabeformat zu erzeugen.


## html-Templates
Die html-Templates werden durch [Thymeleaf](https://www.thymeleaf.org) verarbeitet. Daher ist dessen Syntax für die Templates zu verwenden. Gleichzeitig ist zu bedenken, dass der generierte html-Code in der Regel für die Druckausgabe gedacht ist und dafür durch OpenHtmlToPDF in eine PDF-Datei umgewandelt wird. Daher ist hier insbesondere beim CSS auf die Verwendung von PrintCSS zu achten.

Ein Template wird mit seiner html-Dateien sowie weiteren Eigenschaften in der Enum *HtmlTemplateDefinition* definiert. Dafür wird vorab auch eine Definition unter dem CoreType *ReportingReportvorlage* benötigt. Bzgl. der einzelnen Einträge in der ENUM ist unbedingt die dortige Kommentierung zu beachten.

In der *HtmlTemplateDefinition* wird ein Root-Pfad angegeben. Unter dem müssen alle Ressourcen (html, css, fonts) gefunden werden können. Alle weiteren Pfadangaben werden relativ zu diesem Root-Pfad angegeben. Bei der Verlinkung von css-Dateien bedeutet dies beispielsweise, dass bei einer css-Datei, die neben dem html-Template im gleichen Verzeichnis liegt, zunächst relativ zum Root-Verzeichnis hochgegangen wird (mit "../") und dann wieder durch Angabe des Pfads zum html-Verzeichnis zurück. Beispiel: "../../listen/schueler/kakao-listen.css".

Zudem müssen die css-Dateien mittels Thymeleaf th:href eingebunden werden, damit die Pfade im realen Betrieb auch korrekt gesetzt werden. Ein solcher Aufruf wäre: *\<link rel="stylesheet" th:href="@{css/svws-reporting.css}" href="../../../css/svws-reporting.css" />*

Alle weiteren Informationen rund um die Erstellung der Templates sind in einer weiteren [Dokumentation](reporting-template-erstellung.md) untergebracht.
