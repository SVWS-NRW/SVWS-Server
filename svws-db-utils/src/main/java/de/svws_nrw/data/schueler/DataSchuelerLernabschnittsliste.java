package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittListeEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerLernabschnittListeEintrag}.
 */
public class DataSchuelerLernabschnittsliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerLernabschnittListeEintrag}.
	 * 
	 * @param conn    die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerLernabschnittsliste(DBEntityManager conn) {
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
	private Function<DTOSchuelerLernabschnittsdaten, SchuelerLernabschnittListeEintrag> dtoMapper = (DTOSchuelerLernabschnittsdaten dto) -> {
		SchuelerLernabschnittListeEintrag daten = new SchuelerLernabschnittListeEintrag();
		daten.id = dto.ID;
    	daten.schuelerID = dto.Schueler_ID;
    	daten.schuljahresabschnitt = dto.Schuljahresabschnitts_ID;
    	daten.wechselNr = dto.WechselNr;
    	daten.istGewertet = dto.SemesterWertung == null ? true : dto.SemesterWertung;
    	daten.istWiederholung = dto.Wiederholung == null ? false : dto.Wiederholung;
    	daten.pruefungsOrdnung = dto.PruefOrdnung;
    	daten.klassenID = dto.Klassen_ID;
    	daten.jahrgangID = dto.Jahrgang_ID;
    	daten.jahrgang = dto.ASDJahrgang;
		return daten;
	};
	
	
	@Override
	public Response get(Long id) {
		// Prüfe, ob der Schüler mit der ID existiert
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
    	DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
    	if (schueler == null)
			return OperationError.NOT_FOUND.getResponse();
    	// Bestimme die Lernabschnitte
    	List<DTOSchuelerLernabschnittsdaten> abschnitte = conn.queryNamed("DTOSchuelerLernabschnittsdaten.schueler_id", id, DTOSchuelerLernabschnittsdaten.class);
    	if (abschnitte == null)
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
    	// Bestimme die Klassen aus den Abschnitte und liese diese aus der Klassentabelle aus
    	List<Long> klassenIDs = abschnitte.stream().map(a -> a.Klassen_ID).collect(Collectors.toList());
    	List<DTOKlassen> klassen = conn.queryNamed("DTOKlassen.id.multiple", klassenIDs, DTOKlassen.class);
    	Map<Long, DTOKlassen> mapKlassen = klassen.stream().collect(Collectors.toMap(k -> k.ID, k -> k));
    	// Bestimme die Schuljahres-Abschnitte
    	List<Long> schuljahresabschnittIDs = abschnitte.stream().map(a -> a.Schuljahresabschnitts_ID).collect(Collectors.toList());
    	List<DTOSchuljahresabschnitte> schuljahresabschnitte = conn.queryNamed("DTOSchuljahresabschnitte.id.multiple", schuljahresabschnittIDs, DTOSchuljahresabschnitte.class);
    	Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte = schuljahresabschnitte.stream().collect(Collectors.toMap(a -> a.ID, a -> a));
    	// Konvertiere die Lenabschnitte, ergänze sie um die Klassen- und Schuljahresabschnittsinformationen und füge sie zur Liste hinzu
    	Vector<SchuelerLernabschnittListeEintrag> daten = new Vector<>();
    	for (DTOSchuelerLernabschnittsdaten l : abschnitte) {
    		SchuelerLernabschnittListeEintrag e = dtoMapper.apply(l);
    		DTOKlassen klasse = mapKlassen.get(e.klassenID);
    		if (klasse == null)
    			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
    		e.klasse = klasse.Klasse;
    		e.klasseStatistik = klasse.ASDKlasse;
    		DTOSchuljahresabschnitte schuljahresabschnitt = mapSchuljahresabschnitte.get(e.schuljahresabschnitt);
    		if (schuljahresabschnitt == null)
    			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
    		e.schuljahr = schuljahresabschnitt.Jahr;
    		e.abschnitt = schuljahresabschnitt.Abschnitt;
    		daten.add(e);
    	}
    	daten.sort((SchuelerLernabschnittListeEintrag a, SchuelerLernabschnittListeEintrag b) -> {
    		int tmp = Integer.compare(a.schuljahr, b.schuljahr);
    		if (tmp != 0)
    			return tmp;
    		tmp = Integer.compare(a.abschnitt, b.abschnitt);
    		if (tmp != 0)
    			return tmp;
    		if (a.wechselNr == null)
    			return 1;
    		if (b.wechselNr == null)
    			return -1;
    		return Integer.compare(a.wechselNr, b.wechselNr);
    	});
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}

}
