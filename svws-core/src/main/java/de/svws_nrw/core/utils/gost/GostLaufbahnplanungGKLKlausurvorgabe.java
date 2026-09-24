package de.svws_nrw.core.utils.gost;

import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;

/**
 * Enthält Informationen zu einer Klausurvorgabe, wenn diese für GKLs genutzt wird
 */
public class GostLaufbahnplanungGKLKlausurvorgabe {

	private final @NotNull GostFach fach;
	private final @NotNull GostHalbjahr halbjahr;
	private final @NotNull GostKlausurvorgabe vorgabe;


	/**
	 * Erstelt eine neue Instanz mit den übergebenen Informatione zu der GKL-Klausurvorgabe
	 *
	 * @param fach       das Fach für die Klausurvorgabe
	 * @param halbjahr   das Halbjahr für die Klausurvorgabe
	 * @param vorgabe    die Klausurvorgabe
	 */
	public GostLaufbahnplanungGKLKlausurvorgabe(final @NotNull GostFach fach, final @NotNull GostHalbjahr halbjahr, final @NotNull GostKlausurvorgabe vorgabe) {
		this.fach = fach;
		this.halbjahr = halbjahr;
		this.vorgabe = vorgabe;
	}

	/**
	 * Gibt das Fach zu der Klausurvorgabe zurück.
	 *
	 * @return das Fach
	 */
	public @NotNull GostFach getFach() {
		return fach;
	}

	/**
	 * Gibt das Halbjahr zu der Klausurvorgabe zurück.
	 *
	 * @return das Halbjahr
	 */
	public @NotNull GostHalbjahr getHalbjahr() {
		return halbjahr;
	}

	/**
	 * Gibt die Klausurvorgabe zurück.
	 *
	 * @return die Klausurvorgabe
	 */
	public @NotNull GostKlausurvorgabe getVorgabe() {
		return vorgabe;
	}

}
