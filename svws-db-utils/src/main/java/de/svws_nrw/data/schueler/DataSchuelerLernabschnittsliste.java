package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schueler.SchuelerLernabschnittListeEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerLernabschnittListeEintrag}.
 */
public final class DataSchuelerLernabschnittsliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerLernabschnittListeEintrag}.
	 *
	 * @param conn    die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerLernabschnittsliste(final DBEntityManager conn) {
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


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchuelerLernabschnittsdaten} in einen Core-DTO {@link SchuelerLernabschnittListeEintrag}.
	 */
	private final Function<DTOSchuelerLernabschnittsdaten, SchuelerLernabschnittListeEintrag> dtoMapper = (final DTOSchuelerLernabschnittsdaten dto) -> {
		final SchuelerLernabschnittListeEintrag daten = new SchuelerLernabschnittListeEintrag();
		daten.id = dto.ID;
    	daten.schuelerID = dto.Schueler_ID;
    	daten.schuljahresabschnitt = dto.Schuljahresabschnitts_ID;
    	daten.wechselNr = dto.WechselNr;
    	daten.istGewertet = dto.SemesterWertung == null || dto.SemesterWertung;
    	daten.istWiederholung = dto.Wiederholung != null && dto.Wiederholung;
    	daten.pruefungsOrdnung = dto.PruefOrdnung;
    	daten.klassenID = dto.Klassen_ID;
    	daten.jahrgangID = dto.Jahrgang_ID;
    	daten.jahrgang = dto.ASDJahrgang;
		return daten;
	};


	@Override
	public Response get(final Long id) {
		// Prüfe, ob der Schüler mit der ID existiert
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
    	final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
    	if (schueler == null)
    		return OperationError.NOT_FOUND.getResponse();
    	// Bestimme die Lernabschnitte
    	final List<DTOSchuelerLernabschnittsdaten> abschnitte = conn.queryNamed("DTOSchuelerLernabschnittsdaten.schueler_id", id, DTOSchuelerLernabschnittsdaten.class);
    	if (abschnitte == null)
    		return OperationError.INTERNAL_SERVER_ERROR.getResponse();
    	// Bestimme die Klassen aus den Abschnitte und liese diese aus der Klassentabelle aus
    	final List<Long> klassenIDs = abschnitte.stream().filter(a -> a.Klassen_ID != null).map(a -> a.Klassen_ID).toList();
    	final List<DTOKlassen> klassen = klassenIDs.isEmpty() ? new ArrayList<>() : conn.queryNamed("DTOKlassen.id.multiple", klassenIDs, DTOKlassen.class);
    	final Map<Long, DTOKlassen> mapKlassen = klassen.stream().collect(Collectors.toMap(k -> k.ID, k -> k));
    	// Bestimme die Schuljahres-Abschnitte
    	final List<Long> schuljahresabschnittIDs = abschnitte.stream().map(a -> a.Schuljahresabschnitts_ID).toList();
    	final List<DTOSchuljahresabschnitte> schuljahresabschnitte = conn.queryNamed("DTOSchuljahresabschnitte.id.multiple", schuljahresabschnittIDs, DTOSchuljahresabschnitte.class);
    	final Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte = schuljahresabschnitte.stream().collect(Collectors.toMap(a -> a.ID, a -> a));
    	// Konvertiere die Lenabschnitte, ergänze sie um die Klassen- und Schuljahresabschnittsinformationen und füge sie zur Liste hinzu
    	final ArrayList<SchuelerLernabschnittListeEintrag> daten = new ArrayList<>();
    	for (final DTOSchuelerLernabschnittsdaten l : abschnitte) {
    		final SchuelerLernabschnittListeEintrag e = dtoMapper.apply(l);
    		final DTOKlassen klasse = mapKlassen.get(e.klassenID);
    		if (klasse != null) {
	    		e.klasse = klasse.Klasse;
	    		e.klasseStatistik = klasse.ASDKlasse;
    		}
    		final DTOSchuljahresabschnitte schuljahresabschnitt = mapSchuljahresabschnitte.get(e.schuljahresabschnitt);
    		if (schuljahresabschnitt == null)
    			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
    		e.schuljahr = schuljahresabschnitt.Jahr;
    		e.abschnitt = schuljahresabschnitt.Abschnitt;
    		daten.add(e);
    	}
    	daten.sort((final SchuelerLernabschnittListeEintrag a, final SchuelerLernabschnittListeEintrag b) -> {
    		int tmp = Integer.compare(a.schuljahr, b.schuljahr);
    		if (tmp != 0)
    			return tmp;
    		tmp = Integer.compare(a.abschnitt, b.abschnitt);
    		if (tmp != 0)
    			return tmp;
    		if (a.wechselNr == 0)
    			return 1;
    		if (b.wechselNr == 0)
    			return -1;
    		return Integer.compare(a.wechselNr, b.wechselNr);
    	});
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
