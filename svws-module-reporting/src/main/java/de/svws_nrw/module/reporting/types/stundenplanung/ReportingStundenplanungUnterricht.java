package de.svws_nrw.module.reporting.types.stundenplanung;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;


/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Unterricht im Rahmen der Stundenplanung.
 */
public class ReportingStundenplanungUnterricht extends ReportingBaseType {

	/** Das Fach des Unterrichts */
	public ReportingFach fach;

	/** Die ID des Unterrichts */
	protected long id;

	/** Die Klassen, die dieser Unterrichtseinheit zugeordnet sind. Diese Liste ist leer, falls es sich um Kursunterricht handelt. */
	protected List<ReportingKlasse> klassen = new ArrayList<>();

	/** Der Kurs, sofern es sich um Kursunterricht handelt, andernfalls NULL.*/
	protected ReportingKurs kurs;

	/** Die Lehrkräfte, die diesem Unterricht zugeordnet sind. */
	protected List<ReportingLehrer> lehrkraefte = new ArrayList<>();

	/** Die Räume, die diesem Unterricht zugeordnet sind. */
	protected List<ReportingStundenplanungRaum> raeume = new ArrayList<>();

	/** Die IDs der Schienen, die diesem Unterricht zugeordnet sind (im Normalfall eine, bei Kursen mit Schülern aus mehreren Jahrgangsstufen ggf. mehrere). */
	protected List<Long> schienen = new ArrayList<>();

	/** Der Wochen-Typ bei der Unterscheidung von Mehrwochenplänen (-> 1, 2, ...) oder 0 */
	protected int wochentyp;

	/** Die Stunde im Unterrichtsraster des Stundenplans. */
	protected ReportingStundenplanungUnterrichtsrasterstunde stundeImUnterrichtsraster;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param id                        Die ID des Unterrichts.
	 * @param fach                      Das Fach des Unterrichts.
	 * @param klassen                   Die Klassen, die dieser Unterrichtseinheit zugeordnet sind.
	 * @param kurs                      Der Kurs, sofern es sich um Kursunterricht handelt.
	 * @param lehrkraefte               Die Lehrkräfte, die diesem Unterricht zugeordnet sind.
	 * @param raeume                    Die Räume, die diesem Unterricht zugeordnet sind.
	 * @param schienen                  Die IDs der Schienen, die diesem Unterricht zugeordnet sind.
	 * @param wochentyp                 Der Wochen-Typ bei der Unterscheidung von Mehrwochenplänen.
	 * @param stundeImUnterrichtsraster Die Stunde im Unterrichtsraster des Stundenplans.
	 */
	public ReportingStundenplanungUnterricht(final long id, final ReportingFach fach, final List<ReportingKlasse> klassen, final ReportingKurs kurs,
			final List<ReportingLehrer> lehrkraefte, final List<ReportingStundenplanungRaum> raeume, final List<Long> schienen, final int wochentyp,
			final ReportingStundenplanungUnterrichtsrasterstunde stundeImUnterrichtsraster) {

		this.id = id;
		this.fach = fach;
		this.klassen = klassen;
		if (klassen == null)
			this.klassen = new ArrayList<>();
		this.kurs = kurs;
		if (this.kurs != null) {
			this.klassen = this.kurs.klassen();
		}
		this.lehrkraefte = lehrkraefte;
		this.raeume = raeume;
		this.schienen = schienen;
		this.wochentyp = wochentyp;
		this.stundeImUnterrichtsraster = stundeImUnterrichtsraster;
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	public int hashCode() {
		return 31 + Long.hashCode(id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof final ReportingStundenplanungStundenplan other))
			return false;
		return (id == other.id);
	}

	// ##### Berechnete Methoden #####

	/**
	 * Gibt eine kommagetrennte Auflistung der Klassen mit ihrer Kurzbezeichnung zurück,
	 * die diesem Unterricht zugeordnet sind.
	 *
	 * @return Eine Zeichenkette mit den Kurzbezeichnungen der Klassen, getrennt durch ein Komma.
	 */
	public String klassenAuflistung() {
		return this.klassen().stream().map(ReportingKlasse::kuerzel).collect(Collectors.joining(","));
	}

	/**
	 * Gibt eine kommagetrennte Auflistung der Klassen mit Jahrgang und Parallelität zurück,
	 * Beispiel: 09A,09B,09C,10A,10B.
	 *
	 * @return Eine Zeichenkette mit den Klassen nach Jahrgängen und Parallelität.
	 */
	public String klassenAuflistungJahrgangParallelitaet() {
		return this.klassen().stream().map(k -> k.jahrgang().kuerzel() + k.parallelitaet()).sorted().collect(Collectors.joining(","));
	}

