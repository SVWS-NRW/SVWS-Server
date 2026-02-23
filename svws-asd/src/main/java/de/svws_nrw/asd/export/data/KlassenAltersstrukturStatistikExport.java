package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Daten zur Altersstruktur der Schüler in der (Teil-) Klasse (X95)
 */
@XmlRootElement
@Schema(description = "Die Daten zur Altersstruktur der Schüler in der (Teil-) Klasse (X95).")
@TranspilerDTO
public class KlassenAltersstrukturStatistikExport {

	/** Satzschlüssel: Das Geburtsjahr der Schüler. */
	@Schema(description = "Satzschlüssel: das Geburtsjahr der Schüler", example = "2007")
	public @NotNull String geburtsjahr = "";

	/** Satzschlüssel: Die Nationalität der Schüler. */
	@Schema(description = "Satzschlüssel: die Nationalität der Schüler", example = "141")
	public @NotNull String nationalitaet = "";

	/** Die Schüler des Altersstruktursatzes insgesamt. */
	@Schema(description = "die Schüler des Altersstruktursatzes insgesamt.", example = "12")
	public int schuelerInsgesamt = 0;

	/** Die Schüler des Altersstruktursatzes weiblich. */
	@Schema(description = "die Schüler des Altersstruktursatzes insgesamt.", example = "9")
	public int schuelerWeiblich = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KlassenAltersstrukturStatistikExport() {
	}

}
