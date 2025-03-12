package de.svws_nrw.module.reporting.types.stundenplanung;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.svws_nrw.core.adt.map.ListMap4DLongKeys;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;


/**
 * Diese Klasse repräsentiert den Zeitraum einer Pause im Rahmen der Stundenplanung.
 */
public class ReportingStundenplanungPausenzeit extends ReportingStundenplanungZeitelement {

	/** Die Bezeichnung der Pausenzeit. */
	protected String bezeichnung;

	/** Die ID der Pausenzeit. */
	protected long id;

	/** Der Stundenplan, zu dem diese Pausenzeit gehört. */
	protected ReportingStundenplanungStundenplan stundenplan;

	/** Die Pausenaufsichten, dieser Pausenzeit über alle Tage und Wochen des Stundenplans zugeordnet sind. */
	protected final List<ReportingStundenplanungPausenaufsicht> pausenaufsichten = new ArrayList<>();

	/** Die Klassen, die dieser Pausenzeit zugeordnet sind. Diese Liste ist leer, falls der Zeitraum für alle Klassen gilt. */
	private final List<ReportingKlasse> klassen = new ArrayList<>();

	/** Eine Listmap der Pausenaufsichten nach den IDs zur Pausenaufsicht, zum Lehrer, zum Aufsichtsbereich und zum Wochentyp. */
	private ListMap4DLongKeys<ReportingStundenplanungPausenaufsicht> listMapPausenaufsichten = new ListMap4DLongKeys<>();


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param id		       Die ID der Pausenzeit.
	 * @param stundenplan      Der Stundenplan, zu der diese Pausenzeit gehört.
	 * @param beginn	       Die Uhrzeit in Minuten seit 0 Uhr, wann diese Pausenzeit beginnt. NULL bedeutet "noch nicht definiert".
	 * @param bezeichnung      Die Bezeichnung der Pausenzeit.
	 * @param ende		       Die Uhrzeit in Minuten seit 0 Uhr, wann diese Pausenzeit endet. NULL bedeutet "noch nicht definiert".
	 * @param klassen 	       Die Klassen, die dieser Pausenzeit zugeordnet sind. Diese Liste ist leer, falls der Zeitraum für alle Klassen gilt.
	 * @param pausenaufsichten Die Klassen, die dieser Pausenzeit zugeordnet sind. Diese Liste ist leer, falls der Zeitraum für alle Klassen gilt.
	 * @param wochentag        Der {@link Wochentag} an dem diese Pausenzeit liegt.
	 */
	public ReportingStundenplanungPausenzeit(final long id, final ReportingStundenplanungStundenplan stundenplan, final String bezeichnung,
			final Integer beginn, final Integer ende, final List<ReportingKlasse> klassen, final List<ReportingStundenplanungPausenaufsicht> pausenaufsichten,
			final Wochentag wochentag) {
		super(beginn, ende, wochentag);
		this.id = id;
		this.stundenplan = stundenplan;
		this.bezeichnung = bezeichnung;
		this.klassen.addAll(klassen);
		setPausenaufsichten(pausenaufsichten);
	}


	// ##### Hash und Equals und Compare-Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return 31 + Long.hashCode(id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof final ReportingStundenplanungPausenzeit other))
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

