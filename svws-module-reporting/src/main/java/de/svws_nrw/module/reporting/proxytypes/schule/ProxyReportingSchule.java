package de.svws_nrw.module.reporting.proxytypes.schule;

import java.util.ArrayList;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
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
	 * @param reportingRepository Repository für das Reporting.
	 */
	public ProxyReportingSchule(final ReportingRepository reportingRepository) {
		super(new ArrayList<>(),
				ersetzeNullBlankTrim(reportingRepository.schulstammdaten().email),
				ersetzeNullBlankTrim(reportingRepository.schulstammdaten().fax),
				ersetzeNullBlankTrim(reportingRepository.schulstammdaten().hausnummer),
				ersetzeNullBlankTrim(reportingRepository.schulstammdaten().ort),
				ersetzeNullBlankTrim(reportingRepository.schulstammdaten().plz),
				reportingRepository.schulstammdaten().schulNr,
				ersetzeNullBlankTrim(reportingRepository.schulstammdaten().strassenname),
				ersetzeNullBlankTrim(reportingRepository.schulstammdaten().telefon),
				ersetzeNullBlankTrim(reportingRepository.schulstammdaten().hausnummerZusatz),
				null, // aktuellerSchuljahresabschnitt
				reportingRepository.schulstammdaten().anzJGS_Jahr,
				reportingRepository.schulstammdaten().schuleAbschnitte.anzahlAbschnitte,
				null, // auswahlSchuljahresabschnitt
				ersetzeNullBlankTrim(reportingRepository.schulstammdaten().schuleAbschnitte.abschnittBez),
				reportingRepository.schulstammdaten().schuleAbschnitte.bezAbschnitte,
				reportingRepository.schulstammdaten().dauerUnterrichtseinheit,
				new ArrayList<>(), // schuljahresabschnitte
				null,
				null, // schulleitung
				null, // schullogo
				null, // stvSchulleitung
				ersetzeNullBlankTrim(reportingRepository.schulstammdaten().webAdresse));


		this.reportingRepository = reportingRepository;

		super.bezeichnung.add(ersetzeNullBlankTrim(reportingRepository.schulstammdaten().bezeichnung1));
		super.bezeichnung.add(ersetzeNullBlankTrim(reportingRepository.schulstammdaten().bezeichnung2));
		super.bezeichnung.add(ersetzeNullBlankTrim(reportingRepository.schulstammdaten().bezeichnung3));
		super.bezeichnung.removeIf(Objects::isNull);
		super.bezeichnung.removeIf(String::isBlank);

		super.schullogo = new DataSchuleStammdaten(this.reportingRepository.conn()).getSchullogoBase64();

		super.schulform = this.reportingRepository.katalogSchulformen().values()
				.stream().filter(sf -> ((Objects.equals(sf.kuerzel, reportingRepository.schulstammdaten().schulform)) && (sf.gueltigBis == null)))
				.findFirst().orElse(null);

		super.schuljahresabschnitte = this.reportingRepository.schuljahresabschnitte();
		super.aktuellerSchuljahresabschnitt = this.reportingRepository.aktuellerSchuljahresabschnitt();
		super.auswahlSchuljahresabschnitt = this.reportingRepository.auswahlSchuljahresabschnitt();
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
					this.reportingRepository.lehrer(this.reportingRepository.mapLehrerStammdaten().keySet().stream().toList())
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
					this.reportingRepository.lehrer(this.reportingRepository.mapLehrerStammdaten().keySet().stream().toList())
							.stream().filter(ReportingLehrer::istStvSchulleitungAktuell).findFirst().orElse(null);
		}
		return super.stvSchulleitung;
	}
}
