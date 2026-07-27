[# th:with="schueler = ${GostBlockungsergebnis.schueler()}, anzahlSchueler = ${#lists.size(schueler)}"]
    GOSt-Blockungsergebnis-Schueler-Schienen-Kurse_Abi[(${ GostBlockungsergebnis.abiturjahr() })]_[(${ GostBlockungsergebnis.gostHalbjahr() != null ? #strings.replace(GostBlockungsergebnis.gostHalbjahr().kuerzel, '.', '') : '' })]_(Erg-ID-[(${ GostBlockungsergebnis.id() })])
    [# th:if="${anzahlSchueler == 1}"]
        _[(${#strings.replace(schueler[0].nachname(), ' ', '_')})]__[(${#strings.replace(schueler[0].vorname(), ' ', '_')})]_([(${schueler[0].id()})])
    [/]
[/]
[# th:if="${VorlageParameter.get('dateinameMitZeitstempel')}"]_[(${ #dates.format(#aktuell.jetztAlsDate(), 'yyyyMMdd-HHmm') })][/]
