package de.svws_nrw.data.datenaustausch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import de.svws_nrw.base.CsvReader;
import de.svws_nrw.base.kurs42.Kurs42Import;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelParameterTyp;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.data.SimpleBinaryMultipartBody;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungKurslehrer;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungRegel;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungRegelParameter;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchiene;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchueler;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogRaum;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse stellt Methoden für den Import und Export von Kurs 42 - Blockungen
 * zur Vefügung.
 */
public final class DataKurs42 {

	private DataKurs42() {
		throw new IllegalStateException("Instantiation of " + DataKurs42.class.getName() + " not allowed");
	}

	private static final Random _random = new Random();


	private static boolean removeTempFiles(final Path path) {
		try {
			final Path pBlockung = path.resolve("Blockung.txt");
			if (Files.exists(pBlockung))
				Files.delete(pBlockung);
			final Path pSchueler = path.resolve("Schueler.txt");
			if (Files.exists(pSchueler))
				Files.delete(pSchueler);
			final Path pFaecher = path.resolve("Faecher.txt");
			if (Files.exists(pFaecher))
				Files.delete(pFaecher);
			final Path pKurse = path.resolve("Kurse.txt");
			if (Files.exists(pKurse))
				Files.delete(pKurse);
			final Path pSchienen = path.resolve("Schienen.txt");
			if (Files.exists(pSchienen))
				Files.delete(pSchienen);
			final Path pBlockplan = path.resolve("Blockplan.txt");
			if (Files.exists(pBlockplan))
				Files.delete(pBlockplan);
			final Path pFachwahlen = path.resolve("Fachwahlen.txt");
			if (Files.exists(pFachwahlen))
				Files.delete(pFachwahlen);
			Files.delete(path);
		} catch (@SuppressWarnings("unused") final IOException e) {
			return false;
		}
		return true;
	}


