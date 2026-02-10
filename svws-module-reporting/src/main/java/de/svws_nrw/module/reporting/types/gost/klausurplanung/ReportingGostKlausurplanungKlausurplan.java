package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.module.reporting.filterung.ReportingFilterDataType;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurplan.
 */
public class ReportingGostKlausurplanungKlausurplan extends ReportingBaseType {

	/** Eine Liste von IDs, die die Ausgabe auf diese IDs beschränkt. Auf welchen Datentyp sich diese IDs beziehen, definiert der Wert der Eigenschaft
	 * idsFilterDataType. Ist die Liste leer, dann erfolgt keine Filterung. */
	private List<Long> idsFilter;

	/** Der Typ von Daten, auf den sich die Filterung der IDs bezieht. */
	private final ReportingFilterDataType idsFilterDataType;

	/** Eine Liste, die die gefilterten Schüler zwischenspeichert, wenn ein wiederholter Zugriff erfolgt. */
	private List<ReportingSchueler> gefilterteSchueler;

	/** Eine Liste, die die gefilterten Kurse zwischenspeichert, wenn ein wiederholter Zugriff erfolgt. */
	private List<ReportingKurs> gefilterteKurse;

	/** Eine Liste, die alle Termine des Klausurplanes beinhaltet. */
	protected List<ReportingGostKlausurplanungKlausurtermin> klausurtermine;

	/** Eine Liste, die alle Kurse des Klausurplanes beinhaltet. */
	protected List<ReportingKurs> kurse;

	/** Eine Liste, die alle Kursklausuren des Klausurplanes beinhaltet. */
	protected List<ReportingGostKlausurplanungKursklausur> kursklausuren;

	/** Eine Liste, die alle Schüler des Klausurplanes beinhaltet. */
	protected List<ReportingSchueler> schueler;

