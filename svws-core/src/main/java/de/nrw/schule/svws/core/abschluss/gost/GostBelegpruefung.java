package de.nrw.schule.svws.core.abschluss.gost;

import java.util.List;
import java.util.Vector;

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
	private final @NotNull Vector<@NotNull GostBelegungsfehler> belegungsfehler = new Vector<>();


	/**
	 * Erstellt eine neue Belegprüfung, welche den angegebenen Daten-Manager verwendet.
	 * 
	 * @param manager           der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art     die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungen_vorher   eine vorher durchgeführte Abiturprüfung
	 */
	protected GostBelegpruefung(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungs_art, final GostBelegpruefung... pruefungen_vorher) {
		this.pruefungen_vorher = pruefungen_vorher;
		this.manager = manager;
		this.pruefungs_art = pruefungs_art;
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
	protected void addFehler(@NotNull GostBelegungsfehler fehler) {
		if (!belegungsfehler.contains(fehler))
			belegungsfehler.add(fehler);
	}
	

	/**
	 * Gibt die Belegungsfehler zurück, welche bei der Gesamtprüfung aufgetreten sind.
	 * 
	 * @return die Belegungsfehler
	 */
	public @NotNull Vector<@NotNull GostBelegungsfehler> getBelegungsfehler() {
		return belegungsfehler;
	}
	
	
	/**
	 * Git zurück, ob ein "echter" Belegungsfehler vorliegt und nicht nur eine Warnung oder ein Hinweis.
	 * 
	 * @return true, falls ein "echter" Belegungsfehler vorliegt.
	 */
	public boolean hatBelegungsfehler() {
		for (int i = 0; i < belegungsfehler.size(); i++) {
			@NotNull GostBelegungsfehler fehler = belegungsfehler.get(i);
			if (!fehler.istInfo())
				return false;
		}
		return true;
	}


	/**
	 * Initialisiert die Daten für die Belegprüfungen mithilfe des Abiturdaten-Managers
	 */
	abstract protected void init();
	
	
	/**
	 * Führt alle Belegprüfungen für die EF.1 durch.
	 */
	abstract protected void pruefeEF1();
	
	
	/**
	 * Führt alle Belegprüfungen für die gesamte Oberstufe durch.
	 */
	abstract protected void pruefeGesamt();


	/**
	 * Gibt zurück, ob die angegebenen Belegprüfungsfehler einen "echten" Fehler beinhalten 
	 * und nicht nur einen Hinweise / eine Information.
	 * 
	 * @param alle_fehler   die Belegprüfungsfehler und -informationen der durchgeführten Belegprüfungen
	 * 
	 * @return true, falls kein "echter" Belegprüfungsfehler aufgetreten ist, sonst false
	 */
	public static boolean istErfolgreich(@NotNull Vector<@NotNull GostBelegungsfehler> alle_fehler) {
		for (int i = 0; i < alle_fehler.size(); i++) {
			@NotNull GostBelegungsfehler fehler = alle_fehler.get(i);
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
	public static @NotNull Vector<@NotNull GostBelegungsfehler> getBelegungsfehlerAlle(@NotNull List<@NotNull GostBelegpruefung> pruefungen) {
		@NotNull Vector<@NotNull GostBelegungsfehler> fehler = new Vector<>();
		for (int i = 0; i < pruefungen.size(); i++) {
			@NotNull GostBelegpruefung pruefung = pruefungen.get(i);
			fehler.addAll(pruefung.getBelegungsfehler());
		}
		return fehler;
	}
	
}
