[# th:with="schueler = ${GostKlausurplan.schueler()}, anzahlSchueler = ${#lists.size(schueler)}, schuljahresabschnitt = ${#strings.replace(#strings.replace(Schule.auswahlSchuljahresabschnitt().textSchuljahresabschnittKurz(), '.', ''), '/', '-')}"]
    GOSt-Klausurplanung-Schueler-Klausuren_[(${schuljahresabschnitt})]
    [# th:if="${anzahlSchueler == 1}"]
        _[(${#strings.replace(schueler[0].nachname(), ' ', '_')})]__[(${#strings.replace(schueler[0].vorname(), ' ', '_')})]_([(${schueler[0].id()})])
    [/]
[/]
[# th:if="${VorlageParameter.get('dateinameMitZeitstempel')}"]_[(${ #aktuell.formatiert('yyyyMMdd-HHmm') })][/]
