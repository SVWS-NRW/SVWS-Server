package de.svws_nrw.core.utils.uvblockung;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LERNGRUPPE_A_BENOETIGT_B_LEHRKRAEFTE}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel28 implements UvAlgorithmusDynDatenRegel {

	private final @NotNull Logger log;
	private final int @NotNull [] malus;
	private final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe;
	private int sollAnzahlLehrkraefte;
	private int prioritaet;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log                     Der Logger für Debug-Zwecke.
	 * @param malus                   Das globale Malus-Array.
	 * @param lerngruppe              Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param sollAnzahlLehrkraefte   Die Anzahl an Lehrkräften, die diese Lerngruppe benötigt.
	 * @param prioritaet              Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel28(
			final @NotNull Logger log,
			final int @NotNull [] malus,
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe,
			final int sollAnzahlLehrkraefte,
			final int prioritaet) {

		this.log = log;
		this.malus = malus;
		this.lerngruppe = lerngruppe;
		this.sollAnzahlLehrkraefte = sollAnzahlLehrkraefte;
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}

	void setzeAuf(final int neuesSoll, final int prioritaet) {
		this.changeMalus(-1);
		this.sollAnzahlLehrkraefte = neuesSoll;
		this.prioritaet = prioritaet;
		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		if ((prioritaet < 0) || (prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			log.logLn(LogLevel.ERROR,
					"UvAlgorithmusDynDatenRegel28: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.".formatted(prioritaet));
			return;
		}
		final int anzahl = lerngruppe.lehrkraefteZugeordnetAlle.size();
		final int abweichung = Math.abs(sollAnzahlLehrkraefte - anzahl);

		if (abweichung > 0) {
			malus[prioritaet] += faktor * abweichung;
		}
	}

}
