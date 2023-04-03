package de.svws_nrw.core.data.enm;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu den Arten von Teilleistungen,
 * die in der Notendatei für das Externe-Noten-Modul ENM vorhanden sind. 
 */
@XmlRootElement
@Schema(description="Spezifiziert die Struktur von JSON-Daten zu den Arten von Teilleistungen, die in der Notendatei für das Externe-Noten-Modul ENM vorhanden sind.")
@TranspilerDTO
public class ENMTeilleistungsart {

	/** Die ID der Teilleistungsart (z.B. 42) */
	@Schema(description = "Die ID der Teilleistungsart", example="42")
    public long id;
    
    /** Die Bezeichnung der Teilleistungsart (z.B. Somi-1) */
	@Schema(description = "Die Bezeichnung der Teilleistungsart", example="Somi-1")
    public String bezeichnung;
    
    /** Sortierung der Teilleistungsarten (z.B. 12) */
	@Schema(description = "Sortierung der Teilleistungsarten", example="12")
    public Integer sortierung;

    /** Die Gewichtung der Teilleistungsart in Bezug auf die Leistungsdaten (z.B. 0,25) */
	@Schema(description = "Die Gewichtung der Teilleistungsart in Bezug auf die Leistungsdaten", example="0,25")
    public Double gewichtung;	
	
}
