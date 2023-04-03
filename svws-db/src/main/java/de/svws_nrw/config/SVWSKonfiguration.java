package de.svws_nrw.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import de.svws_nrw.core.data.db.DBSchemaListeEintrag;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;

/**
 * Diese Klasse repräsentiert die Konfiguration der SVWS-Server-Umgebung.
 */
public final class SVWSKonfiguration {

	/** Der Default-Dateiname für die SVWS-Konfiguration als JSON-Datei */
	private static final String DEFAULT_CONFIG_FILENAME = "svwsconfig.json";

	/** Der Default-Dateiname für die SVWS-Konfiguration als XML-Datei */
	private static final String DEFAULT_CONFIG_FILENAME_XML = "svwsconfig.xml";

	/** Die aktuelle Instanz der SVWS-Konfiguration des SVWS-Servers. */
	private static SVWSKonfiguration instanceConfig = new SVWSKonfiguration();


	/** Die aktuelle geladene SVWS-Konfiguration (DTO) des SVWS-Servers. */
	private SVWSKonfigurationDTO dto = null;

	/** Die Menge der gesperrten Schemata */
	private final HashSet<String> schemataLocked = new HashSet<>();



	/**
	 * Erstellt eine neue leere Konfiguration
	 */
	private SVWSKonfiguration() {
	}


	/**
	 * Liefert die Instanz der SVWS-Konfiguration. Wurde noch keine Konfiguration eingelesen,
	 * so wird dies nachgeholt.
	 *
	 * @param path   der Pfad, in welchem die Konfigurationsdatei zu suchen ist.
	 *
	 * @return die SVWS-Konfiguration
	 */
	public static SVWSKonfiguration getFrom(final String path) {
		if (instanceConfig.dto == null)
			readJSON(path, DEFAULT_CONFIG_FILENAME);
		if (instanceConfig.dto == null)
			readXML(path, DEFAULT_CONFIG_FILENAME_XML);
		if (instanceConfig.dto == null) {
			// TODO log error
			System.out.println("Erstelle leere Konfiguration mit Default-Werten...");
			instanceConfig.dto = new SVWSKonfigurationDTO();
			instanceConfig.dto.dbKonfiguration = new SVWSKonfigurationDatabaseDTO();
			instanceConfig.dto.dbKonfiguration.dbms = DBDriver.MARIA_DB.toString();
			instanceConfig.dto.dbKonfiguration.location = "localhost";
		}
		return instanceConfig;
	}


	/**
	 * Liefert die Instanz der SVWS-Konfiguration. Wurde noch keine Konfiguration eingelesen,
	 * so wird dies nachgeholt.
	 *
	 * @return die SVWS-Konfiguration
	 */
	public static SVWSKonfiguration get() {
		if (instanceConfig.dto != null)
			return instanceConfig;
		return getFrom(null);
	}


	/**
	 * Sucht im Dateisystem nach einer Konfigurationsdatei.
	 *
	 * @param path       der Pfad, falls die Konfiguration an einem speziellen Ort zu suchen ist.
	 * @param filename   der Name der Konfigurationsdatei
	 *
	 * @return das zu der gefundenen Datei gehörende Datei-Objekt
	 *
	 * @throws IOException   wird erzeugt, wenn keine Datei mit dem angegebenen Namen gefunden wird
	 */
	private static Path findKonfiguration(final String path, final String filename) throws IOException {
		// Prüfe, ob die Datei an dem speziellen Ort zu finden ist
		if (path != null) {
			final Path p = Paths.get(path + "/" + filename);
			if (Files.exists(p))
				return p;
		}

		// Prüfe, ob die Datei im Ausführungsverzeichnis vorhanden ist
		Path p = Paths.get(System.getProperty("user.dir") + "/" + filename);
		if (Files.exists(p))
			return p;

		// Prüfe, ob die Datei im User-Home-Verzeichnis vorhanden ist
		p = Paths.get(System.getProperty("user.home") + "/" + filename);
		if (Files.exists(p))
			return p;

		// Prüfe, ob die Datei als Java-Resource im Classpath vorhanden ist.
		try {
			final ClassLoader classLoader = SVWSKonfiguration.class.getClassLoader();
			final URL url = classLoader.getResource(filename);
			if (url != null)
				return Paths.get(url.toURI());
	       	throw new IOException("File not found");
		} catch (final URISyntaxException e) {
	       	throw new IOException("File not found", e);
		}
	}


