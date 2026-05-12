package de.svws_nrw.module.reporting.types.schule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.svws_nrw.module.reporting.types.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Lehrer und erweitert die Klasse {@link ReportingBenutzer}.
 */
public class ProxyReportingBenutzer extends ReportingBenutzer {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingBenutzer}.
	 *
	 * @param reportingContext Repository für das Reporting.
	 */
	public ProxyReportingBenutzer(final ReportingContext reportingContext) {
		super("",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				null,
				"",
				"",
				-1,
				false,
				"",
				null,
				"",
				null,
				null,
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				null,
				null);

		this.reportingContext = reportingContext;
		final Benutzer user = this.reportingContext.repositorySchule().benutzer();

		super.benutzername = user.getUsername();
		super.id = user.getId();
		super.istAdmin = user.istAdmin();

		// Prüfe, ob der angemeldete Benutzer Lehrer ist. Übernehme dann dessen Informationen. Andernfalls weitere Informationen aus der Datenbank laden.
		if ((user.getIdLehrer() != null) && this.reportingContext.repositoryLehrer().stammdaten().containsKey(user.getIdLehrer())) {
			super.lehrer = new ProxyReportingLehrer(this.reportingContext, this.reportingContext.repositoryLehrer().stammdaten().get(user.getIdLehrer()));
			super.anzeigename = super.lehrer.vornameNachname();
		} else {
			final DTOViewBenutzerdetails dtoBenutzer = this.reportingContext.repositorySchule().benutzerdetails(super.id);
			if (dtoBenutzer != null) {
				super.anzeigename = dtoBenutzer.AnzeigeName;
			}
		}
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return    true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingContext reportingContext() {
		return reportingContext;
	}
}
