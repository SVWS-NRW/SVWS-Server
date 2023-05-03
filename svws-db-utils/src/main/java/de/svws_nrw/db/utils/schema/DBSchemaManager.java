package de.svws_nrw.db.utils.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.dto.current.svws.db.DTODBVersion;
import de.svws_nrw.db.schema.DBSchemaViews;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleTrigger;
import de.svws_nrw.db.schema.View;

/**
 * Diese Klasse stellt Hilfs-Funktionen zur Verfügung, um auf ein SVWS-Datenbank-Schema zuzugreifen und dieses zu bearbeiten.
 */
public final class DBSchemaManager {

	/// Der Datenbank-Benutzer
	private final Benutzer user;

	/// Der Status des Datenbank-Schema
	private final DBSchemaStatus status;

	/// Ein Logger, um die Abläufe in dem Schema-Manager zu loggen
	private final Logger logger;

	/// Gibt an, ob die Ausführung von Operationen bei einzelnen Fehlern abgebrochen werden sollen.
	private final boolean returnOnError;

	/// Enthält ggf. einen Fehler-String für einen zuletzt aufgetretenen Fehler
	private String lastError;

	/** Der Updater, um Datenbank-Updates durchzuführen */
	public final DBUpdater updater;

	/** Der Backup-Manager, für den Import von und den Export nach SQLite */
	public final DBBackupManager backup;

	/** String-Konstanten, die wiederholt verwendet werden */
	private static final String strOK = "[OK]";
	private static final String strError = "[Fehler]";


	/**
	 * Erstellt einen neuen DBSchema-Manager, der Schema-Operationen mithilfe des angegebenen Datenbank-Benutzer ermöglicht.
	 *
	 * @param user             der Datenbak-Benutzer
	 * @param returnOnError    gibt an, ob Operatioen bei Einzelfehlern abgebrochen werden sollen
	 * @param logger           ein Logger, um die Abläufe in dem Schema-Manager zu loggen
	 */
	private DBSchemaManager(final Benutzer user, final boolean returnOnError, final Logger logger) {
		this.user = user;
		this.status = DBSchemaStatus.read(user);
		this.returnOnError = returnOnError;
		this.logger = logger;
		this.updater = new DBUpdater(this, returnOnError);
		this.backup = new DBBackupManager(this);
	}



	/**
	 * Versucht einen neuen DB-Schema-Manager zu erstellen.
	 *
	 * @param user             der Datenbank-Benutzer
	 * @param returnOnError    gibt an, ob Operatioen bei Einzelfehlern abgebrochen werden sollen
	 * @param logger           ein Logger, um die Abläufe in dem Schema-Manager zu loggen
	 *
	 * @return der DB-Schema-Manager bei Erfolg
	 */
	public static DBSchemaManager create(final Benutzer user, final boolean returnOnError, final Logger logger) {
		return new DBSchemaManager(user, returnOnError, (logger == null) ? new Logger() : logger);
	}



	/**
	 * Gibt den Datenbank-Benutzer des Managers zurück.
	 *
	 * @return der Datenbank-Benutzer
	 */
	public Benutzer getUser() {
		return this.user;
	}


	/**
	 * Gibt den Schema-Status  (see {@link DBSchemaStatus} zurück.
	 *
	 * @return der Schema-Status von diesem Schema-Manager
	 */
	public DBSchemaStatus getSchemaStatus() {
		return this.status;
	}


	/**
	 * Gibt den vom Schema-Manager verwendeten Logger zurück.
	 *
	 * @return der Logger
	 */
	public Logger getLogger() {
		return this.logger;
	}


	/**
	 * Gibt den zuletzt gesetzten Fehler einer Operation des Schema-Managers zurück.
	 *
	 * @return der zuletzt gesetzte Fehler
	 */
	public String getLastError() {
		return lastError;
	}



	/**
	 * Führt die SQL-Skripte zum Erstellen aller Datenbank-Tabellen der angegebenen Schema-Revision
	 * aus.
	 *
	 * @param revision   die Revision des Datenbank-Schemas
	 *
	 * @return true, falls alle Tabellen erfolgreich erstellt wurden
	 */
	private boolean createAllTables(final long revision) {
		try (DBEntityManager conn = user.getEntityManager()) {
			boolean result = true;
			final var dbms = conn.getDBDriver();
			for (final SchemaTabelle tab : Schema.getTabellen(revision)) {
				logger.logLn(tab.name());
				final String script = tab.getSQL(dbms, revision);
				if (conn.executeNativeUpdate(script) == Integer.MIN_VALUE) {
					result = false;
					if (returnOnError)
						break;
				} else {
					final List<String> pkTrigger = tab.getPrimaerschluesselTriggerSQLList(dbms, revision, true);
					if (!pkTrigger.isEmpty()) {
						logger.logLn("  -> Erstelle Trigger für Auto-Inkremente");
						for (final String scriptTrigger : pkTrigger) {
							if (conn.executeNativeUpdate(scriptTrigger) == Integer.MIN_VALUE) {
								result = false;
								if (returnOnError)
									break;
							}
						}
					}
				}
			}
			return result;
		}
	}



