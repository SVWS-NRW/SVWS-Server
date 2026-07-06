package de.svws_nrw.core.data.reporting;

import java.util.ArrayList;
import java.util.List;
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
public class ReportingSortierungDefinitionGruppe {

	/** Die Bezeichnung der Sortierdefinition, die auch zur Anzeige in der UI verwendet werden kann. */
	@Schema(description = "Die Bezeichnung der Gruppe von Sortierdefinitionen, die auch zur Anzeige in der UI verwendet werden kann.",
			example = "Schülersortierung")
	public @NotNull String bezeichnung = "";

	/** Der Typname des zu sortierenden Reporting-Datentyps dieser Gruppe, z. B. 'ReportingSchueler' oder 'ReportingKlasse'. */
	@Schema(description = "Der Typname des zu sortierenden Reporting-Datentyps dieser Gruppe, z. B. 'ReportingSchueler'.", example = "ReportingSchueler")
	public @NotNull String typ = "";

	/** Gibt an, ob die Gruppe in der UI sichtbar sein soll. */
	@Schema(description = "Gibt an, ob die Gruppe in der UI sichtbar sein soll.", example = "true")
	public boolean uiIstSichtbar = true;

	/** Der mindestens erforderliche ServerMode (stable|beta|alpha|dev), damit die Gruppe in der UI verfügbar ist. Leer = in allen Modi verfügbar. */
	@Schema(description = "Der mindestens erforderliche ServerMode (stable|beta|alpha|dev), damit die Gruppe in der UI verfügbar ist. Leer = in allen Modi verfügbar.")
	public @NotNull String uiErforderlicherServerMode = "";

	/** Die IDs der Benutzerkompetenzen (OR-verknüpft), die zur Nutzung der Gruppe erforderlich sind. Leer = keine Kompetenz erforderlich. */
	@Schema(description = "Die IDs der Benutzerkompetenzen (OR-verknüpft), die zur Nutzung der Gruppe erforderlich sind. Leer = keine Kompetenz erforderlich.")
	public @NotNull List<Long> uiErforderlicheKompetenzen = new ArrayList<>();

	/** Eine Liste von Sortierdefinitionen, die in dieser Gruppe zur Verfügung stehen. */
	@Schema(description = "Eine Liste von Sortierdefinitionen, die in dieser Gruppe zur Verfügung stehen.", example = "[]")
	public @NotNull List<ReportingSortierungDefinition> sortierungDefinitionenOptionen = new ArrayList<>();

	/** Eine Liste von Sortierdefinitionen, die in dieser Gruppe ausgewählt wurden. */
	@Schema(description = " Eine Liste von Sortierdefinitionen, die in dieser Gruppe ausgewählt wurden.", example = "[]")
	public @NotNull List<ReportingSortierungDefinition> sortierungDefinitionen = new ArrayList<>();
}
