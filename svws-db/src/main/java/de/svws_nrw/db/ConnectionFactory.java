package de.svws_nrw.db;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;

import de.svws_nrw.config.SVWSKonfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.constraints.NotNull;


/**
 * Eine Factory für Verbindungen in Bezug auf eine Datenbank-Konfiguration.
 */
public class ConnectionFactory {

	/** Das Intervall, nachdem Factories nach einem close aufgeräumt werden, sofern sie nicht zwischenzeitlich neue Verbindungen aufgebaut haben. */
	private static final long CONNECTION_CLEANUP_INTERVAL = 300000;  // 5 min

	/** Ein Zufallszahlen-Generator */
	private static final Random random = new Random();

	private DBConfig config = null;

	/** Die zum Erzeugen der {@link EntityManager} verwendete Instanz des {@link EntityManagerFactory} */
	private EntityManagerFactory emf;

	/** Der Zeitpunkt, wann die letzte Verbindung aufgebaut oder geschlossen wurde. */
	private long tsLastConnection = -1;

	/** Die Menge der offenen Verbindungen bei dieser Factory */
	private final @NotNull Set<DBEntityManager> connections = new HashSet<>();


	/**
	 * Erstellt eine neue Factory für Datenbank-Verbindungen
	 *
	 * @param config   die Datenbank-Konfiguration für die Factory
	 */
	ConnectionFactory(final DBConfig config) {
		ConnectionManager.instance.lock();
		try {
			this.config = config;
			this.emf = createEntityManagerFactory();
		} finally {
			ConnectionManager.instance.unlock();
		}
	}


	/**
	 * Gibt einen neuen JPA {@link EntityManager} zurück. Diese Methode wird
	 * innerhalb dieses Packages vom DBEntityManager bei der Erneuerung der
	 * Verbindung verwendet.
	 *
	 * @return der neue JPA {@link EntityManager}
	 */
	EntityManager getNewJPAEntityManager() {
		if (emf == null)
			return null;
		return emf.createEntityManager();
	}


	/**
	 * Gibt die Datenbank-Konfiguration dieses Verbindungs-Managers zurück.
	 *
	 * @return die Datenbank-Konfiguration dieses Verbindungs-Managers
	 */
	DBConfig getConfig() {
		return this.config;
	}


	/**
	 * Erstellt für den angegebenen Benutzer eine neue Verbindung aus dieser Factory.
	 *
	 * @param user   der Benutzer
	 *
	 * @return die Datenbank-Verbindung
	 *
	 * @throws DBException   bei Fehlern im Verbindungsaufbau
	 */
	DBEntityManager connect(final Benutzer user) throws DBException {
		ConnectionManager.instance.lock();
		try {
			final DBEntityManager conn = (emf == null) ? null : new DBEntityManager(user, this);
			if (conn != null) {
				connections.add(conn);
				tsLastConnection = System.currentTimeMillis();
			}
			return conn;
		} finally {
			ConnectionManager.instance.unlock();
		}
	}


	/**
	 * Der Task, der in einem Thread ausgeführt wird, um die Verbindung ggf. nach einem Clean-Up-Intervall zu schließen
	 */
	private final Runnable cleanupTask = () -> {
		try {
			Thread.sleep(CONNECTION_CLEANUP_INTERVAL);
		} catch (@SuppressWarnings("unused") final InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		}
		ConnectionManager.instance.lock();
		try {
			// Wenn in der Zwischenzeit nicht mindestens CONNECTION_CLEANUP_INTERVAL an Zeit vergangen ist, dann gab
			// es zwischendurch eine weitere Verbindung und dieser Thread ist nicht mehr zuständig
			final long now = System.currentTimeMillis();
			if (now - tsLastConnection < CONNECTION_CLEANUP_INTERVAL)
				return;
			// Ansonsten muss die Verbindung unterbrochen werden, sofern dies nicht zwischenzeitlich passiert ist...
			if (ConnectionManager.instance.hasFactory(config))
				ConnectionManager.instance.closeSingle(config);
		} finally {
			ConnectionManager.instance.unlock();
		}
	};


	/**
	 * Schließt die übergebene Datenbankverbindung
	 *
	 * @param conn   die zu schließende Datenbankverbindung
	 */
	void close(final DBEntityManager conn) {
		ConnectionManager.instance.lock();
		try {
			if (emf == null)
				return;
			tsLastConnection = System.currentTimeMillis();
			connections.remove(conn);
			// Wenn keine Verbindungen mehr da sind und es sich nicht um ein Schema aus der SVWS-Konfiguration handelt, dann kann die Factory geschlossen werden...
			if (connections.isEmpty() && !config.equals(SVWSKonfiguration.get().getDBConfig(config.getDBSchema()))) {
				if (config.getDBDriver().isFileBased() || (config.getDBDriver() == DBDriver.MSSQL)) {
					// Datei-basierte Verbindungen werden sofort geschlossen.
					ConnectionManager.instance.closeSingle(config);
				} else {
					// Andere werden Zeit-verzögert geschlossen
					Thread.ofVirtual().start(cleanupTask);
				}
			}
		} finally {
			ConnectionManager.instance.unlock();
		}
	}


