package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierungService;
import de.svws_nrw.module.reporting.types.lerngruppen.ProxyReportingKurs;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurplan und erweitert die Klasse
 *  {@link ReportingGostKlausurplanungKlausurplan}.
 */
public class ProxyReportingGostKlausurplanungKlausurplan extends ReportingGostKlausurplanungKlausurplan {

	/** Repository für das Reporting. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/** Klausurmanager des GOSt-Klausurplans. */
	@JsonIgnore
	private final GostKlausurplanManager gostKlausurplanManager;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKlausurplanungKlausurplan}. Die Filter-Prädikate für
	 * Schüler, Kurse und Klausurtermine werden über den FilterService aus den Reporting-Parametern abgeleitet.
	 *
	 * @param reportingContext	Repository für das Reporting.
	 * @param klausurtermine		Eine Liste, die alle Termine des Klausurplanes beinhaltet.
	 * @param kurse 				Eine Liste, die alle Kurse des Klausurplanes beinhaltet.
	 * @param kursklausuren 		Eine Liste, die alle Kursklausuren des Klausurplanes beinhaltet.
	 * @param schueler 				Eine Liste, die alle Schüler des Klausurplanes beinhaltet.
	 * @param schuelerklausuren 	Eine Liste, die alle Schülerklausuren des Klausurplanes beinhaltet.
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ProxyReportingGostKlausurplanungKlausurplan(final ReportingContext reportingContext,
			final List<ReportingGostKlausurplanungKlausurtermin> klausurtermine, final List<ReportingKurs> kurse,
			final List<ReportingGostKlausurplanungKursklausur> kursklausuren, final List<ReportingSchueler> schueler,
			final List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren) {
		super(klausurtermine, kurse, kursklausuren, schueler, schuelerklausuren,
				setFilterSchueler(reportingContext), setFilterKurse(reportingContext), setFilterKlausurtermin(reportingContext));
		this.reportingContext = reportingContext;
		this.gostKlausurplanManager = null;
	}


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKlausurplanungKlausurplan} mit explizit übergebenen
	 * Schüler-, Kurs- und Klausurtermin-Prädikaten. Wird verwendet, um Sub-Kontexte (z. B. für Einzelausgaben) auf einen
	 * einzelnen Schüler, Kurs oder Klausurtermin einzuschränken.
	 *
	 * @param reportingContext	Repository für das Reporting.
	 * @param klausurtermine		Eine Liste, die alle Termine des Klausurplanes beinhaltet.
	 * @param kurse 				Eine Liste, die alle Kurse des Klausurplanes beinhaltet.
	 * @param kursklausuren 		Eine Liste, die alle Kursklausuren des Klausurplanes beinhaltet.
	 * @param schueler 				Eine Liste, die alle Schüler des Klausurplanes beinhaltet.
	 * @param schuelerklausuren 	Eine Liste, die alle Schülerklausuren des Klausurplanes beinhaltet.
	 * @param filterSchueler		Ein Prädikat, das bestimmt, welche Schüler in der Ausgabe enthalten sind.
	 * @param filterKurse			Ein Prädikat, das bestimmt, welche Kurse in der Ausgabe enthalten sind.
	 * @param filterKlausurtermine	Ein Prädikat, das bestimmt, welche Klausurtermine in der Ausgabe enthalten sind.
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ProxyReportingGostKlausurplanungKlausurplan(final ReportingContext reportingContext,
			final List<ReportingGostKlausurplanungKlausurtermin> klausurtermine, final List<ReportingKurs> kurse,
			final List<ReportingGostKlausurplanungKursklausur> kursklausuren, final List<ReportingSchueler> schueler,
			final List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren,
			final Predicate<ReportingSchueler> filterSchueler, final Predicate<ReportingKurs> filterKurse,
			final Predicate<ReportingGostKlausurplanungKlausurtermin> filterKlausurtermine) {
		super(klausurtermine, kurse, kursklausuren, schueler, schuelerklausuren,
				filterSchueler, filterKurse, filterKlausurtermine);
		this.reportingContext = reportingContext;
		this.gostKlausurplanManager = null;
	}


	/**
	 * Erstellt ein neues Reporting-Objekt anhand des GostKlausurplanManagers. Die Filter-Prädikate werden über den
	 * FilterService aus den Reporting-Parametern abgeleitet.
	 *
	 * @param reportingContext		Repository für das Reporting.
	 * @param gostKlausurplanManager 	Der Manager der Klausuren zu diesem Klausurplan
	 */
	public ProxyReportingGostKlausurplanungKlausurplan(final ReportingContext reportingContext, final GostKlausurplanManager gostKlausurplanManager) {
		super(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
				setFilterSchueler(reportingContext), setFilterKurse(reportingContext), setFilterKlausurtermin(reportingContext));

		this.reportingContext = reportingContext;
		this.gostKlausurplanManager = gostKlausurplanManager;

		if ((this.reportingContext == null) || (this.gostKlausurplanManager == null)) {
			return;
		}

		// 1. Schülerstammdaten der Schüler aus den Schülerklausuren ermitteln und in Listen und Maps einfügen.
		initSchueler();

		// 2. Kurs-Objekte anhand der Kursklausuren erzeugen.
		super.kurse.addAll(this.gostKlausurplanManager.getKursManager().kurse().stream()
				.map(k -> new ProxyReportingKurs(this.reportingContext, k))
				.toList());

		// 3. Klausurtermine erstellen.
		// HINWEIS: Termine werden ohne Klausuren erzeugt. Wenn Klausuren erzeugt werden, werden diese dem Termin zugewiesen.
		super.klausurtermine.addAll(this.gostKlausurplanManager.terminGetMengeAsList().stream()
				.map(t -> (ReportingGostKlausurplanungKlausurtermin) new ProxyReportingGostKlausurplanungKlausurtermin(t))
				.toList());

		// 4. Kursklausuren erstellen.
		// HINWEIS: Kursklausuren, deren Kurs durch den Filter ausgeschlossen ist, werden übersprungen.
		// HINWEIS: Kursklausuren und Klausurtermine erhalten ihre Schülerklausuren erst bei der Erzeugung der Schülerklausuren.
		// HINWEIS: Die Klausurräume werden in einem folgenden Schritt zentral zugewiesen.
		for (final var k : this.gostKlausurplanManager.kursklausurGetMengeAsList()) {
			final ReportingKurs kurs = kurs(this.gostKlausurplanManager.kursdatenByKursklausur(k).id);
			if (kurs == null) {
				continue;
			}
			final GostKlausurtermin terminOrNull = this.gostKlausurplanManager.terminOrNullByKursklausur(k);
			final ReportingGostKlausurplanungKlausurtermin termin = (terminOrNull == null) ? null : klausurtermin(terminOrNull.id);
			super.kursklausuren.add(new ProxyReportingGostKlausurplanungKursklausur(
					k, this.gostKlausurplanManager.vorgabeByKursklausur(k), termin, kurs));
		}

		// 5. Klausurräume mit Aufsichten (sofern schon zugeteilt) erstellen.
		initKlausurraeume();

		// 6. Schülerklausuren erstellen.
		initSchuelerklausuren();

		// 7. Sortiere alle Schülerklausuren, sowohl in der Gesamtliste als auch bei den Kursklausuren.

		// Prüfe, ob Service und Logger abrufbar sind. Andernfalls würden Standardsortierungen verwendet werden.
		final ReportingSortierungService sortierungService = this.reportingContext.sortierungService();
		final Logger logger = this.reportingContext.logger();

		final Comparator<ReportingGostKlausurplanungSchuelerklausur> comparator =
				ComparatorFactory.buildComparator(sortierungService, logger, ReportingGostKlausurplanungSchuelerklausur.class.getSimpleName(),
						ReportingGostKlausurplanungSchuelerklausur.SORTIERUNG, true);

		super.schuelerklausuren.sort(comparator);
		super.kursklausuren.forEach(kk -> kk.schuelerklausuren().sort(comparator));

		// 8. Ergänze die Schülerklausuren in der Liste der Klausuren des Schülers.
		super.schuelerklausuren.forEach(sk -> sk.schueler().gostKlausurplanungSchuelerklausuren().add(sk));
	}

	private static Predicate<ReportingSchueler> setFilterSchueler(final ReportingContext reportingContext) {
		return (reportingContext == null)
				? s -> true
				: ReportingSchueler.FILTER.bedingung(
						reportingContext.filterService().getFilter(ReportingSchueler.class.getSimpleName()), null);
	}

	private static Predicate<ReportingKurs> setFilterKurse(final ReportingContext reportingContext) {
		return (reportingContext == null)
				? k -> true
				: ReportingKurs.FILTER.bedingung(
						reportingContext.filterService().getFilter(ReportingKurs.class.getSimpleName()), null);
	}

	private static Predicate<ReportingGostKlausurplanungKlausurtermin> setFilterKlausurtermin(final ReportingContext reportingContext) {
		return (reportingContext == null)
				? p -> true
				: ReportingGostKlausurplanungKlausurtermin.FILTER.bedingung(
						reportingContext.filterService().getFilter(ReportingGostKlausurplanungKlausurtermin.class.getSimpleName()), null);
	}

	/**
	 * Initialisiert die Schüler für die später zu erstellenden Schülerklausuren.
	 */
	private void initSchueler() {
		if (this.gostKlausurplanManager == null) {
			return;
		}
		super.schueler.addAll(this.reportingContext.repositorySchueler()
				.schueler(this.gostKlausurplanManager.schuelerklausurGetMengeAsList().stream().map(s -> s.idSchueler).distinct().toList()));
	}

	/**
	 * Initialisiert die Raumdaten und Unterrichtsstunden der Klausurräume. Das Ergebnis wird in den übergebenen Listen gespeichert.
	 */
	private void initKlausurraeume() {
		if (this.gostKlausurplanManager == null) {
			return;
		}

		// Durchlaufe alle Klausurtermine und weise ihnen die ReportingKlausurräume zu, die aus den Daten erzeugt werden.
		for (final ReportingGostKlausurplanungKlausurtermin termin : super.klausurtermine) {
			// Einem Termin können mehrere Räume zugewiesen worden sein. Ermittle sie gemäß TerminID.
			final GostKlausurtermin gostKlausurtermin = this.gostKlausurplanManager.terminGetByIdOrNull(termin.id);
			if (gostKlausurtermin != null) {
				// Durchlaufe alle Räume, ermittle dabei die Klausurstunden und erzeuge damit die Klausurräume.
				for (final GostKlausurraum terminraum : this.gostKlausurplanManager.raumGetMengeByTermin(gostKlausurtermin)) {
					termin.klausurraeume().add(
							new ProxyReportingGostKlausurplanungKlausurraum(this.reportingContext, termin, terminraum,
									this.gostKlausurplanManager.raumstundeGetMengeByRaum(terminraum)));
				}
			}
		}
	}

	/**
	 * Initialisiert die Schülerklausuren mit allen Informationen (auch individuelle Raumdaten, Zeit oder Klausurdaten).
	 */
	private void initSchuelerklausuren() {
		if (this.gostKlausurplanManager == null) {
			return;
		}

		// Listen und Maps mit Daten aus den vorherigen Schritten, um nicht erneut auf die DB zugreifen zu müssen.
		final Map<Long, ReportingGostKlausurplanungKlausurtermin> mapKlausurtermine =
				super.klausurtermine.stream().collect(Collectors.toMap(ReportingGostKlausurplanungKlausurtermin::id, t -> t));
		final Map<Long, ReportingGostKlausurplanungKursklausur> mapKursklausuren =
				super.kursklausuren.stream().collect(Collectors.toMap(ReportingGostKlausurplanungKursklausur::id, k -> k));

		// Durchlaufe nun alle Schülerklausuren und erzeuge dafür deren Termine mit Klausurräumen usw.
		for (final GostSchuelerklausur sk : gostKlausurplanManager.schuelerklausurGetMengeAsList()) {

			// Zu einer Schülerklausur kann es mehrere Schülerklausurtermine geben, die sich in ihrer FolgeNr unterscheiden (z. B. bei Nachschrieb).
			for (final GostSchuelerklausurTermin skTermin : gostKlausurplanManager.schuelerklausurterminGetMengeBySchuelerklausur(sk)) {
				erstelleSchuelerklausurTermin(sk, skTermin, mapKlausurtermine, mapKursklausuren);
			}
		}
	}

	/**
	 * Erstellt einen Schülerklausurtermin und fügt diesen der Gesamtliste der Schülerklausuren hinzu.
	 *
	 * @param sk                Die Gost-Schülerklausur, für die der Termin erstellt werden soll.
	 * @param skTermin          Der zugehörige Termin der Schülerklausur.
	 * @param mapKlausurtermine Eine Zuordnung von Klausurtermin-IDs zu den entsprechenden Reporting-Objekten der Klausurtermine.
	 * @param mapKursklausuren  Eine Zuordnung von Kursklausur-IDs zu den entsprechenden Reporting-Objekten der Kursklausuren.
	 */
	private void erstelleSchuelerklausurTermin(final GostSchuelerklausur sk, final GostSchuelerklausurTermin skTermin,
			final Map<Long, ReportingGostKlausurplanungKlausurtermin> mapKlausurtermine,
			final Map<Long, ReportingGostKlausurplanungKursklausur> mapKursklausuren) {

		// 1. Wenn der Schüler oder die zugehörige Kursklausur durch den Filter ausgeschlossen ist, breche ab.
		final ReportingSchueler reportingSchueler = schueler(sk.idSchueler);
		if (reportingSchueler == null) {
			return;
		}
		final ReportingGostKlausurplanungKursklausur kursklausur =
				mapKursklausuren.get(gostKlausurplanManager.kursklausurBySchuelerklausur(sk).id);
		if (kursklausur == null) {
			return;
		}

		// 2. Den Klausurtermin für den Schülerklausurtermin ermitteln.
		final ReportingGostKlausurplanungKlausurtermin klausurtermin;

		// Der Termin mit FolgeNr 0 und TerminID null ist der Termin der Kursklausur.
		if ((skTermin.folgeNr == 0) && (skTermin.idTermin == null)) {
			klausurtermin = kursklausur.klausurtermin();
		} else {
			klausurtermin = (skTermin.idTermin != null) ? mapKlausurtermine.get(skTermin.idTermin) : null;
		}

		// Wenn der Termin (z. B. durch Filterung) nicht existiert, breche ab.
		if (klausurtermin == null) {
			return;
		}

		// 3. Den Klausurraum mit den Stunden zum Schülerklausurtermin ermitteln.
		ReportingGostKlausurplanungKlausurraum klausurraum = null;

		final GostKlausurraum gostKlausurraum = gostKlausurplanManager.raumGetBySchuelerklausurtermin(skTermin);
		if (gostKlausurraum != null) {
			final List<GostKlausurraumstunde> gostKlausurraumstundenSchueler = gostKlausurplanManager.raumstundeGetMengeByRaum(gostKlausurraum);
			if (!gostKlausurraumstundenSchueler.isEmpty()) {
				klausurraum = new ProxyReportingGostKlausurplanungKlausurraum(
						this.reportingContext, klausurtermin, gostKlausurraum, gostKlausurraumstundenSchueler);
			}
		}

		// 4. Schülerklausur erzeugen und der Gesamtliste der Schülerklausuren hinzufügen.
		super.schuelerklausuren.add(new ProxyReportingGostKlausurplanungSchuelerklausur(sk, skTermin, klausurraum, klausurtermin,
				kursklausur, reportingSchueler));
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	@JsonIgnore
	public ReportingContext reportingContext() {
		return reportingContext;
	}

}
