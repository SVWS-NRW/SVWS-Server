package de.svws_nrw.module.reporting.types.gost.kursplanung;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

import java.util.List;
import java.util.Map;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungBlockungsergebnis.</p>
 * <p>Sie enthält die Daten zu einem Blockungsergebnis, d. h. zu den Anzahlen, den Schienen und Kursen.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKursplanungBlockungsergebnis extends ReportingBaseType {

	/** Das Kalenderjahr, in dem die Abiturprüfung des Blockungsergebnisses stattfindet */
	protected int abiturjahr;

	/** Anzahl der Dummy-Schüler im Ergebnis */
	protected int anzahlDummy;

	/** Anzahl der externen Schüler im Ergebnis */
	protected int anzahlExterne;

	/** Maximale Anzahl an Kursen pro Schiene über alle Schienen */
	protected int anzahlMaxKurseProSchiene;

	/** Anzahl der Schienen */
	protected int anzahlSchienen;

	/** Anzahl der Schüler im Ergebnis */
	protected int anzahlSchueler;

	/** Bezeichnung des Blockungsergebnisses */
	protected String bezeichnung;

	/** Map mit den Fachwahlstatistiken des GOSt-Halbjahres des Blockungsergebnisses zur Fach-ID */
	protected Map<Long, ReportingGostKursplanungFachwahlstatistik> fachwahlstatistik;

	/** Das Halbjahr der gymnasialen Oberstufe des Blockungsergebnisses */
	protected GostHalbjahr gostHalbjahr;

	/** ID des Blockungsergebnisses */
	protected long id;

	/** Eine Liste vom Typ Kurs, die alle Kurse des Blockungsergebnisses beinhaltet. */
	protected List<ReportingGostKursplanungKurs> kurse;

	/** Eine Liste vom Typ Schiene, die alle Schienen des Blockungsergebnisses beinhaltet. */
	protected List<ReportingGostKursplanungSchiene> schienen;

	/** Eine Liste vom Typ Schüler, die alle Schüler des Blockungsergebnisses beinhaltet. */
	protected List<ReportingSchueler> schueler;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param abiturjahr Das Kalenderjahr, in dem die Abiturprüfung des Blockungsergebnisses stattfindet
	 * @param anzahlDummy Anzahl der Dummy-Schüler im Ergebnis
	 * @param anzahlExterne Anzahl der externen Schüler im Ergebnis
	 * @param anzahlMaxKurseProSchiene Maximale Anzahl an Kursen pro Schiene über alle Schienen
	 * @param anzahlSchienen Anzahl der Schienen
	 * @param anzahlSchueler Anzahl der Schüler im Ergebnis
	 * @param bezeichnung Bezeichnung des Blockungsergebnisses
	 * @param fachwahlstatistik Map mit den Fachwahlstatistiken des GOSt-Halbjahres des Blockungsergebnisses zur Fach-ID
	 * @param gostHalbjahr Das Halbjahr der gymnasialen Oberstufe des Blockungsergebnisses
	 * @param id ID des Blockungsergebnisses
	 * @param kurse Eine Liste vom Typ Kurs, die alle Kurse des Blockungsergebnisses beinhaltet.
	 * @param schienen Eine Liste vom Typ Schiene, die alle Schienen des Blockungsergebnisses beinhaltet.
	 * @param schueler Eine Liste vom Typ Schüler, die alle Schüler des Blockungsergebnisses beinhaltet.
	 */
	public ReportingGostKursplanungBlockungsergebnis(final int abiturjahr, final int anzahlDummy, final int anzahlExterne, final int anzahlMaxKurseProSchiene,
			final int anzahlSchienen, final int anzahlSchueler, final String bezeichnung,
			final Map<Long, ReportingGostKursplanungFachwahlstatistik> fachwahlstatistik, final GostHalbjahr gostHalbjahr, final long id,
			final List<ReportingGostKursplanungKurs> kurse, final List<ReportingGostKursplanungSchiene> schienen, final List<ReportingSchueler> schueler) {
		this.abiturjahr = abiturjahr;
		this.anzahlDummy = anzahlDummy;
		this.anzahlExterne = anzahlExterne;
		this.anzahlMaxKurseProSchiene = anzahlMaxKurseProSchiene;
		this.anzahlSchienen = anzahlSchienen;
		this.anzahlSchueler = anzahlSchueler;
		this.bezeichnung = bezeichnung;
		this.fachwahlstatistik = fachwahlstatistik;
		this.gostHalbjahr = gostHalbjahr;
		this.id = id;
		this.kurse = kurse;
		this.schienen = schienen;
		this.schueler = schueler;
	}


	// ##### Berechnete Methoden #####

	/**
	 * Gibt eine Liste mit Kursen zurück, deren IDs in der Filterliste enthalten sind.
	 *
	 * @param idsKurseFilter Die IDs der Kurse, die zurückgegebenen werden sollen.
	 *
	 * @return Die Liste der Kurse, die in der Filterliste enthalten waren.
	 */
	@JsonIgnore
	public List<ReportingGostKursplanungKurs> kurseGefiltert(final List<Long> idsKurseFilter) {
		return kurse.stream().filter(k -> idsKurseFilter.contains(k.id())).toList();
	}

	/**
	 * Gibt eine Liste mit Schülern zurück, deren IDs in der Filterliste enthalten sind.
	 *
	 * @param idsSchuelerFilter Die IDs der Schüler, die zurückgegebenen werden sollen.
	 *
	 * @return Die Liste der Schüler, die in der Filterliste enthalten waren.
	 */
	@JsonIgnore
	public List<ReportingSchueler> schuelerGefiltert(final List<Long> idsSchuelerFilter) {
		return schueler.stream().filter(s -> idsSchuelerFilter.contains(s.id())).toList();
	}



	// ##### Getter #####

	/**
	 * Das Kalenderjahr, in dem die Abiturprüfung des Blockungsergebnisses stattfindet
	 *
	 * @return Inhalt des Feldes abiturjahr
	 */
	public int abiturjahr() {
		return abiturjahr;
	}

	/**
	 * Anzahl der Dummy-Schüler im Ergebnis
	 *
	 * @return Inhalt des Feldes anzahlDummy
	 */
	public int anzahlDummy() {
		return anzahlDummy;
	}

	/**
	 * Anzahl der externen Schüler im Ergebnis
	 *
	 * @return Inhalt des Feldes anzahlExterne
	 */
	public int anzahlExterne() {
		return anzahlExterne;
	}

	/**
	 * Maximale Anzahl an Kursen über alle Schienen
	 *
	 * @return Inhalt des Feldes anzahlMaxKurseProSchiene
	 */
	public int anzahlMaxKurseProSchiene() {
		return anzahlMaxKurseProSchiene;
	}

	/**
	 * Anzahl der Schienen
	 *
	 * @return Inhalt des Feldes anzahlSchienen
	 */
	public int anzahlSchienen() {
		return anzahlSchienen;
	}

	/**
	 * Anzahl der Schüler im Ergebnis
	 *
	 * @return Inhalt des Feldes anzahlSchueler
	 */
	public int anzahlSchueler() {
		return anzahlSchueler;
	}

	/**
	 * Map mit den Fachwahlstatistiken des GOSt-Halbjahres des Blockungsergebnisses zur Fach-ID
	 *
	 * @return Inhalt des Feldes fachwahlstatistik
	 */
	public Map<Long, ReportingGostKursplanungFachwahlstatistik> fachwahlstatistik() {
		return fachwahlstatistik;
	}

	/**
	 * Bezeichnung des Blockungsergebnisses
	 *
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Das Halbjahr der gymnasialen Oberstufe des Blockungsergebnisses
	 *
	 * @return Inhalt des Feldes gostHalbjahr
	 */
	public GostHalbjahr gostHalbjahr() {
		return gostHalbjahr;
	}

	/**
	 * ID des Blockungsergebnisses
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Eine Liste vom Typ Kurs, die alle Kurse des Blockungsergebnisses beinhaltet.
	 *
	 * @return Inhalt des Feldes kurse
	 */
	public List<ReportingGostKursplanungKurs> kurse() {
		return kurse;
	}

	/**
	 * Eine Liste vom Typ Schiene, die alle Schienen des Blockungsergebnisses beinhaltet.
	 *
	 * @return Inhalt des Feldes schienen
	 */
	public List<ReportingGostKursplanungSchiene> schienen() {
		return schienen;
	}

	/**
	 * Eine Liste vom Typ Schüler, die alle Schüler des Blockungsergebnisses beinhaltet.
	 *
	 * @return Inhalt des Feldes schienen
	 */
	public List<ReportingSchueler> schueler() {
		return schueler;
	}

}
