package de.svws_nrw.service.kurse;

import org.openapitools.jackson.nullable.JsonNullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Die Daten zum Ändern einer Zusatzkraft bei einem Kurs. Nur gesetzte Felder werden geändert. Die IDs des Kurses
 * und des Lehrers bilden die ID der Zusatzkraft und sind nicht änderbar.
 */
// Unbekannte Felder (z. B. idKurs) werden ignoriert, damit bisherige Clients weiterhin funktionieren.
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Die Patch-Daten für eine Zusatzkraft bei einem Kurs.")
public final class KurslehrerPatchRequest {

	/** Die Wochenstunden des Lehrers im Kurs */
	@Schema(description = "die Wochenstunden des Lehrers im Kurs", example = "3.0")
	public JsonNullable<@NotNull @PositiveOrZero Double> wochenstundenLehrer = JsonNullable.undefined();

}
