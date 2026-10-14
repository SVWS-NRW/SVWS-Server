package de.svws_nrw.core.data.enm;

import jakarta.validation.constraints.NotNull;

/**
 * Eine Aufzählung, welche die Lehrerfunktion beschreibt, welche im Notenmodul genutzt wird.
 */
public enum ENMLehrerFunktion {

	/** Fachlehrer */
	FACHLEHRER(1, "Fachlehrer/-in"),

	/** Klassenleitung */
	KLASSENLEITUNG(2, "Klassenleitung"),

	/** Abteilungsleitung */
	ABTEILUNGSLEITUNG(3, "Abteilungsleitung"),

	/** Schulleitung */
	SCHULLEITUNG(4, "Schulleitung"),

	/** Notenmoduladministrator */
	NOTENMODULADMIN(5, "Notenmoduladministrator");


	/** Eine eindeutige ID für die Lehrerfunktion */
	public final int id;

	/** Ein Text, welcher die Lehrerfunktion für die Visualisierung beschreibt */
	public final @NotNull String text;


	/**
	 * Erstellt eine neue Lehrer-Funktion für das Notenmodul
	 *
	 * @param id      die eindeutige ID
	 * @param text    die textuelle Beschreibung
	 */
	ENMLehrerFunktion(final int id, final @NotNull String text) {
		this.id = id;
		this.text = text;
	}


	/**
	 * Gibt die Lehrer-Funktion für die übergebene ID zurück
	 *
	 * @param id   die ID oder null
	 *
	 * @return die Lehrer-Funktion oder null, falls die ID ungültig ist
	 */
	public static ENMLehrerFunktion fromID(final Integer id) {
		if (id == null) {
			return null;
		}
		return switch (id) {
			case 1 -> ENMLehrerFunktion.FACHLEHRER;
			case 2 -> ENMLehrerFunktion.KLASSENLEITUNG;
			case 3 -> ENMLehrerFunktion.ABTEILUNGSLEITUNG;
			case 4 -> ENMLehrerFunktion.SCHULLEITUNG;
			case 5 -> ENMLehrerFunktion.NOTENMODULADMIN;
			default -> null;
		};
	}

}
