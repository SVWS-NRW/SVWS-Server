package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import de.svws_nrw.core.utils.DateUtils;


/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungSchuelerklausurtermin.</p>
 * <p>Sie enthält die Daten zu einem Termine einer Klausur der Klausurplanung der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKlausurplanungSchuelerklausurtermin {

	/** Die textuelle Bemerkung zum Schülerklausurtermin, sofern vorhanden. */
	protected String bemerkung;

	/** Die ID des Schülerklausurtermins. */
	public long id;

	/** Der Klausurraum dieses Schülerklausurtermines, inklusive der Aufsichten für die Unterrichtsstunden der Klausur. */
	protected final ReportingGostKlausurplanungKlausurraum klausurraum;

	/** Der Termin der Schülerklausur aus den Klausurterminen. */
	protected ReportingGostKlausurplanungKlausurtermin klausurtermin;

	/** Die Nummer des Termins in der Folge der angesetzten Termine für den Schüler bzgl. der Kursklausur (0 = Kursklausur, 1 = Erste Nachschreibtermin, usw.) */
	protected int nummerTerminfolge;

	/** Die Startzeit des Schülerklausurtermins in Minuten seit 0 Uhr, falls schon gesetzt. */
	protected Integer startzeit;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param bemerkung			Die textuelle Bemerkung zum Schülerklausurtermin, sofern vorhanden.
	 * @param id				Die ID des Schülerklausurtermins.
	 * @param klausurraum 		Der Klausurraum dieses Schülerklausurtermines, inklusive der Aufsichten für die Unterrichtsstunden der Klausur.
	 * @param klausurtermin 	Der Termin der Schülerklausur aus den Klausurterminen.
	 * @param nummerTerminfolge	Die Nummer des Termins in der Folge der angesetzten Termine für den Schüler bzgl. der Kursklausur (0 = Kursklausur, usw.)
	 * @param startzeit			Die Startzeit des Schülerklausurtermins in Minuten seit 0 Uhr, falls schon gesetzt.
	 */
	public ReportingGostKlausurplanungSchuelerklausurtermin(final String bemerkung, final long id, final ReportingGostKlausurplanungKlausurtermin klausurtermin,
			final ReportingGostKlausurplanungKlausurraum klausurraum, final int nummerTerminfolge, final Integer startzeit) {
		this.bemerkung = bemerkung;
		this.id = id;
		this.klausurraum = klausurraum;
		this.klausurtermin = klausurtermin;
		this.nummerTerminfolge = nummerTerminfolge;
		this.startzeit = startzeit;
	}


	// ##### Berechnete Methoden #####

	/**
	 * Die Startuhrzeit des Klausurtermins, falls schon gesetzt
	 * @return Die Uhrzeitangabe der Startzeit.
	 */
	public String startuhrzeit() {
		if (this.startzeit == null) {
			if (klausurtermin != null)
				return DateUtils.gibZeitStringOfMinuten(klausurtermin.startzeit);
			else
				return "";
		} else
			return DateUtils.gibZeitStringOfMinuten(this.startzeit);
	}


	// ##### Getter #####

	/**
	 * Die textuelle Bemerkung zum Schülerklausurtermin, sofern vorhanden.
	 * @return Inhalt des Feldes bemerkung
	 */
	public String bemerkung() {
		return bemerkung;
	}

	/**
	 * Die ID des Schülerklausurtermins.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
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
	 * Die Nummer des Termins in der Folge der angesetzten Termine für den Schüler bzgl. der Kursklausur (0 = Kursklausur, 1 = Erste Nachschreibtermin, usw.)
	 * @return Inhalt des Feldes nummerTerminfolge
	 */
	public int nummerTerminfolge() {
		return nummerTerminfolge;
	}
}
