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
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.db.utils.ApiOperationException;
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
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
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
		manager.kursSchuelerUpdateExecute(manager.kursSchuelerUpdateEntferneSchuelermengeAusAllenKursen(unbekannteSchuelerIDs));
		this.reportingContext.logger().logLn(LogLevel.INFO, 4,
				"INFO: Es wurden Kurszuordnungen von %d Schülern entfernt, die nicht mehr zur Blockung gehören (z. B. Abgänger)."
						.formatted(unbekannteSchuelerIDs.size()));
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
			throw new ApiOperationException(aoe.getStatus(), aoe,
					"FEHLER: Zur angegebenen Blockungsergebnis-ID %d konnte kein Blockungsergebnis ermittelt werden."
							.formatted(idBlockungsergebnis));
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
			throw new ApiOperationException(aoe.getStatus(), aoe,
					"FEHLER: Zur Blockungs-ID %d konnte kein Blockungsdaten-Manager ermittelt werden.".formatted(idBlockung));
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
	 * Fachdaten eines falschen Schuljahres.</p>
	 *
	 * @param idGostHalbjahr Die ID des GOSt-Halbjahres aus den Blockungsdaten.
	 * @param idBlockung     Die ID der Blockung; wird für die Fehlermeldung benötigt.
	 *
	 * @return Das GOSt-Halbjahr der Blockung.
	 *
	 * @throws ApiOperationException Falls die ID kein gültiges GOSt-Halbjahr bezeichnet.
	 */
	private GostHalbjahr ermittleGostHalbjahr(final int idGostHalbjahr, final long idBlockung) throws ApiOperationException {
		final GostHalbjahr gostHalbjahr = GostHalbjahr.fromID(idGostHalbjahr);
		if (gostHalbjahr == null) {
			final String fehlermeldung = "FEHLER: Zur Blockung mit der ID %d konnte aus dem Wert %d kein gültiges GOSt-Halbjahr ermittelt werden."
					.formatted(idBlockung, idGostHalbjahr);
			this.reportingContext.logger().logLn(LogLevel.ERROR, 4, fehlermeldung);
			throw new ApiOperationException(Response.Status.NOT_FOUND, fehlermeldung);
		}
		return gostHalbjahr;
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
		final GostHalbjahr gostHalbjahr = ermittleGostHalbjahr(blockungsdaten.gostHalbjahr, blockungsdaten.id);
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
			return;
		}
		String fachwahlAbiturfach = "";
		boolean fachwahlSchriftlich = false;
		boolean fachwahlGueltig = !manager.getOfSchuelerOfKursIstUngueltig(idKursschueler, kursId);

		if (fachwahlGueltig) {
			try {
				final GostFachwahl gostFachwahl = manager.getOfSchuelerOfKursFachwahl(idKursschueler, kursId);
				fachwahlAbiturfach = (gostFachwahl.abiturfach != null) ? String.valueOf(gostFachwahl.abiturfach) : "";
				fachwahlSchriftlich = gostFachwahl.istSchriftlich;
			} catch (final Exception e) {
				fachwahlGueltig = false;
				ReportingExceptionUtils.logException(
						"INFO: Fehler mit definiertem Rückgabewert abgefangen aufgrund fehlender Fachwahl eines Schülers bei dessen Kursplanungskursbelegung.",
						e, reportingContext.logger(), LogLevel.INFO, 0);
			}
		}

		schueler.gostKursplanungKursbelegungen().add(new ProxyReportingSchuelerGostKursplanungKursbelegung(
				fachwahlAbiturfach, fachwahlGueltig, fachwahlSchriftlich, reportingKurs));
	}
}
