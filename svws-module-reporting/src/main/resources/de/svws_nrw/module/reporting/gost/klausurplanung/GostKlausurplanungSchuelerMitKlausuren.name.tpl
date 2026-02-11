[# th:with="gefilterteSchueler = ${GostKlausurplan.schuelerGefiltert()}, anzahlGefilterteSchueler = ${#lists.size(gefilterteSchueler)}"]
    [# th:if="${anzahlGefilterteSchueler == 0}"]
        GOSt-Klausurplanung-Schueler-Klausuren_[(${#strings.replace(#strings.replace(Schule.auswahlSchuljahresabschnitt().textSchuljahresabschnittKurz(), '.', ''), '/', '-')})]
    [/]

    [# th:if="${anzahlGefilterteSchueler != 0}"]
        [# th:each="schueler,iterState : ${gefilterteSchueler}"]
            [# th:if="${iterState.first && (anzahlGefilterteSchueler == 1)}"]
                GOSt-Klausurplanung-Schueler-Klausuren_[(${#strings.replace(#strings.replace(Schule.auswahlSchuljahresabschnitt().textSchuljahresabschnittKurz(), '.', ''), '/', '-')})]_
                [(${#strings.replace(schueler.nachname(), ' ', '_')})]__
                [(${#strings.replace(schueler.vorname(), ' ', '_')})]_([(${schueler.id()})])_
                [(${#dates.format(#dates.createNow(), 'yyyyMMdd-HHmm')})]
            [/]
            [# th:if="${iterState.first && (anzahlGefilterteSchueler > 1)}"]
                GOSt-Klausurplanung-Schueler-Klausuren_[(${#strings.replace(#strings.replace(Schule.auswahlSchuljahresabschnitt().textSchuljahresabschnittKurz(), '.', ''), '/', '-')})]
            [/]
        [/]
    [/]
[/]
