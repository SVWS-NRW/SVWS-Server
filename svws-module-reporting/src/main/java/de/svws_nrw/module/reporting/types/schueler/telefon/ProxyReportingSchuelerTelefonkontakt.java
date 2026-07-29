package de.svws_nrw.module.reporting.types.schueler.telefon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.schueler.SchuelerTelefon;
import de.svws_nrw.module.reporting.repositories.ReportingContext;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungKursbelegung und erweitert die Klasse {@link ReportingSchuelerTelefonkontakt}.
 */
public class ProxyReportingSchuelerTelefonkontakt extends ReportingSchuelerTelefonkontakt {

	@JsonIgnore
	private final ReportingContext reportingContext;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuelerTelefonkontakt}.
	 *
	 * @param reportingContext	Repository für Reporting-Daten
	 * @param schuelerTelefon		Das SchuelerTelefon-Objekt mit den Telefondaten
	 */
	public ProxyReportingSchuelerTelefonkontakt(final ReportingContext reportingContext, final SchuelerTelefon schuelerTelefon) {
		super(
				ersetzeNullBlankTrim(schuelerTelefon.bemerkung),
				getBezeichnungFuerTelefonArt(reportingContext, schuelerTelefon.idTelefonArt),
				schuelerTelefon.istGesperrt,
				schuelerTelefon.sortierung,
				ersetzeNullBlankTrim(schuelerTelefon.telefonnummer)
		);
		this.reportingContext = reportingContext;
	}

	/**
	 * Ermittelt die Bezeichnung für eine Telefon-Art anhand der ID aus dem Repository.
	 *
	 * @param reportingContext	Das Repository mit den Katalogdaten
	 * @param idTelefonArt			Die ID der Telefon-Art
	 * @return						Die Bezeichnung der Telefon-Art oder ein Leerstring
	 */
	private static String getBezeichnungFuerTelefonArt(final ReportingContext reportingContext, final long idTelefonArt) {
		final var telefonArt = reportingContext.repositoryKataloge().telefonnummerArt(idTelefonArt);
		if (telefonArt == null) {
			return "";
		}
		return ersetzeNullBlankTrim(telefonArt.bezeichnung);
	}

}