	/**
	 * Prüft die übergebenen Credentials gegen die Credentials dieses Managers
	 *
	 * @param username   der Benutzername
	 * @param password   das Kennwort
	 *
	 * @return true, falls die Credentials übereinstimmen, und ansonsten false
	 */
	boolean checkCredentials(final String username, final String password) {
		ConnectionManager.instance.lock();
		try {
			final Map<String, Object> curProps = emf.getProperties();
			final String curUser = (String) curProps.get("jakarta.persistence.jdbc.user");
			final String curPassword = (String) curProps.get("jakarta.persistence.jdbc.password");
			return Objects.equals(username, curUser) && Objects.equals(password, curPassword);
		} finally {
			ConnectionManager.instance.unlock();
		}
	}


	/**
	 * Intern genutzte Methode, um eine {@link EntityManagerFactory} zu erstellen. Hierbei werden auch die
	 * Standardeinstellungen für die Datenbankverbindung in Form der Property-Map hinzugefügt (siehe
	 * {@link Persistence#createEntityManagerFactory(String, java.util.Map)})
	 *
	 * @return die {@link EntityManagerFactory}
	 */
	private @NotNull EntityManagerFactory createEntityManagerFactory() {
		final HashMap<String, Object> propertyMap = new HashMap<>();
		propertyMap.put("jakarta.persistence.jdbc.driver", config.getDBDriver().getJDBCDriver());
		final String username = config.getUsername();
		String password = config.getPassword();
		String url = config.getDBDriver().getJDBCUrl(config.getDBLocation(), config.getDBSchema());
		if (config.getDBDriver() == DBDriver.MDB) {
			try (Database db = DatabaseBuilder.open(new File(config.getDBLocation()))) {
				password = db.getDatabasePassword();
			} catch (@SuppressWarnings("unused") final IOException e) {
				password = "";
			}
			if (config.createDBFile())
				url += ";newdatabaseversion=V2000";
		}
		final String sessionName = "SVWSDB_url=" + url + "_user=" + config.getUsername() + "_random=" + random.ints(48, 123)  // from 0 to z
				.filter(i -> ((i <= 57) || (i >= 65)) && ((i <= 90) || (i >= 97)))  // filter some unicode characters
				.limit(40)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
		propertyMap.put("jakarta.persistence.jdbc.url", url);
		propertyMap.put("jakarta.persistence.jdbc.user", username);
		propertyMap.put("jakarta.persistence.jdbc.password", password);
		propertyMap.put("eclipselink.session-name", sessionName);
		propertyMap.put("eclipselink.flush", "true");
		propertyMap.put("eclipselink.persistence-context.flush-mode", "commit");
		propertyMap.put("eclipselink.allow-zero-id", "true");
		propertyMap.put("eclipselink.logging.level", config.useDBLogging() ? "WARNING" : "OFF");
		// propertyMap.put("eclipselink.logging.level", config.useDBLogging() ? "INFO" : "OFF");
		// propertyMap.put("eclipselink.logging.level", "ALL");
		// propertyMap.put("eclipselink.logging.level.sql", "FINE");
		// propertyMap.put("eclipselink.logging.parameters", "true");
		// propertyMap.put("eclipselink.profiler","PerformanceProfiler");
		propertyMap.put("eclipselink.cache.shared.default", "false");
		// propertyMap.put("eclipselink.exception-handler",
		// "de.svws_nrw.db.DBExceptionHandler");
		if (config.getDBDriver() == DBDriver.SQLITE) {
			propertyMap.put("eclipselink.target-database", "Database");
			// Einstellungen des SQ-Lite-Treibers
			// READWRITE (2) + CREATE (4) + OPEN_URI (64) = 70 bzw. READWRITE (2) + OPEN_URI (64) = 66
			propertyMap.put("open_mode", (config.createDBFile()) ? "70" : "66");
			propertyMap.put("foreign_keys", "true");
		}
		return Persistence.createEntityManagerFactory(config.getPersistenceUnit().name(), propertyMap);
		// TODO avoid Persistence Unit "SVWSDB" as xml file
	}


	/**
	 * <p> Schließt die Factory. </p>
	 *
	 * <b>Hinweis:</b> Diese Methode sollte nur von dem ConnectionManager aufgerufen werden.
	 * Dort ist die Methode closeSingle aufzurufen.
	 */
	void close() {
		if (emf != null) {
			emf.close();
			emf = null;
		}
	}

}
