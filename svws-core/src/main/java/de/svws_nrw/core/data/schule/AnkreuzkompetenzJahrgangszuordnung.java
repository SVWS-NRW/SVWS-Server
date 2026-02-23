package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieses Core-DTO beinhaltet die Zuordnung einer Ankreuzkompetenz zu einem Jahrgang.
 */
@XmlRootElement
@Schema(description = "die Zuordnung einer Ankreuzkompetenz zu einem Jahrgang")
@TranspilerDTO
public class AnkreuzkompetenzJahrgangszuordnung {

	/** Die ID der Zuordnung */
	@Schema(description = "die ID der Zuordnung", example = "2")
	public long id = -1;

	/** Die ID der Ankreuzkompetenz */
	@Schema(description = "die ID der Ankreuzkompetenz", example = "4")
	public long idAnkreuzkompetenz = -1;

	/** Die ID des Jahrgangs */
	@Schema(description = "die ID des Jahrgangs", example = "3")
	public long idJahrgang = -1;

}
