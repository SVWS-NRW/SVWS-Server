package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.DateUtils;


/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurtermin.
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
	public long id;

	/** Die Information, ob es sich um einen Haupttermin handelt oder nicht. */
	protected boolean istHaupttermin;

	/** Die Klausurräume dieses Termines, inklusive der Aufsichten für die Unterrichtsstunden der Klausur. */
	protected final List<ReportingGostKlausurplanungKlausurraum> klausurraeume;

	/** Die Liste von Kursklausuren zu diesem Klausurtermin */
	protected List<ReportingGostKlausurplanungKursklausur> kursklausuren;

	/** Die Information, ob es bei einem Haupttermin auch Nachschreibklausuren zugelassen sind oder nicht. */
	protected boolean nachschreiberZugelassen;

	/** Das Quartal, in welchem die Klausur geschrieben wird. */
	protected int quartal;

	/** Die Liste aller Schülerklausuren zu diesem Termin. */
	protected List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren;

	/** Die Startzeit des Klausurtermins in Minuten seit 0 Uhr, falls schon gesetzt. */
	protected Integer startzeit;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param bemerkung					Die textuelle Bemerkung zum Termin, sofern vorhanden.
	 * @param bezeichnung				Die Bezeichnung des Klausurtermins, falls schon gesetzt.
	 * @param datum						Das Datum des Klausurtermins, falls schon gesetzt.
	 * @param gostHalbjahr				Das Gost-Halbjahr, in dem die Klausur geschrieben wird.
	 * @param id						Die ID des Klausurtermins.
	 * @param istHaupttermin			Die Information, ob es sich um einen Haupttermin handelt oder nicht.
	 * @param klausurraeume 			Die Klausurräume dieses Termines, inklusive der Aufsichten für die Unterrichtsstunden der Klausur.
	 * @param kursklausuren				Die Liste von Kursklausuren zu diesem Klausurtermin
	 * @param nachschreiberZugelassen	Die Information, ob es bei einem Haupttermin auch Nachschreibklausuren zugelassen sind oder nicht.
	 * @param quartal					Das Quartal, in welchem die Klausur geschrieben wird.
	 * @param schuelerklausuren			Die Liste aller Schülerklausuren zu diesem Termin.
	 * @param startzeit					Die Startzeit des Klausurtermins in Minuten seit 0 Uhr, falls schon gesetzt.
	 */
	public ReportingGostKlausurplanungKlausurtermin(final String bemerkung, final String bezeichnung, final String datum,
			final GostHalbjahr gostHalbjahr, final long id, final boolean istHaupttermin,
			final List<ReportingGostKlausurplanungKlausurraum> klausurraeume, final List<ReportingGostKlausurplanungKursklausur> kursklausuren,
			final boolean nachschreiberZugelassen, final int quartal, final List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren,
			final Integer startzeit) {
		this.bemerkung = bemerkung;
		this.bezeichnung = bezeichnung;
		this.datum = datum;
		this.gostHalbjahr = gostHalbjahr;
		this.id = id;
		this.istHaupttermin = istHaupttermin;
		this.klausurraeume = klausurraeume;
		this.kursklausuren = kursklausuren;
		this.nachschreiberZugelassen = nachschreiberZugelassen;
		this.quartal = quartal;
		this.schuelerklausuren = schuelerklausuren;
		this.startzeit = startzeit;
	}


	// ##### Berechnete Methoden #####

	/**
	 * Die Liste der Schülerklausuren zum Termin ihres Kurses.
	 * @return Die Schülerklausuren zum Kurstermin.
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausurenKurstermin() {
		if (this.schuelerklausuren.isEmpty())
			return new ArrayList<>();
		return this.schuelerklausuren.stream().filter(k -> k.nummerTerminfolge == 0).toList();
	}

	/**
	 * Die Liste der Schülerklausuren zum Termin die Nachschreibklausuren darstellen.
	 * @return Die Schülerklausuren als Nachschreibklausuren.
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausurenNachschreibtermin() {
		if (this.schuelerklausuren.isEmpty())
			return new ArrayList<>();
		return this.schuelerklausuren.stream().filter(k -> k.nummerTerminfolge > 0).toList();
	}

	/**
	 * Die Startuhrzeit des Klausurtermins, falls schon gesetzt
	 * @return Die Uhrzeitangabe der Startzeit.
	 */
	public String startuhrzeit() {
		if (this.startzeit == null)
			return "";
		return DateUtils.gibZeitStringOfMinuten(this.startzeit);
	}

	/**
	 * Erstellt eine Liste der Klausurräume mit Stunden für den Klausurtermin.
	 * @return Die Angaben zu Räumen und Stunden.
	 */
	public String raeumeUndStunden() {
		final List<String> tempList = new ArrayList<>();
		for (final ReportingGostKlausurplanungKlausurraum raum : klausurraeume) {
			String temp = "";
			if ((raum.raumdaten != null) && (raum.raumdaten.kuerzel() != null)) {
				temp = temp + " " + raum.raumdaten.kuerzel();
			} else if (!raum.aufsichten.isEmpty()) {
				temp = temp + " ???";
			}
			if (!raum.aufsichten.isEmpty()) {
				temp = temp + " (Std. " + raum.aufsichten.getFirst().unterrichtsstunde.unterrichtstunde() + "-"
						+ raum.aufsichten.getLast().unterrichtsstunde.unterrichtstunde() + ")";
			}
			if (!temp.isEmpty())
				tempList.add(temp);
		}
		if (tempList.isEmpty())
			return "";
		return String.join(",", tempList);
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
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Die Information, ob es sich um einen Haupttermin handelt oder nicht.
	 * @return Inhalt des Feldes istHaupttermin
	 */
	public boolean istHaupttermin() {
		return istHaupttermin;
	}

	/**
	 * Die Klausurräume dieses Termines, inklusive der Aufsichten für die Unterrichtsstunden der Klausur.
	 * @return Inhalt des Feldes klausurraeume
	 */
	public List<ReportingGostKlausurplanungKlausurraum> klausurraeume() {
		return klausurraeume;
	}

	/**
	 * Die Liste von Kursklausuren zu diesem Klausurtermin.
	 * @return Inhalt des Feldes kursklausuren
	 */
	public List<ReportingGostKlausurplanungKursklausur> kursklausuren() {
		return kursklausuren;
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

	/**
	 * Die Liste aller Schülerklausuren zu diesem Termin.
	 * @return Inhalt des Feldes schuelerklausuren
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren() {
		return schuelerklausuren;
	}
}
