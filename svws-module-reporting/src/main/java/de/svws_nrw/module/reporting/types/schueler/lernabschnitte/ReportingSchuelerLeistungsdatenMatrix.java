package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import de.svws_nrw.core.adt.map.ListMap2DLongKeys;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingFach;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingSchueler;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Diese Klasse erstellt eine Matrix der Leistungsdaten für eine Liste von Schülern bezogen auf einen spezifischen Schuljahresabschnitt.
 * Sie ermöglicht eine tabellarische Darstellung, bei der die Schüler Zeilen bilden und die Fächer einheitliche Spalten.
 */
public class ReportingSchuelerLeistungsdatenMatrix {

	/** Die Liste der gefilterten Schüler in der Matrix (Zeilen). */
	private final List<ReportingSchueler> schueler = new ArrayList<>();

	/** Interne Map: Schüler-ID auf den zugehörigen Lernabschnitt. */
	private final Map<Long, ReportingSchuelerLernabschnitt> mapSchuelerLernabschnitte = new HashMap<>();

	/** Die Liste aller vorkommenden Fächer in der Gruppe (Spaltenüberschriften). */
	private final List<ReportingFach> spaltenFaecher;

	/** Interne Map aller in der Matrix vorkommenden Fächer zur Vorbereitung der Spaltensortierung. */
	private final Map<Long, ReportingFach> mapAlleFaecher = new HashMap<>();

	/** Interne Matrix: Schüler-ID (Key1) und Fach-ID (Key2) auf Leistungsdaten. */
	private final ListMap2DLongKeys<ReportingSchuelerLeistungsdaten> matrix = new ListMap2DLongKeys<>();


	/**
	 * Erstellt eine neue Matrix für eine Liste von Schülern. Es werden nur Schüler berücksichtigt, die im angegebenen Schuljahresabschnitt einen Lernabschnitt besitzen.
	 * Die Fächer und Schüler werden nach den übergebenen Sortierungsattributen sortiert. Ist ein Sortierungsattribut null oder leer, wird die Standardsortierung verwendet.
	 *
	 * @param schueler                     Die Liste der potenziellen Schüler.
	 * @param schuljahresabschnitt         Der Schuljahresabschnitt, für den die Daten ermittelt werden sollen.
	 * @param sortierungsAttributeFaecher  Eine Liste von Attributnamen für die Sortierung der Spalten mit den Fächern.
	 *                                     Vordefinierte Listen finden sich unter {@link SortierungRegistryReportingFach#standardsortierung}
	 *                                     oder {@link SortierungRegistryReportingFach#standardsortierungGost}.
	 *                                     Ist die Liste leer oder null, wird die Standardsortierung verwendet.
	 * @param sortierungsAttributeSchueler Eine Liste von Attributnamen für die Sortierung der Zeilen mit den Schülern.
	 *                                     Eine vordefinierte Liste findet sich unter {@link SortierungRegistryReportingSchueler#standardsortierung}.
	 *                                     Ist die Liste leer oder null, wird die Standardsortierung verwendet.
	 * @param filterFaecher                Ein Prädikat zum Filtern der Fächer.
	 */
	public ReportingSchuelerLeistungsdatenMatrix(final List<ReportingSchueler> schueler, final ReportingSchuljahresabschnitt schuljahresabschnitt,
			final List<String> sortierungsAttributeFaecher, final List<String> sortierungsAttributeSchueler, final Predicate<ReportingFach> filterFaecher) {

		// Prüfe die Listen mit den übergebenen Sortierungsattributen. Sind diese null oder empty, wähle die Standardsortierung aus der entsprechenden Registry.
		List<String> sortFaecher = sortierungsAttributeFaecher;
		if ((sortFaecher == null) || sortFaecher.isEmpty())
			sortFaecher = SortierungRegistryReportingFach.standardsortierung();
		List<String> sortSchueler = sortierungsAttributeSchueler;
		if ((sortSchueler == null) || sortSchueler.isEmpty())
			sortSchueler = SortierungRegistryReportingSchueler.standardsortierung();

		// Filter vorbereiten
		final Predicate<ReportingFach> effektiverFilter = (filterFaecher == null) ? f -> true : filterFaecher;

		// Matrix befüllen und dabei die Menge aller vorkommenden Fächer sammeln
		initialisiereMatrix(schueler, schuljahresabschnitt, effektiverFilter);

		// Schüler sortieren
		this.schueler.sort(SortierungRegistryReportingSchueler.buildComparator(sortSchueler, new ArrayList<>()));

		// Spaltenüberschriften (Fächer) sortieren
		this.spaltenFaecher =
				this.mapAlleFaecher.values().stream().sorted(SortierungRegistryReportingFach.buildComparator(sortFaecher, new ArrayList<>())).toList();
	}

