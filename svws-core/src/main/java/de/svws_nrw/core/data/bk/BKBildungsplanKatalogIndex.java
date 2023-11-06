package de.svws_nrw.core.data.bk;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten des Katalogs der Lehrpläne beim Berufskolleg aus dem die
 * Lernfelder mit Zeitrichtwert und zugehörigen Bündelfächern hervorgeht.
 */
@XmlRootElement
@Schema(description = "der Katalog der Lehrpläne am BK.")
@TranspilerDTO
public class BKBildungsplanKatalogIndex {

	/** Der Index (Schulgliederung) für die Verknüpfung von einem Bildungsgang des Berufskollegs mit Fachklassen */
	@Schema(description = "der Index (Schulgliederung) für die Verknüpfung von einem Bildungsgang des Berufskollegs mit Fachklassen", example = "100")
	public int index;

	/** Die Version des Teilkatalogs. Diese wird bei Änderungen am Katalog erhöht. */
	@Schema(description = "die Version des Teilkatalogs. Diese wird bei Änderungen am Katalog erhöht", example = "3")
	public long version = -1;

	/** Die Einträge des Katalogs. */
	@Schema(description = "die Lehrpläne des Katalogs")
	public @NotNull List<@NotNull BKBildungsplanKatalogEintrag> lehrplaene = new ArrayList<>();

}
