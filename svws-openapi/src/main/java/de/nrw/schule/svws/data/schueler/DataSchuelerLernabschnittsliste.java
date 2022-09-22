package de.nrw.schule.svws.data.schueler;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.schueler.SchuelerLernabschnittListeEintrag;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.klassen.DTOKlassen;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.nrw.schule.svws.db.utils.OperationError;


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
    	// Konvertiere die Lenabschnitte, ergänze sie um die Klasseninformationen und füge sie zur Liste hinzu
    	Vector<SchuelerLernabschnittListeEintrag> daten = new Vector<>();
    	for (DTOSchuelerLernabschnittsdaten l : abschnitte) {
    		SchuelerLernabschnittListeEintrag e = dtoMapper.apply(l);
    		DTOKlassen klasse = mapKlassen.get(e.klassenID);
    		if (klasse == null)
    			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
    		e.klasse = klasse.Klasse;
    		e.klasseStatistik = klasse.ASDKlasse;
    		daten.add(e);
    	}
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}

}
