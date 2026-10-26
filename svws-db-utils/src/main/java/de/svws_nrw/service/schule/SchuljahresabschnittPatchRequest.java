package de.svws_nrw.service.schule;

import io.swagger.v3.oas.annotations.media.Schema;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Die Daten für die partielle Änderung eines Schuljahresabschnitts. Schuljahr und Abschnitt bilden die
 * fachliche Identität eines Schuljahresabschnitts und können daher nicht geändert werden.
 */
public class SchuljahresabschnittPatchRequest {

	/** Die ID des vorigen Schuljahresabschnittes. Ein explizites null löst die Verknüpfung. */
	@Schema(description = "Die ID des vorigen Schuljahresabschnittes. Ein explizites null löst die Verknüpfung.", example = "4710")
	public JsonNullable<Long> idVorigerAbschnitt = JsonNullable.undefined();

	/** Die ID des folgenden Schuljahresabschnittes. Ein explizites null löst die Verknüpfung. */
	@Schema(description = "Die ID des folgenden Schuljahresabschnittes. Ein explizites null löst die Verknüpfung.", example = "4712")
	public JsonNullable<Long> idFolgeAbschnitt = JsonNullable.undefined();

}
