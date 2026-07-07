package de.svws_nrw.core.data.gost;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Dieses DTO beschreibt die Informationen zu den Wahlen eines Schülers
 * in seinem Abiturjahrgang in Bezug auf die Gleichwertigen Komplexen Lernleistungen.
 */
@XmlRootElement()
@Schema(description = "Die Fachwahl eines Schüler zu den Gleichwertigen Komplexen Lernleistungen der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostSchuelerGKLWahl {

	/** Die ID des Schülers */
	@Schema(description = "die ID des Schülers", example = "42")
	public long idSchueler = -1;

	/** Die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der EF im Sprachlich-künstlerisch-literarischen Fachbereich gewählt wurde */
	@Schema(description = "die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der EF im Sprachlich-künstlerisch-literarischen Fachbereich gewählt wurde", example = "42")
	public Long idKlausurvorgabeEF_Sprachen = null;

	/** Die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der EF im Gesellschaftswissenschaftlichen Bereich gewählt wurde */
	@Schema(description = "die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der EF im Gesellschaftswissenschaftlichen Bereich gewählt wurde", example = "42")
	public Long idKlausurvorgabeEF_GW = null;

	/** Die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der EF im Mathematisch-Naturwissenschaftlichen Bereich gewählt wurde */
	@Schema(description = "die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der EF im Mathematisch-Naturwissenschaftlichen Bereich gewählt wurde", example = "42")
	public Long idKlausurvorgabeEF_NW = null;

	/** Die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der Q-Phase im Sprachlich-künstlerisch-literarischen Fachbereich gewählt wurde */
	@Schema(description = "die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der Q-Phase im Sprachlich-künstlerisch-literarischen Fachbereich gewählt wurde", example = "42")
	public Long idKlausurvorgabeQ_Sprachen = null;

	/** Die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der Q-Phase im Gesellschaftswissenschaftlichen Bereich gewählt wurde */
	@Schema(description = "die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der Q-Phase im Gesellschaftswissenschaftlichen Bereich gewählt wurde", example = "42")
	public Long idKlausurvorgabeQ_GW = null;

	/** Die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der Q-Phase im Mathematisch-Naturwissenschaftlichen Bereich gewählt wurde */
	@Schema(description = "die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der Q-Phase im Mathematisch-Naturwissenschaftlichen Bereich gewählt wurde", example = "42")
	public Long idKlausurvorgabeQ_NW = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostSchuelerGKLWahl() {
		// leer
	}

}
