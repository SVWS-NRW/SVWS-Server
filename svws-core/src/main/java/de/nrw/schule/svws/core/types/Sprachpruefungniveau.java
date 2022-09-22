package de.nrw.schule.svws.core.types;

import java.util.HashMap;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Enumeration für
 * die Sprachprüfungsniveaus zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum Sprachpruefungniveau {

	/** Prüfungsniveau angelehnt an 'HA9' */
	NIVEAU_HA9(1, "NIVEAU_HA9", "Hauptschulabschluss nach Klasse 9", null, null),

	/** Prüfungsniveau angelehnt an 'HA10' */
	NIVEAU_HA10(2, "NIVEAU_HA10", "Hauptschulabschluss nach Klasse 10", null, null),

	/** Prüfungsniveau angelehnt an 'MSA' */
	NIVEAU_MSA(3, "NIVEAU_MSA", "Mittlerer Schulabschluss (Fachoberschulreife) / Berechtigung zum Besuch der gymnasialen Oberstufe (Gymnasium G8 Klasse 9)", null, null),

	/** Prüfungsniveau angelehnt an 'EF' */
	NIVEAU_EF(4, "NIVEAU_EF", "Ende der Einführungsphase der gymnasialen Oberstufe in einer fortgeführten Fremdsprache (Gymnasium und Gesamtschule)", null, null),

	/** Prüfungsniveau angelehnt an 'FHR' */
	NIVEAU_FHR(5, "NIVEAU_FHR", "Fachhochschulreife (Abschluss an berufsbildenden Schulen)", null, null),

	/** Prüfungsniveau angelehnt an 'WBK_FF' */
	NIVEAU_WBK_FF(6, "NIVEAU_WBK_FF", "Fortgeführte Fremdsprache gemäß § 34 Abs. 4 APO-WbK (nur zweite Pflichtfremdsprache)", null, null);

	
	/** Die Zuordnung der Sprachreferenzniveaus zu ihren IDs */
	private static @NotNull HashMap<@NotNull Integer, @NotNull Sprachpruefungniveau> _mapID = new HashMap<>();

	/** Die Zuordnung der Sprachreferenzniveaus zu ihren Bezeichnungen */
	private static @NotNull HashMap<@NotNull String, @NotNull Sprachpruefungniveau> _mapBezeichnung = new HashMap<>();
	

	/** Die ID des Prüfungsniveaus */
	public final int id;

	/** Die Bezeichnung des Prüfungsniveaus als Text */
	public final @NotNull String bezeichnung;

	/** Die Beschreibung des Prüfungsniveaus als Text */
	public final @NotNull String beschreibung;

	/** Schuljahr, ab dem das Niveau gültig ist */
	public final Integer gueltigVon;

	/** Schuljahr, bis zu dem das Niveau gültig war */
	public final Integer gueltigBis;


	/**
	 * Erstellt ein neues Prüfungsniveau dieser Aufzählung.
	 *
	 * @param id            die ID des Prüfungsniveaus
	 * @param bezeichnung   die Bezeichnung des Prüfungsniveaus
	 * @param beschreibung  die Beschreibung des Prüfungsniveaus
	 * @param gueltigVon	Schuljahr, ab dem das Niveau gültig ist
	 * @param gueltigBis	Schuljahr, bis zu dem das Niveau gültig war
	 */
	private Sprachpruefungniveau(final int id, final @NotNull String bezeichnung, final @NotNull String beschreibung, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.beschreibung = beschreibung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

	
	/**
	 * Gibt eine Map von den IDs der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 */
	private static @NotNull HashMap<@NotNull Integer, @NotNull Sprachpruefungniveau> getMapSprachpruefungniveauByID() {
		if (_mapID.size() == 0)
			for (Sprachpruefungniveau l : Sprachpruefungniveau.values())
				_mapID.put(l.id, l);				
		return _mapID;
	}

	
	/**
	 * Gibt eine Map von den Bezeichnungen der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Bezeichnungen der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull Sprachpruefungniveau> getMapSprachpruefungniveauByBezeichnung() {
		if (_mapBezeichnung.size() == 0)
			for (Sprachpruefungniveau l : Sprachpruefungniveau.values())
				_mapBezeichnung.put(l.bezeichnung, l);				
		return _mapBezeichnung;
	}
		

	/**
	 * Gibt das Prüfungsniveau für die übergebene ID zurück.
	 *
	 * @param id   die ID des Prüfungsniveaus
	 *
	 * @return das Prüfungsniveaus oder null, wenn die ID ungültig ist
	 */
	public static Sprachpruefungniveau getByID(Integer id) {
		return getMapSprachpruefungniveauByID().get(id);
	}


	/**
	 * Gibt das Prüfungsniveau für die übergebene Bezeichnung zurück.
	 * 
	 * @param bezeichnung   die Bezeichnung des Prüfungsniveaus
	 * 
	 * @return das Prüfungsniveaus oder null, wenn die Bezeichnung ungültig ist
	 */
	public static Sprachpruefungniveau getByBezeichnung(String bezeichnung) {
		return getMapSprachpruefungniveauByBezeichnung().get(bezeichnung);
	}

}
