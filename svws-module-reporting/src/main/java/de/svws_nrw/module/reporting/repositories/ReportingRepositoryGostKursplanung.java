package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ProxyReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ProxyReportingGostKursplanungKurs;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ProxyReportingGostKursplanungSchiene;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungSchiene;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.gost.kursplanung.ProxyReportingSchuelerGostKursplanungKursbelegung;
import jakarta.ws.rs.core.Response;

/**
 * Domänen-Repository für die GOSt-Kursplanung. Hält pro Reporting-Request genau einen
 * {@link GostBlockungsergebnisManager} und baut daraus zentral die Reporting-Objekte für das
 * Blockungsergebnis, die Schienen und die Kurse auf. Auf den Listen-/Single-Lookup-Methoden werden
 * die konfigurierten FILTER-Companions angewendet, sodass alle Konsumenten dieselben gefilterten
 * Daten erhalten.
 */
public class ReportingRepositoryGostKursplanung {

	private final ReportingContext reportingContext;

	/** Der GOSt-Blockungsergebnis-Manager dieses Reports (lazy, einmalig initialisiert). */
	private GostBlockungsergebnisManager manager = null;

	/** Cache des Blockungsergebnisses. */
	private final Map<Long, ReportingGostKursplanungBlockungsergebnis> mapBlockungsergebnis = new LinkedHashMap<>();

	/** Cache der bereits erzeugten Schienen, indiziert nach Schienen-ID. */
	private final Map<Long, ReportingGostKursplanungSchiene> mapSchienen = new LinkedHashMap<>();

	/** Cache der bereits erzeugten Kurse des Blockungsergebnisses, indiziert nach Kurs-ID. */
	private final Map<Long, ReportingGostKursplanungKurs> mapKurse = new LinkedHashMap<>();


	/**
	 * Erstellt ein neues ReportingRepositoryGostKursplanung.
	 *
	 * @param reportingContext Der zentrale Reporting-Context.
	 */
	public ReportingRepositoryGostKursplanung(final ReportingContext reportingContext) {
		this.reportingContext = reportingContext;
	}


	/**
	 * Initialisiert den {@link GostBlockungsergebnisManager} anhand der ID des Blockungsergebnisses und baut die
	 * Reporting-Objekte für das Blockungsergebnis, die Schienen und die Kurse auf. Das Blockungsergebnis und der
	 * zugehörige Daten-Manager werden aus der Datenbank geladen. Kurs-Schüler-Zuordnungen von Schülern, die nicht
	 * mehr Teil der Blockung sind (z. B. Abgänger), werden vor dem Aufbau entfernt. Wird einmalig pro
	 * Reporting-Request vom HtmlContext aufgerufen. Folgeaufrufe sind No-Ops (Single-Threaded pro Request).
	 *
	 * @param idBlockungsergebnis Die ID des Blockungsergebnisses.
	 *
	 * @throws ApiOperationException Falls Blockungsergebnis oder Blockungsdaten-Manager nicht ermittelt werden konnten.
	 */
	public void initManager(final long idBlockungsergebnis) throws ApiOperationException {
		if (manager != null) {
			return;
		}

		this.reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Die ID der Blockungsergebnisses wurde ermittelt: " + idBlockungsergebnis);
		final GostBlockungsergebnis blockungsergebnis = ladeBlockungsergebnis(idBlockungsergebnis);
		this.reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Das Blockungsergebnis wurde ermittelt.");
		final GostBlockungsdatenManager datenManager = blockungsdatenManager(blockungsergebnis.blockungID);
		this.reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Der Datenmanager zum Blockungsergebnis wurde ermittelt.");

		manager = new GostBlockungsergebnisManager(datenManager, blockungsergebnis);
		entferneKurszuordnungenUnbekannterSchueler(blockungsergebnis, datenManager);
		erzeugeReportingObjekte(blockungsergebnis, datenManager);
	}

