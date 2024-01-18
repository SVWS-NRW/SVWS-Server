Applikationen:

 1) Schüler
        -Laufbahnplanung    
            Pages:
                Die VueJS-Komponenten mit den enthaltenen HTML-Elementen werden in einzelnen Dateien, welche mit Präfix Page.... anfangen und mit dem 
                Komponentennamen folgen, mit playwright-Locater abgebildet und mit entpsrechen event-Funktionen versehen.
            !!! Alle Pages-Objekte müssen in der Datei SchuelerLaufbahnplanung.pages.ts registriert und als Instanz erzeugt werden. Diese Datei erbt von der Schueler.pages.ts und somit stehen die Page-Objekte vom Oberverzeichnis in dem aktuellen Verzeichnis zur Verfügung.

            Tests:
                Für jedes Pages wird eine eigene Testdatei (z.B. PageSvwsUISubnav.ts -> SvwsUiSubnav.test.ts) erstellt, die die tests enthält.