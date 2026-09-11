package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.types.Wochentag;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LERNGRUPPE_A_LIEGT_AM_WOCHENTAG_B_STUNDE_C_WOCHENTYP_D}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel17 implements UvAlgorithmusDynDatenRegel {

	private final @NotNull int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe;
	private final int prioritaet;

	/** Der Wochentag dieser Regel (des Zeitslots). */
	final int wochentag;

	/** Die Stunde am Wochentag dieser Regel (des Zeitslots). */
	final int stunde;

	/** Der Wochentyp dieser Regel (des Zeitslots). */
	final int wochentyp;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus        Das globale Malus-Array.
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param wochentag    Der {@link Wochentag}.
	 * @param stunde       Die Stunde am Tag.
	 * @param wochentyp    Der Wochentyp (0 = Jeder Woche, 1 = A-Woche, 2 = B-Woche, ...)
	 * @param prioritaet   Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel17(
			final @NotNull int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe,
			final @NotNull int wochentag,
			final @NotNull int stunde,
			final @NotNull int wochentyp,
			final int prioritaet) {

		this.malus = malus;
		this.lerngruppe = lerngruppe;
		this.wochentag = wochentag;
		this.stunde = stunde;
		this.wochentyp = wochentyp;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		if (prioritaet <= 0) {
			return;
		}
		for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : lerngruppe.lehrkraefteZugeordnetAlle) {
			final int genau0 = lehrkraft.istWochentagStundeWochentypCounter[wochentag][stunde][0];

			// Bestimme meine Kollision abhängig davon, ob ich Wochentyp=0 oder Wochentyp>0 bin.
			boolean kollision = false;
			if (wochentyp == 0) {
				// Beispiele: [2*, 0, 0] oder [1*, 1, 0] oder [1*, 0, 1]
				int groesser0 = 0;
				for (int i = 1; i < lehrkraft.istWochentagStundeWochentypCounter[wochentag][stunde].length; i++) {
					groesser0 += lehrkraft.istWochentagStundeWochentypCounter[wochentag][stunde][i];
				}
				kollision = (genau0 + groesser0) > 1;
			} else {
				// Beispiele: [0, 2*, 0] oder [0, 0, 2*] oder [1, 1*, 0] oder [1, 0, 1*]
				kollision = (genau0 + lehrkraft.istWochentagStundeWochentypCounter[wochentag][stunde][wochentyp]) > 1;
			}

			// Gabe es eine Kollision, die ich verursacht habe?
			if (kollision) {
				malus[prioritaet] += faktor;
			}
		}
	}

}
