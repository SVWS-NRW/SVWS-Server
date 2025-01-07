package de.svws_nrw.core.data.enm;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu Listeneinträgen bei der Liste
 * der Lehrer-Initialkennwörter für das Externe-Noten-Modul ENM.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Struktur von JSON-Daten zu Listeneinträgen bei der Liste der Lehrer-Initialkennwörter für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMLehrerInitialKennwort {

	/** Die ID des Lehrers aus der SVWS-DB (z.B. 42) */
	@Schema(description = "Die ID des Lehrers aus der SVWS-DB", example = "42")
	public long id = -1;

	/** Das Initialkennwort für die Dienst-EMail-Adresse des Lehrers */
	@Schema(description = "Das Initialkennwort für die Dienst-EMail-Adresse des Lehrers.", example = "SicherNicht123456")
	public String initialKennwort = null;

}
