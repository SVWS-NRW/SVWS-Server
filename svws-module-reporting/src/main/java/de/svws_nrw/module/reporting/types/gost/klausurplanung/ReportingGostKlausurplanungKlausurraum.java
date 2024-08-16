package de.svws_nrw.module.reporting.types.gost.klausurplanung;


import java.util.List;

import de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung.ProxyReportingGostKlausurplanungKlausurraum;
import de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung.ProxyReportingGostKlausurplanungKursklausur;
import de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung.ProxyReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungRaum;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Raum in der GOSt-Klausurplanung.</p>
 * <p>Sie enthält dei Daten des Raumes gemäß Stundenplan und Termin- und Aufsichtsangaben der Klausur.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKlausurplanungKlausurraum {

	private ProxyReportingGostKlausurplanungKlausurraum proxy;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param proxy
	 */
	public ReportingGostKlausurplanungKlausurraum(final ProxyReportingGostKlausurplanungKlausurraum proxy) {
		this.proxy = proxy;
	}


	// ##### Getter #####

	/**
	 * Die Bemerkung zum Raum für die Klausur.
	 * @return Inhalt des Feldes bemerkung
	 */
	public String bemerkung() {
		return proxy.bemerkung;
	}

	/**
	 * Die ID des Raumes in der Klausurplanung, genauer die ID für die Kombination aus Raum der Schule und Klausurtermin.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return proxy.id;
	}

	/**
	 * Der Klausurtermin, dem dieser Raum mit seinen Aufischten zugeordnet wurde.
	 * @return Inhalt des Feldes klausurtermin
	 */
	public ReportingGostKlausurplanungKlausurtermin klausurtermin() {
		return klausurtermin;
	}

	/**
	 * Die Daten des Raumes gemäß dem zum Klausurtermin gültigen Stundenplanes.
	 * @return Inhalt des Feldes raumdaten
	 */
	public ReportingStundenplanungRaum raumdaten() {
		return raumdaten;
	}
}
