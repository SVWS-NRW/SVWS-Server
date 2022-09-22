package de.nrw.schule.svws.db;

/**
 * Diese Klasse repräsentiert die Datenbank-Konfiguration für den Zugriff auf eine
 * SVWS-Datenbank. 
 */
public class DBConfig {

	/** Der Datenbank-Treiber, der als Default genommen wird */
	private static final DBDriver DEFAULT_DB_DRIVER = DBDriver.MARIA_DB;
	
	/** Der Standard Ort für die Datenbank, falls kein gültiger angegeben wird */
	private static final String DEFAULT_DB_LOCATION = "localhost";

	
	/** Der Datenbank-Treiber */
	private final DBDriver db_driver;
	
	/** Der Ort, an dem sich die Datenbank befindet */
	private final String db_location;
	
	/** Das Schema in der Datenbank, das verwendet werden soll */
	private final String db_schema;
	
	/** Gibt an, ob der SVWS-Benutzername und das Kennwort auch für die Datenbankverbindung verwendet werden soll */
	private final boolean use_db_login; 
	
	/** Der Benutzername für die Datenbankverbindung */
	private final String username;
	
	/** Das Kennwort für die Datenbankverbindung */
	private final String password;
	
	/** Gibt an, ob der Datenbankzugriff geloggt werden soll */
	private final boolean use_db_logging;
	
	/** Gibt an, ob bei der Verbindung zu der Datenbank automatisch eine neue Datenbankdatei erzeugt werden 
	 * soll, falls zuvor keine Datei vorhanden war */
	private final boolean create_db_file;

	

	/**
	 * Erstellt eine Datenbank-Konfiguration mit den angegebenen Parametern
	 * 
	 * @param db_driver        der Typ des DBMS für den Datenbankzugriff
	 * @param db_location      der Ort, an dem sich die Datenbank befindet
	 * @param db_schema        das Schema in der Datenbank, das verwendet werden soll
	 * @param use_db_login     gibt an, dass der SVWS-Benutzername und das Kennwort auch für die Datenbankverbindung verwendet werden 
	 * @param username         der Benutzername für die Datenbankverbindung
	 * @param password         das Password für die Datenbankverbindung
	 * @param use_db_logging   gibt an, ob der Datenbankzugriff geloggt werden soll
	 * @param create_db_file   gibt an, ob bei der Verbindung zu der Datenbank automatisch eine
	 *                         neue Datenbankdatei erzeugt werden soll, falls zuvor keine Datei
	 *                         vorhanden war
	 */
	public DBConfig(final DBDriver db_driver, final String db_location, final String db_schema, final boolean use_db_login, final String username, final String password,
			final boolean use_db_logging, final boolean create_db_file) {
		this.db_driver = (db_driver == null) ? DEFAULT_DB_DRIVER : db_driver;
		this.db_location = ((db_location == null) || "".equals(db_location.trim())) ? DEFAULT_DB_LOCATION : db_location;
		switch(this.db_driver) {
			case MSSQL:
				this.db_schema = ((db_schema == null) || "".equals(db_schema.trim())) ? this.db_driver.getRootSchema() : db_schema;
				this.create_db_file = false;
				break;
			case MDB:
				this.db_schema = this.db_driver.getRootSchema();
				this.create_db_file = create_db_file;
				break;
			case MARIA_DB:
			case MYSQL:
				this.db_schema = ((db_schema == null) || "".equals(db_schema.trim())) ? this.db_driver.getRootSchema() : db_schema;
				this.create_db_file = false;
				break;
			case SQLITE:
			default:
				this.db_schema = this.db_driver.getRootSchema();
				this.create_db_file = create_db_file;
				break;
		}
		this.use_db_login = use_db_login;
		this.username = username;
		this.password = password;
		this.use_db_logging = use_db_logging;
	}

	
	/**
	 * Gibt den Datenbank-Treiber ({@link DBDriver}) der Konfiguration zurück.
	 * 
	 * @return der Datenbank-Treiber
	 */
	public DBDriver getDBDriver() {
		return db_driver;
	}


	/**
	 * Gibt den Ort zurück, an dem die Datenbank liegt.
	 * 
	 * @return der Ort, an dem die Datenbank liegt
	 */
	public String getDBLocation() {
		return db_location;
	}


	/**
	 * Gibt das Schema in der Datenbank zurück.
	 * 
	 * @return das Schema in der Datenbank
	 */
	public String getDBSchema() {
		return db_schema;
	}


	/**
	 * Gibt zurück, ob der SVWS-Benutzername und das Kennwort auch für die Datenbankverbindung verwendet werden
	 * 
	 * @return true, falls der SVWS-Benutzername und das Kennwort auch für die Datenbankverbindung verwendet werden und ansonsten false
	 */
	public boolean useDBLogin() {
		return use_db_login;
	}


	/**
	 * Gibt den Benutzernamen für den Datenbankzugriff zurück.
	 * 
	 * @return der Benutzername
	 */
	public String getUsername() {
		return username;
	}


	/**
	 * Gibt das Kennwort für den Datenbankzugriff zurück.
	 * 
	 * @return das Kennwort
	 */
	public String getPassword() {
		return password;
	}


	/**
	 * Gibt zurück, ob der Datenbankzugriff geloggt werden soll oder nicht
	 * 
	 * @return true, falls der Datenbankzugriff geloggt werden soll und ansonsten false 
	 */
	public boolean useDBLogging() {
		return use_db_logging;
	}


	/**
	 * Gibt zurück, ob bei der Verbindung zu der Datenbank automatisch eine neue Datenbankdatei 
	 * erzeugt werden soll, falls zuvor keine Datei vorhanden war. Dies ist nur für
	 * Access MDB und SQLite von Bedeutung 
	 * 
	 * @return true, falls automatisch eine neue Datenbankdatei erzeugt werden soll und ansonsten false
	 */
	public boolean createDBFile() {
		return create_db_file;
	}
	
	
	/**
	 * Erstellt eine Kopie von dieser Konfiguration, tauscht dabei aber die Benutzerangaben aus. 
	 * @param username   der neue Benutzername
	 * @param password   das neue Kennwort
	 * @return  eine Kopie der Konfiguration mit den neuen Benutzerinformationen
	 */
	public DBConfig switchUser(String username, String password) {
		return new DBConfig(db_driver, db_location, db_schema, use_db_login, username, password, use_db_logging, create_db_file);
	}
	
	
	/**
	 * Erstellt eine Kopie von dieser Konfiguration, tauscht dabei aber 
	 * den Namen des Schemas aus.
	 *  
	 * @param db_schema    der Name des neu ausgewählten Schemas
	 * 
	 * @return die Kopie der Konfiguration mit dem neu ausgewählten Schema-Namen.
	 */
	public DBConfig switchSchema(String db_schema) {
		return new DBConfig(db_driver, db_location, db_schema, use_db_login, username, password, use_db_logging, create_db_file);
	}
	
}
