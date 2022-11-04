package de.nrw.schule.svws.db;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.sessions.server.ConnectionPool;
import org.eclipse.persistence.sessions.server.ServerSession;

import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.ext.jbcrypt.BCrypt;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


/**
 * Diese Klasse repräsentiert u.a. bei einem OpenAPI-Zugriff den angemeldeten Benutzer
 * bei einer SVWS-Datenbank. Für den Datenbankzugriff wird intern eine Instanz der Klasse
 * {@link DBEntityManager} verwendet.
 */
public class Benutzer implements AutoCloseable {

	/** Der Benutzername des angemeldeten Benutzers. */
    private String username;

    /** Das Kennwort des angemeldeten Benutzers */
    private String password;

	/** Enthält bei einem Open-API-Zugriff die Datenbank-ID des zugehörigen SVWS-Benutzers. */
    private Long id = null;

	/** Die verwendete Datenbank-Konfiguration {@link DBConfig} */
	private final DBConfig config;
  
	/** Die zum Erzeugen der {@link EntityManager} verwendete Instanz der {@link EntityManagerFactory} */
	private EntityManagerFactory emf;
	
	
    
    /**
     * Enthält die Information welche Kompetenzen der Benutzer in Bezug auf den Datenbankzugriff hat.
     * Die Kompetenzen erlauben die Zuordnung von OpenAPI-Methode zu einzelnen Kompetenzbereichen, welche 
     * dann einem Datenbankbenutzer administrativ zugewiesen werden. Diese Zuordnung ist in der SVWS-Datenbank
     * gespeichert.
     */
    private List<BenutzerKompetenz> kompetenzen = new Vector<>();
    
    
    
