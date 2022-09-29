package de.nrw.schule.svws.db.schema.csv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.nrw.schule.svws.db.schema.SchemaRevisionen;


/**
 * Diese Klasse dient als DTO für die Datenbanktabelle TabelleUniqueSpalte.
 */
public class TabelleUniqueSpalte {

	/** Der Name der Unique-Constraint in der SVWS-DB */
	@JsonProperty public String Name;
	
	/** Die Revision, in welcher die Unique-Constraint eingeführt wurde - in der CSV-Datei NULL, wenn die Revision mit der Revision der Tabelle übereinstimmt */
	@JsonProperty public Long Revision;
	
	/** Die Revision, in welcher die Unique-Constraint als veraltet definiert wurde - in der CSV-Datei NULL, wenn diese mit der Tabelle übereinstimmt */
	@JsonProperty public Long Veraltet;
	  
	/** Der Name der Tabelle, für welche die Unique-Constraint definiert wurde */
    @JsonProperty public String NameTabelle;

	/** Der Name der Spalte für welche die Unique-Constraint definiert wurde */
    @JsonProperty public String NameSpalte;
	
    
    /** Die Revision, bei der die Unique-Constraint erstellt wird */
    @JsonIgnore public SchemaRevisionen dbRevision;

    /** Die Revision, ab der die Unique-Constraint veraltet ist, oder null */
    @JsonIgnore public SchemaRevisionen dbRevisionVeraltet;
    
    /** Das zugeordnete Tabellen-Objekt */
    @JsonIgnore public Tabelle tabelle;

    /** Das zugeordnete Tabellen-Objekt */
    @JsonIgnore public TabelleSpalte spalte;
    
    
}
