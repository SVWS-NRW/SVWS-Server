package de.nrw.schule.svws.db.schema.csv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.nrw.schule.svws.csv.converter.Boolean01ConverterDeserializer;
import de.nrw.schule.svws.csv.converter.Boolean01ConverterSerializer;
import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchemaTabelleManualSQL.
 */
public class TabelleManualSQL {

	/** Die Revision, in welcher der SQL-Befehl ausgeführt wird, sofern es sich um ein Update handelt. */
    @JsonProperty public Long Revision;
	
	/** Die Revision, bis zu welcher der SQL-Befehl ausgeführt werden soll: -1 falls es keine Einschränkung gibt und NULL,
     * wenn die Revision mit der Veraltet-Revision der Tabelle übereinstimmt */
    @JsonProperty public Long Veraltet;

	/** Gibt die Reihenfolge an, in der die manuelle SQL-Befehle einer Revision abgearbeitet werden sollen */
    @JsonProperty public Integer Reihenfolge;
	
	/** Das Datenbanksystem in wessen SQL-Dialekt der manuelle SQL-Befehl formuliert wurde. ALL, falls der SQL-Befehl in Standard-SQL formuliert wurde und für alle DBMS definiert ist */
    @JsonProperty public String dbms;

	/** Der Name einer Tabelle, aus welche sich der SQL-Befehl bezieht */
    @JsonProperty public String NameTabelle;
		  
	/** Eine kurze Beschreibung der Bedeutung des manuellen SQL-Befehls */
    @JsonProperty public String Kommentar;
	
	/** Gibt an, ob der SQL-Befehl nur zum Aktualisieren von Daten ausgeführt werden soll oder auch bei dem Erstellen/Initialisieren der Tabelle */
    @JsonSerialize(using=Boolean01ConverterSerializer.class)
    @JsonDeserialize(using=Boolean01ConverterDeserializer.class)	
    @JsonProperty public Boolean UpdateOnly;

	/** Gibt den manuellen SQL-Befehl in dem entsprechenden SQL-Dialekt an. */
    @JsonProperty public String sql;

    
    
    /** Die Revision, bei welcher der SQL-Befehl aufgeführt wird */
    @JsonIgnore public SchemaRevisionen dbRevision;

    /** Die Revision, bis zu welcher der SQL-Befehl ausgeführt werden kann, oder null */
    @JsonIgnore public SchemaRevisionen dbRevisionVeraltet;

    
    /** Das DBMS für welches der SQL-Befehl definiert wurde. */
    @JsonIgnore public DBDriver dbDriver;    
    
    /** Das zugeordnete Tabellen-Objekt */
    @JsonIgnore public Tabelle tabelle;
    

    
	/**
	 * Liefert die SQL-Skripte, falls das DBMS geeignet ist.
	 * 
	 * @param dbms     das DBMS für welches das Skript angefragt wird
	 * 
	 * @return das SQL-Skript
	 */
    @JsonIgnore 
	public String getSQL(DBDriver dbms) {
		if ((!"ALL".equals(this.dbms)) && (!dbDriver.equals(dbms)))
			return null;
		return this.sql;
	}
    
}
