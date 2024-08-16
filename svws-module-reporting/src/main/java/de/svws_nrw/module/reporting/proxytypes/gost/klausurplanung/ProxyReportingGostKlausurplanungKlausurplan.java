package de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.module.reporting.proxytypes.kurs.ProxyReportingKurs;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
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
public class ProxyReportingGostKlausurplanungKlausurplan {

	/** Repository für die Reporting. */
	@JsonIgnore
	protected final ReportingRepository reportingRepository;

	/** Klausurmanager des GOSt-Klausurplans. */
	@JsonIgnore
	protected final GostKlausurplanManager manager;

	/** Eine Liste, die alle Kurse des Klausurplanes beinhaltet. */
	protected List<ReportingKurs> kurse;

	/** Eine Liste, die alle Schüler des Klausurplanes beinhaltet. */
	protected List<ReportingSchueler> schueler;



	/**
	 * Erstellt ein neues Reporting-Objekt anhand des Abiturjahres und des Gost-Halbjahres.
	 *
	 * @param reportingRepository		Repository für die Reporting.
	 * @param gostKlausurplanManager 	Der Manager der Klausuren zu diesem Klausurplan
	 */
	public ProxyReportingGostKlausurplanungKlausurplan(final ReportingRepository reportingRepository, final GostKlausurplanManager gostKlausurplanManager) {

		this.reportingRepository = reportingRepository;
		this.manager = gostKlausurplanManager;



	}

	/**
	 * Erstellt ein neues Reporting-Objekt anhand des Abiturjahres und des Gost-Halbjahres.
	 *
	 */
	public void init_all() {
		// 1. Schülerstammdaten der Schüler aus den Schülerklausuren ermitteln und in Listen und Maps einfügen.
		initSchueler();

		// 2. Kurs-Objekte anhand der Kursklausuren erzeugen.
		kurse.addAll(this.manager.getKursManager().kurse().stream()
				.map(k -> new ProxyReportingKurs(this.reportingRepository, k))
				.toList());
	}

	/**
	 * Initialisiert die Schüler für die später zu erstellenden Schülerklausuren.
	 */
	private void initSchueler() {
		final List<SchuelerStammdaten> schuelerStammdaten = new ArrayList<>();
		final List<Long> fehlendeSchueler = new ArrayList<>();

		for (final Long idSchueler : this.manager.schuelerklausurGetMengeAsList().stream().map(s -> s.idSchueler).distinct().toList()) {
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
		schueler.addAll(schuelerStammdaten.stream().map(s -> (ReportingSchueler) new ProxyReportingSchueler(this.reportingRepository, s))
				.sorted(Comparator.comparing(ReportingSchueler::nachname, colGerman)
						.thenComparing(ReportingSchueler::vorname, colGerman)
						.thenComparing(ReportingSchueler::vornamen, colGerman)
						.thenComparing(ReportingSchueler::id))
				.toList());
	}



	/**
	 * Eine Liste vom Typ GostKlausurplanungKlausurtermin, die alle Termine des Klausurplanes beinhaltet, denen bereits ein Datum zugewiesen wurde.
	 * @return Liste der Klausurtermine mit Datumsangabe
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermineMitDatum() {
		return manager.terminMitDatumGetMenge().stream().map(t -> new ReportingGostKlausurplanungKlausurtermin((ProxyReportingGostKlausurplanungKlausurtermin) t)).toList();
	}

	/**
	 * Eine Liste vom Typ GostKlausurplanungKlausurtermin, die alle Termine des Klausurplanes beinhaltet, denen noch kein Datum zugewiesen wurde.
	 * @return Liste der Klausurtermine ohne Datumsangabe
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermineOhneDatum() {
		return manager.terminOhneDatumGetMenge().stream().map(t -> new ReportingGostKlausurplanungKlausurtermin((ProxyReportingGostKlausurplanungKlausurtermin) t)).toList();
	}

	/**
	 * Eine Liste, die alle Termine des Klausurplanes beinhaltet.
	 * @return Liste der Klausurtermine
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermine() {
		return manager.terminGetMengeAsList().stream().map(t -> new ReportingGostKlausurplanungKlausurtermin((ProxyReportingGostKlausurplanungKlausurtermin) t)).toList();
	}

	/**
	 * Eine Liste vom Typ GostKlausurplanungKlausurtermin, die alle Termine des Klausurplanes zum angegebenen Datum beinhaltet.
	 * @param  datum 	Datum, zu dem die Liste der Klausurtermine zurückgegeben werden soll.
	 * @return 			Liste der Klausurtermine mit dem gewünschten Datum
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermineZumDatum(final String datum) {
		return manager.terminGetMengeByDatum(datum).stream().map(t -> new ReportingGostKlausurplanungKlausurtermin((ProxyReportingGostKlausurplanungKlausurtermin) t)).toList();
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
