package de.svws_nrw.core.utils.reporting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import de.svws_nrw.core.data.reporting.ReportingFilterDefinition;
import de.svws_nrw.core.data.reporting.ReportingFilterEintrag;
import de.svws_nrw.core.data.reporting.ReportingFilterKriterium;
import de.svws_nrw.core.types.reporting.ReportingFilterOperation;
import de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung;

/**
 * Factory zum Erzeugen von ReportingFilterDefinitionen (inkl. Kriterien und Einträgen),
 * beispielsweise gedacht für die Definition der "FilterdefinitionenOptionen" in {@code ReportingReportvorlage}.
 */
public final class ReportingFilterDefinitionFactory {

	private ReportingFilterDefinitionFactory() {
		// utility class
	}

	/**
	 * Erzeugt eine {@link ReportingFilterDefinition} für einen Reporting-Datentyp.
	 *
	 * @param bezeichnung    die Bezeichnung der Filterdefinition (UI-Text)
	 * @param typ            der Typname des zu filternden Reporting-Datentyps (z. B. {@code "ReportingFach"})
	 * @param kriterien      die Filterkriterien (Top-Level), die auf den Datentyp angewendet werden sollen
	 *
	 * @return die neu erzeugte Filterdefinition
	 */
	public static @NotNull ReportingFilterDefinition definition(final @NotNull String bezeichnung, final @NotNull String typ, final @NotNull ReportingFilterKriterium... kriterien) {

		final ReportingFilterDefinition d = new ReportingFilterDefinition();
		d.bezeichnung = bezeichnung;
		d.typ = typ;
		d.kriterien = new ArrayList<>(Arrays.asList(kriterien));
		return d;
	}

	/**
	 * Methode zum Erzeugen der Liste von Filterdefinitionen
	 *
	 * @param definitionen die anzubietenden Filterdefinitionen
	 *
	 * @return eine veränderbare Liste mit den übergebenen Filterdefinitionen
	 */
	public static @NotNull List<ReportingFilterDefinition> definitionen(final @NotNull ReportingFilterDefinition... definitionen) {
		return new ArrayList<>(Arrays.asList(definitionen));
	}

	/**
	 * Liefert eine leere, veränderbare Liste für {@code filterdefinitionenOptionen}.
	 *
	 * @return leere Liste von Filterdefinitionen
	 */
	public static @NotNull List<ReportingFilterDefinition> keineDefinitionen() {
		return new ArrayList<>();
	}


	// ##### Kriterien (Gruppen) #####

	/**
	 * Erzeugt ein Filterkriterium, das die übergebenen Einträge mit {@code AND} verknüpft.
	 *
	 * @param eintraege die zu verknüpfenden Filtereinträge
	 *
	 * @return ein {@link ReportingFilterKriterium} mit {@code AND}-Verknüpfung
	 */
	public static @NotNull ReportingFilterKriterium and(final @NotNull ReportingFilterEintrag... eintraege) {
		return kriterium(ReportingFilterVerknuepfung.AND, Arrays.asList(eintraege), List.of(), false);
	}

	/**
	 * Erzeugt ein Filterkriterium, das die übergebenen Einträge mit {@code OR} verknüpft.
	 *
	 * @param eintraege die zu verknüpfenden Filtereinträge
	 *
	 * @return ein {@link ReportingFilterKriterium} mit {@code OR}-Verknüpfung
	 */
	public static @NotNull ReportingFilterKriterium or(final @NotNull ReportingFilterEintrag... eintraege) {
		return kriterium(ReportingFilterVerknuepfung.OR, Arrays.asList(eintraege), List.of(), false);
	}

	/**
	 * Erzeugt ein Filterkriterium, das die übergebenen Unterkriterien mit {@code AND} verknüpft.
	 * (Dabei enthält das Kriterium selbst keine direkten Einträge.)
	 *
	 * @param unterkriterien die zu verknüpfenden Unterkriterien
	 *
	 * @return ein {@link ReportingFilterKriterium} mit {@code AND}-Verknüpfung über Unterkriterien
	 */
	public static @NotNull ReportingFilterKriterium andMany(final @NotNull ReportingFilterKriterium... unterkriterien) {
		return kriterium(ReportingFilterVerknuepfung.AND, List.of(), Arrays.asList(unterkriterien), false);
	}

