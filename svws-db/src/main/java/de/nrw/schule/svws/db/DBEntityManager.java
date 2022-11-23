package de.nrw.schule.svws.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.RollbackException;
import jakarta.persistence.TransactionRequiredException;
import jakarta.persistence.TypedQuery;


/**
 * Die Klasse DBEntityManager regelt den Datenbankzugriff im SVWS-Servers durch
 * die interne Nutzung eines {@link EntityManager} aus der Jakarta Persistence API (JPA). 
 * Dafür werden zahlreiche Methoden zur Verfügung gestellt.
 */
public class DBEntityManager implements AutoCloseable {

	/**
	 * Gibt an, ob das Caching der SVWS-Server-Datenbankdaten beim Zugriff auf die Datenbank genutzt werden soll.
	 * Dies ist als false vorkonfiguriert, da dies zu Problemen führt, wenn mehrere Prozesse auf die Datenbank zugreifen
	 * dürfen. Dies ist gerade durch den Parallelbetrieb von Schild3 zum SVWS-Server aber der Normalfall, so dass
	 * das Aktivieren dieser Option gefährlich ist und vermutlich zu Datenverlusten führt.
	 */
	public static final boolean use_db_caching = false;

	
	/** Formatiert ein Datum als String, in der Art, wie es für die Datumseingabe in einer SQL-Anfrage genutzt wird. */
	private static DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	/** Formatiert ein Datum mit Zeitangabe als String, in der Art, wie es für die Datumseingabe in einer SQL-Anfrage genutzt wird. */
	private static DateTimeFormatter datetime_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	/** Der Datenbank-Benutzer, der dieser Verbindung zugeordnet ist. */
	private final Benutzer user; 	
	
	/** Die verwendete Datenbank-Konfiguration {@link DBConfig} */
	private final DBConfig config;
	
	/** Der intern verwendete {@link EntityManager} der Jakarta Persistence API (JPA) */
	EntityManager em;

	/** Ein intern verwendeter Mutex, der garantiert, dass immer nur ein Thread eine Transaction auf diesem DBEntityManager ausführt. */
	private ReentrantLock mutex = new ReentrantLock();



