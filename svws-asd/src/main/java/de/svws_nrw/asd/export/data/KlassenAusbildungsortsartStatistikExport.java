package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Daten der Ausbildungsorte der (Teil-) Klasse (K85).
 */
@XmlRootElement
@Schema(description = "Die Daten der Ausbildungsorte der (Teil-) Klasse (K85).")
@TranspilerDTO
public class KlassenAusbildungsortsartStatistikExport {

	/** Ausbildungsort Betrieb. */
	@Schema(description = "Ausbildungsort Betrieb.", example = "29")
	public int ausbildungsortBetrieb = 0;

	/** Ausbildungsort Träger. */
	@Schema(description = "Ausbildungsort Träger.", example = "27")
	public int ausbildungsortTraeger = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KlassenAusbildungsortsartStatistikExport() {
		// leer
	}

}
