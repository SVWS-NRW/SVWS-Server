package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import jakarta.ws.rs.core.Response.Status;

/**
 * Hilfsklasse mit den Prüfungen der Eingabeparameter für den Aufbau der Daten-Contexts.
 * <p>Die Methoden sind statisch und nehmen den {@link ReportingContext} als Parameter. Nur so lassen sie sich sowohl aus den Initializern als auch als
 * Methodenreferenz aus der request-unabhängigen Konfiguration der Registry heraus verwenden.</p>
 * <p>Die Prüfungen werfen im Fehlerfall eine {@link ApiOperationException} und behalten dabei ihr bisheriges Logging-Verhalten: Die ID-Prüfungen und die
 * Prüfung auf gymnasiale Oberstufe protokollieren zuvor über {@code reportingContext.logger()}, die Prüfungen für Abiturjahrgang und Halbjahre werfen ohne
 * eigenen Log-Eintrag.</p>
 */
final class HtmlContextValidierung {

	/** Die Bezeichnung des ID-Typs in den Fehlermeldungen der Schüler-Prüfungen. */
	private static final String SCHUELER_IDS = "Schüler-IDs";

	private HtmlContextValidierung() {
		throw new IllegalStateException("Hilfsklasse - Initialisierung nicht möglich.");
	}

	/**
	 * Bereinigt die übergebene Liste von IDs (null-Einträge und Duplikate entfernen) und prüft anschließend, dass die bereinigte Liste nicht leer ist
	 * und zu jeder enthaltenen ID ein passendes Objekt in {@code geladeneObjekte} existiert.
	 * Die IDs der geladenen Objekte werden mittels des übergebenen {@code idExtractor} bestimmt.
	 * Im Fehlerfall wird die zugehörige Meldung geloggt und eine {@link ApiOperationException} mit Status {@link Status#BAD_REQUEST} geworfen.
	 *
	 * @param <T>                         Typ der geladenen Objekte.
	 * @param reportingContext            Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param idsUebergeben               Liste der übergebenen IDs (kann null-Einträge und Duplikate enthalten).
	 * @param geladeneObjekte             Die zu den IDs geladenen Objekte.
	 * @param idExtractor                 Funktion zur Bestimmung der ID eines geladenen Objekts.
	 * @param fehlermeldungIdTyp          Fehlermeldung, falls die bereinigte ID-Liste leer ist.
	 * @param fehlermeldungUnvollstaendig Fehlermeldung, falls eine bereinigte ID nicht in {@code geladeneObjekte} enthalten ist.
	 *
	 * @throws ApiOperationException Falls die bereinigte Liste leer oder unvollständig ist.
	 */
	static <T> void validiereIds(final ReportingContext reportingContext, final List<Long> idsUebergeben, final Collection<T> geladeneObjekte,
			final ToLongFunction<T> idExtractor, final String fehlermeldungIdTyp, final String fehlermeldungUnvollstaendig) throws ApiOperationException {
		final Set<Long> idsVorhanden = geladeneObjekte.stream().mapToLong(idExtractor).boxed().collect(Collectors.toSet());
		validiereIds(reportingContext, idsUebergeben, idsVorhanden, fehlermeldungIdTyp, fehlermeldungUnvollstaendig);
	}

