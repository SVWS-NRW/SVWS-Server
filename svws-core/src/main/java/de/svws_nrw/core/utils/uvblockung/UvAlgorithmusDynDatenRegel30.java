package de.svws_nrw.core.utils.uvblockung;


import java.util.List;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LERNGRUPPEN_ERHALTEN_VERSCHIEDENE_LEHRKRAEFTE}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel30 implements UvAlgorithmusDynDatenRegel {

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
	 * @param lerngruppen    Die Menge der {@link UvAlgorithmusDynDatenLerngruppe}n, welche verschiedene Lehrkräfte haben sollen.
	 * @param nLehrkraefte   Die Anzahl aller Lehrkräfte.
	 * @param prioritaet     Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel30(
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
					"UvAlgorithmusDynDatenRegel30: Ungültige oder deaktivierte Priorität %d - Malus-Änderung wird ignoriert.".formatted(prioritaet));
			return;
		}
		// A, _, _       --> Malus 0
		// A, A, _       --> Malus 1
		// A, A*, B      --> Malus 1
		// A, A*, B, B*  --> Malus 2
		// A, B, C       --> Malus 0
		// (A,B), (A,B)* --> Malus 1

		// Anzahl verschiedener Lehrkräfte berechnen; leere Lehrkraft-Mengen zählen als "verschieden" (Joker).
		int verschieden = 0;
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lerngruppen) {
			// Lerngruppe ohne Lehrkraft zählt als "verschieden".
			if (lerngruppe.lehrkraefteZugeordnetAlle.isEmpty()) {
				verschieden++;
				continue;
			}

			// Überprüfe, ob es alle Lehrkräfte der Lerngruppe bereits gab.
			boolean erstesVorkommen = false;
			for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : lerngruppe.lehrkraefteZugeordnetAlle) {
				if (lehrkraftCounter[lehrkraft.interneID] == 0) {
					erstesVorkommen = true;
				}
				lehrkraftCounter[lehrkraft.interneID]++;
			}

			// Lehrkraft neu?
			if (erstesVorkommen) {
				verschieden++;
			}
		}

		// "lehrkraftCounter" auf 0 setzen.
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lerngruppen) {
			for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : lerngruppe.lehrkraefteZugeordnetAlle) {
				lehrkraftCounter[lehrkraft.interneID] = 0;
			}

		}

		// Malus entsteht, wenn nicht alle verschieden sind, wobei Lerngruppen ohne Lehrkraft einen "Joker" darstellen.
			malus[prioritaet] += faktor * (lerngruppen.size() - verschieden);
	}

}
