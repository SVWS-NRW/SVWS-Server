package de.svws_nrw.core.abschluss.gost;

import jakarta.validation.constraints.NotNull;

/**
 * Eine Aufzählung zur Unterscheidung der Fehlerarten (Belegungsfehler, Schriftlichkeit und Hinweis).
 */
public enum GostBelegungsfehlerArt {

	/** Belegungsfehler */
	BELEGUNG("BELEGUNG"),

	/** Fehler bei der Schriftlichkeit */
	SCHRIFTLICHKEIT("SCHRIFTLICHKEIT"),

	/** Fehler in Bezug auf schulische Rahmenbedingungen - Zusatzkursbeginn oder nicht erlaubte oder geforderte Fachkombinationen */
	SCHULSPEZIFISCH("SCHULSPEZIFISCH"),

	/** Information, aber kein Fehler */
	HINWEIS("HINWEIS");


	/** Das Kürzel für die Belegungsfehlerart */
	public final @NotNull String kuerzel;


	/**
	 * Erzeugt ein neues Abitur-Belegungsfehler-Objekt
	 *
	 * @param kuerzel        das Kürzel der Fehler-Art
	 */
	GostBelegungsfehlerArt(final @NotNull String kuerzel) {
		this.kuerzel = kuerzel;
	}


	/**
	 * Gibt die Belegungsfehler-Art anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Belegungsfehler-Art
	 *
	 * @return die Belegungsfehler-Art
	 */
	public static GostBelegungsfehlerArt fromKuerzel(final String kuerzel) {
		if (kuerzel == null)
			return null;
		switch (kuerzel) {
			case "BELEGUNG": return BELEGUNG;
			case "SCHRIFTLICHKEIT": return SCHRIFTLICHKEIT;
			case "SCHULSPEZIFISCH": return SCHULSPEZIFISCH;
			case "HINWEIS": return HINWEIS;
			default: return null;
		}
	}


	@Override
	public @NotNull String toString() {
		return kuerzel;
	}

}