	/**
	 * Wie {@link #validiereIds(ReportingContext, List, Collection, ToLongFunction, String, String)}, jedoch für Map-basierte Lade-Ergebnisse: eine ID gilt als
	 * vorhanden, wenn der zugehörige Map-Eintrag einen Wert ungleich {@code null} besitzt.
	 *
	 * @param <V>                         Typ der Map-Werte.
	 * @param reportingContext            Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param idsUebergeben               Liste der übergebenen IDs (kann null-Einträge und Duplikate enthalten).
	 * @param geladeneObjekte             Map mit ID als Schlüssel und dem geladenen Objekt als Wert (Wert {@code null} bedeutet "nicht vorhanden").
	 * @param fehlermeldungIdTyp          Fehlermeldung, falls die bereinigte ID-Liste leer ist.
	 * @param fehlermeldungUnvollstaendig Fehlermeldung, falls für eine bereinigte ID kein Eintrag mit Wert ungleich {@code null} existiert.
	 *
	 * @throws ApiOperationException Falls die bereinigte Liste leer oder unvollständig ist.
	 */
	static <V> void validiereIds(final ReportingContext reportingContext, final List<Long> idsUebergeben, final Map<Long, V> geladeneObjekte,
			final String fehlermeldungIdTyp, final String fehlermeldungUnvollstaendig) throws ApiOperationException {
		final Set<Long> idsVorhanden = geladeneObjekte.entrySet().stream()
				.filter(e -> e.getValue() != null).map(Map.Entry::getKey).collect(Collectors.toSet());
		validiereIds(reportingContext, idsUebergeben, idsVorhanden, fehlermeldungIdTyp, fehlermeldungUnvollstaendig);
	}

	/**
	 * Bereinigt die übergebene Roh-Liste von IDs (null-Einträge und Duplikate entfernen) und prüft anschließend, dass die bereinigte Liste nicht leer ist
	 * und jede enthaltene ID in {@code idsVorhanden} existiert. Im Fehlerfall wird die zugehörige Meldung geloggt und eine
	 * {@link ApiOperationException} mit Status {@link Status#BAD_REQUEST} geworfen.
	 *
	 * @param reportingContext            Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param idsUebergeben               Liste der übergebenen IDs (kann null-Einträge und Duplikate enthalten).
	 * @param idsVorhanden                Menge der tatsächlich vorhandenen IDs.
	 * @param fehlermeldungIdTyp          Fehlermeldung, falls die bereinigte ID-Liste leer ist.
	 * @param fehlermeldungUnvollstaendig Fehlermeldung, falls eine bereinigte ID nicht in {@code idsVorhanden} enthalten ist.
	 *
	 * @throws ApiOperationException Falls die bereinigte Liste leer oder unvollständig ist.
	 */
	private static void validiereIds(final ReportingContext reportingContext, final List<Long> idsUebergeben, final Set<Long> idsVorhanden,
			final String fehlermeldungIdTyp, final String fehlermeldungUnvollstaendig) throws ApiOperationException {
		final List<Long> idsBereinigt = idsUebergeben.stream().filter(Objects::nonNull).distinct().toList();
		if (idsBereinigt.isEmpty()) {
			reportingContext.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden keine %s übergeben.".formatted(fehlermeldungIdTyp));
			throw new ApiOperationException(Status.BAD_REQUEST, "FEHLER: Es wurden keine %s übergeben.".formatted(fehlermeldungIdTyp));
		}
		for (final Long id : idsBereinigt) {
			if (!idsVorhanden.contains(id)) {
				reportingContext.logger().logLn(LogLevel.ERROR, 4, fehlermeldungUnvollstaendig);
				throw new ApiOperationException(Status.BAD_REQUEST, fehlermeldungUnvollstaendig);
			}
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
	 * Prüft die übergebenen Klassen-IDs eines Klassen-Stundenplans.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param ids              Die übergebenen Klassen-IDs.
	 *
	 * @throws ApiOperationException Falls die Liste leer ist oder eine ID nicht vorhanden ist.
	 */
	static void pruefungenStundenplanKlassen(final ReportingContext reportingContext, final List<Long> ids) throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten der Klassen für einen Stundenplan für die HTML-Generierung.");
		validiereIds(reportingContext, ids, reportingContext.repositoryLerngruppen().klassen(ids, false), ReportingKlasse::id,
				"Klassen-IDs", "FEHLER: Es wurden ungültige Klassen-IDs übergeben.");
	}

	/**
	 * Prüft die übergebenen Lehrer-IDs eines Lehrer-Stundenplans.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param ids              Die übergebenen Lehrer-IDs.
	 *
	 * @throws ApiOperationException Falls die Liste leer ist oder eine ID nicht vorhanden ist.
	 */
	static void pruefungenStundenplanLehrer(final ReportingContext reportingContext, final List<Long> ids) throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten der Lehrkräfte für einen Stundenplan für die HTML-Generierung.");
		validiereIds(reportingContext, ids, reportingContext.repositoryLehrer().lehrer(ids, false), ReportingLehrer::id,
				"Lehrer-IDs", "FEHLER: Es wurden ungültige Lehrer-IDs übergeben.");
	}

