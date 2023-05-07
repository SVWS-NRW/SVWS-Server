package de.svws_nrw.core.data.schema;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Verbindungsdaten zu einer Datenbank.
 */
@XmlRootElement
@Schema(description = "Datenbank-Verbindungsinformationen")
@TranspilerDTO
public class DatenbankVerbindungsdaten {

	/** Gibt den Benutzernamen für die Datenbank an. */
	@Schema(description = "Gibt den Benutzernamen für die Datenbank an.", example = "Admin")
    public String username;

	/** Gibt das Kennwort für die Datenbank an. */
	@Schema(description = "Gibt das Kennwort für die Datenbank an.", example = "Geheim")
    public String password;

	/**  Gibt den Ort der Datenbank an. */
	@Schema(description = "Gibt den Ort der Datenbank an.", example = "localhost:4711")
    public String location;

	/** Gibt den Schema-Namen der Datenbank an. */
	@Schema(description = "Gibt den Schema-Namen der Datenbank an.", example = "schild_nrw")
    public String schema;

}
