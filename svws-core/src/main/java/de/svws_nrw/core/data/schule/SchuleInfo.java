package de.svws_nrw.core.data.schule;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie gibt die Schulnummer, Schulform, den Namen und die Adresse der Schule zurück.
 */
@XmlRootElement
@Schema(description = "Grundlegende Daten zu einer Schule.")
@TranspilerDTO
public class SchuleInfo {

	/** Die eindeutige Schulnummer der Schule */
	@Schema(description = "die Schulnummer der Schule", example = "123456")
	public long schulNr;

	/** Die Schulform der Schule */
	@Schema(description = "Das Kürzel der Schulform", example = "GY")
	public @NotNull String schulform = "";

	/** Die Bezeichnung der Schule */
	@Schema(description = "die Bezeichnung der Schule", example = "Städt. Gymnasium")
	public @NotNull String bezeichnung = "";

	/** Der Straßenname der Straße in der die Schule liegt. */
	@Schema(description = "der Straßenname der Straße in der die Schule liegt.", example = "Musterweg")
	public String strassenname;

	/** Die Hausnummer zur Straße in der die Schule liegt. */
	@Schema(description = "Ggf. die Hausnummer zur Straße in der die Schule liegt.", example = "4711")
	public String hausnummer;

	/** Ggf. der Hausnummerzusatz zur Straße in der die Schule liegt. */
	@Schema(description = "Ggf. der Hausnummerzusatz zur Straße in der die Schule liegt.", example = "a-d")
	public String hausnummerZusatz;

	/** Die Postleitzahl des Gebietes in dem die Schule liegt. */
	@Schema(description = "die Postleitzahl der Schule", example = "42287")
	public String plz;

	/** Der Ort in dem die Schule liegt. */
	@Schema(description = "der Ort der Schule", example = "Düsseldorf")
	public String ort;

}
