# Reporting - Erstellung von html-Templates
Das Reporting erzeugt für die über die API gestellten Anfragen eine Rückgabe in Form von html- oder PDF-Dateien. Die PDF-Dateien werden dabei ebenfalls aus html-Dateien erzeugt, wofür OpenHtmlToPDF verwendet wird.
Daher sind html-Templates die Basis der Reporterstellung.

Die html-Templates werden durch [Thymeleaf](https://www.thymeleaf.org) verarbeitet. Daher ist dessen Syntax für die Templates zu verwenden.

Auf diese Inhalte wird im Folgenden eingegangen werden.

<!-- TOC -->
* [Reporting - Erstellung von html-Templates](#reporting---erstellung-von-html-templates)
  * [Template-Definitionen](#template-definitionen)
    * [Die Benennung der *ReportingReportvorlage* und der *HtmlTemplateDefinition* und ihr Bezug zu den Daten-Contexts in Thymeleaf](#die-benennung-der-reportingreportvorlage-und-der-htmltemplatedefinition-und-ihr-bezug-zu-den-daten-contexts-in-thymeleaf)
    * [Die Enum *HtmlTemplateDefinition* und ihre Angaben](#die-enum-htmltemplatedefinition-und-ihre-angaben)
  * [Html-Templates](#html-templates)
    * [Ordnerstruktur](#ordnerstruktur)
    * [css-Dateien](#css-dateien)
    * [Html-Dateien erstellen](#html-dateien-erstellen)
      * [Contexts - Namen und Objekt-Charakter](#contexts---namen-und-objekt-charakter)
      * [Seiten - Layout, Einstellungen, Köpfe und Füße](#seiten---layout-einstellungen-köpfe-und-füße)
      * [Thymeleaf-Dialekt #convert](#thymeleaf-dialekt-convert)
  * [Report-Ausgabe testen](#report-ausgabe-testen)
<!-- TOC -->


## Template-Definitionen
Bevor ein html-Template erstellt werden kann, muss es zunächst an zwei Stellen definiert werden, damit später darauf zugegriffen werden kann.

* In der Enum *ReportingReportvorlage* (im core unter svws-core/src/main/java/de/svws_nrw/core/types/reporting/ReportingReportvorlage.java) muss das Template definiert werden. Dabei sind die Hinweise bzgl. der Benennung in der Dokumentation oberhalb Aufzählung unbedingt zu beachten. Dieser Core-Type dient dazu, dass die Vorlage später aus dem Client typsicher angesprochen werden kann.
* In der Enum *HtmlTemplateDefinition* (im reporting-modul unter svws-module-reporting/src/main/java/de/svws_nrw/module/reporting/html/HtmlTemplateDefinition.java) werden dann die grundlegenden Angaben zur Vorlage hinterlegt. Auch hier sind unbedingt die Informationen in der Dokumentation am Anfang der Klasse zu beachten.

### Die Benennung der *ReportingReportvorlage* und der *HtmlTemplateDefinition* und ihr Bezug zu den Daten-Contexts in Thymeleaf
Die zueinander gehörenden Definitionen in der *ReportingReportvorlage* und der *HtmlTemplateDefinition* sind gleich zu benennen. Dabei werden Großbuchstaben verwendet.

Der erste Teil des Namens ist der Bereich, aus dem die Daten geholt werden sollen und der zweite Teil der gewünschte Name der Vorlage. Beide werden durch ein "\_v_" voneinander getrennt. Folgende Bereiche gibt es:
* SCHUELER_v_
* KLASSEN_v_
* KURSE_v_
* LEHRER_v_
* GOST_KURSPLANUNG_v_
* GOST_KLAUSURPLANUNG_v_

Zu jedem dieser Bereiche kann es eine oder mehrere "Datenquellen" in Thymeleaf geben, dort werden sie als *Context* bezeichnet. Aktuell können davon folgende Contexts angesprochen werden:
* Benennung SCHUELER_v_ entspricht dem Context "**Schueler**" in der html-Datei
* Benennung KLASSEN_v_ entspricht dem Context "**Klassen**" in der html-Datei
* Benennung KURSE_v_ entspricht dem Context "**Kurse**" in der html-Datei
* Benennung LEHRER_v_ entspricht dem Context "**Lehrer**" in der html-Datei
* Benennung GOST_KURSPLANUNG_v_ entspricht dem Context "**GostBlockungsergebnis**" in der html-Datei
* Benennung GOST_KLAUSURPLANUNG_v_ entspricht dem Context "**GostKlausurplan**" in der html-Datei

Unabhängig davon gibt es noch die Contexts "**Parameter**" (Daten der übergebenen ReportingParameter) und "**Schule**" (Daten der Schule), der immer angesprochen werden kann.

Zudem werden bei folgenden Bezeichnungen noch weitere Daten initialisiert und geprüft.
* SCHUELER_v_GOST_LAUFBAHNPLANUNG_
* SCHUELER_v_GOST_ABITUR_

Sofern daher Daten aus diesem Bereich verwendet werden sollen, ist es erforderlich die Benennung entsprechend zu verwenden, damit direkt beim Aufruf die notwendigen Daten geladen und validiert werden.

### Die Enum *HtmlTemplateDefinition* und ihre Angaben
Jede *HtmlTemplateDefinition* enthält folgende Angaben:
* Die zugehörige ReportingReportvorlage aus dem Core.
* Eine Angabe zum Root-Pfad unter dem alle Dateien zu finden sind (html, css, fonts).
* Die Angabe des Pfads zur html-Datei relativ zum Root-Pfad.
* Ein (statischer) Dateiname ohne Dateiendung.
* Ein html-Absatz (\<p>), welcher genutzt wird, um Dateinamen ohne Dateiendung mit Daten aus den Daten-Contexts zu nutzen. Dazu kann in diesem Absatz Thymeleaf verwendet werden.
* Die notwendigen Benutzerrechte für die Nutzung des Templates.

In der Regel ist eine Orientierung an den bereits definierten html-Templates sinnvoll.

## Html-Templates
Die html-Template-Dateien werden im Reporting-Modul unter resources gespeichert. Dort ist bereits eine Ordnerstruktur angelegt unter svws-module-reporting/src/main/resources/de/svws_nrw/module/reporting.

Alle Templates verwenden Thymeleaf. Dieses definiert die zur Verfügung stehenden Möglichkeiten bei der Generierung, welche unter [https://www.thymeleaf.org/documentation.html](https://www.thymeleaf.org/documentation.html) dokumentiert sind.

### Ordnerstruktur
Zu der oben bereits angesprochenen Ordnerstruktur unter resources ist Folgendes zu sagen.

* Der css-Ordner enthält css-Dateien, die von mehreren html-Dateien gemeinsam genutzt werden können. Einige davon definieren auch Einstellungen für die PDF-Ausgabe (Print-CSS).
* Der fonts-Ordner enthält den Unicode-Font, der für die html-Dateien verwendet wird.
* Die weiteren Ordner enthalten thematisch sortiert die html-Templates.

### css-Dateien
Grundsätzlich können in einem html-Template mehrere css-Dateien verwendet werden. Dies wird genutzt, um zum einen viele immer wieder verwendete Styles zentral zu speichern (diese liegen im css-Ordner) und zum anderen in einer weiteren css-Datei individuelle Styles für ein bestimmtes Template zu definieren (liegt parallel zur html-Datei).

Die zentral abgelegten css-Dateien sind aufgeteilt in ein css für die PDF-Bibliothek, ein css für die Seiteneinstellungen mit Köpfen und Füßen und ein css mit den immer wieder verwendeten Styles für die Formatierungen.

Das Einbinden der css-Datei in html muss mittels Thymeleaf erfolgen. Dazu wird *th:href* verwendet, damit die Pfade im realen Betrieb auch korrekt gesetzt werden. Ein solcher Aufruf wäre: *\<link rel="stylesheet" th:href="@{css/svws-reporting.css}" href="../../../css/svws-reporting.css" />*. Auf diese Art wird sowohl eine Vorschau bei der Entwicklung als auch die PDF-Generierung unterstützt.

### Html-Dateien erstellen
In der Regel ist es am einfachsten eine bestehende Datei zu kopieren, da so bereits der head mit Angaben zu Thymeleaf und den css-Dateien vorhanden ist und abgesehen von Anpassungen der Dateinamen an dieser Stelle wenig zu tun ist. Danach kann zur Erstellung des Bodies übergehen werden.

An dieser Stelle müsste jetzt ein html- und Thymeleaf-Tutorial folgen, was aber den Umfang hier sprengen würde. D. h. aber, dass für die folgenden Schritte vorausgesetzt wird, dass man mit html und Thymeleaf vertraut ist. Daher werden nur noch einige Besonderheiten mit Bezug auf das Reporting angesprochen.

#### Contexts - Namen und Objekt-Charakter
Um die Templates mit Daten zu füllen, stehen folgende Contexts zur Verfügung, die bei entsprechend gewählter [Benennung](#die-benennung-der-reportingreportvorlage-und-der-htmltemplatedefinition-und-ihr-bezug-zu-den-daten-contexts-in-thymeleaf) des Templates mit Daten gefüllt werden.
* GostBlockungsergebnis
* GostKlausurplan
* Parameter (wird immer gefüllt, enthält die übergebenen Daten der ReportingParameter)
* Schueler
* Klassen
* Kurse
* Lehrer
* Schule (wird immer gefüllt, enthält die Daten der Schule)

Diese Contexts sind Java-Objekte und können mit ihren Attributen und Methoden im Template verwendet werden, sofern Thymeleaf dies auch zulässt.

#### Seiten - Layout, Einstellungen, Köpfe und Füße
Generell wird für das Seitenlayout [Print-CSS](https://wiki.selfhtml.org/wiki/Print-CSS) verwendet. Damit lassen sich viele Einstellungen für den Druck konfigurieren. Leider unterstützen aktuell die Browser dafür keine direkte Vorschau oder die Vorschau ist fehlerhaft. Daher sieht man das fertige Ergebnis erst nach dem Druck des Dokumentes.

Zur Erstellung von Seitenköpfen- und füßen werden die sogenannten [Page-Margin-Boxs](https://wiki.selfhtml.org/wiki/Print-CSS#Zukunft:_Page-Margin_Boxen) verwendet. Auch hier gilt, dass die Vorschau aktuell nicht möglich oder fehlerhaft ist. In den bereits vorhandenen CSS-Dateien sind auch bereits Boxs eingerichtet und können im html-Template zu Beginn des Body eingefügt werden.

Wichtig ist aber stets zu bedenken: Da die Erstellung der Ausgabe durch Rendern der html-Datei erfolgt, ist diese vom Programm abhängig, dass das html rendert. Hier unterscheiden sich schon in der Regel die Browser.
Der SVWS-Server nutzt dafür OpenHtmlToPdf, welches zwar viele der Print-CSS Features unterstützt, dafür aber keinerlei css-Elemente, wie sie für moderne dynamische Webseiten verwendet werden, um deren Layout zu gestalten. Daher bleibt nur die Möglichkeit, Tabellen für die Layouterstellung zu verwenden.

#### Thymeleaf-Dialekt #convert
Um neben den bereits implementierten Thymeleaf-Funktionen noch weitere Funktionen zur Verfügung stellen zu können, wurde Thymeleaf um einen sogenannten Dialekt erweitert.
Dieser heißt #convert und stellt aktuell Umwandlungen für Datumsangaben zur Verfügung.

Ein Beispiel wäre: *th:text="${#convert.toDateDE(schueler.geburtsdatum())}"*

Leider unterstützen die Plugins die Dialekte nicht. Daher können die zur Verfügung stehenden Methoden nur in deren Definition eingesehen werden. Für #convert ist diese hier zu finden: [#convert](html/dialects/ConvertExpressionHelper.java).


## Report-Ausgabe testen
Die erstellten html-Templates können im Browser oder IDE angezeigt werden, wenn man die dortige Vorschau nutzt. Dabei wird aber nur der statische Teil des html angezeigt, es werden keine Daten in das Template geladen.

Möchte man eine Vorschau mit Daten erstellen, kann man die Debug-Schnittstelle unter localhast/debug aufrufen und dort den API-Endpunkt *Reportausgabe* auf. Die anzugebenen Parameter werden als JSON übergeben. Entsprechend sind die Eingaben vorzunehmen.
