package de.svws_nrw.core.data.gost;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Eine Wiederholung ordnet einen Schüler dem folgenden Abiturjahrgang zu, in dem er zwei Halbjahre der gymnasialen Oberstufe
 * wiederholt. Sie hält fest, mit welchem Halbjahr er dort einsteigt. Vor diesem Halbjahr gehört er beiden Jahrgängen an.
 * Laufbahn- und Kursplanung brauchen diese Zuordnung, bevor der Lernabschnitt im neuen Jahrgang angelegt ist.
 */
@XmlRootElement
@Schema(description = "Die Wiederholung eines Schülers im folgenden Abiturjahrgang.")
@TranspilerDTO
public class GostSchuelerWiederholung {

	/** Die ID des Schülers */
	@Schema(description = "die ID des Schülers", example = "4711")
	public long idSchueler = -1;

	/** Das Abiturjahr des Jahrgangs, in dem der Schüler wiederholt, oder null, wenn keine Wiederholung besteht */
	@Schema(description = "das Abiturjahr des Jahrgangs, in dem der Schüler wiederholt, oder null, wenn keine Wiederholung besteht",
			example = "2029", accessMode = Schema.AccessMode.READ_ONLY)
	public Integer abiturjahr = null;

	/**
	 * Das Halbjahr der gymnasialen Oberstufe, mit dem der Schüler im Jahrgang der Wiederholung einsteigt
	 * (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2), oder null, wenn keine Wiederholung besteht
	 */
	@Schema(description = "das Halbjahr der gymnasialen Oberstufe, mit dem der Schüler im Jahrgang der Wiederholung einsteigt "
			+ "(0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2), oder null, wenn keine Wiederholung besteht",
			example = "2", accessMode = Schema.AccessMode.READ_ONLY)
	public Integer gostHalbjahr = null;

	/** Das Abiturjahr, das sich aus dem aktuellen Lernabschnitt des Schülers ergibt, oder null, wenn es nicht bestimmt werden kann */
	@Schema(description = "das Abiturjahr, das sich aus dem aktuellen Lernabschnitt des Schülers ergibt, oder null, wenn es nicht bestimmt werden kann",
			example = "2028", accessMode = Schema.AccessMode.READ_ONLY)
	public Integer abiturjahrLernabschnitt = null;

	/** Gibt an, ob die Schule das Einstiegshalbjahr erreicht hat und der Wechsel damit fällig ist */
	@Schema(description = "gibt an, ob die Schule das Einstiegshalbjahr erreicht hat und der Wechsel damit fällig ist",
			example = "false", accessMode = Schema.AccessMode.READ_ONLY)
	public boolean wechselFaellig = false;

	/** Gibt an, ob der aktuelle Lernabschnitt des Schülers zum Jahrgang der Wiederholung passt und der Wechsel damit vollzogen ist */
	@Schema(description = "gibt an, ob der aktuelle Lernabschnitt des Schülers zum Jahrgang der Wiederholung passt und der Wechsel damit vollzogen ist",
			example = "false", accessMode = Schema.AccessMode.READ_ONLY)
	public boolean wechselVollzogen = false;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostSchuelerWiederholung() {
		// leer
	}

}
