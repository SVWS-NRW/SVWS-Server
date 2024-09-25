package de.svws_nrw.db.utils.schema;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.config.SVWSKonfigurationException;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.PersistenceUnits;
import de.svws_nrw.db.schema.dto.DTOInformationSchema;
import de.svws_nrw.db.schema.dto.DTOInformationUser;



/**
 * Diese Klasse dient dem administrativen Zugriff auf eine Datenbank. Der in der
 * DB-Konfiguration übergebene Benutzer muss über entsprechende administrative Rechte
 * (z.B. root-Rechte) in der Datenbank verfügen.
 */
public final class DBRootManager {

	/** Die Datenbank-Verbindung, die vom Root-Manager genutzt wird */
	private final DBEntityManager conn;


	/**
	 * Erstellt einen neuen DB-Root-Manager
	 *
	 * @param conn     die Datenbank-Verbindung mit den administrativen Rechten
	 */
	private DBRootManager(final DBEntityManager conn) {
		this.conn = conn;
	}


	/**
	 * Erstellt für die angebene Datenbank-Verbindung einen DB-Root-Manager.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return der DB-Root-Manager
	 */
	public static DBRootManager create(final DBEntityManager conn) {
		return new DBRootManager(conn);
	}


	/**
	 * Gibt zurück, ob der DB-Root-Manager eine aktive Datenbankverbindung besitzt.
	 *
	 * @return true, wenn die Datenbankverbindung aktiv ist, sonst false
	 */
	public boolean isConnected() {
		return (conn != null) && conn.isConnected();
	}


	private static final Set<String> _reserverSchemaNames = Set.of("", "information_schema", "mysql", "performance_schema",
			"sys", "master", "tempdb", "model", "msdb");

	/**
	 * Prüft, ob der übergebene Name ein reservierter oder ungültiger Schema-Name ist.
	 *
	 * @param name    der Schema-Name
	 *
	 * @return true, falls der Name reserviert oder ungültig ist.
	 */
	public static boolean isReservedSchemaName(final String name) {
		if (name == null)
			return true;
		return _reserverSchemaNames.contains(name);
	}


	private static final Set<String> _reserverUserNames = Set.of("", "root", "mysql.infoschema", "mysql.session", "mysql.sys",
			"sa", "##MS_PolicyTsqlExecutionLogin##", "##MS_PolicyEventProcessingLogin##");

	/**
	 * Prüft, ob der übergebene Name ein reservierter oder ungültiger DB-Benutzer-Name ist.
	 *
	 * @param name    der DB-Benutzer-Name
	 *
	 * @return true, falls der Name reserviert oder ungültig ist.
	 */
	public static boolean isReservedUserName(final String name) {
		if (name == null)
			return true;
		return _reserverUserNames.contains(name);
	}



	/**
	 * Erstellt ein neues Datenbank-Schema.
	 *
	 * @param nameSchema   der Name des zu erstellenden Schemas
	 *
	 * @return true, wenn das Schema erstellt wurde, sonst false
	 */
	private boolean createDBSchema(final String nameSchema) {
		if ((conn == null) || (nameSchema == null) || "".equals(nameSchema))
			return false;
		final String collation = conn.getDBDriver().getCollation();
		return switch (conn.getDBDriver()) {
			case MARIA_DB, MYSQL -> {
				if ((collation == null) || "".equals(collation))
					yield false;
				yield conn.executeNativeUpdate("CREATE SCHEMA IF NOT EXISTS `" + nameSchema + "` DEFAULT CHARACTER SET 'utf8mb4' DEFAULT COLLATE '" + collation
						+ "'") > Integer.MIN_VALUE;
			}
			case MSSQL -> {
				if ((collation == null) || "".equals(collation))
					yield false;
				yield conn.executeNativeUpdate("CREATE DATABASE [" + nameSchema + "] COLLATE " + collation) > Integer.MIN_VALUE;
			}
			default -> false;
		};
	}



