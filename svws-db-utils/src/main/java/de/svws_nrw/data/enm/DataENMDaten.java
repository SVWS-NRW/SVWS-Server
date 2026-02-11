package de.svws_nrw.data.enm;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.kurse.ZulaessigeKursartKatalogEintrag;
import de.svws_nrw.asd.data.schule.FloskelgruppenartKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Floskelgruppenart;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.base.crypto.Passwords;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.benutzer.BenutzerConfigElement;
import de.svws_nrw.core.data.enm.ENMAnkreuzkompetenz;
import de.svws_nrw.core.data.enm.ENMConfigKlasse;
import de.svws_nrw.core.data.enm.ENMConfigKlasseSpalte;
import de.svws_nrw.core.data.enm.ENMDaten;
import de.svws_nrw.core.data.enm.ENMFach;
import de.svws_nrw.core.data.enm.ENMFloskel;
import de.svws_nrw.core.data.enm.ENMFloskelgruppe;
import de.svws_nrw.core.data.enm.ENMJahrgang;
import de.svws_nrw.core.data.enm.ENMKlasse;
import de.svws_nrw.core.data.enm.ENMLehrer;
import de.svws_nrw.core.data.enm.ENMLehrerInitialKennwort;
import de.svws_nrw.core.data.enm.ENMLeistung;
import de.svws_nrw.core.data.enm.ENMLerngruppe;
import de.svws_nrw.core.data.enm.ENMSchueler;
import de.svws_nrw.core.data.enm.ENMSchuelerAnkreuzkompetenz;
import de.svws_nrw.core.data.enm.ENMServerConfig;
import de.svws_nrw.core.data.enm.ENMServerConfigElement;
import de.svws_nrw.core.data.enm.ENMTeilleistung;
import de.svws_nrw.core.data.enm.ENMTeilleistungsart;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.utils.enm.ENMDatenManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.katalog.DTOAnkreuzkompetenzJahrgang;
import de.svws_nrw.db.dto.current.katalog.DTOFloskelnJahrgaenge;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulCredentials;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulKonfigurationClient;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulKonfigurationServer;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzdaten;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.katalog.DTOFloskelgruppen;
import de.svws_nrw.db.dto.current.katalog.DTOFloskeln;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerTeilleistung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOTeilleistungsarten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsNotenmodulCredentials;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerAnkreuzkompetenzen;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerTeilleistungen;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.ext.jbcrypt.BCrypt;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link ENMDaten}.
 */
public final class DataENMDaten extends DataManager<Long> {

