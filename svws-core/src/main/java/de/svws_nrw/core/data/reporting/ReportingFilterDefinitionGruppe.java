package de.svws_nrw.core.data.reporting;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Beschreibt eine Gruppe von typenbasierten Sortierdefinitionen für einen Reporting-Datentyp.
 * Ein Beispiel wäre "ReportingSchueler".
 */
@XmlRootElement
@Schema(description = "Eine Gruppe von typenbasierte Sortierdefinitionen für einen Reporting-Datentyp.")
@TranspilerDTO
public class ReportingFilterDefinitionGruppe {

	/** Die Bezeichnung der Sortierdefinition, die auch zur Anzeige in der UI verwendet werden kann. */
	@Schema(description = "Die Bezeichnung der Gruppe von Filterdefinitionen, die auch zur Anzeige in der UI verwendet werden kann.", example = "Schülerstatus")
	public @NotNull String bezeichnung = "";

	/** Der Typname des zu sortierenden Reporting-Datentyps dieser Gruppe, z. B. 'ReportingSchueler' oder 'ReportingKlasse'. */
	@Schema(description = "Der Typname des zu filternden Reporting-Datentyps dieser Gruppe, z. B. 'ReportingSchueler'.", example = "ReportingSchueler")
	public @NotNull String typ = "";

	/** Gibt an, ob die Gruppe in der UI sichtbar sein soll. */
	@Schema(description = "Gibt an, ob die Gruppe in der UI sichtbar sein soll.", example = "true")
	public boolean uiIstSichtbar = true;

	/** Gibt an, ob mehrere Filterdefinitionen ausgewählt werden können. */
	@Schema(description = "Gibt an, ob mehrere Filterdefinitionen ausgewählt werden können.", example = "false")
	public boolean uiIstMultiselect = false;

	/** Gibt an, wie bei Mehrfachauswahl die verschiedenen Definitionen miteinander verknüpft werden sollen. */
	@Schema(description = "Gibt an, wie bei Mehrfachauswahl die verschiedenen Definitionen miteinander verknüpft werden sollen.", example = "1 = AND, 2 = OR")
	public int multiselectVerknuepfung = ReportingFilterVerknuepfung.AND.getId();

	/** Eine Liste von Filterdefinitionen, die in dieser Gruppe zur Verfügung stehen. */
	@Schema(description = "Eine Liste von Filterdefinitionen, die in dieser Gruppe zur Verfügung stehen.", example = "[]")
	public @NotNull List<ReportingFilterDefinition> filterDefinitionenOptionen = new ArrayList<>();

	/** Eine Liste von Filterdefinitionen, die in dieser Gruppe ausgewählt wurden. */
	@Schema(description = " Eine Liste von Filterdefinitionen, die in dieser Gruppe ausgewählt wurden.", example = "[]")
	public @NotNull List<ReportingFilterDefinition> filterDefinitionen = new ArrayList<>();
}
