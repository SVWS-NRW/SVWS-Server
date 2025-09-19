package de.svws_nrw.module.reporting.proxytypes.schule;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schule.ReportingSchulkatalogEintragNRW;

/**
 * Proxy-Klasse für die Darstellung eines Schulkatalog-Eintrags in NRW für das Reporting.
 * Diese Klasse erweitert die ReportingSchulkatalogEintragNRW und wird über die Übergabe
 * eines SchulEintrag-Objektes gefüllt.
 */
public class ProxyReportingSchulkatalogEintragNRW extends ReportingSchulkatalogEintragNRW {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt eine neue Proxy-Instanz aus einem SchulEintrag-Objekt.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param schulEintrag Das SchulEintrag-Objekt, aus dem die Proxy-Instanz erstellt wird
	 */
	public ProxyReportingSchulkatalogEintragNRW(final ReportingRepository reportingRepository, final SchulEintrag schulEintrag) {
		super(
				createBezeichnungListe(schulEintrag.name),
				schulEintrag.email,
				schulEintrag.fax,
				schulEintrag.hausnummer,
				schulEintrag.ort,
				schulEintrag.plz,
				parseSchulnummer(schulEintrag.schulnummerStatistik),
				schulEintrag.strassenname,
				schulEintrag.telefon,
				schulEintrag.zusatzHausnummer,
				schulEintrag.id,
				schulEintrag.istSichtbar,
				schulEintrag.kuerzel,
				schulEintrag.kurzbezeichnung,
				getSchulform(reportingRepository, schulEintrag.idSchulform),
				schulEintrag.schulleiter,
				schulEintrag.sortierung
		);
		this.reportingRepository = reportingRepository;
	}

	/**
	 * Erstellt eine Liste der Bezeichnungen aus dem Namen der Schule.
	 *
	 * @param name Der Name der Schule
	 * @return Eine Liste mit dem Namen in der ersten Zeile.
	 */
	private static List<String> createBezeichnungListe(final String name) {
		if ((name == null) || name.isEmpty()) {
			return List.of("");
		}

		// Einfache Implementierung: Name als einzelne Zeile
		return List.of(name);
	}

	/**
	 * Konvertiert die Schulnummer von String zu long.
	 * Falls die Schulnummer nicht geparst werden kann, wird 0 zurückgegeben.
	 *
	 * @param schulnummerStatistik Die Schulnummer als String
	 * @return Die Schulnummer als long-Wert
	 */
	private static long parseSchulnummer(final String schulnummerStatistik) {
		if ((schulnummerStatistik == null) || schulnummerStatistik.isEmpty())
			return 0L;

		try {
			return Long.parseLong(schulnummerStatistik);
		} catch (final NumberFormatException e) {
			// Falls die Schulnummer nicht geparst werden kann, 0 zurückgeben
			return 0L;
		}
	}

	/**
	 * Ermittelt die Schulform anhand der ID aus dem Repository.
	 *
	 * @param reportingRepository Das Repository mit den Katalogdaten
	 * @param idSchulform        Die ID der Schulform
	 * @return Die ermittelte Schulform oder null
	 */
	private static SchulformKatalogEintrag getSchulform(final ReportingRepository reportingRepository, final Long idSchulform) {
		if (idSchulform == null)
			return null;
		return reportingRepository.katalogSchulformen().get(idSchulform);
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
}