	/**
	 * Erstellt einen neuen Datenbank-Benutzer mit administrativen Rechten auf dem angebenen Schema.
	 *
	 * @param conn         die Datenbank-Verbindung
	 * @param nameUser     der Name des zu erstellenden Benutzers
	 * @param pwUser       das Benutzerkennwort des zu erstellenden Benutzers
	 * @param nameSchema   das Schema, auf dem der neue Benutzer seine Rechte bekommen soll
	 *
	 * @return true, wenn der Benutzer erstellt wurde, sonst false
	 *
	 * @throws DBException   wenn ein Verbindungsfehler auftritt
	 */
	public static boolean createDBAdminUser(final DBEntityManager conn, final String nameUser, final String pwUser, final String nameSchema)
			throws DBException {
		if ((conn == null) || !conn.getDBDriver().hasMultiSchemaSupport())
			return false;
		// Prüfe, ob der aktuelle Datenbank-Benutzer überhaupt Rechte auf das Schema hat und sich verbinden kann
		final Benutzer user;
		try {
			user = conn.getUser().connectTo(nameSchema);
		} catch (@SuppressWarnings("unused") final DBException db) {
			return false;
		}
		try (DBEntityManager tmpConn = user.getEntityManager()) {
			if (tmpConn == null)
				return false;
			// Prüfe, ob der Benutzer bereits existiert und erstelle nur einen, wenn keiner existiert
			final List<String> benutzer = DTOInformationUser.queryNames(tmpConn);
			if (!benutzer.contains(nameUser) && !createDBUser(tmpConn, nameUser, pwUser))
				return false;

			// Gibt dem Benutzer administrative Rechte auf das Schema
			return grantAdminRights(tmpConn, nameUser, nameSchema);
		}
	}



	/**
	 * Erstellt einen neuen Datenbank-Benutzer auf der Verbindung, welche durch den
	 * EntityManager gegeben ist. Diese Verbindung muss durch einen DB-Benutzer mit
	 * entsprechenden administrativen Rechten auf dem angebenen Schema aufgebaut sein.
	 *
	 * @param conn         die Datenbank-Verbindung
	 * @param nameUser     der Name des zu erstellenden Benutzers
	 * @param pwUser       das Benutzerkennwort des zu erstellenden Benutzers
	 *
	 * @return true im Erfolgsfall
	 */
	private static boolean createDBUser(final DBEntityManager conn, final String nameUser, final String pwUser) {
		if ((conn == null) || (nameUser == null) || "".equals(nameUser) || (pwUser == null))
			return false;
		// TODO Nutze jeweils eine verschlüsselte Form des Kennwortes -> dieses sollte beim Logging nicht erscheinen -> also nicht "IDENTIFIED BY 'USERPASSWORD'"
		return switch (conn.getDBDriver()) {
			case MARIA_DB, MYSQL -> conn.executeNativeUpdate("CREATE USER IF NOT EXISTS `" + nameUser + "` IDENTIFIED BY '" + pwUser + "'") > Integer.MIN_VALUE;
			case MSSQL -> {
				conn.transactionBegin();
				final int c1 = conn.transactionNativeUpdate("CREATE LOGIN " + nameUser + " WITH PASSWORD = '" + pwUser + "'");
				yield conn.transactionCommit() && (c1 > Integer.MIN_VALUE);
			}
			default -> false;
		};
	}



	/**
	 * Vergibt administrative Rechte für das angegebene Schema nameSchema an den angegebenen
	 * Datenbank-Benutzer nameUser auf der Verbindung, welche durch den angegebenen DBEntityManager conn
	 * aufgebaut ist. Diese Verbindung muss durch einen DB-Benutzer mit entsprechenden administrativen
	 * Rechten auf dem angebenen Schema aufgebaut sein.
	 *
	 * @param conn         die Datenbank-Verbindung
	 * @param nameUser     der Name des Benutzers dem die adminstrativen Rechte gewährt werden sollen
	 * @param nameSchema   das Schema, auf dem der neue Benutzer seine Rechte bekommen soll
	 *
	 * @return true wenn die administrativen Rechte gewährt wurden und ansonsten false
	 */
	public static boolean grantAdminRights(final DBEntityManager conn, final String nameUser, final String nameSchema) {
		if ((conn == null) || (nameUser == null) || "".equals(nameUser) || (nameSchema == null) || "".equals(nameSchema))
			return false;
		if ((conn.getDBDriver() == DBDriver.MARIA_DB) || (conn.getDBDriver() == DBDriver.MYSQL)) {
			conn.transactionBegin();
			final int c1 = conn.transactionNativeUpdate("GRANT ALL PRIVILEGES ON `" + nameSchema + "`.* TO `" + nameUser + "`");
			final int c2 = conn.transactionNativeUpdate("GRANT GRANT OPTION ON `" + nameSchema + "`.* TO `" + nameUser + "`");
			return conn.transactionCommit() && (c1 > Integer.MIN_VALUE) && (c2 > Integer.MIN_VALUE);
		}
		if (conn.getDBDriver() == DBDriver.MSSQL) {
			conn.transactionBegin();
			final int c1 = conn.transactionNativeUpdate("USE [" + nameSchema + "]");
			final int c2 = conn.transactionNativeUpdate("CREATE USER " + nameUser + " FOR LOGIN " + nameUser);
			final int c3 = conn.transactionNativeUpdate("GRANT ALL PRIVILEGES ON DATABASE::[" + nameSchema + "] TO " + nameUser + " WITH GRANT OPTION");
			final int c4 = conn.transactionNativeUpdate("GRANT CONTROL ON DATABASE::[" + nameSchema + "] TO " + nameUser + " WITH GRANT OPTION");
			return conn.transactionCommit() && (c1 > Integer.MIN_VALUE) && (c2 > Integer.MIN_VALUE) && (c3 > Integer.MIN_VALUE) && (c4 > Integer.MIN_VALUE);
		}
		return false;
	}


