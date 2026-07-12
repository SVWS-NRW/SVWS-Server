GOSt-Blockungsergebnis-Kurse-Statistikwerte_Abi[(${GostBlockungsergebnis.abiturjahr()})]_[(${#strings.replace(GostBlockungsergebnis.gostHalbjahr().kuerzel, '.', '')})]_(Erg-ID-[(${GostBlockungsergebnis.id()})])
[# th:if="${VorlageParameter.get('dateinameMitZeitstempel')}"]_[(${ #dates.format(#dates.createNow(), 'yyyyMMdd-HHmm') })][/]