		return listKlassen.stream()
				.collect(Collectors.groupingBy(k -> k.jahrgang().kuerzel(), Collectors.mapping(ReportingKlasse::parallelitaet,
						Collectors.joining()))) // Parallelitäten der Klassen einer Stufe verbinden.
				.entrySet().stream()
				.map(entry -> entry.getKey() + entry.getValue()) // Kombiniert Jahrgang mit Parallelbuchstaben.
				.sorted()
				.collect(Collectors.joining(",")); // Verbindet die unterschiedlichen Einträge durch Kommas.
	}


	// ##### Getter #####

	/**
	 * Liefert die ID der Stunde aus dem Unterrichtsraster.
	 *
	 * @return Die ID der Stunde aus dem Unterrichtsraster.
	 */
	public long id() {
		return id;
	}

	/**
	 * Liefert den Stundenplan, zu dem diese Stunde aus dem Unterrichtsraster gehört.
	 *
	 * @return Der Stundenplan der Unterrichtsstunde.
	 */
	public ReportingStundenplanungStundenplan stundenplan() {
		return stundenplan;
	}

	/**
	 * Liefert eine unveränderliche Liste der zugeordneten Klassen, die dieser Pausenzeit zugeordnet sind.
	 * Diese Liste ist leer, falls der Zeitraum für alle Klassen gilt.
	 *
	 * @return Eine unveränderliche Liste der zugeordneten {@link ReportingKlasse}-Objekte.
	 */
	public List<ReportingKlasse> klassen() {
		return klassen;
	}

	/**
	 * Liefert eine unveränderliche Liste der Pausenaufsichten, die dieser Pausenzeit zugeordnet sind.
	 * Die Liste ist leer, falls der Zeitraum für keine spezifischen Pausenaufsichten definiert wurde.
	 *
	 * @return Eine unveränderliche Liste der zugeordneten {@link ReportingStundenplanungPausenaufsicht}-Objekte.
	 */
	public List<ReportingStundenplanungPausenaufsicht> pausenaufsichten() {
		return Collections.unmodifiableList(pausenaufsichten);
	}

	/**
	 * Gibt eine Liste von Pausenaufsichten basierend auf der angegebenen ID und dem Wochentyp zurück.
	 * Optional können zusätzlich Pausenaufsichten des Wochentyps 0 einbezogen werden.
	 *
	 * @param id                    Die ID, für die die Pausenaufsichten ermittelt werden sollen.
	 * @param wochentyp             Der Wochentyp, für den die Pausenaufsichten ermittelt werden sollen.
	 * @param inklusiveWochentyp0   Gibt an, ob auch Pausenaufsichten des Wochentyps 0 berücksichtigt werden sollen.
	 * @return Eine Liste der ermittelten {@link ReportingStundenplanungPausenaufsicht}-Objekte für die gegebene ID und den angegebenen Wochentyp.
	 */
	public List<ReportingStundenplanungPausenaufsicht> pausenaufsichtenByIdUndWochentyp(final long id, final int wochentyp, final boolean inklusiveWochentyp0) {
		final List<ReportingStundenplanungPausenaufsicht> result = new ArrayList<>();

		if (this.pausenaufsichten.isEmpty())
			return result;

		if (inklusiveWochentyp0) {
			result.addAll(listMapPausenaufsichten.get14(id, 0));
		}
		result.addAll(listMapPausenaufsichten.get14(id, wochentyp));

		return result;
	}

	/**
	 * Liefert eine Liste von Pausenaufsichten basierend auf der Lehrkraft-ID und dem Wochentyp.
	 * Zusätzlich können Pausenaufsichten des Wochentyps 0 einbezogen werden.
	 *
	 * @param idLehrkraft         Die ID der Lehrkraft, für die die Pausenaufsichten ermittelt werden sollen.
	 * @param wochentyp           Der Wochentyp, für den die Pausenaufsichten ermittelt werden sollen.
	 * @param inklusiveWochentyp0 Gibt an, ob auch Pausenaufsichten des Wochentyps 0 berücksichtigt werden sollen.
	 * @return Eine Liste von {@link ReportingStundenplanungPausenaufsicht}-Objekten, die der angegebenen Lehrkraft-ID und dem Wochentyp entsprechen.
	 */
	public List<ReportingStundenplanungPausenaufsicht> pausenaufsichtenByLehrkraftUndWochentyp(final long idLehrkraft, final int wochentyp,
			final boolean inklusiveWochentyp0) {
		return pausenaufsichtenByLehrkraeftenUndWochentyp(List.of(idLehrkraft), wochentyp, inklusiveWochentyp0);
	}

	/**
	 * Liefert eine Liste von Pausenaufsichten basierend auf den IDs der Lehrkräfte und dem Wochentyp.
	 * Zusätzlich können Pausenaufsichten des Wochentyps 0 einbezogen werden.
	 *
	 * @param idsLehrkraefte      Die IDs der Lehrkräfte, für die die Pausenaufsichten ermittelt werden sollen.
	 * @param wochentyp           Der Wochentyp, für den die Pausenaufsichten ermittelt werden sollen.
	 * @param inklusiveWochentyp0 Gibt an, ob auch Pausenaufsichten des Wochentyps 0 berücksichtigt werden sollen.
	 * @return Eine Liste von {@link ReportingStundenplanungPausenaufsicht}-Objekten, die der angegebenen Lehrkraft-ID und dem Wochentyp entsprechen.
	 */
	public List<ReportingStundenplanungPausenaufsicht> pausenaufsichtenByLehrkraeftenUndWochentyp(final List<Long> idsLehrkraefte, final int wochentyp,
			final boolean inklusiveWochentyp0) {
		final List<ReportingStundenplanungPausenaufsicht> result = new ArrayList<>();

		if ((idsLehrkraefte == null) || idsLehrkraefte.isEmpty())
			return result;

		if (this.pausenaufsichten.isEmpty())
			return result;

		for (final long idLehrkraft : idsLehrkraefte) {
			if (inklusiveWochentyp0) {
				result.addAll(listMapPausenaufsichten.get24(idLehrkraft, 0));
			}
			result.addAll(listMapPausenaufsichten.get24(idLehrkraft, wochentyp));
		}

		return result;
	}

	/**
	 * Gibt eine Liste von Pausenaufsichten basierend auf der ID des Aufsichtsbereichs und dem Wochentyp zurück.
	 * Optional können zusätzlich Pausenaufsichten des Wochentyps 0 einbezogen werden.
	 *
	 * @param idAufsichtsbereich   Die ID des Aufsichtsbereichs, für den die Pausenaufsichten ermittelt werden sollen.
	 * @param wochentyp            Der Wochentyp, für den die Pausenaufsichten ermittelt werden sollen.
	 * @param inklusiveWochentyp0  Gibt an, ob auch Pausenaufsichten des Wochentyps 0 berücksichtigt werden sollen.
	 * @return Eine Liste von {@link ReportingStundenplanungPausenaufsicht}-Objekten, die dem angegebenen Aufsichtsbereich und Wochentyp entsprechen.
	 */
	public List<ReportingStundenplanungPausenaufsicht> pausenaufsichtenByAufsichtsbereichUndWochentyp(final long idAufsichtsbereich, final int wochentyp,
			final boolean inklusiveWochentyp0) {
		final List<ReportingStundenplanungPausenaufsicht> result = new ArrayList<>();

		if (this.pausenaufsichten.isEmpty())
			return result;

		if (inklusiveWochentyp0) {
			result.addAll(listMapPausenaufsichten.get34(idAufsichtsbereich, 0));
		}
		result.addAll(listMapPausenaufsichten.get24(idAufsichtsbereich, wochentyp));

		return result;
	}


	// ##### Setter #####

	/**
	 * Setzt die Liste der Pausenaufsichten und aktualisiert die entsprechende ListMap.
	 *
	 * @param pausenaufsichten Die Liste der Pausenaufsichten.
	 */
	public void setPausenaufsichten(final List<ReportingStundenplanungPausenaufsicht> pausenaufsichten) {
		this.pausenaufsichten.clear();
		this.listMapPausenaufsichten = new ListMap4DLongKeys<>();
		this.pausenaufsichten.addAll(pausenaufsichten);
		this.pausenaufsichten.forEach(pa -> this.listMapPausenaufsichten.add(pa.id(), pa.lehrkraft().id(), pa.idAufsichtsbereich(), pa.wochentyp(), pa));
	}

}
