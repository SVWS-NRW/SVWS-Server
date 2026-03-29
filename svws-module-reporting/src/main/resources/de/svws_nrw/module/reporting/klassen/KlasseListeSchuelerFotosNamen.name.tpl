[# th:if="${Klassen.isEmpty()}"]
	Klasse-Liste-Schueler-Fotos-Namen
[/]
[# th:if="${!Klassen.isEmpty()}"]
	[# th:each="klasse,iterState : ${Klassen}"]
		[# th:if="${iterState.first && (Klassen.size() == 1)}"]
			Klasse-Liste-Schueler-Fotos-Namen_[(${#strings.replace(klasse.kuerzel(), ' ', '_')})]
		[/]
		[# th:if="${iterState.first && (Klassen.size() > 1)}"]
			Klasse-Liste-Schueler-Fotos-Namen
		[/]
	[/]
[/]
