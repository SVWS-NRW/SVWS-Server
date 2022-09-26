package de.nrw.schule.svws.core.data.schueler;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert ID, Nachname, Vorname und Geschlecht des Schülers mit der angegebenen ID.  
 */
@XmlRootElement
@Schema(description="Ein Schüler-Eintrag mit der ID, Nachname, Vorname und Geschlecht.")
@TranspilerDTO
public class Schueler {

	/** Die ID des Schülerdatensatzes. */
	@Schema(required = true, description = "die ID", example="4711")
	public long id;
	
	/** Der Nachname des Schülerdatensatzes. */
	@Schema(required = true, description = "der Nachname", example="Mustermann")
	public @NotNull String nachname = "";
	
	/** Der Vorname des Schülerdatensatzes. */
	@Schema(required = true, description = "der Vorname", example="Max")
	public @NotNull String vorname = "";
	
	/** Die ID des Geschlechtes */
	@Schema(required = true, description = "die ID des Geschlechtes", example="3")
	public int geschlecht;
	
}