	/**
	 * Liest die SVWS-Konfiguration aus einer XML-Datei
	 *
	 * @param path       der Pfad, falls die Konfiguration an einem speziellen Ort zu suchen ist.
	 * @param filename   der Name der XML-Datei
	 */
	private static void readXML(final String path, final String filename) {
		try {
			final Path p = findKonfiguration(path, filename);
			final XmlMapper mapper = new XmlMapper();
			final String xml = Files.readString(p);

			instanceConfig.dto = mapper.readValue(xml, SVWSKonfigurationDTO.class);
			instanceConfig.dto.isXMLConfig = true;
			instanceConfig.dto.filepath = p;
		    for (int i = 0; i < instanceConfig.getDBAnzahl(); i++) {
		    	final SVWSKonfigurationSchemaDTO schema = instanceConfig.dto.dbKonfiguration.schemata.get(i);
		    	if (schema.svwslogin == null)
			    	schema.svwslogin = false;
		    	instanceConfig.dto.dbconfigs.put(schema.name, instanceConfig.getDBConfig(i));
		    }
		} catch (final JsonMappingException e) {
			// TODO log error into logger
			System.out.println("Fehler in der Konfiguration '" + filename + "': " + e);
		} catch (@SuppressWarnings("unused") final IOException e) {
			// TODO log error into logger
			System.out.println("Konfiguration '" + filename + "' nicht gefunden!");
		}
	}



	/**
	 * Liest die SVWS-Konfiguration aus einer JSON-Datei
	 *
	 * @param path       der Pfad, falls die Konfiguration an einem speziellen Ort zu suchen ist.
	 * @param filename   der Name der JSON-Datei
	 */
	private static void readJSON(final String path, final String filename) {
		try {
			final Path p = findKonfiguration(path, filename);
			final ObjectMapper mapper = new ObjectMapper();
		    final String json = Files.readString(p);

		    instanceConfig.dto = mapper.readValue(json, SVWSKonfigurationDTO.class);
		    instanceConfig.dto.filepath = p;
		    for (int i = 0; i < instanceConfig.getDBAnzahl(); i++) {
		    	final SVWSKonfigurationSchemaDTO schema = instanceConfig.dto.dbKonfiguration.schemata.get(i);
		    	if (schema.svwslogin == null)
			    	schema.svwslogin = false;
		    	instanceConfig.dto.dbconfigs.put(schema.name, instanceConfig.getDBConfig(i));
		    }
		} catch (final JsonMappingException e) {
			// TODO log error into logger
			System.out.println("Fehler in der Konfiguration '" + filename + "': " + e);
		} catch (@SuppressWarnings("unused") final IOException e) {
			// TODO log error into logger
			System.out.println("Konfiguration '" + filename + "' nicht gefunden!");
		}
	}


