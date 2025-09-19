package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schueler.SchuelerListe;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.gost.GostAbiturjahrUtils;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.gost.DataGostJahrgangsliste;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsliste;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

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
	public Response getAll() throws ApiOperationException {
		Long tmpAbschnitt = this.abschnitt;
		if (tmpAbschnitt == null) {
			final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (schule == null)
				throw new ApiOperationException(Status.NOT_FOUND);
			tmpAbschnitt = schule.Schuljahresabschnitts_ID;
		}
		final List<SchuelerListeEintrag> daten = getListeSchueler(conn, tmpAbschnitt, false);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() throws ApiOperationException {
		Long tmpAbschnitt = this.abschnitt;
		if (tmpAbschnitt == null) {
			final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (schule == null)
				throw new ApiOperationException(Status.NOT_FOUND);
			tmpAbschnitt = schule.Schuljahresabschnitts_ID;
		}
		final List<SchuelerListeEintrag> daten = getListeSchueler(conn, tmpAbschnitt, true);
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
	 * @param schuljahr      das Schuljahr
	 * @param aktAbschnitt   die DB-Informationen zu dem aktuellen Schülerlernabschnitt
	 * @param mapJahrgaenge  die Jahrgangsdefinitionen, welche ihrer ID zugeordnet sind.
	 * @param schulform      die Schulform der Schule
	 *
	 * @return der Schülerlisteneintrag
	 */
	public static SchuelerListeEintrag erstelleSchuelerlistenEintrag(final DTOSchueler schueler, final int schuljahr,
			final DTOSchuelerLernabschnittsdaten aktAbschnitt, final Map<Long, DTOJahrgang> mapJahrgaenge, final Schulform schulform) {
		final SchuelerListeEintrag eintrag = new SchuelerListeEintrag();
		eintrag.id = schueler.ID;
		eintrag.nachname = Objects.toString(schueler.Nachname, "");
		eintrag.vorname = Objects.toString(schueler.Vorname, "");
		eintrag.geschlecht = (schueler.Geschlecht == null) ? "" : schueler.Geschlecht.kuerzel;
		eintrag.geburtsdatum = schueler.Geburtsdatum;
		eintrag.idSchuljahresabschnittSchueler = schueler.Schuljahresabschnitts_ID;
		if (aktAbschnitt == null) {
			eintrag.idSchuljahresabschnitt = schueler.Schuljahresabschnitts_ID;
			eintrag.idKlasse = -1L;
			eintrag.idJahrgang = -1L;
			eintrag.epJahre = null;
			eintrag.jahrgang = "";
			eintrag.schulgliederung = "";
		} else {
			eintrag.idSchuljahresabschnitt = aktAbschnitt.Schuljahresabschnitts_ID;
			eintrag.idKlasse = (aktAbschnitt.Klassen_ID == null) ? -1 : aktAbschnitt.Klassen_ID;
			final DTOJahrgang jg = mapJahrgaenge.get(aktAbschnitt.Jahrgang_ID);
			if (jg == null) {
				eintrag.idJahrgang = -1L;
				eintrag.jahrgang = "";
			} else {
				eintrag.idJahrgang = jg.ID;
				eintrag.jahrgang = Objects.toString(jg.ASDJahrgang, "");
			}
			eintrag.epJahre = aktAbschnitt.EPJahre;
			if (aktAbschnitt.Schulgliederung == null) {
				final Schulgliederung sgl = Schulgliederung.getDefault(schulform);
				final SchulgliederungKatalogEintrag sglke = (sgl == null) ? null : sgl.daten(schuljahr);
				eintrag.schulgliederung = (sglke == null) ? "" : sglke.kuerzel;
			} else {
				eintrag.schulgliederung = aktAbschnitt.Schulgliederung;
			}
		}
		eintrag.status = schueler.idStatus;
		eintrag.istDuplikat = schueler.Duplikat;
		eintrag.externeSchulNr = schueler.ExterneSchulNr;
		return eintrag;
	}


	/**
	 * Lambda-Ausdruck zum Befüllen des Core-DTOs Schueler aus DTOSchueler
	 *
	 * @param dto   das Schüler-DTO mit den zu mappenden Daten
	 * @param abschlussjahrgang   der Abschlussjahrgang des Schülers oder null, falls dieser nicht gesetzt werden soll
	 *
	 * @return das Core-DTO mit den gemappten Daten
	 */
	public static Schueler mapToSchueler(final DTOSchueler dto, final Integer abschlussjahrgang) {
		final Schueler schueler = new Schueler();
		schueler.id = dto.ID;
		schueler.nachname = dto.Nachname;
		schueler.vorname = (dto.Vorname == null) ? "" : dto.Vorname;
		schueler.geschlecht = dto.Geschlecht.id;
		schueler.abschlussjahrgang = (abschlussjahrgang == null) ? -1 : abschlussjahrgang;
		schueler.status = dto.idStatus;
		schueler.externeSchulNr = dto.ExterneSchulNr;
		return schueler;
	}


	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs {@link SchuelerListeEintrag}.
	 */
	public static final Comparator<SchuelerListeEintrag> dataComparator = (a, b) -> {
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
	 * Ermittelt die Kurse, welche von den Schülern in der Liste belegt wurden und ordnet diese zu.
	 * Die Ermittlung der Kurse sollte mit einer gültigen Schuljahresabschnitts-ID eingeschränkt werden.
	 *
	 * @param conn        die Datenbankverbindung
	 * @param schuelerListe             die Schülerliste
	 * @param schuljahresabschnittsID   die ID des Schuljahrsabschnitts
	 */
	private static void getSchuelerKurse(final DBEntityManager conn, final List<SchuelerListeEintrag> schuelerListe, final Long schuljahresabschnittsID) {
		if (!schuelerListe.isEmpty()) {
			final List<Long> schuelerIDs = schuelerListe.stream().map(s -> s.id).toList();
			final Map<Long, List<DTOKursSchueler>> kursSchueler;
			if (schuljahresabschnittsID == null) {
				final String jpql =
						"SELECT ks FROM DTOKurs k, DTOKursSchueler ks WHERE k.ID = ks.Kurs_ID AND ks.Schueler_ID IN :ids AND ks.LernabschnittWechselNr = 0";
				kursSchueler = conn.query(jpql, DTOKursSchueler.class)
						.setParameter("ids", schuelerIDs)
						.getResultList()
						.stream()
						.collect(Collectors.groupingBy(ks -> ks.Schueler_ID));
			} else {
				final String jpql =
						"SELECT ks FROM DTOKurs k, DTOKursSchueler ks WHERE k.ID = ks.Kurs_ID AND k.Schuljahresabschnitts_ID = :abschnitt AND ks.Schueler_ID IN :ids AND ks.LernabschnittWechselNr = 0";
				kursSchueler = conn.query(jpql, DTOKursSchueler.class)
						.setParameter("abschnitt", schuljahresabschnittsID)
						.setParameter("ids", schuelerIDs)
						.getResultList()
						.stream()
						.collect(Collectors.groupingBy(ks -> ks.Schueler_ID));
			}
			for (final SchuelerListeEintrag eintrag : schuelerListe) {
				final List<DTOKursSchueler> kurs_schueler = kursSchueler.get(eintrag.id);
				if ((kurs_schueler != null) && (!kurs_schueler.isEmpty()))
					for (final DTOKursSchueler ks : kurs_schueler)
						eintrag.kurse.add(ks.Kurs_ID);
			}
		}
	}



	/**
	 * Erstellt eine Liste aller nicht gelöschten Schüler mit Daten in Bezug für den angegebenen Schuljahresabschnitt.
	 *
	 * @param conn        die Datenbankverbindung
	 * @param abschnitt   die ID des Schuljahresabschnitts, dessen Daten eingelesen werden soll.
	 * @param nurAktive   gibt an, dass nur aktive Schüler des aktuellen Schuljahrabschnitts der Schule zurückgegeben werden sollen.
	 *
	 * @return die Schülerliste
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static List<SchuelerListeEintrag> getListeSchueler(final DBEntityManager conn, final long abschnitt, final boolean nurAktive)
			throws ApiOperationException {
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
			throw new ApiOperationException(Status.NOT_FOUND);
		// Bestimme die aktuellen Lernabschnitte für die Schüler, ignoriere dabei Lernabschnitte, welche vor einem Wechsel liegen, aber in dem gleichen Lernabschnitt (ein seltener Spezialfall)
		final List<Long> schuelerIDs = schueler.stream().map(s -> s.ID).toList();
		if (schuelerIDs.isEmpty())
			return new ArrayList<>();
		final List<DTOSchuelerLernabschnittsdaten> listAktAbschnitte = conn.queryList(
				"SELECT l FROM DTOSchueler s JOIN DTOSchuelerLernabschnittsdaten l ON s.ID IN ?1 AND s.ID = l.Schueler_ID"
						+ " AND s.Schuljahresabschnitts_ID = l.Schuljahresabschnitts_ID AND l.WechselNr = 0",
				DTOSchuelerLernabschnittsdaten.class,
				schuelerIDs);
		final Map<Long, DTOSchuelerLernabschnittsdaten> mapAktAbschnitte = listAktAbschnitte.stream().collect(Collectors.toMap(l -> l.Schueler_ID, l -> l));
		// Bestimme die Lernabschnitt für den ausgewählten Abschnitt
		final List<DTOSchuelerLernabschnittsdaten> listGewaehlteAbschnitte = conn.queryList(
				"SELECT l FROM DTOSchueler s JOIN DTOSchuelerLernabschnittsdaten l ON s.ID IN ?1 AND s.ID = l.Schueler_ID"
						+ " AND l.Schuljahresabschnitts_ID = ?2 AND l.WechselNr = 0",
				DTOSchuelerLernabschnittsdaten.class, schuelerIDs, abschnitt);
		final Map<Long, DTOSchuelerLernabschnittsdaten> mapGewaehlteAbschnitte =
				listGewaehlteAbschnitte.stream().collect(Collectors.toMap(l -> l.Schueler_ID, l -> l));
		// Führe die beiden Maps zusammen und bevorzuge dabei die gewählten Abschnitte
		final Map<Long, DTOSchuelerLernabschnittsdaten> mapAbschnitte = new HashMap<>();
		for (final long id : schuelerIDs) {
			final DTOSchuelerLernabschnittsdaten a = mapGewaehlteAbschnitte.get(id);
			mapAbschnitte.put(id, (a == null) ? mapAktAbschnitte.get(id) : a);
		}
		// Bestimme die Jahrgänge der Schule
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		final Schulform schulform = Schulform.data().getWertByKuerzel(schule.SchulformKuerzel);
		final Map<Long, DTOJahrgang> mapJahrgaenge = conn.queryAll(DTOJahrgang.class).stream().collect(Collectors.toMap(j -> j.ID, j -> j));
		// Erstelle die Schüler-Liste und sortiere sie
		final List<SchuelerListeEintrag> schuelerListe = new ArrayList<>();
		for (final DTOSchueler s : schueler) {
			final DTOSchuelerLernabschnittsdaten sla = mapAbschnitte.get(s.ID);
			if (sla == null)
				continue;
			final Schuljahresabschnitt sja = conn.getUser().schuleGetAbschnittById(sla.Schuljahresabschnitts_ID);
			if (sja == null)
				continue;
			final SchuelerListeEintrag sle = erstelleSchuelerlistenEintrag(s, sja.schuljahr, mapAbschnitte.get(s.ID), mapJahrgaenge, schulform);
			schuelerListe.add(sle);
		}
		schuelerListe.sort(dataComparator);
		// Ermittle die Kurse, welche von den Schülern belegt wurden.
		getSchuelerKurse(conn, schuelerListe, abschnitt);
		// Bestimme das Abiturjahr, sofern es sich um eine Schule mit gymnasialer Oberstufe handelt.
		if (schulform.daten(conn.getUser().schuleGetAbschnittById(abschnitt).schuljahr).hatGymOb) {
			for (final SchuelerListeEintrag s : schuelerListe) {
				final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(s.idSchuljahresabschnitt);
				if (schuljahresabschnitt == null)
					continue;
				s.abiturjahrgang = GostAbiturjahrUtils.getGostAbiturjahr(schulform, Schulgliederung.data().getWertByKuerzel(s.schulgliederung),
						schuljahresabschnitt.schuljahr, s.jahrgang);
			}
		}
		// Und gib die Schülerliste mit den belegten Kursen zurück...
		return schuelerListe;
	}


	/**
	 * Aggregiert alle Daten, welche für eine Auswahlliste mit dem SchuelerListeManager
	 * benötigt werden.
	 *
	 * @param conn                      die Datenbankverbindung
	 * @param idSchuljahresabschnitt    die ID des Schuljahresabschnitt für welchen die Daten aggregiert werden sollen
	 *
	 * @return die Daten für die Schüler-Auswahlliste
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static SchuelerListe getSchuelerListe(final DBEntityManager conn, final long idSchuljahresabschnitt) throws ApiOperationException {
		// Bestimme zunächst alle Schuljahresabschnitte und prüfe, ob die übergeben ID gültig ist
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt);
		if (schuljahresabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es konnte kein Schuljahresabschnitt mit der ID %d gefunden werden"
					.formatted(idSchuljahresabschnitt));

		final DataKlassendaten dataKlassendaten = new DataKlassendaten(conn);

		// Erstelle das Ergebnis-DTO
		final SchuelerListe result = new SchuelerListe();
		result.idSchuljahresabschnitt = idSchuljahresabschnitt;
		result.schueler.addAll(getListeSchueler(conn, idSchuljahresabschnitt, false));
		result.klassen.addAll(dataKlassendaten.getListBySchuljahresabschnittID(idSchuljahresabschnitt, false));
		result.kurse.addAll(DataKurse.getKursListenFuerAbschnitt(conn, idSchuljahresabschnitt, true));
		result.jahrgaenge.addAll(DataJahrgangsliste.getJahrgangsliste(conn));

		if (conn.getUser().schuleHatGymOb())
			result.jahrgaengeGost.addAll(DataGostJahrgangsliste.getGostJahrgangsliste(conn, schuljahresabschnitt));

		// ermittle ggf. weitere Klassen
		final Set<Long> idsKlassen = result.klassen.stream().map(k -> k.id).collect(Collectors.toSet());
		final List<Long> idsFehlendeKlassen = result.schueler.stream().map(s -> s.idKlasse)
				.filter(id -> (id != null) && (id >= 0) && (!idsKlassen.contains(id)))
				.distinct().toList();

		if (!idsFehlendeKlassen.isEmpty())
			result.klassen.addAll(dataKlassendaten.getListByIdsOhneSchueler(idsFehlendeKlassen, idSchuljahresabschnitt));

		return result;
	}

}
