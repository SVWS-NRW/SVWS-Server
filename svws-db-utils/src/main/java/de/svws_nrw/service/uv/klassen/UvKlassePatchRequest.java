package de.svws_nrw.service.uv.klassen;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Die Klasse beschreibt das Patch-DTO für eine UV-Klasse.
 */
@Schema(description = "Patch-Daten für eine UV-Klasse.")
public class UvKlassePatchRequest {

	/** Die ID der Klasse. */
	@Schema(description = "die ID der Klasse", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die Bezeichnung. */
	@Schema(description = "die Bezeichnung.")
	public JsonNullable<@Size(max = 150, message = "Die Bezeichnung darf maximal 150 Zeichen lang sein.") String> bezeichnung = JsonNullable.undefined();

	/** Das Kürzel. */
	@Schema(description = "das Kürzel.")
	public JsonNullable<
			@NotNull(message = "Das Kürzel muss gesetzt werden.")
			@Size(max = 15, message = "Das Kürzel darf maximal 15 Zeichen lang sein.") String> kuerzel = JsonNullable.undefined();

	/** Die Parallelität. */
	@Schema(description = "die Parallelität.")
	public JsonNullable<
			@NotNull(message = "Die Parallelität muss gesetzt werden.")
			@Size(max = 2, message = "Die Parallelität darf maximal 2 Zeichen lang sein.") String> parallelitaet = JsonNullable.undefined();

	/** Die ID der Stundentafel. */
	@Schema(description = "die ID der Stundentafel.")
	public JsonNullable<Long> idStundentafel = JsonNullable.undefined();

	/** Die ID der Schülergruppe. */
	@Schema(description = "die ID der Schülergruppe.")
	public JsonNullable<@NotNull(message = "Die ID der Schülergruppe muss gesetzt werden.") Long> idSchuelergruppe = JsonNullable.undefined();

	/** Das Organisationsform-Kürzel. */
	@Schema(description = "das Organisationsform-Kürzel.")
	public JsonNullable<
			@Size(max = 1, message = "Das Organisationsform-Kürzel darf maximal 1 Zeichen lang sein.") String> orgFormKrz = JsonNullable.undefined();

	/** Die ID der Fachklasse. */
	@Schema(description = "die ID der Fachklasse.")
	public JsonNullable<Long> idFachklasse = JsonNullable.undefined();

	/** Die ASD-Schulformnummer. */
	@Schema(description = "die ASD-Schulformnummer.")
	public JsonNullable<
			@Size(max = 3, message = "Die ASD-Schulformnummer darf maximal 3 Zeichen lang sein.") String> asdSchulformNr = JsonNullable.undefined();

}