	/**
	 * Entfernt administrative Rechte für das angegebene Schema nameSchema an den angegebenen
	 * Datenbank-Benutzer nameUser auf der Verbindung, welche durch den angegebenen DBEntityManager conn
	 * aufgebaut ist. Diese Verbindung muss durch einen DB-Benutzer mit entsprechenden administrativen
	 * Rechten auf dem angebenen Schema aufgebaut sein.
	 *
	 * @param conn         die Datenbank-Verbindung
	 * @param nameUser     der Name des Benutzers dem die adminstrativen Rechte entzogen werden sollen
	 * @param nameSchema   das Schema, auf dem der Benutzer seine Rechte entzogen bekommen soll
	 *
	 * @return true wenn die administrativen Rechte entfernt wurden und ansonsten false
	 */
	private static boolean revokeAdminRights(final DBEntityManager conn, final String nameUser, final String nameSchema) {
		if ((conn == null) || (nameUser == null) || "".equals(nameUser) || (nameSchema == null) || "".equals(nameSchema))
			return false;
		if ((conn.getDBDriver() == DBDriver.MARIA_DB) || (conn.getDBDriver() == DBDriver.MYSQL)) {
			conn.transactionBegin();
			final int c1 = conn.transactionNativeUpdate("REVOKE GRANT OPTION ON `" + nameSchema + "`.* FROM `" + nameUser + "`");
			final int c2 = conn.transactionNativeUpdate("REVOKE ALL PRIVILEGES ON `" + nameSchema + "`.* FROM `" + nameUser + "`");
			return conn.transactionCommit() && (c1 > Integer.MIN_VALUE) && (c2 > Integer.MIN_VALUE);
		}
		if (conn.getDBDriver() == DBDriver.MSSQL)
			throw new UnsupportedOperationException("MSSQL-Datenbanken werden zur Zeit nicht vollständig unterstützt.");
		return false;
	}


	/**
	 * Erstellt ein neues Datenbank-Schema und einem Benutzer mit administrativen Rechten auf diesem Schema.
	 * Existiert der Benutzer bereits, so werden lediglich Admin-Rechte auf das neue Schema hinzugefügt.
	 *
	 * @param nameUser     der Name des zu erstellenden Benutzers
	 * @param pwUser       das Benutzerkennwort des zu erstellenden Benutzers
	 * @param nameSchema   das Schema, auf dem der neue Benutzer seine Rechte bekommen soll
	 *
	 * @return true, wenn das Schema und der Benutzer erstellt wurden, sonst false
	 *
	 * @throws SVWSKonfigurationException   falls ein Fehler beim Erstellen oder Anpassen der SVWS-Konfiguration auftritt
	 * @throws DBException   wenn ein Verbindungsfehler auftritt
	 */
	public boolean createDBSchemaWithAdminUser(final String nameUser, final String pwUser, final String nameSchema)
			throws SVWSKonfigurationException, DBException {
		// Erstelle zunächst das DB-Schema
		if (!createDBSchema(nameSchema))
			return false;
		// Erstelle dann den DB-Benutzer und gebe diesem Adminstrationsrechte auf das Schema
		if (!createDBAdminUser(conn, nameUser, pwUser, nameSchema)) {
			dropDBSchemaIfExists(nameSchema);
			return false;
		}
		// Aktualisieren der DB-Konfiguration
		SVWSKonfiguration.get().createOrUpdateSchema(nameSchema, nameUser, pwUser, false);
		return true;
	}



