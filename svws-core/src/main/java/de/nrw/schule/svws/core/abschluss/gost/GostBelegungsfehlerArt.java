package de.nrw.schule.svws.core.abschluss.gost;

import jakarta.validation.constraints.NotNull;

/**
 * Eine Aufzählung zur Unterscheidung der Fehlerarten (Belegungsfehler, Schriftlichkeit und Hinweis).
 */
public class GostBelegungsfehlerArt {

	/** Belegungsfehler */
	public static final @NotNull GostBelegungsfehlerArt BELEGUNG = new GostBelegungsfehlerArt("BELEGUNG");
	
	/** Fehler bei der Schriftlichkeit */
	public static final @NotNull GostBelegungsfehlerArt SCHRIFTLICHKEIT = new GostBelegungsfehlerArt("SCHRIFTLICHKEIT");
	
	/** Information, aber kein Fehler */ 
	public static final @NotNull GostBelegungsfehlerArt HINWEIS = new GostBelegungsfehlerArt("HINWEIS");
	
	
	/** Das Kürzel für die Belegungsfehlerart */
	public final @NotNull String kuerzel;

	
	/**
	 * Erzeugt ein neues Abitur-Belegungsfehler-Objekt
	 * 
	 * @param kuerzel        das Kürzel der Fehler-Art
	 */
	private GostBelegungsfehlerArt(@NotNull String kuerzel) {
		this.kuerzel = kuerzel;
	}

	
    /**
     * Gibt die Belegungsfehler-Art anhand des übergebenen Kürzels zurück.
     *
     * @param kuerzel    das Kürzel der Belegungsfehler-Art
     *  
     * @return die Belegungsfehler-Art
     */
	public static GostBelegungsfehlerArt fromKuerzel(String kuerzel) {
		if (kuerzel == null)
			return null;
		switch (kuerzel) {
			case "BELEGUNG": return BELEGUNG;
			case "SCHRIFTLICHKEIT": return SCHRIFTLICHKEIT;
			case "HINWEIS": return HINWEIS;
		}
		return null;
	}


	@Override
	public @NotNull String toString() {
		return kuerzel;
	}
	
}
