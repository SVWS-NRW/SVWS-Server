package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieses DTO beinhaltet die Information zu einer Fachklasse.
 */
@XmlRootElement
@Schema(description = "Dieses DTO beinhaltet die Information zu einer Fachklasse.")
@TranspilerDTO
public class FachklasseEintrag {

	/** Die ID */
	@Schema(description = "id", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die Bezeichnung */
	@Schema(description = "Die Bezeichnung", example = "Anlagenmechaniker/-in")
	@NotNull
	public String bezeichnung = "";

	/** Das Kürzel */
	@Schema(description = "Das Kürzel", example = "AM")
	@NotNull
	public String kuerzel = "";

	/** Die ID der Fachklasse (CoreType) */
	@Schema(description = "Die ID der Fachklasse (CoreType)", example = "7000")
	public Long idFachklasse;

	/** Die Sichtbarkeit */
	@Schema(description = "Die Sichtbarkeit", example = "true")
	public boolean istSichtbar;

	/** Die Sortierung */
	@Schema(description = "Die Sortierung", example = "32000")
	public int sortierung;

}
