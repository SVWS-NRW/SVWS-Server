package de.svws_nrw.db;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.constraints.NotNull;


/**
 * Eine Factory für Verbindungen in Bezug auf eine Datenbank-Konfiguration.
 */
public class ConnectionFactory {

	/** Ein Zufallszahlen-Generator */
	private static final Random random = new Random();

	/** Die verwendete Datenbank-Konfiguration {@link DBConfig} */
	private final @NotNull DBConfig config;

	/** Die zum Erzeugen der {@link EntityManager} verwendete Instanz des {@link EntityManagerFactory} */
	private final @NotNull EntityManagerFactory emf;


	/**
	 * Erstellt eine neue Factory für Datenbank-Verbindungen
	 *
	 * @param config   die Datenbank-Konfiguration für die Factory
	 */
	ConnectionFactory(final DBConfig config) {
		this.config = config;
		this.emf = createEntityManagerFactory();
	}


	/**
	 * Gibt einen neuen JPA {@link EntityManager} zurück. Diese Methode wird
	 * innerhalb dieses Packages vom DBEntityManager bei der Erneuerung der
	 * Verbindung verwendet.
	 *
	 * @return der neue JPA {@link EntityManager}
	 *
	 * @throws DBException   falls keine Verbindung erstellt werden kann
	 */
	EntityManager getNewJPAEntityManager() throws DBException {
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
	 * Prüft die übergebenen Credentials gegen die Credentials dieses Managers
	 *
	 * @param username   der Benutzername
	 * @param password   das Kennwort
	 *
	 * @return true, falls die Credentials übereinstimmen, und ansonsten false
	 */
	boolean checkCredentials(final String username, final String password) {
		final Map<String, Object> curProps = emf.getProperties();
		final String curUser = (String) curProps.get("jakarta.persistence.jdbc.user");
		final String curPassword = (String) curProps.get("jakarta.persistence.jdbc.password");
		return Objects.equals(username, curUser) && Objects.equals(password, curPassword);
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
		return Persistence.createEntityManagerFactory("SVWSDB", propertyMap);
		// TODO avoid Persistence Unit "SVWSDB" as xml file
	}


	/**
	 * Schließt den Verbindungs-Manager
	 */
	void close() {
		emf.close();
	}

}
