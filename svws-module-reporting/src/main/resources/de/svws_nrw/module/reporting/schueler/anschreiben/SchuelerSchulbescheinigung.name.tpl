[# th:with="schueler = ${Schueler}, anzahl = ${#lists.size(schueler)}"]
    [# th:if="${anzahl == 0}"]
        Schulbescheinigung
    [/]
    [# th:if="${anzahl == 1}"]
        Schulbescheinigung_[(${ #strings.replace(schueler[0].nachname(), ' ', '_') })]__[(${ #strings.replace(schueler[0].vorname(), ' ', '_') })]_([(${ schueler[0].id() })])
    [/]
    [# th:if="${anzahl > 1}"]
        Schulbescheinigungen
    [/]
[/]
[# th:if="${VorlageParameter.get('dateinameMitZeitstempel')}"]_[(${ #aktuell.formatiert('yyyyMMdd-HHmm') })][/]
