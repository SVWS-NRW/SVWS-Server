package de.svws_nrw.data.enm;

import java.io.InputStream;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.base.crypto.Passwords;
import de.svws_nrw.core.data.enm.ENMAnkreuzkompetenz;
import de.svws_nrw.core.data.enm.ENMDaten;
import de.svws_nrw.core.data.enm.ENMFach;
import de.svws_nrw.core.data.enm.ENMFloskel;
import de.svws_nrw.core.data.enm.ENMFloskelgruppe;
import de.svws_nrw.core.data.enm.ENMJahrgang;
import de.svws_nrw.core.data.enm.ENMKlasse;
import de.svws_nrw.core.data.enm.ENMLehrer;
import de.svws_nrw.core.data.enm.ENMLeistung;
import de.svws_nrw.core.data.enm.ENMLerngruppe;
import de.svws_nrw.core.data.enm.ENMSchueler;
import de.svws_nrw.core.data.enm.ENMSchuelerAnkreuzkompetenz;
import de.svws_nrw.core.data.enm.ENMTeilleistung;
import de.svws_nrw.core.data.enm.ENMTeilleistungsart;
import de.svws_nrw.asd.data.kurse.ZulaessigeKursartKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.core.types.oauth2.OAuth2ServerTyp;
import de.svws_nrw.core.utils.enm.ENMDatenManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.oauth2.DataOauthClientSecrets;
import de.svws_nrw.data.oauth2.OAuth2Client;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.lehrer.DTOLehrerNotenmodulCredentials;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzdaten;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFloskelgruppen;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFloskeln;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerTeilleistung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOTeilleistungsarten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.svws.auth.DTOSchuleOAuthSecrets;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsLehrerNotenmodulCredentials;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerAnkreuzkompetenzen;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerTeilleistungen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.db.utils.dto.enm.DTOENMLehrerSchuelerAbschnittsdaten;
import de.svws_nrw.ext.jbcrypt.BCrypt;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link ENMDaten}.
 */
public final class DataENMDaten extends DataManager<Long> {

