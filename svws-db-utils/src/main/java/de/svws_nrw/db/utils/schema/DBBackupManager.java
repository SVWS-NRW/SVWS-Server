package de.svws_nrw.db.utils.schema;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.dto.current.svws.db.DTODBVersion;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaTabelle;

/**
 * Diese Klasse stellt Methoden für den Export in eine SQLite-Datenbank und
 * den Import aus einer SQLite-Datenbank zur Verfügung.
 */
public class DBBackupManager {

	/** Der Schema-Manager, welcher für den Import bzw. Export verwendet wird */
	private final DBSchemaManager schemaManager;

	/** Ein Logger, um die Abläufe bei dem Update-Prozess zu loggen */
	private final Logger logger;


	/**
	 * Erzeugt einen neuen {@link DBBackupManager}.
	 *
	 * @param schemaManager   der Schema-Manager, welcher verwendet wird
	 */
	DBBackupManager(final DBSchemaManager schemaManager) {
		this.schemaManager = schemaManager;
		this.logger = schemaManager.getLogger();
	}


	/**
	 * Diese Methode führt einen import dieser SQLite-Datenbank
	 * in die SVWS-Datenbank aus. Das Schema in der Zieldatenbank wird dabei neu angelegt
	 *
	 * @param tgtConfig            die Datenbank-Konfiguration für den Zugriff auf die SVWS-Server-Datenbank
	 * @param tgtRootUser          der Benutzername des Benutzers der mit den benötigten root-Rechten zur Schema-Verwaltung ausgestattet ist
	 * @param tgtRootPW            das root-Kennwort für den Zugriff auf die Zieldatenbank
	 * @param maxUpdateRevision    die Revision, bis zu welcher die Zieldatenbank aktualisiert wird
	 * @param devMode              gibt an, ob auch Schema-Revision erlaubt werden, die nur für Entwickler zur Verfügung stehen
	 * @param logger               ein Logger, welcher den Export loggt.
	 *
	 * @return true, falls der Import erfolgreich durchgeführt wurde
	 */
	public boolean importDB(final DBConfig tgtConfig, final String tgtRootUser, final String tgtRootPW, final long maxUpdateRevision, final boolean devMode, final Logger logger) {
		try (DBEntityManager conn = schemaManager.getUser().getEntityManager()) {
			boolean success = true;
			final long timeStart = System.currentTimeMillis();
			logger.logLn("Exportiere aus der SQLite-Datenbank " + conn.getDBLocation());
			logger.modifyIndent(2);
			DBSchemaManager tgtManager = null;
			final String tgtSchema = tgtConfig.getDBSchema();
			if ((tgtSchema == null) || "".equals(tgtSchema.trim())) {
				logger.logLn("-> Import fehlgeschlagen! (Schemaname darf nicht null oder leer sein)");
				logger.modifyIndent(-2);
				return false;
			}
			if (!SVWSKonfiguration.get().lockSchema(tgtSchema)) {
				logger.logLn("-> Import fehlgeschlagen! (Schema ist aktuell gesperrt und kann daher nicht überschrieben werden)");
				logger.modifyIndent(-2);
				return false;
			}

			try {
				if (((tgtConfig.getDBDriver() == DBDriver.MARIA_DB) || (tgtConfig.getDBDriver() == DBDriver.MYSQL)) && ("root".equals(tgtConfig.getUsername())))
					throw new DBException("Der Benutzer \"root\" ist kein zulässiger SVWS-Admin-Benutzer für MYSQL / MARIA_DB");

				if ((tgtConfig.getDBDriver() == DBDriver.MSSQL) && ("sa".equals(tgtConfig.getUsername())))
					throw new DBException("Der Benutzer \"sa\" ist kein zulässiger SVWS-Admin-Benutzer für MS SQL Server");

				if (!DBRootManager.recreateDB(tgtConfig, tgtRootUser, tgtRootPW, logger))
					throw new DBException("Fehler beim Anlegen des Schemas und des Admin-Benutzers");

				logger.logLn("-> Bestimme die Revision der QuellDatenbank...");
				logger.modifyIndent(2);
				final DTODBVersion version = conn.querySingle(DTODBVersion.class);
				logger.logLn(" - Revision " + version.Revision);
				logger.modifyIndent(-2);

				logger.log("-> Verbinde zum Ziel-Schema mit dem Datenbank-Test-Benutzer...");
				final Benutzer tgtUser = Benutzer.create(tgtConfig);
				try (DBEntityManager tgtConn = tgtUser.getEntityManager()) {
					if (tgtConn == null) {
						logger.logLn(0, " [Fehler]");
						logger.log(LogLevel.ERROR, "Fehler bei der Erstellung der Datenbank-Verbindung (driver='" + tgtConfig.getDBDriver() + "', schema='" + tgtConfig.getDBSchema() + "', location='" + tgtConfig.getDBLocation() + "', user='" + tgtConfig.getUsername() + "')" + System.lineSeparator());
						throw new DBException("Fehler beim Verbinden zur Zieldatenbank");
					}
					logger.logLn(0, " [OK]");
					logger.log(LogLevel.INFO, "Datenbank-Verbindung erfolgreich aufgebaut (driver='" + tgtConfig.getDBDriver() + "', schema='" + tgtConfig.getDBSchema() + "', location='" + tgtConfig.getDBLocation() + "', user='" + tgtConfig.getUsername() + "')" + System.lineSeparator());

					tgtManager = DBSchemaManager.create(tgtUser, true, logger);
					if (tgtManager == null) {
						logger.logLn(0, " [Fehler]");
						throw new DBException("Fehler beim Verbinden zur Zieldatenbank");
					}
					logger.logLn(0, " [OK]");

					logger.logLn("-> Erstelle für den Import in die Ziel-DB ein SVWS-Schema der entsprechenden Revision " + version.Revision);
					logger.modifyIndent(2);
					boolean result = tgtManager.createSVWSSchema(version.Revision, false);
					logger.modifyIndent(-2);
					if (!result) {
						logger.logLn(" [Fehler]");
						throw new DBException("Fehler beim Erstelen des Schemas mit der Revision " + version.Revision);
					}
					logger.logLn("[OK]");

					logger.logLn("-> Kopiere die Daten aus der Quell-DB in die Ziel-DB...");
					logger.modifyIndent(2);
					result = expimpCopyFrom(tgtManager, version.Revision);
					logger.modifyIndent(-2);
					if (!result) {
						logger.logLn(" [Fehler]");
						throw new DBException("Grober Fehler beim Kopieren der zu migrierenden Daten");
					}
					logger.logLn("[OK]");

					if (maxUpdateRevision != 0) {
						logger.logLn("-> Aktualisiere die Ziel-DB auf die " + ((maxUpdateRevision < 0) ? "neueste " : "") + "DB-Revision" + ((maxUpdateRevision > 0) ? " " + maxUpdateRevision : "") + "...");
						logger.modifyIndent(2);
						result = tgtManager.updater.update(maxUpdateRevision < 0 ? -1 : maxUpdateRevision, devMode, false);
						logger.modifyIndent(-2);
						if (!result) {
							logger.logLn("[Fehler]");
							throw new DBException("Fehler beim Aktualsieren der Ziel-DB");
						}
						logger.logLn("[OK]");
					}

					logger.logLn("-> Speicherbelegung (frei/verfügbar/gesamt): " + (Math.round(Runtime.getRuntime().freeMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().totalMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().maxMemory() / 10000000.0) / 100.0) + "G");
					logger.logLn("-> Import erfolgreich in " + ((System.currentTimeMillis() - timeStart) / 1000.0) + " Sekunden abgeschlossen.");
				}
			} catch (final DBException e) {
				logger.logLn("-> Import fehlgeschlagen! (" + e.getMessage() + ")");
				success = false;
			} finally {
				System.gc();
			}

			if (!SVWSKonfiguration.get().unlockSchema(tgtSchema)) {
				logger.logLn("-> Fehler beim Freigeben des Datenbank-Schemas. Schema ist nicht gesperrt - dies wird an dieser Stelle nicht erwartet!");
				success = false;
			}

			logger.modifyIndent(-2);
			return success;
		}
	}


