package de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKursklausurManager;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.module.reporting.proxytypes.kurs.ProxyReportingKurs;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKursklausur;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurplan und erweitert die Klasse
 *  {@link ReportingGostKlausurplanungKlausurplan}.</p>
 *
 *  <p>In diesem Kontext besitzt die Proxy-Klasse ausschließlich die gleichen Methoden wie die zugehörige Reporting-Super-Klasse.
 *  Während die Super-Klasse aber als reiner Datentyp konzipiert ist, d. h. ohne Anbindung an die Datenbank,
 *  greift die Proxy-Klassen an verschiedenen Stellen auf die Datenbank zu.</p>
 *
 *  <ul>
 *      <li>Die Proxy-Klasse stellt in der Regel einen zusätzlichen Constructor zur Verfügung, um Reporting-Objekte
 *  		aus Stammdatenobjekten (aus dem Package core.data) erstellen zu können. Darin werden Felder, die Reporting-Objekte
 *  		zurückgegeben und nicht im Stammdatenobjekt enthalten sind, mit null initialisiert.</li>
 * 		<li>Die Proxy-Klasse überschreibt einzelne Getter der Super-Klasse (beispielsweise bei Felder, die mit null initialisiert wurden)
 *  		und lädt dort dann aus der Datenbank die Daten bei Bedarf nach (lazy-loading), um den Umfang der Datenstrukturen gering zu
 *  		halten.</li>
 *  	<li>Die Proxy-Klasse können zudem auf das Repository {@link ReportingRepository} zugreifen. Dieses
 *  		enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte
 *  		wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden. Werden in der
 *  		Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der entsprechenden Map des Repository ergänzt.</li>
 *  </ul>
 */
public class ProxyReportingGostKlausurplanungKlausurplan extends ReportingGostKlausurplanungKlausurplan {

	/** Repository für die Reporting. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Klausurmanager des GOSt-Klausurplans. */
	@JsonIgnore
	private final GostKursklausurManager gostKlausurManager;


	/**
	 * Erstellt ein neues Reporting-Objekt anhand des Abiturjahres und des Gost-Halbjahres.
	 *
	 * @param reportingRepository	Repository für die Reporting.
	 * @param gostKlausurManager 	Der Manager der Klausuren zu diesem Klausurplan
	 */
	public ProxyReportingGostKlausurplanungKlausurplan(final ReportingRepository reportingRepository, final GostKursklausurManager gostKlausurManager) {
		super(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

		final Collator colGerman = java.text.Collator.getInstance(Locale.GERMAN);

		this.reportingRepository = reportingRepository;
		this.gostKlausurManager = gostKlausurManager;

		// 1. Schülerstammdaten der Schüler aus den Schülerklausuren ermitteln und in Listen und Maps einfügen.
		final List<SchuelerStammdaten> schuelerStammdaten = new ArrayList<>();
		final List<Long> fehlendeSchueler = new ArrayList<>();

		for (final Long idSchueler : this.gostKlausurManager.schuelerklausurGetMengeAsList().stream().map(s -> s.idSchueler).distinct().toList()) {
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

		super.schueler.addAll(schuelerStammdaten.stream().map(s -> (ReportingSchueler) new ProxyReportingSchueler(this.reportingRepository, s))
				.sorted(Comparator.comparing(ReportingSchueler::nachname, colGerman)
						.thenComparing(ReportingSchueler::vorname, colGerman)
						.thenComparing(ReportingSchueler::vornamen, colGerman)
						.thenComparing(ReportingSchueler::id))
				.toList());
		super.mapSchueler().putAll(super.schueler.stream().collect(Collectors.toMap(ReportingSchueler::id, s -> s)));

		// 2. Kurs-Objekte anhand der Kursklausuren erzeugen.
		final List<ReportingKurs> gefundeneKurse = new ArrayList<>();
		final List<Long> gefundeneIdsKurse = new ArrayList<>();

		for (final GostKursklausur kursklausur : this.gostKlausurManager.kursklausurGetMengeAsList()) {
			final KursDaten kursDaten = gostKlausurManager.getKursByKursklausur(kursklausur);
			if (!gefundeneIdsKurse.contains(kursDaten.id)) {
				gefundeneKurse.add(new ProxyReportingKurs(this.reportingRepository, kursDaten));
				gefundeneIdsKurse.add(kursDaten.id);
			}
		}
		super.kurse.addAll(gefundeneKurse);
		super.mapKurse().clear();
		super.mapKurse().putAll(super.kurse.stream().collect(Collectors.toMap(ReportingKurs::id, k -> k)));

		// 3. Klausurtermine erstellen
		// HINWEIS: Termine werden ohne Kursklausuren erzeugt. Wenn Kursklausuren erzeugt werden, werden diese dem Termin zugewiesen.
		super.klausurtermine.addAll(gostKlausurManager.terminGetMengeAsList().stream()
				.map(t -> (ReportingGostKlausurplanungKlausurtermin) new ProxyReportingGostKlausurplanungKlausurtermin(t))
				.toList());
		super.mapKlausurtermine().clear();
		super.mapKlausurtermine()
				.putAll(super.klausurtermine.stream().collect(Collectors.toMap(ReportingGostKlausurplanungKlausurtermin::idKlausurtermin, t -> t)));

		// 4. Kurs- und Schülerklausuren erstellen.
		// HINWEIS: Kursklausuren werden ohne Schülerklausuren erzeugt. Wenn Schülerklausuren erzeugt werden, werden diese der Kursklausur zugewiesen.
		super.kursklausuren.addAll(gostKlausurManager.kursklausurGetMengeAsList().stream()
				.map(k -> (ReportingGostKlausurplanungKursklausur) new ProxyReportingGostKlausurplanungKursklausur(
						k,
						this.gostKlausurManager.vorgabeByKursklausur(k),
						(gostKlausurManager.terminOrNullByKursklausur(k) == null)
								? null : mapKlausurtermine().get(gostKlausurManager.terminOrNullByKursklausur(k).id),
						mapKurse().get(gostKlausurManager.getKursByKursklausur(k).id)))
				.toList());
		super.mapKursklausuren().clear();
		super.mapKursklausuren().putAll(super.kursklausuren.stream().collect(Collectors.toMap(ReportingGostKlausurplanungKursklausur::id, k -> k)));

		super.schuelerklausuren.addAll(gostKlausurManager.schuelerklausurGetMengeAsList().stream()
				.map(s -> (ReportingGostKlausurplanungSchuelerklausur) new ProxyReportingGostKlausurplanungSchuelerklausur(s,
						this.mapKursklausuren().get(gostKlausurManager.kursklausurBySchuelerklausur(s).id), mapSchueler().get(s.idSchueler)))
				.toList());
		super.mapSchuelerklausuren().clear();
		super.mapSchuelerklausuren().putAll(super.schuelerklausuren.stream().collect(Collectors.toMap(ReportingGostKlausurplanungSchuelerklausur::id, s -> s)));
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
