package de.nrw.schule.svws.core.types;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Aufzählung für die Fortschreibungarten 
 * von Kursen bei der Versetzung der Schule in einen neuen Abschnitt zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum KursFortschreibungsart {
	
	/** Keine Fortschreibung Kurs wird bei der Verstzung gelöscht. */
	KEINE(0, "N", "Keine", null, null),
	
	/** Nur Definition, alle Schüler werden aus dem Kurs gelöscht, der Jahrgang wird aber erhöht. */
	NUR_DEFINITION_JAHRGANG_HOCHSCHREIBEN(1, "D", "Nur Definition, Jahrgang hochschreiben", null, null),
	
	/** Nur Definition, alle Schüler werden aus dem Kurs gelöscht, der Jahrgang wird nicht erhöht. */
	NUR_DEFINITION_JAHRGANG_BEIBEHALTEN(2, "B", "Nur Definition, Jahrgang beibehalten", null, null),
	
	/** Komplett, der Kurs wird mit Schülern hochgeschrieben und der Jahrgang erhöht. */
	KOMPLETT(3, "K", "Komplett", null, null);	


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;


	/** Die ID der Kurs-Fortschreibungsart als Integer */
	public final int id;

	/** Das eindeutige einstelleige Kürzel der Kurs-Fortschreibungsart. */
	public final @NotNull String kuerzel;
	
	/** Die Beschreibung der Kurs-Fortschreibungsart */
	public final @NotNull String beschreibung;

	/** Gibt an, in welchem Schuljahr die Fortschreibungsart einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	public final Integer gueltigVon;

	/** Gibt an, bis zu welchem Schuljahr die Fortschreibungsart gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	public final Integer gueltigBis;
	
	
	/**
	 * Erstellt eine neue Kurs-Fortschreibungsart in der Aufzählung.
	 * 
	 * @param id             die ID der Fortchreibungsart
	 * @param kuerzel        das eindeutige einstelleige Kürzel der Kurs-Fortschreibungsart
	 * @param beschreibung   die Beschreibung der Kurs-Fortschreibungsart
	 * @param gueltigVon     gibt an, in welchem Schuljahr die Fortschreibungsart einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 * @param gueltigBis     gibt an, bis zu welchem Schuljahr die Fortschreibungsart gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	private KursFortschreibungsart(final int id, final @NotNull String kuerzel, final @NotNull String beschreibung, final Integer gueltigVon, final Integer gueltigBis) {
    	this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
        this.gueltigVon = gueltigVon;
        this.gueltigBis = gueltigBis;
	}


	/**
	 * Gibt die Kurs-Fortschreibungsart anhand der ID zurück.
	 * Eine ungültige ID wird als Fortschreibungsart KEINE interpretiert. 
	 * 
	 * @param id    die ID
	 * 
	 * @return die Kurs-Fortschreibungsart 
	 */
	public static KursFortschreibungsart fromID(final Integer id) {
		if (id == null)
			return KursFortschreibungsart.KEINE;
		switch (id) {
			case 0: return KursFortschreibungsart.KEINE;
			case 1: return KursFortschreibungsart.NUR_DEFINITION_JAHRGANG_HOCHSCHREIBEN;
			case 2: return KursFortschreibungsart.NUR_DEFINITION_JAHRGANG_BEIBEHALTEN;
			case 3: return KursFortschreibungsart.KOMPLETT;			
		}
		return KursFortschreibungsart.KEINE;
	}


	/**
	 * Gibt die Kurs-Fortschreibungsart anhand des Kürzels zurück. 
	 * Eine ungültiges Kürzel wird als Fortschreibungsart KEINE interpretiert. 
	 * 
	 * @param kuerzel    das Kürzel
	 * 
	 * @return die Kurs-Fortschreibungsart 
	 */
	public static KursFortschreibungsart fromKuerzel(final String kuerzel) {
		if (kuerzel == null)
			return KursFortschreibungsart.KEINE;
		switch (kuerzel) {
			case "N": return KursFortschreibungsart.KEINE;
			case "D": return KursFortschreibungsart.NUR_DEFINITION_JAHRGANG_HOCHSCHREIBEN;
			case "B": return KursFortschreibungsart.NUR_DEFINITION_JAHRGANG_BEIBEHALTEN;
			case "K": return KursFortschreibungsart.KOMPLETT;			
		}
		return KursFortschreibungsart.KEINE;
	}
	
	
	/**
	 * Prüft, ob das übergebene Kürzel für eine gültige Kurs-Fortschreibungsart
	 * steht oder nicht
	 * 
	 * @param kuerzel   das zu prüfende Kürzel
	 * 
	 * @return true, falls das kürzel gültig ist.
	 */
	public static boolean isValidKuerzel(final String kuerzel) {
		for (final KursFortschreibungsart art : KursFortschreibungsart.values())
			if (art.kuerzel.equals(kuerzel))
				return true;
		return false;
	}

}
