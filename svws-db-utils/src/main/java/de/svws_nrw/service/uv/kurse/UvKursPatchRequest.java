package de.svws_nrw.service.uv.kurse;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Die Klasse beschreibt das Patch-DTO für einen UV-Kurs.
 */
@Schema(description = "Patch-Daten für einen UV-Kurs.")
public class UvKursPatchRequest {

	/** Die ID des Kurses. */
	@Schema(description = "die ID des Kurses", example = "4711")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die ID des Faches. */
	@Schema(description = "die ID des Faches.")
	public JsonNullable<@NotNull(message = "Die ID des Faches darf nicht null sein.") Long> idFach = JsonNullable.undefined();

	/** Das Kürzel der Kursart. */
	@Schema(description = "das Kürzel der Kursart.")
	public JsonNullable<
			@NotBlank(message = "Die Kursart muss gesetzt werden.")
			@Size(max = 10, message = "Die Kursart darf maximal 10 Zeichen lang sein.") String> kursart = JsonNullable.undefined();

	/** Die Kursnummer. */
	@Schema(description = "die Kursnummer.")
	public JsonNullable<@NotNull(message = "Die Kursnummer darf nicht null sein.") Integer> kursnummer = JsonNullable.undefined();

	/** Die ID der Schülergruppe. */
	@Schema(description = "die ID der Schülergruppe.")
	public JsonNullable<@NotNull(message = "Die ID der Schülergruppe darf nicht null sein.") Long> idSchuelergruppe = JsonNullable.undefined();

}
