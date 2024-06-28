package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieses Core-DTO beinhaltet die Zuordnung einer Klasse zu einer Abteilung.
 * Die Abteilungsleitung hat im SVWS-Server ggf. erweiterte funktionsbezogene Rechte
 * auf die zugeordnetete Klasse.
 */
@XmlRootElement
@Schema(description = "die Zuordnung einer Klasse zu einer Abteilung.")
@TranspilerDTO
public class AbteilungKlassenzuordnung {

	/** Die ID der Zuordnung */
	@Schema(description = "die ID der Zuordnung", example = "4711")
	public long id = -1;

	/** Die ID der Abteilung */
	@Schema(description = "die ID der Abteilung", example = "4712")
	public long idAbteilung = -1;

	/** Die ID der Klasse */
	@Schema(description = "die ID der Klasse", example = "4713")
	public long idKlasse = -1;

}