    /**
     * Erzeugt einen neuen Datenbank-Benutzer, wobei die für den Datenbankzugriff zu 
     * verwendende {@link DBConfig} angegeben wird.
     * 
	 * @param config   die Datenbank-Konfiguration
     */
    private Benutzer(DBConfig config) {
    	this.username = "niemand";
    	this.password = "keines";
    	this.config = config;
		this.emf = createEntityManagerFactory();
    }

    
	@Override
	public void close() {
		if (emf != null) {			
			emf.close();
			emf = null;
		}
	}	
    
    
	/**
	 * Erstellt basierend auf der übergebenen Datenbank-Konfiguration {@link DBConfig} 
	 * einen neuen Datenbank-{@link Benutzer}.
	 * 
	 * @param config   die Datenbank-Konfiguration.
	 * 
	 * @return die neue Instanz des {@link Benutzer}
	 */
    public static Benutzer create(DBConfig config) {
    	// Erstelle den DB-Benutzer mithilfe der Konfiguration
    	Benutzer benutzer = new Benutzer(config);
    	// Führe eine Dummy-DB-Abfrage aus, um Probleme mit der Server-seitigen Beendung einer Verbindung zu erkennen
    	DBEntityManager conn = benutzer.getEntityManager();
    	
    	try {
    		conn.executeWithJDBCConnection("SELECT 1");
    	} catch (DatabaseException e) {
            // Bestimme die Anzahl der verfügbaren Verbindungen
            ServerSession serverSession = conn.em.unwrap(ServerSession.class);
            ConnectionPool pool = serverSession.getConnectionPools().get("default");
            if (pool == null) {
                System.err.println("Fehler beim Zugriff auf den DB-Connection-Pool default");
            } else {
            	System.err.println("INFO: Verbindung zur Datenbank unterbrochen - versuche sie neu aufzubauen...");
            	System.err.println("Total number of connections: " + pool.getTotalNumberOfConnections());
            	System.err.println("Available number of connections: " + pool.getConnectionsAvailable().size());
            	pool.resetConnections();
            }
    	}
    	return benutzer;
    }
    
    
	/**
	 * Erstellt eine neue Verbindung mit den gleichen Verbindungsinformationen, aber zu
	 * einem anderen Schema.
	 * Anmerkung: ein identischer Schema-Name wird als Sonderfall auch zugelassen.
	 * 
	 * @param schema   der name des Schemas für die neue Verbindung
	 * 
	 * @return der Benutzer für den Datenbankzugriff, oder null im Fehlerfall
	 */
	public Benutzer connectTo(String schema) {
		return create(config.switchSchema(schema));
	}
    
    
    /**
     * Erstellt zu dem angegeben Kennwort einen Passwort-Hash mit dem
     * BCrypt-Algorithmus. Dabei wird ein zufälliger Wert für den Salt verwendet.
     * 
     * @param password   das Kennwort im Klartext.
     * 
     * @return der Hash des Kennwortes
     */
	public static String erstellePasswortHash(String password) {
		if ((password == null) || ("".equals(password)))
			return null;		
		return BCrypt.hashpw(password, BCrypt.gensalt());		
	}
    
	
	/**
	 * Prüft, ob der Benutzer die übergeben Kompetenz eingeräumt wurde oder nicht.
	 * Hierbei findet keine Prüfung auf einen Admin-Benutzer mit vollen Rechten statt.
	 * 
	 * @param kompetenz   die zu prüfende Kompetenz oder BenutzerKompetenz.KEINE
	 * 
	 * @return true, im Falle von BenutzerKompetenz.KEINE als zu prüfende Kompetenz 
	 *         oder wenn der Benutzer die übergebene Kompetenz besitzt. Anonsten 
	 *         wird false zurückgegeben. 
	 */
    private boolean hatKompetenz(BenutzerKompetenz kompetenz) {
    	if (kompetenz == BenutzerKompetenz.KEINE)
    		return true;
        return (kompetenzen != null) && kompetenzen.contains(kompetenz);
    }

    
    /**
     * Prüft, ob es sich bei dem Benutzer um einen Admin-Benutzer mit 
     * allen Kompetenzen handelt.
     * 
     * @return true, falls es sich um einen Admin-Benutzer mit allen Kompetenzen 
     *         handelt und ansonsten false
     */
    public boolean istAdmin() {
        return (kompetenzen != null) && kompetenzen.contains(BenutzerKompetenz.ADMIN);
    }
    
    
    /**
	 * Prüft, ob der Benutzer die übergeben Kompetenz eingeräumt wurde oder nicht.
	 * Hierbei findet zunächst eine Prüfung auf einen Admin-Benutzer mit vollen Rechten 
	 * statt. Ist dies ein Admin-Benutzer, so hat er die Kompetenz ohne dass weiter 
	 * geprüft werden muss.
	 * 
	 * @param kompetenz   die zu prüfende Kompetenz oder BenutzerKompetenz.KEINE
	 * 
	 * @return true, im Falle von BenutzerKompetenz.KEINE als zu prüfende Kompetenz 
	 *         oder wenn der Benutzer die übergebene Kompetenz besitzt. Anonsten 
	 *         wird false zurückgegeben. 
     */
    public boolean pruefeKompetenz(BenutzerKompetenz kompetenz) {
    	return istAdmin() || hatKompetenz(kompetenz);
    }


	/** 
	 * Gibt im Falle eines Open-API-Zugriffs die Datenbank-ID des angemeldeten 
	 * SVWS-Benutzers zurück.
	 * 
	 * @return die Datenbank-ID des Benutzer oder null
	 */
	public Long getId() {
		return id;
	}


	/**
	 * Setzt die Datenbank-ID des angemeldeten SVWS-Benutzers.
	 * 
	 * @param id   die ID des SVWS-Benutzers
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * Gibt den Benutzernamen zurück.
	 * 
	 * @return der Benutzername
	 */
	public String getUsername() {
		return username;
	}


	/**
	 * Setzt den Benutzernamen diese Benutzers. 
	 * 
	 * @param username   der zu setzende Benutzername
	 */
	public void setUsername(String username) {
		this.username = username;
	}


	/**
	 * Gibt das Kennwort dieses Benutzers zurück.
	 * 
	 * @return das Kennwort
	 */
	public String getPassword() {
		return password;
	}


	/**
	 * Setzt das Kennwort dieses Benutzers.
	 * 
	 * @param password   das zu setzende Kennwort
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * Gibt die Liste der zugeorndeten Komptenzen zurück.
	 * 
	 * @return die Liste der Benutzer-Kompetenzen
	 */
	public List<BenutzerKompetenz> getKompetenzen() {
		return kompetenzen;
	}


