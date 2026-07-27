GOSt-Blockungsergebnis-Kurse-Statistikwerte_Abi[(${GostBlockungsergebnis.abiturjahr()})]_[(${GostBlockungsergebnis.gostHalbjahr() != null ? #strings.replace(GostBlockungsergebnis.gostHalbjahr().kuerzel, '.', '') : ''})]_(Erg-ID-[(${GostBlockungsergebnis.id()})])
[# th:if="${VorlageParameter.get('dateinameMitZeitstempel')}"]_[(${ #dates.format(#aktuell.jetztAlsDate(), 'yyyyMMdd-HHmm') })][/]
