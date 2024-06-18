package de.svws_nrw.data.datenaustausch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import de.svws_nrw.base.untis.UntisGPU001;
import de.svws_nrw.base.untis.UntisGPU002;
import de.svws_nrw.base.untis.UntisGPU005;
import de.svws_nrw.base.untis.UntisGPU010;
import de.svws_nrw.base.untis.UntisGPU015;
import de.svws_nrw.base.untis.UntisGPU019;
import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.RGBFarbe;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintragMinimal;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.data.SimpleBinaryMultipartBody;
import de.svws_nrw.data.faecher.DataFaecherliste;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.data.kataloge.DataKatalogRaeume;
import de.svws_nrw.data.kataloge.DataKatalogZeitraster;
import de.svws_nrw.data.kurse.DataKursliste;
import de.svws_nrw.data.lehrer.DataLehrerliste;
import de.svws_nrw.data.schule.DataSchuljahresabschnitte;
import de.svws_nrw.data.stundenplan.DataStundenplanRaeume;
import de.svws_nrw.data.stundenplan.DataStundenplanSchienen;
import de.svws_nrw.data.stundenplan.DataStundenplanZeitraster;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogRaum;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterricht;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtKlasse;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtLehrer;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtRaum;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtSchiene;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse stellt Methoden für den Import und Export von Untis-Daten
 * zur Vefügung.
 */
public final class DataUntis {

	private DataUntis() {
		throw new IllegalStateException("Instantiation of " + DataUntis.class.getName() + " not allowed");
	}

