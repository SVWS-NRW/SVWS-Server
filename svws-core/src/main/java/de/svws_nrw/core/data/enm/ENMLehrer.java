package de.svws_nrw.core.data.enm;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu den Lehrern
 * für das Externe-Noten-Modul ENM. 
 */
@XmlRootElement
@Schema(description="Spezifiziert die Struktur von JSON-Daten zu den Lehrern für das Externe-Noten-Modul ENM. ")
@TranspilerDTO
public class ENMLehrer {

	/** Die ID des Lehrers aus der SVWS-DB (z.B. 42) */
	@Schema(description = "Die ID des Lehrers aus der SVWS-DB", example="42")
	public long id;

	/** Das Kürzel des Lehrers für die Anzeige im Notenmodel (z.B. Mus) */
	@Schema(description = "Das Kürzel des Lehrers für die Anzeige im Notenmodel.", example="MUS")
	public String kuerzel;

	/** Der Nachname des Lehrers (z.B. Mustermann) */
	@Schema(description = "Der Nachname des Lehrers.", example="Mustermann")
	public String nachname;

	/** Der Vorname des Lehrers (z.B. Max) */
	@Schema(description = "Der Vorname des Lehrers.", example="Max")
	public String vorname;

	/** Das Geschlecht des Lehrers (m,w,d,x) */
	@Schema(description = "Das Geschlecht des Lehrers (m - männlich, w - weiblich, d - divers, x - ohne Angabe im Geburtenregister)", example="d")
	public String geschlecht;
	
	/** Die Dienst-EMail-Adresse des Lehrers */
	@Schema(description = "Die Dienst-EMail-Adresse des Lehrers.", example="max.musterman@irgendeine.schule.nrw")
	public String eMailDienstlich;

}
