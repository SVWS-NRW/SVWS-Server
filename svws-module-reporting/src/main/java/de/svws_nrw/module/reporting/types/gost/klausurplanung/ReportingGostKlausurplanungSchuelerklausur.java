package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung.ProxyReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung.ProxyReportingGostKlausurplanungKursklausur;
import de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung.ProxyReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKursklausur.</p>
 * <p>Sie enthält die Daten zu einer Klausur eines Kurses der Klausurplanung der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKlausurplanungSchuelerklausur {

	private ProxyReportingGostKlausurplanungSchuelerklausur proxy;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param proxy
	 */
	public ReportingGostKlausurplanungSchuelerklausur(final ProxyReportingGostKlausurplanungSchuelerklausur proxy) {
		this.proxy = proxy;
	}


	// ##### Berechnete Methoden #####

	/**
	 * Die Startuhrzeit der Schülerklausur, falls schon gesetzt.
	 * @return Die Uhrzeitangabe der Startzeit.
	 */
	public String startuhrzeit() {
		if (this.startzeit == null) {
			if ((klausurtermin != null) && (klausurtermin.startzeit != null))
				return DateUtils.gibZeitStringOfMinuten(klausurtermin.startzeit);
			else
				return "";
		} else
			return DateUtils.gibZeitStringOfMinuten(this.startzeit);
	}


	// ##### Getter #####

	/**
	 * Die textuelle Bemerkung zur Schülerklausur, sofern vorhanden.
	 * @return Inhalt des Feldes bemerkung
	 */
	public String bemerkung() {
		return proxy.bemerkung;
	}

	/**
	 * Die ID der Schülerklausur.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return proxy.id;
	}

	/**
	 * Der Klausurraum dieses Schülerklausurtermines, inklusive der Aufsichten für die Unterrichtsstunden der Klausur.
	 * @return Inhalt des Feldes klausurraum
	 */
	public ReportingGostKlausurplanungKlausurraum klausurraum() {
		return klausurraum;
	}

	/**
	 * Der Termin der Schülerklausur aus den Klausurterminen.
	 * @return Inhalt des Feldes klausurtermin
	 */
	public ReportingGostKlausurplanungKlausurtermin klausurtermin() {
		return klausurtermin;
	}

	/**
	 * Die Kursklausur, die zu dieser Schülerklausur geführt hat. Deren Vorgaben gelten auch für die Schülerklausur.
	 * @return Inhalt des Feldes kursklausur
	 */
	public ReportingGostKlausurplanungKursklausur kursklausur() {
		return kursklausur;
	}

	/**
	 * Die Nummer des Termins in der Folge der angesetzten Termine für den Schüler bzgl. der Kursklausur (0 = Kursklausur, 1 = Erste Nachschreibtermin, usw.)
	 * @return Inhalt des Feldes nummerTerminfolge
	 */
	public int nummerTerminfolge() {
		return nummerTerminfolge;
	}

	/**
	 * Der Schüler dieser Schülerklausur.
	 * @return Inhalt des Feldes schueler
	 */
	public ReportingSchueler schueler() {
		return schueler;
	}
}
