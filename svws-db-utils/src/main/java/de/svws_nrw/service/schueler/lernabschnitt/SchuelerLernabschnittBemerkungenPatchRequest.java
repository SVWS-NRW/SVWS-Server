package de.svws_nrw.service.schueler.lernabschnitt;

import io.swagger.v3.oas.annotations.media.Schema;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Enthält die Bemerkungen, die beim Patchen der Bemerkungen von Schülerlernabschnittsdaten über den
 * {@link SchuelerLernabschnittService} geändert werden können.
 */
public class SchuelerLernabschnittBemerkungenPatchRequest {

	/** Der Text für allgemeine Zeugnisbemerkungen. */
	@Schema(description = "Der Text für allgemeine Zeugnisbemerkungen, oder null, um ihn zu entfernen.", example = "")
	public JsonNullable<String> zeugnisAllgemein = JsonNullable.undefined();

	/** Der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten. */
	@Schema(description = "Der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten, oder null, um ihn zu entfernen.", example = "")
	public JsonNullable<String> zeugnisASV = JsonNullable.undefined();

	/** Der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen. */
	@Schema(description = "Der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen, oder null, um ihn zu entfernen.", example = "")
	public JsonNullable<String> zeugnisLELS = JsonNullable.undefined();

	/** Der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement. */
	@Schema(description = "Der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement, oder null, um ihn zu entfernen.", example = "")
	public JsonNullable<String> zeugnisAUE = JsonNullable.undefined();

	/** Der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I. */
	@Schema(description = "Der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I, "
			+ "oder null, um ihn zu entfernen.", example = "")
	public JsonNullable<String> uebergangESF = JsonNullable.undefined();

	/** Eine Bemerkung zum Förderschwerpunkt. */
	@Schema(description = "Eine Bemerkung zum Förderschwerpunkt, oder null, um sie zu entfernen.", example = "")
	public JsonNullable<String> foerderschwerpunkt = JsonNullable.undefined();

	/** Eine Bemerkung zur Versetzungsentscheidung. */
	@Schema(description = "Eine Bemerkung zur Versetzungsentscheidung, oder null, um sie zu entfernen.", example = "")
	public JsonNullable<String> versetzungsentscheidung = JsonNullable.undefined();

}
