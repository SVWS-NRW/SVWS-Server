package de.svws_nrw.core.data.gost.laufbahnplanung.v1;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Datenaustauschformat für die Laufbahnplanung der gymnasialen Oberstufe:
 *
 * - Informationen zu einem Beratungslehrer
 */
@XmlRootElement
@Schema(description = "Informationen zu einem Beratungslehrer.")
@TranspilerDTO
public class GostLaufbahnplanungExportV1Beratungslehrer {

	/** Die ID des Lehrers. */
	@Schema(description = "die ID des Lehrers", example = "4711")
	public long id;

	/** Das Kürzel des Lehrers. */
	@Schema(description = "das Kürzel des Lehrers", example = "MUS")
	public String kuerzel;

	/** Der Nachname des Lehrers. */
	@Schema(description = "der Nachname des Lehrers", example = "Mustermann")
	public String nachname;

	/** Der Vorname des Lehrers. */
	@Schema(description = "der Vorname des Lehrers", example = "Max")
	public String vorname;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostLaufbahnplanungExportV1Beratungslehrer() {
		// leer
	}

}
