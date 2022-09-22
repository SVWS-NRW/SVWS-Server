package de.nrw.schule.svws.core.abschluss.gost;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählungsklasse spezifiziert die Art der Belegprüfung, d.h. über welche Halbjahre 
 * sie sich erstreckt. 
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public class GostBelegpruefungsArt {
	
	/** Prüfung nur der EF.1 */
	public static final @NotNull GostBelegpruefungsArt EF1 = new GostBelegpruefungsArt("EF.1", "nur EF.1");
	
	/** Gesamtprüfung über die gesamte Oberstufe */
	public static final @NotNull GostBelegpruefungsArt GESAMT = new GostBelegpruefungsArt("Gesamt", "die gesamte Oberstufe");
	
	
	/** Das Kürzel für die Belegprüfungsart */
	public final @NotNull String kuerzel;
	
	/** Eine textuelle Beschreibung für die Art der Belegprüfung */
	public final @NotNull String beschreibung; 
	

	/**
	 * Erzeugt ein neues Abitur-Belegungsart-Objekt
	 * 
	 * @param kuerzel        das der Kurs-Belegungsart
	 * @param beschreibung   die textuelle Beschreibung der Kurs-Belegungsart
	 */
	private GostBelegpruefungsArt(@NotNull String kuerzel, @NotNull String beschreibung) {
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
	}

	
    /**
     * Gibt die Art der Belegprüfung anhand des übergebenen Kürzels zurück.
     *
     * @param kuerzel    das Kürzel der Art der Belegprüfung
     *  
     * @return die Art der Belegprüfung
     */
	public static GostBelegpruefungsArt fromKuerzel(String kuerzel) {
		if (kuerzel == null)
			return null;
		switch (kuerzel) {
			case "EF.1": return EF1;
			case "Gesamt": return GESAMT;
		}
		return null;
	}


	@Override
	public @NotNull String toString() {
		return kuerzel;
	}
	
}