	/**
	 * Prüft, ob das angegebene Datenbank-Schema bereits existiert oder nicht
	 *
	 * @param nameSchema   der Name des Datenbank-Schemas
	 *
	 * @return true, falls das Schema existiert, sonst false
	 */
	public boolean dbSchemaExists(final String nameSchema) {
		if ((conn == null) || !conn.getDBDriver().hasMultiSchemaSupport() || (nameSchema == null) || "".equals(nameSchema))
			return false;
		return DTOInformationSchema.hasSchemaIgnoreCase(conn, nameSchema);
	}


	/**
	 * Verwirft das Datenbank-Schema mit dem angegebenen Namen, sofern es existiert.
	 *
	 * @param nameSchema   der Name des zu verwerfenden Schemas
	 *
	 * @return true, falls das Schema erfolgreich entfernt wurde oder nicht
	 *         existierte, sonst false
	 *
	 * @throws SVWSKonfigurationException   falls ein Fehler beim Entfernen der SVWS-Konfiguration auftritt
	 */
	public boolean dropDBSchemaIfExists(final String nameSchema) throws SVWSKonfigurationException {
		if ((conn == null) || !conn.getDBDriver().hasMultiSchemaSupport() || (nameSchema == null) || "".equals(nameSchema))
			return false;
		final String name = DTOInformationSchema.getSchemanameCaseDB(conn, nameSchema);
		if (name == null)
			return true;
		// Entferne das Schema aus der Datenbank
		final boolean success = switch (conn.getDBDriver()) {
			case MARIA_DB, MYSQL -> conn.executeNativeDelete("DROP SCHEMA IF EXISTS `" + name + "`") > Integer.MIN_VALUE;
			case MSSQL -> conn.executeNativeDelete("DROP DATABASE IF EXISTS [" + name + "]") > Integer.MIN_VALUE;
			default -> false;
		};
		// Entferne die Rechte des zugeordneten Datenbank-Benutzers von dem Schema
		final String nameSchemaConfig = SVWSKonfiguration.get().getSchemanameCaseConfig(nameSchema);
		if (nameSchemaConfig != null) {
			final DBConfig dbConfig = SVWSKonfiguration.get().getDBConfig(nameSchemaConfig);
			if (dbConfig != null)
				revokeAdminRights(conn, dbConfig.getUsername(), dbConfig.getDBSchema());
		}
		// Aktualisieren der DB-Konfiguration
		if (success)
			SVWSKonfiguration.get().removeSchema(nameSchema);
		if (!success)
			SVWSKonfiguration.get().removeSchema(name);
		return success;
	}



	/**
	 * Prüft, ob der angegebene Datenbank-Benutzer breits existiert oder nicht
	 *
	 * @param nameUser   der Name des Datenbank-Benutzer
	 *
	 * @return true, falls der Datenbank-Benutzer existiert, sonst false
	 */
	public boolean dbUserExists(final String nameUser) {
		if ((conn == null) || !conn.getDBDriver().hasMultiSchemaSupport() || (nameUser == null) || "".equals(nameUser))
			return false;
		final List<String> benutzer = DTOInformationUser.queryNames(conn);
		return (benutzer != null) && benutzer.contains(nameUser);
	}



	/**
	 * Verwirft einen Datenbank-Benutzer, sofern dieser existiert.
	 *
	 * @param nameUser    der Name des zu verwerfenden Benutzers
	 *
	 * @return true, falls der Benutzer erfolgreich entfernt wurde oder
	 *         der Benutzer nicht vorhanden war, sonst false
	 */
	public boolean dropDBUserIfExists(final String nameUser) {
		if ((conn == null) || !conn.getDBDriver().hasMultiSchemaSupport() || (nameUser == null) || "".equals(nameUser))
			return false;
		final List<String> benutzer = DTOInformationUser.queryNames(conn);
		if (!benutzer.contains(nameUser))
			return true;
		return switch (conn.getDBDriver()) {
			case MARIA_DB, MYSQL -> conn.executeNativeDelete("DROP USER IF EXISTS `" + nameUser + "`") > Integer.MIN_VALUE;
			case MSSQL -> {
				conn.transactionBegin();
				final int c1 = conn.transactionNativeDelete("DROP USER IF EXISTS " + nameUser);
				final int c2 = conn.transactionNativeDelete("DROP LOGIN " + nameUser);
				yield conn.transactionCommit() && (c1 > Integer.MIN_VALUE) && (c2 > Integer.MIN_VALUE);
			}
			default -> false;
		};
	}




