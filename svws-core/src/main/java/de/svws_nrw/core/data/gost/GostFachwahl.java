package de.svws_nrw.core.data.gost;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dies ist ein Core-Type für die Information zu der Fachwahl eines Schülers
 * in der gymnasialen Oberstufe.
 */
@XmlRootElement
@Schema(description = "Informationen zu einer Fachwahl eines Schülers in der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "fachID", "schuelerID", "kursartID", "istSchriftlich" })
@TranspilerDTO
public class GostFachwahl {

	/** Die ID des Faches */
	@Schema(description = "Die ID des Faches.", example = "4711")
	public long fachID = -1;

	/** Die ID des Schülers */
	@Schema(description = "Die ID des Schülers.", example = "4712")
	public long schuelerID = -1;

	/** Die ID der Kursart */
	@Schema(description = "Die ID der Kursart.", example = "4713")
	public int kursartID = -1;

	/** Gibt an, ob die Fachwahl ein schriftlicher Kurs ist oder nicht */
	@Schema(description = "gibt an, ob die Fachwahl ein schriftlicher Kurs ist oder nicht", example = "true")
	public boolean istSchriftlich = false;

	/** Gibt an, ob die Fachwahl als ein Abiturfach geplant ist oder nicht */
	@Schema(description = "gibt an, ob die Fachwahl als ein Abiturfach geplant ist oder nicht", example = "true")
	public Integer abiturfach = null;

}