	/**
	 * Diese Methode führt einen Export von der SVWS-Datenbank in die SQLite-Datenbank
	 * mit dem angegebenen Dateinamen aus.
	 *
	 * @param filename    der Dateiname für die SQLite-Datenbank
	 * @param logger      ein Logger, welcher den Export loggt.
	 *
	 * @return true, falls der Export erfolgreich durchgeführt wurde
	 */
	public boolean exportDB(final String filename, final Logger logger) {
		try (DBEntityManager conn = schemaManager.getUser().getEntityManager()) {
			final DBConfig tgtConfig = new DBConfig(DBDriver.SQLITE, filename, null, false, null, null, true, true);

			boolean success = true;
			final long timeStart = System.currentTimeMillis();
			logger.logLn("Exportiere von in die SQLite-Datenbank " + filename);
			logger.modifyIndent(2);
			DBSchemaManager tgtManager = null;
			try {
				if (!DBRootManager.recreateDB(tgtConfig, null, null, logger))
					throw new DBException("Fehler beim Anlegen des Schemas in der SQlite-Export-Datei");

				logger.log("-> Verbinde zur SQLite-Export-Datenbank...");
				final Benutzer tgtUser = Benutzer.create(tgtConfig);
				try (DBEntityManager tgtConn = tgtUser.getEntityManager()) {
					if (tgtConn == null) {
						logger.logLn(0, " [Fehler]");
						logger.log(LogLevel.ERROR, "Fehler bei der Erstellung der Datenbank-Verbindung (driver='" + tgtConfig.getDBDriver() + "', location='" + tgtConfig.getDBLocation() + "', user='" + tgtConfig.getUsername() + "')" + System.lineSeparator());
						throw new DBException("Fehler beim Verbinden zur SQLite-Export-Datenbank");
					}
					logger.logLn(0, " [OK]");
					logger.log(LogLevel.INFO, "Datenbank-Verbindung erfolgreich aufgebaut (driver='" + tgtConfig.getDBDriver() + "', location='" + tgtConfig.getDBLocation() + "', user='" + tgtConfig.getUsername() + "')" + System.lineSeparator());

					tgtManager = DBSchemaManager.create(tgtUser, true, logger);
					if (tgtManager == null) {
						logger.logLn(0, " [Fehler]");
						throw new DBException("Fehler beim Verbinden zur SQLite-Export-Datenbank");
					}
					logger.logLn(0, " [OK]");

					logger.logLn("-> Bestimme die Revision der QuellDatenbank...");
					logger.modifyIndent(2);
					final DTODBVersion version = conn.querySingle(DTODBVersion.class);
					logger.logLn(" - Revision " + version.Revision);
					logger.modifyIndent(-2);

					logger.logLn("-> Erstelle für den Export ein SVWS-Schema in der SQLite-Datenbank");
					logger.modifyIndent(2);
					boolean result = tgtManager.createSVWSSchema(version.Revision, false);
					logger.modifyIndent(-2);
					if (!result) {
						logger.logLn(" [Fehler]");
						throw new DBException("Fehler beim Erstelen des Schemas");
					}
					logger.logLn("[OK]");

					logger.logLn("-> Kopiere die Daten aus der Quell-DB in die Ziel-DB...");
					logger.modifyIndent(2);
					result = expimpCopyFrom(tgtManager, version.Revision);
					logger.modifyIndent(-2);
					if (!result) {
						logger.logLn(" [Fehler]");
						throw new DBException("Grober Fehler beim Kopieren der zu migrierenden Daten");
					}
					logger.logLn("[OK]");

					logger.logLn("-> Speicherbelegung (frei/verfügbar/gesamt): " + (Math.round(Runtime.getRuntime().freeMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().totalMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().maxMemory() / 10000000.0) / 100.0) + "G");
					logger.logLn("-> Export erfolgreich in " + ((System.currentTimeMillis() - timeStart) / 1000.0) + " Sekunden abgeschlossen.");
				}
			} catch (final DBException e) {
				logger.logLn("-> Export fehlgeschlagen! (" + e.getMessage() + ")");
				success = false;
			} finally {
				System.gc();
			}

			logger.modifyIndent(-2);
			return success;
		}
	}