	/**
	 * Erzeugt ein Filterkriterium, das die übergebenen Unterkriterien mit {@code OR} verknüpft.
	 * (Dabei enthält das Kriterium selbst keine direkten Einträge.)
	 *
	 * @param unterkriterien die zu verknüpfenden Unterkriterien
	 *
	 * @return ein {@link ReportingFilterKriterium} mit {@code OR}-Verknüpfung über Unterkriterien
	 */
	public static @NotNull ReportingFilterKriterium orMany(final @NotNull ReportingFilterKriterium... unterkriterien) {
		return kriterium(ReportingFilterVerknuepfung.OR, List.of(), Arrays.asList(unterkriterien), false);
	}

	/**
	 * Negiert das übergebene Kriterium (entspricht einem logischen {@code NOT} der gesamten Gruppe).
	 *
	 * @param kriterium
	 *        das zu negierende Kriterium
	 *
	 * @return das gleiche Kriterium-Objekt (fluent), mit gesetzter Negation
	 */
	public static @NotNull ReportingFilterKriterium not(final @NotNull ReportingFilterKriterium kriterium) {
		kriterium.nicht = true;
		return kriterium;
	}

	/**
	 * Erzeugt ein Filterkriterium mit frei konfigurierbarer Verknüpfung, Einträgen und Unterkriterien.
	 *
	 * @param verknuepfung   logische Verknüpfung der enthaltenen Einträge und Unterkriterien
	 * @param eintraege      Liste der konkreten Filtereinträge (Attributvergleiche)
	 * @param unterkriterien Liste der Unterkriterien (verschachtelte Gruppen)
	 * @param nicht          {@code true}, falls das Gesamtergebnis dieser Gruppe negiert werden soll
	 *
	 * @return ein neu erzeugtes {@link ReportingFilterKriterium}
	 */
	public static @NotNull ReportingFilterKriterium kriterium(final @NotNull ReportingFilterVerknuepfung verknuepfung,
			final @NotNull List<ReportingFilterEintrag> eintraege, final @NotNull List<ReportingFilterKriterium> unterkriterien,
			final boolean nicht) {

		final ReportingFilterKriterium k = new ReportingFilterKriterium();
		k.verknuepfung = verknuepfung.getId();
		k.eintraege = new ArrayList<>(eintraege);
		k.unterkriterien = new ArrayList<>(unterkriterien);
		k.nicht = nicht;
		return k;
	}

	// ##### Einträge (Attribut + Operation + Werte) #####

	/**
	 * Erzeugt einen Filtereintrag für {@code attribut == wert}.
	 *
	 * @param attribut der Attributname, auf den gefiltert wird
	 * @param wert     der Vergleichswert
	 *
	 * @return ein {@link ReportingFilterEintrag} mit Operation {@code EQUAL}
	 */
	public static @NotNull ReportingFilterEintrag eq(final @NotNull String attribut, final @NotNull String wert) {
		return eintrag(attribut, ReportingFilterOperation.EQUAL, wert);
	}

	/**
	 * Erzeugt einen Filtereintrag für {@code attribut != wert}.
	 *
	 * @param attribut der Attributname, auf den gefiltert wird
	 * @param wert     der Vergleichswert
	 *
	 * @return ein {@link ReportingFilterEintrag} mit Operation {@code NOT_EQUAL}
	 */
	public static @NotNull ReportingFilterEintrag notEq(final @NotNull String attribut, final @NotNull String wert) {
		return eintrag(attribut, ReportingFilterOperation.NOT_EQUAL, wert);
	}

	/**
	 * Erzeugt einen Filtereintrag für {@code attribut IN (werte...)}.
	 *
	 * @param attribut der Attributname, auf den gefiltert wird
	 * @param werte    die Vergleichswerte
	 *
	 * @return ein {@link ReportingFilterEintrag} mit Operation {@code IN}
	 */
	public static @NotNull ReportingFilterEintrag in(final @NotNull String attribut, final @NotNull String... werte) {
		return eintrag(attribut, ReportingFilterOperation.IN, werte);
	}

	/**
	 * Erzeugt einen Filtereintrag für ein Attribut mit frei wählbarer Operation und Werten.
	 *
	 * @param attribut  der Attributname, auf den gefiltert wird
	 * @param operation die anzuwendende Filteroperation
	 * @param werte     die Vergleichswerte (als Liste, um z. B. {@code IN} zu unterstützen)
	 *
	 * @return ein {@link ReportingFilterEintrag}
	 */
	public static @NotNull ReportingFilterEintrag eintrag(final @NotNull String attribut, final @NotNull ReportingFilterOperation operation,
			final @NotNull String... werte) {

		final ReportingFilterEintrag eintrag = new ReportingFilterEintrag();
		eintrag.attribut = attribut;
		eintrag.operation = operation.getId();
		eintrag.werte = new ArrayList<>(Arrays.asList(werte));
		return eintrag;
	}
}
