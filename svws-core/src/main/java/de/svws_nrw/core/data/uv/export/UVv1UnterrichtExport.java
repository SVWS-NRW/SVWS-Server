package de.svws_nrw.core.data.uv.export;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt eine Unterrichtseinheit innerhalb eines Planungsabschnitts im UV-Export.
 */
@Schema(description = "Diese Klasse beschreibt eine Unterrichtseinheit innerhalb eines Planungsabschnitts im UV-Export.")
@TranspilerDTO
public class UVv1UnterrichtExport {

	/** Die UV-ID des Unterrichts. */
	@Schema(description = "die UV-ID des Unterrichts", example = "4711")
	public long uvId = -1;

	/** Die UV-ID des Zeitraster-Eintrags. */
	@Schema(description = "die UV-ID des Zeitraster-Eintrags", example = "2301")
	public Long zeitrasterEintragUvId = null;

	/** Die UV-ID der Lerngruppe. */
	@Schema(description = "die UV-ID der Lerngruppe", example = "3101")
	public long lerngruppeUvId = -1;

	/** Ein Array mit den UV-IDs der zugewiesenen Räume. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit den UV-IDs der zugewiesenen Räume"))
	public @NotNull List<Long> raumUvIds = new ArrayList<>();

	/** Ein Array mit den UV-IDs der zugewiesenen Lerngruppenlehrer. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit den UV-IDs der zugewiesenen Lerngruppenlehrer"))
	public @NotNull List<Long> lerngruppenlehrerUvIds = new ArrayList<>();

}
