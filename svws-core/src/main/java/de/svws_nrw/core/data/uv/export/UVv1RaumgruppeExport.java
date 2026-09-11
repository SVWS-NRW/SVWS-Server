package de.svws_nrw.core.data.uv.export;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt eine Raumgruppe in den Grunddaten des UV-Exports.
 */
@Schema(description = "Diese Klasse beschreibt eine Raumgruppe in den Grunddaten des UV-Exports.")
@TranspilerDTO
public class UVv1RaumgruppeExport {

	/** Die eindeutige UV-ID der Raumgruppe. */
	@Schema(description = "die eindeutige UV-ID der Raumgruppe", example = "4711")
	public long uvId = -1;

	/** Die Bezeichnung der Raumgruppe. */
	@Schema(description = "die Bezeichnung der Raumgruppe", example = "Computerräume")
	public @NotNull String bezeichnung = "";

	/** Das Datum, ab dem die Raumgruppe gültig ist. */
	@Schema(description = "das Datum, ab dem die Raumgruppe gültig ist", example = "2025-08-01")
	public @NotNull String gueltigVon = "";

	/** Das Datum, bis wann die Raumgruppe gültig ist. Ist kein Datum gesetzt, gilt die Raumgruppe unbegrenzt weiter. */
	@Schema(description = "das Datum, bis wann die Raumgruppe gültig ist. Ist kein Datum gesetzt, gilt die Raumgruppe unbegrenzt weiter.", example = "2026-07-31")
	public String gueltigBis = null;

	/** Die Beschreibung der Raumgruppe. */
	@Schema(description = "die Beschreibung der Raumgruppe", example = "Computerräume")
	public String beschreibung = null;

}
