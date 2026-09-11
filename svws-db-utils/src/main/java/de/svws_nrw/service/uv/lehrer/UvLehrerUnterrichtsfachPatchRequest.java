package de.svws_nrw.service.uv.lehrer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Die Klasse beschreibt das Patch-DTO für ein Unterrichtsfach eines UV-Lehrers.
 */
@Schema(description = "Unterrichtsfach eines UV-Lehrers.")
public class UvLehrerUnterrichtsfachPatchRequest {

	/** Die ID des Unterrichtsfach-Eintrags. */
	@Schema(description = "die ID des UvLehrerUnterrichtsfach-Eintrags", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Gibt an, ob das Fach in der Sekundarstufe I unterrichtet werden darf. */
	@Schema(description = "Gibt an, ob das Fach in der Sekundarstufe I unterrichtet werden darf.")
	public JsonNullable<@NotNull(message = "Das Kennzeichen IstSek1 muss vorhanden sein.") Boolean> istSek1 = JsonNullable.undefined();

	/** Gibt an, ob das Fach in der Sekundarstufe II unterrichtet werden darf. */
	@Schema(description = "Gibt an, ob das Fach in der Sekundarstufe II unterrichtet werden darf.")
	public JsonNullable<@NotNull(message = "Das Kennzeichen IstSek2 muss vorhanden sein.") Boolean> istSek2 = JsonNullable.undefined();

	/** Eine Bemerkung zu dem Unterrichtsfach. */
	@Schema(description = "Eine Bemerkung zu dem Unterrichtsfach.")
	public JsonNullable<String> bemerkung = JsonNullable.undefined();

	/** Das Datum, ab dem die Lehrkraft das Fach unterrichtet (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem die Lehrkraft das Fach unterrichtet (ISO-Format yyyy-MM-dd).")
	public JsonNullable<String> gueltigVon = JsonNullable.undefined();

	/** Das Datum, bis zu dem die Lehrkraft das Fach unterrichtet (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem die Lehrkraft das Fach unterrichtet (ISO-Format yyyy-MM-dd).")
	public JsonNullable<String> gueltigBis = JsonNullable.undefined();

}
