package de.svws_nrw.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.sessions.server.ConnectionPool;
import org.eclipse.persistence.sessions.server.ServerSession;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.validation.constraints.NotNull;


/**
 * Ein Manager für die Datenbank-Verbindungen der Anwendung.
 */
public final class ConnectionManager {

	/*
	 * Initialisiert den Shutdown-Hook, um alle nicht mehr benötigten Datenbank-Verbindungen
	 * über die zugehörigen Factories zu schließen. (siehe auch {@link ConnectionFactory})
	 */
	static {
		instance = new ConnectionManager();
		Runtime.getRuntime().addShutdownHook(new Thread(ConnectionManager.instance::closeAll));
	}

	/** Eine HashMap für den schnellen Zugriff auf eine Connection-Factory anhand der Datenbank-Konfiguration */
	private final HashMap<DBConfig, ConnectionFactory> mapManager = new HashMap<>();

	/** Die Instanz des Connectiion-Managers */
	public static final ConnectionManager instance;


	/**
	 * Erzeugt einen neuen Connection-Manager.
	 */
	private ConnectionManager() {
		// Empty Constructor
	}


	/**
	 * Gibt die Connection-Factory passen für die übergebene Konfiguration zurück.
	 * Sollt keine Factory existieren, so wird versucht eine neue zu erstellen.
	 *
	 * @param config   die Konfiguration der Datenbank-Verbindung
	 *
	 * @return die Factory
	 *
	 * @throws DBException   bei einem fehlerhaften Verbindungsaufbau, z.B. einer fehlschlagenen Authentifizierung
	 */
	public @NotNull ConnectionFactory get(final DBConfig config) throws DBException {
		ConnectionFactory man = mapManager.get(config);
		if ((man != null) && (!man.checkCredentials(config.getUsername(), config.getPassword()))) {
			mapManager.remove(config);
			man.close();
			man = null;
		}
		if (man == null) {
			man = new ConnectionFactory(config);
			try {
				try (EntityManager em = man.getNewJPAEntityManager()) {
					mapManager.put(config, man);
				}
			} catch (final PersistenceException pe) {
				if ((pe.getCause() instanceof final DatabaseException de) && (de.getCause() instanceof final SQLInvalidAuthorizationSpecException ae)) {
					man.close();
					throw new DBException("Fehler beim Aufbau der Verbindung. Überprüfen Sie Benutzername und Kennwort.", ae);
				}
				if (pe.getCause() instanceof DatabaseException) {
					man.close();
					throw new DBException("Fehler beim Aufbau der Verbindung. Überprüfen Sie die Verbindungsparameter.");
				}
				if (pe.getMessage().startsWith("java.lang.IllegalStateException: Could not determine FileFormat")) {
					man.close();
					throw new DBException("Fehlerhaftes oder zu altes MDB-Datei-Format.");
				}
				throw pe;
			}
		} else {
			// Führe eine Dummy-DB-Abfrage aus, um Probleme mit der
			// Server-seitigen Beendung einer Verbindung zu erkennen
			try {
				try (EntityManager em = man.getNewJPAEntityManager()) {
					try {
						em.getTransaction().begin();
						@SuppressWarnings("resource") final Connection conn = em.unwrap(Connection.class);
						try (Statement stmt = conn.createStatement()) {
							try (ResultSet rs = stmt.executeQuery("SELECT 1")) {
								rs.next();
								rs.getInt(1);
							}
						}
						em.getTransaction().commit();
						em.clear();
					} catch (@SuppressWarnings("unused") SQLException | DatabaseException e) {
						// Bestimme die Anzahl der verfügbaren Verbindungen
						final ServerSession serverSession = em.unwrap(ServerSession.class);
						final ConnectionPool pool = serverSession.getConnectionPools().get("default");
						if (pool == null) {
							Logger.global().logLn(LogLevel.ERROR, "Fehler beim Zugriff auf den DB-Connection-Pool default");
						} else {
							Logger.global().logLn(LogLevel.ERROR, "INFO: Verbindung zur Datenbank unterbrochen - versuche sie neu aufzubauen...");
							Logger.global().logLn(LogLevel.ERROR, "Total number of connections: " + pool.getTotalNumberOfConnections());
							Logger.global().logLn(LogLevel.ERROR, "Available number of connections: " + pool.getConnectionsAvailable().size());
							pool.resetConnections();
						}
					}
				}
			} catch (final PersistenceException pe) {
				if ((pe.getCause() instanceof final DatabaseException de) && (de.getCause() instanceof final SQLInvalidAuthorizationSpecException ae)) {
					mapManager.remove(config);
					man.close();
					throw new DBException(ae);
				}
				throw pe;
			}
		}
		return man;
	}

	/**
	 * Schließt den Connection-Manager für die übergebene Config und entfernt
	 * ihn aus der Liste der Manager
	 *
	 * @param config die Konfiguration des zu schließenden Managers
	 */
	private void closeSingle(final DBConfig config) {
		final ConnectionFactory manager = mapManager.get(config);
		if (manager == null) {
			Logger.global().logLn(LogLevel.ERROR, "Fehler beim Schließen des Verbindungs-Managers zu %s (Schema: %s), Datenbank-Benutzer: %s"
					.formatted(config.getDBLocation(), config.getDBSchema(), config.getUsername()));
			return;
		}
		manager.close();
		mapManager.remove(config);
		Logger.global().logLn(LogLevel.INFO, "Verbindungs-Manager des Datenbank-Benutzers %s zu %s (Schema: %s) geschlossen."
				.formatted(config.getUsername(), config.getDBLocation(), config.getDBSchema()));
	}

	/**
	 * Schließt alle noch offenenen Datenbank-Verbindungen.
	 */
	private void closeAll() {
		final List<DBConfig> configs = mapManager.keySet().stream().toList();
		for (final DBConfig config : configs)
			closeSingle(config);
	}

}
