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
 * ohne gymnasiale Oberstufe. Die werfenden Prüfungen behalten ihr Logging-Verhalten: Die Prüfung der leeren ID-Liste und die Prüfung auf gymnasiale
 * Oberstufe protokollieren zuvor, die Prüfungen für Abiturjahrgang und Halbjahre werfen ohne eigenen Log-Eintrag.</p>
 */
final class HtmlContextValidierung {

	/** Die Meldung für eine im Request leere ID-Liste; der Platzhalter nimmt den ID-Typ des jeweiligen Datenaufbaus auf. */
	private static final String FEHLER_KEINE_IDS = "FEHLER: Es wurden keine %s übergeben.";

	private HtmlContextValidierung() {
		throw new IllegalStateException("Hilfsklasse - Initialisierung nicht möglich.");
	}

	/**
	 * Prüft, dass die Anfrage überhaupt Hauptdaten benennt, und meldet je ausgelassener ID ein Ausgabeproblem mit der Ursache aus ihrem Ladezustand. Eine ID,
	 * die sich nicht auflösen lässt, beendet den Report nicht mehr; geworfen wird allein bei einer im Request leeren Liste, denn dort hat der Aufrufer nichts
	 * angefordert. Die vom Benutzerfilter ausgeschlossenen IDs bleiben ungemeldet: Sie fehlen, weil der Anwender es so wollte.
	 *
	 * @param <T>              Typ der ausgewählten Objekte.
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param auswahl          Das Ergebnis der Auswahl.
	 * @param objektart        Die Objektart für den Schlüssel des Ausgabeproblems.
	 * @param bezeichnungen    Die Beschriftungen des Datenaufbaus für Meldungen.
	 *
	 * @throws ApiOperationException Falls die Anfrage keine Hauptdaten benennt.
	 */
	static <T> void pruefeUndMeldeAuswahl(final ReportingContext reportingContext, final ReportingAuswahlergebnis<T> auswahl, final Class<?> objektart,
			final HtmlContextDatenbezeichnungen bezeichnungen) throws ApiOperationException {
		if (auswahl.idsAngefordert().isEmpty()) {
			final String fehlermeldung = FEHLER_KEINE_IDS.formatted(bezeichnungen.idTyp());
			reportingContext.logger().logLn(LogLevel.ERROR, 4, fehlermeldung);
			throw new ApiOperationException(Status.BAD_REQUEST, fehlermeldung);
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
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 *
	 * @throws ApiOperationException Falls die Schule keine gymnasiale Oberstufe besitzt.
	 */
	static void validiereSchuleMitGost(final ReportingContext reportingContext) throws ApiOperationException {
		if (!reportingContext.repositorySchule().istSchuleMitGost()) {
			reportingContext.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Die Schule besitzt keine gymnasiale Oberstufe (GOSt).");
			throw new ApiOperationException(Status.BAD_REQUEST, "FEHLER: Die Schule besitzt keine gymnasiale Oberstufe (GOSt).");
		}
	}

	/**
	 * Schränkt die Auswahl der GOSt-Laufbahnplanung auf die Schüler ein, zu denen Beratungsdaten und darin Abiturdaten vorliegen.
	 * <p>Eine Schule ohne gymnasiale Oberstufe bleibt ein Wurf, denn das ist eine verletzte Voraussetzung des gesamten Reports. Ein einzelner Schüler ohne die
	 * geforderten Daten wird dagegen ausgelassen.</p>
	 *
	 * @param <T>              Typ der ausgewählten Objekte.
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
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
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
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
	 * Validiert die Parameter für Gost-Daten.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param paarweise        Gibt an, ob die Daten paarweise (Abiturjahrgang+GOSt-Halbjahr, Abiturjahrgang+GOSt-Halbjahr, ...) vorliegen müssen.
	 *                         Ist der Wert false, wird ein einzelner Abiturjahrgang gefolgt von beliebigen Halbjahren erwartet.
	 *
	 * @throws ApiOperationException Falls die Parameter ungültig sind.
	 */
	static void validiereParameterFuerAbiturjahrgangUndHalbjahre(final ReportingContext reportingContext, final boolean paarweise)
			throws ApiOperationException {
		final List<Long> parameterDaten = reportingContext.reportingParameter().idsHauptdaten();
		if (parameterDaten.isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"FEHLER: Die Parameter für Abiturjahrgang und GOSt-Halbjahr wurden nicht übergeben.");
		}
		try {
			final List<Integer> vorhandeneAbiturjahrgaenge = reportingContext.repositoryGost().abiturjahrgaenge();

			if (paarweise) {
				validiereParameterPaarweise(parameterDaten, vorhandeneAbiturjahrgaenge);
			} else {
				validiereParameterEinzeln(parameterDaten, vorhandeneAbiturjahrgaenge);
			}
		} catch (final ApiOperationException aoe) {
			throw aoe;
		} catch (final Exception e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e,
					"FEHLER: Die Parameter für Abiturjahrgang und GOSt-Halbjahr konnten nicht gelesen werden oder sind außerhalb des Wertebereichs.");
		}
	}

