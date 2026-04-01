package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Daten zu den Staatsangehörigkeiten der (Teil-) Klasse (K83).
 */
@XmlRootElement
@Schema(description = "Die Daten zu den Staatsangehörigkeiten der (Teil-) Klasse (K83).")
@TranspilerDTO
public class KlassenNationalitaetenStatistikExport {

	/** Satzschlüssel: Die Nationalität. */
	@Schema(description = "satzschlüssel: die Nationalitaet", example = "141")
	public @NotNull String nationalitaet = "";

	/** Die Schüler der Nationalität insgesamt. */
	@Schema(description = "die Schüler der Nationalität insgesamt.", example = "5")
	public int insgesamtZusammen = 0;

	/** Die Schüler der Nationalität insgesamt weiblich. */
	@Schema(description = "die Schüler der Nationalität insgesamt weiblich.", example = "3")
	public int insgesamtWeiblich = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KlassenNationalitaetenStatistikExport() {
		// leer
	}

}
