package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Schülersummen zur Adresse (K88)
 */
@XmlRootElement
@Schema(description = "Die Schülersummen zur Adresse (K88)")
@TranspilerDTO
public class SchuleAdressenSchuelerStatistikExport {

	/** Schüler Insgesamt Zusammen . */
	@Schema(description = "schüler Insgesamt Zusammen", example = "341")
	public int insgesamtZusammen = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuleAdressenSchuelerStatistikExport() {
	}

}
