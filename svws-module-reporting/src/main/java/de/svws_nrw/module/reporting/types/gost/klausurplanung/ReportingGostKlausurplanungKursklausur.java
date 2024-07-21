package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.List;

import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKursklausur.</p>
 * <p>Sie enthält die Daten zu einer Klausur eines Kurses der Klausurplanung der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKlausurplanungKursklausur {

	/** Die Auswahlzeit in Minuten, sofern vorhanden. */
	protected int auswahlzeit;

	/** Die textuelle Bemerkung zur Kursklausur, sofern vorhanden. */
	protected String bemerkung;

	/** Die Dauer der Klausur in Minuten. */
	protected int dauer;

	/** Die ID der Kursklausur. */
	protected long id;

	/** Die Information, ob Audioequipment nötig ist, z.B. für Klausuren mit Hörverstehensanteilen. */
	protected boolean istAudioNotwendig;

	/** Die Information, ob es sich um eine mündliche Prüfung handelt. */
	protected boolean istMdlPruefung;

	/** Die Information, ob Videoequipment nötig ist, z.B. für Klausuren mit Videoanalyse. */
	protected boolean istVideoNotwendig;

	/** Der Termin aus der Klausurplanung, an dem diese Kursklausur stattfindet. */
	protected ReportingGostKlausurplanungKlausurtermin klausurtermin;

	/** Die Liste der Schülerklausuren zu dieser Klausur. */
	protected List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren;

	/** Der Kurs, indem die Klausur geschrieben wird, mit seinen Daten. */
	protected ReportingKurs kurs;

	/** Die Startzeit der Klausur in Minuten seit 0 Uhr, wenn abweichend vom Klausurtermin, sonst null. */
	protected Integer startzeit;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param auswahlzeit		Die Auswahlzeit in Minuten, sofern vorhanden.
	 * @param bemerkung			Die textuelle Bemerkung zur Kursklausur, sofern vorhanden.
	 * @param dauer				Die Dauer der Klausur in Minuten.
	 * @param id				Die ID der Kursklausur.
	 * @param istAudioNotwendig	Die Information, ob Audioequipment nötig ist, z.B. für Klausuren mit Hörverstehensanteilen.
	 * @param istMdlPruefung	Die Information, ob es sich um eine mündliche Prüfung handelt.
	 * @param istVideoNotwendig	Die Information, ob Videoequipment nötig ist, z.B. für Klausuren mit Videoanalyse.
	 * @param klausurtermin		Der Termin aus der Klausurplanung, an dem diese Kursklausur stattfindet.
	 * @param kurs				Der Kurs, indem die Klausur geschrieben wird, mit seinen Daten.
	 * @param schuelerklausuren	Die Liste der Schülerklausuren zu dieser Klausur.
	 * @param startzeit			Die Startzeit der Klausur in Minuten seit 0 Uhr, wenn abweichend vom Klausurtermin, sonst null.
	 */
	public ReportingGostKlausurplanungKursklausur(final int auswahlzeit, final String bemerkung, final int dauer, final long id,
			final boolean istAudioNotwendig, final boolean istMdlPruefung,
			final boolean istVideoNotwendig, final ReportingGostKlausurplanungKlausurtermin klausurtermin, final ReportingKurs kurs,
			final List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren, final Integer startzeit) {
		this.auswahlzeit = auswahlzeit;
		this.bemerkung = bemerkung;
		this.dauer = dauer;
		this.id = id;
		this.istAudioNotwendig = istAudioNotwendig;
		this.istMdlPruefung = istMdlPruefung;
		this.istVideoNotwendig = istVideoNotwendig;
		this.klausurtermin = klausurtermin;
		this.kurs = kurs;
		this.schuelerklausuren = schuelerklausuren;
		this.startzeit = startzeit;
	}


	// ##### Berechnete Methoden #####

	/**
	 * Die Anzahl an Schüler, die an dieser Kursklausur teilnehmen.
	 * @return Anzahl der Schüler
	 */
	public String anzahlSchuelerKlausur() {
		if ((this.schuelerklausuren == null) || this.schuelerklausuren.isEmpty()) {
			return "00";
		} else {
			final int anzahl = schuelerklausuren.stream().map(s -> s.id).distinct().toList().size();
			if (anzahl < 10)
				return "0" + anzahl;
			else
				return "" + anzahl;
		}
	}

	/**
	 * Die Anzahl an Schüler, die im Kurs dieser Klausur sind.
	 * @return Anzahl der Schüler
	 */
	public String anzahlSchuelerKurs() {
		if (this.kurs == null) {
			return "00";
		} else {
			final int anzahl = this.kurs.schueler().stream().map(ReportingSchueler::id).distinct().toList().size();
			if (anzahl < 10)
				return "0" + anzahl;
			else
				return "" + anzahl;
		}
	}



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
	 * Die Auswahlzeit in Minuten, sofern vorhanden.
	 * @return Inhalt des Feldes auswahlzeit
	 */
	public int auswahlzeit() {
		return auswahlzeit;
	}

	/**
	 * Die textuelle Bemerkung zur Kursklausur, sofern vorhanden.
	 * @return Inhalt des Feldes bemerkung
	 */
	public String bemerkung() {
		return bemerkung;
	}

	/**
	 * Die Dauer der Klausur in Minuten.
	 * @return Inhalt des Feldes dauer
	 */
	public int dauer() {
		return dauer;
	}

	/**
	 * Die ID der Kursklausur.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Die Information, ob Audioequipment nötig ist, z.B. für Klausuren mit Hörverstehensanteilen.
	 * @return Inhalt des Feldes istAudioNotwendig
	 */
	public boolean istAudioNotwendig() {
		return istAudioNotwendig;
	}

	/**
	 * Die Information, ob es sich um eine mündliche Prüfung handelt.
	 * @return Inhalt des Feldes istMdlPruefung
	 */
	public boolean istMdlPruefung() {
		return istMdlPruefung;
	}

	/**
	 * Die Information, ob Videoequipment nötig ist, z.B. für Klausuren mit Videoanalyse.
	 * @return Inhalt des Feldes istVideoNotwendig
	 */
	public boolean istVideoNotwendig() {
		return istVideoNotwendig;
	}

	/**
	 * Der Termin aus der Klausurplanung, an dem diese Kursklausur stattfindet.
	 * @return Inhalt des Feldes klausurtermin
	 */
	public ReportingGostKlausurplanungKlausurtermin klausurtermin() {
		return klausurtermin;
	}

	/**
	 * Der Kurs, indem die Klausur geschrieben wird, mit seinen Daten.
	 * @return Inhalt des Feldes kurs
	 */
	public ReportingKurs kurs() {
		return kurs;
	}

	/**
	 * Die Liste der Schüler aus dem Kurs, die diese Klausur schreiben.
	 * @return Inhalt des Feldes klausurschreiber
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren() {
		return schuelerklausuren;
	}

	/**
	 * Die Startzeit der Klausur in Minuten seit 0 Uhr, wenn abweichend vom Klausurtermin, sonst null.
	 * @return Inhalt des Feldes startzeit
	 */
	public Integer startzeit() {
		return startzeit;
	}
}
