package de.svws_nrw.module.reporting.sortierung;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Bündelt für einen Reporting-Typ {@code T} dessen {@link SortierungRegistry} und Standardsortierung zu einer
 * gemeinsamen Konfiguration. Erlaubt es, Comparatoren typsicher zu erzeugen, ohne dass die Aufrufstelle die
 * Registry oder die Standardsortierung kennen muss.
 *
 * <p>Eine Instanz wird üblicherweise als {@code public static final SORTIERUNG} an dem zugehörigen Reporting-Typ
 * exponiert und in einer Begleit-Datei {@code Reporting<Typ>Sortierung.java} aufgebaut.</p>
 *
 * @param <T> Der Reporting-Typ, für den sortiert werden soll.
 */
public final class ReportingSortierung<T> {

	private final SortierungRegistry<T> registry;
	private final List<String> standard;

	private ReportingSortierung(final SortierungRegistry<T> registry, final List<String> standard) {
		this.registry = Objects.requireNonNull(registry, "registry");
		this.standard = (standard == null) ? List.of() : List.copyOf(standard);
	}

	/**
	 * Liefert die zugrundeliegende {@link SortierungRegistry}. Wird z. B. von {@code importiereRegistryEintraege}
	 * benötigt, um die Einträge eines anderen Reporting-Typs unter einem Prefix zu übernehmen.
	 *
	 * @return Die Registry dieses Reporting-Typs.
	 */
	public SortierungRegistry<T> registry() {
		return registry;
	}

	/**
	 * Liefert die Standardsortierung als Attributnamenliste (in Sortierreihenfolge).
	 *
	 * @return Eine unveränderliche Liste der Attributnamen der Standardsortierung.
	 */
	public List<String> standardsortierung() {
		return standard;
	}

	/**
	 * Erzeugt einen {@link Comparator} anhand extern übergebener Attribute. Unbekannte Attribute werden in
	 * {@code validierungsfehler} gesammelt.
	 *
	 * @param attribute Liste der Attributnamen (ggf. mit "-"/":desc"-Suffix für absteigende Sortierung).
	 * @param validierungsfehler Liste, in der Fehler zu unbekannten Attributen gesammelt werden. Darf {@code null} sein.
	 *
	 * @return Ein Comparator gemäß den übergebenen Attributen.
	 */
	public Comparator<T> comparator(final List<String> attribute, final List<String> validierungsfehler) {
		return ComparatorBuilder.build(registry, attribute, validierungsfehler);
	}

	/**
	 * Erzeugt einen {@link Comparator} anhand der intern definierten Standardsortierung. Da die Attribute aus dem
	 * Code selbst stammen, wäre ein unbekanntes Attribut ein Programmierfehler — er wird über
	 * {@link ComparatorBuilder#buildStrict(SortierungRegistry, List)} als {@link IllegalStateException} sichtbar.
	 *
	 * @return Ein Comparator gemäß der Standardsortierung dieses Typs.
	 */
	public Comparator<T> comparatorStandard() {
		return ComparatorBuilder.buildStrict(registry, standard);
	}

	/**
	 * Liefert einen {@link Comparator}, der die Eingabereihenfolge beibehält (Identität, keine Sortierung). Macht den
	 * Aufrufer-Wunsch „keine Sortierung" an der Aufrufstelle explizit.
	 *
	 * @return Ein Comparator, der zwei Elemente immer als gleich bewertet.
	 */
	public Comparator<T> comparatorIdentitaet() {
		return (a, b) -> 0;
	}

	/**
	 * Startet einen Builder für eine {@link ReportingSortierung}-Instanz.
	 *
	 * @param <T> Der Reporting-Typ.
	 *
	 * @return Ein neuer Builder.
	 */
	public static <T> Builder<T> builder() {
		return new Builder<>();
	}

	/**
	 * Builder für {@link ReportingSortierung}.
	 *
	 * @param <T> Der Reporting-Typ.
	 */
	public static final class Builder<T> {

		private SortierungRegistry<T> registry;
		private List<String> standard;

		private Builder() {
		}

		/**
		 * Setzt die {@link SortierungRegistry} für den Reporting-Typ.
		 *
		 * @param value Die Registry mit allen registrierten Attributen.
		 *
		 * @return Diesen Builder zur weiteren Verkettung.
		 */
		public Builder<T> registry(final SortierungRegistry<T> value) {
			this.registry = value;
			return this;
		}

		/**
		 * Setzt die Standardsortierung als Attributnamenliste.
		 *
		 * @param value Liste der Attributnamen in Sortierreihenfolge.
		 *
		 * @return Diesen Builder zur weiteren Verkettung.
		 */
		public Builder<T> standard(final List<String> value) {
			this.standard = value;
			return this;
		}

		/**
		 * Baut die {@link ReportingSortierung}-Instanz.
		 *
		 * @return Die fertige Instanz.
		 */
		public ReportingSortierung<T> build() {
			return new ReportingSortierung<>(registry, standard);
		}
	}
}
