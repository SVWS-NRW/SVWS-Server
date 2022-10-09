package de.nrw.schule.svws.core.data.gost;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dies ist ein Core-Type für die Information zu der Fachwahl eines Schülers
 * in der gymnasialen Oberstufe. 
 */
@XmlRootElement
@Schema(description="Informationen zu einer Fachwahl eines Schülers in der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "fachID", "schuelerID", "kursartID", "istSchriftlich" })
@TranspilerDTO
public class GostFachwahl {

	/** Die ID des Faches */
	@Schema(required = true, description = "Die ID des Faches.", example="4711")
	public long fachID = -1;
	
	/** Die ID des Schülers */
	@Schema(required = true, description = "Die ID des Schülers.", example="4712")
	public long schuelerID = -1;

	/** Die ID der Kursart */
	@Schema(required = true, description = "Die ID der Kursart.", example="4713")
	public long kursartID = -1;
	
	/** Gibt an, ob die Fachwahl ein schriftlicher Kurs ist oder nicht */
	@Schema(required = true, description = "gibt an, ob die Fachwahl ein schriftlicher Kurs ist oder nicht", example="true")
	public boolean istSchriftlich = false;

}
