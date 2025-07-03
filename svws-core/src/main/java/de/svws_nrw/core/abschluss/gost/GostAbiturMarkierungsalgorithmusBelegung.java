package de.svws_nrw.core.abschluss.gost;

import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;

/**
 * Eine Klasse für die interne Nutzung in der Klasse {@link GostAbiturMarkierungsalgorithmus},
 * dem Abiturmarkierungsergebnis für die gymnasialen Oberstufe.
 * Sie enthält die Informationen zu einer bewerteten und anrechenbaren Halbjahresbelegung.
 */
class GostAbiturMarkierungsalgorithmusBelegung {

	/** Die Fachbelegegung */
	public final @NotNull AbiturFachbelegung belegung;

	/** Die Halbjahresbelegung */
	public final @NotNull AbiturFachbelegungHalbjahr belegungHalbjahr;

	/** Das Halbjahr der Halbjahrebelegung */
	public final @NotNull GostHalbjahr halbjahr;

	/** Die Notenpunkte der Halbjahresbelegung */
	public final int notenpunkte;


	/**
	 * Initialisiert das Objekt mit einer ggf. noch anrechenbaren und bewerteten Halbjahresbelegung
	 *
	 * @param belegung
	 * @param belegungHalbjahr
	 * @param notenpunkte
	 */
	GostAbiturMarkierungsalgorithmusBelegung(final @NotNull AbiturFachbelegung belegung, final @NotNull AbiturFachbelegungHalbjahr belegungHalbjahr,
			final int notenpunkte) {
		this.belegung = belegung;
		this.belegungHalbjahr = belegungHalbjahr;
		final GostHalbjahr tmpHalbjahr = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
		this.halbjahr = (tmpHalbjahr == null) ? GostHalbjahr.EF1 : tmpHalbjahr;
		this.notenpunkte = notenpunkte;
	}

}
