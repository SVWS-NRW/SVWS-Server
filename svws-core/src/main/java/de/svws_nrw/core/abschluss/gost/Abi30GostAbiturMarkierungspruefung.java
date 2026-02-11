package de.svws_nrw.core.abschluss.gost;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungAbiFaecher;
import de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungProjektkurse;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostBesondereLernleistung;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
import de.svws_nrw.core.utils.gost.GostFachUtils;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Bei dieser Implementierung handelt es sich um eine Umsetzung in Bezug auf möglichen zukünftigen
 * Änderungen in der APO-GOSt. Diese basiert auf der aktuellen Implementierung und integriert Aspekte
 * aus dem Eckpunktepapier und auf in den Schulleiterdienstbesprechungen erläuterten Vorhaben.
 * Sie dient der Evaluierung von möglichen Umsetzungsvarianten und als Vorbereitung einer späteren
 * Implementierung der Belegprüfung. Insbesondere sollen erste Versuche mit Laufbahnen mit einem
 * 5. Abiturfach und Projektkursen erprobt werden. Detailaspekte können erst nach Erscheinen der APO-GOSt
 * umgesetzt werden.
 * Es handelt sich also um experimentellen Code, der keine Rückschlüsse auf Details einer zukünftigen APO-GOSt
 * erlaubt.
 *
 * Diese Klasse prüft eine Markierung von Halbjahresbelegungen von anrechenbaren Kursen
 * für die Abiturberechnung.
 */
public final class Abi30GostAbiturMarkierungspruefung {

	/** Das Ergebnis der Prüfung */
	private final @NotNull GostAbiturMarkierungspruefungErgebnis ergebnis = new GostAbiturMarkierungspruefungErgebnis();

	/** Die aktuelle Einrückung für das Logging */
	private @NotNull String logIndent = "";

	/** Der Abiturdaten-Manager */
	private final @NotNull AbiturdatenManager manager;

	/** Die zuvor durchgeführten Belegprüfung zu dem Projektkurs */
	private final @NotNull Abi30BelegpruefungProjektkurse belegpruefungProjektkurse;

	/** Die zuvor durchgeführten Belegprüfung zu den Abiturfächern */
	private final @NotNull Abi30BelegpruefungAbiFaecher belegpruefungAbiturfaecher;

	/** Die Belegungen der vier Abiturfächer */
	final @NotNull AbiturFachbelegung[] abi = new AbiturFachbelegung[5];

	/** Die Belegung einer (ersten) vollständig markierten Fremdsprache (keine Bili-Sachfach!) */
	AbiturFachbelegung fremdsprache = null;

	/** Gibt an, ob eine weitere Fremdsprache neben der ersten gefunden wurde (Bili-Sachfach ist möglich) */
	boolean hatWeitereFremdsprache = false;

	/** Die Belegung einer vollständig markierten klassischen Naturwissenschaft */
	AbiturFachbelegung naturwissenschaft = null;

	/** Gibt an, ob eine weitere Naturwissenschaft neben der ersten gefunden wurde */
	boolean hatWeitereNaturwissenschaft = false;


