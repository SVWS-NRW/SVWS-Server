package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Daten zur schulischen Herkunft der (Teil-) Klasse (K82).
 */
@XmlRootElement
@Schema(description = "Die Daten zur schulischen Herkunft der (Teil-) Klasse (K82).")
@TranspilerDTO
public class KlassenHerkunftStatistikExport {

	/** Satzschlüssel: Die Herkunftsschulnummer. */
	@Schema(description = "satzschlüssel: die Herkunftsschulnummer", example = "135443")
	public @NotNull String herkunftsSchulNr = "";

	/** Satzschlüssel: Die Herkunftsschulform. */
	@Schema(description = "satzschlüssel: die Herkunftsschulform", example = "GY")
	public @NotNull String herkunftsschulform = "";

	/** Satzschlüssel: Die Herkunftsart. */
	@Schema(description = "satzschlüssel: die Herkunftsart", example = "11")
	public @NotNull String herkunftsart = "";

	/** Satzschlüssel: Die Grundschulempfehlung. */
	@Schema(description = "satzschlüssel: die Grundschulempfehlung", example = "10XX")
	public @NotNull String kuerzelGrundschuleUebergangsempfehlung = "";

	/** Die Schüler des Herkunftsatzes insgesamt. */
	@Schema(description = "die Schüler des Herkunftsatzes insgesamt.", example = "35")
	public int schuelerInsgesamt = 0;

	/** Die Schüler des Herkunftsatzes weiblich. */
	@Schema(description = "die Schüler des Herkunftsatzes weiblich.", example = "32")
	public int schuelerWeiblich = 0;

	/** Die ausländischen Schüler des Herkunftsatzes zusammen. */
	@Schema(description = "die ausländischen Schüler des Herkunftsatzes zusammen.", example = "29")
	public int schuelerAuslaendischZusammen = 0;

	/** Die ausländischen Schüler des Herkunftsatzes weiblich. */
	@Schema(description = "die ausländischen Schüler des Herkunftsatzes weiblich.", example = "27")
	public int schuelerAuslaendischWeiblich = 0;

	/** Die Anrechnungen des Herkunftssatzes (B-Schulen K86). */
	@Schema(description = "Die Anrechnungen des Herkunftssatzes (B-Schulen K86)")
	public @NotNull KlassenHerkunftAnrechungenStatistikExport klassenHerkunftAnrechungenStatistikExport = new KlassenHerkunftAnrechungenStatistikExport();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KlassenHerkunftStatistikExport() {
	}

}
