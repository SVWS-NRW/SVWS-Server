package de.svws_nrw.asd.data.statistik;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die grundlegenden Daten eines Faches.
 */
@XmlRootElement
@Schema(description = "Die Daten eines Faches.")
@TranspilerDTO
public class FachStatistikGesamt {

	/** Die ID des Fachs. */
	@Schema(description = "die ID des Fachs", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Das eindeutige Kürzel des Fachs */
	@Schema(description = "das eindeutige Kürzel des Fachs.", example = "M")
	public @NotNull String kuerzel = "";

	/** Das Statistik-Kürzel des Fachs */
	@Schema(description = "das Statistik-Kürzel des Fachs.", example = "M")
	public @NotNull String kuerzelStatistik = "";

	/** Die Sprache in der das Fach unterrichtet wird, sofern es sich um ein bilinguales Sachfach handelt. */
	@Schema(description = "Die Sprache in der das Fach unterrichtet wird, sofern es sich um ein bilinguales Sachfach handelt.", example = "Englisch")
	public String bilingualeSprache;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public FachStatistikGesamt() {
		// leer
	}

}
