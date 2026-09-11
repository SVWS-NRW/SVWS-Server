package de.svws_nrw.core.data.uv.export;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt eine Stundentafel in den Grunddaten des UV-Exports.
 */
@Schema(description = "Diese Klasse beschreibt eine Stundentafel in den Grunddaten des UV-Exports.")
@TranspilerDTO
public class UVv1StundentafelExport {

    /** Die UV-ID der Stundentafel. */
    @Schema(description = "die UV-ID der Stundentafel", example = "4711")
    public long uvId = -1;

    /** Die ID des zugehörigen Jahrgangs. */
    @Schema(description = "die ID des zugehörigen Jahrgangs", example = "12")
    public long idJahrgang = -1;

	/** Die optionale Bezeichnung der Stundentafel. */
	@Schema(description = "die optionale Bezeichnung der Stundentafel.", example = "Stundentafel für Jahrgang 12")
	public @NotNull String bezeichnung = "";

    /** Das Datum, ab dem die Stundentafel gültig ist. */
    @Schema(description = "das Datum, ab dem die Stundentafel gültig ist", example = "2025-09-01")
    public @NotNull String gueltigVon = "";

    /** Das Datum, bis wann die Stundentafel gültig ist. */
    @Schema(description = "das Datum, bis wann die Stundentafel gültig ist", example = "2026-08-31")
    public String gueltigBis = "";

	/** Die optionale Beschreibung oder der Kommentar zur Stundentafel. */
	@Schema(description = "die optionale Beschreibung oder der Kommentar zur Stundentafel", example = "Eine Beschreibung oder ein Kommentar zu dieser Stundentafel")
	public String beschreibung = null;

	/** Ein Array mit den Fächern der Stundentafel. */
	@ArraySchema(schema = @Schema(implementation = UVv1StundentafelFachExport.class, description = "Ein Array mit den Fächern der Stundentafel"))
	public @NotNull List<UVv1StundentafelFachExport> stundentafelfaecher = new ArrayList<>();


}
