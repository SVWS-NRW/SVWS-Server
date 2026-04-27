package de.svws_nrw.module.reporting.types.schule;

import java.util.ArrayList;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;

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
	 * @param reportingRepository Repository für das Reporting.
	 */
	public ProxyReportingSchule(final ReportingRepository reportingRepository) {
		super(new ArrayList<>(),
				ersetzeNullBlankTrim(reportingRepository.repositorySchule().stammdaten().email),
				ersetzeNullBlankTrim(reportingRepository.repositorySchule().stammdaten().fax),
				ersetzeNullBlankTrim(reportingRepository.repositorySchule().stammdaten().hausnummer),
				ersetzeNullBlankTrim(reportingRepository.repositorySchule().stammdaten().ort),
				ersetzeNullBlankTrim(reportingRepository.repositorySchule().stammdaten().plz),
				reportingRepository.repositorySchule().stammdaten().schulNr,
				ersetzeNullBlankTrim(reportingRepository.repositorySchule().stammdaten().strassenname),
				ersetzeNullBlankTrim(reportingRepository.repositorySchule().stammdaten().telefon),
				ersetzeNullBlankTrim(reportingRepository.repositorySchule().stammdaten().hausnummerZusatz),
				null, // aktuellerSchuljahresabschnitt
				reportingRepository.repositorySchule().stammdaten().anzJGS_Jahr,
				reportingRepository.repositorySchule().stammdaten().schuleAbschnitte.anzahlAbschnitte,
				null, // auswahlSchuljahresabschnitt
				ersetzeNullBlankTrim(reportingRepository.repositorySchule().stammdaten().schuleAbschnitte.abschnittBez),
				reportingRepository.repositorySchule().stammdaten().schuleAbschnitte.bezAbschnitte,
				reportingRepository.repositorySchule().stammdaten().dauerUnterrichtseinheit,
				new ArrayList<>(), // schuljahresabschnitte
				null,
				null, // schulleitung
				null, // schullogo
				null, // stvSchulleitung
				ersetzeNullBlankTrim(reportingRepository.repositorySchule().stammdaten().webAdresse));


		this.reportingRepository = reportingRepository;

		super.bezeichnung.add(ersetzeNullBlankTrim(reportingRepository.repositorySchule().stammdaten().bezeichnung1));
		super.bezeichnung.add(ersetzeNullBlankTrim(reportingRepository.repositorySchule().stammdaten().bezeichnung2));
		super.bezeichnung.add(ersetzeNullBlankTrim(reportingRepository.repositorySchule().stammdaten().bezeichnung3));
		super.bezeichnung.removeIf(Objects::isNull);
		super.bezeichnung.removeIf(String::isBlank);

		super.schullogo = new DataSchuleStammdaten(this.reportingRepository.conn()).getSchullogoBase64();

		super.schulform = this.reportingRepository.repositoryKataloge().schulformen().values()
				.stream().filter(sf -> ((Objects.equals(sf.kuerzel, reportingRepository.repositorySchule().stammdaten().schulform)) && (sf.gueltigBis == null)))
				.findFirst().orElse(null);

		super.schuljahresabschnitte = this.reportingRepository.repositorySchule().schuljahresabschnitte();
		super.aktuellerSchuljahresabschnitt = this.reportingRepository.repositorySchule().aktuellerSchuljahresabschnitt();
		super.auswahlSchuljahresabschnitt = this.reportingRepository.repositorySchule().auswahlSchuljahresabschnitt();
	}


	// ##### Getter #####

	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}

	/**
	 * Der Lehrer, der die Schulleitungsfunktion besitzt.
	 *
	 * @return Inhalt des Feldes Schulleitung
	 */
	@Override
	public ReportingLehrer schulleitung() {
		if (super.schulleitung == null) {
			super.schulleitung =
					this.reportingRepository.repositoryLehrer().lehrer(this.reportingRepository.repositoryLehrer().stammdaten().keySet().stream().toList())
							.stream().filter(ReportingLehrer::istSchulleitungAktuell).findFirst().orElse(null);
		}
		return super.schulleitung;
	}

	/**
	 * Der Lehrer, der die stv. Schulleitungsfunktion besitzt.
	 *
	 * @return Inhalt des Feldes stvSchulleitung
	 */
	@Override
	public ReportingLehrer stvSchulleitung() {
		if (super.stvSchulleitung == null) {
			super.stvSchulleitung =
					this.reportingRepository.repositoryLehrer().lehrer(this.reportingRepository.repositoryLehrer().stammdaten().keySet().stream().toList())
							.stream().filter(ReportingLehrer::istStvSchulleitungAktuell).findFirst().orElse(null);
		}
		return super.stvSchulleitung;
	}
}
