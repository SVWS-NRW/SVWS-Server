package de.nrw.schule.svws.data.ge;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.abschluss.AbschlussManager;
import de.nrw.schule.svws.core.data.abschluss.GEAbschlussFaecher;
import de.nrw.schule.svws.core.types.Note;
import de.nrw.schule.svws.core.types.ge.GELeistungsdifferenzierteKursart;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.faecher.DTOFach;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GEAbschlussFaecher}.
 */
public class DataGEAbschlussFaecher extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GEAbschlussFaecher}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGEAbschlussFaecher(DBEntityManager conn) {
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
	public Response get(Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
    	DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
    	if (schule == null)
			return OperationError.NOT_FOUND.getResponse();
    	return getPrognoseLeistungsdaten(id, schule.Schuljahresabschnitts_ID);
	}

    /**
     * Fragt die Leistungsdaten aus dem angegebenen Abschnitt zu der ID eines Schüler für 
     * die Abschlussberechnung an der Gesamtschule ab..
     *  
     * @param id            die ID des Schüler, für den die Leistungsdaten ausgelesen werden sollen
     * @param abschnittID   der Schuljahresabschnitt, für den die Leistungsdaten ausgelesen werden sollen
     * 
     * @return die Liste der Fächer
     */
	public Response getByAbschnitt(Long id, Long abschnittID) {
		if ((id == null) || (abschnittID == null))
			return OperationError.NOT_FOUND.getResponse();
    	return getPrognoseLeistungsdaten(id, abschnittID);
	}
	
	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}

	
	
	/**
	 * Ermittelt die Leistungsdaten in Bezug auf die Prognose- bzw. Abschlussberechnung in der Sek I der 
	 * Gesamtschule für den Schüler mit der angegebenen ID aus der Datenbank für das angegebene Schuljahr und 
	 * den angegebenen Abschnitt (Quartal oder Halbjahr)  
	 * 
	 * @param id           die ID des Schülers
	 * @param idAbschnitt  die ID des Schuljahresabschnittes, von dem die Leistungsdaten abgefragt werden
	 * 
	 * @return die HTTP-Antwort mit den Leistungsdaten des angegebenen Lernabschnittes für den Schüler 
	 *         mit der angegebenen ID oder eine HTTP-Fehlermeldung
	 */
	private Response getPrognoseLeistungsdaten(long id, long idAbschnitt) {
		// Prüft, ob der Abschnitt in der Schule definiert ist. 
		DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, idAbschnitt);
		if (abschnitt == null)
			return OperationError.BAD_REQUEST.getResponse();
    	
		// Lies alle Fächer ein, damit auf die Informationen bezüglich Kürzel und Statistikkürzel bei der Auswahl der Leistungsdaten zurückgegriffen werden kann.
    	Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
    	if (faecher == null)
    		return OperationError.NOT_FOUND.getResponse();
		
		// Lese die Schülerdaten aus, um zu prüfen, ob ein Schüler mit der angegebenen ID ind er Datenbank überhaupt existiert. 
		DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
		if (schueler == null)
    		return OperationError.NOT_FOUND.getResponse();

		// Bestimme den Lernabschnitt anhand des angegebenen Schuljahres und des Abschnittes im Schuljahr
		DTOSchuelerLernabschnittsdaten lernabschnitt = conn.queryNamed("DTOSchuelerLernabschnittsdaten.schueler_id", id, DTOSchuelerLernabschnittsdaten.class)
				.stream().filter(l -> (l.Schuljahresabschnitts_ID == idAbschnitt)).findFirst().orElse(null);
		if (lernabschnitt == null)
    		return OperationError.NOT_FOUND.getResponse();
		
		// Bestimme die Leistungsdaten aus dem Lernabschnitt
		List<DTOSchuelerLeistungsdaten> leistungen = conn.queryNamed("DTOSchuelerLeistungsdaten.abschnitt_id", lernabschnitt.ID, DTOSchuelerLeistungsdaten.class);
		if ((leistungen == null) || (leistungen.size() == 0))
    		return OperationError.NOT_FOUND.getResponse();
		
		GEAbschlussFaecher daten = new GEAbschlussFaecher();
		daten.schuljahr = abschnitt.Jahr;
		daten.abschnitt = abschnitt.Abschnitt;
		daten.jahrgang = lernabschnitt.ASDJahrgang;
		daten.faecher = leistungen.stream()
				.filter(l -> (l.AufZeugnis != null) && (l.AufZeugnis))
				.map(l -> {
					DTOFach fach = faecher.get(l.Fach_ID);
					if (fach == null)
						return null;
					GELeistungsdifferenzierteKursart kursart = 
						"E".equals(l.Kursart) ? GELeistungsdifferenzierteKursart.E :
						"G".equals(l.Kursart) ? GELeistungsdifferenzierteKursart.G :
							GELeistungsdifferenzierteKursart.Sonstige;
					boolean istFremdsprache = fach.IstFremdsprache == null ? false : fach.IstFremdsprache;
					Note note = l.NotenKrz;
					if (note == null)
						return null;
					Integer noteSekI = note.getNoteSekI();
					if (noteSekI == null)
						return null;
					// Erkenne das WP-Fach anhand der Kursart bei den Leistungsdaten
					if ("WPI".equals(l.KursartAllg)) {
						return AbschlussManager.erstelleAbschlussFach("WP",  fach.Kuerzel, noteSekI, kursart, istFremdsprache);
					}
					String kuerzel = fach.StatistikFach.daten.kuerzelASD;
					if ("E5".equals(kuerzel))
						kuerzel = "E";
					if ((("M".equals(kuerzel)) || ("D".equals(kuerzel)) || ("E".equals(kuerzel))) && (kursart == GELeistungsdifferenzierteKursart.Sonstige))
						return null;
					return AbschlussManager.erstelleAbschlussFach(kuerzel,  fach.Kuerzel, noteSekI, kursart, istFremdsprache);
				})
				.filter(l -> l != null)
				.collect(Collectors.toList());
		
		// Füge die Lernabschnittsnoten hinzu - sofern sie definiert wurden
		Integer noteNW = lernabschnitt.Gesamtnote_NW == null ? null : lernabschnitt.Gesamtnote_NW.getNoteSekI(); 
		if (noteNW != null)
			daten.faecher.add(AbschlussManager.erstelleAbschlussFach("LBNW", "LBNW", noteNW, GELeistungsdifferenzierteKursart.Sonstige, false));
		Integer noteAL = lernabschnitt.Gesamtnote_GS == null ? null : lernabschnitt.Gesamtnote_GS.getNoteSekI(); 
		if (noteAL != null)
			daten.faecher.add(AbschlussManager.erstelleAbschlussFach("LBAL", "LBAL", noteAL, GELeistungsdifferenzierteKursart.Sonstige, false));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}
	
}
