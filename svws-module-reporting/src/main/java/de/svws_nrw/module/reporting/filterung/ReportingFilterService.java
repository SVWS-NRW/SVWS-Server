package de.svws_nrw.module.reporting.filterung;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import de.svws_nrw.core.data.reporting.ReportingFilterDefinition;
import de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;

/**
 * Zustandsloser Service zur Erstellung von Filtern (Predicates) aus den Reporting-Parametern.
 */
public class ReportingFilterService {

	private final ReportingParameterTypisiert reportingParameterTypisiert;

	private final Logger logger;

	/**
	 * Erstellt einen neuen ReportingFilterService.
	 *
	 * @param reportingParameterTypisiert Die typisierten Reporting-Parameter.
	 * @param logger                      Der Logger.
	 */
	public ReportingFilterService(final ReportingParameterTypisiert reportingParameterTypisiert, final Logger logger) {
		this.reportingParameterTypisiert = reportingParameterTypisiert;
		this.logger = logger;
	}

	/**
	 * Erstellt einen Filter (Predicate) für einen bestimmten Typ basierend auf den Filterdefinitionen in den Reporting-Parametern.
	 *
	 * @param <T>                Der Typ der zu filternden Objekte.
	 * @param typ                Der Name des Typs (z. B. "ReportingFach"), welcher bspw. über class.getSimpleName() ermittelt werden kann.
	 * @param validierungsfehler Eine Liste, in der unbekannte Attribute während der Filtererstellung gesammelt werden (darf null sein).
	 *
	 * @return Ein {@link Predicate}, das die Filterkriterien anwendet. Falls keine Definition vorhanden ist, wird ein Filter zurückgegeben, der alles akzeptiert.
	 */
	public <T> Predicate<T> getFilter(final String typ, final List<String> validierungsfehler) {
		// Finde die Filtergruppe für den angefragten Typ
		final ReportingFilterDefinitionGruppe gruppe =
				((this.reportingParameterTypisiert == null) || (this.reportingParameterTypisiert.filterDefinitionenGruppen() == null)
						|| this.reportingParameterTypisiert.filterDefinitionenGruppen().isEmpty())
								? null
								: this.reportingParameterTypisiert.filterDefinitionenGruppen().stream()
										.filter(g -> (g != null) && Objects.equals(typ, g.typ))
										.findFirst()
										.orElse(null);

		if ((gruppe == null) || (gruppe.filterDefinitionen == null) || gruppe.filterDefinitionen.isEmpty()) {
			return t -> true;
		}

		final FilterRegistry<T> registry = getFilterRegistryByTyp(typ);
		if (registry == null) {
			return t -> true;
		}

		// Wenn nur eine Filterdefinition zurückgegeben wird, wird diese wie definiert verwendet.
		if (gruppe.filterDefinitionen.size() == 1) {
			return registry.erstelleFilter(gruppe.filterDefinitionen.getFirst(), validierungsfehler);
		}

		// Wenn mehrere Filterdefinitionen zurückgegeben werden, werden diese gemäß Verknüpfung kombiniert.
		final boolean istOr = ReportingFilterVerknuepfung.getByID(gruppe.multiselectVerknuepfung) == ReportingFilterVerknuepfung.OR;
		Predicate<T> result = istOr ? (t -> false) : (t -> true);

		for (final ReportingFilterDefinition def : gruppe.filterDefinitionen) {
			final Predicate<T> p = registry.erstelleFilter(def, validierungsfehler);
			result = istOr ? result.or(p) : result.and(p);
		}

		return result;
	}

	/**
	 * Prüft, ob für den angegebenen Reporting-Typ eine Filterdefinition mit mindestens einem Filterkriterium in den
	 * Reporting-Parametern vorliegt. Damit kann unterschieden werden, ob eine Ausgabe gefiltert oder unverändert erfolgen soll.
	 *
	 * @param typ Der Name des Typs (z. B. "ReportingSchueler"), welcher bspw. über class.getSimpleName() ermittelt werden kann.
	 *
	 * @return {@code true}, wenn für den Typ mindestens eine Filterdefinition mit mindestens einem Kriterium vorhanden ist, sonst {@code false}.
	 */
	public boolean hatFilter(final String typ) {
		if ((this.reportingParameterTypisiert == null) || (this.reportingParameterTypisiert.filterDefinitionenGruppen() == null)
				|| this.reportingParameterTypisiert.filterDefinitionenGruppen().isEmpty()) {
			return false;
		}
		final ReportingFilterDefinitionGruppe gruppe = this.reportingParameterTypisiert.filterDefinitionenGruppen().stream()
				.filter(g -> (g != null) && Objects.equals(typ, g.typ))
				.findFirst()
				.orElse(null);
		return (gruppe != null) && (gruppe.filterDefinitionen != null) && !gruppe.filterDefinitionen.isEmpty();
	}

	/**
	 * Eine Hilfsmethode, um die FilterRegistry eines Typs automatisch zu laden. Nutzt Reflection, um die statische Methode
	 * 'filterRegistry()' der Klasse 'de.svws_nrw.module.reporting.filterung.FilterRegistry<Typ>' aufzurufen.
	 *
	 * @param <T> Der Typ der Registry.
	 * @param typ Der Name des Reporting-Typs (z. B. "ReportingFach").
	 *
	 * @return Die FilterRegistry oder null, falls keine gefunden wurde.
	 */
	@SuppressWarnings("unchecked")
	private <T> FilterRegistry<T> getFilterRegistryByTyp(final String typ) {
		if ((typ == null) || typ.isBlank()) {
			return null;
		}
		try {
			final String className = "de.svws_nrw.module.reporting.filterung.FilterRegistry" + typ;
			final Class<?> clazz = Class.forName(className);
			final Method method = clazz.getMethod("filterRegistry");
			return (FilterRegistry<T>) method.invoke(null);
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.DEBUG, 8, "### HINWEIS: Keine FilterRegistry für Typ '" + typ + "' gefunden.");
			return null;
		}
	}
}
