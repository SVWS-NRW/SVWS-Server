package de.nrw.schule.svws.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

/** 
 * Beinhaltet die Datenbank-Konfiguration. Für den Zugriff auf mehrere Schemata können 
 * mehrere SchemaKonfigurations-Einträge angelegt werden 
 */
class SVWSKonfigurationDatabaseDTO {

	/** Gibt das verwendete DBMS an (gültige Werte: MARIA_DB, MYSQL oder MSSQL) */
	@JsonProperty("dbms")
	String dbms;
	
	/** Der Ort an dem sich die Datenbank befindet, bestehend aus dem Hostnamen des Servers und dem Port,
	 * wobei der Port weggelassen werden kann, wenn der Standardport des DBMS verwendet wird  (z.B. localhost:3403) */
	@JsonProperty("location")
	String location;
	
	/** Das Default-Schema, welches ausgewählt wird, wenn bei Pfadangaben kein Schema angegeben ist - Default ist das erste Schema */
	@JsonProperty("defaultschema")
	String defaultschema;
	
	/** Eine Liste mit der/den Schema-Konfiguration(-en) */
	@JsonProperty("SchemaKonfiguration")
	@JacksonXmlElementWrapper(useWrapping = false)
	List<SVWSKonfigurationSchemaDTO> schemata;	
	
}
