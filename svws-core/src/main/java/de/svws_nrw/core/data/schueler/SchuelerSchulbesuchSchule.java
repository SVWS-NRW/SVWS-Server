package de.svws_nrw.core.data.schueler;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Schubesuchsdaten zu einer bisher besuchten Schule.  
 */
@XmlRootElement
@Schema(description="Ein Eintrag in der Liste der bisher besuchtn Schulen.")
@TranspilerDTO
public class SchuelerSchulbesuchSchule {

	/** Die Schulnummer der Schule. */
	@Schema(required = true, description = "die Schulnummer der Schule", example="178947")
	public @NotNull String schulnummer = "";

	/** Die ID des Bildungsganges/Schulgliederung an der Schule. */
	@Schema(required = true, description = "die ID des Bildungsganges/Schulgliederung an der Schule", example="***")
	public String schulgliederung;
	
	/** Die ID des Grundes für die Entlassung von der Schule. */
	@Schema(required = true, description = "die ID des Grundes für die Entlassung von der Schule", example="2")
	public Long entlassgrundID;
	
	/** Die ID des Abschlusses, welcher an der Schule erworben wurde. */
	@Schema(required = true, description = "die ID des Abschlusses, welcher an der Schule erworben wurde", example="OA")
	public String abschlussartID;
	
	/** Die ID der Organisationsform der Schule (z.B. für Halbtagsunterricht). */
	@Schema(required = true, description = "die ID der Organisationsform der Schule (z.B. für Halbtagsunterricht)", example="1")
	public String organisationsFormID;

	/** Das Datum, ab dem die Schule besucht wurde. */
	@Schema(required = true, description = "das Datum, ab dem die Schule besucht wurde", example="1907-12-01")
	public String datumVon;

	/** Das Datum, bis wann die Schule besucht wurde. */
	@Schema(required = true, description = "das Datum, bis wann die Schule besucht wurde", example="1908-12-01")
	public String datumBis;

	/** Der Jahrgang, ab dem die Schule besucht wurde. */
	@Schema(required = true, description = "der Jahrgang, ab dem die Schule besucht wurde", example="07")
	public String jahrgangVon;

	/** Der Jahrgang, bis zu dem die Schule besucht wurde. */
	@Schema(required = true, description = "der Jahrgang, bis zu dem die Schule besucht wurde", example="07")
	public String jahrgangBis;
	
}
