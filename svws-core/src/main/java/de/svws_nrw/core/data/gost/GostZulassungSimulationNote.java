package de.svws_nrw.core.data.gost;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Die in der Simulation der Abiturzulassung eingegebenen Notenpunkte eines Faches in einem Halbjahr der Qualifikationsphase.
 */
@XmlRootElement
@Schema(description = "Die in der Simulation der Abiturzulassung eingegebenen Notenpunkte eines Faches in einem Halbjahr.")
@TranspilerDTO
public class GostZulassungSimulationNote {

	/** Die ID des Faches */
	@Schema(description = "die ID des Faches", example = "4711")
	public long idFach = -1;

	/** Die ID des Halbjahres der gymnasialen Oberstufe (2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2) */
	@Schema(description = "die ID des Halbjahres der gymnasialen Oberstufe (2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2)", example = "2")
	public int halbjahr = -1;

	/** Die eingegebenen Notenpunkte oder null, wenn nichts eingegeben wurde */
	@Schema(description = "die eingegebenen Notenpunkte oder null, wenn nichts eingegeben wurde", example = "11")
	public Integer notenpunkte = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostZulassungSimulationNote() {
		// leer
	}

}
