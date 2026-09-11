package de.svws_nrw.service.uv.zeitraster;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Die Klasse beschreibt das Patch-DTO für einen UV-Zeitraster-Eintrag.
 */
@Schema(description = "Patch-Daten für einen UV-Zeitraster-Eintrag.")
public class UvZeitrasterEintragPatchRequest {

	/** Die ID des UV-Zeitraster-Eintrags. */
	@Schema(description = "die ID des UV-Zeitraster-Eintrags", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Der Wochentag. */
	@Schema(description = "Der Wochentag.")
	public JsonNullable<@NotNull(message = "Der Wochentag darf nicht null sein.") Integer> wochentag = JsonNullable.undefined();

	/** Die Stunde. */
	@Schema(description = "Die Stunde.")
	public JsonNullable<@NotNull(message = "Die Stunde darf nicht null sein.") Integer> stunde = JsonNullable.undefined();

	/** Der Beginn (Minuten ab Mitternacht). */
	@Schema(description = "Der Beginn (Minuten ab Mitternacht).")
	public JsonNullable<@NotNull(message = "Der Beginn darf nicht null sein.") Integer> beginn = JsonNullable.undefined();

	/** Das Ende (Minuten ab Mitternacht). */
	@Schema(description = "Das Ende (Minuten ab Mitternacht).")
	public JsonNullable<@NotNull(message = "Das Ende darf nicht null sein.") Integer> ende = JsonNullable.undefined();

}
