package de.svws_nrw.module.reporting.types.schueler.telefon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.schueler.SchuelerTelefon;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungKursbelegung und erweitert die Klasse {@link ReportingSchuelerTelefonkontakt}.
 */
public class ProxyReportingSchuelerTelefonkontakt extends ReportingSchuelerTelefonkontakt {

	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuelerTelefonkontakt}.
	 *
	 * @param reportingRepository	Repository für Reporting-Daten
	 * @param schuelerTelefon		Das SchuelerTelefon-Objekt mit den Telefondaten
	 */
	public ProxyReportingSchuelerTelefonkontakt(final ReportingRepository reportingRepository, final SchuelerTelefon schuelerTelefon) {
		super(
				ersetzeNullBlankTrim(schuelerTelefon.bemerkung),
				getBezeichnungFuerTelefonArt(reportingRepository, schuelerTelefon.idTelefonArt),
				schuelerTelefon.istGesperrt,
				schuelerTelefon.sortierung,
				ersetzeNullBlankTrim(schuelerTelefon.telefonnummer)
		);
		this.reportingRepository = reportingRepository;
	}

	/**
	 * Ermittelt die Bezeichnung für eine Telefon-Art anhand der ID aus dem Repository.
	 *
	 * @param reportingRepository	Das Repository mit den Katalogdaten
	 * @param idTelefonArt			Die ID der Telefon-Art
	 * @return						Die Bezeichnung der Telefon-Art oder ein Leerstring
	 */
	private static String getBezeichnungFuerTelefonArt(final ReportingRepository reportingRepository, final long idTelefonArt) {
		if (reportingRepository.katalogTelefonnummerArten().containsKey(idTelefonArt)) {
			return ersetzeNullBlankTrim(reportingRepository.katalogTelefonnummerArten().get(idTelefonArt).bezeichnung);
		}
		return "";
	}

}
