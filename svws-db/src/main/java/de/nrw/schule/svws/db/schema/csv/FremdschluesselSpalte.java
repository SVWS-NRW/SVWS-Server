package de.nrw.schule.svws.db.schema.csv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchemaTabelleFremdschluessel
 */
public class FremdschluesselSpalte {
	
	/** Der Name des Fremdschlüssels */
    @JsonProperty public String Name;

	/** Die Revision, in welcher der Fremdschlüssel eingeführt wurde - in der CSV-Datei NULL, wenn die Revision mit der Revision der Tabelle übereinstimmt */
    @JsonProperty public Integer Revision;
	
	/** Die Revision, in welcher der Fremdschlüssel als veraltet definiert wurde - in der CSV-Datei NULL, wenn diese mit der Tabelle übereinstimmt */
    @JsonProperty public Integer Veraltet;
    
	/** Der Name der Tabelle zu der der Fremdschlüssel gehört */
    @JsonProperty public String NameTabelle;

	/** Der Spaltenname in der Tabelle die zu dem Fremdschlüssel gehört */
    @JsonProperty public String NameSpalte;

	/** Der Name der Tabelle, die von dem Fremdschlüssel referneziert wird */
    @JsonProperty public String NameTabelleReferenziert;

	/** Der Spaltenname in der referezierten Tabelle die von der Spalte des Fremdschlüssels referenziert wird */
    @JsonProperty public String NameSpalteReferenziert;
    
    /** Gibt die Aktion an, die beim Aktualisieren von Daten in der referenzierten Tabelle ausgeführt werden sollen */
    @JsonProperty public String OnUpdate;
    
    /** Gibt die Aktion an, die beim Löschen von Daten in der referenzierten Tabelle ausgeführt werden sollen */
    @JsonProperty public String OnDelete;

    
    
    /** Die Revision, bei welcher der Fremdschlüssel eingeführt wurde */
    @JsonIgnore public Versionen dbRevision;

    /** Die Revision, ab der der Fremdschlüssel veraltet ist, oder null */
    @JsonIgnore public Versionen dbRevisionVeraltet;
    
    /** Das zugeordnete Tabellen-Objekt */
    @JsonIgnore public Tabelle tabelle;

    /** Das zugeordnete Spalten-Objekt */
    @JsonIgnore public TabelleSpalte spalte;

    
    /** Das zugeordnete Tabellen-Objekt der referenzierten Tabelle */
    @JsonIgnore public Tabelle tabelleReferenziert;

    /** Das zugeordnete Spalten-Objekt bei der referenzierten Tabelle */
    @JsonIgnore public TabelleSpalte spalteReferenziert;
    
}
