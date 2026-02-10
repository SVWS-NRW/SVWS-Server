package de.svws_nrw.core.types.bk;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Eine Aufzählung zur Unterscheidung der Fehlerarten (Belegungsfehler, Schriftlichkeit und Hinweis).
 */
public enum BKGymBelegungsfehlerArt {

	/** Belegungsfehler */
	BELEGUNG("BELEGUNG"),

	/** Fehler bei der Schriftlichkeit */
	SCHRIFTLICHKEIT("SCHRIFTLICHKEIT"),

	/** Fehler in Bezug auf schulische Rahmenbedingungen - Zusatzkursbeginn oder nicht erlaubte oder geforderte Fachkombinationen */
	SCHULSPEZIFISCH("SCHULSPEZIFISCH"),

	/** Information, aber kein Fehler */
	HINWEIS("HINWEIS");


	/** Das Kürzel für die Belegungsfehlerart */
	public final @NotNull String kuerzel;


	/**
	 * Erzeugt ein neues Abitur-Belegungsfehler-Objekt
	 *
	 * @param kuerzel        das Kürzel der Fehler-Art
	 */
	BKGymBelegungsfehlerArt(final @NotNull String kuerzel) {
		this.kuerzel = kuerzel;
	}


	/**
	 * Gibt die Belegungsfehler-Art anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Belegungsfehler-Art
	 *
	 * @return die Belegungsfehler-Art
	 */
	public static @NotNull BKGymBelegungsfehlerArt fromKuerzel(final @NotNull String kuerzel) {
		return switch (kuerzel) {
			case "BELEGUNG" -> BELEGUNG;
			case "SCHRIFTLICHKEIT" -> SCHRIFTLICHKEIT;
			case "SCHULSPEZIFISCH" -> SCHULSPEZIFISCH;
			case "HINWEIS" -> HINWEIS;
			default -> throw new DeveloperNotificationException("Die Belegungsfehlerart " + kuerzel + " gibt es nicht.");
		};
	}


	@Override
	public @NotNull String toString() {
		return kuerzel;
	}

}