	private static final DateTimeFormatter ofPattern =
			new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true).toFormatter();

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link ENMDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataENMDaten(final DBEntityManager conn) {
		super(conn);
	}


	@Override
	public Response getAll() throws ApiOperationException {
		final ENMDaten daten = getDaten(conn, null);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Gibt die ENM-Daten als mit GZIP komprimiertes JSON-Objekt zurück.
	 *
	 * @return das GZIP-komprimierte JSON-Objekt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response getAllGZip() throws ApiOperationException {
		final ENMDaten daten = getDaten(conn, null);
		return JSONMapper.gzipFileResponseFromObject(daten, "enm.json.gz");
	}


	/**
	 * Gibt die ENM-Daten aller Lehrer als mit GZIP komprimiertes byte[] zurück.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return das GZIP-komprimierte byte[].
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static byte[] getAllGZIPBytes(final DBEntityManager conn) throws ApiOperationException {
		final ENMDaten daten = getDaten(conn, null);
		try {
			return JSONMapper.gzipByteArrayFromObject(daten);
		} catch (final CompressionException ce) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, ce);
		}
	}


	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}


	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final ENMDaten daten = getDaten(conn, id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}



	/**
	 * Ermittelt die ENM-Daten zu dem Lehrer mit der angegebenen ID.
	 * Ist die ID null so werden die ENM-Daten für alle Lehrer des aktuellen
	 * Schuljahresabschnitts generiert.
	 *
	 * @param conn   die Datenbank-Verbindung
	 * @param id     die ID des Lehrers oder null
	 *
	 * @return die ENMDaten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static ENMDaten getDaten(final DBEntityManager conn, final Long id) throws ApiOperationException {
		// Lese die Daten aus der Datenbank ein
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnitt();
		final Map<Long, DTOLehrer> mapLehrer = getLehrerListe(conn);
		final Map<Long, String> mapLehrerPWHash = getLehrerCredsListe(conn);
		final Map<Long, String> mapLehrerPWHashTimestamps = getLehrerCredsTimstampsListe(conn);
		final DTOLehrer dtoLehrer = (id == null) ? null : mapLehrer.get(id);   // Ermittle den Lehrer nur, falls ENM-Daten für einen speziellen Lehrer bestimt werden.
		if ((id != null) && (dtoLehrer == null))
			throw new ApiOperationException(Status.NOT_FOUND);
		final Map<Long, DTOAnkreuzfloskeln> mapKatalogAnkreuzkompetenzen = getAnkreuzkompetenzenListe(conn);
		final Map<Long, List<DTOAnkreuzkompetenzJahrgang>> kompetenzZuordnungenByJahrgang = getKompetenzZuordnungenByJahrgang(conn);
		final Map<Long, DTOFoerderschwerpunkt> mapFoerderschwerpunkte = getFoerderschwerpunktListe(conn);
		final Map<Long, DTOSchueler> mapSchueler = getSchuelerListe(conn, abschnitt);
		final Map<Long, DTOFach> mapFaecher = getFaecherListe(conn);
		final Map<Long, DTOJahrgang> mapJahrgaenge = getJahrgangsListe(conn);
		final Map<Long, DTOKlassen> mapKlassen = getKlassenListe(conn, abschnitt);
		final Map<Long, List<DTOKlassenLeitung>> mapKlassenLeitung = getKlassenleitungen(conn, mapKlassen);
		final Map<Long, DTOKurs> mapKurse = getKurse(conn, abschnitt);
		final Map<Long, DTOTeilleistungsarten> mapTeilleistungsarten =
				conn.queryAll(DTOTeilleistungsarten.class).stream().collect(Collectors.toMap(a -> a.ID, a -> a));

		// Erstelle einen ENM-Daten-Manager und füge ggf. den Lehrer hinzu für welchen die ENM-Daten erzeugt werden
		final ENMDatenManager manager = new ENMDatenManager(id);
		if (dtoLehrer != null) {
			final String creds = mapLehrerPWHash.get(manager.daten.lehrerID);
			final String tsCreds = mapLehrerPWHashTimestamps.get(manager.daten.lehrerID);
			manager.addLehrer(manager.daten.lehrerID, dtoLehrer.Kuerzel, dtoLehrer.Nachname, dtoLehrer.Vorname, dtoLehrer.Geschlecht,
					dtoLehrer.eMailDienstlich, (creds == null) ? "" : creds, (tsCreds == null) ? null : tsCreds);
		}
		initManager(manager, conn.getUser().schuleGetStammdaten(), conn.querySingle(DTOAnkreuzdaten.class), abschnitt);

		// Aggregiert aus den Schülerabschnittsdaten und -leistungsdaten die einzelnen Informationen für das ENM.
		// Dabei wird u.a. auch ermittelt, welche Kataloginformationen (Klassen, Lehrer, Jahrgänge) bei den Daten für das ENM einzutragen sind.

		// Bestimme zunächst alle aktuellen Lernabschnitte des Schuljahresabschnittes
		List<DTOSchuelerLernabschnittsdaten> lernabschnitte =
				conn.queryList("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.WechselNr = 0",
						DTOSchuelerLernabschnittsdaten.class, abschnitt.id).stream().filter(la -> mapSchueler.get(la.Schueler_ID) != null).toList();
		final List<Long> idsLernabschnitte = lernabschnitte.stream().map(la -> la.ID).toList();
		final Map<Long, DTOSchuelerPSFachBemerkungen> mapBemerkungen = idsLernabschnitte.isEmpty() ? new HashMap<>()
				: conn.queryList(DTOSchuelerPSFachBemerkungen.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerPSFachBemerkungen.class, idsLernabschnitte)
						.stream().collect(Collectors.toMap(b -> b.Abschnitt_ID, b -> b));
		final Map<Long, DTOTimestampsSchuelerLernabschnittsdaten> mapTimestampsLernabschnitte = idsLernabschnitte.isEmpty() ? new HashMap<>()
				: conn.queryByKeyList(DTOTimestampsSchuelerLernabschnittsdaten.class, idsLernabschnitte).stream().collect(Collectors.toMap(t -> t.ID, t -> t));
		// Bestimme die relevanten Leistungsdaten zu den Lernabschnitten
		final List<DTOSchuelerLeistungsdaten> leistungsdaten = lernabschnitte.isEmpty() ? new ArrayList<>()
				: ((dtoLehrer == null)
						? conn.queryList(DTOSchuelerLeistungsdaten.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerLeistungsdaten.class, idsLernabschnitte)
						: conn.queryList("SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID IN ?1 AND e.Fachlehrer_ID = ?2",
								DTOSchuelerLeistungsdaten.class, idsLernabschnitte, dtoLehrer.ID));
		final List<Long> idsLeistungsdaten = leistungsdaten.stream().map(ld -> ld.ID).toList();
		final Map<Long, List<DTOSchuelerLeistungsdaten>> mapLeistungen = leistungsdaten.stream().collect(Collectors.groupingBy(l -> l.Abschnitt_ID));
		final Map<Long, DTOTimestampsSchuelerLeistungsdaten> mapTimestampsLeistungsdaten = idsLeistungsdaten.isEmpty() ? new HashMap<>()
				: conn.queryByKeyList(DTOTimestampsSchuelerLeistungsdaten.class, idsLeistungsdaten).stream().collect(Collectors.toMap(t -> t.ID, t -> t));
		// Filtere ggf. die Lernabschnitte anhand von nicht vorhandenen Leistungsdaten, falls der Lehrer nicht als Klassenlehrer eingetragen ist
		if (dtoLehrer != null)
			lernabschnitte = lernabschnitte.stream().filter(la -> {
				// Wenn die ENM-Datei für einen einzelnen Lehrer erstellt wird, so muss ein Lernabschnitt beachtet werden, wo er Klassenlehrer ist ...
				if (la.Klassen_ID != null) {
					final DTOKlassen klasse = mapKlassen.get(la.Klassen_ID);
					if (klasse != null) {
						final List<DTOKlassenLeitung> klassenleitungen = mapKlassenLeitung.get(la.Klassen_ID);
						if (klassenleitungen != null)
							for (final DTOKlassenLeitung klassenleitung : klassenleitungen)
								if (klassenleitung.Lehrer_ID == dtoLehrer.ID)
									return true;
					}
				}
				// ... und ein Lernabschnitt, wo der Lehrer Fachlehrer ist
				return (mapLeistungen.get(la.ID) != null);
			}).toList();
		// Bestimme zusätzlich eine Übersicht zu den Einzelleistungen zu den Leistungsdaten
		final List<DTOSchuelerTeilleistung> listTeilleistungen = idsLeistungsdaten.isEmpty() ? new ArrayList<>()
				: conn.queryList(DTOSchuelerTeilleistung.QUERY_LIST_BY_LEISTUNG_ID, DTOSchuelerTeilleistung.class, idsLeistungsdaten);
		final Map<Long, List<DTOSchuelerTeilleistung>> mapTeilleistungen = listTeilleistungen.stream().collect(Collectors.groupingBy(st -> st.Leistung_ID));
		final List<Long> idsTeilleistungen = listTeilleistungen.stream().map(t -> t.ID).toList();
		final Map<Long, DTOTimestampsSchuelerTeilleistungen> mapTeilleistungenTimestamps = idsTeilleistungen.isEmpty() ? new HashMap<>()
				: conn.queryByKeyList(DTOTimestampsSchuelerTeilleistungen.class, idsTeilleistungen).stream().collect(Collectors.toMap(t -> t.ID, t -> t));
		// Bestimme zusätzlich eine Übersicht zu den Ankreuzkompetenzen
		final List<DTOSchuelerAnkreuzfloskeln> listAnkreuzkompetenzen = idsLernabschnitte.isEmpty() ? new ArrayList<>()
				: conn.queryList(DTOSchuelerAnkreuzfloskeln.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerAnkreuzfloskeln.class, idsLernabschnitte);
		final Map<Long, List<DTOSchuelerAnkreuzfloskeln>> mapAnkreuzkompetenzen =
				listAnkreuzkompetenzen.stream().collect(Collectors.groupingBy(a -> a.Abschnitt_ID));
		final List<Long> idsAnkreuzkompetenzen = listAnkreuzkompetenzen.stream().map(t -> t.ID).toList();
		final Map<Long, DTOTimestampsSchuelerAnkreuzkompetenzen> mapAnkreuzkompetenzenTimestamps = idsAnkreuzkompetenzen.isEmpty() ? new HashMap<>()
				: conn.queryByKeyList(DTOTimestampsSchuelerAnkreuzkompetenzen.class, idsAnkreuzkompetenzen).stream()
						.collect(Collectors.toMap(t -> t.ID, t -> t));
		// Durchwandere die Lernabschnitt der Schüler...
		for (final DTOSchuelerLernabschnittsdaten lernabschnitt : lernabschnitte) {
			if ((lernabschnitt.Klassen_ID == null) || (lernabschnitt.Jahrgang_ID == null))
				continue;
			final DTOKlassen dtoKlasse = mapKlassen.get(lernabschnitt.Klassen_ID);
			if (dtoKlasse == null)
				throw new NullPointerException();
			ENMKlasse enmKlasse = manager.getKlasse(dtoKlasse.ID);
			if (enmKlasse == null) {
				manager.addKlasse(dtoKlasse.ID, dtoKlasse.ASDKlasse, dtoKlasse.Klasse, dtoKlasse.Jahrgang_ID, dtoKlasse.Sortierung);
				enmKlasse = manager.getKlasse(dtoKlasse.ID);
				final List<DTOKlassenLeitung> klassenleitungen = mapKlassenLeitung.get(dtoKlasse.ID);
				if (klassenleitungen != null) {
					for (final DTOKlassenLeitung kl : klassenleitungen) {
						if (manager.getLehrer(kl.Lehrer_ID) == null) {
							final DTOLehrer dtoKlassenlehrer = mapLehrer.get(kl.Lehrer_ID);
							if (dtoKlassenlehrer != null) {
								final String creds = (dtoLehrer == null) ? mapLehrerPWHash.get(dtoKlassenlehrer.ID) : "";
								final String tsCreds = (dtoLehrer == null) ? mapLehrerPWHashTimestamps.get(dtoKlassenlehrer.ID) : null;
								manager.addLehrer(dtoKlassenlehrer.ID, dtoKlassenlehrer.Kuerzel, dtoKlassenlehrer.Nachname, dtoKlassenlehrer.Vorname,
										dtoKlassenlehrer.Geschlecht, dtoKlassenlehrer.eMailDienstlich,
										(creds == null) ? "" : creds, (tsCreds == null) ? null : tsCreds);
							}
						}
						enmKlasse.klassenlehrer.add(kl.Lehrer_ID);
					}
				}
			}

			ENMSchueler enmSchueler = manager.getSchueler(lernabschnitt.Schueler_ID);
			if (enmSchueler == null) {
				final var dtoSchueler = mapSchueler.get(lernabschnitt.Schueler_ID);
				ENMJahrgang enmJahrgang = (lernabschnitt.Jahrgang_ID == null) ? null : manager.getJahrgang(lernabschnitt.Jahrgang_ID);
				if (enmJahrgang == null) {
					final DTOJahrgang dtoJahrgang = mapJahrgaenge.get(lernabschnitt.Jahrgang_ID);
					if (dtoJahrgang == null)
						throw new NullPointerException("Kein Jahrgang zu der Jahrgangs-ID gefunden.");
					manager.addJahrgang(dtoJahrgang.ID, dtoJahrgang.ASDJahrgang, dtoJahrgang.InternKrz,
							dtoJahrgang.ASDBezeichnung, dtoJahrgang.Sekundarstufe, dtoJahrgang.Sortierung);
					enmJahrgang = manager.getJahrgang(lernabschnitt.Jahrgang_ID);
				}
				manager.addSchueler(dtoSchueler.ID, lernabschnitt.Jahrgang_ID, dtoKlasse.ID, dtoSchueler.Nachname, dtoSchueler.Vorname,
						dtoSchueler.Geschlecht, lernabschnitt.BilingualerZweig, lernabschnitt.ZieldifferentesLernen,
						dtoSchueler.Lernstandsbericht);  // Deutsch als Fremdsprache liegt vor, wenn für den Schüler Lernstandsberichte geschrieben werden...

				final DTOSchuelerPSFachBemerkungen bemerkungen = mapBemerkungen.get(lernabschnitt.ID);
				final DTOTimestampsSchuelerLernabschnittsdaten tsLernabschnitt = mapTimestampsLernabschnitte.get(lernabschnitt.ID);
				enmSchueler = manager.getSchueler(lernabschnitt.Schueler_ID);
				enmSchueler.lernabschnitt.id = lernabschnitt.ID;
				enmSchueler.lernabschnitt.fehlstundenGesamt = lernabschnitt.SumFehlStd;
				enmSchueler.lernabschnitt.tsFehlstundenGesamt = tsLernabschnitt.tsSumFehlStd;
				enmSchueler.lernabschnitt.fehlstundenGesamtUnentschuldigt = lernabschnitt.SumFehlStdU;
				enmSchueler.lernabschnitt.tsFehlstundenGesamtUnentschuldigt = tsLernabschnitt.tsSumFehlStdU;
				enmSchueler.lernabschnitt.pruefungsordnung = lernabschnitt.PruefOrdnung;
				final Note noteLB1 = Note.fromNoteSekI(lernabschnitt.Gesamtnote_GS);
				final Note noteLB2 = Note.fromNoteSekI(lernabschnitt.Gesamtnote_NW);
				enmSchueler.lernabschnitt.lernbereich1note = (noteLB1 == null) ? null : noteLB1.daten(abschnitt.schuljahr).kuerzel;
				enmSchueler.lernabschnitt.lernbereich2note = (noteLB2 == null) ? null : noteLB2.daten(abschnitt.schuljahr).kuerzel;
				final DTOFoerderschwerpunkt fs1 =
						(lernabschnitt.Foerderschwerpunkt_ID == null) ? null : mapFoerderschwerpunkte.get(lernabschnitt.Foerderschwerpunkt_ID);
				final DTOFoerderschwerpunkt fs2 =
						(lernabschnitt.Foerderschwerpunkt2_ID == null) ? null : mapFoerderschwerpunkte.get(lernabschnitt.Foerderschwerpunkt2_ID);
				enmSchueler.lernabschnitt.foerderschwerpunkt1 = (fs1 == null) ? null : fs1.StatistikKrz;
				enmSchueler.lernabschnitt.foerderschwerpunkt2 = (fs2 == null) ? null : fs2.StatistikKrz;
				enmSchueler.bemerkungen.ASV = (bemerkungen == null) ? null : bemerkungen.ASV;
				enmSchueler.bemerkungen.tsASV = tsLernabschnitt.tsASV;
				enmSchueler.bemerkungen.AUE = (bemerkungen == null) ? null : bemerkungen.AUE;
				enmSchueler.bemerkungen.tsAUE = tsLernabschnitt.tsAUE;
				enmSchueler.bemerkungen.ZB = lernabschnitt.ZeugnisBem;
				enmSchueler.bemerkungen.tsZB = tsLernabschnitt.tsZeugnisBem;
				enmSchueler.bemerkungen.LELS = (bemerkungen == null) ? null : bemerkungen.LELS;
				enmSchueler.bemerkungen.tsLELS = tsLernabschnitt.tsLELS;
				enmSchueler.bemerkungen.schulformEmpf = (bemerkungen == null) ? null : bemerkungen.ESF;
				enmSchueler.bemerkungen.tsSchulformEmpf = tsLernabschnitt.tsESF;
				enmSchueler.bemerkungen.individuelleVersetzungsbemerkungen = (bemerkungen == null) ? null : bemerkungen.BemerkungVersetzung;
				enmSchueler.bemerkungen.tsIndividuelleVersetzungsbemerkungen = tsLernabschnitt.tsBemerkungVersetzung;
				enmSchueler.bemerkungen.foerderbemerkungen = (bemerkungen == null) ? null : bemerkungen.BemerkungFSP;
				enmSchueler.bemerkungen.tsFoerderbemerkungen = tsLernabschnitt.tsBemerkungFSP;
			}

			final Set<Long> leistungenFachIDs = new HashSet<>(); // Für die Ankreuzkompetenzen, sofern die auf einen unterrichtenden Lehrer eingeschränkt werden (Lehrer-spezifische ENMDaten)
			final List<DTOSchuelerLeistungsdaten> leistungen = mapLeistungen.get(lernabschnitt.ID);
			if (leistungen != null) {
				for (final DTOSchuelerLeistungsdaten leistung : leistungen) {
					if (leistung.Fachlehrer_ID == null)
						continue;

					// Hier wird die temporäre LerngruppenID erstellt, mit der in der Klasse ENMDaten gearbeitet wird.
					// Es wird eine eindeutige ID benötigt, die Kurs- und Klassenübergreifend diese Lerngruppe identifiziert.
					final String strLerngruppenID = (leistung.Kurs_ID == null)
							? ("Klasse:" + lernabschnitt.Klassen_ID + "/" + leistung.Fach_ID)
							: ("Kurs:" + leistung.Kurs_ID);
					ENMLerngruppe lerngruppe = manager.getLerngruppe(strLerngruppenID);
					final ZulaessigeKursart kursart = (leistung.Kurs_ID == null) ? null : ZulaessigeKursart.data().getWertByKuerzel(leistung.Kursart);
					if (lerngruppe == null) {
						final DTOFach fach = mapFaecher.get(leistung.Fach_ID);
						final String kursartAllg;
						if (kursart == null)
							kursartAllg = null;
						else {
							final ZulaessigeKursartKatalogEintrag kursartEintrag = kursart.daten(abschnitt.schuljahr);
							if ((kursartEintrag.kuerzelAllg == null) || "".equals(kursartEintrag.kuerzelAllg))
								kursartAllg = kursartEintrag.kuerzel;
							else
								kursartAllg = kursartEintrag.kuerzelAllg;
						}
						// Fach zu ENMDaten hinzufügen ?
						if (manager.getFach(fach.ID) == null)
							manager.addFach(fach.ID, fach.StatistikKuerzel, fach.Kuerzel, fach.SortierungAllg, fach.IstFremdsprache);
						// Unterscheidung zwischen den beiden Lerngruppen-Typen...
						if (leistung.Kurs_ID == null) {  // es ist eine Klasse
							manager.addLerngruppe(strLerngruppenID, dtoKlasse.ID, leistung.Fach_ID, null,
									fach.Kuerzel, kursartAllg, fach.Unterrichtssprache,
									(leistung.Wochenstunden == null) ? 0 : leistung.Wochenstunden);
						} else {  // es ist ein Kurs
							final DTOKurs kurs = mapKurse.get(leistung.Kurs_ID);
							manager.addLerngruppe(strLerngruppenID, leistung.Kurs_ID, leistung.Fach_ID,
									(kursart == null) ? -1 : Integer.parseInt(kursart.daten(abschnitt.schuljahr).nummer), kurs.KurzBez, kursartAllg,
									fach.Unterrichtssprache, kurs.WochenStd);
						}
						lerngruppe = manager.getLerngruppe(strLerngruppenID);
						lerngruppe.lehrerID.add(leistung.Fachlehrer_ID);
						if (manager.getLehrer(leistung.Fachlehrer_ID) == null) {
							final DTOLehrer dtoFachlehrer = mapLehrer.get(leistung.Fachlehrer_ID);
							if (dtoFachlehrer != null) {
								final String creds = (dtoLehrer == null) ? mapLehrerPWHash.get(dtoFachlehrer.ID) : "";
								final String tsCreds = (dtoLehrer == null) ? mapLehrerPWHashTimestamps.get(dtoFachlehrer.ID) : null;
								manager.addLehrer(dtoFachlehrer.ID, dtoFachlehrer.Kuerzel, dtoFachlehrer.Nachname, dtoFachlehrer.Vorname,
										dtoFachlehrer.Geschlecht, dtoFachlehrer.eMailDienstlich,
										(creds == null) ? "" : creds, (tsCreds == null) ? null : tsCreds);
							}
						}
					}
					// TODO ggf. im Team-Teaching unterrichtende Lehrer hinzufügen (Zusatzkraft in Leistungsdaten bzw. weitere Lehrkraft bei Kursen)
					if ((dtoLehrer != null) && (leistung.Fachlehrer_ID == dtoLehrer.ID))
						leistungenFachIDs.add(lerngruppe.fachID);

					final int halbjahr = abschnitt.abschnitt;
					final boolean istSchriftlich = (leistung.Kurs_ID != null)
							&& ((kursart == ZulaessigeKursart.LK1) || (kursart == ZulaessigeKursart.LK2)
									|| (kursart == ZulaessigeKursart.GKS) || (kursart == ZulaessigeKursart.AB3)
									|| ((kursart == ZulaessigeKursart.AB4) && (!("Q2".equals(dtoKlasse.ASDKlasse) && (halbjahr == 2)))));

					final String tmpAbiFach = leistung.AbiFach;
					final Integer abiFach = (tmpAbiFach == null) ? null : switch (tmpAbiFach) {
						case "1" -> 1;
						case "2" -> 2;
						case "3" -> 3;
						case "4" -> 4;
						default -> null;
					};
					final boolean istGemahnt = (leistung.Warnung != null) && leistung.Warnung;
					final String mahndatum = leistung.Warndatum;
					final DTOTimestampsSchuelerLeistungsdaten tsLeistung = mapTimestampsLeistungsdaten.get(leistung.ID);
					final ENMLeistung enmLeistung = manager.addSchuelerLeistungsdaten(enmSchueler,
							leistung.ID, lerngruppe.id, leistung.NotenKrz, tsLeistung.tsNotenKrz,
							leistung.NotenKrzQuartal, tsLeistung.tsNotenKrzQuartal, istSchriftlich, abiFach,
							leistung.FehlStd, tsLeistung.tsFehlStd, leistung.uFehlStd, tsLeistung.tsuFehlStd,
							leistung.Lernentw, tsLeistung.tsLernentw, null, istGemahnt, tsLeistung.tsWarnung, mahndatum);

					// Teilleistungen und deren Arten hinzufügen
					final List<DTOSchuelerTeilleistung> teilleistungen = mapTeilleistungen.get(leistung.ID);
					if (teilleistungen != null) {
						for (final DTOSchuelerTeilleistung teilleistung : teilleistungen) {
							if (teilleistung.Art_ID == null)
								continue;
							// Prüfe die Teilleistungsart und ergänze sie ggf.
							final ENMTeilleistungsart enmTeilleistungsart = manager.getTeilleistungsart(teilleistung.Art_ID);
							if (enmTeilleistungsart == null) {
								final DTOTeilleistungsarten dtoArt = mapTeilleistungsarten.get(teilleistung.Art_ID);
								if (dtoArt == null) // DB-Error -> should not happen
									throw new NullPointerException();
								manager.addTeilleistungsart(dtoArt.ID, dtoArt.Bezeichnung,
										(dtoArt.Sortierung == null) ? 32000 : dtoArt.Sortierung,
										(dtoArt.Gewichtung == null) ? 1.0 : dtoArt.Gewichtung);
							}
							// Füge die Teilleistung hinzu
							final DTOTimestampsSchuelerTeilleistungen teilleistungTimestamps = mapTeilleistungenTimestamps.get(teilleistung.ID);
							if (teilleistungTimestamps == null)
								throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
										"Es konnten keine Zeitstempel für die Teilleistungen ausgelesen werden. Dies deutet auf einen Fehler in der Datenbank hin.");
							manager.addSchuelerTeilleistung(enmLeistung, teilleistung.ID, teilleistung.Art_ID, teilleistungTimestamps.tsArt_ID,
									teilleistung.Datum, teilleistungTimestamps.tsDatum, teilleistung.Bemerkung, teilleistungTimestamps.tsBemerkung,
									teilleistung.NotenKrz, teilleistungTimestamps.tsNotenKrz);
						}
					}

					// TODO check and add ZP10 - Data
					// TODO check and add BKAbschluss - Data
				}
			}

			// Ankreuzkompetenzen hinzufügen und deren Katalog-Einträge hinzufügen
			final List<DTOSchuelerAnkreuzfloskeln> ankreuzkompetenzen = mapAnkreuzkompetenzen.get(lernabschnitt.ID);
			if (ankreuzkompetenzen != null) {
				for (final DTOSchuelerAnkreuzfloskeln ankreuzkompetenz : ankreuzkompetenzen) {
					final DTOAnkreuzfloskeln dtoAnkreuzkompetenz = mapKatalogAnkreuzkompetenzen.get(ankreuzkompetenz.Floskel_ID);
					if (dtoAnkreuzkompetenz == null) // DB-Error -> should not happen
						throw new NullPointerException();
					// Überspringe bei Lehrer-Spezifischen Daten die Ankreuzkompetezen, falls keine Fachlehrer-Zuordnung vorliegt bzw. der Lehrer kein Klassenlehrer ist
					if ((dtoLehrer != null) && (((dtoAnkreuzkompetenz.Fach_ID == null) && !enmKlasse.klassenlehrer.contains(dtoLehrer.ID))
							|| ((dtoAnkreuzkompetenz.Fach_ID != null) && !leistungenFachIDs.contains(dtoAnkreuzkompetenz.Fach_ID))))
						continue;
					// Prüfe die Ankreuzfloskel und ergänze sie ggf.
					final String jahrgang = getJahrgang(ankreuzkompetenz.ID, lernabschnitt.Fachklasse_ID, mapKlassen, kompetenzZuordnungenByJahrgang, mapJahrgaenge);
					final ENMAnkreuzkompetenz enmAnkreuzkompetenz = manager.getAnkreuzkompetenz(ankreuzkompetenz.Floskel_ID);
					if (enmAnkreuzkompetenz == null)
						manager.addAnkreuzkompetenz(dtoAnkreuzkompetenz.ID, (dtoAnkreuzkompetenz.IstASV == 0), dtoAnkreuzkompetenz.Fach_ID,
								jahrgang, dtoAnkreuzkompetenz.FloskelText, dtoAnkreuzkompetenz.Sortierung);
					// Füge die Schueler-Ankreuzkompetenz hinzu
					final DTOTimestampsSchuelerAnkreuzkompetenzen ankreuzkompetenzTimestamps = mapAnkreuzkompetenzenTimestamps.get(ankreuzkompetenz.ID);
					if (ankreuzkompetenzTimestamps == null)
						throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
								"Es konnten keine Zeitstempel für die Ankreuzkompetenzen ausgelesen werden. Dies deutet auf einen Fehler in der Datenbank hin.");
					final boolean[] stufen = { ankreuzkompetenz.Stufe1, ankreuzkompetenz.Stufe2, ankreuzkompetenz.Stufe3, ankreuzkompetenz.Stufe4,
							ankreuzkompetenz.Stufe5 };
					manager.addSchuelerAnkreuzkompetenz(enmSchueler, ankreuzkompetenz.ID, ankreuzkompetenz.Floskel_ID, stufen,
							ankreuzkompetenzTimestamps.tsStufe);
				}
			}
		}

		// Kopiere den Floskel-Katalog in die ENM-Daten
		getFloskeln(conn, manager);

		return manager.daten;
	}

	private static String getJahrgang(final long idAnkreuzkompetenz, final Long idKlasse, final Map<Long, DTOKlassen> klassenById,
			final Map<Long, List<DTOAnkreuzkompetenzJahrgang>> zuordnungenByIdJahrgang, final Map<Long, DTOJahrgang> jahrgaengeById) {
		if (idKlasse == null) {
			return "";
		}
		final DTOKlassen klasse = klassenById.get(idKlasse);
		if ((klasse == null) || (klasse.Jahrgang_ID == null)) {
			return "";
		}
		final long idJahrgang = klasse.Jahrgang_ID;
		final boolean exists = zuordnungenByIdJahrgang
				.getOrDefault(idJahrgang, List.of())
				.stream()
				.anyMatch(dto -> dto.idAnkreuzkompetenz == idAnkreuzkompetenz);
		if (!exists) {
			return "";
		}
		return Optional.ofNullable(jahrgaengeById.get(idJahrgang))
				.map(dto -> dto.ASDJahrgang)
				.orElse("");
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}


	/**
	 * Lädt die Konfiguration, inwiefern Anpassungen an den Daten für die Klasse zulässig ist oder nicht.
	 *
	 * @param idKlasse   die ID der Klasse, für welche die Konfiguation geladen werden soll
	 *
	 * @return die Konfigurationen für die Klasse oder null, falls keine existiert
	 *
	 * @throws ApiOperationException   falls ein Fehler beim Deserialisieren der Konfiguration auftritt
	 */
	private ENMConfigKlasse getKonfigurationErlaubt(final long idKlasse) throws ApiOperationException {
		try {
			final DTONotenmodulKonfigurationClient config = conn.queryByKey(DTONotenmodulKonfigurationClient.class, "noteneingabe.gesperrt");
			if (config == null)
				return null;
			final List<ENMConfigKlasse> list = JSONMapper.mapper.readerForListOf(ENMConfigKlasse.class).readValue(config.wert);
			if (list == null)
				return null;
			for (final ENMConfigKlasse cfg : list)
				if (cfg.id == idKlasse)
					return cfg;
			return null;
		} catch (final @NotNull IOException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Einlesen der Konfiguration.");
		}
	}


	/**
	 * Prüfe, ob die zeitliche Eingabebeschränkung für den Eingabebeginn die Notenanpassung erlaubt oder nicht.
	 *
	 * @param config  die Konfiguration für die Klasse
	 * @param now     der aktuelle Zeitpunkt
	 *
	 * @throws ApiOperationException   falls die Eingabe von der Zeiteinschränkung her nicht erlaubt ist
	 */
	private static void pruefeEingabebeginn(final @NotNull ENMConfigKlasse config, final @NotNull Timestamp now) throws ApiOperationException {
		if (config.tsEingabeAb == null)
			return;
		try {
			final Timestamp beginn = getTimeStampFromIso(config.tsEingabeAb);
			if (beginn == null)
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Fehlerhaftes Datumsformat beim Eingabebeginn für die Klasse mit der ID " + config.id);
			if (!now.after(beginn))
				throw new ApiOperationException(Status.FORBIDDEN,
						"Die Eingabe ist noch nicht freigegeben. (Das Datum für den Eingabebeginn liegt in der Zukunft).");
		} catch (final DateTimeParseException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
					"Fehlerhaftes Datumsformat beim Eingabebeginn für die Klasse mit der ID " + config.id);
		}
	}


	/**
	 * Prüfe, ob die zeitliche Eingabebeschränkung die Notenanpassung erlaubt oder nicht.
	 *
	 * @param config  die Konfiguration für die Klasse
	 * @param now     der aktuelle Zeitpunkt
	 *
	 * @throws ApiOperationException   falls die Eingabe von der Zeiteinschränkung her nicht erlaubt ist
	 */
	private static void pruefeEingabeende(final @NotNull ENMConfigKlasse config, final @NotNull Timestamp now) throws ApiOperationException {
		if (config.tsEingabeBis == null)
			return;
		try {
			final Timestamp ende = getTimeStampFromIso(config.tsEingabeBis);
			if (ende == null)
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Fehlerhaftes Datumsformat beim Eingabeende für die Klasse mit der ID " + config.id);
			if (!now.before(ende))
				throw new ApiOperationException(Status.FORBIDDEN,
						"Die Eingabe ist nicht mehr freigegeben. (Das Datum für das Eingabeende liegt in der Vergangenheit).");
		} catch (final DateTimeParseException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
					"Fehlerhaftes Datumsformat beim Eingabeende für die Klasse mit der ID " + config.id);
		}
	}


	private void pruefeKonfigurationPatchErlaubt(final long idKlasse, final @NotNull String attribute) throws ApiOperationException {
		// TODO if Entfernen, wenn das Feature auch außerhalb des DEV-Modes genutzt werden soll
		if (SVWSKonfiguration.get().getServerMode() != ServerMode.DEV)
			return;
		final ENMConfigKlasse config = getKonfigurationErlaubt(idKlasse);
		if (config == null)
			throw new ApiOperationException(Status.FORBIDDEN, "Es liegt keine Konfiguration für die Eingabe von Noten für diese Klasse vor.");
		// Prüfe generelle Berechtigung bei der Eingabespalte
		boolean allowed = false;
		for (final @NotNull ENMConfigKlasseSpalte col : config.spalten) {
			if (attribute.equals(col.name) && (!col.gesperrt)) {
				allowed = true;
				break;
			}
		}
		if (!allowed)
			throw new ApiOperationException(Status.FORBIDDEN, "Eine Änderung wurde nicht explizit für diese Klasse erlaubt.");
		// Prüfe die zeitliche Einschränkung für die Eingabe, sofern eine gesetzt wurde
		final Timestamp now = getTimeStampNow();
		pruefeEingabebeginn(config, now);
		pruefeEingabeende(config, now);
	}


	private void pruefeKonfigurationPatchErlaubtFehlstunden(final long idKlasse, final boolean istGesamtFS) throws ApiOperationException {
		// TODO if Entfernen, wenn das Feature auch außerhalb des DEV-Modes genutzt werden soll
		if (SVWSKonfiguration.get().getServerMode() != ServerMode.DEV)
			return;
		final ENMConfigKlasse config = getKonfigurationErlaubt(idKlasse);
		if (config == null)
			throw new ApiOperationException(Status.FORBIDDEN, "Es liegt keine Konfiguration für die Eingabe von Noten für diese Klasse vor.");
		// Prüfe generelle Berechtigung bei den Fehlstunden
		boolean allowed = false;
		for (final @NotNull ENMConfigKlasseSpalte col : config.spalten) {
			if ("Fehlstunden".equals(col.name) && (!col.gesperrt)) {
				allowed = true;
				break;
			}
		}
		if (!allowed)
			throw new ApiOperationException(Status.FORBIDDEN, "Eine Änderung von Fehlstunden wurde nicht explizit für diese Klasse erlaubt.");

		// TODO prüfe auch die Information, ob nur Gesamtfehlstunden eingegeben werden sollen oder auf Basis von Lerngruppen

		// Prüfe die zeitliche Einschränkung für die Eingabe, sofern eine gesetzt wurde
		final Timestamp now = getTimeStampNow();
		pruefeEingabebeginn(config, now);
		pruefeEingabeende(config, now);
	}

	private void pruefeKonfigurationPatchErlaubtTeilleistung(final long idKlasse, final long idTeilleistungsart) throws ApiOperationException {
		// TODO if Entfernen, wenn das Feature auch außerhalb des DEV-Modes genutzt werden soll
		if (SVWSKonfiguration.get().getServerMode() != ServerMode.DEV)
			return;
		final ENMConfigKlasse config = getKonfigurationErlaubt(idKlasse);
		if (config == null)
			throw new ApiOperationException(Status.FORBIDDEN, "Es liegt keine Konfiguration für die Eingabe von Noten für diese Klasse vor.");
		// Prüfe generelle Berechtigung bei der Eingabespalte
		boolean allowed = false;
		boolean allowedSpecial = false;
		for (final @NotNull ENMConfigKlasseSpalte col : config.spalten) {
			if ("Teilnoten".equals(col.name) && (!col.gesperrt)) {
				allowed = true;
			} else if ((col.idTeilleistung != null) && (col.idTeilleistung == idTeilleistungsart) && (!col.gesperrt)) {
				allowedSpecial = true;
			}
		}
		if (!allowed)
			throw new ApiOperationException(Status.FORBIDDEN, "Eine Änderung von Teilleistungen wurde nicht explizit für diese Klasse erlaubt.");
		if (!allowedSpecial)
			throw new ApiOperationException(Status.FORBIDDEN, "Eine Änderung der Teilleistungsart wurde nicht explizit für diese Klasse erlaubt.");
		// Prüfe die zeitliche Einschränkung für die Eingabe, sofern eine gesetzt wurde
		final Timestamp now = getTimeStampNow();
		pruefeEingabebeginn(config, now);
		pruefeEingabeende(config, now);
	}


	/**
	 * Prüft, ob der angemeldete Benutzer eine Berechtigung zum Patchen von Leistungsdaten hat oder nicht.
	 *
	 * @param leistung   die Leistungsdaten.
	 *
	 * @return der Grund für die Berechtigung (0 - allgemeine Kompetenz, 1 - funktionsbezogen als Fachlehrer,
	 *         2 - funktionsbezogen als Klassenlehrer oder Abteilungsleiter)
	 *
	 * @throws ApiOperationException für den Fall, dass keine Berechtigung für das Anpassen der Leistungsdaten vorliegt
	 */
	private int pruefeBerechtigungPatchLeistung(final DTOSchuelerLeistungsdaten leistung) throws ApiOperationException {
		final DTOSchuelerLernabschnittsdaten lernabschnitt = conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, leistung.Abschnitt_ID);
		if (lernabschnitt == null) // Sollte nicht vorkommen, da eine Foreign-Key-Constraint besteht
			throw new ApiOperationException(Status.NOT_FOUND, "Es konnte kein zugehöriger Lernabschnitt gefunden werden.");

		// Prüfe, ob der Lernabschnitt im aktuellen Schuljahresabschnitt der Schule liegt
		if (conn.getUser().schuleGetSchuljahresabschnitt().id != lernabschnitt.Schuljahresabschnitts_ID)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Leistungsdaten sind nicht dem aktuellen Schuljahresabschnitt der Schule zugeordnet.");

		// Prüfe, ob der angemeldete Benutzer eine allgemeine Berechtigung hat, um die Leistungsdaten anzupassen
		if (conn.getUser().istAdmin() || conn.getUser().hatVerwendeteKompetenz(BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN))
			return 0;

		// Prüfe, ob der angemeldete Benutzer eine funktionsbezogene Berechtigung hat, um die Leistungsdaten anzupassen
		final Long idLehrer = conn.getUser().getIdLehrer();
		if (idLehrer == null)
			throw new ApiOperationException(Status.FORBIDDEN, "Ein funktionsbezogener Zugriff ist nur für Lehrer-Benutzer möglich.");

		// Prüfe, ob er diese als Fachlehrer besitzt
		if ((leistung.Fachlehrer_ID != null) && (leistung.Fachlehrer_ID.longValue() == idLehrer.longValue()))
			return 1;

		// Prüfe, ob der angemeldete Lehrer als Klassenlehrer oder Abteilungsleiter die nötigen Rechte besitzt.
		if (conn.getUser().getKlassenIDs().contains(lernabschnitt.Klassen_ID))
			return 2;

		// ... ansonsten ist kein funktionsbezogener Zugriff erlaubt.
		throw new ApiOperationException(Status.FORBIDDEN, "Der Lehrer hat keinen funktionsbezogenen Zugriff auf die ENM-Daten.");
	}


	/**
	 * Prüft, ob der angemeldete Benutzer eine Berechtigung zum Patchen von Lernabschnittsdaten hat oder nicht.
	 *
	 * @param lernabschnitt   die Lernabschnittsdaten.
	 *
	 * @return der Grund für die Berechtigung (0 - allgemeine Kompetenz, 2 - funktionsbezogen als Klassenlehrer oder Abteilungsleiter)
	 *
	 * @throws ApiOperationException für den Fall, dass keine Berechtigung für das Anpassen der Lernabschnittsdaten vorliegt
	 */
	private int pruefeBerechtigungPatchLernabschnitt(final DTOSchuelerLernabschnittsdaten lernabschnitt) throws ApiOperationException {
		// Prüfe, ob der Lernabschnitt im aktuellen Schuljahresabschnitt der Schule liegt
		if (conn.getUser().schuleGetSchuljahresabschnitt().id != lernabschnitt.Schuljahresabschnitts_ID)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Lernabschnittsdaten sind nicht dem aktuellen Schuljahresabschnitt der Schule zugeordnet.");

		// Prüfe, ob der angemeldete Benutzer eine allgemeine Berechtigung hat, um die Leistungsdaten anzupassen
		if (conn.getUser().istAdmin() || conn.getUser().hatVerwendeteKompetenz(BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN))
			return 0;

		// Prüfe, ob der angemeldete Benutzer eine funktionsbezogene Berechtigung hat, um die Leistungsdaten anzupassen
		final Long idLehrer = conn.getUser().getIdLehrer();
		if (idLehrer == null)
			throw new ApiOperationException(Status.FORBIDDEN, "Ein funktionsbezogener Zugriff ist nur für Lehrer-Benutzer möglich.");

		// Prüfe, ob der angemeldete Lehrer als Klassenlehrer oder Abteilungsleiter die nötigen Rechte besitzt.
		if (conn.getUser().getKlassenIDs().contains(lernabschnitt.Klassen_ID))
			return 2;

		// ... ansonsten ist kein funktionsbezogener Zugriff erlaubt.
		throw new ApiOperationException(Status.FORBIDDEN, "Der Lehrer hat keinen funktionsbezogenen Zugriff auf die ENM-Daten.");
	}


	/**
	 * Prüft, ob ein Patchen der Leistungsdaten durch den aktuell angemeldeten Benutzer erlaubt ist
	 * und passt die Leistungsdaten eines Schüler dann ggf. an.
	 *
	 * @param is   der {@link InputStream} mit dem JSON-Patch
	 *
	 * @return Die HTTP-Response der Patch-Operation
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response patchENMLeistung(final InputStream is) throws ApiOperationException {
		final Map<String, Object> patch = JSONMapper.toMap(is);
		if (patch.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST, "In dem Patch sind keine Daten enthalten.");

		// Bestimme die Leistungsdaten anhand der ID des Patches
		if (!patch.containsKey("id"))
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Patch muss eine ID enthalten.");
		final Long id = JSONMapper.convertToLong(patch.get("id"), false, "id");
		final DTOSchuelerLeistungsdaten leistung = conn.queryByKey(DTOSchuelerLeistungsdaten.class, id);
		if (leistung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Für die ID %d konnten keine Leistungsdaten gefunden werden.".formatted(id));

		// Bestimme den Lernabschnitt und die Klasse des Lernabschnittes
		final DTOSchuelerLernabschnittsdaten lernabschnitt = conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, leistung.Abschnitt_ID);
		if ((lernabschnitt == null) || (lernabschnitt.Klassen_ID == null))
			throw new ApiOperationException(Status.NOT_FOUND,
					"Für die Abschnitts-ID %d konnten keine Klassenzugehörigkeit bestimmt werden.".formatted(leistung.Abschnitt_ID));

		// Prüfe die Berechtigung für das Patchen der Leistungsdaten
		pruefeBerechtigungPatchLeistung(leistung); // final int berechtigung =

		// Durchführen des Patches
		// Die Umsetzung der Notenmodul-Konfiguration ist noch nicht erfolgt.
		for (final Entry<String, Object> p : patch.entrySet()) {
			switch (p.getKey()) {
				case "id" -> {
					/* do nothing */ }
				case "noteQuartal" -> {
					pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "Quartalsnoten");
					final String kuerzel = JSONMapper.convertToString(p.getValue(), true, false, null, "noteQuartal");
					if ((kuerzel != null) && (Note.fromKuerzel(kuerzel) == Note.KEINE))
						throw new ApiOperationException(Status.BAD_REQUEST, "Die Zeichenkette '%s' ist keine gültige Note.".formatted(kuerzel));
					leistung.NotenKrzQuartal = kuerzel;
				}
				case "note" -> {
					pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "Note");
					final String kuerzel = JSONMapper.convertToString(p.getValue(), true, false, null, "note");
					if ((kuerzel != null) && (Note.fromKuerzel(kuerzel) == Note.KEINE))
						throw new ApiOperationException(Status.BAD_REQUEST, "Die Zeichenkette '%s' ist keine gültige Note.".formatted(kuerzel));
					leistung.NotenKrz = kuerzel;
				}
				case "fehlstundenFach" -> {
					pruefeKonfigurationPatchErlaubtFehlstunden(lernabschnitt.Klassen_ID, false);
					leistung.FehlStd = JSONMapper.convertToIntegerInRange(p.getValue(), true, 0, 1000, "fehlstundenFach");
				}
				case "fehlstundenUnentschuldigtFach" -> {
					pruefeKonfigurationPatchErlaubtFehlstunden(lernabschnitt.Klassen_ID, false);
					leistung.uFehlStd = JSONMapper.convertToIntegerInRange(p.getValue(), true, 0, 1000, "fehlstundenUnentschuldigtFach");
				}
				case "fachbezogeneBemerkungen" -> {
					pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "FB");
					leistung.Lernentw = JSONMapper.convertToString(p.getValue(), true, true, Schema.tab_SchuelerLeistungsdaten.col_Lernentw.datenlaenge(),
							"fachbezogeneBemerkungen");
				}
				case "istGemahnt" -> {
					pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "Mahnung");
					if ((leistung.Warndatum != null) && (!"".equals(leistung.Warndatum.trim())))
						throw new ApiOperationException(Status.BAD_REQUEST,
								"Patchen, ob gemahnt wurde, ist nicht erlaubt, da bereits ein Warndatum gesetzt ist.");
					leistung.Warnung = JSONMapper.convertToBoolean(p.getValue(), true, p.getKey());
				}
				default ->
					throw new ApiOperationException(Status.BAD_REQUEST, "Das Attribut %s darf nicht im Patch enthalten sein.".formatted(p.getKey()));
			}
		}
		// Prüfen, ob die Werte für die Fehlstunden so zulässig sind.
		final int fs = (leistung.FehlStd == null) ? 0 : leistung.FehlStd;
		final int fsu = (leistung.uFehlStd == null) ? 0 : leistung.uFehlStd;
		if (fsu > fs)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die nicht entschuldigten Fehlstunden (%d) dürfen nicht mehr sein, als die Anzahl der Fehlstunden (%d) in dem Fach"
							.formatted(fsu, fs));
		conn.transactionPersist(leistung);
		conn.transactionFlush();
		return Response.status(Status.NO_CONTENT).build();
	}


	/**
	 * Prüft, ob ein Patchen der Teilleistungen durch den aktuell angemeldeten Benutzer erlaubt ist
	 * und passt die Teilleistung eines Schüler dann ggf. an.
	 *
	 * @param is   der {@link InputStream} mit dem JSON-Patch
	 *
	 * @return Die HTTP-Response der Patch-Operation
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response patchENMTeilleistung(final InputStream is) throws ApiOperationException {
		final Map<String, Object> patch = JSONMapper.toMap(is);
		if (patch.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST, "In dem Patch sind keine Daten enthalten.");

		// Bestimme die Teilleistung anhand der ID des Patches und die Leistungsdaten anhand der Teilleistung
		if (!patch.containsKey("id"))
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Patch muss eine ID enthalten.");
		final Long id = JSONMapper.convertToLong(patch.get("id"), false, "id");
		final DTOSchuelerTeilleistung teilleistung = conn.queryByKey(DTOSchuelerTeilleistung.class, id);
		if (teilleistung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Für die ID %d konnten keine Teilleistungsdaten gefunden werden.".formatted(id));

		// Bestimme die zugehörigen Leistungsdaten anhand der ID aus den Daten der Teilleistung
		final DTOSchuelerLeistungsdaten leistung = conn.queryByKey(DTOSchuelerLeistungsdaten.class, teilleistung.Leistung_ID);
		if (leistung == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Für die ID %d konnten keine Leistungsdaten gefunden werden.".formatted(teilleistung.Leistung_ID));

		// Bestimme den Lernabschnitt und die Klasse des Lernabschnittes
		final DTOSchuelerLernabschnittsdaten lernabschnitt = conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, leistung.Abschnitt_ID);
		if ((lernabschnitt == null) || (lernabschnitt.Klassen_ID == null))
			throw new ApiOperationException(Status.NOT_FOUND,
					"Für die Abschnitts-ID %d konnten keine Klassenzugehörigkeit bestimmt werden.".formatted(leistung.Abschnitt_ID));

		// Prüfe die Berechtigung für das Patchen der Teilleistungsdaten anhand der zugehörigen Leistungsdaten
		pruefeBerechtigungPatchLeistung(leistung); // final int berechtigung =

		// Durchführen des Patches
		// Die Umsetzung der Notenmodul-Konfiguration ist noch nicht erfolgt.
		for (final Entry<String, Object> p : patch.entrySet()) {
			switch (p.getKey()) {
				case "id" -> {
					/* do nothing */ }
				case "note" -> {
					pruefeKonfigurationPatchErlaubtTeilleistung(lernabschnitt.Klassen_ID, teilleistung.Art_ID);
					final String kuerzel = JSONMapper.convertToString(p.getValue(), true, false, null, "note");
					if ((kuerzel != null) && (Note.fromKuerzel(kuerzel) == Note.KEINE))
						throw new ApiOperationException(Status.BAD_REQUEST, "Die Zeichenkette '%s' ist keine gültige Note.".formatted(kuerzel));
					teilleistung.NotenKrz = kuerzel;
				}
				default ->
					throw new ApiOperationException(Status.BAD_REQUEST, "Das Attribut %s darf nicht im Patch enthalten sein.".formatted(p.getKey()));
			}
		}
		conn.transactionPersist(teilleistung);
		conn.transactionFlush();
		return Response.status(Status.NO_CONTENT).build();
	}


	/**
	 * Ermittelt für die übergebene Schüler-ID die Lernabschnittsdaten des aktuellen
	 * Schuljahresabschnittes der Schule.
	 *
	 * @param id   die ID des Schülers
	 *
	 * @return die Lernabschnittsdaten
	 *
	 * @throws ApiOperationException falls die Lernabschnittsdaten nicht erfolgreich bestimmt werden können
	 */
	private DTOSchuelerLernabschnittsdaten getLernabschnitt(final Long id) throws ApiOperationException {
		// Bestimme zunächst den zugehörigen Schüler id der Datenbank
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Bei der Anfrage muss eine Schüler-ID angegeben werden.");
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
		if (schueler == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Es existiert kein Schüler mit der ID %d.".formatted(id));

		// Bestimme den Lernabschnitt mit der Wechsel-Nr 0 des Schülers in dem aktuellen Schuljahresabschnitt der Schule
		final Schuljahresabschnitt sja = conn.getUser().schuleGetSchuljahresabschnitt();
		final List<DTOSchuelerLernabschnittsdaten> slas = conn.queryList(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 AND e.Schuljahresabschnitts_ID = ?2 AND e.WechselNr = 0",
				DTOSchuelerLernabschnittsdaten.class, schueler.ID, sja.id);
		if (slas.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es existiert kein Lernabschnitt im aktuellen Schuljahresabschnitt der Schule für den Schüler mit der ID %d.".formatted(id));
		if (slas.size() > 1)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es gibt mehrere Lernabschnitte im aktuellen Schuljahresabschnitt der Schule für den Schüler mit der ID %d und der WechselNr 0."
							.formatted(id));
		return slas.getFirst();
	}


	/**
	 * Prüft, ob ein Patchen der Bemerkungen zu einem Schüler-Lernabschnitt durch den aktuell angemeldeten
	 * Benutzer erlaubt ist und passt die Bemerkungen dann ggf. an.
	 *
	 * @param id   die ID des Schülers, dessen Bemerkungen angepasst werden sollen
	 * @param is   der {@link InputStream} mit dem JSON-Patch
	 *
	 * @return Die HTTP-Response der Patch-Operation
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response patchENMSchuelerBemerkungen(final Long id, final InputStream is) throws ApiOperationException {
		final Map<String, Object> patch = JSONMapper.toMap(is);
		if (patch.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST, "In dem Patch sind keine Daten enthalten.");

		// Bestimme den Lernabschnitt des Schülers im aktuellen Schuljahresabschnitt der Schule.
		final DTOSchuelerLernabschnittsdaten lernabschnitt = getLernabschnitt(id);

		// Prüfe die Berechtigung für das Patchen der Bemerkungen anhand des Lernabschnittes des Schülers
		pruefeBerechtigungPatchLernabschnitt(lernabschnitt); // final int berechtigung =

		// Bestimme die Bemerkungen, welche dem Schüler zugeordnet sind.
		final List<DTOSchuelerPSFachBemerkungen> sbs =
				conn.queryList(DTOSchuelerPSFachBemerkungen.QUERY_BY_ABSCHNITT_ID, DTOSchuelerPSFachBemerkungen.class, lernabschnitt.ID);
		if (sbs.size() > 1)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Es gibt mehrere Einträge für Fachbemekungen zu dem Lernabschnitt mit der ID %d.".formatted(lernabschnitt.ID));
		final DTOSchuelerPSFachBemerkungen sb;
		if (sbs.isEmpty()) {
			sb = new DTOSchuelerPSFachBemerkungen(conn.transactionGetNextID(DTOSchuelerPSFachBemerkungen.class), lernabschnitt.ID);
		} else {
			sb = sbs.getFirst();
		}

		// Durchführen des Patches
		// Die Umsetzung der Notenmodul-Konfiguration ist noch nicht erfolgt.
		for (final Entry<String, Object> p : patch.entrySet()) {
			switch (p.getKey()) {
				case "ASV" -> {
					pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "ASV");
					sb.ASV = JSONMapper.convertToString(p.getValue(), true, true, Schema.tab_SchuelerLD_PSFachBem.col_ASV.datenlaenge(), p.getKey());
				}
				case "AUE" -> {
					pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "AUE");
					sb.AUE = JSONMapper.convertToString(p.getValue(), true, true, Schema.tab_SchuelerLD_PSFachBem.col_AUE.datenlaenge(), p.getKey());
				}
				case "ZB" -> {
					pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "ZB");
					lernabschnitt.ZeugnisBem =
							JSONMapper.convertToString(p.getValue(), true, true, Schema.tab_SchuelerLernabschnittsdaten.col_ZeugnisBem.datenlaenge(),
									p.getKey());
				}
				case "LELS" -> {
					pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "LELS");
					sb.LELS = JSONMapper.convertToString(p.getValue(), true, true, Schema.tab_SchuelerLD_PSFachBem.col_LELS.datenlaenge(), p.getKey());
				}
				case "schulformEmpf" -> {
					pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "SchulformEmpfehlung");
					sb.ESF = JSONMapper.convertToString(p.getValue(), true, true, Schema.tab_SchuelerLD_PSFachBem.col_ESF.datenlaenge(), p.getKey());
				}
				case "individuelleVersetzungsbemerkungen" -> {
					pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "Versetzungsbemerkungen");
					sb.BemerkungVersetzung = JSONMapper.convertToString(p.getValue(), true, true,
							Schema.tab_SchuelerLD_PSFachBem.col_BemerkungVersetzung.datenlaenge(), p.getKey());
				}
				case "foerderbemerkungen" -> {
					pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "Förderbemerkungen");
					sb.BemerkungFSP =
							JSONMapper.convertToString(p.getValue(), true, true, Schema.tab_SchuelerLD_PSFachBem.col_BemerkungFSP.datenlaenge(), p.getKey());
				}
				default ->
					throw new ApiOperationException(Status.BAD_REQUEST, "Das Attribut %s darf nicht im Patch enthalten sein.".formatted(p.getKey()));
			}
		}
		conn.transactionPersist(lernabschnitt);
		conn.transactionPersist(sb);
		conn.transactionFlush();
		return Response.status(Status.NO_CONTENT).build();
	}


	/**
	 * Prüft, ob ein Patchen eines Schüler-Lernabschnittes durch den aktuell angemeldeten
	 * Benutzer erlaubt ist und passt diesen dann ggf. an.
	 *
	 * @param is   der {@link InputStream} mit dem JSON-Patch
	 *
	 * @return Die HTTP-Response der Patch-Operation
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response patchENMSchuelerlernabschnitt(final InputStream is) throws ApiOperationException {
		final Map<String, Object> patch = JSONMapper.toMap(is);
		if (patch.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST, "In dem Patch sind keine Daten enthalten.");

		// Bestimme den Lernabschnitt des Schülers im aktuellen Schuljahresabschnitt der Schule.
		if (!patch.containsKey("id"))
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Patch muss eine ID enthalten.");
		final Long id = JSONMapper.convertToLong(patch.get("id"), false, "id");
		final DTOSchuelerLernabschnittsdaten sla = conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, id);
		if (sla == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Für die ID %d konnte kein Lernabschnitt gefunden werden.".formatted(id));

		// Prüfe die Berechtigung für das Patchen der Bemerkungen anhand des Lernabschnittes des Schülers
		pruefeBerechtigungPatchLernabschnitt(sla); // final int berechtigung =

		// Durchführen des Patches
		// Die Umsetzung der Notenmodul-Konfiguration ist noch nicht erfolgt.
		for (final Entry<String, Object> p : patch.entrySet()) {
			switch (p.getKey()) {
				case "id" -> {
					/* do nothing */ }
				case "fehlstundenGesamt" -> {
					pruefeKonfigurationPatchErlaubtFehlstunden(sla.Klassen_ID, true);
					sla.SumFehlStd = JSONMapper.convertToIntegerInRange(p.getValue(), true, 0, 1000, "fehlstundenGesamt");
				}
				case "fehlstundenGesamtUnentschuldigt" -> {
					pruefeKonfigurationPatchErlaubtFehlstunden(sla.Klassen_ID, true);
					sla.SumFehlStdU = JSONMapper.convertToIntegerInRange(p.getValue(), true, 0, 1000, "fehlstundenGesamtUnentschuldigt");
				}
				default ->
					throw new ApiOperationException(Status.BAD_REQUEST, "Das Attribut %s darf nicht im Patch enthalten sein.".formatted(p.getKey()));
			}
		}
		// Prüfen, ob die Werte für die Fehlstunden so zulässig sind.
		final int fs = (sla.SumFehlStd == null) ? 0 : sla.SumFehlStd;
		final int fsu = (sla.SumFehlStdU == null) ? 0 : sla.SumFehlStdU;
		if (fsu > fs)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die nicht entschuldigten Fehlstunden (%d) dürfen nicht mehr sein, als die Anzahl der Fehlstunden (%d) in dem Fach"
							.formatted(fsu, fs));
		conn.transactionPersist(sla);
		conn.transactionFlush();
		return Response.status(Status.NO_CONTENT).build();
	}


	private static void initManager(final ENMDatenManager manager, final SchuleStammdaten schule, final DTOAnkreuzdaten ankreuzkompetenzenStufen,
			final Schuljahresabschnitt abschnitt) {
		// Setze die grundlegenden Schuldaten
		manager.setSchuldaten((int) schule.schulNr, abschnitt.schuljahr, (int) schule.schuleAbschnitte.anzahlAbschnitte, abschnitt.abschnitt,
				/* TODO */ null, /* TODO */ true, /* TODO */ false, /* TODO */ true,
				schule.schulform, /* TODO */ null);
		// Setze die Informationen zu den Stufen der Ankreuzkompetenzen
		if (ankreuzkompetenzenStufen == null)
			manager.setAnkreuzkompetenzenStufen(null, null, null, null, null, null);
		else
			manager.setAnkreuzkompetenzenStufen(ankreuzkompetenzenStufen.TextStufe1, ankreuzkompetenzenStufen.TextStufe2, ankreuzkompetenzenStufen.TextStufe3,
					ankreuzkompetenzenStufen.TextStufe4, ankreuzkompetenzenStufen.TextStufe5, ankreuzkompetenzenStufen.BezeichnungSONST);
		// Kopiere den Noten-Katalog aus dem Core-type in die ENM-Daten
		manager.addNoten(abschnitt.schuljahr);
		// Kopiere den Förderschwerpunkt-Katalog aus dem Core-type in die ENM-Daten
		manager.addFoerderschwerpunkte(abschnitt.schuljahr, Schulform.data().getWertByKuerzel(schule.schulform));
	}

	private static Map<Long, DTOLehrer> getLehrerListe(final DBEntityManager conn) throws ApiOperationException {
		final List<DTOLehrer> lehrer = conn.queryAll(DTOLehrer.class);
		if (lehrer.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return lehrer.stream().collect(Collectors.toMap(e -> e.ID, e -> e));
	}

	private static Map<Long, String> getLehrerCredsListe(final DBEntityManager conn) {
		final List<DTONotenmodulCredentials> lehrer = conn.queryAll(DTONotenmodulCredentials.class);
		if (lehrer.isEmpty())
			return new HashMap<>();
		return lehrer.stream().collect(Collectors.toMap(e -> e.idLehrer, e -> e.passwordHash));
	}

	private static Map<Long, String> getLehrerCredsTimstampsListe(final DBEntityManager conn) {
		final List<DTOTimestampsNotenmodulCredentials> lehrer = conn.queryAll(DTOTimestampsNotenmodulCredentials.class);
		if (lehrer.isEmpty())
			return new HashMap<>();
		return lehrer.stream().collect(Collectors.toMap(e -> e.idLehrer, e -> e.tsPasswordHash));
	}

	private static Map<Long, DTOAnkreuzfloskeln> getAnkreuzkompetenzenListe(final DBEntityManager conn) {
		final List<DTOAnkreuzfloskeln> ankreuzkompetenzen = conn.queryAll(DTOAnkreuzfloskeln.class);
		if (ankreuzkompetenzen.isEmpty())
			return new HashMap<>();
		return ankreuzkompetenzen.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
	}

	private static Map<Long, List<DTOAnkreuzkompetenzJahrgang>> getKompetenzZuordnungenByJahrgang(final DBEntityManager conn) {
		final List<DTOAnkreuzkompetenzJahrgang> ankreuzkompetenzen = conn.queryAll(DTOAnkreuzkompetenzJahrgang.class);
		if (ankreuzkompetenzen.isEmpty()) {
			return new HashMap<>();
		}
		return ankreuzkompetenzen.stream().collect(Collectors.groupingBy(dto -> dto.idJahrgang));
	}

	private static Map<Long, DTOFoerderschwerpunkt> getFoerderschwerpunktListe(final DBEntityManager conn) {
		final List<DTOFoerderschwerpunkt> foerderschwerpunkte = conn.queryAll(DTOFoerderschwerpunkt.class);
		if (foerderschwerpunkte.isEmpty())
			return new HashMap<>();
		return foerderschwerpunkte.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
	}

	private static Map<Long, DTOSchueler> getSchuelerListe(final DBEntityManager conn, final Schuljahresabschnitt abschnitt)
			throws ApiOperationException {
		final List<DTOSchueler> schueler = conn.queryList(DTOSchueler.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOSchueler.class, abschnitt.id);
		if (schueler.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return schueler.stream().filter(s -> (s.idStatus == SchuelerStatus.AKTIV.daten(abschnitt.schuljahr).id)
				|| (s.idStatus == SchuelerStatus.EXTERN.daten(abschnitt.schuljahr).id)).collect(Collectors.toMap(s -> s.ID, s -> s));
	}

	private static Map<Long, DTOFach> getFaecherListe(final DBEntityManager conn) throws ApiOperationException {
		final List<DTOFach> faecher = conn.queryAll(DTOFach.class);
		if (faecher.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return faecher.stream().collect(Collectors.toMap(f -> f.ID, f -> f));
	}

	private static Map<Long, DTOJahrgang> getJahrgangsListe(final DBEntityManager conn) throws ApiOperationException {
		final List<DTOJahrgang> jahrgaenge = conn.queryAll(DTOJahrgang.class);
		if (jahrgaenge.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return jahrgaenge.stream().collect(Collectors.toMap(j -> j.ID, j -> j));
	}

	private static Map<Long, DTOKlassen> getKlassenListe(final DBEntityManager conn, final Schuljahresabschnitt abschnitt) throws ApiOperationException {
		final List<DTOKlassen> klassen = conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, abschnitt.id);
		if (klassen.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return klassen.stream().collect(Collectors.toMap(e -> e.ID, e -> e));
	}

	private static Map<Long, List<DTOKlassenLeitung>> getKlassenleitungen(final DBEntityManager conn, final Map<Long, DTOKlassen> mapKlassen) {
		if (mapKlassen.isEmpty())
			return new HashMap<>();
		return conn.queryList(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID, DTOKlassenLeitung.class, mapKlassen.keySet()).stream()
				.collect(Collectors.groupingBy(kl -> kl.Klassen_ID));
	}

	private static Map<Long, DTOKurs> getKurse(final DBEntityManager conn, final Schuljahresabschnitt abschnitt) throws ApiOperationException {
		final List<DTOKurs> kurse = conn.queryList(DTOKurs.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKurs.class, abschnitt.id);
		if (kurse == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return conn.queryAll(DTOKurs.class).stream().collect(Collectors.toMap(k -> k.ID, k -> k));
	}


	/**
	 * Füllt die Datenstruktur für die Floskelgruppen des ENM mit den in der SVWS-DB hinterlegten
	 * Floskelgruppen und den zugehörigen Floskeln.
	 *
	 * @param conn            die Datenbank-Verbindung
	 * @param manager         der ENM-Daten-Manager
	 */
	private static void getFloskeln(final DBEntityManager conn, final ENMDatenManager manager) {
		final List<DTOFloskelgruppen> dtoFloskelgruppen = conn.queryAll(DTOFloskelgruppen.class);
		final HashMap<Long, ENMFloskelgruppe> map = new HashMap<>();
		for (final DTOFloskelgruppen dto : dtoFloskelgruppen) {
			final ENMFloskelgruppe enmFG = new ENMFloskelgruppe();
			enmFG.kuerzel = dto.Kuerzel;
			enmFG.bezeichnung = dto.Bezeichnung;
			final FloskelgruppenartKatalogEintrag eintrag = Floskelgruppenart.data().getEintragByID(dto.Hauptgruppe_ID);
			enmFG.hauptgruppe = (eintrag == null) ? null : eintrag.schluessel;
			map.put(dto.ID, enmFG);
			manager.daten.floskelgruppen.add(enmFG);
		}
		final Map<Long, List<Long>> jahrgangIdsByFloskelIds = conn
				.queryAll(DTOFloskelnJahrgaenge.class).stream()
				.collect(Collectors.groupingBy(
						fj -> fj.Floskel_ID,
						Collectors.mapping(f -> f.Jahrgang_ID, Collectors.toList())
				));

		final List<DTOFloskeln> dtoFloskeln = conn.queryAll(DTOFloskeln.class);
		for (final DTOFloskeln dto : dtoFloskeln) {
			final ENMFloskelgruppe enmFG = map.get(dto.Gruppe_ID);
			if (enmFG == null) // TODO alternativ Fehlerbehandlung: Wie ordnet man die Floskel zu? -> allgemein ?
				continue;
			final ENMFach fach = (dto.Fach_ID == null) ? null : manager.getFach(dto.Fach_ID);
			final List<Long> jahrgangsIds = jahrgangIdsByFloskelIds.get(dto.ID);
			Long niveau;
			try {
				niveau = (dto.Niveau == null) ? null : dto.Niveau.longValue();
			} catch (@SuppressWarnings("unused") final NumberFormatException e) {
				niveau = null;
			}
			if ((jahrgangsIds == null) || jahrgangsIds.isEmpty()) {
				final ENMFloskel enmFl = new ENMFloskel();
				enmFl.kuerzel = dto.Kuerzel;
				enmFl.text = dto.Text;
				enmFl.fachID = (fach == null) ? null : fach.id;
				enmFl.niveau = niveau;
				enmFl.jahrgangID = null;
				enmFG.floskeln.add(enmFl);
			} else {
				for (final Long jg : jahrgangsIds) {
					final ENMFloskel enmFl = new ENMFloskel();
					enmFl.kuerzel = dto.Kuerzel;
					enmFl.text = dto.Text;
					enmFl.fachID = (fach == null) ? null : fach.id;
					enmFl.niveau = niveau;
					enmFl.jahrgangID = jg;
					enmFG.floskeln.add(enmFl);
				}
			}
		}
	}


	/**
	 * Holt die lokalen Notenmodul-Konfiguration.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response getNotenmodulLocalConfig(final DBEntityManager conn) {
		final ENMServerConfig res = new ENMServerConfig();
		res.server.addAll(conn.queryAll(DTONotenmodulKonfigurationServer.class)
				.stream().map(e -> new BenutzerConfigElement(e.schluessel, e.wert)).toList());
		res.global.addAll(conn.queryAll(DTONotenmodulKonfigurationClient.class)
				.stream().map(e -> new BenutzerConfigElement(e.schluessel, e.wert)).toList());
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(res).build();
	}


	/**
	 * Schreibt ein Konfigurationselement in die Notenmodul-Konfiguration des Servers.
	 *
	 * @param conn   die Datenbank-Verbindung
	 * @param elem   das Konfigurationselement
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response setNotenmodulLocalConfigElement(final DBEntityManager conn, final ENMServerConfigElement elem) throws ApiOperationException {
		if (elem == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wurde kein gültiges Konfigurationselement übergeben.");
		// Füge den Konfiguationswert hinzu ...
		if ("server".equals(elem.type)) {
			// ... in der Server-Konfiguration
			DTONotenmodulKonfigurationServer dto = conn.queryByKey(DTONotenmodulKonfigurationServer.class, elem.key);
			if (dto == null) {
				dto = new DTONotenmodulKonfigurationServer(elem.key, elem.value);
			} else {
				dto.wert = elem.value;
			}
			conn.transactionPersist(dto);
		} else if ("global".equals(elem.type)) {
			// ... in der allgemeinen Client-Konfiguration
			DTONotenmodulKonfigurationClient dto = conn.queryByKey(DTONotenmodulKonfigurationClient.class, elem.key);
			if (dto == null) {
				dto = new DTONotenmodulKonfigurationClient(elem.key, elem.value);
			} else {
				dto.wert = elem.value;
			}
			conn.transactionPersist(dto);
		} else
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wurde ein ungültiger Typ für das Konfigurations übergeben.");
		return Response.status(Status.OK).build();
	}


	/**
	 * Gibt für alle Lehrer, welche bei den ENM-Daten vorkommen die Initialkennwörter zurück.
	 *
	 * @return eine Response mit der Liste der Initialkennwörter
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response getLehrerInitialkennwoerter() throws ApiOperationException {
		// Erstelle zunächst Initialkennwörter, falls eine Lehrer noch keines hat
		generateInitialCredentials(this.conn);
		// Erstelle die ENM-Daten, damit klar ist, für welche Lehrer die Initialkennwörter zurückgegeben werden müssen
		final ENMDaten enmdaten = getDaten(conn, null);
		// Bestimme die Menge der Lehrer-IDs und lese dann dafür die Initialkennwörter aus der Datenbank.
		final List<ENMLehrerInitialKennwort> daten = new ArrayList<>();
		final List<Long> idsLehrer = enmdaten.lehrer.stream().map(l -> l.id).toList();
		if (!idsLehrer.isEmpty()) {
			final List<DTONotenmodulCredentials> dtos = conn.queryByKeyList(DTONotenmodulCredentials.class, idsLehrer);
			for (final DTONotenmodulCredentials dto : dtos) {
				final ENMLehrerInitialKennwort cred = new ENMLehrerInitialKennwort();
				cred.id = dto.idLehrer;
				cred.initialKennwort = dto.initialkennwort;
				daten.add(cred);
			}
		}
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Erstellt für alle Lehrer initiale Credentials, sofern ein Lehrer nicht bereits welche besitzt.
	 *
	 * @param conn   die Datenbankverbindung
	 */
	public static void generateInitialCredentials(final DBEntityManager conn) {
		// Prüfe zunächst die existierenden Credentials auf Vollständigkeit
		final List<DTONotenmodulCredentials> existing = conn.queryAll(DTONotenmodulCredentials.class);
		for (final DTONotenmodulCredentials cred : existing) {
			final boolean hasInitial = (cred.initialkennwort != null) && (!cred.initialkennwort.isBlank());
			final boolean hasHash = (cred.passwordHash != null) && (!cred.passwordHash.isBlank());
			if (hasInitial && hasHash)
				continue;
			if (!hasInitial)
				cred.initialkennwort = Passwords.generateRandomPasswordWithoutSpecialChars(10);
			if (!hasHash)
				cred.passwordHash = BCrypt.hashpw(cred.initialkennwort, BCrypt.gensalt());
			conn.transactionPersist(cred);
		}
		// Erstelle dann die noch fehlenden Credentials
		final Set<Long> idsExisting = existing.stream().map(c -> c.idLehrer).collect(Collectors.toUnmodifiableSet());
		final List<Long> ids = conn.queryAll(DTOLehrer.class).stream().map(l -> l.ID).filter(l -> !idsExisting.contains(l)).toList();
		for (final long id : ids) {
			final String initial = Passwords.generateRandomPasswordWithoutSpecialChars(10);
			final String hash = BCrypt.hashpw(initial, BCrypt.gensalt());
			conn.transactionPersist(new DTONotenmodulCredentials(id, initial, hash));
		}
		conn.transactionFlush();
	}


	/**
	 * Setzt das Kennwort des Lehrers auf das Initialkennwort zurück. Ist kein Initialkennwort vorhanden,
	 * so wird ein neues generiert.
	 *
	 * @param conn       die Datenbankverbindung
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @throws ApiOperationException   NOT_FOUND: falls kein Lehrer mit der angegeben ID existiert
	 */
	public static void resetInitialPassword(final DBEntityManager conn, final long idLehrer) throws ApiOperationException {
		final DTOLehrer dtoLehrer = conn.queryByKey(DTOLehrer.class, idLehrer);
		if (dtoLehrer == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Ein Lehrer mit der ID %d konnte nicht gefunden werden.".formatted(idLehrer));
		DTONotenmodulCredentials cred = conn.queryByKey(DTONotenmodulCredentials.class, idLehrer);
		if (cred == null) {
			final String initial = Passwords.generateRandomPasswordWithoutSpecialChars(10);
			final String hash = BCrypt.hashpw(initial, BCrypt.gensalt());
			cred = new DTONotenmodulCredentials(idLehrer, initial, hash);
		} else {
			final boolean hasInitial = (cred.initialkennwort != null) && (!cred.initialkennwort.isBlank());
			if (!hasInitial)
				cred.initialkennwort = Passwords.generateRandomPasswordWithoutSpecialChars(10);
			cred.passwordHash = BCrypt.hashpw(cred.initialkennwort, BCrypt.gensalt());
		}
		conn.transactionPersist(cred);
	}


	/**
	 * Setzt das Kennwort des Lehrers auf das übergebene Kennwort. Das Initialkennwort bleibt dabei
	 * bestehen oder wird durch ein generiertes gesetzt, wenn der Lehrer vorher kein Initialkennwort hatte.
	 *
	 * @param conn       die Datenbankverbindung
	 * @param idLehrer   die ID des Lehrers
	 * @param password   das neu zu setzende Kennwort
	 *
	 * @throws ApiOperationException   NOT_FOUND: falls kein Lehrer mit der angegeben ID existiert
	 */
	public static void setPassword(final DBEntityManager conn, final long idLehrer, final String password) throws ApiOperationException {
		// TODO geeignetere Kriterien festlegen und in Passwords.java als Methode zum Prüfen implementieren
		if ((password == null) || (password.isBlank()) || (password.length() < 6))
			throw new ApiOperationException(Status.BAD_REQUEST, "Ein neues Kennwort darf nicht leer sein und muss mindestens 6 Zeichen enthalten.");
		// Prüfe, ob der Lehrer in der DB vorhanden ist
		final DTOLehrer dtoLehrer = conn.queryByKey(DTOLehrer.class, idLehrer);
		if (dtoLehrer == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Ein Lehrer mit der ID %d konnte nicht gefunden werden.".formatted(idLehrer));
		// Setze die Credentials und erzeuge bei Bedarf neue
		DTONotenmodulCredentials cred = conn.queryByKey(DTONotenmodulCredentials.class, idLehrer);
		final String hash = BCrypt.hashpw(password, BCrypt.gensalt());
		if (cred == null) {
			final String initial = Passwords.generateRandomPasswordWithoutSpecialChars(10);
			cred = new DTONotenmodulCredentials(idLehrer, initial, hash);
		} else {
			final boolean hasInitial = (cred.initialkennwort != null) && (!cred.initialkennwort.isBlank());
			if (!hasInitial)
				cred.initialkennwort = Passwords.generateRandomPasswordWithoutSpecialChars(10);
			cred.passwordHash = hash;
		}
		conn.transactionPersist(cred);
	}


	/**
	 * Integriert die Veränderungen bei den importierten ENM-Daten gegenüber dem Stand
	 * der SVWS-DB in die SVWS-DB.
	 *
	 * @param conn    die Datenbank-Verbindung
	 * @param daten   die importierten ENMDaten GZIP-komprimiert als Byte-Array
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static void importDatenGZip(final DBEntityManager conn, final byte[] daten) throws ApiOperationException {
		try {
			importDaten(conn, JSONMapper.toObjectGZip(daten, ENMDaten.class));
		} catch (final CompressionException e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e, "Fehler bei entpacken der komprimierten ENM-Daten: " + e.getMessage());
		}
	}


	/**
	 * Integriert die Veränderungen bei den importierten ENM-Daten gegenüber dem Stand
	 * der SVWS-DB in die SVWS-DB.
	 *
	 * @param conn    die Datenbank-Verbindung
	 * @param daten   die importierten Daten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static void importDaten(final DBEntityManager conn, final ENMDaten daten) throws ApiOperationException {
		// TODO Prüfe (daten.lehrer == null) - dann ist es eine Gesamt-ENM-Datei und nicht lehrerspezifisch
		// TODO Führe eine Prüfung der Integrität der Daten durch
		importEnmLehrer(conn, daten.lehrer);
		importEnmSchueler(conn, daten.schueler);
	}


	/**
	 * Importiert die Hashes aus den gegebenen ENMLehrer-Daten in die SVWS-Datenbank. Prüft dazu die Zeitstempel
	 * und aktualisiert neuere Datensätze und deren Zeitstempel.
	 *
	 * @param conn              die Datenbank-Verbindung
	 * @param listEnmLehrer     die zu importierenden Lehrer-Daten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static void importEnmLehrer(final DBEntityManager conn, final List<ENMLehrer> listEnmLehrer) throws ApiOperationException {
		// Bestimme die Lehrer-IDs, anhand der zu importierenden Daten
		if (listEnmLehrer.isEmpty())
			return; // nichts zu tun, da keine Daten vorhanden sind
		final List<Long> idsLehrer = listEnmLehrer.stream().map(s -> s.id).distinct().toList();
		if (idsLehrer.size() != listEnmLehrer.size())
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ENM-Daten haben mindestens eine Lehrer-ID doppelt enthalten. Dies ist nicht zulässig.");
		// Bestimme die Datenbank-DTOs des Lehrers
		final Map<Long, DTOLehrer> mapLehrerDTOs = conn.queryByKeyList(DTOLehrer.class, idsLehrer).stream().collect(Collectors.toMap(l -> l.ID, l -> l));
		if (mapLehrerDTOs.keySet().size() != idsLehrer.size())
			throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle Lehrer in den ENM-Daten konnten auch in der Datenbank gefunden werden.");
		final Map<Long, DTONotenmodulCredentials> mapLehrerCreds =
				conn.queryByKeyList(DTONotenmodulCredentials.class, idsLehrer).stream().collect(Collectors.toMap(c -> c.idLehrer, c -> c));
		final Map<Long, DTOTimestampsNotenmodulCredentials> mapLehrerCredsTimestamps =
				conn.queryByKeyList(DTOTimestampsNotenmodulCredentials.class, idsLehrer).stream().collect(Collectors.toMap(t -> t.idLehrer, t -> t));
		// Gehe die einzelnen Lehrer durch und aktualisiere ggf. die Credentials
		for (final ENMLehrer enmLehrer : listEnmLehrer) {
			final DTOLehrer dtoLehrer = mapLehrerDTOs.get(enmLehrer.id);
			if (dtoLehrer == null)
				throw new ApiOperationException(Status.NOT_FOUND,
						"Der Lehrer in den ENM-Daten mit der ID %d konnte in der Datenbank nicht gefunden werden.".formatted(enmLehrer.id));
			DTONotenmodulCredentials cred = mapLehrerCreds.get(enmLehrer.id);
			final DTOTimestampsNotenmodulCredentials credTS = mapLehrerCredsTimestamps.get(enmLehrer.id);
			if (isTimestampAfter(enmLehrer.tsPasswordHash, credTS == null ? null : credTS.tsPasswordHash)) {
				if (cred == null)
					cred = new DTONotenmodulCredentials(enmLehrer.id, "", enmLehrer.passwordHash);
				else
					cred.passwordHash = enmLehrer.passwordHash;
				conn.transactionPersist(cred);
			}
		}
		conn.transactionFlush();
	}


	/**
	 * Importiert die gegebenen ENMSchueler-Daten in die SVWS-Datenbank. Prüft dazu die Zeitstempel
	 * der einzelnen Felder und aktualisiert neuere Datensätze und deren Zeitstempel.
	 *
	 * @param conn              die Datenbank-Verbindung
	 * @param listEnmSchueler   die zu importierenden Schüler
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static void importEnmSchueler(final DBEntityManager conn, final List<ENMSchueler> listEnmSchueler) throws ApiOperationException {
		// Bestimme die Schüler-IDs, anhand der zu importierenden Daten
		if (listEnmSchueler.isEmpty())
			return; // nichts zu tun, da keine Daten vorhanden sind
		final List<Long> idsSchueler = listEnmSchueler.stream().map(s -> s.id).distinct().toList();
		if (idsSchueler.size() != listEnmSchueler.size())
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ENM-Daten haben mindestens eine Schüler-ID doppelt enthalten. Dies ist nicht zulässig.");
		final Map<Long, DTOSchueler> mapSchuelerDTOs = conn.queryByKeyList(DTOSchueler.class,
				idsSchueler).stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		if (mapSchuelerDTOs.keySet().size() != idsSchueler.size())
			throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle Schüler in den ENM-Daten konnten auch in der Datenbank gefunden werden.");

		// Bestimme die Schüler-Lernabschnittsdaten anhand der IDs aus den zu importierenden Daten und validiere die Schuljahresabschnitts-ID
		final List<Long> idsLernabschnitte = listEnmSchueler.stream().map(s -> s.lernabschnitt.id).distinct().toList();
		if (idsLernabschnitte.size() != idsSchueler.size())
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ENM-Daten enthalten nicht genügend Lernabschnitte. Dies ist nicht zulässig.");
		final List<DTOSchuelerLernabschnittsdaten> listLernabschnitte = conn.queryByKeyList(DTOSchuelerLernabschnittsdaten.class, idsLernabschnitte);
		if (listLernabschnitte.size() != idsLernabschnitte.size())
			throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle Lernabschnitte aus den ENM-Daten konnten auch in der Datenbank gefunden werden.");
		final Map<Long, DTOSchuelerLernabschnittsdaten> mapLernabschnitte = listLernabschnitte.stream().collect(Collectors.toMap(l -> l.ID, l -> l));

		// Bestimme die Fachbemerkungen zu den Lernabschnitten und erzeuge eine Map für die Abschnitts-ID
		final List<DTOSchuelerPSFachBemerkungen> listLernabschnittsbemerkungen = conn.queryList(DTOSchuelerPSFachBemerkungen.QUERY_LIST_BY_ABSCHNITT_ID,
				DTOSchuelerPSFachBemerkungen.class, idsLernabschnitte);
		final Map<Long, DTOSchuelerPSFachBemerkungen> mapLernabschnittsbemerkungen = listLernabschnittsbemerkungen.isEmpty() ? new HashMap<>()
				: listLernabschnittsbemerkungen.stream().collect(Collectors.toMap(b -> b.Abschnitt_ID, b -> b));

		// Bestimme die Schüler-Ankreuzkompetenzen in der DB anhand der IDs auf dem Import
		final List<ENMSchuelerAnkreuzkompetenz> enmSchuelerAnkreuzkompetenz = listEnmSchueler.stream().flatMap(s -> s.ankreuzkompetenzen.stream()).toList();
		final List<Long> idsSchuelerAnkreuzkompetenz = enmSchuelerAnkreuzkompetenz.stream().map(l -> l.id).toList();
		final List<DTOSchuelerAnkreuzfloskeln> listSchuelerAnkreuzkompetenz = idsSchuelerAnkreuzkompetenz.isEmpty() ? new ArrayList<>()
				: conn.queryByKeyList(DTOSchuelerAnkreuzfloskeln.class, idsSchuelerAnkreuzkompetenz);
		if (listSchuelerAnkreuzkompetenz.size() != enmSchuelerAnkreuzkompetenz.size())
			throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle Ankreuzkompetenzen aus den ENM-Daten konnten auch in der Datenbank gefunden werden.");
		final Map<Long, DTOSchuelerAnkreuzfloskeln> mapSchuelerAnkreuzkompetenz =
				listSchuelerAnkreuzkompetenz.stream().collect(Collectors.toMap(l -> l.ID, l -> l));

		// Bestimme die Schüler-Leistungsdaten in der DB anhand der IDs auf dem Import
		final List<ENMLeistung> enmLeistungen = listEnmSchueler.stream().flatMap(s -> s.leistungsdaten.stream()).toList();
		final List<Long> idsLeistungen = enmLeistungen.stream().map(l -> l.id).toList();
		final List<DTOSchuelerLeistungsdaten> listLeistungen = idsLeistungen.isEmpty() ? new ArrayList<>()
				: conn.queryByKeyList(DTOSchuelerLeistungsdaten.class, idsLeistungen);
		if (listLeistungen.size() != enmLeistungen.size())
			throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle Leistungsdaten aus den ENM-Daten konnten auch in der Datenbank gefunden werden.");
		final Map<Long, DTOSchuelerLeistungsdaten> mapLeistungen = listLeistungen.stream().collect(Collectors.toMap(l -> l.ID, l -> l));

		// Bestimme die alle Teilleistungen von Schülern in der DB anhand des Imports
		final List<ENMTeilleistung> enmTeilleistungen = enmLeistungen.stream().flatMap(l -> l.teilleistungen.stream()).toList();
		final List<Long> idsTeilleistungen = enmTeilleistungen.stream().map(l -> l.id).toList();
		final List<DTOSchuelerTeilleistung> listTeilleistungen = idsTeilleistungen.isEmpty() ? new ArrayList<>()
				: conn.queryByKeyList(DTOSchuelerTeilleistung.class, idsTeilleistungen);
		if (listTeilleistungen.size() != enmTeilleistungen.size())
			throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle Teilleistungen aus den ENM-Daten konnten auch in der Datenbank gefunden werden.");
		final Map<Long, DTOSchuelerTeilleistung> mapTeilleistungen = listTeilleistungen.stream().collect(Collectors.toMap(l -> l.ID, l -> l));

		// Bestimme die ENM-Timestamps für die Lernabschnitte, die Leistungen und die Teilleistungen
		final Map<Long, DTOTimestampsSchuelerLernabschnittsdaten> mapLernabschnitteTimestamps = idsLernabschnitte.isEmpty() ? new HashMap<>()
				: conn.queryByKeyList(DTOTimestampsSchuelerLernabschnittsdaten.class, idsLernabschnitte).stream().collect(Collectors.toMap(l -> l.ID, l -> l));
		final Map<Long, DTOTimestampsSchuelerLeistungsdaten> mapLeistungenTimestamps = idsLeistungen.isEmpty() ? new HashMap<>()
				: conn.queryByKeyList(DTOTimestampsSchuelerLeistungsdaten.class, idsLeistungen).stream().collect(Collectors.toMap(l -> l.ID, l -> l));
		final Map<Long, DTOTimestampsSchuelerTeilleistungen> mapTeilleistungenTimestamps = idsTeilleistungen.isEmpty() ? new HashMap<>()
				: conn.queryByKeyList(DTOTimestampsSchuelerTeilleistungen.class, idsTeilleistungen).stream().collect(Collectors.toMap(t -> t.ID, t -> t));
		final Map<Long, DTOTimestampsSchuelerAnkreuzkompetenzen> mapAnkreuzkompetenzenTimestamps = idsSchuelerAnkreuzkompetenz.isEmpty() ? new HashMap<>()
				: conn.queryByKeyList(DTOTimestampsSchuelerAnkreuzkompetenzen.class, idsSchuelerAnkreuzkompetenz).stream()
						.collect(Collectors.toMap(t -> t.ID, t -> t));

		// Sets, um die veränderten DTOs zwischenzuspeichern und später in einem Rutsch in der DB zu persistieren
		final Set<DTOSchuelerLernabschnittsdaten> setLernabschnitte = new HashSet<>();
		final Set<DTOSchuelerPSFachBemerkungen> setLernabschnittsbemerkungen = new HashSet<>();
		final Set<DTOSchuelerPSFachBemerkungen> setLernabschnittsbemerkungenNeu = new HashSet<>();
		final Set<DTOTimestampsSchuelerLernabschnittsdaten> setLernabschnitteTimestamps = new HashSet<>();
		final Set<DTOSchuelerLeistungsdaten> setLeistungen = new HashSet<>();
		final Set<DTOTimestampsSchuelerLeistungsdaten> setLeistungenTimestamps = new HashSet<>();
		final Set<DTOSchuelerTeilleistung> setTeilleistungen = new HashSet<>();
		final Set<DTOTimestampsSchuelerTeilleistungen> setTeilleistungenTimestamps = new HashSet<>();
		final Set<DTOSchuelerAnkreuzfloskeln> setAnkreuzkompetenzen = new HashSet<>();
		final Set<DTOTimestampsSchuelerAnkreuzkompetenzen> setAnkreuzkompetenzenTimestamps = new HashSet<>();
		long idNeueFachbemerkung = conn.transactionGetNextID(DTOSchuelerPSFachBemerkungen.class);

		// Durchwandere die importierten ENM-Daten und gleiche diese mit den Daten in der Datenbank ab.
		for (final ENMSchueler enmSchueler : listEnmSchueler) {
			final DTOSchuelerLernabschnittsdaten lernabschnitt = mapLernabschnitte.get(enmSchueler.lernabschnitt.id);
			final DTOTimestampsSchuelerLernabschnittsdaten lernabschnittTS = mapLernabschnitteTimestamps.get(enmSchueler.lernabschnitt.id);
			boolean neuBemerkungen = false;
			DTOSchuelerPSFachBemerkungen lernabschnittsbemerkungen = mapLernabschnittsbemerkungen.get(enmSchueler.lernabschnitt.id);
			if (lernabschnittsbemerkungen == null) {
				lernabschnittsbemerkungen = new DTOSchuelerPSFachBemerkungen(idNeueFachbemerkung++, enmSchueler.lernabschnitt.id);
				neuBemerkungen = true;
			}

			boolean updatedBemerkungen = false;
			if (neuBemerkungen || isTimestampAfter(enmSchueler.bemerkungen.tsASV, lernabschnittTS.tsASV)) {
				lernabschnittsbemerkungen.ASV = enmSchueler.bemerkungen.ASV;
				lernabschnittTS.tsASV = enmSchueler.bemerkungen.tsASV;
				updatedBemerkungen = true;
			}
			if (neuBemerkungen || isTimestampAfter(enmSchueler.bemerkungen.tsAUE, lernabschnittTS.tsAUE)) {
				lernabschnittsbemerkungen.AUE = enmSchueler.bemerkungen.AUE;
				lernabschnittTS.tsAUE = enmSchueler.bemerkungen.tsAUE;
				updatedBemerkungen = true;
			}
			if (neuBemerkungen || isTimestampAfter(enmSchueler.bemerkungen.tsLELS, lernabschnittTS.tsLELS)) {
				lernabschnittsbemerkungen.LELS = enmSchueler.bemerkungen.LELS;
				lernabschnittTS.tsLELS = enmSchueler.bemerkungen.tsLELS;
				updatedBemerkungen = true;
			}
			if (neuBemerkungen || isTimestampAfter(enmSchueler.bemerkungen.tsSchulformEmpf, lernabschnittTS.tsESF)) {
				lernabschnittsbemerkungen.ESF = enmSchueler.bemerkungen.schulformEmpf;
				lernabschnittTS.tsESF = enmSchueler.bemerkungen.tsSchulformEmpf;
				updatedBemerkungen = true;
			}
			if (neuBemerkungen || isTimestampAfter(enmSchueler.bemerkungen.tsFoerderbemerkungen, lernabschnittTS.tsBemerkungFSP)) {
				lernabschnittsbemerkungen.BemerkungFSP = enmSchueler.bemerkungen.foerderbemerkungen;
				lernabschnittTS.tsBemerkungFSP = enmSchueler.bemerkungen.tsFoerderbemerkungen;
				updatedBemerkungen = true;
			}
			if (neuBemerkungen || isTimestampAfter(enmSchueler.bemerkungen.tsIndividuelleVersetzungsbemerkungen, lernabschnittTS.tsBemerkungVersetzung)) {
				lernabschnittsbemerkungen.BemerkungVersetzung = enmSchueler.bemerkungen.individuelleVersetzungsbemerkungen;
				lernabschnittTS.tsBemerkungVersetzung = enmSchueler.bemerkungen.tsIndividuelleVersetzungsbemerkungen;
				updatedBemerkungen = true;
			}

			boolean updatedLernabschnitt = false;
			if (isTimestampAfter(enmSchueler.bemerkungen.tsZB, lernabschnittTS.tsZeugnisBem)) {
				lernabschnitt.ZeugnisBem = enmSchueler.bemerkungen.ZB;
				lernabschnittTS.tsZeugnisBem = enmSchueler.bemerkungen.tsZB;
				updatedLernabschnitt = true;
			}
			if (isTimestampAfter(enmSchueler.lernabschnitt.tsFehlstundenGesamt, lernabschnittTS.tsSumFehlStd)) {
				lernabschnitt.SumFehlStd = enmSchueler.lernabschnitt.fehlstundenGesamt;
				lernabschnittTS.tsSumFehlStd = enmSchueler.lernabschnitt.tsFehlstundenGesamt;
				updatedLernabschnitt = true;
			}
			if (isTimestampAfter(enmSchueler.lernabschnitt.tsFehlstundenGesamtUnentschuldigt, lernabschnittTS.tsSumFehlStdU)) {
				lernabschnitt.SumFehlStdU = enmSchueler.lernabschnitt.fehlstundenGesamtUnentschuldigt;
				lernabschnittTS.tsSumFehlStdU = enmSchueler.lernabschnitt.tsFehlstundenGesamtUnentschuldigt;
				updatedLernabschnitt = true;
			}

			if (updatedBemerkungen && !neuBemerkungen)
				setLernabschnittsbemerkungen.add(lernabschnittsbemerkungen);
			if (neuBemerkungen)
				setLernabschnittsbemerkungenNeu.add(lernabschnittsbemerkungen);
			if (updatedLernabschnitt)
				setLernabschnitte.add(lernabschnitt);
			if (updatedBemerkungen || updatedLernabschnitt)
				setLernabschnitteTimestamps.add(lernabschnittTS);

			for (final ENMSchuelerAnkreuzkompetenz enmAnkreuzkompetenz : enmSchueler.ankreuzkompetenzen) {
				final DTOSchuelerAnkreuzfloskeln ankreuzkompetenz = mapSchuelerAnkreuzkompetenz.get(enmAnkreuzkompetenz.id);
				final DTOTimestampsSchuelerAnkreuzkompetenzen ankreuzkompetenzTS = mapAnkreuzkompetenzenTimestamps.get(enmAnkreuzkompetenz.id);

				boolean updatedAnkreuzkompetenz = false;
				if (isTimestampAfter(enmAnkreuzkompetenz.tsStufe, ankreuzkompetenzTS.tsStufe)) {
					ankreuzkompetenz.Stufe1 = enmAnkreuzkompetenz.stufen[0];
					ankreuzkompetenz.Stufe2 = enmAnkreuzkompetenz.stufen[1];
					ankreuzkompetenz.Stufe3 = enmAnkreuzkompetenz.stufen[2];
					ankreuzkompetenz.Stufe4 = enmAnkreuzkompetenz.stufen[3];
					ankreuzkompetenz.Stufe5 = enmAnkreuzkompetenz.stufen[4];
					ankreuzkompetenzTS.tsStufe = enmAnkreuzkompetenz.tsStufe;
					updatedAnkreuzkompetenz = true;
				}

				if (updatedAnkreuzkompetenz) {
					setAnkreuzkompetenzen.add(ankreuzkompetenz);
					setAnkreuzkompetenzenTimestamps.add(ankreuzkompetenzTS);
				}
			}

			for (final ENMLeistung enmLeistung : enmSchueler.leistungsdaten) {
				final DTOSchuelerLeistungsdaten leistung = mapLeistungen.get(enmLeistung.id);
				final DTOTimestampsSchuelerLeistungsdaten leistungTS = mapLeistungenTimestamps.get(enmLeistung.id);

				boolean updatedLeistung = false;
				if (isTimestampAfter(enmLeistung.tsFachbezogeneBemerkungen, leistungTS.tsLernentw)) {
					leistung.Lernentw = enmLeistung.fachbezogeneBemerkungen;
					leistungTS.tsLernentw = enmLeistung.tsFachbezogeneBemerkungen;
					updatedLeistung = true;
				}
				if (isTimestampAfter(enmLeistung.tsFehlstundenFach, leistungTS.tsFehlStd)) {
					leistung.FehlStd = enmLeistung.fehlstundenFach;
					leistungTS.tsFehlStd = enmLeistung.tsFehlstundenFach;
					updatedLeistung = true;
				}
				if (isTimestampAfter(enmLeistung.tsFehlstundenUnentschuldigtFach, leistungTS.tsuFehlStd)) {
					leistung.uFehlStd = enmLeistung.fehlstundenUnentschuldigtFach;
					leistungTS.tsuFehlStd = enmLeistung.tsFehlstundenUnentschuldigtFach;
					updatedLeistung = true;
				}
				if (isTimestampAfter(enmLeistung.tsIstGemahnt, leistungTS.tsWarnung)) {
					leistung.Warnung = enmLeistung.istGemahnt;
					leistungTS.tsWarnung = enmLeistung.tsIstGemahnt;
					updatedLeistung = true;
				}
				if (isTimestampAfter(enmLeistung.tsNote, leistungTS.tsNotenKrz)) {
					leistung.NotenKrz = enmLeistung.note;
					leistungTS.tsNotenKrz = enmLeistung.tsNote;
					updatedLeistung = true;
				}
				if (isTimestampAfter(enmLeistung.tsNoteQuartal, leistungTS.tsNotenKrzQuartal)) {
					leistung.NotenKrzQuartal = enmLeistung.noteQuartal;
					leistungTS.tsNotenKrzQuartal = enmLeistung.tsNoteQuartal;
					updatedLeistung = true;
				}

				if (updatedLeistung) {
					setLeistungen.add(leistung);
					setLeistungenTimestamps.add(leistungTS);
				}

				for (final ENMTeilleistung enmTeilleistung : enmLeistung.teilleistungen) {
					final DTOSchuelerTeilleistung teilleistung = mapTeilleistungen.get(enmTeilleistung.id);
					final DTOTimestampsSchuelerTeilleistungen teilleistungTS = mapTeilleistungenTimestamps.get(enmTeilleistung.id);

					boolean updatedTeilleistung = false;
					if (isTimestampAfter(enmTeilleistung.tsArtID, teilleistungTS.tsArt_ID)) {
						teilleistung.Art_ID = enmTeilleistung.artID;
						teilleistungTS.tsArt_ID = enmTeilleistung.tsArtID;
						updatedTeilleistung = true;
					}
					if (isTimestampAfter(enmTeilleistung.tsDatum, teilleistungTS.tsDatum)) {
						teilleistung.Datum = enmTeilleistung.datum;
						teilleistungTS.tsDatum = enmTeilleistung.tsDatum;
						updatedTeilleistung = true;
					}
					if (isTimestampAfter(enmTeilleistung.tsBemerkung, teilleistungTS.tsBemerkung)) {
						teilleistung.Bemerkung = enmTeilleistung.bemerkung;
						teilleistungTS.tsBemerkung = enmTeilleistung.tsBemerkung;
						updatedTeilleistung = true;
					}
					if (isTimestampAfter(enmTeilleistung.tsNote, teilleistungTS.tsNotenKrz)) {
						teilleistung.NotenKrz = enmTeilleistung.note;
						teilleistungTS.tsNotenKrz = enmTeilleistung.tsNote;
						updatedTeilleistung = true;
					}

					if (updatedTeilleistung) {
						setTeilleistungen.add(teilleistung);
						setTeilleistungenTimestamps.add(teilleistungTS);
					}
				}
			}
		}
		if (!setLernabschnittsbemerkungenNeu.isEmpty())
			conn.transactionPersistAll(setLernabschnittsbemerkungenNeu);
		if (!setLernabschnittsbemerkungen.isEmpty())
			conn.transactionPersistAll(setLernabschnittsbemerkungen);
		if (!setLernabschnitte.isEmpty())
			conn.transactionPersistAll(setLernabschnitte);
		if (!setLeistungen.isEmpty())
			conn.transactionPersistAll(setLeistungen);
		if (!setTeilleistungen.isEmpty())
			conn.transactionPersistAll(setTeilleistungen);
		if (!setAnkreuzkompetenzen.isEmpty())
			conn.transactionPersistAll(setAnkreuzkompetenzen);
		conn.transactionFlush();

		if (!setLernabschnitteTimestamps.isEmpty())
			conn.transactionPersistAll(setLernabschnitteTimestamps);
		if (!setLeistungenTimestamps.isEmpty())
			conn.transactionPersistAll(setLeistungenTimestamps);
		if (!setTeilleistungenTimestamps.isEmpty())
			conn.transactionPersistAll(setTeilleistungenTimestamps);
		if (!setAnkreuzkompetenzenTimestamps.isEmpty())
			conn.transactionPersistAll(setAnkreuzkompetenzenTimestamps);
		conn.transactionFlush();
	}


	/**
	 * Wandelt die im ISO-Format übergeben Zeitangabe in einen Timestamp um.
	 *
	 * @param iso   der ISO-String
	 *
	 * @return der Timestamp oder null, falls der ISO-String ungültig ist
	 */
	private static Timestamp getTimeStampFromIso(final String iso) {
		if ((iso == null) || iso.isBlank())
			return null;
		return Timestamp.valueOf(LocalDateTime.parse(iso, ofPattern));
	}


	/**
	 * Gibt den aktuellen Zeitpunkt als Timestamp zurück.
	 *
	 * @return der aktuelle Timestamp
	 */
	private static Timestamp getTimeStampNow() {
		return Timestamp.valueOf(LocalDateTime.now());
	}


	/**
	 * Prüft, ob der gegebene Timestamp-String tsCheckStr nach dem Timestamp-String tsOtherStr
	 * liegt.
	 *
	 * @param tsCheckStr   der zu prüfende Timestamp-String
	 * @param tsOtherStr   der andere Timestamp-String
	 *
	 * @return true, wenn tsCheckStr nach tsOtherStr liegt
	 */
	private static boolean isTimestampAfter(final String tsCheckStr, final String tsOtherStr) {
		final Timestamp tsCheck = getTimeStampFromIso(tsCheckStr);
		if (tsCheck == null)
			return false;
		final Timestamp tsOther = getTimeStampFromIso(tsOtherStr);
		if (tsOther == null)
			return true;
		return tsCheck.after(tsOther);
	}

}
