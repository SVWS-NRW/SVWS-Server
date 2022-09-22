package de.nrw.schule.svws.db.schema.csv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchemaDefaultDaten.
 */
public class TabelleDefaultDaten  {

	/** Der Name der Tabelle mit Default-Daten */
    @JsonProperty public String NameTabelle;
	
	/** Die Revision, bei welcher die Default-Daten angepasst wurden */
    @JsonProperty public Integer Revision;
	
	/** Der Kommentar zu der Anpassung der Default-Daten */
    @JsonProperty public String Kommentar;
    
    
    /** Das zugeordnete Tabellen-Objekt */
    @JsonIgnore public Tabelle tabelle;
    
    /** Die Revision, bei der die Default-Daten angepasst wurden */
    @JsonIgnore public Versionen dbRevision;

}
