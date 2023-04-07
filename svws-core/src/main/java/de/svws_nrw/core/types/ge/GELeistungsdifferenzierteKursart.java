package de.svws_nrw.core.types.ge;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung enthält die zulässigen Kursarten für leistungsdifferenzierte
 * Kurse an Bildungsgängen der Gesamtschule.
 */
public enum GELeistungsdifferenzierteKursart {

	/** Es handelt sich um einen leistungsdifferenzierten Kurs auf Erweiterungsebene (E-Kurs) */
	E("E"),

	/** Es handelt sich um einen leistungsdifferenzierten Kurs auf Grundebene (G-Kurs) */
	G("G"),

	/** Es handelt sich um einen sonstigen Kurs ohne Leistungsdifferenzierung */
	Sonstige("");


	/** Das Kürzel der leistungsdifferenzierten Kursart */
	public final @NotNull String kuerzel;


	/**
	 * Erzeugt eine neue leistungsdifferenzierte Kursart.
	 *
	 * @param kuerzel   das Kürzel für die leistungsdifferenzierte Kursart
	 */
	GELeistungsdifferenzierteKursart(final @NotNull String kuerzel) {
		this.kuerzel = kuerzel;
	}


	/**
	 * Wandelt den übergebenen String, in einen Objekt dieser Aufzählung um.
	 * Dabei wird "E" als E-Kurs und "G" als G-Kurs interpretiert. Alles andere
	 * wird als sonstiger Kurs interpretiert.
	 *
	 * @param kuerzel   die Zeichenkette, die als Kursart interpretiert werden soll
	 *
	 * @return das resultierende Objekt dieser Aufzählung
	 */
	public static @NotNull GELeistungsdifferenzierteKursart from(final String kuerzel) {
		switch (kuerzel) {
			case "E": return GELeistungsdifferenzierteKursart.E;
			case "G": return GELeistungsdifferenzierteKursart.G;
			default: return GELeistungsdifferenzierteKursart.Sonstige;
		}
	}


	/**
	 * Prüft, ob die leistungsdifferenzierte Kursart das übergeben Kürzel hat
	 *
	 * @param kuerzel   das zu prüfende Kürzel
	 *
	 * @return true, falls sie das Kürzel hat und ansonsten false
	 */
	public boolean hat(final String kuerzel) {
		return this.kuerzel.equals(from(kuerzel).kuerzel);
	}


	/**
	 * Wandelt dieses Objekt in das zugehörige Kürzel um.
	 *
	 * @return die Zeichenkette die zu dieser Kursart gehört
	 */
	@Override
	public @NotNull String toString() {
		return kuerzel;
	}

}
