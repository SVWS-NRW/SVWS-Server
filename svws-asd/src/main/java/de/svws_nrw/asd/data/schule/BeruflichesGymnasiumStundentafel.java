package de.svws_nrw.asd.data.schule;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse beschreibt eine Stundentafel eines Bildungsgangs aus der Prüfungsordnung für das berufliche Gymnasium.
 */
@XmlRootElement
@Schema(description = "Eine Stundentafel des Beruflichen Gymnasiums.")
@TranspilerDTO
public class BeruflichesGymnasiumStundentafel {

	/** Die Variante der Stundentafel bezogen auf einen Bildungsgang */
	@Schema(description = "Variante der Stundentafel bezogen auf einen Bildungsgang", example = "1")
	public int variante;

	/** Die allgemeine Bezeichnung der Abschnitte (z.B. Quartal oder Halbjahr) */
	@Schema(description = "allgemeine Bezeichnung der Abschnitte", example = "Halbjahr")
	public @NotNull List<BeruflichesGymnasiumStundentafelFach> faecher = new ArrayList<>();

	/** Die Wahlmöglichkeiten der Abiturfächer dieser Variante inkl. zukünftigem fünften Fach */
	@Schema(description = "Fachwahlmöglichkeiten im Abitur für diese Variante", example = "")
	public @NotNull List<BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit> wahlmoeglichkeiten = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public BeruflichesGymnasiumStundentafel() {
		// leer
	}

}
