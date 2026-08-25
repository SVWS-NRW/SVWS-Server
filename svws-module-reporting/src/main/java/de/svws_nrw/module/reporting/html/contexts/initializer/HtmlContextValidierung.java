package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.List;
import java.util.Map;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingAuswahlergebnis;
import de.svws_nrw.module.reporting.diagnose.ReportingLadezustand;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import jakarta.ws.rs.core.Response.Status;

/**
 * Hilfsklasse mit den Prüfungen der Eingabeparameter für den Aufbau der Daten-Contexts.
 * <p>Die Methoden sind statisch. Alle Prüfungen, die Daten nachladen, nehmen den {@link ReportingContext} als Parameter. Nur so lassen sie sich sowohl aus
 * den Initializern als auch als Methodenreferenz aus der request-unabhängigen Konfiguration der Registry heraus verwenden. Die reinen Wertprüfungen für
 * Abiturjahr und Halbjahr kommen ganz ohne Infrastruktur aus und sind dadurch ohne Context testbar.</p>
 * <p>Nicht jeder Befund beendet die Ausgabe: Ein einzelner Datensatz, der sich nicht auflösen lässt oder fachlich nicht dazugehört, wird ausgelassen.
 * Geworfen wird allein bei einer verletzten Voraussetzung des gesamten Reports - eine im Request leere ID-Liste, ein unzulässiger Parameterwert, eine Schule
 * ohne gymnasiale Oberstufe. Die geworfene Exception trägt den Abbruchgrund als Meldung; protokolliert wird er an der Abschlussgrenze.</p>
 * <p>Eine Ausnahme macht die Prüfung der Parameter eines Abiturjahrgangs, und dort nur bei einem ungültigen Halbjahr oder einem Zahlenüberlauf: Welcher
 * übergebene Wert beanstandet wird, nennt dann weder die Meldung noch das Eingangsprotokoll. Diese eine technische Angabe hält eine eigene Log-Zeile fest.
 * Die Meldungen zum Abiturjahrgang tragen den Wert dagegen selbst und kommen ohne sie aus.</p>
 */
final class HtmlContextValidierung {

	/** Die Meldung für eine im Request leere ID-Liste; der Platzhalter nimmt die Bezeichnung des jeweiligen Datenaufbaus auf. */
	private static final String FEHLER_KEINE_IDS = "### FEHLER: Es wurden keine %s ausgewählt. Für die Ausgabe ist mindestens ein Datensatz auszuwählen.";

	/** Die Meldung für Parameter, die sich nicht als Abiturjahrgang und GOSt-Halbjahre auswerten lassen. */
	private static final String FEHLER_PARAMETER_UNLESBAR = "### FEHLER: Die Angaben zu Abiturjahrgang und GOSt-Halbjahr konnten nicht "
			+ "ausgewertet werden.";

	private HtmlContextValidierung() {
		throw new IllegalStateException("Hilfsklasse - Initialisierung nicht möglich.");
	}

	/**
	 * Prüft, dass die Anfrage überhaupt Hauptdaten benennt, und meldet je ausgelassener ID ein Ausgabeproblem mit der Ursache aus ihrem Ladezustand. Eine ID,
	 * die sich nicht auflösen lässt, beendet den Report nicht mehr; geworfen wird allein bei einer im Request leeren Liste, denn dort hat der Aufrufer nichts
	 * angefordert. Die vom Benutzerfilter ausgeschlossenen IDs bleiben ungemeldet: Sie fehlen, weil der Anwender es so wollte.
	 *
	 * @param <T>              Typ der ausgewählten Objekte.
	 * @param reportingContext Context mit Parametern, Repositories und der Meldefassade für Ausgabeprobleme.
	 * @param auswahl          Das Ergebnis der Auswahl.
	 * @param objektart        Die Objektart für den Schlüssel des Ausgabeproblems.
	 * @param bezeichnungen    Die Beschriftungen des Datenaufbaus für Meldungen.
	 *
	 * @throws ApiOperationException Falls die Anfrage keine Hauptdaten benennt.
	 */
	static <T> void pruefeUndMeldeAuswahl(final ReportingContext reportingContext, final ReportingAuswahlergebnis<T> auswahl, final Class<?> objektart,
			final HtmlContextDatenbezeichnungen bezeichnungen) throws ApiOperationException {
		if (auswahl.idsAngefordert().isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST, FEHLER_KEINE_IDS.formatted(bezeichnungen.nominativ()));
		}

