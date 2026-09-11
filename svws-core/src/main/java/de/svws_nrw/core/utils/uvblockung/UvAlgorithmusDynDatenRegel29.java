package de.svws_nrw.core.utils.uvblockung;


import java.util.List;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Implementiert die Regel {@link UvBlockungRegelTyp#LERNGRUPPEN_ERHALTEN_SELBE_LEHRKRAFT}.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenRegel29 implements UvAlgorithmusDynDatenRegel {

	private final @NotNull int @NotNull [] malus;
	private final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppen;
	private final int prioritaet;
	private final @NotNull int @NotNull [] lehrkraftCounter;


	/**
	 * Erzeugt eine neue Instanz der Regel und passt den Anfangs-Malus an.
	 *
	 * @param malus          Das globale Malus-Array.
	 * @param lerngruppen    Die Menge der {@link UvAlgorithmusDynDatenLerngruppe}n, welche die selbe Lehrkraft haben sollen.
	 * @param nLehrkraefte   Die Anzahl aller Lehrkräfte.
	 * @param prioritaet     Die Priorität der Regel.
	 */
	public UvAlgorithmusDynDatenRegel29(
			final @NotNull int @NotNull [] malus,
			final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppen,
			final int nLehrkraefte,
			final int prioritaet) {

		this.malus = malus;
		this.lerngruppen = lerngruppen;
		this.lehrkraftCounter = new int[nLehrkraefte];
		this.prioritaet = prioritaet;

		this.changeMalus(1);
	}


	@Override
	public void changeMalus(final int faktor) {
		// A, _, _      --> Malus 0
		// A, A, _      --> Malus 0
		// A, A, B      --> Malus 1
		// A, A, B, B   --> Malus 1
		// A, B, C      --> Malus 2
		// (A,B), (A,B) --> Malus 0 (da die Mengen übereinstimmen)

		// Anzahl verschiedener Lehrkräfte berechnen, dabei leere Lehrkraft-Mengen ignorieren.
		int verschieden = 0;
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lerngruppen) {
			// Überspringe Lerngruppe ohne Lehrkraft.
			if (lerngruppe.lehrkraefteZugeordnetAlle.isEmpty()) {
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

		// Malus entsteht, wenn mehr als eine verschiedene Lehrkraft in der Menge unterrichtet.
		if (verschieden > 1) {
			malus[prioritaet] += faktor * (verschieden - 1);
		}
	}

}
