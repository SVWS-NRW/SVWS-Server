package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Daten zur regionalen Lage des Ausbildungsortes der Schüler in der (Teil-) Klasse (X96).
 */
@XmlRootElement
@Schema(description = "die Daten zur regionalen Lage des Ausbildungsortes der Schüler in der (Teil-) Klasse (X96).")
@TranspilerDTO
public class KlassenAusbildungsorteStatistikExport {

	/** Satzschlüssel: Der amtliche Gemeindeschlüssel. */
	@Schema(description = "Satzschlüssel: der amtliche Gemeindeschlüssel", example = "05110000")
	public @NotNull String gemeindeschluessel = "";

	/** Die Schüler des Ausbildungsortsatzes insgesamt. */
	@Schema(description = "die Schüler des Ausbildungsortsatzes insgesamt.", example = "12")
	public int schuelerInsgesamt = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KlassenAusbildungsorteStatistikExport() {
	}

}
