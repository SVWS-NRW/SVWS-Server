package de.nrw.schule.svws.db.schema.csv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchemaInit.
 */
public class SchemaInit {

	/** Das Datenbanksystem für welches dit Initialisierungsskripte gedacht sind */
    @JsonProperty public String dbms;
	
	/** Die Revision, ab welcher die Skripte ausgeführt werden sollen */
    @JsonProperty public Integer Revision;
	
	/** Eine Version, ab der die Skripte veraltet sind oder: -1 falls die Skripte auch bei der neuesten Version noch aktuell sind */
    @JsonProperty public Integer Veraltet;
	
	/** Ein Integer-Wert, der die Reihenfolge angibt, in der die SQL-Befehle ausgeführt werden sollen. */
    @JsonProperty public Integer Reihenfolge;
	
	/** Ein Muster für ein SQL-Skript zum Vorbereiten des Schemas */
    @JsonProperty public String sqlPrepare;

	/** SQL-Befehle, die nach dem Erstllen des Schemas mit allen Tabellen ausgeführt werden sollen. */
    @JsonProperty public String sqlFinalize;

	/** Ein Muster für ein SQL-Skript zum Entfernen des Schemas */
    @JsonProperty public String sqlUnprepare;

    
	/** Die Revision, ab welcher die Skripte ausgeführt werden sollen */
    @JsonIgnore public Versionen dbRevision;

	/** Eine Version, ab der die Skripte veraltet sind oder: -1 falls die Skripte auch bei der neuesten Version noch aktuell sind */
    @JsonIgnore public Versionen dbRevisionVeraltet;
    
}
