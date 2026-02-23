package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Daten zur Betreuung der (Teil-) Klasse (K87).
 */
@XmlRootElement
@Schema(description = "Die Daten zur Betreuung der (Teil-) Klasse (K87).")
@TranspilerDTO
public class KlassenBetreuungStatistikExport {

	/** Die Art der Betreuung */
	@Schema(description = "die Art der Betreuung", example = "4")
	public String betreuungsart = "";

	/** Die Schüler Insgesamt. */
	@Schema(description = "die Schüler Insgesamt.", example = "14")
	public int insgesamtZusammen = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KlassenBetreuungStatistikExport() {
	}

}
