package de.svws_nrw.core.abschluss.gost;

import java.util.List;
import java.util.ArrayList;

import jakarta.validation.constraints.NotNull;

/**
 * Eine abstrakte Basisklasse für Belegprüfungen auf Abiturdaten eines Schülers.
 * Eine Belegprüfung muss die abstrakten Methode gemäß ihrer Beschreibung implementieren.
 * Die Auswertung der Prüfungsergebnisse kann automatisiert über den zugehörigen
 * AbiturdatenManager erfolgen.
 */
public abstract class GostBelegpruefung {

	/** Eine ggf. zuvor durchgeführte Abitur-Belegprüfung, welche in dieser Belegprüfung als Voraussetzung vorhanden sein muss. */
	protected final @NotNull GostBelegpruefung@NotNull[] pruefungen_vorher;

	/** Der Daten-Manager für die Abiturdaten */
	protected final @NotNull AbiturdatenManager manager;

	/** Die Art der Belegprüfung (nur EF.1, Gesamte Oberstufe, evtl. weitere) */
	protected final @NotNull GostBelegpruefungsArt pruefungs_art;

	/** Ein Set von Belegungsfehlern, die bei der Gesamtprüfung entstanden sind. */
	private final @NotNull ArrayList<@NotNull GostBelegungsfehler> belegungsfehler = new ArrayList<>();


	/**
	 * Erstellt eine neue Belegprüfung, welche den angegebenen Daten-Manager verwendet.
	 *
	 * @param manager           der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt      die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungenVorher   eine vorher durchgeführte Abiturprüfung
	 */
	protected GostBelegpruefung(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt, final GostBelegpruefung... pruefungenVorher) {
		this.pruefungen_vorher = pruefungenVorher;
		this.manager = manager;
		this.pruefungs_art = pruefungsArt;
	}


	/**
	 * Führt eine Belegprüfung durch.
	 */
	public void pruefe() {
		init();
		if (pruefungs_art == GostBelegpruefungsArt.EF1)
			pruefeEF1();
		else if (pruefungs_art == GostBelegpruefungsArt.GESAMT)
			pruefeGesamt();
	}


	/**
	 * Fügt einen Belegungsfehler zu der Belegprüfung hinzu. Diese Methode wird von den Sub-Klassen
	 * aufgerufen, wenn dort ein Belegungsfehler erkannt wird.
	 *
	 * @param fehler   der hinzuzufügende Belegungsfehler
	 */
	protected void addFehler(final @NotNull GostBelegungsfehler fehler) {
		if (!belegungsfehler.contains(fehler))
			belegungsfehler.add(fehler);
	}


	/**
	 * Gibt die Belegungsfehler zurück, welche bei der Gesamtprüfung aufgetreten sind.
	 *
	 * @return die Belegungsfehler
	 */
	public @NotNull ArrayList<@NotNull GostBelegungsfehler> getBelegungsfehler() {
		return belegungsfehler;
	}


	/**
	 * Git zurück, ob ein "echter" Belegungsfehler vorliegt und nicht nur eine Warnung oder ein Hinweis.
	 *
	 * @return true, falls ein "echter" Belegungsfehler vorliegt.
	 */
	public boolean hatBelegungsfehler() {
		for (int i = 0; i < belegungsfehler.size(); i++) {
			final @NotNull GostBelegungsfehler fehler = belegungsfehler.get(i);
			if (!fehler.istInfo())
				return false;
		}
		return true;
	}


	/**
	 * Initialisiert die Daten für die Belegprüfungen mithilfe des Abiturdaten-Managers
	 */
	protected abstract void init();


	/**
	 * Führt alle Belegprüfungen für die EF.1 durch.
	 */
	protected abstract void pruefeEF1();


	/**
	 * Führt alle Belegprüfungen für die gesamte Oberstufe durch.
	 */
	protected abstract void pruefeGesamt();


	/**
	 * Gibt zurück, ob die angegebenen Belegprüfungsfehler einen "echten" Fehler beinhalten
	 * und nicht nur einen Hinweise / eine Information.
	 *
	 * @param alleFehler   die Belegprüfungsfehler und -informationen der durchgeführten Belegprüfungen
	 *
	 * @return true, falls kein "echter" Belegprüfungsfehler aufgetreten ist, sonst false
	 */
	public static boolean istErfolgreich(final @NotNull ArrayList<@NotNull GostBelegungsfehler> alleFehler) {
		for (int i = 0; i < alleFehler.size(); i++) {
			final @NotNull GostBelegungsfehler fehler = alleFehler.get(i);
			if (!fehler.istInfo())
				return false;
		}
		return true;
	}


	/**
	 * Liefert alle Belegprüfungsfehler der übergebenen Teil-Belegprüfungen zurück.
	 * Doppelte Fehler werden dabei nur einfach zurückgegeben (Set).
	 *
	 * @param pruefungen   die durchgeführten Belegprüfungen, deren Fehler zurückgegeben werden sollen.
	 *
	 * @return die Menge der Belegprüfungsfehler
	 */
	public static @NotNull ArrayList<@NotNull GostBelegungsfehler> getBelegungsfehlerAlle(final @NotNull List<@NotNull GostBelegpruefung> pruefungen) {
		final @NotNull ArrayList<@NotNull GostBelegungsfehler> fehler = new ArrayList<>();
		for (int i = 0; i < pruefungen.size(); i++) {
			final @NotNull GostBelegpruefung pruefung = pruefungen.get(i);
			fehler.addAll(pruefung.getBelegungsfehler());
		}
		return fehler;
	}

}
