package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.List;


/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungDatumsgruppeKlausurtermin.</p>
 * <p>Sie gruppiert verschiedene Klausurtermine nach Datum für die Ausgabe der Klausurplanung der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKlausurplanungDatumsgruppeKlausurtermin {

	/** Das Datum der Klausurtermine der Datumsgruppe, falls schon gesetzt. */
	protected String datum;

	/** Eine Liste vom Typ GostKlausurplanungTermin, die alle Termine des Datums der Gruppe beinhaltet. */
	protected List<ReportingGostKlausurplanungKlausurtermin> klausurtermine;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param datum				Das Datum der Klausurtermine der Datumsgruppe, falls schon gesetzt.
	 * @param klausurtermine	Eine Liste vom Typ GostKlausurplanungTermin, die alle Termine des Datums der Gruppe beinhaltet.
	 */
	public ReportingGostKlausurplanungDatumsgruppeKlausurtermin(final String datum, final List<ReportingGostKlausurplanungKlausurtermin> klausurtermine) {
		this.datum = datum;
		this.klausurtermine = klausurtermine;
	}


	// ##### Getter #####

	/**
	 * Das Datum der Klausurtermine der Datumsgruppe, falls schon gesetzt.
	 * @return Inhalt des Feldes datum
	 */
	public String datum() {
		return datum;
	}

	/**
	 * Eine Liste vom Typ GostKlausurplanungTermin, die alle Termine des Datums der Gruppe beinhaltet.
	 * @return Inhalt des Feldes klausurtermine
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermine() {
		return klausurtermine;
	}

}
