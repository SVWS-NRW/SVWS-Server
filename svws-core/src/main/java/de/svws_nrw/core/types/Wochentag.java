package de.svws_nrw.core.types;

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

	/** Die eindeutige ID des Wochentags*/
	public final @NotNull int id;

	/** Der voll ausgeschriebene Wochentag, z.B. Montag*/
	public final @NotNull String beschreibung;

	/** Das Kürzel des Wochentags, z.B. Mo */
	public final @NotNull String kuerzel;

	/**
	 * Erzeugt einen neuen Wochentag für die Aufzählung.
	 *
	 * @param id             die eindeutige ID des Wochentags
	 * @param kuerzel        das Kürzel des Wochentags
	 * @param beschreibung   der ausgeschriebene Wochentag
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
	    DeveloperNotificationException.ifTrue("Der Wochentag(" + id + ") muss zwischen 1 (Montag) und 7 (Sonntag) liegen!", id < 1 || id > 7);
	    final @NotNull Wochentag @NotNull[] wochentage = values();
	    return wochentage[id - 1];
	}

}
