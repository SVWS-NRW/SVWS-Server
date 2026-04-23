package de.svws_nrw.module.reporting.sortierung;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.svws_nrw.core.data.reporting.ReportingSortierungDefinition;
import de.svws_nrw.core.data.reporting.ReportingSortierungDefinitionGruppe;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;

/**
 * Zustandsloser Service zur Ermittlung von Sortierungsattributen aus den Reporting-Parametern.
 */
public class ReportingSortierungService {

	private final ReportingParameterTypisiert reportingParameterTypisiert;

	private final Logger logger;

	/**
	 * Erstellt einen neuen ReportingSortierungService.
	 *
	 * @param reportingParameterTypisiert Die typisierten Reporting-Parameter.
	 * @param logger                      Der Logger.
	 */
	public ReportingSortierungService(final ReportingParameterTypisiert reportingParameterTypisiert, final Logger logger) {
		this.reportingParameterTypisiert = reportingParameterTypisiert;
		this.logger = logger;
	}

	/**
	 * Ermittelt die Sortierattribute für einen bestimmten Typ aus den Reporting-Parametern. Dabei werden die Attribute bereinigt (Leerzeichen, Klammern).
	 * Falls keine benutzerdefinierte Sortierung vorliegt oder die Standardsortierung gewählt wurde, wird (je nach Parameter) die im System für diesen Typ
	 * definierte Standardsortierung zurückgegeben.
	 *
	 * @param typ                                Der Name des Typs (z. B. "ReportingSchueler"), welcher bspw. über class.getSimpleName() ermittelt werden kann.
	 * @param nutzeStandardsortierungAlsFallback Gibt an, ob die Standardsortierung bei fehlenden/fehlerhaften Attributen geladen werden soll.
	 *
	 * @return Eine Liste der bereinigten Attributnamen.
	 */
	public List<String> getSortierungsAttribute(final String typ, final boolean nutzeStandardsortierungAlsFallback) {
		// Finde die Sortierungsgruppe für den angefragten Typ
		final ReportingSortierungDefinitionGruppe gruppe =
				((this.reportingParameterTypisiert == null) || (this.reportingParameterTypisiert.sortierungDefinitionenGruppen() == null))
						? null
						: this.reportingParameterTypisiert.sortierungDefinitionenGruppen().stream()
								.filter(g -> (g != null) && Objects.equals(typ, g.typ))
								.findFirst()
								.orElse(null);

		// Verwende die erste ausgewählte Sortierungsdefinition aus der Gruppe
		final ReportingSortierungDefinition reportingSortierungDefinition =
				((gruppe == null) || (gruppe.sortierungDefinitionen == null) || gruppe.sortierungDefinitionen.isEmpty())
						? null
						: gruppe.sortierungDefinitionen.stream()
								.filter(Objects::nonNull)
								.findFirst()
								.orElse(null);

		// 1. Fall: Es gibt eine explizite benutzerdefinierte Sortierung mit vorhandenen Attributen
		if ((reportingSortierungDefinition != null) && Boolean.FALSE.equals(reportingSortierungDefinition.verwendeStandardsortierung)
				&& (reportingSortierungDefinition.attribute != null) && !reportingSortierungDefinition.attribute.isEmpty()) {
			return reportingSortierungDefinition.attribute.stream()
					.filter(Objects::nonNull)
					.map(sa -> sa.replace("()", "").trim())
					.filter(sa -> !sa.isBlank())
					.toList();
		}

		// 2. Fall: Es besteht der explizite Wunsch nach Standardsortierung in den Parametern ODER Fallback bei fehlender Definition
		if (((reportingSortierungDefinition != null) && Boolean.TRUE.equals(reportingSortierungDefinition.verwendeStandardsortierung))
				|| ((reportingSortierungDefinition == null) && nutzeStandardsortierungAlsFallback)) {
			return getStandardsortierungByTyp(typ);
		}

		// 3. Fall: Keine Sortierung gewünscht oder zulässig
		return new ArrayList<>();
	}

	/**
	 * Eine Hilfsmethode, um die Standardsortierung eines Typs automatisch aus der zuständigen Registry zu laden. Nutzt Reflection, um die statische Methode
	 * 'standardsortierung()' der Klasse 'de.svws_nrw.module.reporting.sortierung.SortierungRegistry<Typ>' aufzurufen.
	 *
	 * @param typ Der Name des Reporting-Typs (z. B. "ReportingSchueler"), welcher bspw. über class.getSimpleName() ermittelt werden kann.
	 *
	 * @return Die Liste der Standard-Sortierattribute oder eine leere Liste, falls keine Registry gefunden wurde.
	 */
	@SuppressWarnings("unchecked")
	private List<String> getStandardsortierungByTyp(final String typ) {
		if ((typ == null) || typ.isBlank()) {
			return new ArrayList<>();
		}
		try {
			final String className = "de.svws_nrw.module.reporting.sortierung.SortierungRegistry" + typ;
			final Class<?> clazz = Class.forName(className);
			final Method method = clazz.getMethod("standardsortierung");
			return (List<String>) method.invoke(null);
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.DEBUG, 8, "### HINWEIS: Keine SortierungRegistry oder Standardsortierung für Typ '" + typ + "' gefunden.");
			return new ArrayList<>();
		}
	}
}
