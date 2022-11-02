package de.nrw.schule.svws.core.data.schule;

import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert einen Eintrag für den Katalog der Fachklassen beim Berufskolleg.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Fachklassen beim Berufskolleg.")
@TranspilerDTO
public class BerufskollegFachklassenKatalogEintrag {

	/** Der Fachklassenschlüssel. */
	@Schema(required = true, description = "der Fachklassenschlüssel - Teil 1", example="620")
	public @NotNull String schluessel = "";

	/** Der Fachklassenschlüssel - Teil 2. */
	@Schema(required = true, description = "der Fachklassenschlüssel - Teil 2", example="00")
	public @NotNull String schluessel2 = "";

	/** Die Historie des Katalog-Eintrags. */
	@Schema(required = true, description = "die Historie des Katalog-Eintrags")
	public @NotNull List<@NotNull BerufskollegFachklassenKatalogDaten> historie = new Vector<>();

}
