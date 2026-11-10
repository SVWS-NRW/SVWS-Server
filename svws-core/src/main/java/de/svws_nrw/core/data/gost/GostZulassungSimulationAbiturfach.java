package de.svws_nrw.core.data.gost;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Die in der Simulation der Abiturzulassung eingegebenen Prüfungsergebnisse eines Abiturfaches.
 * Welches Fach Abiturfach ist, ergibt sich aus der Laufbahnplanung und wird hier nicht abgelegt.
 */
@XmlRootElement
@Schema(description = "Die in der Simulation der Abiturzulassung eingegebenen Prüfungsergebnisse eines Abiturfaches.")
@TranspilerDTO
public class GostZulassungSimulationAbiturfach {

	/** Die ID des Faches */
	@Schema(description = "die ID des Faches", example = "4711")
	public long idFach = -1;

	/** Die Notenpunkte der Abiturprüfung oder null, wenn nichts eingegeben wurde */
	@Schema(description = "die Notenpunkte der Abiturprüfung oder null, wenn nichts eingegeben wurde", example = "11")
	public Integer pruefungNotenpunkte = null;

	/** Gibt an, ob eine mündliche Bestehensprüfung angesetzt ist, oder null, wenn dies nicht festgelegt wurde */
	@Schema(description = "gibt an, ob eine mündliche Bestehensprüfung angesetzt ist, oder null, wenn dies nicht festgelegt wurde", example = "false")
	public Boolean muendlichBestehen = null;

	/** Gibt an, ob eine freiwillige mündliche Prüfung angesetzt ist, oder null, wenn dies nicht festgelegt wurde */
	@Schema(description = "gibt an, ob eine freiwillige mündliche Prüfung angesetzt ist, oder null, wenn dies nicht festgelegt wurde", example = "false")
	public Boolean muendlichFreiwillig = null;

	/** Die Reihenfolge der mündlichen Prüfung oder null, wenn nichts eingegeben wurde */
	@Schema(description = "die Reihenfolge der mündlichen Prüfung oder null, wenn nichts eingegeben wurde", example = "1")
	public Integer muendlichReihenfolge = null;

	/** Die Notenpunkte der mündlichen Prüfung oder null, wenn nichts eingegeben wurde */
	@Schema(description = "die Notenpunkte der mündlichen Prüfung oder null, wenn nichts eingegeben wurde", example = "11")
	public Integer muendlichNotenpunkte = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostZulassungSimulationAbiturfach() {
		// leer
	}

}
