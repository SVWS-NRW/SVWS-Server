package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurplan.
 */
public class ReportingGostKlausurplanungKlausurplan extends ReportingBaseType {

	/** Ein Prädikat, das bestimmt, welche Schüler in der Ausgabe enthalten sind. */
	protected final Predicate<ReportingSchueler> filterSchueler;

	/** Ein Prädikat, das bestimmt, welche Kurse in der Ausgabe enthalten sind. */
	protected final Predicate<ReportingKurs> filterKurse;

	/** Ein Prädikat, das bestimmt, welche Klausurtermine in der Ausgabe enthalten sind. */
	protected final Predicate<ReportingGostKlausurplanungKlausurtermin> filterKlausurtermine;

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
	 * @param klausurtermine        Eine Liste, die alle Termine des Klausurplanes beinhaltet.
	 * @param kurse                 Eine Liste, die alle Kurse des Klausurplanes beinhaltet.
	 * @param kursklausuren         Eine Liste, die alle Kursklausuren des Klausurplanes beinhaltet.
	 * @param schueler              Eine Liste, die alle Schüler des Klausurplanes beinhaltet.
	 * @param schuelerklausuren     Eine Liste, die alle Schülerklausuren des Klausurplanes beinhaltet.
	 * @param filterSchueler        Ein Prädikat, das bestimmt, welche Schüler in der Ausgabe enthalten sind.
	 * @param filterKurse           Ein Prädikat, das bestimmt, welche Kurse in der Ausgabe enthalten sind.
	 * @param filterKlausurtermine  Ein Prädikat, das bestimmt, welche Klausurtermine in der Ausgabe enthalten sind.
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ReportingGostKlausurplanungKlausurplan(final List<ReportingGostKlausurplanungKlausurtermin> klausurtermine, final List<ReportingKurs> kurse,
			final List<ReportingGostKlausurplanungKursklausur> kursklausuren, final List<ReportingSchueler> schueler,
			final List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren,
			final Predicate<ReportingSchueler> filterSchueler, final Predicate<ReportingKurs> filterKurse,
			final Predicate<ReportingGostKlausurplanungKlausurtermin> filterKlausurtermine) {

		// Fülle die Basislisten mit den übergebenen Daten.
		this.schueler = (schueler != null) ? new ArrayList<>(schueler.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
		this.kurse = (kurse != null) ? new ArrayList<>(kurse.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
		this.klausurtermine = (klausurtermine != null) ? new ArrayList<>(klausurtermine.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
		this.kursklausuren = (kursklausuren != null) ? new ArrayList<>(kursklausuren.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
		this.schuelerklausuren =
				(schuelerklausuren != null) ? new ArrayList<>(schuelerklausuren.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();

		this.filterSchueler = (filterSchueler == null) ? s -> true : filterSchueler;
		this.filterKurse = (filterKurse == null) ? k -> true : filterKurse;
		this.filterKlausurtermine = (filterKlausurtermine == null) ? kt -> true : filterKlausurtermine;
	}


	// ##### Berechnete Methoden #####

	/**
	 * Eine Liste vom Typ String, die alle vorhandenen Datumsangaben der Termine des Klausurplanes beinhaltet (distinct).
	 *
	 * @return Liste der Datumsangaben der Klausurtermine
	 */
	public List<String> datumsangabenKlausurtermine() {
		return this.klausurtermine.stream().filter(filterKlausurtermine).filter(t -> !t.datum().isEmpty()).map(t -> t.datum).sorted().distinct().toList();
	}

	/**
	 * Eine Liste vom Typ GostKlausurplanungKlausurtermin, die alle Termine des Klausurplanes beinhaltet, denen bereits ein Datum zugewiesen wurde.
	 *
	 * @return Liste der Klausurtermine mit Datumsangabe
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermineMitDatum() {
		return this.klausurtermine.stream().filter(filterKlausurtermine).filter(t -> !t.datum().isEmpty()).toList();
	}

	/**
	 * Eine Liste vom Typ GostKlausurplanungKlausurtermin, die alle Termine des Klausurplanes beinhaltet, denen noch kein Datum zugewiesen wurde.
	 *
	 * @return Liste der Klausurtermine ohne Datumsangabe
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermineOhneDatum() {
		return this.klausurtermine.stream().filter(filterKlausurtermine).filter(t -> t.datum().isEmpty()).toList();
	}

	/**
	 * Eine Liste vom Typ GostKlausurplanungKlausurtermin, die alle Termine des Klausurplanes zum angegebenen Datum beinhaltet.
	 *
	 * @param  datum 	Datum, zu dem die Liste der Klausurtermine zurückgegeben werden soll.
	 *
	 * @return 			Liste der Klausurtermine mit dem gewünschten Datum
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermineZumDatum(final String datum) {
		if ((datum == null) || datum.isEmpty()) {
			return new ArrayList<>();
		}
		return this.klausurtermine.stream().filter(filterKlausurtermine).filter(t -> datum.equals(t.datum()))
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
		return (id < 0) ? null : this.klausurtermine.stream().filter(filterKlausurtermine).filter(t -> id == t.id).findFirst().orElse(null);
	}

	/**
	 * Gibt den Kurs zur übergebenen ID zurück
	 *
	 * @param  id 	Die ID des Kurses
	 *
	 * @return 		Der Kurs zur ID oder null, wenn nicht vorhanden.
	 */
	public ReportingKurs kurs(final long id) {
		return (id < 0) ? null : this.kurse.stream().filter(filterKurse).filter(k -> id == k.id()).findFirst().orElse(null);
	}

	/**
	 * Gibt die Kursklausur zur übergebenen ID zurück
	 *
	 * @param  id 	Die ID der Kursklausur
	 *
	 * @return 		Die Kursklausur zur ID oder null, wenn nicht vorhanden.
	 */
	public ReportingGostKlausurplanungKursklausur kursklausur(final long id) {
		return (id < 0) ? null : this.kursklausuren.stream().filter(k -> id == k.id()).findFirst().orElse(null);
	}

	/**
	 * Gibt den Schüler zur übergebenen ID zurück
	 *
	 * @param  id 	Die ID des Schülers
	 *
	 * @return 		Der Schüler zur ID oder null, wenn nicht vorhanden.
	 */
	public ReportingSchueler schueler(final long id) {
		return (id < 0) ? null : this.schueler.stream().filter(filterSchueler).filter(s -> id == s.id()).findFirst().orElse(null);
	}

	/**
	 * Gibt die Schülerklausur zur übergebenen ID zurück
	 *
	 * @param  id 	Die ID der Schülerklausur
	 *
	 * @return 		Die Schülerklausur zur ID oder null, wenn nicht vorhanden.
	 */
	public ReportingGostKlausurplanungSchuelerklausur schuelerklausur(final long id) {
		return (id < 0) ? null : this.schuelerklausuren.stream().filter(s -> id == s.id()).findFirst().orElse(null);
	}


	// ##### Getter #####

	/**
	 * Eine Liste, die alle Termine des Klausurplanes beinhaltet.
	 *
	 * @return Liste der Klausurtermine
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermine() {
		return this.klausurtermine.stream().filter(filterKlausurtermine).toList();
	}

	/**
	 * Eine Liste, die alle Kurse des Klausurplanes beinhaltet.
	 *
	 * @return Liste der Kurse
	 */
	public List<ReportingKurs> kurse() {
		return this.kurse.stream().filter(filterKurse).toList();
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
		return this.schueler.stream().filter(filterSchueler).toList();
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
