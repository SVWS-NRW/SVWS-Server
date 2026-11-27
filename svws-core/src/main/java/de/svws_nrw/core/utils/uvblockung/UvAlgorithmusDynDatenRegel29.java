package de.svws_nrw.core.utils.uvblockung;


import java.util.List;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LERNGRUPPEN_ERHALTEN_SELBE_LEHRKRAFT}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel29 implements UvAlgorithmusDynDatenRegel {

	private final @NotNull Logger log;
	private final int @NotNull [] malus;
	private final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppen;
	private final int prioritaet;
	private final int @NotNull [] lehrkraftCounter;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param log            Der Logger für Debug-Zwecke.
	 * @param malus          Das globale Malus-Array.
	 * @param lerngruppen    Die Menge der {@link UvAlgorithmusDynDatenLerngruppe}n, welche dieselbe Lehrkraft haben sollen.
	 * @param nLehrkraefte   Die Anzahl aller Lehrkräfte.
	 * @param prioritaet     Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel29(
			final @NotNull Logger log,
			final int @NotNull [] malus,
			final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppen,
			final int nLehrkraefte,
			final int prioritaet) {

		this.log = log;
		this.malus = malus;
		this.lerngruppen = lerngruppen;
		this.lehrkraftCounter = new int[nLehrkraefte];
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		if ((prioritaet < 0) || (prioritaet >= UvBlockungRegelPrioritaet.DEAKTIVIERT.nr)) {
			log.logLn(LogLevel.ERROR,
					"UvAlgorithmusDynDatenRegel29: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.".formatted(prioritaet));
			return;
		}
		final int verschieden = zaehleVerschiedeneLehrkraefte();
		setzeLehrkraftCounterZurueck();
		// Malus entsteht, wenn mehr als eine verschiedene Lehrkraft in der Menge unterrichtet.
		if (verschieden > 1) {
			malus[prioritaet] += faktor * (verschieden - 1);
		}
	}


	/**
	 * Zählt die verschiedenen Lehrkräfte, die in den Lerngruppen unterrichten.
	 *
	 * @return die Anzahl verschiedener Lehrkräfte.
	 */
	private int zaehleVerschiedeneLehrkraefte() {
		int verschieden = 0;
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lerngruppen) {
			if (lerngruppe.lehrkraefteZugeordnetAlle.isEmpty()) {
				continue;
			}
			boolean erstesVorkommen = false;
			for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : lerngruppe.lehrkraefteZugeordnetAlle) {
				if (lehrkraftCounter[lehrkraft.interneID] == 0) {
					erstesVorkommen = true;
				}
				lehrkraftCounter[lehrkraft.interneID]++;
			}
			if (erstesVorkommen) {
				verschieden++;
			}
		}
		return verschieden;
	}


	/**
	 * Setzt den Lehrkraft-Zähler zurück.
	 */
	private void setzeLehrkraftCounterZurueck() {
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lerngruppen) {
			for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : lerngruppe.lehrkraefteZugeordnetAlle) {
				lehrkraftCounter[lehrkraft.interneID] = 0;
			}
		}
	}

}
