package de.svws_nrw.data.klassen;

import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

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
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
    	final DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, id);
    	if (klasse == null)
			return OperationError.NOT_FOUND.getResponse();
    	final List<DTOKlassenLeitung> klassenLeitungen = conn.queryNamed("DTOKlassenLeitung.klassen_id", klasse.ID, DTOKlassenLeitung.class);
    	// Bestimme die Schüler der Klasse
    	final List<Long> schuelerIDs = conn.queryNamed("DTOSchuelerLernabschnittsdaten.klassen_id", klasse.ID, DTOSchuelerLernabschnittsdaten.class)
    			.stream().filter(sla -> sla.WechselNr == null).map(sla -> sla.Schueler_ID).toList();
    	final List<DTOSchueler> dtoSchueler = schuelerIDs == null || schuelerIDs.size() == 0 ? new ArrayList<>()
    			: conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class);
    	// Erstelle das Core-DTO-Objekt für die Klasse
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
		for (final DTOSchueler dto : dtoSchueler)
			daten.schueler.add(DataSchuelerliste.mapToSchueler.apply(dto));
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
