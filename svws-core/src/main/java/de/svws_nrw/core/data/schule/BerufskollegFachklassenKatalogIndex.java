package de.svws_nrw.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten des Teilkatalogs eines Index von Fachklassen beim Berufskolleg.
 */
@XmlRootElement
@Schema(description = "der Teilkatalog für einen Index von Fachklassen beim Berufskolleg.")
@TranspilerDTO
public class BerufskollegFachklassenKatalogIndex {

	/** Der Index für die Verknüpfung von einem Bildungsgang des Berufskollegs mit Fachklassen */
	@Schema(description = "der Index für die Verknüpfung von einem Bildungsgang des Berufskollegs mit Fachklassen", example = "100")
	public int index;

	/** Die Version des Teilkatalogs. Diese wird bei Änderungen am Katalog erhöht. */
	@Schema(description = "die Version des Teilkatalogs. Diese wird bei Änderungen am Katalog erhöht", example = "3")
	public long version = -1;

	/** Die Einträge des Katalogs. */
	@Schema(description = "die Fachklassen des Katalogs")
	public @NotNull List<@NotNull BerufskollegFachklassenKatalogEintrag> fachklassen = new Vector<>();

}
