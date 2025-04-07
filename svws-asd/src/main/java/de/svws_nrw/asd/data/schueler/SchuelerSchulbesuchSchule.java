package de.svws_nrw.asd.data.schueler;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Schubesuchsdaten zu einer bisher besuchten Schule.
 */
@XmlRootElement
@Schema(description = "Ein Eintrag in der Liste der bisher besuchten Schulen.")
@TranspilerDTO
public class SchuelerSchulbesuchSchule {

	/** Die ID der Informationen zum vorigen Schulbesuch in der Datenbank. */
	@Schema(description = "die ID der Informationen zum vorigen Schulbesuch in der Datenbank", example = "226984", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die ID der Schule. */
	@Schema(description = "Die ID der Schule", example = "178947")
	public Long idSchule;

	/** Der Schlüssel des Bildungsganges/Schulgliederung an der Schule. */
	@Schema(description = "Der Schlüssel des Bildungsganges/Schulgliederung an der Schule", example = "***")
	public String schulgliederung;

	/** Die ID des Grundes für die Entlassung von der Schule. */
	@Schema(description = "die ID des Grundes für die Entlassung von der Schule", example = "2")
	public Long entlassgrundID;

	/** Die ID des Abschlusses, welcher an der Schule erworben wurde. */
	@Schema(description = "die ID des Abschlusses, welcher an der Schule erworben wurde", example = "OA")
	public String abschlussartID;

	/** Die ID der Organisationsform der Schule (z.B. für Halbtagsunterricht). */
	@Schema(description = "die ID der Organisationsform der Schule (z.B. für Halbtagsunterricht)", example = "1")
	public String organisationsFormID;

	/** Das Datum, ab dem die Schule besucht wurde. */
	@Schema(description = "das Datum, ab dem die Schule besucht wurde", example = "1907-12-01")
	public String datumVon;

	/** Das Datum, bis wann die Schule besucht wurde. */
	@Schema(description = "das Datum, bis wann die Schule besucht wurde", example = "1908-12-01")
	public String datumBis;

	/** Der Jahrgang, ab dem die Schule besucht wurde. */
	@Schema(description = "der Jahrgang, ab dem die Schule besucht wurde", example = "07")
	public String jahrgangVon;

	/** Der Jahrgang, bis zu dem die Schule besucht wurde. */
	@Schema(description = "der Jahrgang, bis zu dem die Schule besucht wurde", example = "07")
	public String jahrgangBis;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuelerSchulbesuchSchule() {
		// leer
	}

}