	/**
	 * Validiert die Parameter für Gost-Daten paarweise, d. h. am Abiturjahr ist direkt das Gost-Halbjahr angehängt (beispielsweise 20253).
	 *
	 * @param parameterDaten             Liste der Parameter
	 * @param vorhandeneAbiturjahrgaenge Liste der vorhandenen Abiturjahrgänge
	 *
	 * @throws ApiOperationException Falls die Parameter ungültig sind.
	 */
	static void validiereParameterPaarweise(final List<Long> parameterDaten, final List<Integer> vorhandeneAbiturjahrgaenge)
			throws ApiOperationException {
		for (final Long kombinierteId : parameterDaten) {
			if (kombinierteId != null) {
				final int abiturjahr = (int) (kombinierteId / 10);
				validiereAbiturjahr(abiturjahr, vorhandeneAbiturjahrgaenge);
				final int idGostHalbjahr = (int) (kombinierteId % 10);
				validiereHalbjahr(idGostHalbjahr);
			}
		}
	}

	/**
	 * Validiert die Parameter für Gost-Daten einzeln (ein Abiturjahrgang gefolgt von beliebigen Halbjahren).
	 * <p>Die Liste muss mindestens den Abiturjahrgang enthalten; auf eine leere Liste prüft der Aufrufer
	 * {@link #validiereParameterFuerAbiturjahrgangUndHalbjahre(ReportingContext, boolean)}.</p>
	 *
	 * @param parameterDaten             Liste der Parameter
	 * @param vorhandeneAbiturjahrgaenge Liste der vorhandenen Abiturjahrgänge
	 *
	 * @throws ApiOperationException Falls die Parameter ungültig sind.
	 */
	static void validiereParameterEinzeln(final List<Long> parameterDaten, final List<Integer> vorhandeneAbiturjahrgaenge)
			throws ApiOperationException {
		validiereAbiturjahr(Math.toIntExact(parameterDaten.getFirst()), vorhandeneAbiturjahrgaenge);
		for (int i = 1; i < parameterDaten.size(); i++) {
			validiereHalbjahr(Math.toIntExact(parameterDaten.get(i)));
		}
	}

	/**
	 * Validiert ein einzelnes Abiturjahr.
	 *
	 * @param abiturjahr                 das zu prüfende Abiturjahr
	 * @param vorhandeneAbiturjahrgaenge Liste der vorhandenen Abiturjahrgänge
	 *
	 * @throws ApiOperationException Falls das Abiturjahr ungültig ist.
	 */
	static void validiereAbiturjahr(final int abiturjahr, final List<Integer> vorhandeneAbiturjahrgaenge) throws ApiOperationException {
		if ((abiturjahr < 1900) || !vorhandeneAbiturjahrgaenge.contains(abiturjahr)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "FEHLER: Ein Abiturjahr liegt außerhalb des Wertebereichs.");
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
			throw new ApiOperationException(Status.BAD_REQUEST, "FEHLER: Ein GOSt-Halbjahr liegt außerhalb des Wertebereichs.");
		}
	}

}
