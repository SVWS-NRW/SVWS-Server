package de.svws_nrw.core.data.schule;

import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Anzahl und die Definition der einzelnen Abschnitte an der Schule.  
 */
@XmlRootElement
@Schema(description = "Die Abschnittsinformationen der Schule.")
@TranspilerDTO
public class SchuleAbschnitte {

	/** Die Anzahl der Abschnitte pro Jahr */
	@Schema(description = "Anzahl der Abschnitte pro Jahr", example = "2")
	public long anzahlAbschnitte;
	
	/** Die allgemeine Bezeichnung der Abschnitte (z.B. Quartal oder Halbjahr) */
	@Schema(description = "allgemeine Bezeichnung der Abschnitte", example = "Halbjahr")
	public @NotNull String abschnittBez = "Halbjahr";
	
	/** Eine Liste der einzelnen speziellen Bezeichnungnen für dei Abschnitte (z.B. 1. Quartal, 2. Quartal, ...) */
	@Schema(description = "Bezeichnungen für die Abschnitte", example = "1. Quartal")
	public @NotNull Vector<@NotNull String> bezAbschnitte = new Vector<>();
		
}
