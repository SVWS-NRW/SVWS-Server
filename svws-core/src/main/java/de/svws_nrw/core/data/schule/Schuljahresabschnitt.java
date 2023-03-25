package de.svws_nrw.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt einen Abschnitt eines Schuljahres.  
 */
@XmlRootElement
@Schema(description="Ein Abschnitt eines Schuljahres (z.B. Quartal / Halbjahr).")
@TranspilerDTO
public class Schuljahresabschnitt {

	/** Die ID des Schuljahresabschnittes */
	@Schema(required = true, description = "Das Schuljahr, in welchem der Abschnitt liegt", example="2024")
	public long id;
	
	/** Das Schuljahr, in welchem der Schuljahresabschnitt liegt */
	@Schema(required = true, description = "Das Schuljahr, in welchem der Abschnitt liegt", example="2024")
	public int schuljahr;	

	/** Die Nummer des Abschnitts im Schuljahr */
	@Schema(required = true, description = "Die Nummer des Abschnitts im Schuljahr", example="2")
	public int abschnitt;	
	
}
