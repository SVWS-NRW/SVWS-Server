package de.svws_nrw.db.utils.schema;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
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
		switch (name) {
			case "":
			case "information_schema":
			case "mysql":
			case "performance_schema":
			case "sys":
			case "master":
			case "tempdb":
			case "model":
			case "msdb":
				return true;
		}
		return false;
	}



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
		switch (name) {
			case "":
			case "root":
			case "mysql.infoschema":
			case "mysql.session":
			case "mysql.sys":
			case "sa":
			case "##MS_PolicyTsqlExecutionLogin##":
			case "##MS_PolicyEventProcessingLogin##":
				return true;
		}
		return false;
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
		switch (conn.getDBDriver()) {
			case MARIA_DB:
			case MYSQL:
				if ((collation == null) || "".equals(collation))
					return false;
				return conn.executeNativeUpdate("CREATE SCHEMA IF NOT EXISTS `" + nameSchema + "` DEFAULT CHARACTER SET 'utf8mb4' DEFAULT COLLATE '" + collation + "'") > Integer.MIN_VALUE;
			case MSSQL:
				if ((collation == null) || "".equals(collation))
					return false;
				return conn.executeNativeUpdate("CREATE DATABASE [" + nameSchema + "] COLLATE " + collation) > Integer.MIN_VALUE;
			default:
				break;
		}
		return false;
	}



	/**
	 * Erstellt einen neuen Datenbank-Benutzer mit administrativen Rechten auf dem angebenen Schema.
	 *
	 * @param nameUser     der Name des zu erstellenden Benutzers
	 * @param pwUser       das Benutzerkennwort des zu erstellenden Benutzers
	 * @param nameSchema   das Schema, auf dem der neue Benutzer seine Rechte bekommen soll
	 *
	 * @return true, wenn der Benutzer erstellt wurde, sonst false
	 */
	private boolean createDBAdminUser(final String nameUser, final String pwUser, final String nameSchema) {
		if ((conn == null) || !conn.getDBDriver().hasMultiSchemaSupport())
			return false;
		// Prüfe, ob der aktuelle Datenbank-Benutzer überhaupt Rechte auf das Schema hat und sich verbinden kann
		final Benutzer user = this.conn.getUser().connectTo(nameSchema);
		try (DBEntityManager tmpConn = user.getEntityManager()) {
			if (tmpConn == null)
				return false;
			// Prüfe, ob der Benutzer bereits existiert und erstelle nur einen, wenn keiner existiert
			final List<String> benutzer = DTOInformationUser.queryNames(tmpConn);
			if (!benutzer.contains(nameUser) && !createDBUser(tmpConn, nameUser, pwUser, nameSchema))
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
	 * @param nameSchema   das Schema, auf dem der neue Benutzer seine Rechte bekommen soll
	 *
	 * @return true im Erfolgsfall
	 */
	private static boolean createDBUser(final DBEntityManager conn, final String nameUser, final String pwUser, final String nameSchema) {
		if ((conn == null) || (nameUser == null) || "".equals(nameUser) || (pwUser == null))
			return false;
		switch (conn.getDBDriver()) {
			case MARIA_DB:
			case MYSQL:
				// TODO Nutze eine verschlüsselte Form des Kennwortes -> dieses sollte beim Logging nicht erscheinen -> also nicht "IDENTIFIED BY 'USERPASSWORD'"
				return conn.executeNativeUpdate("CREATE USER IF NOT EXISTS " + nameUser + " IDENTIFIED BY '" + pwUser + "'") > Integer.MIN_VALUE;
			case MSSQL:
				conn.transactionBegin();
				// TODO Nutze eine verschlüsselte Form des Kennwortes -> dieses sollte beim Logging nicht erscheinen -> also nicht "WITH PASSWORD = 'USERPASSWORD'"
				final int c1 = conn.transactionNativeUpdate("CREATE LOGIN " + nameUser + " WITH PASSWORD = '" + pwUser + "'");
				return conn.transactionCommit() && (c1 > Integer.MIN_VALUE);
			default:
				break;
		}
		return false;
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
	private static boolean grantAdminRights(final DBEntityManager conn, final String nameUser, final String nameSchema) {
		if ((conn == null) || (nameUser == null) || "".equals(nameUser) || (nameSchema == null) || "".equals(nameSchema))
			return false;
		if ((conn.getDBDriver() == DBDriver.MARIA_DB) || (conn.getDBDriver() == DBDriver.MYSQL)) {
			conn.transactionBegin();
			final int c1 = conn.transactionNativeUpdate("GRANT ALL PRIVILEGES ON `" + nameSchema + "`.* TO " + nameUser);
			final int c2 = conn.transactionNativeUpdate("GRANT GRANT OPTION ON `" + nameSchema + "`.* TO " + nameUser);
			final int c3 = conn.transactionNativeUpdate("GRANT CREATE USER ON *.* TO " + nameUser);
			return conn.transactionCommit() && (c1 > Integer.MIN_VALUE) && (c2 > Integer.MIN_VALUE) && (c3 > Integer.MIN_VALUE);
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
	 * Erstellt ein neues Datenbank-Schema und einem Benutzer mit administrativen Rechten auf diesem Schema.
	 * Existiert der Benutzer bereits, so werden lediglich Admin-Rechte auf das neue Schema hinzugefügt.
	 *
	 * @param nameUser     der Name des zu erstellenden Benutzers
	 * @param pwUser       das Benutzerkennwort des zu erstellenden Benutzers
	 * @param nameSchema   das Schema, auf dem der neue Benutzer seine Rechte bekommen soll
	 *
	 * @return true, wenn das Schema und der Benutzer erstellt wurden, sonst false
	 */
	public boolean createDBSchemaWithAdminUser(final String nameUser, final String pwUser, final String nameSchema) {
		// Erstelle zunächst das DB-Schema
		if (!createDBSchema(nameSchema))
			return false;
		// Erstelle dann den DB-Benutzer und gebe diesem Adminstrationsrechte auf das Schema
		if (!createDBAdminUser(nameUser, pwUser, nameSchema)) {
			dropDBSchemaIfExists(nameSchema);
			return false;
		}
		// Aktualisieren der DB-Konfiguration
		return SVWSKonfiguration.get().createOrUpdateSchema(nameSchema, nameUser, pwUser, false);
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
		final List<String> schemata = DTOInformationSchema.queryNames(conn);
		return (schemata != null) && schemata.contains(nameSchema.toLowerCase());
	}


	/**
	 * Verwirft das Datenbank-Schema mit dem angegebenen Namen, sofern es existiert.
	 *
	 * @param nameSchema   der Name des zu verwerfenden Schemas
	 *
	 * @return true, falls das Schema erfolgreich entfernt wurde oder nicht
	 *         existierte, sonst false
	 */
	public boolean dropDBSchemaIfExists(final String nameSchema) {
		if ((conn == null) || !conn.getDBDriver().hasMultiSchemaSupport() || (nameSchema == null) || "".equals(nameSchema))
			return false;
		final List<String> schemata = DTOInformationSchema.queryNames(conn);
		if (!schemata.contains(nameSchema.toLowerCase()))
			return true;
		boolean success;
		switch (conn.getDBDriver()) {
			case MARIA_DB:
			case MYSQL:
				success = conn.executeNativeDelete("DROP SCHEMA IF EXISTS `" + nameSchema + "`") > Integer.MIN_VALUE;
				break;
			case MSSQL:
				success = conn.executeNativeDelete("DROP DATABASE IF EXISTS [" + nameSchema + "]") > Integer.MIN_VALUE;
				break;
			default:
				return false;
		}
		// Aktualisieren der DB-Konfiguration
		if (success)
			success = SVWSKonfiguration.get().removeSchema(nameSchema);
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
		switch (conn.getDBDriver()) {
			case MARIA_DB:
			case MYSQL:
				return conn.executeNativeDelete("DROP USER IF EXISTS " + nameUser) > Integer.MIN_VALUE;
			case MSSQL:
				conn.transactionBegin();
				final int c1 = conn.transactionNativeDelete("DROP USER IF EXISTS " + nameUser);
				final int c2 = conn.transactionNativeDelete("DROP LOGIN " + nameUser);
				return conn.transactionCommit() && (c1 > Integer.MIN_VALUE) && (c2 > Integer.MIN_VALUE);
			default:
				break;
		}
		return false;
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
		switch (driver) {
			case MARIA_DB:
			case MYSQL:
			case MSSQL:
			default:
				return true;
			case MDB:
			case SQLITE:
				break;
		}
		final File file = new File(db_location);
		if (!file.exists())
			return true;
		try {
			return file.delete();
		} catch (@SuppressWarnings("unused") final SecurityException e) {
			return false;
		}
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
		switch (driver) {
			case MARIA_DB:
			case MYSQL:
				return new DBConfig(driver, db_location, "mysql", false, user_root == null ? "root" : user_root, pw_root, true, false);
			case MDB:
				return new DBConfig(driver, db_location, null, false, null, "", true, true);
			case MSSQL:
				return new DBConfig(driver, db_location, "master", false, user_root == null ? "sa" : user_root, pw_root, true, false);
			case SQLITE:
				return new DBConfig(driver, db_location, null, false, null, null, true, true);
		}
		return null;
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
	 */
	public static boolean recreateDB(final DBConfig config, final String user_root, final String pw_root, final Logger logger) {
		if (config.getDBDriver().isFileBased()) {
			logger.log("-> Lösche existierende Datenbankdatei für die Ziel-DB - falls vorhanden...");
			removeDBFile(config.getDBDriver(), config.getDBLocation());
			logger.logLn(0, " [OK]");
			return true;
		}

		if (config.getDBDriver().hasMultiSchemaSupport()) {
			logger.logLn("-> Verbinde mit einem DB-Root-Manager zu der Ziel-DB...");
			final DBConfig rootConfig = getDBRootConfig(config.getDBDriver(), config.getDBLocation(), user_root, pw_root);
			final Benutzer rootUser = Benutzer.create(rootConfig);
			try (DBEntityManager rootConn = rootUser.getEntityManager()) {
				logger.modifyIndent(2);
				logger.log("- ");
				if (rootConn == null) {
					logger.logLn(0, " [Fehler]");
					logger.log(LogLevel.ERROR, 0, "Fehler bei der Erstellung der Datenbank-Verbindung (driver='" + config.getDBDriver() + "', schema='" + config.getDBSchema() + "', location='" + config.getDBLocation() + "', user='" + config.getUsername() + "')");
					throw new SQLException();
				}
				logger.logLn(0, "Datenbank-Verbindung erfolgreich aufgebaut (driver='" + config.getDBDriver() + "', schema='" + config.getDBSchema() + "', location='" + config.getDBLocation() + "', user='" + config.getUsername() + "')");
				final DBRootManager root_manager = new DBRootManager(rootConn);

				logger.log("- Entferne aus der Ziel-DB das alte Schema, falls vorhanden...");
				if (!root_manager.dropDBSchemaIfExists(config.getDBSchema()))
					throw new SQLException();
				logger.logLn(0, " [OK]");

				logger.log("- Erstelle in der Ziel-DB das Schema und den Admin-Benutzer:");
				if (!root_manager.createDBSchemaWithAdminUser(config.getUsername(), config.getPassword(), config.getDBSchema()))
					throw new SQLException();
				logger.logLn(0, " [OK]");

				logger.modifyIndent(-2);
				return true;
			} catch (@SuppressWarnings("unused") final SQLException e) {
				logger.logLn(0, " [Fehler]");
				logger.modifyIndent(-2);
				return false;
			}
		}

		return false;
	}


}
