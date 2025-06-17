package de.svws_nrw.core.abschluss.gost;

import java.util.List;

import de.svws_nrw.core.abschluss.gost.belegpruefung.AbiFaecher;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Projektkurse;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse prüft eine Markierung von Halbjahresbelegungen von anrechenbaren Kursen
 * für die Abiturberechnung.
 */
public final class GostAbiturMarkierungspruefung {

	/** Das Ergebnis der Prüfung */
	private final @NotNull GostAbiturMarkierungspruefungErgebnis ergebnis = new GostAbiturMarkierungspruefungErgebnis();

	/** Die aktuelle Einrückung für das Logging */
	private @NotNull String logIndent = "";

	/** Der Abiturdaten-Manager */
	private final @NotNull AbiturdatenManager manager;

	/** Die zuvor durchgeführten Belegprüfung zu dem Projektkurs */
	private final @NotNull Projektkurse belegpruefungProjektkurse;

	/** Die zuvor durchgeführten Belegprüfung zu den Abiturfächern */
	private final @NotNull AbiFaecher belegpruefungAbiturfaecher;

	/** Die Belegungen der vier Abiturfächer */
	final @NotNull AbiturFachbelegung[] abi = new AbiturFachbelegung[4];


	/**
	 * Erstellt eine neue Instanz des Markierungsalgorithmus unter Verwendung des übergebenen Abiturdaten-Manager und den zuvor
	 * durchgeführten Belegprüfungen.
	 *
	 * @param manager            der Abiturdaten-Manager
	 * @param belegpruefungen    die durchgeführten Belegprüfungen
	 */
	private GostAbiturMarkierungspruefung(final @NotNull AbiturdatenManager manager, final @NotNull List<GostBelegpruefung> belegpruefungen) {
		this.manager = manager;
		this.logIndent = "";
		// Bestimme die zuvor durchgeführten Belegprüfungen zu den Projektkursen, dem Schwerpunkt und den Abiturfächern
		Projektkurse tmpBelegpruefungProjektkurse = null;
		AbiFaecher tmpBelegpruefungAbiturfaecher = null;
		for (final @NotNull GostBelegpruefung pruefung : belegpruefungen) {
			if (pruefung instanceof Projektkurse)
				tmpBelegpruefungProjektkurse = (Projektkurse) pruefung;
			if (pruefung instanceof AbiFaecher)
				tmpBelegpruefungAbiturfaecher = (AbiFaecher) pruefung;
		}
		if (tmpBelegpruefungProjektkurse == null)
			throw new DeveloperNotificationException("Die Projektkursprüfung muss als Belegprüfung vorhanden sein.");
		this.belegpruefungProjektkurse = tmpBelegpruefungProjektkurse;
		if (tmpBelegpruefungAbiturfaecher == null)
			throw new DeveloperNotificationException("Die Abiturfächerprüfung muss als Belegprüfung vorhanden sein.");
		this.belegpruefungAbiturfaecher = tmpBelegpruefungAbiturfaecher;
		abi[0] = this.belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.LK1);
		abi[1] = this.belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.LK2);
		abi[2] = this.belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.AB3);
		abi[3] = this.belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.AB4);
	}


	/**
	 * Führt eine Prüfung der Markierung von Halbjahresbelegungen zur Verwendung in Block II
	 * von anrechenbaren Kursen für die Abiturberechnung durch. Vorraussetzung hierfür ist, dass
	 * alle anrechenbare Kurse ein gültige Note zugeordnet haben.
	 *
	 * @param manager           der Manager zur Verwaltung der Abiturdaten.
	 * @param belegpruefungen   die zuvor durchgeführten Belegprüfung der Laufbahnplanung
	 *
	 * @return das Ergebnis der Prüfung
	 */
	public static @NotNull GostAbiturMarkierungspruefungErgebnis pruefe(final @NotNull AbiturdatenManager manager,
			final @NotNull List<GostBelegpruefung> belegpruefungen) {
		final @NotNull GostAbiturMarkierungspruefung pruefung = new GostAbiturMarkierungspruefung(manager, belegpruefungen);
		pruefung.ergebnis.erfolgreich = pruefung.pruefung();
		return pruefung.ergebnis;
	}


	/**
	 * Führt die Prüfung der aktuellen Markierung der Abiturdaten des Abiturdaten-Manager durch.
	 *
	 * @return true, falls die Prüfung erfolgreich war, und ansonsten false
	 */
	private boolean pruefung() {
		boolean success = true;

		// Schritt 1: Leistungskurs-Defizite prüfen
		success = pruefeLeistungskursDefizite();
		if (!success)
			return success;

		// Schritt 2: Prüfe den Abiturbereich auf 0-Punkte-Belegungen
		success = pruefeAbiturfachNullPunkte();
		if (!success)
			return success;

		// Schritt 3: Prüfe auf Markierung aller Abiturfächer
		success = pruefeAbiturfachMarkierung();
		if (!success)
			return success;

		// Schritt 4: Prüfe auf Markierung von Deutsch
		success = pruefeDeutschMarkierung();
		if (!success)
			return success;

		// TODO

		return success;
	}


	private boolean pruefeLeistungskursDefizite() {
		if ((abi[0] == null) || (abi[1] == null)) {
			ergebnis.log.add("Es müssen zwei Leistungskurse als Abiturfächer gewählt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		int defiziteLK = 0;
		for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
			if ((abi[0].belegungen[halbjahr.id] == null) || (abi[1].belegungen[halbjahr.id] == null)) {
				ergebnis.log.add(
						"Beide Leistungskurse müssen im Halbjahr %s belegt sein, damit eine Abiturzulassung möglich ist.".formatted(halbjahr.kuerzel));
				return false;
			}
			final Integer np1 = manager.getNotenpunkteOfFachbelegungHalbjahr(abi[0].belegungen[halbjahr.id]);
			final Integer np2 = manager.getNotenpunkteOfFachbelegungHalbjahr(abi[1].belegungen[halbjahr.id]);
			if ((np1 == null) || (np2 == null)) {
				ergebnis.log.add("Beide Leistungskurse müssen im Halbjahr %s bewertet sein, damit eine Abiturzulassung möglich ist."
						.formatted(halbjahr.kuerzel));
				return false;
			}
			if (np1 < 5)
				defiziteLK++;
			if (np2 < 5)
				defiziteLK++;
		}
		if (defiziteLK > 3) {
			ergebnis.log.add("Es liegen mehr als drei Leistungskursdefizite vor. Keine Zulassung zum Abitur.");
			return false;
		}
		return true;
	}


	private boolean pruefeAbiturfachNullPunkte() {
		if ((abi[0] == null) || (abi[1] == null) || (abi[2] == null) || (abi[3] == null)) {
			ergebnis.log.add("Es müssen vier Abiturfächer gewählt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
			if ((abi[0].belegungen[halbjahr.id] == null) || (abi[1].belegungen[halbjahr.id] == null)
					|| (abi[2].belegungen[halbjahr.id] == null) || (abi[3].belegungen[halbjahr.id] == null)) {
				ergebnis.log.add("Alle Abiturfächer müssen im Halbjahr %s belegt sein, damit eine Abiturzulassung möglich ist."
						.formatted(halbjahr.kuerzel));
				return false;
			}
			final Integer np1 = manager.getNotenpunkteOfFachbelegungHalbjahr(abi[0].belegungen[halbjahr.id]);
			final Integer np2 = manager.getNotenpunkteOfFachbelegungHalbjahr(abi[1].belegungen[halbjahr.id]);
			final Integer np3 = manager.getNotenpunkteOfFachbelegungHalbjahr(abi[2].belegungen[halbjahr.id]);
			final Integer np4 = manager.getNotenpunkteOfFachbelegungHalbjahr(abi[3].belegungen[halbjahr.id]);
			if ((np1 == null) || (np2 == null) || (np3 == null) || (np4 == null)) {
				ergebnis.log.add("Alle Abiturfächer müssen im Halbjahr %s bewertet sein, damit eine Abiturzulassung möglich ist."
						.formatted(halbjahr.kuerzel));
				return false;
			}
			if ((np1 == 0) || (np2 == 0) || (np3 == 0) || (np4 == 0)) {
				ergebnis.log.add("Abiturfächer mit 0 Notenpunkten gelten als nicht belegt. Keine Zulassung zum Abitur.");
				return false;
			}
		}
		return true;
	}


	private boolean pruefeAbiturfachMarkierung() {
		if ((abi[0] == null) || (abi[1] == null) || (abi[2] == null) || (abi[3] == null)) {
			ergebnis.log.add("Es müssen vier Abiturfächer gewählt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		for (int i = 1; i < 5; i++) {
			if (!manager.hatMarkierungQualifikationsphase(abi[i - 1])) {
				ergebnis.log.add("Es müssen alle Abiturfächer durchgehend markiert sein. Dies ist mindestens bei dem %d. Abiturfach nicht der Fall."
						.formatted(i));
				return false;
			}
		}
		return true;
	}


	private boolean pruefeDeutschMarkierung() {
		final AbiturFachbelegung belegung = manager.getFachbelegung(GostFachbereich.DEUTSCH);
		if (belegung == null) {
			ergebnis.log.add("Deutsch muss belegt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
			if (belegung.belegungen[halbjahr.id] == null) {
				ergebnis.log.add("Deutsch mussen im Halbjahr %s belegt sein, damit eine Abiturzulassung möglich ist.".formatted(halbjahr.kuerzel));
				return false;
			}
			final Integer np = manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
			if (np == null) {
				ergebnis.log.add("Deutsch muss im Halbjahr %s bewertet sein, damit eine Abiturzulassung möglich ist.".formatted(halbjahr.kuerzel));
				return false;
			}
			if (np == 0) {
				ergebnis.log.add(
						"Deutsch wurde im Halbjahr %s mit 0 Notenpunkten bewertet und gilt damit als nicht belegt. Eine Abiturzulassung ist nicht möglich."
								.formatted(halbjahr.kuerzel));
				return false;
			}
			if (!manager.hatMarkierungQualifikationsphase(belegung)) {
				ergebnis.log.add("Deutsch muss durchgehend markiert sein.");
				return false;
			}
		}
		return true;
	}

}