	/**
	 * Entfernt die Datenbank-Datei an dem angegeben Pfad db_location.
	 *
	 * @param driver        das DBMS
	 * @param db_location   der Pfad der Datei
	 *
	 * @return true, falls die Datenbank-Datei gelöscht wurde, zuvor nicht existierte oder das DBMS nicht eine einzelne DB-Datei verwendet, sonst false
	 */
	private static boolean removeDBFile(final DBDriver driver, final String db_location) {
		if ((driver != DBDriver.MDB) && (driver != DBDriver.SQLITE))
			return true;
		final Path path = Paths.get(db_location);
		if (!Files.exists(path))
			return true;
		try {
			Files.delete(path);
			return true;
		} catch (@SuppressWarnings("unused") final Exception e) {
			// do nothing
		}
		return false;
	}



	/**
	 * Erstelle anhand der Parameter eine DB-Konfiguration für den Zugriff als root-Benutzer.
	 *
	 * @param driver        das DBMS
	 * @param db_location   der Ort, an dem sich die Datenbank befindet
	 * @param user_root     der Benutzer-Name für den Benutzer der mit root-Rechten ausgestattet ist
	 * @param pw_root       das root-Kennwort für den Datenbank-Zugriff
	 *
	 * @return die Konfiguration oder null bei einem nicht unterstützten DBMS
	 */
	private static DBConfig getDBRootConfig(final DBDriver driver, final String db_location, final String user_root, final String pw_root) {
		return switch (driver) {
			case MARIA_DB, MYSQL ->
				new DBConfig(PersistenceUnits.SVWS_ROOT, driver, db_location, "mysql", false, (user_root == null) ? "root" : user_root, pw_root, true, false);
			case MDB -> new DBConfig(PersistenceUnits.SVWS_ROOT, driver, db_location, null, false, null, "", true, true);
			case MSSQL ->
				new DBConfig(PersistenceUnits.SVWS_ROOT, driver, db_location, "master", false, (user_root == null) ? "sa" : user_root, pw_root, true, false);
			case SQLITE -> new DBConfig(PersistenceUnits.SVWS_ROOT, driver, db_location, null, false, null, null, true, true);
			default -> null;
		};
	}


	/**
	 * Prüft für die übergebene Konfiguration, ob der Benutzer nicht in der
	 * Datenbank angelegt ist oder ob er angelegt ist und das Kennwort gültig
	 * ist. Die Prüfung des Kennwortes erfolgt über eine Verbindung zum
	 * Schema 'information_schema'.
	 *
	 * @param config   die Konfiguration mit dem zu prüfenden Benutzernamen und dem Kennwort
	 *
	 * @return true, falls der Benutzer nicht existiert oder sein Kennwort gültig ist, und ansonsten false
	 */
	private boolean checkUserNotExistsOrCredentialsValid(final DBConfig config) {
		try {
			final List<String> benutzer = DTOInformationUser.queryNames(conn);
			if (!benutzer.contains(config.getUsername()))
				return true;
			final Benutzer userInformationSchema = Benutzer.create(config.switchSchema(PersistenceUnits.SVWS_ROOT, "information_schema"));
			try (DBEntityManager tmpConn = userInformationSchema.getEntityManager()) {
				/* Kein Zugriff über tmpConn nötig... Nur ein Verbindungstest */
			}
			return true;
		} catch (@SuppressWarnings("unused") final Exception e) {
			return false;
		}
	}


