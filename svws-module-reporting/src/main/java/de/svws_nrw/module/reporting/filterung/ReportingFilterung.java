package de.svws_nrw.module.reporting.filterung;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import de.svws_nrw.core.data.reporting.ReportingFilterDefinition;
import de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe;
import de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung;

/**
 * Bündelt für einen Reporting-Typ {@code T} dessen {@link FilterRegistry} und liefert daraus ein {@link Predicate}
 * für eine {@link ReportingFilterDefinitionGruppe}. Erlaubt es, Filter typsicher zu erzeugen, ohne dass die
 * Aufrufstelle die Registry kennen muss.
 *
 * <p>Eine Instanz wird üblicherweise als {@code public static final FILTER} an dem zugehörigen Reporting-Typ
 * exponiert und in einer Begleit-Datei {@code Reporting<Typ>Filter.java} aufgebaut.</p>
 *
 * @param <T> Der Reporting-Typ, für den gefiltert werden soll.
 */
public final class ReportingFilterung<T> {

	private final FilterRegistry<T> registry;

	private ReportingFilterung(final FilterRegistry<T> registry) {
		this.registry = Objects.requireNonNull(registry, "registry");
	}

	/**
	 * Liefert die zugrundeliegende {@link FilterRegistry}. Wird z. B. von {@code importiereRegistryEintraege}
	 * benötigt, um die Einträge eines anderen Reporting-Typs unter einem Prefix zu übernehmen.
	 *
	 * @return Die Registry dieses Reporting-Typs.
	 */
	public FilterRegistry<T> registry() {
		return registry;
	}

	/**
	 * Liefert ein {@link Predicate} für die übergebene Filtergruppe. Ist {@code gruppe == null} oder enthält sie keine
	 * Filterdefinitionen, wird ein Predicate zurückgegeben, das alle Objekte akzeptiert — die Daten bleiben damit
	 * ungefiltert. Mehrere Definitionen innerhalb der Gruppe werden gemäß {@link ReportingFilterDefinitionGruppe#multiselectVerknuepfung}
	 * mit OR oder AND kombiniert.
	 *
	 * @param gruppe Die Filtergruppe aus den ReportParametern. Darf {@code null} sein.
	 * @param validierungsfehler Liste, in der Namen unbekannter Attribute gesammelt werden. Darf {@code null} sein.
	 *
	 * @return Ein Predicate, das ein Objekt akzeptiert, falls es die Bedingungen der Gruppe erfüllt.
	 */
	public Predicate<T> bedingung(final ReportingFilterDefinitionGruppe gruppe, final List<String> validierungsfehler) {
		if ((gruppe == null) || (gruppe.filterDefinitionen == null) || gruppe.filterDefinitionen.isEmpty()) {
			return t -> true;
		}
		if (gruppe.filterDefinitionen.size() == 1) {
			return registry.erstelleFilter(gruppe.filterDefinitionen.getFirst(), validierungsfehler);
		}
		final boolean istOr = ReportingFilterVerknuepfung.getByID(gruppe.multiselectVerknuepfung) == ReportingFilterVerknuepfung.OR;
		Predicate<T> result = istOr ? (t -> false) : (t -> true);
		for (final ReportingFilterDefinition def : gruppe.filterDefinitionen) {
			final Predicate<T> p = registry.erstelleFilter(def, validierungsfehler);
			result = istOr ? result.or(p) : result.and(p);
		}
		return result;
	}

	/**
	 * Startet einen Builder für eine {@link ReportingFilterung}-Instanz.
	 *
	 * @param <T> Der Reporting-Typ.
	 *
	 * @return Ein neuer Builder.
	 */
	public static <T> Builder<T> builder() {
		return new Builder<>();
	}

	/**
	 * Builder für {@link ReportingFilterung}.
	 *
	 * @param <T> Der Reporting-Typ.
	 */
	public static final class Builder<T> {

		private FilterRegistry<T> registry;

		private Builder() {
		}

		/**
		 * Setzt die {@link FilterRegistry} für den Reporting-Typ.
		 *
		 * @param value Die Registry mit allen registrierten Attributen.
		 *
		 * @return Diesen Builder zur weiteren Verkettung.
		 */
		public Builder<T> registry(final FilterRegistry<T> value) {
			this.registry = value;
			return this;
		}

		/**
		 * Baut die {@link ReportingFilterung}-Instanz.
		 *
		 * @return Die fertige Instanz.
		 */
		public ReportingFilterung<T> build() {
			return new ReportingFilterung<>(registry);
		}
	}
}
