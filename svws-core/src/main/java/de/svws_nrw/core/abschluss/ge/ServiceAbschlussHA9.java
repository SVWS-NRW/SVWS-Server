package de.svws_nrw.core.abschluss.ge;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import de.svws_nrw.asd.types.schule.SchulabschlussAllgemeinbildend;
import de.svws_nrw.core.Service;
import de.svws_nrw.core.abschluss.AbschlussManager;
import de.svws_nrw.core.data.abschluss.AbschlussErgebnis;
import de.svws_nrw.core.data.abschluss.GEAbschlussFach;
import de.svws_nrw.core.data.abschluss.GEAbschlussFaecher;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.ge.GELeistungsdifferenzierteKursart;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt einen Service zur Abschlussberechnung in Bezug auf den Hauptschulabschluss
 * nach Klasse 9 zur Verfügung.
 */
public class ServiceAbschlussHA9 extends Service<GEAbschlussFaecher, AbschlussErgebnis> {

	/** Filter für alle nicht ausgeglichenen Defizite */
	private static final @NotNull Predicate<GEAbschlussFach> filterDefizit = (final @NotNull GEAbschlussFach f) -> (f.note > 4) && (!f.ausgeglichen);

	/** Filter für alle mangelhaften Fächer */
	private static final @NotNull Predicate<GEAbschlussFach> filterMangelhaft = (final @NotNull GEAbschlussFach f) -> f.note == 5;

	/** Filter für alle ungenügenden Fächer */
	private static final @NotNull Predicate<GEAbschlussFach> filterUngenuegend = (final @NotNull GEAbschlussFach f) -> f.note == 6;

	/** Filter für alle Fächer, welche als E-Kurs belegt wurden. */
	private static final @NotNull Predicate<GEAbschlussFach> filterEKurse =
			(final @NotNull GEAbschlussFach f) -> (GELeistungsdifferenzierteKursart.E.hat(f.kursart));

	/** Filter zur Bestimmung aller Fremdsprachen, die nicht als E-Kurs belegt wurden. */
	private static final @NotNull Predicate<GEAbschlussFach> filterWeitereFremdsprachen =
			(final @NotNull GEAbschlussFach f) -> (!"E".equals(f.kuerzel) && (f.istFremdsprache != null) && (f.istFremdsprache));

	/** Die Zeichenkette, welche zum Trennen von Teilen des Logs verwendet wird. */
	private static final @NotNull String LOG_SEPERATOR = "______________________________";

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ServiceAbschlussHA9() {
		// leer
	}

