package de.svws_nrw.data.schueler;

import de.svws_nrw.core.data.schueler.SchuelerLernabschnittNachpruefung;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittNachpruefungsdaten;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.fach.BilingualeSprache;
import de.svws_nrw.core.types.klassen.Klassenart;
import de.svws_nrw.core.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.core.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.core.types.schule.SchulabschlussAllgemeinbildend;
import de.svws_nrw.core.types.schule.SchulabschlussBerufsbildend;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.core.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.json.JsonDaten;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerLernabschnittsdaten}.
 */
public final class DataSchuelerLernabschnittsdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerLernabschnittsdaten}.
	 *
	 * @param conn                   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerLernabschnittsdaten(final DBEntityManager conn) {
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
	 * Bestimmt die Lernabschnittsdaten zur Wechsel-Nr. 0 (aktiver Abschnitt im Schuljahresabschnitt)
	 * anhand der übergebenen Schüler-ID und dem angegebenen Schuljahresabschnitt
	 *
	 * @param schueler_id            die Schüler-ID
	 * @param schuljahresabschnitt   der Schuljahresabschnitt
	 *
	 * @return die Lernabschnittsdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response get(final Long schueler_id, final long schuljahresabschnitt) throws ApiOperationException {
		final SchuelerLernabschnittsdaten daten = getFromSchuelerIDUndSchuljahresabschnittID(schueler_id, schuljahresabschnitt);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Bestimmt die Lernabschnittsdaten anhand der übergebenen Lernabschnitts-ID
	 *
	 * @param id die Lernabschnitt-ID
	 *
	 * @return die Lernabschnittsdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public Response get(final Long id) throws ApiOperationException {
		final SchuelerLernabschnittsdaten daten = getFromID(id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Bestimmt die Lernabschnittsdaten anhand der übergebenen Schüler-ID und dem angegebenen Schuljahresabschnitt.
	 *
	 * @param schueler_id           	die Schüler-ID
	 * @param schuljahresabschnitt_id   der Schuljahresabschnitt
	 *
	 * @return die Lernabschnittsdaten zu den übergebenen IDs.
	 *
	 * @throws ApiOperationException im Fehlerfall
     */
	public SchuelerLernabschnittsdaten getFromSchuelerIDUndSchuljahresabschnittID(final Long schueler_id, final long schuljahresabschnitt_id) throws ApiOperationException {
		// Prüfe, ob der Schüler mit der ID existiert
		if (schueler_id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es ist keine Schueler-ID angegeben worden.");
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Schüler mit der ID " + schueler_id + " gefunden.");

		// Bestimme den aktuellen Lernabschnitt
		final String jpql = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 and e.Schuljahresabschnitts_ID = ?2 and e.WechselNr = 0";
		final List<DTOSchuelerLernabschnittsdaten> lernabschnittsdaten = conn.queryList(jpql, DTOSchuelerLernabschnittsdaten.class, schueler_id, schuljahresabschnitt_id);
		if ((lernabschnittsdaten == null) || lernabschnittsdaten.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Lernabschnitt zum Schüler mit der ID " + schueler_id + " und der Schuljahresabschnitt-ID " + schuljahresabschnitt_id + " gefunden.");
		if (lernabschnittsdaten.size() > 1)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Mehr als einen aktuellen Lernabschnitt zum Schüler mit der ID " + schueler_id + " und der Schuljahresabschnitt-ID " + schuljahresabschnitt_id + " gefunden.");

		return getFromID(lernabschnittsdaten.getFirst().ID);
	}


	/**
	 * Erstellt eine Liste von Lernabschnittsdaten anhand der übergebenen Schüler-IDs.
	 *
	 * @param schueler_ids           	die Liste mit Schüler-IDs
	 * @param mitWechseln				legt fest, ob auch die Lernabschnitte berücksichtigt werden soll,
	 *                                  die durch einen Wechsel entstanden sind, also Abschnitt mit Wechsel-Nr größer 0.
	 *
	 * @return die Lernabschnittsdaten zu den übergebenen IDs.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<SchuelerLernabschnittsdaten> getListFromSchuelerIDs(final List<Long> schueler_ids, final boolean mitWechseln) throws ApiOperationException {
		// Prüfe, ob die Liste der Schüler-IDs existiert
		if (schueler_ids == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es sind keine Schueler-ID angegeben worden.");
		if (schueler_ids.isEmpty())
			return new ArrayList<>();

		final Map<Long, DTOSchueler> mapSchueler = conn.queryByKeyList(DTOSchueler.class, schueler_ids)
			.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		for (final Long schuelerID : schueler_ids)
			if (mapSchueler.get(schuelerID) == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Ein Schüler mit der ID %d existiert nicht.".formatted(schuelerID));

		// Hole alle Lernabschnitte der übergebenen Schüler-IDs und filtere sie auf den Schuljahresabschnitt und die Wechsel-Nr.
		final List<DTOSchuelerLernabschnittsdaten> dtoLernabschnitte = conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_LIST_BY_SCHUELER_ID,
				DTOSchuelerLernabschnittsdaten.class, schueler_ids).stream()
			.filter(a -> (mitWechseln ? a.WechselNr >= 0 : a.WechselNr == 0))
			.sorted(Comparator
				.comparing((final DTOSchuelerLernabschnittsdaten a) -> a.Schueler_ID)
				.thenComparing((final DTOSchuelerLernabschnittsdaten a) -> a.Schuljahresabschnitts_ID)
				.thenComparing((final DTOSchuelerLernabschnittsdaten a) -> a.WechselNr))
			.toList();

		final List<SchuelerLernabschnittsdaten> daten = new ArrayList<>();
		for (final DTOSchuelerLernabschnittsdaten a : dtoLernabschnitte)
			daten.add(getFromID(a.ID));
		return daten;
	}

	/**
	 * Erstellt eine Liste von Lernabschnittsdaten anhand der übergebenen Schüler-IDs und dem angegebenen Schuljahresabschnitt.
	 *
	 * @param schueler_ids           	die Liste mit Schüler-IDs
	 * @param schuljahresabschnitt_id   der Schuljahresabschnitt
	 * @param mitWechseln				legt fest, ob auch die Lernabschnitte berücksichtigt werden soll,
	 *                                  die durch einen Wechsel entstanden sind, also Abschnitt mit Wechsel-Nr größer 0.
	 *
	 * @return	die Lernabschnittsdaten zu den übergebenen IDs.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<SchuelerLernabschnittsdaten> getListFromSchuelerIDsUndSchuljahresabschnittID(final List<Long> schueler_ids, final long schuljahresabschnitt_id, final boolean mitWechseln) throws ApiOperationException {

		return getListFromSchuelerIDs(schueler_ids, mitWechseln).stream()
			.filter(a -> a.schuljahresabschnitt == schuljahresabschnitt_id)
			.toList();
	}


	/**
	 * Gibt die Lernabschnittsdaten zur ID des Abschnitts zurück.
	 *
	 * @param id	die ID des Lernabschnitts.
	 *
	 * @return		die SchuelerLernabschnittsdaten zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public SchuelerLernabschnittsdaten getFromID(final Long id) throws ApiOperationException {
		// Prüfe, ob der Lernabschnitt mit der ID existiert
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es ist keine Abschnitt-ID angegeben worden.");
		final DTOSchuelerLernabschnittsdaten aktuell = conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, id);
		if (aktuell == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Lernabschnittsdaten zur Abschnitt-ID " + id + " gefunden.");
		// Ermittle die Fachbemerkungen
		final List<DTOSchuelerPSFachBemerkungen> bemerkungen = conn.queryList(DTOSchuelerPSFachBemerkungen.QUERY_BY_ABSCHNITT_ID, DTOSchuelerPSFachBemerkungen.class, aktuell.ID);
		if (bemerkungen == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Datensatz mit Bemerkungen zur Abschnitt-ID " + id + " gefunden.");
		if (bemerkungen.size() > 1)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Mehr als einen Datensatz mit Bemerkungen zur Abschnitt-ID " + id + " gefunden.");

		final SchuelerLernabschnittsdaten daten = new SchuelerLernabschnittsdaten();
		daten.id = aktuell.ID;
		daten.schuelerID = aktuell.Schueler_ID;
		daten.schuljahresabschnitt = aktuell.Schuljahresabschnitts_ID;
		daten.wechselNr = aktuell.WechselNr;
		daten.datumAnfang = aktuell.DatumVon;
		daten.datumEnde = aktuell.DatumBis;
		daten.datumKonferenz = aktuell.Konferenzdatum;
		daten.datumZeugnis = aktuell.ZeugnisDatum;
		daten.anzahlSchulbesuchsjahre = aktuell.Schulbesuchsjahre;
		daten.istGewertet = aktuell.SemesterWertung == null || aktuell.SemesterWertung;
		daten.istWiederholung = aktuell.Wiederholung != null && aktuell.Wiederholung;
		daten.pruefungsOrdnung = aktuell.PruefOrdnung;
		daten.tutorID = aktuell.Tutor_ID;
		daten.klassenID = aktuell.Klassen_ID;
		daten.folgeklassenID = aktuell.Folgeklasse_ID;
		daten.schulgliederung = aktuell.Schulgliederung.daten.kuerzel;
		daten.jahrgangID = aktuell.Jahrgang_ID;
		daten.fachklasseID = aktuell.Fachklasse_ID;
		daten.schwerpunktID = aktuell.Schwerpunkt_ID;
		daten.organisationsform = aktuell.OrgFormKrz;
		daten.Klassenart = aktuell.Klassenart;
		daten.fehlstundenGesamt = aktuell.SumFehlStd == null ? 0 : aktuell.SumFehlStd;
		daten.fehlstundenUnentschuldigt = aktuell.SumFehlStdU == null ? 0 : aktuell.SumFehlStdU;
		daten.fehlstundenGrenzwert = aktuell.FehlstundenGrenzwert;
		daten.hatSchwerbehinderungsNachweis = aktuell.Schwerbehinderung != null && aktuell.Schwerbehinderung;
		daten.hatAOSF = aktuell.AOSF != null && aktuell.AOSF;
		daten.hatAutismus = aktuell.Autist != null && aktuell.Autist;
		daten.hatZieldifferentenUnterricht = aktuell.ZieldifferentesLernen != null && aktuell.ZieldifferentesLernen;
		daten.foerderschwerpunkt1ID = aktuell.Foerderschwerpunkt_ID;
		daten.foerderschwerpunkt2ID = aktuell.Foerderschwerpunkt2_ID;
		daten.sonderpaedagogeID = aktuell.Sonderpaedagoge_ID;
		daten.bilingualerZweig = aktuell.BilingualerZweig;
		daten.istFachpraktischerAnteilAusreichend = aktuell.FachPraktAnteilAusr;
		daten.versetzungsvermerk = aktuell.VersetzungKrz;
		daten.noteDurchschnitt = aktuell.DSNote;
		daten.noteLernbereichGSbzwAL = aktuell.Gesamtnote_GS == null ? null : aktuell.Gesamtnote_GS.getNoteSekI();
		daten.noteLernbereichNW = aktuell.Gesamtnote_NW == null ? null : aktuell.Gesamtnote_NW.getNoteSekI();
		daten.abschlussart = aktuell.AbschlussArt;
		daten.istAbschlussPrognose = aktuell.AbschlIstPrognose;
		daten.abschluss = aktuell.Abschluss;
		daten.abschlussBerufsbildend = aktuell.Abschluss_B;
		daten.textErgebnisPruefungsalgorithmus = aktuell.PruefAlgoErgebnis;
		daten.zeugnisart = aktuell.Zeugnisart;
		if (aktuell.MoeglNPFaecher != null) {
			final String[] moeglicheNPFaecher = aktuell.MoeglNPFaecher.split(",");
			if ((moeglicheNPFaecher.length > 0) && (!moeglicheNPFaecher[0].trim().isBlank())) {
				daten.nachpruefungen = new SchuelerLernabschnittNachpruefungsdaten();
                Collections.addAll(daten.nachpruefungen.moegliche, moeglicheNPFaecher);
				if (aktuell.NPV_Fach_ID != null) {
					final SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
					np.grund = "V";
					np.fachID = aktuell.NPV_Fach_ID;
					np.datum = aktuell.NPV_Datum;
					np.note = aktuell.NPV_NoteKrz;
					daten.nachpruefungen.pruefungen.add(np);
				}
				if (aktuell.NPAA_Fach_ID != null) {
					final SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
					np.grund = "A";
					np.fachID = aktuell.NPAA_Fach_ID;
					np.datum = aktuell.NPAA_Datum;
					np.note = aktuell.NPAA_NoteKrz;
					daten.nachpruefungen.pruefungen.add(np);
				}
				if (aktuell.NPBQ_Fach_ID != null) {
					final SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
					np.grund = "B";
					np.fachID = aktuell.NPBQ_Fach_ID;
					np.datum = aktuell.NPBQ_Datum;
					np.note = aktuell.NPBQ_NoteKrz;
					daten.nachpruefungen.pruefungen.add(np);
				}
			}
		}
		daten.bemerkungen.zeugnisAllgemein = aktuell.ZeugnisBem;
		if (!bemerkungen.isEmpty()) {
			final DTOSchuelerPSFachBemerkungen b = bemerkungen.get(0);
			daten.bemerkungen.zeugnisASV = b.ASV;
			daten.bemerkungen.zeugnisLELS = b.LELS;
			daten.bemerkungen.zeugnisAUE = b.AUE;
			daten.bemerkungen.uebergangESF = b.ESF;
			daten.bemerkungen.foerderschwerpunkt = b.BemerkungFSP;
			daten.bemerkungen.versetzungsentscheidung = b.BemerkungVersetzung;
		}

		if (!(new DataSchuelerLeistungsdaten(conn).getByLernabschnitt(aktuell.ID, daten.leistungsdaten)))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Keine Leistungsdaten zur Abschnitt-ID " + id + " gefunden.");

		return daten;
	}

	private static final Map<String, DataBasicMapper<DTOSchuelerLernabschnittsdaten>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id != dto.ID))
				throw new ApiOperationException(Status.BAD_REQUEST);
		}),
		Map.entry("schuelerID", (conn, dto, value, map) -> {
			final long idSchueler = JSONMapper.convertToLong(value, false);
			if (conn.queryByKey(DTOSchueler.class, idSchueler) == null)
				throw new ApiOperationException(Status.CONFLICT);
			dto.Schueler_ID = idSchueler;
		}),
		Map.entry("schuljahresabschnitt", (conn, dto, value, map) -> {
			final long schuljahresabschnitt = JSONMapper.convertToLong(value, false);
			if (conn.queryByKey(DTOSchuljahresabschnitte.class, schuljahresabschnitt) == null)
				throw new ApiOperationException(Status.CONFLICT);
			dto.Schuljahresabschnitts_ID = schuljahresabschnitt;
		}),
		Map.entry("wechselNr", (conn, dto, value, map) -> dto.WechselNr = JSONMapper.convertToIntegerInRange(value, true, 0, 1000)),
		Map.entry("datumAnfang", (conn, dto, value, map) -> {
			final String strDatum = JSONMapper.convertToString(value, true, false, null);
			// TODO Datum validieren
			dto.DatumVon = strDatum;
		}),
		Map.entry("datumEnde", (conn, dto, value, map) -> {
			final String strDatum = JSONMapper.convertToString(value, true, false, null);
			// TODO Datum validieren
			dto.DatumBis = strDatum;
		}),
		Map.entry("datumKonferenz", (conn, dto, value, map) -> {
			final String strDatum = JSONMapper.convertToString(value, true, false, null);
			// TODO Datum validieren
			dto.Konferenzdatum = strDatum;
		}),
		Map.entry("datumZeugnis", (conn, dto, value, map) -> {
			final String strDatum = JSONMapper.convertToString(value, true, false, null);
			// TODO Datum validieren
			dto.ZeugnisDatum = strDatum;
		}),
		Map.entry("anzahlSchulbesuchsjahre", (conn, dto, value, map) -> dto.Schulbesuchsjahre = JSONMapper.convertToIntegerInRange(value, true, 0, 100)),
		Map.entry("istGewertet", (conn, dto, value, map) -> dto.SemesterWertung = JSONMapper.convertToBoolean(value, false)),
		Map.entry("istWiederholung", (conn, dto, value, map) -> dto.Wiederholung = JSONMapper.convertToBoolean(value, false)),
		Map.entry("pruefungsOrdnung", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			// TODO Prüfungsordnung anhand des Schild3-Katalogs validieren
			dto.PruefOrdnung = str;
		}),
		Map.entry("tutorID", (conn, dto, value, map) -> {
			final Long idLehrer = JSONMapper.convertToLong(value, true);
			if ((idLehrer != null) && (conn.queryByKey(DTOLehrer.class, idLehrer) == null))
				throw new ApiOperationException(Status.CONFLICT);
			dto.Tutor_ID = idLehrer;
		}),
		Map.entry("klassenID", (conn, dto, value, map) -> {
			final Long idKlasse = JSONMapper.convertToLong(value, true);
			if ((idKlasse != null) && (conn.queryByKey(DTOKlassen.class, idKlasse) == null))
				throw new ApiOperationException(Status.CONFLICT);
			dto.Klassen_ID = idKlasse;
		}),
		Map.entry("folgeklassenID", (conn, dto, value, map) -> {
			final Long idKlasse = JSONMapper.convertToLong(value, true);
			if ((idKlasse != null) && (conn.queryByKey(DTOKlassen.class, idKlasse) == null))
				throw new ApiOperationException(Status.CONFLICT);
			dto.Folgeklasse_ID = idKlasse;
		}),
		Map.entry("schulgliederung", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			dto.Schulgliederung = Schulgliederung.getByKuerzel(str);
		}),
		Map.entry("jahrgangID", (conn, dto, value, map) -> {
			final Long idJahrgang = JSONMapper.convertToLong(value, true);
			if ((idJahrgang != null) && (conn.queryByKey(DTOJahrgang.class, idJahrgang) == null))
				throw new ApiOperationException(Status.CONFLICT);
			dto.Jahrgang_ID = idJahrgang;
		}),
		Map.entry("fachklasseID", (conn, dto, value, map) -> {
			final Long idFachklasse = JSONMapper.convertToLong(value, true);
			if (idFachklasse != null) {
				final var manager = JsonDaten.fachklassenManager;
				if (manager.getDaten(idFachklasse) == null)
					throw new ApiOperationException(Status.CONFLICT);
			}
			dto.Fachklasse_ID = idFachklasse;
		}),
		Map.entry("schwerpunktID", (conn, dto, value, map) -> {
			final Long idSchwerpunkt = JSONMapper.convertToLong(value, true);
			// TODO Validierung des Schwerpunktes
			dto.Schwerpunkt_ID = idSchwerpunkt;
		}),
		Map.entry("organisationsform", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (str != null) {
				if ((schule.Schulform == Schulform.WB) && (WeiterbildungskollegOrganisationsformen.getByKuerzel(str) == null))
					throw new ApiOperationException(Status.CONFLICT);
				if ((AllgemeinbildendOrganisationsformen.getByKuerzel(str) == null) && (BerufskollegOrganisationsformen.getByKuerzel(str) == null))
					throw new ApiOperationException(Status.CONFLICT);
			}
			dto.OrgFormKrz = str;
		}),
		Map.entry("Klassenart", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			if ((str != null) && (Klassenart.getByKuerzel(str) == null))
				throw new ApiOperationException(Status.CONFLICT);
			dto.Klassenart = str;
		}),
		Map.entry("fehlstundenGesamt", (conn, dto, value, map) -> dto.SumFehlStd = JSONMapper.convertToIntegerInRange(value, true, 0, 100000)),
		Map.entry("fehlstundenUnentschuldigt", (conn, dto, value, map) -> dto.SumFehlStdU = JSONMapper.convertToIntegerInRange(value, true, 0, 100000)),
		Map.entry("fehlstundenGrenzwert", (conn, dto, value, map) -> dto.FehlstundenGrenzwert = JSONMapper.convertToIntegerInRange(value, true, 0, 100000)),
		Map.entry("hatSchwerbehinderungsNachweis", (conn, dto, value, map) -> dto.Schwerbehinderung = JSONMapper.convertToBoolean(value, false)),
		Map.entry("hatAOSF", (conn, dto, value, map) -> dto.AOSF = JSONMapper.convertToBoolean(value, false)),
		Map.entry("hatAutismus", (conn, dto, value, map) -> dto.Autist = JSONMapper.convertToBoolean(value, false)),
		Map.entry("hatZieldifferentenUnterricht", (conn, dto, value, map) -> dto.ZieldifferentesLernen = JSONMapper.convertToBoolean(value, false)),
		Map.entry("foerderschwerpunkt1ID", (conn, dto, value, map) -> {
			final Long idFoerderschwerpunkt = JSONMapper.convertToLong(value, true);
			if ((idFoerderschwerpunkt != null) && (conn.queryByKey(DTOFoerderschwerpunkt.class, idFoerderschwerpunkt) == null))
					throw new ApiOperationException(Status.CONFLICT);
			dto.Foerderschwerpunkt_ID = idFoerderschwerpunkt;
		}),
		Map.entry("foerderschwerpunkt2ID", (conn, dto, value, map) -> {
			final Long idFoerderschwerpunkt = JSONMapper.convertToLong(value, true);
			if ((idFoerderschwerpunkt != null) && (conn.queryByKey(DTOFoerderschwerpunkt.class, idFoerderschwerpunkt) == null))
					throw new ApiOperationException(Status.CONFLICT);
			dto.Foerderschwerpunkt2_ID = idFoerderschwerpunkt;
		}),
		Map.entry("sonderpaedagogeID", (conn, dto, value, map) -> {
			final Long idLehrer = JSONMapper.convertToLong(value, true);
			if ((idLehrer != null) && (conn.queryByKey(DTOLehrer.class, idLehrer) == null))
				throw new ApiOperationException(Status.CONFLICT);
			dto.Sonderpaedagoge_ID = idLehrer;
		}),
		Map.entry("bilingualerZweig", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			if ((str != null) && (BilingualeSprache.getByKuerzel(str) == null))
				throw new ApiOperationException(Status.CONFLICT);
			dto.BilingualerZweig = str;
		}),
		Map.entry("istFachpraktischerAnteilAusreichend", (conn, dto, value, map) -> dto.FachPraktAnteilAusr = JSONMapper.convertToBoolean(value, true)),
		Map.entry("versetzungsvermerk", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			// TODO Prüfung des Versetzungsvermerks
			//if ((str != null) && (???))
			//	throw new ApiOperationException(Status.CONFLICT);
			dto.VersetzungKrz = str;
		}),
		Map.entry("noteDurchschnitt", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			// TODO Prüfung der Durchschnittsnote
			dto.DSNote = str;
		}),
		Map.entry("noteLernbereichGSbzwAL", (conn, dto, value, map) -> dto.Gesamtnote_GS = Note.fromNoteSekI(JSONMapper.convertToIntegerInRange(value, true, 1, 6))),
		Map.entry("noteLernbereichNW", (conn, dto, value, map) -> dto.Gesamtnote_NW = Note.fromNoteSekI(JSONMapper.convertToIntegerInRange(value, true, 1, 6))),
		Map.entry("abschlussart", (conn, dto, value, map) -> {
			final Integer abschlussart = JSONMapper.convertToInteger(value, true);
			// TODO Prüfung der Abschlussart
			dto.AbschlussArt = abschlussart;
		}),
		Map.entry("istAbschlussPrognose", (conn, dto, value, map) -> dto.AbschlIstPrognose = JSONMapper.convertToBoolean(value, true)),
		Map.entry("abschluss", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			if ((str != null) && (SchulabschlussAllgemeinbildend.getByKuerzel(str) == null))
				throw new ApiOperationException(Status.CONFLICT);
			dto.Abschluss = str;
		}),
		Map.entry("abschlussBerufsbildend", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			if ((str != null) && (SchulabschlussBerufsbildend.getByKuerzel(str) == null))
				throw new ApiOperationException(Status.CONFLICT);
			dto.Abschluss_B = str;
		}),
		Map.entry("textErgebnisPruefungsalgorithmus", (conn, dto, value, map) -> dto.PruefAlgoErgebnis = JSONMapper.convertToString(value, true, false, null)),
		Map.entry("zeugnisart", (conn, dto, value, map) -> dto.Zeugnisart = JSONMapper.convertToString(value, true, false, 5))
	);

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOSchuelerLernabschnittsdaten.class, patchMappings);
	}

	/**
	 * Für einen Patch für die angegebenen Bemerkungsfelder aus.
	 *
	 * @param id   die ID des Lernabschnitts
	 * @param is   ein Input-Stream mit den JSON-Daten des Patches
	 *
	 * @return die HTTP-Response für den Patch
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response patchBemerkungen(final Long id, final InputStream is) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Ein Patch mit der ID null ist nicht möglich.");
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "In dem Patch sind keine Daten enthalten.");
		final DTOSchuelerLernabschnittsdaten dto = conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<DTOSchuelerPSFachBemerkungen> dtoListFachBem = conn.queryList(DTOSchuelerPSFachBemerkungen.QUERY_BY_ABSCHNITT_ID, DTOSchuelerPSFachBemerkungen.class, id);
		final DTOSchuelerPSFachBemerkungen dtoFachBem = (dtoListFachBem.isEmpty())
				? new DTOSchuelerPSFachBemerkungen(conn.transactionGetNextID(DTOSchuelerPSFachBemerkungen.class), id)
				: dtoListFachBem.getFirst();
		boolean patchedDTOLernabschitt = false;
		boolean patchedDTOFachBem = false;
		for (final Entry<String, Object> entry : map.entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			switch (key) {
				case "zeugnisAllgemein" -> {
					dto.ZeugnisBem = JSONMapper.convertToString(value, true, true, null);
					patchedDTOLernabschitt = true;
				}
				case "zeugnisASV" -> {
					dtoFachBem.ASV = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				case "zeugnisLELS" -> {
					dtoFachBem.LELS = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				case "zeugnisAUE" -> {
					dtoFachBem.AUE = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				case "uebergangESF" -> {
					dtoFachBem.ESF = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				case "foerderschwerpunkt" -> {
					dtoFachBem.BemerkungFSP = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				case "versetzungsentscheidung" -> {
					dtoFachBem.BemerkungVersetzung = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
			}
		}
		if (patchedDTOLernabschitt) {
			conn.transactionPersist(dto);
			conn.transactionFlush();
		}
		if (patchedDTOFachBem) {
			conn.transactionPersist(dtoFachBem);
			conn.transactionFlush();
		}
		return Response.status(Status.OK).build();
	}

	// TODO Patch für Nachprüfungen als getrennte Patch-Methode - SchuelerLernabschnittNachpruefungsdaten

}
