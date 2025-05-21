package de.svws_nrw.core.abschluss.bk.d;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Die abstrakte Klasse für die Belegprüfungen bei Bildungsgängen.
 */
public abstract class BKGymBelegpruefung {

	/** Die Abiturdaten-Manager */
	protected final @NotNull BKGymAbiturdatenManager manager;

	/** Die Belegungsfehler, die bei der Prüfung entstanden sind. */
	private final @NotNull List<BKGymBelegungsfehler> belegungsfehler = new ArrayList<>();


	/**
	 * Erzeugt eine neue Belegprüfung mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Abiturdaten
	 */
	public BKGymBelegpruefung(final @NotNull BKGymAbiturdatenManager manager) {
		this.manager = manager;
	}


	/**
	 * Fügt einen Belegungsfehler zu der Belegprüfung hinzu. Diese Methode wird von den Sub-Klassen
	 * aufgerufen, wenn dort ein Belegungsfehler erkannt wird.
	 *
	 * @param fehler   der hinzuzufügende Belegungsfehler
	 */
	protected void addFehler(final @NotNull BKGymBelegungsfehler fehler) {
		if (!belegungsfehler.contains(fehler))
			belegungsfehler.add(fehler);
	}


	/**
	 * Gibt die Belegungsfehler zurück, welche bei der Prüfung aufgetreten sind.
	 *
	 * @return die Belegungsfehler
	 */
	public @NotNull List<BKGymBelegungsfehler> getBelegungsfehler() {
		return belegungsfehler;
	}


	/**
	 * Gibt zurück, ob ein "echter" Belegungsfehler vorliegt und nicht nur eine Warnung oder ein Hinweis.
	 *
	 * @return true, wenn kein "echter" Belegungsfehler vorliegt, und ansonsten false.
	 */
	public boolean istErfolgreich() {
		for (final @NotNull BKGymBelegungsfehler fehler : belegungsfehler)
			if (!fehler.istInfo())
				return false;
		return true;
	}


	/**
	 * Führt die Belegprüfung durch.
	 */
	public abstract void pruefe();


	/**
	 * Erstellt eine Belegprüfung zu einer Fachklasse in der Schulgliederung D01.
	 *
	 * @param manager   der Manager für die Abiturdaten
	 *
	 * @return der Belegprüfungsalgorithmus
	 */
	private static @NotNull BKGymBelegpruefung createPruefungD01(final @NotNull BKGymAbiturdatenManager manager) {
		final @NotNull String fks = manager.getFachklassenschluessel();
		return switch (fks) {
			case "10600" -> new BKGymBelegpruefungD01_10600(manager);
			default -> throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + manager.getGliederung().name()
					+ " und den Fachklassenschlüssel " + manager.getFachklassenschluessel() + " wird noch nicht unterstützt.");
		};
	}


	/**
	 * Erstellt die zugehörige Belegprüfung mit den Abiturdaten anhand des übergebenen Bildungsganges.
	 *
	 * @param manager   der Manager für die Abiturdaten
	 *
	 * @return der Belegprüfungsalgorithmus
	 */
	public static @NotNull BKGymBelegpruefung getPruefung(final @NotNull BKGymAbiturdatenManager manager) {
		final @NotNull Schulgliederung sgl = manager.getGliederung();
		final @NotNull BKGymBelegpruefung pruefung = switch (sgl) {
			case D01 -> createPruefungD01(manager);
			default ->
				throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + manager.getGliederung().name() + " wird noch nicht unterstützt.");
		};
		return pruefung;
	}

}
