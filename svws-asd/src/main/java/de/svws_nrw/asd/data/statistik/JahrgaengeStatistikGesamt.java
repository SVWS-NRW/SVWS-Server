package de.svws_nrw.asd.data.statistik;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die grundlegenden Daten eines Jahrgangs.
 */
@XmlRootElement
@Schema(description = "Die Daten eines Jahrgangs.")
@TranspilerDTO
public class JahrgaengeStatistikGesamt {

	/** Die ID des Jahrgangs. */
	@Schema(description = "die ID des Jahrgangs", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Das schulinterne Kürzel des Jahrgangs. */
	@Schema(description = "Das schulinterne Kürzel des Jahrgangs.", example = "ABC")
	public String kuerzel;

	/** Die ID des dem Jahrgang zugeordneten Statistik-Kürzels. */
	@Schema(description = "die ID des dem Jahrgang zugeordneten Statistik-Kürzels", example = "21000001")
	public Long idKatalog;

	/** Die Sortierreihenfolge des Jahrgangslisten-Eintrags. */
	@Schema(description = "die Sortierreihenfolge des Jahrgangslisten-Eintrags", example = "1")
	public int sortierung;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public JahrgaengeStatistikGesamt() {
		// leer
	}

}
