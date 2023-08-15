package de.svws_nrw.core.types.gost.klausurplanung;

import java.util.HashMap;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Core-Type beschreibt die möglichen Modi bei der Blockung
 * von Klausurterminen in Bezug auf die Quartale.
 */
public enum KlausurterminblockungModusQuartale {

	/** Alle Klausuren eines Halbjahres werden gemeinsam geblockt. */
	ZUSAMMEN(0, "Zusammen"),

	/** Die Klausuren werden pro Quartal im Halbjahr geblockt. */
	GETRENNT(1, "Getrennt");


	/** Die ID */
	public final int id;

	/** Die textuelle Bezeichnung */
	public final @NotNull String bezeichnung;


	/** Eine Map mit der Zuordnung zu der ID */
	private static final @NotNull HashMap<@NotNull Integer, @NotNull KlausurterminblockungModusQuartale> _mapID = new HashMap<>();


	/**
	 * Erstellt einen neuen Modus.
	 *
	 * @param id            die ID
	 * @param bezeichnung   die Bezeichnung
	 */
	KlausurterminblockungModusQuartale(final int id, final @NotNull String bezeichnung) {
		this.id = id;
		this.bezeichnung = bezeichnung;
	}


	/**
	 * Gibt die Map mit der Zuordnung zu der ID zurück. Sollte diese noch nicht
	 * initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map mit der Zuordnung zu der ID
	 */
	private static @NotNull HashMap<@NotNull Integer, @NotNull KlausurterminblockungModusQuartale> getMapByID() {
		if (_mapID.size() == 0)
			for (final @NotNull KlausurterminblockungModusQuartale e : KlausurterminblockungModusQuartale.values())
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
	public static KlausurterminblockungModusQuartale get(final int id) {
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
	public static @NotNull KlausurterminblockungModusQuartale getOrException(final int id) {
		return DeveloperNotificationException.ifMapGetIsNull(getMapByID(), id);
	}

}
