package de.svws_nrw.module.reporting.types.gost.kursplanung;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.module.reporting.filterung.ReportingFilterDataType;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

	/** Eine Liste von IDs, die die Ausgabe auf diese IDs beschränkt. Auf welchen Datentyp sich diese IDs beziehen, definiert der Wert der Eigenschaft
	 * idsFilterDataType. Ist die Liste leer, dann erfolgt keine Filterung. */
	private List<Long> idsFilter;

	/** Der Typ von Daten, auf den sich die Filterung der IDs bezieht. */
	private final ReportingFilterDataType idsFilterDataType;

	/** Eine Liste, die die gefilterten Schüler zwischenspeichert, wenn ein wiederholter Zugriff erfolgt. */
	private List<ReportingSchueler> gefilterteSchueler;

	/** Eine Liste, die die gefilterten Kurse zwischenspeichert, wenn ein wiederholter Zugriff erfolgt. */
	private List<ReportingGostKursplanungKurs> gefilterteKurse;

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
	 * @param idsFilter Eine Liste von IDs, die die Ausgabe auf diese IDs beschränkt. Auf welchen Datentyp sich diese IDs beziehen, definiert der Wert der Eigenschaft idsFilterDataType.
	 * @param idsFilterDataType Der Typ von Daten, auf den sich die Filterung der IDs bezieht.
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ReportingGostKursplanungBlockungsergebnis(final int abiturjahr, final int anzahlDummy, final int anzahlExterne, final int anzahlMaxKurseProSchiene,
			final int anzahlSchienen, final int anzahlSchueler, final String bezeichnung,
			final Map<Long, ReportingGostKursplanungFachwahlstatistik> fachwahlstatistik, final GostHalbjahr gostHalbjahr, final long id,
			final List<ReportingGostKursplanungKurs> kurse, final List<ReportingGostKursplanungSchiene> schienen, final List<ReportingSchueler> schueler,
			final List<Long> idsFilter, final ReportingFilterDataType idsFilterDataType) {
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

		this.idsFilter = (idsFilter == null) ? new ArrayList<>() : idsFilter.stream().filter(Objects::nonNull).distinct().toList();
		this.idsFilterDataType = idsFilterDataType;
		filterDaten();
	}


	// ##### Berechnete Methoden #####

	/**
	 * Filtert die Daten entsprechend dem Typ der IDs (Schüler oder Kurse), die in der Eigenschaft idsFilterDataType angegeben sind,
	 * und beschränkt die Ergebnisse auf die IDs in der Liste idsFilter. Falls die Filterliste leer ist, wird keine Filterung vorgenommen,
	 * und alle entsprechenden Daten werden zurückgegeben.
	 * Die gefilterten Ergebnisse werden in den entsprechenden Eigenschaften gefilterteSchueler und gefilterteKurse gespeichert.
	 */
	private void filterDaten() {
		switch (this.idsFilterDataType) {
			case SCHUELER -> {
				if (this.idsFilter.isEmpty()) {
					// Wenn die Liste der zu filterenden IDs leer ist, wird keine Filterung vorgenommen, also alle Schüler verwendet.
					this.gefilterteSchueler = new ArrayList<>(this.schueler);
				} else if (!this.schueler.isEmpty()) {
					// Filtere die Schüler heraus, die gewünscht werden.
					gefilterteSchueler = this.schueler.stream().filter(s -> idsFilter.contains(s.id())).toList();
					// Bereinige anschließend unter Umständen die Liste der Filter-IDs.
					this.idsFilter = gefilterteSchueler.stream().map(ReportingSchueler::id).toList();
				}
				gefilterteKurse = new ArrayList<>(this.kurse);
			}
			case KURSE -> {
				gefilterteSchueler = new ArrayList<>(this.schueler);
				if (this.idsFilter.isEmpty()) {
					// Wenn die Liste der zu filterenden IDs leer ist, wird keine Filterung vorgenommen, also alle Kurse verwendet.
					this.gefilterteKurse = new ArrayList<>(this.kurse);
				} else if (!this.kurse.isEmpty()) {
					// Filtere die Kurse heraus, die gewünscht werden.
					gefilterteKurse = this.kurse.stream().filter(k -> idsFilter.contains(k.id())).toList();
					// Bereinige anschließend unter Umständen die Liste der Filter-IDs.
					this.idsFilter = gefilterteKurse.stream().map(ReportingGostKursplanungKurs::id).toList();
				}
			}
			case null, default -> {
				gefilterteSchueler = new ArrayList<>(this.schueler);
				gefilterteKurse = new ArrayList<>(this.kurse);
			}
		}
	}

	/**
	 * Gibt eine gefilterte Liste der Kurse zurück. Die Filterung basiert auf den Angaben
	 * der IDs und des Datentyps, die in den Eigenschaften der Klasse gespeichert sind.
	 * Vor der Rückgabe wird eine Filterung der Daten durchgeführt.
	 *
	 * @return Eine Liste vom Typ ReportingGostKursplanungKurs mit den gefilterten Kursen.
	 */
	@JsonIgnore
	public List<ReportingGostKursplanungKurs> kurseGefiltert() {
		filterDaten();
		return this.gefilterteKurse;
	}

	/**
	 * Gibt eine gefilterte Liste der Schüler zurück. Die Filterung basiert auf den Angaben
	 * der IDs und des Datentyps, die in den Eigenschaften der Klasse gespeichert sind.
	 * Vor der Rückgabe wird eine Filterung der Daten durchgeführt.
	 *
	 * @return Eine Liste vom Typ ReportingSchueler mit den gefilterten Schülern.
	 */
	@JsonIgnore
	public List<ReportingSchueler> schuelerGefiltert() {
		filterDaten();
		return this.gefilterteSchueler;
	}

	/**
	 * Gibt eine gefilterte Liste von IDs basierend auf dem aktuellen Filter-Datentyp zurück.
	 * Der Filter kann auf Schüler, Kurse oder keinen spezifischen Datentyp angewendet werden.
	 *
	 * @return Eine Liste von Long-Werten, die die gefilterten IDs enthalten. Ist kein spezifischer
	 *         Datentyp festgelegt oder sind keine passenden Einträge vorhanden, wird eine leere Liste zurückgegeben.
	 */
	@JsonIgnore
	public List<Long> idsGefiltert() {
		switch (this.idsFilterDataType) {
			case SCHUELER -> {
				return schuelerGefiltert().stream().map(ReportingSchueler::id).toList();
			}
			case KURSE -> {
				return kurseGefiltert().stream().map(ReportingGostKursplanungKurs::id).toList();
			}
			case null, default -> {
				return new ArrayList<>();
			}
		}
	}

	/**
	 * Gibt eine gefilterte Liste von Daten zurück, basierend auf dem Typ, der in der
	 * Eigenschaft idsFilterDataType gespeichert ist. Aktuell werden folgende Typen unterstützt:
	 * - SCHUELER: Rückgabe einer Liste gefilterter Schüler
	 * - KURSE: Rückgabe einer Liste gefilterter Kurse
	 * - Standardwert oder null: Rückgabe einer leeren Liste
	 *
	 * @return Eine Liste von Objekten des jeweiligen Typs, die den Filterkriterien entsprechen.
	 */
	@JsonIgnore
	public List<ReportingBaseType> datenGefiltert() {
		switch (this.idsFilterDataType) {
			case SCHUELER -> {
				return new ArrayList<>(schuelerGefiltert());
			}
			case KURSE -> {
				return new ArrayList<>(kurseGefiltert());
			}
			case null, default -> {
				return new ArrayList<>();
			}
		}
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
	 * Die IDs, die zur Filterung verwendet werden. Auf welchen Datentyp sich diese IDs beziehen, definiert der Wert der Eigenschaft idsFilterDataType.
	 *
	 * @return Inhalt des Feldes idsFilter
	 */
	public List<Long> idsFilter() {
		return idsFilter;
	}

	/**
	 * Der Datentyp der Filterung.
	 *
	 * @return Inhalt des Feldes idsFilterDataType
	 */
	public ReportingFilterDataType idsFilterDataType() {
		return idsFilterDataType;
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
