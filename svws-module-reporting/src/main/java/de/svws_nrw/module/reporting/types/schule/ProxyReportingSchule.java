package de.svws_nrw.module.reporting.types.schule;

import java.util.ArrayList;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Schule und erweitert die Klasse {@link ReportingSchule}.
 */
public class ProxyReportingSchule extends ReportingSchule {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchule}.
	 *
	 * @param reportingContext Repository für das Reporting.
	 */
	public ProxyReportingSchule(final ReportingContext reportingContext) {
		super(new ArrayList<>(),
				ersetzeNullBlankTrim(reportingContext.repositorySchule().stammdaten().email),
				ersetzeNullBlankTrim(reportingContext.repositorySchule().stammdaten().fax),
				ersetzeNullBlankTrim(reportingContext.repositorySchule().stammdaten().hausnummer),
				ersetzeNullBlankTrim(reportingContext.repositorySchule().stammdaten().ort),
				ersetzeNullBlankTrim(reportingContext.repositorySchule().stammdaten().plz),
				reportingContext.repositorySchule().stammdaten().schulNr,
				ersetzeNullBlankTrim(reportingContext.repositorySchule().stammdaten().strassenname),
				ersetzeNullBlankTrim(reportingContext.repositorySchule().stammdaten().telefon),
				ersetzeNullBlankTrim(reportingContext.repositorySchule().stammdaten().hausnummerZusatz),
				null, // aktuellerSchuljahresabschnitt
				reportingContext.repositorySchule().stammdaten().anzJGS_Jahr,
				reportingContext.repositorySchule().stammdaten().schuleAbschnitte.anzahlAbschnitte,
				null, // auswahlSchuljahresabschnitt
				ersetzeNullBlankTrim(reportingContext.repositorySchule().stammdaten().schuleAbschnitte.abschnittBez),
				reportingContext.repositorySchule().stammdaten().schuleAbschnitte.bezAbschnitte,
				reportingContext.repositorySchule().stammdaten().dauerUnterrichtseinheit,
				new ArrayList<>(), // schuljahresabschnitte
				null,
				null, // schulleitung
				null, // stvSchulleitung
				ersetzeNullBlankTrim(reportingContext.repositorySchule().stammdaten().webAdresse));


		this.reportingContext = reportingContext;

		super.bezeichnung.add(ersetzeNullBlankTrim(reportingContext.repositorySchule().stammdaten().bezeichnung1));
		super.bezeichnung.add(ersetzeNullBlankTrim(reportingContext.repositorySchule().stammdaten().bezeichnung2));
		super.bezeichnung.add(ersetzeNullBlankTrim(reportingContext.repositorySchule().stammdaten().bezeichnung3));
		super.bezeichnung.removeIf(Objects::isNull);
		super.bezeichnung.removeIf(String::isBlank);

		super.schulform = this.reportingContext.repositoryKataloge().schulformen()
				.stream().filter(sf -> ((Objects.equals(sf.kuerzel, reportingContext.repositorySchule().stammdaten().schulform)) && (sf.gueltigBis == null)))
				.findFirst().orElse(null);

		super.schuljahresabschnitte = this.reportingContext.repositorySchule().schuljahresabschnitte();
		super.aktuellerSchuljahresabschnitt = this.reportingContext.repositorySchule().aktuellerSchuljahresabschnitt();
		super.auswahlSchuljahresabschnitt = this.reportingContext.repositorySchule().auswahlSchuljahresabschnitt();
	}


	// ##### Getter #####

	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingContext reportingContext() {
		return reportingContext;
	}

	/**
	 * Der Lehrer, der die Schulleitungsfunktion besitzt.
	 *
	 * @return Inhalt des Feldes Schulleitung
	 */
	@Override
	public ReportingLehrer schulleitung() {
		if (super.schulleitung == null) {
			super.schulleitung = this.reportingContext.repositoryLehrer().alleLehrer()
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
			super.stvSchulleitung = this.reportingContext.repositoryLehrer().alleLehrer()
					.stream().filter(ReportingLehrer::istStvSchulleitungAktuell).findFirst().orElse(null);
		}
		return super.stvSchulleitung;
	}

	/**
	 * Das Bild aus der Logoverwaltung zu der übergebenen Bilddefinition.
	 *
	 * @param bildDefinition Die Bilddefinition, deren Bild gesucht wird.
	 *
	 * @return Das Bild, nie {@code null}.
	 */
	@Override
	public ReportingBild bild(final ReportingBildDefinition bildDefinition) {
		return this.reportingContext.repositorySchule().bild(bildDefinition);
	}
}
