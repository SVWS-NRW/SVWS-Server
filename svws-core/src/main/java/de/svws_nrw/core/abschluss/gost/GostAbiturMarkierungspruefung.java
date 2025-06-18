package de.svws_nrw.core.abschluss.gost;

import java.util.List;

import de.svws_nrw.core.abschluss.gost.belegpruefung.AbiFaecher;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Projektkurse;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.gost.GostFachUtils;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
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

	/** Die Belegung einer (ersten) vollständig markierten Fremdsprache (keine Bili-Sachfach!) */
	AbiturFachbelegung fremdsprache = null;

	/** Gibt an, ob eine weitere Fremdsprache neben der ersten gefunden wurde (Bili-Sachfach ist möglich) */
	boolean hatWeitereFremdsprache = false;


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
		this.fremdsprache = null;

		// Schritt 1: Leistungskurs-Defizite prüfen
		boolean success = pruefeLeistungskursDefizite();

		// Schritt 2: Prüfe den Abiturbereich auf 0-Punkte-Belegungen
		success = success && pruefeAbiturfachNullPunkte();

		// Schritt 3: Prüfe auf Markierung aller Abiturfächer
		success = success && pruefeAbiturfachMarkierung();

		// Schritt 4: Prüfe auf Markierung von Deutsch
		success = success && pruefeDeutschMarkierung();

		// Schritt 5: Prüfe auf Markierung einer Fremdsprache (diese wird zwischengespeichert)
		success = success && pruefeFremdsprachenMarkierung();

		// evtl. die Prüfung schon beenden
		if (!success)
			return success;

		// Schritt 6: Prüfe, ob eine weitere Fremdsprache oder ein Bili-Sachfach markiert wurde
		pruefeAufWeitereFremdsprache();

		// Schritt 7: Prüfe ggf. auf zwei Kurse einer neu einsetzenden Fremdsprache, wenn keine zweite Fremdsprache in der Sek I belegt wurde
		success = pruefeNeuEinsetzendeFremdsprache();

		// Schritt 8: Prüfe, ob mindestes 2 Kurse in Kunst, Musik oder einem Ersatzfach markiert sind
		success = success && pruefeKunstMusikOderErsatzMarkierung();

		// Schritt 9: Prüfe, ob bei einem Ersatzfach für Kunst oder Musik maximal 2 Kurse markiert sind
		success = success && pruefeKunstMusikErsatzAnzahlMarkierung();

		// Schritt 10: Prüfe die Anzahl der Markierungen von Musik je nach ob Musik LK-Abiturfach, GK-Abiturfach oder kein Abiturfach ist
		success = success && pruefeMusikAnzahlMarkierung();

		// Schritt 11: Prüfe auf mind. zwei markierte Kurse in Geschichte
		success = success && pruefeAnzahlMarkierungen(GostFachbereich.GESCHICHTE, 2, "Es müssen mindestens zwei Kurse in Geschichte markiert werden.");

		// Schritt 12: Prüfe auf mind. zwei markierte Kurse in Geschichte
		success = success && pruefeAnzahlMarkierungen(GostFachbereich.SOZIALWISSENSCHAFTEN, 2,
				"Es müssen mindestens zwei Kurse in Sozialwissenschaften markiert werden.");

		// Schritt 13: Prüfe, ob eine Gesellschaftswissenschaft mit Markierungen in allen Halbjahren der QPhase existiert
		success = success && pruefeExistiertMitAnzahlMarkierungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH, 4,
				"Es muss ein gesellschaftswissenschaftliches Fach durchgängig markiert sein.");

		// evtl. die Prüfung schon beenden
		if (!success)
			return success;

		// TODO
		success = false;

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
			if (!manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
				ergebnis.log.add("Deutsch muss durchgehend markiert sein.");
				return false;
			}
		}
		return true;
	}


	private boolean pruefeFremdsprachenMarkierung() {
		final @NotNull List<AbiturFachbelegung> belegungen = manager.getFachbelegungen(GostFachbereich.FREMDSPRACHE);
		if (belegungen.isEmpty()) {
			ergebnis.log.add("Es muss mindestens eine Fremdsprache belegt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			boolean found = true;
			for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				// Alle Halbjahre müssen belegt sein
				if (belegung.belegungen[halbjahr.id] == null) {
					found = false;
					break;
				}
				// Es darf keine 0-Punkte-Belegung markiert werden
				final Integer np = manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
				if ((np == null) || (np == 0)) {
					found = false;
					break;
				}
				// und es muss eine Markierung vorliegen
				if (!manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
					found = false;
					break;
				}
			}
			if (found) {
				this.fremdsprache = belegung;
				return true;
			}
		}
		ergebnis.log.add("Es muss mindestens eine Fremdsprache durchgängig markiert sein, damit eine Abiturzulassung möglich ist.");
		return false;
	}


	private void pruefeAufWeitereFremdsprache() {
		this.hatWeitereFremdsprache = false;
		// Prüfe zunächst die normalen Fremdsprachen
		@NotNull List<AbiturFachbelegung> belegungen = manager.getFachbelegungen(GostFachbereich.FREMDSPRACHE);
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			// Aber nicht die Fremdsprache, die als erstes gefunden wurde
			if (belegung == this.fremdsprache)
				continue;
			boolean found = true;
			for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				// Alle Halbjahre müssen belegt sein
				if (belegung.belegungen[halbjahr.id] == null) {
					found = false;
					break;
				}
				// Es darf keine 0-Punkte-Belegung markiert werden
				final Integer np = manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
				if ((np == null) || (np == 0)) {
					found = false;
					break;
				}
				// und es muss eine Markierung vorliegen
				if (!manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
					found = false;
					break;
				}
			}
			if (found) {
				this.hatWeitereFremdsprache = true;
				return;
			}
		}
		// Prüfe dann auf Bili-Sachfächer, deren Unterrichtssprache nicht im ersten Schritt markiert wurde
		belegungen = manager.getFachbelegungenBilingual();
		if (!belegungen.isEmpty()) {
			// Bestimme das Sprachkürzel der ersten Fremdsprache
			final GostFach tmpFach = manager.getFach(this.fremdsprache);
			final String fs = (tmpFach == null) ? "" : GostFachUtils.getFremdsprache(tmpFach);
			for (final @NotNull AbiturFachbelegung belegung : belegungen) {
				// Aber kein bilinguales Sachfach, welches als Sprache die erste Fremdsprache hat
				final GostFach sachfach = manager.getFach(this.fremdsprache);
				if ((sachfach == null) || (sachfach.biliSprache == null) || (sachfach.biliSprache.isBlank()) || (sachfach.biliSprache.equals(fs)))
					continue;
				boolean found = true;
				for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
					// Alle Halbjahre müssen belegt sein
					if (belegung.belegungen[halbjahr.id] == null) {
						found = false;
						break;
					}
					// Es darf keine 0-Punkte-Belegung markiert werden
					final Integer np = manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
					if ((np == null) || (np == 0)) {
						found = false;
						break;
					}
					// und es muss eine Markierung vorliegen
					if (!manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
						found = false;
						break;
					}
				}
				if (found) {
					this.hatWeitereFremdsprache = true;
					return;
				}
			}
		}
	}


	private boolean pruefeNeuEinsetzendeFremdsprache() {
		// Prüfe, ob eine zweite Fremdsprache in der Sek I vorhanden ist
		final String fs2 = SprachendatenUtils.getZweiteSpracheInSekI(manager.getSprachendaten());
		if (fs2 != null)
			return true;
		// Prüfe, ob eine neu einsetzende Fremdsprachenbelegung in Q2.1 und Q2.2 markiert wurde
		final @NotNull List<AbiturFachbelegung> belegungen =
				manager.filterFremdspracheNeuEinsetzend(manager.getFachbelegungen(GostFachbereich.FREMDSPRACHE));
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			// Aber nicht die Fremdsprache, die als erstes gefunden wurde
			if (belegung == this.fremdsprache)
				continue;
			boolean found = true;
			for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				// Alle Halbjahre müssen belegt sein
				if (belegung.belegungen[halbjahr.id] == null) {
					found = false;
					break;
				}
				// Es darf keine 0-Punkte-Belegung markiert werden
				final Integer np = manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
				if ((np == null) || (np == 0)) {
					found = false;
					break;
				}
				// und es muss eine Markierung vorliegen
				if (((halbjahr == GostHalbjahr.Q21) || (halbjahr == GostHalbjahr.Q22)) && (!manager.hatMarkierungHalbjahr(belegung, halbjahr))) {
					found = false;
					break;
				}
			}
			if (found)
				return true;
		}
		ergebnis.log.add("Es muss eine neu einsetzende Fremdsprache in der Q2.1 und Q2.2 markiert sein, damit eine Abiturzulassung möglich ist.");
		return false;
	}


	private boolean pruefeKunstMusikOderErsatzMarkierung() {
		final @NotNull List<AbiturFachbelegung> belegungen = manager.getFachbelegungen(GostFachbereich.LITERARISCH_KUENSTLERISCH);
		if (belegungen.isEmpty()) {
			ergebnis.log.add("Es muss mindestens Kunst, Musik oder ein Ersatzfach belegt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			int kurseMarkiert = 0;
			for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				// Alle Halbjahre müssen belegt sein
				if (belegung.belegungen[halbjahr.id] == null)
					continue;
				// Es darf keine 0-Punkte-Belegung markiert werden
				final Integer np = manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
				if ((np == null) || (np == 0))
					continue;
				// und es muss eine Markierung vorliegen
				if (manager.hatMarkierungHalbjahr(belegung, halbjahr))
					kurseMarkiert++;
			}
			if (kurseMarkiert >= 2)
				return true;
		}
		ergebnis.log.add("Es müssen mindestens 2 Kurse in Kunst, Musik oder einem Ersatzfach markiert sein, damit eine Abiturzulassung möglich ist.");
		return false;
	}


	private boolean pruefeKunstMusikErsatzAnzahlMarkierung() {
		final @NotNull List<AbiturFachbelegung> belegungen = manager.getFachbelegungen(GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ);
		int faecherMarkiert = 0;
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			int kurseMarkiert = 0;
			for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				// Alle Halbjahre müssen belegt sein
				if (belegung.belegungen[halbjahr.id] == null)
					continue;
				// Es darf keine 0-Punkte-Belegung markiert werden
				final Integer np = manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
				if ((np == null) || (np == 0))
					continue;
				// und es muss eine Markierung vorliegen
				if (manager.hatMarkierungHalbjahr(belegung, halbjahr))
					kurseMarkiert++;
			}
			if (kurseMarkiert > 0)
				faecherMarkiert++;
			if (kurseMarkiert > 2) {
				ergebnis.log.add("Es dürfen maximal 2 Kurse in einem Ersatzfach markiert sein.");
				return false;
			}
		}
		if (faecherMarkiert > 2) {
			ergebnis.log.add("Es dürfen nur Kurse in einem Ersatzfach markiert sein.");
			return false;
		}
		return true;
	}


	private boolean pruefeMusikAnzahlMarkierung() {
		final AbiturFachbelegung belMU = manager.getFachbelegungByKuerzel("MU");
		final AbiturFachbelegung belVP = manager.getFachbelegungByKuerzel("VP");
		final AbiturFachbelegung belIN = manager.getFachbelegungByKuerzel("IN");
		final boolean istAbiLK = (abi[0] == belMU) || (abi[1] == belMU);
		final boolean istAbiGK = (abi[2] == belMU) || (abi[3] == belMU);
		final int anzahl = manager.zaehleMarkierungenQualifikationsphase(belMU)
				+ manager.zaehleMarkierungenQualifikationsphase(belVP)
				+ manager.zaehleMarkierungenQualifikationsphase(belIN);
		if (istAbiLK) {
			if (anzahl > 4) {
				ergebnis.log.add("Wenn Musik als Leistungskurs belegt ist, dann dürfen keine weiteren Kurse in MU,VP und IN markiert werden.");
				return false;
			}
			return true;
		}
		if (istAbiGK) {
			if (anzahl > 6) {
				ergebnis.log.add("Wenn Musik als Grundkurs im Abitur belegt ist, dann dürfen maximal sechs Kurse in MU,VP und IN markiert werden.");
				return false;
			}
			return true;
		}
		if (anzahl > 6) {
			ergebnis.log.add("Es dürfen maximal fünf Kurse in MU,VP und IN markiert werden.");
			return false;
		}
		return true;
	}


	private boolean pruefeAnzahlMarkierungen(final @NotNull GostFachbereich fb, final int min, final @NotNull String fehler) {
		final @NotNull List<AbiturFachbelegung> belegungen = manager.getFachbelegungen(fb);
		int anzahl = 0;
		for (final @NotNull AbiturFachbelegung belegung : belegungen)
			anzahl += manager.zaehleMarkierungenQualifikationsphase(belegung);
		if (anzahl < min) {
			ergebnis.log.add(fehler);
			return false;
		}
		return true;
	}


	private boolean pruefeExistiertMitAnzahlMarkierungen(final @NotNull GostFachbereich fb, final int min, final @NotNull String fehler) {
		final @NotNull List<AbiturFachbelegung> belegungen = manager.getFachbelegungen(fb);
		for (final @NotNull AbiturFachbelegung belegung : belegungen)
			if (manager.zaehleMarkierungenQualifikationsphase(belegung) >= min)
				return true;
		ergebnis.log.add(fehler);
		return false;
	}

}
