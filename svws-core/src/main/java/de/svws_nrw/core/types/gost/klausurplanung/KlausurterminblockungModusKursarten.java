package de.svws_nrw.core.types.gost.klausurplanung;

import java.util.HashMap;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Core-Type beschreibt die möglichen Modi bei der Blockung
 * von Klausurterminen in Bezug darauf, ob die Leistungskurse (LK) und
 * die Grundkurse (GK) zusammen geblockt werden sollen und wenn ja,
 * in welcher Reihenfolge dies erfolgen soll.
 */
public enum KlausurterminblockungModusKursarten {

	/** Dieser Modus blockt beide Kursarten (LK und GK) gemischt. */
	BEIDE(0, "Gemischt"),

	/** Dieser Modus blockt zuerst die Kursart LK, danach die Kursart GK. */
	GETRENNT(1, "Getrennt"),

	/** Dieser Modus blockt nur die Kursart LK. */
	NUR_LK(2, "Nur LK"),

	/** Dieser Modus blockt nur die Kursart GK. */
	NUR_GK(3, "Nur GK");



	/** Die ID */
	public final int id;

	/** Die textuelle Bezeichnung */
	public final @NotNull String bezeichnung;


	/** Eine Map mit der Zuordnung zu der ID */
	private static final @NotNull HashMap<@NotNull Integer, @NotNull KlausurterminblockungModusKursarten> _mapID = new HashMap<>();


	/**
	 * Erstellt einen neuen Modus.
	 *
	 * @param id            die ID
	 * @param bezeichnung   die Bezeichnung
	 */
	KlausurterminblockungModusKursarten(final int id, final @NotNull String bezeichnung) {
		this.id = id;
		this.bezeichnung = bezeichnung;
	}


	/**
	 * Gibt die Map mit der Zuordnung zu der ID zurück. Sollte diese noch nicht
	 * initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map mit der Zuordnung zu der ID
	 */
	private static @NotNull HashMap<@NotNull Integer, @NotNull KlausurterminblockungModusKursarten> getMapByID() {
		if (_mapID.size() == 0)
			for (final @NotNull KlausurterminblockungModusKursarten e : KlausurterminblockungModusKursarten.values())
				_mapID.put(e.id, e);
		return _mapID;
	}


	/**
	 * Liefert den Modus anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Modus oder null, falls die ID ungültig ist
	 */
	public static KlausurterminblockungModusKursarten get(final int id) {
		return getMapByID().get(id);
	}

	/**
	 * Liefert den Modus anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Modus
	 *
	 * @throws DeveloperNotificationException falls die ID nicht definiert ist
	 */
	public static @NotNull KlausurterminblockungModusKursarten getOrException(final int id) {
		return DeveloperNotificationException.ifMapGetIsNull(getMapByID(), id);
	}

}
