package de.svws_nrw.service.schueler.lernabschnitt;

public record SchuelerLernabschnittCreateRequest(
		long idSchueler,
		long idSchuljahresabschnitt,
		Long idKlasse,
		Long idJahrgang
) { }
