[# th:if="${Schueler.isEmpty()}"]
    Schueler-Liste-Kontaktdaten-Erzieher
[/]
[# th:if="${!Schueler.isEmpty()}"]
    [# th:each="schueler,iterState : ${Schueler}"]
        [# th:if="${iterState.first}"]
            Schueler-Liste-Kontaktdaten-Erzieher_[(${ #dates.format(#dates.createNow(), 'yyyyMMdd-HHmm') })]
        [/]
    [/]
[/]

