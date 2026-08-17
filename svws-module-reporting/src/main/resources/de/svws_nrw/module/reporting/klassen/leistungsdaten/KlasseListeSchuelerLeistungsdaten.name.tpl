[# th:with="klassen = ${Klassen}, anzahl = ${#lists.size(klassen)}"]
    Klasse-Liste-Schueler-Leistungsdaten
    [# th:if="${anzahl == 1}"]
        _[(${ #strings.replace(klassen[0].kuerzel(), ' ', '_') })]
    [/]
[/]
[# th:if="${VorlageParameter.get('dateinameMitZeitstempel')}"]_[(${ #aktuell.formatiert('yyyyMMdd-HHmm') })][/]
