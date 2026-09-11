package de.svws_nrw.core.data.uv.export;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt ein Zeitraster in den Grunddaten des UV-Exports.
 */
@Schema(description = "Diese Klasse beschreibt ein Zeitraster in den Grunddaten des UV-Exports.")
@TranspilerDTO
public class UVv1ZeitrasterExport {

	/** Die UV-ID des Zeitrasters. */
	@Schema(description = "die UV-ID des Zeitrasters", example = "4711")
	public long uvId = -1;

	/** Das Datum, ab dem das Zeitraster gültig ist (ISO-Datum als String, z. B. 2025-08-01). */
	@Schema(description = "das Datum, ab dem das Zeitraster gültig ist (ISO-Datum)", example = "2025-08-01")
	public @NotNull String gueltigVon = "";

	/** Das Datum, bis zu dem das Zeitraster gültig ist (oder null, falls unbegrenzt gültig). */
	@Schema(description = "das Datum, bis zu dem das Zeitraster gültig ist (oder null, falls unbegrenzt gültig)", example = "2026-07-31")
	public String gueltigBis;

	/** Die Bezeichnung des Zeitrasters. */
	@Schema(description = "die Bezeichnung des Zeitrasters", example = "Zeitraster Schuljahr 2025/26")
	public String bezeichnung;

	/** Ein Array mit den Einträgen des Zeitrasters. */
	@ArraySchema(schema = @Schema(implementation = UVv1ZeitrasterEintragExport.class, description = "Ein Array mit den Einträgen des Zeitrasters"))
	public @NotNull List<UVv1ZeitrasterEintragExport> eintraege = new ArrayList<>();


}
