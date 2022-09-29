package de.nrw.schule.svws.db.schema.csv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchemeTabelleTrigger.
 */
public class Trigger {

	/** Der Name des Triggers */
    @JsonProperty public String Name;
	
	/** Die Revision, in welcher der Trigger eingeführt wurde - NULL, wenn die Revision mit der Revision der Tabelle übereinstimmt */
    @JsonProperty public Long Revision;
	
	/** Die Revision, in welcher der Trigger als veraltet definiert wurde - NULL, wenn diese mit der Tabelle übereinstimmt */
    @JsonProperty public Long Veraltet;
	  
	/** Das Datenbanksystem für welches der Trigger definiert wurde - NULL nicht erlaubt, da Trigger vom SQL-Dialekt abhängen */
    @JsonProperty public String dbms;

	/** Der Name einer Tabelle, an welche der Trigger gebunden ist */
    @JsonProperty public String NameTabelle;
		  
	/** Ggf. der Name einer weiteren Tabelle mit welcher der Trigger arbeitet. Noch weitere Tabellen können zwar verwendet werden, hier aber nicht mehr referenziert werden */
    @JsonProperty public String NameTabelle2;
		  
	/** Der SQL-Befehl zum Löschen des Triggers in dem entsprechenden SQL-Dialekt des DBMS */
    @JsonProperty public String sql_drop;

	/** Der SQL-Befehl zum Erstellen des Triggers in dem entsprechenden SQL-Dialekt des DBMS */
    @JsonProperty public String sql_create;

    
    
    /** Die Revision, bei welcher der Trigger erstellt wird */
    @JsonIgnore public SchemaRevisionen dbRevision;

    /** Die Revision, bis zu welcher der Trigger gültig ist, oder null */
    @JsonIgnore public SchemaRevisionen dbRevisionVeraltet;
    
    
    /** Das DBMS für welches der Trigger definiert wurde. */
    @JsonIgnore public DBDriver dbDriver;
    
    /** Das zugeordnete Tabellen-Objekt */
    @JsonIgnore public Tabelle tabelle;


    
	/**
	 * Liefert die SQL-Skripte zum Erstellen oder Entfernen von Triggern
	 * 
	 * @param dbms     das DBMS für welches das Skript angefragt wird
	 * @param create   gibt an, ob das CREATE-Skript oder das Drop-Skript angefragt wird.
	 * 
	 * @return das SQL-Skript zum Erstellen oder Entfernen von Triggern
	 */
    @JsonIgnore 
	public String getSQL(DBDriver dbms, boolean create) {
		if (!dbDriver.equals(dbms))
			return null;
		return create ? this.sql_create : this.sql_drop;
	}
    
}