	/**
	 * Konstruktor für die interne Nutzung. Es wird eine neue Instanz auf Basis
	 * der übergebenen Konfiguration (siehe {@link DBConfig}) erstellt. Für den 
	 * Fall, dass die Verbindung nicht erfolgreich ist, wird eine 
	 * {@link DBConnectionException} generiert.
	 * 
	 * @param user     der Benutzer, der dieser Verbindung zugeordnet ist.
	 * @param config   die Datenbank-Konfiguration
	 */
	DBEntityManager(Benutzer user, DBConfig config) {
		this.user = user;
		this.config = config;
		this.em = user.connectionManager.getNewJPAEntityManager();
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
	 * Erneuert die Verbindung zur Datenbank. Dabei geht der Zustand
	 * in Bezug auf die verwalteten Entitäten verloren. 
	 * 
	 * @throws DBConnectionException   die Verbindung konnte nicht neu aufgebaut werden
	 */
	public void reconnect() throws DBConnectionException {
		if (em != null) {
			em.clear();
			em.close();
		}
		try {
			em = user.connectionManager.getNewJPAEntityManager();
		} catch (IllegalStateException e) {
			throw new DBConnectionException(e);
		}
	}
	
	
	@Override
	public void close() {
		if (em != null) {
			em.clear();
			em.close();
			em = null;
		}
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
		return config.useDBLogin();
	}
	
	
	/**
	 * Gibt den für die Datenbankverbindung verwendeten {@link DBDriver} zurück.
	 *  
	 * @return der für die Datenbankverbindung verwendete {@link DBDriver} 
	 */
	public DBDriver getDBDriver() {
		return config.getDBDriver();
	}
	
	
	/**
	 * Gibt das für die Datenbankverbindung verwendeten Schema der Datenbank zurück.
	 * 
	 * @return das für die Datenbankverbindung verwendeten Schema der Datenbank
	 */
	public String getDBSchema() {
		return config.getDBSchema();
	}
	
	
	/**
	 * Gibt den Ort zurück, an dem die Datenbank der genutzten Datenbankverbindung liegt.
	 * Dies ist entweder der Host-Teil mit Port einer URL oder ein Dateiname, wenn es
	 * sich um eine lokal gespeicherte Datenbank wie SQLite oder Access MDB handelt.
	 *  
	 * @return der Ort, an dem die Datenbank der genutzten Datenbankverbindung liegt.
	 */
	public String getDBLocation() {
		return config.getDBLocation();
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
	 * Beginnt eine neue Transaction.
	 */
	public void transactionBegin() {
		this.lock();
		em.getTransaction().begin();		
		// TODO Handle Exceptions
	}
	
	
	/**
	 * Beendet eine aktuelle Transaction mithilfe eines Commit.
	 * 
	 * @return true, falls der Commit erfolgreich war und ansonsten false
	 */
	public boolean transactionCommit() {
		try {
			if (em.getTransaction().isActive())
				em.getTransaction().commit();
			return true;
		} catch (@SuppressWarnings("unused") RollbackException | IllegalStateException e) {
			return false;
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
		} catch (@SuppressWarnings("unused") PersistenceException e) {
			return false;
		} finally {
			this.unlock();
		}
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
		} catch (@SuppressWarnings("unused") PersistenceException  | IllegalStateException e) {
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
	 * Persistiert die übergebene Entity in der Datenbank. Die zugehörige Transaktion 
	 * darum muss manuell gehandhabt werden.  
	 * 
	 * @param entity   die zu persistierende Entity
	 *  
	 * @return true, falls die Entity erfolgreich persistiert wurde und ansonsten false 
	 */
	public boolean transactionPersist(Object entity) {
		try {
			em.persist(entity);
			return true;
		} catch (@SuppressWarnings("unused") TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
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
	public boolean transactionRemove(Object entity) {
		try {
			em.remove(entity);
			return true;
		} catch (@SuppressWarnings("unused") TransactionRequiredException | IllegalArgumentException e) {
			return false;
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
	public boolean persistAll(Collection<? extends Object> entities) {
		if (entities == null)
			return false;
		try {
			this.lock();
			this.transactionBegin();
			for (Object obj : entities)
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
	public boolean persistRange(final List<? extends Object> entities, int indexFirst, int indexLast) {
		if (entities == null)
			return false;
		int first = (indexFirst < 0) ? 0 : indexFirst;
		int last = (indexLast >= entities.size()) ? entities.size() - 1 : indexLast;
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
	public boolean persist(Object entity) {
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
	public boolean remove(Object entity) {
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
	public boolean replace(Object oldEntity, Object newEntity) {
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
	public int executeDelete(String query) {
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
	public int executeUpdate(String query) {
		try {
			this.lock();
			this.transactionBegin();
			int count = em.createQuery(query).executeUpdate();
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
	public int executeNativeDelete(String query) {
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
	public int executeNativeUpdate(String query) {
		try {
			this.lock();
			this.transactionBegin();
			int count = em.createNativeQuery(query).executeUpdate();
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
	 * Führt den SQL-Befehlt direkt auf der JDBC-Connection (siehe {@link Connection}) aus. 
	 * Die zugehörige Transaktion wird durch diese Methode gehandhabt.
	 * 
	 * @param query   der SQL-Befehl
	 * 
	 * @return die Anzahl der aktualisierten Entities, 0 falls der Befehl keine Rückgabe hat 
	 *         oder Integer.MIN_VALUE im Fehlerfall
	 */
	public int executeWithJDBCConnection(String query) {
		try {
			this.lock();
			this.transactionBegin();
			Connection conn = em.unwrap(Connection.class);
			try (Statement stmt = conn.createStatement()) {
				int count = stmt.executeUpdate(query); 
				if (this.transactionCommit())
					return count;
				this.transactionRollback();
				return Integer.MIN_VALUE;
			}
		} catch (@SuppressWarnings("unused") SQLException e) {
			this.transactionRollback();
			return Integer.MIN_VALUE;
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
	public List<Object[]> query(String sql) {
		return em.createNativeQuery(sql).getResultList();
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
	public boolean insertRangeNative(final String tablename, final List<String> colnames, final List<Object[]> entities, int indexFirst, int indexLast) {
		if ((entities == null) || (colnames == null) || (tablename == null) ||
				(colnames.size() == 0) || (entities.size() == 0))
			return false;
		int first = (indexFirst < 0) ? 0 : indexFirst;
		int last = (indexLast >= entities.size()) ? entities.size() - 1 : indexLast;
		try {
			this.lock();
			this.transactionBegin();
			try (Connection conn = em.unwrap(Connection.class)) {
				String sql = "INSERT INTO " + tablename + "("
						+ colnames.stream().collect(Collectors.joining(", ")) 
						+ ") VALUES ("
						+ colnames.stream().map(col -> "?").collect(Collectors.joining(", "))
						+ ")";
				try (PreparedStatement prepared = conn.prepareStatement(sql)) {
					for (int i = first; i <= last; i++) {
						Object[] data = entities.get(i);
						for (int j = 0; j < colnames.size(); j++) {
							if ((config.getDBDriver() == DBDriver.SQLITE) && (data[j] instanceof Timestamp)) {
								prepared.setString(j+1, datetime_formatter.format(((Timestamp)data[j]).toLocalDateTime()));
							} else if ((config.getDBDriver() == DBDriver.SQLITE) && (data[j] instanceof Date)) {
								prepared.setString(j+1, date_formatter.format(((Date)data[j]).toLocalDate()));
							} else {
								prepared.setObject(j+1, data[j]);
							}
						}
						prepared.addBatch();
					}
					prepared.executeBatch();
					prepared.close();
				}
			}
			if (this.transactionCommit())
				return true;
			this.transactionRollback();
			return false;
		} catch (@SuppressWarnings("unused") SQLException | PersistenceException | IllegalStateException e) {
			this.transactionRollback();
			return false;
		} finally {
			this.unlock();
		}
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
	public <T> TypedQuery<T> query(String query, Class<T> cl) {
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
	public <T> List<T> queryList(String query, Class<T> cl, Object... params) {
		TypedQuery<T> q = em.createQuery(query, cl);
		for (int i = 0; i < params.length; i++) {
			q = q.setParameter(i+1, params[i]);  // Die Parameter in JPQL beginnnen mit 1 und nicht mit 0...
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
	public <T> List<T> queryNative(String query, Class<T> cl) {
		return em.createNativeQuery(query, cl).getResultList();
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
	public <T> List<T> queryNative(String query, String resultSetMapping) {
		return em.createNativeQuery(query, resultSetMapping).getResultList();
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
	public <T> TypedQuery<T> queryNamed(String name, Class<T> cl) {
		return em.createNamedQuery(name, cl);
	}

	
	/**
	 * Erstellt eine Query für eine Liste aller DTOs anhand des Namens des übergebenen 
	 * Attributes der DTO-Klasse und eines Wertes für dieses Attribut.
	 * 
	 * @param <T>     die Klasse des Ergebnistyps
	 * @param name    der Name der JPQL-Anfrage
	 * @param value   der Wert des Attributes, für welchen die Query ausgeführt wird.
	 * @param cl      das Klassen-Objekt für den Ergebnistyp
	 * 
	 * @return die Anfrage als {@link TypedQuery}
	 */
	public <T> List<T> queryNamed(String name, Object value, Class<T> cl) {
		return em.createNamedQuery(name, cl).setParameter("value", value).getResultList();
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
	public <T> List<T> queryAll(Class<T> cl) {
		var result = queryNamed(cl.getSimpleName() + ".all", cl).getResultList();
		return result;
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
	public <T> T querySingle(Class<T> cl) {
		List<T> entries = queryAll(cl);
		return ((entries == null) || (entries.size() == 0)) ? null : entries.get(0);
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
	public <T> T queryByKey(Class<T> cl, Object... id) {
		TypedQuery<T> q = queryNamed(cl.getSimpleName() + ".primaryKeyQuery", cl);
		for (int i = 0; i < id.length; i++) {
			q = q.setParameter(i+1, id[i]);
		}
		List<T> entries = q.getResultList();
		if (entries.isEmpty())
			return null;
		return entries.get(0);
	}

}