	/**
	 * Führt die SQL-Skripte zum Erstellen aller Datenbank-Indizes der angegebenen Schema-Revision
	 * aus.
	 *
	 * @param revision   die Revision des Datenbank-Schemas
	 *
	 * @return true, falls alle Indizes erfolgreich erstellt wurden
	 */
	private boolean createAllIndizes(final long revision) {
		try (DBEntityManager conn = user.getEntityManager()) {
			boolean result = true;
			for (final SchemaTabelle tab : Schema.getTabellen(revision)) {
				for (final SchemaTabelleIndex idx : tab.indizes()) {
					logger.logLn(idx.name());
					final String script = idx.getSQL();
					if (conn.executeNativeUpdate(script) == Integer.MIN_VALUE) {
						result = false;
						if (returnOnError)
							break;
					}
				}
			}
			return result;
		}
	}



	/**
	 * Führt die SQL-Skripte zum Erstellen aller Datenbank-Trigger der angegebenen Schema-Revision
	 * aus, welche nicht zu den Auto-Inkrementen bei Primärschlüsseln gehören.
	 *
	 * @param revision   die Revision des Datenbank-Schemas
	 *
	 * @return true, falls alle Trigger erfolgreich erstellt wurden
	 */
	private boolean createAllTrigger(final long revision) {
		try (DBEntityManager conn = user.getEntityManager()) {
			boolean result = true;
			final var dbms = conn.getDBDriver();
			for (final SchemaTabelle tab : Schema.getTabellen(revision)) {
				for (final SchemaTabelleTrigger trig : tab.trigger()) {
					if (!dbms.equals(trig.dbms()))
						continue;
					if (revision < trig.revision().revision)
						continue;
					if ((trig.veraltet().revision >= 0) && (revision > trig.veraltet().revision))
						continue;
					logger.logLn(trig.name());
					final String script = trig.getSQL(conn.getDBDriver(), true);
					if (conn.executeWithJDBCConnection(script) == Integer.MIN_VALUE) {
						result = false;
						if (returnOnError)
							break;
					}
				}
			}
			return result;
		}
	}



	/**
	 * Führt die manuellen SQL-Skripte zum Erstellen eines Schemas der angegebenen Schema-Revision
	 * aus.
	 *
	 * @param revision   die Revision des Datenbank-Schemas
	 *
	 * @return true, falls alle Skripte erfolgreich ausgeführt wurden
	 */
	private boolean executeManualSQLOnCreate(final long revision) {
		try (DBEntityManager conn = user.getEntityManager()) {
			final var dbms = conn.getDBDriver();
			for (long r = 0; r <= revision; r++) {
				final SchemaRevisionUpdateSQL msqlAll = SchemaRevisionen.get(revision).getUpdater();
				if ((!msqlAll.runFirst(conn, logger)) && returnOnError)
					return false;
				for (int i = 0; i < msqlAll.size(); i++) {
					final String script = msqlAll.getSQL(dbms, i);
					if ((script == null) || "".equals(script))
						continue; // should not happen
					logger.logLn(msqlAll.getKommentar(i));
					if ((conn.executeNativeUpdate(script) == Integer.MIN_VALUE) && (returnOnError))
						return false;
				}
				if ((!msqlAll.runLast(conn, logger)) && returnOnError)
					return false;
			}
			return true;
		}
	}



	/**
	 * Erstellt die Views für das Schemas der angegebenen Schema-Revision.
	 *
	 * @param revision   die Revision des Datenbank-Schemas
	 *
	 * @return true, falls alle Skripte erfolgreich ausgeführt wurden
	 */
	private boolean executeSQLCreateViews(final long revision) {
		try (DBEntityManager conn = user.getEntityManager()) {
			boolean result = true;
			final List<View> views = DBSchemaViews.getInstance().getViewsActive(revision);
			for (final View view : views) {
				logger.logLn(view.name);
				if (conn.executeNativeUpdate(view.getSQLCreate(conn.getDBDriver())) == Integer.MIN_VALUE) {
					result = false;
					if (returnOnError)
						break;
				}
			}
			return result;
		}
	}