	/**
	 * Ordnet die übergebenen Kompetenzen diesem Benutzer zu.
	 * 
	 * @param kompetenzen   die Kompetenzen, die diesem Benutzer zugeordnet werden. 
	 */
	public void setKompetenzen(List<BenutzerKompetenz> kompetenzen) {
		this.kompetenzen = kompetenzen;
	}


	/**
	 * Gibt die Konfiguration für den Datenbank-Zugriff zurück.
	 * 
	 * @return die Konfiguration für den Datenbank-Zugriff
	 */
	public final DBConfig getDBConfig() {
		return this.config;
	}
	
	
	/**
	 * Gibt eine neue Instanz des {@link DBEntityManager} zurück, die diesem Benutzer 
	 * für den Datenbank-Zugriff zugeordnet ist. Diese repräsentiert gleichzeitig
	 * eine neue Verbindung.
	 * 
	 * @return die Instanz des {@link DBEntityManager}
	 */
	public DBEntityManager getEntityManager() {
		try {
			return new DBEntityManager(this, config, emf.createEntityManager());
		} catch(IllegalStateException e) {
			// TODO error handling
			return null;
		}
	}
	
	
	/**
	 * Gibt einen neuen JPA {@link EntityManager} zurück. Diese Methode wird innerhalb dieses
	 * Packages vom DBEntityManager bei der Erneuerung der Verbindung verwendet.
	 *  
	 * @return der neue JPA {@link EntityManager}
	 */
	EntityManager getNewJPAEntityManager() {
		return emf.createEntityManager();
	}

	
	/**
	 * Intern genutzte Methode, um eine {@link EntityManagerFactory} für diesen 
	 * {@link DBEntityManager} zu erstellen. Hierbei werden auch die Standardeinstellungen
	 * für die Datenbankverbindung in Form der Property-Map hinzugefügt
	 * (siehe {@link Persistence#createEntityManagerFactory(String, java.util.Map)})
	 * 
	 * @return die {@link EntityManagerFactory}
	 */
	private EntityManagerFactory createEntityManagerFactory() {
		HashMap<String,Object> propertyMap = new HashMap<>();
		propertyMap.put("jakarta.persistence.jdbc.driver", config.getDBDriver().getJDBCDriver());
		String url = config.getDBDriver().getJDBCUrl(config.getDBLocation(), config.getDBSchema());
		if (config.getDBDriver() == DBDriver.MDB && config.createDBFile())
			url += ";newdatabaseversion=V2000";
		propertyMap.put("jakarta.persistence.jdbc.url", url);
		propertyMap.put("jakarta.persistence.jdbc.user", config.getUsername());
		propertyMap.put("jakarta.persistence.jdbc.password", config.getPassword());
		propertyMap.put("eclipselink.flush", "true");
		propertyMap.put("eclipselink.persistence-context.flush-mode", "commit");
		propertyMap.put("eclipselink.allow-zero-id", "true");
		propertyMap.put("eclipselink.logging.level", config.useDBLogging() ? "WARNING" : "OFF");
//		propertyMap.put("eclipselink.logging.level", config.useDBLogging() ? "INFO" : "OFF");
//		propertyMap.put("eclipselink.logging.level", "ALL");
//		propertyMap.put("eclipselink.profiler","PerformanceProfiler");
		propertyMap.put("eclipselink.cache.shared.default", "false");
		propertyMap.put("eclipselink.exception-handler", "de.nrw.schule.svws.db.DBExceptionHandler");
		if (config.getDBDriver() == DBDriver.SQLITE) {
			propertyMap.put("eclipselink.target-database", "Database");
			// Einstellungen des SQ-Lite-Treibers
			propertyMap.put("open_mode", (config.createDBFile()) ? "70" : "66");   // READWRITE (2) + CREATE (4) + OPEN_URI (64) = 70 bzw. // READWRITE (2) + OPEN_URI (64)  = 66  
			propertyMap.put("foreign_keys", "true");
		}
		return Persistence.createEntityManagerFactory("SVWSDB", propertyMap);
		// TODO avoid Persistence Unit "SVWSDB" as xml file
	}
	
}
