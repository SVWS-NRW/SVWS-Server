[# th:with="schueler = ${Schueler}, anzahl = ${#lists.size(schueler)}"]
    GOSt-Laufbahnplanung-Pruefungsergebnisse
    [# th:if="${anzahl > 0}"]
        _Abi[(${ schueler[0].gostLaufbahnplanung().abiturjahr() + '_' + #strings.replace(schueler[0].gostLaufbahnplanung().auswahlGOStHalbjahr(), '.', '') })]
    [/]
[/]
[# th:if="${VorlageParameter.get('dateinameMitZeitstempel')}"]_[(${ #aktuell.formatiert('yyyyMMdd-HHmm') })][/]
