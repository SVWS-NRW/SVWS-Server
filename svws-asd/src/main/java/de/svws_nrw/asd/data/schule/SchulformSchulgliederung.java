package de.svws_nrw.asd.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert das Paar einer Kombination aus Schulform und Schulgliederung.
 */
@XmlRootElement
@Schema(description = "eine Kombination aus Schulform und Schulgliederung.")
@TranspilerDTO
public class SchulformSchulgliederung {

	/** Das Kürzel der Schulform */
	@Schema(description = "das Kürzel der Schulform", example = "BK")
	public @NotNull String schulform = "GY";

	/** Das Kürzel der Schulgliederung bzw. des Bildungsganges. Null, falls alle Gliederungen der Schulform gemeint sind. */
	@Schema(description = "das Kürzel der Schulgliederung bzw. des Bildungsganges", example = "A01")
	public String gliederung = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchulformSchulgliederung() {
		// leer
	}

}
