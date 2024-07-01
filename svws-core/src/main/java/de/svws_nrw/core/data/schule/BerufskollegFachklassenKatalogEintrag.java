package de.svws_nrw.core.data.schule;

import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert einen Eintrag für den Katalog der Fachklassen beim Berufskolleg.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Fachklassen beim Berufskolleg.")
@TranspilerDTO
public class BerufskollegFachklassenKatalogEintrag {

	/** Der Fachklassenschlüssel. */
	@Schema(description = "der Fachklassenschlüssel - Teil 1", example = "620")
	public @NotNull String schluessel = "";

	/** Der Fachklassenschlüssel - Teil 2. */
	@Schema(description = "der Fachklassenschlüssel - Teil 2", example = "00")
	public @NotNull String schluessel2 = "";

	/** Die Historie des Katalog-Eintrags. */
	@Schema(description = "die Historie des Katalog-Eintrags")
	public @NotNull List<BerufskollegFachklassenKatalogDaten> historie = new ArrayList<>();

}
