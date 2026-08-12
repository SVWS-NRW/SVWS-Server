package de.svws_nrw.service.lehrer.unterrichtsfach;

import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Die Klasse beschreibt das Patch-DTO für die Unterrichtsfächer von Lehrern.
 */
@Schema(description = "Die Zuordnung eines Unterrichtsfachs zu einer Lehrkraft.")
public class LehrerUnterrichtsfachPatchRequest {

	/** Gibt an, ob das Fach in der Sekundarstufe I unterrichtet werden darf. */
	@Schema(description = "Gibt an, ob das Fach in der Sekundarstufe I unterrichtet werden darf.", example = "true")
	@NotNull(message = "Das Feld 'istSek1' darf nicht null sein.")
	public JsonNullable<Boolean> istSek1 = JsonNullable.undefined();

	/** Gibt an, ob das Fach in der Sekundarstufe II unterrichtet werden darf. */
	@Schema(description = "Gibt an, ob das Fach in der Sekundarstufe II unterrichtet werden darf.", example = "false")
	@NotNull(message = "Das Feld 'istSek2' darf nicht null sein.")
	public JsonNullable<Boolean> istSek2 = JsonNullable.undefined();

	/** Die Bemerkung zum Unterrichtsfach. */
	@Schema(description = "Die Bemerkung zum Unterrichtsfach.", example = "")
	public JsonNullable<String> bemerkung = JsonNullable.undefined();

	/** Das Datum, ab dem die Lehrkraft das Fach unterrichtet. */
	@Schema(description = "Das Datum, ab dem die Lehrkraft das Fach unterrichtet.", example = "2025-08-01")
	public JsonNullable<String> gueltigVon = JsonNullable.undefined();

	/** Das Datum, bis zu dem die Lehrkraft das Fach unterrichtet. */
	@Schema(description = "Das Datum, bis zu dem die Lehrkraft das Fach unterrichtet.", example = "2026-07-31")
	public JsonNullable<String> gueltigBis = JsonNullable.undefined();

}