	/**
	 * Führt die SQL-Finalisierungs-Skripte beim Erstellen eines Schemas der angegebenen Schema-Revision
	 * aus.
	 *
	 * @param revision   die Revision des Datenbank-Schemas
	 *
	 * @return true, falls alle Skripte erfolgreich ausgeführt wurden
	 */
	private boolean createDefaultSVWSBenutzer(final long revision) {
		try (DBEntityManager conn = user.getEntityManager()) {
			boolean result = true;
			final List<String> sqlList = Schema.getCreateBenutzerSQL(revision);
			for (final String sql : sqlList) {
				logger.logLn(sql);
				if (conn.executeNativeUpdate(sql) == Integer.MIN_VALUE) {
					result = false;
					if (returnOnError)
						break;
				}
			}
			return result;
		}
	}


	/**
	 * Setzt die Datenbank-Revision auf die angegebene Revision
	 *
	 * @param revision   die zu setzende Revision, bei -1 wird die neueste Revision gesetzt
	 *
	 * @return true, falls die Revision erfolgreich gesetzt wurde, sonst false
	 */
	public boolean setDBRevision(final long revision) {
		try (DBEntityManager conn = user.getEntityManager()) {
			final long rev = (revision == -1) ? SchemaRevisionen.maxRevision.revision : revision;
			if (rev == -1)
				return false;
			final DTODBVersion oldObj = conn.querySingle(DTODBVersion.class);
			final DTODBVersion newObj = new DTODBVersion(rev, (rev > SchemaRevisionen.maxRevision.revision) || ((oldObj != null) && (oldObj.IsTainted)));
			if (oldObj == null) {
				conn.persist(newObj);
			} else {
				conn.replace(oldObj, newObj);
			}
			return true;
		}
	}


	/**
	 * Erstellt ein SVWS-Datenbank-Schema der angegebenen Revision
	 *
	 * @param revision    die Revision für das SVWS-DB-Schema
	 * @param createUser  gibt an, ob Default-SVWS-Benutzer angelegt werden sollen
	 *
	 * @return true, wenn das Schema erfolgreich erstellt wurde, sonst false
	 */
	public boolean createSVWSSchema(final long revision, final boolean createUser) {
		logger.logLn("- Erstelle Tabellen für die aktuelle DB-Revision... ");
		logger.modifyIndent(2);
		boolean success = createAllTables(revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn(strError);
			if (returnOnError) return false;
		}
		logger.logLn(strOK);

		logger.logLn("- Erstelle Indizes für die aktuelle DB-Revision... ");
		logger.modifyIndent(2);
		success = createAllIndizes(revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn(strError);
			if (returnOnError) return false;
		}
		logger.logLn(strOK);

		logger.logLn("- Erstelle Trigger für die aktuelle DB-Revision... ");
		logger.modifyIndent(2);
		success = createAllTrigger(revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn(strError);
			if (returnOnError) return false;
		}
		logger.logLn(strOK);

		logger.logLn("- Schreibe die Daten der Core-Types");
		logger.modifyIndent(2);
		success = updater.coreTypes.update(false, revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn(strError);
			if (returnOnError) return false;
		}
		logger.logLn(strOK);

		logger.logLn("- Führe manuelle SQL-Befehle aus: ");
		logger.modifyIndent(2);
		success = executeManualSQLOnCreate(revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn(strError);
			if (returnOnError) return false;
		}
		logger.logLn(strOK);

		logger.logLn("- Erstelle Views: ");
		logger.modifyIndent(2);
		success = executeSQLCreateViews(revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn(strError);
			if (returnOnError) return false;
		}
		logger.logLn(strOK);

		if (createUser) {
			logger.logLn("- Lege Default-Benutzer an: ");
			logger.modifyIndent(2);
			success = createDefaultSVWSBenutzer(revision);
			logger.modifyIndent(-2);
			if (!success) {
				logger.logLn(strError);
				if (returnOnError) return false;
			}
			logger.logLn(strOK);
		}

		logger.logLn("- Setze die DB-Revision in der neu erzeugten Datenbank: ");
		logger.modifyIndent(2);
		success = setDBRevision(revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn(strError);
			if (returnOnError) return false;
		}
		logger.logLn(strOK);
		return true;
	}


