package de.svws_nrw.module.reporting.filterung;

import java.util.Objects;

import de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;

/**
 * Zustandsloser Service zur Auflösung von Filtergruppen aus den Reporting-Parametern.
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
	 * Liefert den Filter in Form einer {@link ReportingFilterDefinitionGruppe} aus den Reporting-Parametern für den angegebenen Reporting-Typ.
	 *
	 * @param typ Der Name des Typs (z. B. "ReportingFach"), welcher bspw. über class.getSimpleName() ermittelt werden kann.
	 *
	 * @return Die Filtergruppe oder {@code null}, falls keine vorhanden ist.
	 */
	public ReportingFilterDefinitionGruppe getFilter(final String typ) {
		if ((this.reportingParameterTypisiert == null) || (this.reportingParameterTypisiert.filterDefinitionenGruppen() == null)
				|| this.reportingParameterTypisiert.filterDefinitionenGruppen().isEmpty()) {
			return null;
		}
		return this.reportingParameterTypisiert.filterDefinitionenGruppen().stream()
				.filter(g -> (g != null) && Objects.equals(typ, g.typ))
				.findFirst()
				.orElse(null);
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
		final ReportingFilterDefinitionGruppe gruppe = getFilter(typ);
		return (gruppe != null) && (gruppe.filterDefinitionen != null) && !gruppe.filterDefinitionen.isEmpty();
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