	private static void _importGPU001(final Logger logger, final DBEntityManager conn, final long idSchuljahresabschnitt,
			final String beginn, final String beschreibung, final List<UntisGPU001> unterrichte, final int wochentyp, final boolean ignoreMissing)
			throws ApiOperationException {
		// Prüfe die ID des Schuljahreabschnitts
		logger.logLn("-> Prüfe, ob der Schuljahresabschnitt existiert... ");
		final Schuljahresabschnitt schuljahresabschnitt = DataSchuljahresabschnitte.getByID(conn, idSchuljahresabschnitt);
		if (schuljahresabschnitt == null) {
			logger.logLn(2, "[Fehler] - Die ID des Schuljahresabschnitt %d ist ungültig.".formatted(idSchuljahresabschnitt));
			throw new ApiOperationException(Status.NOT_FOUND, "Die ID des Schuljahresabschnitt %d ist ungültig.".formatted(idSchuljahresabschnitt));
		}
		// Bestimme die Lehrer
		final Map<String, LehrerListeEintrag> mapLehrerByKuerzel = DataLehrerliste.getLehrerListe(conn).stream()
				.collect(Collectors.toMap(l -> l.kuerzel, l -> l));
		// Bestimme die Fächer
		final Map<String, FachDaten> mapFaecherByKuerzel = DataFaecherliste.getFaecherListe(conn).stream().collect(Collectors.toMap(f -> f.kuerzel, f -> f));
		// Bestimme die Klassen des Schuljahresabschnitts
		final List<DTOKlassen> klassen = conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, schuljahresabschnitt.id);
		final Map<String, DTOKlassen> mapKlassenByKuerzel = klassen.stream().collect(Collectors.toMap(k -> k.Klasse, k -> k));
		// Bestimme die Kurse des Schuljahresabschnitts
		final List<KursDaten> kurse = DataKursliste.getKursListenFuerAbschnitt(conn, idSchuljahresabschnitt, false);
		final HashMap2D<String, Long, KursDaten> mapKurseByKuerzelUndJahrgang = new HashMap2D<>();
		for (final KursDaten kurs : kurse)
			for (final long idJahrgang : kurs.idJahrgaenge)
				mapKurseByKuerzelUndJahrgang.put(kurs.kuerzel, idJahrgang, kurs);
		// Prüfe den Beginn des Stundenplan - ist dieser evtl. nach dem Schuljahr des angegebenen Schuljahresabschnitts?
		final int schuljahr = DateUtils.getSchuljahrFromDateISO8601(beginn);
		if (schuljahr > schuljahresabschnitt.schuljahr) {
			logger.logLn(2, "[Fehler] - Das angegebene Startdatum %s liegt nach dem Schuljahr %d des angegebenen Schuljahresabschnitts"
					+ " und ist damit unzulässig.".formatted(beginn, schuljahresabschnitt.schuljahr));
			throw new ApiOperationException(Status.CONFLICT, "Das angegebene Startdatum %s liegt nach dem Schuljahr %d des angegebenen Schuljahresabschnitts"
					+ " und ist damit unzulässig.".formatted(beginn, schuljahresabschnitt.schuljahr));
		}
		if (schuljahr < schuljahresabschnitt.schuljahr) {
			logger.logLn(2, "[Fehler] - Das angegebene Startdatum %s liegt vor dem Schuljahr %d des angegebenen Schuljahresabschnitts und ist damit unzulässig."
					.formatted(beginn, schuljahresabschnitt.schuljahr));
			throw new ApiOperationException(Status.CONFLICT, "Das angegebene Startdatum %s liegt vor dem Schuljahr %d des angegebenen Schuljahresabschnitts"
					+ " und ist damit unzulässig.".formatted(beginn, schuljahresabschnitt.schuljahr));
		}
		// Erstelle den neuen Stundenplan
		final long idStundenplan = conn.transactionGetNextID(DTOStundenplan.class);
		final DTOStundenplan dtoStundenplan = new DTOStundenplan(idStundenplan, idSchuljahresabschnitt, beginn, beschreibung, wochentyp);
		dtoStundenplan.Ende = "%04d-%02d-%02d".formatted(schuljahresabschnitt.schuljahr + 1, 7, 31);
		conn.transactionPersist(dtoStundenplan);
		conn.transactionFlush();
		// Übertrage den Katalog der Räume
		DataStundenplanRaeume.addRaeume(conn, dtoStundenplan, DataKatalogRaeume.getRaeume(conn));
		// Übertrage den Katalog mit dem Zeitraster
		DataStundenplanZeitraster.addZeitraster(conn, dtoStundenplan, DataKatalogZeitraster.getZeitraster(conn));
		// Erzeuge die Einträge für die Kurs-Schienen
		DataStundenplanSchienen.addSchienenFromKursliste(conn, idStundenplan, kurse);
		final List<StundenplanSchiene> schienen = DataStundenplanSchienen.getSchienen(conn, idStundenplan);
		final HashMap2D<Long, Integer, StundenplanSchiene> mapSchienen = new HashMap2D<>();
		for (final StundenplanSchiene schiene : schienen)
			mapSchienen.put(schiene.idJahrgang, schiene.nummer, schiene);
		// Erzeuge die nötigen Einträge für den Stundenplan
		long next_uid = conn.transactionGetNextID(DTOStundenplanUnterricht.class);
		long next_lid = conn.transactionGetNextID(DTOStundenplanUnterrichtLehrer.class);
		long next_kid = conn.transactionGetNextID(DTOStundenplanUnterrichtKlasse.class);
		long next_rid = conn.transactionGetNextID(DTOStundenplanUnterrichtRaum.class);
		long next_sid = conn.transactionGetNextID(DTOStundenplanUnterrichtSchiene.class);
		final Set<LongArrayKey> setKursUnterricht = new HashSet<>();
		for (final UntisGPU001 u : unterrichte) {
			logger.logLn("-> Importiere Unterricht: " + u.toString());
			// Bestimme den Zeitraster-Eintrag des neuen Stundenplans
			final StundenplanZeitraster zeitraster = DataStundenplanZeitraster.getOrCreateZeitrasterEintrag(conn, idStundenplan, u.wochentag, u.stunde);
			// Bestimme die Klasse des Unterrichtes
			final DTOKlassen klasse = mapKlassenByKuerzel.get(u.klasseKuerzel);
			if (klasse == null) {
				logger.logLn(2, "[Fehler] - Die Klasse mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden.".formatted(u.klasseKuerzel));
				if (ignoreMissing) {
					logger.logLn(2, "Der Unterrichts-Eintrag wird ignoriert.");
					continue;
				}
				throw new ApiOperationException(Status.NOT_FOUND, "Die Klasse mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden."
						.formatted(u.klasseKuerzel));
			}
			// Bestimme den Fachlehrer
			final LehrerListeEintrag lehrer = mapLehrerByKuerzel.get(u.lehrerKuerzel);
			if ((u.lehrerKuerzel != null) && (lehrer == null)) {
				logger.logLn(2, "[Fehler] - Der Lehrer mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden.".formatted(u.lehrerKuerzel));
				if (ignoreMissing) {
					logger.logLn(2, "Der Unterrichts-Eintrag wird ignoriert.");
					continue;
				}
				throw new ApiOperationException(Status.NOT_FOUND,
						"Der Lehrer mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden.".formatted(u.lehrerKuerzel));
			}
			// Prüfe, ob es sich um Kursunterricht handelt
			final KursDaten kurs = mapKurseByKuerzelUndJahrgang.getOrNull(u.fachKuerzel, klasse.Jahrgang_ID);
			if (kurs == null) {
				// Bestimme das Fach
				final FachDaten fach = mapFaecherByKuerzel.get(u.fachKuerzel);
				if (fach == null) {
					logger.logLn(2, "[Fehler] - Das Fach bzw. der Kurs mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden."
							.formatted(u.fachKuerzel));
					if (ignoreMissing) {
						logger.logLn(2, "Der Unterrichts-Eintrag wird ignoriert.");
						continue;
					}
					throw new ApiOperationException(Status.NOT_FOUND, "Das Fach bzw. der Kurs mit dem Kürzel %s konnte nicht in der Datenbank gefunden werden."
							.formatted(u.fachKuerzel));
				}
				// Erstelle den Klassen-Unterricht ...
				final long uid = next_uid++;
				final DTOStundenplanUnterricht dtoUnterricht = new DTOStundenplanUnterricht(uid, zeitraster.id, 0, fach.id);
				dtoUnterricht.Kurs_ID = null;
				conn.transactionPersist(dtoUnterricht);
				conn.transactionFlush();
				// ... Lehrer ...
				if (lehrer != null)
					conn.transactionPersist(new DTOStundenplanUnterrichtLehrer(next_lid++, uid, lehrer.id));
				// ... Klasse ...
				conn.transactionPersist(new DTOStundenplanUnterrichtKlasse(next_kid++, uid, klasse.ID));
				// ... Raum
				if (u.raumKuerzel != null)
					conn.transactionPersist(new DTOStundenplanUnterrichtRaum(next_rid++, uid,
							DataStundenplanRaeume.getOrCreateRaum(conn, idStundenplan, u.raumKuerzel).id));
			} else {
				// Prüfe, ob der Kursunterricht schon mit einem früheren Datensatz bearbeitet wurde
				final long[] key = { kurs.id, u.idUnterricht, u.wochentag, u.stunde };
				if (!setKursUnterricht.add(new LongArrayKey(key))) {
					logger.logLn(2, "Unterricht mit der ID %d wurde für den Kurs '%s' mit der ID %d bereits für den Wochentag %d und der Stunde %d hinzugefügt."
							+ " Überspringe diesen Eintrag...".formatted(u.idUnterricht, kurs.kuerzel, kurs.id, u.wochentag, u.stunde));
					continue;
				}
				// Erstelle den Kurs-Unterricht ...
				final long uid = next_uid++;
				final DTOStundenplanUnterricht dtoUnterricht = new DTOStundenplanUnterricht(uid, zeitraster.id, 0, kurs.idFach);
				dtoUnterricht.Kurs_ID = kurs.id;
				conn.transactionPersist(dtoUnterricht);
				conn.transactionFlush();
				// ... Lehrer ...
				if (lehrer != null)
					conn.transactionPersist(new DTOStundenplanUnterrichtLehrer(next_lid++, uid, lehrer.id));
				// ... Schiene ...
				for (final long idJahrgang : kurs.idJahrgaenge) {
					for (final int schiene : kurs.schienen) {
						final StundenplanSchiene s = mapSchienen.getOrNull(idJahrgang, schiene);
						if (s == null)
							throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
									"Interner Fehler beim Anlegen der Schienen für den Kursunterricht des Stundenplans.");
						conn.transactionPersist(new DTOStundenplanUnterrichtSchiene(next_sid++, uid, s.id));
					}
				}
				// ... Raum
				if (u.raumKuerzel != null)
					conn.transactionPersist(new DTOStundenplanUnterrichtRaum(next_rid++, uid,
							DataStundenplanRaeume.getOrCreateRaum(conn, idStundenplan, u.raumKuerzel).id));
			}
			conn.transactionFlush();
		}
	}


	/**
	 * Importiert die in dem Multipart übergebene Datei.
	 *
	 * @param conn        die Datenbank-Verbindung
	 * @param multipart   der Multipart-Body mmit der Datei
	 * @param ignoreMissing   wenn true, dann werden fehlende Klassen und Kurse ignoriert
	 *                        und protokolliert, es wird aber kein Fehler erzeugt.
	 *
	 * @return die HTTP-Response mit dem Log
	 */
	public static Response importGPU001(final DBEntityManager conn, final UntisGPU001MultipartBody multipart, final boolean ignoreMissing) {
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		final SimpleOperationResponse daten = new SimpleOperationResponse();
		logger.addConsumer(log);
		logger.addConsumer(new LogConsumerConsole());
		try {
			// Erstelle aus dem Multipart den String mit dem Inhalt der Textdatei
			logger.logLn("Lese die Datensätze aus der Text-Datei ein.");
			final String strGPU001 = new String(multipart.data, StandardCharsets.UTF_8);
			final List<UntisGPU001> unterrichte = UntisGPU001.readCSV(strGPU001);
			final StundenplanListeEintragMinimal entry = new ObjectMapper().readValue(multipart.entry, StundenplanListeEintragMinimal.class);
			logger.logLn("Importiere den Stundenplan:");
			_importGPU001(logger, conn, entry.idSchuljahresabschnitt, entry.gueltigAb, entry.bezeichnung, unterrichte, 0, ignoreMissing);
			logger.logLn("Import beendet");
		} catch (@SuppressWarnings("unused") final IOException e) {
			logger.logLn(2, "Fehler beim Einlesen der Datensätze.");
			daten.success = false;
			daten.log = log.getStrings();
			return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (final ApiOperationException e) {
			logger.logLn(2, "[FEHLER] beim Import");
			daten.success = false;
			daten.log = log.getStrings();
			return Response.status(e.getStatus()).type(MediaType.APPLICATION_JSON).entity(daten).build();
		}
		daten.success = true;
		daten.log = log.getStrings();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Importiert die in dem Multipart übergebene Datei.
	 *
	 * @param conn        die Datenbank-Verbindung
	 * @param multipart   der Multipart-Body mmit der Datei
	 *
	 * @return die HTTP-Response mit dem Log
	 */
	public static Response importGPU005(final DBEntityManager conn, final SimpleBinaryMultipartBody multipart) {
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		logger.addConsumer(new LogConsumerConsole());

		boolean success = true;
		Status statusCode = Status.OK;
		try {
			final String csv = new String(multipart.data, StandardCharsets.UTF_8);
			logger.logLn("Importiere die Räume aus der Untis-Datei GPU005.txt:");
			importUntisRaeume(conn, logger, csv);
			logger.logLn("  Import beendet");
		} catch (final Exception e) {
			success = false;
			if (e instanceof final ApiOperationException aoe) {
				statusCode = aoe.getStatus();
			} else {
				logger.logLn("  [FEHLER] Unerwarteter Fehler: " + e.getLocalizedMessage());
				statusCode = Status.INTERNAL_SERVER_ERROR;
			}
		}
		final SimpleOperationResponse daten = new SimpleOperationResponse();
		daten.success = success;
		daten.log = log.getStrings();
		return Response.status(statusCode).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Importiert Räume aus der Untis-Datei GPU005.txt in das Datenbank-Schema, welches durch die übergebene
	 * Verbindung festgelegt ist.
	 *
	 * @param conn     die Datenbank-Verbindung.
	 * @param logger   der Logger für Rückmeldungen zum Import-Prozess
	 * @param csv      die CSV-Datei mit den Räumen (GPU005.txt)
	 *
	 * @return true im Erfolgsfall und false im Fehlerfall
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static boolean importUntisRaeume(final DBEntityManager conn, final Logger logger, final String csv) throws ApiOperationException {
		// Lese zunächst die Informationen zur Schule aus der SVWS-Datenbank und überprüfe die Schulform
		logger.logLn("-> Lese Informationen zu der Schule ein...");
		logger.modifyIndent(2);
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null) {
			logger.logLn("[Fehler] - Konnte die Informationen zur Schule nicht aus der Datenbank lesen");
			throw new ApiOperationException(Status.NOT_FOUND, "Konnte die Informationen zur Schule nicht aus der Datenbank lesen.");
		}
		logger.logLn("[OK]");
		logger.modifyIndent(-2);
		try {
			logger.logLn("-> Lese die Räume aus der CSV-Datei ein...");
			logger.modifyIndent(2);
			final List<UntisGPU005> raeume = UntisGPU005.readCSV(csv);
			logger.logLn("[OK]");
			logger.modifyIndent(-2);

			conn.transactionBegin();

			logger.logLn("-> Lese die bereits im Katalog vorhandenen Räume ein...");
			logger.modifyIndent(2);
			final Map<String, DTOKatalogRaum> raeumeVorhanden = conn.queryAll(DTOKatalogRaum.class).stream().collect(Collectors.toMap(r -> r.Kuerzel, r -> r));
			logger.logLn("[OK]");
			logger.modifyIndent(-2);

			logger.logLn("-> Schreibe die Räume...");
			logger.modifyIndent(2);
			long id = conn.transactionGetNextID(DTOKatalogRaum.class);
			for (final UntisGPU005 raum : raeume) {
				if (raum.kuerzel == null) {
					logger.logLn("[Fehler] - Konnte die Informationen zur Schule nicht aus der Datenbank lesen");
					logger.modifyIndent(-2);
					throw new ApiOperationException(Status.BAD_REQUEST, "Jeder Raum muss ein gültiges Kürzel haben.");
				}
				if (raeumeVorhanden.containsKey(raum.kuerzel)) {
					logger.logLn("Raum '%s' wird nicht übernommen, da er bereits vorhanden ist.".formatted(raum.kuerzel));
					continue;
				}
				final DTOKatalogRaum dto = new DTOKatalogRaum(id++, raum.kuerzel, raum.bezeichnung == null ? raum.kuerzel : raum.bezeichnung,
						raum.groesse == null ? 40 : raum.groesse);
				conn.transactionPersist(dto);
				logger.logLn("Raum '%s' hinzugefügt.".formatted(raum.kuerzel));
			}

			if (!conn.transactionCommit()) {
				logger.logLn("[Fehler] Unerwarteter Fehler beim Schreiben in die Datenbank.");
				logger.modifyIndent(-2);
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
			}
			logger.logLn("[OK]");
			logger.modifyIndent(-2);
			return true;
		} catch (final UnrecognizedPropertyException upe) {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			upe.printStackTrace(pw);
			sw.toString().lines().forEach(logger::logLn);
			logger.logLn("[Fehler] Konnte die Datei GPU005.txt nicht einlesen. Prüfen sie ggf. auch die Zeichenkodierung der Datei."
					+ " Diese muss UTF-8 (ohne BOM) sein.");
			logger.modifyIndent(-2);
			return false;
		} catch (@SuppressWarnings("unused") final Exception exception) {
			logger.logLn("[Fehler]");
			logger.modifyIndent(-2);
			return false;
		} finally {
			conn.transactionRollback();
		}
	}

	/**
	 * Erzeugt den Export eines Blockungsergebnisses für Untis, indem die Dateien GPU002.txt, GPU010.txt, GPU015.txt und GPU019.txt
	 * erzeugt werden und in einem Zip-File in der Response zurückgegeben werden.
	 *
	 * @param conn                  die Datenbank-Verbindung
	 * @param logger                der Logger
	 * @param idBlockungsergebnis   die ID des Blockungsergebnisses
	 * @param idUnterrichtStart     die erste ID für die Unterricht-IDs, welche in dem Untis-Export verwendet wird
	 *
	 * @return eine Response mit dem Zip-File
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static Response exportUntisBlockungsergebnis(final DBEntityManager conn, final Logger logger, final long idBlockungsergebnis,
			final long idUnterrichtStart) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		logger.logLn("-> Prüfe, ob das Blockungsergebnis gültig ist und die erste ID für den Unterricht > 0 ist...");
		logger.modifyIndent(2);
		logger.log("Prüfe die ID für den Unterricht auf Gültigkeit... ");
		if (idUnterrichtStart <= 0) {
			logger.logLn(0, "[Fehler]");
			logger.logLn("Die ID %d ist kleiner oder gleich 0".formatted(idUnterrichtStart));
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID %d für die erster Unterricht ID ist kleiner oder gleich 0.");
		}
		logger.logLn("[OK]");

		logger.log("Lade das Ergebnis aus der Datenbank... ");
		final GostBlockungsergebnisManager ergebnisManager;
		final GostBlockungsdatenManager datenManager;
		try {
			ergebnisManager = DataGostBlockungsergebnisse.getErgebnismanagerFromID(conn, idBlockungsergebnis);
			datenManager = ergebnisManager.getParent();
		} catch (final Exception e) {
			logger.logLn(0, "[Fehler]");
			if (e instanceof final ApiOperationException aoe) {
				logger.logLn(aoe.getMessage());
				logger.modifyIndent(-2);
				throw aoe;
			}
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Unerwarteter Fehler beim Einlesen der Blockung");
		}
		logger.logLn("[OK]");

		logger.log("Lade die Informationen zu den Schülern der Blockung aus der Datenbank... ");
		final List<Schueler> schueler = datenManager.schuelerGetListe();
		final List<Long> idsSchueler = schueler.stream().map(s -> s.id).toList();
		if (idsSchueler.isEmpty()) {
			logger.logLn("Keine Schüler in der Blockung vorhanden. Der Export wird abgebrochen.");
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Schüler in der Blockung vorhanden.");
		}
		final List<DTOSchueler> dtosSchueler = conn.queryByKeyList(DTOSchueler.class, idsSchueler);
		if (dtosSchueler.size() != idsSchueler.size()) {
			logger.logLn("Es konnten nicht alle Schüler der Blockung in der Datenbank gefunden werden. Der Export wird abgebrochen.");
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Es konnten nicht alle Schüler der Blockung in der Datenbank gefunden werden.");
		}
		final Map<Long, DTOSchueler> mapSchueler = dtosSchueler.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		logger.modifyIndent(-2);

		logger.logLn("-> Erstelle Liste der Unterrichte für GPU002.txt");
		logger.modifyIndent(2);
		final String csvUnterrichte;
		final @NotNull Map<@NotNull Long, @NotNull Long> mapKursZuUnterricht = new HashMap<>();
		try {
			final @NotNull List<@NotNull GostBlockungKurs> kurse = datenManager.kursGetListeSortiertNachKursartFachNummer();
			long idUnterricht = idUnterrichtStart;
			final List<UntisGPU002> unterrichte = new ArrayList<>();
			for (final @NotNull GostBlockungKurs kurs : kurse) {
				final UntisGPU002 u = new UntisGPU002();
				u.idUnterricht = idUnterricht++;
				mapKursZuUnterricht.put(kurs.id, u.idUnterricht);
				u.wochenstunden = kurs.wochenstunden;
				u.wochenstundenKlasse = kurs.wochenstunden;
				u.wochenstundenLehrer = kurs.wochenstunden;
				u.klasseKuerzel = datenManager.getHalbjahr().jahrgang;
				u.lehrerKuerzel = kurs.lehrer.isEmpty() ? null : kurs.lehrer.get(0).kuerzel;
				u.fachKuerzel = datenManager.kursGetName(kurs.id);
				u.studentenZahl = ergebnisManager.getOfKursAnzahlSchueler(kurs.id);
				u.wochenTyp = "WA";
				u.jahreswert = 0.0048;
				final ZulaessigesFach fach = ZulaessigesFach.getByKuerzelASD(ergebnisManager.getFach(kurs.fach_id).kuerzel);
				final RGBFarbe fachFarbe = (fach == null) ? new RGBFarbe(255, 255, 255) : fach.getFarbe();
				u.farbeHintergrund = "" + ((fachFarbe.red * 65536) + (fachFarbe.green * 256) + fachFarbe.blue);
				u.kennzeichen = "n";
				u.doppelStdMin = 1;
				u.doppelStdMax = 1;
				u.studentenMaennlich = 0;
				u.studentenWeiblich = 0;
				for (final Schueler s : ergebnisManager.getOfKursSchuelermenge(kurs.id)) {
					switch (Geschlecht.fromValue(s.geschlecht)) {
						case Geschlecht.M -> u.studentenMaennlich++;
						case Geschlecht.W -> u.studentenWeiblich++;
						default -> {
							/* nicht zu tun */
						}
					}
				}
				u.eigenwert = "100000";
				u.eigenwertHunderttausendstel = "1";
				unterrichte.add(u);
			}
			csvUnterrichte = UntisGPU002.writeCSV(unterrichte);
		} catch (final Exception e) {
			logger.logLn("Fehler beim Erstellen der Datei GPU002.txt.");
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der Datei GPU002.txt.");
		}
		logger.logLn("OK");
		logger.modifyIndent(-2);

		logger.logLn("-> Erstelle Liste der Schüler für GPU010.txt");
		final String csvSchueler;
		logger.modifyIndent(2);
		try {
			final List<UntisGPU010> gpu010 = new ArrayList<>();
			for (final Schueler s : schueler) {
				final DTOSchueler dtoSchueler = mapSchueler.get(s.id);
				final LocalDate date =
						((dtoSchueler.Geburtsdatum == null) || "".equals(dtoSchueler.Geburtsdatum)) ? null : LocalDate.parse(dtoSchueler.Geburtsdatum);
				final UntisGPU010 dto = new UntisGPU010();
				dto.geburtsdatum = (date == null) ? null : "%04d%02d%02d".formatted(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
				dto.name = ((s.nachname == null) || ("".equals(s.nachname.trim())) ? "???" : s.nachname.trim().replace(" ", ""))
						+ "_" + (((s.vorname == null) || "".equals(s.vorname.trim())) ? "???" : s.vorname.trim().replace(" ", "").substring(0, 3))
						+ "_" + ((dto.geburtsdatum == null) ? "????????" : dto.geburtsdatum);
				dto.langname = (s.nachname == null) || ("".equals(s.nachname.trim())) ? "???" : s.nachname.trim();
				dto.vorname = ((s.vorname == null) || "".equals(s.vorname.trim())) ? "???" : s.vorname.trim();
				dto.klasse = datenManager.getHalbjahr().jahrgang;
				dto.geschlecht = switch (Geschlecht.fromValue(s.geschlecht)) {
					case Geschlecht.M -> "2";
					case Geschlecht.W -> "1";
					default -> null;
				};
				dto.emailAdresse = dtoSchueler.Email;
				dto.fremdschluessel = "" + s.id;
				gpu010.add(dto);
			}
			csvSchueler = UntisGPU010.writeCSV(gpu010);
		} catch (final Exception e) {
			logger.logLn("Fehler beim Erstellen der Datei GPU010.txt.");
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der Datei GPU002.txt.");
		}
		logger.logLn("OK");
		logger.modifyIndent(-2);

		logger.logLn("-> Erstelle Liste der Kurswahlen für GPU015.txt");
		final String csvKurswahlen;
		logger.modifyIndent(2);
		try {
			final List<UntisGPU015> gpu015 = new ArrayList<>();
			for (final Schueler s : schueler) {
				final DTOSchueler dtoSchueler = mapSchueler.get(s.id);
				final LocalDate date =
						((dtoSchueler.Geburtsdatum == null) || "".equals(dtoSchueler.Geburtsdatum)) ? null : LocalDate.parse(dtoSchueler.Geburtsdatum);
				final String gebDatum = (date == null) ? null : "%04d%02d%02d".formatted(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
				final String schuelerName = ((s.nachname == null) || ("".equals(s.nachname.trim())) ? "???" : s.nachname.trim().replace(" ", ""))
						+ "_" + (((s.vorname == null) || "".equals(s.vorname.trim())) ? "???" : s.vorname.trim().replace(" ", "").substring(0, 3))
						+ "_" + ((gebDatum == null) ? "????????" : gebDatum);

				for (final GostBlockungsergebnisKurs k : ergebnisManager.getOfSchuelerKursmenge(s.id)) {
					final UntisGPU015 dto = new UntisGPU015();
					dto.name = schuelerName;
					dto.idUnterricht = mapKursZuUnterricht.get(k.id);
					dto.fach = datenManager.kursGetName(k.id);
					dto.klasse = datenManager.getHalbjahr().jahrgang;
					dto.statistikKennzeichen = datenManager.schuelerGetOfFachFachwahl(s.id, k.fachID).istSchriftlich ? "S" : "M";
					dto.idsUnterrichteAlternativkurse = "";
					dto.kuerzelAlternativkurse = "";
					dto.prioAlternativkurse = "";
					String tilde = "";
					for (final GostBlockungKurs ak : datenManager.kursGetListeByFachUndKursart(k.fachID, k.kursart)) {
						if ("".equals(tilde))
							tilde = "~";
						final Long idUnterrichtAlternativ = mapKursZuUnterricht.get(ak.id);
						dto.idsUnterrichteAlternativkurse += tilde + idUnterrichtAlternativ;
						dto.kuerzelAlternativkurse += tilde + datenManager.kursGetName(ak.id);
						dto.prioAlternativkurse += tilde + "1";
					}
					gpu015.add(dto);
				}
			}
			csvKurswahlen = UntisGPU015.writeCSV(gpu015);
		} catch (final Exception e) {
			logger.logLn("Fehler beim Erstellen der Datei GPU015.txt.");
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der Datei GPU002.txt.");
		}
		logger.logLn("OK");
		logger.modifyIndent(-2);

		logger.logLn("-> Erstelle Liste der Schienen für GPU019.txt für die Kurs-Schienen-Zuordnung");
		final String csvSchienen;
		logger.modifyIndent(2);
		try {
			final @NotNull List<@NotNull GostBlockungKurs> kurse = datenManager.kursGetListeSortiertNachKursartFachNummer();
			final List<UntisGPU019> gpu019 = new ArrayList<>();
			for (final @NotNull GostBlockungKurs kurs : kurse) {
				final UntisGPU019 dto = new UntisGPU019();
				// TODO evtl. Multi-Schienen-Kurse unterstützen
				final Set<GostBlockungsergebnisSchiene> schienen = ergebnisManager.getOfKursSchienenmenge(kurs.id);
				final GostBlockungsergebnisSchiene schiene = schienen.iterator().next();
				dto.name = datenManager.schieneGet(schiene.id).bezeichnung;
				dto.art = "2";
				dto.anzahlWochenstunden = kurs.wochenstunden;
				dto.idUnterricht = mapKursZuUnterricht.get(kurs.id);
				dto.fach = datenManager.kursGetName(kurs.id);
				dto.klassen = datenManager.getHalbjahr().jahrgang;
				gpu019.add(dto);
			}
			csvSchienen = UntisGPU019.writeCSV(gpu019);
		} catch (final Exception e) {
			logger.logLn("Fehler beim Erstellen der Datei GPU019.txt.");
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der Datei GPU002.txt.");
		}
		logger.logLn("OK");
		logger.modifyIndent(-2);

		logger.logLn("-> Erzeuge den Zip-File mit den vier GPU-Dateien...");
		logger.modifyIndent(2);
		final String zipname = "BlockungExportUntis.zip";
		final byte[] zipdata;
		try {
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				try (ZipOutputStream zos = new ZipOutputStream(baos)) {
					zos.putNextEntry(new ZipEntry("GPU002.txt"));
					zos.write(csvUnterrichte.getBytes(StandardCharsets.UTF_8));
					zos.closeEntry();
					baos.flush();
					zos.putNextEntry(new ZipEntry("GPU010.txt"));
					zos.write(csvSchueler.getBytes(StandardCharsets.UTF_8));
					zos.closeEntry();
					baos.flush();
					zos.putNextEntry(new ZipEntry("GPU015.txt"));
					zos.write(csvKurswahlen.getBytes(StandardCharsets.UTF_8));
					zos.closeEntry();
					baos.flush();
					zos.putNextEntry(new ZipEntry("GPU019.txt"));
					zos.write(csvSchienen.getBytes(StandardCharsets.UTF_8));
					zos.closeEntry();
					baos.flush();
				}
				zipdata = baos.toByteArray();
			}
		} catch (final IOException e) {
			logger.logLn("Fehler beim Erstellen der Zip-Datei.");
			logger.modifyIndent(-2);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der Zip-Datei");
		}
		logger.logLn("Zip-Datei wurde erstellt.");
		logger.modifyIndent(-2);
		return Response.ok(zipdata).header("Content-Disposition", "attachment; filename=\"" + zipname + "\"").build();
	}

}
