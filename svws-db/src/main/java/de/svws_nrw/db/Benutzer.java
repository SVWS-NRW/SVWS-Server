package de.svws_nrw.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.ext.jbcrypt.BCrypt;


/**
 * Diese Klasse repräsentiert u.a. bei einem OpenAPI-Zugriff den angemeldeten Benutzer
 * bei einer SVWS-Datenbank. Für den Datenbankzugriff wird intern eine Instanz der Klasse
 * {@link DBEntityManager} verwendet.
 */
public final class Benutzer {

	/** Der Benutzername des angemeldeten Benutzers. */
    private String _username;

    /** Das Kennwort des angemeldeten Benutzers */
    private String _password;

	/** Enthält bei einem Open-API-Zugriff die Datenbank-ID des zugehörigen SVWS-Benutzers. */
    private Long _id = null;

	/** Die verwendete Datenbank-Konfiguration {@link DBConfig} */
	private final DBConfig _config;

	/** Der Manager für die Datenbank-Verbindungen */
	public final ConnectionManager connectionManager;

	/** Enthält bei einem Open-API-Zugriff die Datenbank-ID des zugehörigen Lehrers, falls der Benutzer ein Lehrer ist*/
	private Long _idLehrer = null;


    /**
     * Enthält die Information welche Kompetenzen der Benutzer in Bezug auf den Datenbankzugriff hat.
     * Die Kompetenzen erlauben die Zuordnung von OpenAPI-Methode zu einzelnen Kompetenzbereichen, welche
     * dann einem Datenbankbenutzer administrativ zugewiesen werden. Diese Zuordnung ist in der SVWS-Datenbank
     * gespeichert.
     */
    private List<BenutzerKompetenz> _kompetenzen = new ArrayList<>();



    /**
     * Erzeugt einen neuen Datenbank-Benutzer, wobei die für den Datenbankzugriff zu
     * verwendende {@link DBConfig} angegeben wird.
     *
	 * @param config   die Datenbank-Konfiguration
	 *
     * @throws DBException wenn die Authentifizierung fehlschlägt
     */
    private Benutzer(final DBConfig config) throws DBException {
    	this._username = "niemand";
    	this._password = "keines";
    	this._config = config;
    	this.connectionManager = ConnectionManager.get(config);
    }


