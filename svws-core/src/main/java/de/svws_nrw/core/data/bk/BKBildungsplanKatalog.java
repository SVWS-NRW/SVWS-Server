package de.svws_nrw.core.data.bk;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten der Teilkataloge der Lehrpläne für ein Gliederungsindex beim Berufskolleg,
 * in dem die zugehörigen Lehrpläne eingetragen sind.
 */
@XmlRootElement
@Schema(description = "ein Teilkatalog der Lehrpläne für einen Gliederungsindex am BK.")
@TranspilerDTO
public class BKBildungsplanKatalog {

	/** Die Version des Katalogs. Diese wird bei Änderungen am Katalog erhöht. */
	@Schema(description = "die Version des Katalogs. Diese wird bei Änderungen am Katalog erhöht", example = "3")
	public long version = -1;

	/** Die Einträge des Katalogs. */
	@Schema(description = "die Lehrpläne des Katalogs")
	public @NotNull List<@NotNull BKBildungsplanKatalogEintrag> lehrplaene = new ArrayList<>();

}
