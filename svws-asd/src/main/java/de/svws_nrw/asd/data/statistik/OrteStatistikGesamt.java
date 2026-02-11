package de.svws_nrw.asd.data.statistik;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten für die Katalog der Orte übergeben werden.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Orte.")
@TranspilerDTO
public class OrteStatistikGesamt {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die Postleitzahl. */
	@Schema(description = "die Postleitzahl", example = "53840")
	public String plz;

	/** Der Name des Ortes. */
	@Schema(description = "der Name des Ortes", example = "Troisdorf")
	public String ortsname;
}
