package de.svws_nrw.db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.LongFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.db.schema.tabellen.Tabelle_Schema_AutoInkremente;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.persistence.RollbackException;
import jakarta.persistence.Table;
import jakarta.persistence.TransactionRequiredException;
import jakarta.persistence.TypedQuery;


/**
 * Die Klasse DBEntityManager regelt den Datenbankzugriff im SVWS-Servers durch
 * die interne Nutzung eines {@link EntityManager} aus der Jakarta Persistence API (JPA).
 * Dafür werden zahlreiche Methoden zur Verfügung gestellt.
 */
public final class DBEntityManager implements AutoCloseable {

	/**
	 * Gibt an, ob das Caching der SVWS-Server-Datenbankdaten beim Zugriff auf die Datenbank genutzt werden soll.
	 * Dies ist als false vorkonfiguriert, da dies zu Problemen führt, wenn mehrere Prozesse auf die Datenbank zugreifen
	 * dürfen. Dies ist gerade durch den Parallelbetrieb von Schild3 zum SVWS-Server aber der Normalfall, so dass
	 * das Aktivieren dieser Option gefährlich ist und vermutlich zu Datenverlusten führt.
	 */
	public static final boolean use_db_caching = false;


	/** Formatiert eine Zeitangabe als String, in der Art, wie es für die Datumseingabe in einer SQL-Anfrage genutzt wird. */
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	/** Formatiert ein Datum als String, in der Art, wie es für die Datumseingabe in einer SQL-Anfrage genutzt wird. */
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/** Formatiert ein Datum mit Zeitangabe als String, in der Art, wie es für die Datumseingabe in einer SQL-Anfrage genutzt wird. */
	private static final DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/** Der Datenbank-Benutzer, der dieser Verbindung zugeordnet ist. */
	private final Benutzer user;

	/** Die Connection-Factory, welche für diese Verbindung verantwortlich ist. */
	private final ConnectionFactory factory;

	/** Der intern verwendete {@link EntityManager} der Jakarta Persistence API (JPA) */
	private EntityManager em;

	/** Ein intern verwendeter Mutex, der garantiert, dass immer nur ein Thread eine Transaction auf diesem DBEntityManager ausführt. */
	private final ReentrantLock mutex = new ReentrantLock();



	/**
	 * Konstruktor für die interne Nutzung. Es wird eine neue Instanz auf Basis
	 * der übergebenen Konfiguration (siehe {@link DBConfig}) erstellt. Für den
	 * Fall, dass die Verbindung nicht erfolgreich ist, wird eine
	 * {@link DBConnectionException} generiert.
	 *
	 * @param user      der Benutzer, der dieser Verbindung zugeordnet ist.
	 * @param factory   die Factory für die Datenbank-Verbindungen
	 */
	DBEntityManager(final Benutzer user, final ConnectionFactory factory) {
		this.user = user;
		this.factory = factory;
		this.em = factory.getNewJPAEntityManager();
	}


	/**
	 * Gibt den Benutzer zurück, der mit dieser Datenbank-Verbindung
	 * assoziiert ist.
	 *
	 * @return der Datenbank-Benutzer.
	 */
	public Benutzer getUser() {
		return this.user;
	}


	/**
	 * Prüft, ob der Benutzer dieser Datenbank-Verbindung einen Benutzernamen hat, welcher
	 * dem priviligierten Datenbbank-Benutzer gehört, der in der SVWS-Konfiguration eingetragen
	 * ist.
	 *
	 * @return true, falls es sich um den priviligierten Datenbank-Benutzer handelt.
	 */
	public boolean isPrivilegedDatabaseUser() {
		return SVWSKonfiguration.get().getPrivilegedDatabaseUser().equals(this.user.getUsername());
	}


	@Override
	public void close() {
		if (em != null) {
			em.clear();
			em.close();
			em = null;
		}
		factory.close(this);
	}


	/**
	 * Prüft, ob eine Verbindung zur Datenbank besteht oder nicht,
	 * indem die Existenz des internen {@link EntityManager}
	 * geprüft wird.
	 *
	 * @return true, falls eine Verbindung besteht und ansonsten false.
	 */
	public boolean isConnected() {
		return (em != null);
	}


	/**
	 * Gibt an, ob die Konfiguration vorsieht, dass der SVWS-Benutzername und das Kennwort
	 * der Konfiguration auch für die Datenbankverbindung verwendet werden.
	 *
	 * @return true, falls der SVWS-Benutzername und das Kennwort der Konfiguration auch
	 *         für die Datenbankverbindung verwendet werden
	 */
	public boolean useDBLogin() {
		return factory.getConfig().useDBLogin();
	}


	/**
	 * Gibt den für die Datenbankverbindung verwendeten {@link DBDriver} zurück.
	 *
	 * @return der für die Datenbankverbindung verwendete {@link DBDriver}
	 */
	public DBDriver getDBDriver() {
		return factory.getConfig().getDBDriver();
	}


	/**
	 * Gibt das für die Datenbankverbindung verwendeten Schema der Datenbank zurück.
	 *
	 * @return das für die Datenbankverbindung verwendeten Schema der Datenbank
	 */
	public String getDBSchema() {
		return factory.getConfig().getDBSchema();
	}


	/**
	 * Gibt den Ort zurück, an dem die Datenbank der genutzten Datenbankverbindung liegt.
	 * Dies ist entweder der Host-Teil mit Port einer URL oder ein Dateiname, wenn es
	 * sich um eine lokal gespeicherte Datenbank wie SQLite oder Access MDB handelt.
	 *
	 * @return der Ort, an dem die Datenbank der genutzten Datenbankverbindung liegt.
	 */
	public String getDBLocation() {
		return factory.getConfig().getDBLocation();
	}


	/**
	 * Nimmt den Lock für DBEntityManager für den aktuellen Thread in Besitz und
	 * kehrt zurück, sofern dieser nicht im Besitz eines anderen Threads ist
	 * (siehe {@link ReentrantLock#lock()}).
	 */
	public void lock() {
		mutex.lock();
	}


