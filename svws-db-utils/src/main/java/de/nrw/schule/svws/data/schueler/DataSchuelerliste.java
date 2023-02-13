package de.nrw.schule.svws.data.schueler;

import java.io.InputStream;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.schueler.SchuelerListeEintrag;
import de.nrw.schule.svws.core.types.SchuelerStatus;
import de.nrw.schule.svws.core.utils.gost.GostAbiturjahrUtils;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.kurse.DTOKursSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.persistence.TypedQuery;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerListeEintrag}.
 */
public class DataSchuelerliste extends DataManager<Long> {

	private final Long abschnitt;
	
	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerListeEintrag}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abschnitt   der Lernabschnitt, für welchen die Schülerliste erstellt werden soll
	 */
	public DataSchuelerliste(DBEntityManager conn, Long abschnitt) {
		super(conn);
		this.abschnitt = abschnitt;
	}
	
	
	@Override
	public Response getAll() {
		Long abschnitt = this.abschnitt;
		if (abschnitt == null) {
			DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (schule == null)
				return OperationError.NOT_FOUND.getResponse();
			abschnitt = schule.Schuljahresabschnitts_ID;
		}
		List<SchuelerListeEintrag> daten = getListeSchueler(abschnitt, false);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		Long abschnitt = this.abschnitt;
		if (abschnitt == null) {
			DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (schule == null)
				return OperationError.NOT_FOUND.getResponse();
			abschnitt = schule.Schuljahresabschnitts_ID;
		}
		List<SchuelerListeEintrag> daten = getListeSchueler(abschnitt, true);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}

	
	/** 
	 * Diese Funktion erstellt einen {@link SchuelerListeEintrag} anhand der Schülerinformation 
	 * aus den Datenbank-DTOs {@link DTOSchueler} und {@link DTOSchuelerLernabschnittsdaten}.
	 * 
	 * @param schueler       die DB-Informationen zum Schüler
	 * @param aktAbschnitt   die DB-Informationen zu dem aktuellen Schülerlernabschnitt
	 * 
	 * @return der Schülerlisteneintrag
	 */
	public static SchuelerListeEintrag erstelleSchuelerlistenEintrag(DTOSchueler schueler, DTOSchuelerLernabschnittsdaten aktAbschnitt) {
		SchuelerListeEintrag eintrag = new SchuelerListeEintrag();
		eintrag.id = schueler.ID;
		eintrag.nachname = schueler.Nachname == null ? "" : schueler.Nachname;
		eintrag.vorname = schueler.Vorname == null ? "" : schueler.Vorname;
		eintrag.idKlasse = (aktAbschnitt == null) ? null : aktAbschnitt.Klassen_ID;
		eintrag.jahrgang = (aktAbschnitt == null) ? null : aktAbschnitt.ASDJahrgang;
		if ((aktAbschnitt != null) && (aktAbschnitt.Schulgliederung == null))
			throw new NullPointerException("Cannot read field \"daten\" because \"aktAbschnitt.Schulgliederung\" is null - Schüler-Lernabschnitts-ID: " + aktAbschnitt.ID);
		eintrag.schulgliederung = (aktAbschnitt == null) ? null : aktAbschnitt.Schulgliederung.daten.kuerzel;
		eintrag.status = schueler.Status.bezeichnung;
		eintrag.idSchuljahresabschnitt = schueler.Schuljahresabschnitts_ID;
		return eintrag;		
	}

	
	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs {@link SchuelerListeEintrag}.  
	 */
	public static Comparator<SchuelerListeEintrag> dataComparator = (a,b) -> {
		if ((a.idKlasse == null) && (b.idKlasse != null))
			return -1;
		else if ((a.idKlasse != null) && (b.idKlasse == null))
			return 1;
		else if ((a.idKlasse == null) && (b.idKlasse == null))
			return 0;
		int result = Long.compare(a.idKlasse, b.idKlasse); 
		Collator collator = Collator.getInstance(Locale.GERMAN);
		if (result == 0) {
    		if ((a.nachname == null) && (b.nachname != null))
    			return -1;
    		else if ((a.nachname != null) && (b.nachname == null))
    			return 1;
    		else if ((a.nachname == null) && (b.nachname == null))
    			return 0;
    		result = collator.compare(a.nachname, b.nachname);
		}
		if (result == 0) {
    		if ((a.vorname == null) && (b.vorname != null))
    			return -1;
    		else if ((a.vorname != null) && (b.vorname == null))
    			return 1;
    		else if ((a.vorname == null) && (b.vorname == null))
    			return 0;
    		result = collator.compare(a.vorname, b.vorname);
		}
		return result;		
	};

	
	/**
	 *  Ermittelt die Kurse, welche von den Schülern in der Liste belegt wurden und ordnet diese zu.
	 *  Die Ermittlung der Kurse sollte mit einer gültigen Schuljahresabschnitts-ID eingeschränkt werden.
	 *  
	 *  @param schuelerListe             die Schülerliste
	 *  @param schuljahresabschnittsID   die ID des Schuljahrsabschnitts
	 */
	private void getSchuelerKurse(List<SchuelerListeEintrag> schuelerListe, Long schuljahresabschnittsID) {
		if (schuelerListe.size() > 0) {
			List<Long> schuelerIDs = schuelerListe.stream().map(s -> s.id).collect(Collectors.toList());
			Map<Long, List<DTOKursSchueler>> kursSchueler;
			if (schuljahresabschnittsID == null) {
				String jpql = "SELECT ks FROM DTOKurs k, DTOKursSchueler ks WHERE k.ID = ks.Kurs_ID AND ks.Schueler_ID IN :ids";
		    	kursSchueler = conn.query(jpql, DTOKursSchueler.class)
		    		.setParameter("ids", schuelerIDs)
		    		.getResultList()
		    		.stream()
		    		.collect(Collectors.groupingBy(ks -> ks.Schueler_ID));				
			} else {
				String jpql = "SELECT ks FROM DTOKurs k, DTOKursSchueler ks WHERE k.ID = ks.Kurs_ID AND k.Schuljahresabschnitts_ID = :abschnitt AND ks.Schueler_ID IN :ids";
		    	kursSchueler = conn.query(jpql, DTOKursSchueler.class)
		    		.setParameter("abschnitt", schuljahresabschnittsID)
		    		.setParameter("ids", schuelerIDs)
		    		.getResultList()
		    		.stream()
		    		.collect(Collectors.groupingBy(ks -> ks.Schueler_ID));				
			}
	    	for (SchuelerListeEintrag eintrag : schuelerListe) {
	    		List<DTOKursSchueler> kurs_schueler = kursSchueler.get(eintrag.id);
	    		if ((kurs_schueler != null) && (kurs_schueler.size() > 0))
	    			for (DTOKursSchueler ks : kurs_schueler)
	    				eintrag.kurse.add(ks.Kurs_ID);
	    	}
		}
	}


	
	/**
	 * Erstellt eine Liste der nicht gelöschten Schüler für den angegebenen Abschnitt.
	 * 
	 * @param abschnitt   die ID des Schuljahresabschnitts, der eingelesen werden soll
	 * @param nurAktive   gibt an, dass nur aktive Schüler zurückgegeben werden sollen
	 * 
	 * @return die Schülerliste
	 */
	private List<SchuelerListeEintrag> getListeSchueler(long abschnitt, boolean nurAktive) {
		// Lese die Schüler aus der Datenbank
		List<DTOSchueler> schueler = null;
		if (nurAktive) {
			TypedQuery<DTOSchueler> querySchueler = conn.query("SELECT s FROM DTOSchueler s WHERE s.ID IS NOT NULL AND "
					+ "(s.Geloescht = null OR s.Geloescht = false) AND s.Status = :status", DTOSchueler.class);
			schueler = querySchueler.setParameter("status", SchuelerStatus.AKTIV).getResultList();
		} else {
			TypedQuery<DTOSchueler> querySchueler = conn.query("SELECT s FROM DTOSchueler s WHERE s.ID IS NOT NULL AND "
					+ "(s.Geloescht = null OR s.Geloescht = false)", DTOSchueler.class);
			schueler = querySchueler.getResultList();
		}
    	if (schueler == null)
    		throw OperationError.NOT_FOUND.exception();
    	// Bestimme die aktuellen Lernabschnitte für die Schüler, ignoriere dabei Lernabschnitte, welche vor einem Wechsel liegen, aber in dem gleichen Lernabschnitt (ein seltener Spezialfall)
    	List<Long> schuelerIDs = schueler.stream().map(s -> s.ID).collect(Collectors.toList());
		TypedQuery<DTOSchuelerLernabschnittsdaten> queryDtoSchuelerLernabschnitte = conn.query(
				"SELECT l FROM DTOSchueler s JOIN DTOSchuelerLernabschnittsdaten l ON "
				+ "s.ID IN :value AND s.ID = l.Schueler_ID AND s.Schuljahresabschnitts_ID = l.Schuljahresabschnitts_ID AND l.WechselNr IS NULL", DTOSchuelerLernabschnittsdaten.class
		);
		List<DTOSchuelerLernabschnittsdaten> listAktAbschnitte = queryDtoSchuelerLernabschnitte
				.setParameter("value", schuelerIDs).getResultList();
		Map<Long, DTOSchuelerLernabschnittsdaten> mapAktAbschnitte = listAktAbschnitte.stream().collect(Collectors.toMap(l -> l.Schueler_ID, l -> l));
		List<Long> listSchuljahresabschnitteIDs = listAktAbschnitte.stream().map(a -> a.Schuljahresabschnitts_ID).distinct().toList();
		List<DTOSchuljahresabschnitte> listSchuljahresabschnitte = conn.queryNamed("DTOSchuljahresabschnitte.id.multiple", listSchuljahresabschnitteIDs, DTOSchuljahresabschnitte.class);
		Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte = listSchuljahresabschnitte.stream().collect(Collectors.toMap(a -> a.ID, a -> a));
    	// Erstelle die Schüler-Liste und sortiere sie
    	List<SchuelerListeEintrag> schuelerListe = schueler.stream()
    		.map(s -> erstelleSchuelerlistenEintrag(s, mapAktAbschnitte.get(s.ID)))
    		.sorted(dataComparator)
	    	.collect(Collectors.toList());
    	// Ermittle die Kurse, welche von den Schülern belegt wurden.
    	getSchuelerKurse(schuelerListe, abschnitt);
    	// Bestimme das Abiturjahr, sofern es sich um eine Schule mit gymnasialer Oberstufe handelt.
    	DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
    	if (schule.Schulform.daten.hatGymOb) {
    		for (SchuelerListeEintrag s : schuelerListe) {
    			DTOSchuelerLernabschnittsdaten lernabschnitt = mapAktAbschnitte.get(s.id);
    			if (lernabschnitt == null)
    				continue;
    			DTOSchuljahresabschnitte schuljahresabschnitt = mapSchuljahresabschnitte.get(lernabschnitt.Schuljahresabschnitts_ID);
    			if (schuljahresabschnitt == null)
    				continue;
    			s.abiturjahrgang = GostAbiturjahrUtils.getGostAbiturjahr(schule.Schulform, lernabschnitt.Schulgliederung, schuljahresabschnitt.Jahr, lernabschnitt.ASDJahrgang);
    		}
    	}
    	// Und gib die Schülerliste mit den belegten Kursen zurück...
    	return schuelerListe;		
	}
	
}
