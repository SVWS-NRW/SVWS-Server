package de.svws_nrw.module.reporting.proxytypes.fach;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.fach.FachKatalogEintrag;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.fach.ReportingStatistikFach;

import java.util.List;
import java.util.Objects;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ StatistikFach und erweitert die Klasse {@link ReportingStatistikFach}.</p>
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
 * 		<li>Die Proxy-Klasse können zudem auf das Repository {@link ReportingRepository} zugreifen. Dieses
 * 			enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte
 * 			wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden. Werden in der
 * 			Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der entsprechenden Map des Repository ergänzt.</li>
 *  </ul>
 */
public class ProxyReportingStatistikFach extends ReportingStatistikFach {

	/** Repository für die Reporting */
	@JsonIgnore
	private final ReportingRepository reportingRepository;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis der Daten eines Statistik-Faches.
	 * Wenn das Statistikfach in dem im Repository hinterlegtem Schuljahr nicht gültig war, können keine Daten des Statistikfaches ergänzt werden. Es ist
	 * dann null.
	 * @param reportingRepository Repository für die Reporting.
	 * @param zulaessigesFach Fach aus dem Katalog der zulässigen Fächer
	 */
	public ProxyReportingStatistikFach(final ReportingRepository reportingRepository, final Fach zulaessigesFach) {
		super(null,
				-1,
				null,
				false,
				null,
				null,
				null,
				null,
				"",
				-1,
				false,
				false,
				false,
				false,
				false,
				null,
				null,
				false);
		this.reportingRepository = reportingRepository;
		initReportingStatistikFach(zulaessigesFach);
	}

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis der Daten eines Statistik-Fachkürzels (ASD-Kürzel). Dadurch werden die Fachdaten des Statistik-Faches angelegt.
	 * Wenn das Statistikfach in dem im Repository hinterlegtem Schuljahr nicht gültig war, können keine Daten des Statistikfaches ergänzt werden. Es ist
	 * dann null.
	 * @param reportingRepository Repository für die Reporting.
	 * @param kuerzelStatistik ASD-Kürzel des zugehörigen Statistik-Faches
	 */
	public ProxyReportingStatistikFach(final ReportingRepository reportingRepository, final String kuerzelStatistik) {
		super(null,
				-1,
				null,
				false,
				null,
				null,
				null,
				null,
				"",
				-1,
				false,
				false,
				false,
				false,
				false,
				null,
				null,
				false);
		this.reportingRepository = reportingRepository;
		if ((kuerzelStatistik != null) && !kuerzelStatistik.isEmpty())
			initReportingStatistikFach(Fach.getBySchluesselOrDefault(kuerzelStatistik));
	}


	private void initReportingStatistikFach(final Fach zulaessigesFach) {

		final FachKatalogEintrag datenZulaessigesFach = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr());

		// Wenn die datenZulaessigesFach null sind, dann war das Statistikfach wahrscheinlich im angegebenen Schuljahr nicht gültig.
		if (datenZulaessigesFach != null) {
			super.abJahrgang = datenZulaessigesFach.abJahrgang;
			super.aufgabenfeld = datenZulaessigesFach.aufgabenfeld;
			super.bezeichnung = datenZulaessigesFach.text;
			super.exportASD = datenZulaessigesFach.exportASD;
			super.fachgruppe = zulaessigesFach.getFachgruppe(reportingRepository.auswahlSchuljahr());
			super.gueltigBis = datenZulaessigesFach.gueltigBis;
			super.gueltigVon = datenZulaessigesFach.gueltigVon;
			super.htmlFarbeRGB = zulaessigesFach.getHMTLFarbeRGB(reportingRepository.auswahlSchuljahr());
			super.idFachkatalog = datenZulaessigesFach.id;
			super.istAusRegUFach = datenZulaessigesFach.istAusRegUFach;
			super.istErsatzPflichtFS = datenZulaessigesFach.istErsatzPflichtFS;
			super.istFremdsprache = datenZulaessigesFach.istFremdsprache;
			super.istHKFS = datenZulaessigesFach.istHKFS;
			super.istKonfKoop = datenZulaessigesFach.istKonfKoop;
			super.kuerzel = datenZulaessigesFach.kuerzel;
			super.kuerzelASD = datenZulaessigesFach.schluessel;
			super.nurSII = datenZulaessigesFach.nurSII;

			// Prüfe, ob es im Fachkatalog des Reporting-Repositories der Schule ein Fach gibt, dessen Kürzel identisch ist mir dem ASD-Kürzel dieses Statistikfaches.
			final List<ReportingFach> faecher = this.reportingRepository.mapReportingFaecher().values().stream()
					.filter(f -> Objects.equals(f.kuerzel(), datenZulaessigesFach.schluessel)).toList();
			if (faecher.size() == 1) {
				super.fach = faecher.getFirst();
			}
		}
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}
}
