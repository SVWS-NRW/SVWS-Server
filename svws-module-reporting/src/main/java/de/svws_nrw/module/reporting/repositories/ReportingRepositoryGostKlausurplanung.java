package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenAlleKlausurdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenHalbjahresdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurtermin;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.gost.klausuren.GostKlausurplanManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ProxyReportingGostKlausurplanungKlausurraum;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ProxyReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ProxyReportingGostKlausurplanungKursklausur;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ProxyReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurraum;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKursklausur;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.service.gost.klausuren.GostKlausurenServiceFactoryBuilder;
import jakarta.ws.rs.core.Response;

/**
 * Domänen-Repository für die GOSt-Klausurplanung. Hält pro Reporting-Request genau einen
 * {@link GostKlausurplanManager} und baut daraus zentral die Reporting-Objekte für Klausurtermine,
 * Kursklausuren und Schülerklausuren auf.
 *
 * <p>Filterung und Sortierung folgen einem einheitlichen Schema:
 * <ul>
 *   <li><b>Filterung</b> über das FILTER-Companion des jeweiligen Reporting-Typs. <i>Default:</i> Liegt in den
 *       ReportParametern keine Filterdefinition zum Typ vor, wirkt der Filter als Allpass (keine Einschränkung).</li>
 *   <li><b>Sortierung</b> über das SORTIERUNG-Companion des jeweiligen Reporting-Typs. <i>Default:</i> Liegt in den
 *       ReportParametern keine Sortierdefinition zum Typ vor, gilt die Standardsortierung des Companions.</li>
 * </ul>
 *
 * <p>Die Methoden in diesem Repository wenden dieses Schema wie folgt an:
 * <ul>
 *   <li>Listen-Methoden ({@link #klausurtermine()}, {@link #kursklausuren()}, {@link #schuelerklausuren()}):
 *       Filter <b>und</b> Sortierung.</li>
 *   <li>Single-Lookup-Methoden ({@link #klausurtermin(long)}, {@link #kursklausur(long)},
 *       {@link #schuelerklausur(long)}): ausschließlich Filter (Sortierung nicht relevant).</li>
 *   <li>{@link #schueler()} und {@link #kurse()}: delegieren an die zentralen Schüler-/Lerngruppen-Repositories;
 *       deren Filter und Sortierung greifen. Es findet <b>keine</b> klausurplan-spezifische Zusatzfilterung statt.</li>
 * </ul>
 */
public class ReportingRepositoryGostKlausurplanung {

	private final ReportingContext reportingContext;

	/** Der GOSt-Klausurplan-Manager dieses Reports. Ein gesetztes Feld bedeutet: Manager und Reporting-Objekte stehen vollständig. */
	private GostKlausurplanManager manager = null;

	/** Der Fehler, an dem der Aufbau gescheitert ist. Jeder weitere Zugriff wirft ihn erneut, statt neu aufzubauen. */
	private ApiOperationException aufbaufehler = null;

	/** Kennzeichen, dass ein Aufbau begonnen hat. Es hält auch Abbrüche fest, die der Catch nicht erfasst. */
	private boolean aufbauBegonnen = false;

	/** Cache der bereits erzeugten Klausurtermine, indiziert nach Klausurtermin-ID. */
	private final Map<Long, ReportingGostKlausurplanungKlausurtermin> mapKlausurtermine = new LinkedHashMap<>();

	/** Cache der bereits erzeugten Kursklausuren, indiziert nach Kursklausur-ID. */
	private final Map<Long, ReportingGostKlausurplanungKursklausur> mapKursklausuren = new LinkedHashMap<>();

	/** Cache der bereits erzeugten Schülerklausuren, indiziert nach Schülerklausurtermin-ID. */
	private final Map<Long, ReportingGostKlausurplanungSchuelerklausur> mapSchuelerklausuren = new LinkedHashMap<>();


	/**
	 * Erstellt eine neue ReportingRepositoryGostKlausurplanung.
	 *
	 * @param reportingContext Der zentrale Reporting-Context.
	 */
	public ReportingRepositoryGostKlausurplanung(final ReportingContext reportingContext) {
		this.reportingContext = reportingContext;
	}