	/**
	 * Entfernt über die Manager-API alle Kurs-Schüler-Zuordnungen von Schülern, die im Blockungsdaten-Manager
	 * nicht (mehr) existieren (z. B. Abgänger). Die Datenbank kann für solche Schüler noch Kurs-Schüler-Zuordnungen
	 * des Zwischenergebnisses enthalten; der Einzel-Ergebnis-Ladepfad übernimmt diese ungefiltert. Ohne Bereinigung
	 * würden Manager-Methoden, die Schüler-IDs auflösen (z. B. {@link GostBlockungsergebnisManager#getOfKursSchuelermenge}),
	 * beim Aufbau der Reporting-Objekte eine Exception werfen. Entspricht inhaltlich dem Filter in
	 * {@link DataGostBlockungsergebnisse#getErgebnisListe}.
	 * <p>Jeder betroffene Schüler wird vor dem Entfernen als Ausgabeproblem gemeldet: Es entfallen gespeicherte Daten des Zwischenergebnisses, und nur im
	 * Log wäre dieser Entfall für den Anwender unsichtbar - die Ausgabe meldete sich sonst als vollständig.</p>
	 *
	 * @param blockungsergebnis Das geladene Blockungsergebnis.
	 * @param datenManager      Der zugehörige Blockungsdaten-Manager.
	 */
	private void entferneKurszuordnungenUnbekannterSchueler(final GostBlockungsergebnis blockungsergebnis,
			final GostBlockungsdatenManager datenManager) {
		final Set<Long> unbekannteSchuelerIDs = blockungsergebnis.schienen.stream()
				.flatMap(s -> s.kurse.stream())
				.flatMap(k -> k.schueler.stream())
				.filter(idSchueler -> datenManager.schuelerGetOrNull(idSchueler) == null)
				.collect(Collectors.toSet());
		if (unbekannteSchuelerIDs.isEmpty()) {
			return;
		}
		for (final Long idSchueler : unbekannteSchuelerIDs) {
			meldeEntfernteKurszuordnungen(idSchueler);
		}
		manager.kursSchuelerUpdateExecute(manager.kursSchuelerUpdateEntferneSchuelermengeAusAllenKursen(unbekannteSchuelerIDs));
	}

	/**
	 * Meldet die Kurszuordnungen eines Schülers, der nicht mehr Teil der Blockung ist, als Ausgabeproblem. Die Zuordnungen stehen im gespeicherten
	 * Zwischenergebnis, lassen sich aber keinem Schüler der Blockung mehr zuordnen und fehlen deshalb in der Ausgabe.
	 *
	 * @param idSchueler Die ID des Schülers, dessen Kurszuordnungen entfallen.
	 */
	void meldeEntfernteKurszuordnungen(final long idSchueler) {
		this.reportingContext.meldeAusgabeproblem(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.TEILDATEN_FEHLEN,
				ReportingProblemSchluessel.fuer(ProxyReportingSchuelerGostKursplanungKursbelegung.class, idSchueler),
				"Die gespeicherten Kurszuordnungen des Schülers %d gehören zu keinem Schüler der Blockung mehr (z. B. Abgänger) und fehlen in der Ausgabe."
						.formatted(idSchueler), null);
	}

	/**
	 * Gibt den {@link GostBlockungsergebnisManager} zurück. Liefert {@code null}, wenn der Manager noch nicht
	 * initialisiert wurde.
	 *
	 * @return Der Blockungsergebnis-Manager oder {@code null}.
	 */
	public GostBlockungsergebnisManager manager() {
		return manager;
	}


	// ##### Blockungsergebnis #####

	/**
	 * Lädt das GOSt-Blockungsergebnis zur übergebenen ID aus der Datenbank. Schlägt das Laden fehl, wird der Status der Datenschicht durchgereicht: Ein
	 * pauschales {@code NOT_FOUND} machte aus einem Serverfehler die Auskunft, das Ergebnis gebe es nicht.
	 *
	 * @param idBlockungsergebnis Die ID des Blockungsergebnisses.
	 *
	 * @return Das geladene Blockungsergebnis.
	 *
	 * @throws ApiOperationException Mit dem Status der Datenschicht, falls das Ergebnis nicht ermittelt werden konnte.
	 */
	private GostBlockungsergebnis ladeBlockungsergebnis(final long idBlockungsergebnis) throws ApiOperationException {
		try {
			return DataGostBlockungsergebnisse.getErgebnisFromID(this.reportingContext.conn(), idBlockungsergebnis);
		} catch (final ApiOperationException aoe) {
			throw new ApiOperationException(aoe.getStatus(), aoe, "### FEHLER: Das gewählte Blockungsergebnis konnte nicht ermittelt werden.");
		}
	}

