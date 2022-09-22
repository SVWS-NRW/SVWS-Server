package de.nrw.schule.svws.core.data.lehrer;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gütligen Statistikwerte für den Katalog der LehrerLeitungsfunktionen.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Lehrer-Leitungsfunktionen.")
@TranspilerDTO
public class LehrerKatalogLeitungsfunktionEintrag {

	/** Die ID des Leitungsfunktions-Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Leitungsfunktions-Katalog-Eintrags", example="4711")
	public long id;

	/** Die Bezeichnung der Leitungsfunktion. */
	@Schema(required = true, description = "die Bezeichnung der Leitungsfunktion", example="Schulleitung")
	public @NotNull String text = "";

}
