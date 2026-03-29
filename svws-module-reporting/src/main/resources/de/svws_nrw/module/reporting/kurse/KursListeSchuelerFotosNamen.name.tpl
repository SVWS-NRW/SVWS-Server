[# th:if="${Kurse.isEmpty()}"]
	Kurs-Liste-Schueler-Fotos-Namen
[/]
[# th:if="${!Kurse.isEmpty()}"]
	[# th:each="kurs,iterState : ${Kurse}"]
		[# th:if="${iterState.first && (Kurse.size() == 1)}"]
			Kurs-Liste-Schueler-Fotos-Namen_[(${#strings.replace(kurs.kuerzel(), ' ', '_')})]
		[/]
		[# th:if="${iterState.first && (Kurse.size() > 1)}"]
			Kurs-Liste-Schueler-Fotos-Namen
		[/]
	[/]
[/]
