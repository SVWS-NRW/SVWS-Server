package de.svws_nrw.core.data.uv.export;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt den vollständigen Export einer Unterrichtsverteilung im Format UV v1.
 */
@Schema(description = "Diese Klasse beschreibt den vollständigen Export einer Unterrichtsverteilung im Format UV v1.")
@TranspilerDTO
public class UVv1Export {

	/** Die Revision des UVv1Export-Datenformates, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1 */
	@Schema(description = "Die Revision des UVv1Export-Datenformates, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1.",
			example = "2")
	public int uvExportRevision = 1;

	/** Die Grunddaten der UV, die für alle Planungsabschnitte gelten. */
	@Schema(description = "die Grunddaten der UV, die für alle Planungsabschnitte gelten")
	public @NotNull UVv1GrunddatenExport grunddaten = new UVv1GrunddatenExport();

	/** Ein Array mit den Planungsabschnitten der Unterrichtsverteilung. */
	@ArraySchema(schema = @Schema(implementation = UVv1PlanungsabschnittExport.class, description = "Ein Array mit den Planungsabschnitten der Unterrichtsverteilung"))
	public @NotNull List<UVv1PlanungsabschnittExport> planungsabschnitte = new ArrayList<>();

}
