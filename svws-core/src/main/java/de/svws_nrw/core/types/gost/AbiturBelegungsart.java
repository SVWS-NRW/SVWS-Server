package de.svws_nrw.core.types.gost;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Aufzählung für die Abitur-Belegungsarten
 * zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum AbiturBelegungsart {

	/** AbiturBelegungsart "nicht belegt" */
	NICHT_BELEGT("-", "nicht belegt"),

	/** AbiturBelegungsart "mündlich" */
	MUENDLICH("M", "mündlich"),

	/** AbiturBelegungsart "schriftlich" */
	SCHRIFTLICH("S", "schriftlich");


	/** Das Kürzel für die Kurs-Belegungsart (-,M oder S)*/
	public @NotNull String kuerzel;

	/** Die textuelle Beschreibung der Kurs-Belegungsart (nicht belegt, mündlich oder schriftlich) */
	public @NotNull String beschreibung;


	/**
	 * Erzeugt ein neues Abitur-Belegungsart-Objekt
	 *
	 * @param kuerzel        das der Kurs-Belegungsart
	 * @param beschreibung   die textuelle Beschreibung der Kurs-Belegungsart
	 */
	AbiturBelegungsart(final @NotNull String kuerzel, final @NotNull String beschreibung) {
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
	}


	/**
	 * Gibt die Kurs-Belegungsart anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Kurs-Belegungsart
	 *
	 * @return die Kurs-Belegungsart oder null, falls das kuerzel fehlerhaft ist
	 */
	public static AbiturBelegungsart fromKuerzel(final String kuerzel) {
		if (kuerzel == null)
			return NICHT_BELEGT;
		switch (kuerzel) {
			case "-": return NICHT_BELEGT;
			case "M": return MUENDLICH;
			case "S": return SCHRIFTLICH;
		}
		return null;
	}


	@Override
	public @NotNull String toString() {
		return kuerzel;
	}


}
