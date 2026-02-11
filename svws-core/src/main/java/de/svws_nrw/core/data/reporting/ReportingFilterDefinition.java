package de.svws_nrw.core.data.reporting;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Definition von Filtern für einen bestimmten Reporting-Datentyp.
 */
@XmlRootElement
@Schema(description = "Filterdefinition für einen Reporting-Datentyp.")
@TranspilerDTO
public class ReportingFilterDefinition {

	/** Der Typname des zu filternden Reporting-Datentyps, z. B. 'ReportingFach'. */
	@Schema(description = "Der Typname des zu filternden Reporting-Datentyps, z. B. 'ReportingFach'.", example = "ReportingFach")
	public @NotNull String typ = "";

	/** Liste der Filterkriterien. */
	@Schema(description = "Liste der Filterkriterien.")
	public @NotNull List<ReportingFilterKriterium> kriterien = new ArrayList<>();

}
