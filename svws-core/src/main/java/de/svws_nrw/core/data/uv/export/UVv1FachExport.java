package de.svws_nrw.core.data.uv.export;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt ein Fach in den Grunddaten des UV-Exports.
 */
@Schema(description = "Diese Klasse beschreibt ein Fach in den Grunddaten des UV-Exports.")
@TranspilerDTO
public class UVv1FachExport {

	/** Die UV-ID des UV-Fachs. */
	@Schema(description = "die UV-ID des UV-Fachs", example = "4711")
	public long uvId = -1;

	/** Die ID des Fachs in den Fachdaten. */
	@Schema(description = "die ID des Fachs in den Fachdaten", example = "17")
	public long idFach = -1;

	/** Das Datum, ab dem das Fach gültig ist. */
	@Schema(description = "das Datum, ab dem das Fach gültig ist", example = "2024-08-01")
	public @NotNull String gueltigVon = "";

	/** Das Datum, bis wann das Fach gültig ist (falls vorhanden). */
	@Schema(description = "das Datum, bis wann das Fach gültig ist (falls vorhanden)", example = "2025-07-31")
	public String gueltigBis;

}
