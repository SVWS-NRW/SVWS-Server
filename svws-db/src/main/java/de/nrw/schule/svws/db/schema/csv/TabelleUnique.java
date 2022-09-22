package de.nrw.schule.svws.db.schema.csv;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.nrw.schule.svws.db.DBDriver;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle TabelleUnique.
 */
public class TabelleUnique {

	/** Der Name der Unique-Constraint in der SVWS-DB */
	@JsonProperty public String Name;
	
	/** Die Revision, in welcher die Unique-Constraint eingeführt wurde */
    @JsonIgnore public Versionen dbRevision;
	
	/** Die Revision, in welcher die Unique-Constraint als veraltet definiert wurde */
    @JsonIgnore public Versionen dbRevisionVeraltet;
    
    /** Die Tabelle, welcher die Unique-Constraint zugeorndet ist */
    @JsonIgnore public Tabelle tabelle; 
	
    /** Die Tabellenspalten, die dieser Unique-Constraint zugeordnet sind. */
	@JsonIgnore public HashMap<String, TabelleSpalte> spalten = new HashMap<>();
	
	/** Die CSV-DTO-Objekte für die Tabellenspalten der Unique-Constraint, die dieser Unique-Constraint zugeordnet sind */
	@JsonIgnore public HashMap<String, TabelleUniqueSpalte> uniqueSpalten = new HashMap<>();


	
    /**
     * Liefert die Tabellenspalten der Unique-Constraint in der durch das Feld Sortierung definierten Reihenfolge
     * 
     * @return die Tabellenspalten der Unique-Constraint in der durch das Feld Sortierung definierten Reihenfolge 
     */
    @JsonIgnore 
    public List<TabelleSpalte> getSpalten() {
    	return spalten.values().stream().sorted((a,b) -> { return a.Sortierung.compareTo(b.Sortierung); }).collect(Collectors.toList());
    }
	
	
	/**
	 * Erstellt einen SQL-String für das Erstellen einer Unique-Constraint 
	 * 
	 * @return der SQL-String für das Erstellen der Unique-Constraint
	 */
	@JsonIgnore
	public String getSQL() {
		return "CONSTRAINT " + this.Name + " UNIQUE (" 
				+ getSpalten().stream().map(spalte -> spalte.NameSpalte).collect(Collectors.joining(", "))
				+ ")";
	}
	
	
	/**
	 * Erstellt einen SQL-String für das nachträgliche Erstellen einer Unique-Constraint 
	 * für den SQL-Dialekt des angegebenen DBMS
	 * 
	 * @param dbms   das DBMS
	 * 
	 * @return der SQL-String für das nachträgliche Erstellen der Unique-Constraint
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
	 * Erzeugt den SQL-Drop-Befehl für diese Unique-Constraint für den SQL-Dialekt des angegebenen DBMS
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
				return "ALTER TABLE " + this.tabelle.Name + " DROP INDEX " + this.Name + ";";
			case MDB:
			case MSSQL:
				return "ALTER TABLE " + this.tabelle.Name + " DROP CONSTRAINT " + this.Name + ";";
		}
	}
	
	
	
}
