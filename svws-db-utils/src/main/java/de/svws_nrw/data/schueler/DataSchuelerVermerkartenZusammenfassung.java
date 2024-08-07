package de.svws_nrw.data.schueler;

import java.util.List;

import de.svws_nrw.core.data.schueler.SchuelerVermerkartZusammenfassung;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link SchuelerVermerkartZusammenfassung}DataManagerRevised
 */
public final class DataSchuelerVermerkartenZusammenfassung extends DataManagerRevised<Long, DTOSchueler, SchuelerVermerkartZusammenfassung> {

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden
	 * soll
	 */
	public DataSchuelerVermerkartenZusammenfassung(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public SchuelerVermerkartZusammenfassung map(final DTOSchueler dtoSchueler) {
		final SchuelerVermerkartZusammenfassung schuelerVermerkartZusammenfassung = new SchuelerVermerkartZusammenfassung();
		schuelerVermerkartZusammenfassung.id = dtoSchueler.ID;
		schuelerVermerkartZusammenfassung.nachname = dtoSchueler.Nachname;
		schuelerVermerkartZusammenfassung.vorname = (dtoSchueler.Vorname == null) ? "" : dtoSchueler.Vorname;
		return schuelerVermerkartZusammenfassung;
	}

	/**
	 * Methode zum Befüllen des Core-DTOs SchuelerVermerkartZusammenfassung aus DTOSchueler
	 *
	 *  @param dtoSchueler   		DTO Object mit den Schüelerdaten
	 * 	@param anzahlVermerke 		die Anzahl der eingesetzen Vermerke
	 * 	@param vermerkartId			die spezifische VermerkartId
	 *
	 *  @return das SchuelerVermerkartZusammenfassung Object mit Stammdaten und der anzahl der genutzten Vermerke.
	 */
	public SchuelerVermerkartZusammenfassung map(final DTOSchueler dtoSchueler, final Long anzahlVermerke, final Long vermerkartId) {
		final SchuelerVermerkartZusammenfassung schuelerVermerkartZusammenfassung = map(dtoSchueler);
		schuelerVermerkartZusammenfassung.vermerkart = vermerkartId;
		schuelerVermerkartZusammenfassung.anzahlVermerke = anzahlVermerke;
		return schuelerVermerkartZusammenfassung;
	}

	/**
	 * Gibt die Liste von reduzierten Schülerbjekten zurück welche Vermerke einer bestimmten Vermerkart haben.
	 *
	 * @param vermerkartID    die Vermerkart ID
	 *
	 * @return die Liste der Schüler mit {ID, Vorname, Nachname, [Anzahl der Vermerke der übergebenen Vermerkart]}
	 *
	 */
	public Response getListByVermerkartIdAsResponse(final long vermerkartID) {
		final String query = """
						SELECT schueler, COUNT(schueler.ID) as anzahlVermerke
						FROM DTOSchueler schueler
						JOIN DTOSchuelerVermerke sv on sv.Schueler_ID=schueler.ID
						JOIN DTOVermerkArt vermerkart on sv.VermerkArt_ID=vermerkart.ID
						WHERE vermerkart.ID= :vermerkartID
						GROUP BY schueler.ID
						""";
		final List<Object[]> dtoSchuelerListe = conn.query(query, Object[].class).setParameter("vermerkartID", vermerkartID).getResultList();
		final List<SchuelerVermerkartZusammenfassung> schuelerVermerkartZusammenfassungListe = dtoSchuelerListe.stream()
				.map(element -> map((DTOSchueler) element[0], (Long) element[1], vermerkartID)).toList();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(schuelerVermerkartZusammenfassungListe).build();
	}

}
