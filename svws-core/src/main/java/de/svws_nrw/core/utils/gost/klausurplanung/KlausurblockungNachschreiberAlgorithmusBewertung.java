package de.svws_nrw.core.utils.gost.klausurplanung;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient zur Speicherung einer Bewertung eines {@link KlausurblockungNachschreiberAlgorithmus}
 *
 *
 * @author Benjamin A. Bartsch
 */
public class KlausurblockungNachschreiberAlgorithmusBewertung {

	/**
	 * Die Anzahl an Terminen, auf die Nachschreib-Klausuren gelegt wurden.
	 */
	int anzahl_termine = 0;

	/**
	 * Die Anzahl an neuen zusätzlichen Terminen, auf die Nachschreib-Klausuren gelegt wurden.
	 */
	int anzahl_zusatztermine = 0;

	/**
	 * Liefert -1, 0 und +1, wenn dieses Objekt besser, gleich und schlechter als das übergebene Objekt ist.
	 *
	 * @param b  Das übergebene Objekt.
	 *
	 * @return -1, 0 und +1, wenn dieses Objekt besser, gleich und schlechter als das übergebene Objekt ist.
	 */
	public int compare(final @NotNull KlausurblockungNachschreiberAlgorithmusBewertung b) {
		if (anzahl_zusatztermine < b.anzahl_zusatztermine) return -1;
		if (anzahl_zusatztermine > b.anzahl_zusatztermine) return +1;

		if (anzahl_termine < b.anzahl_termine) return -1;
		if (anzahl_termine > b.anzahl_termine) return +1;

		return 0;
	}

}
