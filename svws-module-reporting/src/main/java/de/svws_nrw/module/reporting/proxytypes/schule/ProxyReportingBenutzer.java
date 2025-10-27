package de.svws_nrw.module.reporting.proxytypes.schule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schule.ReportingBenutzer;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Lehrer und erweitert die Klasse {@link ReportingBenutzer}.
 */
public class ProxyReportingBenutzer extends ReportingBenutzer {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingBenutzer}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 */
	public ProxyReportingBenutzer(final ReportingRepository reportingRepository) {
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

		this.reportingRepository = reportingRepository;
		final Benutzer user = this.reportingRepository.conn().getUser();

		super.benutzername = user.getUsername();
		super.id = user.getId();
		super.istAdmin = user.istAdmin();

		// Prüfe, ob der angemeldete Benutzer Lehrer ist. Übernehme dann dessen Informationen. Andernfalls weitere Informationen aus der Datenbank laden.
		if ((user.getIdLehrer() != null) && this.reportingRepository.mapLehrerStammdaten().containsKey(user.getIdLehrer())) {
			super.lehrer = new ProxyReportingLehrer(this.reportingRepository, this.reportingRepository.mapLehrerStammdaten().get(user.getIdLehrer()));
			super.anzeigename = super.lehrer.vornameNachname();
		} else {
			try {
				final DTOViewBenutzerdetails dtoBenutzer = this.reportingRepository.conn().queryByKey(DTOViewBenutzerdetails.class, super.id);
				if (dtoBenutzer != null)
					super.anzeigename = dtoBenutzer.AnzeigeName;
			} catch (final Exception ignore) {
				// Bei einem Fehler im Datenbankzugriff fehlt nur der Anzeigename. Daher kann der Fehler ignoriert werden.
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
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}
}