	/**
	 * Schreibt die SVWS-Konfiguration als JSON in die angegebene Datei. Ist die Datei bereits vorhanden,
	 * so wird sie überschrieben. Ist der Dateiname null oder leer, so wird
	 * der Default-Name DEFAULT_CONFIG_FILENAME verwendet.
	 *
	 * @param filepath   der Pfad der zu schreibenden JSON-Konfigurationsdatei
	 *
	 * @throws IOException   erzeugt eine IOException, wenn die Konfiguration nicht geschrieben werden kann
	 */
	private static void writeJSON(final Path filepath) throws IOException {
		final Path path = ((filepath == null) || ("".equals(filepath.toString()))) ? Paths.get(DEFAULT_CONFIG_FILENAME) : filepath;
		final ObjectMapper mapper = new ObjectMapper();
		final String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(instanceConfig.dto);
		Files.writeString(path, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}



	/**
	 * Schreibt die SVWS-Konfiguration als XML in die angegebene Datei. Ist die Datei bereits vorhanden,
	 * so wird sie überschrieben. Ist der Dateiname null oder leer, so wird
	 * der Default-Name DEFAULT_CONFIG_FILENAME_XML verwendet.
	 *
	 * @param filepath   der Pfad der zu schreibenden XML-Konfigurationsdatei
	 *
	 * @throws IOException   erzeugt eine IOException, wenn die Konfiguration nicht geschrieben werden kann
	 */
	private static void writeXML(final Path filepath) throws IOException {
		final Path path = ((filepath == null) || ("".equals(filepath.toString()))) ? Paths.get(DEFAULT_CONFIG_FILENAME_XML) : filepath;
		final XmlMapper mapper = new XmlMapper();
		final String xml = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(instanceConfig.dto);
		Files.writeString(path, xml, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}



	/**
	 * Schreibt die SVWS-Konfiguration in die Datei, die in der Konfiguration angegeben ist. Ist die Datei
	 * bereits vorhanden, so wird sie überschrieben.
	 * Beim Schreiben wird das JSON-Format gewählt, es sei denn in der Konfiguration
	 * wurde isXMLConfig gesetzt. Dann wird das XML-Format gewählt. Ist der Dateiname null oder leer, so wird
	 * der Default-Name DEFAULT_CONFIG_FILENAME bzw. DEFAULT_CONFIG_FILENAME_XML verwendet.
	 *
	 * @return true, falls die Konfiguration erfolgreich geschrieben wurde, sonst false.
	 */
	public static boolean write() {
		try {
			if (instanceConfig.dto == null)
				return false;
			if (instanceConfig.dto.isXMLConfig) {
				writeXML(instanceConfig.dto.filepath);
			} else {
				writeJSON(instanceConfig.dto.filepath);
			}
			return true;
		} catch (@SuppressWarnings("unused") final IOException e) {
			return false;
		}
	}



	/**
	 * Erzeugt eine neue SVWS-Konfiguration mit Defaultwerten und ggf. angepassten Werten, falls diese nicht null sind.
	 *
	 * @param clientPath         der Pfad, in dem die Web-Client-Dateien zu finden sind
	 * @param loggingPath        der Pfad, in welchem Logging-Dateien des Servers erzeugt werden. Null um Logging zu deaktivieren
	 * @param tempPath           der Pfad, in welchem tmeporäre Dateien des Servers angelegt werden.
	 * @param keystorePath       der Pfad, in welchem der Keystore für die TLS-Verbindung gesucht wird
	 * @param keystorePassword   das Kennwort, welches für den Keystore für die TLS-Verbindung genutzt wird
	 * @param dbms               das DBMS für die Datenbankverbindung
	 * @param dbLocation         der Name des Hosts, auf welchem der Datenbank-Server läuft
	 * @param dbPort             der Port des Datenbank-Servers
	 * @param noSchema           gibt an, falls keine Schema-Information in der Konfiguration angelegt werden soll
	 * @param schema             das Schema, welches als Default genutzt werden soll
	 * @param schemaUser         der Benutzername für das Schema
	 * @param schemaPassword     das zum Benutzernamen gehörige Kennwort
	 *
	 * @return die neue SVWS-Konfiguration oder null im Fehlerfall
	 */
	public static SVWSKonfiguration getDefault(final String clientPath, final String loggingPath, final String tempPath, final String keystorePath, final String keystorePassword, final DBDriver dbms,
			final String dbLocation, final int dbPort, final boolean noSchema, final String schema, final String schemaUser, final String schemaPassword) {
		instanceConfig = new SVWSKonfiguration();
		instanceConfig.dto = new SVWSKonfigurationDTO();
		instanceConfig.dto.useCORSHeader = default_use_cors_header;
		instanceConfig.dto.clientPath = ((clientPath == null) || "".equals(clientPath)) ? default_webclient_path : clientPath;
		instanceConfig.dto.loggingEnabled = (loggingPath != null);
		instanceConfig.dto.loggingPath = ((loggingPath == null) || "".equals(loggingPath)) ? "logs" : loggingPath;
		instanceConfig.dto.tempPath = ((tempPath == null) || "".equals(tempPath)) ? "tmp" : tempPath;
		instanceConfig.dto.tlsKeystorePath = ((keystorePath == null) || "".equals(keystorePath)) ? "" : keystorePath;
		instanceConfig.dto.tlsKeystorePassword = ((keystorePassword == null) || "".equals(keystorePassword)) ? "" : keystorePassword;
		instanceConfig.dto.dbKonfiguration = new SVWSKonfigurationDatabaseDTO();
		instanceConfig.dto.dbKonfiguration.dbms = (dbms == null) ? DBDriver.MARIA_DB.toString() : dbms.toString();
		instanceConfig.dto.dbKonfiguration.location = dbLocation + ":" + dbPort;
		instanceConfig.dto.dbKonfiguration.schemata = new Vector<>();
		if (!noSchema) {
			instanceConfig.dto.dbKonfiguration.defaultschema = ((schema == null) || ("".equals(schema))) ? "svwsschema" : schema;
			final SVWSKonfigurationSchemaDTO schemaConfig = new SVWSKonfigurationSchemaDTO();
			schemaConfig.name = instanceConfig.dto.dbKonfiguration.defaultschema;
			schemaConfig.svwslogin = false;
			schemaConfig.username = ((schemaUser == null) || "".equals(schemaUser)) ? "svwsadmin" : schemaUser;
			schemaConfig.password = ((schemaPassword == null) || "".equals(schemaPassword)) ? "svwsadmin" : schemaPassword;
			instanceConfig.dto.dbKonfiguration.schemata.add(schemaConfig);
			instanceConfig.dto.dbconfigs.put(instanceConfig.dto.dbKonfiguration.defaultschema, instanceConfig.getDBConfig(0));
		}
		return instanceConfig;
	}



	/**
	 * Gibt den zu dieser Konfiguration gehörenden Dateinamen zurück.
	 *
	 * @return der Dateiname
	 */
	public String getFilename() {
		return ((dto == null) || (dto.filepath == null) || ("".equals(dto.filepath.toString())))
			? (dto == null || !dto.isXMLConfig ? DEFAULT_CONFIG_FILENAME : DEFAULT_CONFIG_FILENAME_XML)
			: dto.filepath.toString();
	}


	/**
	 * Setzt den zu dieser Konfiguration gehörenden Dateinamen.
	 *
	 * @param filename    der Dateiname
	 */
	public void setFilename(final String filename) {
		this.dto.filepath = ((dto == null) || ("".equals(filename))) ? null : Paths.get(filename);
	}


	/** Ein Default-Pfad, in welchem die Client-Dateien gesucht werden, falls in der Konfigurationsdatei keiner angegeben ist. */
	public static String default_webclient_path = "webclient";

	/**
	 * Gibt den Pfad zu den Webclient-Dateien zurück.
	 *
	 * @return der Pfad zu den Webclient-Dateien zurück.
	 */
	public String getClientPath() {
		return ((dto == null) || (dto.clientPath == null) || ("".equals(dto.clientPath))) ? default_webclient_path : dto.clientPath;
	}


	private static boolean default_enableClientProtection = false;

	/**
	 * Gibt an, ob der Zugriff auf Dateien des SVWS-Clients auch über die Authentifikation des Servers geschützt werden
	 * oder nicht.
	 *
	 * @return true, falls die Dateien des SVWS-Clients geschützt werden, sonst false
	 */
	public boolean isEnableClientProtection() {
		return (dto == null) || (this.dto.enableClientProtection == null) ? default_enableClientProtection : this.dto.enableClientProtection;
	}



	private static boolean default_disableDBRootAccess = false;

	/**
	 * Gibt an, ob der Zugriff auf Schema-Operation, die einen root-Zugriff auf die SVWS benötigen deaktiviert werden soll.
	 *
	 * @return true, falls der Zugriff deaktiviert wurde, sonst false
	 */
	public boolean isDBRootAccessDisabled() {
		return (dto == null) || (this.dto.disableDBRootAccess == null) ? default_disableDBRootAccess : this.dto.disableDBRootAccess;
	}



	private static boolean default_disableAutoUpdates = false;

	/**
	 * Gibt an, ob die Auto Updates auf alle Schemata zu Beginn des Server-Starts deaktiviert
	 * werden sollen.
	 *
	 * @return true, falls die Updates deaktiviert wurden, sonst false (default)
	 */
	public boolean isAutoUpdatesDisabled() {
		return (dto == null) || (this.dto.disableAutoUpdates == null) ? default_disableAutoUpdates : this.dto.disableAutoUpdates;
	}



	private static boolean default_disableTLS = false;

	/**
	 * Gibt an, ob beim Server TLS deaktiviert werden soll und HTTP statt HTTPS verwendet
	 * werden soll.
	 *
	 * @return true, falls TLS deaktiviert wurde, sonst false (default)
	 */
	public boolean isTLSDisabled() {
		return (dto == null) || (this.dto.disableTLS == null) ? default_disableTLS : this.dto.disableTLS;
	}



	/** Gibt den Default HTTP-Port des Servers an, sofern TLS deaktiviert wurde */
	public static int default_PortHTTP = 8080;

	/**
	 * Gibt den HTTP-Port des Servers zurück, sofern TLS deaktiviert wurde
	 *
	 * @return der HTTP-Port des Servers, sofern TLS deaktiviert wurde
	 */
	public int getPortHTTP() {
		return (dto == null) || (this.dto.portHTTP == null) ? default_PortHTTP : this.dto.portHTTP;
	}


	/** Gibt an, dass die HTTP-Version 1.1 statt 2 als Default genutzt wird */
	public static boolean default_useHTTPDefaultv11 = false;

	/**
	 * Gibt zurück, ob HTTP-Version 1.1 statt 2 als Default genutzt wird.
	 *
	 * @return true, falls HTTP-Version 1.1 als Deafult genutzt werden soll, sonst false
	 */
	public boolean useHTTPDefaultv11() {
		return (dto == null) || (this.dto.useHTTPDefaultv11 == null) ? default_useHTTPDefaultv11 : this.dto.useHTTPDefaultv11;
	}


	/** Gibt den Default HTTPS-Port des Servers an */
	public static int default_PortHTTPS = 443;

	/**
	 * Gibt den HTTPS-Port des Servers zurück
	 *
	 * @return der HTTPS-Port des Servers
	 */
	public int getPortHTTPS() {
		return (dto == null) || (this.dto.portHTTPS == null) ? default_PortHTTPS : this.dto.portHTTPS;
	}


	/** Gibt an, das CORS-Header als Default verwendet werden, wenn nichts dazu in der Konfiguration angegeben ist. */
	public static boolean default_use_cors_header = true;

	/**
	 * Gibt an, ob CORS-Header verwendet werden sollen oder nicht.
	 *
	 * @return true, falls CORS-Header verwendet werden sollen, ansonsten false
	 */
	public boolean useCORSHeader() {
		return (dto == null) || (this.dto.useCORSHeader == null) ? default_use_cors_header : this.dto.useCORSHeader;
	}


	/**
	 * Gibt die Anzahl der Datenbank-Konfigurationen zurück.
	 *
	 * @return die Anzahl der Datenbank-Konfigurationen
	 */
	public int getDBAnzahl() {
		if (dto == null)
			return 0;
		return dto.dbKonfiguration.schemata.size();
	}

	/**
	 * Gibt die i-te Datenbank-Konfiguration zurück.
	 *
	 * @param i   die Nummer der Datenbank-Konfiguration
	 *
	 * @return die Datenbank-Konfiguration
	 */
	private DBConfig getDBConfig(final int i) {
		if ((dto == null) || (i < 0) || (i >= getDBAnzahl()))
			return null;
		final DBDriver driver = DBDriver.valueOf(dto.dbKonfiguration.dbms);
		final SVWSKonfigurationSchemaDTO schema = dto.dbKonfiguration.schemata.get(i);
		return new DBConfig(driver, dto.dbKonfiguration.location, schema.name, schema.svwslogin, schema.username, schema.password, true, false);
	}

	/**
	 * Gibt die Datenbank-Konfiguration zu dem Schemanamen zurück.
	 *
	 * @param schema   der Name des gesuchten Schemas
	 *
	 * @return die Datenbank-Konfiguration
	 */
	public DBConfig getDBConfig(final String schema) {
		if (dto == null)
			return null;
		return dto.dbconfigs.get(schema);
	}

	/**
	 * Gibt die Datenbank-Konfiguration für Zugriff auf das root-Schema der Datenbank zurück.
	 *
	 * @param username   der Benutzername für den Zugriff
	 * @param password   das Passwort für den Zugriff
	 *
	 * @return die Datenbank-Konfiguration für den Zugriff
	 */
	public DBConfig getRootDBConfig(final String username, final String password) {
		if (dto == null)
			return null;
		final DBDriver driver = DBDriver.valueOf(dto.dbKonfiguration.dbms);
		return new DBConfig(driver, dto.dbKonfiguration.location, driver.getRootSchema(), true, username, password, true, false);
	}


	/**
	 * Gibt den Namen des konfigurierten Default-Schemas zurück.
	 *
	 * @return der Name des konfigurierten Default-Schemas
	 */
	public String getDefaultSchema() {
		if ((dto == null) || (dto.dbKonfiguration.schemata.size() == 0))
			return null;
		if ((dto.dbKonfiguration.defaultschema == null) || ("".equals(dto.dbKonfiguration.defaultschema)))
			return dto.dbKonfiguration.schemata.get(0).name;
		final DBConfig schema = dto.dbconfigs.get(dto.dbKonfiguration.defaultschema);
		if (schema != null)
			return schema.getDBSchema();
		return dto.dbKonfiguration.schemata.get(0).name;
	}


	/**
	 * Prüft, ob für den angebenen Schema-Namen eine Konfiguration vorliegt oder nicht.
	 *
	 * @param schemaName   der Name des Datenbank-Schemas
	 *
	 * @return true, falls eine Konfiguratio für das Schema vorliegt, ansonsten false
	 */
	public boolean hasSchema(final String schemaName) {
		if ((dto == null) || (schemaName == null) || "".equals(schemaName))
			return false;
		return dto.dbconfigs.get(schemaName) != null;
	}


	/**
	 * Sperrt das angegebene Schema
	 *
	 * @param schemaName   der Name des zu sperrenden Schemas
	 *
	 * @return true, fall das Schema gesperrt werden konnte
	 */
	public synchronized boolean lockSchema(final String schemaName) {
		if (schemataLocked.contains(schemaName))
			return false;
		schemataLocked.add(schemaName);
		return true;
	}


	/**
	 * Entsperrt das angegebene Schema
	 *
	 * @param schemaName   der Name des zu entsperrenden Schemas
	 *
	 * @return true, fall das Schema entsperrt werden konnte
	 */
	public synchronized boolean unlockSchema(final String schemaName) {
		if (schemataLocked.contains(schemaName)) {
			schemataLocked.remove(schemaName);
			return true;
		}
		return false;
	}


	/**
	 * Gibt an, ob das angegebene Schema gesperrt ist oder nicht
	 *
	 * @param schemaName   der Name des Schema
	 *
	 * @return true, falls das Schema gesperrt ist
	 */
	public synchronized boolean isLockedSchema(final String schemaName) {
		return (schemataLocked.contains(schemaName));
	}


	/**
	 * Liefert eine Liste mit allen konfigurierten Schemanamen zurück.
	 *
	 * @return eine Liste mit allen konfigurierten Schemanamen
	 */
	public List<DBSchemaListeEintrag> getSchemaList() {
		if (dto == null)
			return Collections.emptyList();
    	final String defaultSchema = this.getDefaultSchema();
    	return dto.dbconfigs.values().stream()
    			.map(c -> c.getDBSchema())
    			.filter(s -> s != null).sorted().map(s -> {
    				final DBSchemaListeEintrag result = new DBSchemaListeEintrag();
    				result.name = s;
    				result.isDefault = (defaultSchema != null) && (defaultSchema.equals(s));
    				return result;
    			}).collect(Collectors.toList());

	}


	/**
	 * Gibt die Schema-Konfiguration mit dem angebenen Namen zurück.
	 *
	 * @param schemaName   der Name des Schemas
	 *
	 * @return die Schema-Konfiguration, falls eines vorhanden ist, ansonsten null
	 */
	private SVWSKonfigurationSchemaDTO getSchemaKonfiguration(final String schemaName) {
		if ((dto == null) || (schemaName == null) || ("".equals(schemaName)))
			return null;
		for (final SVWSKonfigurationSchemaDTO config : dto.dbKonfiguration.schemata) {
			if (schemaName.equals(config.name))
				return config;
		}
		return null;
	}



	/**
	 * Erstellt oder Aktualisiert die Schema-Konfiguration des angegebenen Schemas
	 *
	 * @param schemaName      der Name des Datenbank-Schemas
	 * @param userName        der Name des Schema-Admin-Benutzers
	 * @param userPassword    das Kennwort des Schema-Admin-Benutzers
	 * @param userSVWSLogin   true, false die Authentifizierung über die DB erfolgt, sonst false
	 *
	 * @return true im Erfolgsfalls, sonst false
	 */
	public boolean createOrUpdateSchema(final String schemaName, final String userName, final String userPassword, final boolean userSVWSLogin) {
		if ((dto == null) || (schemaName == null) || ("".equals(schemaName)) || (userName == null) || ("".equals(userName)))
			return false;
		String password = userPassword;
		if (password == null)
			password = "";
		SVWSKonfigurationSchemaDTO config = getSchemaKonfiguration(schemaName);
		if (config == null) {
			// Neue Config erstellen
			config = new SVWSKonfigurationSchemaDTO();
			config.name = schemaName;
			config.username = userName;
			config.password = password;
			config.svwslogin = userSVWSLogin;
			dto.dbKonfiguration.schemata.add(config);
			dto.dbconfigs.put(schemaName, this.getDBConfig(dto.dbKonfiguration.schemata.size() - 1));
			// Prüfe, ob dies das erste Schema ist -> setzen als Default-Schema
			if (dto.dbKonfiguration.schemata.size() == 1)
				dto.dbKonfiguration.defaultschema = schemaName;
		} else {
			// Vorhandene Config aktualisieren
			config.name = schemaName;
			config.username = userName;
			config.password = password;
			config.svwslogin = userSVWSLogin;
		}
		return write();
	}



	/**
	 * Entfernt das Schema aus der Konfiguration, sofern es enthalten ist.
	 *
	 * @param schemaName   der Name des Datenbank-Schemas, welches aus der
	 *                     Konfiguration entfernt werden soll
	 *
	 * @return true, falls die Konfiguration erfolgreich entfernt wurde oder nicht vorhanden ist, ansonsten false
	 */
	public boolean removeSchema(final String schemaName) {
		if ((dto == null) || (schemaName == null) || ("".equals(schemaName)))
			return false;
		if (!dto.dbconfigs.containsKey(schemaName))
			return true;
		// Entferne das schema aus der Konfiguration
		dto.dbconfigs.remove(schemaName);
		for (final SVWSKonfigurationSchemaDTO config : dto.dbKonfiguration.schemata) {
			if (schemaName.equals(config.name)) {
				dto.dbKonfiguration.schemata.remove(config);
				break;
			}
		}
		// Persistiere die Änderungen in der Konfigurationsdatei
		return write();
	}



	/**
	 * Gibt zurück, ob das Logging in Dateien aktiviert ist oder nicht.
	 *
	 * @return true, falls dass Logging aktiviert ist, sonst false
	 */
	public boolean isLoggingEnabled() {
		return (dto == null) || (dto.loggingEnabled == null) ? false : dto.loggingEnabled;
	}


	/**
	 * Gibt den Basis-Pfad an, in den Log-Dateien geschrieben werden sollen.
	 *
	 * @return der Basis-Pfad für das Logging
	 */
	public String getLoggingPath() {
		return (dto == null) || (dto.loggingPath == null) ? "." : dto.loggingPath;
	}


	/**
	 * Gibt den Basis-Pfad an, in den temporäre Dateien geschrieben werden sollen.
	 *
	 * @return der Basis-Pfad für temporäre Dateien
	 */
	public String getTempPath() {
		return (dto == null) || (dto.tempPath == null) ? "./tmp" : dto.tempPath;
	}


	/** Ein Default-Alias, welcher für den SVWS-Server-Key im keystore genutzt wird. */
	public static String default_tls_key_alias = "selfsigned";

	/**
	 * Gibt den Alias des SVWS-Server-Keys zurück.
	 *
	 * @return der Alias des SVWS-Server-Keys
	 */
	public String getTLSKeyAlias() {
		return (dto == null) || (dto.tlsKeyAlias == null) ? default_tls_key_alias : dto.tlsKeyAlias;
	}


	/**
	 * Gibt den Pfad zum Keystore an, der für die TLS-Verbindung des Servers genutzt wird.
	 *
	 * @return der Pfad zum Keystore
	 */
	public String getTLSKeystorePath() {
		return (dto == null) || (dto.tlsKeystorePath == null) ? "." : dto.tlsKeystorePath;
	}



	/** Ein Default-Kennwort, welches für den keystore genutzt wird, falls in der Konfigurationsdatei keines angegeben ist. */
	public static String default_tls_keystore_password = "svwskeystore";

	/**
	 * Gibt das Kennwort für Keystore an, der für die TLS-Verbindung genutzt wird.
	 *
	 * @return das Kennwort
	 */
	public String getTLSKeystorePassword() {
		return (dto == null) || (dto.tlsKeystorePassword == null) ? default_tls_keystore_password : dto.tlsKeystorePassword;
	}


	/**
	 * Diese Methode liefert den aktuell konfigurierten Java-Key-Store zurück.
	 *
	 * @return der Java-Key-Store
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn der Zugriff auf den
	 *                             Keystore nicht möglich ist.
	 */
	public static KeyStore getKeystore() throws KeyStoreException {
		try {
	    	final SVWSKonfiguration config = SVWSKonfiguration.get();
	    	try (FileInputStream is = new FileInputStream(config.getTLSKeystorePath() + "/keystore")) {
				final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
				keystore.load(is, config.getTLSKeystorePassword().toCharArray());
				return keystore;
	    	}
		} catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
			throw new KeyStoreException("Zugriff azuf Keystore fehlgeschlagen.", e);
		}
	}


	/**
	 * Diese Methode liefert den Private Key aus dem Keystore, sofern dieser aus der Konfiguration
	 * geladen werden kann.
	 *
	 * @return der Private-Key
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn der Zugriff auf den
	 *                             Keystore oder den Private Key nicht möglich ist.
	 */
	public static PrivateKey getPrivateKey() throws KeyStoreException {
        try {
	    	final SVWSKonfiguration config = SVWSKonfiguration.get();
			final KeyStore keystore = getKeystore();
			final Key key = keystore.getKey(config.getTLSKeyAlias(), config.getTLSKeystorePassword().toCharArray());
			if (key instanceof PrivateKey)
				return (PrivateKey) key;
			throw new KeyStoreException("Konnte keinen privaten Schlüssel auslesen.");
		} catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException e) {
			throw new KeyStoreException("", e);
		}
	}