	/**
	 * Lädt den Blockungsdaten-Manager zur angegebenen Blockungs-ID aus der Datenbank. Schlägt das Laden fehl, wird der Status der Datenschicht durchgereicht;
	 * die Begründung ist dieselbe wie bei {@link #ladeBlockungsergebnis}.
	 *
	 * @param idBlockung Die ID der Blockung.
	 *
	 * @return Der Blockungsdaten-Manager zur Blockung.
	 *
	 * @throws ApiOperationException Mit dem Status der Datenschicht, falls die Daten nicht ermittelt werden konnten.
	 */
	private GostBlockungsdatenManager blockungsdatenManager(final long idBlockung) throws ApiOperationException {
		try {
			return DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(this.reportingContext.conn(), idBlockung);
		} catch (final ApiOperationException aoe) {
			// Die Blockung ist aus dem gewählten Ergebnis abgeleitet: Das Eingangsprotokoll kennt sie nicht, und nicht jeder Fehlerpfad der Datenschicht
			// nennt sie.
			this.reportingContext.logger().logLn(LogLevel.ERROR, 4, "Blockung " + idBlockung);
			throw new ApiOperationException(aoe.getStatus(), aoe,
					"### FEHLER: Die Blockungsdaten zum gewählten Blockungsergebnis konnten nicht ermittelt werden.");
		}
	}

	/**
	 * Liefert das aufgebaute Blockungsergebnis dieses Reports. Liefert {@code null}, wenn der Manager noch nicht
	 * initialisiert wurde.
	 *
	 * @return Das Blockungsergebnis oder {@code null}.
	 */
	public ReportingGostKursplanungBlockungsergebnis blockungsergebnis() {
		return mapBlockungsergebnis.isEmpty() ? null : mapBlockungsergebnis.values().iterator().next();
	}

	/**
	 * Liefert das gefilterte Blockungsergebnis zur übergebenen ID oder {@code null}, falls keines vorhanden ist
	 * oder das zugehörige Blockungsergebnis durch den Filter ausgeschlossen ist.
	 *
	 * @param id Die ID des Blockungsergebnisses.
	 *
	 * @return Das Blockungsergebnis oder {@code null}.
	 */
	public ReportingGostKursplanungBlockungsergebnis blockungsergebnis(final long id) {
		return getObjektMitFilter(mapBlockungsergebnis, id, ReportingGostKursplanungBlockungsergebnis.class, ReportingGostKursplanungBlockungsergebnis.FILTER);
	}


	// ##### Schienen #####

	/**
	 * Liefert die gefilterte Liste aller Schienen des Blockungsergebnisses.
	 *
	 * @return Liste der Schienen.
	 */
	public List<ReportingGostKursplanungSchiene> schienen() {
		return getListeMitFilter(mapSchienen, ReportingGostKursplanungSchiene.class, ReportingGostKursplanungSchiene.FILTER, null);
	}

	/**
	 * Liefert die gefilterte Schiene zur übergebenen ID oder {@code null}.
	 *
	 * @param id Die ID der Schiene.
	 *
	 * @return Die Schiene oder {@code null}.
	 */
	public ReportingGostKursplanungSchiene schiene(final long id) {
		return getObjektMitFilter(mapSchienen, id, ReportingGostKursplanungSchiene.class, ReportingGostKursplanungSchiene.FILTER);
	}


	// ##### Kurse #####

	/**
	 * Liefert die gefilterte Liste aller Kurse des Blockungsergebnisses.
	 *
	 * @return Liste der Kurse.
	 */
	public List<ReportingGostKursplanungKurs> kurse() {
		return getListeMitFilter(mapKurse, ReportingGostKursplanungKurs.class,
				ReportingGostKursplanungKurs.FILTER, null);
	}

