package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.DateUtils;


/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurtermin.</p>
 * <p>Sie enthält die Daten zu einem Termine einer Klausur der Klausurplanung der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKlausurplanungKlausurtermin {

	/** Die textuelle Bemerkung zum Termin, sofern vorhanden. */
	protected String bemerkung;

	/** Die Bezeichnung des Klausurtermins, falls schon gesetzt. */
	protected String bezeichnung;

	/** Das Datum des Klausurtermins, falls schon gesetzt. */
	protected String datum;

	/** Das Gost-Halbjahr, in dem die Klausur geschrieben wird. */
	protected GostHalbjahr gostHalbjahr;

	/** Die ID des Klausurtermins. */
	public long idKlausurtermin;

	/** Die Information, ob es sich um einen Haupttermin handelt oder nicht. */
	protected boolean istHaupttermin;

	/** Die Information, ob es bei einem Haupttermin auch Nachschreibklausuren zugelassen sind oder nicht. */
	protected boolean nachschreiberZugelassen;

	/** Das Quartal, in welchem die Klausur geschrieben wird. */
	protected int quartal;

	/** Die Startzeit des Klausurtermins in Minuten seit 0 Uhr, falls schon gesetzt. */
	protected int startzeit;

	//protected final List<ReportingGostKlausurplanungKursklausur> kursklausuren;

	//protected final List<ReportingGostKlausurplanungKlausurraum> klausurraeume;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param bemerkung					Die textuelle Bemerkung zum Termin, sofern vorhanden.
	 * @param bezeichnung				Die Bezeichnung des Klausurtermins, falls schon gesetzt.
	 * @param datum						Das Datum des Klausurtermins, falls schon gesetzt.
	 * @param gostHalbjahr				Das Gost-Halbjahr, in dem die Klausur geschrieben wird.
	 * @param idKlausurtermin			Die ID des Klausurtermins.
	 * @param istHaupttermin			Die Information, ob es sich um einen Haupttermin handelt oder nicht.
	 * @param nachschreiberZugelassen	Die Information, ob es bei einem Haupttermin auch Nachschreibklausuren zugelassen sind oder nicht.
	 * @param quartal					Das Quartal, in welchem die Klausur geschrieben wird.
	 * @param startzeit					Die Startzeit des Klausurtermins in Minuten seit 0 Uhr, falls schon gesetzt.
	 */
	public ReportingGostKlausurplanungKlausurtermin(final String bemerkung, final String bezeichnung, final String datum,
			final GostHalbjahr gostHalbjahr, final long idKlausurtermin, final boolean istHaupttermin, final boolean nachschreiberZugelassen, final int quartal,
			final int startzeit) {
		this.bemerkung = bemerkung;
		this.bezeichnung = bezeichnung;
		this.datum = datum;
		this.gostHalbjahr = gostHalbjahr;
		this.idKlausurtermin = idKlausurtermin;
		this.istHaupttermin = istHaupttermin;
		this.nachschreiberZugelassen = nachschreiberZugelassen;
		this.quartal = quartal;
		this.startzeit = startzeit;
	}


	// ##### Berechnete Methoden #####

	/**
	 * Die Startuhrzeit des Klausurtermins, falls schon gesetzt
	 * @return Die Uhrzeitangabe der Startzeit.
	 */
	public String startuhrzeit() {
		return DateUtils.gibZeitStringOfMinuten(this.startzeit);
	}


	// ##### Getter #####

	/**
	 * Die textuelle Bemerkung zum Termin, sofern vorhanden.
	 * @return Inhalt des Feldes bemerkung
	 */
	public String bemerkung() {
		return bemerkung;
	}

	/**
	 * Die Bezeichnung des Klausurtermins, falls schon gesetzt.
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Das Datum des Klausurtermins, falls schon gesetzt.
	 * @return Inhalt des Feldes datum
	 */
	public String datum() {
		return datum;
	}

	/**
	 * Das Gost-Halbjahr, in dem die Klausur geschrieben wird.
	 * @return Inhalt des Feldes gostHalbjahr
	 */
	public GostHalbjahr gostHalbjahr() {
		return gostHalbjahr;
	}

	/**
	 * Die ID des Klausurtermins.
	 * @return Inhalt des Feldes idKlausurtermin
	 */
	public long idKlausurtermin() {
		return idKlausurtermin;
	}

	/**
	 * Die Information, ob es sich um einen Haupttermin handelt oder nicht.
	 * @return Inhalt des Feldes istHaupttermin
	 */
	public boolean istHaupttermin() {
		return istHaupttermin;
	}

	/**
	 * Die Information, ob es bei einem Haupttermin auch Nachschreibklausuren zugelassen sind oder nicht.
	 * @return Inhalt des Feldes nachschreiberZugelassen
	 */
	public boolean nachschreiberZugelassen() {
		return nachschreiberZugelassen;
	}

	/**
	 * Das Quartal, in welchem die Klausur geschrieben wird.
	 * @return Inhalt des Feldes quartal
	 */
	public int quartal() {
		return quartal;
	}

}
