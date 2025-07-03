package de.svws_nrw.core.abschluss.gost;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Eine Klasse für die Rückmeldung zu einer Markierung bei einem Ergebnis des Abiturmarkierungsergebnis in der gymnasialen Oberstufe.
 */
@XmlRootElement(name = "GostAbiturMarkierungsalgorithmusMarkierung")
@Schema(name = "GostAbiturMarkierungsalgorithmusMarkierung",
		description = "enthält die Informationen zu einer Markierung bei einem Ergebnis des Abiturmarkierungsergebnis")
@TranspilerDTO
public class GostAbiturMarkierungsalgorithmusMarkierung {

	/** Die ID des Faches, für welches die Markierung vorgenommen wurde */
	@Schema(description = "die ID des Faches, für welches die Markierung vorgenommen wurde", example = "16")
	public long idFach = -1;

	/** Das Halbjahr der Qualifikationsphase, für welches die Markierung vorgenommen oder nicht vorgenommen wurde (2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2) */
	@Schema(description = "das Halbjahr der Qualifikationsphase, für welches die Markierung vorgenommen oder nicht vorgenommen wurde (2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2)",
			example = "2")
	public int idHalbjahr = -1;

	/** Gibt an, ob der Algorithmus die Belgung markiert hat oder nicht */
	@Schema(description = "gibt an, ob der Algorithmus die Belgung markiert hat oder nicht.", example = "true")
	public boolean markiert;

}
