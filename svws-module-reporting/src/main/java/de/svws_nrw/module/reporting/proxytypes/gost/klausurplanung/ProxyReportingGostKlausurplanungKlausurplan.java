package de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.module.reporting.proxytypes.kurs.ProxyReportingKurs;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurraum;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKursklausur;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurplan und erweitert die Klasse
 *  {@link ReportingGostKlausurplanungKlausurplan}.
 */
public class ProxyReportingGostKlausurplanungKlausurplan extends ReportingGostKlausurplanungKlausurplan {

	/** Repository für die Reporting. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Klausurmanager des GOSt-Klausurplans. */
	@JsonIgnore
	private final GostKlausurplanManager gostKlausurplanManager;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKlausurplanungKlausurplan}.
	 * @param reportingRepository	Repository für die Reporting.
	 * @param klausurtermine		Eine Liste, die alle Termine des Klausurplanes beinhaltet.
	 * @param kurse 				Eine Liste, die alle Kurse des Klausurplanes beinhaltet.
	 * @param kursklausuren 		Eine Liste, die alle Kursklausuren des Klausurplanes beinhaltet.
	 * @param schueler 				Eine Liste, die alle Schüler des Klausurplanes beinhaltet.
	 * @param schuelerklausuren 	Eine Liste, die alle Schülerklausuren des Klausurplanes beinhaltet.
	 * @param idsFilterSchueler 	Eine Liste, die die schülerbezogene Ausgabe auf die Schüler mit den enthaltenen IDs beschränkt.
	 */
	public ProxyReportingGostKlausurplanungKlausurplan(final ReportingRepository reportingRepository,
			final List<ReportingGostKlausurplanungKlausurtermin> klausurtermine, final List<ReportingKurs> kurse,
			final List<ReportingGostKlausurplanungKursklausur> kursklausuren, final List<ReportingSchueler> schueler,
			final List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren, final List<Long> idsFilterSchueler) {
		super(klausurtermine, kurse, kursklausuren, schueler, schuelerklausuren, idsFilterSchueler);
		this.reportingRepository = reportingRepository;
		this.gostKlausurplanManager = null;
	}


	/**
	 * Erstellt ein neues Reporting-Objekt anhand des GostKlausurplanManagers.
	 * @param reportingRepository		Repository für die Reporting.
	 * @param gostKlausurplanManager 	Der Manager der Klausuren zu diesem Klausurplan
	 * @param idsFilterSchueler 		Eine Liste, die die schülerbezogene Ausgabe auf die Schüler mit den enthaltenen IDs beschränkt.
	 */
	public ProxyReportingGostKlausurplanungKlausurplan(final ReportingRepository reportingRepository, final GostKlausurplanManager gostKlausurplanManager,
			final List<Long> idsFilterSchueler) {
		super(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), idsFilterSchueler);

		this.reportingRepository = reportingRepository;
		this.gostKlausurplanManager = gostKlausurplanManager;

		if (this.gostKlausurplanManager == null)
			return;

		// 1. Schülerstammdaten der Schüler aus den Schülerklausuren ermitteln und in Listen und Maps einfügen.
		initSchueler();

		// 2. Kurs-Objekte anhand der Kursklausuren erzeugen.
		super.kurse.addAll(this.gostKlausurplanManager.getKursManager().kurse().stream()
				.map(k -> new ProxyReportingKurs(this.reportingRepository, k))
				.toList());

		// 3. Klausurtermine erstellen
		// HINWEIS: Termine werden ohne Klausuren erzeugt. Wenn Klausuren erzeugt werden, werden diese dem Termin zugewiesen.
		super.klausurtermine.addAll(this.gostKlausurplanManager.terminGetMengeAsList().stream()
				.map(t -> (ReportingGostKlausurplanungKlausurtermin) new ProxyReportingGostKlausurplanungKlausurtermin(t))
				.toList());

		// 4. Kursklausuren erstellen.
		// HINWEIS: Kursklausuren und Klausurtermine erhalten ihre Schülerklausuren erst bei der Erzeugung der Schülerklausuren.
		// HINWEIS: Die Klausurräume werden in einem folgenden Schritt zentral zugewiesen.
		super.kursklausuren.addAll(this.gostKlausurplanManager.kursklausurGetMengeAsList().stream()
				.map(k -> (ReportingGostKlausurplanungKursklausur) new ProxyReportingGostKlausurplanungKursklausur(
						k,
						this.gostKlausurplanManager.vorgabeByKursklausur(k),
						(this.gostKlausurplanManager.terminOrNullByKursklausur(k) == null) ? null
								: klausurtermin(this.gostKlausurplanManager.terminOrNullByKursklausur(k).id),
						kurs(this.gostKlausurplanManager.kursdatenByKursklausur(k).id)))
				.toList());

		// 5. Klausurräume mit Aufsichten (sofern schon zugeteilt) erstellen.
		initKlausurraeume();

		// 6. Schülerklausuren erstellen.
		initSchuelerklausuren();

		// 7. Sortiere alle Schülerklausuren, sowohl Gesamtliste als auch bei den Kursklausuren.
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		super.schuelerklausuren.sort(Comparator
				.comparing((final ReportingGostKlausurplanungSchuelerklausur sk) -> sk.schueler().nachname(), colGerman)
				.thenComparing(sk -> sk.schueler().vorname(), colGerman)
				.thenComparing(sk -> sk.schueler().vornamen(), colGerman)
				.thenComparing(sk -> sk.schueler().id())
				.thenComparing(sk -> ((sk.klausurtermin() != null) && (sk.klausurtermin().datum() != null)) ? sk.klausurtermin().datum() : "yyyy-MM-dd"));
		super.kursklausuren.forEach(kk -> kk.schuelerklausuren().sort(Comparator
				.comparing((final ReportingGostKlausurplanungSchuelerklausur sk) -> sk.schueler().nachname(), colGerman)
				.thenComparing(sk -> sk.schueler().vorname(), colGerman)
				.thenComparing(sk -> sk.schueler().vornamen(), colGerman)
				.thenComparing(sk -> sk.schueler().id())
				.thenComparing(sk -> ((sk.klausurtermin() != null) && (sk.klausurtermin().datum() != null)) ? sk.klausurtermin().datum() : "yyyy-MM-dd")));

		// 8. Ergänze die Schülerklausuren in der Liste der KLausuren des Schülers.
		super.schuelerklausuren.forEach(sk -> sk.schueler().gostKlausurplanungSchuelerklausuren().add(sk));
	}

	/**
	 * Initialisiert die Schüler für die später zu erstellenden Schülerklausuren.
	 */
	private void initSchueler() {
		if (this.gostKlausurplanManager == null)
			return;

		final List<SchuelerStammdaten> schuelerStammdaten = new ArrayList<>();
		final List<Long> fehlendeSchueler = new ArrayList<>();

		for (final Long idSchueler : this.gostKlausurplanManager.schuelerklausurGetMengeAsList().stream().map(s -> s.idSchueler).distinct().toList()) {
			if (this.reportingRepository.mapSchuelerStammdaten().containsKey(idSchueler))
				schuelerStammdaten.add(this.reportingRepository.mapSchuelerStammdaten().get(idSchueler));
			else
				fehlendeSchueler.add(idSchueler);
		}
		if (!fehlendeSchueler.isEmpty()) {
			final List<SchuelerStammdaten> fehlendeSchuelerStammdaten = DataSchuelerStammdaten.getListStammdaten(this.reportingRepository.conn(),
					fehlendeSchueler);
			schuelerStammdaten.addAll(fehlendeSchuelerStammdaten);
			fehlendeSchuelerStammdaten.forEach(s -> this.reportingRepository.mapSchuelerStammdaten().putIfAbsent(s.id, s));
		}

		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		super.schueler.addAll(schuelerStammdaten.stream().map(s -> (ReportingSchueler) new ProxyReportingSchueler(this.reportingRepository, s))
				.sorted(Comparator.comparing(ReportingSchueler::nachname, colGerman)
						.thenComparing(ReportingSchueler::vorname, colGerman)
						.thenComparing(ReportingSchueler::vornamen, colGerman)
						.thenComparing(ReportingSchueler::id))
				.toList());
	}

	/**
	 * Initialisiert die Raumdaten und Unterrichtsstunden der Klausurräume. Das Ergebnis wird in den übergebenen Listen gespeichert.
	 */
	private void initKlausurraeume() {
		if (this.gostKlausurplanManager == null)
			return;

		// Durchlaufe alle Klausurtermine und weise ihnen die ReportingKlausurräume zu, die aus den Daten erzeugt werden.
		for (final ReportingGostKlausurplanungKlausurtermin termin : super.klausurtermine) {
			// Einem Termin können mehrere Räume zugewiesen worden sein. Ermittle sie gemäß TerminID.
			final GostKlausurtermin gostKlausurtermin = this.gostKlausurplanManager.terminGetByIdOrNull(termin.id);
			if (gostKlausurtermin != null) {
				// Durchlaufe alle Räume, ermittle dabei die Klausurstunden und erzeuge damit die Klausurräume.
				for (final GostKlausurraum terminraum : this.gostKlausurplanManager.raumGetMengeByTermin(gostKlausurtermin)) {
					termin.klausurraeume().add(
							new ProxyReportingGostKlausurplanungKlausurraum(this.reportingRepository, termin, terminraum,
									this.gostKlausurplanManager.raumstundeGetMengeByRaum(terminraum)));
				}
			}
		}
	}

	/**
	 * Initialisiert die Schülerklausuren mit allen Informationen (auch individuelle Raumdaten, Zeit oder Klausurdaten).
	 */
	private void initSchuelerklausuren() {
		if (this.gostKlausurplanManager == null)
			return;

		// Listen und Maps mit Daten aus den vorherigen Schritten, um nicht erneut auf die DB zugreifen zu müssen.
		final Map<Long, ReportingGostKlausurplanungKlausurtermin> mapKlausurtermine =
				super.klausurtermine.stream().collect(Collectors.toMap(ReportingGostKlausurplanungKlausurtermin::id, t -> t));
		final Map<Long, ReportingGostKlausurplanungKursklausur> mapKursklausuren =
				super.kursklausuren.stream().collect(Collectors.toMap(ReportingGostKlausurplanungKursklausur::id, k -> k));

		// Durchlaufe nun alle Schülerklausuren und erzeuge dafür deren Termine mit Klausurräumen usw.
		for (final GostSchuelerklausur sk : gostKlausurplanManager.schuelerklausurGetMengeAsList()) {

			// Zu einer Schülerklausur kann es mehrere Schülerklausurtermine geben, die sich in ihrer FolgeNr unterscheiden (z. B. bei Nachschrieb).
			for (final GostSchuelerklausurTermin skTermin : gostKlausurplanManager.schuelerklausurterminGetMengeBySchuelerklausur(sk)) {
				// 1. Den Klausurtermin für den Schülerklausurtermin ermitteln.
				ReportingGostKlausurplanungKlausurtermin klausurtermin = null;

				// Der Termin mit FolgeNr 0 und TerminID null ist der Termin der Kursklausur.
				if ((skTermin.folgeNr == 0) && (skTermin.idTermin == null)) {
					klausurtermin = mapKursklausuren.get(gostKlausurplanManager.kursklausurBySchuelerklausur(sk).id).klausurtermin();
				} else {
					klausurtermin = (skTermin.idTermin != null) ? mapKlausurtermine.get(skTermin.idTermin) : null;
				}

				// 2. Den Klausurraum mit den Stunden zum Schülerklausurtermin ermitteln.
				ReportingGostKlausurplanungKlausurraum klausurraum = null;

				final GostKlausurraum gostKlausurraum = gostKlausurplanManager.raumGetBySchuelerklausurtermin(skTermin);
				if (gostKlausurraum != null) {
					final List<GostKlausurraumstunde> gostKlausurraumstundenSchueler = gostKlausurplanManager.raumstundeGetMengeByRaum(gostKlausurraum);
					if (!gostKlausurraumstundenSchueler.isEmpty()) {
						klausurraum = new ProxyReportingGostKlausurplanungKlausurraum(
								this.reportingRepository, klausurtermin, gostKlausurraum, gostKlausurraumstundenSchueler);
					}
				}

				// 3. Schülerklausur erzeugen und der Gesamtliste der Schülerklausuren hinzufügen.
				super.schuelerklausuren.add(new ProxyReportingGostKlausurplanungSchuelerklausur(sk, skTermin, klausurraum, klausurtermin,
						mapKursklausuren.get(gostKlausurplanManager.kursklausurBySchuelerklausur(sk).id), schueler(sk.idSchueler)));
			}
		}
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	@JsonIgnore
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}

}
