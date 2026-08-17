GOSt-Blockungsergebnis-Kurse-Statistikwerte_Abi[(${GostBlockungsergebnis.abiturjahr()})]_[(${GostBlockungsergebnis.gostHalbjahr() != null ? #strings.replace(GostBlockungsergebnis.gostHalbjahr().kuerzel, '.', '') : ''})]_(Erg-ID-[(${GostBlockungsergebnis.id()})])
[# th:if="${VorlageParameter.get('dateinameMitZeitstempel')}"]_[(${ #aktuell.formatiert('yyyyMMdd-HHmm') })][/]
