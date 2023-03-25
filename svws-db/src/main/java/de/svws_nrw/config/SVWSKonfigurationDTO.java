package de.svws_nrw.config;

import java.nio.file.Path;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.svws_nrw.db.DBConfig;

/**
 * Diese Klasse repräsentiert die Daten der Konfiguration der SVWS-Server-Umgebung. 
 */
class SVWSKonfigurationDTO {
	
	/** Der Dateiname aus der die Konfiguration gelesen wurde oder null, falls kein Name festgelegt wurde. (wird zum Schreiben verwendet) */
	@JsonIgnore
	Path filepath = null; 
	
	/** Gibt an, ob die Konfigurationsdatei eine XML-Datei ist (Default: false, d.h. Json-Datei) */
	@JsonIgnore
	boolean isXMLConfig = false;
	
	/** Eine Map mit DB-Konfigurationen passend zu den aktuellen Konfigurationseinstellungen */
	@JsonIgnore
	HashMap<String, DBConfig> dbconfigs = new HashMap<>();
		
	
	/** Gibt an, ob die Dateien des Web-Clients auch über eine Authentifizierung an der Datenbank geschützt sind. */
	@JsonProperty("EnableClientProtection")
	Boolean enableClientProtection;
	
	/** Gibt an, ob der Zugriff auf Schema-Operation, die einen root-Zugriff auf die SVWS-DB benötigen deaktiviert werden soll. */
	@JsonProperty("DisableDBRootAccess")
	Boolean disableDBRootAccess;

	/** Gibt an, ob die Auto Updates auf alle Schemata zu Beginn des Server-Starts deaktiviert werden sollen */
	@JsonProperty("DisableAutoUpdates")
	Boolean disableAutoUpdates;

	/** Gibt an, ob beim Server TLS deaktiviert werden soll und HTTP statt HTTPS verwendet werden soll. */
	@JsonProperty("DisableTLS")
	Boolean disableTLS;
	
	/** Gibt den HTTP-Port des Servers an, sofern TLS dekativiert wurde */
	@JsonProperty("PortHTTP")
	Integer portHTTP;
	
	/** Gibt an, dass die HTTP-Version 1.1 statt 2 als Default genutzt wird */
	@JsonProperty("UseHTTPDefaultv11")
	Boolean useHTTPDefaultv11;

	/** Gibt den HTTPS-Port des Servers an */
	@JsonProperty("PortHTTPS")
	Integer portHTTPS;
	
	/** Gibt an, ob bei dem OpenAPI-Server der CORS-Header gesetzt wird. */
	@JsonProperty("UseCORSHeader")
	Boolean useCORSHeader;	

	/** Gibt den Pfad an, der für temporäre Dateien genutzt wird. */
	@JsonProperty("TempPath")
	String tempPath;	
	
	/** Gibt den Alias für den SVWS-Server-Key an. */
	@JsonProperty("TLSKeyAlias")
	String tlsKeyAlias;
	
	/** Gibt den Pfad zum Keystore an, der für die TLS-Verbindung genutzt wird. */
	@JsonProperty("TLSKeystorePath")
	String tlsKeystorePath;
	
	/** Gibt das Kennwort für Keystore an, der für die TLS-Verbindung genutzt wird. */
	@JsonProperty("TLSKeystorePassword")
	String tlsKeystorePassword;	
	
	/** Gibt den Pfad an, wo die Dateien des Web-Clients liegen */
	@JsonProperty("ClientPath")
	String clientPath;
	
	/** Gibt an, ob Logging in Dateien aktiviert werden soll. */
	@JsonProperty("LoggingEnabled")
	Boolean loggingEnabled;
	
	/** Gibt den Pfad an, der für das Logging verwendet werden soll. Ist keiner angegeben, so wird das aktuelle Verzeichnis verwendet. */
	@JsonProperty("LoggingPath")
	String loggingPath;
	
	/** Die Konfiguration des SVWS-Servers für den Datenbankzugriff */
	@JsonProperty("DBKonfiguration")
	SVWSKonfigurationDatabaseDTO dbKonfiguration;	

}
