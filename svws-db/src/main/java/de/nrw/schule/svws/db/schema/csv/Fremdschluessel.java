package de.nrw.schule.svws.db.schema.csv;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;


/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Fremdschluessel.
 */
public class Fremdschluessel {
	
	/** Der Name des Fremdschlüssels */
	@JsonIgnore public String Name;
    
    /** Die Revision, bei welcher der Fremdschlüssel eingeführt wurde */
    @JsonIgnore public SchemaRevisionen dbRevision;

    /** Die Revision, ab der der Fremdschlüssel veraltet ist, oder null */
    @JsonIgnore public SchemaRevisionen dbRevisionVeraltet;
    
	/** Die Tabelle, zu der der Fremdschlüssel gehört*/
	@JsonIgnore public Tabelle tabelle; 
	
	/** Die Spalten, die zu dem Fremdschlüssel gehören */ 
	@JsonIgnore public Vector<TabelleSpalte> spalten = new Vector<>();
	
	/** Die Tabelle, die von dem Fremdschlüssel referenziert wird */
	@JsonIgnore public Tabelle tabelleReferenziert; 
	
	/** Die Spalten, die von dem Fremdschlüssel referenziert werden */ 
	@JsonIgnore public Vector<TabelleSpalte> spaltenReferenziert = new Vector<>();	

    /** Gibt die Aktion an, die beim Aktualisieren von Daten in der referenzierten Tabelle ausgeführt werden sollen */
    @JsonProperty public String OnUpdate;
    
    /** Gibt die Aktion an, die beim Löschen von Daten in der referenzierten Tabelle ausgeführt werden sollen */
    @JsonProperty public String OnDelete;
    
    
    
    /**
     * Liefert die Tabellenspalten des Fremdschlüssels in der durch das Feld Sortierung definierten Reihenfolge
     * 
     * @return die Tabellenspalten des Fremdschlüssels in der durch das Feld Sortierung definierten Reihenfolge 
     */
    @JsonIgnore 
    public List<TabelleSpalte> getSpalten() {
    	return spalten.stream().sorted((a,b) -> { return a.Sortierung.compareTo(b.Sortierung); }).collect(Collectors.toList());
    }
	
	
	/**
	 * Erstellt einen SQL-String für das Erstellen einen Fremdschlüssels als SQL-CONSTRAINT 
	 * 
	 * @return der SQL-String für das Erstellen des Fremdschlüssels
	 */
	@JsonIgnore
	public String getSQL() {
		return "CONSTRAINT " + this.Name + " FOREIGN KEY (" 
				+ getSpalten().stream().map(spalte -> spalte.NameSpalte).collect(Collectors.joining(", "))
				+ ") REFERENCES " + this.tabelleReferenziert.Name + '('
				+ spaltenReferenziert.stream().map(spalte -> spalte.NameSpalte).collect(Collectors.joining(", "))
				+ ")" 
				+ ("".equals(this.OnUpdate) || "NO ACTION".equals(this.OnUpdate) ? "" : " ON UPDATE " + this.OnUpdate)
				+ ("".equals(this.OnDelete) || "NO ACTION".equals(this.OnDelete) ? "" : " ON DELETE " + this.OnDelete);
	}

	
	/**
	 * Erstellt einen SQL-String für das nachträgliche Erstellen einen Fremdschlüssels 
	 * für den SQL-Dialekt des angegebenen DBMS
	 * 
	 * @param dbms   das DBMS
	 * 
	 * @return der SQL-String für das nachträgliche Erstellen des Fremdschlüssels
	 */
	@JsonIgnore
	public String getSQLCreate(DBDriver dbms) {
		switch (dbms) {
			case SQLITE:
				// TODO currently not supported
				return null;
			case MDB:
			case MARIA_DB:
			case MYSQL:
			case MSSQL:
			default:
				return "ALTER TABLE " + this.tabelle.Name + " ADD " + this.getSQL();		
		}
	}
	
	
	/**
	 * Erzeugt den SQL-Drop-Befehl für diesen Fremdschlüssel für den SQL-Dialekt des angegebenen DBMS
	 * 
	 * @param dbms   das DBMS
	 * 
	 * @return der SQL-Drop-Befehl
	 */
	@JsonIgnore
	public String getSQLDrop(DBDriver dbms) {
		switch (dbms) {
			case SQLITE:
				// TODO SQLite - Currently not supported
				return null;  
			case MARIA_DB:
			case MYSQL:
			default:
				return "ALTER TABLE " + this.tabelle.Name + " DROP FOREIGN KEY " + this.Name + ";";
			case MDB:
			case MSSQL:
				return "ALTER TABLE " + this.tabelle.Name + " DROP CONSTRAINT " + this.Name + ";";
		}
	}
	
}
