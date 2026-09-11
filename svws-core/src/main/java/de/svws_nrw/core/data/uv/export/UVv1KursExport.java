package de.svws_nrw.core.data.uv.export;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt einen Kurs innerhalb eines Planungsabschnitts im UV-Export.
 */
@Schema(description = "Diese Klasse beschreibt einen Kurs innerhalb eines Planungsabschnitts im UV-Export.")
@TranspilerDTO
public class UVv1KursExport {

	/** Die UV-ID des Kurses. */
	@Schema(description = "die UV-ID des Kurses", example = "501")
	public long uvId = -1;

	/** Die ID des Schuljahresabschnitts. */
	@Schema(description = "die ID des Schuljahresabschnitts", example = "42")
	public long idSchuljahresabschnitt = -1;

	/** Die UV-ID des Faches. */
	@Schema(description = "die UV-ID des Faches", example = "12")
	public long fachUvId = -1;

	/** Die Kursart. */
	@Schema(description = "die Kursart", example = "GK")
	public @NotNull String kursart = "";

	/** Die Kursnummer. */
	@Schema(description = "die Kursnummer", example = "1")
	public int kursnummer = 0;

	/** Die UV-ID der zugehörigen Schülergruppe. */
	@Schema(description = "die UV-ID der zugehörigen Schülergruppe", example = "815")
	public long schuelergruppeUvId = -1;

}
