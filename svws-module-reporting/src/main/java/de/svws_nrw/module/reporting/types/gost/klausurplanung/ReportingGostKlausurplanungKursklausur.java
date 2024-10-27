package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKursklausur.
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
	 * Die Anzahl an Schülern, die an diese Kursklausur schreiben müssen.
	 * @return Anzahl der Schüler
	 */
	public String anzahlSchuelerKlausur() {
		if ((this.schuelerklausuren == null) || this.schuelerklausuren.isEmpty())
			return "00";
		final int anzahl = schuelerklausuren.stream().map(s -> s.id).distinct().toList().size();
		if (anzahl < 10)
			return "0" + anzahl;
		return "" + anzahl;
	}

	/**
	 * Die Anzahl an Schüler, die im Kurs dieser Klausur sind.
	 * @return Anzahl der Schüler
	 */
	public String anzahlSchuelerKurs() {
		if (this.kurs == null)
			return "00";
		final int anzahl = this.kurs.schueler().stream().map(ReportingSchueler::id).distinct().toList().size();
		if (anzahl < 10)
			return "0" + anzahl;
		return "" + anzahl;
	}

	/**
	 * Die Liste aller Namen der regulären Klausurschreiber dieser Kursklausur.
	 * @return	Liste der Klausurschreiber.
	 */
	public List<String> klausurschreiberNamen() {
		if (this.kurs == null)
			return new ArrayList<>();
		return schuelerklausuren.stream().map(s -> s.schueler.vorname() + " " + s.schueler.nachname()).toList();
	}

	/**
	 * Die Liste der Räume, in denen die Schüler des Kurses ihre Klausur schreiben.
	 * @return Die Liste der Räume der Kursklausur.
	 */
	public List<String> raeume() {
		if ((schuelerklausuren == null) || schuelerklausuren.isEmpty())
			return new ArrayList<>();
		// Der erste Termin einer Schülerklausur ist der Termin der Kursklausur (FolgeNr ist 0).
		return schuelerklausuren.stream()
				.filter(s -> (s.klausurtermin != null) && (s.nummerTerminfolge == 0)
						&& (s.klausurraum != null) && (s.klausurraum.raumdaten != null))
				.map(s -> s.klausurraum.raumdaten.kuerzel()).distinct().toList();
	}

	/**
	 * Die Startuhrzeit der Kursklausur, falls schon gesetzt.
	 * @return Die Uhrzeitangabe der Startzeit.
	 */
	public String startuhrzeit() {
		if (this.startzeit == null) {
			if ((klausurtermin != null) && (klausurtermin.startzeit != null))
				return DateUtils.gibZeitStringOfMinuten(klausurtermin.startzeit);
			return "";
		}
		return DateUtils.gibZeitStringOfMinuten(this.startzeit);
	}

	/**
	 * Die Unterrichtsstunden, in denen die Schüler des Kurses ihre Klausur schreiben.
	 * @return Die Unterrichtsstunden der Klausur.
	 */
	public List<Integer> stunden() {
		if ((schuelerklausuren == null) || schuelerklausuren.isEmpty()) {
			return new ArrayList<>();
		}
		// Der erste Termin einer Schülerklausur ist der Termin der Kursklausur (FolgeNr ist 0). Nehme diesen für die Zeiten.
		final List<ReportingGostKlausurplanungSchuelerklausur> klausurenMitRaumUndStunden = schuelerklausuren.stream()
				.filter(s -> (s.klausurtermin != null)
						&& (s.nummerTerminfolge == 0)
						&& (s.klausurraum != null)
						&& (!s.klausurraum.aufsichten.isEmpty()))
				.toList();

		if (!klausurenMitRaumUndStunden.isEmpty())
			return klausurenMitRaumUndStunden.getFirst().klausurraum.aufsichten.stream()
					.map(a -> a.unterrichtsstunde.unterrichtstunde()).toList();
		return new ArrayList<>();
	}

	/**
	 * Die kommaseparierte Liste in Textform aller Namen der regulären Klausurschreiber dieser Kursklausur.
	 * @return	Liste der Klausurschreiber als Text.
	 */
	public String textKlausurschreiberNamen() {
		if (this.kurs == null)
			return "";
		return String.join(", ", klausurschreiberNamen());
	}

	/**
	 * Die kommaseparierte Liste der Räume, in denen die Schüler des Kurses ihre Klausur schreiben.
	 * @return Die Liste der Räume der Kursklausur als Text.
	 */
	public String textRaeume() {
		if ((schuelerklausuren == null) || schuelerklausuren.isEmpty())
			return "";
		return String.join(", ", raeume());
	}

	/**
	 * Die Unterrichtsstunden als Zeitbereich in Textform.
	 * @return Die Unterrichtsstunden der Klausur als Text.
	 */
	public String textStunden() {
		if (!stunden().isEmpty())
			return stunden().getFirst() + "-" + stunden().getLast();
		return "";
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
