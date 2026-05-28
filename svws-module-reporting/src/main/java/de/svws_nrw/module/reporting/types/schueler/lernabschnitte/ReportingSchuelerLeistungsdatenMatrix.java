package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import de.svws_nrw.core.adt.map.ListMap2DLongKeys;
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

	/** Interne Map aller in der Matrix vorkommenden Fächer in der Reihenfolge der Spalten. */
	private final Map<Long, ReportingFach> mapAlleFaecher = new LinkedHashMap<>();

	/** Interne Matrix: Schüler-ID (Key1) und Fach-ID (Key2) auf Leistungsdaten. */
	private final ListMap2DLongKeys<ReportingSchuelerLeistungsdaten> matrix = new ListMap2DLongKeys<>();


	/**
	 * Erstellt eine neue Matrix für eine Liste von Schülern. Es werden nur Schüler berücksichtigt, die im angegebenen Schuljahresabschnitt einen Lernabschnitt besitzen.
	 * Die Schüler werden nach den übergebenen Sortierungsattributen sortiert. Die Spalten (Fächer) werden über die Sortierung der Leistungsdaten
	 * abgeleitet: Jedes Fach wird in der Reihenfolge seines ersten Auftretens im sortierten Leistungsdatenstrom als Spalte aufgenommen.
	 * Ist ein Sortierungsattribut null oder leer, wird die Standardsortierung verwendet.
	 *
	 * @param schueler                            Die Liste der potenziellen Schüler.
	 * @param schuljahresabschnitt                Der Schuljahresabschnitt, für den die Daten ermittelt werden sollen.
	 * @param comparatorLeistungsdaten            Der Comparator für die Sortierung der Leistungsdaten, aus der die Spaltenreihenfolge der Fächer
	 *                                            abgeleitet wird. Ist der Comparator null, wird die Standardsortierung verwendet.
	 * @param comparatorSchueler                  Der Comparator für die Sortierung der Zeilen mit den Schülern.
	 *                                            Ist der Comparator null, wird die Standardsortierung verwendet.
	 * @param filterLeistungsdaten                Ein Prädikat zum Filtern der Leistungsdaten. Nur Leistungsdaten, die den Filter passieren, werden in die Matrix
	 *                                            aufgenommen; die Spaltenmenge ergibt sich aus deren Fächern.
	 */
	public ReportingSchuelerLeistungsdatenMatrix(final List<ReportingSchueler> schueler, final ReportingSchuljahresabschnitt schuljahresabschnitt,
			final Comparator<ReportingSchuelerLeistungsdaten> comparatorLeistungsdaten, final Comparator<ReportingSchueler> comparatorSchueler,
			final Predicate<ReportingSchuelerLeistungsdaten> filterLeistungsdaten) {

		// Prüfe die übergebenen Comparatoren. Sind diese null, wähle die Standardsortierung aus der entsprechenden Registry.
		Comparator<ReportingSchuelerLeistungsdaten> compLeistungsdaten = comparatorLeistungsdaten;
		if (compLeistungsdaten == null) {
			compLeistungsdaten = ReportingSchuelerLeistungsdaten.SORTIERUNG.comparator(ReportingSchuelerLeistungsdaten.SORTIERUNG.standardsortierung(), new ArrayList<>());
		}
		Comparator<ReportingSchueler> compSchueler = comparatorSchueler;
		if (compSchueler == null) {
			compSchueler = ReportingSchueler.SORTIERUNG.comparator(ReportingSchueler.SORTIERUNG.standardsortierung(), new ArrayList<>());
		}

		// Filter vorbereiten
		final Predicate<ReportingSchuelerLeistungsdaten> effektiverFilter = (filterLeistungsdaten == null) ? l -> true : filterLeistungsdaten;

		// Matrix befüllen und alle gefilterten Leistungsdaten sammeln, um daraus die Spaltenreihenfolge abzuleiten.
		final List<ReportingSchuelerLeistungsdaten> alleLeistungsdaten = new ArrayList<>();
		initialisiereMatrix(schueler, schuljahresabschnitt, effektiverFilter, alleLeistungsdaten);

		// Schüler sortieren
		this.schueler.sort(compSchueler);

		// Spaltenreihenfolge aus der Sortierung der Leistungsdaten ableiten (First-Encounter im sortierten Strom).
		alleLeistungsdaten.sort(compLeistungsdaten);
		for (final ReportingSchuelerLeistungsdaten daten : alleLeistungsdaten) {
			this.mapAlleFaecher.putIfAbsent(daten.fach().id(), daten.fach());
		}
		this.spaltenFaecher = new ArrayList<>(this.mapAlleFaecher.values());
	}

	/**
	 * Durchläuft die Schülerliste, ermittelt die passenden Lernabschnitte und stößt die Verarbeitung der Leistungsdaten zur Matrix an.
	 *
	 * @param schuelerListe          Die Liste der Schüler.
	 * @param schuljahresabschnitt   Der betrachtete Schuljahresabschnitt.
	 * @param filterLeistungsdaten   Das Filterkriterium für die Leistungsdaten.
	 * @param alleLeistungsdaten     Sammler-Liste, in die alle die Matrix befüllenden Leistungsdaten zur späteren Sortierung aufgenommen werden.
	 */
	private void initialisiereMatrix(final List<ReportingSchueler> schuelerListe,
			final ReportingSchuljahresabschnitt schuljahresabschnitt,
			final Predicate<ReportingSchuelerLeistungsdaten> filterLeistungsdaten,
			final List<ReportingSchuelerLeistungsdaten> alleLeistungsdaten) {
		if ((schuelerListe == null) || (schuljahresabschnitt == null)) {
			return;
		}

		for (final ReportingSchueler s : schuelerListe) {
			final ReportingSchuelerLernabschnitt la = s.aktiverLernabschnittInSchuljahresabschnitt(schuljahresabschnitt);
			if (la == null) {
				continue;
			}

			this.schueler.add(s);
			this.mapSchuelerLernabschnitte.put(s.id(), la);
			verarbeiteLeistungsdaten(s.id(), la.leistungsdaten(), filterLeistungsdaten, alleLeistungsdaten);
		}
	}

	/**
	 * Verarbeitet die Leistungsdaten eines Schülers, wendet den Filter an und befüllt die Matrix-Datenstruktur.
	 *
	 * @param schuelerId             Die ID des Schülers.
	 * @param leistungsdaten         Die Liste der Leistungsdaten aus dem Lernabschnitt.
	 * @param filterLeistungsdaten   Der anzuwendende Filter für die Leistungsdaten.
	 * @param alleLeistungsdaten     Sammler-Liste, die alle die Matrix befüllenden Leistungsdaten zur späteren Sortierung aufnimmt.
	 */
	private void verarbeiteLeistungsdaten(final long schuelerId, final List<ReportingSchuelerLeistungsdaten> leistungsdaten,
			final Predicate<ReportingSchuelerLeistungsdaten> filterLeistungsdaten,
			final List<ReportingSchuelerLeistungsdaten> alleLeistungsdaten) {
		if (leistungsdaten == null) {
			return;
		}

		for (final ReportingSchuelerLeistungsdaten daten : leistungsdaten) {
			final ReportingFach fach = daten.fach();
			if ((fach == null) || !filterLeistungsdaten.test(daten)) {
				continue;
			}

			this.matrix.add(schuelerId, fach.id(), daten);
			alleLeistungsdaten.add(daten);
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
			if ((daten != null) && (daten.textFachbezogeneLernentwicklung() != null) && !daten.textFachbezogeneLernentwicklung().isEmpty()) {
				return true;
			}
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
			if (hatFachbezogeneLernentwicklung(s)) {
				return true;
			}
		}
		return false;
	}


}