	/**
	 * Liefert den gefilterten Kurs zur übergebenen ID oder {@code null}.
	 *
	 * @param id Die ID des Kurses.
	 *
	 * @return Der Kurs oder {@code null}.
	 */
	public ReportingGostKursplanungKurs kurs(final long id) {
		return getObjektMitFilter(mapKurse, id, ReportingGostKursplanungKurs.class, ReportingGostKursplanungKurs.FILTER);
	}


	// ##### Filter-/Sortier-Helfer #####

	/**
	 * Liefert die Werte des übergebenen Caches als gefilterte (und ggf. sortierte) Liste. Die Filterbedingung
	 * wird über das FILTER-Companion aus den im {@link de.svws_nrw.module.reporting.filterung.ReportingFilterService}
	 * konfigurierten Attributen abgeleitet. Wird eine Sortierung übergeben, so wird zusätzlich der zugehörige
	 * Comparator über {@link ComparatorFactory#buildComparator} aufgebaut und auf das Ergebnis angewendet.
	 *
	 * @param <T>        Der Typ der zu liefernden Reporting-Objekte.
	 * @param cache      Die Cache-Map, deren Werte gefiltert und ggf. sortiert zurückgegeben werden.
	 * @param typ        Der Typ der Reporting-Objekte; sein Simple-Name dient als Schlüssel für Filter- und Sortierungs-Konfiguration.
	 * @param filterung  Das FILTER-Companion des Reporting-Typs.
	 * @param sortierung Das SORTIERUNG-Companion des Reporting-Typs oder {@code null}, falls keine Sortierung erfolgen soll.
	 *
	 * @return Die gefilterte (und ggf. sortierte) Liste der Reporting-Objekte.
	 */
	private <T> List<T> getListeMitFilter(final Map<Long, T> cache, final Class<T> typ, final ReportingFilterung<T> filterung,
			final ReportingSortierung<T> sortierung) {
		final Predicate<T> filter = filterung.bedingung(
				reportingContext.filterService().getFilter(typ.getSimpleName()), null);
		if (sortierung == null) {
			return cache.values().stream().filter(filter).toList();
		}
		final Comparator<T> comparator = ComparatorFactory.buildComparator(
				reportingContext.sortierungService(), reportingContext.logger(), typ.getSimpleName(), sortierung, true);
		return cache.values().stream().filter(filter).sorted(comparator).toList();
	}

	/**
	 * Liefert das Reporting-Objekt zur übergebenen ID aus dem Cache, sofern es die zentrale Filterbedingung erfüllt.
	 * Liefert {@code null}, wenn der Cache zur ID keinen Eintrag hat oder das Objekt durch den Filter ausgeschlossen wird.
	 *
	 * @param <T>       Der Typ der zu liefernden Reporting-Objekte.
	 * @param cache     Die Cache-Map, aus der das Objekt zur übergebenen ID gelesen wird.
	 * @param id        Die ID des gesuchten Reporting-Objekts.
	 * @param typ       Der Typ der Reporting-Objekte; sein Simple-Name dient als Schlüssel für die Filter-Konfiguration.
	 * @param filterung Das FILTER-Companion des Reporting-Typs.
	 *
	 * @return Das gefilterte Reporting-Objekt oder {@code null}.
	 */
	private <T> T getObjektMitFilter(final Map<Long, T> cache, final long id, final Class<T> typ, final ReportingFilterung<T> filterung) {
		final T t = cache.get(id);
		if (t == null) {
			return null;
		}
		final Predicate<T> filter = filterung.bedingung(
				reportingContext.filterService().getFilter(typ.getSimpleName()), null);
		return filter.test(t) ? t : null;
	}


	// ##### Build #####

