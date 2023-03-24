package de.nrw.schule.svws.core.types.gost;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die drei Arten von Besonderen Lernleistungen als 
 * Core-Type-Aufzählung zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum GostBesondereLernleistung {

	/** keine besondere Lernleistung = K */
	KEINE("K", "Keine"),

	/** Projektkurs ist besondere Lernleistung = P */
	PROJEKTKURS("P", "Projektkurs ist besondere Lernleistung"),

	/** externe besondere Lernleistung = E */
	EXTERNE("E", "externe besondere Lernleistung");


	/** Das Kürzel für die Art der Besonderen Lernleistung */
	public final @NotNull String kuerzel;
	
	/** Die textuelle Beschreibung der Art der Besonderen Lernleistung */
	public final @NotNull String beschreibung; 
	

	/**
	 * Erzeugt ein neues Objekt für die Aufzählung der Arten von Besonderen
	 * Lernleistungen. 
	 * 
	 * @param kuerzel        das Kürzel für die Art der Besonderen Lernleistung
	 * @param beschreibung   die textuelle Beschreibung der Art der Besonderen Lernleistung
	 */
	private GostBesondereLernleistung(final @NotNull String kuerzel, final @NotNull String beschreibung) {
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
	}

	
	/** 
	 * Gibt die Art der Besonderen Lernleistung anhand des angegebenen Kürzels zurück. 
	 * 
	 * @param kuerzel   das Kürzel der Art der Besonderen Lernleistung
	 * 
	 * @return die Art der Besonderen Lernleistung
	 */
	public static @NotNull GostBesondereLernleistung fromKuerzel(final String kuerzel) {
		if (kuerzel == null)
			return KEINE;
		switch (kuerzel) {
			case "K": return KEINE;
			case "P": return PROJEKTKURS;
			case "E": return EXTERNE;
		}
		return KEINE;
	}

	
	@Override
	public @NotNull String toString() {
		return kuerzel;
	}


	/**
	 * Prüft, ob diese Art der Besonderen Lernleistung mit der Art der c
	 * Lernleistung des übergebenen Kürzels übereinstimmt.
	 * 
	 * @param kuerzel   das zu prüfende Kürzel der anderen Art der 
	 *                  besonderen Lernleistung
	 *
	 * @return true, falls die Arten übereinstimmen und ansonsten false
	 */
	@JsonIgnore
	public boolean is(final String kuerzel) {
		return this.kuerzel.equals(kuerzel);
	}

}
