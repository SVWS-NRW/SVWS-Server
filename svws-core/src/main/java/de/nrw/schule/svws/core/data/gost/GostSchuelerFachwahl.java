package de.nrw.schule.svws.core.data.gost;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Dieses DTO beschreibt die Informationen zu der Fachwahl eines Schülers 
 * in seinem Abiturjahrgang in Bezug auf ein Fach der gymnasialen Oberstufe.
 */
@XmlRootElement()
@Schema(description="Die Fachwahl eines Schüler zu einem Fach der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostSchuelerFachwahl {

	/** Die Fachwahl des Schülers für die EF.1 */
	@Schema(required = false, description = "die Fachwahl des Schülers für die EF.1", example="M")
	public String EF1;	

	/** Die Fachwahl des Schülers für die EF.2 */
	@Schema(required = false, description = "die Fachwahl des Schülers für die EF.2", example="M")
	public String EF2;	
	
	/** Die Fachwahl des Schülers für die Q1.1 */
	@Schema(required = false, description = "die Fachwahl des Schülers für die Q1.1", example="LK")
	public String Q11;	
	
	/** Die Fachwahl des Schülers für die Q1.2 */
	@Schema(required = false, description = "die Fachwahl des Schülers für die Q1.2", example="LK")
	public String Q12;	
	
	/** Die Fachwahl des Schülers für die Q2.1 */
	@Schema(required = false, description = "die Fachwahl des Schülers für die Q2.1", example="LK")
	public String Q21;	
	
	/** Die Fachwahl des Schülers für die Q2.2 */
	@Schema(required = false, description = "die Fachwahl des Schülers für die Q2.2", example="LK")
	public String Q22;	

	/** Die Nummer des Abiturfaches (1-4), falls dieses Fach als Abiturfach gewählt wurde und ansonsten null */
	@Schema(required = false, description = "die Nummer des Abiturfaches (1-4), falls dieses Fach als Abiturfach gewählt wurde und ansonsten null", example="3")
	public Integer abiturFach;	
	
}
