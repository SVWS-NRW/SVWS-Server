package de.svws_nrw.db.utils.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.dto.current.schema.DTOSchemaRevision;
import de.svws_nrw.db.schema.DBSchemaViews;
import de.svws_nrw.db.schema.Schema;
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
	 * @param conn       die Datenbank-Verbindung mit aktiver Transaktion
	 * @param revision   die Revision des Datenbank-Schemas
	 *
	 * @return true, falls alle Tabellen erfolgreich erstellt wurden
	 */
	private boolean createAllTables(final DBEntityManager conn, final long revision) {
		boolean result = true;
		final var dbms = conn.getDBDriver();
		for (final SchemaTabelle tab : Schema.getTabellen(revision)) {
			logger.logLn(tab.name());
			final String script = tab.getSQL(dbms, revision);
			final boolean success = conn.transactionNativeUpdate(script) != Integer.MIN_VALUE;
			conn.transactionFlush();
			if (!success) {
				result = false;
				if (returnOnError)
					break;
			} else {
				final List<String> pkTrigger = tab.getPrimaerschluesselTriggerSQLList(dbms, revision, true);
				if (!pkTrigger.isEmpty()) {
					logger.logLn("  -> Erstelle Trigger für Auto-Inkremente");
					for (final String scriptTrigger : pkTrigger) {
						if (conn.transactionNativeUpdate(scriptTrigger) == Integer.MIN_VALUE) {
							result = false;
							if (returnOnError)
								break;
						}
						conn.transactionFlush();
					}
				}
			}
		}
		return result;
	}



	/**
	 * Führt die SQL-Skripte zum Erstellen aller Datenbank-Indizes der angegebenen Schema-Revision
	 * aus.
	 *
	 * @param conn       die Datenbank-Verbindung mit aktiver Transaktion
	 * @param revision   die Revision des Datenbank-Schemas
	 *
	 * @return true, falls alle Indizes erfolgreich erstellt wurden
	 */
	private boolean createAllIndizes(final DBEntityManager conn, final long revision) {
		boolean result = true;
		for (final SchemaTabelle tab : Schema.getTabellen(revision)) {
			for (final SchemaTabelleIndex idx : tab.indizes()) {
				logger.logLn(idx.name());
				final String script = idx.getSQL();
				if (conn.transactionNativeUpdate(script) == Integer.MIN_VALUE) {
					result = false;
					if (returnOnError)
						break;
				}
				conn.transactionFlush();
			}
		}
		return result;
	}


	/**
	 * Erstellt die Views für das Schemas der angegebenen Schema-Revision.
	 *
	 * @param conn       die Datenbank-Verbindung mit aktiver Transaktion
	 * @param revision   die Revision des Datenbank-Schemas
	 *
	 * @return true, falls alle Skripte erfolgreich ausgeführt wurden
	 */
	private boolean executeSQLCreateViews(final DBEntityManager conn, final long revision) {
		boolean result = true;
		final List<View> views = DBSchemaViews.getInstance().getViewsActive(revision);
		for (final View view : views) {
			logger.logLn(view.name);
			if (conn.transactionNativeUpdate(view.getSQLCreate(conn.getDBDriver())) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}
			conn.transactionFlush();
		}
		return result;
	}



	/**
	 * Führt die SQL-Finalisierungs-Skripte beim Erstellen eines Schemas der angegebenen Schema-Revision
	 * aus.
	 *
	 * @param conn       die Datenbank-Verbindung mit aktiver Transaktion
	 * @param revision   die Revision des Datenbank-Schemas
	 *
	 * @return true, falls alle Skripte erfolgreich ausgeführt wurden
	 */
	private boolean createDefaultSVWSBenutzer(final DBEntityManager conn, final long revision) {
		boolean result = true;
		final List<String> sqlList = Schema.getCreateBenutzerSQL(revision);
		for (final String sql : sqlList) {
			logger.logLn(sql);
			if (conn.transactionNativeUpdate(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}
			conn.transactionFlush();
		}
		return result;
	}


	/**
	 * Setzt die Datenbank-Revision auf die angegebene Revision. Dabei wird die Transaktion aus der übergebenen
	 * Datenbankverbindung genutzt.
	 *
	 * @param conn       die Datenbankverbindung
	 * @param revision   die zu setzende Revision, bei -1 wird die neueste Revision gesetzt
	 *
	 * @return true, falls die Revision erfolgreich gesetzt wurde, sonst false
	 */
	public static boolean setDBRevision(final DBEntityManager conn, final long revision) {
		final long rev = (revision == -1) ? SchemaRevisionen.maxRevision.revision : revision;
		if (rev == -1)
			return false;
		final DTOSchemaRevision oldObj = conn.querySingle(DTOSchemaRevision.class);
		final DTOSchemaRevision newObj = new DTOSchemaRevision(rev, (rev > SchemaRevisionen.maxRevision.revision) || ((oldObj != null) && (oldObj.IsTainted)));
		if (oldObj == null) {
			if (!conn.transactionPersist(newObj))
				return false;
		} else {
			if (!conn.transactionReplace(oldObj, newObj))
				return false;
		}
		conn.transactionFlush();
		return true;
	}


	/**
	 * Führt die SQL-Skripte zum Erstellen aller Datenbank-Trigger der angegebenen Schema-Revision
	 * aus, welche nicht zu den Auto-Inkrementen bei Primärschlüsseln gehören.
	 *
	 * Die Datenbank-Transaktion muss außerhalb dieser Methoden gehandhabt werden.
	 *
	 * @param conn            die Datenbank-Verbindung
	 * @param logger          der Logger
	 * @param revision        die Revision des Datenbank-Schemas
	 * @param returnOnError   gibt an, ob die Funktion bei einem Fehler sofort zurückkehrt oder nicht
	 *
	 * @return true, falls alle Trigger erfolgreich erstellt wurden
	 */
	public static boolean createAllTrigger(final DBEntityManager conn, final Logger logger, final long revision, final boolean returnOnError) {
		logger.logLn("- Erstelle Trigger für die aktuelle DB-Revision... ");
		logger.modifyIndent(2);
		boolean result = true;
		final var dbms = conn.getDBDriver();
		for (final SchemaTabelle tab : Schema.getTabellen(revision)) {
			for (final SchemaTabelleTrigger trig : tab.trigger()) {
				if (!dbms.equals(trig.dbms()))
					continue;
				if (revision < trig.revision().revision)
					continue;
				if ((trig.veraltet().revision >= 0) && (revision >= trig.veraltet().revision))
					continue;
				logger.logLn(trig.name());
				final String script = trig.getSQL(conn.getDBDriver(), true);
				if (conn.transactionNativeUpdate(script) == Integer.MIN_VALUE) {
					result = false;
					if (returnOnError)
						break;
				}
			}
		}
		conn.transactionFlush();
		logger.modifyIndent(-2);
		if (!result) {
			logger.logLn(strError);
			if (returnOnError)
				return false;
		}
		logger.logLn(strOK);
		return result;
	}


	/**
	 * Erstellt ein SVWS-Datenbank-Schema der angegebenen Revision
	 *
	 * @param conn            die Datenbank-Verbindung zum Erstellen des Schemas
	 * @param revision        die Revision für das SVWS-DB-Schema
	 * @param createUser      gibt an, ob Default-SVWS-Benutzer angelegt werden sollen
	 * @param createTrigger   gibt an, ob auch die Trigger für die Datenbank-Revision erstellt werden sollen
	 *                        (oder ob dies später seperat aufgerufen wird)
	 *
	 * @return true, wenn das Schema erfolgreich erstellt wurde, sonst false
	 */
	private boolean createSVWSSchema(final DBEntityManager conn, final long revision, final boolean createUser, final boolean createTrigger) {
		logger.logLn("- Erstelle Tabellen für die aktuelle DB-Revision... ");
		logger.modifyIndent(2);
		boolean success = createAllTables(conn, revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn(strError);
			if (returnOnError)
				return false;
		}
		logger.logLn(strOK);

		logger.logLn("- Erstelle Indizes für die aktuelle DB-Revision... ");
		logger.modifyIndent(2);
		success = createAllIndizes(conn, revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn(strError);
			if (returnOnError)
				return false;
		}
		logger.logLn(strOK);

		if (createTrigger) {
			logger.logLn("- Erstelle Trigger für die aktuelle DB-Revision... ");
			logger.modifyIndent(2);
			success = createAllTrigger(conn, logger, revision, returnOnError);
			logger.modifyIndent(-2);
			if (!success) {
				logger.logLn(strError);
				if (returnOnError)
					return false;
			}
			logger.logLn(strOK);
		}

		logger.logLn("- Schreibe die Daten der Core-Types");
		logger.modifyIndent(2);
		success = updater.coreTypes.update(conn, false, revision);
		conn.transactionFlush();
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn(strError);
			if (returnOnError)
				return false;
		}
		logger.logLn(strOK);

		logger.logLn("- Erstelle Views: ");
		logger.modifyIndent(2);
		success = executeSQLCreateViews(conn, revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn(strError);
			if (returnOnError)
				return false;
		}
		logger.logLn(strOK);

		if (createUser) {
			logger.logLn("- Lege Default-Benutzer an: ");
			logger.modifyIndent(2);
			success = createDefaultSVWSBenutzer(conn, revision);
			logger.modifyIndent(-2);
			if (!success) {
				logger.logLn(strError);
				if (returnOnError)
					return false;
			}
			logger.logLn(strOK);
		}

		logger.logLn("- Setze die DB-Revision in der neu erzeugten Datenbank: ");
		logger.modifyIndent(2);
		success = setDBRevision(conn, revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn(strError);
			if (returnOnError)
				return false;
		}
		logger.logLn(strOK);
		return true;
	}


	/**
	 * Erstellt ein SVWS-Datenbank-Schema der angegebenen Revision
	 *
	 * @param user            der Datenbank-Benutzer über welchen eine Datenbank-Verbindung zum Erstellen des Schemas aufgebaut wird
	 * @param revision        die Revision für das SVWS-DB-Schema
	 * @param createUser      gibt an, ob Default-SVWS-Benutzer angelegt werden sollen
	 * @param createTrigger   gibt an, ob auch die Trigger für die Datenbank-Revision erstellt werden sollen
	 *                        (oder ob dies später seperat aufgerufen wird)
	 *
	 * @throws DBException falls das Erstellen des Schemas fehlschlägt
	 */
	public void createSVWSSchema(final Benutzer user, final long revision, final boolean createUser, final boolean createTrigger) throws DBException {
		logger.logLn("-> Erstelle in der Ziel-DB ein SVWS-Schema der Revision " + revision);
		logger.modifyIndent(2);
		boolean result = true;
		try (DBEntityManager conn = user.getEntityManager()) {
			try {
				conn.transactionBegin();
				result = createSVWSSchema(conn, revision, createUser, createTrigger);
				if (!result)
					throw new DBException("Fehler beim Erstellen des Schemas");
				if (!conn.transactionCommit())
					throw new DBException("Fehler beim Erstellen des Schemas - Datenbank-Transaktion konnte nicht abgeschlossen werden.");
			} catch (final Exception e) {
				logger.logLn(" " + strError);
				if (e instanceof DBException)
					throw e;
				throw new DBException("Unerwarteter Fehler beim Erstellen des Schemas: " + e.getMessage());
			} finally {
				logger.modifyIndent(-2);
				conn.transactionRollback();
			}
		}
		logger.logLn(strOK);
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


	/**
	 * Diese Methode erstellt in der durch tgtConfig beschriebene SVWS-Server-Datenbank ein neues Schema.
	 *
	 * @param tgtConfig            die Datenbank-Konfiguration für den Zugriff auf die SVWS-Server-Datenbank
	 * @param tgtRootUser          der Benutzername eines Benutzers, der mit den Rechten zum Verwalten der Datenbankschemata ausgestattet ist.
	 * @param tgtRootPW            das root-Kennwort für den Zugriff auf die Zieldatenbank
	 * @param maxUpdateRevision    die Revision, bis zu welcher die Zieldatenbank aktualisiert wird
	 * @param logger               ein Logger, welcher das Erstellen loggt.
	 *
	 * @return true, falls das Erstellen erfolgreich durchgeführt wurde.
	 */
	public static boolean createNewSchema(final DBConfig tgtConfig, final String tgtRootUser, final String tgtRootPW, final long maxUpdateRevision, final Logger logger) {
		final long max_revision = SchemaRevisionen.maxRevision.revision;
		long rev = maxUpdateRevision;
		if (rev < 0)
			rev = max_revision;
		if (rev > max_revision)
			return false;

		if (!DBMigrationManager.createNewTargetSchema(tgtConfig, tgtRootUser, tgtRootPW, logger))
			return false;

		final Benutzer schemaUser = Benutzer.create(tgtConfig);
		final DBSchemaManager manager = DBSchemaManager.create(schemaUser, true, logger);

		logger.logLn("Erstelle das Schema zunächst in der Revision 0.");
		logger.modifyIndent(2);
		try (DBEntityManager conn = schemaUser.getEntityManager()) {
			try {
				conn.transactionBegin();
				if (!manager.createSVWSSchema(conn, 0, true, true))
					throw new DBException("Fehler beim Erstellen des Schemas");
				if (!conn.transactionCommit())
					throw new DBException("Fehler beim Erstellen des Schemas - Transaktion konnte nicht abgeschlossen werden");
			} catch (final DBException e) {
				logger.logLn(e.getMessage());
				return false;
			} catch (final Exception e) {
				logger.logLn("Unerwarteter Fehler beim Erstellen des Schemas: " + e.getMessage());
				return false;
			} finally {
				logger.modifyIndent(-2);
				conn.transactionRollback();
			}
		}

		logger.logLn("Aktualisiere das Schema schrittweise auf Revision " + rev + ".");
		logger.modifyIndent(2);
		if (!manager.updater.update(schemaUser, rev, false, true))
			return false;
		logger.modifyIndent(-2);
		return true;
	}

}
