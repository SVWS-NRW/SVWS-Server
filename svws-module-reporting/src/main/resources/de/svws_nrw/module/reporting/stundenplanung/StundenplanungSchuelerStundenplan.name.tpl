[# th:with="stundenplaene = ${SchuelerStundenplaene}, anzahl = ${#lists.size(stundenplaene)}, schuljahresabschnitt = ${#strings.replace(#strings.replace(Schule.auswahlSchuljahresabschnitt().textSchuljahresabschnittKurz(), '.', ''), '/', '-')}"]
    [# th:if="${anzahl == 1}"]
        Schueler-Stundenplan_[(${schuljahresabschnitt})]_[(${ #strings.replace(stundenplaene[0].schueler().nachname(), ' ', '_') })]__[(${ #strings.replace(stundenplaene[0].schueler().vorname(), ' ', '_') })]_([(${ stundenplaene[0].schueler().id() })])
    [/]
    [# th:if="${anzahl != 1}"]
        Schueler-Stundenplaene_[(${schuljahresabschnitt})]
    [/]
[/]
[# th:if="${VorlageParameter.get('dateinameMitZeitstempel')}"]_[(${ #aktuell.formatiert('yyyyMMdd-HHmm') })][/]