	/**
	 * Führt die Abschlussberechnung anhand der übergebenen Abschlussfächer durch
	 * und gibt das Berechnungsergebnis zurück.
	 *
	 * @param input    die Abschlussfächer
	 *
	 * @return das Ergebnis der Abschlussberechnung
	 */
	@Override
	public @NotNull AbschlussErgebnis handle(final @NotNull GEAbschlussFaecher input) {
		if ("10".equals(input.jahrgang)) {
			logger.logLn(LogLevel.INFO, "Im Jahrgang 10 gibt es keinen HA9-Abschluss mehr.");
			return AbschlussManager.getErgebnis(null, false);
		}

		logger.logLn(LogLevel.INFO, "Prüfe HA9:");
		logger.logLn(LogLevel.DEBUG, "==========");

		// Prüfe, ob genügend leistungsdifferenzierte Kurse vorkommen
		if ((input.faecher == null) || (!AbschlussManager.pruefeHat4LeistungsdifferenzierteFaecher(input))) {
			logger.logLn(LogLevel.DEBUG, LOG_SEPERATOR);
			logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht genügend leistungsdiffernzierte Fächer gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}

		// Prüfe, ob Fächerkürzel mehrfach zur Abschlussprüfung übergeben wurden
		if (!AbschlussManager.pruefeKuerzelDuplikate(input)) {
			logger.logLn(LogLevel.DEBUG, LOG_SEPERATOR);
			logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden Fächer mit dem gleichen Kürzel zur Abschlussprüfung übergeben. Dies ist nicht zulässig.");
			return AbschlussManager.getErgebnis(null, false);
		}

		// Bestimme die Fächergruppen für die Berechnung des Abschlusses
		final @NotNull AbschlussFaecherGruppen faecher = new AbschlussFaecherGruppen(
				new AbschlussFaecherGruppe(input.faecher, Arrays.asList("D", "M"), null),
				new AbschlussFaecherGruppe(input.faecher, null, Arrays.asList("D", "M", "LBNW", "LBAL")));

		// Prüfe, ob alle nötigen Fächer in der FG1 vorhanden sind
		if (!faecher.fg1.istVollstaendig(Arrays.asList("D", "M"))) {
			logger.logLn(LogLevel.DEBUG, LOG_SEPERATOR);
			logger.logLn(LogLevel.DEBUG, " => Fehler: Es wurden nicht alle nötigen Leistungen für die Fächergruppe 1 gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}

		// Prüfe, ob Fächer in der Fächergruppe II vorhanden sind
		if (faecher.fg2.isEmpty()) {
			logger.logLn(LogLevel.DEBUG, LOG_SEPERATOR);
			logger.logLn(LogLevel.DEBUG, " => Fehler: Keine Leistungen für die Fächergruppe 2 gefunden.");
			return AbschlussManager.getErgebnis(null, false);
		}

		// Ignoriere alle Fremdsprachen ausser Englisch
		final @NotNull List<GEAbschlussFach> weitereFS = faecher.fg2.entferneFaecher(filterWeitereFremdsprachen);
		if (!weitereFS.isEmpty()) {
			for (final GEAbschlussFach fs : weitereFS) {
				if (fs.bezeichnung == null)
					continue;
				logger.logLn(LogLevel.DEBUG, " -> Ignoriere weitere Fremdsprache: " + fs.bezeichnung + "(" + fs.note + ")");
			}
		}

		// Verbessere ggf. die Noten aller E-Kurse in beiden Fächergruppen um eine Note
		logger.logLn(LogLevel.DEBUG, " - ggf. Verbessern der E-Kurs-Noten für die Defizitberechnung:");
		final @NotNull List<GEAbschlussFach> tmpFaecher = faecher.getFaecher(filterEKurse);
		for (final @NotNull GEAbschlussFach f : tmpFaecher) {
			if (f.kuerzel == null)
				continue;
			final int note = f.note;
			final int note_neu = (note == 1) ? 1 : (note - 1);
			logger.logLn(LogLevel.DEBUG, "   " + f.kuerzel + "(E):" + note + "->" + note_neu);
			f.note = note_neu;
		}

		// Gibt die Fächer und Noten der Fächergruppen aus, welche bei der Berechnung des Abschlusses verwendet werden
		logger.logLn(LogLevel.DEBUG, " -> FG1: Fächer " + faecher.fg1.toString());
		logger.logLn(LogLevel.DEBUG, " -> FG2: Fächer " + faecher.fg2.toString());

		// Prüfe anhand der Defizite, ob ein Abschluss erworben wurde
		final @NotNull AbschlussErgebnis abschlussergebnis = this.pruefeDefizite(faecher, "");
		if (abschlussergebnis.erworben) {
			logger.logLn(LogLevel.DEBUG, LOG_SEPERATOR);
			logger.logLn(LogLevel.INFO, " => HA 9: APO-SI §40 (3)");
		} else if (AbschlussManager.hatNachpruefungsmoeglichkeit(abschlussergebnis)) {
			logger.logLn(LogLevel.INFO, " => kein HA9 - Nachprüfungsmöglichkeite(en) in " + AbschlussManager.getNPFaecherString(abschlussergebnis));
		} else {
			logger.logLn(LogLevel.INFO, " => kein HA9 - KEINE Nachprüfungsmöglichkeiten!");
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
		final long fg1_defizite = faecher.fg1.getFaecherAnzahl(filterDefizit);
		final long fg2_defizite = faecher.fg2.getFaecherAnzahl(filterDefizit);
		final long ges_defizite = fg1_defizite + fg2_defizite;
		final long fg1_mangelhaft = faecher.fg1.getFaecherAnzahl(filterMangelhaft);
		final long fg1_ungenuegend = faecher.fg1.getFaecherAnzahl(filterUngenuegend);
		final long fg2_ungenuegend = faecher.fg2.getFaecherAnzahl(filterUngenuegend);

		if (fg1_defizite > 0)
			logger.logLn(LogLevel.DEBUG, logIndent + " -> FG1: Defizit" + (fg1_defizite > 1 ? "e" : "") + ": " + faecher.fg1.getKuerzelListe(filterDefizit));

		if (fg2_defizite > 0)
			logger.logLn(LogLevel.DEBUG, logIndent + " -> FG2: Defizit" + (fg2_defizite > 1 ? "e" : "") + ": " + faecher.fg2.getKuerzelListe(filterDefizit));

		// Kein Abschluss und keine Nachprüfungsmöglichkeit bei mind. einer 6 in FG1 oder zwei 6en in FG2
		if ((fg1_ungenuegend > 0) || (fg2_ungenuegend > 1)) {
			logger.logLn(LogLevel.DEBUG, logIndent + " -> zu oft ungenügend (6) - 0x6 in FG1 und max. 1x6 in FG2 erlaubt.");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA9, false);
		}
		logger.logLn(LogLevel.DEBUG, logIndent + " -> "
				+ ((fg2_ungenuegend == 1) ? "1x6 in FG2 erlaubt" : "0x6 in FG1 und FG2")
				+ " -> prüfe weitere Defizite");

		// Kein Abschluss bei zu vielen Defiziten in FG1
		if (fg1_mangelhaft > 2) {
			logger.logLn(LogLevel.DEBUG, logIndent + " -> zu viele Defizite: Mehr als 2x5 in FG1");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA9, false);
		}
		if ((fg1_mangelhaft == 2) && (fg2_defizite > 1)) {
			logger.logLn(LogLevel.DEBUG, logIndent + " -> zu viele Defizite: 2x5 in FG1 und mind. ein weiteres Defizit in FG2");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA9, false);
		}

		// Prüfe auf zu viele Gesamtdefizite (davon max. 1x5 in FG1)
		if (ges_defizite > 3) {
			logger.logLn(LogLevel.DEBUG, logIndent + " -> zu viele Defizite: Insgesamt mehr als 3 Defizite");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA9, false);
		}

