package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKursklausur.
 */
public class ReportingGostKlausurplanungSchuelerklausur {

	/** Die textuelle Bemerkung zur Schülerklausur, sofern vorhanden. */
	protected String bemerkung;

	/** Die ID der Schülerklausur. */
	protected long id;

	/** Die ID des Schülerklausurtermins. */
	protected long idSchuelerklausurtermin;

	/** Der Klausurraum dieses Schülerklausurtermins, inklusive der Aufsichten für die Unterrichtsstunden der Klausur. */
	protected final ReportingGostKlausurplanungKlausurraum klausurraum;

	/** Der Termin der Schülerklausur aus den Klausurterminen. */
	protected ReportingGostKlausurplanungKlausurtermin klausurtermin;

	/** Die Kursklausur, die zu dieser Schülerklausur geführt hat. Deren Vorgaben gelten auch für die Schülerklausur. */
	protected ReportingGostKlausurplanungKursklausur kursklausur;

	/** Die Nummer des Termins in der Folge der angesetzten Termine für den Schüler bezüglich der Kursklausur (0 = Kursklausur, 1 = Erste Nachschreibtermin, usw.) */
	protected int nummerTerminfolge;

	/** Der Schüler dieser Schülerklausur. */
	protected ReportingSchueler schueler;

	/** Die Startzeit des Schülerklausurtermins in Minuten seit 0 Uhr, falls schon gesetzt. */
	protected Integer startzeit;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param bemerkung					Die textuelle Bemerkung zur Schülerklausur, sofern vorhanden.
	 * @param id						Die ID der Schülerklausur.
	 * @param idSchuelerklausurtermin	Die ID des Schülerklausurtermins.
	 * @param klausurraum				Der Klausurraum dieses Schülerklausurtermins, inklusive der Aufsichten für die Unterrichtsstunden der Klausur.
	 * @param klausurtermin				Der Termin der Schülerklausur aus den Klausurterminen.
	 * @param kursklausur				Die Kursklausur, die zu dieser Schülerklausur geführt hat. Deren Vorgaben gelten auch für die Schülerklausur.
	 * @param nummerTerminfolge			Die Nummer des Termins in der Folge der angesetzten Termine für den Schüler bzgl. der Kursklausur (0 = Kursklausur, usw.)
	 * @param schueler					Der Schüler dieser Schülerklausur.
	 * @param startzeit					Die Startzeit des Schülerklausurtermins in Minuten seit 0 Uhr, falls schon gesetzt.
	 */
	public ReportingGostKlausurplanungSchuelerklausur(final String bemerkung, final long id,
			final long idSchuelerklausurtermin, final ReportingGostKlausurplanungKlausurraum klausurraum,
			final ReportingGostKlausurplanungKlausurtermin klausurtermin, final ReportingGostKlausurplanungKursklausur kursklausur,
			final int nummerTerminfolge, final ReportingSchueler schueler, final Integer startzeit) {
		this.klausurraum = klausurraum;
		this.bemerkung = bemerkung;
		this.id = id;
		this.idSchuelerklausurtermin = idSchuelerklausurtermin;
		this.klausurtermin = klausurtermin;
		this.kursklausur = kursklausur;
		this.nummerTerminfolge = nummerTerminfolge;
		this.schueler = schueler;
		this.startzeit = startzeit;
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
		return bemerkung;
	}

	/**
	 * Die ID der Schülerklausur.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Die ID des Schülerklausurtermins.
	 * @return Inhalt des Feldes idSchuelerklausurtermin
	 */
	public long idSchuelerklausurtermin() {
		return idSchuelerklausurtermin;
	}

	/**
	 * Der Klausurraum dieses Schülerklausurtermins, inklusive der Aufsichten für die Unterrichtsstunden der Klausur.
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
	 * Die Nummer des Termins in der Folge der angesetzten Termine für den Schüler bezüglich der Kursklausur (0 = Kursklausur, 1 = Erste Nachschreibtermin, usw.)
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