	private static final String ENM_UPLOAD_PATH = "/api/secure/import";
	private static final String ENM_DOWNLOAD_PATH = "/api/secure/export";
	private static final String ENM_TRUNCATE_PATH = "/api/secure/truncate";


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
		final Map<Long, DTOSchueler> mapSchueler = getSchuelerListe(conn, abschnitt);
		final Map<Long, DTOFach> mapFaecher = getFaecherListe(conn);
		final Map<Long, DTOJahrgang> mapJahrgaenge = getJahrgangsListe(conn);
		final Map<String, DTOKlassen> mapKlassen = getKlassenListe(conn, abschnitt);
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
		final List<DTOENMLehrerSchuelerAbschnittsdaten> schuelerabschnitte = (dtoLehrer == null)
				? DTOENMLehrerSchuelerAbschnittsdaten.queryAll(conn, abschnitt.id)
				: DTOENMLehrerSchuelerAbschnittsdaten.query(conn, abschnitt.id, dtoLehrer.Kuerzel);
		// Bestimme zusätzlich eine Übersicht zu den Einzelleistungen zu den Leistungsdaten
		final List<Long> idsLeistungen = schuelerabschnitte.stream().map(a -> a.leistungID).toList();
		final List<DTOSchuelerTeilleistung> listTeilleistungen = idsLeistungen.isEmpty() ? new ArrayList<>()
				: conn.queryList(DTOSchuelerTeilleistung.QUERY_LIST_BY_LEISTUNG_ID, DTOSchuelerTeilleistung.class, idsLeistungen);
		final Map<Long, List<DTOSchuelerTeilleistung>> mapTeilleistungen = listTeilleistungen.stream().collect(Collectors.groupingBy(st -> st.Leistung_ID));
		final List<Long> idsTeilleistungen = listTeilleistungen.stream().map(t -> t.ID).toList();
		final Map<Long, DTOTimestampsSchuelerTeilleistungen> mapTeilleistungenTimestamps = idsTeilleistungen.isEmpty() ? new HashMap<>()
				: conn.queryByKeyList(DTOTimestampsSchuelerTeilleistungen.class, idsTeilleistungen).stream().collect(Collectors.toMap(t -> t.ID, t -> t));
		// Bestimme zusätzlich eine Übersicht zu den Ankreuzkompetenzen
		final List<Long> idsLernabschnitte = schuelerabschnitte.stream().map(a -> a.abschnittID).distinct().toList();
		final List<DTOSchuelerAnkreuzfloskeln> listAnkreuzkompetenzen = idsLernabschnitte.isEmpty() ? new ArrayList<>()
				: conn.queryList(DTOSchuelerAnkreuzfloskeln.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerAnkreuzfloskeln.class, idsLernabschnitte);
		final Map<Long, List<DTOSchuelerAnkreuzfloskeln>> mapAnkreuzkompetenzen =
				listAnkreuzkompetenzen.stream().collect(Collectors.groupingBy(a -> a.Abschnitt_ID));
		final List<Long> idsAnkreuzkompetenzen = listAnkreuzkompetenzen.stream().map(t -> t.ID).toList();
		final Map<Long, DTOTimestampsSchuelerAnkreuzkompetenzen> mapAnkreuzkompetenzenTimestamps = idsAnkreuzkompetenzen.isEmpty() ? new HashMap<>()
				: conn.queryByKeyList(DTOTimestampsSchuelerAnkreuzkompetenzen.class, idsAnkreuzkompetenzen).stream()
						.collect(Collectors.toMap(t -> t.ID, t -> t));
		// Durchwandere die Lernabschnitt der Schüler...
		for (final DTOENMLehrerSchuelerAbschnittsdaten schuelerabschnitt : schuelerabschnitte) {
			final DTOKlassen dtoKlasse = mapKlassen.get(schuelerabschnitt.klasse);
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

			final ZulaessigeKursart kursart = (schuelerabschnitt.kursID == null) ? null : ZulaessigeKursart.data().getWertByKuerzel(schuelerabschnitt.kursart);

			ENMSchueler enmSchueler = manager.getSchueler(schuelerabschnitt.schuelerID);
			if (enmSchueler == null) {
				final var dtoSchueler = mapSchueler.get(schuelerabschnitt.schuelerID);
				ENMJahrgang enmJahrgang = manager.getJahrgang(schuelerabschnitt.jahrgangID);
				if (enmJahrgang == null) {
					final DTOJahrgang dtoJahrgang = mapJahrgaenge.get(schuelerabschnitt.jahrgangID);
					if (dtoJahrgang == null)
						throw new NullPointerException();
					manager.addJahrgang(dtoJahrgang.ID, dtoJahrgang.ASDJahrgang, dtoJahrgang.InternKrz,
							dtoJahrgang.ASDBezeichnung, dtoJahrgang.Sekundarstufe, dtoJahrgang.Sortierung);
					enmJahrgang = manager.getJahrgang(schuelerabschnitt.jahrgangID);
				}

				manager.addSchueler(dtoSchueler.ID, schuelerabschnitt.jahrgangID, dtoKlasse.ID, dtoSchueler.Nachname, dtoSchueler.Vorname,
						dtoSchueler.Geschlecht, schuelerabschnitt.BilingualerZweig, schuelerabschnitt.ZieldifferentesLernen,
						dtoSchueler.Lernstandsbericht);  // Deutsch als Fremdsprache liegt vor, wenn für den Schüler Lernstandsberichte geschrieben werden...
				enmSchueler = manager.getSchueler(schuelerabschnitt.schuelerID);
				enmSchueler.lernabschnitt.id = schuelerabschnitt.abschnittID;
				enmSchueler.lernabschnitt.fehlstundenGesamt = schuelerabschnitt.fehlstundenSummeGesamt;
				enmSchueler.lernabschnitt.tsFehlstundenGesamt = schuelerabschnitt.tsFehlstundenSummeGesamt;
				enmSchueler.lernabschnitt.fehlstundenGesamtUnentschuldigt = schuelerabschnitt.fehlstundenSummeUnentschuldigt;
				enmSchueler.lernabschnitt.tsFehlstundenGesamtUnentschuldigt = schuelerabschnitt.tsFehlstundenSummeUnentschuldigt;
				enmSchueler.lernabschnitt.pruefungsordnung = schuelerabschnitt.pruefungsordnung;
				enmSchueler.lernabschnitt.lernbereich1note = (schuelerabschnitt.lernbereich1notenKuerzel == null)
						|| schuelerabschnitt.lernbereich1notenKuerzel.isBlank()
								? null : schuelerabschnitt.lernbereich1notenKuerzel;
				enmSchueler.lernabschnitt.lernbereich2note = (schuelerabschnitt.lernbereich2notenKuerzel == null)
						|| schuelerabschnitt.lernbereich2notenKuerzel.isBlank()
								? null : schuelerabschnitt.lernbereich2notenKuerzel;
				enmSchueler.lernabschnitt.foerderschwerpunkt1 = schuelerabschnitt.foerderschwerpunkt1Kuerzel;
				enmSchueler.lernabschnitt.foerderschwerpunkt2 = schuelerabschnitt.foerderschwerpunkt2Kuerzel;
				enmSchueler.bemerkungen.ASV = schuelerabschnitt.ASV;
				enmSchueler.bemerkungen.tsASV = schuelerabschnitt.tsASV;
				enmSchueler.bemerkungen.AUE = schuelerabschnitt.AUE;
				enmSchueler.bemerkungen.tsAUE = schuelerabschnitt.tsAUE;
				enmSchueler.bemerkungen.ZB = schuelerabschnitt.zeugnisBemerkungen;
				enmSchueler.bemerkungen.tsZB = schuelerabschnitt.tsZeugnisBemerkungen;
				enmSchueler.bemerkungen.LELS = schuelerabschnitt.LELS;
				enmSchueler.bemerkungen.tsLELS = schuelerabschnitt.tsLELS;
				enmSchueler.bemerkungen.schulformEmpf = schuelerabschnitt.ESF;
				enmSchueler.bemerkungen.tsSchulformEmpf = schuelerabschnitt.tsESF;
				enmSchueler.bemerkungen.individuelleVersetzungsbemerkungen = schuelerabschnitt.bemerkungVersetzung;
				enmSchueler.bemerkungen.tsIndividuelleVersetzungsbemerkungen = schuelerabschnitt.tsBemerkungVersetzung;
				enmSchueler.bemerkungen.foerderbemerkungen = schuelerabschnitt.bemerkungFSP;
				enmSchueler.bemerkungen.tsFoerderbemerkungen = schuelerabschnitt.tsBemerkungFSP;

				// Ankreuzkompetenzen hinzufügen und deren Katalog-Einträge hinzufügen
				final List<DTOSchuelerAnkreuzfloskeln> ankreuzkompetenzen = mapAnkreuzkompetenzen.get(schuelerabschnitt.abschnittID);
				if (ankreuzkompetenzen != null) {
					for (final DTOSchuelerAnkreuzfloskeln ankreuzkompetenz : ankreuzkompetenzen) {
						// Prüfe die Teilleistungsart und ergänze sie ggf.
						final ENMAnkreuzkompetenz enmAnkreuzkompetenz = manager.getAnkreuzkompetenz(ankreuzkompetenz.Floskel_ID);
						if (enmAnkreuzkompetenz == null) {
							final DTOAnkreuzfloskeln dtoAnkreuzkompetenz = mapKatalogAnkreuzkompetenzen.get(ankreuzkompetenz.Floskel_ID);
							if (dtoAnkreuzkompetenz == null) // DB-Error -> should not happen
								throw new NullPointerException();
							manager.addAnkreuzkompetenz(dtoAnkreuzkompetenz.ID, (dtoAnkreuzkompetenz.IstASV == 0), dtoAnkreuzkompetenz.Fach_ID,
									dtoAnkreuzkompetenz.Jahrgang, dtoAnkreuzkompetenz.FloskelText, dtoAnkreuzkompetenz.Sortierung);
						}
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

			// Hier wird die temporäre LerngruppenID erstellt, mit der in der Klasse ENMDaten gearbeitet wird.
			// Es wird eine eindeutige ID benötigt, die Kurs- und Klassenübergreifend diese Lerngruppe identifiziert.
			final String strLerngruppenID = (schuelerabschnitt.kursID == null)
					? ("Klasse:" + schuelerabschnitt.klasse + "/" + schuelerabschnitt.fachID)
					: ("Kurs:" + schuelerabschnitt.kursID);
			ENMLerngruppe lerngruppe = manager.getLerngruppe(strLerngruppenID);
			if (lerngruppe == null) {
				final DTOFach fach = mapFaecher.get(schuelerabschnitt.fachID);
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
				if (schuelerabschnitt.kursID == null) {  // es ist eine Klasse
					manager.addLerngruppe(strLerngruppenID, dtoKlasse.ID, schuelerabschnitt.fachID, null,
							fach.Kuerzel, kursartAllg, fach.Unterichtssprache,
							(schuelerabschnitt.wochenstunden == null) ? 0 : schuelerabschnitt.wochenstunden);
				} else {  // es ist ein Kurs
					final DTOKurs kurs = mapKurse.get(schuelerabschnitt.kursID);
					manager.addLerngruppe(strLerngruppenID, schuelerabschnitt.kursID, schuelerabschnitt.fachID,
							(kursart == null) ? -1 : Integer.parseInt(kursart.daten(abschnitt.schuljahr).nummer), kurs.KurzBez, kursartAllg,
							fach.Unterichtssprache, kurs.WochenStd);
				}
				lerngruppe = manager.getLerngruppe(strLerngruppenID);
				lerngruppe.lehrerID.add(schuelerabschnitt.lehrerID);
				if (manager.getLehrer(schuelerabschnitt.lehrerID) == null) {
					final DTOLehrer dtoFachlehrer = mapLehrer.get(schuelerabschnitt.lehrerID);
					if (dtoFachlehrer != null) {
						final String creds = (dtoLehrer == null) ? mapLehrerPWHash.get(dtoFachlehrer.ID) : "";
						final String tsCreds = (dtoLehrer == null) ? mapLehrerPWHashTimestamps.get(dtoFachlehrer.ID) : null;
						manager.addLehrer(dtoFachlehrer.ID, dtoFachlehrer.Kuerzel, dtoFachlehrer.Nachname, dtoFachlehrer.Vorname,
								dtoFachlehrer.Geschlecht, dtoFachlehrer.eMailDienstlich,
								(creds == null) ? "" : creds, (tsCreds == null) ? null : tsCreds);
					}
				}
				// TODO ggf. im Team-Teaching unterrichtende Lehrer hinzufügen (Zusatzkraft in Leistungsdaten bzw. weitere Lehrkraft bei Kursen)
			}

			final int halbjahr = abschnitt.abschnitt;
			final boolean istSchriftlich = (schuelerabschnitt.kursID != null)
					&& ((kursart == ZulaessigeKursart.LK1) || (kursart == ZulaessigeKursart.LK2)
							|| (kursart == ZulaessigeKursart.GKS) || (kursart == ZulaessigeKursart.AB3)
							|| ((kursart == ZulaessigeKursart.AB4) && (!("Q2".equals(dtoKlasse.ASDKlasse) && (halbjahr == 2)))));

			final String tmpAbiFach = schuelerabschnitt.AbiturFach;
			final Integer abiFach = (tmpAbiFach == null) ? null : switch (tmpAbiFach) {
				case "1" -> 1;
				case "2" -> 2;
				case "3" -> 3;
				case "4" -> 4;
				default -> null;
			};
			final boolean istGemahnt = (schuelerabschnitt.istGemahnt != null) && schuelerabschnitt.istGemahnt;
			final String mahndatum = schuelerabschnitt.mahndatum;
			final ENMLeistung enmLeistung = manager.addSchuelerLeistungsdaten(enmSchueler,
					schuelerabschnitt.leistungID, lerngruppe.id, schuelerabschnitt.noteKuerzel, schuelerabschnitt.tsNote,
					schuelerabschnitt.noteKuerzelQuartal, schuelerabschnitt.tsNoteQuartal, istSchriftlich, abiFach,
					schuelerabschnitt.fehlstundenGesamt, schuelerabschnitt.tsFehlstundenGesamt,
					schuelerabschnitt.fehlstundenUnentschuldigt, schuelerabschnitt.tsFehlstundenUnentschuldigt,
					schuelerabschnitt.fachbezogeneBemerkungen, schuelerabschnitt.tsFachbezogeneBemerkungen, null,
					istGemahnt, schuelerabschnitt.tsIstGemahnt, mahndatum);

			// Teilleistungen und deren Arten hinzufügen
			final List<DTOSchuelerTeilleistung> teilleistungen = mapTeilleistungen.get(schuelerabschnitt.leistungID);
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

		// Kopiere den Floskel-Katalog in die ENM-Daten
		getFloskeln(conn, manager, mapJahrgaenge);

		return manager.daten;
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
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
		final List<DTOLehrerNotenmodulCredentials> lehrer = conn.queryAll(DTOLehrerNotenmodulCredentials.class);
		if (lehrer.isEmpty())
			return new HashMap<>();
		return lehrer.stream().collect(Collectors.toMap(e -> e.Lehrer_ID, e -> e.PasswordHash));
	}

	private static Map<Long, String> getLehrerCredsTimstampsListe(final DBEntityManager conn) {
		final List<DTOTimestampsLehrerNotenmodulCredentials> lehrer = conn.queryAll(DTOTimestampsLehrerNotenmodulCredentials.class);
		if (lehrer.isEmpty())
			return new HashMap<>();
		return lehrer.stream().collect(Collectors.toMap(e -> e.Lehrer_ID, e -> e.tsPasswordHash));
	}

	private static Map<Long, DTOAnkreuzfloskeln> getAnkreuzkompetenzenListe(final DBEntityManager conn) {
		final List<DTOAnkreuzfloskeln> ankreuzkompetenzen = conn.queryAll(DTOAnkreuzfloskeln.class);
		if (ankreuzkompetenzen.isEmpty())
			return new HashMap<>();
		return ankreuzkompetenzen.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
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

	private static Map<String, DTOKlassen> getKlassenListe(final DBEntityManager conn, final Schuljahresabschnitt abschnitt) throws ApiOperationException {
		final List<DTOKlassen> klassen = conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, abschnitt.id);
		if (klassen.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return klassen.stream().collect(Collectors.toMap(e -> e.Klasse, e -> e));
	}

	private static Map<Long, List<DTOKlassenLeitung>> getKlassenleitungen(final DBEntityManager conn, final Map<String, DTOKlassen> mapKlassen) {
		final List<Long> klassenIDs = mapKlassen.values().stream().map(kl -> kl.ID).toList();
		return conn.queryList(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID, DTOKlassenLeitung.class, klassenIDs).stream()
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
	 * @param mapJahrgaenge   eine Map mit den jeweiligen Jahrgängen
	 */
	private static void getFloskeln(final DBEntityManager conn, final ENMDatenManager manager, final Map<Long, DTOJahrgang> mapJahrgaenge) {
		final Map<String, List<DTOJahrgang>> mapJG = mapJahrgaenge.values().stream().collect(Collectors.groupingBy(j -> j.ASDJahrgang));
		final List<DTOFloskelgruppen> dtoFloskelgruppen = conn.queryAll(DTOFloskelgruppen.class);
		final HashMap<String, ENMFloskelgruppe> map = new HashMap<>();
		for (final DTOFloskelgruppen dto : dtoFloskelgruppen) {
			final ENMFloskelgruppe enmFG = new ENMFloskelgruppe();
			enmFG.kuerzel = dto.Kuerzel;
			enmFG.bezeichnung = dto.Bezeichnung;
			enmFG.hauptgruppe = dto.Hauptgruppe;
			map.put(enmFG.kuerzel, enmFG);
			manager.daten.floskelgruppen.add(enmFG);
		}
		final List<DTOFloskeln> dtoFloskeln = conn.queryAll(DTOFloskeln.class);
		for (final DTOFloskeln dto : dtoFloskeln) {
			final ENMFloskelgruppe enmFG = map.get(dto.FloskelGruppe);
			if (enmFG == null) // TODO alternativ Fehlerbehandlung: Wie ordnet man die Floskel zu? -> allgemein ?
				continue;
			final ENMFach fach = manager.getFachByKuerzel(dto.FloskelFach);
			final List<DTOJahrgang> jahrgaenge = mapJG.get(dto.FloskelJahrgang);
			Long niveau;
			try {
				niveau = Long.parseLong(dto.FloskelNiveau);
			} catch (@SuppressWarnings("unused") final NumberFormatException e) {
				niveau = null;
			}
			if ((jahrgaenge == null) || jahrgaenge.isEmpty()) {
				final ENMFloskel enmFl = new ENMFloskel();
				enmFl.kuerzel = dto.Kuerzel;
				enmFl.text = dto.FloskelText;
				enmFl.fachID = (fach == null) ? null : fach.id;
				enmFl.niveau = niveau;
				enmFl.jahrgangID = null;
				enmFG.floskeln.add(enmFl);
			} else {
				for (final DTOJahrgang jg : jahrgaenge) {
					final ENMFloskel enmFl = new ENMFloskel();
					enmFl.kuerzel = dto.Kuerzel;
					enmFl.text = dto.FloskelText;
					enmFl.fachID = (fach == null) ? null : fach.id;
					enmFl.niveau = niveau;
					enmFl.jahrgangID = jg.ID;
					enmFG.floskeln.add(enmFl);
				}
			}
		}
	}


	/**
	 * Erstellt für alle Lehrer initiale Credentials, sofern ein Lehrer nicht bereits welche besitzt.
	 *
	 * @param conn   die Datenbankverbindung
	 */
	public static void generateInitialCredentials(final DBEntityManager conn) {
		// Prüfe zunächst die existierenden Credentials auf Vollständigkeit
		final List<DTOLehrerNotenmodulCredentials> existing = conn.queryAll(DTOLehrerNotenmodulCredentials.class);
		for (final DTOLehrerNotenmodulCredentials cred : existing) {
			final boolean hasInitial = (cred.Initialkennwort != null) && (!cred.Initialkennwort.isBlank());
			final boolean hasHash = (cred.PasswordHash != null) && (!cred.PasswordHash.isBlank());
			if (hasInitial && hasHash)
				continue;
			if (!hasInitial)
				cred.Initialkennwort = Passwords.generateRandomPasswordWithoutSpecialChars(10);
			if (!hasHash)
				cred.PasswordHash = BCrypt.hashpw(cred.Initialkennwort, BCrypt.gensalt());
			conn.transactionPersist(cred);
		}
		// Erstelle dann die noch fehlenden Credentials
		final Set<Long> idsExisting = existing.stream().map(c -> c.Lehrer_ID).collect(Collectors.toUnmodifiableSet());
		final List<Long> ids = conn.queryAll(DTOLehrer.class).stream().map(l -> l.ID).filter(l -> !idsExisting.contains(l)).toList();
		for (final long id : ids) {
			final String initial = Passwords.generateRandomPasswordWithoutSpecialChars(10);
			final String hash = BCrypt.hashpw(initial, BCrypt.gensalt());
			conn.transactionPersist(new DTOLehrerNotenmodulCredentials(id, initial, hash));
		}
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
		DTOLehrerNotenmodulCredentials cred = conn.queryByKey(DTOLehrerNotenmodulCredentials.class, idLehrer);
		if (cred == null) {
			final String initial = Passwords.generateRandomPasswordWithoutSpecialChars(10);
			final String hash = BCrypt.hashpw(initial, BCrypt.gensalt());
			cred = new DTOLehrerNotenmodulCredentials(idLehrer, initial, hash);
		} else {
			final boolean hasInitial = (cred.Initialkennwort != null) && (!cred.Initialkennwort.isBlank());
			if (!hasInitial)
				cred.Initialkennwort = Passwords.generateRandomPasswordWithoutSpecialChars(10);
			cred.PasswordHash = BCrypt.hashpw(cred.Initialkennwort, BCrypt.gensalt());
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
		DTOLehrerNotenmodulCredentials cred = conn.queryByKey(DTOLehrerNotenmodulCredentials.class, idLehrer);
		final String hash = BCrypt.hashpw(password, BCrypt.gensalt());
		if (cred == null) {
			final String initial = Passwords.generateRandomPasswordWithoutSpecialChars(10);
			cred = new DTOLehrerNotenmodulCredentials(idLehrer, initial, hash);
		} else {
			final boolean hasInitial = (cred.Initialkennwort != null) && (!cred.Initialkennwort.isBlank());
			if (!hasInitial)
				cred.Initialkennwort = Passwords.generateRandomPasswordWithoutSpecialChars(10);
			cred.PasswordHash = hash;
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
		final Map<Long, DTOLehrerNotenmodulCredentials> mapLehrerCreds =
				conn.queryByKeyList(DTOLehrerNotenmodulCredentials.class, idsLehrer).stream().collect(Collectors.toMap(c -> c.Lehrer_ID, c -> c));
		final Map<Long, DTOTimestampsLehrerNotenmodulCredentials> mapLehrerCredsTimestamps =
				conn.queryByKeyList(DTOTimestampsLehrerNotenmodulCredentials.class, idsLehrer).stream().collect(Collectors.toMap(t -> t.Lehrer_ID, t -> t));
		// Gehe die einzelnen Lehrer durch und aktualisieren ggf. die Credentials
		for (final ENMLehrer enmLehrer : listEnmLehrer) {
			final DTOLehrer dtoLehrer = mapLehrerDTOs.get(enmLehrer.id);
			if (dtoLehrer == null)
				throw new ApiOperationException(Status.NOT_FOUND,
						"Der Lehrer in den ENM-Daten mit der ID %d konnte in der Datenbank nicht gefunden werden.".formatted(enmLehrer.id));
			DTOLehrerNotenmodulCredentials cred = mapLehrerCreds.get(enmLehrer.id);
			final DTOTimestampsLehrerNotenmodulCredentials credTS = mapLehrerCredsTimestamps.get(enmLehrer.id);
			if (isTimestampAfter(enmLehrer.tsPasswordHash, credTS == null ? null : credTS.tsPasswordHash)) {
				if (cred == null)
					cred = new DTOLehrerNotenmodulCredentials(enmLehrer.id, "", enmLehrer.passwordHash);
				else
					cred.PasswordHash = enmLehrer.passwordHash;
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
	 * Prüft, ob der gegebene Timestamp-String tsCheckStr nach dem Timestamp-String tsOtherStr
	 * liegt.
	 *
	 * @param tsCheckStr   der zu prüfende Timestamp-String
	 * @param tsOtherStr   der andere Timestamp-String
	 *
	 * @return true, wenn tsCheckStr nach tsOtherStr liegt
	 */
	private static boolean isTimestampAfter(final String tsCheckStr, final String tsOtherStr) {
		if ((tsCheckStr == null) || tsCheckStr.isBlank())
			return false;
		final DateTimeFormatter ofPattern = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss")
				.appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true).toFormatter();
		final Timestamp tsCheck = Timestamp.valueOf(LocalDateTime.parse(tsCheckStr, ofPattern));
		if ((tsOtherStr == null) || tsOtherStr.isBlank())
			return true;
		final Timestamp tsOther = Timestamp.valueOf(LocalDateTime.parse(tsOtherStr, ofPattern));
		return tsCheck.after(tsOther);
	}


	/**
	 * Erstellt mit einer gegebenen Datenbankverbindung einen {@link OAuthClient}
	 *
	 * @param conn die Datenbankverbindung
	 *
	 * @return der OAuthClient
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static OAuth2Client getWenomOAuthClient(final DBEntityManager conn) throws ApiOperationException {
		final DTOSchuleOAuthSecrets dto = new DataOauthClientSecrets(conn).getDto(OAuth2ServerTyp.WENOM);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return OAuth2Client.getClient(dto);
	}


	/**
	 * Lädt die ENM-Daten über den gegebenen OAuthClient vom ENM-Server und mit dem gegebenen DataManager in die
	 * Datenbank
	 *
	 * @param conn    die Datenbank-Verbindung
	 * @param client  der OAuthClient
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static void downloadENMDaten(final DBEntityManager conn, final OAuth2Client client) throws ApiOperationException {
		final HttpResponse<byte[]> httpResponse = client.get(ENM_DOWNLOAD_PATH, BodyHandlers.ofByteArray());
		if (httpResponse.statusCode() != Status.OK.getStatusCode()) {
			throw new ApiOperationException(Status.BAD_GATEWAY, httpResponse.body());
		}
		importDatenGZip(conn, httpResponse.body());
	}


	/**
	 * Lädt die ENM-Daten beim ENM-Server hoch
	 *
	 * @param conn         die Datenbank-Verbindung
	 * @param client       der OAuth-Client zur Verbindung mit dem ENM
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static void uploadENMDaten(final DBEntityManager conn, final OAuth2Client client) throws ApiOperationException {
		final byte[] daten = getAllGZIPBytes(conn);
		final HttpResponse<String> response = client.postMultipart(ENM_UPLOAD_PATH, "json.gz", daten,
				BodyHandlers.ofString());
		if (response.statusCode() != Status.OK.getStatusCode()) {
			throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
		}
	}


	/**
	 * Synchronisiert die Daten des Externen Notenmoduls (ENM) mit dem WeNoM-Server und lädt
	 * dabei diese als ZIP beim ENM hoch und anschließend wieder von diesem herunter und speichert
	 * diese in der Datenbank.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response synchronize(final DBEntityManager conn) throws ApiOperationException {
		final OAuth2Client client = getWenomOAuthClient(conn);
		uploadENMDaten(conn, client);
		downloadENMDaten(conn, client);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(Boolean.TRUE).build();
	}


	/**
	 * Lädt die ENM-Daten aus der Datenbank zu dem WeNoM-Server hoch.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response upload(final DBEntityManager conn) throws ApiOperationException {
		final OAuth2Client client = getWenomOAuthClient(conn);
		uploadENMDaten(conn, client);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(Boolean.TRUE).build();
	}


	/**
	 * Importiert die ENM-Daten von dem WeNoM-Server und schreibt diese in die Datenbank.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response download(final DBEntityManager conn) throws ApiOperationException {
		final OAuth2Client client = getWenomOAuthClient(conn);
		downloadENMDaten(conn, client);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(Boolean.TRUE).build();
	}


	/**
	 * Entfernt die ENM-Daten von dem WeNoM-Server.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response truncate(final DBEntityManager conn) throws ApiOperationException {
		final OAuth2Client client = getWenomOAuthClient(conn);
		final HttpResponse<String> response = client.postEmpty(ENM_TRUNCATE_PATH, BodyHandlers.ofString());
		if (response.statusCode() != Status.OK.getStatusCode())
			throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(Boolean.TRUE).build();
	}

}
