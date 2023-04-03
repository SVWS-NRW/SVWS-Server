package de.svws_nrw.core.abschluss.bk.a;

import jakarta.xml.bind.annotation.XmlRootElement;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse stellt einen Service für die Fachinformationen eines Faches für die Abschlussberechnung nach Anlage A zur Verfügung.
 */
@XmlRootElement(name = "BKAbschlussFach")
@Schema(name = "BKAbschlussFach", description = "die Fachinformationen eines Faches für die Abschlussberechnung nach Anlage A")
public class BKAnlageAFach {

	/** Das Kürzel des Faches. */
	@Schema(required = true, description = "Das Kürzel des Faches.", example = "D")
	public String kuerzel = null;

	/** Die Note in dem Fach. */
	@Schema(required = true, description = "Die Note in dem Fach.", example = "2")
	public int note = -1;

}
