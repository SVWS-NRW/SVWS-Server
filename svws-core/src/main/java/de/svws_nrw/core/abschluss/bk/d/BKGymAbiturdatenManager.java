package de.svws_nrw.core.abschluss.bk.d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.data.bk.abi.BKGymFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.bk.BKGymFaecherManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Methoden zur Verfügung um die angegebenen Abiturdaten zu bearbeiten und Auswertungen durchzuführen.
 */
public class BKGymAbiturdatenManager {

	/** Die Abiturdaten des Schülers */
	private final @NotNull BKGymAbiturdaten abidaten;

	/** Die Schulgliederung des Bildungsgangs des Schülers */
	private final @NotNull Schulgliederung gliederung;

	/** Der Fachklassen-Schlüssel des Bildungsgangs des Schülers */
	private final @NotNull String fks;

	/** Der Manager für die Fächer des beruflichen Gymnasiums */
	private final @NotNull BKGymFaecherManager faecherManager;

	/** Das Halbjahr, bis zu welchem die Belegprüfung durchgeführt werden soll */
	private final @NotNull GostHalbjahr bisHalbjahr;

	/** Der Belegprüfungsalgorithmus */
	private final @NotNull BKGymBelegpruefung belegpruefung;

	/** Eine HashMap, welche den schnellen Zugriff auf die Fachbelegungen für ein Fach anhand der Bezeichnung ermöglicht */
	private final @NotNull Map<String, BKGymAbiturFachbelegung> mapFachbelegungenByFachbezeichnung = new HashMap<>();

	/** Die Menge der Belegprüfungsfehler, die bei den durchgeführten Belegprüfungen aufgetreten sind. */
	private @NotNull List<BKGymBelegungsfehler> belegpruefungsfehler = new ArrayList<>();

	/** Gibt an, ob die Belegprüfung insgesamt erfolgreich war oder nicht. */
	private boolean belegpruefungErfolgreich = false;


	/**
	 * Erstellt ein neues Manager-Objekt, welches mit den übergebenen Abiturdaten verknüpft wird.
	 *
	 * @param abidaten         die Abiturdaten des Schülers
	 * @param gliederung       die Schulgliederung des Bildungsgangs des Schülers
	 * @param fks              der fünfstellige Fachklassenschlüssel des Bildungsgangs des Schülers
	 * @param faecherManager   der Manager für die Fächer
	 * @param bisHalbjahr      die Art der Belegpruefung - bis zu welchem Halbjahr geprüft werden soll
	 */
	public BKGymAbiturdatenManager(final @NotNull BKGymAbiturdaten abidaten, final @NotNull Schulgliederung gliederung, final @NotNull String fks,
			final @NotNull BKGymFaecherManager faecherManager, final @NotNull GostHalbjahr bisHalbjahr) {
		this.abidaten = abidaten;
		this.gliederung = gliederung;
		this.fks = fks;
		this.faecherManager = faecherManager;
		this.bisHalbjahr = bisHalbjahr;
		this.belegpruefung = getBelegpruefung();
		init();
		this.belegpruefung.pruefe();
		belegpruefungsfehler = this.belegpruefung.getBelegungsfehler();
		belegpruefungErfolgreich = this.belegpruefung.istErfolgreich();
	}


	/**
	 * Initialisiert bzw. reinitialisert die Datenstrukturen, die für den schnellen Zugriff auf die Daten
	 * eingerichtet werden.
	 */
	public void init() {
		// Leere die HashMaps und erstelle ggf. neue Listen für die Zuordnung von Abitur-Fachbelegungen
		mapFachbelegungenByFachbezeichnung.clear();

		// Durchwandere alle belegten Fächer und weise diese den Fachbereichen zu
		final @NotNull List<BKGymAbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final BKGymAbiturFachbelegung fachbelegung : fachbelegungen) {
			final BKGymFach fach = faecherManager.get(fachbelegung.fachID);
			if ((fach == null) || (fach.bezeichnung == null))
				continue;
			mapFachbelegungenByFachbezeichnung.put(fach.bezeichnung, fachbelegung);
		}
	}


	/**
	 * Erstellt eine Belegprüfung zu einer Fachklasse in der Schulgliederung D01.
	 *
	 * @return der Belegprüfungsalgorithmus
	 */
	private @NotNull BKGymBelegpruefung createBelegpruefungD01() {
		return switch (fks) {
			case "10600" -> new BKGymBelegpruefungD01_10600(this);
			default -> throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + gliederung.name()
					+ " und den Fachklassenschlüssel " + fks + " wird noch nicht unterstützt.");
		};
	}


	/**
	 * Erstellt die zugehörige Belegprüfung mit den Abiturdaten anhand des übergebenen Bildungsganges.
	 *
	 * @return der Belegprüfungsalgorithmus
	 */
	private @NotNull BKGymBelegpruefung getBelegpruefung() {
		final @NotNull BKGymBelegpruefung pruefung = switch (gliederung) {
			case D01 -> createBelegpruefungD01();
			default ->
				throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + gliederung.name() + " wird noch nicht unterstützt.");
		};
		return pruefung;
	}


	/**
	 * Getter für den Zugriff auf die Abiturdaten
	 *
	 * @return die Abiturdaten
	 */
	public @NotNull BKGymAbiturdaten getAbidaten() {
		return abidaten;
	}


	/**
	 * Getter für den Zugriff auf das Halbjahr, bis zu welchem geprüft werde soll
	 *
	 * @return das Halbjahr
	 */
	public @NotNull GostHalbjahr getBisHalbjahr() {
		return bisHalbjahr;
	}


	/**
	 * Getter für den Zugriff auf die Schulgliederung des Bildungsganges
	 *
	 * @return die Schulgliederung des Bildungsganges
	 */
	public @NotNull Schulgliederung getGliederung() {
		return gliederung;
	}


	/**
	 * Getter für den Zugriff auf den Fachklassenschlüssel des Bildungsganges
	 *
	 * @return der Fachklassenschlüssel des Bildungsganges
	 */
	public @NotNull String getFachklassenschluessel() {
		return fks;
	}


	/**
	 * Gibt das Ergebnis der Belegprüfung zurück. Dieses enthält eine Liste der Fehler, die bei der Belegprüfung
	 * festgestellt wurden und ob diese erfolgreich gewesen ist oder nicht.
	 *
	 * @return das Ergebnis der Belegprüfung
	 */
	public @NotNull BKGymBelegpruefungErgebnis getBelegpruefungErgebnis() {
		final @NotNull BKGymBelegpruefungErgebnis ergebnis = new BKGymBelegpruefungErgebnis();
		ergebnis.erfolgreich = belegpruefungErfolgreich;
		for (int i = 0; i < belegpruefungsfehler.size(); i++) {
			final @NotNull BKGymBelegungsfehler fehler = belegpruefungsfehler.get(i);
			ergebnis.fehlercodes.add(new BKGymBelegpruefungErgebnisFehler(fehler));
		}
		return ergebnis;
	}

}
