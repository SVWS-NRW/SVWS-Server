package de.svws_nrw.module.reporting.proxytypes.fach;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	 * Erstellt ein neues Reporting-Objekt auf Basis der Daten eines Fach- und GostFach-Objektes.
	 * @param reportingRepository Repository für die Reporting.
	 * @param zulaessigesFach Fach aus dem Katalog der zulässigen Fächer
	 */
	public ProxyReportingStatistikFach(final ReportingRepository reportingRepository, final Fach zulaessigesFach) {
		super(zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).abJahrgang,
				zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).aufgabenfeld,
				zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).text,
				zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).exportASD,
				null,
				zulaessigesFach.getFachgruppe(reportingRepository.auswahlSchuljahr()),
				zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).gueltigBis,
				zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).gueltigVon,
				zulaessigesFach.getHMTLFarbeRGB(reportingRepository.auswahlSchuljahr()),
				zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).id,
				zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).istAusRegUFach,
				zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).istErsatzPflichtFS,
				zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).istFremdsprache,
				zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).istHKFS,
				zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).istKonfKoop,
				zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).kuerzel,
				zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).schluessel,
				zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).nurSII);
		this.reportingRepository = reportingRepository;
		// Prüfe, ob es im Fachkatalog des Reporting-Repositories der Schule ein Fach gibt, dessen Kürzel identisch ist mir dem ASD-Kürzel dieses Statistikfaches.
		final List<ReportingFach> faecher = this.reportingRepository.mapReportingFaecher().values().stream()
				.filter(f -> Objects.equals(f.kuerzel(), zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).schluessel)).toList();
		if (faecher.size() == 1) {
			super.fach = faecher.getFirst();
		}
	}

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis der Daten eines Statistik-Fachkürzels (ASD-Kürzel). Dadurch werden die Fachdaten des Statistik-Faches angelegt.
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

		if ((kuerzelStatistik != null) && !kuerzelStatistik.isEmpty()) {
			final Fach zulaessigesFach = Fach.getBySchluesselOrDefault(kuerzelStatistik);
			super.abJahrgang = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).abJahrgang;
			super.aufgabenfeld = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).aufgabenfeld;
			super.bezeichnung = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).text;
			super.exportASD = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).exportASD;
			super.fachgruppe = zulaessigesFach.getFachgruppe(reportingRepository.auswahlSchuljahr());
			super.gueltigBis = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).gueltigBis;
			super.gueltigVon = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).gueltigVon;
			super.htmlFarbeRGB = zulaessigesFach.getHMTLFarbeRGB(reportingRepository.auswahlSchuljahr());
			super.idFachkatalog = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).id;
			super.istAusRegUFach = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).istAusRegUFach;
			super.istErsatzPflichtFS = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).istErsatzPflichtFS;
			super.istFremdsprache = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).istFremdsprache;
			super.istHKFS = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).istHKFS;
			super.istKonfKoop = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).istKonfKoop;
			super.kuerzel = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).kuerzel;
			super.kuerzelASD = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).schluessel;
			super.nurSII = zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).nurSII;
			// Prüfe, ob es im Fachkatalog des Reporting-Repositories der Schule ein Fach gibt, dessen Kürzel identisch ist mir dem ASD-Kürzel dieses Statistikfaches.
			final List<ReportingFach> faecher = this.reportingRepository.mapReportingFaecher().values().stream()
					.filter(f -> Objects.equals(f.kuerzel(), zulaessigesFach.daten(reportingRepository.auswahlSchuljahr()).schluessel)).toList();
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
