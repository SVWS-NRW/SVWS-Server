package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer UV-Lerngruppe.
 */
@TranspilerDTO
@Schema(description = "die Informationen zum Erstellen einer UV-Lerngruppe.")
public class UvLerngruppeCreateRequest {

	/** Die negative temporäre ID beim Sammelimport. */
	public @NotNull Long id = 0L;

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts.")
	@NotNull(message = "Die ID des Planungsabschnitts muss gesetzt werden.")
	public Long idPlanungsabschnitt = 0L;

	/** Die ID der Klasse. */
	@Schema(description = "die ID der Klasse.")
	public Long idKlasse;

	/** Die ID des Fachs. */
	@Schema(description = "die ID des Fachs.")
	public Long idFach;

	/** Die ID des Kurses. */
	@Schema(description = "die ID des Kurses.")
	public Long idKurs;

	/** Die Wochenstunden. */
	@Schema(description = "die Wochenstunden.")
	@NotNull(message = "Die Wochenstunden müssen gesetzt werden.")
	public Double wochenstunden = 0.0;

	/** Die unterrichteten Wochenstunden. */
	@Schema(description = "die unterrichteten Wochenstunden.")
	@NotNull(message = "Die unterrichteten Wochenstunden müssen gesetzt werden.")
	public Double wochenstundenUnterrichtet = 0.0;

	/** Die Kooperationsschulnummer. */
	@Schema(description = "die Kooperationsschulnummer.")
	@Size(max = 6, message = "Die Kooperationsschulnummer darf maximal 6 Zeichen lang sein.")
	public String koopSchulNr;

	/** Die Anzahl externer Schülerinnen und Schüler. */
	@Schema(description = "die Anzahl externer Schülerinnen und Schüler.")
	public Integer koopAnzahlExterne = 0;

}
