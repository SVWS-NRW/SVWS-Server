package de.nrw.schule.svws.db.schema.csv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.nrw.schule.svws.csv.converter.Boolean01ConverterDeserializer;
import de.nrw.schule.svws.csv.converter.Boolean01ConverterSerializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Datenbanksysteme.
 */
public class Datenbanksysteme {
	
	/** Der eindeutige Name des DBMS */
    @JsonProperty public String Name;
	
	/** Gibt an, ob das DBMS bei einem DROP-Befehl das Schlüsselwort IF EXISTS unterstützt. */
    @JsonSerialize(using=Boolean01ConverterSerializer.class)
    @JsonDeserialize(using=Boolean01ConverterDeserializer.class)	
    @JsonProperty public Boolean DropIfExists;

	
	/** Der Name der für das Schema zu verwendenden Collation (oder NULL) */
    @JsonProperty public String CollationSchema;
	
	/** Der Name der für die Tabellen zu verwendenden Collation (oder NULL) */
    @JsonProperty public String CollationTable;
	    
}
