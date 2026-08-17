[# th:with="kurse = ${Kurse}, anzahl = ${#lists.size(kurse)}"]
    Kurs-Liste-Schueler-Fotos-Namen
    [# th:if="${anzahl == 1}"]
        _[(${ #strings.replace(kurse[0].kuerzel(), ' ', '_') })]
    [/]
[/]
[# th:if="${VorlageParameter.get('dateinameMitZeitstempel')}"]_[(${ #aktuell.formatiert('yyyyMMdd-HHmm') })][/]
