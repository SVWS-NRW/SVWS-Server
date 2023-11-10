package de.svws_nrw.core.types.fach;

import java.util.HashMap;

import de.svws_nrw.core.data.fach.SprachpruefungsniveauKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Enumeration für
 * die Sprachprüfungsniveaus zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum Sprachpruefungniveau {

	/** Prüfungsniveau angelehnt an 'HA9' */
	ESA(new SprachpruefungsniveauKatalogEintrag[] {
			new SprachpruefungsniveauKatalogEintrag(1, "NIVEAU_ESA", "Erster Schulabschluss", null, null)
	}),

	/** Prüfungsniveau angelehnt an 'HA10' */
	EESA(new SprachpruefungsniveauKatalogEintrag[] {
			new SprachpruefungsniveauKatalogEintrag(2, "NIVEAU_EESA", "Erweiterter Erster Schulabschluss", null, null)
	}),

	/** Prüfungsniveau angelehnt an 'MSA' */
	MSA(new SprachpruefungsniveauKatalogEintrag[] {
			new SprachpruefungsniveauKatalogEintrag(3, "NIVEAU_MSA", "Mittlerer Schulabschluss / Berechtigung zum Besuch der gymnasialen Oberstufe (Gymnasium G8 Klasse 9)", null, null),
	}),

	/** Prüfungsniveau angelehnt an 'EF' */
	EF(new SprachpruefungsniveauKatalogEintrag[] {
			new SprachpruefungsniveauKatalogEintrag(4, "NIVEAU_EF", "Ende der Einführungsphase der gymnasialen Oberstufe in einer fortgeführten Fremdsprache (Gymnasium und Gesamtschule)", null, null),
	}),

	/** Prüfungsniveau angelehnt an 'FHR' */
	FHR(new SprachpruefungsniveauKatalogEintrag[] {
			new SprachpruefungsniveauKatalogEintrag(5, "NIVEAU_FHR", "Fachhochschulreife (Abschluss an berufsbildenden Schulen)", null, null)
	}),

	/** Prüfungsniveau angelehnt an 'WBK_FF' */
	WBK_FF(new SprachpruefungsniveauKatalogEintrag[] {
			new SprachpruefungsniveauKatalogEintrag(6, "NIVEAU_WBK_FF", "Fortgeführte Fremdsprache gemäß § 34 Abs. 4 APO-WbK (nur zweite Pflichtfremdsprache)", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten des Sprachprüfungsniveaus */
	public final @NotNull SprachpruefungsniveauKatalogEintrag daten;

	/** Die Historie mit den Einträgen des Sprachprüfungsniveaus */
	public final @NotNull SprachpruefungsniveauKatalogEintrag@NotNull[] historie;

	/** Die Zuordnung der Sprachreferenzniveaus zu ihren IDs */
	private static final @NotNull HashMap<@NotNull Integer, @NotNull Sprachpruefungniveau> _mapID = new HashMap<>();

	/** Die Zuordnung der Sprachreferenzniveaus zu ihren Bezeichnungen */
	private static final @NotNull HashMap<@NotNull String, @NotNull Sprachpruefungniveau> _mapKuerzel = new HashMap<>();



	/**
	 * Erstellt ein neues Prüfungsniveau dieser Aufzählung.
	 *
	 * @param historie   die Historie des Sprachreferenzniveaus, welche ein Array von
	 *                   {@link SprachpruefungsniveauKatalogEintrag} ist
	 */
	Sprachpruefungniveau(final @NotNull SprachpruefungsniveauKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den IDs der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 */
	private static @NotNull HashMap<@NotNull Integer, @NotNull Sprachpruefungniveau> getMapByID() {
		if (_mapID.size() == 0)
			for (final Sprachpruefungniveau l : Sprachpruefungniveau.values())
				_mapID.put(l.daten.id, l);
		return _mapID;
	}


	/**
	 * Gibt eine Map von den Bezeichnungen der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Bezeichnungen der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull Sprachpruefungniveau> getMapByKuerzel() {
		if (_mapKuerzel.size() == 0)
			for (final Sprachpruefungniveau l : Sprachpruefungniveau.values())
				_mapKuerzel.put(l.daten.kuerzel, l);
		return _mapKuerzel;
	}


	/**
	 * Gibt das Prüfungsniveau für die übergebene ID zurück.
	 *
	 * @param id   die ID des Prüfungsniveaus
	 *
	 * @return das Prüfungsniveaus oder null, wenn die ID ungültig ist
	 */
	public static Sprachpruefungniveau getByID(final Integer id) {
		return getMapByID().get(id);
	}


	/**
	 * Gibt das Prüfungsniveau für das übergebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel des Prüfungsniveaus
	 *
	 * @return das Prüfungsniveaus oder null, wenn das Kürzel ungültig ist
	 */
	public static Sprachpruefungniveau getByKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}

}
