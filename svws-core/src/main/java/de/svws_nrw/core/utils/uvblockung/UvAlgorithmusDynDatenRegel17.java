package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.Wochentag;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LERNGRUPPE_A_LIEGT_AM_WOCHENTAG_B_STUNDE_C_WOCHENTYP_D}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel17 implements UvAlgorithmusDynDatenRegel {

	private final @NotNull Logger log;
	private final int @NotNull [] malus;
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
	 * @param log             Der Logger für Debug-Zwecke.
	 * @param malus           Das globale Malus-Array.
	 * @param lerngruppe      Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param wochentag       Der {@link Wochentag}.
	 * @param stunde          Die Stunde am Tag.
	 * @param wochentypWert   Der Wochentyp (0 = jede Woche, 1 = A-Woche, 2 = B-Woche, ...).
	 * @param prioritaet      Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel17(
			final @NotNull Logger log,
			final int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe,
			final int wochentag,
			final int stunde,
			final int wochentypWert,
			final int prioritaet) {

		this.log = log;
		this.malus = malus;
		this.lerngruppe = lerngruppe;
		this.wochentag = wochentag;
		this.stunde = stunde;
		this.wochentyp = wochentypWert;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		if ((prioritaet < 0) || (prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			log.logLn(LogLevel.ERROR,
					"UvAlgorithmusDynDatenRegel17: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.".formatted(prioritaet));
			return;
		}

		int anzahlZuviel = 0;
		for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : lerngruppe.lehrkraefteZugeordnetAlle) {
			final int anzahl = (wochentyp == 0)
					? gibMinimumUeberAlleWochentypen(lehrkraft)
					: lehrkraft.stundenplanAnzahl[wochentag][stunde][wochentyp];
			if (anzahl > 1) {
				anzahlZuviel += anzahl - 1;
			}
		}

		if (anzahlZuviel > 0) {
			malus[prioritaet] += faktor * anzahlZuviel;
		}
	}


	/**
	 * Liefert das Minimum des Stundenplans der Lehrkraft im aktuellen Zeitslot über alle Wochentypen > 0.
	 * <br>(Im typischen Fall entspricht das der Anzahl der Lerngruppen, die jede Woche stattfinden.)
	 *
	 * @param lehrkraft   Die Lehrkraft.
	 *
	 * @return das Minimum des Stundenplans der Lehrkraft im aktuellen Zeitslot über alle Wochentypen > 0.
	 */
	private int gibMinimumUeberAlleWochentypen(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		int minimum = Integer.MAX_VALUE;
		for (int wt = 1; wt < lehrkraft.stundenplanAnzahl[wochentag][stunde].length; wt++) {
			minimum = Math.min(minimum, lehrkraft.stundenplanAnzahl[wochentag][stunde][wt]);
		}
		return (minimum == Integer.MAX_VALUE) ? 0 : minimum;
	}

}
