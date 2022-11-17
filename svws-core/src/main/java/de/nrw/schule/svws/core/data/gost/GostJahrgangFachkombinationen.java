package de.nrw.schule.svws.core.data.gost;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Core-DTO für die Informationen zu einer an der Schule unzulässigen oder 
 * erforderlichen Fachkombination. Diese kann ggf. auch Kursart-spezifisch sein.   
 */
@XmlRootElement
@Schema(description="Die Informationen zu einer an der Schule unzulässigen oder erforderlichen Fachkombination. Diese kann ggf. auch Kursart-spezifisch sein.")
@TranspilerDTO
public class GostJahrgangFachkombinationen {
	
	/** Die ID der Fachkombination */
	@Schema(required = true, description = "die ID der Fachkombination", example="42")
	public long id;

	/** Das Jahr, in welchem der Jahrgang Abitur machen wird oder NULL, falls es sich um die Vorlage für neue Jahrgänge handelt */
	@Schema(required = true, description = "das Jahr, in welchem der Jahrgang Abitur machen wird oder NULL, falls es sich um die Vorlage für neue Jahrgänge handelt", example="2042")
	public Integer abiturjahr;

	/** Die ID des Faches (Fach 1), dessen Wahl die Kombination mit einem anderen Fach verlangt oder ausschließt */
	@Schema(required = true, description = "die ID des Faches, dessen Wahl die Kombination mit einem anderen Fach verlangt oder ausschließt", example="16")
	public long fachID1;

	/** Die Kursart der Fachwahl von Fach 1, falls die Fachkombination auf diese Kursart bei Fach 1 eingeschränkt ist */
	@Schema(required = true, description = "die Kursart der Fachwahl von Fach 1, falls die Fachkombination auf diese Kursart bei Fach 1 eingeschränkt ist", example="LK")
	public String kursart1;

	/** Die ID des Faches (Fach 2), welches in der Kombination verlangt oder ausgeschlossen wird */
	@Schema(required = true, description = "die ID des Faches, welches in der Kombination verlangt oder ausgeschlossen wird", example="17")
	public long fachID2;

	/** Die Kursart der Fachwahl von Fach 2, falls die Fachkombination auf diese Kursart bei Fach 2 eingeschränkt ist */
	@Schema(required = true, description = "die Kursart der Fachwahl von Fach 2, falls die Fachkombination auf diese Kursart bei Fach 2 eingeschränkt ist", example="LK")
	public String kursart2;

	/** Gibt an, ob für die jeweilige Halbjahre der Oberstufe die Fachkombination gilt (0 = EF.1, 1=EF.2, ...) */
	@ArraySchema(schema = @Schema(required = true, implementation = Boolean.class, description = "gibt an, ob für die jeweilige Halbjahre der Oberstufe die Fachkombination gilt (0 = EF.1, 1=EF.2, ...)"))
	public @NotNull boolean[] gueltigInHalbjahr = new boolean[6];

	/** Der Typ der Fachkombination (0: Wahl von Fach 2 ist in Kombination mit Fach 1 unzulässig, 1: Wahl von Fach 2 ist bei Wahl von Fach 1 nötig) */
	@Schema(required = true, description = "der Typ der Fachkombination (0: Wahl von Fach 2 ist in Kombination mit Fach 1 unzulässig, 1: Wahl von Fach 2 ist bei Wahl von Fach 1 nötig)", example="1")
	public int typ;	

	/** Der erläuternde Hinweistext zu der Fachkombination */
	@Schema(required = true, description = "der erläuternde Hinweistext zu der Fachkombination", example="Der Sport-Leistungskurs ist an dieser Schule nur in Verbindung mit einem Mathematik-Leistungskurs wählbar.")
	public @NotNull String hinweistext = "";

}
