package de.svws_nrw.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert den gesamten Katalog der Fachklassen beim Berufskolleg.
 */
@XmlRootElement
@Schema(description = "der gesamte Katalog der Fachklassen beim Berufskolleg.")
@TranspilerDTO
public class BerufskollegFachklassenKatalog {

	/** Die Version des Katalogs. Diese wird bei Änderungen am Katalog erhöht. */
	@Schema(description = "die Version des Katalogs. Diese wird bei Änderungen am Katalog erhöht", example = "37")
	public long version = 0;

	/** Die Teilkataloge in Abhängigkeit vom Index der Fachklassen. */
	@Schema(description = "die Teilkataloge in Abhängigkeit vom Index der Fachklassen")
	public @NotNull List<@NotNull BerufskollegFachklassenKatalogIndex> indizes = new Vector<>();

}
