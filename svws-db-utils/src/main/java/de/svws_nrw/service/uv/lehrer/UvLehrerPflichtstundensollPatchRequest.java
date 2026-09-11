package de.svws_nrw.service.uv.lehrer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Die Klasse beschreibt das Patch-DTO für die Anrechnungsstunden von Lehrern.
 */
@Schema(description = "Anrechnungsstunden bei Lehrerabschnittsdaten.")
public class UvLehrerPflichtstundensollPatchRequest {

	/** Die ID des UvLehrerPflichtstundensoll. */
	@Schema(description = "die ID des UvLehrerPflichtstundensoll", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die Anzahl der Pflichtstunden, die der Lehrer in dem Gültigkeitszeitraum zu leisten hat. */
	@Schema(description = "Die Anzahl der Pflichtstunden, die der Lehrer in dem Gültigkeitszeitraum zu leisten hat.")
	public JsonNullable<
			@NotNull(message = "Das Pflichtstundensoll muss vorhanden sein.")
			@Min(value = 0, message = "Die Anzahl der Pflichtstunden darf nicht negativ sein.") Double> pflichtstdSoll = JsonNullable.undefined();

	/** Das Datum, ab dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).")
	public JsonNullable<
			@NotNull(message = "Der Gültigkeitsbeginn eines Pflichtstundensolls muss gesetzt werden.") String> gueltigVon = JsonNullable.undefined();

	/** Das Datum, bis zu dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).")
	public JsonNullable<String> gueltigBis = JsonNullable.undefined();

}
