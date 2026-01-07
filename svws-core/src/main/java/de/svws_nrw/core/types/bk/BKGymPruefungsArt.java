package de.svws_nrw.core.types.bk;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Eine Aufzählung zur Unterscheidung der Prüfungsarten (Belegung, Zulassung, Nachprüfung, Bestehen).
 */
public enum BKGymPruefungsArt {

	/** Prüfung der Belegung von Kursen*/
	BELEGUNG("BELEGUNG"),

	/** Markierung von Kursen*/
	MARKIERUNG("MARKIERUNG"),

	/** Prüfung der Zulassung zur Abiturprüfung - entspricht Markierungsbedingungen */
	ZULASSUNG("ZULASSUNG"),

	/** Prüfung des Abiturergebnisses auf notwendige Nachprüfungen*/
	NACHPRUEFUNG("NACHPRUEFUNG"),

	/** Prüfung ob das Abitur nach Ablegen aller Prüfungsbestandteile bestanden wurde. */
	BESTEHEN("BESTEHEN");

	/** Das Kürzel für die Prüfungsart */
	public final @NotNull String kuerzel;


	/**
	 * Erzeugt ein neues Abitur-Pruefungsart-Objekt
	 *
	 * @param kuerzel        das Kürzel der Prüfungsart
	 */
	BKGymPruefungsArt(final @NotNull String kuerzel) {
		this.kuerzel = kuerzel;
	}


	/**
	 * Gibt die Prüfungsart anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Prüfungsart
	 *
	 * @return die Prüfungsart
	 */
	public static BKGymPruefungsArt fromKuerzel(final @NotNull String kuerzel) {
		return switch (kuerzel) {
			case "BELEGUNG" -> BELEGUNG;
			case "ZULASSUNG" -> ZULASSUNG;
			case "NACHPRUEFUNG" -> NACHPRUEFUNG;
			case "BESTEHEN" -> BESTEHEN;
			default -> throw new DeveloperNotificationException("Die Prüfungsart " + kuerzel + " gibt es nicht.");
		};
	}


	@Override
	public @NotNull String toString() {
		return kuerzel;
	}

}
