package de.svws_nrw.service.uv.lehrer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Die Klasse beschreibt das Patch-DTO für die Anrechnungsstunden von Lehrern.
 */
@Schema(description = "Anrechnungsstunden bei Lehrerabschnittsdaten.")
public class UvLehrerPatchRequest {

	/** Die ID des UvLehrers. */
	@Schema(description = "die ID des UvLehrers", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die ID des Lehrers als Fremdschlüssel auf die Tabelle K_Lehrer. */
	@Schema(description = "die ID des Lehrers als Fremdschlüssel auf die Tabelle K_Lehrer", example = "102")
	public JsonNullable<Long> idKLehrer = JsonNullable.undefined();

	/** Das Lehrer-Kürzel für eine eindeutige Identifikation. */
	@Schema(description = "das Lehrer-Kürzel für eine eindeutige Identifikation", example = "ABC")
	public JsonNullable<@NotNull(message = "Das Lehrer-Kürzel muss gesetzt werden.") String> kuerzel = JsonNullable.undefined();

	/** Der Nachname des Lehrers. */
	@Schema(description = "der Nachname des Lehrers", example = "Mustermann")
	public JsonNullable<String> nachname = JsonNullable.undefined();

	/** Der Vorname (bzw. Rufname) des Lehrers. */
	@Schema(description = "der Vorname (bzw. Rufname) des Lehrers", example = "Max")
	public JsonNullable<String> vorname = JsonNullable.undefined();

}
