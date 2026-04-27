package de.svws_nrw.service.bk;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import jakarta.validation.constraints.NotNull;

public final class BKGymUtils {

	private BKGymUtils() {
		/* Diese Klasse soll nicht instanziiert werden */
	}


	/**
	 * Gibt die Schriftlichkeit entsprechend der APO-BK Anlage D zurück.
	 * Die Kursart LK ist immer schriftlich, die Kursart GK ist nur schriftlich, wenn es sich um einen Kurs der Kursarten GKS, AB3 oder AB4 handelt.
	 * Bei der Kursart AB4 ist zusätzlich zu beachten, dass diese nur in den Halbjahren Q11, Q12 und Q21 schriftlich ist, nicht aber im Halbjahr Q22.
	 *
	 * @param kursart       die Kursart des Kurses
	 * @param kursartAllg   die allgemeine Kursart des Kurses (z.B. GKS, AB3, AB4)
	 * @param halbjahr      das Halbjahr, in dem der Kurs belegt wurde
	 *
	 * @return true, wenn der Kurs schriftlich ist, sonst false
	 */
	public static boolean istSchriftlich(final @NotNull GostKursart kursart, final String kursartAllg, final @NotNull GostHalbjahr halbjahr) {
		return (kursart == GostKursart.LK)
					|| ((kursart == GostKursart.GK) && (("GKS".equals(kursartAllg))
							|| ("AB3".equals(kursartAllg))
							|| ("AB4".equals(kursartAllg) && (halbjahr != GostHalbjahr.Q22))));
	}


	/**
	 * Gibt die Sprache als einstelliges Kürzel zurück, sofern der Kurs bilingual unterrichtet wurde.
	 * Sollte die Unterrichtssprache nicht angegeben sein oder es sich um Deutsch handeln, so wird null zurückgegeben.
	 *
	 * @param unterrichtssprache   die Unterrichtssprache des Kurses
	 *
	 * @return die Sprache als einstelliges Kürzel oder null
	 */
	public static String getBilingualeSprache(final String unterrichtssprache) {
		return ((unterrichtssprache != null) && (!"".equals(unterrichtssprache)) && (!"D".equals(unterrichtssprache)))
				? unterrichtssprache.substring(0, 1) : null;
	}
}
