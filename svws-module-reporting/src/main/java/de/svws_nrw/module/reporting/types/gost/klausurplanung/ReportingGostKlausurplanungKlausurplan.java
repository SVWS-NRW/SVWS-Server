package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurplan.</p>
 * <p>Sie enthält die Daten zu einem Klausurplan, d. h. beispielsweise zu den Terminen.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKlausurplanungKlausurplan {

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

	/** Eine Map, die alle Termine des Klausurplanes zu deren ID beinhaltet. */
	private Map<Long, ReportingGostKlausurplanungKlausurtermin> mapKlausurtermine;

	/** Eine Map, die alle Kurse des Klausurplanes zu deren ID beinhaltet. */
	private Map<Long, ReportingKurs> mapKurse;

	/** Eine Map, die alle Kursklausuren des Klausurplanes zu deren ID beinhaltet. */
	private Map<Long, ReportingGostKlausurplanungKursklausur> mapKursklausuren;

	/** Eine Map, die alle Schüler des Klausurplanes zu deren ID beinhaltet. */
	private Map<Long, ReportingSchueler> mapSchueler;

	/** Eine Map, die alle Schülerklausuren des Klausurplanes zu deren ID beinhaltet. */
	private Map<Long, ReportingGostKlausurplanungSchuelerklausur> mapSchuelerklausuren;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param klausurtermine	Eine Liste, die alle Termine des Klausurplanes beinhaltet.
	 * @param kurse 			Eine Liste, die alle Kurse des Klausurplanes beinhaltet.
	 * @param kursklausuren 	Eine Liste, die alle Kursklausuren des Klausurplanes beinhaltet.
	 * @param schueler 			Eine Liste, die alle Schüler des Klausurplanes beinhaltet.
	 * @param schuelerklausuren Eine Liste, die alle Schülerklausuren des Klausurplanes beinhaltet.
	 */
	public ReportingGostKlausurplanungKlausurplan(final List<ReportingGostKlausurplanungKlausurtermin> klausurtermine, final List<ReportingKurs> kurse,
			final List<ReportingGostKlausurplanungKursklausur> kursklausuren, final List<ReportingSchueler> schueler,
			final List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren) {

		// Fülle die Basislisten mit den übergebenen Daten.
		this.schueler = (schueler != null) ? schueler : new ArrayList<>();
		this.kurse = (kurse != null) ? kurse : new ArrayList<>();
		this.klausurtermine = (klausurtermine != null) ? klausurtermine : new ArrayList<>();
		this.kursklausuren = (kursklausuren != null) ? kursklausuren : new ArrayList<>();
		this.schuelerklausuren = (schuelerklausuren != null) ? schuelerklausuren : new ArrayList<>();

		// Erzeuge Maps aus den erstellten Listen.
		if (!this.klausurtermine.isEmpty())
			this.mapKlausurtermine = this.klausurtermine.stream().collect(Collectors.toMap(ReportingGostKlausurplanungKlausurtermin::idKlausurtermin, t -> t));
		else
			this.mapKlausurtermine = new HashMap<>();

		if (!this.kursklausuren.isEmpty())
			this.mapKurse = this.kurse.stream().collect(Collectors.toMap(ReportingKurs::id, k -> k));
		else
			this.mapKurse = new HashMap<>();

		if (!this.kursklausuren.isEmpty())
			this.mapKursklausuren = this.kursklausuren.stream().collect(Collectors.toMap(ReportingGostKlausurplanungKursklausur::id, k -> k));
		else
			this.mapKursklausuren = new HashMap<>();

		if (!this.schueler.isEmpty())
			this.mapSchueler = this.schueler.stream().collect(Collectors.toMap(ReportingSchueler::id, s -> s));
		else
			this.mapSchueler = new HashMap<>();

		if (!this.schuelerklausuren.isEmpty())
			this.mapSchuelerklausuren = this.schuelerklausuren.stream().collect(Collectors.toMap(ReportingGostKlausurplanungSchuelerklausur::id, s -> s));
		else
			this.mapSchuelerklausuren = new HashMap<>();
	}


	// ##### Berechnete Methoden #####

	/**
	 * Eine Liste vom Typ String, die alle vorhandenen Datumsangaben der Termine des Klausurplanes beinhaltet (distinct).
	 * @return Liste der Datumsangaben der Klausurtermine
	 */
	public List<String> datumsangabenKlausurtermine() {
		return this.klausurtermine.stream().filter(t -> Objects.nonNull(t.datum())).map(t -> t.datum).sorted().distinct().toList();
	}

	/**
	 * Eine Liste vom Typ GostKlausurplanungKlausurtermin, die alle Termine des Klausurplanes beinhaltet, denen bereits ein Datum zugewiesen wurde.
	 * @return Liste der Klausurtermine mit Datumsangabe
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermineMitDatum() {
		return this.klausurtermine.stream().filter(t -> Objects.nonNull(t.datum())).toList();
	}

	/**
	 * Eine Liste vom Typ GostKlausurplanungKlausurtermin, die alle Termine des Klausurplanes beinhaltet, denen noch kein Datum zugewiesen wurde.
	 * @return Liste der Klausurtermine ohne Datumsangabe
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermineOhneDatum() {
		return this.klausurtermine.stream().filter(t -> Objects.isNull(t.datum())).toList();
	}

	/**
	 * Eine Liste vom Typ GostKlausurplanungKlausurtermin, die alle Termine des Klausurplanes zum angegebenen Datum beinhaltet.
	 * @param  datum 	Datum, zu dem die Liste der Klausurtermine zurückgegeben werden soll.
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
	 * @param  id 	Die ID des Klausurtermins
	 * @return 		Der Klausurtermin zur ID
	 */
	public ReportingGostKlausurplanungKlausurtermin klausurtermineZurID(final Long id) {
		if ((id == null) || (id < 0) || !mapKlausurtermine.containsKey(id))
			return null;
		else
			return mapKlausurtermine.get(id);
	}


	// ##### Getter #####

	/**
	 * Eine Liste, die alle Termine des Klausurplanes beinhaltet.
	 * @return Liste der Klausurtermine
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermine() {
		return this.klausurtermine;
	}

	/**
	 * Eine Liste, die alle Kurse des Klausurplanes beinhaltet.
	 * @return Liste der Kurse
	 */
	public List<ReportingKurs> kurse() {
		return this.kurse;
	}

	/**
	 * Eine Liste, die alle Kursklausuren des Klausurplanes beinhaltet.
	 * @return Liste der Kursklausuren
	 */
	public List<ReportingGostKlausurplanungKursklausur> kursklausuren() {
		return this.kursklausuren;
	}

	/**
	 * Eine Liste, die alle Schüler des Klausurplanes beinhaltet.
	 * @return Liste der Schüler
	 */
	public List<ReportingSchueler> schueler() {
		return this.schueler;
	}

	/**
	 * Eine Liste, die alle Schülerklausuren des Klausurplanes beinhaltet.
	 * @return Liste der Schülerklausuren
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren() {
		return this.schuelerklausuren;
	}

	/**
	 * Eine Map, die alle Termine des Klausurplanes zu deren ID beinhaltet.
	 * @return Inhalt des Feldes mapKlausurtermine
	 */
	public Map<Long, ReportingGostKlausurplanungKlausurtermin> mapKlausurtermine() {
		return mapKlausurtermine;
	}

	/**
	 * Eine Map, die alle Kurse des Klausurplanes zu deren ID beinhaltet.
	 * @return Inhalt des Feldes mapKurse
	 */
	public Map<Long, ReportingKurs> mapKurse() {
		return mapKurse;
	}

	/**
	 * Eine Map, die alle Kursklausuren des Klausurplanes zu deren ID beinhaltet.
	 * @return Inhalt des Feldes mapKursklausuren
	 */
	public Map<Long, ReportingGostKlausurplanungKursklausur> mapKursklausuren() {
		return mapKursklausuren;
	}

	/**
	 * Eine Map, die alle Schüler des Klausurplanes zu deren ID beinhaltet.
	 * @return Inhalt des Feldes mapSchueler
	 */
	public Map<Long, ReportingSchueler> mapSchueler() {
		return mapSchueler;
	}

	/**
	 * Eine Map, die alle Schülerklausuren des Klausurplanes zu deren ID beinhaltet.
	 * @return Inhalt des Feldes mapSchuelerklausuren
	 */
	public Map<Long, ReportingGostKlausurplanungSchuelerklausur> mapSchuelerklausuren() {
		return mapSchuelerklausuren;
	}
}
