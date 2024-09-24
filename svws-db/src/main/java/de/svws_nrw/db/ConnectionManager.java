package de.svws_nrw.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

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
	private final HashMap<DBConfig, ConnectionFactory> mapFactories = new HashMap<>();

	/** Die Instanz des Connectiion-Managers */
	public static final ConnectionManager instance;

	/** Ein Mutex, dass immer nur ein Thread den Zustand dieses Managers verändern kann. */
	private final ReentrantLock mutex = new ReentrantLock();



	/**
	 * Erzeugt einen neuen Connection-Manager.
	 */
	private ConnectionManager() {
		// Empty Constructor
	}


	/**
	 * Bestimmt für den angegebenen Benutzer die zugehörige Connection-Factory
	 * und mit dieser dann eine neue Verbindung.
	 *
	 * @param user   der Benutzer
	 *
	 * @return die Datenbank-Verbindung
	 *
	 * @throws DBException   bei Fehlern im Verbindungsaufbau
	 */
	DBEntityManager getConnection(final Benutzer user) throws DBException {
		this.lock();
		final @NotNull ConnectionFactory factory = this.get(user.getConfig());
		final @NotNull DBEntityManager conn = factory.connect(user);
		this.unlock();
		return conn;
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
	private @NotNull ConnectionFactory get(final DBConfig config) throws DBException {
		ConnectionFactory factory = mapFactories.get(config);
		if ((factory != null) && (!factory.checkCredentials(config.getUsername(), config.getPassword()))) {
			mapFactories.remove(config);
			factory.close();
			factory = null;
		}
		if (factory == null) {
			factory = new ConnectionFactory(config);
			try {
				try (EntityManager em = factory.getNewJPAEntityManager()) {
					mapFactories.put(config, factory);
				}
				Logger.global().logLn(LogLevel.INFO, "Factory für Verbindung(-en) des Datenbank-Benutzers %s zu %s (Schema: %s) erzeugt."
						.formatted(config.getUsername(), config.getDBLocation(), config.getDBSchema()));
			} catch (final PersistenceException pe) {
				if ((pe.getCause() instanceof final DatabaseException de) && (de.getCause() instanceof final SQLInvalidAuthorizationSpecException ae)) {
					factory.close();
					throw new DBException("Fehler beim Aufbau der Verbindung. Überprüfen Sie Benutzername und Kennwort.", ae);
				}
				if (pe.getCause() instanceof DatabaseException) {
					factory.close();
					throw new DBException("Fehler beim Aufbau der Verbindung. Überprüfen Sie die Verbindungsparameter.");
				}
				if (pe.getMessage().startsWith("java.lang.IllegalStateException: Could not determine FileFormat")) {
					factory.close();
					throw new DBException("Fehlerhaftes oder zu altes MDB-Datei-Format.");
				}
				throw pe;
			}
		} else {
			// Führe eine Dummy-DB-Abfrage aus, um Probleme mit der
			// Server-seitigen Beendung einer Verbindung zu erkennen
			try {
				try (EntityManager em = factory.getNewJPAEntityManager()) {
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
					mapFactories.remove(config);
					factory.close();
					throw new DBException(ae);
				}
				throw pe;
			}
		}
		return factory;
	}


	/**
	 * Schließt die Factory für die übergebene Config und entfernt sie aus der Map der Factories
	 *
	 * @param config   die Konfiguration der zu schließenden Factory
	 */
	void closeSingle(final DBConfig config) {
		this.lock();
		final ConnectionFactory factory = mapFactories.get(config);
		if (factory == null) {
			Logger.global().logLn(LogLevel.ERROR, "Fehler beim Schließen der Factory für Verbindung(-en) zu %s (Schema: %s), Datenbank-Benutzer: %s"
					.formatted(config.getDBLocation(), config.getDBSchema(), config.getUsername()));
			return;
		}
		factory.close();
		mapFactories.remove(config);
		Logger.global().logLn(LogLevel.INFO, "Factory für Verbindung(-en) des Datenbank-Benutzers %s zu %s (Schema: %s) geschlossen."
				.formatted(config.getUsername(), config.getDBLocation(), config.getDBSchema()));
		this.unlock();
	}


	/**
	 * Schließt alle noch offenenen Factories.
	 */
	private void closeAll() {
		this.lock();
		final List<DBConfig> configs = mapFactories.keySet().stream().toList();
		for (final DBConfig config : configs)
			closeSingle(config);
		this.unlock();
	}


	/**
	 * Nimmt den Lock für Connection-Manager für den aktuellen Thread in Besitz und
	 * kehrt zurück, sofern dieser nicht im Besitz eines anderen Threads ist
	 * (siehe {@link ReentrantLock#lock()}).
	 */
	public void lock() {
		mutex.lock();
	}


	/**
	 * Gibt den Lock für Connection-Manager wieder frei, sofern dieser Thread im Besitz
	 * des Locks ist (siehe {@link ReentrantLock#isLocked()} und
	 * {@link ReentrantLock#unlock()})
	 */
	public void unlock() {
		if (mutex.isLocked() && mutex.isHeldByCurrentThread())
			mutex.unlock();
	}

}
