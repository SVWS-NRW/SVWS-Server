package de.nrw.schule.svws.core.data.enm;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu den Teilleistungsdaten
 * eines Schüler in Bezug auf den Lernabschnitt für das Externe-Noten-Modul ENM. 
 */
@XmlRootElement
@Schema(description="Spezifiziert die Struktur von JSON-Daten zu den Teilleistungsdaten eines Schüler in Bezug auf den Lernabschnitt für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMTeilleistung {

	/** Die ID dieser Teilleistung in der SVWS-DB (z.B. 307956) */
	@Schema(required = true, description = "Die ID dieser Teilleistung in der SVWS-DB", example="307956")
	public long id;

	/** Die ID der Teilleistungsart (z.B. 42) */
	@Schema(required = true, description = "Die ID der Teilleistungsart", example="42")
	public long artID;

	/** Das Datum an dem die Teilleistung erbracht bzw. festgelegt wurde. (z.B. "2020-10-10") */
	@Schema(required = false, description = "Das Datum an dem die Teilleistung erbracht bzw. festgelegt wurde", example="2020-10-10")
	public String datum;
	
	/** Eine Bemerkung zu der Teilleistung (z.B. "Nachgeschrieben") */
	@Schema(required = false, description = "Eine Bemerkung zu der Teilleistung", example="Nachgeschrieben")
	public String bemerkung; 

	/** Das Noten-Kürzel für die Teilleistung (z.B. "1+") */
	@Schema(required = false, description = "Das Noten-Kürzel für die Teilleistung", example="1+")
	public String notenKuerzel;
	
}
