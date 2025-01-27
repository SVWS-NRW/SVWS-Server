package de.svws_nrw.data.schueler;

import java.util.List;

import de.svws_nrw.core.data.schueler.SchuelerEinwilligungsartenZusammenfassung;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link SchuelerEinwilligungsartenZusammenfassung}DataManagerRevised
 */
public final class DataSchuelerEinwilligungsartenZusammenfassung extends DataManagerRevised<Long, DTOSchueler, SchuelerEinwilligungsartenZusammenfassung> {

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden
	 * soll
	 */
	public DataSchuelerEinwilligungsartenZusammenfassung(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public SchuelerEinwilligungsartenZusammenfassung map(final DTOSchueler dtoSchueler) {
		final SchuelerEinwilligungsartenZusammenfassung schuelerEinwilligungsartenZusammenfassung = new SchuelerEinwilligungsartenZusammenfassung();
		schuelerEinwilligungsartenZusammenfassung.id = dtoSchueler.ID;
		schuelerEinwilligungsartenZusammenfassung.nachname = (dtoSchueler.Nachname == null) ? "" : dtoSchueler.Nachname;
		schuelerEinwilligungsartenZusammenfassung.vorname = (dtoSchueler.Vorname == null) ? "" : dtoSchueler.Vorname;
		return schuelerEinwilligungsartenZusammenfassung;
	}

	/**
	 * Methode zum Befüllen des Core-DTOs SchuelerEinwilligungsartenZusammenfassung aus DTOSchueler
	 *
	 *  @param dtoSchueler   		DTO Object mit den Schüelerdaten
	 * 	@param anzahlEinwilligungen 		die Anzahl der eingesetzen Einwilligungen
	 * 	@param idEinwilligungsart			die spezifische ID der Einwilligungsartart
	 *
	 *  @return das SchuelerEinwilligungsartenZusammenfassung Object mit Stammdaten und der Anzahl der genutzten Einwilligungen.
	 */
	public SchuelerEinwilligungsartenZusammenfassung map(final DTOSchueler dtoSchueler, final Long anzahlEinwilligungen, final Long idEinwilligungsart) {
		final SchuelerEinwilligungsartenZusammenfassung schuelerEinwilligungsartenZusammenfassung = map(dtoSchueler);
		schuelerEinwilligungsartenZusammenfassung.idEinwilligungsart = idEinwilligungsart;
		schuelerEinwilligungsartenZusammenfassung.anzahlEinwilligungen = anzahlEinwilligungen;
		return schuelerEinwilligungsartenZusammenfassung;
	}

	/**
	 * Gibt die Liste von reduzierten Schülerbjekten zurück welche Einwilligungen einer bestimmten Einwilligungsart haben.
	 *
	 * @param idEinwilligungsart    die Einwilligungsart ID
	 *
	 * @return die Liste der Schüler mit {ID, Vorname, Nachname, [Anzahl der Einwilligungen der übergebenen Einwilligungsart]}
	 *
	 */
	public Response getListByEinwilligungsartIdAsResponse(final long idEinwilligungsart) {
		final String query = """
						SELECT schueler, COUNT(schueler.ID) as anzahlEinwilligungen
						FROM DTOSchueler schueler
						JOIN DTOSchuelerDatenschutz sd on sd.Schueler_ID=schueler.ID
						JOIN DTOKatalogEinwilligungsart einwilligungsart on sd.Datenschutz_ID=einwilligungsart.ID
						WHERE einwilligungsart.ID= :idEinwilligungsart
						GROUP BY schueler.ID
						""";
		final List<Object[]> dtoSchuelerListe = conn.query(query, Object[].class).setParameter("idEinwilligungsart", idEinwilligungsart).getResultList();
		final List<SchuelerEinwilligungsartenZusammenfassung> schuelerEinwilligungsartenZusammenfassung = dtoSchuelerListe.stream()
				.map(element -> map((DTOSchueler) element[0], (Long) element[1], idEinwilligungsart)).toList();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(schuelerEinwilligungsartenZusammenfassung).build();
	}
}
