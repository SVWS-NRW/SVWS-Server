package de.svws_nrw.core.data.uv.regel;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert die möglichen Regel-Prioritäten einer {@link UvBlockungRegel}.
 * <br>Hinweis: Die Indizes müssen bei 0 beginnen und "deaktiviert" muss die letzte Regel sein.
*/
public enum UvBlockungRegelPrioritaet {

	/** Sehr hohe Priorität. */
	SEHR_HOCH(0, "sehr hoch"),

	/** Hohe Priorität. */
	HOCH(1, "hoch"),

	/** Mittlere Hohe Priorität. */
	MITTEL(2, "mittel"),

	/** Geringe Priorität. */
	GERING(3, "gering"),

	/** Sehr geringe Priorität. */
	SEHR_GERING(4, "sehr gering"),

	/** Eine deaktivierte Regel. Muss den letzten Index haben.*/
	DEAKTIVIERT(5, "deaktiviert");


	/** Die Nummer (der Index) der Priorität. */
	public final int nr;

	/** Die Bezeichnung der Priorität. */
	public final String bezeichnung;


	/**
	 * Erstellt einen neuen Regel-Typ mit der angegeben ID.
	 *
	 * @param nr            die Nummer der Priorität.
	 * @param bezeichnung   die textuelle Bezeichnung der Priorität.
	 */
	UvBlockungRegelPrioritaet(final int nr, final @NotNull String bezeichnung) throws IllegalArgumentException {
		this.nr = nr;
		this.bezeichnung = bezeichnung;
	}


}
