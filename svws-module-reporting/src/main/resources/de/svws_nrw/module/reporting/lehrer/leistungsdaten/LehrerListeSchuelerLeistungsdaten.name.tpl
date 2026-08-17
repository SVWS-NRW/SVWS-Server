[# th:with="lehrer = ${Lehrer}, anzahl = ${#lists.size(lehrer)}"]
    Lehrer-Liste-Schueler-Leistungsdaten
    [# th:if="${anzahl == 1}"]
        _[(${ #strings.replace(lehrer[0].kuerzel(), ' ', '_') })]
    [/]
[/]
[# th:if="${VorlageParameter.get('dateinameMitZeitstempel')}"]_[(${ #aktuell.formatiert('yyyyMMdd-HHmm') })][/]
