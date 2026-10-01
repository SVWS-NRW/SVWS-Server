[# th:with="lehrer = ${Lehrer}, anzahl = ${#lists.size(lehrer)}"]
    [# th:if="${anzahl == 0}"]
        Lehrer-Zugangsdaten-Notenmodul
    [/]
    [# th:if="${anzahl == 1}"]
        Lehrer-Zugangsdaten-Notenmodul_[(${ #strings.replace(lehrer[0].nachname(), ' ', '_') })]__[(${ #strings.replace(lehrer[0].vorname(), ' ', '_') })]_([(${ lehrer[0].id() })])
    [/]
    [# th:if="${anzahl > 1}"]
        Lehrer-Zugangsdaten-Notenmodul
    [/]
[/]
[# th:if="${VorlageParameter.get('dateinameMitZeitstempel')}"]_[(${ #aktuell.formatiert('yyyyMMdd-HHmm') })][/]
