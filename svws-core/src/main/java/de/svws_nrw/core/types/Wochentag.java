package de.svws_nrw.core.types;

import java.util.HashMap;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Aufzählung für die Wochentage in dar.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen als auch für die OpenAPI-Schnittstelle.
 *
 * @author Benjamin A. Bartsch
 */
public enum Wochentag {

	/** Montag */
	MONTAG(1, "Montag", "Mo"),

	/** Dienstag */
	DIENSTAG(2, "Dienstag", "Di"),

	/** Mittwoch */
	MITTWOCH(3, "Mittwoch", "Mi"),

	/** Donnerstag */
	DONNERSTAG(4, "Donnerstag", "Do"),

	/** Freitag */
	FREITAG(5, "Freitag", "Fr"),

	/** Samstag */
	SAMSTAG(6, "Samstag", "Sa"),

	/** Sonntag */
	SONNTAG(7, "Sonntag", "So");

	/** Mapping von der ID zum Objekt Wochentag. */
	private static final @NotNull HashMap<@NotNull Integer, @NotNull Wochentag> _map_id_zu_wochentag = new HashMap<>();

	/** Die eindeutige ID der Kursart der Gymnasialen Oberstufe*/
	public final @NotNull int id;

	/** Die textuelle Beschreibung der allgemeinen Kursart der Gymnasialen Oberstufe */
	public final @NotNull String beschreibung;

	/** Das Kürzel der Kursart der Gymnasialen Oberstufe */
	public final @NotNull String kuerzel;

	/**
	 * Erzeugt einen neuen Wochentag für die Aufzählung.
	 *
	 * @param id             die eindeutige ID der Kursart der Gymnasialen Oberstufe
	 * @param kuerzel        das Kürzel der Kursart der Gymnasialen Oberstufe
	 * @param beschreibung   die textuelle Beschreibung der allgemeinen Kursart der Gymnasialen Oberstufe
	 */
	Wochentag(final @NotNull int id, final @NotNull String beschreibung, final @NotNull String kuerzel) {
		this.id = id;
		this.beschreibung = beschreibung;
		this.kuerzel = kuerzel;
	}

	@Override
	public @NotNull String toString() {
		return beschreibung;
	}

	/**
	 * Liefert das {@link Wochentag}-Objekt anhand seiner ID.
	 *
	 * @param id  Die ID des Wochentages.
	 *
	 * @return das {@link Wochentag}-Objekt anhand seiner ID.
	 * @throws DeveloperNotificationException falls die ID ungültig ist
	 */
	public static @NotNull Wochentag fromIDorException(final int id) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(getMapIDZuWochentag(), id);
	}

	/**
	 * Liefert eine Map, welche die ID dem {@link Wochentag}-Objekt zuordnet.
	 * Eine leere Map wird dabei zuvor initialisiert.
	 *
	 * @return eine Map, welche die ID dem {@link Wochentag}-Objekt zuordnet.
	 */
	private static @NotNull HashMap<@NotNull Integer, @NotNull Wochentag> getMapIDZuWochentag() {
		if (_map_id_zu_wochentag.isEmpty())
			for (final @NotNull Wochentag wt : Wochentag.values())
				_map_id_zu_wochentag.put(wt.id, wt);
		return _map_id_zu_wochentag;
	}

}
