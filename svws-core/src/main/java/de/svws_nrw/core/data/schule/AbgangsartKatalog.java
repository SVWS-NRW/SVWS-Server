package de.svws_nrw.core.data.schule;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert den Katalog der Abgangsarten.  
 */
@XmlRootElement
@Schema(description = "der Katalog der Abgangsarten.")
@TranspilerDTO
public class AbgangsartKatalog {

	/** Die Version des Katalog. Diese wird bei Änderungen am Katalog um 1 erhöht. */
	@Schema(description = "die Version des Katalog. Diese wird bei Änderungen am Katalog um 1 erhöht", example = "3")
	public long version = -1;

	/** Die Einträge des Katalogs. */
	@Schema(description = "die Einträge des Katalogs")
	public @NotNull List<@NotNull AbgangsartKatalogEintrag> eintraege = new Vector<>();

}
