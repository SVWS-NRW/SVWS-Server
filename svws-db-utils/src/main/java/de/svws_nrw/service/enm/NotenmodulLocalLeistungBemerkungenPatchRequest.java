package de.svws_nrw.service.enm;

import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Die Informationenen zur Aktualisierung von Bemerkungen zu einem Schüler-Lernabschnitt im lokalen Notenmodul
 */
@Schema(description = "Die Informationenen zur Aktualisierung von Bemerkungen zu einem Schüler-Lernabschnitt im lokalen Notenmodul")
public class NotenmodulLocalLeistungBemerkungenPatchRequest {

	/** Informationen zum Arbeits- und Sozialverhalten */
	@Schema(description = "Informationen zum Arbeits- und Sozialverhalten.", example = "Text zum ASV")
	public JsonNullable<String> ASV = JsonNullable.undefined();

	/** Informationen zu dem Außerunterrichtlichen Engagement (AUE) */
	@Schema(description = "Informationen zu dem Außerunterrichtlichen Engagement (AUE).", example = "Text zum AUE")
	public JsonNullable<String> AUE = JsonNullable.undefined();

	/** Zeugnisbemerkungen */
	@Schema(description = "Zeugnisbemerkungen.", example = "Text der Zeugnisbemerkung")
	public JsonNullable<String> ZB = JsonNullable.undefined();

	/** Bemerkungen zur Lern und Leistungsentwicklung (LELS) in den Fächern */
	@Schema(description = "Bemerkungen zur Lern und Leistungsentwicklung (LELS) in den Fächern.", example = "Text zum LELS")
	public JsonNullable<String> LELS = JsonNullable.undefined();

	/** Schulform-Empfehlungen */
	@Schema(description = "Schulform-Empfehlungen.", example = "R")
	public JsonNullable<String> schulformEmpf = JsonNullable.undefined();

	/** Individuelle Bemerkungen zur Versetzung */
	@Schema(description = "Individuelle Bemerkungen zur Versetzung.", example = "Text zur Versetzung")
	public JsonNullable<String> individuelleVersetzungsbemerkungen = JsonNullable.undefined();

	/** Förderbemerkungen */
	@Schema(description = "Förderbemerkungen.", example = "Text zum Förderschwerpunkt")
	public JsonNullable<String> foerderbemerkungen = JsonNullable.undefined();

}