	/**
	 * Gibt den Lock für DBEntityManager wieder frei, sofern dieser Thread im Besitz
	 * des Locks ist (siehe {@link ReentrantLock#isLocked()} und
	 * {@link ReentrantLock#unlock()})
	 */
	public void unlock() {
		if (mutex.isLocked() && mutex.isHeldByCurrentThread())
			mutex.unlock();
	}

	/**
	 * Prüft, ob eine Transaktion aktive ist
	 *
	 * @return true, falls eine Transaktion aktiv ist.
	 */
	public boolean hasActiveTransaction() {
		return em.getTransaction().isActive();
	}

	/**
	 * Beginnt eine neue Transaction.
	 */
	public void transactionBegin() {
		this.lock();
		em.getTransaction().begin();
	}


	/**
	 * Synchronisiert den Persistence-Kontext mit der Datenbank.
	 * Bei Transaktionen kann dies genutzt werden, um eine Reihenfolge der
	 * Befehle zu garantieren und damit Foreign-Key-Constraints einzuhalten.
	 */
	public void transactionFlush() {
		em.flush();
	}


	/**
	 * Beendet eine aktuelle Transaction mithilfe eines Commit.
	 *
	 * @return true, falls der Commit erfolgreich war und ansonsten false
	 */
	public boolean transactionCommit() {
		try {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
				em.clear();
			}
			return true;
		} catch (@SuppressWarnings("unused") RollbackException | IllegalStateException e) {
			return false;
		} finally {
			this.unlock();
		}
	}


	/**
	 * Beendet eine aktuelle Transaction mithilfe eines Commit.
	 *
	 * @throws RollbackException       wenn ein Fehler beim Commit auftritt
	 * @throws IllegalStateException   wenn keine Transaktion aktiv ist
	 */
	public void transactionCommitOrThrow() {
		try {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
				em.clear();
			}
		} finally {
			this.unlock();
		}
	}


	/**
	 * Beendet eine aktuelle Transaction mithilfe eines Rollback.
	 *
	 * @return true, falls der Rollback erfolgreich war und ansonsten false
	 */
	public boolean transactionRollback() {
		try {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			return true;
		} catch (@SuppressWarnings("unused") final PersistenceException e) {
			return false;
		} finally {
			this.unlock();
		}
	}


	/**
	 * Beendet eine aktuelle Transaction mithilfe eines Rollback.
	 *
	 * @throws PersistenceException wenn ein Fehler beim Rollback auftritt
	 */
	public void transactionRollbackOrThrow() {
		try {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
		} finally {
			this.unlock();
		}
	}


	/**
	 * Führt eine Update-Anfrage auf der Datenbank aus. Die zugehörige Transaktion
	 * darum muss manuell gehandhabt werden.
	 *
	 * @param query   die JPQL-Anfrage
	 *
	 * @return die Anzahl der aktualisierten Entities oder Integer.MIN_VALUE im Fehlerfall
	 */
	public int transactionExecuteUpdate(final String query) {
		try {
			return em.createQuery(query).executeUpdate();
		} catch (@SuppressWarnings("unused") PersistenceException | IllegalStateException e) {
			return Integer.MIN_VALUE;
		}
	}


	/**
	 * Führt eine Delete-Anfrage auf der Datenbank aus. Die zugehörige Transaktion
	 * darum muss manuell gehandhabt werden.
	 *
	 * @param query   die JPQL-Anfrage
	 *
	 * @return die Anzahl der gelöschten Entities oder Integer.MIN_VALUE im Fehlerfall
	 */
	public int transactionExecuteDelete(final String query) {
		return transactionExecuteUpdate(query);
	}



	/**
	 * Führt eine Update-Anfrage auf der Datenbank aus. Die zugehörige Transaktion
	 * darum muss manuell gehandhabt werden.
	 *
	 * @param sqlQuery   der SQL-Befehl
	 *
	 * @return die Anzahl der aktualisierten Entities oder Integer.MIN_VALUE im Fehlerfall
	 */
	public int transactionNativeUpdate(final String sqlQuery) {
		try {
			return em.createNativeQuery(sqlQuery).executeUpdate();
		} catch (@SuppressWarnings("unused") PersistenceException | IllegalStateException e) {
			return Integer.MIN_VALUE;
		}
	}


	/**
	 * Führt eine Update-Anfrage auf der Datenbank aus. Die zugehörige Transaktion
	 * darum muss manuell gehandhabt werden. Im Anschluss an die Update-Anfrage
	 * wird ein Flush ausgeführt.
	 *
	 * @param sqlQuery   der SQL-Befehl
	 *
	 * @return die Anzahl der aktualisierten Entities oder Integer.MIN_VALUE im Fehlerfall
	 */
	public int transactionNativeUpdateAndFlush(final String sqlQuery) {
		try {
			final int result = em.createNativeQuery(sqlQuery).executeUpdate();
			em.flush();
			return result;
		} catch (@SuppressWarnings("unused") PersistenceException | IllegalStateException e) {
			return Integer.MIN_VALUE;
		}
	}


	/**
	 * Führt eine Delete-Anfrage auf der Datenbank aus. Die zugehörige Transaktion
	 * darum muss manuell gehandhabt werden.
	 *
	 * @param sqlQuery   der SQL-Befehl
	 *
	 * @return die Anzahl der gelöschten Entities oder Integer.MIN_VALUE im Fehlerfall
	 */
	public int transactionNativeDelete(final String sqlQuery) {
		return transactionNativeUpdate(sqlQuery);
	}


	/**
	 * Führt eine Delete-Anfrage auf der Datenbank aus. Die zugehörige Transaktion
	 * darum muss manuell gehandhabt werden. Im Anschluss an die Update-Anfrage
	 * wird ein Flush ausgeführt.
	 *
	 * @param sqlQuery   der SQL-Befehl
	 *
	 * @return die Anzahl der gelöschten Entities oder Integer.MIN_VALUE im Fehlerfall
	 */
	public int transactionNativeDeleteAndFlush(final String sqlQuery) {
		return transactionNativeUpdateAndFlush(sqlQuery);
	}


	/**
	 * Persistiert die übergebene Entity in der Datenbank. Die zugehörige Transaktion
	 * darum muss manuell gehandhabt werden.
	 *
	 * @param entity   die zu persistierende Entity
	 *
	 * @return true, falls die Entity erfolgreich persistiert wurde und ansonsten false
	 */
	public boolean transactionPersist(final Object entity) {
		try {
			em.persist(entity);
			return true;
		} catch (@SuppressWarnings("unused") TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
			return false;
		}
	}


	/**
	 * Persistiert die übergebenen Entities in der Datenbank. Die zugehörige Transaktion
	 * darum muss manuell gehandhabt werden.
	 *
	 * @param entities   die zu persistierenden Entities
	 *
	 * @return true, falls die Entities erfolgreich persistiert wurden und ansonsten false
	 */
	public boolean transactionPersistAll(final Collection<? extends Object> entities) {
		if (entities == null)
			return false;
		try {
			for (final Object obj : entities)
				em.persist(obj);
			return true;
		} catch (@SuppressWarnings("unused") TransactionRequiredException | EntityExistsException | IllegalStateException e) {
			return false;
		}
	}


	/**
	 * Entfernt die übergebene Entity aus der Datenbank. Die zugehörige Transaktion
	 * darum muss manuell gehandhabt werden.
	 *
	 * @param entity   die zu entfernende Entity
	 *
	 * @return true, falls die Entity erfolgreich entfernt wurde und ansonsten false
	 */
	public boolean transactionRemove(final Object entity) {
		try {
			em.remove(entity);
			return true;
		} catch (@SuppressWarnings("unused") TransactionRequiredException | IllegalArgumentException e) {
			return false;
		}
	}

	/**
	 * Entfernt die übergebenen Entities aus der Datenbank. Die zugehörige Transaktion
	 * darum muss manuell gehandhabt werden.
	 *
	 * @param entities   die zu entfernenden Entities
	 *
	 * @return true, falls die Entities erfolgreich entfernt wurden und ansonsten false
	 */
	public boolean transactionRemoveAll(final Collection<? extends Object> entities) {
		if (entities == null)
			return false;
		try {
			for (final Object obj : entities)
				em.remove(obj);
			return true;
		} catch (@SuppressWarnings("unused") TransactionRequiredException | IllegalStateException e) {
			return false;
		}
	}


	/**
	 * Tauscht eine Entity gegen eine andere Entity in der Datenbank aus. Die
	 * zugehörige Transaktion darum muss manuell gehandhabt werden.
	 *
	 * @param oldEntity   die für den Tausch zu entfernende Entity
	 * @param newEntity   die neue Entity des Tauschs
	 *
	 * @return true, falls der Tausch erfolgreich durchgeführt wurde
	 */
	public boolean transactionReplace(final Object oldEntity, final Object newEntity) {
		try {
			em.remove(oldEntity);
			em.persist(newEntity);
			return true;
		} catch (@SuppressWarnings("unused") TransactionRequiredException | EntityExistsException | IllegalStateException | IllegalArgumentException e) {
			return false;
		}
	}


	/**
	 * Führt den SQL-Befehl direkt auf der JDBC-Connection (siehe {@link Connection}) aus.
	 * Die zugehörige Transaktion darum muss manuell gehandhabt werden.
	 *
	 * @param query   der SQL-Befehl
	 *
	 * @return die Anzahl der aktualisierten Entities, 0 falls der Befehl keine Rückgabe hat
	 *         oder Integer.MIN_VALUE im Fehlerfall
	 */
	public int transactionExecuteWithJDBCConnection(final String query) {
		try {
			final Connection conn = em.unwrap(Connection.class);
			try (Statement stmt = conn.createStatement()) {
				return stmt.executeUpdate(query);
			}
		} catch (@SuppressWarnings("unused") final SQLException e) {
			return Integer.MIN_VALUE;
		}
	}


	/**
	 * Persistiert die übergebenen Entities in der Datenbank. Die zugehörige Transaktion
	 * wird durch diese Methode gehandhabt.
	 *
	 * @param entities   die zu persistierenden Entities
	 *
	 * @return true, falls die Entities erfolgreich persistiert wurde und ansonsten false
	 */
	public boolean persistAll(final Collection<? extends Object> entities) {
		if (entities == null)
			return false;
		try {
			this.lock();
			this.transactionBegin();
			for (final Object obj : entities)
				em.persist(obj);
			if (this.transactionCommit())
				return true;
			this.transactionRollback();
			return false;
		} catch (@SuppressWarnings("unused") TransactionRequiredException | EntityExistsException | RollbackException | IllegalStateException e) {
			this.transactionRollback();
			return false;
		} finally {
			this.unlock();
		}
	}


	/**
	 * Persistiert die Entities aus dem angegebenen Bereich der übergebenen Liste in der Datenbank. Die
	 * zugehörige Transaktion wird durch diese Methode gehandhabt.
	 *
	 * @param entities    die zu persistierenden Entities
	 * @param indexFirst  der Index der ersten zu persistierenden Entity
	 * @param indexLast   der Index der letzten zu persistierenden Entity
	 *
	 * @return true, falls die Entities erfolgreich persistiert wurde und ansonsten false
	 */
	public boolean persistRange(final List<? extends Object> entities, final int indexFirst, final int indexLast) {
		if (entities == null)
			return false;
		final int first = (indexFirst < 0) ? 0 : indexFirst;
		final int last = (indexLast >= entities.size()) ? (entities.size() - 1) : indexLast;
		try {
			this.lock();
			this.transactionBegin();
			for (int i = first; i <= last; i++)
				em.persist(entities.get(i));
			if (this.transactionCommit())
				return true;
			this.transactionRollback();
			return false;
		} catch (@SuppressWarnings("unused") TransactionRequiredException | EntityExistsException | RollbackException | IllegalStateException e) {
			this.transactionRollback();
			return false;
		} finally {
			this.unlock();
		}
	}


	/**
	 * Persistiert die übergebene Entity in der Datenbank. Die
	 * zugehörige Transaktion wird durch diese Methode gehandhabt.
	 *
	 * @param entity   die zu persistierende Entity
	 *
	 * @return true, falls die Entity erfolgreich persistiert wurde und ansonsten false
	 */
	public boolean persist(final Object entity) {
		try {
			this.lock();
			this.transactionBegin();
			em.persist(entity);
			if (this.transactionCommit())
				return true;
			this.transactionRollback();
			return false;
		} catch (@SuppressWarnings("unused") TransactionRequiredException | EntityExistsException | RollbackException | IllegalStateException e) {
			return false;
		} finally {
			this.unlock();
		}
	}


	/**
	 * Entfernt die übergebene Entity aus der Datenbank. Die
	 * zugehörige Transaktion wird durch diese Methode gehandhabt.
	 *
	 * @param entity   die zu entfernende Entity
	 *
	 * @return true, falls die Entity erfolgreich entfernt wurde und ansonsten false
	 */
	public boolean remove(final Object entity) {
		try {
			this.lock();
			this.transactionBegin();
			em.remove(entity);
			if (this.transactionCommit())
				return true;
			this.transactionRollback();
			return false;
		} catch (@SuppressWarnings("unused") TransactionRequiredException | EntityExistsException | RollbackException | IllegalStateException e) {
			this.transactionRollback();
			return false;
		} finally {
			this.unlock();
		}
	}


	/**
	 * Tauscht eine Entity gegen eine andere Entity in der Datenbank aus. Die
	 * zugehörige Transaktion wird durch diese Methode gehandhabt.
	 *
	 * @param oldEntity   die für den Tausch zu entfernende Entity
	 * @param newEntity   die neue Entity des Tauschs
	 *
	 * @return true, falls der Tausch erfolgreich durchgeführt wurde
	 */
	public boolean replace(final Object oldEntity, final Object newEntity) {
		try {
			this.lock();
			this.transactionBegin();
			em.remove(oldEntity);
			em.persist(newEntity);
			if (this.transactionCommit())
				return true;
			this.transactionRollback();
			return false;
		} catch (@SuppressWarnings("unused") TransactionRequiredException | EntityExistsException | RollbackException | IllegalStateException e) {
			this.transactionRollback();
			return false;
		} finally {
			this.unlock();
		}
	}


	/**
	 * Führt eine JPQL-Lösch-Anfrage durch. Die zugehörige Transaktion wird durch diese
	 * Methode gehandhabt.
	 *
	 * @param query   die JPQL-Anfrage
	 *
	 * @return die Anzahl der gelöschten Entities oder Integer.MIN_VALUE im Fehlerfall
	 */
	public int executeDelete(final String query) {
		return executeUpdate(query);
	}


	/**
	 * Führt eine JPQL-Aktualisierungs-Anfrage durch. Die zugehörige Transaktion wird durch diese
	 * Methode gehandhabt.
	 *
	 * @param query   die JPQL-Anfrage
	 *
	 * @return die Anzahl der aktualisierten Entities oder Integer.MIN_VALUE im Fehlerfall
	 */
	public int executeUpdate(final String query) {
		try {
			this.lock();
			this.transactionBegin();
			final int count = em.createQuery(query).executeUpdate();
			if (this.transactionCommit())
				return count;
			this.transactionRollback();
			return Integer.MIN_VALUE;
		} catch (@SuppressWarnings("unused") TransactionRequiredException | EntityExistsException | RollbackException | IllegalStateException e) {
			this.transactionRollback();
			return Integer.MIN_VALUE;
		} finally {
			this.unlock();
		}
	}


	/**
	 * Führt eine SQL-Lösch-Anfrage durch. Die zugehörige Transaktion wird durch diese
	 * Methode gehandhabt.
	 *
	 * @param query   die SQL-Anfrage
	 *
	 * @return die Anzahl der gelöschten Entities oder Integer.MIN_VALUE im Fehlerfall
	 */
	public int executeNativeDelete(final String query) {
		return executeNativeUpdate(query);
	}


	/**
	 * Führt eine SQL-Aktualisierungs-Anfrage durch. Die zugehörige Transaktion wird durch diese
	 * Methode gehandhabt.
	 *
	 * @param query   die SQL-Anfrage
	 *
	 * @return die Anzahl der aktualisierten Entities oder Integer.MIN_VALUE im Fehlerfall
	 */
	public int executeNativeUpdate(final String query) {
		try {
			this.lock();
			this.transactionBegin();
			final int count = em.createNativeQuery(query).executeUpdate();
			if (this.transactionCommit())
				return count;
			this.transactionRollback();
			return Integer.MIN_VALUE;
		} catch (@SuppressWarnings("unused") TransactionRequiredException | EntityExistsException | RollbackException | IllegalStateException e) {
			this.transactionRollback();
			return Integer.MIN_VALUE;
		} finally {
			this.unlock();
		}
	}


	/**
	 * Führt den SQL-Befehl direkt auf der JDBC-Connection (siehe {@link Connection}) aus.
	 * Die zugehörige Transaktion wird durch diese Methode gehandhabt.
	 *
	 * @param query   der SQL-Befehl
	 *
	 * @return die Anzahl der aktualisierten Entities, 0 falls der Befehl keine Rückgabe hat
	 *         oder Integer.MIN_VALUE im Fehlerfall
	 */
	public int executeWithJDBCConnection(final String query) {
		try {
			this.lock();
			this.transactionBegin();
			final Connection conn = em.unwrap(Connection.class);
			try (Statement stmt = conn.createStatement()) {
				final int count = stmt.executeUpdate(query);
				if (this.transactionCommit())
					return count;
				this.transactionRollback();
				return Integer.MIN_VALUE;
			}
		} catch (@SuppressWarnings("unused") final SQLException e) {
			this.transactionRollback();
			return Integer.MIN_VALUE;
		} finally {
			this.unlock();
		}
	}


	/**
	 * Führt die übergebenen SQL-Befehle direkt auf der JDBC-Connection (siehe {@link Connection}) aus.
	 * Die zugehörige Transaktion wird durch diese Methode gehandhabt.
	 *
	 * @param queries   die Liste der SQL-Befehle
	 *
	 * @return die Anzahl der aktualisierten Entities, 0 falls die Befehle keine Rückgabe haben
	 *
	 * @throws DBException im Fehlerfall
	 */
	@SuppressWarnings("resource")
	public int executeBatchWithJDBCConnection(final List<String> queries) throws DBException {
		try {
			this.lock();
			this.transactionBegin();
			final Connection conn = em.unwrap(Connection.class);
			try (Statement stmt = conn.createStatement()) {
				for (final String query : queries)
					stmt.addBatch(query);
				final int[] count = stmt.executeBatch();
				if (this.transactionCommit())
					return Arrays.stream(count).sum();
				this.transactionRollback();
			}
			throw new DBException("Fehler beim Ausführen der SQL-Befehle");
		} catch (final SQLException e) {
			this.transactionRollback();
			throw new DBException(e);
		} finally {
			this.unlock();
		}
	}


	/**
	 * Führt eine SQL-Abfrage aus und gibt das Ergebnis als eine Liste von Objekt-Arrays zurück.
	 * Dabei entspricht jedes Objekt-Array einem Datensatz der Abfrage.
	 *
	 * @param sql   der SQL-Befehl
	 *
	 * @return das Abfrage-Ergebnis als Objekt-Array
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> query(final String sql) {
		return em.createNativeQuery(sql).getResultList();
	}


	/**
	 * Diese Methode fügt die Entities aus dem angegebenen Bereich der übergebenen Liste in die angebene
	 * Tabelle mit den übergebenen Spalten ein. Die entsprechende SQL-INSERT-Anfrage wird aus den übergebenen
	 * Daten generiert und im Rahmen einer Transaktion ausgeführt. Dies Ausführung erfolgt über ein
	 * {@link PreparedStatement} mit Batch-Processing auf der JDBC-Connection (siehe {@link Connection}), die
	 * aus dem intern verwendeten {@link EntityManager} ausgelesen wird.
	 * Die zugehörige Transaktion darum muss manuell gehandhabt werden.
	 *
	 * @param tablename   der Name der Tabelle, in die eingefügt wird
	 * @param colnames    die Liste mit den Spaltennamen, die zu der Reihenfolge der Object-Array-Elementen
	 *                    der Entitäten passen muss
	 * @param entities    die Liste mit den einzelnen Datensätzen in Form von Object-Arrays
	 * @param indexFirst  der Index des ersten Datensatzes aus der Entitätenliste, der geschrieben wird
	 * @param indexLast   der Index des letzten Datensatzes aus der Entitätenliste, der geschrieben wird
	 *
	 * @return true, falls die SQL-Anfrage erfolgreich ausgeführt wurde und ansonsten false
	 */
	public boolean transactionInsertRangeNative(final String tablename, final List<String> colnames, final List<Object[]> entities, final int indexFirst,
			final int indexLast) {
		if ((entities == null) || (colnames == null) || (tablename == null) || (colnames.isEmpty()) || (entities.isEmpty()))
			return false;
		final int first = (indexFirst < 0) ? 0 : indexFirst;
		final int last = (indexLast >= entities.size()) ? (entities.size() - 1) : indexLast;
		try {
			final Connection conn = em.unwrap(Connection.class);
			final StringBuilder sb = new StringBuilder();
			final String prepCols = colnames.stream().map(col -> "?").collect(Collectors.joining(", ", "(", ")"));
			sb.append("INSERT INTO ").append(tablename).append("(")
					.append(colnames.stream().collect(Collectors.joining(", ")))
					.append(") VALUES ")
					.append(IntStream.rangeClosed(first, last).mapToObj(e -> prepCols).collect(Collectors.joining(", ")));
			final String sql = sb.toString();
			try (PreparedStatement prepared = conn.prepareStatement(sql)) {
				int pos = 1;
				for (int i = first; i <= last; i++) {
					final Object[] data = entities.get(i);
					for (int j = 0; j < colnames.size(); j++) {
						if ((factory.getConfig().getDBDriver() == DBDriver.SQLITE) && (data[j] instanceof final Timestamp timestamp)) {
							prepared.setString(pos, datetimeFormatter.format(timestamp.toLocalDateTime()));
						} else if ((factory.getConfig().getDBDriver() == DBDriver.SQLITE) && (data[j] instanceof final Date date)) {
							prepared.setString(pos, dateFormatter.format(date.toLocalDate()));
						} else if ((factory.getConfig().getDBDriver() == DBDriver.SQLITE) && (data[j] instanceof final Time time)) {
							prepared.setString(pos, timeFormatter.format(time.toLocalTime()));
						} else {
							prepared.setObject(pos, data[j]);
						}
						pos++;
					}
				}
				prepared.executeUpdate();
			}
			return true;
		} catch (@SuppressWarnings("unused") final SQLException e) {
			return false;
		}
	}


	/**
	 * Diese Methode fügt die Entities aus dem angegebenen Bereich der übergebenen Liste in die angebene
	 * Tabelle mit den übergebenen Spalten ein. Die entsprechende SQL-INSERT-Anfrage wird aus den übergebenen
	 * Daten generiert und im Rahmen einer Transaktion ausgeführt. Dies Ausführung erfolgt über ein
	 * {@link PreparedStatement} mit Batch-Processing auf der JDBC-Connection (siehe {@link Connection}), die
	 * aus dem intern verwendeten {@link EntityManager} ausgelesen wird.
	 *
	 * @param tablename   der Name der Tabelle, in die eingefügt wird
	 * @param colnames    die Liste mit den Spaltennamen, die zu der Reihenfolge der Object-Array-Elementen
	 *                    der Entitäten passen muss
	 * @param entities    die Liste mit den einzelnen Datensätzen in Form von Object-Arrays
	 * @param indexFirst  der Index des ersten Datensatzes aus der Entitätenliste, der geschrieben wird
	 * @param indexLast   der Index des letzten Datensatzes aus der Entitätenliste, der geschrieben wird
	 *
	 * @return true, falls die SQL-Anfrage erfolgreich ausgeführt wurde und ansonsten false
	 */
	public boolean insertRangeNative(final String tablename, final List<String> colnames, final List<Object[]> entities, final int indexFirst,
			final int indexLast) {
		if ((entities == null) || (colnames == null) || (tablename == null) || (colnames.isEmpty()) || (entities.isEmpty()))
			return false;
		try {
			this.lock();
			this.transactionBegin();
			final boolean success = transactionInsertRangeNative(tablename, colnames, entities, indexFirst, indexLast);
			if (success && this.transactionCommit())
				return true;
			this.transactionRollback();
			return false;
		} catch (@SuppressWarnings("unused") PersistenceException | IllegalStateException e) {
			this.transactionRollback();
			return false;
		} finally {
			this.unlock();
		}
	}



	private String toSQLStringWitEscapeSequences(final String str) {
		if (str == null)
			return null;
		if (factory.getConfig().getDBDriver() == DBDriver.SQLITE)
			return "'" + str.replace("'", "''").replace("\0", "'||char(0)||'") + "'";
		// else MariaDB / MYSQL ...
		return "'" + str.replace("\\", "\\\\").replace("'", "\\'") + "'";
	}


	/**
	 * Führt eine SQL-Aktualisierungs-Anfrage durch. Die zugehörige Transaktion wird durch diese
	 * Methode gehandhabt.
	 *
	 * @param query   die SQL-Anfrage
	 *
	 * @return die Anzahl der aktualisierten Entities oder Integer.MIN_VALUE im Fehlerfall
	 */
	protected int internalExecuteNativeUpdateConnectionUnprepared(final String query) {
		try {
			this.lock();
			this.transactionBegin();
			final Connection conn = em.unwrap(Connection.class);
			try (Statement statement = conn.createStatement()) {
				final int count = statement.executeUpdate(query);
				if (this.transactionCommit())
					return count;
				this.transactionRollback();
				return Integer.MIN_VALUE;
			}
		} catch (@SuppressWarnings("unused") TransactionRequiredException | EntityExistsException | RollbackException | IllegalStateException
				| SQLException e) {
			this.transactionRollback();
			return Integer.MIN_VALUE;
		} finally {
			this.unlock();
		}
	}


	/**
	 * Diese Methode fügt die Entities aus dem angegebenen Bereich der übergebenen Liste in die angebene
	 * Tabelle mit den übergebenen Spalten ein. Die entsprechende native SQL-INSERT-Anfrage wird aus den übergebenen
	 * Daten generiert und im Rahmen einer Transaktion ausgeführt.
	 *
	 * @param tablename      der Name der Tabelle, in die eingefügt wird
	 * @param colnames       die Liste mit den Spaltennamen, die zu der Reihenfolge der Object-Array-Elementen
	 *                       der Entitäten passen muss
	 * @param entities       die Liste mit den einzelnen Datensätzen in Form von Object-Arrays
	 * @param indexFirst     der Index des ersten Datensatzes aus der Entitätenliste, der geschrieben wird
	 * @param indexLast      der Index des letzten Datensatzes aus der Entitätenliste, der geschrieben wird
	 * @param maxSQLStrLen   die maximal erlaubte Länge für den SQL-String, der Versuch zu schreiben schlägt fehlt,
	 *                       wenn die Länge überschritte wird. (-1 um die Prüfung zu deaktivieren)
	 *
	 * @return true, falls die SQL-Anfrage erfolgreich ausgeführt wurde und ansonsten false
	 */
	public boolean insertRangeNativeUnprepared(final String tablename, final List<String> colnames, final List<Object[]> entities,
			final int indexFirst, final int indexLast, final int maxSQLStrLen) {
		if ((entities == null) || (colnames == null) || (tablename == null) || (colnames.isEmpty()) || (entities.isEmpty()))
			return false;
		final int first = (indexFirst < 0) ? 0 : indexFirst;
		final int last = (indexLast >= entities.size()) ? (entities.size() - 1) : indexLast;
		final StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ").append(tablename).append("(")
				.append(colnames.stream().collect(Collectors.joining(", ")))
				.append(") VALUES ");
		for (int i = first; i <= last; i++) {
			sb.append("(");
			final Object[] data = entities.get(i);
			for (int j = 0; j < colnames.size(); j++) {
				if (data[j] == null)
					sb.append("null");
				else if (data[j] instanceof final Timestamp timestamp)
					sb.append(toSQLStringWitEscapeSequences(datetimeFormatter.format(timestamp.toLocalDateTime())));
				else if (data[j] instanceof final Date date)
					sb.append(toSQLStringWitEscapeSequences(dateFormatter.format(date.toLocalDate())));
				else if (data[j] instanceof final Time time)
					sb.append(toSQLStringWitEscapeSequences(timeFormatter.format(time.toLocalTime())));
				else if (data[j] instanceof final String str)
					sb.append(toSQLStringWitEscapeSequences(str));
				else if (data[j] instanceof final Number number)
					sb.append(number);
				else
					return false;
				if (j < (colnames.size() - 1))
					sb.append(",");
			}
			sb.append(")");
			if (i != last)
				sb.append(",");
		}
		if ((maxSQLStrLen > 0) && (sb.length() > maxSQLStrLen))
			return false;
		return internalExecuteNativeUpdateConnectionUnprepared(sb.toString()) != Integer.MIN_VALUE;
	}


	/**
	 * Erzeugt eine TypedQuery gemäß der Methode {@link EntityManager#createQuery(String, Class)}
	 *
	 * @param <T>     die Klasse des Ergebnistyps
	 * @param query   der JPQL-String der Anfrage
	 * @param cl      das Klassen-Objekt für den Ergenistyp
	 *
	 * @return die Anfrage als {@link TypedQuery}
	 */
	public <T> TypedQuery<T> query(final String query, final Class<T> cl) {
		return em.createQuery(query, cl);
	}


	/**
	 * Erzeugt eine TypedQuery gemäß der Methode {@link EntityManager#createQuery(String, Class)},
	 * setzt die übergebenen Parameter der Query in der angegebenen Reihenfolge und gibt das Ergebnis
	 * als eine Liste von DB-DTOs des angegebenen Typs zurück.
	 *
	 * @param <T>      die DTO-Klasse
	 * @param query   der JPQL-String der Anfrage
	 * @param cl       das Klassenobjekt der DTO-Klasse
	 * @param params  die Parameter der JPQL-Anfrage
	 *
	 * @return die Liste mit DTO-Objekten
	 */
	public <T> List<T> queryList(final String query, final Class<T> cl, final Object... params) {
		TypedQuery<T> q = em.createQuery(query, cl);
		for (int i = 0; i < params.length; i++) {
			q = q.setParameter(i + 1, params[i]);  // Die Parameter in JPQL beginnnen mit 1 und nicht mit 0...
		}
		return q.getResultList();
	}


	/**
	 * Führt eine Native SQL-Abfrage auf die Datenbank aus und gibt das Ergebnis als
	 * List von DTO-Objekten des Typs T zurück.
	 *
	 * @param <T>      die DTO-Klasse
	 * @param query    die SQL-Abfrage
	 * @param cl       das Klassenobjekt der DTO-Klasse
	 *
	 * @return die Liste mit DTO-Objekten
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryNative(final String query, final Class<T> cl) {
		return em.createNativeQuery(query, cl).getResultList();
	}


	/**
	 * Führt eine Native SQL-Abfrage auf die Datenbank aus und gibt das Ergebnis als
	 * List von DTO-Objekten des Typs T zurück.
	 *
	 * @param <T>      die DTO-Klasse
	 * @param query    die SQL-Abfrage
	 *
	 * @return die Liste mit DTO-Objekten
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryNative(final String query) {
		return em.createNativeQuery(query).getResultList();
	}


	/**
	 * Führt eine Native SQL-Abfrage auf die Datenbank aus und gibt das Ergebnis als
	 * List von DTO-Objekten des Typs T zurück.
	 *
	 * @param <T>      die DTO-Klasse
	 * @param query    die SQL-Abfrage
	 * @param resultSetMapping       der Name SQL-Result-Set-Mappings ({@link jakarta.persistence.SqlResultSetMapping})
	 *
	 * @return die Liste mit DTO-Objekten
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryNative(final String query, final String resultSetMapping) {
		return em.createNativeQuery(query, resultSetMapping).getResultList();
	}


	/**
	 * Gibt eine Native SQL-Abfrage auf die Datenbank zur Ausführung zurück.
	 *
	 * @param query    die SQL-Abfrage
	 *
	 * @return die Abfrage
	 */
	public Query getNativeQuery(final String query) {
		return em.createNativeQuery(query);
	}


	/**
	 * Erzeugt eine TypedQuery gemäß der Methode {@link EntityManager#createNamedQuery(String, Class)}
	 *
	 * @param <T>     die Klasse des Ergebnistyps
	 * @param name    der Name der JPQL-Anfrage
	 * @param cl      das Klassen-Objekt für den Ergenistyp
	 *
	 * @return die Anfrage als {@link TypedQuery}
	 */
	public <T> TypedQuery<T> queryNamed(final String name, final Class<T> cl) {
		return em.createNamedQuery(name, cl);
	}


	/**
	 * Bestimmt den Query-String aus dem angebenen Attribut der DTO-Klasse
	 *
	 * @param <T>              der Typ der DTO-Klasse
	 * @param cl               die Klasse des DTOs
	 * @param queryAttribute   das auszulesende Attribut der DTO-Klasse mit dem Query-String
	 *
	 * @return der Query-String
	 */
	private static <T> String getQueryString(final Class<T> cl, final String queryAttribute) {
		try {
			final Field f = cl.getField(queryAttribute);
			final Object obj = f.get(null);
			if (obj instanceof final String s)
				return s;
			throw new NoSuchFieldException("Das angeforderte Attribut für die Query ist nicht vom Typ String.");
		} catch (final Exception e) {
			e.printStackTrace();
			throw new DeveloperNotificationException("Der Datenbankzugriff ist fehlgeschlagen, da das Query-Attribut " + queryAttribute + " bei der Klasse "
					+ cl.getCanonicalName() + " nicht vorhanden ist.");
		}
	}


	/**
	 * Stellt eine Datenbank-Anfrage für alle Datensätze vom angegebenen DTO-Typ
	 * und gibt eine Liste dieser Daten zurück.
	 *
	 * @param <T>     die DTO-Klasse
	 * @param cl      das Klassen-Objekt für die DTO-Klasse
	 *
	 * @return die Liste mit den einzelnen Datensätzen
	 */
	public <T> List<T> queryAll(final Class<T> cl) {
		return queryList(getQueryString(cl, "QUERY_ALL"), cl);
	}


	/**
	 * Stellt eine Datenbank-Anfrage für alle Datensätze vom angegebenen DTO-Typ
	 * und gibt eine Liste dieser Daten zurück.
	 *
	 * @param <T>     die DTO-Klasse
	 * @param cl      das Klassen-Objekt für die DTO-Klasse
	 *
	 * @return die Liste mit den einzelnen Datensätzen
	 */
	public <T> List<T> migrationQueryAll(final Class<T> cl) {
		return queryList(getQueryString(cl, "QUERY_MIGRATION_ALL"), cl);
	}


	/**
	 * Stellt eine Datenbank-Anfrage für den ersten Datensatz vom angegebenen DTO-Typ
	 * und gibt diesen zurück. Sollte kein Datensatz vorhanden sein, so wird null
	 * zurückgegeben
	 *
	 * @param <T>     die DTO-Klasse
	 * @param cl      das Klassen-Objekt für die DTO-Klasse
	 *
	 * @return der erste Datensatz oder null
	 */
	public <T> T querySingle(final Class<T> cl) {
		final List<T> entries = queryAll(cl);
		return ((entries == null) || (entries.isEmpty())) ? null : entries.get(0);
	}



	/**
	 * Stellt eine Datenbank-Anfrage für den Datensatz vom angegebenen DTO-Typ
	 * mit den übergebenen Werten des Primärschlüssels und gibt
	 * diesen Datensatz zurück. Sollte kein Datensatz vorhanden sein, so wird null
	 * zurückgegeben
	 *
	 * @param <T>   die DTO-Klasse
	 * @param cl    das Klassen-Objekt für die DTO-Klasse
	 * @param id    die Werte des Primärschlüssels für den gesuchten Datensatz
	 *
	 * @return der erste Datensatz oder null
	 */
	public <T> T queryByKey(final Class<T> cl, final Object... id) {
		final List<T> entries = queryList(getQueryString(cl, "QUERY_PK"), cl, id);
		if (entries.isEmpty())
			return null;
		return entries.get(0);
	}


	/**
	 * Stellt eine Datenbank-Anfrage für die Datensätze vom angegebenen DTO-Typ,
	 * welcher einen Primärschlüssel aus einem Attribut hat, mit den übergebenen Werten
	 * für den Primärschlüssels und gibt die Datensätze zurück.
	 * Sollte kein Datensatz vorhanden sein, so wird eine leere Liste zurückgegeben.
	 *
	 * @param <T>   die DTO-Klasse
	 * @param cl    das Klassen-Objekt für die DTO-Klasse
	 * @param ids   die Werte des Primärschlüssels für die gesuchten Datensätze
	 *
	 * @return doe Liste mit den Datensätzen
	 */
	public <T> List<T> queryByKeyList(final Class<T> cl, final Collection<?> ids) {
		if ((ids == null) || ids.isEmpty())
			return new ArrayList<>();
		return queryList(getQueryString(cl, "QUERY_LIST_PK"), cl, ids);
	}


	/**
	 * Bestimmt für die übergebene DTOKlasse die nächste verfügbare Datenbank-ID
	 *
	 * @param <T>   der Typ der DtoKlasse
	 * @param t     die DtoKlasse
	 *
	 * @return die nächste verfügbare ID
	 */
	public <T> long transactionGetNextID(final Class<T> t) {
		final String tableName = t.getAnnotation(Table.class).name();
		if (tableName == null)
			throw new NullPointerException("Die angegebene Klasse hat keine Tabellen-Annotation");
		final Tabelle_Schema_AutoInkremente tabelleSvwsDbAutoInkremente = new Tabelle_Schema_AutoInkremente();
		final String col_MaxID = tabelleSvwsDbAutoInkremente.col_MaxID.name();
		final String tableAutoInkrementeName = tabelleSvwsDbAutoInkremente.name();
		final Query q = em.createNativeQuery("SELECT " + col_MaxID + " FROM " + tableAutoInkrementeName + " WHERE nametabelle = ?tableName");
		q.setParameter("tableName", tableName);
		try {
			final Long currentID = (Long) q.getSingleResult();
			return (currentID == null) ? 1 : (currentID + 1);
		} catch (@SuppressWarnings("unused") final NoResultException e) {
			return 1;
		}
	}


	/**
	 * Bestimmt für die übergebene DTOKlasse die nächste verfügbare Datenbank-ID
	 *
	 * @param tableName   der Name der Tabelle mit Autoinkrement
	 *
	 * @return die nächste verfügbare ID
	 */
	public long transactionGetNextIDByTablename(final String tableName) {
		if (tableName == null)
			throw new NullPointerException("Der angebene Tabellenname ist null.");
		final Tabelle_Schema_AutoInkremente tabelleSvwsDbAutoInkremente = new Tabelle_Schema_AutoInkremente();
		final String col_MaxID = tabelleSvwsDbAutoInkremente.col_MaxID.name();
		final String tableAutoInkrementeName = tabelleSvwsDbAutoInkremente.name();
		final Query q = em.createNativeQuery("SELECT " + col_MaxID + " FROM " + tableAutoInkrementeName + " WHERE nametabelle = ?tableName");
		q.setParameter("tableName", tableName);
		try {
			final Long currentID = (Long) q.getSingleResult();
			return (currentID == null) ? 1 : (currentID + 1);
		} catch (@SuppressWarnings("unused") final NoResultException e) {
			return 1;
		}
	}


	/**
	 * Generische Methode zum Speichern von Daten
	 *
	 * @param t       Typ der zu speichernden Daten
	 * @param idApplicator methode die die ID im Objekt übernimmt
	 * @param <T>     erwarteter Rückgabetyp
	 *
	 * @return Rückgabetyp
	 */
	public <T> boolean persistNewWithAutoInkrement(final Class<T> t, final LongFunction<T> idApplicator) {
		this.transactionBegin();
		final long nextID = transactionGetNextID(t);
		final T daten = idApplicator.apply(nextID);
		this.transactionPersist(daten);
		if (!this.transactionCommit()) {
			this.transactionRollback();
			return false;
		}
		return true;
	}

}
