package de.nrw.schule.svws.db.schema.csv;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle TabelleIndex.
 */
public class TabelleIndex {

	/** Der Name des Index in der SVWS-DB */
	@JsonProperty public String Name;
	
	/** Die Revision, in welcher der Index eingeführt wurde */
    @JsonIgnore public SchemaRevisionen dbRevision;
	
	/** Die Revision, in welcher der Index als veraltet definiert wurde */
    @JsonIgnore public SchemaRevisionen dbRevisionVeraltet;
    
    /** Die Tabelle, welcher der Index zugeorndet ist */
    @JsonIgnore public Tabelle tabelle; 
	
    /** Die Tabellenspalten, die diesem Index zugeordnet sind. */
	@JsonIgnore public HashMap<String, TabelleSpalte> spalten = new HashMap<>();
	
	/** Die CSV-DTO-Objekte für die Tabellenspalten des Index, die diesem Index zugeordnet sind */
	@JsonIgnore public HashMap<String, TabelleIndexSpalte> indexSpalten = new HashMap<>();

	
	
    /**
     * Liefert die Tabellenspalten des Index in der durch das Feld Sortierung definierten Reihenfolge
     * 
     * @return die Tabellenspalten des Index in der durch das Feld Sortierung definierten Reihenfolge 
     */
    @JsonIgnore 
    public List<TabelleSpalte> getSpalten() {
    	return spalten.values().stream().sorted((a,b) -> { return a.Sortierung.compareTo(b.Sortierung); }).collect(Collectors.toList());
    }
	
	
	/**
	 * Erstellt einen SQL-String für das Erstellen eines Index 
	 * 
	 * @return der SQL-String für das Erstellen des Index
	 */
	@JsonIgnore
	public String getSQL() {
		return "CREATE INDEX " + this.Name + " ON " + this.tabelle.Name + '(' 
				+ getSpalten().stream().map(spalte -> spalte.NameSpalte).collect(Collectors.joining(", "))
				+ ");";
	}
	
	
	/**
	 * Erzeugt den SQL-Drop-Befehl für diesen Index für den SQL-Dialekt des angegebenen DBMS
	 * 
	 * @param dbms   das DBMS
	 * 
	 * @return der SQL-Drop-Befehl
	 */
	@JsonIgnore
	public String getSQLDrop(DBDriver dbms) {
		switch (dbms) {
			case SQLITE:
				return "DROP INDEX IF EXISTS " + this.Name + ";";
			case MARIA_DB:
			case MYSQL:
			default:
				return "ALTER TABLE " + this.tabelle.Name + " DROP INDEX " + this.Name + ';';
			case MDB:
				return "DROP INDEX " + this.Name + " ON " + this.tabelle.Name + ";";
			case MSSQL:
				return "DROP INDEX " + this.tabelle.Name + "." + this.Name + ";";
		}
	}
	
}