	/** Eine Liste, die alle Schülerklausuren des Klausurplanes beinhaltet. */
	protected List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param klausurtermine	Eine Liste, die alle Termine des Klausurplanes beinhaltet.
	 * @param kurse 			Eine Liste, die alle Kurse des Klausurplanes beinhaltet.
	 * @param kursklausuren 	Eine Liste, die alle Kursklausuren des Klausurplanes beinhaltet.
	 * @param schueler 			Eine Liste, die alle Schüler des Klausurplanes beinhaltet.
	 * @param schuelerklausuren Eine Liste, die alle Schülerklausuren des Klausurplanes beinhaltet.
	 * @param idsFilter             Eine Liste von IDs, die die Ausgabe auf diese IDs beschränkt. Auf welchen Datentyp sich diese IDs beziehen, definiert der
	 *                              Wert der Eigenschaft idsFilterDataType.
	 * @param idsFilterDataType     Der Typ von Daten, auf den sich die Filterung der IDs bezieht.
	 */
	public ReportingGostKlausurplanungKlausurplan(final List<ReportingGostKlausurplanungKlausurtermin> klausurtermine, final List<ReportingKurs> kurse,
			final List<ReportingGostKlausurplanungKursklausur> kursklausuren, final List<ReportingSchueler> schueler,
			final List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren, final List<Long> idsFilter,
			final ReportingFilterDataType idsFilterDataType) {

		// Fülle die Basislisten mit den übergebenen Daten.
		this.schueler = (schueler != null) ? schueler : new ArrayList<>();
		this.kurse = (kurse != null) ? kurse : new ArrayList<>();
		this.klausurtermine = (klausurtermine != null) ? klausurtermine : new ArrayList<>();
		this.kursklausuren = (kursklausuren != null) ? kursklausuren : new ArrayList<>();
		this.schuelerklausuren = (schuelerklausuren != null) ? schuelerklausuren : new ArrayList<>();

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
				} else  if (!this.kurse.isEmpty()) {
					// Filtere die Kurse heraus, die gewünscht werden.
					gefilterteKurse = this.kurse.stream().filter(k -> idsFilter.contains(k.id())).toList();
					// Bereinige anschließend unter Umständen die Liste der Filter-IDs.
					this.idsFilter = gefilterteKurse.stream().map(ReportingKurs::id).toList();
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
	 * @return Eine Liste vom Typ ReportingKurs mit den gefilterten Kursen.
	 */
	@JsonIgnore
	public List<ReportingKurs> kurseGefiltert() {
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
				return schuelerGefiltert().stream().map(ReportingSchueler::id).distinct().toList();
			}
			case KURSE -> {
				return kurseGefiltert().stream().map(ReportingKurs::id).distinct().toList();
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
	public List<?> datenGefiltert() {
		switch (this.idsFilterDataType) {
			case SCHUELER -> {
				return schuelerGefiltert();
			}
			case KURSE -> {
				return kurseGefiltert();
			}
			case null, default -> {
				return new ArrayList<>();
			}
		}
	}

	/**
	 * Eine Liste vom Typ String, die alle vorhandenen Datumsangaben der Termine des Klausurplanes beinhaltet (distinct).
	 *
	 * @return Liste der Datumsangaben der Klausurtermine
	 */
	public List<String> datumsangabenKlausurtermine() {
		return this.klausurtermine.stream().filter(t -> Objects.nonNull(t.datum())).map(t -> t.datum).sorted().distinct().toList();
	}

	/**
	 * Eine Liste vom Typ GostKlausurplanungKlausurtermin, die alle Termine des Klausurplanes beinhaltet, denen bereits ein Datum zugewiesen wurde.
	 *
	 * @return Liste der Klausurtermine mit Datumsangabe
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermineMitDatum() {
		return this.klausurtermine.stream().filter(t -> Objects.nonNull(t.datum())).toList();
	}

	/**
	 * Eine Liste vom Typ GostKlausurplanungKlausurtermin, die alle Termine des Klausurplanes beinhaltet, denen noch kein Datum zugewiesen wurde.
	 *
	 * @return Liste der Klausurtermine ohne Datumsangabe
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermineOhneDatum() {
		return this.klausurtermine.stream().filter(t -> Objects.isNull(t.datum())).toList();
	}

	/**
	 * Eine Liste vom Typ GostKlausurplanungKlausurtermin, die alle Termine des Klausurplanes zum angegebenen Datum beinhaltet.
	 *
	 * @param  datum 	Datum, zu dem die Liste der Klausurtermine zurückgegeben werden soll.
	 *
	 * @return 			Liste der Klausurtermine mit dem gewünschten Datum
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermineZumDatum(final String datum) {
		if ((datum == null) || datum.isEmpty())
			return new ArrayList<>();
		return this.klausurtermine.stream().filter(t -> datum.equals(t.datum()))
				.sorted(Comparator
						.comparing(ReportingGostKlausurplanungKlausurtermin::gostHalbjahr)
						.thenComparing(ReportingGostKlausurplanungKlausurtermin::startuhrzeit))
				.toList();
	}

	/**
	 * Gibt den Klausurtermin zur übergebenen ID zurück
	 *
	 * @param  id 	Die ID des Klausurtermins
	 *
	 * @return 		Der Klausurtermin zur ID oder null, wenn nicht vorhanden.
	 */
	public ReportingGostKlausurplanungKlausurtermin klausurtermin(final long id) {
		if (id < 0)
			return null;
		return this.klausurtermine.stream().filter(t -> id == t.id).findFirst().orElse(null);
	}

	/**
	 * Gibt den Kurs zur übergebenen ID zurück
	 *
	 * @param  id 	Die ID des Kurses
	 *
	 * @return 		Der Kurs zur ID oder null, wenn nicht vorhanden.
	 */
	public ReportingKurs kurs(final long id) {
		if (id < 0)
			return null;
		return this.kurse.stream().filter(k -> id == k.id()).findFirst().orElse(null);
	}

	/**
	 * Gibt die Kursklausur zur übergebenen ID zurück
	 *
	 * @param  id 	Die ID der Kursklausur
	 *
	 * @return 		Die Kursklausur zur ID oder null, wenn nicht vorhanden.
	 */
	public ReportingGostKlausurplanungKursklausur kursklausur(final long id) {
		if (id < 0)
			return null;
		return this.kursklausuren.stream().filter(k -> id == k.id()).findFirst().orElse(null);
	}

	/**
	 * Gibt den Schüler zur übergebenen ID zurück
	 *
	 * @param  id 	Die ID des Schülers
	 *
	 * @return 		Der Schüler zur ID oder null, wenn nicht vorhanden.
	 */
	public ReportingSchueler schueler(final long id) {
		if (id < 0)
			return null;
		return this.schueler.stream().filter(s -> id == s.id()).findFirst().orElse(null);
	}

	/**
	 * Gibt die Schülerklausur zur übergebenen ID zurück
	 *
	 * @param  id 	Die ID der Schülerklausur
	 *
	 * @return 		Die Schülerklausur zur ID oder null, wenn nicht vorhanden.
	 */
	public ReportingGostKlausurplanungSchuelerklausur schuelerklausur(final long id) {
		if (id < 0)
			return null;
		return this.schuelerklausuren.stream().filter(s -> id == s.id()).findFirst().orElse(null);
	}


	// ##### Getter #####

	/**
	 * Eine Liste, die alle Termine des Klausurplanes beinhaltet.
	 *
	 * @return Liste der Klausurtermine
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermine() {
		return this.klausurtermine;
	}

	/**
	 * Eine Liste, die alle Kurse des Klausurplanes beinhaltet.
	 *
	 * @return Liste der Kurse
	 */
	public List<ReportingKurs> kurse() {
		return this.kurse;
	}

	/**
	 * Eine Liste, die alle Kursklausuren des Klausurplanes beinhaltet.
	 *
	 * @return Liste der Kursklausuren
	 */
	public List<ReportingGostKlausurplanungKursklausur> kursklausuren() {
		return this.kursklausuren;
	}

	/**
	 * Eine Liste, die alle Schüler des Klausurplanes beinhaltet.
	 *
	 * @return Liste der Schüler
	 */
	public List<ReportingSchueler> schueler() {
		return this.schueler;
	}

	/**
	 * Eine Liste, die alle Schülerklausuren des Klausurplanes beinhaltet.
	 *
	 * @return Liste der Schülerklausuren
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren() {
		return this.schuelerklausuren;
	}
}
