package de.svws_nrw.core.abschluss.ge;

import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.function.Predicate;

import de.svws_nrw.core.Service;
import de.svws_nrw.core.abschluss.AbschlussManager;
import de.svws_nrw.core.data.abschluss.AbschlussErgebnis;
import de.svws_nrw.core.data.abschluss.GEAbschlussFach;
import de.svws_nrw.core.data.abschluss.GEAbschlussFaecher;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.ge.GELeistungsdifferenzierteKursart;
import de.svws_nrw.core.types.schule.SchulabschlussAllgemeinbildend;


/**
 * Diese Klasse stellt einen Service zur Berechnung zur Verfügung, ob die Berechtigung zum
 * Besuch der Gymnasialen Oberstufe in Kombination mit dem Mittleren Schulabschluss
 * nach Klasse 10 erworben wurde.
 */
public class ServiceBerechtigungMSAQ extends Service<@NotNull GEAbschlussFaecher, @NotNull AbschlussErgebnis> {

	private final @NotNull Predicate<@NotNull GEAbschlussFach> filterDefizite = (final @NotNull GEAbschlussFach f) -> (f.note > 3) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 2));
	private final @NotNull Predicate<@NotNull GEAbschlussFach> filterDefizite1NS = (final @NotNull GEAbschlussFach f) -> ((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note == 4)) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note == 3));
	private final @NotNull Predicate<@NotNull GEAbschlussFach> filterDefizite2NS = (final @NotNull GEAbschlussFach f) -> ((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note == 5)) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note == 4));

	private final @NotNull Predicate<@NotNull GEAbschlussFach> filterDefizitWP = (final @NotNull GEAbschlussFach f) -> (f.note > 3) && "WP".equalsIgnoreCase(f.kuerzel);
	private final @NotNull Predicate<@NotNull GEAbschlussFach> filterDefizitNichtWP = (final @NotNull GEAbschlussFach f) -> (f.note > 3) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 2)) && !"WP".equalsIgnoreCase(f.kuerzel);

	private final @NotNull Predicate<@NotNull GEAbschlussFach> filterFG1NichtAusgleichbar = (final @NotNull GEAbschlussFach f) -> (f.note > 4) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 3));
	private final @NotNull Predicate<@NotNull GEAbschlussFach> filterFG2NichtAusgleichbar = (final @NotNull GEAbschlussFach f) -> (f.note > 5) || ((GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note > 3));
	private final @NotNull Predicate<@NotNull GEAbschlussFach> filterAusgleiche = (final @NotNull GEAbschlussFach f) -> !f.ausgleich && ((f.note < 2) || ((!GELeistungsdifferenzierteKursart.G.hat(f.kursart)) && (f.note < 3)));

	private final @NotNull Predicate<@NotNull GEAbschlussFach> filterEKurse = (final @NotNull GEAbschlussFach f) -> (GELeistungsdifferenzierteKursart.E.hat(f.kursart));




	/**
	 * Führt die Abschlussberechnung (bzw. Berechtigungsberechnung) anhand der übergebenen
	 * Abschlussfächer durch und gibt das Berechnungsergebnis zurück.
	 *
	 * @param input    die Abschlussfächer
	 *
	 * @return das Ergebnis der Abschlussberechnung
	 */
	@Override
	public @NotNull AbschlussErgebnis handle(final @NotNull GEAbschlussFaecher input) {
		logger.logLn(LogLevel.INFO, "Prüfe MSA-Q:");
		logger.logLn(LogLevel.DEBUG, "============");

		// Prüfe, ob genügend leistungsdifferenzierte Kurse vorkommen
		if ((input.faecher == null) || (!AbschlussManager.pruefeHat4LeistungsdifferenzierteFaecher(input))) {
			logger.logLn(LogLevel.DEBUG, "______________________________");
			logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht genügend leistungsdifferenzierte Fächer gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}

		// Prüfe, ob Fächerkürzel mehrfach zur Abschlussprüfung übergeben wurden
		if (!AbschlussManager.pruefeKuerzelDuplikate(input)) {
			logger.logLn(LogLevel.DEBUG, "______________________________");
			logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden Fächer mit dem gleichen Kürzel zur Abschlussprüfung übergeben. Dies ist nicht zulässig.");
			return AbschlussManager.getErgebnis(null, false);
		}

		// Bestimme die Fächergruppen für die Berechnung des Abschlusses
		final @NotNull AbschlussFaecherGruppen faecher = ServiceAbschlussMSA.getFaechergruppen(input.faecher);

		// Prüfe, ob alle nötigen Fächer in der FG1 vorhanden sind
		if (!faecher.fg1.istVollstaendig(Arrays.asList("D", "M", "E", "WP"))) {
			logger.logLn(LogLevel.DEBUG, "______________________________");
			logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht alle nötigen Leistungen für die Fächergruppe 1 gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}

		// Prüfe, ob Fächer in der Fächergruppe II vorhanden sind
		if (faecher.fg2.isEmpty()) {
			logger.logLn(LogLevel.DEBUG, "______________________________");
			logger.logLn(LogLevel.DEBUG, " => Fehler: Keine Leistungen für die Fächergruppe 2 gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}

		// Prüfe ob mindestens drei E-Kurse vorhanden sind.
		final long anzahlEKurse = faecher.getFaecherAnzahl(filterEKurse);
		if (anzahlEKurse < 3) {
			logger.logLn(LogLevel.DEBUG, "______________________________");
			logger.logLn(LogLevel.INFO, " => kein MSA-Q (FOR-Q) - nicht genügend E-Kurse belegt");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		} else if (anzahlEKurse > 3) {
			logger.logLn(LogLevel.DEBUG, " - Verbessern der E-Kurs-Noten für die Defizitberechnung, falls mehr als 3 E-Kurse vorhanden sind:");

			// Verbessere den vierten E-Kurs, d.h. den aus FG2
			final @NotNull List<@NotNull GEAbschlussFach> tmpFaecher = faecher.fg2.getFaecher(filterEKurse);
			for (final @NotNull GEAbschlussFach f : tmpFaecher) {
				final int note = f.note;
				final int note_neu = (note == 1) ? 1 : note - 1;
				logger.logLn(LogLevel.DEBUG, "   " + f.kuerzel + ":(E)" + note + "->(G)" + note_neu);
				f.note = note_neu;
				f.kursart = GELeistungsdifferenzierteKursart.G.kuerzel;
			}
		}

		// Gibt die Fächer und Noten der Fächergruppen aus, welche bei der Berechnung des Abschlusses verwendet werden
		logger.logLn(LogLevel.DEBUG, " -> FG1: Fächer " + faecher.fg1.toString());
		logger.logLn(LogLevel.DEBUG, " -> FG2: Fächer " + faecher.fg2.toString());

		// Prüfe anhand der Defizite, ob ein Abschluss erworben wurde
		final @NotNull AbschlussErgebnis abschlussergebnis = this.pruefeDefizite(faecher, "");
		if (abschlussergebnis.erworben) {
			logger.logLn(LogLevel.DEBUG, "______________________________");
			logger.logLn(LogLevel.INFO, " => MSA-Q (FOR-Q): APO-SI §43 (4)");
		} else if (AbschlussManager.hatNachpruefungsmoeglichkeit(abschlussergebnis)) {
			logger.logLn(LogLevel.INFO, " => kein MSA-Q (FOR-Q) - Nachprüfungsmöglichkeite(en) in " + AbschlussManager.getNPFaecherString(abschlussergebnis));
		} else {
			logger.logLn(LogLevel.INFO, " => kein MSA-Q (FOR-Q) - KEINE Nachprüfungsmöglichkeiten!");
		}
		return abschlussergebnis;
	}




	/**
	 * Prüft in Bezug auf Defizite, ob der Abschluss erworben wurde.
	 *
	 * @param faecher      die Abschlussfächer nach Fächergruppen sortiert
	 * @param logIndent    die Einrückung für das Logging
	 *
	 * @return das Ergebnis der Abschlussberechnung in Bezug die Defizitberechnung
	 */
	private @NotNull AbschlussErgebnis pruefeDefizite(final @NotNull AbschlussFaecherGruppen faecher, final @NotNull String logIndent) {
		// Bestimme die Defizite in den beiden Fächergruppen
		final @NotNull List<@NotNull GEAbschlussFach> fg1_defizite = faecher.fg1.getFaecher(filterDefizite);
		final @NotNull List<@NotNull GEAbschlussFach> fg2_defizite = faecher.fg2.getFaecher(filterDefizite);

		if (fg1_defizite.size() > 0)
			logger.logLn(LogLevel.DEBUG, logIndent + " -> FG1: Defizit" + (fg1_defizite.size() > 1 ? "e" : "") + ": " + faecher.fg1.getKuerzelListe(filterDefizite));
		if (fg2_defizite.size() > 0)
			logger.logLn(LogLevel.DEBUG, logIndent + " -> FG2: Defizit" + (fg2_defizite.size() > 1 ? "e" : "") + ": " + faecher.fg2.getKuerzelListe(filterDefizite));

		// Prüfe, ob in FG1 oder FG2 Fächer vorhanden sind, die nicht ausgeglichen werden können und in denen keine Nachprüfung möglich ist
		boolean nachpruefung_genutzt = false;
		final @NotNull List<@NotNull GEAbschlussFach> npFaecher = new Vector<>();
		final @NotNull List<@NotNull GEAbschlussFach> fg1_nicht_ausgleichbar = faecher.fg1.getFaecher(filterFG1NichtAusgleichbar);
		final @NotNull List<@NotNull GEAbschlussFach> fg2_nicht_ausgleichbar = faecher.fg2.getFaecher(filterFG2NichtAusgleichbar);
		if ((fg1_nicht_ausgleichbar.size() > 0) || (fg2_nicht_ausgleichbar.size() > 0)) {
			final @NotNull String str_faecher = faecher.getKuerzelListe(filterFG1NichtAusgleichbar, filterFG2NichtAusgleichbar);
			logger.logLn(LogLevel.DEBUG, logIndent + " -> Defizit(e) in " + str_faecher + " aufgrund zu hoher Abweichungen nicht ausgleichbar.");
			// Prüfe, ob die Besonderheit vorliegt, dass nur das leistungsdifferenzierte Fach in der FG2 um zwei Notenstufen abweicht -> Nachprüfungsmöglichkeit
			if ((fg1_nicht_ausgleichbar.size() == 0) && (fg2_nicht_ausgleichbar.size() == 1)
					&& (GELeistungsdifferenzierteKursart.G.hat(fg2_nicht_ausgleichbar.get(0).kursart)) && (fg2_nicht_ausgleichbar.get(0).note == 4)) {
				// In diesem Fall muss auf jeden Fall im leistungsdifferenzierten Fach der FG2 eine Nachprüfung stattfinden, damit ggf. die Berechtigung über die Ausgleichsregelung möglich werden kann
				logger.logLn(LogLevel.DEBUG, logIndent + "   -> Nachprüfung muss falls möglich in " + fg2_nicht_ausgleichbar.get(0).kuerzel + " stattfinden!");
				nachpruefung_genutzt = true;
				npFaecher.add(fg2_nicht_ausgleichbar.get(0));
			} else {
				// Nachprüfung nicht möglich, Berechtigung MSA_Q nicht erreicht
				return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
			}
		}

		// Bestimme die möglichen Ausgleichsfächer in FG1
		final @NotNull List<@NotNull GEAbschlussFach> fg1_ausgleichsfaecher = faecher.fg1.getFaecher(filterAusgleiche);
		final GEAbschlussFach wp_defizit = faecher.fg1.getFach(filterDefizitWP);

		// Prüfe Defizite in FG1 auf Ausgleich
		if ((fg1_defizite.size() > 2) || ((fg1_defizite.size() == 2) && (wp_defizit == null))) {
			logger.logLn(LogLevel.DEBUG, logIndent + " -> zu viele Defizite in FG1");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		} else if ((fg1_defizite.size() == 2) && (wp_defizit != null) && (fg1_ausgleichsfaecher.size() == 0)) {
			logger.logLn(LogLevel.DEBUG, logIndent + " -> zu viele Defizite in FG1 - kein Ausgleich möglich");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		} else if ((fg1_defizite.size() == 2) && (wp_defizit != null) && (fg1_ausgleichsfaecher.size() > 0) && (!nachpruefung_genutzt)) {
			// Ausgleich in dem nicht WP-Fach
			final GEAbschlussFach defizitFach = faecher.fg1.getFach(filterDefizitNichtWP);
			if (defizitFach == null)
				throw new NullPointerException();
			final @NotNull GEAbschlussFach ausgleichsFach = fg1_ausgleichsfaecher.get(0);
			defizitFach.ausgeglichen = true;
			ausgleichsFach.ausgleich = true;
			logger.logLn(LogLevel.DEBUG, logIndent + " -> Ausgleich von " + defizitFach.kuerzel + " durch " + ausgleichsFach.kuerzel);
			// Nachprüfung in WP nötig
			nachpruefung_genutzt = true;
			npFaecher.add(wp_defizit);
			// Prüfe nun hiermit FG2
			final @NotNull AbschlussErgebnis abschlussergebnis = pruefeFG2(faecher, logIndent, npFaecher, nachpruefung_genutzt);
			if (abschlussergebnis.erworben) {
				return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA_Q, AbschlussManager.getKuerzel(npFaecher));
			}
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		}

		// Prüfe, ob ein einzelnes, nicht-WP-Defizit ausgeglichen werden kann
		if ((fg1_defizite.size() == 1) && (wp_defizit == null) && (fg1_ausgleichsfaecher.size() == 0)) {
			logger.logLn(LogLevel.DEBUG, logIndent + " -> kein Defizit-Ausgleich in FG1");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		}

		// Nutze Ausgleichregelung bei einem Nicht-WP-Fach-Defizit
		if ((fg1_defizite.size() == 1) && (wp_defizit == null)) {
			final GEAbschlussFach defizitFach = faecher.fg1.getFach(filterDefizitNichtWP);
			if (defizitFach == null)
				throw new NullPointerException();
			final @NotNull GEAbschlussFach ausgleichsFach = fg1_ausgleichsfaecher.get(0);
			defizitFach.ausgeglichen = true;
			ausgleichsFach.ausgleich = true;
			logger.logLn(LogLevel.DEBUG, logIndent + " -> Ausgleich von " + defizitFach.kuerzel + " durch " + ausgleichsFach.kuerzel);
		}

		// Prüfe FG2: Ist das WP-Fach das einzige FG1-Defizit, so wird eine Fallunterscheidung nötig - Ausgleich oder Nachprüfung
		if ((fg1_defizite.size() == 1) && (wp_defizit != null)) {
			// Prüfe FG2 mit der Option eines Ausgleichsfaches - bestimme ggf. Nachprüfungsmöglichkeiten
			if ((fg1_ausgleichsfaecher.size() > 0)) {
				// Nutze Ausgleichregelung und prüfe dann FG2 mit Nachprüfungsoption
				final @NotNull GEAbschlussFach defizitFach = wp_defizit;
				final @NotNull GEAbschlussFach ausgleichsFach = fg1_ausgleichsfaecher.get(0);
				defizitFach.ausgeglichen = true;
				ausgleichsFach.ausgleich = true;
				logger.logLn(LogLevel.DEBUG, logIndent + " -> Prüfe FG2 mit der Option Ausgleich von " + defizitFach.kuerzel + " durch " + ausgleichsFach.kuerzel);

				// Prüfe FG2 - falls ein Abschluss mit der Ausgleichs-Option möglich ist - kann die Prüfung insgesamt beendet werden
				final @NotNull AbschlussErgebnis abschlussergebnis = pruefeFG2(faecher, logIndent + "  ", npFaecher, nachpruefung_genutzt);
				if (abschlussergebnis.erworben)
					return abschlussergebnis;

				// Für die weitere Prüfung wird der Ausgleich zurückgenommen
				defizitFach.ausgeglichen = false;
				ausgleichsFach.ausgleich = false;
			}

			// Prüfe FG2 mit der Option der Nachprüfung in WP, dabei muss berücksichtigt werden, ob in FG2 bereits eine Nachprüfung im leistungsdifferenzierten Fach nötig ist!
			if (nachpruefung_genutzt) {
				logger.logLn(LogLevel.DEBUG, logIndent + " -> Eine Nachprüfung im WP-Fach und in dem leistungsdifferenzierten Fach der FG2 ist nicht gleichzeitig möglich.");
				return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
			}
			wp_defizit.ausgleich = true;
			wp_defizit.note--; // verbessere zwischenzeitlich
			final @NotNull AbschlussErgebnis abschlussergebnis = pruefeFG2(faecher, logIndent, npFaecher, true);
			wp_defizit.note++; // verschlechtere wieder
			wp_defizit.ausgleich = false;
			if (abschlussergebnis.erworben) {
				nachpruefung_genutzt = true;
				npFaecher.add(wp_defizit);
			}
			return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA_Q, AbschlussManager.getKuerzel(npFaecher));
		}

		// Prüfe FG2: keine weiteren Defizite vorhanden
		@NotNull String log_fg2_indent = logIndent;
		if (fg2_nicht_ausgleichbar.size() == 1) {
			logger.logLn(LogLevel.DEBUG, logIndent + " -> Prüfe FG2 mit Nachprüfung in " + fg2_nicht_ausgleichbar.get(0).kuerzel);
			log_fg2_indent += "  ";
		}
		final @NotNull AbschlussErgebnis abschlussergebnis = pruefeFG2(faecher, log_fg2_indent, npFaecher, nachpruefung_genutzt);
		if (((fg2_nicht_ausgleichbar.size() == 1) && abschlussergebnis.erworben) || ((!abschlussergebnis.erworben) && (AbschlussManager.hatNachpruefungsmoeglichkeit(abschlussergebnis)))) {
			return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA_Q, AbschlussManager.getKuerzel(npFaecher));
		}
		return abschlussergebnis;
	}



	/**
	 * Führt eine Detailprüfung in der Fächergruppe 2 durch. Diese Methode wird ggf. mehrfach - auch rekursiv - aufgerufen.
	 *
	 * @param faecher               die Abschlussfächer nach Fächergruppen sortiert
	 * @param logIndent             die Einrückung für das Logging
	 * @param npFaecher             die Liste der Nachprüfungsfächer, die bisher schon feststehen
	 * @param nachpruefungGenutzt   gibt an, ob die Nachprüfungsmöglichkeit bereits eingesetzt werden musste
	 *
	 * @return das Ergebnis der Abschlussberechnung in Bezug auf den Stand dieser Detailprüfung
	 */
	private @NotNull AbschlussErgebnis pruefeFG2(final @NotNull AbschlussFaecherGruppen faecher, final @NotNull String logIndent, final @NotNull List<@NotNull GEAbschlussFach> npFaecher, final boolean nachpruefungGenutzt) {
		final @NotNull List<@NotNull GEAbschlussFach> ges_ausgleichsfaecher = faecher.getFaecher(filterAusgleiche);

		final @NotNull List<@NotNull GEAbschlussFach> fg2_defizite_1NS = faecher.fg2.getFaecher(filterDefizite1NS);
		final @NotNull List<@NotNull GEAbschlussFach> fg2_defizite_2NS = faecher.fg2.getFaecher(filterDefizite2NS);
		final int fg2_defizit_anzahl = fg2_defizite_1NS.size() + fg2_defizite_2NS.size();

		// Wenn keine Defizite vorhanden sind, dann braucht nicht weiter geprüft zu werden...
		if (fg2_defizit_anzahl == 0) {
			logger.logLn(LogLevel.DEBUG, logIndent + " -> keine Defizite in FG2");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, true);
		}

		// Entweder müssen Defizite ausgeglichen werden oder per Nachprüfung nachträglich behebbar sein
		if ((fg2_defizite_2NS.size() > 2) || (fg2_defizit_anzahl > (nachpruefungGenutzt ? 3 : 4))) {
			logger.logLn(LogLevel.DEBUG, logIndent + " -> zu viele Defizite in FG2 - mit Ausgleich und Nachprüfung kein Abschluss möglich");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		}

		// Prüfe, ob auch mit Nachprüfung nicht genug Ausgleichsmöglichkeiten geschaffen werden können
		if (ges_ausgleichsfaecher.size() < fg2_defizit_anzahl - (nachpruefungGenutzt ? 0 : 1)) {
			logger.logLn(LogLevel.DEBUG, logIndent + " -> zu viele Defizite in FG2 - nicht genügend Ausgleichsfächer vorhanden");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, false);
		}

		// Prüfe für den Fall, dass 2 FG2-Fächer mit Defiziten mit zwei Notenstufen vorhanden sind, ob eine Nachprüfung in einem der Fächer möglich ist.
		if (fg2_defizite_2NS.size() == 2) {
			for (final GEAbschlussFach defizitFach : fg2_defizite_2NS) {
				defizitFach.ausgeglichen = true;
				defizitFach.ausgleich = true;
				defizitFach.note--; // verbessere zwischenzeitlich
				logger.logLn(LogLevel.DEBUG, logIndent + " -> Prüfe erneut mit Nachprüfung in " + defizitFach.kuerzel);
				final @NotNull AbschlussErgebnis abschlussergebnis = pruefeFG2(faecher, logIndent + "  ", npFaecher, true);
				logger.logLn(LogLevel.DEBUG, logIndent + "   -> Nachprüfung in " + defizitFach.kuerzel + (abschlussergebnis.erworben ? " möglich" : " nicht möglich"));
				if (abschlussergebnis.erworben)
					npFaecher.add(defizitFach);
				defizitFach.ausgeglichen = true;
				defizitFach.ausgleich = true;
				defizitFach.note++; // verschlechtere wieder
			}
			return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA_Q, AbschlussManager.getKuerzel(npFaecher));
		}

		// Prüfe, ob genug Ausgleichsfächer für FG2 vorhanden sind.
		if (ges_ausgleichsfaecher.size() >= fg2_defizit_anzahl) {
			logger.logLn(LogLevel.DEBUG, logIndent + " -> genug Ausgleichsfächer vorhanden." + (nachpruefungGenutzt ? "" : " Nachprüfung nicht nötig."));
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.MSA_Q, true);
		}

		// Prüfe Nachprüfungsmöglichkeiten
		for (final GEAbschlussFach defizitFach : fg2_defizite_1NS) {
			defizitFach.ausgeglichen = true;
			defizitFach.ausgleich = true;
			defizitFach.note--; // verbessere zwischenzeitlich
			logger.logLn(LogLevel.DEBUG, logIndent + " -> Prüfe erneut mit Nachprüfung in " + defizitFach.kuerzel);
			final @NotNull AbschlussErgebnis abschlussergebnis = pruefeFG2(faecher, logIndent + "  ", npFaecher, true);
			logger.logLn(LogLevel.DEBUG, logIndent + "   -> Nachprüfung in " + defizitFach.kuerzel + (abschlussergebnis.erworben ? " möglich" : " nicht möglich"));
			if (abschlussergebnis.erworben)
				npFaecher.add(defizitFach);
			defizitFach.ausgeglichen = true;
			defizitFach.ausgleich = true;
			defizitFach.note++; // verschlechtere wieder
		}

		return AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.MSA_Q, AbschlussManager.getKuerzel(npFaecher));
	}

}
