package de.svws_nrw.module.reporting.sortierung;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.svws_nrw.core.data.reporting.ReportingSortierungDefinition;
import de.svws_nrw.core.data.reporting.ReportingSortierungDefinitionGruppe;
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
	 * Falls keine benutzerdefinierte Sortierung vorliegt oder explizit die Standardsortierung gewünscht ist, wird die übergebene Fallback-Standardsortierung
	 * zurückgegeben.
	 *
	 * @param typ                       Der Name des Typs (z. B. "ReportingSchueler"), welcher bspw. über
	 *                                  {@code class.getSimpleName()} ermittelt werden kann.
	 * @param fallbackStandardsortierung Die explizit übergebene Standardsortierung. Wird verwendet, falls keine
	 *                                   benutzerdefinierte Sortierung vorliegt oder explizit die Standardsortierung
	 *                                   gewünscht ist. Bei {@code null} wird in diesen Fällen eine leere Liste
	 *                                   geliefert (keine Sortierung).
	 *
	 * @return Eine Liste der bereinigten Attributnamen.
	 */
	public List<String> getSortierungsAttribute(final String typ, final List<String> fallbackStandardsortierung) {
		final ReportingSortierungDefinitionGruppe gruppe =
				((this.reportingParameterTypisiert == null) || (this.reportingParameterTypisiert.sortierungDefinitionenGruppen() == null))
						? null
						: this.reportingParameterTypisiert.sortierungDefinitionenGruppen().stream()
								.filter(g -> (g != null) && Objects.equals(typ, g.typ))
								.findFirst()
								.orElse(null);

		final ReportingSortierungDefinition reportingSortierungDefinition =
				((gruppe == null) || (gruppe.sortierungDefinitionen == null) || gruppe.sortierungDefinitionen.isEmpty())
						? null
						: gruppe.sortierungDefinitionen.stream()
								.filter(Objects::nonNull)
								.findFirst()
								.orElse(null);

		// 1. Fall: Es gibt eine explizite benutzerdefinierte Sortierung mit vorhandenen Attributen
		if ((reportingSortierungDefinition != null) && !reportingSortierungDefinition.verwendeStandardsortierung
				&& (reportingSortierungDefinition.attribute != null) && !reportingSortierungDefinition.attribute.isEmpty()) {
			return reportingSortierungDefinition.attribute.stream()
					.filter(Objects::nonNull)
					.map(sa -> sa.replace("()", "").trim())
					.filter(sa -> !sa.isBlank())
					.toList();
		}

		// 2. Fall: Explizit Standardsortierung gewünscht ODER Fallback bei fehlender Definition
		if (((reportingSortierungDefinition != null) && reportingSortierungDefinition.verwendeStandardsortierung)
				|| ((reportingSortierungDefinition == null) && (fallbackStandardsortierung != null))) {
			return (fallbackStandardsortierung == null) ? new ArrayList<>() : fallbackStandardsortierung;
		}

		// 3. Fall: Keine Sortierung gewünscht oder zulässig
		return new ArrayList<>();
	}

	/**
	 * Liefert den konfigurierten {@link Logger}.
	 *
	 * @return Der Logger.
	 */
	public Logger logger() {
		return logger;
	}
}