		for (final Map.Entry<Long, ReportingLadezustand<T>> ausgelassen : auswahl.ausgelassen().entrySet()) {
			reportingContext.meldeAusgabeproblem(ausgelassen.getValue().ursache(), ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN,
					ReportingProblemSchluessel.fuer(objektart, ausgelassen.getKey()),
					"Der Datensatz aus %s wird in der Ausgabe ausgelassen.".formatted(bezeichnungen.dativ()), ausgelassen.getValue().fehler());
		}
	}

	/**
	 * Prüft, ob die Schule eine gymnasiale Oberstufe (GOSt) besitzt, wenn dies für Datenquellen relevant ist.
	 *
	 * @param reportingContext Context mit Parametern, Repositories und der Meldefassade für Ausgabeprobleme.
	 *
	 * @throws ApiOperationException Falls die Schule keine gymnasiale Oberstufe besitzt.
	 */
	static void validiereSchuleMitGost(final ReportingContext reportingContext) throws ApiOperationException {
		if (!reportingContext.repositorySchule().istSchuleMitGost()) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"### FEHLER: Der Report ist nur für Schulen mit gymnasialer Oberstufe (GOSt) vorgesehen.");
		}
	}

	/**
	 * Schränkt die Auswahl der GOSt-Laufbahnplanung auf die Schüler ein, zu denen Beratungsdaten und darin Abiturdaten vorliegen.
	 * <p>Eine Schule ohne gymnasiale Oberstufe bleibt ein Wurf, denn das ist eine verletzte Voraussetzung des gesamten Reports. Ein einzelner Schüler ohne die
	 * geforderten Daten wird dagegen ausgelassen.</p>
	 *
	 * @param <T>              Typ der ausgewählten Objekte.
	 * @param reportingContext Context mit Parametern, Repositories und der Meldefassade für Ausgabeprobleme.
	 * @param auswahl          Die bisherige Auswahl.
	 *
	 * @return Die eingeschränkte Auswahl.
	 *
	 * @throws ApiOperationException Falls die Schule keine gymnasiale Oberstufe besitzt.
	 */
	static <T> ReportingAuswahlergebnis<T> pruefungenGostLaufbahnplanung(final ReportingContext reportingContext,
			final ReportingAuswahlergebnis<T> auswahl) throws ApiOperationException {
		validiereSchuleMitGost(reportingContext);
		// Schrittweise: Die zweite Prüfung fragt nur noch die Schüler ab, die die erste übrig gelassen hat. Ein Schüler, der nicht zur GOSt gehört, würde
		// sonst erneut geladen und könnte einen zweiten, irreführenden Befund erzeugen.
		final ReportingAuswahlergebnis<T> mitBeratungsdaten =
				auswahl.nurMitGeladenen(reportingContext.repositoryGost().zustaendeBeratungsdaten(auswahl.idsAusgewaehlt()));
		return mitBeratungsdaten
				.nurMitGeladenen(reportingContext.repositoryGost().zustaendeBeratungsdatenAbiturdaten(mitBeratungsdaten.idsAusgewaehlt()));
	}

	/**
	 * Schränkt die Auswahl des GOSt-Abiturs auf die Schüler ein, zu denen Abiturdaten vorliegen.
	 *
	 * @param <T>              Typ der ausgewählten Objekte.
	 * @param reportingContext Context mit Parametern, Repositories und der Meldefassade für Ausgabeprobleme.
	 * @param auswahl          Die bisherige Auswahl.
	 *
	 * @return Die eingeschränkte Auswahl.
	 *
	 * @throws ApiOperationException Falls die Schule keine gymnasiale Oberstufe besitzt.
	 */
	static <T> ReportingAuswahlergebnis<T> pruefungenGostAbitur(final ReportingContext reportingContext, final ReportingAuswahlergebnis<T> auswahl)
			throws ApiOperationException {
		validiereSchuleMitGost(reportingContext);
		return auswahl.nurMitGeladenen(reportingContext.repositoryGost().zustaendeSchuelerAbiturdaten(auswahl.idsAusgewaehlt()));
	}

	/**
	 * Validiert die Parameter eines Reports, dessen Hauptressource ein einzelner Abiturjahrgang ist - etwa die Fachwahlstatistiken der GOSt-Laufbahnplanung.
	 * Erwartet wird ein Abiturjahrgang gefolgt von beliebigen GOSt-Halbjahren.
	 * <p>Die Statuswahl folgt der Rolle: Ein nicht vorhandener Abiturjahrgang ist die adressierte Hauptressource und ergibt {@code 404}, ein unlesbarer Wert
	 * oder eine Wertebereichsverletzung {@code 400}. Ein Fehler beim Laden der vorhandenen Abiturjahrgänge ist ein Serverproblem; das Repository wirft ihn
	 * statustragend, und er bleibt hier unangetastet.</p>
	 *
	 * @param reportingContext Context mit Parametern, Repositories, Logger und der Meldefassade für Ausgabeprobleme.
	 *
	 * @throws ApiOperationException Falls die Parameter ungültig sind oder die vorhandenen Abiturjahrgänge nicht geladen werden konnten.
	 */
	static void validiereAbiturjahrgangAlsHauptressource(final ReportingContext reportingContext) throws ApiOperationException {
		final List<Long> parameterDaten = pflichtParameterAbiturjahrgang(reportingContext);
		final List<Integer> vorhandeneAbiturjahrgaenge = reportingContext.repositoryGost().abiturjahrgaenge();
		// Die Schleife läuft hier und nicht in den Prüfmethoden, weil allein sie den beanstandeten Wert kennt: Die Meldungen für Halbjahr und Überlauf
		// nennen ihn nicht, und das Eingangsprotokoll führt nur einen Auszug der Rohwerte.
		for (int i = 0; i < parameterDaten.size(); i++) {
			final Long wert = parameterDaten.get(i);
			try {
				if (i == 0) {
					validiereAbiturjahrgang(Math.toIntExact(wert), vorhandeneAbiturjahrgaenge);
				} else {
					validiereHalbjahr(Math.toIntExact(wert));
				}
			} catch (final ApiOperationException aoe) {
				// Die Meldungen zum Abiturjahrgang nennen den Wert selbst; nur die Meldung zum Halbjahr lässt ihn aus.
				if (i > 0) {
					protokolliereBeanstandetenWert(reportingContext, wert);
				}
				throw aoe;
			} catch (final Exception e) {
				// Hier auch für den Abiturjahrgang: Ein übergelaufener Wert erreicht die Prüfung gar nicht, und Math.toIntExact meldet ihn nicht.
				protokolliereBeanstandetenWert(reportingContext, wert);
				throw new ApiOperationException(Status.BAD_REQUEST, e, FEHLER_PARAMETER_UNLESBAR);
			}
		}
	}


	/**
	 * Hält den beanstandeten Wert im Log fest, damit er auffindbar bleibt.
	 * <p>Aufgerufen wird sie nur, wo keine andere Ebene den Wert trägt: Ein ungültiges Halbjahr wird ohne Wert abgewiesen, ein Wert jenseits des
	 * Zahlenbereichs meldet allein "integer overflow". Die Meldungen zum Abiturjahrgang nennen ihn dagegen selbst. Das Eingangsprotokoll hilft in keinem
	 * Fall, denn es zeigt einen Auszug der Rohwerte, während hier die von Dubletten und Leerwerten bereinigte Liste geprüft wird. Die Zeile nennt allein
	 * diesen einen Wert; die ganze Liste stünde einer Anfrage mit vielen IDs ungekürzt im Log.</p>
	 *
	 * @param reportingContext Context mit Parametern, Repositories, Logger und der Meldefassade für Ausgabeprobleme.
	 * @param wert             Der Wert, an dem die Prüfung scheitert.
	 */
	private static void protokolliereBeanstandetenWert(final ReportingContext reportingContext, final Long wert) {
		reportingContext.logger().logLn(LogLevel.ERROR, 4, "Beanstandeter Parameter: " + wert);
	}

	/**
	 * Validiert den Abiturjahrgang als Hauptressource eines Reports. Existiert er nicht, lautet die Antwort {@code 404}; ein Wert außerhalb des
	 * Wertebereichs bleibt ein Client-Fehler mit {@code 400}.
	 *
	 * @param abiturjahr                 Das zu prüfende Abiturjahr.
	 * @param vorhandeneAbiturjahrgaenge Liste der vorhandenen Abiturjahrgänge
	 *
	 * @throws ApiOperationException Falls das Abiturjahr außerhalb des Wertebereichs liegt oder der Abiturjahrgang nicht existiert.
	 */
	static void validiereAbiturjahrgang(final int abiturjahr, final List<Integer> vorhandeneAbiturjahrgaenge) throws ApiOperationException {
		if (abiturjahr < 1900) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"### FEHLER: Das Abiturjahr '%d' liegt außerhalb des zulässigen Bereichs.".formatted(abiturjahr));
		}
		if (!vorhandeneAbiturjahrgaenge.contains(abiturjahr)) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"### FEHLER: Der Abiturjahrgang '%d' ist an dieser Schule nicht vorhanden.".formatted(abiturjahr));
		}
	}

	/**
	 * Validiert ein einzelnes GOSt-Halbjahr.
	 *
	 * @param halbjahrId die ID des Halbjahres
	 *
	 * @throws ApiOperationException Falls das GOSt-Halbjahr ungültig ist.
	 */
	static void validiereHalbjahr(final int halbjahrId) throws ApiOperationException {
		if (GostHalbjahr.fromID(halbjahrId) == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "### FEHLER: Ein angegebenes GOSt-Halbjahr ist ungültig.");
		}
	}

	/**
	 * Liefert die Hauptdaten-IDs des Requests und weist eine leere Liste als Client-Fehler ab.
	 *
	 * @param reportingContext Context mit Parametern, Repositories und der Meldefassade für Ausgabeprobleme.
	 *
	 * @return Die Hauptdaten-IDs des Requests.
	 *
	 * @throws ApiOperationException Falls der Request keine Hauptdaten benennt.
	 */
	private static List<Long> pflichtParameterAbiturjahrgang(final ReportingContext reportingContext) throws ApiOperationException {
		final List<Long> parameterDaten = reportingContext.reportingParameter().idsHauptdaten();
		if (parameterDaten.isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "### FEHLER: Es wurden kein Abiturjahrgang und keine GOSt-Halbjahre übergeben.");
		}
		return parameterDaten;
	}

}