	/**
	 * Verwirft das SVWS-Datenbank-Schema, wenn das DBMS keine Unterstützung zum Verwerfen von
	 * mehreren Tabellen hat. Diese Implementierung wird für SQLite- und MDB-Access-Datenbanken verwendet.
	 *
	 * @param conn    die Datenbankverbindung
	 * @param driver  die Informationen zum verwendeten Datenbank-Treiber (s.o.)
	 *
	 * @return true, falls die Operationen erfolgreich waren und ansonsten false
	 */
	private boolean dropSVWSSchemaMultipleStatements(final DBEntityManager conn, final DBDriver driver) {
		// no support to drop multiple tables in one drop statement - drop table order is important due to foreign key constraints
		boolean success = true;
		// Bestimme die aktuelle Revision der Datenbank
		final DBSchemaVersion version = status.getVersion();
		final long revision = version.getRevisionOrDefault(0);
		final List<SchemaTabelle> tabellen = Schema.getTabellen(revision);
		Collections.reverse(tabellen);
		for (final SchemaTabelle tab : tabellen) {
			// Prüfe bei einer Lecagy-Schild-DB, ob eine Tabelle für die Migration vorliegt - nur diese sollen verworfen werden
			if (!version.isValid() && !tab.migrate())
				continue;
			if (!status.hasTable(tab.name()))
				continue;
			logger.log(tab.name() + "... ");
			final String sql = "DROP TABLE " + ((driver == DBDriver.SQLITE) ? "IF EXISTS " : "") + tab.name() + ";";
			final int result = conn.executeWithJDBCConnection(sql);
			if (result == Integer.MIN_VALUE) {
				logger.logLn(0, " " + strError);
				success = false;
			} else {
				logger.logLn(0, " " + strOK);
			}
		}

		// berücksichtige auch Tabellen, die bei der Revision eigentlich nicht definiert oder nicht für die Migration vorgemerkt sind
		status.update();
		for (final String tabname : status.getTabellen()) {
			logger.log(tabname + "... ");
			final String sql = "DROP TABLE " + ((driver == DBDriver.SQLITE) ? "IF EXISTS " : "") + tabname + ";";
			final int result = conn.executeWithJDBCConnection(sql);
			if (result == Integer.MIN_VALUE) {
				logger.logLn(0, " " + strError);
				success = false;
			} else {
				logger.logLn(0, " " + strOK);
			}
		}
		return success;
	}

	/**
	 * Verwirft das SVWS-Datenbank-Schema.
	 *
	 * @return true, wenn das Schema erfolgreich verworfen wurde,
	 *         false wenn dabei Fehler aufgetreten sind.
	 */
	public boolean dropSVWSSchema() {
		try (DBEntityManager conn = user.getEntityManager()) {
			boolean success = true;
			logger.logLn("- Verwerfe Tabellen...");
			logger.modifyIndent(2);

			final DBDriver driver = conn.getDBDriver();
			if ((driver == DBDriver.MDB) || (driver == DBDriver.SQLITE)) {
				success = dropSVWSSchemaMultipleStatements(conn, driver);
			} else if ((driver == DBDriver.MARIA_DB) || (driver == DBDriver.MYSQL)) {
				// drop all existing in one statement - we do not need to cope with foreign key constraints
				final List<String> tableNames = status.getTabellen();
				if (!tableNames.isEmpty()) {
					logger.log("alle auf einmal... ");
					final List<String> sql = new ArrayList<>();
					sql.add("SET foreign_key_checks = 0");
					sql.add(status.getTabellen().stream().collect(Collectors.joining(",", "DROP TABLE IF EXISTS ", ";")));
					sql.add("SET foreign_key_checks = 1");
					try {
						conn.executeBatchWithJDBCConnection(sql);
						logger.logLn(0, " " + strOK);
					} catch (final DBException e) {
						e.printStackTrace();
						logger.logLn(0, " " + strError);
						success = false;
					}
				}
			} else if (driver == DBDriver.MSSQL) {
				// drop all existing in one statement - we do not need to cope with foreign key constraints
				final List<String> tableNames = status.getTabellen();
				if (!tableNames.isEmpty()) {
					logger.log("alle auf einmal... ");
					final String sql = status.getTabellen().stream().collect(Collectors.joining(",", "DROP TABLE IF EXISTS ", ";"));
					final int result = conn.executeWithJDBCConnection(sql);
					if (result == Integer.MIN_VALUE) {
						logger.logLn(0, " " + strError);
						success = false;
					} else {
						logger.logLn(0, " " + strOK);
					}
				}
			}
			logger.modifyIndent(-2);
			status.update();
			return success;
		}
	}

}
