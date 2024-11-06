package de.svws_nrw.module.reporting.proxytypes.schule;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schule.ReportingSchule;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Schule und erweitert die Klasse {@link ReportingSchule}.
 */
public class ProxyReportingSchule extends ReportingSchule {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchule}.
	 *
	 * @param reportingRepository Repository für die Reporting.
	 */
	public ProxyReportingSchule(final ReportingRepository reportingRepository) {
		super(null,
				reportingRepository.schulstammdaten().anzJGS_Jahr,
				reportingRepository.schulstammdaten().schuleAbschnitte.anzahlAbschnitte,
				null,
				reportingRepository.schulstammdaten().bezeichnung1,
				reportingRepository.schulstammdaten().bezeichnung2,
				reportingRepository.schulstammdaten().bezeichnung3,
				reportingRepository.schulstammdaten().schuleAbschnitte.abschnittBez,
				reportingRepository.schulstammdaten().schuleAbschnitte.bezAbschnitte,
				reportingRepository.schulstammdaten().dauerUnterrichtseinheit,
				reportingRepository.schulstammdaten().email,
				reportingRepository.schulstammdaten().fax,
				reportingRepository.schulstammdaten().hausnummer,
				reportingRepository.schulstammdaten().hausnummerZusatz,
				reportingRepository.schulstammdaten().ort,
				reportingRepository.schulstammdaten().plz,
				new ArrayList<>(),
				reportingRepository.schulstammdaten().schulform,
				null,
				reportingRepository.schulstammdaten().schulNr,
				reportingRepository.schulstammdaten().strassenname,
				reportingRepository.schulstammdaten().telefon,
				reportingRepository.schulstammdaten().webAdresse);

		this.reportingRepository = reportingRepository;

		super.schullogo = new DataSchuleStammdaten(this.reportingRepository.conn()).getSchullogoBase64();

		super.schuljahresabschnitte = this.reportingRepository.schuljahresabschnitte();
		super.aktuellerSchuljahresabschnitt = this.reportingRepository.aktuellerSchuljahresabschnitt();
		super.auswahlSchuljahresabschnitt = this.reportingRepository.auswahlSchuljahresabschnitt();
	}


	// ##### Getter #####

	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für die Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}
}
