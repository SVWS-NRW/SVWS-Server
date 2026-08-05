package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Daten zur regionalen Herkunft der Schüler nach dem Wohnort in der (Teil-) Klasse (X94).
 */
@XmlRootElement
@Schema(description = "Die Daten zur regionalen Herkunft der Schüler nach dem Wohnort in der (Teil-) Klasse (X94).")
@TranspilerDTO
public class KlassenWohnorteStatistikExport {

	/** Satzschlüssel: Der Jahrgang. */
	@Schema(description = "satzschlüssel: der Jahrgang.", example = "05")
	public @NotNull String jahrgang = "";

	/** Satzschlüssel: 1. Stelle Parallelität / Das Bildungsgangkennzeichen. */
	@Schema(description = "satzschlüssel: die 1. Stelle Parallelität / das Bildungsgangkennzeichen.", example = "A")
	public @NotNull String bildungsgangkennzeichen = "";

	/** Satzschlüssel: 2. Stelle Parallelität. */
	@Schema(description = "satzschlüssel: die 2. Stelle Parallelität.", example = "A")
	public @NotNull String parallelitaet2 = "";

	/** Satzschlüssel: Die Teilklasse. */
	@Schema(description = "satzschlüssel: die Teilklasse.", example = "02")
	public @NotNull String teilklasse = "";

	/** Satzschlüssel: Die Postleitzahl. */
	@Schema(description = "Ssatzschlüssel: die Postleitzahl", example = "41836")
	public @NotNull String postleitzahl = "";

	/** Satzschlüssel: Der amtliche Gemeindeschlüssel. */
	@Schema(description = "satzschlüssel: der amtliche Gemeindeschlüssel", example = "05110000")
	public @NotNull String gemeindeschluessel = "";

	/** Die Schüler des Wohnortsatzes insgesamt. */
	@Schema(description = "die Schüler des Wohnortsatzes insgesamt.", example = "12")
	public int schuelerInsgesamt = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KlassenWohnorteStatistikExport() {
		// leer
	}

}
