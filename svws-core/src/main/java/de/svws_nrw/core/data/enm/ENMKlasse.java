package de.svws_nrw.core.data.enm;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu den Klasse der
 * Schüler für das Externe-Noten-Modul ENM.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Struktur von JSON-Daten zu den Klassen der Schüler für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMKlasse {

	/** Die ID der Klasse aus der SVWS-DB (z.B. 16) */
	@Schema(description = "Die ID der Klasse aus der SVWS-DB.", example = "12")
	public long id;

	/** Das Kürzel der Klasse, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. EF) */
	@Schema(description = "Das Kürzel der Klasse, wie es im Rahmen der amtlichen Schulstatistik verwendet wird.", example = "EFA")
	public String kuerzel;

	/** Das Kürzel ser Klasse, wie er im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. EF) */
	@Schema(description = "Das Kürzel der Klasse, wie er im Rahmen der Schule benannt wird und angezeigt werden soll.", example = "EF")
	public String kuerzelAnzeige;

	/** Die ID des Jahrgangs aus der SVWS-DB zu der die Klasse gehört (z.B. 11) oder null, falls es sich um eine jahrgangsübergreifende Klasse handelt */
	@Schema(description = "Die ID des Jahrgangs aus der SVWS-DB zu der die Klasse gehört oder null, falls es sich um eine jahrgangsübergreifende Klasse handelt.", example = "11")
	public Long idJahrgang;

	/** Die Reihenfolge der Klasse bei der Sortierung der Klasse. (z.B. 8) */
	@Schema(description = "Die Reihenfolge der Klasse bei der Sortierung der Klasse.", example = "20")
	public int sortierung;

	/** Die IDs der zugeordneten Klassenlehrer. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit den Informationen der IDs der zugeordneten Klassenlehrer."))
	public @NotNull List<@NotNull Long> klassenlehrer = new ArrayList<>();

}
