package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToLongFunction;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageDatenContext;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKlausurplanungKlausurplanSchueler;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKlausurplanungKlausurplanTermine;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKursplanungBlockungsergebnisKurse;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKursplanungBlockungsergebnisSchueler;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextKlassen;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextKurse;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextLehrer;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchueler;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextStundenplanungFachStundenplan;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextStundenplanungKlassenStundenplan;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextStundenplanungLehrerStundenplan;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextStundenplanungRaumStundenplan;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextStundenplanungSchuelerStundenplan;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import jakarta.ws.rs.core.Response.Status;

/**
 * Die Registry führt vom Datenaufbau einer Reportvorlage zu dessen Konfiguration. Sie ist eine unveränderliche Zuordnung von
 * {@link ReportingReportvorlageDatenContext} auf {@link HtmlContextAufbau} — keine Sammlung von Fabrik-Lambdas.
 * <p>Ein neuer Datenaufbau bedeutet damit einen neuen Enum-Wert plus eine Zeile in dieser Tabelle; eine neue Klasse braucht es nur bei einem neuen
 * Ablaufmuster.</p>
 */
public final class HtmlContextInitializerRegistry {

	private HtmlContextInitializerRegistry() {
		throw new IllegalStateException("Hilfsklasse - Initialisierung nicht möglich.");
	}

	/** Die Beschriftungen der drei Schüler-Datenaufbauten. Sie gehören fachlich zusammen und sind für alle drei dieselben. */
	private static final HtmlContextDatenbezeichnungen BEZEICHNUNGEN_SCHUELER = bezeichnungen("Schüler", "Schülern", "Schüler-IDs");

