package de.nrw.schule.svws.core.types.gost;

import java.util.Collection;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Auszählung für die Kursarten in 
 * der gymnasialen Oberstufe zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public class GostKursart {

	/** Die Zuordnung der Kursarten zu dem Kürzel der Kursart */
	private static final @NotNull HashMap<@NotNull String, @NotNull GostKursart> map = new HashMap<>();



	/** Leistungskurs = LK */
	public static final @NotNull GostKursart LK = new GostKursart(1, "LK", "Leistungskurs");

	/** Grundkurs = GK */
	public static final @NotNull GostKursart GK = new GostKursart(2, "GK", "Grundkurs");

	/** Zusatzkurs = ZK */
	public static final @NotNull GostKursart ZK = new GostKursart(3, "ZK", "Zusatzkurs");

	/** Projektkurs = PJK */
	public static final @NotNull GostKursart PJK = new GostKursart(4, "PJK", "Projektkurs");

	/** Vertiefungskurs = VTF */
	public static final @NotNull GostKursart VTF = new GostKursart(5, "VTF", "Vertiefungskurs");

	
	/** Die eindeutige ID der Kursart der Gymnasialen Oberstufe */
	public final @NotNull int id;

	/** Das Kürzel der Kursart der Gymnasialen Oberstufe */
	public final @NotNull String kuerzel;

	/** Die textuelle Beschreibung der allgemeinen Kursart der Gymnasialen Oberstufe */
	public final @NotNull String beschreibung; 



	/**
	 * Erzeugt eine neue Kursart für die Aufzählung.
	 * 
	 * @param id             die eindeutige ID der Kursart der Gymnasialen Oberstufe
	 * @param kuerzel        das Kürzel der Kursart der Gymnasialen Oberstufe
	 * @param beschreibung   die textuelle Beschreibung der allgemeinen Kursart der Gymnasialen Oberstufe
	 */
	private GostKursart(final @NotNull int id, final @NotNull String kuerzel, final @NotNull String beschreibung) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
		map.put(kuerzel, this);
	}


    /**
     * Prüft die Anzahl der Wochenstunden zu der Kursart.
     * 
     * @param anzahl   Anzahl der Wochenstunden
     * 
     * @return         Anzahl der Wochenstunden der Kursart korrekt, true oder false
     */
	@JsonIgnore
	public boolean pruefeWochenstunden(int anzahl) {
		switch (kuerzel) {
			case "GK":
				return (anzahl == 3) || (anzahl == 4);  // neu einsetzende Fremdsprachen können 4-stündig sein
			case "LK":
				return (anzahl == 5);
			case "PJK":
				return (anzahl == 2) || (anzahl == 3);
			case "VTF":
				return (anzahl == 2);
			case "ZK":
				return (anzahl == 3);
		}
		return false;
	}


	/**
	 * Gibt alle Kursarten der gymnasialen Oberstufe zurück.
	 * 
	 * @return eine {@link Collection} mit den Kursarten.
	 */
	public static final @NotNull Collection<@NotNull GostKursart> values() {
		return map.values();
	}

	
    /**
     * Gibt die Kursart aus der ID Kursart zurück.
     * 
     * @param id    die ID der Kursart
     * 
     * @return die Kursart
     * 
     * @throws IllegalArgumentException falls die ID ungültig ist 
     */
	public static @NotNull GostKursart fromID(int id) throws IllegalArgumentException {
		switch (id) {
			case 1: return GostKursart.LK; 
			case 2: return GostKursart.GK; 
			case 3: return GostKursart.ZK; 
			case 4: return GostKursart.PJK; 
			case 5: return GostKursart.VTF; 
		}
		throw new IllegalArgumentException("Invalid ID value.");
	}
	

    /**
     * Gibt die Kursart aus der ID Kursart zurück.
     * 
     * @param id    die ID der Kursart
     * 
     * @return die Kursart oder null falls die ID ungültig ist 
     */
	public static GostKursart fromIDorNull(int id) {
		switch (id) {
			case 1: return GostKursart.LK; 
			case 2: return GostKursart.GK; 
			case 3: return GostKursart.ZK; 
			case 4: return GostKursart.PJK; 
			case 5: return GostKursart.VTF; 
		}
		return null;
	}
	

    /**
     * Gibt die Kursart aus dem Kürzel der Kursart zurück.
     * 
     * @param kuerzel    das Kürzel der Kursart
     * 
     * @return die Kursart oder null, falls das Kürzel ungültig ist 
     */
	public static GostKursart fromKuerzel(String kuerzel) {
		return map.get(kuerzel);
	}

	
	@Override
	@JsonIgnore
	public @NotNull String toString() {
		return kuerzel;
	}


}