	/**
	 * Kopiert die Daten von dem Schema-Managers in das Schema des angegebenen Ziel-Schema-Managers.
	 * Die Schema-Revisionen von Quelle und Ziel müssen übereinstimmen.
	 *
	 * @param tgtManager   der Schema-Manager der Ziel-Datenbank
	 * @param rev          die gemeinsame Revision der beiden Schemata
	 *
	 * @return true, falls die Daten erfolgreich kopiert wurden und sonst false
	 */
	private boolean expimpCopyFrom(final DBSchemaManager tgtManager, final long rev) {
		// Durchwandere alle Tabellen in der geeigneten Reihenfolge, so dass Foreign-Key-Constraints erfüllt werden
		for (final SchemaTabelle tab : Schema.getTabellen(rev)) {
			// Prüfe, ob die Tabelle bei dem Import/Export beachtet werden soll, wenn nicht dann übespringe sie
			if (!tab.importExport())
				continue;

			@SuppressWarnings("resource")
			final
			DBEntityManager srcConn = schemaManager.getUser().getEntityManager();
			logger.logLn("Tabelle " + tab.name() + ":");
			logger.modifyIndent(2);

			// Lese alle Datensätze aus der Quell-Tabelle
			logger.log("- Lese Datensätze: ");
			final String sql = tab.getSpalten(rev).stream()
					.map(t -> t.name())
					.collect(Collectors.joining(", ", "SELECT ", " FROM " + tab.name()));
			List<Object[]> entities = srcConn.query(sql);
			if (entities == null) {
				logger.logLn(LogLevel.ERROR, 0, "[FEHLER] - Kann die Datensätze nicht einlesen - Überspringe die Tabelle");
				continue;
			}
			logger.logLn(0, entities.size() + " Datensätze eingelesen (Freier Speicher: " + (Math.round(Runtime.getRuntime().freeMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().totalMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().maxMemory() / 10000000.0) / 100.0) + "G)");

			// Wenn keine Daten vorhanden sind, dann brauchen auch keine geschrieben zu werden...
			if (entities.size() == 0) {
				logger.modifyIndent(-2);
				continue;
			}

			// Schreibe die Datensätze in die Zieltabelle
			logger.logLn("- Schreibe " + entities.size() + " Datensätze: ");
			logger.modifyIndent(2);
			@SuppressWarnings("resource")
			final
			DBEntityManager tgtConn = tgtManager.getUser().getEntityManager();
			// Versuche zunächst in Blöcken von 10000 Datensätzen zu schreiben, diese werden je nach Erfolg später noch unterteilt...
			int write_errors = 0;
			final LinkedList<Map.Entry<Integer, Integer>> ranges = new LinkedList<>();
			for (int i = 0; i <= ((entities.size() - 1) / 10000); i++) {
				final int first = i * 10000;
				int last = (i + 1) * 10000 - 1;
				if (last >= entities.size())
					last = entities.size() - 1;
				ranges.add(Map.entry(first, last));
			}
			while (!ranges.isEmpty()) {
				final Map.Entry<Integer, Integer> range = ranges.removeFirst();
				if (tgtConn.insertRangeNative(tab.name(), tab.getSpalten(rev).stream().map(col -> col.name()).toList(), entities, range.getKey(), range.getValue())) {
					if (range.getKey().equals(range.getValue()))
						logger.logLn("Datensatz " + range.getKey() + " erfolgreich geschrieben. (Freier Speicher: " + (Math.round(Runtime.getRuntime().freeMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().totalMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().maxMemory() / 10000000.0) / 100.0) +  "G)");
					else
						logger.logLn("Datensätze " + range.getKey() + "-" + range.getValue() + " erfolgreich geschrieben. (Freier Speicher: " + (Math.round(Runtime.getRuntime().freeMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().totalMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().maxMemory() / 10000000.0) / 100.0) +  "G)");
				} else {
					if (range.getKey().equals(range.getValue())) {
						logger.logLn(LogLevel.ERROR, "Datensatz " + range.getKey() + " konnte nicht geschrieben werden - Datensatz wird übersprungen.");
						logger.logLn(LogLevel.ERROR, "[FEHLER] " + entities.get(range.getKey()));
						write_errors++;
					} else {
						logger.logLn("Datensätze " + range.getKey() + "-" + range.getValue() + " konnten nicht geschrieben werden geschrieben - Teile den Block auf und versuche die Teilblöcke zu schreiben.");
						// Teile den Block auf
						int step = (range.getValue() - range.getKey() + 1) / 10;
						if (step < 1)
							step = 1;
						for (int last = range.getValue(); last >= range.getKey(); last -= step) {
							final int first = last - step + 1;
							ranges.addFirst(Map.entry(first >= range.getKey() ? first : range.getKey(), last));
						}
					}
				}
			}
			logger.modifyIndent(-2);
			logger.logLn("" + (entities.size() - write_errors) + " Datensätze geschrieben, " + write_errors + " fehlerhafte Datensätze übersprungen.");
			logger.modifyIndent(-2);
			entities = null;
			System.gc();
		}
		return true;
	}

}
