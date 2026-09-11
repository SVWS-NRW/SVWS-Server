package de.svws_nrw.service.uv.lehrer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Die Klasse beschreibt das Patch-DTO für die Anrechnungsstunden von Lehrern.
 */
@Schema(description = "Anrechnungsstunden bei Lehrerabschnittsdaten.")
public class UvLehrerAnrechnungsstundenPatchRequest {

	/** Die ID des UvLehrerPflichtstundensoll. */
	@Schema(description = "die ID des UvLehrerAnrechnugnsstunden", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Das Kürzel des Anrechnungsgrundes (z. B. 'AG' für Arbeitsgemeinschaft). */
	@Schema(description = "das Kürzel des Anrechnungsgrundes", example = "AG")
	public JsonNullable<
			@NotNull(message = "Das Kürzel des Anrechnungsgrunds muss gesetzt werden.") String> anrechnungsgrundKrz = JsonNullable.undefined();

	/** Die Anzahl der angerechneten Stunden. */
	@Schema(description = "die Anzahl der angerechneten Stunden", example = "2.5")
	public JsonNullable<
			@NotNull(message = "Die Anzahl der Anrechnungsstunden muss vorhanden sein.")
			@Min(value = 0, message = "Die Anzahl der Stunden darf nicht negativ sein.") Double> anzahlStunden = JsonNullable.undefined();

	/** Das Datum, ab dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).")
	public JsonNullable<
			@NotNull(message = "Der Gültigkeitsbeginn eines Pflichtstundensolls muss gesetzt werden.") String> gueltigVon = JsonNullable.undefined();

	/** Das Datum, bis zu dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).")
	public JsonNullable<String> gueltigBis = JsonNullable.undefined();

}
