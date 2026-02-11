[# th:if="${Schueler.isEmpty()}"]
    Schulbescheinigung
[/]
[# th:if="${!Schueler.isEmpty()}"]
    [# th:each="schueler,iterState : ${Schueler}"]
        [# th:if="${iterState.first && (Schueler.size() == 1)}"]
            Schulbescheinigung_[(${ #strings.replace(schueler.nachname(), ' ', '_') })]__[(${ #strings.replace(schueler.vorname(), ' ', '_') })]_([(${ schueler.id() })])_[(${ #dates.format(#dates.createNow(), 'yyyyMMdd-HHmm') })]
        [/]
        [# th:if="${iterState.first && (Schueler.size() > 1)}"]
            Schulbescheinigungen
        [/]
    [/]
[/]
