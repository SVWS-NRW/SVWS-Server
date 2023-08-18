package de.svws_nrw.core.types.gost.klausurplanung;

import java.util.HashMap;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Core-Type beschreibt die möglichen Algorithmen, die bei der Blockung
 * von Klausurterminen verwendet werden können.
 */
public enum KlausurterminblockungAlgorithmen {

	/** Ein Algorithmus, welcher versucht die Anzahl der Termine zu minimieren. */
	NORMAL(1, "Normal"),

	/** Dieser Algorithmus forciert, das pro Termin nur die selben Fächer sind.
	 * Im zweiten Schritt wird versucht die Anzahl der Termine zu minimieren. */
	FAECHERWEISE(2, "Fächerweise"),

	/** Dieser Algorithmus forciert, das pro Termin nur die selben Kurs-Schienen sind.
	 * Im zweiten Schritt wird versucht die Anzahl der Termine zu minimieren. */
	SCHIENENWEISE(3, "Schienenweise");



	/** Die ID */
	public final int id;

	/** Die textuelle Bezeichnung */
	public final @NotNull String bezeichnung;


	/** Eine Map mit der Zuordnung zu der ID */
	private static final @NotNull HashMap<@NotNull Integer, @NotNull KlausurterminblockungAlgorithmen> _mapID = new HashMap<>();


	/**
	 * Erstellt einen neuen Algorithmus-Typ.
	 *
	 * @param id            die ID
	 * @param bezeichnung   die Bezeichnung
	 */
	KlausurterminblockungAlgorithmen(final int id, final @NotNull String bezeichnung) {
		this.id = id;
		this.bezeichnung = bezeichnung;
	}


	/**
	 * Gibt die Map mit der Zuordnung zu der ID zurück. Sollte diese noch nicht
	 * initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map mit der Zuordnung zu der ID
	 */
	private static @NotNull HashMap<@NotNull Integer, @NotNull KlausurterminblockungAlgorithmen> getMapByID() {
		if (_mapID.size() == 0)
			for (final @NotNull KlausurterminblockungAlgorithmen e : KlausurterminblockungAlgorithmen.values())
				_mapID.put(e.id, e);
		return _mapID;
	}


	/**
	 * Liefert den Algorithmus anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Algorithmus oder null, falls die ID ungültig ist
	 */
	public static KlausurterminblockungAlgorithmen get(final int id) {
		return getMapByID().get(id);
	}

	/**
	 * Liefert den Algorithmus anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Algorithmus
	 *
	 * @throws DeveloperNotificationException falls die ID nicht definiert ist
	 */
	public static @NotNull KlausurterminblockungAlgorithmen getOrException(final int id) {
		return DeveloperNotificationException.ifMapGetIsNull(getMapByID(), id);
	}

}