	private static boolean unzipMultipartData(final Path path, final byte[] data) {
		try {
			Files.createDirectories(path);
			try (ZipInputStream zipInput = new ZipInputStream(new ByteArrayInputStream(data))) {
				ZipEntry zipFile;
				while ((zipFile = zipInput.getNextEntry()) != null) {
					final String filename = switch (zipFile.getName()) {
						case "Blockung.txt", "Schueler.txt", "Faecher.txt", "Kurse.txt", "Schienen.txt", "Blockplan.txt", "Fachwahlen.txt" -> zipFile.getName();
						default -> null;
					};
					if (filename != null) {
						final Path filePath = path.resolve(filename);
						Files.write(filePath, zipInput.readAllBytes(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
					}
					zipInput.closeEntry();
				}
			}
		} catch (@SuppressWarnings("unused") final IOException e) {
			return false;
		}
		return true;
	}


	/**
	 * Importiert die in dem Multipart übergebene Datei.
	 *
	 * @param conn        die Datenbank-Verbindung
	 * @param multipart   der Multipart-Body mmit der Datei
	 *
	 * @return die HTTP-Response mit dem Log
	 */
	public static Response importZip(final DBEntityManager conn, final SimpleBinaryMultipartBody multipart) {
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		logger.addConsumer(new LogConsumerConsole());

		boolean success = true;
		Status statusCode = Status.OK;
		try {
			// Erstelle temporär eine Zip-Datei aus dem übergebenen Byte-Array
			final String tmpDirectory = SVWSKonfiguration.get().getTempPath();
			final String tmpFilename = conn.getUser().connectionManager.getConfig().getDBSchema() + "_" + _random.ints(48, 123)  // from 0 to z
					.filter(i -> ((i <= 57) || (i >= 65)) && ((i <= 90) || (i >= 97)))  // filter some unicode characters
					.limit(40)
					.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
					.toString() + ".zip";
			logger.logLn("Erstelle eine temporäres Verzechnis mit dem Inhalt der Zip-Datei unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\"");
			final Path path = Paths.get(tmpDirectory).resolve(tmpFilename);
			if (!unzipMultipartData(path, multipart.data)) {
				logger.logLn(2, "Fehler beim Erstellen der temporären Zip-Datei unter dem Namen \"" + tmpDirectory + "/" + tmpFilename + "\"");
				throw new ApiOperationException(Status.CONFLICT, "Fehler beim Erstellen der temporären Zip-Datei unter dem Namen \"" + tmpDirectory + "/"
						+ tmpFilename + "\"");
			}

			logger.logLn("Importiere die Blockung mithilfe der extrahierten Daten:");

			try {
				importKurs42(conn, logger, path);
				logger.logLn("  Import beendet");
			} finally {
				// Entferne die temporär angelegten Dateien wieder...
				logger.logLn("Löschen des temporären Verzeichnis \"" + tmpDirectory + "/" + tmpFilename + "\".");
				if (removeTempFiles(path)) {
					logger.logLn(2, "[OK]");
				} else {
					success = false;
					logger.logLn(2, "[FEHLER]");
				}
			}
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


	/** Funktion zum Bestimmen der nächsten freien ID bei einer Tabelle mit Autoinkrement */
	private static BiFunction<DBEntityManager, SchemaTabelle, Long> getNextID = (final DBEntityManager conn, final SchemaTabelle tab) -> {
		final DTOSchemaAutoInkremente dbID = conn.queryByKey(DTOSchemaAutoInkremente.class, tab.name());
		return dbID == null ? 1 : dbID.MaxID + 1;
	};


	/**
	 * Importiert eine Kurs-42-Blockung in das Datenbank-Schema, welches durch die übergebene
	 * Verbindung festgelegt ist.
	 *
	 * @param conn     die Datenbank-Verbindung.
	 * @param logger   der Logger für Rückmeldungen zum Import-Prozess
	 * @param path     der Pfad, wo sich die Kurs-42-Import-Dateien befinden
	 *
	 * @return true im Erfolgsfall und false im Fehlerfall
	 *
	 * @throws IOException   Falls der Zugriff auf die Kurs42-Dateien fehlschlägt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static boolean importKurs42(final DBEntityManager conn, final Logger logger, final Path path) throws IOException, ApiOperationException {
		// Lese zunächst die Informationen zur Schule aus der SVWS-Datenbank und überprüfe die Schulform
		logger.logLn("-> Lese Informationen zu der Schule ein...");
		logger.modifyIndent(2);
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null) {
			logger.logLn("[Fehler] - Konnte die Informationen zur Schule nicht aus der Datenbank lesen");
			throw new ApiOperationException(Status.NOT_FOUND, "Konnte die Informationen zur Schule nicht aus der Datenbank lesen.");
		}
		final Schulform schulform = schule.Schulform;
		if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb)) {
			logger.logLn("[Fehler] - Die Schulform hat keine gymnasiale Oberstufe oder konnte nicht bestimmt werden");
			throw new ApiOperationException(Status.CONFLICT, "Die Schulform hat keine gymnasiale Oberstufe oder konnte nicht bestimmt werden.");
		}
		logger.logLn("[OK]");
		logger.modifyIndent(-2);
		// Bestimme nun die IDs der Schüler, welche in der Schule vorhanden sind
		logger.logLn("-> Bestimme die Schüler der Schule aus der Datenbank...");
		logger.modifyIndent(2);
		final List<DTOSchueler> listeSchueler = conn.queryAll(DTOSchueler.class);
		if ((listeSchueler == null) || (listeSchueler.isEmpty())) {
			logger.logLn("[Fehler] - Konnte die Liste der Schüler nicht einlesen");
			throw new ApiOperationException(Status.NOT_FOUND, "Konnte die Liste der Schüler nicht einlesen.");
		}
		final Set<Long> setSchueler = listeSchueler.stream().map(s -> s.ID).collect(Collectors.toSet());
		logger.logLn("[OK]");
		logger.modifyIndent(-2);
		// Bestimme nun die Lehrkräfte, die an der Schule tätig sind.
		logger.logLn("-> Bestimme die Lehrkräfte der Schule aus der Datenbank...");
		logger.modifyIndent(2);
		final List<DTOLehrer> listeLehrer = conn.queryAll(DTOLehrer.class);
		if ((listeLehrer == null) || (listeLehrer.isEmpty())) {
			logger.logLn("[Fehler] - Konnte die Liste der Lehrer nicht einlesen");
			throw new ApiOperationException(Status.NOT_FOUND, "Konnte die Liste der Lehrer nicht einlesen.");
		}
		final Map<String, Long> mapLehrer = listeLehrer.stream().collect(Collectors.toMap(l -> l.Kuerzel, l -> l.ID));
		logger.logLn("[OK]");
		logger.modifyIndent(-2);
		// Bestimme die Fächer, die an der Schule vorhanden sind.
		logger.logLn("-> Bestimme die Fächer der Schule aus der Datenbank...");
		logger.modifyIndent(2);
		final List<DTOFach> listeFaecher = conn.queryList(DTOFach.QUERY_BY_ISTOBERSTUFENFACH, DTOFach.class, true);
		if ((listeFaecher == null) || (listeFaecher.isEmpty())) {
			logger.logLn("[Fehler] - Konnte die Liste der Fächer der Oberstufe nicht einlesen");
			throw new ApiOperationException(Status.NOT_FOUND, "Konnte die Liste der Fächer der Oberstufe nicht einlesen.");
		}
		final Map<Long, String> mapFaecher = listeFaecher.stream().collect(Collectors.toMap(f -> f.ID, f -> f.Kuerzel));
		logger.logLn("[OK]");
		logger.modifyIndent(-2);
		// Lese nun die Daten von Kurs 42 aus dem angegeben Pfad ein
		logger.logLn("-> Lese die Kurs 42 - Export - Textdateien ein...");
		logger.modifyIndent(2);
		Kurs42Import k42 = null;
		try {
			k42 = new Kurs42Import(path, schule.SchulNr, mapLehrer, setSchueler, logger);
		} catch (final IOException e) {
			logger.logLn("[Fehler] - Fehler beim Einlesen des Kurs42-Text-Exports: ");
			final StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.logLn(sw.toString());
			throw new ApiOperationException(Status.BAD_REQUEST, "Fehler beim Einlesen des Kurs42-Text-Exports.");
		}
		// Prüfe, ob der Abiturjahrgang zuvor angelegt wurde
		logger.logLn("-> Prüfe, ob der Abiturjahrgang %d angelegt ist...".formatted(k42.abiturjahrgang));
		logger.modifyIndent(2);
		final DTOGostJahrgangsdaten jahrgangsdaten = conn.queryByKey(DTOGostJahrgangsdaten.class, k42.abiturjahrgang);
		if (jahrgangsdaten == null) {
			logger.logLn("[Fehler] - Der Abiturjahrgang %d ist nicht angelegt.".formatted(k42.abiturjahrgang));
			throw new ApiOperationException(Status.NOT_FOUND, "Der Abiturjahrgang %d ist nicht angelegt.".formatted(k42.abiturjahrgang));
		}
		logger.logLn("[OK]");
		logger.modifyIndent(-2);
		try {
			logger.logLn("-> Schreibe die Blockungsdaten...");
			logger.modifyIndent(2);
			conn.transactionBegin();
			// Bestimme zunächst die IDs für die neue Blockung, die Schienen, die Kurse, die Regeln und das Ergebnis
			final Long blockungID = getNextID.apply(conn, Schema.tab_Gost_Blockung);
			final Long schienenID = getNextID.apply(conn, Schema.tab_Gost_Blockung_Schienen);
			final Long kursID = getNextID.apply(conn, Schema.tab_Gost_Blockung_Kurse);
			final Long regelID = getNextID.apply(conn, Schema.tab_Gost_Blockung_Regeln);
			final Long ergebnisID = getNextID.apply(conn, Schema.tab_Gost_Blockung_Zwischenergebnisse);
			// Lege die Blockung und ein zugehöriges Ergebnis an
			final DTOGostBlockung blockung = new DTOGostBlockung(blockungID, k42.name + " (import)", k42.abiturjahrgang, k42.halbjahr, false);
			final DTOGostBlockungZwischenergebnis erg = new DTOGostBlockungZwischenergebnis(ergebnisID, blockungID, false);
			conn.transactionPersist(blockung);
			conn.transactionPersist(erg);
			// Lege die Schienen an
			for (final GostBlockungSchiene schiene : k42.schienen) {
				conn.transactionPersist(new DTOGostBlockungSchiene(schienenID + schiene.id,
						blockungID, schiene.nummer, schiene.bezeichnung, schiene.wochenstunden));
			}
			// Lege die Kurse an
			for (final GostBlockungKurs kurs : k42.kurse) {
				// Prüfe, ob die Fach ID in Ordnung ist
				if (mapFaecher.get(kurs.fach_id) == null) {
					conn.transactionRollback();
					logger.logLn("[Fehler] - Fach-ID " + kurs.fach_id + " ist in der Datenbank nicht bekannt");
					throw new ApiOperationException(Status.NOT_FOUND, "Fach-ID " + kurs.fach_id + " ist in der Datenbank nicht bekannt");
				}
				conn.transactionPersist(new DTOGostBlockungKurs(kursID + kurs.id, blockungID, kurs.fach_id,
						GostKursart.fromID(kurs.kursart), kurs.nummer, kurs.istKoopKurs, kurs.anzahlSchienen, kurs.wochenstunden));
				for (final GostBlockungKursLehrer lehrer : kurs.lehrer)
					conn.transactionPersist(new DTOGostBlockungKurslehrer(kursID + kurs.id,
							lehrer.id, lehrer.reihenfolge, lehrer.wochenstunden));
			}
			// Lege die Regeln an
			for (final GostBlockungRegel regel : k42.regeln) {
				final GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(regel.typ);
				conn.transactionPersist(new DTOGostBlockungRegel(regelID + regel.id, blockungID, typ));
				for (int i = 0; i < regel.parameter.size(); i++) {
					Long param = regel.parameter.get(i);
					final GostKursblockungRegelParameterTyp paramTyp = typ.getParamType(i);
					param = switch (paramTyp) {
						case KURSART -> param;
						case SCHIENEN_NR -> param;
						case KURS_ID -> kursID + param;
						case SCHUELER_ID -> param;
						default -> param;
					};
					conn.transactionPersist(new DTOGostBlockungRegelParameter(regelID + regel.id, i, param));
				}
			}
			// Schreibe die Kurs-Schienen-Zuordnungen
			for (final Pair<Long, Long> zuordnung : k42.zuordnung_kurs_schiene.getNonNullValuesAsList())
				conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchiene(ergebnisID,
						kursID + zuordnung.a, schienenID + zuordnung.b));
			// Schreibe die Kurs-Schüler-Zuordnungen
			for (final Pair<Long, Long> zuordnung : k42.zuordnung_kurs_schueler.getNonNullValuesAsList())
				conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchueler(ergebnisID,
						kursID + zuordnung.a, zuordnung.b));
			if (!conn.transactionCommit()) {
				logger.logLn("[Fehler] Unerwarteter Fehler beim Schreiben in die Datenbank.");
				logger.modifyIndent(-2);
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
			}
			logger.logLn("[OK]");
			logger.modifyIndent(-2);
			return true;
		} catch (@SuppressWarnings("unused") final Exception exception) {
			return false;
		} finally {
			conn.transactionRollback();
		}
	}



	/**
	 * Importiert die in dem Multipart übergebene Datei.
	 *
	 * @param conn        die Datenbank-Verbindung
	 * @param multipart   der Multipart-Body mmit der Datei
	 *
	 * @return die HTTP-Response mit dem Log
	 */
	public static Response importRaeume(final DBEntityManager conn, final SimpleBinaryMultipartBody multipart) {
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		logger.addConsumer(new LogConsumerConsole());

		boolean success = true;
		Status statusCode = Status.OK;
		try {
			final String csv = new String(multipart.data, StandardCharsets.UTF_8);
			logger.logLn("Importiere die Räume aus der CSV-Datei:");
			importRaeumeCSV(conn, logger, csv);
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
	 * Importiert Räume aus Kurs-42 in das Datenbank-Schema, welches durch die übergebene
	 * Verbindung festgelegt ist.
	 *
	 * @param conn     die Datenbank-Verbindung.
	 * @param logger   der Logger für Rückmeldungen zum Import-Prozess
	 * @param csv      die CSV-Datei mit den Räumen
	 *
	 * @return true im Erfolgsfall und false im Fehlerfall
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static boolean importRaeumeCSV(final DBEntityManager conn, final Logger logger, final String csv) throws ApiOperationException {
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
			final List<Kurs42DataRaum> raeume = CsvReader.from(csv, Kurs42DataRaum.class);
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
			for (final Kurs42DataRaum raum : raeume) {
				if (raum.RaumKuerzel == null) {
					logger.logLn("[Fehler] - Konnte die Informationen zur Schule nicht aus der Datenbank lesen");
					logger.modifyIndent(-2);
					throw new ApiOperationException(Status.BAD_REQUEST, "Jeder Raum muss ein gültiges Kürzel haben.");
				}
				if (raeumeVorhanden.containsKey(raum.RaumKuerzel)) {
					logger.logLn("Raum '%s' wird nicht übernommen, da er bereits vorhanden ist.".formatted(raum.RaumKuerzel));
					continue;
				}
				final DTOKatalogRaum dto = new DTOKatalogRaum(id++, raum.RaumKuerzel, raum.RaumKuerzel, 40);
				conn.transactionPersist(dto);
				logger.logLn("Raum '%s' hinzugefügt.".formatted(raum.RaumKuerzel));
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
			logger.logLn("[Fehler] Konnte die CSV-Datei nicht einlesen. Prüfen sie ggf. auch die Zeichenkodierung der Datei."
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

}
