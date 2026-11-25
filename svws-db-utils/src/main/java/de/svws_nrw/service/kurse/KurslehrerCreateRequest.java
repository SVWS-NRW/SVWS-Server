package de.svws_nrw.service.kurse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Die Daten zum Anlegen einer Zusatzkraft bei einem Kurs. Die ID des Kurses ergibt sich aus dem Pfad der Anfrage
 * und wird daher nicht aus den Daten übernommen.
 */
// Unbekannte Felder (z. B. idKurs) werden ignoriert, damit bisherige Clients weiterhin funktionieren.
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Die Daten zum Anlegen einer Zusatzkraft bei einem Kurs.")
public final class KurslehrerCreateRequest {

	/** Die ID des Lehrers */
	@Schema(description = "die ID des Lehrers", example = "17")
	@NotNull
	public Long idLehrer;

	/** Die Wochenstunden des Lehrers im Kurs */
	@Schema(description = "die Wochenstunden des Lehrers im Kurs", example = "3.0")
	@PositiveOrZero
	public Double wochenstundenLehrer;

}
