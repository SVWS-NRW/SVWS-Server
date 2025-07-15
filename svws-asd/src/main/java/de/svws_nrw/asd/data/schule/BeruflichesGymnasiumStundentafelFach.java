package de.svws_nrw.asd.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse beschreibt ein Fach der Stundentafel eines Bildungsgangs aus der Prüfungsordnung
 * für das berufliche Gymnasium.
 */
@XmlRootElement
@Schema(description = "Eine Fach der Stundentafel des Beruflichen Gymnasiums.")
@TranspilerDTO
public class BeruflichesGymnasiumStundentafelFach {

	/** Die Bezeichnung des Fachs laut Prüfungsordnung */
	@Schema(description = "Die Bezeichnung des Fachs laut Prüfungsordnung", example = "Physik")
	public @NotNull String fachbezeichnung = "";

	/** Die Kursart des Fachs */
	@Schema(description = "die Kursart des Fachs", example = "LK1")
	public @NotNull String kursart = "";

	/** Der Stundenumfang für alle sechs Halbjahre EF.1, EF.2, Q1.1, Q1.2, Q2.1, Q2.2 */
	@Schema(description = "der Stundenumfang für jedes Halbjahr", example = "[2,2,3,3,3,3]")
	public @NotNull int[] stundenumfang = new int[6];

	/** Der Zeugnisbereich des Fachs */
	@Schema(description = "der Zeugnisbereich des Fachsd", example = "Berufsbezogener Lernbereich")
	public @NotNull String zeugnisbereich = "";

	/** Die Position des Fachs in der Stundentafel */
	@Schema(description = "die Position des Fachs in der Stundentafel", example = "1")
	public int sortierung;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public BeruflichesGymnasiumStundentafelFach() {
		// leer
	}

}
