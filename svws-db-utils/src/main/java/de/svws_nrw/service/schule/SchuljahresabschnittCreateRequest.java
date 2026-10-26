package de.svws_nrw.service.schule;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Die Daten für das Erstellen eines neuen Schuljahresabschnitts.
 */
public class SchuljahresabschnittCreateRequest {

	/** Das Schuljahr, in welchem der Schuljahresabschnitt liegt */
	@Schema(description = "Das Schuljahr, in welchem der Abschnitt liegt", example = "2024")
	@Min(1990)
	@Max(2100)
	public int schuljahr;

	/** Die Nummer des Abschnitts im Schuljahr */
	@Schema(description = "Die Nummer des Abschnitts im Schuljahr", example = "2")
	@Min(1)
	public int abschnitt;

	/** Die ID des vorigen Schuljahresabschnittes */
	@Schema(description = "Die ID des vorigen Schuljahresabschnittes", example = "4710")
	public Long idVorigerAbschnitt = null;

	/** Die ID des folgenden Schuljahresabschnittes */
	@Schema(description = "Die ID des folgenden Schuljahresabschnittes", example = "4712")
	public Long idFolgeAbschnitt = null;

}