	/**
	 * Liefert das Zertifikat des SVWS-Servers, sofern dieses aus der Konfiguration
	 * geladen werden kann.
	 *
	 * @return das Zertifikat des SVWS-Servers.
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn der Zugriff auf den
	 *                             Keystore oder das Zertifikat nicht möglich ist.
	 */
	public static Certificate getCertificate() throws KeyStoreException {
        try {
	    	final SVWSKonfiguration config = SVWSKonfiguration.get();
			final KeyStore keystore = getKeystore();
			return keystore.getCertificate(config.getTLSKeyAlias());
		} catch (final KeyStoreException e) {
			throw new KeyStoreException("", e);
		}
	}


	/**
	 * Liefert das Zertifikat des SVWS-Servers in Base64-Kodierung, sofern dieses
	 * aus der Konfiguration geladen werden kann.
	 *
	 * @return das Zertifikat des SVWS-Servers.
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn der Zugriff auf den
	 *                             Keystore oder das Zertifikat nicht möglich ist.
	 */
	public static String getCertificateBase64() throws KeyStoreException {
        try {
	    	final SVWSKonfiguration config = SVWSKonfiguration.get();
			final KeyStore keystore = getKeystore();
			final Certificate cert = keystore.getCertificate(config.getTLSKeyAlias());
			return Base64.getMimeEncoder().encodeToString(cert.getEncoded());
		} catch (KeyStoreException | CertificateEncodingException e) {
			throw new KeyStoreException("", e);
		}
	}


	/**
	 * Liefert den öffentlichen Schlüssel des SVWS-Servers, sofern dieser aus der Konfiguration
	 * geladen werden kann.
	 *
	 * @return der öffentliche Schlüssel des SVWS-Servers.
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn der Zugriff auf den
	 *                             Keystore oder den öffentlichen Schlüssel nicht möglich ist.
	 */
	public static PublicKey getPublicKey() throws KeyStoreException {
		return getCertificate().getPublicKey();
	}


	/**
	 * Liefert den öffentlichen Schlüssel des SVWS-Servers in Base64-Kodierung, sofern dieser
	 * aus der Konfiguration geladen werden kann.
	 *
	 * @return der öffentliche Schlüssel des SVWS-Servers oder null im Fehler
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn der Zugriff auf den
	 *                             Keystore oder den öffentlichen Schlüssel nicht möglich ist.
	 */
	public static String getPublicKeyBase64() throws KeyStoreException {
    	final PublicKey pubKey = SVWSKonfiguration.getPublicKey();
		return Base64.getMimeEncoder().encodeToString(pubKey.getEncoded());
	}

}
