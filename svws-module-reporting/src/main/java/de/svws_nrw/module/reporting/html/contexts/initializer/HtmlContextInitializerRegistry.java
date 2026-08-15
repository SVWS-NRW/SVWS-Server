package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageDatenContext;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingAuswahlergebnis;
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
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungRaum;
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

	/** Die Beschriftungen der Schüler-Datenaufbauten. Sie gelten für die drei Listen-Aufbauten und den Schüler-Stundenplan gleichermaßen. */
	private static final HtmlContextDatenbezeichnungen BEZEICHNUNGEN_SCHUELER = bezeichnungen("Schüler", "Schülern", "Schüler-IDs");

	/** Die Beschriftungen der Klassen-Datenaufbauten - für die Klassenliste und den Klassen-Stundenplan dieselben. */
	private static final HtmlContextDatenbezeichnungen BEZEICHNUNGEN_KLASSEN = bezeichnungen("Klassen", "Klassen", "Klassen-IDs");

	/** Die Beschriftungen der Lehrer-Datenaufbauten - für die Lehrerliste und den Lehrer-Stundenplan dieselben. */
	private static final HtmlContextDatenbezeichnungen BEZEICHNUNGEN_LEHRER = bezeichnungen("Lehrer", "Lehrern", "Lehrer-IDs");

	/**
	 * Die Zuordnung von Datenaufbau auf dessen Konfiguration. Zu jedem Wert des Enums gehört genau ein Eintrag; der Vollständigkeitstest der Registry
	 * sichert das ab.
	 */
	private static final Map<ReportingReportvorlageDatenContext, HtmlContextAufbau> AUFBAUTEN = Map.ofEntries(

			Map.entry(ReportingReportvorlageDatenContext.SCHUELER, listenAufbau(
					BEZEICHNUNGEN_SCHUELER, HtmlContextSchluessel.SCHUELER, ReportingSchueler.class,
					(ctx, ids) -> ctx.repositorySchueler().waehleAus(ids),
					HtmlContextSchueler::new)),

			Map.entry(ReportingReportvorlageDatenContext.SCHUELER_GOST_LAUFBAHNPLANUNG, listenAufbau(
					BEZEICHNUNGEN_SCHUELER, HtmlContextSchluessel.SCHUELER, ReportingSchueler.class,
					(ctx, ids) -> ctx.repositorySchueler().waehleAus(ids),
					HtmlContextSchueler::new,
					HtmlContextValidierung::pruefungenGostLaufbahnplanung)),

			Map.entry(ReportingReportvorlageDatenContext.SCHUELER_GOST_ABITUR, listenAufbau(
					BEZEICHNUNGEN_SCHUELER, HtmlContextSchluessel.SCHUELER, ReportingSchueler.class,
					(ctx, ids) -> ctx.repositorySchueler().waehleAus(ids),
					HtmlContextSchueler::new,
					HtmlContextValidierung::pruefungenGostAbitur)),

			Map.entry(ReportingReportvorlageDatenContext.KLASSEN, listenAufbau(
					BEZEICHNUNGEN_KLASSEN, HtmlContextSchluessel.KLASSEN, ReportingKlasse.class,
					(ctx, ids) -> ctx.repositoryLerngruppen().waehleKlassenAus(ids),
					HtmlContextKlassen::new)),

			Map.entry(ReportingReportvorlageDatenContext.KURSE, listenAufbau(
					bezeichnungen("Kurse", "Kursen", "Kurs-IDs"), HtmlContextSchluessel.KURSE, ReportingKurs.class,
					(ctx, ids) -> ctx.repositoryLerngruppen().waehleKurseAus(ids),
					HtmlContextKurse::new)),

			Map.entry(ReportingReportvorlageDatenContext.LEHRER, listenAufbau(
					BEZEICHNUNGEN_LEHRER, HtmlContextSchluessel.LEHRER, ReportingLehrer.class,
					(ctx, ids) -> ctx.repositoryLehrer().waehleAus(ids),
					HtmlContextLehrer::new)),

			// Fächer und Räume stammen aus dem bereits geladenen Stundenplan; ihre Auswahl löst gegen dessen Bestand auf, statt Daten nachzuladen.
			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_FACH, stundenplanAufbau(
					bezeichnungen("Fächer", "Fächern", "Fach-IDs"), HtmlContextSchluessel.STUNDENPLANUNG_FAECHER, ReportingFach.class,
					(ctx, stundenplan, ids) -> ReportingAuswahlergebnis.ausVorhandenen(ids,
							id -> (stundenplan.schuljahresabschnitt() == null) ? null : stundenplan.schuljahresabschnitt().fach(id)),
					HtmlContextStundenplanungFachStundenplan::new)),

			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_KLASSEN, stundenplanAufbau(
					BEZEICHNUNGEN_KLASSEN, HtmlContextSchluessel.STUNDENPLANUNG_KLASSEN, ReportingKlasse.class,
					(ctx, stundenplan, ids) -> ctx.repositoryLerngruppen().waehleKlassenAus(ids),
					HtmlContextStundenplanungKlassenStundenplan::new)),

			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER, stundenplanAufbau(
					BEZEICHNUNGEN_LEHRER, HtmlContextSchluessel.STUNDENPLANUNG_LEHRER, ReportingLehrer.class,
					(ctx, stundenplan, ids) -> ctx.repositoryLehrer().waehleAus(ids),
					HtmlContextStundenplanungLehrerStundenplan::new)),

			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_RAUM, stundenplanAufbau(
					bezeichnungen("Räume", "Räumen", "Raum-IDs"), HtmlContextSchluessel.STUNDENPLANUNG_RAEUME, ReportingStundenplanungRaum.class,
					(ctx, stundenplan, ids) -> ReportingAuswahlergebnis.ausVorhandenen(ids, stundenplan::raum),
					HtmlContextStundenplanungRaumStundenplan::new)),

			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_SCHUELER, stundenplanAufbau(
					BEZEICHNUNGEN_SCHUELER, HtmlContextSchluessel.STUNDENPLANUNG_SCHUELER, ReportingSchueler.class,
					(ctx, stundenplan, ids) -> ctx.repositorySchueler().waehleAus(ids),
					HtmlContextStundenplanungSchuelerStundenplan::new)),

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
	 * Zu jedem Datenaufbau die Entscheidung, ob seine Datenzugriffe vollständig über die Diagnose melden. Nur bei true trägt seine Ausgabe den
	 * Hinweis-Header. Die Zuordnung ist vollständig geführt und wird vom Vollständigkeitstest der Registry erzwungen: Ein neuer Datenaufbau muss die
	 * Entscheidung treffen, statt still ohne Header zu bleiben.
	 * <p>Angebunden sind die Datenaufbauten nach dem Listen-Muster und die fünf Sichtweisen der Stundenplanung; ihre Datenzugriffe sind je Aufbau gelesen und
	 * melden oder werfen. Die GOSt-Datenaufbauten stehen bewusst auf false: Sie lesen zusätzlich GOSt-Daten, deren Zugriffe noch nicht vollständig melden -
	 * ein Header dort würde eine geprüfte Vollständigkeit behaupten, die es nicht gibt.</p>
	 */
	private static final Map<ReportingReportvorlageDatenContext, Boolean> HINWEISVERTRAG_ANBINDUNG = Map.ofEntries(
			Map.entry(ReportingReportvorlageDatenContext.SCHUELER, true),
			Map.entry(ReportingReportvorlageDatenContext.SCHUELER_GOST_LAUFBAHNPLANUNG, false),
			Map.entry(ReportingReportvorlageDatenContext.SCHUELER_GOST_ABITUR, false),
			Map.entry(ReportingReportvorlageDatenContext.KLASSEN, true),
			Map.entry(ReportingReportvorlageDatenContext.KURSE, true),
			Map.entry(ReportingReportvorlageDatenContext.LEHRER, true),
			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_FACH, true),
			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_KLASSEN, true),
			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER, true),
			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_RAUM, true),
			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_SCHUELER, true),
			Map.entry(ReportingReportvorlageDatenContext.GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG, false),
			Map.entry(ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_KURSE, false),
			Map.entry(ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_SCHUELER, false),
			Map.entry(ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_SCHUELER, false),
			Map.entry(ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_TERMINE, false));


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

	/**
	 * Gibt an, ob der Datenaufbau seine Ausgabeprobleme vollständig über die Diagnose meldet.
	 * <p>Ein Datenaufbau ohne Eintrag gilt als noch nicht angebunden - ein vergessener Eintrag darf nicht wie eine geprüfte, vollständige Ausgabe aussehen.</p>
	 *
	 * @param datenContext Der Datenaufbau der Reportvorlage.
	 *
	 * @return true, wenn die Anbindung vollständig ist, sonst false.
	 */
	public static boolean istAnHinweisvertragAngebunden(final ReportingReportvorlageDatenContext datenContext) {
		return Boolean.TRUE.equals(HINWEISVERTRAG_ANBINDUNG.get(datenContext));
	}

	/**
	 * Gibt die geführte Entscheidung zum Hinweisvertrag zurück oder {@code null}, falls sie zum Datenaufbau fehlt. Für den Vollständigkeitstest der
	 * Registry, der genau dieses Fehlen finden soll.
	 *
	 * @param datenContext Der Datenaufbau der Reportvorlage.
	 *
	 * @return true oder false gemäß Zuordnung, sonst {@code null}.
	 */
	static Boolean anbindungHinweisvertragOderNull(final ReportingReportvorlageDatenContext datenContext) {
		return HINWEISVERTRAG_ANBINDUNG.get(datenContext);
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
	 * Erzeugt die Konfiguration eines Datenaufbaus nach dem Listen-Muster ohne fachliche Einschränkung.
	 *
	 * @param <T>               Der Reporting-Typ der ausgewählten Hauptdaten.
	 * @param bezeichnungen     Die Beschriftungen für Log-Ausgaben und Fehlermeldungen.
	 * @param contextSchluessel Der Schlüssel des Haupt-Contexts in der Context-Map.
	 * @param objektart         Die Objektart der Hauptdaten für den Schlüssel eines Ausgabeproblems.
	 * @param auswahl           Wählt die Hauptdaten zu den übergebenen IDs aus.
	 * @param contextErzeuger   Erzeugt den Haupt-Context aus den ausgewählten Objekten.
	 *
	 * @return Die Konfiguration des Datenaufbaus.
	 */
	private static <T> HtmlContextAufbauListe<T> listenAufbau(final HtmlContextDatenbezeichnungen bezeichnungen, final String contextSchluessel,
			final Class<T> objektart, final BiFunction<ReportingContext, List<Long>, ReportingAuswahlergebnis<T>> auswahl,
			final BiFunction<ReportingContext, List<T>, HtmlContext<T>> contextErzeuger) {
		return listenAufbau(bezeichnungen, contextSchluessel, objektart, auswahl, contextErzeuger, (ctx, ergebnis) -> ergebnis);
	}

	/**
	 * Erzeugt die Konfiguration eines Datenaufbaus nach dem Listen-Muster mit einer fachlichen Einschränkung der Auswahl.
	 *
	 * @param <T>               Der Reporting-Typ der ausgewählten Hauptdaten.
	 * @param bezeichnungen     Die Beschriftungen für Log-Ausgaben und Fehlermeldungen.
	 * @param contextSchluessel Der Schlüssel des Haupt-Contexts in der Context-Map.
	 * @param objektart         Die Objektart der Hauptdaten für den Schlüssel eines Ausgabeproblems.
	 * @param auswahl           Wählt die Hauptdaten zu den übergebenen IDs aus.
	 * @param contextErzeuger   Erzeugt den Haupt-Context aus den ausgewählten Objekten.
	 * @param einschraenkung    Die fachliche Einschränkung dieses Datenaufbaus.
	 *
	 * @return Die Konfiguration des Datenaufbaus.
	 */
	// SONARQUBE WARNUNG: Es sollen max. 7 Parameter übergeben werden. Hier sind es sechs benannte Bestandteile einer Konfiguration, die einzeln lesbar sind.
	private static <T> HtmlContextAufbauListe<T> listenAufbau(final HtmlContextDatenbezeichnungen bezeichnungen, final String contextSchluessel,
			final Class<T> objektart, final BiFunction<ReportingContext, List<Long>, ReportingAuswahlergebnis<T>> auswahl,
			final BiFunction<ReportingContext, List<T>, HtmlContext<T>> contextErzeuger, final HtmlContextEinschraenkung<T> einschraenkung) {
		return new HtmlContextAufbauListe<>(bezeichnungen, contextSchluessel, objektart, auswahl, contextErzeuger, einschraenkung);
	}

	/**
	 * Erzeugt die Konfiguration einer Sichtweise der Stundenplanung.
	 *
	 * @param <T>               Der Reporting-Typ der Context-Daten dieser Sichtweise.
	 * @param <H>               Der Reporting-Typ der ausgewählten Hauptdaten dieser Sichtweise.
	 * @param bezeichnungen     Die Beschriftungen für Log-Ausgaben und Fehlermeldungen.
	 * @param contextSchluessel Der Schlüssel des Haupt-Contexts in der Context-Map.
	 * @param objektart         Die Objektart der Hauptdaten für den Schlüssel eines Ausgabeproblems.
	 * @param auswahl           Wählt die Hauptdaten zu den übergebenen IDs aus, ohne bei fehlenden IDs abzubrechen.
	 * @param contextErzeuger   Erzeugt den Haupt-Context aus Stundenplan und den ausgewählten Hauptdaten-IDs.
	 *
	 * @return Die Konfiguration des Datenaufbaus.
	 */
	private static <T, H> HtmlContextAufbauStundenplan<T, H> stundenplanAufbau(final HtmlContextDatenbezeichnungen bezeichnungen,
			final String contextSchluessel, final Class<H> objektart, final HtmlContextStundenplanAuswahl<H> auswahl,
			final HtmlContextStundenplanErzeuger<T> contextErzeuger) {
		return new HtmlContextAufbauStundenplan<>(bezeichnungen, contextSchluessel, objektart, auswahl, contextErzeuger);
	}

}
