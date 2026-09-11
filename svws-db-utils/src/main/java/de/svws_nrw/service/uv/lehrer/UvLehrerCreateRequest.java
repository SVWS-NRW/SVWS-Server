package de.svws_nrw.service.uv.lehrer;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Die Klasse beschreibt das DTO für das Erstellen von Einträgen für die Anrechnungsstunden von Lehrern.
 */
@Schema(description = "die Informationen zu einem Lehrer innerhalb der Unterrichtsverteilung.")
public class UvLehrerCreateRequest {

	/** Die ID des Lehrers als Fremdschlüssel auf die Tabelle K_Lehrer. */
	@Schema(description = "die ID des Lehrers als Fremdschlüssel auf die Tabelle K_Lehrer", example = "102")
	public Long idKLehrer;

	/** Das Lehrer-Kürzel für eine eindeutige Identifikation. */
	@Schema(description = "das Lehrer-Kürzel für eine eindeutige Identifikation", example = "ABC")
	public String kuerzel;

	/** Der Nachname des Lehrers. */
	@Schema(description = "der Nachname des Lehrers", example = "Mustermann")
	public String nachname;

	/** Der Vorname (bzw. Rufname) des Lehrers. */
	@Schema(description = "der Vorname (bzw. Rufname) des Lehrers", example = "Max")
	public String vorname;

}