	/**
	 * Erstellt basierend auf der übergebenen Datenbank-Konfiguration {@link DBConfig}
	 * einen neuen Datenbank-{@link Benutzer}.
	 *
	 * @param config   die Datenbank-Konfiguration.
	 *
	 * @return die neue Instanz des {@link Benutzer}
	 *
	 * @throws DBException wenn die Authentifizierung fehlschlägt
	 */
    public static Benutzer create(final DBConfig config) throws DBException {
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
	 *
	 * @throws DBException wenn die Authentifizierung fehlschlägt
	 */
	public Benutzer connectTo(final String schema) throws DBException {
		return create(_config.switchSchema(schema));
	}


    /**
     * Erstellt zu dem angegeben Kennwort einen Passwort-Hash mit dem
     * BCrypt-Algorithmus. Dabei wird ein zufälliger Wert für den Salt verwendet.
     *
     * @param password   das Kennwort im Klartext.
     *
     * @return der Hash des Kennwortes
     */
	public static String erstellePasswortHash(final String password) {
		if ((password == null) || ("".equals(password)))
			return null;
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}


	/**
	 * Prüft, ob dem Benutzer eine der übergeben Kompetenzen eingeräumt wurde oder nicht.
	 * Hierbei findet keine Prüfung auf einen Admin-Benutzer mit vollen Rechten statt.
	 *
	 * @param kompetenzen   die zu prüfenden Kompetenzen oder BenutzerKompetenz.KEINE
	 *
	 * @return true, im Falle von BenutzerKompetenz.KEINE als zu prüfende Kompetenz
	 *         oder wenn der Benutzer einer der übergebenen Kompetenzen besitzt. Anonsten
	 *         wird false zurückgegeben.
	 */
    private boolean hatKompetenz(final Set<BenutzerKompetenz> kompetenzen) {
    	if (kompetenzen == null)
    		return false;
    	if (kompetenzen.contains(BenutzerKompetenz.KEINE))
    		return true;
    	for (final BenutzerKompetenz kompetenz : kompetenzen)
    		if (this._kompetenzen.contains(kompetenz))
    			return true;
        return false;
    }


    /**
     * Prüft, ob es sich bei dem Benutzer um einen Admin-Benutzer mit
     * allen Kompetenzen handelt.
     *
     * @return true, falls es sich um einen Admin-Benutzer mit allen Kompetenzen
     *         handelt und ansonsten false
     */
    public boolean istAdmin() {
        return (_kompetenzen != null) && _kompetenzen.contains(BenutzerKompetenz.ADMIN);
    }


    /**
	 * Prüft, ob der Benutzer eine der übergeben Kompetenz eingeräumt wurde oder nicht.
	 * Hierbei findet zunächst eine Prüfung auf einen Admin-Benutzer mit vollen Rechten
	 * statt. Ist dies ein Admin-Benutzer, so hat er die Kompetenz ohne dass weiter
	 * geprüft werden muss.
	 *
	 * @param kompetenzen   die zu prüfenden Kompetenzen oder BenutzerKompetenz.KEINE
	 *
	 * @return true, im Falle von BenutzerKompetenz.KEINE als zu prüfende Kompetenz
	 *         oder wenn der Benutzer die übergebene Kompetenz besitzt. Anonsten
	 *         wird false zurückgegeben.
     */
    public boolean pruefeKompetenz(final BenutzerKompetenz... kompetenzen) {
    	final Set<BenutzerKompetenz> tmp = new HashSet<>(Arrays.asList(kompetenzen));
    	return istAdmin() || hatKompetenz(tmp);
    }


    /**
	 * Prüft, ob der Benutzer eine der übergeben Kompetenz eingeräumt wurde oder nicht.
	 * Hierbei findet zunächst eine Prüfung auf einen Admin-Benutzer mit vollen Rechten
	 * statt. Ist dies ein Admin-Benutzer, so hat er die Kompetenz ohne dass weiter
	 * geprüft werden muss.
	 *
	 * @param kompetenzen   die zu prüfenden Kompetenzen oder BenutzerKompetenz.KEINE
	 *
	 * @return true, im Falle von BenutzerKompetenz.KEINE als zu prüfende Kompetenz
	 *         oder wenn der Benutzer die übergebene Kompetenz besitzt. Anonsten
	 *         wird false zurückgegeben.
     */
    public boolean pruefeKompetenz(final Set<BenutzerKompetenz> kompetenzen) {
    	return istAdmin() || hatKompetenz(kompetenzen);
    }


	/**
	 * Gibt im Falle eines Open-API-Zugriffs die Datenbank-ID des angemeldeten
	 * SVWS-Benutzers zurück.
	 *
	 * @return die Datenbank-ID des Benutzer oder null
	 */
	public Long getId() {
		return _id;
	}


	/**
	 * Setzt die Datenbank-ID des angemeldeten SVWS-Benutzers.
	 *
	 * @param id   die ID des SVWS-Benutzers
	 */
	public void setId(final Long id) {
		this._id = id;
	}


	/**
	 * Gibt den Benutzernamen zurück.
	 *
	 * @return der Benutzername
	 */
	public String getUsername() {
		return _username;
	}


	/**
	 * Setzt den Benutzernamen diese Benutzers.
	 *
	 * @param username   der zu setzende Benutzername
	 */
	public void setUsername(final String username) {
		this._username = username;
	}


	/**
	 * Gibt das Kennwort dieses Benutzers zurück.
	 *
	 * @return das Kennwort
	 */
	public String getPassword() {
		return _password;
	}


	/**
	 * Setzt das Kennwort dieses Benutzers.
	 *
	 * @param password   das zu setzende Kennwort
	 */
	public void setPassword(final String password) {
		this._password = password;
	}


	/**
	 * Gibt die Liste der zugeorndeten Komptenzen zurück.
	 *
	 * @return die Liste der Benutzer-Kompetenzen
	 */
	public List<BenutzerKompetenz> getKompetenzen() {
		return _kompetenzen;
	}


	/**
	 * Ordnet die übergebenen Kompetenzen diesem Benutzer zu.
	 *
	 * @param kompetenzen   die Kompetenzen, die diesem Benutzer zugeordnet werden.
	 */
	public void setKompetenzen(final List<BenutzerKompetenz> kompetenzen) {
		this._kompetenzen = kompetenzen;
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
			return new DBEntityManager(this, _config);
		} catch (@SuppressWarnings("unused") final IllegalStateException e) {
			// TODO error handling
			return null;
		}
	}


	/**
	 * Gibt die Lehrer-ID des angemeldeten Benutzers zurück, sofern
	 * ein Lehrer angemeldet ist.
	 *
	 * @return die Lehrer-ID oder null, falls der angemeldete Benutzer kein Lehrer ist
	 */
	public Long getIdLehrer() {
		return _idLehrer;
	}


	/**
	 * Setzt die Lehrer-ID des angemeldeten Benutzers
	 *
	 * @param idLehrer   die Lehrer-ID oder null, falls der angemeldete Benutzer kein Lehrer ist
	 */
	public void setIdLehrer(final Long idLehrer) {
		this._idLehrer = idLehrer;
	}

}