	/**
	 * Erstelle als root-Benutzer ein neuese Datenbank-Schema bzw. eine neue Datenbankdatei. Ein bereits
	 * vorhandenes Schema bzw. eine bereits vorhandene Datei wird zuvor gelöscht.
	 *
	 * @param config    die Datenbank-Konfiguration mit den Zugriffs-Informationen für den neuen Admin-Benutzer
	 * @param user_root der Benutzername für den Benutzer der mit den "root"-Rechten auf die Datenbank ausgestattet ist
	 * @param pw_root   das Kennwort für den root-Zugriff auf die Datenbank
	 * @param logger    ein Logger, welcher die jeweiligen Informationen zu den einzelnen Operationen loggt.
	 *
	 * @return true im Erfolgsfall und false, falls ein Fehler aufgetreten ist.
	 *
	 * @throws DBException falls ein Fehler auftritt
	 */
	public static boolean recreateDB(final DBConfig config, final String user_root, final String pw_root, final Logger logger) throws DBException {
		if (config.getDBDriver().isFileBased()) {
			logger.log("-> Lösche existierende Datenbankdatei für die Ziel-DB - falls vorhanden...");
			removeDBFile(config.getDBDriver(), config.getDBLocation());
			logger.logLn(0, " [OK]");
			return true;
		}

		if (config.getDBDriver().hasMultiSchemaSupport()) {
			logger.logLn("-> Verbinde mit einem DB-Root-Manager zu der Ziel-DB...");
			final DBConfig rootConfig = getDBRootConfig(config.getDBDriver(), config.getDBLocation(), user_root, pw_root);
			final Benutzer rootUser;
			try {
				rootUser = Benutzer.create(rootConfig);
			} catch (@SuppressWarnings("unused") final DBException db) {
				logger.logLn(2, " [Fehler]");
				logger.log(LogLevel.ERROR, 2, "Fehler bei der Erstellung der Datenbank-Verbindung (driver='" + config.getDBDriver() + "', schema='"
						+ config.getDBSchema() + "', location='" + config.getDBLocation() + "', user='" + config.getUsername() + "')");
				logger.log(LogLevel.ERROR, 2, "Überprüfen Sie das verwendete Kennwort.");
				return false;
			}
			try (DBEntityManager rootConn = rootUser.getEntityManager()) {
				logger.modifyIndent(2);
				logger.log("- ");
				if (rootConn == null) {
					logger.logLn(0, " [Fehler]");
					logger.log(LogLevel.ERROR, 0, "Fehler bei der Erstellung der Datenbank-Verbindung (driver='" + config.getDBDriver() + "', schema='"
							+ config.getDBSchema() + "', location='" + config.getDBLocation() + "', user='" + config.getUsername() + "')");
					throw new DBException("");
				}
				logger.logLn(0, "Datenbank-Verbindung erfolgreich aufgebaut (driver='" + config.getDBDriver() + "', schema='" + config.getDBSchema()
						+ "', location='" + config.getDBLocation() + "', user='" + config.getUsername() + "')");
				final DBRootManager root_manager = new DBRootManager(rootConn);

				logger.log("- Prüfe, ob der Benutzer noch angelegt werden kann oder ob das angebene Kennwort zu einem existierenden Benutzer passt...");
				if (!root_manager.checkUserNotExistsOrCredentialsValid(config))
					throw new DBException("Der Datenbank-Benutzer exisiert bereits und das angegeben Kennwort passt nicht dazu.");
				logger.logLn(0, " [OK]");

				logger.log("- Entferne aus der Ziel-DB das alte Schema, falls vorhanden...");
				if (!root_manager.dropDBSchemaIfExists(config.getDBSchema()))
					throw new DBException("Das bereits existierende Schema konnte nicht entfernt werden.");
				logger.logLn(0, " [OK]");

				logger.log("- Erstelle in der Ziel-DB das Schema und den Admin-Benutzer:");
				if (!root_manager.createDBSchemaWithAdminUser(config.getUsername(), config.getPassword(), config.getDBSchema()))
					throw new DBException("Das Schema konnte nicht erstellt werden oder der Admin-Benutzer konnte nicht angelegt werden.");
				logger.logLn(0, " [OK]");

				return true;
			} catch (final Exception e) {
				logger.logLn(0, " [Fehler]");
				if (e instanceof final DBException dbe)
					throw dbe;
				if (e instanceof final SVWSKonfigurationException ske)
					throw new DBException(
							"Ein unerwarteter Fehler ist beim Zugriff auf die SVWS-Konfiguration nach dem Erstellen des Datenbank-Schemas aufgetreten: "
									+ ske.getMessage(),
							ske.getCause());
				throw new DBException("Ein unerwarteter Fehler ist beim Erstellen des Datenbank-Schemas aufgetreten.", e);
			} finally {
				logger.modifyIndent(-2);
			}
		}

		return false;
	}


}
