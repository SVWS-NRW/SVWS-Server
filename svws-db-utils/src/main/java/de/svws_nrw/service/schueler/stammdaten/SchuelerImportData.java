package de.svws_nrw.service.schueler.stammdaten;

/**
 * Enthält die minimalen Stammdaten, die für das Anlegen eines Schülers aus einem externen Import benötigt werden.
 *
 * @param nachname            der Nachname des Schülers
 * @param vorname             der Vorname des Schülers
 * @param alleVornamen        alle Vornamen des Schülers, sofern mehrere vorhanden
 * @param idGeschlecht        die ID des Geschlechts
 * @param geburtsdatum        das Geburtsdatum im Format YYYY-MM-DD
 * @param idSchuelerStatus    die ID des aktuellen Schülerstatus
 * @param anmeldedatum        das Anmeldedatum im Format YYYY-MM-DD
 * @param aufnahmedatum       das Aufnahmedatum im Format YYYY-MM-DD
 * @param beginnBildungsgang  der Beginn des Bildungsgangs im Format YYYY-MM-DD
 * @param dauerBildungsgang   die Dauer des Bildungsgangs am BK
 * @param idReligion          die ID der Religion
 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
 */
public record SchuelerImportData(
		String nachname,
		String vorname,
		String alleVornamen,
		int idGeschlecht,
		String geburtsdatum,
		int idSchuelerStatus,
		String anmeldedatum,
		String aufnahmedatum,
		String beginnBildungsgang,
		Integer dauerBildungsgang,
		Long idReligion,
		Long idSchuljahresabschnitt
) {

}
