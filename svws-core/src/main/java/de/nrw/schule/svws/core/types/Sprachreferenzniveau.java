package de.nrw.schule.svws.core.types;

import java.util.HashMap;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Ennummeration für 
 * die AbiturBelegungsart zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public class Sprachreferenzniveau implements Comparable<Sprachreferenzniveau> {
	
	/** Die Zuordnung der Sprachreferenzniveaus zu ihren Bezeichnungen */
	private static @NotNull HashMap<@NotNull String, @NotNull Sprachreferenzniveau> mapBezeichnung = new HashMap<>();
	

	/** Referenzniveau nach GeR A1. */
	public static final @NotNull Sprachreferenzniveau A1 = new Sprachreferenzniveau(1, "A1");
	
	/** Referenzniveau nach GeR A1 Plus */
	public static final @NotNull Sprachreferenzniveau A1P = new Sprachreferenzniveau(2, "A1+");
	
	/** Referenzniveau nach GeR A1A2 */
	public static final @NotNull Sprachreferenzniveau A1A2 = new Sprachreferenzniveau(3, "A1/A2");
	
	/** Referenzniveau nach GeR A2 */
	public static final @NotNull Sprachreferenzniveau A2 = new Sprachreferenzniveau(4, "A2");
	
	/** Referenzniveau nach GeR A2 Plus */
	public static final @NotNull Sprachreferenzniveau A2P = new Sprachreferenzniveau(5, "A2+");
	
	/** Referenzniveau nach GeR A2B1. */
	public static final @NotNull Sprachreferenzniveau A2B1 = new Sprachreferenzniveau(6, "A2/B1");
	
	/** Referenzniveau nach GeR B1. */
	public static final @NotNull Sprachreferenzniveau B1 = new Sprachreferenzniveau(7, "B1");
	
	/** Referenzniveau nach GeR B1 Plus. */
	public static final @NotNull Sprachreferenzniveau B1P = new Sprachreferenzniveau(8, "B1+");
	
	/** Referenzniveau nach GeR B1B2. */
	public static final @NotNull Sprachreferenzniveau B1B2 = new Sprachreferenzniveau(9, "B1/B2");
	
	/** Referenzniveau nach GeR B2. */
	public static final @NotNull Sprachreferenzniveau B2 = new Sprachreferenzniveau(10, "B2");
	
	/** Referenzniveau nach GeR B2C1. */
	public static final @NotNull Sprachreferenzniveau B2C1 = new Sprachreferenzniveau(11, "B2/C1");
	
	/** Referenzniveau nach GeR C1. */
	public static final @NotNull Sprachreferenzniveau C1 = new Sprachreferenzniveau(12, "C1");
	
	/** Referenzniveau nach GeR C2. */
	public static final @NotNull Sprachreferenzniveau C2 = new Sprachreferenzniveau(13, "C2");


	/** Die Sortierung der referenzniveaus, welche intern für vergleiche genutzt wird. */
	private final int sortierung;

	/** Die Bezeichnung des Referenzniveaus als Text */
	public final @NotNull String bezeichnung;


	/** 
	 * Erstellt ein neues Sprachreferenzniveau dieser Aufzählung.

	 * @param sortierung    die Sortierung, welche nur intern für den vergleich von 
	 *                      Referenzniveaus genutzt wird. 
	 * @param bezeichnung   die Bezeichnung des Sprachreferenzniveaus
	 */
	private Sprachreferenzniveau(final int sortierung, final @NotNull String bezeichnung) {
		this.sortierung = sortierung;
		this.bezeichnung = bezeichnung;
		mapBezeichnung.put(bezeichnung, this);
	}

	
	/**
	 * Gibt das Sprachreferenzniveau für die übergebene Bezeichnung zurück.
	 * 
	 * @param bezeichnung   die Bezeichnung des Sprachreferenzniveaus
	 * 
	 * @return das Sprachreferenzniveau oder null im Fehlerfall
	 */
	public static Sprachreferenzniveau getByBezeichnung(String bezeichnung) {
		return mapBezeichnung.get(bezeichnung);
	}
	

	@Override
	public int compareTo(Sprachreferenzniveau other) {
		if (other == null)
			return 1;   // irgendetwas ist besser als kein Sprachreferenzniveau
		return Integer.compare(sortierung, other.sortierung);
	}

	/**
	 * Vergleicht dieses Sprachreferenzniveau mit dem Niveau der übergebenen Bezeichnung.
	 * 
	 * @param bezeichnung   die Bezeichnung des anderen Sprachreferenzniveaus
	 * 
	 * @return siehe {@link Sprachreferenzniveau#compareTo(Sprachreferenzniveau)}
	 */
	public int compareTo(String bezeichnung) {
		return compareTo(getByBezeichnung(bezeichnung));
	}
	
}
