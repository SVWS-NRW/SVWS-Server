package de.svws_nrw.core.data.schuljahresabschnitt.v1;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die externe Open-API-Schnittstelle verwendet.
 * Sie beschreibt einen Abschnitt eines Schuljahres.
 */
@XmlRootElement
@Schema(description = "Ein Abschnitt eines Schuljahres (z.B. Quartal / Halbjahr).")
@TranspilerDTO
public class SchuljahresabschnittV1 {

	/** Die ID des Schuljahresabschnittes */
	@Schema(description = "Die ID des Schuljahresabschnittes", example = "4711")
	public long id;

	/** Das Schuljahr, in welchem der Schuljahresabschnitt liegt */
	@Schema(description = "Das Schuljahr, in welchem der Abschnitt liegt", example = "2024")
	public int schuljahr;

	/** Die Nummer des Abschnitts im Schuljahr */
	@Schema(description = "Die Nummer des Abschnitts im Schuljahr", example = "2")
	public int abschnitt;

	/** Die ID des vorigen Schuljahresabschnittes */
	@Schema(description = "die ID des vorigen Schuljahresabschnittes", example = "4710", nullable = true)
	public Long idVorigerAbschnitt;

	/** Die ID des folgenden Schuljahresabschnittes */
	@Schema(description = "die ID des folgenden Schuljahresabschnittes", example = "4712", nullable = true)
	public Long idFolgeAbschnitt;
}
