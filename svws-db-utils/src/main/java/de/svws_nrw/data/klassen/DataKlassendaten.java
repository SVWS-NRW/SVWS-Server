package de.svws_nrw.data.klassen;

import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KlassenDaten}.
 */
public final class DataKlassendaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KlassenDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKlassendaten(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Mapper zum Erstellen des Core-DTOs aus den Datenbank-DTOs zu den Klassen, Klassenleitungen und Schülern.
	 *
	 * @param klasse			Klasse
	 * @param klassenLeitungen	Leitungen der Klasse
	 * @param schueler			Schüler der Klasse
	 *
	 * @return	Core-DTO mit allen Klasseninformationen
	 */
	private static KlassenDaten dtoMapper(final DTOKlassen klasse, final List<DTOKlassenLeitung> klassenLeitungen, final List<DTOSchueler> schueler) {
		final KlassenDaten daten = new KlassenDaten();
		daten.id = klasse.ID;
		daten.kuerzel = klasse.Klasse;
		daten.idJahrgang = klasse.Jahrgang_ID;
		daten.parallelitaet = klasse.ASDKlasse.length() < 3 ? null : klasse.ASDKlasse.substring(2, 3);
		daten.sortierung = klasse.Sortierung;
		daten.istSichtbar = klasse.Sichtbar;
		if (klassenLeitungen != null)
			for (final DTOKlassenLeitung kl : klassenLeitungen)
				daten.klassenLeitungen.add(kl.Lehrer_ID);
		for (final DTOSchueler dto : schueler)
			daten.schueler.add(DataSchuelerliste.mapToSchueler.apply(dto));
		return daten;
	}

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long id) {
		final KlassenDaten daten = getFromID(id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Gibt die Daten einer Klasse zu deren ID zurück.
	 * @param id	Die ID der Klasse.
	 * @return		Die Daten der KLasse zur ID.
	 */
	public KlassenDaten getFromID(final Long id) throws WebApplicationException {
		if (id == null)
			throw OperationError.NOT_FOUND.exception("Keine ID für die Klasse übergeben.");
		final DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, id);
		if (klasse == null)
			throw OperationError.NOT_FOUND.exception("Keine Klasse zur ID " + id + " gefunden.");
		final List<DTOKlassenLeitung> klassenLeitungen = conn.queryNamed("DTOKlassenLeitung.klassen_id", klasse.ID, DTOKlassenLeitung.class);
		// Bestimme die Schüler der Klasse
		final List<Long> schuelerIDs = conn.queryNamed("DTOSchuelerLernabschnittsdaten.klassen_id", klasse.ID, DTOSchuelerLernabschnittsdaten.class)
			.stream().filter(sla -> sla.WechselNr == 0).map(sla -> sla.Schueler_ID).toList();
		final List<DTOSchueler> dtoSchueler = schuelerIDs == null || schuelerIDs.isEmpty() ? new ArrayList<>()
			: conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class);
		// Erstelle das Core-DTO-Objekt für die Klasse
		return dtoMapper(klasse, klassenLeitungen, dtoSchueler);
	}


	/**
	 * Gibt die Daten einer Klasse zu deren ID OHNE Schülerliste zurück.
	 * @param id	Die ID der Klasse.
	 * @return		Die Daten der KLasse zur ID OHNE Schülerliste.
	 */
	public KlassenDaten getFromIDOhneSchueler(final Long id) throws WebApplicationException {
		if (id == null)
			throw OperationError.NOT_FOUND.exception("Keine ID für die Klasse übergeben.");
		final DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, id);
		if (klasse == null)
			throw OperationError.NOT_FOUND.exception("Keine Klasse zur ID " + id + " gefunden.");
		final List<DTOKlassenLeitung> klassenLeitungen = conn.queryNamed("DTOKlassenLeitung.klassen_id", klasse.ID, DTOKlassenLeitung.class);
		// Erstelle das Core-DTO-Objekt für die Klasse
		return dtoMapper(klasse, klassenLeitungen, Collections.emptyList());
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