	/**
	 * Gibt den vollständig aufgebauten {@link GostKlausurplanManager} zurück und baut ihn beim ersten Zugriff auf. Jeder Daten-Getter ruft diese Methode.
	 * So gibt es keinen Zugriff auf ein nur teilweise aufgebautes Repository.
	 *
	 * @return Der Klausurplan-Manager dieses Reports.
	 *
	 * @throws ApiOperationException Falls der Aufbau scheitert oder bereits gescheitert ist.
	 */
	private GostKlausurplanManager aufgebauterManager() throws ApiOperationException {
		if (aufbaufehler != null) {
			throw aufbaufehler;
		}
		if (manager != null) {
			return manager;
		}
		// Der Aufbau wird höchstens einmal versucht. Ein zweiter Lauf verdoppelt die Schülerklausuren an den geteilten Schüler-Objekten.
		if (aufbauBegonnen) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR,
					"### FEHLER: Der Aufbau der Daten des gewählten Klausurplans ist abgebrochen.");
		}
		aufbauBegonnen = true;
		try {
			manager = baueManagerAuf();
		} catch (final Exception e) {
			// Die Core-Manager melden ihre Datenfehler als DeveloperNotificationException. Sie ist keine ApiOperationException.
			aufbaufehler = alsApiOperationException(e);
			throw aufbaufehler;
		}
		return manager;
	}

	/**
	 * Baut den {@link GostKlausurplanManager} und die Reporting-Objekte für alle Klausurtermine, Kursklausuren und Schülerklausuren auf. Die Stufen der
	 * Auswahl stehen in den Reportparametern oder werden aus dem ausgewählten Schuljahresabschnitt abgeleitet.
	 * <p>Der Manager wird erst nach dem Aufbau aller Reporting-Objekte zurückgegeben. Ein Abbruch dazwischen hinterlässt deshalb kein gefülltes Feld.</p>
	 *
	 * @return Der aufgebaute Klausurplan-Manager.
	 *
	 * @throws ApiOperationException Falls eine Stufe ungültig ist oder die Klausurplandaten nicht ermittelt werden konnten.
	 */
	private GostKlausurplanManager baueManagerAuf() throws ApiOperationException {
		final List<GostKlausurenHalbjahresdaten> selection = waehleStufenAus();
		final GostKlausurenAlleKlausurdaten allData = GostKlausurenServiceFactoryBuilder.getGostKlausurenServiceFactory()
				.getGostKlausurenAllDataService().getAllData(selection);
		final GostKlausurplanManager klausurplanManager = new GostKlausurplanManager(allData);
		erzeugeReportingObjekte(klausurplanManager);
		return klausurplanManager;
	}

	/**
	 * Wählt die Stufen des Klausurplans aus: die übergebenen kombinierten IDs oder ohne Übergabe die drei aus dem ausgewählten Schuljahresabschnitt
	 * abgeleiteten Stufen. Ein nicht vorhandener Abiturjahrgang und ein abgeleitetes Paar ohne GOSt-Halbjahr werden ausgelassen und gemeldet - für sie gibt
	 * es keine Ausgabe.
	 *
	 * @return Die ausgewählten Stufen; leer, wenn keine angeforderte oder abgeleitete Stufe vorhanden ist.
	 *
	 * @throws ApiOperationException Falls eine übergebene ID ungültig ist oder die vorhandenen Abiturjahrgänge nicht geladen werden konnten.
	 */
	private List<GostKlausurenHalbjahresdaten> waehleStufenAus() throws ApiOperationException {
		final List<long[]> kandidaten = new ArrayList<>();
		final List<Long> parameterDaten = reportingContext.reportingParameter().idsHauptdaten();

		if (parameterDaten.isEmpty()) {
			// Ohne übergebene Stufen gilt der Grundfall des Clients: alle drei Stufen gemäß dem ausgewählten Schuljahresabschnitt.
			final int schuljahr = reportingContext.repositorySchule().auswahlSchuljahresabschnitt().schuljahr();
			final int abschnitt = reportingContext.repositorySchule().auswahlSchuljahresabschnitt().abschnitt();
			kandidatOderMelde(kandidaten, schuljahr + 3L, abschnitt - 1L);
			kandidatOderMelde(kandidaten, schuljahr + 2L, abschnitt + 1L);
			kandidatOderMelde(kandidaten, schuljahr + 1L, abschnitt + 3L);
		} else {
			for (final Long kombinierteId : parameterDaten) {
				if (kombinierteId != null) {
					kandidaten.add(zerlegeStufenId(kombinierteId));
				}
			}
		}

		// Der Existenzabgleich folgt nach der Formprüfung; ein Ladefehler der vorhandenen Abiturjahrgänge wirft statustragend als Serverfehler.
		final List<Integer> vorhandeneAbiturjahrgaenge = reportingContext.repositoryGost().abiturjahrgaenge();
		final List<GostKlausurenHalbjahresdaten> selection = new ArrayList<>();
		for (final long[] kandidat : kandidaten) {
			final int abiturjahr = (int) kandidat[0];
			final int halbjahr = (int) kandidat[1];
			if (vorhandeneAbiturjahrgaenge.contains(abiturjahr)) {
				selection.add(new GostKlausurenHalbjahresdaten(abiturjahr, halbjahr));
			} else {
				meldeAusgelasseneStufe(abiturjahr, halbjahr,
						"Der Abiturjahrgang %d ist nicht vorhanden; die Stufe mit dem GOSt-Halbjahr %s wird in der Ausgabe ausgelassen."
								.formatted(abiturjahr, GostHalbjahr.fromID(halbjahr).kuerzel));
			}
		}

		return selection;
	}

	/**
	 * Zerlegt eine übergebene kombinierte ID (z. B. 20253 für Abitur 2025 in Q1.2) und prüft ihre Form. Das Repository prüft die selbst gelesene Angabe als
	 * letzte Instanz; ohne gültiges GOSt-Halbjahr liefen der Ladeweg und die Meldung der Auslassung in eine Zugriffsverletzung.
	 *
	 * @param kombinierteId Die kombinierte ID aus Abiturjahr und GOSt-Halbjahr.
	 *
	 * @return Das Paar aus Abiturjahr und Halbjahres-ID.
	 *
	 * @throws ApiOperationException Falls Abiturjahr oder Halbjahr außerhalb des Wertebereichs liegen.
	 */
	private static long[] zerlegeStufenId(final long kombinierteId) throws ApiOperationException {
		final long abiturjahr = kombinierteId / 10;
		if ((abiturjahr < 1900) || (abiturjahr > 9999) || (GostHalbjahr.fromID((int) (kombinierteId % 10)) == null)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "### FEHLER: Die Angabe zur gewählten Stufe ist ungültig.");
		}
		return new long[] { abiturjahr, kombinierteId % 10 };
	}

	/**
	 * Übernimmt ein aus dem Schuljahresabschnitt abgeleitetes Paar in die Kandidatenliste, sofern die Halbjahres-ID ein GOSt-Halbjahr bezeichnet. Ein
	 * Abschnitt jenseits der beiden Schulhalbjahre erzeugt IDs ohne GOSt-Halbjahr; diese Stufe wird ausgelassen und gemeldet, denn der Anwender hat hier
	 * nichts übergeben, das sich abweisen ließe.
	 *
	 * @param kandidaten Die Kandidatenliste der Auswahl.
	 * @param abiturjahr Das abgeleitete Abiturjahr.
	 * @param halbjahr   Die abgeleitete Halbjahres-ID.
	 */
	private void kandidatOderMelde(final List<long[]> kandidaten, final long abiturjahr, final long halbjahr) {
		if (GostHalbjahr.fromID((int) halbjahr) == null) {
			meldeAusgelasseneStufe((int) abiturjahr, (int) halbjahr,
					"Zum ausgewählten Schuljahresabschnitt gehört kein GOSt-Halbjahr; die abgeleitete Stufe des Abiturjahrgangs %d wird in der Ausgabe ausgelassen."
							.formatted(abiturjahr));
			return;
		}
		kandidaten.add(new long[] { abiturjahr, halbjahr });
	}

	/**
	 * Meldet eine ausgelassene Stufe als Ausgabeproblem. Der Schlüssel trägt die kombinierte ID, sodass dieselbe Stufe je Aufruf einmal zählt.
	 *
	 * @param abiturjahr   Das Abiturjahr der Stufe.
	 * @param halbjahr     Die Halbjahres-ID der Stufe.
	 * @param beschreibung Der Sachverhalt für das Log.
	 */
	private void meldeAusgelasseneStufe(final int abiturjahr, final int halbjahr, final String beschreibung) {
		reportingContext.meldeAusgabeproblem(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN,
				ReportingProblemSchluessel.fuer(GostKlausurenHalbjahresdaten.class, (abiturjahr * 10L) + halbjahr), beschreibung, null);
	}

	/**
	 * Führt einen Fehler des Aufbaus auf eine {@link ApiOperationException} zurück. Eine bereits passende Ausnahme bleibt unverändert. So behält sie den
	 * Status und die Ursachenkette der Datenschicht.
	 *
	 * @param fehler Der aufgetretene Fehler.
	 *
	 * @return Der Fehler als ApiOperationException.
	 */
	private static ApiOperationException alsApiOperationException(final Exception fehler) {
		if (fehler instanceof final ApiOperationException aoe) {
			return aoe;
		}
		return new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, fehler,
				"### FEHLER: Die Daten des gewählten Klausurplans konnten nicht aufgebaut werden.");
	}


	// ##### Schüler & Kurse (abgeleitet aus dem Manager; Filter/Sortierung der zentralen Repos) #####

	/**
	 * Liefert die Schüler des Klausurplans über das zentrale Schüler-Repository. Die Schüler-IDs werden aus den
	 * Schülerklausuren des Managers ermittelt; die zurückgegebene Liste ist gemäß FILTER und SORTIERUNG des
	 * Schüler-Repositories gefiltert und sortiert. Eine klausurplan-spezifische Zusatzfilterung erfolgt nicht.
	 * Default-Verhalten siehe Klassen-JavaDoc.
	 *
	 * @return Liste der Schüler.
	 *
	 * @throws ApiOperationException Falls der Aufbau des Klausurplans scheitert.
	 */
	public List<ReportingSchueler> schueler() throws ApiOperationException {
		final GostKlausurplanManager klausurplanManager = aufgebauterManager();
		final List<Long> ids = klausurplanManager.schuelerklausurGetMengeAsList().stream().map(sk -> sk.idSchueler).distinct().toList();
		return reportingContext.repositorySchueler().schueler(ids);
	}

	/**
	 * Gibt die Anzahl der Schüler zurück, die der Klausurplan-Manager zu den ausgewählten Stufen kennt - vor jeder Filterung. Aus ihr entsteht das Feld
	 * {@code angefordert} des Hinweis-Headers der Schüler-Sichtweise.
	 *
	 * @return Die Anzahl der Schüler im Plan.
	 *
	 * @throws ApiOperationException Falls der Aufbau des Klausurplans scheitert.
	 */
	public int anzahlSchuelerVorhanden() throws ApiOperationException {
		return (int) aufgebauterManager().schuelerklausurGetMengeAsList().stream().map(sk -> sk.idSchueler).distinct().count();
	}

	/**
	 * Liefert die Kurse des Klausurplans über das zentrale Lerngruppen-Repository. Die Kurs-IDs werden aus dem
	 * Kurs-Manager des Klausurplan-Managers ermittelt; die zurückgegebene Liste ist gemäß FILTER und SORTIERUNG des
	 * Lerngruppen-Repositories gefiltert und sortiert. Eine klausurplan-spezifische Zusatzfilterung erfolgt nicht.
	 * Default-Verhalten siehe Klassen-JavaDoc.
	 *
	 * @return Liste der Kurse.
	 *
	 * @throws ApiOperationException Falls der Aufbau des Klausurplans scheitert.
	 */
	public List<ReportingKurs> kurse() throws ApiOperationException {
		final List<Long> ids = aufgebauterManager().getKursManager().kurse().stream().map(k -> k.id).toList();
		return reportingContext.repositoryLerngruppen().kurse(ids);
	}

	/**
	 * Gibt die Anzahl der Kurse zurück, die der Kurs-Manager des Klausurplan-Managers kennt - vor jeder Filterung. Aus ihr entsteht das Feld
	 * {@code angefordert} des Hinweis-Headers der Kurs-Sichtweise.
	 *
	 * @return Die Anzahl der Kurse im Plan.
	 *
	 * @throws ApiOperationException Falls der Aufbau des Klausurplans scheitert.
	 */
	public int anzahlKurseVorhanden() throws ApiOperationException {
		return aufgebauterManager().getKursManager().kurse().size();
	}


	// ##### Klausurtermine #####

	/**
	 * Liefert die Liste aller Klausurtermine, gefiltert über {@link ReportingGostKlausurplanungKlausurtermin#FILTER}
	 * und sortiert über {@link ReportingGostKlausurplanungKlausurtermin#SORTIERUNG}. Default-Verhalten siehe Klassen-JavaDoc.
	 *
	 * @return Liste der Klausurtermine.
	 *
	 * @throws ApiOperationException Falls der Aufbau des Klausurplans scheitert.
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermine() throws ApiOperationException {
		aufgebauterManager();
		return getListeMitFilter(mapKlausurtermine, ReportingGostKlausurplanungKlausurtermin.class,
				ReportingGostKlausurplanungKlausurtermin.FILTER, ReportingGostKlausurplanungKlausurtermin.SORTIERUNG);
	}

	/**
	 * Gibt die Anzahl der Klausurtermine des Klausurplans zurück - vor der Filterung über das FILTER-Companion. Aus ihr entsteht das Feld
	 * {@code angefordert} des Hinweis-Headers der Termin-Sichtweise.
	 *
	 * @return Die Anzahl der Klausurtermine im Plan.
	 *
	 * @throws ApiOperationException Falls der Aufbau des Klausurplans scheitert.
	 */
	public int anzahlKlausurtermineVorhanden() throws ApiOperationException {
		aufgebauterManager();
		return mapKlausurtermine.size();
	}

	/**
	 * Liefert den Klausurtermin zur übergebenen ID oder {@code null}, falls keiner vorhanden ist oder das Objekt
	 * durch {@link ReportingGostKlausurplanungKlausurtermin#FILTER} ausgeschlossen wird. Sortierung ist hier nicht
	 * relevant. Default-Verhalten siehe Klassen-JavaDoc.
	 *
	 * @param id Die ID des Klausurtermins.
	 *
	 * @return Der Klausurtermin oder {@code null}.
	 *
	 * @throws ApiOperationException Falls der Aufbau des Klausurplans scheitert.
	 */
	public ReportingGostKlausurplanungKlausurtermin klausurtermin(final long id) throws ApiOperationException {
		aufgebauterManager();
		return getObjektMitFilter(mapKlausurtermine, id, ReportingGostKlausurplanungKlausurtermin.class, ReportingGostKlausurplanungKlausurtermin.FILTER);
	}


	// ##### Kursklausuren #####

	/**
	 * Liefert die Liste aller Kursklausuren, gefiltert über {@link ReportingGostKlausurplanungKursklausur#FILTER}
	 * und sortiert über {@link ReportingGostKlausurplanungKursklausur#SORTIERUNG}. Default-Verhalten siehe Klassen-JavaDoc.
	 *
	 * @return Liste der Kursklausuren.
	 *
	 * @throws ApiOperationException Falls der Aufbau des Klausurplans scheitert.
	 */
	public List<ReportingGostKlausurplanungKursklausur> kursklausuren() throws ApiOperationException {
		aufgebauterManager();
		return getListeMitFilter(mapKursklausuren, ReportingGostKlausurplanungKursklausur.class,
				ReportingGostKlausurplanungKursklausur.FILTER, ReportingGostKlausurplanungKursklausur.SORTIERUNG);
	}

	/**
	 * Liefert die Kursklausur zur übergebenen ID oder {@code null}, falls keine vorhanden ist oder das Objekt
	 * durch {@link ReportingGostKlausurplanungKursklausur#FILTER} ausgeschlossen wird. Sortierung ist hier nicht relevant.
	 * Default-Verhalten siehe Klassen-JavaDoc.
	 *
	 * @param id Die ID der Kursklausur.
	 *
	 * @return Die Kursklausur oder {@code null}.
	 *
	 * @throws ApiOperationException Falls der Aufbau des Klausurplans scheitert.
	 */
	public ReportingGostKlausurplanungKursklausur kursklausur(final long id) throws ApiOperationException {
		aufgebauterManager();
		return getObjektMitFilter(mapKursklausuren, id, ReportingGostKlausurplanungKursklausur.class, ReportingGostKlausurplanungKursklausur.FILTER);
	}


	// ##### Schülerklausuren #####

	/**
	 * Liefert die Liste aller Schülerklausuren, gefiltert über {@link ReportingGostKlausurplanungSchuelerklausur#FILTER}
	 * und sortiert über {@link ReportingGostKlausurplanungSchuelerklausur#SORTIERUNG}. Default-Verhalten siehe Klassen-JavaDoc.
	 *
	 * @return Liste der Schülerklausuren.
	 *
	 * @throws ApiOperationException Falls der Aufbau des Klausurplans scheitert.
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren() throws ApiOperationException {
		aufgebauterManager();
		return getListeMitFilter(mapSchuelerklausuren, ReportingGostKlausurplanungSchuelerklausur.class,
				ReportingGostKlausurplanungSchuelerklausur.FILTER, ReportingGostKlausurplanungSchuelerklausur.SORTIERUNG);
	}

	/**
	 * Liefert die Schülerklausur zur übergebenen Schülerklausurtermin-ID oder {@code null}, falls keine vorhanden ist
	 * oder das Objekt durch {@link ReportingGostKlausurplanungSchuelerklausur#FILTER} ausgeschlossen wird. Sortierung
	 * ist hier nicht relevant. Default-Verhalten siehe Klassen-JavaDoc.
	 *
	 * @param idSchuelerklausurtermin Die ID des Schülerklausurtermins (eindeutiger Schlüssel je Proxy-Objekt).
	 *
	 * @return Die Schülerklausur oder {@code null}.
	 *
	 * @throws ApiOperationException Falls der Aufbau des Klausurplans scheitert.
	 */
	public ReportingGostKlausurplanungSchuelerklausur schuelerklausur(final long idSchuelerklausurtermin) throws ApiOperationException {
		aufgebauterManager();
		return getObjektMitFilter(mapSchuelerklausuren, idSchuelerklausurtermin, ReportingGostKlausurplanungSchuelerklausur.class,
				ReportingGostKlausurplanungSchuelerklausur.FILTER);
	}


	// ##### Filter-/Sortier-Helfer #####

	/**
	 * Liefert die Werte des übergebenen Caches als gefilterte (und ggf. sortierte) Liste.
	 * <ul>
	 *   <li>Filter: Bedingung des FILTER-Companions mit den im {@link de.svws_nrw.module.reporting.filterung.ReportingFilterService}
	 *       konfigurierten Attributen. <i>Default:</i> ohne ReportParameter-Filterdefinition wirkt der Filter als Allpass.</li>
	 *   <li>Sortierung: Comparator über {@link ComparatorFactory#buildComparator}, falls {@code sortierung != null}.
	 *       <i>Default:</i> ohne ReportParameter-Sortierdefinition wirkt die Standardsortierung des Companions.
	 *       Bei {@code sortierung == null} bleibt die Reihenfolge des Caches erhalten.</li>
	 * </ul>
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
	 * Liefert das Reporting-Objekt zur übergebenen ID aus dem Cache, sofern es die FILTER-Bedingung erfüllt.
	 * Liefert {@code null}, wenn der Cache zur ID keinen Eintrag hat oder das Objekt durch den Filter ausgeschlossen wird.
	 * <i>Default:</i> ohne ReportParameter-Filterdefinition zum Typ wirkt der Filter als Allpass und es wird stets
	 * der Cache-Eintrag (oder {@code null}, falls nicht vorhanden) zurückgegeben.
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
	 * Baut die Reporting-Objekte aller Klausurtermine, Kursklausuren, Klausurräume und Schülerklausuren aus dem
	 * Manager auf und legt sie in den Cache-Maps ab. Bereits beim Aufbau wirken die FILTER der zentralen
	 * Schüler- und Lerngruppen-Repositories: Kursklausuren, deren Kurs nicht vom Lerngruppen-Repository geliefert
	 * wird, und Schülerklausuren, deren Schüler nicht vom Schüler-Repository geliefert wird, werden übersprungen.
	 * Die klausurplan-spezifischen FILTER-Companions (für Termin/Kursklausur/Schülerklausur) wirken <b>nicht</b>
	 * hier beim Aufbau, sondern erst beim Auslesen in den öffentlichen Getter-Methoden.
	 *
	 * Im Anschluss werden die Cache-Maps über {@link #sortiereMaps()} gemäß ihrer SORTIERUNG-Konfiguration neu
	 * geordnet (deterministische Iterationsreihenfolge). Abschließend verteilt {@link #verteileSchuelerklausuren()}
	 * die Schülerklausuren auf die zugehörigen Schüler und Kursklausuren. Hierbei wird der FILTER der Schülerklausur vorab angewandt.
	 *
	 * @param klausurplanManager Der im Aufbau befindliche Klausurplan-Manager.
	 */
	private void erzeugeReportingObjekte(final GostKlausurplanManager klausurplanManager) {
		erzeugeKlausurtermine(klausurplanManager);
		erzeugeKursklausuren(klausurplanManager);
		verknuepfeKlausurraeumeMitTerminen(klausurplanManager);
		erzeugeSchuelerklausuren(klausurplanManager);
		sortiereMaps();
		verteileSchuelerklausuren();
	}

	private void erzeugeKlausurtermine(final GostKlausurplanManager klausurplanManager) {
		for (final GostKlausurtermin t : klausurplanManager.terminGetMengeAsList()) {
			mapKlausurtermine.put(t.id, new ProxyReportingGostKlausurplanungKlausurtermin(t));
		}
	}

	private void erzeugeKursklausuren(final GostKlausurplanManager klausurplanManager) {
		for (final var k : klausurplanManager.kursklausurGetMengeAsList()) {
			final ReportingKurs kurs = reportingContext.repositoryLerngruppen().kurs(klausurplanManager.kursdatenByKursklausur(k).id);
			if (kurs == null) {
				// Bewusst still: Das zentrale Lerngruppen-Repository liefert kein Objekt, wenn der Benutzerfilter den Kurs ausschließt
				// (Auswahlentscheidung) oder sein Laden scheiterte - den Ladefehler meldet es dabei selbst über die Fassade.
				continue;
			}
			final GostKlausurtermin terminOrNull = klausurplanManager.terminOrNullByKursklausur(k);
			final ReportingGostKlausurplanungKlausurtermin termin = (terminOrNull == null) ? null : mapKlausurtermine.get(terminOrNull.id);
			mapKursklausuren.put(k.id, new ProxyReportingGostKlausurplanungKursklausur(k, klausurplanManager.vorgabeByKursklausur(k), termin, kurs));
		}
	}

	private void verknuepfeKlausurraeumeMitTerminen(final GostKlausurplanManager klausurplanManager) {
		for (final ReportingGostKlausurplanungKlausurtermin termin : mapKlausurtermine.values()) {
			final GostKlausurtermin gostTermin = klausurplanManager.terminGetByIdOrNull(termin.id());
			if (gostTermin == null) {
				// Bewusst still: Die Termin-Map ist aus derselben Manager-Menge aufgebaut; ein hier nicht auflösbarer Termin ist auf dem produktiven
				// Weg nicht erreichbar.
				continue;
			}
			for (final GostKlausurraum terminraum : klausurplanManager.raumGetMengeByTermin(gostTermin)) {
				termin.klausurraeume().add(new ProxyReportingGostKlausurplanungKlausurraum(reportingContext, termin, terminraum,
						klausurplanManager.raumstundeGetMengeByRaum(terminraum)));
			}
		}
	}

	private void erzeugeSchuelerklausuren(final GostKlausurplanManager klausurplanManager) {
		for (final GostSchuelerklausur sk : klausurplanManager.schuelerklausurGetMengeAsList()) {
			for (final GostSchuelerklausurtermin skTermin : klausurplanManager.schuelerklausurterminGetMengeBySchuelerklausur(sk)) {
				erzeugeSchuelerklausurtermin(klausurplanManager, sk, skTermin);
			}
		}
	}

	/**
	 * Wendet den FILTER der Schülerklausur vorab an und verteilt die übrig bleibenden Schülerklausuren auf die zugehörigen
	 * Schüler-Reporting-Objekte, Kursklausuren sowie Klausurtermine in der aktuell konfigurierten Sortierreihenfolge.
	 */
	private void verteileSchuelerklausuren() {
		final Predicate<ReportingGostKlausurplanungSchuelerklausur> filterSchuelerklausuren = ReportingGostKlausurplanungSchuelerklausur.FILTER.bedingung(
				reportingContext.filterService().getFilter(ReportingGostKlausurplanungSchuelerklausur.class.getSimpleName()), null);

		// Zuerst bestehende unsortierte Listen in Kursklausuren und Klausurterminen leeren
		for (final ReportingGostKlausurplanungKursklausur kk : mapKursklausuren.values()) {
			kk.schuelerklausuren().clear();
		}
		for (final ReportingGostKlausurplanungKlausurtermin kt : mapKlausurtermine.values()) {
			if (kt.schuelerklausuren() != null) {
				kt.schuelerklausuren().clear();
			}
		}

		// Da mapSchuelerklausuren in sortiereMaps() bereits über den Sortier-Service sortiert wurde,
		// liefert mapSchuelerklausuren.values() die Elemente bereits in der gewünschten Sortierreihenfolge.
		for (final ReportingGostKlausurplanungSchuelerklausur s : mapSchuelerklausuren.values()) {
			if (filterSchuelerklausuren.test(s)) {
				s.schueler().gostKlausurplanungSchuelerklausuren().add(s);
				if (s.kursklausur() != null) {
					s.kursklausur().schuelerklausuren().add(s);
				}
				if (s.klausurtermin() != null) {
					s.klausurtermin().schuelerklausuren().add(s);
				}
			}
		}
	}


	private void erzeugeSchuelerklausurtermin(final GostKlausurplanManager klausurplanManager, final GostSchuelerklausur sk,
			final GostSchuelerklausurtermin skTermin) {
		final ReportingSchueler schueler = reportingContext.repositorySchueler().schueler(sk.idSchueler);
		if (schueler == null) {
			// Bewusst still: Das zentrale Schüler-Repository liefert kein Objekt, wenn der Benutzerfilter den Schüler ausschließt oder sein Laden
			// scheiterte - den Ladefehler meldet es dabei selbst über die Fassade.
			return;
		}
		final ReportingGostKlausurplanungKursklausur kursklausur = mapKursklausuren.get(klausurplanManager.kursklausurBySchuelerklausur(sk).id);
		if (kursklausur == null) {
			// Bewusst still: Die Kursklausur fehlt genau dann, wenn ihr Kurs oben ausgeschlossen wurde - dieselbe Auswahlentscheidung, kein neuer Befund.
			return;
		}

		// Termin mit FolgeNr 0 und TerminID null ist der Termin der Kursklausur, sonst der separat angesetzte Termin.
		final ReportingGostKlausurplanungKlausurtermin klausurtermin;
		if ((skTermin.folgeNr == 0) && (skTermin.idTermin == null)) {
			klausurtermin = kursklausur.klausurtermin();
		} else {
			klausurtermin = (skTermin.idTermin != null) ? mapKlausurtermine.get(skTermin.idTermin) : null;
		}
		if (klausurtermin == null) {
			// Bewusst still: Ein noch nicht angesetzter Termin - etwa ein offener Nachschreibtermin - ist ein regulärer fachlicher Zustand der Planung.
			return;
		}

		ReportingGostKlausurplanungKlausurraum klausurraum = null;
		final GostKlausurraum gostKlausurraum = klausurplanManager.raumGetBySchuelerklausurtermin(skTermin);
		if (gostKlausurraum != null) {
			final List<GostKlausurraumstunde> raumstunden = klausurplanManager.raumstundeGetMengeByRaum(gostKlausurraum);
			if (!raumstunden.isEmpty()) {
				klausurraum = new ProxyReportingGostKlausurplanungKlausurraum(reportingContext, klausurtermin, gostKlausurraum, raumstunden);
			}
		}

		mapSchuelerklausuren.put(skTermin.id,
				new ProxyReportingGostKlausurplanungSchuelerklausur(sk, skTermin, klausurraum, klausurtermin, kursklausur, schueler));
	}

	/**
	 * Ordnet die Cache-Maps für Klausurtermine, Kursklausuren und Schülerklausuren gemäß SORTIERUNG-Companion neu an,
	 * damit nachgelagerte Iterationen über die LinkedHashMaps (z. B. {@link #verteileSchuelerklausuren()})
	 * eine deterministische Reihenfolge erhalten. Default-Verhalten siehe Klassen-JavaDoc.
	 */
	private void sortiereMaps() {
		sortiereMap(mapKlausurtermine, ReportingGostKlausurplanungKlausurtermin.class,
				ReportingGostKlausurplanungKlausurtermin.SORTIERUNG, ReportingGostKlausurplanungKlausurtermin::id);
		sortiereMap(mapKursklausuren, ReportingGostKlausurplanungKursklausur.class,
				ReportingGostKlausurplanungKursklausur.SORTIERUNG, ReportingGostKlausurplanungKursklausur::id);
		sortiereMap(mapSchuelerklausuren, ReportingGostKlausurplanungSchuelerklausur.class,
				ReportingGostKlausurplanungSchuelerklausur.SORTIERUNG, ReportingGostKlausurplanungSchuelerklausur::idSchuelerklausurtermin);
	}

	/**
	 * Sortiert die Werte der übergebenen Cache-Map gemäß SORTIERUNG-Companion via {@link ComparatorFactory#buildComparator}
	 * und legt sie unter ihrem Schlüssel neu ein. <i>Default:</i> ohne ReportParameter-Sortierdefinition zum Typ wirkt
	 * die Standardsortierung des Companions.
	 *
	 * @param <T>        Der Typ der Reporting-Objekte.
	 * @param cache      Die zu sortierende Cache-Map (wird in-place neu befüllt).
	 * @param typ        Der Typ der Reporting-Objekte; sein Simple-Name dient als Schlüssel für die Sortierungs-Konfiguration.
	 * @param sortierung Das SORTIERUNG-Companion des Reporting-Typs.
	 * @param schluessel Funktion zur Ableitung des Map-Schlüssels aus einem Reporting-Objekt.
	 */
	private <T> void sortiereMap(final Map<Long, T> cache, final Class<T> typ, final ReportingSortierung<T> sortierung,
			final ToLongFunction<T> schluessel) {
		final Comparator<T> comparator = ComparatorFactory.buildComparator(
				this.reportingContext.sortierungService(), this.reportingContext.logger(),
				typ.getSimpleName(), sortierung, true);
		final List<T> sortiert = new ArrayList<>(cache.values());
		sortiert.sort(comparator);
		cache.clear();
		for (final T t : sortiert) {
			cache.put(schluessel.applyAsLong(t), t);
		}
	}
}
