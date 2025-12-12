[# th:if="${Klassen.isEmpty()}"]
	Klasse-Liste-Schueler-Kontaktdaten-Erzieher
[/]
[# th:if="${!Klassen.isEmpty()}"]
	[# th:each="klasse,iterState : ${Klassen}"]
		[# th:if="${iterState.first && (Klassen.size() == 1)}"]
			Klasse-Liste-Schueler-Kontaktdaten-Erzieher_[(${#strings.replace(klasse.kuerzel(), ' ', '_')})]
		[/]
		[# th:if="${iterState.first && (Klassen.size() > 1)}"]
			Klasse-Liste-Schueler-Kontaktdaten-Erzieher
		[/]
	[/]
[/]
