package de.svws_nrw.core.data.bk.abi;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Eine Klasse für die Rückmeldung zu einer Markierung bei einem Ergebnis des Abiturmarkierungsergebnis in der gymnasialen Oberstufe.
 */
@XmlRootElement(name = "BkGymAbiturMarkierungsalgorithmusMarkierung")
@Schema(name = "BkGymAbiturMarkierungsalgorithmusMarkierung",
		description = "enthält die Informationen zu einer Markierung bei einem Ergebnis des Abiturmarkierungsergebnis")
@TranspilerDTO
public class BKGymAbiturMarkierungsalgorithmusMarkierung {

	/** Die ID des Faches, für welches die Markierung vorgenommen wurde */
	@Schema(description = "die ID des Faches, für welches die Markierung vorgenommen wurde", example = "16")
	public long fachID = -1;

	/** Das Halbjahr der Qualifikationsphase, für welches die Markierung vorgenommen oder nicht vorgenommen wurde (2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2) */
	@Schema(description = "das Halbjahr der Qualifikationsphase, für welches die Markierung vorgenommen oder nicht vorgenommen wurde (2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2)",
			example = "2")
	public int halbjahrID = -1;

	/** Die Punkte, die erreicht wurden */
	@Schema(description = "die Punkte, die erreicht wurden.", example = "10")
	public Integer punkte;
}
