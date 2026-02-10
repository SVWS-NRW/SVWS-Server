package de.svws_nrw.core.data.reporting;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.types.reporting.ReportingFilterOperation;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Ein konkreter Eintrag in einem Filterkriterium, der einen Vergleich auf ein Attribut beschreibt.
 */
@XmlRootElement
@Schema(description = "Ein konkreter Eintrag in einem Filterkriterium, der einen Vergleich auf ein Attribut beschreibt.")
@TranspilerDTO
public class ReportingFilterEintrag {

	/** Der Name des Attributs, auf das gefiltert werden soll (z. B. 'kuerzel'). */
	@Schema(description = "Der Name des Attributs, auf das gefiltert werden soll (z. B. 'kuerzel').", example = "kuerzel")
	public @NotNull String attribut = "";

	/** Die Operation, die angewendet werden soll. */
	@Schema(description = "Die Filter-Operation, die angewendet werden soll.", example = "1")
	public int operation = ReportingFilterOperation.EQUAL.getId();

	/** Die Werte für den Filter (als Liste, um auch Bedingungen für Operationen wie 'IN' zu ermöglichen). */
	@Schema(description = "Die Vergleichswerte für den Filter.", example = "[\"D\"]")
	public @NotNull List<String> werte = new ArrayList<>();

}
