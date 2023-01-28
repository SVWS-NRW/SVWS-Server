package de.nrw.schule.svws.core.data.gost.klausuren;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu dem Stundenplan eines Schülers.  
 */
@XmlRootElement
@Schema(description="der Stundenplan eines Schülers.")
@TranspilerDTO
public class Klausurvorgabe {

	/** Die ID des Stundenplans. */
	@Schema(required = true, description = "die ID des Stundenplans", example="815")
	public long idVorgabe = -1;

	/** Die ID des Stundenplans. */
	@Schema(required = true, description = "die ID des Stundenplans", example="815")
	public int abiJahrgang = -1;

	/** Die ID des Stundenplans. */
	@Schema(required = true, description = "die ID des Stundenplans", example="815")
	public int halbjahr = -1;

	/** Die ID des Stundenplans. */
	@Schema(required = true, description = "die ID des Stundenplans", example="815")
	public @NotNull String kursartAllg = "";

	/** Die ID des Stundenplans. */
	@Schema(required = true, description = "die ID des Stundenplans", example="815")
	public int quartal = -1;
	
	/** Die ID des Stundenplans. */
	@Schema(required = true, description = "die ID des Stundenplans", example="815")
	public int dauer = -1;
	
	/** Die ID des Stundenplans. */
	@Schema(required = true, description = "die ID des Stundenplans", example="815")
	public int auswahlzeit = -1;
	
	/** Die ID des Stundenplans. */
	@Schema(required = true, description = "die ID des Stundenplans", example="815")
	public boolean istMdlPruefung = false;

	/** Die ID des Stundenplans. */
	@Schema(required = true, description = "die ID des Stundenplans", example="815")
	public boolean istAudioNotwendig = false;

	/** Die ID des Stundenplans. */
	@Schema(required = true, description = "die ID des Stundenplans", example="815")
	public boolean istVideoNotwendig = false;
	
	/** Die ID des Stundenplans. */
	@Schema(required = true, description = "die ID des Stundenplans", example="815")
	public String bemerkungVorgabe = "";


}