		// Nachprüfungen -> Bestimmung der möglichen Fächer der einzelnen Fächergruppen
		final boolean hatNP = (fg1_mangelhaft == 2) || (ges_defizite == 3);
		if (hatNP) {
			// Nachprüfung kann prinzipiell bei allen Fächern mit mangelhaft erfolgen
			// bei 2x5 in FG1 muss die Nachprüfung allerdings in FG1 erfolgen!
			final @NotNull List<String> np_faecher = (fg1_mangelhaft == 2)
					? faecher.fg1.getKuerzel(filterMangelhaft)
					: faecher.getKuerzel(filterMangelhaft);
			logger.logLn(LogLevel.DEBUG, logIndent + " -> zu viele Defizite: "
					+ ((fg1_mangelhaft == 2) ? "2x5 in FG1, aber kein weiteres Defizit in FG2" : "3 Defizite nicht erlaubt"));

			final @NotNull AbschlussErgebnis abschlussergebnis = AbschlussManager.getErgebnisNachpruefung(SchulabschlussAllgemeinbildend.HA9, np_faecher);
			logger.logLn(LogLevel.INFO, " -> Nachprüfungsmöglichkeit(en) in " + AbschlussManager.getNPFaecherString(abschlussergebnis));
			return abschlussergebnis;
		}

		// Abschluss - keine Defizite vorhanden
		if ((fg1_defizite == 0) && (fg2_defizite == 0)) {
			logger.logLn(LogLevel.DEBUG, logIndent + " -> keine Defizite in FG1 und FG2");
			return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA9, true);
		}
		logger.logLn(LogLevel.DEBUG, logIndent + " -> zwei Defizite erlaubt (solange nicht beide in FG1)");
		return AbschlussManager.getErgebnis(SchulabschlussAllgemeinbildend.HA9, true);
	}


}