	/**
	 * Die Zuordnung von Datenaufbau auf dessen Konfiguration. Zu jedem Wert des Enums gehört genau ein Eintrag; der Vollständigkeitstest der Registry
	 * sichert das ab.
	 */
	private static final Map<ReportingReportvorlageDatenContext, HtmlContextAufbau> AUFBAUTEN = Map.ofEntries(

			Map.entry(ReportingReportvorlageDatenContext.SCHUELER, listenAufbau(
					BEZEICHNUNGEN_SCHUELER, HtmlContextSchluessel.SCHUELER,
					(ctx, ids) -> ctx.repositorySchueler().schueler(ids, false),
					ReportingSchueler::id, HtmlContextSchueler::new)),

			Map.entry(ReportingReportvorlageDatenContext.SCHUELER_GOST_LAUFBAHNPLANUNG, listenAufbau(
					BEZEICHNUNGEN_SCHUELER, HtmlContextSchluessel.SCHUELER,
					(ctx, ids) -> ctx.repositorySchueler().schueler(ids, false),
					ReportingSchueler::id, HtmlContextSchueler::new,
					HtmlContextValidierung::pruefungenGostLaufbahnplanung)),

			Map.entry(ReportingReportvorlageDatenContext.SCHUELER_GOST_ABITUR, listenAufbau(
					BEZEICHNUNGEN_SCHUELER, HtmlContextSchluessel.SCHUELER,
					(ctx, ids) -> ctx.repositorySchueler().schueler(ids, false),
					ReportingSchueler::id, HtmlContextSchueler::new,
					HtmlContextValidierung::pruefungenGostAbitur)),

			Map.entry(ReportingReportvorlageDatenContext.KLASSEN, listenAufbau(
					bezeichnungen("Klassen", "Klassen", "Klassen-IDs"), HtmlContextSchluessel.KLASSEN,
					(ctx, ids) -> ctx.repositoryLerngruppen().klassen(ids, false),
					ReportingKlasse::id, HtmlContextKlassen::new)),

			Map.entry(ReportingReportvorlageDatenContext.KURSE, listenAufbau(
					bezeichnungen("Kurse", "Kursen", "Kurs-IDs"), HtmlContextSchluessel.KURSE,
					(ctx, ids) -> ctx.repositoryLerngruppen().kurse(ids, false),
					ReportingKurs::id, HtmlContextKurse::new)),

			Map.entry(ReportingReportvorlageDatenContext.LEHRER, listenAufbau(
					bezeichnungen("Lehrer", "Lehrern", "Lehrer-IDs"), HtmlContextSchluessel.LEHRER,
					(ctx, ids) -> ctx.repositoryLehrer().lehrer(ids, false),
					ReportingLehrer::id, HtmlContextLehrer::new)),

			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_FACH, stundenplanAufbau(
					HtmlContextSchluessel.STUNDENPLANUNG_FAECHER, HtmlContextStundenplanungFachStundenplan::new)),

			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_KLASSEN, stundenplanAufbau(
					HtmlContextSchluessel.STUNDENPLANUNG_KLASSEN, HtmlContextStundenplanungKlassenStundenplan::new,
					HtmlContextValidierung::pruefungenStundenplanKlassen)),

			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER, stundenplanAufbau(
					HtmlContextSchluessel.STUNDENPLANUNG_LEHRER, HtmlContextStundenplanungLehrerStundenplan::new,
					HtmlContextValidierung::pruefungenStundenplanLehrer)),

			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_RAUM, stundenplanAufbau(
					HtmlContextSchluessel.STUNDENPLANUNG_RAEUME, HtmlContextStundenplanungRaumStundenplan::new)),

			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_SCHUELER, stundenplanAufbau(
					HtmlContextSchluessel.STUNDENPLANUNG_SCHUELER, HtmlContextStundenplanungSchuelerStundenplan::new,
					HtmlContextValidierung::pruefungenStundenplanSchueler)),

			Map.entry(ReportingReportvorlageDatenContext.GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG, new HtmlContextAufbauGostLaufbahnplanung()),

			Map.entry(ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_KURSE,
					new HtmlContextAufbauGostKursplanung(HtmlContextGostKursplanungBlockungsergebnisKurse::new)),

			Map.entry(ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_SCHUELER,
					new HtmlContextAufbauGostKursplanung(HtmlContextGostKursplanungBlockungsergebnisSchueler::new)),

			Map.entry(ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_SCHUELER,
					new HtmlContextAufbauGostKlausurplanung(HtmlContextGostKlausurplanungKlausurplanSchueler::new)),

			Map.entry(ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_TERMINE,
					new HtmlContextAufbauGostKlausurplanung(HtmlContextGostKlausurplanungKlausurplanTermine::new)));


	/**
	 * Liefert die Konfiguration zum übergebenen Datenaufbau.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param datenContext     Der Datenaufbau der Reportvorlage.
	 *
	 * @return Die Konfiguration des Datenaufbaus.
	 *
	 * @throws ApiOperationException Falls zum Datenaufbau kein Eintrag registriert ist.
	 */
	public static HtmlContextAufbau aufbau(final ReportingContext reportingContext, final ReportingReportvorlageDatenContext datenContext)
			throws ApiOperationException {
		final HtmlContextAufbau aufbau = AUFBAUTEN.get(datenContext);
		if (aufbau == null) {
			final String fehlermeldung =
					"FEHLER: Für den Datenaufbau %s ist kein Aufbau der Daten-Contexts registriert.".formatted(datenContext.name());
			reportingContext.logger().logLn(LogLevel.ERROR, 4, fehlermeldung);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, fehlermeldung);
		}
		return aufbau;
	}

	/**
	 * Wie {@link #aufbau(ReportingContext, ReportingReportvorlageDatenContext)}, liefert bei fehlendem Eintrag jedoch {@code null} statt einen Fehler zu
	 * werfen. Für den Vollständigkeitstest der Registry, der ohne Reporting-Context auskommen muss.
	 *
	 * @param datenContext Der Datenaufbau der Reportvorlage.
	 *
	 * @return Die Konfiguration des Datenaufbaus oder {@code null}, falls kein Eintrag registriert ist.
	 */
	static HtmlContextAufbau aufbauOderNull(final ReportingReportvorlageDatenContext datenContext) {
		return AUFBAUTEN.get(datenContext);
	}


	// ##### Hilfsmethoden, die die Tabelle oben lesbar halten #####

	/**
	 * Fasst die Beschriftungen eines Datenaufbaus zusammen.
	 *
	 * @param nominativ Die Bezeichnung im Nominativ Plural, z. B. "Kurse".
	 * @param dativ     Die Bezeichnung im Dativ Plural, z. B. "Kursen".
	 * @param idTyp     Die Bezeichnung des ID-Typs, z. B. "Kurs-IDs".
	 *
	 * @return Die Beschriftungen des Datenaufbaus.
	 */
	private static HtmlContextDatenbezeichnungen bezeichnungen(final String nominativ, final String dativ, final String idTyp) {
		return new HtmlContextDatenbezeichnungen(nominativ, dativ, idTyp);
	}

	/**
	 * Erzeugt die Konfiguration eines Datenaufbaus nach dem Listen-Muster ohne Zusatzprüfungen.
	 *
	 * @param <T>               Der Reporting-Typ der geladenen Hauptdaten.
	 * @param bezeichnungen     Die Beschriftungen für Log-Ausgaben und Fehlermeldungen.
	 * @param contextSchluessel Der Schlüssel des Haupt-Contexts in der Context-Map.
	 * @param lader             Lädt die Hauptdaten zu den übergebenen IDs.
	 * @param idExtractor       Bestimmt die ID eines geladenen Objekts.
	 * @param contextErzeuger   Erzeugt den Haupt-Context.
	 *
	 * @return Die Konfiguration des Datenaufbaus.
	 */
	private static <T> HtmlContextAufbauListe<T> listenAufbau(final HtmlContextDatenbezeichnungen bezeichnungen, final String contextSchluessel,
			final BiFunction<ReportingContext, List<Long>, List<T>> lader, final ToLongFunction<T> idExtractor,
			final Function<ReportingContext, HtmlContext<T>> contextErzeuger) {
		return listenAufbau(bezeichnungen, contextSchluessel, lader, idExtractor, contextErzeuger, (ctx, ids) -> {
			// Dieser Datenaufbau kennt keine Zusatzprüfungen.
		});
	}

	/**
	 * Erzeugt die Konfiguration eines Datenaufbaus nach dem Listen-Muster mit Zusatzprüfungen.
	 *
	 * @param <T>               Der Reporting-Typ der geladenen Hauptdaten.
	 * @param bezeichnungen     Die Beschriftungen für Log-Ausgaben und Fehlermeldungen.
	 * @param contextSchluessel Der Schlüssel des Haupt-Contexts in der Context-Map.
	 * @param lader             Lädt die Hauptdaten zu den übergebenen IDs.
	 * @param idExtractor       Bestimmt die ID eines geladenen Objekts.
	 * @param contextErzeuger   Erzeugt den Haupt-Context.
	 * @param zusatzpruefung    Die zusätzlichen Prüfungen dieses Datenaufbaus.
	 *
	 * @return Die Konfiguration des Datenaufbaus.
	 */
	// SONARQUBE WARNUNG: Es sollen max. 7 Parameter übergeben werden. Hier sind es sechs benannte Bestandteile einer Konfiguration, die einzeln lesbar sind.
	private static <T> HtmlContextAufbauListe<T> listenAufbau(final HtmlContextDatenbezeichnungen bezeichnungen, final String contextSchluessel,
			final BiFunction<ReportingContext, List<Long>, List<T>> lader, final ToLongFunction<T> idExtractor,
			final Function<ReportingContext, HtmlContext<T>> contextErzeuger, final BiConsumer<ReportingContext, List<Long>> zusatzpruefung) {
		return new HtmlContextAufbauListe<>(bezeichnungen, contextSchluessel, lader, idExtractor, contextErzeuger, zusatzpruefung);
	}

	/**
	 * Erzeugt die Konfiguration einer Sichtweise der Stundenplanung, deren Hauptdaten-IDs nicht eigens geprüft werden.
	 *
	 * @param <T>               Der Reporting-Typ der Context-Daten dieser Sichtweise.
	 * @param contextSchluessel Der Schlüssel des Haupt-Contexts in der Context-Map.
	 * @param contextErzeuger   Erzeugt den Haupt-Context aus Stundenplan und Hauptdaten-IDs.
	 *
	 * @return Die Konfiguration des Datenaufbaus.
	 */
	private static <T> HtmlContextAufbauStundenplan<T> stundenplanAufbau(final String contextSchluessel,
			final HtmlContextStundenplanErzeuger<T> contextErzeuger) {
		return stundenplanAufbau(contextSchluessel, contextErzeuger, (ctx, ids) -> {
			// Diese Sichtweise der Stundenplanung prüft ihre Hauptdaten-IDs nicht eigens.
		});
	}

	/**
	 * Erzeugt die Konfiguration einer Sichtweise der Stundenplanung mit einer Prüfung ihrer Hauptdaten-IDs.
	 *
	 * @param <T>               Der Reporting-Typ der Context-Daten dieser Sichtweise.
	 * @param contextSchluessel Der Schlüssel des Haupt-Contexts in der Context-Map.
	 * @param contextErzeuger   Erzeugt den Haupt-Context aus Stundenplan und Hauptdaten-IDs.
	 * @param pruefung          Die Prüfung der Hauptdaten-IDs dieser Sichtweise.
	 *
	 * @return Die Konfiguration des Datenaufbaus.
	 */
	private static <T> HtmlContextAufbauStundenplan<T> stundenplanAufbau(final String contextSchluessel,
			final HtmlContextStundenplanErzeuger<T> contextErzeuger, final BiConsumer<ReportingContext, List<Long>> pruefung) {
		return new HtmlContextAufbauStundenplan<>(contextSchluessel, contextErzeuger, pruefung);
	}

}
