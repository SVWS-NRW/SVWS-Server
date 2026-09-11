package de.svws_nrw.service.uv.lerngruppen;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Die Klasse beschreibt das Patch-DTO für eine UV-Lerngruppe.
 */
@Schema(description = "Patch-Daten für eine UV-Lerngruppe.")
public class UvLerngruppePatchRequest {

	/** Die ID der Lerngruppe. */
	@Schema(description = "die ID der Lerngruppe", example = "102")
	@NotNull(message = "Die ID des zu patchenden Objekts muss vorhanden sein.")
	public Long id;

	/** Die ID der Klasse. */
	@Schema(description = "die ID der Klasse.")
	public JsonNullable<Long> idKlasse = JsonNullable.undefined();

	/** Die ID des Fachs. */
	@Schema(description = "die ID des Fachs.")
	public JsonNullable<Long> idFach = JsonNullable.undefined();

	/** Die ID des Kurses. */
	@Schema(description = "die ID des Kurses.")
	public JsonNullable<Long> idKurs = JsonNullable.undefined();

	/** Die Wochenstunden. */
	@Schema(description = "die Wochenstunden.")
	public JsonNullable<@NotNull(message = "Die Wochenstunden müssen gesetzt werden.") Double> wochenstunden = JsonNullable.undefined();

	/** Die unterrichteten Wochenstunden. */
	@Schema(description = "die unterrichteten Wochenstunden.")
	public JsonNullable<
			@NotNull(message = "Die unterrichteten Wochenstunden müssen gesetzt werden.") Double> wochenstundenUnterrichtet = JsonNullable.undefined();

	/** Die Kooperationsschulnummer. */
	@Schema(description = "die Kooperationsschulnummer.")
	public JsonNullable<
			@Size(max = 6, message = "Die Kooperationsschulnummer darf maximal 6 Zeichen lang sein.") String> koopSchulNr = JsonNullable.undefined();

	/** Die Anzahl externer Schülerinnen und Schüler. */
	@Schema(description = "die Anzahl externer Schülerinnen und Schüler.")
	public JsonNullable<
			@NotNull(message = "Die Anzahl externer Schülerinnen und Schüler muss gesetzt werden.") Integer> koopAnzahlExterne = JsonNullable.undefined();

}
