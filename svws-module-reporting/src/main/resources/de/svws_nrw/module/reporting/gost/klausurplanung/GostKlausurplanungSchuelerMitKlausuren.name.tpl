[# th:with="schueler = ${GostKlausurplan.schueler()}, anzahlSchueler = ${#lists.size(schueler)}"]
    [# th:if="${anzahlSchueler == 0}"]
        GOSt-Klausurplanung-Schueler-Klausuren_[(${#strings.replace(#strings.replace(Schule.auswahlSchuljahresabschnitt().textSchuljahresabschnittKurz(), '.', ''), '/', '-')})]
    [/]

    [# th:if="${anzahlSchueler != 0}"]
        [# th:each="schueler,iterState : ${schueler}"]
            [# th:if="${iterState.first && (anzahlSchueler == 1)}"]
                GOSt-Klausurplanung-Schueler-Klausuren_[(${#strings.replace(#strings.replace(Schule.auswahlSchuljahresabschnitt().textSchuljahresabschnittKurz(), '.', ''), '/', '-')})]_
                [(${#strings.replace(schueler.nachname(), ' ', '_')})]__
                [(${#strings.replace(schueler.vorname(), ' ', '_')})]_([(${schueler.id()})])_
                [(${#dates.format(#dates.createNow(), 'yyyyMMdd-HHmm')})]
            [/]
            [# th:if="${iterState.first && (anzahlSchueler > 1)}"]
                GOSt-Klausurplanung-Schueler-Klausuren_[(${#strings.replace(#strings.replace(Schule.auswahlSchuljahresabschnitt().textSchuljahresabschnittKurz(), '.', ''), '/', '-')})]
            [/]
        [/]
    [/]
[/]
