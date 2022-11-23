package de.nrw.schule.svws.db;

import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.ext.jbcrypt.BCrypt;


/**
 * Diese Klasse repräsentiert u.a. bei einem OpenAPI-Zugriff den angemeldeten Benutzer
 * bei einer SVWS-Datenbank. Für den Datenbankzugriff wird intern eine Instanz der Klasse
 * {@link DBEntityManager} verwendet.
 */
public class Benutzer {

	/** Der Benutzername des angemeldeten Benutzers. */
    private String username;

    /** Das Kennwort des angemeldeten Benutzers */
    private String password;

	/** Enthält bei einem Open-API-Zugriff die Datenbank-ID des zugehörigen SVWS-Benutzers. */
    private Long id = null;

	/** Die verwendete Datenbank-Konfiguration {@link DBConfig} */
	private final DBConfig config;
  
	/** Der Managaer für dei Datenbank-Verbindungen */
	public final ConnectionManager connectionManager;
	
	
    
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
    	this.connectionManager = ConnectionManager.get(config);
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
    	return new Benutzer(config);
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
	 * Gibt eine neue Instanz des {@link DBEntityManager} zurück, die diesem Benutzer 
	 * für den Datenbank-Zugriff zugeordnet ist. Diese repräsentiert gleichzeitig
	 * eine neue Verbindung.
	 * 
	 * @return die Instanz des {@link DBEntityManager}
	 */
	public DBEntityManager getEntityManager() {
		try {
			return new DBEntityManager(this, config);
		} catch(@SuppressWarnings("unused") IllegalStateException e) {
			// TODO error handling
			return null;
		}
	}
	
}
