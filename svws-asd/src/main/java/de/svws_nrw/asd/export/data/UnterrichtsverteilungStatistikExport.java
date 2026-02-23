package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Unterrichtsverteilungsdaten (U71)
 */
@XmlRootElement
@Schema(description = "Die Unterrichtsverteilungsdaten (U71)")
@TranspilerDTO
public class UnterrichtsverteilungStatistikExport {

	//**** Nicht Abschnittsbezogene Daten

	/** Satzschlüssel: Die Unterrichtseinheitennummer. */
	@Schema(description = "satzschlüssel: die Unterrichtseinheitennummer", example = "0134", accessMode = Schema.AccessMode.READ_ONLY)
	public String unterrichtseinheitennummer = "";

	/** Satzschlüssel: Die Kopplungsnummer. */
	@Schema(description = "satzschlüssel: die Kopplungsnummer", example = "000", accessMode = Schema.AccessMode.READ_ONLY)
	public String kopplungsnummer = "";

	/** Das Folgezeilenmerkmal. */
	@Schema(description = "das Folgezeilenmerkmal", example = "1")
	public @NotNull String folgezeilenmerkmal = "";

	/** Der Jahrgang. */
	@Schema(description = "der Jahrgang", example = "05")
	public @NotNull String jahrgang = "";

	/** 1. Stelle Parallelität / Das Bildungsgangkennzeichen. */
	@Schema(description = "die 1. Stelle Parallelität / das Bildungsgangkennzeichen", example = "A")
	public @NotNull String bildungsgangkennzeichen = "";

	/** 2. Stelle Parallelität. */
	@Schema(description = "die 2. Stelle Parallelität", example = "A")
	public @NotNull String parallelitaet2 = "";

	/** Die Teilklasse. */
	@Schema(description = "die Teilklasse", example = "A")
	public @NotNull String teilklasse = "";

	/** Die schulinterne Bezeichnung. */
	@Schema(description = "die schulinterne Bezeichnung", example = "11H12G")
	public @NotNull String schulinterneBezeichnung = "";

	/** Die Schulgliederung der Klasse / Gruppe. */
	@Schema(description = "die Schulgliederung der Klasse / Gruppe", example = "H")
	public @NotNull String schulgliederung = "";

	/** Die Art der Gruppe. */
	@Schema(description = "die Art der Gruppe", example = "10")
	public @NotNull String artDerGruppe = "";

	/** Die Wochenstunden. */
	@Schema(description = "die Wochenstunden", example = "1.0")
	public @NotNull double wochenstunden = 0.0;

	/** Das Fach. */
	@Schema(description = "das Fach", example = "GE")
	public @NotNull String fach = "";

	/** Das Kürzel des Lehrers. */
	@Schema(description = "das Kürzel des Lehrers", example = "MUS")
	public @NotNull String kuerzel = "";

	/** Die teilnehmenden Schüler insgesamt. */
	@Schema(description = "die teilnehmenden Schüler insgesamt", example = "12")
	public int schuelerInsgesamt = 0;

	/** Die teilnehmenden Schüler weiblich. */
	@Schema(description = "die teilnehmenden Schüler weiblich", example = "11")
	public int schuelerWeiblich = 0;

	/** Die Unterrichtssprache bei bilingualem Unterricht. */
	@Schema(description = "die Unterrichtssprache bei bilingualem Unterricht", example = "F")
	public @NotNull String bilingualSprache = "";

	/** Schüler von anderer Schule. */
	@Schema(description = "schüler von anderer Schule", example = "true")
	public @NotNull boolean fremdschueler = false;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UnterrichtsverteilungStatistikExport() {
		// leer
	}

}
