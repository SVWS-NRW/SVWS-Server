package de.nrw.schule.svws.db.schema.csv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Versionen.
 */
public class Versionen implements Comparable<Versionen> {

	/** Die Revision, welche eine Version eindeutig identifiziert */
    @JsonProperty public int Revision;
	
	/** Die Revisionnummer für die maximale Revision bei Revision -1 (wird für den Updateprozess genutzt) */
    @JsonProperty public Integer MaxRevision;

	/** Das Datum, an welchem die Version erzeugt wurde. */
    @JsonProperty public String Datum;

	/** Ein Kommentar zu der Java-Version */
    @JsonProperty public String Kommentar;

    
    @JsonIgnore 
    @Override
	public int compareTo(Versionen other) {
		return Integer.compare(this.Revision, other.Revision);
	}
    
}