	/**
	 * Gibt eine kommagetrennte Auflistung der Klassen mit Jahrgang und Parallelität zurück,
	 * wobei parallele Klassen gleichen Jahrgangs als Gruppe dargestellt werden.
	 * Beispiel: Aus 09A,09B,09C,10A,10B wird 09ABC,10AB.
	 *
	 * @return Eine Zeichenkette mit den Klassen, zusammengefasst nach Jahrgängen und Parallelität.
	 */
	public String klassenAuflistungJahrgangParallelitaetKurz() {
		final List<ReportingKlasse> listKlassen = new ArrayList<>();

		if (!this.klassen().isEmpty())
			listKlassen.addAll(this.klassen());
		else
			listKlassen.addAll(this.kurs().klassen());

		return listKlassen.stream()
				.collect(Collectors.groupingBy(k -> k.jahrgang().kuerzel(), Collectors.mapping(ReportingKlasse::parallelitaet,
						Collectors.joining()))) // Parallelitäten der Klassen einer Stufe verbinden.
				.entrySet().stream()
				.map(entry -> entry.getKey() + entry.getValue()) // Kombiniert Jahrgang mit Parallelbuchstaben.
				.sorted()
				.collect(Collectors.joining(",")); // Verbindet die unterschiedlichen Einträge durch Kommas.
	}

	/**
	 * Gibt eine kommagetrennte Auflistung der Kürzel der Lehrkräfte zurück,
	 * die diesem Unterricht zugeordnet sind.
	 *
	 * @return Eine Zeichenkette mit den Kürzeln der Lehrkräfte, getrennt durch ein Komma.
	 */
	public String lehrkraefteAuflistung() {
		return this.lehrkraefte().stream().map(l -> String.valueOf(l.kuerzel())).collect(Collectors.joining(","));
	}

	/**
	 * Gibt eine kommagetrennte Auflistung der Kurzbezeichnungen der Räume zurück,
	 * die diesem Unterricht zugeordnet sind.
	 *
	 * @return Eine Zeichenkette mit den Kurzbezeichnungen der Räume, getrennt durch ein Komma.
	 */
	public String raeumeAuflistung() {
		return this.raeume().stream().map(r -> String.valueOf(r.kuerzel())).collect(Collectors.joining(","));
	}


	// ##### Getter #####

	/**
	 * Gibt das Fach des Unterrichts zurück.
	 *
	 * @return Das Fach des Unterrichts.
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Gibt die ID des Unterrichts zurück.
	 *
	 * @return Die ID des Unterrichts.
	 */
	public long id() {
		return id;
	}

	/**
	 * Gibt die Klassen zurück, die dieser Unterrichtseinheit zugeordnet sind.
	 *
	 * @return Die Klassen dieser Unterrichtseinheit.
	 */
	public List<ReportingKlasse> klassen() {
		return klassen;
	}

	/**
	 * Gibt den Kurs zurück, sofern es sich um Kursunterricht handelt.
	 *
	 * @return Der Kurs dieser Unterrichtseinheit oder NULL, wenn keiner existiert.
	 */
	public ReportingKurs kurs() {
		return kurs;
	}

	/**
	 * Gibt die Lehrkräfte zurück, die diesem Unterricht zugeordnet sind.
	 *
	 * @return Die Lehrkräfte diesem Unterricht.
	 */
	public List<ReportingLehrer> lehrkraefte() {
		return lehrkraefte;
	}

	/**
	 * Gibt die Räume zurück, die diesem Unterricht zugeordnet sind.
	 *
	 * @return Die Räume diesem Unterricht.
	 */
	public List<ReportingStundenplanungRaum> raeume() {
		return raeume;
	}

	/**
	 * Gibt die IDs der Schienen zurück, die diesem Unterricht zugeordnet sind.
	 *
	 * @return Die IDs der Schienen diesem Unterricht.
	 */
	public List<Long> schienen() {
		return schienen;
	}

	/**
	 * Gibt die Stunde im Unterrichtsraster des Stundenplans zurück.
	 *
	 * @return Die Stunde im Unterrichtsraster des Stundenplans.
	 */
	public ReportingStundenplanungUnterrichtsrasterstunde stundeImUnterrichtsraster() {
		return stundeImUnterrichtsraster;
	}

	/**
	 * Gibt den Wochen-Typ bei der Unterscheidung von Mehrwochenplänen zurück.
	 *
	 * @return Der Wochen-Typ diesem Unterricht oder 0.
	 */
	public int wochentyp() {
		return wochentyp;
	}
}
