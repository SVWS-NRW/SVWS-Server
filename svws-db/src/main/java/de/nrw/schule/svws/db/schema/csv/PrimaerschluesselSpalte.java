package de.nrw.schule.svws.db.schema.csv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.nrw.schule.svws.csv.converter.Boolean01ConverterDeserializer;
import de.nrw.schule.svws.csv.converter.Boolean01ConverterSerializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchemaTabellePrimaerschluessel
 */
public class PrimaerschluesselSpalte {
	
	/** Der Name der Tabelle zu der die Spalte gehört */
    @JsonProperty public String NameTabelle;

	/** Der eindeutige Spaltenname in der Tabelle */
    @JsonProperty public String NameSpalte;
    
    /** Gibt an, ob dem Primärschlüssel ein Autominkrement hinzugefügt werden soll. Dies ist
     * nur bei Primärschlüsseln zulässig, die aus einem Attribut mit einem Integer-Datentyp bestehen. */ 
    @JsonSerialize(using=Boolean01ConverterSerializer.class)
    @JsonDeserialize(using=Boolean01ConverterDeserializer.class)	
    @JsonProperty public Boolean hatAutoIncrement;

}