	/**
	 * Prüft die übergebenen Schüler-IDs eines Schüler-Stundenplans.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param ids              Die übergebenen Schüler-IDs.
	 *
	 * @throws ApiOperationException Falls die Liste leer ist oder eine ID nicht vorhanden ist.
	 */
	static void pruefungenStundenplanSchueler(final ReportingContext reportingContext, final List<Long> ids) throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten der Schüler für einen Stundenplan für die HTML-Generierung.");
		validiereIds(reportingContext, ids, reportingContext.repositorySchueler().schueler(ids, false), ReportingSchueler::id,
				SCHUELER_IDS, "FEHLER: Es wurden ungültige Schüler-IDs übergeben.");
	}

	/**
	 * Prüft die Schülerdaten der GOSt-Laufbahnplanung: Die Schule besitzt eine gymnasiale Oberstufe, zu allen übergebenen Schülern liegen Beratungsdaten
	 * vor und zu allen Beratungsdaten gehören Abiturdaten der Laufbahnplanung.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param ids              Die übergebenen Schüler-IDs.
	 *
	 * @throws ApiOperationException Falls die Schule keine gymnasiale Oberstufe besitzt oder zu einer ID die geforderten Daten fehlen.
	 */
	static void pruefungenGostLaufbahnplanung(final ReportingContext reportingContext, final List<Long> ids) throws ApiOperationException {
		validiereSchuleMitGost(reportingContext);
		validiereIds(reportingContext, ids, reportingContext.repositoryGost().beratungsdaten(ids),
				SCHUELER_IDS, "FEHLER: Es wurden Schüler-IDs übergeben, die nicht zur GOSt gehören.");
		validiereIds(reportingContext, ids, reportingContext.repositoryGost().beratungsdatenAbiturdaten(ids),
				SCHUELER_IDS, "FEHLER: Es wurden Schüler-IDs übergeben, für die keine Abiturdaten in der GOSt-Laufbahnplanung existieren.");
	}

	/**
	 * Prüft die Schülerdaten des GOSt-Abiturs: Die Schule besitzt eine gymnasiale Oberstufe und zu allen übergebenen Schülern liegen Abiturdaten vor.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param ids              Die übergebenen Schüler-IDs.
	 *
	 * @throws ApiOperationException Falls die Schule keine gymnasiale Oberstufe besitzt oder zu einer ID die Abiturdaten fehlen.
	 */
	static void pruefungenGostAbitur(final ReportingContext reportingContext, final List<Long> ids) throws ApiOperationException {
		validiereSchuleMitGost(reportingContext);
		validiereIds(reportingContext, ids, reportingContext.repositoryGost().schuelerAbiturdaten(ids),
				SCHUELER_IDS, "FEHLER: Es wurden Schüler-IDs übergeben, für die keine Abiturdaten in der GOSt existieren.");
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
	private static void validiereParameterPaarweise(final List<Long> parameterDaten, final List<Integer> vorhandeneAbiturjahrgaenge)
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
	 *
	 * @param parameterDaten             Liste der Parameter
	 * @param vorhandeneAbiturjahrgaenge Liste der vorhandenen Abiturjahrgänge
	 *
	 * @throws ApiOperationException Falls die Parameter ungültig sind.
	 */
	private static void validiereParameterEinzeln(final List<Long> parameterDaten, final List<Integer> vorhandeneAbiturjahrgaenge)
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
	private static void validiereAbiturjahr(final int abiturjahr, final List<Integer> vorhandeneAbiturjahrgaenge) throws ApiOperationException {
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
	private static void validiereHalbjahr(final int halbjahrId) throws ApiOperationException {
		if (GostHalbjahr.fromID(halbjahrId) == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "FEHLER: Ein GOSt-Halbjahr liegt außerhalb des Wertebereichs.");
		}
	}

}
