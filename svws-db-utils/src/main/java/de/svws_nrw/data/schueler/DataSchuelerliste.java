package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.utils.gost.GostAbiturjahrUtils;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;
import jakarta.persistence.TypedQuery;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerListeEintrag}.
 */
public final class DataSchuelerliste extends DataManager<Long> {

	private final Long abschnitt;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerListeEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abschnitt   der Lernabschnitt, für welchen die Schülerliste erstellt werden soll
	 */
	public DataSchuelerliste(final DBEntityManager conn, final Long abschnitt) {
		super(conn);
		this.abschnitt = abschnitt;
	}


	@Override
	public Response getAll() {
		Long abschnitt = this.abschnitt;
		if (abschnitt == null) {
			final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (schule == null)
				return OperationError.NOT_FOUND.getResponse();
			abschnitt = schule.Schuljahresabschnitts_ID;
		}
		final List<SchuelerListeEintrag> daten = getListeSchueler(abschnitt, false);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		Long abschnitt = this.abschnitt;
		if (abschnitt == null) {
			final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (schule == null)
				return OperationError.NOT_FOUND.getResponse();
			abschnitt = schule.Schuljahresabschnitts_ID;
		}
		final List<SchuelerListeEintrag> daten = getListeSchueler(abschnitt, true);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
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
	public static SchuelerListeEintrag erstelleSchuelerlistenEintrag(final DTOSchueler schueler, final DTOSchuelerLernabschnittsdaten aktAbschnitt) {
		final SchuelerListeEintrag eintrag = new SchuelerListeEintrag();
		eintrag.id = schueler.ID;
		eintrag.nachname = schueler.Nachname == null ? "" : schueler.Nachname;
		eintrag.vorname = schueler.Vorname == null ? "" : schueler.Vorname;
		eintrag.idKlasse = (aktAbschnitt == null) ? null : aktAbschnitt.Klassen_ID;
		eintrag.jahrgang = (aktAbschnitt == null) ? null : aktAbschnitt.ASDJahrgang;
		eintrag.schulgliederung = ((aktAbschnitt == null) || (aktAbschnitt.Schulgliederung == null)) ? null : aktAbschnitt.Schulgliederung.daten.kuerzel;
		eintrag.status = schueler.Status.id;
		eintrag.idSchuljahresabschnitt = schueler.Schuljahresabschnitts_ID;
		return eintrag;
	}


	/**
	 * Lambda-Ausdruck zum Befüllen des Core-DTOs Schueler aus DTOSchueler
	 */
	public static Function<DTOSchueler, Schueler> mapToSchueler = (final DTOSchueler dto) -> {
		final Schueler schueler = new Schueler();
		schueler.id = dto.ID;
		schueler.nachname = dto.Nachname;
		schueler.vorname = dto.Vorname;
		schueler.geschlecht = dto.Geschlecht.id;
		schueler.status = dto.Status.id;
		return schueler;
	};


	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs {@link SchuelerListeEintrag}.
	 */
	public static Comparator<SchuelerListeEintrag> dataComparator = (a, b) -> {
		if ((a.idKlasse == null) && (b.idKlasse != null))
			return -1;
		else if ((a.idKlasse != null) && (b.idKlasse == null))
			return 1;
		else if ((a.idKlasse == null) && (b.idKlasse == null))
			return 0;
		int result = Long.compare(a.idKlasse, b.idKlasse);
		final Collator collator = Collator.getInstance(Locale.GERMAN);
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
	private void getSchuelerKurse(final List<SchuelerListeEintrag> schuelerListe, final Long schuljahresabschnittsID) {
		if (schuelerListe.size() > 0) {
			final List<Long> schuelerIDs = schuelerListe.stream().map(s -> s.id).collect(Collectors.toList());
			Map<Long, List<DTOKursSchueler>> kursSchueler;
			if (schuljahresabschnittsID == null) {
				final String jpql = "SELECT ks FROM DTOKurs k, DTOKursSchueler ks WHERE k.ID = ks.Kurs_ID AND ks.Schueler_ID IN :ids";
		    	kursSchueler = conn.query(jpql, DTOKursSchueler.class)
		    		.setParameter("ids", schuelerIDs)
		    		.getResultList()
		    		.stream()
		    		.collect(Collectors.groupingBy(ks -> ks.Schueler_ID));
			} else {
				final String jpql = "SELECT ks FROM DTOKurs k, DTOKursSchueler ks WHERE k.ID = ks.Kurs_ID AND k.Schuljahresabschnitts_ID = :abschnitt AND ks.Schueler_ID IN :ids";
		    	kursSchueler = conn.query(jpql, DTOKursSchueler.class)
		    		.setParameter("abschnitt", schuljahresabschnittsID)
		    		.setParameter("ids", schuelerIDs)
		    		.getResultList()
		    		.stream()
		    		.collect(Collectors.groupingBy(ks -> ks.Schueler_ID));
			}
	    	for (final SchuelerListeEintrag eintrag : schuelerListe) {
	    		final List<DTOKursSchueler> kurs_schueler = kursSchueler.get(eintrag.id);
	    		if ((kurs_schueler != null) && (kurs_schueler.size() > 0))
	    			for (final DTOKursSchueler ks : kurs_schueler)
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
	private List<SchuelerListeEintrag> getListeSchueler(final long abschnitt, final boolean nurAktive) {
		// Lese die Schüler aus der Datenbank
		List<DTOSchueler> schueler = null;
		if (nurAktive) {
			final TypedQuery<DTOSchueler> querySchueler = conn.query("SELECT s FROM DTOSchueler s WHERE s.ID IS NOT NULL AND "
					+ "(s.Geloescht = null OR s.Geloescht = false) AND s.Status = :status", DTOSchueler.class);
			schueler = querySchueler.setParameter("status", SchuelerStatus.AKTIV).getResultList();
		} else {
			final TypedQuery<DTOSchueler> querySchueler = conn.query("SELECT s FROM DTOSchueler s WHERE s.ID IS NOT NULL AND "
					+ "(s.Geloescht = null OR s.Geloescht = false)", DTOSchueler.class);
			schueler = querySchueler.getResultList();
		}
    	if (schueler == null)
    		throw OperationError.NOT_FOUND.exception();
    	// Bestimme die aktuellen Lernabschnitte für die Schüler, ignoriere dabei Lernabschnitte, welche vor einem Wechsel liegen, aber in dem gleichen Lernabschnitt (ein seltener Spezialfall)
    	final List<Long> schuelerIDs = schueler.stream().map(s -> s.ID).collect(Collectors.toList());
		final List<DTOSchuelerLernabschnittsdaten> listAktAbschnitte =
			conn.queryList("SELECT l FROM DTOSchuelerLernabschnittsdaten l WHERE l.Schueler_ID IN ?1 AND l.Schuljahresabschnitts_ID = ?2 AND l.WechselNr IS NULL",
				DTOSchuelerLernabschnittsdaten.class, schuelerIDs, abschnitt);
		final Map<Long, DTOSchuelerLernabschnittsdaten> mapAktAbschnitte = listAktAbschnitte.stream().collect(Collectors.toMap(l -> l.Schueler_ID, l -> l));
		final List<Long> listSchuljahresabschnitteIDs = listAktAbschnitte.stream().map(a -> a.Schuljahresabschnitts_ID).distinct().toList();
		final List<DTOSchuljahresabschnitte> listSchuljahresabschnitte = conn.queryNamed("DTOSchuljahresabschnitte.id.multiple", listSchuljahresabschnitteIDs, DTOSchuljahresabschnitte.class);
		final Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte = listSchuljahresabschnitte.stream().collect(Collectors.toMap(a -> a.ID, a -> a));
    	// Erstelle die Schüler-Liste und sortiere sie
    	final List<SchuelerListeEintrag> schuelerListe = schueler.stream()
    		.map(s -> erstelleSchuelerlistenEintrag(s, mapAktAbschnitte.get(s.ID)))
    		.sorted(dataComparator)
	    	.collect(Collectors.toList());
    	// Ermittle die Kurse, welche von den Schülern belegt wurden.
    	getSchuelerKurse(schuelerListe, abschnitt);
    	// Bestimme das Abiturjahr, sofern es sich um eine Schule mit gymnasialer Oberstufe handelt.
    	final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
    	if (schule.Schulform.daten.hatGymOb) {
    		for (final SchuelerListeEintrag s : schuelerListe) {
    			final DTOSchuelerLernabschnittsdaten lernabschnitt = mapAktAbschnitte.get(s.id);
    			if (lernabschnitt == null)
    				continue;
    			final DTOSchuljahresabschnitte schuljahresabschnitt = mapSchuljahresabschnitte.get(lernabschnitt.Schuljahresabschnitts_ID);
    			if (schuljahresabschnitt == null)
    				continue;
    			s.abiturjahrgang = GostAbiturjahrUtils.getGostAbiturjahr(schule.Schulform, lernabschnitt.Schulgliederung, schuljahresabschnitt.Jahr, lernabschnitt.ASDJahrgang);
    		}
    	}
    	// Und gib die Schülerliste mit den belegten Kursen zurück...
    	return schuelerListe;
	}

}
