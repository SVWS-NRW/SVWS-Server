GOSt-Klausurplanung-Klausurtermine-Kurse_[(${#strings.replace(#strings.replace(Schule.auswahlSchuljahresabschnitt().textSchuljahresabschnittKurz(), '.', ''), '/', '-')})]
[# th:if="${VorlageParameter.get('dateinameMitZeitstempel')}"]_[(${ #dates.format(#dates.createNow(), 'yyyyMMdd-HHmm') })][/]
