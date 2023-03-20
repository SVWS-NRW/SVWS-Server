package de.nrw.schule.svws.core.data.gost;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Dieses DTO beschreibt die Informationen zu der Fachwahl eines Schülers 
 * in seinem Abiturjahrgang in Bezug auf ein Fach der gymnasialen Oberstufe.
 */
@XmlRootElement()
@Schema(description="Die Fachwahl eines Schüler zu einem Fach der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostSchuelerFachwahl {

	/** Die Fachwahlen des Schülers für die sechs Halbjahre der gymnasialen Oberstufe */
	@ArraySchema(schema = @Schema(implementation = String.class, description = "die Fachwahl des Schülers für das Halbjahr", example = "M"))
	public @NotNull String[] halbjahre = new String[6];

	/** Die Nummer des Abiturfaches (1-4), falls dieses Fach als Abiturfach gewählt wurde und ansonsten null */
	@Schema(description = "die Nummer des Abiturfaches (1-4), falls dieses Fach als Abiturfach gewählt wurde und ansonsten null", example="3")
	public Integer abiturFach;	
	
}