	/**
	 * Durchläuft die Schülerliste, ermittelt die passenden Lernabschnitte und stößt die Verarbeitung der Leistungsdaten zur Matrix an.
	 *
	 * @param schuelerListe        Die Liste der Schüler.
	 * @param schuljahresabschnitt Der betrachtete Schuljahresabschnitt.
	 * @param filterFaecher        Das Filterkriterium für die Fächer.
	 */
	private void initialisiereMatrix(final List<ReportingSchueler> schuelerListe,
			final ReportingSchuljahresabschnitt schuljahresabschnitt, final Predicate<ReportingFach> filterFaecher) {
		if ((schuelerListe == null) || (schuljahresabschnitt == null))
			return;

		for (final ReportingSchueler s : schuelerListe) {
			final ReportingSchuelerLernabschnitt la = s.aktiverLernabschnittInSchuljahresabschnitt(schuljahresabschnitt);
			if (la == null)
				continue;

			this.schueler.add(s);
			this.mapSchuelerLernabschnitte.put(s.id(), la);
			verarbeiteLeistungsdaten(s.id(), la.leistungsdaten(), filterFaecher);
		}
	}

	/**
	 * Verarbeitet die Leistungsdaten eines Schülers, wendet Filter an und befüllt die Matrix-Datenstruktur.
	 *
	 * @param schuelerId     Die ID des Schülers.
	 * @param leistungsdaten Die Liste der Leistungsdaten aus dem Lernabschnitt.
	 * @param filterFaecher  Der anzuwendende Fachfilter.
	 */
	private void verarbeiteLeistungsdaten(final long schuelerId, final List<ReportingSchuelerLeistungsdaten> leistungsdaten,
			final Predicate<ReportingFach> filterFaecher) {
		if (leistungsdaten == null)
			return;

		for (final ReportingSchuelerLeistungsdaten daten : leistungsdaten) {
			final ReportingFach fach = daten.fach();
			if ((fach == null) || !filterFaecher.test(fach))
				continue;

			this.matrix.add(schuelerId, fach.id(), daten);
			this.mapAlleFaecher.putIfAbsent(fach.id(), fach);
		}
	}

	/**
	 * Gibt die Liste der tatsächlich berücksichtigten Schüler zurück.
	 *
	 * @return Die gefilterte Schülerliste.
	 */
	public List<ReportingSchueler> schueler() {
		return this.schueler;
	}

	/**
	 * Liefert den Lernabschnitt für einen Schüler, aus dem die Leistungsdaten entnommen wurden.
	 *
	 * @param schueler Der Schüler, für den der Lernabschnitt ermittelt werden soll.
	 *
	 * @return Der Lernabschnitt oder null, falls der Schüler nicht in der Matrix enthalten ist.
	 */
	public ReportingSchuelerLernabschnitt schuelerLernabschnitt(final ReportingSchueler schueler) {
		return this.mapSchuelerLernabschnitte.get(schueler.id());
	}

	/**
	 * Liefert für einen Schüler eine Liste von Leistungsdaten, die exakt der Reihenfolge der {@link #spaltenFaecher} entspricht.
	 * Nicht belegte Fächer werden in der Liste durch null repräsentiert, um die Spaltentreue zu wahren.
	 *
	 * @param schueler Der Schüler, dessen Zeile generiert werden soll.
	 *
	 * @return Eine Liste von Leistungsdaten (inkl. null-Werten für Lücken).
	 */
	public List<ReportingSchuelerLeistungsdaten> schuelerLeistungsdatenZeile(final ReportingSchueler schueler) {
		final List<ReportingSchuelerLeistungsdaten> zeile = new ArrayList<>();
		for (final ReportingFach fach : this.spaltenFaecher) {
			zeile.add(this.matrix.getSingle12OrNull(schueler.id(), fach.id()));
		}
		return zeile;
	}

	/**
	 * Gibt die Liste der in der Matrix enthaltenen Fächer (Spalten) zurück.
	 *
	 * @return Die sortierte Fächerliste.
	 */
	public List<ReportingFach> spaltenFaecher() {
		return this.spaltenFaecher;
	}


	/**
	 * Liefert die Liste aller Kürzel der in der Matrix enthaltenen Fächer.
	 *
	 * @return Liste der Fachkürzel.
	 */
	public List<String> spaltenFaecherKuerzel() {
		return this.spaltenFaecher.stream().map(ReportingFach::kuerzel).toList();
	}

	/**
	 * Prüft, ob für den angegebenen Schüler Einträge zur fachbezogenen Lernentwicklung vorliegen.
	 * Dabei werden nur die Fächer berücksichtigt, die aktuell in der Matrix (Spalten) enthalten sind.
	 *
	 * @param schueler Der zu prüfende Schüler.
	 *
	 * @return true, wenn mindestens ein Eintrag vorhanden ist.
	 */
	public boolean hatFachbezogeneLernentwicklung(final ReportingSchueler schueler) {
		for (final ReportingFach fach : this.spaltenFaecher) {
			final ReportingSchuelerLeistungsdaten daten = this.matrix.getSingle12OrNull(schueler.id(), fach.id());
			if ((daten != null) && (daten.textFachbezogeneLernentwicklung() != null) && !daten.textFachbezogeneLernentwicklung().isEmpty())
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob es in der Matrix mindestens einen Schüler gibt, der Einträge zur fachbezogenen Lernentwicklung hat.
	 *
	 * @return true, wenn mindestens ein Schüler einen Eintrag hat.
	 */
	public boolean hatSchuelerMitFachbezogenerLernentwicklung() {
		for (final ReportingSchueler s : this.schueler) {
			if (hatFachbezogeneLernentwicklung(s))
				return true;
		}
		return false;
	}


}
