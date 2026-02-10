[# th:if="${Lehrer.isEmpty()}"]
    Lehrer-Stammdatenliste
[/]
[# th:if="${!Lehrer.isEmpty()}"]
    [# th:each="lehrer,iterState : ${Lehrer}"]
        [# th:if="${iterState.first}"]
            Lehrer-Stammdatenliste_[(${ #dates.format(#dates.createNow(), 'yyyyMMdd-HHmm') })]
        [/]
    [/]
[/]

