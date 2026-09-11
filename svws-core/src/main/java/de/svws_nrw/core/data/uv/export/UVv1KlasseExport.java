package de.svws_nrw.core.data.uv.export;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt eine Klasse innerhalb eines Planungsabschnitts im UV-Export.
 */
@Schema(description = "Diese Klasse beschreibt eine Klasse innerhalb eines Planungsabschnitts im UV-Export.")
@TranspilerDTO
public class UVv1KlasseExport {

	/**
	 * Die UV-ID der Klasse (generiert, planungsspezifisch).
	 */
	@Schema(description = "die UV-ID der Klasse", example = "4711")
	public long uvId;

	/**
	 * Die ID des Schuljahresabschnitts.
	 */
	@Schema(description = "die ID des Schuljahresabschnitts", example = "42")
	public long idSchuljahresabschnitt;

	/**
	 * Bezeichnender Text für die Klasse.
	 */
	@Schema(description = "die Bezeichnung der Klasse", example = "5a")
	public String bezeichnung;

	/**
	 * Das Kürzel der Klasse.
	 */
	@Schema(description = "das Klassenkürzel", example = "5a")
	public @NotNull String kuerzel = "";

	/**
	 * Die Parallelität (z. B. a/b/c).
	 */
	@Schema(description = "die Parallelität der Klasse", example = "b")
	public @NotNull String parallelitaet = "";

	/**
	 * Die UV-ID der Stundentafel.
	 */
	@Schema(description = "die UV-ID der Stundentafel", example = "17")
	public Long idStundentafel;

	/**
	 * Die UV-ID der zugehörigen Schülergruppe.
	 */
	@Schema(description = "die UV-ID der Schülergruppe", example = "815")
	public long schuelergruppeUvId;

	/**
	 * Das Kürzel der Organisationsform.
	 */
	@Schema(description = "das Kürzel der Organisationsform", example = "G8")
	public String orgFormKrz;

	/**
	 * Die ID der Fachklasse (nur BK SBK).
	 */
	@Schema(description = "die ID der Fachklasse", example = "12")
	public Long idFachklasse;

	/**
	 * Die Schulgliederungsnummer (ASD-Schulform-Nr).
	 */
	@Schema(description = "die Schulgliederungsnummer (ASD-Schulform-Nr)", example = "03")
	public String asdSchulformNr;

	/** Ein Array mit den Klassenlehrer-Zuordnungen der Klasse. */
	@ArraySchema(schema = @Schema(implementation = UVv1KlassenLehrerExport.class, description = "Ein Array mit den Klassenlehrer-Zuordnungen der Klasse"))
	public @NotNull List<UVv1KlassenLehrerExport> klassenlehrer = new ArrayList<>();

}
