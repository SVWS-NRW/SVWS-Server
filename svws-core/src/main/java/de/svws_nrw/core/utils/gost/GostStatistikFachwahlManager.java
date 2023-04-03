package de.svws_nrw.core.utils.gost;

import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.data.gost.GostStatistikFachwahlHalbjahr;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klassen bietet Hilfsmethoden für den Zugriff auf Daten des
 * Typs {@link GostStatistikFachwahl}.
 */
public class GostStatistikFachwahlManager {

	/**
	 * Aktualisiert die Informationen zu einer Fachwahl in dem angegebenen Halbjahr anhand der übergebenen Fachwahl
	 *
	 * @param statfw     die Statistik-Informationen zu den Fachwahlen, bei denen die Informationen hinzugefügt werden sollen
	 * @param halbjahr   das Halbjahr, zu der die Fachwahl gehört
	 * @param fachwahl   die Fachwahl
	 */
	public static void setFachwahlHalbjahr(final @NotNull GostStatistikFachwahl statfw, final @NotNull GostHalbjahr halbjahr, final String fachwahl) {
		if (statfw.fachwahlen[halbjahr.id] == null)
			statfw.fachwahlen[halbjahr.id] = new GostStatistikFachwahlHalbjahr();
		if ((fachwahl == null) || !("M".equals(fachwahl) || "S".equals(fachwahl) || "ZK".equals(fachwahl) || "LK".equals(fachwahl)))
			return;
		statfw.fachwahlen[halbjahr.id].wahlenGKMuendlich += "M".equals(fachwahl) ? 1 : 0;
		statfw.fachwahlen[halbjahr.id].wahlenGKSchriftlich += "S".equals(fachwahl) ? 1 : 0;
		statfw.fachwahlen[halbjahr.id].wahlenGK += ("M".equals(fachwahl) || "S".equals(fachwahl)) ? 1 : 0;
		statfw.fachwahlen[halbjahr.id].wahlenZK += "ZK".equals(fachwahl) ? 1 : 0;
		statfw.fachwahlen[halbjahr.id].wahlenLK += "LK".equals(fachwahl) ? 1 : 0;
	}

}