	/**
	 * Erstellt eine neue Instanz des Markierungsalgorithmus unter Verwendung des übergebenen Abiturdaten-Manager und den zuvor
	 * durchgeführten Belegprüfungen.
	 *
	 * @param manager            der Abiturdaten-Manager
	 * @param belegpruefungen    die durchgeführten Belegprüfungen
	 */
	private Abi30GostAbiturMarkierungspruefung(final @NotNull AbiturdatenManager manager, final @NotNull List<GostBelegpruefung> belegpruefungen) {
		this.manager = manager;
		this.logIndent = "";
		// Bestimme die zuvor durchgeführten Belegprüfungen zu den Projektkursen, dem Schwerpunkt und den Abiturfächern
		Abi30BelegpruefungProjektkurse tmpBelegpruefungProjektkurse = null;
		Abi30BelegpruefungAbiFaecher tmpBelegpruefungAbiturfaecher = null;
		for (final @NotNull GostBelegpruefung pruefung : belegpruefungen) {
			if (pruefung instanceof final Abi30BelegpruefungProjektkurse projektkurse)
				tmpBelegpruefungProjektkurse = projektkurse;
			if (pruefung instanceof final Abi30BelegpruefungAbiFaecher abiturfaecher)
				tmpBelegpruefungAbiturfaecher = abiturfaecher;
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
		abi[4] = this.belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.AB5);
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
		final @NotNull Abi30GostAbiturMarkierungspruefung pruefung = new Abi30GostAbiturMarkierungspruefung(manager, belegpruefungen);
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

		// Schritt 8: Prüfe, ob mindestes 2 Kurse in Kunst, Musik oder Literatur markiert sind
		success = success && pruefeKunstMusikOderLiteraturMarkierung();

		// Schritt 9: Prüfe, ob bei Literatur als Ersatzfach für Kunst oder Musik maximal 2 Kurse markiert sind
		success = success && pruefeLiteraturAnzahlMarkierung();

		// Schritt 10: Prüfe die Anzahl der Markierungen von Musik je nach ob Musik LK-Abiturfach, GK-Abiturfach oder kein Abiturfach ist
		success = success && pruefeMusikMarkierung();

		// Schritt 11: Prüfe auf mind. zwei markierte Kurse in Geschichte
		success = success && pruefeAnzahlMarkierungen(GostFachbereich.GESCHICHTE, 2, "Es müssen mindestens zwei Kurse in Geschichte markiert werden.");

		// Schritt 12: Prüfe auf mind. zwei markierte Kurse in Geschichte
		success = success && pruefeAnzahlMarkierungen(GostFachbereich.SOZIALWISSENSCHAFTEN, 2,
				"Es müssen mindestens zwei Kurse in Sozialwissenschaften markiert werden.");

		// Schritt 13: Prüfe, ob eine Gesellschaftswissenschaft mit Markierungen in allen Halbjahren der QPhase existiert
		success = success && pruefeGesellschaftswissenschaftMarkierung();

		// Schritt 14/15: Prüfe, ob Religion oder ein Ersatzfach mit zwei Markierungen berücksichtigt wurde
		success = success && pruefeReligionsOderErsatzMarkierungen();

		// Schritt 16: auf Markierung von Mathematik
		success = success && pruefeAnzahlMarkierungen(GostFachbereich.MATHEMATIK, 4, "Mathematik muss durchgehend markiert sein.");

		// Schritt 17: Prüfe auf Markierung einer klassischen Naturwissenschaft (diese wird zwischengespeichert)
		success = success && pruefeNaturwissenschaftMarkierung();

		// evtl. die Prüfung schon beenden
		if (!success)
			return success;

		// Schritt 18: Prüfe, ob eine weitere Naturwissenschaft markiert wurde
		pruefeAufWeitereNaturwissenschaft();

		// Schritt 19: Prüfe, ob ein mind. eine Schwerpunktbedingung für Fremdsprachen oder Naturwissenschaften existiert
		success = pruefeSchwerpunkt();

		// Schritt 20: Prüfe die Markierung von Projektkursbelegungen
		success = success && pruefeProjektkurs();

		// Schritte 21-26: Prüfe Anzahl der markierten Kurse und Anzahl der markierten Defizite
		success = success && pruefeAnzahlUndDefizite();

		// evtl. die Prüfung schon beenden
		if (!success)
			return success;

		// Schritt 27/28: Prüfe, ob noch Kurse markiert werden können, welche die Durchschnittsnote verbessern würden
		return pruefeOptimierung();
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
		if ((abi[0] == null) || (abi[1] == null) || (abi[2] == null) || (abi[3] == null) || (abi[4] == null)) {
			ergebnis.log.add("Es müssen fünf Abiturfächer gewählt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		// Projektkurse als Abiturfächer werden gesondert behandelt (Referentfächer). Prüfe daher, ob das 5. Fach ein PJK-Fach ist
		final boolean istAbiPJK = GostKursart.PJK.kuerzel.equals(abi[4].letzteKursart);
		for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
			if ((abi[0].belegungen[halbjahr.id] == null) || (abi[1].belegungen[halbjahr.id] == null)
					|| (abi[2].belegungen[halbjahr.id] == null) || (abi[3].belegungen[halbjahr.id] == null)
					|| (!istAbiPJK && (abi[4].belegungen[halbjahr.id] == null))
					|| (istAbiPJK && ((halbjahr == GostHalbjahr.Q21) || (halbjahr == GostHalbjahr.Q22)) && (abi[4].belegungen[halbjahr.id] == null))) {
				ergebnis.log.add("Alle Abiturfächer müssen im Halbjahr %s belegt sein, damit eine Abiturzulassung möglich ist."
						.formatted(halbjahr.kuerzel));
				return false;
			}
			final Integer np1 = manager.getNotenpunkteOfFachbelegungHalbjahr(abi[0].belegungen[halbjahr.id]);
			final Integer np2 = manager.getNotenpunkteOfFachbelegungHalbjahr(abi[1].belegungen[halbjahr.id]);
			final Integer np3 = manager.getNotenpunkteOfFachbelegungHalbjahr(abi[2].belegungen[halbjahr.id]);
			final Integer np4 = manager.getNotenpunkteOfFachbelegungHalbjahr(abi[3].belegungen[halbjahr.id]);
			final Integer np5 = manager.getNotenpunkteOfFachbelegungHalbjahr(abi[4].belegungen[halbjahr.id]);
			// Ist das 5. Abiturfach ein Projektkursfach, so erfolgt hier nur eine Prüfung der Q2. Das Referenzfach wird im Rahmen der Projektkursprüfung dann geprüft.
			if (istAbiPJK && halbjahr.istIn(GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12))
				continue;
			if ((np1 == null) || (np2 == null) || (np3 == null) || (np4 == null) || (np5 == null)) {
				ergebnis.log.add("Alle Abiturfächer müssen im Halbjahr %s bewertet sein, damit eine Abiturzulassung möglich ist."
						.formatted(halbjahr.kuerzel));
				return false;
			}
			if ((np1 == 0) || (np2 == 0) || (np3 == 0) || (np4 == 0) || (np5 == 0)) {
				ergebnis.log.add("Abiturfächer mit 0 Notenpunkten gelten als nicht belegt. Keine Zulassung zum Abitur.");
				return false;
			}
		}
		return true;
	}


	private boolean pruefeAbiturfachMarkierung() {
		if ((abi[0] == null) || (abi[1] == null) || (abi[2] == null) || (abi[3] == null) || (abi[4] == null)) {
			ergebnis.log.add("Es müssen vier Abiturfächer gewählt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}

		// Prüfe die duchgehende Markierung der Abiturfächer (beachte die Ausnahme PJK bei einem 5. Abiturfach)
		for (int i = 1; i < 6; i++) {
			if ((i == 5) && GostKursart.PJK.kuerzel.equals(abi[4].letzteKursart)) {
				if (manager.hatMarkierungHalbjahr(abi[i - 1], GostHalbjahr.Q21) && manager.hatMarkierungHalbjahr(abi[i - 1], GostHalbjahr.Q22))
					continue;
				ergebnis.log.add("Ein Projektkursfach als 5. Abiturfach muss in der Q2 markiert sein.");
				return false;
			}
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
					if (((halbjahr == GostHalbjahr.Q21) || (halbjahr == GostHalbjahr.Q22)) || manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
						found = false;
						break;
					}
					continue;
				}
				// und es muss eine Markierung vorliegen
				if (((halbjahr == GostHalbjahr.Q21) || (halbjahr == GostHalbjahr.Q22)) && !manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
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
				final GostFach sachfach = manager.getFach(belegung);
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
						if (((halbjahr == GostHalbjahr.Q21) || (halbjahr == GostHalbjahr.Q22)) || manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
							found = false;
							break;
						}
						continue;
					}
					// und es muss eine Markierung vorliegen
					if (((halbjahr == GostHalbjahr.Q21) || (halbjahr == GostHalbjahr.Q22)) && !manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
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


	private boolean pruefeKunstMusikOderLiteraturMarkierung() {
		final @NotNull List<AbiturFachbelegung> belegungen = manager.getFachbelegungen(GostFachbereich.KUNST_MUSIK_LITERATUR);
		if (belegungen.isEmpty()) {
			ergebnis.log.add("Es muss mindestens Kunst, Musik oder Literatur belegt sein, damit eine Abiturzulassung möglich ist.");
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
		ergebnis.log.add("Es müssen mindestens 2 Kurse in Kunst, Musik oder Literatur markiert sein, damit eine Abiturzulassung möglich ist.");
		return false;
	}


	private boolean pruefeLiteraturAnzahlMarkierung() {
		final AbiturFachbelegung belegung = manager.getFachbelegungByKuerzel("LI");
		if (belegung == null)
			return true;
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
		if (kurseMarkiert > 2) {
			ergebnis.log.add("Es dürfen maximal 2 Kurse in Literatur markiert sein.");
			return false;
		}
		return true;
	}


	private boolean pruefeMusikMarkierung() {
		final AbiturFachbelegung belVP = manager.getFachbelegungByKuerzel("VP");
		final AbiturFachbelegung belIN = manager.getFachbelegungByKuerzel("IN");
		if ((manager.zaehleMarkierungenQualifikationsphase(belVP) > 0) || (manager.zaehleMarkierungenQualifikationsphase(belIN) > 0)) {
			ergebnis.log.add("Vokal- und Instrumentalpraktischer Grundkurse können nicht für die Abiturzulassung markiert werden.");
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


	private boolean pruefeGesellschaftswissenschaftMarkierung() {
		final @NotNull List<AbiturFachbelegung> belegungen = manager.getFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH);
		if (belegungen.isEmpty()) {
			ergebnis.log.add("Es muss mindestens eine Gesellschaftswissenschaft belegt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		if (manager.pruefeMarkierungExistiertDurchgaengig(belegungen) != null)
			return true;
		ergebnis.log.add("Es muss mindestens eine Gesellschaftswissenschaft durchgängig markiert sein, damit eine Abiturzulassung möglich ist.");
		return false;
	}


	private boolean pruefeReligionsOderErsatzMarkierungen() {
		final boolean hatAbiRE = manager.hatFachbereichInAbiturfaechern(GostFachbereich.RELIGION);
		final boolean hatAbiPL = manager.hatFachbereichInAbiturfaechern(GostFachbereich.PHILOSOPHIE);
		final @NotNull List<AbiturFachbelegung> belRE = manager.getFachbelegungen(GostFachbereich.RELIGION);
		final int countRE = manager.zaehleAlleMarkierungenQualifikationsphase(belRE);
		final int countPL = manager.zaehleAlleMarkierungenQualifikationsphase(manager.getFachbelegungen(GostFachbereich.PHILOSOPHIE));
		if (!hatAbiRE && !hatAbiPL && ((countRE + countPL) < 2)) {
			ergebnis.log.add("Es müssen mindestens zwei Kurse aus der Fächergruppe Religionslehre und Philosophie markiert werden.");
			return false;
		}
		final int countGW = manager.zaehleAlleMarkierungenQualifikationsphase(
				manager.getFachbelegungen(GostFachbereich.GESCHICHTE, GostFachbereich.SOZIALWISSENSCHAFTEN,
						GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE));
		final boolean hatAbiGW = manager.hatFachbereichInAbiturfaechern(GostFachbereich.GESCHICHTE, GostFachbereich.SOZIALWISSENSCHAFTEN,
				GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE);
		if (!(!hatAbiRE && hatAbiPL && !hatAbiGW))
			return true;
		// Wurden zwei Kurse Religionslehre in Q2.1 bis Q2.2 markiert?
		if (countRE >= 2)
			return true;
		if (manager.pruefeBelegungExistiert(belRE, GostHalbjahr.Q11, GostHalbjahr.Q12)) {
			ergebnis.log.add("Es müssen mindestens zwei Kurse aus der Fächergruppe Religionslehre markiert werden.");
			return false;
		}
		// Wurden ein Kurs Religionslehre in Q1.1 bis Q2.2 und wurden mindestens 9 Kurse aller gesellschaftswissenschaftlichen Fächer (auch Zusatzkurse) in Q1.1 bis Q2.2 markiert?
		if ((countRE == 1) && (countGW + countPL >= 9))
			return true;
		if (manager.pruefeBelegungExistiert(belRE, GostHalbjahr.Q11) || manager.pruefeBelegungExistiert(belRE, GostHalbjahr.Q11)) {
			ergebnis.log.add("Es müssen zwei Kurse Religionslehre oder ein Kurs Religionslehre und ein Kurs des Ersatzfaches markiert werden.");
			return false;
		}
		// Wurden mindestens 10 Kurse aller gesellschaftswissenschaftlichen Fächer (auch Zusatzkurse) markiert?
		if (countGW + countPL >= 10)
			return true;
		ergebnis.log.add(
				"Es müssen zwei Kurse Religionslehre oder ein Kurs Religionslehre und ein Kurs des Ersatzfaches oder zwei Kurse des Ersatzfaches markiert werden.");
		return false;
	}


	private boolean pruefeNaturwissenschaftMarkierung() {
		final @NotNull List<AbiturFachbelegung> belegungen = manager.getFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH);
		if (belegungen.isEmpty()) {
			ergebnis.log.add("Es muss mindestens eine klassische Naturwissenschaft belegt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		this.naturwissenschaft = manager.pruefeMarkierungExistiertDurchgaengig(belegungen);
		if (this.naturwissenschaft != null)
			return true;
		ergebnis.log.add("Es muss mindestens eine klassische Naturwissenschaft durchgängig markiert sein, damit eine Abiturzulassung möglich ist.");
		return false;
	}


	private void pruefeAufWeitereNaturwissenschaft() {
		if (this.naturwissenschaft == null)
			return;
		this.hatWeitereNaturwissenschaft = false;
		final @NotNull List<AbiturFachbelegung> belegungen = manager.getFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH);
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			// Aber nicht die Naturwissenschaft, die als erstes gefunden wurde
			// (Prüfe die ID, da es sich evtl. auch um ein bilinguales Sachfach handeln kann, welches verlassen wurde)
			if (belegung.fachID == this.naturwissenschaft.fachID)
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
					if (((halbjahr == GostHalbjahr.Q21) || (halbjahr == GostHalbjahr.Q22)) || manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
						found = false;
						break;
					}
					continue;
				}
				// und es muss eine Markierung vorliegen
				if (((halbjahr == GostHalbjahr.Q21) || (halbjahr == GostHalbjahr.Q22)) && !manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
					found = false;
					break;
				}
			}
			if (found) {
				this.hatWeitereNaturwissenschaft = true;
				return;
			}
		}
	}


	private boolean pruefeSchwerpunkt() {
		if (this.hatWeitereFremdsprache || this.hatWeitereNaturwissenschaft)
			return true;
		ergebnis.log
				.add("Es müssen zwei Kurse einer Naturwissenschaft oder einer schriftlich belegten weiteren Fremdsprache in Q2.1 und Q2.2 markiert werden.");
		return false;
	}


	private boolean pruefeProjektkurs() {
		final AbiturFachbelegung projektkurs = belegpruefungProjektkurse.getProjektkurs();
		if ((projektkurs == null) || (!manager.pruefeBelegungMitSchriftlichkeit(projektkurs, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q21, GostHalbjahr.Q22))) {
			ergebnis.log.add("Es muss ein Projektkurs schriftlich in der Q2 belegt werden.");
			return false;
		}
		if (manager.zaehleHalbjahresbelegungen(projektkurs, GostHalbjahr.getQualifikationsphase()) != 2) {
			ergebnis.log.add("Der Projektkurs wurde in zu vielen Halbjahren belegt.");
			return false;
		}
		if (manager.zaehleMarkierungenQualifikationsphase(projektkurs) != 2) {
			ergebnis.log.add("Beide Halbjahre des Projektkurse müssen markiert werden.");
			return false;
		}

		final GostFach fach = manager.faecher().get(projektkurs.fachID);
		if (fach == null) {
			ergebnis.log.add("Es konnte kein Fach für die Projektkurs-Fachbelegung bestimmt werden.");
			return false;
		}

		// Prüfe, ob die Markierungen bei den Referenzfach-Belegungen geeignet sind
		final @NotNull List<AbiturFachbelegung> referenzfachBelegungen = new ArrayList<>();
		if (fach.projektKursLeitfach1ID != null) {
			final AbiturFachbelegung fb1 = manager.getFachbelegungByID(fach.projektKursLeitfach1ID);
			if (fb1 != null)
				referenzfachBelegungen.add(fb1);
		}
		if (fach.projektKursLeitfach2ID != null) {
			final AbiturFachbelegung fb2 = manager.getFachbelegungByID(fach.projektKursLeitfach2ID);
			if (fb2 != null)
				referenzfachBelegungen.add(fb2);
		}
		for (final @NotNull AbiturFachbelegung referenzfachBelegung : referenzfachBelegungen) {
			final GostFach referenzfach = manager.faecher().get(referenzfachBelegung.fachID);
			if (referenzfach == null) {
				ergebnis.log.add("Es konnte kein Fach für die Belegung des Referenzfaches bestimmt werden.");
				continue;
			}

			// Prüfe, falls das Projektkursfach als Abiturfach verwendet wurde, ob die Belegung des Referenzfaches im Abiturbereich genutzt wurde
			if ((projektkurs.abiturFach != null) && (referenzfachBelegung.abiturFach != null))
				continue;

			// Prüfe die Belegung des Referenzfaches in der EF und Q1
			if (!manager.pruefeBelegung(referenzfachBelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12))
				continue;

			// Prüfe die schriftliche Belegung des Referenzfaches in der Q1
			if (!manager.pruefeBelegungMitSchriftlichkeit(referenzfachBelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12))
				continue;

			// ... und für das Referenzfach die beiden Halbjahre in der Q1, sofern sie nicht bereits vorher markiert wurden
			if (manager.hatMarkierungHalbjahr(referenzfachBelegung, GostHalbjahr.Q11) && manager.hatMarkierungHalbjahr(referenzfachBelegung, GostHalbjahr.Q12))
				return true;
		}
		ergebnis.log.add("Für den Projektkurs muss ein Referenzfach in der Q1 markiert werden. Ist das Projektkursfach fünftes Abiturfach, so darf das Referenzfach kein Abiturfach sein.");
		return false;
	}


	private boolean pruefeAnzahlUndDefizite() {
		// Prüfe, ob Kurse ohne Wertung oder mit 0 Punkten markiert wurden
		if (manager.zaehleMarkierungenOhneWertungOderMitNullPunkten(manager.daten().fachbelegungen) > 0) {
			ergebnis.log.add(
					"Es wurden Kurse markiert, welche mit 0 Punkten bewertet wurden. Diese gelten aber als nicht belegt und dürfen nicht markiert werden.");
			return false;
		}
		// Prüfe die Anzahl der Kurse
		final int count = manager.zaehleAlleMarkierungenQualifikationsphase(manager.daten().fachbelegungen);
		if ((count != 36)) {
			ergebnis.log.add("Es müssen genau 36 Kurse markiert werden.");
			return false;
		}
		// Prüfe die Gesamtzahl der Defizite in Abhängigkeit der Anzahl der markierten Kurse
		final int countDefizite = manager.zaehleMarkierungenMitDefiziten(manager.daten().fachbelegungen);
		if (countDefizite > 7) {
			ergebnis.log.add("Keine Zulassung zum Abitur. Es wurden zu viele Kurse mit Defizit markiert.");
			return false;
		}
		return true;
	}


	private boolean pruefeOptimierung() {
		// Prüfe die Anzahl der Kurse
		final int count = manager.zaehleAlleMarkierungenQualifikationsphase(manager.daten().fachbelegungen);
		if (count >= 36)
			return true;
		// Berechne die Durchschnittspunkte aller markierten Kurse. Zähle LK-Kurse und LK-Punkte dabei doppelt.
		final double durchschnitt = manager.berechneMarkierungenDurchschnittspunkte();

		// Erstelle eine absteigend nach Notepunkten sortierte Restmenge der nicht markierten Kurse
		// Ein Projektkurs fließt nur ein, wenn dieser nicht als besondere Lernleistung eingebracht wird
		final boolean hatPjkBLL = GostBesondereLernleistung.PROJEKTKURS.kuerzel.equals(manager.daten().besondereLernleistung);
		final @NotNull List<GostAbiturMarkierungsalgorithmusBelegung> auswahlliste = new ArrayList<>();
		for (final @NotNull AbiturFachbelegung belegung : manager.daten().fachbelegungen) {
			// Prüfe das Fach, Projektkurse fließen nur ein, falls dieser nicht als BLL eingebracht wird
			final GostFach fach = manager.getFach(belegung);
			if ((fach == null) || (hatPjkBLL && "PX".equals(fach.kuerzel)))
				continue;
			for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase()) {
				final AbiturFachbelegungHalbjahr belHj = belegung.belegungen[hj.id];
				if ((belHj == null) || ((belHj.block1gewertet != null) && belHj.block1gewertet))
					continue;
				final Integer np = manager.getNotenpunkteOfFachbelegungHalbjahr(belHj);
				if ((np == null) || (np == 0))
					continue;
				auswahlliste.add(new GostAbiturMarkierungsalgorithmusBelegung(belegung, belHj, np));
			}
		}
		if (auswahlliste.isEmpty())
			return true;

		// Bestimme zunächst die Markierungen bei Musik und den Ersatzfächern
		final AbiturFachbelegung belMU = manager.getFachbelegungByKuerzel("MU");
		final AbiturFachbelegung belLI = manager.getFachbelegungByKuerzel("LI");
		final int countMU = manager.zaehleMarkierungenQualifikationsphase(belMU);
		final int countLI = manager.zaehleMarkierungenQualifikationsphase(belLI);
		final int countMoeglichLiteratur = 2 - countLI;
		final int countMoeglichMusik = 4 - countMU;

		// Sortiere die Auswahlliste und bestimme ggf. den ersten geeigneten Eintrag in der Liste
		sort(auswahlliste);
		while (!auswahlliste.isEmpty()) {
			final @NotNull GostAbiturMarkierungsalgorithmusBelegung bel = auswahlliste.getFirst();
			auswahlliste.removeFirst();
			if (bel.notenpunkte <= durchschnitt)
				break;
			final GostFach fach = manager.getFach(bel.belegung);
			if ((fach == null)
					|| ("LI".equals(fach.kuerzel) && (countMoeglichLiteratur <= 0))
					|| ("MU".equals(fach.kuerzel) && (countMoeglichMusik <= 0)))
				continue;
			ergebnis.log.add("Es existieren nicht markierte Kurse, die durch Markierung den Abiturdurchschnitt verbessern können.");
			return false;
		}
		return true;
	}


	private void sort(final @NotNull List<GostAbiturMarkierungsalgorithmusBelegung> auswahlliste) {
		final @NotNull Comparator<GostAbiturMarkierungsalgorithmusBelegung> comparatorBelegungen =
				(final @NotNull GostAbiturMarkierungsalgorithmusBelegung a, final @NotNull GostAbiturMarkierungsalgorithmusBelegung b) -> {
					int tmp = b.notenpunkte - a.notenpunkte;
					if (tmp != 0)
						return tmp;
					// Ansonsten gilt die Sortierung des Faches ...
					final GostFach aFach = manager.getFach(a.belegung);
					final GostFach bFach = manager.getFach(b.belegung);
					if ((aFach == null) || (bFach == null)) // Kann hier nicht auftreten - nur für den Compiler...
						return -1;
					tmp = GostFachbereich.compareGostFach(aFach, bFach);
					if (tmp != 0)
						return tmp;
					// Ansonsten die des Halbjahres
					final GostHalbjahr hjA = GostHalbjahr.fromKuerzel(a.belegungHalbjahr.halbjahrKuerzel);
					final GostHalbjahr hjB = GostHalbjahr.fromKuerzel(b.belegungHalbjahr.halbjahrKuerzel);
					if ((hjA == null) || (hjB == null)) // Kann hier nicht auftreten - nur für den Compiler...
						return -1;
					return hjB.id - hjA.id;
				};
		auswahlliste.sort(comparatorBelegungen);
	}

}
