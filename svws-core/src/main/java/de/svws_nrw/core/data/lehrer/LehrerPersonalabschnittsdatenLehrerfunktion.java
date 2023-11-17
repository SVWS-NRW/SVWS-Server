package de.svws_nrw.core.data.lehrer;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die schulspezifischen Lehrerfunktionen für die Lehrerabschnittsdaten mit der angebenen ID.
 */
@XmlRootElement
@Schema(description = "Schulspezifische Lehrerfunktion bei Lehrerabschnittsdaten.")
@TranspilerDTO
public class LehrerPersonalabschnittsdatenLehrerfunktion {

	/** Die ID für diesen Eintrag. */
	@Schema(description = "Die ID für diesen Eintrag.", example = "4711")
	public long id = -1;

	/** Die ID der Lehrerabschnittsdaten. */
	@Schema(description = "Die ID der Lehrerabschnittsdaten.", example = "4712")
	public long idAbschnittsdaten = -1;

	/** Die ID in dem Katalog der schulspezifischen Lehrerfunktionen. */
	@Schema(description = "Die ID in dem Katalog der schulspezifischen Lehrerfunktionen.", example = "4713")
	public long idFunktion = -1;

}
