package de.svws_nrw.core.data.uv.export;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt einen Raum in den Grunddaten des UV-Exports.
 */
@Schema(description = "Diese Klasse beschreibt einen Raum in den Grunddaten des UV-Exports.")
@TranspilerDTO
public class UVv1RaumExport {

	/** Die eindeutige UV-ID des Raums. */
	@Schema(description = "die eindeutige UV-ID des Raums", example = "4711")
	public long uvId = -1;

	/** Das Kürzel des Raums. */
	@Schema(description = "das Kürzel des Raums", example = "R204")
	public @NotNull String kuerzel = "";

	/** Die Beschreibung des Raums. */
	@Schema(description = "die Beschreibung des Raums", example = "Klassenraum der Klasse 07b")
	public String beschreibung = "";

	/** Die Größe des Raumes an Arbeitsplätzen für Schüler. */
	@Schema(description = "die Größe des Raumes an Arbeitsplätzen für Schüler", example = "30")
	public int groesse = -1;

	/** Die UV-ID der Raumgruppe, falls der Raum zu einer solchen gehört. */
	@Schema(description = "die UV-ID der Raumgruppe, falls der Raum zu einer solchen gehört", example = "102")
	public Long raumgruppeUvId = null;

	/** Das Datum, ab dem der Raum gültig ist. */
	@Schema(description = "das Datum, ab dem der Raum gültig ist", example = "2025-08-01")
	public @NotNull String gueltigVon = "";

	/** Das Datum, bis wann der Raum gültig ist. Ist kein Datum gesetzt, gilt der Raum unbegrenzt weiter. */
	@Schema(description = "das Datum, bis wann der Raum gültig ist. Ist kein Datum gesetzt, gilt der Raum unbegrenzt weiter.", example = "2026-07-31")
	public String gueltigBis = null;

}
