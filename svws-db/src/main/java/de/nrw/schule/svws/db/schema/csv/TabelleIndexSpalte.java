package de.nrw.schule.svws.db.schema.csv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Diese Klasse dient als DTO für die Datenbanktabelle TabelleIndexSpalte.
 */
public class TabelleIndexSpalte {

	/** Der Name des Index in der SVWS-DB */
	@JsonProperty public String Name;
	
	/** Die Revision, in welcher der Index eingeführt wurde - in der CSV-Datei NULL, wenn die Revision mit der Revision der Tabelle übereinstimmt */
	@JsonProperty public Integer Revision;
	
	/** Die Revision, in welcher der Index als veraltet definiert wurde - in der CSV-Datei NULL, wenn diese mit der Tabelle übereinstimmt */
	@JsonProperty public Integer Veraltet;
	  
	/** Der Name der Tabelle, für welche der Index definiert wurde */
    @JsonProperty public String NameTabelle;

	/** Der Name der Spalte für welche der Index definiert wurde */
    @JsonProperty public String NameSpalte;
	
    
    /** Die Revision, bei der der Index erstellt wird */
    @JsonIgnore public Versionen dbRevision;

    /** Die Revision, ab der der Index veraltet ist, oder null */
    @JsonIgnore public Versionen dbRevisionVeraltet;
    
    /** Das zugeordnete Tabellen-Objekt */
    @JsonIgnore public Tabelle tabelle;

    /** Das zugeordnete Tabellen-Objekt */
    @JsonIgnore public TabelleSpalte spalte;
    
    
}
