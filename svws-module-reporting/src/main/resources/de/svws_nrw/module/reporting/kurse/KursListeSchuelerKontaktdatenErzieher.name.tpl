[# th:if="${Kurse.isEmpty()}"]
	Kurs-Liste-Schueler-Kontaktdaten-Erzieher
[/]
[# th:if="${!Kurse.isEmpty()}"]
	[# th:each="kurs,iterState : ${Kurse}"]
		[# th:if="${iterState.first && (Kurse.size() == 1)}"]
			Kurs-Liste-Schueler-Kontaktdaten-Erzieher_[(${#strings.replace(kurs.kuerzel(), ' ', '_')})]-[(${#strings.replace(#strings.replace(kurs.auflistungJahrgaenge(), ' ', '_'), ',', '-')})]
		[/]
		[# th:if="${iterState.first && (Kurse.size() > 1)}"]
			Kurs-Liste-Schueler-Kontaktdaten-Erzieher
		[/]
	[/]
[/]
