package de.svws_nrw.service.schule.katalog.ankreuzkompetenz;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.core.data.kataloge.AnkreuzkompetenzKonfiguration;

/**
 * Der Patch-Request für {@link AnkreuzkompetenzKonfiguration}
 */
public class AnkreuzkompetenzKonfigurationPatchRequest {

	/** Die ID des Lehramteintrags des Lehrers. */
	@Schema(description = "Die ID des Lehramteintrags des Lehrers.", example = "4712")
	@NotNull
	public JsonNullable<@NotNull @Size(min = 5, max = 5) String[]> textStufen = JsonNullable.undefined();

	/** Der für die frei definierbare Zeugnisrubrik "Sonstiges" zu verwendenden Text. */
	@Schema(description = "Der für die frei definierbare Zeugnisrubrik \"Sonstiges\" zu verwendenden Text.", example = "100815")
	@NotNull
	public JsonNullable<String> textSonstiges = JsonNullable.undefined();

}