	/**
	 * Ermittelt das {@link GostHalbjahr} zu der in den Blockungsdaten hinterlegten Halbjahres-ID.
	 *
	 * <p>Aus dem Halbjahr werden Schuljahr und Abschnitt der Blockung abgeleitet. Eine ungültige ID darf deshalb nicht
	 * stillschweigend übergangen werden: Sie führte zu einem falsch berechneten Schuljahresabschnitt und damit zu den
	 * Fachdaten eines falschen Schuljahres. Der Wert stammt aus den gespeicherten Blockungsdaten des Servers; ein
	 * ungültiger Wert ist damit eine Inkonsistenz der Serverdaten und ein Serverfehler - als "nicht gefunden" suchte
	 * der Anwender die Ursache bei seiner Anfrage. Den Fehlerblock schreibt die Abschlussgrenze; Blockung und Wert hält
	 * {@link #gostHalbjahrDerBlockung(GostBlockungsdaten)} in der erlaubten technischen Zeile fest, denn die Meldung nennt beides nicht. Diese Prüfung
	 * selbst kommt ohne Context aus.</p>
	 *
	 * @param idGostHalbjahr Die ID des GOSt-Halbjahres aus den Blockungsdaten.
	 *
	 * @return Das GOSt-Halbjahr der Blockung.
	 *
	 * @throws ApiOperationException Mit Status 500, falls die ID kein gültiges GOSt-Halbjahr bezeichnet.
	 */
	static GostHalbjahr ermittleGostHalbjahr(final int idGostHalbjahr) throws ApiOperationException {
		final GostHalbjahr gostHalbjahr = GostHalbjahr.fromID(idGostHalbjahr);
		if (gostHalbjahr == null) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "### FEHLER: Die Blockungsdaten nennen kein gültiges GOSt-Halbjahr.");
		}
		return gostHalbjahr;
	}

	/**
	 * Ermittelt das GOSt-Halbjahr der Blockung und hält bei einem ungültigen Wert Blockung und Wert im Log fest. Die Meldung nennt beides nicht, weil der
	 * Anwender keines von beiden kennt; in keiner Ursachenkette und in keinem Eingangsprotokoll steht es - ohne diese Zeile wäre der inkonsistente Datensatz
	 * nicht auffindbar.
	 *
	 * @param blockungsdaten Die Blockungsdaten mit der Halbjahres-ID.
	 *
	 * @return Das GOSt-Halbjahr der Blockung.
	 *
	 * @throws ApiOperationException Mit Status 500, falls die Blockungsdaten kein gültiges GOSt-Halbjahr nennen.
	 */
	GostHalbjahr gostHalbjahrDerBlockung(final GostBlockungsdaten blockungsdaten) throws ApiOperationException {
		try {
			return ermittleGostHalbjahr(blockungsdaten.gostHalbjahr);
		} catch (final ApiOperationException aoe) {
			this.reportingContext.logger().logLn(LogLevel.ERROR, 4,
					"Blockung %d, GOSt-Halbjahr-Wert %d".formatted(blockungsdaten.id, blockungsdaten.gostHalbjahr));
			throw aoe;
		}
	}

	/**
	 * Baut die Reporting-Objekte des Blockungsergebnisses, aller Schienen und Kurse aus dem Manager auf und legt
	 * sie in den Cache-Maps ab. Schüler werden über das zentrale Schüler-Repository bezogen und sind damit bereits
	 * gefiltert; Kursbelegungen für zentral herausgefilterte Schüler werden übersprungen.
	 *
	 * @param blockungsergebnis Das Blockungsergebnis.
	 * @param datenManager      Der zugehörige Blockungsdaten-Manager.
	 *
	 * @throws ApiOperationException Falls die Blockungsdaten kein gültiges GOSt-Halbjahr enthalten.
	 */
	private void erzeugeReportingObjekte(final GostBlockungsergebnis blockungsergebnis, final GostBlockungsdatenManager datenManager)
			throws ApiOperationException {
		final var blockungsdaten = datenManager.daten();
		final GostHalbjahr gostHalbjahr = gostHalbjahrDerBlockung(blockungsdaten);
		final int schuljahr = gostHalbjahr.getSchuljahrFromAbiturjahr(blockungsdaten.abijahrgang);
		final int abschnitt = gostHalbjahr.halbjahr;
		// Blockungen sind auch für Halbjahre möglich, für die die Schule noch keinen Schuljahresabschnitt angelegt hat.
		// Der Abschnitt dient hier allein als Zugriff auf die Fächer des Schuljahres, daher genügt ein virtueller Abschnitt.
		final var schuljahresabschnitt = reportingContext.repositorySchule().schuljahresabschnittOderVirtuell(schuljahr, abschnitt);

		final List<ReportingSchueler> schueler = reportingContext.repositorySchueler()
				.schueler(datenManager.schuelerGetListe().stream().map(s -> s.id).toList());
		final Map<Long, ReportingSchueler> mapSchueler = new HashMap<>();
		for (final ReportingSchueler s : schueler) {
			mapSchueler.put(s.id(), s);
		}

		// Schienen ohne Kurse werden nicht berücksichtigt.
		final List<GostBlockungSchiene> aktiveSchienen = datenManager.schieneGetListe().stream()
				.filter(s -> !manager.getOfSchieneKursmengeSortiert(s.id).isEmpty()).toList();

		// Blockungsergebnis-Proxy mit leeren Listen vorab erzeugen — Schienen- und Kurs-Proxys halten eine Rückreferenz.
		// Die vollständig aufgebauten Listen werden dem Ergebnis am Ende dieser Methode per setSchienen/setKurse übergeben.
		final List<ReportingGostKursplanungKurs> kurseListe = new ArrayList<>();
		final List<ReportingGostKursplanungSchiene> schienenListe = new ArrayList<>();
		final ProxyReportingGostKursplanungBlockungsergebnis proxy = new ProxyReportingGostKursplanungBlockungsergebnis(
				reportingContext,
				blockungsdaten.abijahrgang,
				manager.getAnzahlSchuelerDummy(),
				manager.getAnzahlSchuelerExterne(),
				manager.getOfSchieneMaxKursanzahl(),
				aktiveSchienen.size(),
				datenManager.schuelerGetAnzahl(),
				blockungsdaten.name,
				gostHalbjahr,
				blockungsergebnis.id,
				kurseListe, schienenListe, schueler);
		mapBlockungsergebnis.put(blockungsergebnis.id, proxy);

		for (final GostBlockungSchiene s : aktiveSchienen) {
			final ReportingGostKursplanungSchiene schiene = new ProxyReportingGostKursplanungSchiene(
					proxy,
					manager.getOfSchieneAnzahlSchuelerDummy(s.id),
					manager.getOfSchieneAnzahlSchuelerExterne(s.id),
					manager.getOfSchieneAnzahlSchueler(s.id),
					s.bezeichnung,
					manager.getOfSchieneHatKollision(s.id),
					s.id,
					manager.getOfSchieneKursmengeMitKollisionen(s.id).stream().map(k -> k.id).toList(),
					List.copyOf(manager.getOfSchieneSchuelermengeMitKollisionen(s.id)),
					new ArrayList<>(),
					s.nummer);
			mapSchienen.put(s.id, schiene);
		}
		schienenListe.addAll(mapSchienen.values());

		for (final GostBlockungKurs kurs : datenManager.kursGetListeSortiertNachKursartFachNummer()) {
			final List<ReportingLehrer> kursLehrer = datenManager.kursGetLehrkraefteSortiert(kurs.id).stream()
					.map(l -> reportingContext.repositoryLehrer().lehrer(l.id))
					.filter(java.util.Objects::nonNull)
					.toList();

			final ReportingGostKursplanungKurs reportingKurs = new ProxyReportingGostKursplanungKurs(
					proxy,
					manager.getOfKursAnzahlSchuelerAbiturLK(kurs.id),
					manager.getOfKursAnzahlSchuelerAbitur3(kurs.id),
					manager.getOfKursAnzahlSchuelerAbitur4(kurs.id),
					manager.getOfKursAnzahlSchuelerDummy(kurs.id),
					manager.getOfKursAnzahlSchuelerExterne(kurs.id),
					manager.getOfKursAnzahlSchueler(kurs.id),
					manager.getOfKursAnzahlSchuelerSchriftlich(kurs.id),
					datenManager.kursGetName(kurs.id),
					schuljahresabschnitt.fach(kurs.fach_id),
					null,
					gostHalbjahr,
					GostKursart.fromID(manager.getKursE(kurs.id).kursart),
					kurs.id,
					kursLehrer,
					manager.getOfKursSchienenmenge(kurs.id).stream().map(sch -> mapSchienen.get(sch.id)).toList(),
					new ArrayList<>());

			mapKurse.put(kurs.id, reportingKurs);
			kurseListe.add(reportingKurs);

			for (final long idKursschueler : manager.getOfKursSchuelermenge(kurs.id).stream().map(s -> s.id).toList()) {
				ergaenzeKursbelegung(idKursschueler, kurs.id, reportingKurs, mapSchueler);
			}

			reportingKurs.schienen().forEach(s -> mapSchienen.get(s.id()).kurse().add(reportingKurs));
		}

		// kurseListe / schienenListe sind jetzt vollständig aufgebaut → in das Ergebnis als Defensivkopie übernehmen.
		proxy.setSchienen(schienenListe);
		proxy.setKurse(kurseListe);
	}

	private void ergaenzeKursbelegung(final long idKursschueler, final long kursId,
			final ReportingGostKursplanungKurs reportingKurs, final Map<Long, ReportingSchueler> mapSchueler) {
		final ReportingSchueler schueler = mapSchueler.get(idKursschueler);
		if (schueler == null) {
			// Bewusst still: Hier fehlen nur Schüler, die der Benutzerfilter des zentralen Schüler-Repositorys ausgeschlossen hat - eine
			// Auswahlentscheidung des Anwenders und kein Ausgabeproblem.
			return;
		}
		final FachwahlDaten fachwahl = fachwahlDatenOderMelde(manager, idKursschueler, kursId);

		schueler.gostKursplanungKursbelegungen().add(new ProxyReportingSchuelerGostKursplanungKursbelegung(
				fachwahl.abiturfach(), fachwahl.gueltig(), fachwahl.schriftlich(), reportingKurs));
	}

	/**
	 * Die Angaben der Fachwahl zu einer Kursbelegung: Abiturfach, Schriftlichkeit und ob die Belegung gültig ist.
	 *
	 * @param abiturfach  Die Nummer des Abiturfachs als Text oder ein leerer String.
	 * @param schriftlich Gibt an, ob das Fach schriftlich belegt ist.
	 * @param gueltig     Gibt an, ob die Kursbelegung gültig ist.
	 */
	record FachwahlDaten(String abiturfach, boolean schriftlich, boolean gueltig) {
	}

	/**
	 * Löst die Fachwahl eines Schülers zu seiner Kursbelegung über den Ergebnis-Manager auf. Fehlt die Fachwahl zu einer gültigen Belegung, ist das eine
	 * Datenlücke der Blockung: Die Belegung erscheint als ungültig, und der Befund wird über die Fassade gemeldet. Jeder andere Fehler propagiert - er wäre
	 * sonst still als fehlende Fachwahl gedeutet.
	 *
	 * @param ergebnisManager Der Manager des Blockungsergebnisses.
	 * @param idKursschueler  Die ID des Schülers im Kurs.
	 * @param idKurs          Die ID des Kurses.
	 *
	 * @return Die Angaben der Fachwahl zur Kursbelegung.
	 */
	FachwahlDaten fachwahlDatenOderMelde(final GostBlockungsergebnisManager ergebnisManager, final long idKursschueler, final long idKurs) {
		if (ergebnisManager.getOfSchuelerOfKursIstUngueltig(idKursschueler, idKurs)) {
			return new FachwahlDaten("", false, false);
		}
		try {
			final GostFachwahl gostFachwahl = ergebnisManager.getOfSchuelerOfKursFachwahl(idKursschueler, idKurs);
			return new FachwahlDaten((gostFachwahl.abiturfach != null) ? String.valueOf(gostFachwahl.abiturfach) : "", gostFachwahl.istSchriftlich, true);
		} catch (final DeveloperNotificationException e) {
			this.reportingContext.meldeAusgabeproblem(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.TEILDATEN_FEHLEN,
					ReportingProblemSchluessel.fuer(ProxyReportingSchuelerGostKursplanungKursbelegung.class, idKursschueler),
					"Die Fachwahl des Schülers %d zum Kurs %d fehlt; die Kursbelegung erscheint in der Ausgabe als ungültig."
							.formatted(idKursschueler, idKurs), e);
			return new FachwahlDaten("", false, false);
		}
	}
}
