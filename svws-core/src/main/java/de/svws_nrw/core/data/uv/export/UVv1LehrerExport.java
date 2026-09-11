package de.svws_nrw.core.data.uv.export;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt eine Lehrkraft in den Grunddaten des UV-Exports.
 */
@Schema(description = "Diese Klasse beschreibt eine Lehrkraft in den Grunddaten des UV-Exports.")
@TranspilerDTO
public class UVv1LehrerExport {

	/** Die eindeutige UV-ID des Lehrers im Planungsabschnitt (planungsspezifisch). */
	@Schema(description = "die eindeutige UV-ID des Lehrers im Planungsabschnitt (planungsspezifisch)", example = "4711")
	public long uvId = -1;

	/** Die ID des Lehrers in den Lehrerdaten. */
	@Schema(description = "die ID des Lehrers in den Lehrerdaten", example = "102")
	public Long idLehrer = null;

	/** Das Lehrer-Kürzel für eine eindeutige Identifikation. */
	@Schema(description = "das Lehrer-Kürzel für eine eindeutige Identifikation", example = "ABC")
	public @NotNull String kuerzel = "";

	/** Der Nachname des Lehrers. */
	@Schema(description = "der Nachname des Lehrers", example = "Mustermann")
	public String nachname = null;

	/** Der Vorname (bzw. Rufname) des Lehrers. */
	@Schema(description = "der Vorname (bzw. Rufname) des Lehrers", example = "Max")
	public String vorname = null;

	/** Ein Array mit den Unterrichtsfächern des Lehrers */
	@ArraySchema(schema = @Schema(implementation = UVv1LehrerUnterrichtsfachExport.class, description = "Ein Array mit den Unterrichtsfächern des Lehrers"))
	public @NotNull List<UVv1LehrerUnterrichtsfachExport> unterrichtsfaecher = new ArrayList<>();

	/** Ein Array mit den Anrechnungsstunden des Lehrers */
	@ArraySchema(schema = @Schema(implementation = UVv1LehrerAnrechnungsstundenExport.class, description = "Ein Array mit den Anrechnungsstunden des Lehrers"))
	public @NotNull List<UVv1LehrerAnrechnungsstundenExport> anrechnungsstunden = new ArrayList<>();

	/** Ein Array mit dem Pflichtstundensoll des Lehrers */
	@ArraySchema(schema = @Schema(implementation = UVv1LehrerPflichtstundensollExport.class, description = "Ein Array mit dem Pflichtstundensoll des Lehrers"))
	public @NotNull List<UVv1LehrerPflichtstundensollExport> pflichtstundensoll = new ArrayList<>();


}
