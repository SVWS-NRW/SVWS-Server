package de.svws_nrw.asd.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Eine Kombination aus einem allgemeinbildenden und einem berufsbildenen Abschluss
 * an einer Schulform des Berufskolleg in Bezug auf die möglichen Jahrgänge für diese
 * Abschlusskombination.
 */
@XmlRootElement
@Schema(description = "eine Kombination aus einem allgemeinbildenden und einem berufsbildenen Abschluss an einer Schulform des Berufskolleg in Bezug auf die möglichen Jahrgänge für diese Abschlusskombination.")
@TranspilerDTO
public class SchulgliederungGueltigerAbschluss {

	/** Das Kürzel des allgemeinbildenden Abschlusses */
	@Schema(description = "das Kürzel des allgemeinbildenden Abschlusses", example = "MSA")
	public @NotNull String allgemeinbildend = "OA";

	/** Das Kürzel des berufsbildenden Abschlusses */
	@Schema(description = "das Kürzel der Schulgliederung bzw. des Bildungsganges", example = "BS")
	public @NotNull String berufsbildend = "OA";

	/** der niedrigste Jahrgang, in dem der Abschluss möglich ist */
	@Schema(description = "der niedrigste Jahrgang, in dem der Abschluss möglich ist", example = "JAHRGANG_02")
	public @NotNull String jahrgangVon = "JAHRGANG_01";

	/** der höchste Jahrgang, in dem der Abschluss möglich ist */
	@Schema(description = "der höchste Jahrgang, in dem der Abschluss möglich ist", example = "JAHRGANG_04")
	public @NotNull String jahrgangBis = "JAHRGANG_13";

}
